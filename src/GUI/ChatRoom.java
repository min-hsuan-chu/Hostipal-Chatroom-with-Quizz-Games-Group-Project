package GUI;

import javax.swing.*;

import client.Client;
import client.ClientController;
import component.Chatroom;
import component.Conversation;
import component.User;

import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatRoom extends JPanel{
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	private static final String conversationFormat = "[%s]<%s> %s \n";
	
	private JPanel contentPane;
	private Client client;
	private ClientController ctrl;
//	static JPanel container = new JPanel();
	
    JPanel chatroompage = new JPanel();
    JButton sendMessage;
    JTextField messageBox;
    JTextArea chatBox;
//    JTextField usernameChooser;
    
//    User thisUser;
//    Chatroom room;
    
    
    // TODO: add a new button (start quiz game) 
    //       When user click this button, call controller method createGame() 
 
    
    

    public ChatRoom(JPanel contentPane, Client client) {
    	this.contentPane = contentPane;
    	this.client = client;
        //container.setLayout(null);
//    	this.thisUser = client.getThisUser();
    	this.ctrl = new ClientController(client);
//    	this.room = client.getRoomOfThisUser();
    	
    	setLayout(new BorderLayout(10, 10));
    	setBounds(250,100,900,600);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(6, 6, 900, 600);
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.yellow);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);//set column 30
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(new sendMessageButtonListener());
        chatroompage.setLayout(null);
        chatroompage.setPreferredSize(new Dimension(700, 600));

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);
//        chatBox.setPreferredSize(new Dimension(700, 600));

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER); //put chatBox in center

        GridBagConstraints left = new GridBagConstraints();
        left.gridy = 0;
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;
        
        // add messagebox and send message btn in south panel
        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        chatroompage.add(mainPanel);
//        chatroompage.setBounds(250,100,900,600);
        chatroompage.setVisible(true);
	    add(chatroompage);
    }
    
    //function for send message
    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
        	User thisUser = client.getThisUser();
        	Chatroom room = client.getRoomOfThisUser();
        	
            if (messageBox.getText().length() < 1) {
                // do nothing
            }
//            else if (messageBox.getText().equals(".clear")) {
//                chatBox.setText("Cleared all messages\n");
//                messageBox.setText("");
//            }
            else {
            	// Print conversation of current user to chat box 
            	Date date = new Date();
            	String conversation = messageBox.getText();
            	chatBox.append(String.format(conversationFormat, sdf.format(new Timestamp(date.getTime())), 
            			thisUser.getUsername(), conversation));
                messageBox.setText("");
                // Send conversation to server
                ctrl.sendConversation(room.getId(), conversation, thisUser);
            }
            messageBox.requestFocusInWindow();
        }
    }
    
    public void receiveConversation(Conversation conv) {
    	String receivedConv = conv.getContent();
    	Timestamp ts = conv.getCreateTime();
    	String username = conv.getUsername();
    	chatBox.append(String.format(conversationFormat, sdf.format(ts), username, receivedConv));
    }

	public void receiveCreateGameResult(int gameRecordId) {
		// TODO: if success, store game record id in local variable
		//       then move to quiz game screen
	}


}