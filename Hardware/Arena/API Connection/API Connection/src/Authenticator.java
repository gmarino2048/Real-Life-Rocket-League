/*
 * Soccar Project
 * 
 * Class Authenticator
 * 
 * This class is designed to store the authentication information
 * required to log into the particle site. The authentication
 * information is then stored as an encrypted string where it can
 * be recovered later (on server boot).
 * 
 * Data Summary:
 * 
 * Method Summary:
 * 
 */
import java.security.spec.*;
import java.util.Scanner;

import javax.crypto.*;
import javax.crypto.spec.*;Web Front End
Back End Server
Car
Arena


public class Authenticator {
	
	// The username to log into the particle cloud
	private String username;
	
	// The password for the particle cloud
	private String password;
	
	// The device ID to connect to
	private String deviceID;
	
	// The OAuth 2.0 access tokens
	private String key1;
	private String key2;
	
	// The OAuth file manager which saves the information
	private OAuthFileManager fileManager;
	
	// The key to decrypt the access.oauth file
	private String encryptionKey;
	
	// Data members necessary for encryption
	private static final String ENCRYPTION_SCHEME = "DESede";
	private static final String CHARSET = "UTF8";
	private KeySpec keySpec;
	private SecretKeyFactory skFactory;
	private Cipher cipher;
	private SecretKey secretKey;
	
	/*
	 * Constructor
	 * 
	 * The Authenticator constructor takes no arguments and is
	 * meant to be called once so that the variables are initialized
	 * properly.
	 * 
	 */
	public Authenticator () {
		// Create the file manager used to update the access.oauth file
		fileManager = new OAuthFileManager();
	}
	
	/*
	 * setEncryptionKey
	 * 
	 * This method takes the user specified encryption key from stdin and
	 * resets the encryption key. This method is usually called if the 
	 * encryption key is not already set or if the user calls the function.
	 * 
	 */
	public void setEncryptionKey () {
		// Create the scanner to listen to user input
		Scanner scan = new Scanner(System.in);
		
		// Print out the standard messages and read the user input
		System.out.println("Enter the new encryption key below:");
		String newEncryptionKey = scan.nextLine();
		
		// Get user confirmation that the key is correct
		System.out.println("The new encryption key entered is: " + newEncryptionKey);
		System.out.println("Is this correct? (y/n)");
		String confirmation = scan.nextLine();
		
		// Update the encryption Key and close the scanner
		if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
			encryptionKey = newEncryptionKey;
			scan.close();
		}
		
		// Retry getting the encryption Key
		else {
			scan.close();
			setEncryptionKey();
		}
	}
	
	/*
	 * setEncryptionKey
	 * 
	 * This method uses the previously defined setEncryptionKey method
	 * to update the encryption key. This method enables a message to be
	 * sent to the user in order to update the encryption key.
	 * 
	 * Arguments:
	 * message, the message to display to the user
	 * 
	 */
	public void setEncryptionKey (String message) {
		// Send the output to the Server manager
		System.out.println(message);
		
		// Update the encryption Key
		setEncryptionKey();
	}
	
	public String getSecureUserInput (String message) {
		return "";
	}
	
	public String getUserName () {
		return "";
	}
	
	public void UpdateInformation () {
		
	}
	
	public void setUsername (String newUserName) {
		
	}
	
	public void setPassword (String newPassword) {
		
	}
	
	public void setDeviceID (String newDeviceID) {
		
	}
	
	public void SetAuthenticationTokens (String newToken1, String newToken2) {
		
	}
	
	/*
	 * encrypt
	 * 
	 * This method encrypts a string using the DESede scheme
	 * 
	 * Arguments:
	 * regex, the unencryped string
	 * 
	 * Returns:
	 * A new string encrypted based on encryptionKey
	 */
	private String encrypt (String regex) {
		//Check that encryption key exists
		if (encryptionKey != null) {
			try {
				//Show the manager the encryption key
				System.out.println("The key used for this encryption is:\n" + encryptionKey);
				
				//Convert encryption key to byte array
				byte[] key = encryptionKey.getBytes(CHARSET);
				
				//Set up the encryption
				keySpec = new DESedeKeySpec(key);
				skFactory = SecretKeyFactory.getInstance(ENCRYPTION_SCHEME);
				cipher = Cipher.getInstance(ENCRYPTION_SCHEME);
				secretKey = skFactory.generateSecret(keySpec);
				
				//Encrypt the string
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
				byte[] unencrypted = regex.getBytes(CHARSET);
				byte[] encrypted = cipher.doFinal(unencrypted);
				return new String(encrypted, CHARSET);
			}
			catch (Exception e) {
				//Print error
				e.printStackTrace(System.out);
				
				//Input new key and try again
				setEncryptionKey("Encryption Failed.");
				return encrypt(regex);
			}
		}
		//Otherwise prompt for encryption key and try again
		else {
			setEncryptionKey("Encryption Key Not Set.");
			return encrypt(regex);
		}
	}
	
	
	/*
	 * decrypt
	 * 
	 * This method encrypts a string using the DESede scheme
	 * 
	 * Arguments:
	 * regex, the encrypted string
	 * 
	 * Returns:
	 * The string decrypted based on encryptionKey
	 */
	private String decrypt (String regex) {
		//Check that an encryption key exists
		if (encryptionKey != null) {
			try {
				//Show the manager the encryption key
				System.out.println("The key used for this encryption is:\n" + encryptionKey);
				
				//Convert encryption key to byte array
				byte[] key = encryptionKey.getBytes(CHARSET);
				
				//Set up the encryption
				keySpec = new DESedeKeySpec(key);
				skFactory = SecretKeyFactory.getInstance(ENCRYPTION_SCHEME);
				cipher = Cipher.getInstance(ENCRYPTION_SCHEME);
				secretKey = skFactory.generateSecret(keySpec);
				
				//Decrypt the String
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
				byte[] encrypted = regex.getBytes(CHARSET);
				byte[] unencrypted = cipher.doFinal(encrypted);
				return new String(unencrypted, CHARSET);
			}
			catch (Exception e) {
				//Print error
				e.printStackTrace(System.out);
				
				//Input new key and try again
				setEncryptionKey("Decryption Failed.");
				return decrypt(regex);
			}
		}
		//Otherwise prompt for encryption key and try again
		else {
			setEncryptionKey("Encryption Key Not Set.");
			return decrypt(regex);
		}
	}
	
	
}
