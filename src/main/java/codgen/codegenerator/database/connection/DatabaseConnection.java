package codgen.codegenerator.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
	Connection connection;

	private String hostName;
	private String sid;
	private String userName;
	private String password;
	private int port;

	public DatabaseConnection(String hostName, String sid, String userName, String password, int port) {
		this.hostName = hostName;
		this.sid = sid;
		this.userName = userName;
		this.password = password;
		this.port = port;
	}

	public Connection getOracleConnection() {

		// Declare the class Driver for Oracle DB
		// This is necessary with Java 5 (or older)
		// Java6 (or newer) automatically find the appropriate driver.
		// If you use Java> 6, then this line is not needed.
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// Example: jdbc:oracle:thin:@localhost:1521:db11g
		String connectionURL = "jdbc:oracle:thin:@" + hostName + ":" + port + ":" + sid;

		System.out.println(connectionURL);

		try {
			connection =  DriverManager.getConnection(connectionURL, userName, password);
		} catch (SQLException e) {
			System.out.println("Problem while connecting to database");
			e.printStackTrace();
		}
		return this.connection;
	}
	
	public void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			System.out.println("Problem while closing connection");
			e.printStackTrace();
		}
	}
}
