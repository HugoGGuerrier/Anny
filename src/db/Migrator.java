package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import tools.Logger;

/**
 * A class to migrate the database from a version to another
 * 
 * @author Siau Emilie
 * @author Hugo Guerrier
 */
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
	private final String[] version1Upgrade = {
			"CREATE OR REPLACE TABLE USER ("
			+ "userId VARCHAR(32) PRIMARY KEY NOT NULL, "
			+ "userPseudo VARCHAR(64) NOT NULL, "
			+ "userName VARCHAR(64) NOT NULL, "
			+ "userSurname VARCHAR(32) NOT NULL, "
			+ "userEmail VARCHAR(64) NOT NULL UNIQUE, "
			+ "userPassword VARCHAR(128) NOT NULL, "
			+ "userDate DATE NOT NULL, "
			+ "userAdmin BOOLEAN DEFAULT 0"
			+ "); ",

			"CREATE OR REPLACE TABLE FOLLOW ("
			+ "followedUserId VARCHAR(32) NOT NULL, "
			+ "followingUserId VARCHAR(32) NOT NULL, "
			+ "followDate DATE NOT NULL, "
			+ "PRIMARY KEY(followedUserId, followingUserId), "
			+ "FOREIGN KEY (followedUserId) REFERENCES USER(userId) ON DELETE CASCADE ON UPDATE CASCADE, "
			+ "FOREIGN KEY (followingUserId) REFERENCES USER(userId) ON DELETE CASCADE ON UPDATE CASCADE "
			+ "); ",

			"CREATE OR REPLACE TABLE BOARD ("
			+ "boardName VARCHAR(32) PRIMARY KEY NOT NULL, "
			+ "boardDescription VARCHAR(254) NOT NULL, "
			+ "boardCreatorId VARCHAR(32), "
			+ "FOREIGN KEY (boardCreatorId) REFERENCES USER(userId) ON DELETE SET NULL ON UPDATE CASCADE"
			+ "); ",

			"CREATE OR REPLACE TABLE BELONGS_TO_BOARD ("
			+ "messageId VARCHAR(64) PRIMARY KEY NOT NULL, "
			+ "boardName VARCHAR(32) NOT NULL, "
			+ "FOREIGN KEY (boardName) REFERENCES BOARD(boardName) ON DELETE CASCADE ON UPDATE CASCADE"
			+ "); "
			
	};

	/** Downgrade version 1 */
	private final String[] version1Downgrade = {"DROP TABLE IF EXISTS FOLLOW; ",
			"DROP TABLE IF EXISTS BELONGS_TO_BOARD; ",
			"DROP TABLE IF EXISTS BOARD; ",
			"DROP TABLE IF EXISTS USER; "
	};


	// ----- Constructors -----


	/**
	 * Create the unique migrator instance and get the database version
	 * 
	 * @throws SQLException If the class cannot open MySQL connection
	 */
	private Migrator() throws SQLException {
		// Get the application logger
		this.logger = Logger.getInstance();

		// Get the database connection
		Connection connection = Database.getMySQLConnection();
		Statement stmt = connection.createStatement();

		try {

			// Fetch the database version
			ResultSet resultSet = stmt.executeQuery("SELECT * FROM DB_VERSION");
			resultSet.next();

			this.currentDatabaseVersion = resultSet.getInt("databaseVersion");

		} catch (SQLException e) {

			// Create the table and set the current version to 0
			this.currentDatabaseVersion = 0;

			stmt.executeUpdate("DROP TABLE IF EXISTS DB_VERSION");
			stmt.executeUpdate("CREATE TABLE DB_VERSION(databaseVersion INT NOT NULL PRIMARY KEY)");
			stmt.executeUpdate("INSERT INTO DB_VERSION (databaseVersion) VALUES(0)");
			
			this.logger.log("Database version intialized", Logger.INFO);

		}
	}

	/**
	 * Get the migrator unique instance
	 * 
	 * @return The migrator instance
	 * @throws SQLException If there is an exception during the instance creation
	 */
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
	
	
	// ----- Setters -----
	
	
	private void setCurrentDatabaseVersion(int version) throws SQLException {
		Connection connection = Database.getMySQLConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM DB_VERSION");
		stmt.executeUpdate("INSERT INTO DB_VERSION (databaseVersion) VALUES(" + String.valueOf(version) + ")");
		this.currentDatabaseVersion = version;
	}


	// ----- Class methods -----


	/**
	 * Upgrade the database to the targeted version
	 * 
	 * @param targetVersion The wanted version
	 * @return If the database is in the correct version
	 * @throws SQLException If there is an error during the database upgrading
	 */
	public boolean upgrade(int targetVersion) throws SQLException {
		Connection connection = Database.getMySQLConnection();
		Statement stmt = connection.createStatement();

		if(targetVersion > this.currentDatabaseVersion) {

			if(this.currentDatabaseVersion < 1 && targetVersion >= 1) {
				for (String update : this.version1Upgrade) {
					stmt.executeUpdate(update);
				}
			}

			// Set the current database version
			this.setCurrentDatabaseVersion(targetVersion);
			return true;
		}

		// Return false if the target version is lower than the current version
		return false;
	}

	/**
	 * Downgrade the version to the targeted version
	 * 
	 * @param targetVersion The targeted version
	 * @return If the database is in the correct state
	 * @throws SQLException If there is an error during SQL execution
	 */
	public boolean downgrade(int targetVersion) throws SQLException {
		Connection connection = Database.getMySQLConnection();
		Statement stmt = connection.createStatement();

		if(targetVersion < this.currentDatabaseVersion) {

			if(this.currentDatabaseVersion >= 1 && targetVersion < 1) {
				for (String update : this.version1Downgrade) {
					stmt.executeUpdate(update);
				}
			}

			// Set the current database version
			this.setCurrentDatabaseVersion(targetVersion);
			return true;
		}

		// Return false if the target version is greater than the current version
		return false;
	}

}
