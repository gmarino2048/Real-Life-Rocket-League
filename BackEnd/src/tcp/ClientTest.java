package tcp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import testing.ServerTest;

public class ClientTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setIn(System.in);
		int portno = ServerTest.portNo;
		try {
			Socket sock = new Socket("localhost",portno);
			
			byte[] data = new byte[100];
			System.in.read(data);
			sock.getOutputStream().write(data);
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
