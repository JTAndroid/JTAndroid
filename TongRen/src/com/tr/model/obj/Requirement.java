package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Parcel;
import android.os.Parcelable;


/**
 * @ClassName:     Requirement.java
 * @Description:   普通需求对象
 * @Author         leon
 * @Version        v 1.0  
 * @Date           2014-04-11
 * @LastEdit       2014-04-14
 */
public class Requirement implements Serializable{

	// "requirement":{
	// "userid":"需求发布人id，发布时为空",
	// "name":"需求发布人姓名，发布时为空",
	// "publishTime":"需求发布时间，发布时为空",
	// "title":"需求标题",
	// "id":"需求id，上传需求时，该值为null",
	// "type":"0-投资，1-融资",
	// "publishRange":"0-仅自己可见，1-对所有人公开，2-对listConnections里的人可见",
	// "listConnections":[],
	//
	// "investKeyword":{},
	//
	// "comment":"备注信息",
	// "listJTFile":[],
	//
	// "listMatchRequirementMini":[],
	// "listMatchConnections":[],
	// "listMatchKnowledgeMini":[],
	// 
	// }
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public String mUserID = "";
	public String mName = "";
	public String mPublishTime = "";
	public int mFriendState = 0; // 默认是好友
	public int mUserType = 0; // 0-个人，1-机构
	public String mUserAvatar = "";
	public String mTitle = "";
	public String mID = "";
	public int mType = 0;
	public int mPublishRange = 1;
	public InvestKeyword investKeyword = new InvestKeyword(); // 投融资资关键字
	public String mContent = ""; // 备注
	public String mTaskId = "";
	public List<JTFile> listJTFile = new ArrayList<JTFile>(); // 附件
	public ArrayList<ConnectionsMini> listConnectionsMini= new ArrayList<ConnectionsMini>(); // 对此需求公开Connections
	public List<RequirementMini> listMatchRequirementMini = new ArrayList<RequirementMini>(); // 匹配需求
	public List<ConnectionsMini> listMatchConnectionsMini = new ArrayList<ConnectionsMini>(); // 匹配关系
	public List<KnowledgeMini> listMatchKnowledgeMini = new ArrayList<KnowledgeMini>(); // 匹配知识

	public Requirement(){
		
	}
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {

		String str_key = null;

		// 需求发布人id
		str_key = "userid";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mUserID = jsonObject.optString(str_key);
		}
		
