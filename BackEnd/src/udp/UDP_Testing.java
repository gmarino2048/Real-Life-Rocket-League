package udp;

import org.json.JSONObject;

public class UDP_Testing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		JSONObject j = new JSONObject();
		
		j.accumulate("messageType", "goalData");
		j.accumulate("num", 90).accumulate("subnum", 10);
		j.put("putnum", 100).put("bool", true);
		j.accumulate("messageType",10);
		j.append("messageType", 9.9);

		//System.out.println(j.toString(2));
		//j.getJSONObject("messageType");
		j.getJSONArray("messageType").put(3, true);

		System.out.println(j.toString(2));
		
		
		
	}

}
