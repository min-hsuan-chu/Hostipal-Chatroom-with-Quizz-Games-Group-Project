package component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Message class for communication between server and client.
 *
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 20258L;

	private String type;
	private String content;
	private Timestamp timestamp;
	private boolean isSuccess;
	
	private User user;
	private Set<User> userSet;
	
	private Chatroom chatroom;
	private List<Conversation> conversationsHistory;//
	private ArrayList<Question> questionList;
	
	private Conversation conversation;
	
	private int roomId;
	
	private int gameRecordId;
	private int score;

	/**
	 * Constructor for empty message
	 */
	public Message() {
		this.type = "";
	}
	
	/**
	 * Constructor for simple message with only type and timestamp
	 * @param type Message type
	 * @param timestamp Timestamp of message
	 */
	public Message(String type, Timestamp timestamp) {
		this.type = type;
		this.timestamp = timestamp;
	}
	
	/**
	 * Constructor for general message
	 * @param type Message type
	 * @param content Content of message in string
	 * @param timestamp Timestamp of message
	 */
	public Message(String type, String content, Timestamp timestamp) {
		this.type = type;
		this.content = content;
		this.timestamp = timestamp;
	}
	
	/**
	 * Constructor for message reply from server indicating result of operation
	 * @param type Message type
	 * @param isSuccess Boolean result of operation replied by server
	 * @param content Result of operation in string
	 * @param timestamp Timestamp of message
	 */
	public Message(String type, boolean isSuccess, String content, Timestamp timestamp) {
		this.type = type;
		this.content = content;
		this.isSuccess = isSuccess;
		this.timestamp = timestamp;
	}
	
	/**
	 * Constructor for message regarding create chatroom
	 * @param type Message type
	 * @param userSet A set of user
	 * @param timestamp Timestamp of message
	 */
	public Message(String type, Set<User> userSet, Timestamp timestamp) {
		this.type = type;
		this.userSet = userSet;
		this.timestamp = timestamp;
	}
	
	/**
	 * Constructor for message regarding send conversation
	 * @param type Message type
	 * @param roomId Chatroom Id
	 * @param content Conversation to send
	 * @param timestamp Timestamp of message
	 */
	public Message(String type, int roomId, String content, User thisUser, Timestamp timestamp) {
		this.type = type;
		this.roomId = roomId;
		this.content = content;
		this.user = thisUser;
		this.timestamp = timestamp;
	}

	/**
	 * Constructor for message regarding get history
	 * @param type Message type
	 * @param roomId Chatroom Id
	 * @param timestamp Timestamp of message
	 */
	public Message(String type, int roomId, Timestamp timestamp) {
		this.type = type;
		this.roomId = roomId;
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public String getContent() {
		return content;
	}
  
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	public boolean getIsSuccess() {
		return isSuccess;
	}
	
	public User getUser() {
		return user;
	}
	
	public Set<User> getUserSet() {
		return userSet;
	}
	
	public Chatroom getChatroom() {
		return chatroom;
	}
	
	public int getRoomId() {
		return roomId;
	}
	
	public Conversation getConversation() {
		return conversation;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setUserSet(Set<User> userSet) {
		this.userSet = userSet;
	}
	
	public void setChatroom(Chatroom chatroom) {
		this.chatroom = chatroom;
	}
	
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}
	
	public List<Conversation> getConversationsHistory() {
		return conversationsHistory;
	}

	public void setConversationsHistory(List<Conversation> conversationsHistory) {
		this.conversationsHistory = conversationsHistory;
	}
	
	public int getGameRecordId() {
		return gameRecordId;
	}

	public void setGameRecordId(int gameRecordId) {
		this.gameRecordId = gameRecordId;
	}

	public ArrayList<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(ArrayList<Question> questionList) {
		this.questionList = questionList;
	}
	

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		String format = "-- Message -- \n" + 
						"Type: %s \n" +
				        "Timestamp: %s \n" +
						"Content: %s \n" + 
				        (user != null ? "User: "+user+" \n" : "") +
//				        (!userSet.isEmpty() ? "User Set: "+userSet+" \n" : "") +
//				        "Room Id: "+ roomId +" \n" +
				        "isSuccess: %b";
		return String.format(format, type, timestamp, content, isSuccess);
	}


}
