package com.tongmeng.alliance.dao;

public class GroupListDao {

	public String id;
	public String huanxinId;
	public String activityId;
	public String name;
	public String memberCount;
	public String lastMessage;
	public String picPath;
	public String role;
	public String lastMessageTime;

	public String getLastMessageTime() {
		return lastMessageTime;
	}

	public void setLastMessageTime(String lastMessageTime) {
		this.lastMessageTime = lastMessageTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHuanxinId() {
		return huanxinId;
	}

	public void setHuanxinId(String huanxinId) {
		this.huanxinId = huanxinId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(String memberCount) {
		this.memberCount = memberCount;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id:" + id + ",activityId:" + activityId + ",huanxinId:"
				+ huanxinId + ",name:" + name + ",memberCount:" + memberCount
				+ ",lastMessage:" + lastMessage + ",picPath:" + picPath
				+ ",role:" + role + ",lastMessageTime:" + lastMessageTime;
	}
}
