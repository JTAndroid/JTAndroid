package com.tr.model.work;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BUAffarLog {
	public long id;		//": "主键",
	public String  affairId;		//": "事务id",
	public String  info;		//": "日志内容",
	public String  type;		//": "类型 n：新建 e:编辑，o：完成，r：重新开启，q：退出 x：过期",
	public String  ctime;		//": "时间"
	
	public static List<BUAffarLog> genAffarListByJson(JSONObject inJsonObject)
	{
		int i;
		List<BUAffarLog> vAffarLogList= new ArrayList<BUAffarLog>();
		try {
			JSONObject vPageObject=inJsonObject.getJSONObject("page");
			
			if (vPageObject!=null &&  vPageObject.has("list"))
			{
				JSONArray vListData=vPageObject.getJSONArray("list");

				for (i=0;i<vListData.length();i++)
				{
					JSONObject vAffarLogObject=(JSONObject) vListData.get(i);
					BUAffarLog vAffarLog=BUAffarLog.genAffarLogByJson(vAffarLogObject);
					vAffarLogList.add(vAffarLog);
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vAffarLogList;
	}
	
	public static BUAffarLog genAffarLogByJson(JSONObject inJSONObject)
	{
		BUAffarLog vAffarLog=new BUAffarLog();
		
		try {
			vAffarLog.id=inJSONObject.getLong("id");
			vAffarLog.info=inJSONObject.getString("info");
			vAffarLog.type=inJSONObject.getString("type");

			vAffarLog.ctime=inJSONObject.getString("ctime");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return vAffarLog;
	}
}
