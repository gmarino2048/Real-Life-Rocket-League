package sql;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MySQL_Connector {

	private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/SocCar?autoReconnect=true&useSSL=false";
	private static final String username = "root";
	private static final String password = "SocCar";
	private static final String userTable = "Users";
	private static final String gameTable = "Games";

	private static final String salt = "lkj13487fd0hj24";

	private Connection connection;
	private Statement statement;
	private ResultSet result;

	
	public ResultSet getResult() {
		return result;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MySQL_Connector c = new MySQL_Connector();
		synchronized (c) {
			try {

				c.createGame("gjs64", "nsc27");
				c.wait(2000);
				
				c.updateGameScore(1, 6);
				// c.createUser("nikhil", "loofa");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public MySQL_Connector() {

		try {
			this.connection = DriverManager.getConnection(DB_URL, username, password);
			this.statement = this.connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL connection failed");
			e.printStackTrace();
		}

	}

	/**
	 * checks if a user has made an account already
	 * 
	 * @param user
	 *            - username to check against the database
	 * @return true if already a user, false else
	 */
	public boolean isUser(String user) {
		this.getUser(user);
		return (this.result != null);
	}

	/**
	 * creates empty game with only data being the datetime, player1 username,
	 * player2 username
	 * 
	 * @param player1
	 * @param player2
	 */
	public String createGame(String player1, String player2) {
		LocalDateTime now = LocalDateTime.now();
		String query = "insert into " + gameTable + " (gameID, p1_username, p2_username, isActive) values ('"
				+ now.toString() + "', '" + player1 + "', '" + player2 + "', 1)";
		try {
			this.statement.executeUpdate(query);
			return now.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public Statement getStatement() {
		return this.statement;
	}
	
	/**
	 * Get's all the data in a result set for a certain game
	 * 
	 * @param gameID
	 *            - datetime for the game in question
	 * @return ResultSet, also the same as this.result
	 */
	public ResultSet getGameData(String gameID) {
		String query = "select * from " + gameTable + " where gameID='" + gameID+"'";
		try {
			this.result = this.statement.executeQuery(query);
			this.result.next();
			return this.result;

		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void updateGameScore(int p1Score, int p2Score) {
		String query = "update " + gameTable + " set goals_p1 = " + p1Score + ", goals_p2 = " + p2Score
				+ " where isActive = 1";
		try {
			this.statement.executeUpdate(query);
			query = "select * from " + gameTable + " where isActive=1";
			this.result = this.statement.executeQuery(query);
			this.result.next();
			String p1username = this.result.getString("p1_username");
			String p2username = this.result.getString("p2_username");
			query = "update " + gameTable +" set isActive = 0, winner = '";
			if (p1Score > p2Score) {
				query += p1username+"'";
				
				updatePlayerInfo(p1username, true, p1Score);
				updatePlayerInfo(p2username, false, p2Score);
			} else if (p1Score < p2Score) {
				query += p2username +"'";
				updatePlayerInfo(p2username, true, p2Score);
				updatePlayerInfo(p1username, false, p1Score);
			} else if (p1Score == p2Score) {
				query += "NULL_TIE'";
				updatePlayerInfo(p1username, false, p1Score);
				updatePlayerInfo(p2username, false, p2Score);
			}
			query += " where isActive = 1";

			this.statement.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void updatePlayerInfo(String username, boolean win, int score) {
		// TODO Auto-generated method stub
		getUser(username);
		int wins;
		int losses;
		int totalGames;
		int totalGoals;
		try {
			wins = this.result.getInt("totalWins");
			losses = this.result.getInt("totalLosses");
			totalGames = this.result.getInt("totalGames");
			totalGoals = this.result.getInt("totalGoals");

			String query = "update " + userTable;

			if (win) {
				query += " set totalWins = " + (wins + 1) + ", totalLosses = " + losses + ", totalGames = "
						+ (totalGames + 1) + ", totalGoals = " + (totalGoals + score) + " where username = '" + username
						+ "'";
			} else {
				query += " set totalWins = " + wins + ", totalLosses = " + (losses + 1) + ", totalGames = "
						+ (totalGames + 1) + ", totalGoals = " + (totalGoals + score) + " where username = '" + username
						+ "'";
			}
			
			this.statement.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createUser(String name, String password) {
		String postHash = hashPassword(password);
		String query = "insert into " + userTable
				+ " (username, totalGames, totalWins, totalLosses, totalGoals, password) " + "values ('" + name
				+ "', 0, 0, 0, 0, '" + postHash + "')";
		try {
			this.statement.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("failed to create user: " + name);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String hashPassword(String password) {

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update((password + salt).getBytes());
			byte[] digest = md.digest();
			StringBuilder hashed = new StringBuilder();
			for (int i = 0; i < digest.length; i++) {
				hashed.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
			}
			return hashed.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			System.out.println("password hashing failure");
			e.printStackTrace();
			return "failure";
		}

	}

	/**
	 * Get's user data, stores it in the ResultSet object
	 * 
	 * @param user
	 *            - user's username
	 */
	public void getUser(String user) {

		try {
			this.result = this.statement.executeQuery("select * from " + userTable + " where username='" + user+"'");
			this.result.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("get User: " + user + "; failure");
			e.printStackTrace();
			this.result = null;
		}
	}

	/**
	 * Closes the connection to the SQL server
	 * 
	 * @return
	 */
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
	
	
	public boolean getMostRecentGame(String player1, String player2) {
		String query = "select * from "+gameTable+" where p1_username ='"+player1+"' and p2_username='"+player2+"'";
		try {
			//System.out.println(query);
			this.result = this.statement.executeQuery(query);
			LocalDateTime d,earliest;
			earliest = LocalDateTime.now().minusYears(5);
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
			while(this.result.next()) {
				//System.out.println(this.result);
				d = LocalDateTime.parse(result.getString("gameID"), format);
				if(d.isAfter(earliest)) {
					earliest = d;
				}
			}
			result.previous();
			//System.out.println(result.getString("gameID"));
			query = "select * from "+gameTable+" where gameID = '"+earliest.toString().replace("T", " ")+"'";
			//System.out.println(earliest.toString());
			this.result = this.statement.executeQuery(query);
			this.result.next();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
		
	}
	
	
	public String getActiveGameId() {
		String query = "select * from "+gameTable+" where isActive = 1";
		try {
			this.result = this.statement.executeQuery(query);
			this.result.next();
			
			return this.result.getString("gameID");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return "";
		}
	}
}
