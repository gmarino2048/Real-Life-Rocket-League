package udp;

import java.io.IOException;

import APIConnection.APIConnection;

public class apitest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		APIConnection c = new APIConnection();
		
		try {
			Thread.sleep(1000);
			System.out.write("Password".getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
