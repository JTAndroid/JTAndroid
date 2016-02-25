package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class EduExperience implements Serializable{
//	 "id":"对象ID",
//     "startTime":"起始时间，如2002-07-01",
//     "endTime":"结束时间",
//     "school": "学校",
//     "major":"专业",
//     "background":"学历  0小学 1初中 2高中 3中专 4专科 5本科 6硕士 7博士",
//     "isOverseas":"是否海外经历",
//     "description":"描述",
	
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
//	public static final int background_xiao =0; 
//	public static final int background_chu =1; 
//	public static final int background_gao =2; 
//	public static final int background_zhong =3;
//	public static final int background_zhuan =4;
//	public static final int background_ben =5;
//	public static final int background_shuo =6;
//	public static final int background_bo =7;
	
	
	public static String BackgroudString[]={"小学","初中","高中" ,"中专","专科 ","本科","硕士","博士"};
	
	public final static String today="今";
	public static String getBackgroudString(int id){
		return BackgroudString[id];
	}
	
	public int mid=0;
	public String mstartTime="";
	public String mendTime=today;
	public String mschool="";
	public String mmajor="";
	public int mbackground=0;
	public String mbackgroundoffline="";
	public boolean misOverseas=false;
	public String mdescription="";
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {
		String str_key = null;
		
		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mid = jsonObject.getInt(str_key);
		}
		str_key = "startTime";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mstartTime = jsonObject.getString(str_key);
		}
		str_key = "endTime";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mendTime = jsonObject.getString(str_key);
		}
		str_key = "school";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mschool = jsonObject.optString(str_key,"");
		}
		str_key = "major";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mmajor = jsonObject.getString(str_key);
		}
		str_key = "background";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mbackground = jsonObject.getInt(str_key);
		}
		str_key = "backgroundOffline";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mbackgroundoffline = jsonObject.getString(str_key);
		}
		str_key = "isOverseas";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			misOverseas = jsonObject.getBoolean(str_key);
		}
		str_key = "description";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mdescription = jsonObject.getString(str_key);
		}
	}
	
	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("id",mid);
		jObject.put("startTime",mstartTime);
		jObject.put("endTime",mendTime);
		jObject.put("school",mschool);
		jObject.put("major",mmajor);
		jObject.put("background",mbackground);
		jObject.put("backgroundOffline",mbackgroundoffline);
		jObject.put("isOverseas",misOverseas);
		jObject.put("description",mdescription);
		return jObject;
	}
}
