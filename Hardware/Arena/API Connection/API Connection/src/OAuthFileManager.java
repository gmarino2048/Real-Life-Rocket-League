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
	
	// The constructor initializes the information buffer, gets the directory format,
	// and opens the file.
	public OAuthFileManager () {
		informationBuffer = new String[FIELDS];
		getDirectoryFormat();
		getFile();
	}
	
	// Sets all of the fields in th buffer at once
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
	
	// Sets all the elements in the buffer with new elements at a single time
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
	
	// Returns the entire string array of the buffer
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
	
	// Writes the username to the buffer
	public void setUsername (String newUsername) {
		informationBuffer[USERNAME] = newUsername;
	}
	
	// Returns the username from the buffer
	public String getUsername () {
		return informationBuffer[USERNAME];
	}
	
	// Writes the password to the buffer
	public void setPassword (String newPassword) {
		informationBuffer[PASSWORD] = newPassword;
	}
	
	// Returns the password from the buffer
	public String getPassword () {
		return informationBuffer[PASSWORD];
	}
	
	// Writes the device ID to the buffer
	public void setDeviceID (String newDeviceID) {
		informationBuffer[DEVICEID] = newDeviceID;
	}
	
	// Gets the device ID from the buffer
	public String getDeviceID () {
		return informationBuffer[DEVICEID];
	}
	
	// Writes the client ID to the buffer
	public void setClientID (String newClientID) {
		informationBuffer[CLIENTID] = newClientID;
	}
	
	// Returns the client ID from the buffer
	public String getClientID () {
		return informationBuffer[CLIENTID];
	}
	
	// Writes the client secret to the buffer
	public void setClientSecret (String newClientSecret) {
		informationBuffer[CLIENTSECRET] = newClientSecret;
	}
	
	// Returns the client secret from the buffer
	public String getClientSecret () {
		return informationBuffer[CLIENTSECRET];
	}
	
	// Writes the access token to the buffer
	public void setAccessToken (String newToken) {
		informationBuffer[TOKEN] = newToken;
	}
	
	// Returns the access token from the buffer
	public String getAccessToken () {
		return informationBuffer[TOKEN];
	}

	
	// Formats the directory slashes so that they go the right way for the OS
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
	
	// This function gets the Current Working Directory of wherever this function was called from
	// Returns a string with the path of the current working directory
	private String getCWD () {
		return System.getProperty("user.dir");
	}
	
	// This function follows the directory path to the file and opens it.
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
	
	// Check to make sure none of the fields are null
	// Return true if they are all filled, false otherwise
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
	
	// This function writes the current buffer to the access.oauth file
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
	
	
	// This function attempts to read from the access.oauth file
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
