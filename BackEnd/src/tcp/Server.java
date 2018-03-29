package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Project 1: Create Web Proxy
 * 
 * @author Savi Medlang
 *
 */
public class Server {

	private static int PORT = 9090;
	private static ServerSocket serverSocket = null;

	public static void main(String[] args) throws IOException {

		// PORT NUMBER SPECIFIED BY ARGUMENTS. IF INVALID, USES DEFAULT PORT NUMBER
		try {
			PORT = Integer.parseInt(args[1]);
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println("Unable to process port number. Defaulting to port number: " + PORT);
		}

		/*
		 * CREATES SOCKET ON THE PORT SPECIFIED.
		 */
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("The proxy is running at port " + PORT + "!");
		} catch (Exception e) {
			System.out.println("Unable to connect to port " + PORT + ".");
			System.exit(-1);
		}

		/* INFINITE LOOP TO LAUNCH THREADS TO OPEN CONNECTIONS */
		while (true) {
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
				new RequestResponse(clientSocket).start();
			} catch (Exception e) {
				e.getStackTrace();
			}
			
		}
	}

}