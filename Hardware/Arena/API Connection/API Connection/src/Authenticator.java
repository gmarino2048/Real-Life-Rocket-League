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
import java.io.UnsupportedEncodingException;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

import java.io.*;


public class Authenticator {
	
	//The username to log into the particle cloud
	private String username;
	
	//The password for the particle cloud
	private String password;
	
	//The device ID to connect to
	private String deviceID;
	
	//The OAuth 2.0 access tokens
	private String key1;
	private String key2;
	
	//The OAuth file manager which saves the information
	private OAuthFileManager fileManager;
	
	//The key to decrypt the access.oauth file
	private String encryptionKey;
	
	//Data members necessary for encryption
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
	 */
	public Authenticator () {
		fileManager = new OAuthFileManager();
	}
	
	/*
	 * informationFound
	 * 
	 * This method parses the local directory for the "access.oauth"
	 * file.
	 * 
	 * Returns:
	 * true if the file is found, false if the file could not be found
	 */
	public boolean fileFound () {
		return false;
	}
	
	public String setEncryptionKey () {
		return "";
	}
	
	public String setEncryptionKey (String status) {
		return "";
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
