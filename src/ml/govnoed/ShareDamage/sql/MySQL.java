package ml.govnoed.ShareDamage.sql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class MySQL {
		
	Properties props = new Properties();
	String filename = "plugins/sharedamage/database.properties";
	InputStream is = null;

	private String host;
	private String port;
	private String database;
	private String username;
	private String password;

	private Connection connection;
	
	public boolean isConnected() {
		return (connection == null ? false : true);
	}
	
	public void connect() throws ClassNotFoundException, SQLException {
		readfile();
		if (!isConnected()) {
			host = props.getProperty("host");
			port = props.getProperty("port");
			database = props.getProperty("database");
			username = props.getProperty("username");
			password = props.getProperty("password");
			connection = DriverManager.getConnection("jdbc:mysql://" +
				     host + ":" + port + "/" + database + "?useSSL=false",
				     username, password);
		}
		
	}
	
	public void disconnect() {
		if (isConnected()) {
			try {
				connection.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	private void readfile() {
		try {
		    is = new FileInputStream(filename);
		} catch (FileNotFoundException ex) {
		    ex.printStackTrace();
		}
		try {
			// load default
			props.setProperty("host", "");
		    props.setProperty("port", "");
		    props.setProperty("database", "");
		    props.setProperty("username", "");
		    props.setProperty("password", "");
		    // load user props
		    props.load(is);
		    System.out.print("Your database settings in plugins/shagedamage/database.properties - " + props.entrySet().toString());
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
	}
}

