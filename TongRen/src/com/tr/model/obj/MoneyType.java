package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 货币类型
 */
public class MoneyType extends IMGroupCategory{
	
	private static final long serialVersionUID = 1L;
	public String tag="";
	public String name="";


	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {
		String str_key = null;
		
		str_key = "tag";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			tag = jsonObject.getString(str_key);
		}
		str_key = "name";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			name = jsonObject.getString(str_key);
		}
	}
	
	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("tag",tag);
		jObject.put("name",name);
		return jObject;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