		// 需求发布人姓名
		str_key = "name";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mName = jsonObject.optString(str_key);
		}
		
		// 发布人头像
		str_key = "userAvatar";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mUserAvatar = jsonObject.getString(str_key);
		}
		
		// 需求发布时间
		str_key = "publishTime";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mPublishTime = jsonObject.optString(str_key);
		}
		
		// 用户类型
		str_key = "userType";
		if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
			mUserType = jsonObject.getInt(str_key);
		}
		
		// 需求标题
		str_key = "title";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mTitle = jsonObject.optString(str_key);
		}

		// 需求ID
		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mID = jsonObject.optString(str_key);
		}
		
		// 好友关系
		str_key = "friendState";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mFriendState = jsonObject.getInt(str_key);
		}
		
		// 需求类型 （0-投资，1-融资）
		str_key = "type";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mType = jsonObject.getInt(str_key);
		}
		
		// 需求可见范围（0-仅自己可见，1-对所有人公开，2-对listConnections里的人可见）
		str_key = "publishRange";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mPublishRange = jsonObject.getInt(str_key);
		}
		
		// 投融资关键字
		str_key = "investKeyword";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			investKeyword.initWithJson(jsonObject.getJSONObject(str_key));
		}
		
		// taskId
		str_key = "taskId";
		if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
			mTaskId = jsonObject.optString(str_key);
		}
	
		// 附件
		str_key = "listJTFile";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			JSONArray jsonArray = jsonObject.getJSONArray(str_key);
			if (jsonArray != null && jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JTFile file = new JTFile();
					file.initWithJson(jsonArray.getJSONObject(i));
					listJTFile.add(file);
				}
			}
		}
		
		// 备注
		str_key = "comment";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mContent = jsonObject.getString(str_key);
		}
		
		// 对此需求可见的人脉
		str_key = "listConnectionsMini";
		if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if(jArray!=null && jArray.length()>0){
				for(int i=0;i<jArray.length();i++){
					ConnectionsMini connsMini = ConnectionsMini
							.createFactory(jArray.getJSONObject(i));
					if (connsMini != null) {
						listConnectionsMini.add(connsMini);
					}
				}
			}
		}

		// 匹配需求
		str_key = "listMatchRequirementMini";
		if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if(jArray!=null && jArray.length()>0){
				for(int i=0;i<jArray.length();i++){
					RequirementMini reqMini =  new RequirementMini();
					reqMini.initWithJson(jArray.getJSONObject(i));
					listMatchRequirementMini.add(reqMini);
				}
			}
		}
		
		// 匹配的知识
		str_key = "listMatchKnowledgeMini";
		if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if(jArray!=null && jArray.length()>0){
				for(int i=0;i<jArray.length();i++){
					KnowledgeMini knoMini =  new KnowledgeMini();
					knoMini.initWithJson(jArray.getJSONObject(i));
					listMatchKnowledgeMini.add(knoMini);
				}
			}
		}
		
		// 匹配的人脉
		str_key = "listMatchConnectionsMini";
		if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if(jArray!=null && jArray.length()>0){
				for(int i=0;i<jArray.length();i++){
					ConnectionsMini connsMini =  ConnectionsMini.createFactory(jArray.getJSONObject(i));
					if(connsMini!=null){
						listMatchConnectionsMini.add(connsMini);
					}
				}
			}
		}
				
		// 相似需求
		/*
		str_key = "listAnalogousRequirement";
		if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
			JSONArray jArray = jsonObject.getJSONArray(str_key);
			if(jArray!=null && jArray.length()>0){
				for(int i=0;i<jArray.length();i++){
					RequirementMini reqMini =  new RequirementMini();
					reqMini.initWithJson(jArray.getJSONObject(i));
					listAnalogousRequirement.add(reqMini);
				}
			}
		}
		*/
	}
	
	// 获取相关的JSON对象
	public JSONObject toJSONObject() throws JSONException{
		JSONObject jObject = new JSONObject();
		jObject.put("id", mID);
		jObject.put("userid", mUserID);
		jObject.put("name", mName);
		jObject.put("friendState", mFriendState);
		jObject.put("userType", mUserType);
		jObject.put("title", mTitle);
		jObject.put("publishTime", mPublishTime);
		jObject.put("type", mType);
		jObject.put("publishRange", mPublishRange);
		jObject.put("comment", mContent);
		jObject.put("investKeyword", investKeyword.toJSONObject());
		jObject.put("taskId", mTaskId);
		JSONArray jsonArr = new JSONArray();
		// 附件
		/*
		jsonArr.clear();
		for (int i = 0; i < mListJTFile.size(); i++) {
			jsonArr.add(mListJTFile.get(i).toJson());
		}
		jObject.put("listJTFile", jsonArr);
		*/
		// 公开的关系
		jsonArr = new JSONArray();
		for (int i = 0; i < listConnectionsMini.size(); i++) {
			jsonArr.put(listConnectionsMini.get(i).toJSONObject());
		}
		jObject.put("listConnectionsMini",jsonArr);
		// 匹配关系
		jsonArr = new JSONArray();
		for (int i = 0; i < listMatchConnectionsMini.size(); i++) {
			jsonArr.put(listMatchConnectionsMini.get(i).toJSONObject());
		}
		jObject.put("listMatchConnectionsMini",jsonArr);
		// 匹配需求
		jsonArr = new JSONArray();
		for (int i = 0; i < listMatchRequirementMini.size(); i++) {
			jsonArr.put(listMatchRequirementMini.get(i).toJSONObject());
		}
		jObject.put("listMatchRequirementMini",jsonArr);
		// 匹配知识
		jsonArr = new JSONArray();
		for (int i = 0; i < listMatchKnowledgeMini.size(); i++) {
			jsonArr.put(listMatchKnowledgeMini.get(i).toJSONObject());
		}
		jObject.put("listMatchKnowledgeMini", jsonArr);
		// 相似需求
		/*
		jsonArr = new JSONArray();
		for (int i = 0; i < listAnalogousRequirement.size(); i++) {
			jsonArr.put(listAnalogousRequirement.get(i).toJSONObject());
		}
		jObject.put("listAnalogousRequirement", jsonArr);
		*/
		return jObject;
	}
	
	
	
	/**
	 * 转为资源精简对象
	 * @return
	 */
	public AffairsMini toAffairMini(){
		AffairsMini affair = new AffairsMini();
		affair.id=Integer.parseInt(mID);
		affair.title = mTitle;
		affair.name = mName;
		affair.content = mContent;
		return affair;
	}
}
