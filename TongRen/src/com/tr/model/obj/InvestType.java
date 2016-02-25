package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import com.utils.string.StringUtils;

public class InvestType extends IMGroupCategory{

	private static final long serialVersionUID = 1L;
	public String mID = "";
	public String mTitle = "";
	
	public InvestType(){
		
	}
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key = null;

		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mID = jsonObject.optString(str_key);
		}
		
		str_key = "title";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mTitle = jsonObject.optString(str_key);
		}
	}
	
	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("id", mID);
		jObject.put("title", mTitle);
		return jObject;
	}

	@Override
	public String getName() {
		return mTitle;
	}
	
	public String getID(){
		if(mID!=null){
			return mID.replace(",", "");
		}
		return "";
	}
}
