import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
	
	private boolean buffersEqual (String[] buffer1, String[] buffer2) {
		boolean areEqual = true;
		
		if (buffer1.length == buffer2.length) {
			for (int i = 0; i < buffer1.length; i++) {
				areEqual = areEqual && buffer1[i].equals(buffer2[i]);
				if (!areEqual) {
					System.out.println(buffer1[i] + '\t' + buffer2[i]);
				}
			}
			
			return areEqual;
		}
		else return false;
	}

}
