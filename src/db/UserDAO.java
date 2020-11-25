package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import component.User;

/**
 * UserDAO is the class specifically used for access the database and execute SQL 
 * on User table.
 * @author King Yin Kenneth Chen
 * @version 2020-02-29
 */

public class UserDAO {
	
	private DBConnection dbc;
	private User user;

	public UserDAO(User user, DBConnection dbc) {
		this.user = user;
		this.dbc = dbc;
	}
	
	public boolean checkUserNotExist() {
		Connection conn = dbc.getConnection();
		
		String sql = "SELECT COUNT(*) FROM \"user\" WHERE username = ?";
		
		try {
			PreparedStatement checkStatement = conn.prepareStatement(sql);
			checkStatement.setString(1, user.getUsername());
			ResultSet rs = checkStatement.executeQuery();
			int count = 0;
			while (rs.next())
			{
				count = rs.getInt("COUNT");
			}
			if (count == 0) {
				// Username not exist
				return true;
			}
		}
		catch (SQLException e) {
			System.out.println("Error occurred in checkUserNotExist()");
			e.printStackTrace();
			return false;
		}
		return false;
		
	}
	
	/**
	 * insertNewUser is used to insert the user into user table
	 * @return True or false (True if successfully inserted; false if not)
	 */
	public boolean insertNewUser() {
		
		Connection conn = dbc.getConnection();
		
		String sql = "INSERT INTO \"user\" (username, password, role_id, create_time, last_update_time) VALUES (?, ?, ?, ?, ?)";
		
		try 
		{
			
			PreparedStatement insertStatement = conn.prepareStatement(sql);
			insertStatement.setString(1, user.getUsername());
			insertStatement.setString(2, user.getPassword());
			insertStatement.setInt(3,  user.getRole());
			
			Date currentDate = new Date();
			Timestamp currentTs = new Timestamp(currentDate.getTime());
			insertStatement.setTimestamp(4, currentTs);
			insertStatement.setTimestamp(5, currentTs);
			
			insertStatement.executeUpdate();
			System.out.println("Insert new user success!");
			return true;
		}
		catch (SQLException e)
		{
			System.out.println("Error occurred in insertNewUser()");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * checkUserExistAndPw is used to check whether the user exist in database 
	 * and return that user as result
	 * @return The user found in table as User object
	 */
	public User checkUserExistAndPw() {

		Connection conn = dbc.getConnection();
		
		User theUser = new User();
		
		String sql = "SELECT user_id, username, role_id FROM \"user\" WHERE username = ? AND password = ? LIMIT 1";
		
		try 
		{
			PreparedStatement selectStatement = conn.prepareStatement(sql);
			selectStatement.setString(1, user.getUsername());
			selectStatement.setString(2, user.getPassword());
			
			ResultSet rs = selectStatement.executeQuery();
			int count = 0;
			while (rs.next()) {
				count++;
				theUser.setId(rs.getInt("user_id"));
				theUser.setUsername(rs.getString("username"));
				theUser.setRole(rs.getInt("role_id"));
			}
			if (count == 0) {
				System.out.println("User cannot be found in table or password is not correct.");
			}
			System.out.println("Check user in table success!");
		}
		catch (SQLException e)
		{
			System.out.println("Error occurred in checkUserExistAndPw()");
			e.printStackTrace();
		}
		
		return theUser;
	}

}
