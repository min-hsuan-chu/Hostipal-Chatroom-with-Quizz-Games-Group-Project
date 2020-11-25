package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import component.Message;

public class ClientReadThread extends Thread {
	
	private ClientController ctrl = null;
	private Socket clientSocket;
	private ObjectInputStream fromServer = null;
	
	private boolean isConnected;
	
	public ClientReadThread(ClientController ctrl, Socket clientSocket) {
		this.ctrl = ctrl;
		this.clientSocket = clientSocket;
		
	}
	
	public ObjectInputStream getFromServer() {
		return fromServer;
	}
	
	/** 
	 * init is the starting point of this thread class, 
	 * the input stream will be retrieved from socket.
	 */
	public void init() {
		try {
			fromServer = new ObjectInputStream(clientSocket.getInputStream());
			
		} catch (IOException e) {
			System.out.println("Error occurred in getting input stream from client socket.");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void close() {
		try {
			if (fromServer != null) fromServer.close();

		} catch (IOException e) {
			System.out.println("Error occurred in closing input streams.");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void run() {
		init();
		isConnected = true;
		while (isConnected) {
			try {
				// Receive message from server
				Message msgFromServer = (Message) fromServer.readObject();
				System.out.println("Message from server: \n"+msgFromServer);
				// Tell different GUI based on message type using ClientController
				ctrl.receiveMsg(msgFromServer);
			} catch (ClassNotFoundException e) {
				System.out.println("Server Thread : run : Message class is not found.");
				e.printStackTrace();
				System.exit(-1);
			} catch (EOFException e) {
			} catch (IOException e) {
				System.out.println("Server Thread : run : Error occurred in reading Input streams.");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
}
