package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.*;

import sql.MySQL_Connector;

public class SQL_Server {

	private static final int portno = 9009;
	private static MySQL_Connector conn;
	private static ServerSocket server;
	
	


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ServerSocket server;
		
		try {
			server = new ServerSocket(portno);
			conn = new MySQL_Connector();
			
			while(true) {
				Socket sock = server.accept();
				new Thread(new SQL_Server().new SQL_Query(sock)).start();
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}
	
	
	private class SQL_Query implements Runnable{
		
		private Socket socket;
		
		public SQL_Query(Socket sock) {
			socket = sock;
		
		}
		
		
		private JSONObject getData() {
			byte[] data = new byte[256];
			try {
				this.socket.getInputStream().read(data);
				return new JSONObject(new String(data));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			
			
		}


		@Override
		public void run() {
			// TODO Auto-generated method stub
			JSONObject request = getData();
			if(request.getString("uname") != null) {
				userCreationRequest(request);
			}
			
		}


		private void userCreationRequest(JSONObject request) {
			// TODO Auto-generated method stub
			conn.createUser(request.getString("uname"), request.getString("pw"));
			
			
		}
	}

}
