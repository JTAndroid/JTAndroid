package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WorkExperience implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int mid=0;
	public String mstartTime="";
	public String mendTime=EduExperience.today;
	public String mtitle="";//职位
	public String mcompany="";//company
	public String mcontent="";//工作描述
	public String mtrade="";//行业
	public String mdepartment="";//部门
	public List<Trade> listTrade=new ArrayList<Trade>();
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {
		String str_key = null;
		
		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mid = jsonObject.getInt(str_key);
		}
		str_key = "trade";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mtrade = jsonObject.getString(str_key);
		}
		str_key = "department";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mdepartment = jsonObject.getString(str_key);
		}
		str_key = "startTime";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mstartTime = jsonObject.getString(str_key);
		}
		str_key = "endTime";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mendTime = jsonObject.getString(str_key);
		}
		str_key = "title";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mtitle = jsonObject.getString(str_key);
		}
		str_key = "company";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mcompany = jsonObject.getString(str_key);
		}
		str_key = "content";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mcontent = jsonObject.getString(str_key);
		}
		str_key = "listTrade";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if (jArray != null && jArray.length() > 0) {
				for (int i = 0; i < jArray.length(); i++) {
					Trade trade=new Trade() ;
					trade.initWithJson(jArray.getJSONObject(i));
					listTrade.add(trade);
				}
			}
		}
	}
	
	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("id",mid);
		jObject.put("startTime",mstartTime);
		jObject.put("endTime",mendTime);
		jObject.put("title",mtitle);
		jObject.put("company",mcompany);
		jObject.put("content",mcontent);
		jObject.put("trade",mtrade);
		jObject.put("department",mdepartment);
		JSONArray jsonArray=new JSONArray();
			for(Trade workExperience :listTrade){
				jsonArray.put(workExperience.toJSONObject());
			}
			jObject.put("listTrade", jsonArray);
		return jObject;
	}
	
}
