
package sql;

import java.io.IOException;
import java.sql.SQLException;

public class SQL_Tester {

	public static void main(String[] args) throws SQLException, IOException {
		// TODO Auto-generated method stub
//		SQL_Tester q = new SQL_Tester(new MySQL_Connector());
//		System.out.println("nsc27 (Nikhil) user data currently");
//		q.printUserData("nsc27");
//		System.in.read();
//		System.out.println("gjs64 (Griffin) user data currently");
//		q.printUserData("gjs64");
//		System.in.read();
//		q.c.createGame("nsc27", "gjs64");
//		System.out.println("created game bw nikhil and griffin");
//		System.in.read();
//		q.c.updateGameScore(9,3);
//		q.c.getMostRecentGame("nsc27", "gjs64");
//		System.out.println("finished their game that was started at: " + q.c.getResult().getString("gameID"));
//		System.in.read();
//		System.out.println(q.c.getResult().getString("goals_P1"));
//		System.out.println("nikhil scored that many goals");
//		System.in.read();
//		System.out.println(q.c.getResult().getString("goals_P2"));
//		System.out.println("griffin scored that many goals");
//		System.in.read();
//		System.out.println("nsc27 (Nikhil) user data after the game");
//		q.printUserData("nsc27");
//		System.in.read();
//		System.out.println("gjs64 (Griffin) user data after the game");
//		q.printUserData("gjs64");

		MySQL_Connector c = new MySQL_Connector();
		c.createGame("nsc27", "gjs64");
	}

	MySQL_Connector c;

	public SQL_Tester(MySQL_Connector c) {
		this.c = c;
	}

	public void printUserData(String user) {
		c.getUser(user);
		try {
			System.out.println("user: " + c.getResult().getString("username"));
			System.out.println("total games: " + c.getResult().getString("totalGames"));
			System.out.println("total goals: " + c.getResult().getString("totalGoals"));
			System.out.println("total wins: " + c.getResult().getString("totalWins"));
			System.out.println("total losses: " + c.getResult().getString("totalLosses"));
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public void getUserTest(String user) {
//		String g = c.get
//	}
	
	

}
