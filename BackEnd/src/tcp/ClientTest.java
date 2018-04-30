package tcp;

import java.io.IOException;
import java.net.*;
import java.net.UnknownHostException;

import org.json.JSONObject;

import playground.ServerTest;

public class ClientTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setIn(System.in);
		int portno = 5000;
		try {
			DatagramSocket sock = new DatagramSocket(portno);
			boolean run = true;
			while (run) {
				byte[] one = { 1, 6, 8, 9, 11 };

				for (int i = 0; i < one.length; i++) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					byte[] send = new byte[2];
					send[0] = one[i];
					System.out.println(send[0]);
					sock.send(new DatagramPacket(send, send.length, InetAddress.getByName("172.19.39.112"), 1234));

				}

			}
			sock.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
