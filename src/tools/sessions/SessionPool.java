package tools.sessions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import tools.exceptions.SessionException;
import tools.models.UserModel;

/**
 * A class to store all sessions in the cache and manage them
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class SessionPool {
	
	// ----- Attributes -----
	
	
	/** Cache writter instance */
	private CacheManager manager;
	
	/** Thread of the cache writter */
	private Thread managerThread;
	
	/** Lock to exclude multiple user to write and read cache at the same time */
	private Lock cacheLock;
	
	/** Condition to wait for the writter to stop running */
	private Condition getRequestCondition;
	
	/** The number of current sessions */
	private long currentSessions;
	
	/** If there is a get request formulated */
	private boolean getRequest;
	
	/** Instance of the session pool (singleton) */
	private static SessionPool instance = null;
	
	
	// ----- Constructors -----
	
	
	/**
	 * Construct a new SessionPool with the wanted max sessions
	 * 
	 * @param maxSessions
	 */
	private SessionPool() {
		this.currentSessions = 0;
		
		this.manager = new CacheManager(this);
		this.managerThread =  new Thread(this.manager);
		
		this.cacheLock = new ReentrantLock(true);
		this.getRequestCondition = this.cacheLock.newCondition();
	}
	
	/**
	 * Get the unique instance of SessionPool
	 * 
	 * @return The SessionPool
	 */
	public static SessionPool getInstance() {
		if(SessionPool.instance == null) {
			SessionPool.instance = new SessionPool();
		}
		return SessionPool.instance;
	}
	
	
	// ----- Getters -----
	
	
	public long getCurrentSessions() {
		return this.currentSessions;
	}
	
	public Lock getCacheLock() {
		return this.cacheLock;
	}
	
	public Condition getCacheRunningCondition() {
		return this.getRequestCondition;
	}
	
	
	// ----- Class methods -----
	
	
	/**
	 * Get a stored session and verify if it's not expired and execute the action if you want
	 * 
	 * @param sessionId The session ID you want to get
	 * @param autoAction If you want to execute the action automatically
	 * @return The session if it exists, null else
	 */
	public Session getSession(String sessionId, boolean doAction) {
		// TODO : Lire le fichier de cache et la tampon pour récupérer une session
		
		return null;
	}
	
	/**
	 * Add a session to the session pool from its ID and an user if there is enough place
	 * 
	 * @param sessionId The session ID
	 * @param user The user liked to the session
	 */
	public void addSession(String sessionId, UserModel user) throws SessionException {
		// TODO : Ajouter la session dans le tampon de session et lancer le daemon d'écriture
	}
	
	/**
	 * Remove the session from the session pool
	 * 
	 * @param sessionId The session ID
	 */
	public void removeSession(String sessionId) {
		// TODO : Ajouter la session dans le tempon de suppression et lancer le daemon si il n'est pas lancé
	}
	
	/**
	 * Remove all the expired sessions
	 */
	public void cleanSessions() {
		// TODO : Lancer le daemon en mode de suppression
	}
	
	/**
	 * Generate and return a random session ID which is a String of 32 character
	 * 
	 * @return The session id
	 */
	public String generateSessionId() {
		// Define needed variables
		String possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		Random random = new Random();
		StringBuilder res = new StringBuilder();
		
		// Generate the session ID
		Session testSession = null;
		
		do {
			for (int i = 0; i < 32; i++) {
				int selectedChar = random.nextInt(possibleChars.length());
				res.append(possibleChars.charAt(selectedChar));
			}
			testSession = this.getSession(res.toString(), false);
		} while (testSession != null);
		
		
		// Return the generated ID
		return res.toString();
	}
	
	
	// ----- Nested class CacheWritter -----
	
	
	private class CacheManager implements Runnable {
		
		// ----- Attributes -----
		
		
		/** The add mode */
		private static final int ADD_MODE = 0;
		
		/** The remove mode */
		private static final int REMOVE_MODE = 1;
		
		/** The cleaning mode */
		private static final int CLEAN_MODE = 2;
		
		/** The session pool to look at */
		private SessionPool sessionPool;
		
		/** The file to write the cache in */
		private File cacheFile;
		
		/** The sessions to write in the cache file */
		private List<Session> sessionAddBuffer;
		
		/** The sessions to remove from the cache file */
		private List<Session> sessionRemoveBuffer;
		
		/** Cache writter mode */
		private int mode;
		
		/** If the writter is running */
		private boolean running;
		
		
		// ----- Constructors -----
		
		
		private CacheManager(SessionPool sessionPool) {
			this.sessionPool = sessionPool;
			this.sessionAddBuffer = new ArrayList<Session>();
			this.sessionRemoveBuffer = new ArrayList<Session>();
		}
		
		
		// ----- Getters -----
		
		
		private int getMode() {
			return this.mode;
		}
		
		private boolean isRunning() {
			return this.running;
		}
		
		
		// ----- Setters -----
		
		
		private void setMode(int mode) {
			this.mode = mode;
		}
		
		
		// ----- Class methods -----
		
		
		@Override
		public void run() {
			this.running = true;
			
			// TODO : Faire les action requises par le pool de sessions
			
			this.running = false;
			this.sessionPool.getCacheRunningCondition().notifyAll();
		}
		
	}

}
