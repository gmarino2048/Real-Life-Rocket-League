import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Authenticator {
	
	
	public Authenticator () {
		
	}
	
	public String getAccessToken () {
		return "";
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
}