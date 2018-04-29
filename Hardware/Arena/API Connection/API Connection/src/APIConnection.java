import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class APIConnection {

	// Authenticator used to save and retrieve access token and device id
	private Authenticator auth;
	
	// The OAuth access token
	private String authToken;
	// The particle device ID assigned to the arena
	private String deviceID;
	
	// This function makes a new instance of the APIConnection and gets
	// the access token and device ID from the Authenticator
	public APIConnection () {
		auth = new Authenticator();
		
		authToken = auth.getAccessToken();
		deviceID = auth.getDeviceID();
	}
	
	// This function starts a single game between two users
	// The function returns 0 upon successful completion, -1 otherwise
	public int startGame (String username1, String username2, int minutes) {
		boolean valid = true;
		
		// Set the usernames
		valid = valid && setUsername1(username1) == 0;
		valid = valid && setUsername2(username2) == 0;
		
		// Set both scores to 0
		valid = valid && setScore1(0) == 0;
		valid = valid && setScore2(0) == 0;
		
		// Initialize the time
		valid = valid && setTime(minutes) == 0;
		
		// Make sure that all functions completed successfully
		// If they did not, notify the user
		if (valid)
			return 0;
		else
			return -1;
	}
	
	// This function voids a game immediately if the players so desire
	// The function returns 0 upon successful completion, 1 otherwise
	public int voidGame () {
		boolean valid = true;
		
		// Reset the usernames to generic defaults
		valid = valid && setUsername1("USER1") == 0;
		valid = valid && setUsername2("USER2") == 0;
		
		// Reset the scores to 0
		valid = valid && setScore1(0) == 0;
		valid = valid && setScore2(0) == 0;
		
		// Reset the time to 0
		valid = valid && setTime(0) == 0;
		
		// Check to make sure all of the functions completed successfully
		// If they did not, notify the user
		if (valid)
			return 0;
		else
			return -1;
	}
	
	// This function returns an int with the current value of Player 1's score
	public int getScore1 () {
		
		// Use GET to get int from the API, return -1 if impossible
		try {
			return getInt("score1");
		} 
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	// This function returns an int with the current value of Player 2's score
	public int getScore2 () {
		
		// Use GET to get int value of score 2. If this is impossible, return -1
		try {
			return getInt("score2");
		} 
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	// This function returns the String value of Player 1's username
	public String getUsername1 () {
		
		// Use Get to get String value of the username, if impossible return -1
		try {
			return getString("username1");
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	// This function returns the string value of Player 2's username
	public String getUsername2 () {
		
		// Use Get to get String value of the username, if impossible return -1
		try {
			return getString("username2");
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	// Sets the 1st player's score
	public int setScore1 (int value) {
		try {
			// Set the score, then push the score to the arena
			int setScore = postInt("setscore1", value);
			int sendScore = postString("sendscore1", "");
			
			// Ensure both functions completed successfully
			if (setScore == 0 && sendScore == 0) {
				return 0;
			}
			// If not return -1
			else return -1;
		}
		// Print error and return -1 if there was an exception
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	// This function sets player 2's score in the arena
	public int setScore2 (int value) {
		try {
			// Set the score, then push the score value to the arena
			int setScore = postInt("setscore2", value);
			int sendScore = postString("sendscore2", "");
			
			// Ensure both functions completed successfully
			if (setScore == 0 && sendScore == 0) {
				return 0;
			}
			// If not, return -1
			else return -1;
		}
		// Print exception and return -1 if either of above throw exception
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	// This function sets player 1's username on the arena display
	public int setUsername1 (String value) {
		try {
			// Set username and push to arena
			int setUsername = postString("setuname1", value);
			int sendUsername = postString("senduname1", "");
			
			// Check for valid completion
			if (setUsername == 0 && sendUsername == 0) {
				return 0;
			}
			// Return -1 if error exists
			else return -1;
		}
		// Return -1 if exception handled
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	// This function sets the second player's username on the arena display
	public int setUsername2 (String value) {
		try {
			// Set username, then push to arena
			int setUsername = postString("setuname2", value);
			int sendUsername = postString("senduname2", "");
			
			// Check for validity
			if (setUsername == 0 && sendUsername == 0) {
				return 0;
			}
			// Return -1 on invalid completion
			else return -1;
		}
		// Return -1 if exception handled
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	// This function sets the arena clock time, in minutes
	public int setTime (int minutes) {
		try {
			// Set time and push to arena
			int setTime = postInt("settime", minutes);
			int sendTime = postString("sendtime", "");
			
			// Check function for completion
			if (setTime == 0 && sendTime == 0) {
				return 0;
			}
			// If incomplete, return -1
			else return -1;
		}
		// Return -1 on exception handled
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	
	// Retrieves an integer value from the Particle API using GET
	private int getInt (String variableName) throws Exception {
		// Build the path to the variable
		String urlName = buildVariableURL(variableName);
		
		// Create the URL and establish connection to API, also set request method
		URL url = new URL(urlName);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		// If connection is valid...
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			// Generate response string and read response bytewise from API
			String response = "";
			InputStream in = connection.getInputStream();
			
			// Read the response
			int readVal = in.read();
			while (readVal != -1) {
				response += (char) readVal;
				readVal = in.read();
			}
			
			// Read the "result" returned by the API in JSON format
			JSONObject json = new JSONObject(response);
			int value = json.getInt("result");
			return value;
		}
		// If connection invalid, throw exception
		else {
			throw new Exception("Bad Response from URL when reading int");
		}
	}
		
	
	// This function fetches a named String value from the particle API
	private String getString (String variableName) throws Exception{
		// Build path to variable
		String urlName = buildVariableURL(variableName);
		
		// Establish connection and set request method to "GET"
		URL url = new URL(urlName);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		// If response OK...
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			// Generate response string and...
			String response = "";
			InputStream in = connection.getInputStream();
			
			// Read the data bytewise from the API
			int readVal = in.read();
			while (readVal != -1) {
				response += (char) readVal;
				readVal = in.read();
			}
			
			// Extract the string value from the JSON object returned, under "result"
			JSONObject json = new JSONObject(response);
			String value = json.getString("result");
			return value;
		}
		// If connection is bad, throw an exception
		else {
			throw new Exception("Bad Response from URL when reading String");
		}
	}
	
	// This function posts an int value to one of the particle API's functions
	private int postInt (String functionName, int arguments) throws Exception{
		// Generate path to function
		String urlName = buildFunctionURL(functionName);
		
		// Generate POST arguments using int val and access token
		String args = "";
		args += "arg=" + arguments;
		args += "&access_token=" + authToken;
		
		// Establish connection and set headers to mimic cURL request
		URL url = new URL(urlName);
		
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", Integer.toString(args.length()));
		connection.setRequestProperty("charset", "UTF-8");
		
		// Write the argument data
		DataOutputStream bw = new DataOutputStream(connection.getOutputStream());
		
		bw.write(args.getBytes("UTF-8"));
		
		// If connection OK
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			// Generate response string and...
			String response = "";
			InputStream in = connection.getInputStream();
			
			// Read response bytewise
			int readVal = in.read();
			while (readVal != -1) {
				response += (char) readVal;
				readVal = in.read();
			}
			
			// Get function status from JSON object
			JSONObject json = new JSONObject(response);
			int value = json.getInt("return_value");
			return value;
		}
		// If connection is bad, throw an exception
		else {
			throw new Exception("Bad Response from URL when reading int");
		}
	}
	
	// This function calls a named particle function with a string argument
	private int postString (String functionName, String arguments) throws Exception{
		// Generate path to function
		String urlName = buildFunctionURL(functionName);
		
		// Generate arguments for function, along with access token
		String args = "";
		args += "arg=" + arguments;
		args += "&access_token=" + authToken;
		
		// Establish connection and set headers to mimic cURL request
		URL url = new URL(urlName);
		
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", Integer.toString(args.length()));
		connection.setRequestProperty("charset", "UTF-8");
		
		// Write argument data
		DataOutputStream bw = new DataOutputStream(connection.getOutputStream());
		
		bw.write(args.getBytes("UTF-8"));
		
		// If connection OK...
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			// Create response string
			String response = "";
			InputStream in = connection.getInputStream();
			
			// Read response bytewise
			int readVal = in.read();
			while (readVal != -1) {
				response += (char) readVal;
				readVal = in.read();
			}
			
			// Extract function status from JSON object
			JSONObject json = new JSONObject(response);
			int value = json.getInt("return_value");
			return value;
		}
		// If connection is bad, throw an exception
		else {
			throw new Exception("Bad Response from URL when reading int");
		}
	}
	
	// This function builds the URL used to get to a particle variable
	private String buildVariableURL (String variableName) {
		// Set the base URL
		String url = "https://api.particle.io/v1/devices/";
		
		// Include device ID
		url += deviceID;
		url += "/";
		
		// Set the variable name
		url += variableName;
		
		// Set the access token
		url += "?access_token=";
		url += authToken;
		
		return url;
	}
	
	
	// This function builds the URL used to access a particle function
	private String buildFunctionURL (String functionName) {
		// Set the base URL
		String url = "https://api.particle.io/v1/devices/";
		
		// Specify the device ID
		url += deviceID;
		url += "/";
		
		// Set the name of the function
		url += functionName;
		
		return url;
	}
}
