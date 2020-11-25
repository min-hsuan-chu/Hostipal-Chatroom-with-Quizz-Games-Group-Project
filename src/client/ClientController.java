package client;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


import GUI.HospitalChatFrame;
import component.Chatroom;
import component.Conversation;
import component.Message;
import component.Question;
import component.User;


public class ClientController {

	private Client client;

	public ClientController() {
		this.client = null;
	}
	public ClientController(Client client) {
		this.client = client;
	}

	/**
	 * Get message from server and pass it to GUI's different method based on type
	 * @param message Message object from server
	 */
	public void receiveMsg(Message message) {
		
		// Get GUI object in client object
		HospitalChatFrame gui = client.getGUI();

		String type = message.getType();
		boolean isSuccess = message.getIsSuccess();
		String content = message.getContent();
		Set<User> userSet = message.getUserSet();
		User user = message.getUser();
		Chatroom room = message.getChatroom();
		Conversation conv = message.getConversation();
		int gameRecordId = message.getGameRecordId();
		ArrayList<Question> questionList = message.getQuestionList();
		List<Conversation> convHistory = message.getConversationsHistory();
		
		switch (type) {
			case "login":
				gui.receiveLoginResult(isSuccess, content, user);
				break;

			case "register":
				gui.receiveRegisterResult(isSuccess, content);
				break;

			case "createchatroom":
				if (isSuccess) {
					gui.receiveCreateChatroomResult(room);
				}
				break;
				
			case "updateavailability":
				if (isSuccess) 
					gui.receiveUpdateAvailability(userSet);
				break;
				
			case "updateonlineuserlist":
				if (isSuccess && user != null) {
					gui.receiveUpdateOnlineUserList(user);
				}
				break;

			case "receiveconversation":
				if (isSuccess) {
					gui.receiveConversation(conv);
				}
				break;
				
			case "getonlineuserlist":
				if (isSuccess)
					gui.receiveOnlineUserList(userSet);
				break;
			
				//
//			case "logout":
//				if (isSuccess) {
//					gui.receiveLogout(content);
//					this.client.close();
//				}
//				break;
				
//			case "gethistory":
//				if (isSuccess)
//					gui.receiveHistory(convHistory);
//				break;
				
//			case "leavechatroom":
//				if (isSuccess) {
//					gui.receiveLeaveChatroom();
//				}
//				break;
				
			case "creategame":
				if (isSuccess)
					gui.receiveCreateGameResult(gameRecordId);
				break;
				
			case "getquestions":
				if (isSuccess)
					gui.receiveRandomQuestions(questionList);
				break;
				
			case "gameresult":
				gui.receiveRecordGameResult(isSuccess);
				break;

			default:
				break;
		}
	}

	public void login(String username, String password) {
		String type = "login";

		Date currentDate = new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());

		if (!client.isConnected()) {
			client.connect();
		}

		String loginContentFormat = "username=%s;password=%s";
		String loginContent = String.format(loginContentFormat, username, password);

		Message msg = new Message(type, loginContent, currentTs);

