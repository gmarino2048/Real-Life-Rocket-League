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
import javax.crypto.*;


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
	
	//File readers and writers, for  the access.oauth file
	
	
	//The key to decrypt the access.oauth file
	private String decryptionKey;
	
	/*
	 * Constructor
	 * 
	 * The Authenticator constructor takes no arguments and is
	 * meant to be called once so that the variables are initialized
	 * properly.
	 */
	public Authenticator () {
		
	}
	
	/*
	 * informationFound
	 * 
	 * This method parses the local directory for the "access.oauth"
	 * file. If the file is found, the informationFound method
	 * returns true, otherwise the method returns false.
	 */
	public boolean informationFound () {
		return false;
	}
	
	public String setDecryptionKey () {
		
	}
	
	public String getUserName () {
		
	}
	
	
	public void setUsername (String newUserName) {
		
	}
	
	public void setPassword (String newPassword) {
		
	}
	
	public void setDeviceID (String newDeviceID) {
		
	}
	
	public void SetAuthenticationTokens (String newToken1, String newToken2) {
		
	}
	
	
}
