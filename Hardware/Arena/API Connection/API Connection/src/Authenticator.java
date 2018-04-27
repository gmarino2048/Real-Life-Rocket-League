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
	
	private static String BASE_URL = "https://api.particle.io/oauth/token";
	
	private OAuthFileManager fileManager;
	
	private String encryptionKey;
	
	private String username;
	private String password;
	private String deviceid;
	private String clientid;
	private String clientsecret;
	private String accessToken;
	
	public Authenticator () {
		fileManager = new OAuthFileManager();
		count = 0;
		retrieve();
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public String getDeviceID () {
		return deviceid;
	}
	
	public void fetchAccessToken () throws Exception{
		boolean usernameIsNull = username == null;
		boolean passwordIsNull = password == null;
		boolean clientidIsNull = clientid == null;
		boolean clientsecretIsNull = clientsecret == null;
		
		if (usernameIsNull || passwordIsNull || clientidIsNull || clientsecretIsNull) {
			retrieve();
		}
		
		String toSend = "";
		
		toSend += "client_id=" + clientid;
		toSend += "&client_secret=" + clientsecret;
		toSend += "&grant_type=password";
		toSend += "&username=" + username;
		toSend += "&password=" + password;
		toSend += "&expires_in=0";
        
		System.out.println(toSend);
		
		try {
			URL url = new URL(BASE_URL);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", Integer.toString(toSend.length()));
			connection.setRequestProperty("charset", "UTF-8");
			
			
			DataOutputStream bw = new DataOutputStream(connection.getOutputStream());
			
			bw.write(toSend.getBytes("UTF-8"));
			
			String rawresponse ;
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				rawresponse = "";
	            
				int character = inputStream.read();
				while (character != -1) {
					rawresponse += (char) character;
					character = inputStream.read();
				}
				
				JSONObject response = new JSONObject(rawresponse);
				accessToken = response.getString("access_token");
				System.out.println(accessToken);
			}
			else {
				throw new Exception ("Bad response from server");
			}
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	public void stash () {
		try {
			while (encryptionKey == null) {
				setEncryptionKey("Encryption Key not set.");
			}
			
			String encUsername = encrypt(username, encryptionKey);
			String encPassword = encrypt(password, encryptionKey);
			String encDeviceID = encrypt(deviceid, encryptionKey);
			String encClientID = encrypt(clientid, encryptionKey);
			String encClientSecret = encrypt(clientsecret, encryptionKey);
			String encToken = encrypt(accessToken, encryptionKey);
			
			
			fileManager.setAll(encUsername, encPassword, encDeviceID, encClientID, encClientSecret, encToken);
			
			
			fileManager.writeBuffer();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int count;
	
	public void retrieve () {
		try {
			fileManager.readFile();
			
			if (encryptionKey == null) {
				setEncryptionKey();
			}
			
			username = decrypt(fileManager.getUsername(), encryptionKey);
			password = decrypt(fileManager.getPassword(), encryptionKey);
			deviceid = decrypt(fileManager.getDeviceID(), encryptionKey);
			clientid = decrypt(fileManager.getClientID(), encryptionKey);
			clientsecret = decrypt(fileManager.getClientSecret(), encryptionKey);
			accessToken = decrypt(fileManager.getAccessToken(), encryptionKey);
		}
		catch (javax.crypto.BadPaddingException bpe) {
			if (count < 5) {
				setEncryptionKey("Bad encryption key, try again");
				count ++;
				retrieve();
			}
			else if (count >= 5) {
				System.out.println("Number of attempts exceeded");
				getUserInfo();
				count = 0;
			}
			else {
				System.out.println("Something went wrong... getting user info manually.");
				getUserInfo();
				return;
			}
		}
		catch (Exception e) {
			
			System.out.println(e.getClass().toString());
			System.out.println("Unable to retreive from file... getting user info.");
			getUserInfo();
			return;
		}
	}
	
	public void getUserInfo () {
		setUsername();
		setPassword();
		setDeviceID();
		setClientID();
		setClientSecret();
		
		try {
			fetchAccessToken();
			stash();
		}
		catch (Exception e) {
			System.out.println("Could not generate access token with given info");
			getUserInfo();
		}
	}
	
	
	
	public void setEncryptionKey(String message) {
		System.out.println(message);
		
		setEncryptionKey();
	}
	
	public void setEncryptionKey () {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		String temp;
		
		System.out.println("Enter cipher key:");
		try {
			temp = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		String confirmation;
		System.out.println("Your entered key is: " + temp + "\nIs that correct? (y/n)");
		try {
			confirmation = inputReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
			encryptionKey = temp;
		}
		else {
			setEncryptionKey();
		}
	}
	
	public static String encrypt (String text, String key) throws Exception {
		String data;
		
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] encryptedString = cipher.doFinal(text.getBytes());
			data = Base64.getEncoder().encodeToString(encryptedString);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		
		return data;
	}
	
	public static String decrypt (String hash, String key) throws BadPaddingException, Exception {
		String data;
		
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] decryptedString = cipher.doFinal(Base64.getDecoder().decode(hash));
			data = new String(decryptedString);
		}
		catch (BadPaddingException e) {
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return data;
		
	}
	
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