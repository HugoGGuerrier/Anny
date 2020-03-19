package one.anny.main.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import one.anny.main.tools.Config;
import one.anny.main.tools.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Class which handle SQL connections and use connection pooling
 * 
 * @author Laure Soulier
 */
public class Database {

	// ----- Attributes -----

	
	/** The datasource for the connection pooling */
	private DataSource dataSource;

	/** The database instance */
	private static Database instance = null;


	// ----- Constructors -----


	/**
	 * Construct a new database and the datasource associated to it
	 * 
	 * @param ressource_name
	 * @throws SQLException
	 */
	public Database(String ressource_name) throws SQLException {
		try {
			
			this.dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + ressource_name);
			
		} catch(NamingException e) {
			
			throw new SQLException(ressource_name + " unreachable : " + e.getMessage());
			
		}
	}
	
	
	// ----- Class methods -----
	

	/**
	 * Get a connection from the pool
	 * 
	 * @return A connection
	 * @throws SQLException If there is an error during the pooling
	 */
	public Connection getConnection() throws SQLException {
		return this.dataSource.getConnection();
	}

	/**
	 * Get an MySQL connection with or without the pooling
	 * 
	 * @return The MySQL connection
	 * @throws SQLException
	 */
	public static Connection getMySQLConnection() throws SQLException {
		// Check if the pooling is used
		if(!Config.isMysqlPooling()) {
			try {
				
				// Import the JDBC MySQL driver and return the connection
				Class.forName("com.mysql.cj.jdbc.Driver");
				String dsn = "jdbc:mysql://" + Config.getMysqlHost() + "/" + Config.getMysqlDatabase();
				return DriverManager.getConnection(dsn , Config.getMysqlLogin(), Config.getMysqlPassword());
				
			} catch (ClassNotFoundException e) {
				
				// Log the database error
				Logger.log("JDBC mysql driver is missing", Logger.ERROR);
				Logger.log(e, Logger.ERROR);
				
				// Throw an error to avoid null pointer
				throw new SQLException("Cannot get a mysql connection");
				
			}
		} else {
			if(Database.instance == null) {
				Database.instance = new Database("jdbc/db");
			}
			
			return Database.instance.getConnection();
		}
	}

	/**
	 * Get the MongoDB connection
	 * 
	 * @return The connection
	 */
	public static MongoDatabase getMongoDBConnection()  {
		MongoClient mongo = MongoClients.create();
		return mongo.getDatabase(Config.getMongoDatabase());
	}

}