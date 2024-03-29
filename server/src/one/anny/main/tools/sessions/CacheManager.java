package one.anny.main.tools.sessions;

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

import one.anny.main.tools.Config;
import one.anny.main.tools.Logger;
import one.anny.main.tools.StdVar;
import one.anny.main.tools.exceptions.SessionException;

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

	/** The sessions to write in the cache file */
	private Queue<SessionModel> sessionsAddBuffer;

	/** The sessions to remove from the cache file */
	private Queue<String> sessionsRemoveBuffer;

	/** Condition for the buffer */
	private Condition bufferEmptyCondition;

	/** Lock for the cache file reading */
	private Lock cacheFileLock;

	/** Number of added sessions since the last cleaning */
	private long addedSessions;


	// ----- Constructors -----


	/**
	 * Create a new cache manager with the session pool related to it and create the cache file
	 * 
	 * @param sessionPool The session pool
	 */
	public CacheManager() {
		this.addedSessions = 0;
		
		this.sessionsAddBuffer = new ConcurrentLinkedQueue<SessionModel>();
		this.sessionsRemoveBuffer = new ConcurrentLinkedQueue<String>();

		this.cacheFileLock = new ReentrantLock(true);
		this.bufferEmptyCondition = this.cacheFileLock.newCondition();

		this.cacheFile = new File(Config.getBasePath() + StdVar.SESSION_CACHE_FILE);

		// Create the sessions file if it doesn't exists
		try {

			if(this.cacheFile.createNewFile()) {
				this.resetSessions();
				Logger.log("The session cache file were created and initilized", Logger.INFO);
			}

		} catch (IOException e) {

			Logger.log(e, Logger.ERROR);

		}
	}


	// ----- Setters -----


	public void addSessionsAddBuffer(SessionModel session) {
		this.cacheFileLock.lock();
		this.sessionsAddBuffer.add(session);
		this.bufferEmptyCondition.signalAll();
		this.cacheFileLock.unlock();
	}

	public void addSessionsRemoveBuffer(String sessionId) {
		this.cacheFileLock.lock();
		this.sessionsRemoveBuffer.add(sessionId);
		this.bufferEmptyCondition.signalAll();
		this.cacheFileLock.unlock();
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

			Logger.log(e, Logger.ERROR);

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
	public SessionModel getSession(String sessionId) {
		// Aquire the read lock
		this.cacheFileLock.lock();

		// Prepare the result
		SessionModel res = null;

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

					res = new SessionModel(sessionWantedJSON);
					if(this.sessionsRemoveBuffer.contains(res.getSessionId())) {
						res = null;
					}

				} catch (SessionException e) {

					Logger.log(e, Logger.WARNING);

				}
			} else {
				// Get the session in the add buffer
				for (SessionModel session : sessionsAddBuffer) {
					if(session.getSessionId() == sessionId) {
						res = session;
					}
				}
			}

		} catch (IOException e) {

			Logger.log("Error during session cache file reading", Logger.ERROR);
			Logger.log(e, Logger.ERROR);

		} catch (ParseException e) {

			Logger.log("Error during the session cache file parsing", Logger.ERROR);
			Logger.log(e, Logger.ERROR);

		} finally {

			this.cacheFileLock.unlock();

		}

		return res;
	}

	/**
	 * Clean the cache from expired sessions, this method only launched from a locked method
	 */
	private void cleanSessions() {
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
					SessionModel session = new SessionModel(sessionJSON);
					if(session.isExpired()) {
						this.sessionsRemoveBuffer.add(session.getSessionId());
					}

				} catch (SessionException e) {

					Logger.log(e, Logger.WARNING);

				}					
			}

		} catch (IOException e) {

			Logger.log("Error during session cache file reading", Logger.ERROR);
			Logger.log(e, Logger.ERROR);

		} catch (ParseException e) {

			Logger.log("Error during the session cache file parsing", Logger.ERROR);
			Logger.log(e, Logger.ERROR);

		} finally {
			
			this.addedSessions = 0;
			
		}
	}

	/**
	 * The run method of the cache manager, this is an infinite loop which wait for the buffers to be filled and write them
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void run() {

		while(true) {
			try {
				// Get the read lock to avoid get request during the writing period
				this.cacheFileLock.lock();


				// Wait for the buffer to be filled
				while(this.sessionsAddBuffer.size() == 0 && this.sessionsRemoveBuffer.size() == 0) {
					this.bufferEmptyCondition.await();
				}
				
				// Verify the added sessions
				if(this.addedSessions >= Config.getCacheCleaningInterval()) {
					Logger.log("Cleaning session cache", Logger.INFO);
					this.cleanSessions();
				}

				// Get the JSON object of the sessions
				Reader sessionReader = new FileReader(this.cacheFile);
				JSONParser parser = new JSONParser();
				JSONObject sessionsJSON = (JSONObject) parser.parse(sessionReader);
				sessionReader.close();
				
				// Remove a session in the JSON
				if(this.sessionsRemoveBuffer.size() > 0) {
					String sessionIdToRemove = this.sessionsRemoveBuffer.poll();
					sessionsJSON.remove(sessionIdToRemove);
				}

				// Add a session in the JSON
				if(this.sessionsAddBuffer.size() > 0) {
					SessionModel sessionToAdd = this.sessionsAddBuffer.poll();
					sessionsJSON.put(sessionToAdd.getSessionId(), sessionToAdd.getJSON());
					this.addedSessions++;
				}

				// Write the new session file
				FileWriter sessionWriter = new FileWriter(this.cacheFile);
				sessionWriter.write(sessionsJSON.toJSONString());
				sessionWriter.close();

			} catch (IOException e) {

				Logger.log("Error during session cache file writing", Logger.ERROR);
				Logger.log(e, Logger.ERROR);

			} catch (ParseException e) {

				Logger.log("Error during the session cache file parsing", Logger.ERROR);
				Logger.log(e, Logger.ERROR);

			} catch (InterruptedException e) {

				Logger.log("Cache writer has been stopped", Logger.ERROR);
				Logger.log(e, Logger.ERROR);

			} finally {
				
				this.cacheFileLock.unlock();
				
			}
		}
	}

}
