package testing;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {

	public static final int portNo = 3010;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket sock;
		try {
			sock = new ServerSocket(portNo);

			while (true) {
				parseAndPrint(sock.accept());
			}
			//sock.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
		}

	}

	private static void parseAndPrint(Socket socket) throws IOException {
		// TODO Auto-generated method stub
		InputStream is = socket.getInputStream();
		byte[] data = new byte[100];
		is.read(data);
		System.out.println(new String(data));
		System.out.println(data.length);

	}

}
