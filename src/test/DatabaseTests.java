package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import db.Database;
import db.Migrator;
import db.managers.BoardDatabaseManager;
import db.managers.FollowDatabaseManager;
import db.managers.UserDatabaseManager;
import tools.Config;
import tools.StdVar;
import tools.models.BoardModel;
import tools.models.FollowModel;
import tools.models.UserModel;

public class DatabaseTests {

	// ----- Attributes -----


	private static Connection mysqlConnection;


	// ----- Configuration methods -----


	@BeforeClass
	public static void setup() {		
		// Load the test configuration files
		Path configTestPath = Paths.get(StdVar.TEST_CONFIG_FILE);

		try {
			Reader reader = new FileReader(configTestPath.toAbsolutePath().toFile());
			Config.setBasePath(Paths.get("").toAbsolutePath().toString() + "/WebContent/");
			Config.init(reader);

			// Upgrade the database to the wanted version
			Migrator migrator = Migrator.getInstance();
			migrator.upgrade(Config.getDatabaseVersion());

			// Get the database connections
			DatabaseTests.mysqlConnection = Database.getMySQLConnection();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Cannot load the test config file !");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot open the mysql connection");
		}

	}

	@AfterClass
	public static void teardown() {
		// Close the connections
		try {
			DatabaseTests.mysqlConnection.close();

			Migrator migrator = Migrator.getInstance();
			// migrator.downgrade(0);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot close the mysql connection !");
		}
	}


	// ----- Test methods -----


	@Test
	public void testMysqlUser() {
		UserDatabaseManager userDatabaseManager = UserDatabaseManager.getInstance();

		// Test insertion
		UserModel exampleUser = new UserModel();
		exampleUser.setUserId("@tester_1");
		exampleUser.setUserPseudo("Test_pseudo");
		exampleUser.setUserName("TestName");
		exampleUser.setUserSurname("TestSurname");
		exampleUser.setUserEmail("test@test.mail");
		exampleUser.setUserPassword("0e3e75234abc68f4378a86b3f4b32a198ba301845b0cd6e50106e874345700cc6663a86c1ea125dc5e92be17c98f9a0f85ca9d5f595db2012f7cc3571945c123");
		exampleUser.setUserAdmin(true);
		try {
			userDatabaseManager.insertUser(exampleUser);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot insert a new user");
		}

		// Test updater
		exampleUser.setUserName("TestName2");
		try {
			userDatabaseManager.updateUser(exampleUser);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot update user");
		}

		// Test getter
		UserModel userFilter = new UserModel();
		userFilter.setUserId("@tester_1");
		try {
			List<UserModel> users = userDatabaseManager.getUsers(userFilter, false);
			assertEquals(1, users.size());
			assertEquals("Test_pseudo", users.get(0).getUserPseudo());
			assertEquals("TestName2", users.get(0).getUserName());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot get users");
		}

		// Test deleter
		try {
			//userDatabaseManager.deleteUser(exampleUser);
			List<UserModel> noUser = userDatabaseManager.getUsers(userFilter, false);
			assertEquals(0, noUser.size());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot delete user");
		}
	}

	@Test
	public void testMysqlFollow() {
		FollowDatabaseManager followDatabaseManager = FollowDatabaseManager.getInstance();
	}

	@Test
	public void testMysqlBoard() {
		BoardDatabaseManager boardDatabaseManager = BoardDatabaseManager.getInstance();

		// Test insertion
		BoardModel newBoard = new BoardModel();
		newBoard.setBoardName("test_board");
		newBoard.setBoardCreatorId("@tester_1");
		newBoard.setBoardDescription("This is the test board of the test user");
		newBoard.addMessageId(1l);
		newBoard.addMessageId(3l);
		try {
			boardDatabaseManager.insertBoard(newBoard);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot insert a new board !");
		}

		// Test the board get
		BoardModel filter = new BoardModel();
		filter.setBoardName("test_board");
		try {
			System.out.println(boardDatabaseManager.getBoards(filter, false).size());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot get boards !");
		}

	}

}
