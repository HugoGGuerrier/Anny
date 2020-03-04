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
import tools.sessions.SessionModel;
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
		sessionPool.putSession(new SessionModel(ToolsTest.sessionId1, ToolsTest.user1));
		sessionPool.putSession(new SessionModel(ToolsTest.sessionId2, ToolsTest.user2));
		sessionPool.putSession(new SessionModel(ToolsTest.sessionId3, ToolsTest.user3));
	}


	// ----- Test methods -----


	@Test
	public void testConfig() {
		assertEquals("0.1.1", Config.getVersion());
		assertEquals(0, Config.getEnv());
		assertEquals(5, Config.getCacheCleaningInterval());
		assertEquals(3, Config.getSessionTimeToLive());
		
		assertFalse(Config.isMysqlPooling());
		assertEquals("localhost", Config.getMysqlHost());
		assertEquals("DB_BIRDY_TEST", Config.getMysqlDatabase());
		
		assertEquals("DB_BIRDY_TEST", Config.getMongoDatabase());
		assertEquals("messages", Config.getMongoMessageCollection());
	}

	@Test
	public void testSecurity() {
		// Test the HTML encode/decode
		assertEquals("&lt;script&gt;alert('test')&lt;/script&gt;", this.security.htmlEncode(ToolsTest.htmlTestString));
		assertEquals(ToolsTest.htmlTestString, this.security.htmlDecode("&lt;script&gt;alert('test')&lt;/script&gt;"));
		assertEquals("ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff", this.security.hashString("test"));
		
		assertTrue(this.security.isValidDate("2020-04-12"));
		assertTrue(this.security.isValidDate("2001-05-31"));
		assertFalse(this.security.isValidDate("2020-13-20"));
		assertFalse(this.security.isValidDate("1956-02-10"));
		assertFalse(this.security.isValidDate("2019-05-32"));
		
		assertTrue(this.security.isValidUserId("@test"));
		assertFalse(this.security.isValidUserId("@"));
		assertFalse(this.security.isValidUserId("test"));
		
		assertTrue(this.security.isValidEmail("test@gmail.com"));
		assertTrue(this.security.isValidEmail("blablabbla@mail.fr"));
		assertFalse(this.security.isValidEmail("thisisatest.gmail.com"));
		
		assertTrue(this.security.isValidPassword(this.security.hashString("this is a test password")));
		assertFalse(this.security.isValidPassword("12345abcef"));
		
		assertTrue(this.security.isValidBoardName("testboard"));
		assertFalse(this.security.isValidBoardName("test_ttt"));
		
		assertTrue(this.security.isValidMessageId("1.1.2"));
		assertTrue(this.security.isValidMessageId("1"));
		assertFalse(this.security.isValidMessageId("1."));
	}

	@Test
	public void testSessions() {
		// Test the session getting
		try {
			SessionModel testSession1 = this.sessionPool.getSession(ToolsTest.sessionId1, false);
			assertEquals(ToolsTest.user1.getUserId(), testSession1.getUserId());
		} catch (SessionException e) {
			e.printStackTrace();
			fail("Cannot get the session 1 !");
		}
		
		// Test the session deleting
		try {
			this.sessionPool.removeSession(ToolsTest.sessionId1);
			this.sessionPool.getSession(ToolsTest.sessionId1, false);
			fail("Session 1 has not been removed !");
		} catch (SessionException e) {
			// Success !
		}
		
		// Test the session updating
		try {
			SessionModel test1 = this.sessionPool.getSession(ToolsTest.sessionId2, false);
			Thread.sleep(2500);
			this.sessionPool.getSession(ToolsTest.sessionId2, true);
			SessionModel test2 = this.sessionPool.getSession(ToolsTest.sessionId2, false);
			assertNotEquals(test1.getLastActionDate(), test2.getLastActionDate());
		} catch (InterruptedException e) {
			fail("Please do not stop the test");
		} catch (SessionException e) {
			e.printStackTrace();
			fail("Cannot get the session 2 !");
		}
		
		// Test the session clean
		try {
			Thread.sleep(1500);
			
			UserModel testUser = new UserModel();
			testUser.setUserId("4");
			SessionModel newSession = new SessionModel("d", testUser);
			
			this.sessionPool.putSession(newSession);
			
			this.sessionPool.getSession(ToolsTest.sessionId3, false);
			fail("The session 3 were not removed by the clean !");
		} catch (InterruptedException e) {
			fail("Please do not stop the test");
		} catch (SessionException e) {
			// Success !
		}
	}

}
