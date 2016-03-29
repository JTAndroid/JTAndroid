package com.tongmeng.alliance.dao;

public class MyparticipateActivity {
	int id ;
	String title;
	String startTime ;
	String picPath ;
	String province ;
	String city;
	String address ;
	int status ;
	int payStatus ;
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
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	public MyparticipateActivity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MyparticipateActivity(int id, String title, String startTime,
			String picPath, String province, String city, String address,
			int status, int payStatus) {
		super();
		this.id = id;
		this.title = title;
		this.startTime = startTime;
		this.picPath = picPath;
		this.province = province;
		this.city = city;
		this.address = address;
		this.status = status;
		this.payStatus = payStatus;
	}
	
}
