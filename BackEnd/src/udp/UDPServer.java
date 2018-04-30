package udp;

import java.io.IOException;
import java.net.*;
import org.json.*;
import sql.MySQL_Connector;

public class UDPServer {
	private static final int PORT_NUM = 8001;
	public static final String car1ip = "172.19.32.118";// .125
	public static final String car2ip = "";
	public static final int carPort = 1234;
	public static final int endGamePort = 9090;
	public static final int endGame = 11;

	public APIConnection conn = new APIConnection();
	public static int p1prev = -10;
	public static int p2prev = -10;

	public static void main(String argv[]) {

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
			 * @author nikhil
			 */
			byte[] data;
			boolean run = true;
			DatagramPacket pack;

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

	private class UDPClient implements Runnable {

		private DatagramPacket pack;

		public UDPClient(DatagramPacket pack) {
			this.pack = pack;
		}

		private JSONObject getData() {
			byte[] data = new byte[256];

			return new JSONObject(new String(this.pack.getData()));

		}

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

			if (state == endGame) {

				InetAddress client = pack.getAddress();
				try {
					data[0] = (byte) endGame;
					sock = new DatagramSocket();
					sock.send(new DatagramPacket(data, data.length, InetAddress.getByName(car2ip), carPort));
					sock.send(new DatagramPacket(data, data.length, InetAddress.getByName(car1ip), carPort));
					// sock.send(new DatagramPacket(data, data.length,
					// InetAddress.getByName(sendIp), carPort));
					p1prev = -10;
					p2prev = -10;
					MySQL_Connector conn = new MySQL_Connector();
					String gameid = (conn.getActiveGameId());
					conn.updateGameScore(1, 0); // need to obtain actual score here
					JSONObject gameData = conn.getJSONforGame(gameid);
					System.out.println(client.toString());
					System.out.println(gameData.toString());
					sock.send(new DatagramPacket(gameData.toString().getBytes(), gameData.toString().getBytes().length,
							client, endGamePort));
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return;
			}
			if (player == 1) {
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

				data[0] = (byte) state;
				System.out.println(data[0]);
				sock = new DatagramSocket();
				sock.send(new DatagramPacket(data, data.length, InetAddress.getByName(sendIp), carPort));
				sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// this.pack.getOutputStream().write("go ".getBytes());
			// // JSONObject command = this.getData();

		}

	}
}