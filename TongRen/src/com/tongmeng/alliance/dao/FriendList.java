package com.tongmeng.alliance.dao;

public class FriendList {
	/*
	 * "id":好友ID,
	"friendName":"好友昵称",
	"picPath":好友头像
	 * */
	
	int id;
	String friendName;
	String picPath;
	public FriendList(int id, String friendName, String picPath) {
		super();
		this.id = id;
		this.friendName = friendName;
		this.picPath = picPath;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public FriendList() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
