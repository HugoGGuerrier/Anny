package tools.sessions;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import tools.Config;
import tools.Logger;
import tools.StdVar;
import tools.exceptions.SessionException;

/**
 * This class manage the session cache reading and writing
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class CacheManager implements Runnable {

	// ----- Attributes -----


	/** The file to write the cache in */
	private File cacheFile;

	/** The application logger */
	private Logger logger;

	/** The sessions to write in the cache file */
	private Queue<Session> sessionsAddBuffer;

	/** The sessions to remove from the cache file */
	private Queue<String> sessionsRemoveBuffer;

	/** Condition for the buffer */
	private Condition bufferEmptyCondition;

	/** Lock for the cache file reading */
	private Lock cacheFileLock;

	/** Number of added sessions since the last cleaning */
	private long addedSessions;

	/** If the thread has to run */
	private boolean running;


	// ----- Constructors -----


	/**
	 * Create a new cache manager with the session pool related to it and create the cache file
	 * 
	 * @param sessionPool The session pool
	 */
	public CacheManager() {
		this.addedSessions = 0;
		this.running = false;

		this.logger = Logger.getInstance();
		this.sessionsAddBuffer = new ConcurrentLinkedQueue<Session>();
		this.sessionsRemoveBuffer = new ConcurrentLinkedQueue<String>();

		this.cacheFileLock = new ReentrantLock(true);
		this.bufferEmptyCondition = this.cacheFileLock.newCondition();

		this.cacheFile = new File(Config.getBasePath() + StdVar.SESSION_CACHE_FILE);

		// Create the sessions file if it doesn't exists
		try {

			if(this.cacheFile.createNewFile()) {
				this.resetSessions();
				this.logger.log("The session cache file were created and initilized", Logger.INFO);
			}

		} catch (IOException e) {

			this.logger.log(e, Logger.ERROR);

		}
	}


	// ----- Setters -----


	public void addSessionsAddBuffer(Session session) {
		this.sessionsAddBuffer.add(session);
		this.cacheFileLock.lock();
		this.bufferEmptyCondition.signalAll();
		this.cacheFileLock.unlock();
	}

	public void addSessionsRemoveBuffer(String sessionId) {
		this.sessionsRemoveBuffer.add(sessionId);
		this.cacheFileLock.lock();
		this.bufferEmptyCondition.signalAll();
		this.cacheFileLock.unlock();
	}

	public void start() {
		this.running = true;
	}

	public void stop() {
		this.running = false;
	}


	// ----- Class methods -----


	/**
	 * Reset the sessions file to an empty file
	 */
	public void resetSessions() {
		// Lock the file writing adn reading
		this.cacheFileLock.lock();

		try {

			FileWriter writer = new FileWriter(this.cacheFile);
			writer.write("{}");
			writer.close();

		} catch (IOException e) {

			this.logger.log(e, Logger.ERROR);

		} finally {

			// Unlock all
			this.cacheFileLock.unlock();

		}

	}

	/**
	 * Get a session with its session ID
	 * 
	 * @param sessionId The session id you want to get
	 * @return The wanted session or null if it doesn't exists
	 */
	public Session getSession(String sessionId) {
		// Aquire the read lock
		this.cacheFileLock.lock();

		// Prepare the result
		Session res = null;

		try {

			// Get the sessions JSON
			Reader sessionReader = new FileReader(this.cacheFile);
			JSONParser parser = new JSONParser();
			JSONObject sessionsJSON = (JSONObject) parser.parse(sessionReader);
			sessionReader.close();

			// Get the wanted session
			JSONObject sessionWantedJSON = (JSONObject) sessionsJSON.get(sessionId);

			if(sessionWantedJSON != null) {
				// Get the session in the cache
				try {

					res = new Session(sessionWantedJSON);
					if(this.sessionsRemoveBuffer.contains(res.getSessionId())) {
						res = null;
					}

				} catch (SessionException e) {

					this.logger.log(e, Logger.WARNING);

				}
			} else {
				// Get the session in the add buffer
				for (Session session : sessionsAddBuffer) {
					if(session.getSessionId() == sessionId) {
						res = session;
					}
				}
			}

		} catch (IOException e) {

			this.logger.log("Error during session cache file reading", Logger.ERROR);
			this.logger.log(e, Logger.ERROR);

		} catch (ParseException e) {

			this.logger.log("Error during the session cache file parsing", Logger.ERROR);
			this.logger.log(e, Logger.ERROR);

		} finally {

			this.cacheFileLock.unlock();

		}

		return res;
	}

	/**
	 * Clean the cache from expired sessions
	 */
	private void clean() {
		try {
			// Get the sessions JSON
			Reader sessionReader = new FileReader(this.cacheFile);
			JSONParser parser = new JSONParser();
			JSONObject sessionsJSON = (JSONObject) parser.parse(sessionReader);
			sessionReader.close();

			// Iterate over sessions
			for (Object key : sessionsJSON.keySet()) {
				JSONObject sessionJSON = (JSONObject) sessionsJSON.get(key);

				try {

					// If the session is expired add it to the remove buffer
					Session session = new Session(sessionJSON);
					if(session.isExpired()) {
						this.sessionsRemoveBuffer.add(session.getSessionId());
					}

				} catch (SessionException e) {

					this.logger.log(e, Logger.WARNING);

				}					
			}

		} catch (IOException e) {

			this.logger.log("Error during session cache file reading", Logger.ERROR);
			this.logger.log(e, Logger.ERROR);

		} catch (ParseException e) {

			this.logger.log("Error during the session cache file parsing", Logger.ERROR);
			this.logger.log(e, Logger.ERROR);

		}
	}

	/**
	 * The run method of the cache manager, this is an infinite loop which wait for the buffers to be filled and write them
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void run() {

		while(this.running) {
			try {
				// Get the read lock to avoid get request during the writing period
				this.cacheFileLock.lock();
				
				// Verify the added sessions
				if(this.addedSessions > Config.getSessionTimeToLive()) {
					this.clean();
				}

				// Wait for the buffer to be filled
				while(this.sessionsAddBuffer.size() == 0 && this.sessionsRemoveBuffer.size() == 0) {
					this.bufferEmptyCondition.await();
				}

				// Get the JSON object of the sessions
				Reader sessionReader = new FileReader(this.cacheFile);
				JSONParser parser = new JSONParser();
				JSONObject sessionsJSON = (JSONObject) parser.parse(sessionReader);
				sessionReader.close();

				// Add a session in the JSON
				if(this.sessionsAddBuffer.size() > 0) {
					Session sessionToAdd = this.sessionsAddBuffer.poll();
					sessionsJSON.put(sessionToAdd.getSessionId(), sessionToAdd.getJSON());
					this.addedSessions++;
				}

				// Remove a session in the JSON
				if(this.sessionsRemoveBuffer.size() > 0) {
					String sessionIdToRemove = this.sessionsRemoveBuffer.poll();
					sessionsJSON.remove(sessionIdToRemove);
				}

				// Write the new session file
				FileWriter sessionWriter = new FileWriter(this.cacheFile);
				sessionWriter.write(sessionsJSON.toJSONString());
				sessionWriter.close();

			} catch (IOException e) {

				this.logger.log("Error during session cache file writing", Logger.ERROR);
				this.logger.log(e, Logger.ERROR);

			} catch (ParseException e) {

				this.logger.log("Error during the session cache file parsing", Logger.ERROR);
				this.logger.log(e, Logger.ERROR);

			} catch (InterruptedException e) {

				this.logger.log("Cache writer has been stopped", Logger.ERROR);
				this.logger.log(e, Logger.ERROR);

			} finally {
				
				this.cacheFileLock.unlock();
				
			}
		}
	}

}
