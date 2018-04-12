package udp;

import java.net.*;
import java.util.*;

public class CarUDPClient {

	static Map<String, Byte> stateTable = new HashMap<String, Byte>();
	{
		stateTable.put("error", (byte) -2);
		stateTable.put("start-end", (byte) -1);
		stateTable.put("neutral", (byte) 0);
		stateTable.put("forward", (byte) 1);
		stateTable.put("wheels_right", (byte) 2);
		stateTable.put("reverse", (byte) 3);
		stateTable.put("wheels_left", (byte) 4);
		stateTable.put("drive_right", (byte) 5);
		stateTable.put("drive_left", (byte) 6);
		stateTable.put("reverse_right", (byte) 7);
		stateTable.put("reverse_left", (byte) 8);
		stateTable.put("brake", (byte) 9);
		stateTable.put("boost", (byte) 10);
	}

	private static final int carPort = 1234;
	private static final String carIp = "172.20.42.125";
	static String ip = "172.20.42.173";
	static DatagramSocket sock;
	static DatagramPacket pack;
	static InetAddress add;
	static byte[] data = new byte[1];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, Byte> stateTable = new HashMap<String, Byte>();
		{
			stateTable.put("error", (byte) -2);
			stateTable.put("start-end", (byte) -1);
			stateTable.put("neutral", (byte) 0);
			stateTable.put("forward", (byte) 1);
			stateTable.put("wheels_right", (byte) 2);
			stateTable.put("reverse", (byte) 3);
			stateTable.put("wheels_left", (byte) 4);
			stateTable.put("drive_right", (byte) 5);
			stateTable.put("drive_left", (byte) 6);
			stateTable.put("reverse_right", (byte) 7);
			stateTable.put("reverse_left", (byte) 8);
			stateTable.put("brake", (byte) 9);
			stateTable.put("boost", (byte) 10);
		}
		try {
			add = InetAddress.getByName(carIp);
			sock = new DatagramSocket();
			System.out.println("starting");
			for (String s : stateTable.keySet()) {
				System.out.println("here");
				if ((int) stateTable.get(s) > 0) {

					Thread.sleep(500);
					
					data[0] = stateTable.get(s);
					//System.out.println("sending: "+Arrays.toString(data));
					sock.send(new DatagramPacket(data,data.length, add, carPort));
					Thread.sleep(1000);
					Integer g = 0;
					
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
