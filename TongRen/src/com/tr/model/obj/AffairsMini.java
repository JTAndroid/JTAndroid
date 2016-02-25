package com.tr.model.obj;

import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.tr.model.demand.DemandASSOData;
import com.tr.model.page.IPageBaseItem;

/**
 * 
 * @ClassName:     AffairsMini.java
 * @Description:   事务mini对象，获取我的事务时用 
 * @author         xuxinjian
 * @version        V1.0  
 * @Date           2014-4-18 下午6:35:16
 */
public class AffairsMini extends ResourceBase implements IPageBaseItem{
	
	private static final long serialVersionUID = 198212999910064422L;

	public final static int TYPE_BUSINESS = 1; // 业务需求
	public final static int TYPE_TASK = 2; // 任务
	public final static int TYPE_PROJECT = 3; // 项目
	public final static int TYPE_REQUIREMENT = 4; // 需求
	 
	public String requirementType; //需求类型.
	public int id = 0;
	public String title = ""; // 标题
	public String time = ""; // 发布时间
	public int type; // 类型
	public String name = ""; // 发布者姓名
	public String content = ""; // 简述
	public String deadline = "";
	/*保留字段,需求：1-投资需求,2-融资需求,3-专家需求；任务：暂无；项目：暂无*/
	public String reserve = "";
	public Connections connections;

	
	public AffairsMini(){
		
	}
	
	public AffairsMini(String title){
		this.title = title;
	}
	
	public static AffairsMini createFactory(JSONObject jsonObject) {
		try {
			AffairsMini self = new AffairsMini();
			self.id = jsonObject.optInt("id");
			self.title = jsonObject.optString("title");
			self.time = jsonObject.optString("time");
			self.type = jsonObject.optInt("type");
			self.content = jsonObject.optString("content");
			self.deadline = jsonObject.optString("deadline");
			self.name = jsonObject.optString("name");
			self.reserve = jsonObject.optString("reserve");
			return self;
		}catch (Exception e) {
			return null;
		}
	}
	public DemandASSOData toDemandASSOData() {
		DemandASSOData conn = new DemandASSOData();
		conn.id=String.valueOf(this.id);
		conn.title = this.title;
		conn.name = this.name;
		conn.requirementtype = this.reserve;
		return conn;
	}
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key = null;

		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			id = jsonObject.optInt(str_key);
		}

		str_key = "name";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			name = jsonObject.optString(str_key);
		}

		str_key = "time";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			time = jsonObject.optString(str_key);
		}

		str_key = "title";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			title = jsonObject.optString(str_key);
		}

		str_key = "content";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			content = jsonObject.getString(str_key);
		}

		str_key = "type";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			type = jsonObject.getInt(str_key);
		}
		
		str_key = "deadline";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			deadline = jsonObject.getString(str_key);
		}
		
		str_key = "reserve";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			reserve = jsonObject.getString(str_key);
		}
	}
	
	
	public JSONObject toJSONObject() throws JSONException{
		JSONObject jObject = new JSONObject();
		jObject.put("id", id);
		jObject.put("title", title);
		jObject.put("time", time);
		jObject.put("type", type);
		jObject.put("content", content);
		jObject.put("deadline", deadline);
		jObject.put("name", name);
		jObject.put("reserve",reserve);
		return jObject;
	}
	
	public RequirementMini toRequirementMini(){
		RequirementMini requirementMini = new RequirementMini();
		requirementMini.mID = id;
		requirementMini.mTitle = title;
		requirementMini.mContent = content;
		requirementMini.mTime = time;
		requirementMini.setConnections(connections);
		return requirementMini;
	}
}
