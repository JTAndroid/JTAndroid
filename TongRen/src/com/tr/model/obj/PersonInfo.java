package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class PersonInfo implements Serializable{
	/**
	 * 
	 */
	
	public final static int type_phones=1;
	public final static int type_email=2;
	public final static int type_sns=3;
	public final static int type_connection=4;
	public final static int type_dates=5;
	public final static int type_detailed=6;
	public final static int type_company=7;
	public  int type=0;
	public boolean isAdd=false;//item有加和减，添加的标示。
	private static final long serialVersionUID = 1L;
	public String mtag="";
	public String mvalue="";
	public String mhint="";


	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {
		String str_key = null;
		
		str_key = "tag";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mtag = jsonObject.getString(str_key);
		}
		str_key = "value";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mvalue = jsonObject.getString(str_key);
		}
	}
	
	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("tag",mtag);
		jObject.put("value",mvalue);
		return jObject;
	}
}
