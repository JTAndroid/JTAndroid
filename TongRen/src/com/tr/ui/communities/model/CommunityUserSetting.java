package com.tr.ui.communities.model;

import java.io.Serializable;

public class CommunityUserSetting implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// "id":"主键信息",
	// ”communityId“：”社群id“,
	// ”newMessageRemind“:"群消息提醒:0开启 1无声 2 关闭"
	// "nickname":"社群里的昵称",
	// "showNickname":"是否显示群成员昵称：0是 1否",
	// "createdUserId":"用户id"，
	// “createdTime”：“创建时间”,
	// "updatedTime":"更新时间"
	private String id;
	private String communityId;
	private String newMessageRemind;
	private String nickname;
	private String showNickname;
	private String createdUserId;
	private String createdTime;
	private String updatedTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getNewMessageRemind() {
		return newMessageRemind;
	}

	public void setNewMessageRemind(String newMessageRemind) {
		this.newMessageRemind = newMessageRemind;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getShowNickname() {
		return showNickname;
	}

	public void setShowNickname(String showNickname) {
		this.showNickname = showNickname;
	}

	public String getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(String createdUserId) {
		this.createdUserId = createdUserId;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

}
