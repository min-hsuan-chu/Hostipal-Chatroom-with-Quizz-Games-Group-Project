package server;

import java.io.*;
import java.net.*;
import component.Message;
import component.User;

public class ServerThread extends Thread {
	
	private ServerController ctrl;
	private Server server;
	private Socket clientSocket = null;

	private ObjectInputStream fromClient = null;
	private ObjectOutputStream toClient = null;
	
	private boolean isConnected;
	
	public ServerThread(Server server, ServerController ctrl, Socket clientSocket) {
		super("ServerThread");
		this.ctrl = ctrl;
		this.server = server;
		this.clientSocket = clientSocket;
	}
	
	
	public ObjectOutputStream getToClient() {
		return toClient;
	}
	
	public void init() {
		try {
			fromClient = new ObjectInputStream(clientSocket.getInputStream());
			
		} catch (IOException e) {
			System.out.println("Error occurred in getting input stream from client socket.");
			e.printStackTrace();
			System.exit(-1);
		}
		
		try {
			toClient = new ObjectOutputStream(clientSocket.getOutputStream());
			
		} catch (IOException e) {
			System.out.println("Error occurred in getting output stream from client socket.");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void close() {
		try {
			if (fromClient != null) fromClient.close();
			if (toClient != null) toClient.close();
			if (clientSocket != null) clientSocket.close();

		} catch (IOException e) {
			System.out.println("Error occurred in closing I/O streams and socket.");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void run() {
		
		init();
		isConnected = true;
		while (isConnected) {
			try {
				// Receive message from client
				Message msgFromClient = (Message) fromClient.readObject();
				System.out.println("Message from client: \n"+msgFromClient);
				// Do operation based on message type using ServerController and return result message
				Message resultMsg = ctrl.receiveMsg(msgFromClient);
				// If the request is login, add user to the online list and thread list in server object
				if (msgFromClient.getType().compareTo("login") == 0) {
					User theUser = resultMsg.getUser();
					if (theUser != null) {
						String username = theUser.getUsername();
						server.addUserToThreadList(username, this);
					}
				}
				// Reply back to the same client the result message
				if (resultMsg != null) {
					System.out.println("Result message of server: \n"+resultMsg);
					toClient.writeObject(resultMsg);
				}
			} catch (ClassNotFoundException e) {
				System.out.println("Server Thread : run : Message class is not found.");
				e.printStackTrace();
				System.exit(-1);
			} catch (EOFException e) {
			} catch (IOException e) {
				System.out.println("Server Thread : run : Error occurred in reading I/O streams.");
				e.printStackTrace();
				System.exit(-1);
			}
			
		}
	}

}
