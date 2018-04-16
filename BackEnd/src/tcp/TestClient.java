package tcp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			Socket sock = new Socket("172.19.21.177",9009);
			sock.getOutputStream().write("hello".getBytes());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
