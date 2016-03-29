package com.tongmeng.alliance.dao;

public class MyCreateActivitis {
	int id ; 
	String title ;
	String startTime ; 
	String picPath ;
	int status ;
	int peopleNumber ;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPeopleNumber() {
		return peopleNumber;
	}
	public void setPeopleNumber(int peopleNumber) {
		this.peopleNumber = peopleNumber;
	}
	public MyCreateActivitis() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MyCreateActivitis(int id, String title, String startTime,
			String picPath, int status, int peopleNumber) {
		super();
		this.id = id;
		this.title = title;
		this.startTime = startTime;
		this.picPath = picPath;
		this.status = status;
		this.peopleNumber = peopleNumber;
	}
	
}
