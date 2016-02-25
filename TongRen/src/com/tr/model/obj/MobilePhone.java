package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;


public class MobilePhone implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name="";
	public String mobile="";
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {
		String str_key = null;
		
		str_key = "name";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			name = jsonObject.getString(str_key);
		}
		str_key = "mobile";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mobile = jsonObject.getString(str_key);
		}
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("name",name);
		jObject.put("mobile",mobile);
		return jObject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
	
}
