package com.tr.model.model;

import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class ResourceMini extends BaseObject {

	/**
	 * "id": "id",
                "avatar": "人脉头像",
                "requirementType": "需求类型:(融资:RZXQ;投资:TZXQ)",
                "name": "人脉姓名;需求标题;知识标题",
                "reason": "人脉关系",
                "range": "几度人脉;知识来源"
	 */
	public static final long serialVersionUID = 1L;

	public String id = "";
	public String avatar = "";
	public String requirementType = "";
	public String name = "";
	public String reason = "";
	public String range = "";
	public String excepted = ""; // 人脉还是用户
	
	public ResourceMini(){
		
	}
	
	public ResourceMini(String name,String reson,String range){
		
	}
	
	
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key = null;

		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			id = jsonObject.optString(str_key);
		}

		str_key = "avatar";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			avatar = jsonObject.optString(str_key);
		}

		str_key = "requirementType";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			requirementType = jsonObject.optString(str_key);
		}

		str_key = "name";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			name = jsonObject.optString(str_key);
		}

		str_key = "reason";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			reason = jsonObject.optString(str_key);
		}
		
		str_key = "range";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			range = jsonObject.optString(str_key);
		}
		
		str_key = "excepted";
		if(jsonObject.has(str_key) && ! jsonObject.isNull(str_key)){
			excepted =  jsonObject.optString(str_key);
		}
	}
}
