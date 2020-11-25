package client;

import java.io.*;
import java.net.*;

import GUI.HospitalChatFrame;
import component.Chatroom;
import component.User;

public class Client {
	
	private String host = "";
	private int port = 0;
	private ClientController controller = null;//////111
	private Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;
    
    private boolean isConnected;
    
    private HospitalChatFrame gui;
    
    private User thisUser;
    private Chatroom roomOfThisUser;
    

    public Client(String host, int port) {
    	
    	this.host = host;
    	this.port = port;
    	
    	connect();
    }
    
    public boolean isConnected() {
    	return isConnected;
    }
    
    public ObjectOutputStream getToServer() {
    	return toServer;
    }
    
    public ObjectInputStream getFromServer() {
    	return fromServer;
    }
    
    public HospitalChatFrame getGUI() {
    	return gui;
    }
    
    public void setGUI(HospitalChatFrame gui) {
    	this.gui = gui;
    }
    
    public User getThisUser() {
    	return thisUser;
    }
    
    public void setThisUser(User thisUser) {
    	this.thisUser = thisUser;
    }
    
    public Chatroom getRoomOfThisUser() {
    	return roomOfThisUser;
    }
    
    public void setRoomOfThisUser(Chatroom room) {
    	this.roomOfThisUser = room;
    }
    
    public void connect() {
		try {
			socket = new Socket(this.host, this.port);
			toServer = new ObjectOutputStream(socket.getOutputStream());
//			fromServer = new ObjectInputStream(socket.getInputStream());
			
			// Start a thread for getting input stream and sit for reading message from server if there is any
			ClientController ctrl = new ClientController(this);
			ClientReadThread cit = new ClientReadThread(ctrl, socket);
			cit.start();
			
			isConnected = true;
			System.out.println("Client is successfully connected to "+ host + ":" + port);
		} catch (UnknownHostException e) {
			System.out.println("Client : Unknown host = "+ host);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Client : Unable to get the I/O streams for connection to host : "+ host);
			e.printStackTrace();
		}
	}
    
    public void close() {
		try {
			// Send message "0" to server indicating no longer need server
			toServer.writeInt(0);
			// Close I/O streams
			toServer.close();
            fromServer.close();
			// Close connection to server
			socket.close();
			
			isConnected = false;
		} catch (IOException e) {
			System.out.println("Client : Error occurred when closing the client");
			e.printStackTrace();
		}
	}

////////111
	public ClientController getController() {
		return controller;
	}
}

