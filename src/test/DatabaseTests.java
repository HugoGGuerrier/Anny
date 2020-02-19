package test;

import static org.junit.Assert.fail;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import db.Database;
import db.Migrator;
import tools.Config;
import tools.Logger;
import tools.StdVar;

public class DatabaseTests {

	// ----- Attributes -----


	private static Connection mysqlConnection;


	// ----- Config methods -----


	@BeforeClass
	public static void setup() {		
		// Load the test config files
		Path configTestPath = Paths.get(StdVar.TEST_CONFIG_FILE);
		
		try {
			Reader reader = new FileReader(configTestPath.toAbsolutePath().toFile());
			Config.setBasePath(Paths.get("").toAbsolutePath().toString() + "/WebContent/");
			Config.init(reader);
			
			Migrator migrator = Migrator.getInstance();
			
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
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot close the mysql connection !");
		}
	}


	// ----- Test methods -----


	@Test
	public void testMysql() {
		// Test queries
		try {
			String q = "SELECT * FROM USER";
			Statement st = DatabaseTests.mysqlConnection.createStatement();
			ResultSet rs = st.executeQuery(q);

			while(rs.next()) {
				System.out.println(rs.getString("userId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot execute query !");
		}

		// Test insertions
		try {
			String q = "INSERT INTO USER VALUES ('@marmotte_officiel', 'Marmotte', 'MARMOTTA', 'Marmotta', 'momottejolie@mail.motte', 'caclette4life', 1)";
			Statement st = DatabaseTests.mysqlConnection.createStatement();
			int r = st.executeUpdate(q);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Cannot execute insertion !");
		}
	}
}
