package com.tr.model.obj;

import java.io.Serializable;

import org.json.JSONObject;

public class DynamicPeopleRelation implements Serializable {

	private static final long serialVersionUID = 1L;

	private long userId;// 提及某人
	private String userName;// 名称

	public static DynamicPeopleRelation createFactory(JSONObject jsonObject) {
		try {
			DynamicPeopleRelation self = new DynamicPeopleRelation();
			self.userId = jsonObject.optLong("userId");
			self.userName = jsonObject.optString("userName");
			return self;
		} catch (Exception e) {
			return null;
		}
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}