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
import one.anny.main.db.filters.FollowFilter;
import one.anny.main.tools.models.FollowModel;

public class FollowDatabaseManager {

	// ----- Class Methods -----


	/**
	 * Insert a new follow in the database
	 * 
	 * @param followModel The follow you want to insert
	 * @throws SQLException If there is an error during the insertion
	 */
	public static void insertFollow(FollowModel followModel) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Create the SQL insertion
		String insertion = "INSERT INTO FOLLOW (followedUserId, followingUserId, followDate) VALUES (?, ?, ?)";

		// Prepare the statement
		PreparedStatement preparedStatement = connection.prepareStatement(insertion);

		// Bind the parameters
		preparedStatement.setString(1, followModel.getFollowedUserId());
		preparedStatement.setString(2, followModel.getFollowingUserId());
		preparedStatement.setDate(3, followModel.getFollowDate());

		// Execute the statement
		preparedStatement.executeUpdate();
	}

	/**
	 * Delete the follow from the database
	 * 
	 * @param followModel The follow you want to delete
	 * @throws SQLException If there is an error during the deletion
	 */
	public static void deleteFollow(FollowModel followModel) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Create the SQL insertion
		String deletion = "DELETE FROM FOLLOW WHERE followedUserId = ? AND followingUserId = ?";

		// Prepare the statement
		PreparedStatement preparedStatement = connection.prepareStatement(deletion);

		// Bind the parameters
		preparedStatement.setString(1, followModel.getFollowedUserId());
		preparedStatement.setString(2, followModel.getFollowingUserId());

		// Execute the statement
		preparedStatement.executeUpdate();
	}

	/**
	 * Get a list of follow from the wanted model
	 * 
	 * @param model The follow model
	 * @param isLike Use the LIKE SQL test
	 * @return The list of follow
	 * @throws SQLException If there is an error during the follows fetching
	 */
	public static List<FollowModel> getFollows(FollowFilter filter, boolean isLike) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();

		// Prepare the selection query
		StringBuilder query = new StringBuilder("SELECT * FROM FOLLOW WHERE true");

		// Create the wanted SQL
		
		// Add all wanted followed user id
		if(filter.getFollowedUserIdSet().size() > 0) {
			query.append(" AND (");
			
			Iterator<String> iterator = filter.getFollowedUserIdSet().iterator();
			while(iterator.hasNext()) {
				iterator.next();
				query.append(" followedUserId " + (isLike ? "LIKE" : "=") + " ?");
				if(iterator.hasNext()) {
					query.append(" OR");
				}
			}
			
			query.append(" )");
		}
		
		// Add all following user id to look for
		if(filter.getFollowingUserIdSet().size() > 0) {
			query.append(" AND (");
			
			Iterator<String> iterator = filter.getFollowingUserIdSet().iterator();
			while(iterator.hasNext()) {
				iterator.next();
				query.append(" followingUserId " + (isLike ? "LIKE" : "=") + " ?");
				if(iterator.hasNext()) {
					query.append(" OR");
				}
			}
			
			query.append(" )");
		}
		
		// Add all follow date to search
		if(filter.getFollowDateSet().size() > 0) {
			query.append(" AND (");
			
			Iterator<Date> iterator = filter.getFollowDateSet().iterator();
			while(iterator.hasNext()) {
				iterator.next();
				query.append(" followDate = ?");
				if(iterator.hasNext()) {
					query.append(" OR");
				}
			}
			
			query.append(" )");
		}

		// Prepare the statement
		PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
		int nextArgPointer = 1;

		// Bind the parameters
		for(String userId : filter.getFollowedUserIdSet()) {
			preparedStatement.setString(nextArgPointer++, userId);
		}
		for(String userId : filter.getFollowingUserIdSet()) {
			preparedStatement.setString(nextArgPointer++, userId);
		}
		for(Date date : filter.getFollowDateSet()) {
			preparedStatement.setDate(nextArgPointer++, date);
		}

		// Prepare the result 
		List<FollowModel> res = new ArrayList<FollowModel>();

		// Get the result from the database
		ResultSet resultSet = preparedStatement.executeQuery();

		while(resultSet.next()) {
			String followedUserId = resultSet.getString("followedUserId");
			String followingUserId = resultSet.getString("followingUserId");
			Date followDate = Date.valueOf(resultSet.getString("followDate"));
			FollowModel newFollow = new FollowModel();
			newFollow.setFollowedUserId(followedUserId);
			newFollow.setFollowingUserId(followingUserId);
			newFollow.setFollowDate(followDate);

			res.add(newFollow);
		}

		// Return the result
		return res;
	}

}
