package sqltest;
/*
 * This class tests the functionality of MySQL_Connector.java
 */

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sound.midi.Synthesizer;

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
	private static final String opponent = "nsc27";
	private static String[] gameIds = new String[2];
	private static int index = 0;
	private static MySQL_Connector conn;

	@Test
	public void test1() {
		System.out.println("initializing SQL connection for testing");
		conn = new MySQL_Connector();
		assertEquals(conn.getStatement() != null, true);
		try {
			System.out.println("finished test 1,hit enter for next test");
			System.in.read(new byte[100]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void test2() {
		conn.createUser(user, password);
		assertEquals(conn.isUser(user), true);
		conn.getUser(user);
		try {

			assertEquals(conn.getResult().getString("username"), user);
			assertEquals(conn.getResult().getInt("totalWins"), 0);
			assertEquals(conn.getResult().getInt("totalLosses"), 0);
			assertEquals(conn.getResult().getInt("totalGames"), 0);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			assertEquals(1, 0);
			e.printStackTrace();
		}
		try {
			System.out.println("finished test 2,hit enter for next test");
			System.in.read(new byte[100]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test3() {
		String id = conn.createGame(user, opponent);

		id = id.replaceAll("T", " ");
		id = id.substring(0, id.length() - 7);
		// id = id +"0";
		conn.getMostRecentGame(user, opponent);

		try {
			String res = conn.getResult().getString("gameID");
			gameIds[index] = res;
			index++;
			assertEquals(id, res.substring(0, res.length() - 5));
			assertEquals(user, conn.getResult().getString("p1_username"));
			assertEquals(opponent, conn.getResult().getString("p2_username"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			assertEquals(1, 0);
			e.printStackTrace();
		}
		try {
			System.out.println("finished test 3, hit enter for next test");
			System.in.read(new byte[100]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			assertEquals(1,0);
			e.printStackTrace();
		}
		try {
			System.out.println("finished test 4,hit enter for next test");
			System.in.read(new byte[100]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test5() {
		try {
			Thread.sleep(2000);
			// otherwise, the game ID will be the same because the games will be during the
			// same second
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		test3();
		test4();

		List<String> userGames = conn.getGameList(user);
		int i = 0;
		for (String result : userGames) {

			assertEquals(result, gameIds[i]);
			i++;

		}
		try {
			System.out.println("finished test 5,hit enter for next test");
			System.in.read(new byte[100]);
		} catch (IOException e) {
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
			assertEquals(1,0);
			e.printStackTrace();
		}
		conn.terminate();
	}
}
