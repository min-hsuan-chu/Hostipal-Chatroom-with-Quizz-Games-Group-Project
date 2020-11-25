package component;

import java.io.Serializable;
import java.sql.Timestamp;

public class Conversation implements Serializable {
	
	private static final long serialVersionUID = 98505L;
	
	private int id;
	private int chatroomId;
	private int userId;
	private String username;
	private String content;
	private Timestamp createTime;

	public Conversation(int chatroomId, int userId, String username, String content, Timestamp createTime) {
		this.chatroomId = chatroomId;
		this.userId = userId;
		this.username = username;
		this.content = content;
		this.createTime = createTime;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getChatroomId() {
		return chatroomId;
	}

	public void setChatroomId(int chatroomId) {
		this.chatroomId = chatroomId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
}
