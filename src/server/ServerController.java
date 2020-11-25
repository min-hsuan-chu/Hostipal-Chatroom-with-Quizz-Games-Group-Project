package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.*;
import component.Chatroom;
import component.Conversation;
import component.Message;
import component.Question;
import component.User;
import db.ChatroomDAO;
import db.ConversationDAO;
import db.DBConnection;
import db.GameDAO;
import db.UserDAO;
/**
 * ServerController class contains all the required 
 * functional method on server side for this chat system.
 * The method here will request database for SQL execution via xxxDAO class.
 * @author porto
 *
 */
public class ServerController {

	private Server server;

	public ServerController(Server server) {
		this.server = server;
	}

	public Message receiveMsg(Message message) {

		Message resultMsg = null;

		String type = message.getType().toLowerCase();
		String content = message.getContent();
//		Timestamp timestamp = message.getTimestamp();
		Set<User> userSet = message.getUserSet();
		Chatroom chatroom = message.getChatroom();
		int roomId = message.getRoomId();
		User user = message.getUser();
		int gameRecordId = message.getGameRecordId();
		int score = message.getScore();
		

		switch (type) {
			case "register":
				resultMsg = register(content);
				resultMsg.setType(type);
				break;

			case "login":
				resultMsg = login(content);
				resultMsg.setType(type);
				break;

			case "createchatroom":
				createChatroom(userSet);
				break;

			case "sendconversation":
				sendConversation(roomId, content, user);
				break;
				
			case "getonlineuserlist":
				resultMsg = getOnlineUserList();
				resultMsg.setType(type);
				break;
				
			case "gethistory":
				resultMsg = getHistory(roomId);
				resultMsg.setType(type);
				break;
				
			case "logout":
				resultMsg=logout(user);
				resultMsg.setType(type);
				break;
				
			case "leaveChatroom":
				leaveChatroom(chatroom);
				break;
				
			case "creategame":
				resultMsg = createGame(roomId);
				resultMsg.setType(type);
				break;
				
			case "getquestions":
				resultMsg = getRandomQuestions();
				resultMsg.setType(type);
				break;
				
			case "gameresult":
				resultMsg = gameResult(gameRecordId, user, score);
				resultMsg.setType(type);
				break;
				
				
			default:
				break;
		}

		return resultMsg;
	}

	/**
	 * Method to send login request to database for validating the user
	 *
	 * @param content String of specific format containing username and password
	 * @return Message containing result and the user object
	 */
	public Message login(String content) {
		// Initiate User object
		User user = new User();
		// Initiate Message object for return as result
		Message resultMsg = new Message();

		// Extract content and convert it into key-value pairs stored in Map
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo = extractContent(content);

		// Set user's info to user object
		if (userInfo.containsKey("username") && userInfo.containsKey("password")) {
			user.setUsername(userInfo.get("username"));
			user.setPassword(userInfo.get("password"));
		} else {
			System.out.println("ServerController : login : User doesn't contain username or password.");
		}

		// Check if user already login
		Set<User> onlineUsers = server.getOnlineUsers();
		for (User u : onlineUsers) {
			// Check users are the same one by checking username
			if (u.compareTo(user) == 0) {
				resultMsg.setIsSuccess(false);
				resultMsg.setContent("You are already logged in.");
				Date currentDate = new Date();
				Timestamp currentTs = new Timestamp(currentDate.getTime());
				resultMsg.setTimestamp(currentTs);
				return resultMsg;
			}
		}

		User theUser = new User();
		// Create DAO for checking info into database
		if (user.isValidForLogin()) {
			DBConnection dbc = new DBConnection();
			UserDAO userDAO = new UserDAO(user, dbc);
			theUser = userDAO.checkUserExistAndPw();
			if (theUser != null && !theUser.getUsername().isEmpty() && theUser.getRole() != -1) {
				resultMsg.setIsSuccess(true);
				resultMsg.setContent("SUCCESS!");
				resultMsg.setUser(theUser);
				server.addUserToOnlineList(theUser);
			} else {
				resultMsg.setContent("Username and/or password is incorrect. Please enter again.");
				resultMsg.setIsSuccess(false);
			}
			// Close db connection
			dbc.close();

		} else {
			System.out.println("ServerController : login : User object is not valid.");
		}
		
		// Send message to client to notify GUI update online user list
		Map<String, ServerThread> threads = server.getThreads();
		String username = "";
		ServerThread st = null;
		ObjectOutputStream out = null;
		Message msgToClient = new Message();
		msgToClient.setType("updateonlineuserlist");
		msgToClient.setUser(theUser);
		
		Date currentDate = new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());
		
