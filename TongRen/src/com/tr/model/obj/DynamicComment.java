package com.tr.model.obj;

import java.io.Serializable;

import org.json.JSONObject;

public class DynamicComment implements Serializable {

	private static final long serialVersionUID = 1L;
	/* 主键 */
	private long id;
	/* 发布人id */
	private long dynamicId;
	/* 评论者id */
	private long userId;
	/* 评论者名字 */
	private String userName;
	/* 回复内容 */
	private String comment;
	/* 发布时间 格式"yyyy-MM-dd HH:mm:ss" */
	private long ctime;
	/* 是否删除 0：正常； -1：删除 */
	private int delstatus = 0;

	// 新增
	private long targetUserId;// 被评论者id
	private String targetUserName;// 被评论者名称

	public static DynamicComment createFactory(JSONObject jsonObject) {
		try {
			DynamicComment self = new DynamicComment();
			self.id = jsonObject.optLong("id");
			self.dynamicId = jsonObject.optLong("dynamicId");
			self.userId = jsonObject.optLong("userId");
			self.userName = jsonObject.optString("userName");
			self.comment = jsonObject.optString("comment");
			self.ctime = jsonObject.optLong("ctime");
			self.delstatus = jsonObject.optInt("delstatus");
			self.targetUserId = jsonObject.optLong("targetUserId");
			self.targetUserName = jsonObject.optString("targetUserName");
			return self;
		} catch (Exception e) {
			return null;
		}
	}

	public static long createFactoryWithName(JSONObject jsonObject) {
		try {
			long newId = jsonObject.optLong("newsId");
			return newId;
		} catch (Exception e) {
			return 0L;
		}
	}

	public long getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(long targetUserId) {
		this.targetUserId = targetUserId;
	}

	public String getTargetUserName() {
		return targetUserName;
	}

	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public int getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(int delstatus) {
		this.delstatus = delstatus;
	}

}