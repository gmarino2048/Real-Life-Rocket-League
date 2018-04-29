import static org.junit.jupiter.api.Assertions.*;

import java.io.File;


import org.junit.jupiter.api.Test;

class APIConnectionTest {

	@Test
	void masterTest() {
		// Remove existing access.oauth file
		removeFile();
		
		// Create new connection
		APIConnection connection = new APIConnection();
		
		// Start game
		connection.startGame("USER 1", "This is user 2", 45);
		
		// Check user scores, make sure they are zero
		assertEquals(connection.getScore1(), 0);
		assertEquals(connection.getScore2(), 0);
		
		// Check usernames
		assertEquals(connection.getUsername1(), "USER 1");
		assertEquals(connection.getUsername2(), "This is user 2");
		
		// Wait for a minute
		try {
			Thread.sleep(60000);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// Void the game and clear the connection
		connection.voidGame();
		
		connection = null;
		System.gc();
		
		// Start a new API Connection to make sure the oauth file works right
		connection = new APIConnection();
		
		// The file should be readable and should start the connection on its own
		
		// Start the new game
		connection.startGame("This is user 1", "USER 2", 15);
		
		//This test should hit > 90% Code coverage over the three classes
	}
	
	// Removes the access.oauth file at the end of each test
	private void removeFile () {
		String dir;
		String operatingSystem = System.getProperty("os.name").toLowerCase();
		
		//Check to see if the OS is a version of windows
		if (operatingSystem.contains("windows")) {
			dir = "\\";
		}
		//Otherwise use a backslash
		else {
			dir = "/";
		}
		
		File access = new File(System.getProperty("user.dir") + dir + "access.oauth");
		
		if (access.exists()) {
			access.delete();
		}
	}

}