		try {
			if (client != null) {
				// Send message to server
				client.getToServer().writeObject(msg);
			}
			else
				System.out.println("Client : login : Client object is null.");
		} catch (IOException e) {
			System.out.println("Client : login : Error occurred in login()");
			e.printStackTrace();
		}

	}

	public void accountRegister(String username, String password, String checkPassword, int role) {
		String type = "register";

		Date currentDate = new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());

		if (!client.isConnected()) {
			client.connect();
		}

		String registerContentFormat = "username=%s;password=%s;checkPassword=%s;role=%d";
		String registerContent = String.format(registerContentFormat, username, password, checkPassword, role);

		Message msg = new Message(type, registerContent, currentTs);

		try {
			if (client != null) {
				// Send message to server
				client.getToServer().writeObject(msg);
			}
			else
				System.out.println("Client : accountRegister : Client object is null.");
		} catch (IOException e) {
			System.out.println("Client : accountRegister : Error occurred in accountRegister()");
			e.printStackTrace();
		}

	}

	public void createChatroom(Set<User> users) {
		String type = "createchatroom";

		Date currentDate = new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());

		if (!client.isConnected()) {
			client.connect();
		}

		Message msg = new Message(type, users, currentTs);
		try {
			if (client != null) {
				// Send message to server
				client.getToServer().writeObject(msg);
			}
			else
				System.out.println("Client : createChatroom : Client object is null.");
		} catch (IOException e) {
			System.out.println("Client : createChatroom : Error occurred in createChatroom()");
			e.printStackTrace();
		}
	}

	public void sendConversation(int roomId, String conversation, User thisUser) {
		String type = "sendconversation";

		Date currentDate = new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());

		if (!client.isConnected()) {
			client.connect();
		}

		Message msg = new Message(type, roomId, conversation, thisUser, currentTs);
		try {
			if (client != null) {
				// Send message to server
				client.getToServer().writeObject(msg);
			}
			else
				System.out.println("Client : sendConversation : Client object is null.");
		} catch (IOException e) {
			System.out.println("Client : sendConversation : Error occurred in sendConversation()");
			e.printStackTrace();
		}
	}
	
	public void getOnlineUserList() {
		String type = "getonlineuserlist";
		
		Date currentDate = new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());
		
		if (!client.isConnected()) {
			client.connect();
		}
		
		Message msg = new Message(type, currentTs);
		try {
			if (client != null) {
				// Send message to server
				client.getToServer().writeObject(msg);
			}
			else 
				System.out.println("Client : getOnlineUserList : Client object is null.");
		} catch (IOException e) {
			System.out.println("Client : getOnlineUserList : Error occurred in getOnlineUserList()");
			e.printStackTrace();
		}
	}

	public void logout(User user){
		String type = "logout";

		Date currentDate= new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());

		if (!client.isConnected()) {
			client.connect();
		}
		
		Message msg = new Message(type, currentTs);
		msg.setUser(user);
		try {
			if (client != null) {
				// Send message to server
				client.getToServer().writeObject(msg);
			}
			else
				System.out.println("Client : logout : Client object is null.");
		} catch (IOException e) {
			System.out.println("Client : logout : Error occurred in logout()");
			e.printStackTrace();
		}

	}

	public void getHistory(int roomId){
		String type = "gethistory";

		Date currentDate = new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());

		if (!client.isConnected()) {
			client.connect();
		}

		Message msg = new Message(type, roomId, currentTs);
		try {
			if (client != null) {
				// Send message to server
				client.getToServer().writeObject(msg);
			}
			else
				System.out.println("Client : getHistory : Client object is null.");
		} catch (IOException e) {
			System.out.println("Client : getHistory : Error occurred in getHistory()");
			e.printStackTrace();
		}
	}

	public void leaveChatroom(Chatroom room){
		String type = "leavechatroom";

		Date currentDate = new Date();
		Timestamp currentTs = new Timestamp(currentDate.getTime());

		if (!client.isConnected()) {
			client.connect();
		}

		Message msg = new Message(type, currentTs);
		msg.setChatroom(room);
		try {
			if (client != null) {
				// Send message to server
				client.getToServer().writeObject(msg);
			}
			else
				System.out.println("Client : leaveChatroom : Client object is null.");
		} catch (IOException e) {
			System.out.println("Client : leaveChatroom : Error occurred in leaveChatroom()");
			e.printStackTrace();
		}
	}
	
	/**
	 * Create a new game record under the specific room id
	 * @param roomId The chatroom id
	 */
	public void createGame(int roomId) {
		// TODO: Create a message object containing type = "creategame", roomId, timestamp 
		//       and send to server
		
		
		
		
		
	}
	
	/**
	 * Get questions from db randomly
	 */
	public void getRandomQuestions() {
		// TODO: Create a message object containing type = "getquestions", timestamp
		//       and send to server
		
		
		
		
	}
	
	public void sendGameResult() {
		// TODO: Create a message object containing type = "gameresult", timestamp, score, userId, gameRecordId
		//       and send to server
		
		
		
		
		
	}

}
