package sql;

import java.sql.*;

import com.mysql.*;

public class MySQL_Connector {

	private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/SocCar?autoReconnect=true&useSSL=false";
	private static final String username = "root";
	private static final String password = "SocCar";
	private static final String userTable = "Users";
	private static final String gameTable = "Games";
	
	private Connection connection;
	private Statement statement;
	private ResultSet result;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MySQL_Connector c = new MySQL_Connector();
		
		try {
			
			c.connection = DriverManager.getConnection(DB_URL, username, password);
			String query = "select * from " + userTable;
			c.statement = c.connection.createStatement();
			c.result = c.statement.executeQuery(query);
			c.result.next();
			System.out.println(c.result.getString("username"));
			System.out.println(c.result.getInt("totalGames"));
			
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public MySQL_Connector() {
		
		try {
			this.connection = DriverManager.getConnection(DB_URL,username,password);
			this.statement = this.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL connection failed");
			e.printStackTrace();
		}
		
		
	}
	
	public boolean terminate() {
		try {
			this.connection.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection failed to terminate");
			e.printStackTrace();
			return false;
		}
	}
	
	public String getStringFromUsers(String user, String textField) {
		String value = "";
		
		try {
			this.result = this.statement.executeQuery("select * from "+ userTable +" where username="+user);
			this.result.next();
			value = this.result.getString(textField);
			return value;
		} catch (SQLException e) {
			System.out.println("failure to get this String");
			// TODO Auto-generated catch block
			e.printStackTrace();
			return value;
		}
	}
	
	public int getNumFromUsers(String user, String numField) {
		int num = -1;
		try {
			this.result = this.statement.executeQuery("select * from "+ userTable +" where username="+user);
			this.result.next();
			num = this.result.getInt(numField);
			return num;
		} catch (SQLException e) {
			System.out.println("failure to get this num");
			// TODO Auto-generated catch block
			e.printStackTrace();
			return num;
		}
		
	}

	
	
}
