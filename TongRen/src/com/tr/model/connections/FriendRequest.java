package com.tr.model.connections;

import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.tr.model.obj.Connections;

import android.text.TextUtils;

public class FriendRequest {
	public static int state_agreed=1;
	public static int state_request=2;
	public static int type_org=2;
	public static int type_persion=1;
	
	public String id="";
	public String name="";
	public String sourceFrom="";
	public String userID = "";
	public int state=0;
	public int userType=0;
	public String image="";
	private int type;
			
//	public Area(){
//		
//	}
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key = null;
		str_key = "id";
		if (jsonObject.has(str_key)) {
			id = jsonObject.optString(str_key);
		}
		userID = jsonObject.optString("userID");
		str_key = "name";
		if (jsonObject.has(str_key)) {
			name = jsonObject.optString(str_key);
		}
		str_key = "sourceFrom";
		if (jsonObject.has(str_key)) {
			sourceFrom = jsonObject.optString(str_key);
		}
		str_key = "image";
		if (jsonObject.has(str_key)) {
			image = jsonObject.optString(str_key);
		}
		str_key = "state";
		if (jsonObject.has(str_key)) {
			state = jsonObject.optInt(str_key);
		}
		str_key = "userType";
		if (jsonObject.has(str_key)) {
			userType = jsonObject.optInt(str_key);
		}
		
		type = jsonObject.optInt("type");
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("id", id);
		jObject.put("userType", userType);
		jObject.put("sourceFrom", sourceFrom);
		jObject.put("image", image);
		jObject.put("name", name);
		jObject.put("state", state);
		jObject.put("type", type);
		return jObject;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSourceFrom() {
		if(TextUtils.isEmpty(sourceFrom))
			return "";
		else
			return sourceFrom;
	}

	public void setSourceFrom(String sourceFrom) {
		this.sourceFrom = sourceFrom;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public Connections toConneections() {
		Connections connection = new Connections();
		connection.type =this.userType;
		connection.setName(name);
		connection.setOnline(true);
		connection.setID(userID);
		connection.setImage(image);
		return connection;
	}
	
}
