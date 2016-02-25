package com.tr.model.work;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
/**
 * 事务关联资源
 * @author Administrator
 *
 */
public class BUAffarRelation implements Serializable{


	private static final long serialVersionUID = 7087425809648076787L;
	public long id;			//"id": "主键",
	public long affairId;		//"affairId": "事务id",
	public String label;		//"label": "关联标签",
	public long relateId;		//"relateId":"关联id",
	public String type;			//"type": "关联类型 p:人员，o：组织，k：知识，r:事件",
	public String title;		//"title": "标题"
	public String picUrl;
	public String relateTime;
	public String userOrType;
	
	//public String career;
	//public String company;
	
	// 目录
	//	public String columnpath;
		public String reserve = "";
	
	//public int peopleType;		//1 type_persion = 1; type_org = 2;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAffairId() {
		return affairId;
	}
	public void setAffairId(long affairId) {
		this.affairId = affairId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public long getRelateId() {
		return relateId;
	}
	public void setRelateId(long relateId) {
		this.relateId = relateId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public static List<BUAffarRelation> genAffarRelationListByJson(JSONObject inJsonObject)
	{
		int i;
		List<BUAffarRelation> vAffarRelationList= new ArrayList<BUAffarRelation>();
		try {
			if (inJsonObject!=null)
			{
				if (inJsonObject.has("affairRelations"))
				{
					JSONArray vListData=inJsonObject.getJSONArray("affairRelations");

					for (i=0;i<vListData.length();i++)
					{
						JSONObject vAffarRelObject=(JSONObject) vListData.get(i);
						BUAffarRelation vAffarRel=BUAffarRelation.genAffarRelationByJson(vAffarRelObject);
						
						vAffarRelationList.add(vAffarRel);
					}
				}
				
			}
						
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vAffarRelationList;
	}
	
	public static BUAffarRelation genAffarRelationByJson(JSONObject inJSONObject)
	{
		BUAffarRelation vAffarRelation=new BUAffarRelation();
		
		try {
			vAffarRelation.id=inJSONObject.getLong("id");
			vAffarRelation.affairId=inJSONObject.getLong("affairId");
			vAffarRelation.label=inJSONObject.getString("label");
			vAffarRelation.relateId=inJSONObject.getLong("relateId");
			vAffarRelation.type=inJSONObject.getString("type");
			vAffarRelation.title=inJSONObject.getString("title");
			vAffarRelation.picUrl=inJSONObject.getString("picUrl");
			vAffarRelation.relateTime=inJSONObject.getString("relateTime");
			vAffarRelation.userOrType=inJSONObject.getString("userOrType");
			Log.d("xmx","pic:"+vAffarRelation.picUrl);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return vAffarRelation;
	}

	
}
