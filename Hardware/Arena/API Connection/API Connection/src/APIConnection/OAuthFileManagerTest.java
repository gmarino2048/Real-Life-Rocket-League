package APIConnection;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class OAuthFileManagerTest {
	
	OAuthFileManager manager;
	
	ArrayList<String> strings;
	
	public static int LIST_SIZE = 100000;

	@BeforeEach
	void setUp() throws Exception {
		// Create a new File Manager instance
		manager = new OAuthFileManager();
		
		// Generate a list of random strings for test cases
		strings = generateRandomStrings(LIST_SIZE);
	}

	@AfterEach
	void tearDown() throws Exception {
		// Set all used variables to null
		manager = null;
		strings = null;
		
		removeFile();
		
		System.out.println((int) '\n');
		
		// Tell garbage collector to clean up to prepare for next set
		System.gc();
	}
	
	@Test
	void testNullFile () {
		// Create a blank file
		createFile();
		
		// Create boolean to catch exception
		boolean exceptionCaught = false;
		
		try {
			manager.readFile();
		} catch (Exception e) {
			// File was empty so reader should throw exception
			e.printStackTrace();
			exceptionCaught = true;
		}
		
		assertTrue(exceptionCaught);
	}
	
	@Test
	void testNullCheck () {
		// Test when all entries are null
		createFile();
		
		assertTrue(manager.nullCheck());
		
		// Create initially null string array
		String[] tempbuffer = {null, null, null, null, null, null};
		
		// Fill one entry at a time and test null check on each
		for (int i = 0; i < 5; i++) {
			tempbuffer[i] = "This is a string";
			manager.setAll(tempbuffer);
			
			assertTrue(manager.nullCheck());
		}
		
		// Fill the sixth and make sure null check is false
		tempbuffer[5] = "Final string";
		manager.setAll(tempbuffer);
		
		assertFalse(manager.nullCheck());
	}
	
	@Test
	void testOpenFile () {
		// Just try to open a new filemanager when the file already exists
		manager = null;
		
		System.gc();
		
		// Create new manager
		manager = new OAuthFileManager();
		
		String[] newBuffer = {"a","b","c","d","e","f"};
		manager.setAll(newBuffer);
		
		// Ensure new manager's file is not null
		assertFalse(manager.nullCheck());
	}
	
	@Test
	void testBuffer() {
		// Test setAll with bad inputs
		String[] tooSmall = {"a", "b", "c"};
		manager.setAll(tooSmall);
		
		assertTrue(manager.getBuffer() == null);
		
		String [] tooBig = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
		manager.setAll(tooBig);
		
		assertTrue(manager.getBuffer() == null);
		
		// Test getBuffer with null entries
		String[] nullHoles = {"a", "b", "c", null, "d", "e"};
		manager.setAll(nullHoles);
		
		assertTrue(manager.getBuffer() == null);
		
		// Generate a 5 long string array
		for (int i = 0; i < LIST_SIZE - 6; i += 6) {
			// Generate random array
			String [] compareTo = {strings.get(i), strings.get(i+1), strings.get(i+2),
					strings.get(i+3), strings.get(i+4), strings.get(i+5)};
			
			// Test set all using string array
			manager.setAll(compareTo);
			assertEquals(manager.getBuffer(), compareTo);
			
			// Test set all using separate fields
			manager.setAll(strings.get(i), strings.get(i+1), strings.get(i+2), strings.get(i+3),
					strings.get(i+4), strings.get(i+5));
			
			assertEquals(manager.getBuffer(), compareTo);
			
		}
	}

	@Test
	void testUsername() {
		// For every randomized string
		for (String regex : strings) {
			// Put the string in the username
			manager.setUsername(regex);
			
			// Assume the string has not changed
			assertEquals(manager.getUsername(), regex);
		}
	}
	
	@Test
	void testPassword() {
		// For every randomized string
		for (String regex : strings) {
			// Put the string in the password
			manager.setPassword(regex);
			
			// Assume the string has not changed
			assertEquals(manager.getPassword(), regex);
		}
	}
	
	@Test
	void testDeviceID() {
		// For every randomized string
		for (String regex : strings) {
			// Put the string in the device id
			manager.setDeviceID(regex);
			
			// Assume the string has not changed
			assertEquals(manager.getDeviceID(), regex);
		}
	}
	
	@Test
	void testClientID() {
		// For every randomized string
		for (String regex : strings) {
			// Put the string in the client id
			manager.setClientID(regex);
			
			// Assume the string has not changed
			assertEquals(manager.getClientID(), regex);
		}
	}
	
	@Test
	void testClientSecret() {
		// For every randomized string
		for (String regex : strings) {
			// Put the string in the client secret
			manager.setClientSecret(regex);
			
			// Assume the string has not changed
			assertEquals(manager.getClientSecret(), regex);
		}
	}
	
	@Test
	void testAccessToken() {
		// For every randomized string
		for (String regex : strings) {
			// Put the string in the access token
			manager.setAccessToken(regex);
			
			// Assume the string has not changed
			assertEquals(manager.getAccessToken(), regex);
		}
	}
	
	@Test
	void testWriteBuffer () {
		// Create boolean to check exception handling
		boolean exceptionHandled = false;
		
		// Create initially null string array
		String[] tempbuffer = {null, null, null, null, null, null};
		manager.setAll(tempbuffer);
		
		// Try to write with null buffer
		try {
			manager.writeBuffer();
		}
		catch (Exception e){
			exceptionHandled = true;
		}
		
		assertTrue(exceptionHandled);
		
		exceptionHandled = false;
		
		// Fill one entry at a time and test null check on each
		for (int i = 0; i < 5; i++) {
			tempbuffer[i] = "This is a string";
			manager.setAll(tempbuffer);
			
			// Try to write to buffer. Make sure exception gets caught
			try {
				manager.writeBuffer();
			}
			catch (Exception e) {
				exceptionHandled = true;
			}
			
			// Make sure exception was handled and reset boolean
			assertTrue(exceptionHandled);
			exceptionHandled = false;
		}
		
		// Fill the sixth and make sure null check is false
		tempbuffer[5] = "Final string";
		manager.setAll(tempbuffer);
		
		// Try to write buffer and make sure exception DOES NOT get handled
		try {
			manager.writeBuffer();
		}
		catch (Exception e) {
			exceptionHandled = true;
		}
		assertFalse(exceptionHandled);
	}
	
	@Test
	void testReadWrite () {
		// Create exception handled boolean
		boolean exceptionHandled = false;
		
		String[] temp = {"This", "Is", "a", "plaintext", "test", "!"};
		manager.setAll(temp);
	
		try {
			manager.writeBuffer();
			
			manager.readFile();
		}
		catch (Exception e) {
			exceptionHandled = true;
		}
		
		assertFalse(exceptionHandled);
		assertTrue(buffersEqual(temp, manager.getBuffer()));
		
		exceptionHandled = false;
	}
	
	private ArrayList<String> generateRandomStrings (int listSize) {
		// Create a new temporary list
		ArrayList<String> randomList = new ArrayList<String>();
		
		// Create a random number generator
		Random randomGenerator = new Random();
		
		// Populate the list with random strings between 0 and 64 characters
		for (int i = 0; i < listSize; i++) {
			// Create a temporary string
			String temp = "";
			
			// Get random length of string between 0 and 64
			int randLength = randomGenerator.nextInt(64);
			
			// Populate the string
			for (int j = 0; j < randLength; j++) {
				// Generate a random int between 0 and 255 inclusive
				int character = randomGenerator.nextInt(256);
				
				// Cast int to char and add to the string
				if (character != 10)
					temp += (char) character;
			}
			
			// Add the string to the List
			randomList.add(temp);
		}
		
		// Return the list
		return randomList;
	}
	
	// Creates an empty access.oauth file
	private void createFile () {
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
		
		if (!access.exists()) {
			access.getParentFile().mkdirs();
			try {
				access.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
