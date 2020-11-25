package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import component.Conversation;
/**
 * ConversationDAO is for inserting and getting conversation history into/from database
 * @author King Yin Kenneth Chen
 * @version 2020-03-08
 *
 */
public class ConversationDAO {
	private DBConnection dbc;
	private Conversation conv;
	private int roomId = 0;
	
	// Constructor for getting conversation history
	public ConversationDAO(int roomId, DBConnection dbc) {
		this.roomId = roomId;
		this.dbc = dbc;
	}
	
	// Constructor for inserting conversation history
	public ConversationDAO(Conversation conv, DBConnection dbc) {
		this.conv = conv;
		this.dbc = dbc;
	}
	
	public void setConversation(Conversation conv) {
		this.conv = conv;
	}
	
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	
	/**
	 * Method to get conversation history from database based on chatroom id
	 * @return A list of Conversation object
	 */
	public List<Conversation> getHistory() {
		Connection conn = dbc.getConnection();
		
		List<Conversation> convListTemp = new ArrayList<Conversation>();
		
		String sql = "SELECT conversation_id, chatroom_id, u.user_id, u.username, content, ch.create_time FROM \n" + 
				"(conversation_history AS ch \n" + 
				"JOIN \"user\" AS u ON (ch.user_id = u.user_id))\n" + 
				"WHERE ch.chatroom_id = ? \n" + 
				"ORDER BY ch.create_time ASC";
		
		try {
			if (roomId != 0) {
				PreparedStatement selectStatement = conn.prepareStatement(sql);
				selectStatement.setInt(1, roomId);
				ResultSet rs = selectStatement.executeQuery();
				
				while (rs.next())
				{
					String content = rs.getString("content");
					String username = rs.getString("username");
					Timestamp createTime = rs.getTimestamp("create_time");
					int userId = rs.getInt("user_id");
					int chatroomId = rs.getInt("chatroom_id");
					
					convListTemp.add(new Conversation(chatroomId, userId, username, content, createTime));
				}
				System.out.println("Get conversation history success!");
			} else {
				System.out.println("Error occurred in getHistory(), please specify roomId.");
			}
		}
		catch (SQLException e) {
			System.out.println("Error occurred in getHistory()");
			e.printStackTrace();
		}
		return convListTemp;
	}
	
	/**
	 * Method to insert a new conversation into database 
	 * @return Boolean result of insertion
	 */
	public boolean insertConversation() {
		Connection conn = dbc.getConnection();
		
		String sql = "INSERT INTO conversation_history (chatroom_id, user_id, content, create_time) "
				+ "VALUES (?, ?, ?, ?)";
		
		try {
			if (this.conv != null) {
				PreparedStatement insertStatement = conn.prepareStatement(sql);
				
				int chatroomId = conv.getChatroomId();
				int userId = conv.getUserId();
				String content = conv.getContent();
				Timestamp createTime = conv.getCreateTime();
				
				insertStatement.setInt(1, chatroomId);
				insertStatement.setInt(2, userId);
				insertStatement.setString(3, content);
				insertStatement.setTimestamp(4, createTime);
				
				insertStatement.executeUpdate();
				
				System.out.println("Insert new conversation success!");
				return true;
			} else {
				System.out.println("Error occurred in insertConversation(), Conversation object is null.");
			}
		}
		catch (SQLException e) {
			System.out.println("Error occurred in insertConversation()");
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
}
