package one.anny.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.bson.Document;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.client.MongoCollection;

import one.anny.main.db.Database;
import one.anny.main.db.Migrator;
import one.anny.main.db.filters.BoardFilter;
import one.anny.main.db.filters.FollowFilter;
import one.anny.main.db.filters.MessageFilter;
import one.anny.main.db.filters.UserFilter;
import one.anny.main.db.managers.BoardDatabaseManager;
import one.anny.main.db.managers.FollowDatabaseManager;
import one.anny.main.db.managers.MessageDatabaseManager;
import one.anny.main.db.managers.UserDatabaseManager;
import one.anny.main.tools.Config;
import one.anny.main.tools.Logger;
import one.anny.main.tools.StdVar;
import one.anny.main.tools.exceptions.MongoException;
import one.anny.main.tools.models.BoardModel;
import one.anny.main.tools.models.FollowModel;
import one.anny.main.tools.models.MessageModel;
import one.anny.main.tools.models.UserModel;

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
			Logger.init();

			// Upgrade the database to the wanted version
			Migrator migrator = Migrator.getInstance();
			migrator.migrate(Config.getDatabaseVersion());

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
			// Remove all the test messages
			MongoCollection<Document> messagesCollection = Database.getMongoDBConnection().getCollection(Config.getMongoMessageCollection());
			messagesCollection.deleteMany(new Document());
			
			DatabaseTests.mysqlConnection.close();

			Migrator migrator = Migrator.getInstance();
			migrator.migrate(0);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot close the mysql connection !");
		}
	}


	// ----- Test methods -----


	@Test
	public void testUser() {
		// Test insertion
		UserModel exampleUser = new UserModel();
		exampleUser.setUserId("@tester_1");
		exampleUser.setUserPseudo("Test_pseudo");
		exampleUser.setUserEmail("test1@test.mail");
		exampleUser.setUserPassword("0e3e75234abc68f4378a86b3f4b32a198ba301845b0cd6e50106e874345700cc6663a86c1ea125dc5e92be17c98f9a0f85ca9d5f595db2012f7cc3571945c123");
		exampleUser.setUserDate(new Date(new java.util.Date().getTime()));
		exampleUser.setUserAdmin(true);
		try {
			UserDatabaseManager.insertUser(exampleUser);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot insert a new user !");
		}

		// Test updater
		exampleUser.setUserPseudo("Test_pseudo2");
		try {
			UserDatabaseManager.updateUser(exampleUser);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot update user !");
		}

		// Test getter
		UserFilter userFilter = new UserFilter();
		userFilter.addUserId("@tester_1");
		try {
			List<UserModel> users = UserDatabaseManager.getUsers(userFilter, false);
			assertEquals(1, users.size());
			assertEquals("Test_pseudo2", users.get(0).getUserPseudo());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot get users !");
		}

		// Test deleter
		try {
			UserDatabaseManager.deleteUser(exampleUser);
			List<UserModel> noUser = UserDatabaseManager.getUsers(userFilter, false);
			assertEquals(0, noUser.size());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot delete user !");
		}
	}

	@Test
	public void testFollow() {
		// Insert test users
		UserModel exampleUser1 = new UserModel();
		exampleUser1.setUserId("@tester_2");
		exampleUser1.setUserPseudo("Test_pseudo");
		exampleUser1.setUserEmail("test2@test.mail");
		exampleUser1.setUserPassword("0e3e75234abc68f4378a86b3f4b32a198ba301845b0cd6e50106e874345700cc6663a86c1ea125dc5e92be17c98f9a0f85ca9d5f595db2012f7cc3571945c123");
		exampleUser1.setUserDate(new Date(new java.util.Date().getTime()));
		exampleUser1.setUserAdmin(true);

		UserModel exampleUser2 = new UserModel();
		exampleUser2.setUserId("@tester_3");
		exampleUser2.setUserPseudo("Test_pseudo");
		exampleUser2.setUserEmail("test3@test.mail");
		exampleUser2.setUserPassword("0e3e75234abc68f4378a86b3f4b32a198ba301845b0cd6e50106e874345700cc6663a86c1ea125dc5e92be17c98f9a0f85ca9d5f595db2012f7cc3571945c123");
		exampleUser2.setUserDate(new Date(new java.util.Date().getTime()));
		exampleUser2.setUserAdmin(true);

		UserModel exampleUser3 = new UserModel();
		exampleUser3.setUserId("@tester_4");
		exampleUser3.setUserPseudo("Test_pseudo");
		exampleUser3.setUserEmail("test4@test.mail");
		exampleUser3.setUserPassword("0e3e75234abc68f4378a86b3f4b32a198ba301845b0cd6e50106e874345700cc6663a86c1ea125dc5e92be17c98f9a0f85ca9d5f595db2012f7cc3571945c123");
		exampleUser3.setUserDate(new Date(new java.util.Date().getTime()));
		exampleUser3.setUserAdmin(true);

		try {
			UserDatabaseManager.insertUser(exampleUser1);
			UserDatabaseManager.insertUser(exampleUser2);
			UserDatabaseManager.insertUser(exampleUser3);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot insert users for follow tests !");
		}

		// Test insertion
		FollowModel followModel1 = new FollowModel();
		followModel1.setFollowedUserId(exampleUser1.getUserId());
		followModel1.setFollowingUserId(exampleUser2.getUserId());
		followModel1.setFollowDate(new Date(new java.util.Date().getTime()));

		FollowModel followModel2 = new FollowModel();
		followModel2.setFollowedUserId(exampleUser1.getUserId());
		followModel2.setFollowingUserId(exampleUser3.getUserId());
		followModel2.setFollowDate(new Date(new java.util.Date().getTime()));

		FollowModel followModel3 = new FollowModel();
		followModel3.setFollowedUserId(exampleUser3.getUserId());
		followModel3.setFollowingUserId(exampleUser1.getUserId());
		followModel3.setFollowDate(new Date(new java.util.Date().getTime()));
		try {
			FollowDatabaseManager.insertFollow(followModel1);
			FollowDatabaseManager.insertFollow(followModel2);
			FollowDatabaseManager.insertFollow(followModel3);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot insert follows in the database !");
		}

		// Test getting
		FollowFilter filter = new FollowFilter();
		filter.addFollowedUserId(exampleUser1.getUserId());
		try {
			List<FollowModel> follows = FollowDatabaseManager.getFollows(filter, false);
			assertEquals(2, follows.size());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot get follows !");
		}

		// Test deletion
		try {
			FollowDatabaseManager.deleteFollow(followModel1);
			List<FollowModel> follows = FollowDatabaseManager.getFollows(filter, false);
			assertEquals(1, follows.size());
			assertEquals("@tester_4", follows.get(0).getFollowingUserId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot delete follows !");
		}
	}

	@Test
	public void testBoard() {
		// Insert a new user to simulate board creation
		UserModel exampleUser = new UserModel();
		exampleUser.setUserId("@tester_5");
		exampleUser.setUserPseudo("Test_pseudo");
		exampleUser.setUserEmail("test5@test.mail");
		exampleUser.setUserPassword("0e3e75234abc68f4378a86b3f4b32a198ba301845b0cd6e50106e874345700cc6663a86c1ea125dc5e92be17c98f9a0f85ca9d5f595db2012f7cc3571945c123");
		exampleUser.setUserDate(new Date(new java.util.Date().getTime()));
		exampleUser.setUserAdmin(true);
		try {
			UserDatabaseManager.insertUser(exampleUser);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot insert a new user for board testing !");
		}

		// Test insertion
		BoardModel newBoard = new BoardModel();
		newBoard.setBoardName("test_board");
		newBoard.setBoardCreatorId("@tester_5");
		newBoard.setBoardDescription("This is the test board of the test user");
		newBoard.addMessageId("1");
		newBoard.addMessageId("3");
		try {
			BoardDatabaseManager.insertBoard(newBoard);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot insert a new board !");
		}

		// Test updating
		newBoard.setBoardDescription("LOL");
		try {
			BoardDatabaseManager.updateBoard(newBoard);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot update the board !");
		}

		// Test the board get for one
		BoardFilter filter = new BoardFilter();
		filter.addBoardName("test_board");
		filter.addBoardCreatorId("@tester_5");
		try {
			List<BoardModel> res = BoardDatabaseManager.getBoards(filter, false);
			assertEquals(1, res.size());
			assertEquals("LOL", res.get(0).getBoardDescription());
			assertEquals(2, res.get(0).getBoardMessagesId().size());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot get one board !");
		}

		// Test the board deletion
		try {
			BoardDatabaseManager.deleteBoard(newBoard);
			List<BoardModel> noBoard = BoardDatabaseManager.getBoards(filter, false);
			assertEquals(0, noBoard.size());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot delete the board !");
		}
	}

	@Test
	public void testMessage() {
		// Insert a new user to simulate board creation
		UserModel exampleUser = new UserModel();
		exampleUser.setUserId("@tester_6");
		exampleUser.setUserPseudo("Test_pseudo");
		exampleUser.setUserEmail("test6@test.mail");
		exampleUser.setUserPassword("0e3e75234abc68f4378a86b3f4b32a198ba301845b0cd6e50106e874345700cc6663a86c1ea125dc5e92be17c98f9a0f85ca9d5f595db2012f7cc3571945c123");
		exampleUser.setUserDate(new Date(new java.util.Date().getTime()));
		exampleUser.setUserAdmin(true);
		try {
			UserDatabaseManager.insertUser(exampleUser);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot insert a new user for board testing !");
		}

		// Test a new board to make new message
		BoardModel newBoard = new BoardModel();
		newBoard.setBoardName("test_board2");
		newBoard.setBoardCreatorId("@tester_6");
		newBoard.setBoardDescription("This is the test board of the test user");
		try {
			BoardDatabaseManager.insertBoard(newBoard);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot insert a new board !");
		}

		// Test message insertion
		MessageModel message = new MessageModel();
		message.setMessageBoardName("test_board2");
		message.setMessageDate(new Date(new java.util.Date().getTime()));
		message.setMessageId("1");
		message.setMessagePosterId("@tester");
		message.setMessageText("This is a test message");
		try {
			assertEquals("1", MessageDatabaseManager.getNextRootMessageId());
			MessageDatabaseManager.insertMessage(message);
			assertEquals("2", MessageDatabaseManager.getNextRootMessageId());
		} catch (MongoException e) {
			e.printStackTrace();
			fail("Cannot insert a new message !");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot add the message to the board !");
		}

		// Test answer insertion
		MessageModel answer = new MessageModel();
		answer.setMessageBoardName("test_board2");
		answer.setMessageDate(new Date(new java.util.Date().getTime()));
		answer.setMessageId(message.getNextAnswerId());
		answer.setMessagePosterId("@tester");
		answer.setMessageText("This is a test answer");
		try {
			MessageDatabaseManager.insertMessage(answer);
		} catch (MongoException e) {
			e.printStackTrace();
			fail("Cannot insert an answer !");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot add the message to the board !");
		}

		// Test updating
		answer.setMessageText("LOL");
		try {
			MessageDatabaseManager.updateMessage(answer);
		} catch (MongoException e) {
			e.printStackTrace();
			fail("Cannot update a message");
		}

		// Test getting
		MessageFilter filter = new MessageFilter();
		filter.addMessageId("1.");
		List<MessageModel> messages = MessageDatabaseManager.getMessage(filter, true, true);
		assertEquals("1.1", messages.get(0).getMessageId());
		assertEquals("LOL", messages.get(0).getMessageText());
		

		// Test deleting
		MessageModel answerAnswer = new MessageModel();
		answerAnswer.setMessageId(answer.getNextAnswerId());
		answerAnswer.setMessageText("This is an answer to an answer");
		answerAnswer.setMessageBoardName(newBoard.getBoardName());
		answerAnswer.setMessagePosterId(exampleUser.getUserId());
		answerAnswer.setMessageDate(new Date(new java.util.Date().getTime()));
		
		BoardFilter newBoardFilter = new BoardFilter();
		newBoardFilter.addBoardName(newBoard.getBoardName());
		
		message.addAnwserId("1.1");
		answer.addAnwserId("1.1.1");
		try {
			MessageDatabaseManager.insertMessage(answerAnswer);
			assertEquals(3, MessageDatabaseManager.getMessage(new MessageFilter(), false, true).size());
			
			BoardModel board = BoardDatabaseManager.getBoards(newBoardFilter, false).get(0);
			assertEquals(1, board.getBoardMessagesId().size());

			MessageDatabaseManager.deleteMessage(message);
			assertEquals(0, MessageDatabaseManager.getMessage(new MessageFilter(), false, true).size());

			board = BoardDatabaseManager.getBoards(newBoardFilter, false).get(0);
			assertEquals(0, board.getBoardMessagesId().size());
		} catch (MongoException e) {
			e.printStackTrace();
			fail("Cannot delete message !");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot delete the message from the BELONGS_TO_BOARD table !");
		}

	}

}
