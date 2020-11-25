package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import component.Chatroom;
import component.User;
/**
 * ChatroomDAO is the class specifically used for access the database and execute SQL 
 * on all chatroom related tables.
 * @author King Yin Kenneth Chen
 * @version 2020-02-29
 */
public class ChatroomDAO {
	private DBConnection dbc;
	private Chatroom chatroom;

	public ChatroomDAO(Chatroom chatroom, DBConnection dbc) {
		this.chatroom = chatroom;
		this.dbc = dbc;
	}
	
	public void setChatroom(Chatroom chatroom) {
		this.chatroom = chatroom;
	}
	
	/**
	 * Check if the chatroom has already created based on the combination of user in user set
	 * @return Found chatroom id if exists, 0 if not exists
	 */
	public int checkChatroomExist() {
		Connection conn = dbc.getConnection();
		
		int chatroomId = 0;
		
		String sql = "SELECT chatroom_id FROM \n" + 
					"(SELECT chatroom_id, COUNT(*) FROM \n" + 
					"(SELECT * FROM chatroom_user \n" + 
					"WHERE chatroom_id IN (\n" + 
					"SELECT chatroom_id FROM\n" + 
					"(SELECT chatroom_id, COUNT(*) FROM chatroom_user \n" + 
					"GROUP BY chatroom_id) a\n" + 
					"WHERE count = ?) AND user_id IN (?)) b\n" + 
					"GROUP BY b.chatroom_id) c\n" + 
					"WHERE count = ?";
		
		try {
			
			Set<User> users = chatroom.getUserSet();
			int userCount = users.size();
			Set<Integer> userIds = new TreeSet<Integer>();
			users.forEach(u -> {
				userIds.add(u.getId());
			});
			
			String sqlIN = userIds.stream()
					.map(x -> String.valueOf(x))
					.collect(Collectors.joining(",", "(", ")"));
					
		    sql = sql.replace("(?)", sqlIN);
			
		    PreparedStatement checkStatement = conn.prepareStatement(sql);
			checkStatement.setInt(1, userCount);
			checkStatement.setInt(2, userCount);
			ResultSet rs = checkStatement.executeQuery();
			
			while (rs.next())
			{
				chatroomId = rs.getInt("chatroom_id");
			}
		}
		catch (SQLException e) {
			System.out.println("Error occurred in checkChatroomExist()");
			e.printStackTrace();
		}
		return chatroomId;
		
	}
	
	/**
	 * Get all users of a chatroom and retrun a Chatroom object with its id and user set.
	 * @return Chatroom object with its id and user set
	 */
	public Chatroom getChatroomDetails() {
		Connection conn = dbc.getConnection();
		
		Chatroom crTemp = new Chatroom();
		Set<User> users = new HashSet<User>();
		
		String sql = "SELECT u.user_id, u.username, u.role_id \n" + 
					"FROM \n" + 
					"(\n" + 
					"	chatroom_user cu JOIN \"user\" u ON (cu.user_id = u.user_id)\n" + 
					")\n" + 
					"WHERE cu.chatroom_id = ?";
		
		try {
			PreparedStatement selectStatement = conn.prepareStatement(sql);
			selectStatement.setInt(1, chatroom.getId());
			ResultSet rs = selectStatement.executeQuery();
			while (rs.next())
			{
				User user = new User();
				user.setId(rs.getInt("user_id"));
				user.setUsername(rs.getString("username"));
				user.setRole(rs.getInt("role_id"));
				users.add(user);
			}
			
			crTemp.setId(chatroom.getId());
			crTemp.setUserSet(users);
		}
		catch (SQLException e) {
			System.out.println("Error occurred in getChatroomDetails()");
			e.printStackTrace();
		}
		return crTemp;
	}
	
	/**
	 * Insert a new chatroom into chatroom table and 
	 * insert chatroom-user pair into chatroom_user table
	 * @return The id of the new chatroom, 0 if failed to insert
	 */
	public int insertNewChatroom() {
		Connection conn = dbc.getConnection();
		
		String sql = "INSERT INTO \"chatroom\" (create_time) VALUES (?) RETURNING chatroom_id";
		String sql2 = "INSERT INTO \"chatroom_user\" (chatroom_id, user_id) VALUES (?, ?)";
		
		int newChatroomId = 0;
		try 
		{
			PreparedStatement insertStatement = conn.prepareStatement(sql);
			
			Date currentDate = new Date();
			Timestamp currentTs = new Timestamp(currentDate.getTime());
			insertStatement.setTimestamp(1, currentTs);
			
			ResultSet rs = insertStatement.executeQuery();
			
			while (rs.next()) {
				newChatroomId = rs.getInt(1);
			}
			System.out.println("Insert new chatroom "+ newChatroomId +" success!");
			
			Set<User> users = chatroom.getUserSet();
			for (User u : users) {
				PreparedStatement insertStatement2 = conn.prepareStatement(sql2);
				insertStatement2.setInt(1, newChatroomId);
				insertStatement2.setInt(2, u.getId());
				insertStatement2.executeUpdate();
			}
			System.out.println("Insert new chatroom_user record success!");
		}
		catch (SQLException e)
		{
			System.out.println("Error occurred in insertNewChatroom()");
			e.printStackTrace();
		}
		return newChatroomId;
	}
	
}
