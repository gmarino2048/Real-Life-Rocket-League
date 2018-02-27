package udp;

import java.io.IOException;
import java.net.*;

public class UDPServer {

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DatagramSocket serv;
		byte[] buff = new byte[256];
		boolean alive = true;
		DatagramPacket pack;
		
		try {
			serv = new DatagramSocket();
			while(alive) {
				pack = new DatagramPacket(buff, buff.length);
				serv.receive(pack);
				
				
			}
			serv.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
