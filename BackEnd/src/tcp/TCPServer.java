import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/** 
 * 
 * @author smedlang 
 * TCP Server to receive byte states
 */

class TCPServer {
 private static int PORT_NUM = 9000;
 public static void main(String argv[]) throws Exception {
	 System.out.println();
  String clientInput;
  ServerSocket welcomeSocket = new ServerSocket(PORT_NUM);

  while (true) {
   Socket connectionSocket = welcomeSocket.accept();
   BufferedReader inFromClient =
    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
   DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
   clientInput = inFromClient.readLine();
   System.out.println("Received: " + clientInput);
   outToClient.writeBytes("OK: " + clientInput + '\n');

  }
 }
}