package com.tr.ui.flow.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 动态评论
 * 
 */
public class DynamicComment implements Serializable {

	/**
   * 
   */
	private static final long serialVersionUID = 1L;
	public static final int UNDELATE_FLAG = 0;
	public static final int DELATE_FLAG = -1;
	// 评论ID
	private long id;

	// 动态ID
	private long dynamicId;

	// 被评论者ID
	private long targetUserId;

	// 被评论者名称
	private String targetUserName;

	// 回复者ID
	private long userId;

	// 回复者名称
	private String userName;

	// 回复内容
	private String comment;
	// 回复时间，格式如2015-01-14 11:10:28
	private Long ctime;

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

	public Long getCtime() {
		return ctime;
	}

	public void setCtime(Long ctime) {
		this.ctime = ctime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
