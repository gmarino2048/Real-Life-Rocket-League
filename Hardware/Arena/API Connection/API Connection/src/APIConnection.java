import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class APIConnection {

	private Authenticator auth;
	
	private String authToken;
	private String deviceID;
	
	public APIConnection () {
		auth = new Authenticator();
		
		authToken = auth.getAccessToken();
		deviceID = auth.getDeviceID();
	}
	
	public int startGame (String username1, String username2, int minutes) {
		boolean valid = true;
		
		valid = valid && setUsername1(username1) == 0;
		valid = valid && setUsername2(username2) == 0;
		valid = valid && setScore1(0) == 0;
		valid = valid && setScore2(0) == 0;
		valid = valid && setTime(minutes) == 0;
		
		if (valid)
			return 0;
		else
			return -1;
	}
	
	public int voidGame () {
		boolean valid = true;
		
		valid = valid && setUsername1("USER1") == 0;
		valid = valid && setUsername2("USER2") == 0;
		valid = valid && setScore1(0) == 0;
		valid = valid && setScore2(0) == 0;
		valid = valid && setTime(0) == 0;
		
		if (valid)
			return 0;
		else
			return -1;
	}
	
	public int getScore1 () {
		try {
			return getInt("score1");
		} 
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int getScore2 () {
		try {
			return getInt("score2");
		} 
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public String getUsername1 () {
		try {
			return getString("username1");
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getUsername2 () {
		try {
			return getString("username2");
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public int setScore1 (int value) {
		try {
			int setScore = postInt("setscore1", value);
			int sendScore = postString("sendscore1", "");
			
			if (setScore == 0 && sendScore == 0) {
				return 0;
			}
			else return -1;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int setScore2 (int value) {
		try {
			int setScore = postInt("setscore2", value);
			int sendScore = postString("sendscore2", "");
			
			if (setScore == 0 && sendScore == 0) {
				return 0;
			}
			else return -1;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int setUsername1 (String value) {
		try {
			int setUsername = postString("setuname1", value);
			int sendUsername = postString("senduname1", "");
			
			if (setUsername == 0 && sendUsername == 0) {
				return 0;
			}
			else return -1;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int setUsername2 (String value) {
		try {
			int setUsername = postString("setuname2", value);
			int sendUsername = postString("senduname2", "");
			
			if (setUsername == 0 && sendUsername == 0) {
				return 0;
			}
			else return -1;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int setTime (int minutes) {
		try {
			int setTime = postInt("settime", minutes);
			int sendTime = postString("sendtime", "");
			
			if (setTime == 0 && sendTime == 0) {
				return 0;
			}
			else return -1;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	private int getInt (String variableName) throws Exception {
		String urlName = buildVariableURL(variableName);
		
		URL url = new URL(urlName);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			String response = "";
			InputStream in = connection.getInputStream();
			
			int readVal = in.read();
			while (readVal != -1) {
				response += (char) readVal;
				readVal = in.read();
			}
			
			JSONObject json = new JSONObject(response);
			int value = json.getInt("result");
			return value;
		}
		else {
			throw new Exception("Bad Response from URL when reading int");
		}
	}
		
	
	private String getString (String variableName) throws Exception{
String urlName = buildVariableURL(variableName);
		
		URL url = new URL(urlName);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			String response = "";
			InputStream in = connection.getInputStream();
			
			int readVal = in.read();
			while (readVal != -1) {
				response += (char) readVal;
				readVal = in.read();
			}
			
			JSONObject json = new JSONObject(response);
			String value = json.getString("result");
			return value;
		}
		else {
			throw new Exception("Bad Response from URL when reading int");
		}
	}
	
	private int postInt (String functionName, int arguments) throws Exception{
		String urlName = buildFunctionURL(functionName);
		
		String args = "";
		args += "arg=" + arguments;
		args += "&access_token=" + authToken;
		
		URL url = new URL(urlName);
		
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", Integer.toString(args.length()));
		connection.setRequestProperty("charset", "UTF-8");
		
		DataOutputStream bw = new DataOutputStream(connection.getOutputStream());
		
		bw.write(args.getBytes("UTF-8"));
		
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			String response = "";
			InputStream in = connection.getInputStream();
			
			int readVal = in.read();
			while (readVal != -1) {
				response += (char) readVal;
				readVal = in.read();
			}
			
			JSONObject json = new JSONObject(response);
			int value = json.getInt("return_value");
			return value;
		}
		else {
			throw new Exception("Bad Response from URL when reading int");
		}
	}
	
	private int postString (String functionName, String arguments) throws Exception{
		String urlName = buildFunctionURL(functionName);
		
		String args = "";
		args += "arg=" + arguments;
		args += "&access_token=" + authToken;
		
		URL url = new URL(urlName);
		
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", Integer.toString(args.length()));
		connection.setRequestProperty("charset", "UTF-8");
		
		DataOutputStream bw = new DataOutputStream(connection.getOutputStream());
		
		bw.write(args.getBytes("UTF-8"));
		
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			String response = "";
			InputStream in = connection.getInputStream();
			
			int readVal = in.read();
			while (readVal != -1) {
				response += (char) readVal;
				readVal = in.read();
			}
			
			JSONObject json = new JSONObject(response);
			int value = json.getInt("return_value");
			return value;
		}
		else {
			throw new Exception("Bad Response from URL when reading int");
		}
	}
	
	private String buildVariableURL (String variableName) {
		String url = "https://api.particle.io/v1/devices/";
		
		url += deviceID;
		url += "/";
		url += variableName;
		url += "?access_token=";
		url += authToken;
		
		return url;
	}
	
	private String buildFunctionURL (String functionName) {
		String url = "https://api.particle.io/v1/devices/";
		
		url += deviceID;
		url += "/";
		url += functionName;
		
		return url;
	}
}
