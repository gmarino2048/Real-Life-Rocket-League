package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import org.json.*;

import sql.MySQL_Connector;

public class SQL_Server {

	private static final int portno = 9009;
	private static MySQL_Connector conn;
	private static ServerSocket server;
	private static final String userCreate= "userCreation";
	private static final String userInfo = "userInfo";
	
	


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		try {
			server = new ServerSocket(portno);
			conn = new MySQL_Connector();
			//System.out.println(server.getInetAddress().getHostName());
			while(true) {
				Socket sock = server.accept();
				System.out.println("conn made");
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
			if(request.getString("queryType") == null) {
				sendErrorMessage(request);
			}
			else {
				if(request.getString("queryType").equals(userCreate)) {
					userCreationRequest(request);
				} else if(request.get("queryType").equals(userInfo)) {
					userInfoRequest(request);
				}
			}
//			byte[] data = new byte[256];
//			try {
//				this.socket.getInputStream().read(data);
//				System.out.println(new String(data));
//				this.socket.getOutputStream().write("hi savi".getBytes());
//				this.socket.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			
		}


		private void userInfoRequest(JSONObject request) {
			// TODO Auto-generated method stub
			conn.getUser(request.getString("username"));
			
		}


		private void sendErrorMessage(JSONObject request) {
			// TODO Auto-generated method stub
			
		}


		private void userCreationRequest(JSONObject request) {
			// TODO Auto-generated method stub
			conn.createUser(request.getString("username"), request.getString("password"));
			JSONObject send = new JSONObject();
			send.accumulate("queryResult", "success").accumulate("queryType", request.get("queryType"));
			try {
				this.socket.getOutputStream().write(send.toString().getBytes());
				this.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		private JSONObject resultSetToJSON() {
			if(conn.getResult() == null) {
				return null;
			}
			else {
				try {
					conn.getResult().beforeFirst();
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
	}

}
