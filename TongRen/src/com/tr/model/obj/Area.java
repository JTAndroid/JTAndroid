package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Area extends IMGroupCategory{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// "id":"地区id",
	// "name": "地区名称",
	// "listArea": []
	
	public String mID = "";
	public String mName = "";
	public List<Area> mListArea = new ArrayList<Area>();
	
	public Area(){
		
	}
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key = null;

		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mID = jsonObject.optString(str_key);
		}

		str_key = "name";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mName = jsonObject.optString(str_key);
		}
		
		str_key = "listArea";
		if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if (jArray != null && jArray.length() > 0) {
				for (int i = 0; i < jArray.length(); i++) {
					Area area = new Area();
					area.initWithJson(jArray.getJSONObject(i));
					mListArea.add(area);
				}
			}
		}
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("id", mID);
		jObject.put("name", mName);
		JSONArray jArray = new JSONArray();
		for (int i = 0; i < mListArea.size(); i++) {
			jArray.put(mListArea.get(i).toJSONObject());
		}
		jObject.put("listArea", jArray);
		return jObject;
	}

	@Override
	public String getName() {
		
		return mName;
	}
}
