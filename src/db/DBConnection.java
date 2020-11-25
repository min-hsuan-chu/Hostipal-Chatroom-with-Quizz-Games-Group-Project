package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Database connection class is for connecting to the PostgreSQL Database,
 * and closing connection as well.
 * @author King Yin Kenneth Chen
 * @version 2020-02-29
 *
 */
public class DBConnection {
	
	private Connection connection = null;
	private Statement statement = null; // Statement for normal SQL query without parameter
	private PreparedStatement preparedStatement = null;  // Statement for parameterised SQL query
	
	public DBConnection() {
		connect();
		
		/* ----------------------------------
		 * Set schema using Statement
		 * ---------------------------------- */
		try 
		{
			String sql = "SET SEARCH_PATH = 'hospitalchat'";
			Statement setSchemaStatement = connection.createStatement();
			setSchemaStatement.execute(sql);
			setSchemaStatement.close();
		} 
		catch (SQLException e) 
		{
			System.out.println("Error occurred when executing sql of setting schema.");
			e.printStackTrace();
		}
		
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Method to connect to the PostgreSQL Database using JDBC Driver
	 */
	public void connect() {
		
		
		 
		String url = null;
		String username = null;
		String password = null;

		/* ----------------------------------------------
		 * Reading properties file for getting properties 
		 * of database connection
		 * ---------------------------------------------- */
		try (FileInputStream input = new FileInputStream(new File("db/db.properties"))) {
			
			Properties props = new Properties();
			props.load(input);

			username = (String) props.getProperty("username");
			password = (String) props.getProperty("password");
			url = (String) props.getProperty("URL");

		}
		catch (IOException e) {
			System.out.println("Error occurred when input file.");
			e.printStackTrace();
			return;
		}
		
		/* ----------------------------------------------
		 * Connecting to the database using DriverManager
		 * ---------------------------------------------- */
		try {
			connection = DriverManager.getConnection(url, username, password);
			
			if (connection != null) {
				System.out.println("Connection is now successfully established with your database!");
			} else {
				System.out.println("The connection object is still null. Connection failed.");
			}
        }
		catch (SQLException e) {
			System.out.println("Error occurred when establishing connection with database.");
			e.printStackTrace();
			return;
		}
		
	}
	
	/**
	 * Method to close the connection with PostgreSQL database
	 */
	public void close() {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
				System.out.println("DB Connection is closed.");
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * getRecordCount is used to return the number of rows presented in a table
	 * @param table The table name
	 * @return The count number of rows in integer
	 */
	private int getRecordCount(String table) {
		statement = null;
		int count = 0;
		String sql = String.format("SELECT COUNT(*) FROM %s", table);
		try {
			System.out.println("SQL execute: " + sql);
			
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			
			while (resultSet.next())
			{
				count = resultSet.getInt("COUNT");
			}
		} 
		catch (SQLException e) 
		{
			System.out.println("Error occurred in getRecordCount()");
			e.printStackTrace();
		}
		return count;
	}

	public static void main(String[] args) {
		// Create connection to database by initialising DBConnection object
		DBConnection dbc = new DBConnection();
		// For testing
		System.out.println(dbc.getRecordCount("role"));
	}

}