		msgToClient.setTimestamp(currentTs);
		msgToClient.setIsSuccess(true);
		for (User u : onlineUsers) {
			if (u.compareTo(user) != 0) {
				username = u.getUsername();
				st = threads.get(username);
				out = st.getToClient();
				try {
					out.writeObject(msgToClient);
				} catch (IOException e) {
					System.out.println("ServerController : createChatroom : Error in writing object to online users.");
					e.printStackTrace();
				}
			}
		}

		resultMsg.setTimestamp(currentTs);

		return resultMsg;

	}

	/**
	 * Method to send register request to database for inserting a new user record
	 *
	 * @param content String of specific format containing username, password, checkPassword and role
	 * @return Message object containing result of insertion
	 */
	public Message register(String content) {
		// Initiate User object
		User user = new User();
		// Initiate Message object for return as result
		Message resultMsg = new Message();
		resultMsg.setIsSuccess(false);

		// Extract content and convert it into key-value pairs stored in Map
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo = extractContent(content);

		// Set user's info to user object
		if (userInfo.get("password").compareTo(userInfo.get("checkPassword")) == 0) {
			if (userInfo.containsKey("username") && userInfo.containsKey("password") &&
					userInfo.containsKey("role")) {
				user.setUsername(userInfo.get("username"));
				user.setPassword(userInfo.get("password"));
				user.setRole(Integer.parseInt(userInfo.get("role")));

				// Create DAO for inserting info into database
				if (user.isValid()) {
					DBConnection dbc = new DBConnection();
					UserDAO userDAO = new UserDAO(user, dbc);
					if (userDAO.checkUserNotExist()) {
						resultMsg.setIsSuccess(userDAO.insertNewUser());
					} else {
						resultMsg.setContent("Username has already been used.");
					}

					// Close db connection
					dbc.close();

				} else {
					System.out.println("ServerController : register : User object is not valid.");
				}
			} else {
				System.out.println("ServerController : register : User doesn't contain username, password or role.");
			}
		} else {
			resultMsg.setContent("Password and checkPassword doesn't match.");
			System.out.println("ServerController : register : password and checkPassword doesn't match.");
		}

		Date currentDate = new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());
		resultMsg.setTimestamp(currentTs);

		return resultMsg;
	}

	/**
	 * Initialise a chatroom by adding users into chatroom
	 *
	 * @param userSet User set to be added into chatroom
	 * @return Message object with chatroom details
	 */
	public synchronized void createChatroom(Set<User> userSet) {
		
		// Get current date time
		Date currentDate = new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());
		
		Set<User> onlineUserList = server.getOnlineUsers();
		
		List<String> usernameList = new ArrayList<String>();
		Set<User> userSetTemp = new HashSet<User>();
		for (User user : userSet) {
			usernameList.add(user.getUsername());
			user.setIsAvailable(false);
			userSetTemp.add(user);
		}
		userSet = userSetTemp;
		
		// Check if user set contains any currently not available user
		boolean available = true;
		for (User ou : onlineUserList) {
			if (usernameList.contains(ou.getUsername())) {
				if (!ou.getIsAvailable()) {
					available = false;
					break;
				}
			}
		}
		
		if (available) {
			Chatroom cr = new Chatroom();
			// This chatroom object has id = 0, check exist based on combination of users in userSet
			cr.setUserSet(userSet);
	
			DBConnection dbc = new DBConnection();
			ChatroomDAO crDao = new ChatroomDAO(cr, dbc);
			int chatroomId = crDao.checkChatroomExist();
			if (chatroomId == 0) {
				// Chatroom not exist, can create new one
				// insertNewChatroom() will return new chatroom id
				chatroomId = crDao.insertNewChatroom();
			}
			dbc.close();
			
			if (chatroomId != 0) {
				// Add chatroom to server global variable (rooms)
				Chatroom theChatroom = new Chatroom(chatroomId, userSet);
				server.addNewChatroom(theChatroom);
				
				// Update isAvailable status (to false) of currently online user list and
				// send a Message object to the users in new chatroom to notify GUI to enter chatroom page and
				// send a Message object to other online users to notify them the update of any availability change
				Set<User> onlineUserListTemp = new HashSet<User>();
				
				Message msgToChatroomUsers = new Message();
				msgToChatroomUsers.setType("createchatroom");
				msgToChatroomUsers.setChatroom(theChatroom);
				msgToChatroomUsers.setTimestamp(currentTs);
				msgToChatroomUsers.setIsSuccess(true);
				
				Map<String, ServerThread> threads = server.getThreads();
				String username = "";
				ServerThread st = null;
				ObjectOutputStream out = null;
				for (User ou : onlineUserList) {
					username = ou.getUsername();
					st = threads.get(username);
					out = st.getToClient();
					if (usernameList.contains(ou.getUsername())) { // Users in new chatroom
						User userInRoom = ou;
						userInRoom.setIsAvailable(false);
						onlineUserListTemp.add(userInRoom);
						try {
							out.writeObject(msgToChatroomUsers);
						} catch (IOException e) {
							System.out.println("ServerController : createChatroom : Error in writing object to chatroom users.");
							e.printStackTrace();
						}
					} else {  // Other users
						onlineUserListTemp.add(ou);
					}
				}
				server.setOnlineUsers(onlineUserListTemp); // Update availability status
				
				Message msgToClient = new Message();
				msgToClient.setType("updateavailability");
//				msgToClient.setUserSet(onlineUserListTemp);  // New user list with updated availability
				msgToClient.setUserSet(userSet);
				msgToClient.setTimestamp(currentTs);
				msgToClient.setIsSuccess(true);
				
				for (User newou : onlineUserListTemp) {
					username = newou.getUsername();
					st = threads.get(username);
					out = st.getToClient();
					if (!usernameList.contains(newou.getUsername())) { // Others Users
						try {
							out.writeObject(msgToClient);
						} catch (IOException e) {
							System.out.println("ServerController : createChatroom : Error in writing object to other users.");
							e.printStackTrace();
						}
					}
				}
				
			} else {
				System.out.println("ServerController : createChatroom : Chatroom doesn't exist.");
			}
		} else {
			System.out.println("ServerController : createChatroom : Some users are already in chat with someone.");
		}
	}

	/**
	 * Method to send conversation to all users in the room but except the user who
	 * send the conversation
	 *
	 * @param roomId       Target room's id to send the conversation
	 * @param conversation String content to send
	 * @param thisUser     The user who send the conversation
	 */
	public void sendConversation(int roomId, String conversation, User thisUser) {
		// Get currently available chatroom from server global variable
		TreeSet<Chatroom> allRooms = server.getChatrooms();
		Chatroom targetRoom = new Chatroom();
		// Target room will be the chatroom object with id = roomId
		for (Chatroom cr : allRooms) {
			if (cr.getId() == roomId) {
				targetRoom = cr;
				break;
			}
		}

		// Insert conversation to db by calling ConversationDAO
		DBConnection dbc = new DBConnection();
		Date currentDate0 = new Date();
		Timestamp currentTs0 = new Timestamp(currentDate0.getTime());
		Conversation conv = new Conversation(roomId, thisUser.getId(), thisUser.getUsername(), conversation, currentTs0);//int chatroomId, int userId, String username, String content, Timestamp createTime
		ConversationDAO convDao = new ConversationDAO(conv, dbc);
		boolean insert = convDao.insertConversation();
		//
		if (insert) {
			Set<User> targetUsers = targetRoom.getUserSet();
			// Get users' thread for locating stream to send the conversation
			Map<String, ServerThread> threads = server.getThreads();
			String username = "";
			ServerThread st = null;
			ObjectOutputStream out = null;
			Message msgToClient = new Message();
			msgToClient.setType("receiveconversation");
//			msgToClient.setContent(conversation);
			msgToClient.setConversation(conv);
			msgToClient.setChatroom(targetRoom);
			Date currentDate = new Date();
			Timestamp currentTs = new Timestamp(currentDate.getTime());
			msgToClient.setTimestamp(currentTs);
			msgToClient.setIsSuccess(true);
			for (User u : targetUsers) {
				// Broadcast message to all users in the room except the user who send this conversation
				if (u.compareTo(thisUser) != 0) {
					username = u.getUsername();
					st = threads.get(username);
					out = st.getToClient();

					try {
						out.writeObject(msgToClient);
					} catch (IOException e) {
						System.out.println("ServerController : sendConversation : Error in writing object to client.");
						e.printStackTrace();
					}
				}
			}

		} else {
			System.out.println("ServerController : sendConversation : Error in insert conversation to db.");
		}
		dbc.close();
	}
	
	public Message getOnlineUserList() {
		
		Set<User> onlineUserList = server.getOnlineUsers();
		
		// Initiate Message object for return as result
		Message resultMsg = new Message();
		resultMsg.setIsSuccess(true);
		resultMsg.setUserSet(onlineUserList);
		
		Date currentDate = new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());
		resultMsg.setTimestamp(currentTs);
		
		return resultMsg;
		
	}

	/**
	 * Method to get conversation history from database and return a message object to client
	 * @param roomid The id of the room needed to retrieve conversation
	 * @return Message object with list of conversation
	 */
	public Message getHistory(int roomid) {

		Message returnMsg = new Message();
		returnMsg.setRoomId(roomid);

		DBConnection dbc = new DBConnection();
		Date currentDate0 = new Date();
		Timestamp currentTs0 = new Timestamp(currentDate0.getTime());
		ConversationDAO convDao = new ConversationDAO(roomid, dbc);
		List<Conversation> getHistory = convDao.getHistory();
		System.out.println("getHistory = " + getHistory);
		returnMsg.setConversationsHistory(getHistory);
		returnMsg.setTimestamp(currentTs0);
		dbc.close();

        return returnMsg;
	}
	
	public Message createGame(int roomId) {
		
		Message resultMsg = new Message();
		
		// Call GameDAO class methods to insert new game record in database
		// DAO will return a game record id, put the game record id in Message object
		// then return the message object (containing type = "creategame", timestamp, game record id, isSuccess)
		DBConnection dbc = new DBConnection();
		Date currentDate0 = new Date();
		Timestamp currentTs0 = new Timestamp(currentDate0.getTime());
		GameDAO gameDao = new GameDAO(dbc);
		gameDao.setChatroomId(roomId);
		int gameRecordId = gameDao.insertNewGameRecord();
		resultMsg.setGameRecordId(gameRecordId);
		resultMsg.setTimestamp(currentTs0);
		if (gameRecordId != 0)
			resultMsg.setIsSuccess(true);
		else 
			resultMsg.setIsSuccess(false);
		dbc.close();
		
		return resultMsg;
	}
	
	public Message getRandomQuestions() {
		Message resultMsg = new Message();
		
		// Call GameDAO class methods to get questions randomly in db
		// DAO will return a ArrayList<Question>, put the ArrayList<Question> in Message object
		// then return the message object (containing type = "getquestions", timestamp, ArrayList<Question>, isSuccess)
		DBConnection dbc = new DBConnection();
		Date currentDate0 = new Date();
		Timestamp currentTs0 = new Timestamp(currentDate0.getTime());
		GameDAO gameDao = new GameDAO(dbc);
		ArrayList<Question> questionList = gameDao.getRandomQuestions();
		
		if (questionList.isEmpty()) {
			resultMsg.setIsSuccess(false);
		} else {
			resultMsg.setIsSuccess(true);
			resultMsg.setQuestionList(questionList);
		}
		resultMsg.setTimestamp(currentTs0);	
		dbc.close();
		
		return resultMsg;
	}
	
	public Message gameResult(int recordId, User user, int score) {
		Message resultMsg = new Message();
		
		// Call GameDAO class methods recordGameResult() to record game result in db
		// DAO will return a boolean result, put the boolean result in Message object as isSuccess
		// then return the message object (containing type = "gameresult", timestamp, isSuccess)
		DBConnection dbc = new DBConnection();
		Date currentDate0 = new Date();
		Timestamp currentTs0 = new Timestamp(currentDate0.getTime());
		GameDAO gameDao = new GameDAO(dbc);
		int userId = user != null ? user.getId() : 0;
		gameDao.setRecordId(recordId);
		gameDao.setUserId(userId);
		gameDao.setScore(score);
		if (userId != 0) {
			resultMsg.setIsSuccess(true);
			gameDao.recordGameResult();
		} else {
			resultMsg.setIsSuccess(false);
			System.out.println("ServerController : gameResult : User id should not be 0.");
		}
		
		resultMsg.setTimestamp(currentTs0);	
		dbc.close();
				
		return resultMsg;
	}
	
	public Message logout(User user) {
		Date currentDate = new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());
		Message resultMsg = new Message();

		Set<User> onlineUsers = server.getOnlineUsers();
		Map<String, ServerThread> threads = server.getThreads();

		Set<User> onlineUserListTemp = new HashSet<User>();

		for (User u : onlineUsers) {
			if (u.compareTo(user) == 0) {
				resultMsg.setContent("You are logged out.");
			} else {
				onlineUserListTemp.add(u);
				System.out.println("user is logging out = " + u);
				//server.setOnlineUsers(onlineUsers);
			}
		}
		server.setOnlineUsers(onlineUserListTemp);
		// Remove user's thread from thread list
		server.removeThreadFromList(user.getUsername());

		resultMsg.setTimestamp(currentTs);
		resultMsg.setIsSuccess(true);
		return resultMsg;
	}

	public void leaveChatroom(Chatroom chatroom) {

		Set<User> userSet = chatroom.getUserSet();
		Date currentDate = new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());

		Set<User> onlineUsers = server.getOnlineUsers();
		Set<User> onlineUserListTemp = new HashSet<User>();
		server.removeChatroom(chatroom);
		
		List<String> usernameList = new ArrayList<String>();
		Set<User> userSetTemp = new HashSet<User>();
		for (User user : userSet) {
			usernameList.add(user.getUsername());
			user.setIsAvailable(true);
			userSetTemp.add(user);
		}
		userSet = userSetTemp;
		
		Map<String, ServerThread> threads = server.getThreads();
		String username = "";
		ServerThread st = null;
		ObjectOutputStream out = null;
		
		Message msgToClient = new Message();
		msgToClient.setType("updateavailability");
		msgToClient.setUserSet(userSet);
		msgToClient.setTimestamp(currentTs);
		msgToClient.setIsSuccess(true);
		
		Message msgToChatroomUsers = new Message();
		msgToChatroomUsers.setType("leavechatroom");
		msgToChatroomUsers.setContent("This chatroom is dismissed.");
		msgToChatroomUsers.setTimestamp(currentTs);
		msgToChatroomUsers.setIsSuccess(true);
		
		for (User ou : onlineUsers) {
			username = ou.getUsername();
			st = threads.get(username);
			out = st.getToClient();
			if (usernameList.contains(ou.getUsername())) { 
				User userOutRoom = ou;
				userOutRoom.setIsAvailable(true);
				onlineUserListTemp.add(userOutRoom);
				try {
					out.writeObject(msgToChatroomUsers);
				} catch (IOException e) {
					System.out.println("ServerController : leaveChatroom : Error in writing object to chatroom users.");
					e.printStackTrace();
				}
			} else {  // Other users
				onlineUserListTemp.add(ou);
				try {
					out.writeObject(msgToClient);
				} catch (IOException e) {
					System.out.println("ServerController : leaveChatroom : Error in writing object to chatroom users.");
					e.printStackTrace();
				}
			}
		}
		

		server.setOnlineUsers(onlineUserListTemp);

	}

	/**
	 * extractContent is used to split the content string in part to extract
	 * the information stored in the string. Then, put them into Map<String, String>.
	 * @param content Content string of message
	 * @return Map<String, String> key-value pairs of extracted information
	 */
	public Map<String, String> extractContent(String content) {
		Map<String, String> map = new HashMap<String, String>();
		
		/*
		 * Split content by ";" into array 
		 * (Example content: username=John;password=123456;checkPassword=123456)
		 */
		String[] splitContent = content.split(";");
		
		// Split each part in content by "=" to get the key and value pair
		for (int i = 0; i < splitContent.length; i++) {
			String[] extract = splitContent[i].split("=");
			if (extract.length == 2) {
				map.put(extract[0], extract[1]);
			} else {
				System.out.println("ServerController : extractContent : Incorrect format of content.");
			}
		}
		
		return map;
	}

}
