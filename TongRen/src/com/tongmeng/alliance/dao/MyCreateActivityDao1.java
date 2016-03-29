package com.tongmeng.alliance.dao;

public class MyCreateActivityDao1 {

	public String id;
	public String title;
	public String startTime;
	public String endTime;
	public String picPath;
	public String status;
	public String peopleNumber;
	public String applyNumber;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPeopleNumber() {
		return peopleNumber;
	}

	public void setPeopleNumber(String peopleNumber) {
		this.peopleNumber = peopleNumber;
	}

	public String getApplyNumber() {
		return applyNumber;
	}

	public void setApplyNumber(String applyNumber) {
		this.applyNumber = applyNumber;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id:" + id + ",title:" + title + ",startTime:" + startTime
				+ ",endTime:" + endTime + ",picPath:" + picPath + ",status:"
				+ status + ",peopleNumber:" + peopleNumber + ",applyNumber:"
				+ applyNumber;
	}
}
