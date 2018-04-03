package sqltest;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import sql.MySQL_Connector;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MySQL_ConnecterTester {
	private static final String user = "tester";
	private static final String password = "password";
	private static MySQL_Connector conn;

	@Test
	public void test1() {
		System.out.println("init");
		conn = new MySQL_Connector();

	}
	// @Test
	// void test() {
	// fail("Not yet implemented");
	// }

	@Test
	public void test2() {
		conn.createUser(user, password);
		conn.getUser(user);
		try {

			assertEquals(conn.getResult().getString("username"), user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			assertEquals(1, 0);
			e.printStackTrace();
		}
	}

	@Test
	public void test3() {
		String id = conn.createGame(user, "nsc27");
		id = id.replaceAll("T", " ");
		id = id.substring(0, id.length() - 7);
		// id = id +"0";
		conn.getMostRecentGame(user, "nsc27");

		try {
			String res = conn.getResult().getString("gameID");
			assertEquals(id, res.substring(0, res.length() - 5));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			assertEquals(1, 0);
			e.printStackTrace();
		}
	}

	@Test
	public void test4() {
		String gameid = conn.getActiveGameId();
		conn.updateGameScore(10, 9);
		conn.getGameData(gameid);

		try {
			int userScore = conn.getResult().getInt("goals_p1");
			int oppScore = conn.getResult().getInt("goals_p2");
			String winner = conn.getResult().getString("winner");
			assertEquals(userScore, 10);
			assertEquals(oppScore, 9);
			assertEquals(winner, user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test9() {
		System.out.println("terminating");

		String query = "delete from Users where username = '" + user + "'";
		try {
			conn.getStatement().executeUpdate(query);
			query = "delete from Games where p1_username = '" + user + "'";
			conn.getStatement().executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn.terminate();
	}
}
