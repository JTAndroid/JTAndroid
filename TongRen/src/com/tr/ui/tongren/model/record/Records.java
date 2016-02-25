package com.tr.ui.tongren.model.record;

import java.io.Serializable;
import java.util.List;

public class Records implements Serializable {

	/**
	 * realWorkDays 实际出勤天数 comelateDays 迟到天数 leaveearlierDays 早退天数 absenceDays
	 * 旷工天数 normalWorkDays 应出勤天数
	 */
	private static final long serialVersionUID = 1L;

	private String absenceDays; // 旷工天数
	private String comelateDays; // 迟到天数
	private String leaveearlierDays; // 早退天数
	private String month;
	private String normalWorkDays;// 应出勤天数
	private String realWorkDays;// 实际出勤天数
	private String userId;//
	private String userName;//

	private List<RecordDetail> recordDetail;

	public String getAbsenceDays() {
		return absenceDays;
	}

	public void setAbsenceDays(String absenceDays) {
		this.absenceDays = absenceDays;
	}

	public String getComelateDays() {
		return comelateDays;
	}

	public void setComelateDays(String comelateDays) {
		this.comelateDays = comelateDays;
	}

	public String getLeaveearlierDays() {
		return leaveearlierDays;
	}

	public void setLeaveearlierDays(String leaveearlierDays) {
		this.leaveearlierDays = leaveearlierDays;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getNormalWorkDays() {
		return normalWorkDays;
	}

	public void setNormalWorkDays(String normalWorkDays) {
		this.normalWorkDays = normalWorkDays;
	}

	public String getRealWorkDays() {
		return realWorkDays;
	}

	public void setRealWorkDays(String realWorkDays) {
		this.realWorkDays = realWorkDays;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<RecordDetail> getRecordDetail() {
		return recordDetail;
	}

	public void setRecordDetail(List<RecordDetail> recordDetail) {
		this.recordDetail = recordDetail;
	}

}
