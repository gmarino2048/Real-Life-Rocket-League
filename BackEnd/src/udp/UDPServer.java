package udp;

import java.io.IOException;
import java.net.*;

public class UDPServer {

	private static final int serverPortNo = 3006;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DatagramSocket udpServer;
		byte[] buffer = new byte[256];
		boolean running = true;
		DatagramPacket packet;
		
		try {
			
			udpServer = new DatagramSocket(serverPortNo);
			boolean run = true;
			while(run) {
				
				packet = new DatagramPacket(buffer, buffer.length);
				udpServer.receive(packet);
				new Thread(new DataMessage(new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort()))).start();
				
			}
			
			udpServer.close();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
