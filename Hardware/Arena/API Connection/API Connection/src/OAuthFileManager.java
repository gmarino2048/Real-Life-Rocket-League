/*
 * SocCar Project
 * 
 * OAuthFileManager
 * 
 * 
 */

import java.io.*;

public class OAuthFileManager {
	
	//A buffer to store the read/write information
	private String[] informationBuffer;
	
	//The file where the information is stored
	private File accessFile;
	
	//The reader and writer to the file
	private BufferedReader reader;
	private BufferedWriter writer;
	
	//The type of char used to separate directories
	private char dir;
	
	//The number of fields in the buffer
	private static final int FIELDS = 6;
	
	//The respective line numbers and buffer locations of the information
	private static final int USERNAME = 0;
	private static final int PASSWORD = 1;
	private static final int DEVICEID = 2;
	private static final int CLIENTID = 3;
	private static final int CLIENTSECRET = 4;
	private static final int TOKEN = 5;
	
	//The name of the file
	private static final String FILENAME = "access.oauth";
	
	
	public OAuthFileManager () {
		informationBuffer = new String[FIELDS];
		getDirectoryFormat();
		getFile();
	}
	
	/*
	 * setAll
	 */
	public void setAll (String username, String password, String deviceID,
			String clientid, String clientsecret, String token) {
		//Put all strings in their relative positions inside the buffer
		informationBuffer[USERNAME] = username;
		informationBuffer[PASSWORD] = password;
		informationBuffer[DEVICEID] = deviceID;
		informationBuffer[CLIENTID] = clientid;
		informationBuffer[CLIENTSECRET] = clientsecret;
		informationBuffer[TOKEN] = token;
	}
	
	/*
	 * setAll
	 */
	public void setAll (String[] newBuffer) {
		//Check to make sure the new buffer is the right length
		if (newBuffer.length == FIELDS) {
			informationBuffer = newBuffer;
		}
		//Otherwise keep the old buffer and notify user
		else {
			System.out.println("Error: Wrong buffer size");
		}
	}
	
	/*
	 * getBuffer
	 */
	public String[] getBuffer () {
		boolean empty = false;
		
		//Check to make sure none of the entries are empty
		for(String regex : informationBuffer) {
			if (regex == null) {
				empty = true;
			}
		}
		
		//If there are no empty spaces, return the current informationBuffer
		if (!empty) {
			return informationBuffer;
		}
		//Otherwise produce a null result
		else return null;
	}
	
	/*
	 * setUsername
	 */
	public void setUsername (String newUsername) {
		informationBuffer[USERNAME] = newUsername;
	}
	
	/*
	 * getUsername
	 */
	public String getUsername () {
		return informationBuffer[USERNAME];
	}
	
	/*
	 * setPassword
	 */
	public void setPassword (String newPassword) {
		informationBuffer[PASSWORD] = newPassword;
	}
	
	/*
	 * getPassword
	 */
	public String getPassword () {
		return informationBuffer[PASSWORD];
	}
	
	/*
	 * setDeviceID
	 */
	public void setDeviceID (String newDeviceID) {
		informationBuffer[DEVICEID] = newDeviceID;
	}
	
	/*
	 * getDeviceID
	 */
	public String getDeviceID () {
		return informationBuffer[DEVICEID];
	}
	
	/*
	 * setClientID
	 */
	public void setClientID (String newClientID) {
		informationBuffer[CLIENTID] = newClientID;
	}
	
	
	
	/*
	 * getAccessToken
	 */
	public String getClientID () {
		return informationBuffer[CLIENTID];
	}
	
	/*
	 * setClientSecret
	 */
	public void setClientSecret (String newClientSecret) {
		informationBuffer[CLIENTSECRET] = newClientSecret;
	}
	
	/*
	 * getClientSecret
	 */
	public String getClientSecret () {
		return informationBuffer[CLIENTSECRET];
	}
	
	/*
	 * setAccessToken
	 */
	public void setAccessToken (String newToken) {
		informationBuffer[TOKEN] = newToken;
	}
	
	/*
	 * getClientSecret
	 */
	public String getAccessToken () {
		return informationBuffer[TOKEN];
	}

	
	/*
	 * getDirectoryFormat
	 */
	private void getDirectoryFormat () {
		//Get the operating System of the machine
		String operatingSystem = System.getProperty("os.name").toLowerCase();
		
		//Check to see if the OS is a version of windows
		if (operatingSystem.contains("windows")) {
			dir = '\\';
		}
		//Otherwise use a backslash
		else {
			dir = '/';
		}
	}
	
	/*
	 * getCWD
	 */
	private String getCWD () {
		return System.getProperty("user.dir");
	}
	
	/*
	 * getFile
	 */
	private void getFile () {
		//Get the current working directory
		String cwd = getCWD();
		
		//Initialize the file to the cwd with the file extension
		File information = new File(cwd + dir + FILENAME);
		
		//Try to create/open the file
		try {
			information.getParentFile().mkdirs();
			if (information.createNewFile()) {
				System.out.println("Created the file");
			}
			else {
				System.out.println("File already exists");
			}
			//Set the new file as the one to be used.
			accessFile = information;
			
		}
		//Return false if the file could not be created or opened.
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * nullCheck
	 */
	public boolean nullCheck () {
		boolean fileIsNull = accessFile == null;
		boolean bufferIsNull = false;
		
		//Check for a null entry in all of regex
		for(String regex : informationBuffer) {
			if (regex == null) {
				bufferIsNull = true;
			}
		}
		
		//Return true if file is null or buffer entry is null
		return fileIsNull || bufferIsNull;
	}
	
	/*
	 * writeBuffer
	 */
	public void writeBuffer () throws Exception {
		// Exit the function if any of the fields are null
		if (nullCheck()) {
			System.out.println("Error: Some of the buffer fields are null");
			return;
		}
		
		// Try to write all the fields to the file
		try {
			writer = new BufferedWriter(new FileWriter(accessFile));
			
			for (int i = 0; i < FIELDS; i++) {
				writer.write(informationBuffer[i] + '\n');
			}
			
			writer.close();
		}
		// If something goes wrong, close the writer
		catch (Exception e) {
			e.printStackTrace();
			try {
				writer.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception(ex);
			}
			throw new Exception(e);
		}
	}
	
	
	/*
	 * readFile
	 */
	public void readFile () throws Exception {
		//Attempt to read from the file
		try {
			reader = new BufferedReader(new FileReader(accessFile));
			String[] temporaryBuffer = new String[FIELDS];
			
			for (int i = 0; i < FIELDS; i++) {
				temporaryBuffer[i] = reader.readLine();
				//Debug check line. If not reading properly
				//System.out.println("Read " + temporaryBuffer[i] + " from file.");
			}
			
			//Check to make sure the file wasn't empty
			boolean syntaxOK = true;
			for (int j = 0; j < FIELDS; j++) {
				if (temporaryBuffer[j] == null) {
					syntaxOK = false;
				}
			}
			
			if (syntaxOK) {
				setAll(temporaryBuffer);
			}
			else {
				throw new Exception ("File was partially or fully empty");
			}
		}
		//Catch any exceptions that occur
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
}
