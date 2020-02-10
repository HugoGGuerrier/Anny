package test;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;

import tools.Config;
import tools.Handler;
import tools.Session;
import tools.SessionPool;
import tools.exceptions.SessionException;
import tools.models.User;

public class ToolsTest {
	
	// ----- Attributes -----
	
	// --- Handler
	private Handler handler = Handler.getInstance();
	
	// --- Sessions
	private SessionPool sessionPool = SessionPool.getInstance();
	
	private static final String sessionId1 = "a";
	private static final String sessionId2 = "b";
	private static final String sessionId3 = "c";
	
	private static final User user1 = new User("1");
	private static final User user2 = new User("2");
	private static final User user3 = new User("3");
	
	
	
	// ----- Config methods -----
	
	
	@BeforeClass
	public static void setup() {
		// Load the test config files
		Path configTestPath = Paths.get("./src/test/config_test.json");
		try {
			Reader reader = new FileReader(configTestPath.toAbsolutePath().toFile());
			Config.init(reader);
		} catch (IOException e) {
			fail("Can't load the test config file !");
		}
		
		// Create fake session to test them
		SessionPool sessionPool = SessionPool.getInstance();
		try {
			sessionPool.addSession(ToolsTest.sessionId1, ToolsTest.user1);
			sessionPool.addSession(ToolsTest.sessionId2, ToolsTest.user2);
			sessionPool.addSession(ToolsTest.sessionId3, ToolsTest.user3);
		} catch (SessionException e) {
			fail("Can't add sessions at the start of the tests !");
		}
	}
	
	
	// ----- Test methods -----

	
	/**
	 * Test the configuration loading
	 */
	@Test
	public void testConfig() {
		assertEquals("test_version", Config.getVersion());
		assertEquals(0, Config.getEnv());
		assertEquals(42, Config.getMaxSessions());
		assertEquals(2400, Config.getSessionTimeToLive());
	}
	
	/**
	 * Test the handler
	 */
	@Test
	public void testHandler() {
		fail("Not implemented yet !");
	}
	
	/**
	 * Test the session pool
	 */
	@Test
	public void testSessionPool() {
		// Init the session pool
		this.sessionPool.init();
		
		// Test the session pool simple methods
		assertEquals(42, sessionPool.getMaxSessions());
		assertEquals(32, this.sessionPool.generateSessionId().length());
		assertEquals(ToolsTest.user1, this.sessionPool.getSession(sessionId1).getUser());
		
		// Test the session pool session remove
		assertEquals(3, this.sessionPool.getCurrentSessions());
		this.sessionPool.removeSession(ToolsTest.sessionId2);
		assertEquals(2, this.sessionPool.getCurrentSessions());
		
		// Assert that session pool add the previously deleted session
		try {
			this.sessionPool.addSession(ToolsTest.sessionId2, ToolsTest.user2);
		} catch (SessionException e) {
			fail("SessionPool can't insert a previously deleted session !");
		}
		
		// Assert that session pool don't add an existing session
		try {
			this.sessionPool.addSession(ToolsTest.sessionId1, ToolsTest.user1);
			fail("SessionPool inserted an already existing session !");
		} catch (SessionException e) {
			// Success !
		}
	}
	
	/**
	 * Test the sessions
	 */
	@Test
	public void testSession() {
		// Get the session
		Session testSession = this.sessionPool.getSession(ToolsTest.sessionId1);
		
		// Test the session function
		assertEquals(ToolsTest.sessionId1, testSession.getSessionId());
		assertEquals(2400, testSession.getTimeToLive());
		assertEquals(false, testSession.isExpired());
	}

}
