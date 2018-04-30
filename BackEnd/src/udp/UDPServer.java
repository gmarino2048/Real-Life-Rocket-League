package udp;

import java.io.IOException;
import java.net.*;
import org.json.*;
import sql.*;
import tcp.SQL_Server;

/**
 * UDP server class that acts as a middle man between the front end and hardware
 * components. It receives commands to move the car from the front end and
 * passes them on to the hardware
 * 
 * @author nikhil
 *
 */
public class UDPServer {
	private static final int PORT_NUM = 8001;
	public static final String car1ip = "172.19.32.118";
	public static final String car2ip = ""; // we never made a 2nd car, so this was invalid regardless. However, the
											// code is structured so that just putting another car's ip here should work
											// 100%
	public static final int carPort = 1234;
	public static final int endGamePort = 9090;
	public static final int endGameState = 11;

	// stores the last command sent to cars. This helps prevent sending the cars 20
	// packets a second, all giving it the same state of 1, for example. we only
	// send the state if it is a new one
	public static int p1prev = -10;
	public static int p2prev = -10;

	public static SQL_Server sqlserver;

	public static void main(String argv[]) {
		sqlserver = new SQL_Server();
		new Thread(sqlserver).start();
		try {

			DatagramSocket welcomeSocket = new DatagramSocket(PORT_NUM);
			// DatagramSocket sock = new DatagramSocket();
			// byte[] data = new byte[100];
			// data[0] = 11;
			// sock.send(new DatagramPacket(data, data.length,
			// InetAddress.getByName(car1ip), carPort));
			/**
			 * the above code was used to send an endgame command to the car just in case it
			 * was running and we needed to kill the process this came up a lot via testing,
			 * it almost broke the car multiple times, which is why I am leaving it, but
			 * commented out
			 * 
			 * @author nikhil
			 */

			byte[] data;
			boolean run = true;
			DatagramPacket pack;

			/**
			 * infinite loop to receive UDP packets from the front end, starting threads for
			 * each one
			 */
			while (run) {
				data = new byte[200];
				pack = new DatagramPacket(data, data.length);
				welcomeSocket.receive(pack);
				new Thread(new UDPServer().new UDPClient(pack)).start();

			}
			welcomeSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * private class for UDP server, is a thread
	 * 
	 * @author nikhil
	 *
	 */
	private class UDPClient implements Runnable {

		private DatagramPacket pack;

		public UDPClient(DatagramPacket pack) {
			this.pack = pack;
		}

		/**
		 * gets the data of the UDP packet, and jsonifies it
		 * 
		 * @return
		 */
		private JSONObject getData() {

			return new JSONObject(new String(this.pack.getData()));

		}

		/**
		 * thread that runs: it checks for an endgame state, and if met, does that set
		 * of actions. if not, it sends the command to the appropriate car
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			JSONObject command = getData();
			System.out.println(command.toString());
			int player = command.getInt("player");
			int state = command.getInt("command");
			DatagramSocket sock;
			String sendIp = null;
			byte[] data = new byte[5];

			if (state == endGameState) {

				InetAddress client = pack.getAddress();
				try {
					data[0] = (byte) endGameState;
					sock = new DatagramSocket();
					// tell both cars the game is over
					sock.send(new DatagramPacket(data, data.length, InetAddress.getByName(car2ip), carPort));
					sock.send(new DatagramPacket(data, data.length, InetAddress.getByName(car1ip), carPort));

					// reset the previous states for the cars
					p1prev = -10;
					p2prev = -10;

					// gain access to SQL database
					MySQL_Connector conn = new MySQL_Connector();
					String gameid = (conn.getActiveGameId());

					// the code below was not working with the particle API connection, so we
					// hardcoded some values in
					// conn.updateGameScore(sqlserver.apiConn.getScore1(),
					// sqlserver.apiConn.getScore2());
					conn.updateGameScore(1, 0);
					JSONObject gameData = conn.getJSONforGame(gameid);
					System.out.println(client.toString());
					System.out.println(gameData.toString());
					sock.send(new DatagramPacket(gameData.toString().getBytes(), gameData.toString().getBytes().length,
							client, endGamePort)); // sends info back to front end
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return;
			}
			if (player == 1) { // if it was not an end game state, send the command to the appropriate car if
								// it was a new state for that car. If it was a repeated command, exit the
								// thread
				sendIp = car1ip;
				if (p1prev == state) {

					return;
				} else {
					p1prev = state;
				}
			} else if (player == 2) {
				sendIp = car2ip;
				if (p2prev == state) {

					return;
				} else {
					p2prev = state;
				}
			}

			try {
				// send
				data[0] = (byte) state;
				System.out.println(data[0]);
				// send the command to the correct car
				sock = new DatagramSocket();
				sock.send(new DatagramPacket(data, data.length, InetAddress.getByName(sendIp), carPort));
				sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}

	}
}