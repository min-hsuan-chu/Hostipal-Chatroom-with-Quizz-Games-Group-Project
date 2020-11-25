package GUI;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.*;
import client.Client;
import component.Chatroom;
import component.Conversation;
import component.Question;
import component.User;
public class HospitalChatFrame {
//
	private JPanel contentPane;
	private Loginframe loginPanel;
	private Register registerPanel;
	private ChatLobby chatLobbyPanel;
	private ChatRoom chatroomPanel;
	private QuizGame quizgamePanel;
	private Client client;
	
	private void displayGUI(){
	
	    client = new Client("localhost", 51212);
	    client.setGUI(this);
	    
	    JFrame frame = new JFrame("Hospital Chat");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JPanel contentPane = new JPanel();
	    contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	    contentPane.setLayout(new CardLayout());
	    loginPanel = new Loginframe(contentPane, client);
	    registerPanel = new Register(contentPane, client);
	    chatLobbyPanel = new ChatLobby(contentPane, client);
	    chatroomPanel = new ChatRoom(contentPane, client);
	    quizgamePanel = new QuizGame(contentPane, client);
	    contentPane.add(loginPanel, "Login");
	    contentPane.add(registerPanel, "Register");
	    contentPane.add(chatLobbyPanel, "Chatlobby");
	    contentPane.add(chatroomPanel, "Chatroom");
	    contentPane.add(quizgamePanel, "Quizgame");
	    
	    frame.setContentPane(contentPane);
	    frame.setBounds(500,100,370,600);
	    frame.setResizable(true);
	    frame.pack();
	    frame.setLocationByPlatform(true);
	    frame.setVisible(true);
	}
	
	/*
	 * Methods which will be called when server has sent some message to client.
	 * ClientController will determine which method to be called based on type in message.
	 */
	public void receiveLoginResult(boolean isSuccess, String errorMsg, User loggedInUser) {
		client.setThisUser(loggedInUser);
		loginPanel.receiveLoginResult(isSuccess, errorMsg);
		
	}
	
	public void receiveRegisterResult(boolean isSuccess, String errorMsg) {
		registerPanel.receiveRegisterResult(isSuccess, errorMsg);
	}
	
	public void receiveOnlineUserList(Set<User> userSet) {
		chatLobbyPanel.receiveOnlineUserList(userSet);
	}
	
	public void receiveCreateChatroomResult(Chatroom room) {
		client.setRoomOfThisUser(room);
		chatLobbyPanel.receiveCreateChatroomResult();
	}
	
	public void receiveUpdateAvailability(Set<User> userSet) {
		chatLobbyPanel.receiveUpdateAvailability(userSet);
	}
	
	public void receiveConversation(Conversation conv) {
		chatroomPanel.receiveConversation(conv);
	}
	
	public void receiveUpdateOnlineUserList(User user) {
		chatLobbyPanel.receiveUpdateOnlineUserList(user);
	}
	
	public void receiveCreateGameResult(int gameRecordId) {
		chatroomPanel.receiveCreateGameResult(gameRecordId);
	}
	
	
	public void receiveRandomQuestions(ArrayList<Question> questionList) {
		quizgamePanel.receiveRandomQuestions(questionList);
		
	}
	
	public void receiveRecordGameResult(boolean isSuccess) {
		quizgamePanel.receiveRecordGameResult(isSuccess);
	}

	public static void main(String... args)
	{
	    SwingUtilities.invokeLater(new Runnable()
	    {
	        public void run()
	        {
	            new HospitalChatFrame().displayGUI();
	        }
	    });
	}
}
