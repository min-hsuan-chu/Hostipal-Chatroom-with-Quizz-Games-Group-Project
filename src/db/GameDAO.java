package db;

import java.util.ArrayList;

import component.Question;

/**
 * GameDAO is the class specifically used for access the database and execute SQL 
 * on all quiz game related tables.
 * @author Porto
 * @version 2020-02-29
 */
public class GameDAO {
	private DBConnection dbc;
	private int chatroomId;
	private int recordId;
	private int userId;
	private int score;
	
	public GameDAO(DBConnection dbc) {
		this.dbc = dbc;
	}
	
	public void setChatroomId(int chatroomId) {
		this.chatroomId = chatroomId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Insert a new game record in game_records table and 
	 * insert a new chatroom-game association
	 * @return Game record ID ( return 0 if insertion failed )
	 */
	public int insertNewGameRecord() {
		
		int recordId = 0;
		
		// Write SQL query for inserting a new row in game_records table
		// then execute the query
		
		
		
		
		// Write SQL query for inserting a new row in chatroom_game table
		// then execute the query
		
		
		
		
		// return true if the all queries successfully done
		// return false if any of the queries failed
		
		
		return recordId;
	}
	
	/**
	 * Get a certain number of questions randomly from question table
	 * @return ArrayList of Question object
	 */
	public ArrayList<Question> getRandomQuestions() {
		
		ArrayList<Question> questionList = new ArrayList<Question>();
		
		// Write SQL query for getting 5 questions RANDOMLY in question table
		// and return an ArrayList of Question object 
		
		
		
		
		
		return questionList;
	}
	
	/**
	 * Record users' game playing result in game_user table
	 * @return boolean result (true if success; false if failed)
	 */
	public boolean recordGameResult() {
		
		// Write SQL query for inserting a new record in game_user table (record_id, user_id, score, create_time)
		// then execute the query
		
		
		
		
		return true;
	}
	
}
