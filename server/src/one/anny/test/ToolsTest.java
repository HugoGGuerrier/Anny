package one.anny.test;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;

import one.anny.main.tools.Config;
import one.anny.main.tools.Security;
import one.anny.main.tools.StdVar;
import one.anny.main.tools.models.UserModel;
import one.anny.main.tools.sessions.SessionModel;
import one.anny.main.tools.sessions.SessionPool;

public class ToolsTest {

	// ----- Attributes -----


	private static final String htmlTestString = "<script>alert('test')</script>";

	// --- Sessions
	private SessionPool sessionPool = SessionPool.getInstance();

	private static final String sessionId1 = "a";
	private static final String sessionId2 = "b";
	private static final String sessionId3 = "c";

	private static final UserModel user1 = new UserModel();
	private static final UserModel user2 = new UserModel();
	private static final UserModel user3 = new UserModel();


	// ----- Configuration methods -----


	@BeforeClass
	public static void setup() {
		// Load the test configuration files
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
		assertEquals("test.version.1", Config.getVersion());
		assertEquals(0, Config.getEnv());
		assertEquals(5, Config.getCacheCleaningInterval());
		assertEquals(3, Config.getSessionTimeToLive());
		assertTrue(Config.isVerbose());

		assertFalse(Config.isMysqlPooling());
		assertEquals("localhost", Config.getMysqlHost());
		assertEquals("DB_ANNY_TEST", Config.getMysqlDatabase());

		assertEquals("DB_ANNY_TEST", Config.getMongoDatabase());
		assertEquals("messages", Config.getMongoMessageCollection());
	}

	@Test
	public void testSecurity() {
		// Test the HTML encode/decode
		assertEquals("&lt;script&gt;alert('test')&lt;/script&gt;", Security.htmlEncode(ToolsTest.htmlTestString));
		assertEquals(ToolsTest.htmlTestString, Security.htmlDecode("&lt;script&gt;alert('test')&lt;/script&gt;"));
		assertEquals("ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff", Security.hashString("test"));

		assertTrue(Security.isValidDate("2020-04-12"));
		assertTrue(Security.isValidDate("2001-05-31"));
		assertFalse(Security.isValidDate("2020-13-20"));
		assertFalse(Security.isValidDate("1956-02-10"));
		assertFalse(Security.isValidDate("2019-05-32"));

		assertTrue(Security.isValidUserId("@test"));
		assertFalse(Security.isValidUserId("@"));
		assertFalse(Security.isValidUserId("test"));

		assertTrue(Security.isValidEmail("test@gmail.com"));
		assertTrue(Security.isValidEmail("blablabbla@mail.fr"));
		assertFalse(Security.isValidEmail("thisisatest.gmail.com"));

		assertTrue(Security.isValidPassword(Security.hashString("this is a test password")));
		assertFalse(Security.isValidPassword("12345abcef"));

		assertTrue(Security.isValidBoardName("testboard"));
		assertFalse(Security.isValidBoardName("test_ttt"));

		assertTrue(Security.isValidMessageId("1.1.2"));
		assertTrue(Security.isValidMessageId("1"));
		assertFalse(Security.isValidMessageId("1."));
	}

	@Test
	public void testSessions() {
		// Test the session getting
		SessionModel testSession1 = this.sessionPool.getSession(ToolsTest.sessionId1, false);
		assertEquals(ToolsTest.user1.getUserId(), testSession1.getUserId());

		// Test the session deleting
		this.sessionPool.removeSession(ToolsTest.sessionId1);
		assertNull(this.sessionPool.getSession(ToolsTest.sessionId1, false));

		// Test the session updating
		try {
			SessionModel test1 = this.sessionPool.getSession(ToolsTest.sessionId2, false);
			Thread.sleep(2500);
			this.sessionPool.getSession(ToolsTest.sessionId2, true);
			SessionModel test2 = this.sessionPool.getSession(ToolsTest.sessionId2, false);
			assertNotEquals(test1.getLastActionDate(), test2.getLastActionDate());
		} catch (InterruptedException e) {
			fail("Please do not stop the test");
		}

		// Test the session clean
		try {
			Thread.sleep(1500);

			UserModel testUser = new UserModel();
			testUser.setUserId("4");
			SessionModel newSession = new SessionModel("d", testUser);

			this.sessionPool.putSession(newSession);

			assertNull(this.sessionPool.getSession(ToolsTest.sessionId3, false));
		} catch (InterruptedException e) {
			fail("Please do not stop the test");
		}
	}

}
