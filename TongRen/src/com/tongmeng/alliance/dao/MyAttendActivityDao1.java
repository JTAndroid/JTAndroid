package com.tongmeng.alliance.dao;

public class MyAttendActivityDao1 {
	public String id;
	public String title;
	public String picPath;
	public String startTime;
	public String endTime;
	public String province;
	public String city;
	public String address;
	public String status;
	public String payStatus;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	@Override
	public String toString() {
		return "id:" + id + ",title:" + title + ",picPath:" + picPath
				+ ",startTime:" + startTime + ",endTime:" + endTime
				+ ",province:" + province + ",city:" + city + ",address:"
				+ address + ",status:" + status + ",payStatus:" + payStatus;
	}
}
