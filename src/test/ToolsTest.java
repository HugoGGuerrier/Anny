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
import tools.Security;
import tools.StdVar;
import tools.exceptions.SessionException;
import tools.models.UserModel;
import tools.sessions.Session;
import tools.sessions.SessionPool;

public class ToolsTest {

	// ----- Attributes -----


	// --- Security
	private Security security = Security.getInstance();

	private static final String htmlTestString = "<script>alert('test')</script>";

	// --- Sessions
	private SessionPool sessionPool = SessionPool.getInstance();

	private static final String sessionId1 = "a";
	private static final String sessionId2 = "b";
	private static final String sessionId3 = "c";

	private static final UserModel user1 = new UserModel();
	private static final UserModel user2 = new UserModel();
	private static final UserModel user3 = new UserModel();


	// ----- Config methods -----


	@BeforeClass
	public static void setup() {
		// Load the test config files
		Path configTestPath = Paths.get(StdVar.TEST_CONFIG_FILE);
		try {
			Reader reader = new FileReader(configTestPath.toAbsolutePath().toFile());
			Config.setBasePath(Paths.get("").toAbsolutePath().toString() + "/WebContent/");
			Config.init(reader);
		} catch (IOException e) {
			fail("Cannot load the test config file !");
		}
		
		// Setup the users
		ToolsTest.user1.setUserId("1");
		ToolsTest.user2.setUserId("2");
		ToolsTest.user3.setUserId("3");

		// Create fake session to test them
		SessionPool sessionPool = SessionPool.getInstance();
		sessionPool.reset();
		sessionPool.addSession(ToolsTest.sessionId1, ToolsTest.user1);
		sessionPool.addSession(ToolsTest.sessionId2, ToolsTest.user2);
		sessionPool.addSession(ToolsTest.sessionId3, ToolsTest.user3);
	}


	// ----- Test methods -----


	@Test
	public void testConfig() {
		assertEquals("0.1.1", Config.getVersion());
		assertEquals(0, Config.getEnv());
		assertEquals(42, Config.getCacheCleaningInterval());
		assertEquals(3, Config.getSessionTimeToLive());
		
		assertFalse(Config.isMysqlPooling());
		assertEquals("localhost", Config.getMysqlHost());
		assertEquals("DB_BIRDY_TEST", Config.getMysqlDatabase());
		
		assertEquals("DB_BIRDY_TEST", Config.getMongoDatabase());
		assertEquals("messages", Config.getMongoMessageCollection());
	}

	@Test
	public void testSecurity() {
		// Test the html endcode/decode
		assertEquals("&lt;script&gt;alert('test')&lt;/script&gt;", this.security.htmlEncode(ToolsTest.htmlTestString));
		assertEquals(ToolsTest.htmlTestString, this.security.htmlDecode("&lt;script&gt;alert('test')&lt;/script&gt;"));
		assertEquals("ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff", this.security.hashString("test"));
		
		assertTrue(this.security.isValidPassword(this.security.hashString("this is a test password")));
		assertFalse(this.security.isValidPassword("12345abcef"));
		
		assertTrue(this.security.isValidUserId("@test"));
		assertFalse(this.security.isValidUserId("@"));
		assertFalse(this.security.isValidUserId("test"));
	}

	@Test
	public void testSessionPool() {
		// Test the session pool simple methods
		assertEquals(32, this.sessionPool.generateSessionId().length());
		try {
			assertEquals(ToolsTest.user1.getUserId(), this.sessionPool.getSession(sessionId1, true).getUserId());
		} catch (SessionException e) {
			fail("Cannot get the session 1 !");
		}

		// Test the session pool session remove
		sessionPool.removeSession(ToolsTest.sessionId2);
		try {
			this.sessionPool.getSession(ToolsTest.sessionId2, false);
			fail("Session 2 has not been removed !");
		} catch (SessionException e) {
			// Success !
		}

		// Test the session update
		try {
			Session session1 = this.sessionPool.getSession(ToolsTest.sessionId1, true);
			session1.putAttribute("testAttribute", "This is a test");
			sessionPool.updateSession(session1);
			assertEquals("This is a test", sessionPool.getSession(ToolsTest.sessionId1, true).getAttribute("testAttribute"));
		} catch (SessionException e) {
			e.printStackTrace();
			fail("Cannot get the session 1 !");
		}

		// Test the session cleaner
	}

	@Test
	public void testSession() {
		// Get the session
		try {
			Session testSession = this.sessionPool.getSession(ToolsTest.sessionId1, true);

			// Test the session function
			assertEquals(ToolsTest.sessionId1, testSession.getSessionId());
			assertEquals(3, testSession.getTimeToLive());
			assertEquals(false, testSession.isExpired());

			// Test the session expiration
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				fail("Please don't stop the test !");
			}
			assertEquals(true, testSession.isExpired());
		} catch (SessionException e) {
			e.printStackTrace();
			fail("Cannot get the session 1 !");
		}
	}

}
