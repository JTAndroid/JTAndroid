package com.tr.model.obj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;


/**
 * @ClassName:     MUCDetail.java 文档1.29
 * @author         xuxinjian
 * @version        V1.0  
 * @Date           2014-4-14 上午7:54:17
 * @description    会议详情信息
 */
public class MUCDetail implements Serializable{
	
	private static final long serialVersionUID = 198155564311124422L;
	
	protected List<ConnectionsMini> listConnectionsMini = new ArrayList<ConnectionsMini>(); // 现有人员
	protected List<ConnectionsMini> listConnectionsMini2 = new ArrayList<ConnectionsMini>(); // 社群详情页面现有人员
	protected ArrayList<ConnectionsMini> listExcludedMucMembers = new ArrayList<ConnectionsMini>(); // 除名人员

	protected List<JTFile> listJTFile = new ArrayList<JTFile>(); // 会议相关文件
	protected int id;// 会议id
	protected String compereID; // 主持人id
	protected String title;
	protected String subject;
	protected boolean isConference;
	protected int max;
	protected boolean isStick;
	protected boolean isNotifyNews;
	protected boolean isAutoRecord;
	protected String content; // 会议内容
	protected String orderTime; // 会议开始时间
	protected String groupId; // 群聊Id
	
	/**
	 * 根据用户ID获取用户详情
	 * @param userID
	 * @return
	 */
	public ConnectionsMini getConnectionsMiniByUserId(String userID){
		if(TextUtils.isEmpty(userID)){
			return null;
		}
		for(ConnectionsMini connsMini : listConnectionsMini){ // 现在的成员
			if(connsMini.getId().equalsIgnoreCase(userID)){
				return connsMini;
			}
		}
		for(ConnectionsMini connsMini : listExcludedMucMembers){ // 除名的成员
			if(connsMini.getId().equalsIgnoreCase(userID)){
				return connsMini;
			}
		}
		return null;
	}

	public List<ConnectionsMini> getListConnectionsMini() {
		return listConnectionsMini; 
	}
	

	public void setListConnectionsMini(List<ConnectionsMini> listConnections) {
		this.listConnectionsMini = listConnections;
	}
	
	public List<ConnectionsMini> getListConnectionsMini2() {
		return listConnectionsMini2;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public boolean isConference() {
		return isConference;
	}

	public void setConference(boolean isConference) {
		this.isConference = isConference;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public boolean isStick() {
		return isStick;
	}

	public void setStick(boolean isStick) {
		this.isStick = isStick;
	}

	public boolean isNotifyNews() {
		return isNotifyNews;
	}

	public void setNotifyNews(boolean isNotifyNews) {
		this.isNotifyNews = isNotifyNews;
	}

	public boolean isAutoRecord() {
		return isAutoRecord;
	}

	public void setAutoRecord(boolean isAutoRecord) {
		this.isAutoRecord = isAutoRecord;
	}


	public static MUCDetail createFactory(JSONObject jsonObject) {
		try {
			JSONObject mucObj = jsonObject.optJSONObject("mucDetail");
			MUCDetail mucDetail = MUCDetail.createFactoryWithoutName(mucObj);
			if (jsonObject.has("excludedMucMembers")) {
				JSONArray array = jsonObject.getJSONArray("excludedMucMembers");
				if(array != null) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject jObject = array.getJSONObject(i);
						ConnectionsMini connsMini = ConnectionsMini.createFactory(jObject);
						if (connsMini != null){
							mucDetail.listExcludedMucMembers.add(connsMini);
						}
					}
				}
			
			}
			// 附件
			if(jsonObject.has("listJTFile")){
				JSONArray array = jsonObject.getJSONArray("listJTFile");
				if(array != null ){
					for (int i = 0; i < array.length(); i++) {
						JSONObject objJTFile = array.getJSONObject(i);
						if (objJTFile != null) {
							JTFile jtFile = JTFile.createFactory(objJTFile);
							if (jtFile != null)
								mucDetail.listJTFile.add(jtFile);
						}
					}
				}
			}
			return mucDetail;
		}catch (Exception e) {
			return null;
		}
	}
	
	public static MUCDetail createFactoryWithoutName(JSONObject jsonObject) {
		try {
			MUCDetail self = new MUCDetail();
			self.id = jsonObject.optInt("id");
			self.compereID = jsonObject.optString("compereID");
			self.title = jsonObject.optString("title");
			self.subject = jsonObject.optString("subject");
			self.content = jsonObject.optString("content");
			self.groupId = jsonObject.optString("groupId");
			self.orderTime = jsonObject.optString("orderTime");
			self.isConference = jsonObject.optBoolean("isConference");
			self.max = jsonObject.optInt("max");
			self.isStick = jsonObject.optBoolean("isStick");
			self.isNotifyNews = jsonObject.optBoolean("isNotifyNews");
			self.isAutoRecord = jsonObject.optBoolean("isAutoRecord");
			
			// 群聊成员
			if (jsonObject.has("listConnectionsMini")) {
				JSONArray array = jsonObject.getJSONArray("listConnectionsMini");
				if (array != null) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject objConnections = array.getJSONObject(i);
						ConnectionsMini cmt = ConnectionsMini.createFactory(objConnections);
						if (cmt != null)
							self.listConnectionsMini.add(cmt);
					}
				}
			}
			return self;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<JTFile> getListJTFile() {
		return listJTFile;
	}

	public void setListJTFile(List<JTFile> listJTFile) {
		this.listJTFile = listJTFile;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompereID() {
		return compereID;
	}

	public void setCompereID(String compereID) {
		this.compereID = compereID;
	}
	
	public ArrayList<ConnectionsMini> getListExcludedMucMembers() {
		return listExcludedMucMembers;
	}

	public void setListExcludedMucMembers(
			ArrayList<ConnectionsMini> listExcludedMucMembers) {
		this.listExcludedMucMembers = listExcludedMucMembers;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	
}
