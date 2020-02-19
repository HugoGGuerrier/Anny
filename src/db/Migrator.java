package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import tools.Logger;

public class Migrator {

	// ----- Attributes -----


	/** The current database version stored in the table DB_VERSION */
	private int currentDatabaseVersion;

	/** The logger */
	private Logger logger;

	/** Unique migrator instance (singleton) */
	private static Migrator instance = null;

	// --- SQL scripts
	
	/** Upgrade version 1 */
	private final String version1Upgrade = "CREATE OR REPLACE TABLE USER ("
			+ "userId VARCHAR(32) PRIMARY KEY NOT NULL, "
			+ "userPseudo VARCHAR(64) NOT NULL, "
			+ "userName VARCHAR(64) NOT NULL, "
			+ "userSurname VARCHAR(32) NOT NULL, "
			+ "userEmail VARCHAR(64) NOT NULL, "
			+ "userPassword VARCHAR(128) NOT NULL, "
			+ "userAdmin BOOLEAN"
			+ ") ; "

			+ "CREATE OR REPLACE TABLE FOLLOWS ("
			+ "followedUserId FOREIGN KEY REFERENCES USER(userId) NOT NULL, "
			+ "followingUserId FOREIGN KEY REFERENCES USER(userId) NOT NULL, "
			+ "followDate DATE NOT NULL"
			+ ") ; "

			+ "CREATE OR REPLACE TABLE BOARD ("
			+ "boardName VARCHAR(32) PRIMARY KEY NOT NULL, "
			+ "boardDescription VARCHAR(254) NOT NULL, "
			+ "boardCreatorId FOREIGN KEY REFERENCES USER(userId) NOT NULL"
			+ ") ; "

			+ "CREATE OR REPLACE TABLE BELONGS_TO_BOARD ("
			+ "boardName FOREIGN KEY REFERENCES BOARD(boardName) NOT NULL, "
			+ "messageId BIGINT NOT NULL"
			+ ") ; ";
	
	/** Downgrade version 1 */
	private final String version1Downgrade = "DROP TABLE IF EXISTS FOLLOWS; "
			+ "DROP TABLE IF EXISTS BELONGS_TO_BOARD; "
			+ "DROP TABLE IF EXISTS BOARD; "
			+ "DROP TABLE IF EXISTS USER; ";


	// ----- Constructors -----


	private Migrator() throws SQLException {
		// Get the app logger
		this.logger = Logger.getInstance();

		// Get the database connection
		Connection connection = Database.getMySQLConnection();
		Statement stmt = connection.createStatement();

		try {

			// Fetch the database version
			ResultSet resultSet = stmt.executeQuery("SELECT * FROM DB_VERSION");
			resultSet.next();

			this.currentDatabaseVersion = resultSet.getInt("databaseVersion");
			System.out.println("DB version OK");

		} catch (SQLException e) {

			// Create the table and set the current version to 0
			this.currentDatabaseVersion = 0;

			stmt.executeUpdate("DROP TABLE IF EXISTS DB_VERSION");
			stmt.executeUpdate("CREATE TABLE DB_VERSION(databaseVersion INT NOT NULL PRIMARY KEY)");
			stmt.executeUpdate("INSERT INTO DB_VERSION (databaseVersion) VALUES(0)");

		}
	}

	public static Migrator getInstance() throws SQLException {
		if(Migrator.instance == null) {
			Migrator.instance = new Migrator();
		}
		return Migrator.instance;
	}


	// ----- Getters -----


	public int getCurrentDatabaseVersion() {
		return this.currentDatabaseVersion;
	}


	// ----- Class methods -----


	public void upgrade(int targetVersion) throws SQLException {
		Connection connection = Database.getMySQLConnection();
		Statement stmt = connection.createStatement();
		
		if(targetVersion > this.currentDatabaseVersion) {
			
			if(this.currentDatabaseVersion < 1 && targetVersion >= 1) {
				stmt.executeUpdate(this.version1Upgrade);
			}
			
		}
	}

	public void downgrade(int targetVersion) throws SQLException {
		Connection connection = Database.getMySQLConnection();
		Statement stmt = connection.createStatement();
		
		if(targetVersion < this.currentDatabaseVersion) {
			
			if(this.currentDatabaseVersion >= 1 && targetVersion < 1) {
				stmt.executeUpdate(this.version1Downgrade);
			}
			
		}
	}

}
