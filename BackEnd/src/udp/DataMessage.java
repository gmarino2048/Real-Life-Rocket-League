package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;

import org.json.*;

public class DataMessage implements Runnable {

	private static final String ip_car1 = "exIP1";
	private static final String ip_car2 = "exIP2";
	private static final int carPortNo = 1234;
	private static final String[] responseTypes = { "carCommand", "userData", "arenaData" };
	private static final int MAX_DATA = 2048;
	
	
	private DatagramPacket packet;
	private Socket socket;
	private String messageReceived;
	private JSONObject json;
	private int reponseSchema;
	private int receiveSchema;
	private String destination_IP;
	private int destination_Port;

	/**
	 * this constructor is for the message received from the UDP server, and will create a TCP connection elsewhere
	 * @param packet - DatagramPacket from UDP server
	 */
	public DataMessage(DatagramPacket packet) {
		this.packet = packet;
		socket = new Socket();
		
		messageReceived = new String(packet.getData());
		json = new JSONObject(messageReceived);
		receiveSchema = determineSchema();

	}
	
	/**
	 * this constructor is for the message received from a TCP connection, and will create a UDP DatagramPacket to send elswhere
	 * @param socket
	 */
	public DataMessage(Socket socket) {
		packet = new DatagramPacket(new byte[MAX_DATA], MAX_DATA);
		this.socket = socket;
		
		byte[] temp = new byte[MAX_DATA];
		try {
			socket.getInputStream().read(temp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("reading socket: failure");
			e.printStackTrace();
		}
		
		
		
		this.messageReceived = new String(temp);
		
		
	}

	public int determineSchema() {

		return json.getInt("messageType");

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		switch (receiveSchema) {

		case 0:

		case 1:

		case 2:

		default:

		}

	}
	
	public void pushPacket() {
		
		
		
	}

}
