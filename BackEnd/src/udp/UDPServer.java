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
			serv = new DatagramSocket(3006);
			while(alive) {
				pack = new DatagramPacket(buff, buff.length);
				serv.receive(pack);
				InetAddress add = pack.getAddress();
				int port = pack.getPort();
				pack = new DatagramPacket(buff, buff.length, add, port);
				String received = new String(pack.getData(),0,pack.getLength());
				System.out.println(received);
				if(received.contains("end"))
					alive = false;
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
