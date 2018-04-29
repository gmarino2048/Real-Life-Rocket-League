import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

public class Authenticator {
	
	// The base url used to access Particle token generation
	private static String BASE_URL = "https://api.particle.io/oauth/token";
	
	// The File Manager used to store and retrieve the information
	private OAuthFileManager fileManager;
	
	// The encryption key used to encrypt and decrypt the information
	private String encryptionKey;
	
	// The particle account username: "something@email.com"
	private String username;
	// The particle account password
	private String password;
	// The particle device ID of the arena, found at https://console.particle.io/
	private String deviceid;
	// The client ID of the service, see the text file "ID and Secret.txt"
	private String clientid;
	// The client Secret of the service, see the text file "ID and Secret.txt"
	private String clientsecret;
	// The access token used to get and send data with the API
	private String accessToken;
	
	// The Authenticator Constructor
	// Creates a new file manager for data and sets the bad password count to 0. Attempts to
	// retrieve stored data.
	public Authenticator () {
		fileManager = new OAuthFileManager();
		count = 0;
		retrieve();
	}
	
	// Returns the access token of the Authenticator
	public String getAccessToken() {
		return accessToken;
	}
	
	// Returns the device ID of the Authenticator
	public String getDeviceID () {
		return deviceid;
	}
	
	// Fetches an access token from particle's website that never expires
	public void fetchAccessToken () throws Exception{
		// Make sure none of the fields are null
		boolean usernameIsNull = username == null;
		boolean passwordIsNull = password == null;
		boolean clientidIsNull = clientid == null;
		boolean clientsecretIsNull = clientsecret == null;
		
		// If they are... retrieve new info
		if (usernameIsNull || passwordIsNull || clientidIsNull || clientsecretIsNull) {
			retrieve();
		}
		
		// Generate the arguments to be used in the POST request
		String toSend = "";
		
		toSend += "client_id=" + clientid;
		toSend += "&client_secret=" + clientsecret;
		toSend += "&grant_type=password";
		toSend += "&username=" + username;
		toSend += "&password=" + password;
		toSend += "&expires_in=0";
		
		try {
			// Attempt a connection with the token generation server
			URL url = new URL(BASE_URL);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			// Set request type to "POST" and proper headers to mimic cURL request
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", Integer.toString(toSend.length()));
			connection.setRequestProperty("charset", "UTF-8");
			
			// Write the arguments to the connection
			DataOutputStream bw = new DataOutputStream(connection.getOutputStream());
			
			bw.write(toSend.getBytes("UTF-8"));
			
			// If the connection is OK and data was accepted
			String rawresponse ;
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				rawresponse = "";
	            
				// Read the response from the server bytewise and...
				int character = inputStream.read();
				while (character != -1) {
					rawresponse += (char) character;
					character = inputStream.read();
				}
				
				// Get the access token from the responding JSON
				JSONObject response = new JSONObject(rawresponse);
				accessToken = response.getString("access_token");
				System.out.println(accessToken);
			}
			// If connection is not okay, then throw an exception
			else {
				throw new Exception ("Bad response from server");
			}
		}
		// Forward any caught exceptions
		catch (Exception e) {
			throw e;
		}
	}
	
	
	// This function encrypts the data and stores it in the access.oauth file
	public void stash () {
		try {
			// Make sure encryption key is not null
			while (encryptionKey == null) {
				// If it is, set it
				setEncryptionKey("Encryption Key not set.");
			}
			
			// Encrypt ALL the data
			String encUsername = encrypt(username, encryptionKey);
			String encPassword = encrypt(password, encryptionKey);
			String encDeviceID = encrypt(deviceid, encryptionKey);
			String encClientID = encrypt(clientid, encryptionKey);
			String encClientSecret = encrypt(clientsecret, encryptionKey);
			String encToken = encrypt(accessToken, encryptionKey);
			
			// Set encrypted data in file manager
			fileManager.setAll(encUsername, encPassword, encDeviceID, encClientID, encClientSecret, encToken);
			
			// Write to file
			fileManager.writeBuffer();
		}
		// Catch and print any exceptions
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// The number of bad password attempts
	private int count;
	
	public void retrieve () {
		try {
			// Attempt to load data from file
			fileManager.readFile();
			
			// Make sure encryption key is not null
			if (encryptionKey == null) {
				// If it is, set it
				setEncryptionKey();
			}
			
			// Try to decrypt all info
			username = decrypt(fileManager.getUsername(), encryptionKey);
			password = decrypt(fileManager.getPassword(), encryptionKey);
			deviceid = decrypt(fileManager.getDeviceID(), encryptionKey);
			clientid = decrypt(fileManager.getClientID(), encryptionKey);
			clientsecret = decrypt(fileManager.getClientSecret(), encryptionKey);
			accessToken = decrypt(fileManager.getAccessToken(), encryptionKey);
		}
		// This exception is thrown if the encryption key is bad
		catch (javax.crypto.BadPaddingException bpe) {
			// Prompt the user for a new key if password is bad
			if (count < 5) {
				setEncryptionKey("Bad encryption key, try again");
				count ++;
				retrieve();
			}
			// Get user info manually if more than 5 bad attempts in a row
			else if (count >= 5) {
				System.out.println("Number of attempts exceeded");
				getUserInfo();
				count = 0;
			}
			// Just get user info manually in case something goes wrong
			else {
				System.out.println("Something went wrong... getting user info manually.");
				getUserInfo();
				return;
			}
		}
		// If the data can't be retrieved just get it from the user
		catch (Exception e) {
			System.out.println("Unable to retreive from file... getting user info.");
			getUserInfo();
			return;
		}
	}
	
	// Gets all of the user info from System.in
	public void getUserInfo () {
		setUsername();
		setPassword();
		setDeviceID();
		setClientID();
		setClientSecret();
		
		// Try to fetch the access token
		try {
			fetchAccessToken();
			stash();
		}
		// Try again if getting token failed
		catch (Exception e) {
			System.out.println("Could not generate access token with given info");
			getUserInfo();
		}
	}
	
	// Gets the encryption key after displaying a message
	public void setEncryptionKey(String message) {
		System.out.println(message);
		setEncryptionKey();
	}
	
	// Sets the encryption key from stdin
	public void setEncryptionKey () {
		// Open the buffered reader to read from stdin
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		String temp;
		
		// Try to read the key
		System.out.println("Enter cipher key:");
		try {
			temp = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// Prompt user for confirmation
		String confirmation;
		System.out.println("Your entered key is: " + temp + "\nIs that correct? (y/n)");
		try {
			confirmation = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// Verify confirmation. If confirmation is bad, try again.
		if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
			encryptionKey = temp;
		}
		else {
			setEncryptionKey();
		}
	}
	
	// Encrypts a string to another String value
	// Returns the encrypted string
	public static String encrypt (String text, String key) throws Exception {
		String data;
		try {
			// Set up the cipher info
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			
			// Encrypt the string's byte array and then encode it into another string
			byte[] encryptedString = cipher.doFinal(text.getBytes());
			data = Base64.getEncoder().encodeToString(encryptedString);
		}
		// Throw an exception if anything goes wrong
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		
		// Return the encrypted String
		return data;
	}
	
	// Takes an encrypted string and attempts to decrypt it with the current encryption key
	// Returns the decrypted string
	public static String decrypt (String hash, String key) throws BadPaddingException, Exception {
		String data;
		try {
			// Set up the cipher info
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			
			// Decrypt the string's byte array and store the result in data
			byte[] decryptedString = cipher.doFinal(Base64.getDecoder().decode(hash));
			data = new String(decryptedString);
		}
		// Forward the bad key exception
		catch (BadPaddingException e) {
			throw e;
		}
		// Forward any other exceptions
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		// Return the decrypted String
		return data;
	}
	
	/*
	 * NOTE: The following functions will not be commented
	 * 
	 * Why? because they are literally cookie cutter string functions in the same exact format
	 * as setEncryptionKey. They take user input, and prompt for confirmation. That's all. If you
	 * really want to know what's going on, look at the comments for setEncryptionKey. It's all
	 * the same stuff.
	 */
	
	private void setUsername () {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		String temp;
		
		System.out.println("Enter your username:");
		try {
			temp = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		String confirmation;
		System.out.println("Your entered username is: " + temp + "\nIs that correct? (y/n)");
		try {
			confirmation = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
			username = temp;
		}
		else {
			setUsername();
		}
	}
	
	private void setPassword () {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		String temp;
		
		System.out.println("Enter your password:");
		try {
			temp = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		String confirmation;
		System.out.println("Your entered password is: " + temp + "\nIs that correct? (y/n)");
		try {
			confirmation = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
			password = temp;
		}
		else {
			setPassword();
		}
	}
	
	private void setDeviceID () {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		String temp;
		
		System.out.println("Enter your device's ID:");
		try {
			temp = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		String confirmation;
		System.out.println("Your entered ID is: " + temp + "\nIs that correct? (y/n)");
		try {
			confirmation = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
			deviceid = temp;
		}
		else {
			setDeviceID();
		}
	}
	
	private void setClientID () {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		String temp;
		
		System.out.println("Enter your client ID:");
		try {
			temp = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		String confirmation;
		System.out.println("Your entered ID is: " + temp + "\nIs that correct? (y/n)");
		try {
			confirmation = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
			clientid = temp;
		}
		else {
			setClientID();
		}
	}
	
	private void setClientSecret () {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		String temp;
		
		System.out.println("Enter your client secret:");
		try {
			temp = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		String confirmation;
		System.out.println("Your entered secret is: " + temp + "\nIs that correct? (y/n)");
		try {
			confirmation = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
			clientsecret = temp;
		}
		else {
			setClientSecret();
		}
	}
}