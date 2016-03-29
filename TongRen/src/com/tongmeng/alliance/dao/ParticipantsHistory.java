package com.tongmeng.alliance.dao;

public class ParticipantsHistory {
/*
 * "id":活动ID,
"title":"活动标题",
"picPath":封面,
"startTime":"开始时间 格式如2016-08-31 05:30",
"endTime":"结束时间 格式如2016-08-31 05:30",
"province":"省",
"city":"市",
"address":"地址"
 * 
 * */
	
	int id;
	String title;
	String picPath;
	String startTime;
	String endTime;
	String province;
	String city;
	String address;
//	String province;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public ParticipantsHistory(int id, String title, String picPath,
			String startTime, String endTime, String province, String city,
			String address) {
		super();
		this.id = id;
		this.title = title;
		this.picPath = picPath;
		this.startTime = startTime;
		this.endTime = endTime;
		this.province = province;
		this.city = city;
		this.address = address;
	}
	public ParticipantsHistory() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
