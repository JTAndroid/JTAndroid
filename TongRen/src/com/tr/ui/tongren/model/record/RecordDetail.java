package com.tr.ui.tongren.model.record;

import java.io.Serializable;

public class RecordDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// "attendanceSystemId":0,
	// "createTime":null,
	// "date":1449746451855,
	// "id":3918582987882501,
	// "ipAddrEnd":"",
	// "ipAddrStart":"",
	// "lonlatEnd":"",
	// "lonlatStart":"40.007313,116.387281",
	// "organizationId":3918182876446725,
	// "startWorkTime":1449746451855,
	// "updateTime":null,
	// "userId":13594,
	// "workTimeOut":null
	private String attendanceSystemId;
	private String createTime;
	private String date;
	private String id;
	private String ipAddrEnd;
	private String ipAddrStart;
	private String lonlatEnd;
	private String lonlatStart;
	private String organizationId;
	private String startWorkTime;
	private String updateTime;
	private String userId;
	private String workTimeOut;
	private RecordDetailExtend extend;

	private String end;
	private String endStatus;
	private String isWarkDay;
	private String start;
	private String startStatus;
	private String status;
	private String workDay;

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getEndStatus() {
		return endStatus;
	}

	public void setEndStatus(String endStatus) {
		this.endStatus = endStatus;
	}

	public String getIsWarkDay() {
		return isWarkDay;
	}

	public void setIsWarkDay(String isWarkDay) {
		this.isWarkDay = isWarkDay;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getStartStatus() {
		return startStatus;
	}

	public void setStartStatus(String startStatus) {
		this.startStatus = startStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWorkDay() {
		return workDay;
	}

	public void setWorkDay(String workDay) {
		this.workDay = workDay;
	}

	public RecordDetailExtend getExtend() {
		return extend;
	}

	public void setExtend(RecordDetailExtend extend) {
		this.extend = extend;
	}

	public String getAttendanceSystemId() {
		return attendanceSystemId;
	}

	public void setAttendanceSystemId(String attendanceSystemId) {
		this.attendanceSystemId = attendanceSystemId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIpAddrEnd() {
		return ipAddrEnd;
	}

	public void setIpAddrEnd(String ipAddrEnd) {
		this.ipAddrEnd = ipAddrEnd;
	}

	public String getIpAddrStart() {
		return ipAddrStart;
	}

	public void setIpAddrStart(String ipAddrStart) {
		this.ipAddrStart = ipAddrStart;
	}

	public String getLonlatEnd() {
		return lonlatEnd;
	}

	public void setLonlatEnd(String lonlatEnd) {
		this.lonlatEnd = lonlatEnd;
	}

	public String getLonlatStart() {
		return lonlatStart;
	}

	public void setLonlatStart(String lonlatStart) {
		this.lonlatStart = lonlatStart;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getStartWorkTime() {
		return startWorkTime;
	}

	public void setStartWorkTime(String startWorkTime) {
		this.startWorkTime = startWorkTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWorkTimeOut() {
		return workTimeOut;
	}

	public void setWorkTimeOut(String workTimeOut) {
		this.workTimeOut = workTimeOut;
	}

}
