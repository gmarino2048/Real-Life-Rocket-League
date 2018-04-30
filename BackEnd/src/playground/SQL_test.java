package playground;

import org.json.JSONObject;

import sql.MySQL_Connector;

public class SQL_test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MySQL_Connector c = new MySQL_Connector();
		System.out.println(c.getJSONforUserGames("nsc27"));
		System.out.println(c.getJSONforGame(c.getMostRecentGame("nsc27", "gjs64")));
	}

}
