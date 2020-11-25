package component;

import java.io.Serializable;

/**
 * User is the class for storing and manipulating information of a user of this chat system.
 *
 */
public class User implements Comparable<User>, Serializable {
	
	private static final long serialVersionUID = 49217L;
	
	private int id;
	private String username;
	private String password;
	private int role;
	private boolean isAvailable = true;
	private boolean isSelected = false;
	
	public User() {
		this.username = "";
		this.password = "";
		this.role = -1;
	}
	
	public User(String username, String password, int role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	public int getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getRole() {
		return role;
	}
	
	public boolean getIsAvailable() {
		return isAvailable;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setRole(int role) {
		this.role = role;
	}
	
	public void setIsAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	public boolean getIsSelected() {
	    return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public boolean isValid() {
		if (username == "" || password == "" || role == -1) {
			return false;
		}
		return true;
	}
	
	public boolean isValidForLogin() {
		if (username == "" || password == "") {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		String format = "-- User -- \n" + 
						"Username: %s \n" +
				        "Password: %s \n" +
						"Role: %s \n"
						+ "IsAvailable: %b";
		return String.format(format, username, password, role, isAvailable);
	}
	
	@Override
    public int compareTo(User other) {
    	return this.getUsername().compareTo(other.getUsername());
    }
	
}
