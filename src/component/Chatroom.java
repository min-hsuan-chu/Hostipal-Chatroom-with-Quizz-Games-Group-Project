package component;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Chatroom implements Comparable<Chatroom>, Serializable {
	
	private static final long serialVersionUID = 60102L;
	
	private int id;
	private Set<User> users;
	private List<Conversation> conversationHistory;
	
	public Chatroom() {
		this.id = 0;
		this.users = new HashSet<User>();
	}
	
	public Chatroom(int id, Set<User> users) {
		this.id = id;
		this.users = users;
	}
	
	public int getId() {
		return id;
	}
	
	public Set<User> getUserSet() {
		return users;
	}
	
	public List<Conversation> getConversationHistory() {
		return conversationHistory;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setUserSet(Set<User> users) {
		this.users = users;
	}
	
	public void setConversationHistory(List<Conversation> conversationHistory) {
		this.conversationHistory = conversationHistory;
	}
	
	@Override
	public String toString() {
		String format = "-- Chatroom -- \n" + 
						"Id: %d \n" +
				        "Users: %s";
		return String.format(format, id, users);
	}
	
	@Override
    public int compareTo(Chatroom other) {
    	return this.getId() - other.getId();
    }

}
