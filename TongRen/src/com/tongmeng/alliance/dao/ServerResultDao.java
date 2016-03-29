package com.tongmeng.alliance.dao;

public class ServerResultDao {
	String notifyCode;
	String notifyInfo;
	String responseData;

	public String getNotifyCode() {
		return notifyCode;
	}

	public void setNotifyCode(String notifyCode) {
		this.notifyCode = notifyCode;
	}

	public String getNotifyInfo() {
		return notifyInfo;
	}

	public void setNotifyInfo(String notifyInfo) {
		this.notifyInfo = notifyInfo;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "notifyCode:" + notifyCode + ",notifyInfo:" + notifyInfo
				+ ",responseData:" + responseData;
	}

}
