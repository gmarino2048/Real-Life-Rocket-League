package playground;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


	private static int PORT_NUM = 9090;
	
	public static void main (String [] args) throws IOException{
		ServerSocket serverSocket = new ServerSocket(PORT_NUM);
		try {
			while(true){
				Socket socket = serverSocket.accept();
			
				try{
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				}finally {
					socket.close();
				}
			}
		} finally{
			serverSocket.close();
		}
	}
}
