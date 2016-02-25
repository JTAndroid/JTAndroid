package com.tr.model.obj;

import java.io.Serializable;

import org.json.JSONObject;

public class DynamicApprove implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;// 点赞id
	private long dynamicId;// 动态id
	private long userId;// 点赞者id
	private String userName;// 点赞者名称
	private long ctime;// 点赞时间，格式如2015-01-14 11:10:28

	public static DynamicApprove createFactory(JSONObject jsonObject) {
		try {
			DynamicApprove self = new DynamicApprove();
			self.id = jsonObject.optLong("id");
			self.dynamicId = jsonObject.optLong("dynamicId");
			self.userId = jsonObject.optLong("userId");
			self.userName = jsonObject.optString("userName");
			self.ctime = jsonObject.optLong("ctime");
			return self;
		} catch (Exception e) {
			return null;
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDynamicId() {
		return dynamicId;
	}

	public void setDynamicId(long dynamicId) {
		this.dynamicId = dynamicId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}