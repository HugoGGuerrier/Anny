package one.anny.main.db.managers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import one.anny.main.db.Database;
import one.anny.main.db.filters.UserFilter;
import one.anny.main.tools.models.UserModel;

/**
 * Class that helps user database management
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class UserDatabaseManager {

	// ----- Class Methods -----


	/**
	 * Insert a new user from an user model
	 * 
	 * @param userModel The user you want to insert
	 * @throws SQLException if there is an exception during the user insertion
	 */
	public static void insertUser(UserModel userModel) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Create the SQL insertion
		String insertion = "INSERT INTO USER (userId, userPseudo, userName, userSurname, userEmail, userPassword, userDate, userAdmin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		// Prepare the statement
		PreparedStatement preparedStatement = connection.prepareStatement(insertion);

		// Bind the parameters
		preparedStatement.setString(1, userModel.getUserId());
		preparedStatement.setString(2, userModel.getUserPseudo());
		preparedStatement.setString(3, userModel.getUserName());
		preparedStatement.setString(4, userModel.getUserSurname());
		preparedStatement.setString(5, userModel.getUserEmail());
		preparedStatement.setString(6, userModel.getUserPassword());
		preparedStatement.setDate(7, userModel.getUserDate());
		preparedStatement.setBoolean(8, userModel.isUserAdmin());

		// Execute the statement
		preparedStatement.executeUpdate();
	}

	/**
	 * Update the user in the database basing on the model's id
	 * 
	 * @param userModel The user you want to update
	 * @throws SQLException If there is an error during the updating
	 */
	public static void updateUser(UserModel userModel) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Create the SQL insertion (no date because you can't update the inscription date)
		String update = "UPDATE USER SET userPseudo = ?, userName = ?, userSurname = ?, userEmail = ?, userPassword = ?, userAdmin = ? WHERE userId = ?";

		// Prepare the statement
		PreparedStatement preparedStatement = connection.prepareStatement(update);

		// Bind the parameters
		preparedStatement.setString(1, userModel.getUserPseudo());
		preparedStatement.setString(2, userModel.getUserName());
		preparedStatement.setString(3, userModel.getUserSurname());
		preparedStatement.setString(4, userModel.getUserEmail());
		preparedStatement.setString(5, userModel.getUserPassword());
		preparedStatement.setInt(6, userModel.isUserAdmin() ? 1 : 0);
		preparedStatement.setString(7, userModel.getUserId());

		// Execute the statement
		preparedStatement.executeUpdate();
	}

	/**
	 * Delete an user from the database
	 * 
	 * @param userModel The user you want to delete
	 * @throws SQLException If there is an error during the user deletion
	 */
	public static void deleteUser(UserModel userModel) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Create the SQL insertion
		String deletion = "DELETE FROM USER WHERE userId = ?";

		// Prepare the statement
		PreparedStatement preparedStatement = connection.prepareStatement(deletion);

		// Bind the parameters
		preparedStatement.setString(1, userModel.getUserId());

		// Execute the statement
		preparedStatement.executeUpdate();
	}

	/**
	 * Get a list of users with the user model
	 * 
	 * @param model The user model
	 * @param isLike True if you want to use like
	 * @return The list of users
	 * @throws SQLException If there is an error during the query
	 */
	public static List<UserModel> getUsers(UserFilter filter, boolean isLike) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Prepare the selection query
		StringBuilder query = new StringBuilder("SELECT * FROM USER WHERE true");

		// Create the wanted SQL query

		// Add all the wanted id
		if(filter.getUserIdSet().size() > 0) {
			query.append(" AND (");

			Iterator<String> iterator = filter.getUserIdSet().iterator();
			while(iterator.hasNext()) {
				iterator.next();
				query.append(" userId " + (isLike ? "LIKE" : "=") + " ?");
				if(iterator.hasNext()) {
					query.append(" OR");
				}
			}

			query.append(" )");
		}

		// Add all the wanted pseudo
		if(filter.getUserPseudoSet().size() > 0) {
			query.append(" AND (");

			Iterator<String> iterator = filter.getUserPseudoSet().iterator();
			while(iterator.hasNext()) {
				iterator.next();
				query.append(" userPseudo " + (isLike ? "LIKE" : "=") + " ?");
				if(iterator.hasNext()) {
					query.append(" OR");
				}
			}

			query.append(" )");
		}

		// Add all the wanted name
		if(filter.getUserNameSet().size() > 0) {
			query.append(" AND (");

			Iterator<String> iterator = filter.getUserNameSet().iterator();
			while(iterator.hasNext()) {
				iterator.next();
				query.append(" userName " + (isLike ? "LIKE" : "=") + " ?");
				if(iterator.hasNext()) {
					query.append(" OR");
				}
			}

			query.append(" )");
		}

		// Add all the wanted surname
		if(filter.getUserSurnameSet().size() > 0) {
			query.append(" AND (");

			Iterator<String> iterator = filter.getUserSurnameSet().iterator();
			while(iterator.hasNext()) {
				iterator.next();
				query.append(" userSurname " + (isLike ? "LIKE" : "=") + " ?");
				if(iterator.hasNext()) {
					query.append(" OR");
				}
			}

			query.append(" )");
		}

		// Add all the wanted email
		if(filter.getUserEmailSet().size() > 0) {
			query.append(" AND (");

			Iterator<String> iterator = filter.getUserEmailSet().iterator();
			while(iterator.hasNext()) {
				iterator.next();
				query.append(" userEmail " + (isLike ? "LIKE" : "=") + " ?");
				if(iterator.hasNext()) {
					query.append(" OR");
				}
			}

			query.append(" )");
		}

		// Add all the wanted password
		if(filter.getUserPasswordSet().size() > 0) {
			query.append(" AND (");

			Iterator<String> iterator = filter.getUserPasswordSet().iterator();
			while(iterator.hasNext()) {
				iterator.next();
				query.append(" userPassword = ?");
				if(iterator.hasNext()) {
					query.append(" OR");
				}
			}

			query.append(" )");
		}

		// Add all the wanted date
		if(filter.getUserDateSet().size() > 0) {
			query.append(" AND (");

			Iterator<Date> iterator = filter.getUserDateSet().iterator();
			while(iterator.hasNext()) {
				iterator.next();
				query.append(" userDate = ?");
				if(iterator.hasNext()) {
					query.append(" OR");
				}
			}

			query.append(" )");
		}
		
		// Set the admin filter
		if(filter.isUserAdmin() != null) {
			query.append(" AND userAdmin = ?");
		}
		
		// Write the order statement
		if(filter.getOrderColumn() != null) {
			query.append(" ORDER BY " + filter.getOrderColumn() + (filter.isOrderReversed() ? " DESC" : " ASC"));
		}

		// Prepare the statement
		PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
		int nextArgPointer = 1;

		for(String id : filter.getUserIdSet()) {
			preparedStatement.setString(nextArgPointer++, id);
		}
		
		for(String pseudo : filter.getUserPseudoSet()) {
			preparedStatement.setString(nextArgPointer++, pseudo);
		}
		
		for(String name : filter.getUserNameSet()) {
			preparedStatement.setString(nextArgPointer++, name);
		}
		
		for(String surname : filter.getUserSurnameSet()) {
			preparedStatement.setString(nextArgPointer++, surname);
		}
		
		for(String email : filter.getUserEmailSet()) {
			preparedStatement.setString(nextArgPointer++, email);
		}
		
		for(String password : filter.getUserPasswordSet()) {
			preparedStatement.setString(nextArgPointer++, password);
		}
		
		for(Date date : filter.getUserDateSet()) {
			preparedStatement.setDate(nextArgPointer++, date);
		}
		
		if(filter.isUserAdmin() != null) {
			preparedStatement.setBoolean(nextArgPointer++, filter.isUserAdmin());
		}
		
		System.out.println(preparedStatement.toString());

		// Prepare the result list
		List<UserModel> res = new ArrayList<UserModel>();

		// Get the result from the database
		ResultSet resultSet = preparedStatement.executeQuery();

		while(resultSet.next()) {
			String id = resultSet.getString("userId");
			String pseudo = resultSet.getString("userPseudo");
			String name = resultSet.getString("userName");
			String surname = resultSet.getString("userSurname");
			String email = resultSet.getString("userEmail");
			String password = resultSet.getString("userPassword");
			Date date = resultSet.getDate("userDate");
			Boolean admin = resultSet.getBoolean("userAdmin");

			UserModel newUser = new UserModel();
			newUser.setUserId(id);
			newUser.setUserPseudo(pseudo);
			newUser.setUserName(name);
			newUser.setUserSurname(surname);
			newUser.setUserEmail(email);
			newUser.setUserPassword(password);
			newUser.setUserDate(date);
			newUser.setUserAdmin(admin);

			res.add(newUser);
		}

		// Return the result
		return res;
	}

}
