package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import component.Chatroom;
import component.User;

public class Server {
	
	private int port = 0;
	
	private ServerSocket serverSocket = null;
	private boolean isConnected;
	
	// Global storage of all online users and their threads
	private Set<User> onlineUsers = new HashSet<User>();
	private Map<String, ServerThread> threads = new HashMap<String, ServerThread>();
	private TreeSet<Chatroom> rooms = new TreeSet<Chatroom>();
	
	public Server(int port) {
		this.port = port;
		
		init();
	}
	
	public synchronized Set<User> getOnlineUsers() {
		return onlineUsers;
	}
	
	public synchronized Map<String, ServerThread> getThreads() {
		return threads;
	}
	
	public synchronized TreeSet<Chatroom> getChatrooms() {
		return rooms;
	} 
	
	public void init() {
		try {
            serverSocket = new ServerSocket(port);
            isConnected = true;
            System.out.println("Server has started!");
        } catch (IOException e) {
            System.out.println("Failed to listen to the port : " + port);
            e.printStackTrace();
        }
		
		/* --------------------------------------------------------------
		 * Start listening to the socket, open to accept incoming request
		 * And start a new thread for each client
		 * -------------------------------------------------------------- */
		try {
			while (isConnected) {
				System.out.println("Server started listening...");
				Socket clientSocket = serverSocket.accept();
				
				ServerController ctrl = new ServerController(this);
				ServerThread st = new ServerThread(this, ctrl, clientSocket);
				st.start();
				System.out.println("One client is connected. New thread is started for this client.");
				
				System.out.println(onlineUsers);
				System.out.println(threads);
				System.out.println(rooms);
			}
		} catch (Exception e) {
			System.out.println("Server : init : Error occurred in starting server socket "
					+ "and listening incoming request from client.");
			e.printStackTrace();
			close();
		}
	}
	
	public void close() {
		try {
			// Close server socket
            serverSocket.close();
            isConnected = false;
		} catch (IOException e) {
			System.out.println("Error occurred when closing the server");
			e.printStackTrace();
		}
	}
	
	public synchronized void addUserToOnlineList(User user) {
		onlineUsers.add(user);
	}
	
	public synchronized void setOnlineUsers(Set<User> onlineUsers) {
		this.onlineUsers = onlineUsers;
	}
	
	public synchronized void addUserToThreadList(String username, ServerThread st) {
		threads.put(username, st);
	}
	
	public synchronized void removeThreadFromList(String username) {
		this.threads.remove(username);
	}
	
	public synchronized void addNewChatroom(Chatroom newRoom) {
		rooms.add(newRoom);
	}
	
	public synchronized void setChatrooms(TreeSet<Chatroom> rooms) {
		this.rooms = rooms;
	}
	
	public synchronized void removeChatroom(Chatroom removeRoom){rooms.remove(removeRoom); }
	
	public static void main(String[] args) {
		Server server = null;
		if (args.length == 1)
			server = new Server(Integer.parseInt(args[0]));
		else 
			System.out.println("Please enter a port number to start the server.");
		
	}

}
