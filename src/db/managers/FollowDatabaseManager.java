package db.managers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.Database;
import tools.models.FollowModel;

public class FollowDatabaseManager {

	// ----- Attributes -----
	
	
	/** */
	private static FollowDatabaseManager instance = null;
	
	
	// ----- Constructors ----- 
	
	
	/**
	 * Construct an instance of follows database manager
	 */
	private FollowDatabaseManager() {
		
	}
	
	/**
	 * Get the unique instance of the follows database manager
	 * 
	 * @return
	 */
	public static FollowDatabaseManager getInstance() {
		if(FollowDatabaseManager.instance == null) {
			FollowDatabaseManager.instance = new FollowDatabaseManager();
		}
		return FollowDatabaseManager.instance;
	}
	
	
	// ----- Class Methods -----

	
	/**
	 * Insert a new follow in the database
	 * 
	 * @param followModel The follow you want to insert
	 * @throws SQLException If there is an error during the insertion
	 */
	public void insertFollow(FollowModel followModel) throws SQLException {
		
	}
	
	/**
	 * Delete the follow from the database
	 * 
	 * @param followModel The follow you want to delete
	 * @throws SQLException If there is an error during the deletion
	 */
	public void deleteFollow(FollowModel followModel) throws SQLException {
		
	}
	
	/**
	 * Get a list of follow from the wanted model
	 * 
	 * @param model The follow model
	 * @param isLike Use the LIKE SQL test
	 * @return The list of follow
	 * @throws SQLException If there is an error during the follows fetching
	 */
	public List<FollowModel> getFollows(FollowModel model, boolean isLike) throws SQLException {
		// Get the MySQL connection
		Connection connection = Database.getMySQLConnection();
		
		// Prepare the selection query
		StringBuilder query = new StringBuilder("SELECT * FROM FOLLOW WHERE 1=1");
		
		// Create the wanted SQL
		if(isLike) {
			
			if(model.getFollowedUserId() != null) {
				query.append(" AND followedUserId LIKE ?");
			}
			if(model.getFollowingUserId() != null) {
				query.append(" AND followingUserId LIKE ?");
			}
			if(model.getFollowDate() != null) {
				query.append(" AND followDate LIKE ?");
			}
			
		} else {
			
			if(model.getFollowedUserId() != null) {
				query.append(" AND followedUserId = ?");
			}
			if(model.getFollowingUserId() != null) {
				query.append(" AND followingUserId = ?");
			}
			if(model.getFollowDate() != null) {
				query.append(" AND followDate = ?");
			}
			
		}
		
		// Prepare the statement
		PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
		int nextArgPointer = 1;
		
		// Bind the parameters
		if(isLike) {
			
			if(model.getFollowedUserId() != null) {
				preparedStatement.setString(nextArgPointer++, "%" + model.getFollowedUserId() + "%");
			}
			if(model.getFollowingUserId() != null) {
				preparedStatement.setString(nextArgPointer++, "%" + model.getFollowingUserId() + "%");
			}
			if(model.getFollowDate() != null) {
				preparedStatement.setString(nextArgPointer++, "%" + model.getFollowDate().toString() + "%");
			}
			
		} else {
			
			if(model.getFollowedUserId() != null) {
				preparedStatement.setString(nextArgPointer++, model.getFollowedUserId());
			}
			if(model.getFollowingUserId() != null) {
				preparedStatement.setString(nextArgPointer++, model.getFollowingUserId());
			}
			if(model.getFollowDate() != null) {
				preparedStatement.setString(nextArgPointer++, model.getFollowDate().toString());
			}
			
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
