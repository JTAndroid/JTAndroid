 package com.tr.model.work;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;

public class BUAffarList {
	public List <BUAffar> mAffarList;
	public String isNew;//"是否有新的事物通知,1:有新事务，0：没有新事务"
	
	public BUAffarList()
	{
		mAffarList = new ArrayList<BUAffar>();
		isNew=new String();
	}
	
	
	
	public static BUAffarList genAfferByJson(JSONObject inJsonObject)
	{
		int i;
		BUAffarList vAffarList= new BUAffarList();
		//Log.d("xmx","return json:"+inJsonObject.toString());
		
		try {
			if (inJsonObject.has("page")) {
				JSONObject vPageObject = inJsonObject.getJSONObject("page");
				
				if (vPageObject != null && vPageObject.has("list")) {
					JSONArray vListData = vPageObject.getJSONArray("list");
					
					// Log.d("xmx","list:"+vListData.toString());
					for (i = 0; i < vListData.length(); i++) {
						JSONObject vAffarObject = (JSONObject) vListData.get(i);
						BUAffar vAffar = BUAffar
								.genAffarMinByJson(vAffarObject);
						vAffarList.mAffarList.add(vAffar);
						// Log.d("xmx","id:"+vAffar.getId());
					}
				}
				//是否有新事物
				if (vPageObject != null && vPageObject.has("isNew")) {
					vAffarList.isNew=vPageObject.getString("isNew");
				}
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return vAffarList;
	}
	
	
	public static List<String> genAfferMonthDataByJson(JSONObject inJsonObject)
	{
		int i;
		List<String> vAffarMonthList= new ArrayList<String>();
		Log.d("xmx","genAfferMonthDataByJson return json:"+inJsonObject.toString());
		
		
		try {			
			if (inJsonObject!=null && inJsonObject.has("days"))
			{
				JSONArray vListData=inJsonObject.getJSONArray("days");
				Log.d("xmx","list:"+vListData.toString());
				for (i=0;i<vListData.length();i++)
				{
					String vDate=(String)vListData.get(i);
					Log.d("xmx","markDate:"+vDate);
					
					vAffarMonthList.add(vDate);
					/*
					String vStr=vDateObject.getString("");
					vAffarList.mAffarList.add(vAffar);
					//Log.d("xmx","id:"+vAffar.getId());
					 
					 */
				} 
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return vAffarMonthList;
	}
}
