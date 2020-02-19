package db.managers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.Database;
import tools.models.UserModel;

public class UserDatabaseManager {

	// ----- Attributes -----
	
	
	/** */
	private static UserDatabaseManager instance = null;
	
	
	// ----- Constructors ----- 
	
	
	private UserDatabaseManager() {
		
	}
	
	public static UserDatabaseManager getInstance() {
		if (UserDatabaseManager.instance == null) {
			UserDatabaseManager.instance = new UserDatabaseManager();
		}
		return UserDatabaseManager.instance;
	}
	
	
	// ----- Class Methods -----

	
	public List<UserModel> getUsers(UserModel model, boolean isLike) throws SQLException {
		Connection connection = Database.getMySQLConnection();
		Statement stmt = connection.createStatement();
		
		// 1=1 in case of model==null or there is no initialized field.
		StringBuilder query = new StringBuilder("SELECT * FROM USER WHERE 1=1");
		
		if (isLike) {
			
			//TODO
			
		} else {
			
			if (model.getUserId() != null) {
				query.append(" AND userId = '" + model.getUserId() + "'");
			}
			if (model.getUserPseudo() != null) {
				query.append(" AND userPseudo = '" + model.getUserPseudo() + "'");
			}
			if (model.getUserName() != null) {
				query.append(" AND userName = '" + model.getUserName() + "'");
			}
			if (model.getUserSurname() != null) {
				query.append(" AND userSurname = '" + model.getUserSurname() + "'");
			}
			if (model.getUserEmail() != null) {
				query.append(" AND userEmail = '" + model.getUserEmail() + "'");
			}
			if (model.isUserAdmin() != null) {
				query.append(" AND userAdmin = " + (model.isUserAdmin() ? "1" : "0"));
			}
			
		}

		ResultSet resultSet = stmt.executeQuery(query.toString());
		List<UserModel> res = new ArrayList<UserModel>();
		
		while (resultSet.next()) {
			
		}
		
		
		return res;
	}
	
}
