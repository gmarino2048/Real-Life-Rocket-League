import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class APIConnection {
	
	public static void main (String[] args) {
		APIConnection connection = new APIConnection();
		
		try {
			connection.getScore1();
			connection.getScore2();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Authenticator auth;
	
	private String authToken;
	private String deviceID;
	
	private static String BASE_URL = "https://api.particle.io/v1/devices/";
	private String deviceURL;
	private String accessURL;
	
	public APIConnection () {
		auth = new Authenticator();
		
		authToken = auth.getAccessToken();
		deviceID = auth.getDeviceID();
		
		deviceURL = BASE_URL + deviceID + "/";
		accessURL = "?access_token=" + authToken;
	}
	
	public int getScore1 () throws Exception{
		try {
			URL url = new URL(deviceURL + "score1"+ accessURL);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
			
			String rawresponse;
			
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				rawresponse = "";
	            
				int character = inputStream.read();
				while (character != -1) {
					rawresponse += (char) character;
					character = inputStream.read();
				}
				
				JSONObject response = new JSONObject(rawresponse);
				int score1 = response.getInt("result");
				System.out.println(score1);
				return score1;
			}
			else {
				throw new Exception ();
			}
		} 
		catch (Exception e) {
			throw e;
		}
	}
	
	public int getScore2 () throws Exception{
		try {
			URL url = new URL(deviceURL + "score2"+ accessURL);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
			
			String rawresponse;
			
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				rawresponse = "";
	            
				int character = inputStream.read();
				while (character != -1) {
					rawresponse += (char) character;
					character = inputStream.read();
				}
				
				JSONObject response = new JSONObject(rawresponse);
				int score2 = response.getInt("result");
				System.out.println(score2);
				return score2;
			}
			else {
				throw new Exception ("Bad response from server");
			}
		} 
		catch (Exception e) {
			throw e;
		}
	}
	
	//TODO: Make post code to set score 1
	public void setScore1 (int score1) throws Exception {
		
	}
	
	//TODO: Make post code to set score 2
	public void setScore2 (int score2) throws Exception {
		
	}
}
