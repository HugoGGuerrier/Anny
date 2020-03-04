package db.managers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.Database;
import tools.models.UserModel;

/**
 * Class that helps user database management
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
public class UserDatabaseManager {

	// ----- Attributes -----


	/** Unique instance of the user database manager */
	private static UserDatabaseManager instance = null;


	// ----- Constructors ----- 


	/**
	 * Construct an instance for user database manager
	 */
	private UserDatabaseManager() {

	}

	/**
	 * Get the unique instance of user database manager
	 * 
	 * @return The database manager
	 */
	public static UserDatabaseManager getInstance() {
		if (UserDatabaseManager.instance == null) {
			UserDatabaseManager.instance = new UserDatabaseManager();
		}
		return UserDatabaseManager.instance;
	}


	// ----- Class Methods -----


	/**
	 * Insert a new user from an user model
	 * 
	 * @param userModel The user you want to insert
	 * @throws SQLException if there is an exception during the user insertion
	 */
	public void insertUser(UserModel userModel) throws SQLException {
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
	public void updateUser(UserModel userModel) throws SQLException {
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
	public void deleteUser(UserModel userModel) throws SQLException {
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
	public List<UserModel> getUsers(UserModel model, boolean isLike) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Prepare the selection query
		StringBuilder query = new StringBuilder("SELECT * FROM USER WHERE 1=1");

		// Create the wanted SQL query
		if (isLike) {

			if (model.getUserId() != null) {
				query.append(" AND userId LIKE ?");
			}
			if (model.getUserPseudo() != null) {
				query.append(" AND userPseudo LIKE ?");
			}
			if (model.getUserName() != null) {
				query.append(" AND userName LIKE ?");
			}
			if (model.getUserSurname() != null) {
				query.append(" AND userSurname LIKE ?");
			}
			if (model.getUserEmail() != null) {
				query.append(" AND userEmail LIKE ?");
			}
			if (model.getUserPassword() != null) {
				query.append(" AND userPassword LIKE ?");
			}
			if (model.getUserDate() != null) {
				query.append(" AND userDate = ?");
			}
			if (model.isUserAdmin() != null) {
				query.append(" AND userAdmin = ?");
			}

		} else {

			if (model.getUserId() != null) {
				query.append(" AND userId = ?");
			}
			if (model.getUserPseudo() != null) {
				query.append(" AND userPseudo = ?");
			}
			if (model.getUserName() != null) {
				query.append(" AND userName = ?");
			}
			if (model.getUserSurname() != null) {
				query.append(" AND userSurname = ?");
			}
			if (model.getUserEmail() != null) {
				query.append(" AND userEmail = ?");
			}
			if (model.getUserPassword() != null) {
				query.append(" AND userPassword = ?");
			}
			if (model.getUserDate() != null) {
				query.append(" AND userDate = ?");
			}
			if (model.isUserAdmin() != null) {
				query.append(" AND userAdmin = ?");
			}

		}

		// Prepare the statement
		PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
		int nextArgPointer = 1;

		// Bind the wanted parameters
		if (isLike) {

			if (model.getUserId() != null) {
				preparedStatement.setString(nextArgPointer++, "%" + model.getUserId() + "%");
			}
			if (model.getUserPseudo() != null) {
				preparedStatement.setString(nextArgPointer++, "%" + model.getUserPseudo() + "%");
			}
			if (model.getUserName() != null) {
				preparedStatement.setString(nextArgPointer++, "%" + model.getUserName() + "%");
			}
			if (model.getUserSurname() != null) {
				preparedStatement.setString(nextArgPointer++, "%" + model.getUserSurname() + "%");
			}
			if (model.getUserEmail() != null) {
				preparedStatement.setString(nextArgPointer++, "%" + model.getUserEmail() + "%");
			}
			if (model.getUserPassword() != null) {
				preparedStatement.setString(nextArgPointer++, "%" + model.getUserPassword() + "%");
			}
			if (model.getUserDate() != null) {
				preparedStatement.setDate(nextArgPointer++, model.getUserDate());
			}
			if (model.isUserAdmin() != null) {
				preparedStatement.setInt(nextArgPointer, (model.isUserAdmin() ? 1 : 0));
			}

		} else {

			if (model.getUserId() != null) {
				preparedStatement.setString(nextArgPointer++, model.getUserId());
			}
			if (model.getUserPseudo() != null) {
				preparedStatement.setString(nextArgPointer++, model.getUserPseudo());
			}
			if (model.getUserName() != null) {
				preparedStatement.setString(nextArgPointer++, model.getUserName());
			}
			if (model.getUserSurname() != null) {
				preparedStatement.setString(nextArgPointer++, model.getUserSurname());
			}
			if (model.getUserEmail() != null) {
				preparedStatement.setString(nextArgPointer++, model.getUserEmail());
			}
			if (model.getUserPassword() != null) {
				preparedStatement.setString(nextArgPointer++, model.getUserPassword());
			}
			if (model.getUserDate() != null) {
				preparedStatement.setDate(nextArgPointer++, model.getUserDate());
			}
			if (model.isUserAdmin() != null) {
				preparedStatement.setInt(nextArgPointer, (model.isUserAdmin() ? 1 : 0));
			}

		}

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
