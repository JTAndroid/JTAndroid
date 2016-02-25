package com.tr.model.obj;

import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.tr.model.conference.MMeetingData;
import com.tr.model.page.IPageBaseItem;


/**
 * @ClassName:     Requirement.java
 * @Description:   需求对象精简版
 * @Author         leon
 * @Version        v 1.0  
 * @Date           2014-04-11
 * @LastEdit       2014-04-12
 */
public class RequirementMini implements IPageBaseItem{
	 private static final long serialVersionUID = 198212344230064422L;
	// "RequirementMini":
	// {
	// "id":"需求id",
	// "typeName":"需求类型名称，如我要投资，我要融资",
	// "title":"需求标题",
	// "content":"需求内容"
	// }
	
	public int mID = 0;
	public String mTypeName = "";
	public String mTitle = "";
	public String mContent = "";
	public String mTime = ""; // 发布时间
	private int reqType = 0;
	

	private String time;
	private Connections connections;
	
	public boolean isAlterMeeting;
    public MMeetingData alterMeetingData;
    
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getmID() {
		return mID;
	}

	public void setmID(int mID) {
		this.mID = mID;
	}

	public String getmTypeName() {
		return mTypeName;
	}

	public void setmTypeName(String mTypeName) {
		this.mTypeName = mTypeName;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmContent() {
		return mContent;
	}

	public void setmContent(String mContent) {
		this.mContent = mContent;
	}
	
	public Connections getConnections() {
		return connections;
	}

	public void setConnections(Connections connections) {
		this.connections = connections;
	}
	
	public int getReqType() {
		return reqType;
	}

	public void setReqType(int reqType) {
		this.reqType = reqType;
	}
	
	public RequirementMini(){
		
	}
	
	public static RequirementMini createFactory(JSONObject jsonObject){
		RequirementMini self = null;
		try{
			self = new RequirementMini();
			self.initWithJson(jsonObject);
		}catch(Exception e){
			
		}
		return self;
	}
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key = null;

		// 需求id
		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mID = jsonObject.optInt(str_key);
		}
		
		// 需求类型
		str_key = "typeName";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mTypeName = jsonObject.optString(str_key);
		}

		// 需求名称
		str_key = "title";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mTitle = jsonObject.optString(str_key);
		}
		
		time = jsonObject.getString("time");

		// 需求内容
		str_key = "content";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mContent = jsonObject.optString(str_key);
		}
		
		// 发布时间
		str_key = "time";
		if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
			mTime =  jsonObject.optString(str_key);
		}
	}
	
	public JSONObject toJSONObject() throws JSONException {
		
		JSONObject jObject = new JSONObject();
		jObject.put("id", mID);
		jObject.put("typeName", mTypeName);
		jObject.put("title", mTitle);
		jObject.put("content", mContent);
		jObject.put("time", mTime);
		return jObject;
	}
	
	public JTFile toJTFile(){
		return null;
	}
	/**
	 * 转为AffairMini ，关联需求时用     add by zhongshan
	 * @return
	 */
	public AffairsMini toAffairMini(){
		AffairsMini affairsMini = new AffairsMini();
		affairsMini.connections = connections;
		affairsMini.content = mContent;
		affairsMini.deadline = "";
		affairsMini.id = mID;
		affairsMini.time = mTime;
		affairsMini.title = mTitle;
		affairsMini.type = affairsMini.TYPE_REQUIREMENT;
		return affairsMini;
		
	}
}
