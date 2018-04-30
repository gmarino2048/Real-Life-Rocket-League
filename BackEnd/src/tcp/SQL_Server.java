package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.*;

import APIConnection.APIConnection;
import sql.MySQL_Connector;

public class SQL_Server implements Runnable{

	private static final int portno = 9009;
	private static MySQL_Connector conn;
	private static ServerSocket server;
	private static final String userCreate = "userCreation";
	private static final String userInfo = "userInfo";
	private static final String userGames = "userGames";
	private static final String recentGame = "recentGame";
	private static final String createGame = "gameCreation";
	public APIConnection apiConn;

	public SQL_Server() {
	//	apiConn = new APIConnection();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			server = new ServerSocket(portno);
			conn = new MySQL_Connector();
			// System.out.println(server.getInetAddress().getHostName());
			boolean run = true;
			while (run) {
				Socket sock = server.accept();
				System.out.println("conn made");
				new Thread(new SQL_Server().new SQL_Query(sock)).start();

			}
			server.close();
			conn.terminate();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			server = new ServerSocket(portno);
			conn = new MySQL_Connector();
			// System.out.println(server.getInetAddress().getHostName());
			boolean run = true;
			while (run) {
				Socket sock = server.accept();
				System.out.println("conn made");
				new Thread(new SQL_Server().new SQL_Query(sock)).start();

			}
			server.close();
			conn.terminate();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class SQL_Query implements Runnable {

		private Socket socket;

		public SQL_Query(Socket sock) {
			socket = sock;

		}

		private JSONObject getData() {
			byte[] data = new byte[256];
			try {
				this.socket.getInputStream().read(data);
				System.out.println(new String(data));
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
			if (request.getString("queryType") == null) {
				sendErrorMessage(request);
			} else {
				if (request.getString("queryType").equals(userCreate)) {
					userCreationRequest(request);
				} else if (request.get("queryType").equals(userInfo)) {
					userInfoRequest(request);
				} else if (request.get("queryType").equals(userGames)) {
					userGamesRequest(request);
				} else if (request.getString("queryType").equals(recentGame)) {
					recentGameRequest(request);
				} else if (request.getString("queryType").equals(createGame)) {
					gameCreationRequest(request);
				}
			}

		}

		private void gameCreationRequest(JSONObject request) {
			// TODO Auto-generated method stub
			conn.createGame(request.getString("player1"), request.getString("player2"));
			//apiConn.startGame(request.getString("player1"), request.getString("player2"), 10);
			try {
				this.socket.getOutputStream().write(new JSONObject().accumulate("queryResult", "success")
						.accumulate("queryType", request.get("queryType")).toString().getBytes());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void recentGameRequest(JSONObject request) {
			// TODO Auto-generated method stub
			conn.getMostRecentGame(request.getString("player1"), request.getString("player2"));
			try {
				this.socket.getOutputStream().write(conn
						.getJSONforGame(
								conn.getMostRecentGame(request.getString("player1"), request.getString("player2")))
						.accumulate("queryType", request.getString("queryType")).accumulate("queryResult", "success")
						.toString().getBytes());
			} catch (JSONException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		private void userGamesRequest(JSONObject request) {
			// TODO Auto-generated method stub
			try {
				this.socket.getOutputStream()
						.write(conn.getJSONforUserGames(request.getString("username"))
								.accumulate("queryType", request.getString("queryType"))
								.accumulate("queryResult", "success").toString().getBytes());
				this.socket.close();
			} catch (JSONException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		private void userInfoRequest(JSONObject request) {
			// TODO Auto-generated method stub
			if (conn.validateLogin(request.get("username").toString(), request.get("password").toString())) {
				conn.getUser(request.getString("username"));
				JSONObject userInfo = conn.getJSONforUser();
				if (userInfo.getString("username") != null) {
					userInfo.accumulate("queryResult", "success").accumulate("queryType",
							request.getString("queryType"));
				}
				try {
					this.socket.getOutputStream().write(userInfo.toString().getBytes());
					this.socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				this.sendErrorMessage(request);
			}

		}

		private void sendErrorMessage(JSONObject request) {
			// TODO Auto-generated method stub
			try {
				this.socket.getOutputStream().write(new JSONObject().accumulate("queryResult", "failure")
						.accumulate("queryType", null).toString().getBytes());
				this.socket.close();
			} catch (JSONException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

	}


}
