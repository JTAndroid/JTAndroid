package com.tongmeng.alliance.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.internal.Streams;

/**
 * 会议详情类，用于会议详情界面
 * 
 * @author Administrator
 * 
 */
public class Meeting {
	int id;
	String title;
	String picPath;
	int picFileId;
	int createrId;
	String createrName;
	String createrPic;
	String startTime;
	String endTime;
	String province;
	String city;
	String address;
	String fee;
	int peopleNumber;
	int applyNumber;
	int status;// 状态 1:未开始 2:已开始 3:已结束
	String activityDesc;
	ArrayList<String> activityLabelList;// 活动标签
	ArrayList<String> applyLabelList;// 报名标签
	String isAttender;// 是否已参加
	String redirectUrl;
	// 坐标地址
	String positionX;
	String positionY;
	int isOpen;// 是否公开 0:不公开 1:公开
	String source;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

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

	public int getPicFileId() {
		return picFileId;
	}

	public void setPicFileId(int picFileId) {
		this.picFileId = picFileId;
	}

	public int getCreaterId() {
		return createrId;
	}

	public void setCreaterId(int createrId) {
		this.createrId = createrId;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getCreaterPic() {
		return createrPic;
	}

	public void setCreaterPic(String createrPic) {
		this.createrPic = createrPic;
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

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public int getPeopleNumber() {
		return peopleNumber;
	}

	public void setPeopleNumber(int peopleNumber) {
		this.peopleNumber = peopleNumber;
	}

	public int getApplyNumber() {
		return applyNumber;
	}

	public void setApplyNumber(int applyNumber) {
		this.applyNumber = applyNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}

	public ArrayList<String> getActivityLabelList() {
		return activityLabelList;
	}

	public void setActivityLabelList(ArrayList<String> activityLabelList) {
		this.activityLabelList = activityLabelList;
	}

	public ArrayList<String> getApplyLabelList() {
		return applyLabelList;
	}

	public void setApplyLabelList(ArrayList<String> applyLabelList) {
		this.applyLabelList = applyLabelList;
	}

	public String getIsAttender() {
		return isAttender;
	}

	public void setIsAttender(String isAttender) {
		this.isAttender = isAttender;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getPositionX() {
		return positionX;
	}

	public void setPositionX(String positionX) {
		this.positionX = positionX;
	}

	public String getPositionY() {
		return positionY;
	}

	public void setPositionY(String positionY) {
		this.positionY = positionY;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id:" + id + ", title:" + title + ", picPath:" + picPath
				+ ",picFileId:" + picFileId + ",createrId:" + createrId + ",createrName:"
				+ createrName + ",createrPic:" + createrPic + ",startTime :" + startTime
				+ ",endTime:" + endTime + ",province:" + province + ",city:" + city
				+ ",address:" + address + ",fee:" + fee + ",peopleNumber:" + peopleNumber
				+ ",applyNumber:" + applyNumber + ",activityDesc:" + activityDesc
				+ ",activityLabelList:" + activityLabelList.toString() + ",applyLabelList:"
				+ applyLabelList.toString() + ",status:" + status + ",isAttender:"
				+ isAttender + ",坐标:" + "(" + positionX + "," + positionY
				+ "), isOpen:" + isOpen;
	}
}
