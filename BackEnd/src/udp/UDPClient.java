package udp;

import java.io.IOException;
import java.net.*;

public class UDPClient {
	
	private static final int carPortNo = 1234;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DatagramSocket sock;
		InetAddress add;
		byte[] buff = new byte[256];
		
		try {
			sock = new DatagramSocket();
			add = InetAddress.getByName("localhost");
			System.in.read(buff);
			
			
			DatagramPacket pack = new DatagramPacket(buff, buff.length, add, carPortNo);
			sock.send(pack);
			
			
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
