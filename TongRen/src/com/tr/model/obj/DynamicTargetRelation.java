package com.tr.model.obj;

import java.io.Serializable;

import org.json.JSONObject;

public class DynamicTargetRelation implements Serializable {

	private static final long serialVersionUID = 1L;

	private long targetId;// 目标id，例如type为10或11时，此id就是知识的id
	private String targetTitle;// 目标标题或名称 如果是知识就是标题，如果是人或组织就是姓名
	private String picPath;// 目标图片
	private String ctime;// 格式如2015-01-14 11:10:28
	private int type;// 目标类型 例如 10：创建知识；11：转发知识

	public static DynamicTargetRelation createFactory(JSONObject jsonObject) {
		try {
			DynamicTargetRelation self = new DynamicTargetRelation();
			self.targetId = jsonObject.optLong("targetId");
			self.targetTitle = jsonObject.optString("targetTitle");
			self.picPath = jsonObject.optString("picPath");
			self.ctime = jsonObject.optString("ctime");
			self.type = jsonObject.optInt("type");
			return self;
		} catch (Exception e) {
			return null;
		}
	}
	
	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public String getTargetTitle() {
		return targetTitle;
	}

	public void setTargetTitle(String targetTitle) {
		this.targetTitle = targetTitle;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}