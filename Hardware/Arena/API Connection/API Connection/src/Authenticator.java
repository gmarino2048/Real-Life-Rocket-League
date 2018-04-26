import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Authenticator {
	
	public static void main (String[] args) {
		Authenticator auth = new Authenticator();
		auth.retrieve();
	}
	
	private OAuthFileManager fileManager;
	
	private String encryptionKey;
	
	private String username;
	private String password;
	private String deviceid;
	private String accessToken;
	
	public Authenticator () {
		fileManager = new OAuthFileManager();
	}
	
	public String getAccessToken () throws Exception{
		return "";
	}
	
	public void stash () {
		
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
			accessToken = decrypt(fileManager.getAccessToken(), encryptionKey);
		}
		catch (BadPaddingException bpe) {
			if (bpe.getMessage().equals("javax.crypto.BadPaddingException: Given final block not properly padded") && count < 5) {
				setEncryptionKey("Bad encryption key, try again");
				retrieve();
				count ++;
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
			System.out.println("Unable to retreive from file... getting user info.");
			getUserInfo();
			return;
		}
	}
	
	public void getUserInfo () {
		setUsername();
		setPassword();
		setDeviceID();
		
		try {
			getAccessToken();
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
	
	public static String decrypt (String hash, String key) throws Exception{
		String data;
		
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] decryptedString = cipher.doFinal(Base64.getDecoder().decode(hash));
			data = new String(decryptedString);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
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
}