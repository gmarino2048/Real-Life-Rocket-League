package tcp;

import java.io.IOException;
import java.net.*;
import org.json.*;

import sql.MySQL_Connector;

/**
 * 
 * @author smedlang TCP Server to receive byte states
 */

public class UDPServer {
	private static final int PORT_NUM = 8001;
	public static final String car1ip = "172.20.42.174";// .125
	public static final String car2ip = "";
	public static final int carPort = 1234;
	public static final int endGame = -1;

	public static void main(String argv[]) {

		try {
			DatagramSocket welcomeSocket = new DatagramSocket(PORT_NUM);

			boolean run = true;
			DatagramPacket pack;
			byte[] data;
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
			InetAddress add;
			DatagramSocket sock;
			String sendIp = null;
			byte[] data = new byte[5];

			if (state == endGame) {

				InetAddress client = pack.getAddress();
				try {
					data[0] = (byte) state;
					sock = new DatagramSocket();
					sock.send(new DatagramPacket(data, data.length, InetAddress.getByName(car2ip), carPort));
					sock.send(new DatagramPacket(data, data.length, InetAddress.getByName(car1ip), carPort));
					// sock.send(new DatagramPacket(data, data.length,
					// InetAddress.getByName(sendIp), carPort));
					MySQL_Connector conn = new MySQL_Connector();
					String gameid = (conn.getActiveGameId());
					conn.updateGameScore(1, 0);
					JSONObject gameData = conn.getJSONforGame(gameid);
					System.out.println(client.toString());
					System.out.println(gameData.toString());
					sock.send(new DatagramPacket(gameData.toString().getBytes(), gameData.toString().getBytes().length,
							client, 9000));
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return;
			}
			if (player == 1) {
				sendIp = car1ip;
			} else if (player == 2) {
				sendIp = car2ip;
			}

			try {
				data[0] = (byte) state;
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