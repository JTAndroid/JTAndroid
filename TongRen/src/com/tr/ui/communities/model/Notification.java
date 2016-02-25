package com.tr.ui.communities.model;

import java.io.Serializable;

import android.text.TextUtils;

/**
 * 后台返回的提示消息 eg: notification":{"notifInfo":"1005-该成员已存在该社群","notifCode":"0002"}
 */
public class Notification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String notifCode;
	private String notifInfo;

	public String getNotifCode() {
		return notifCode;
	}

	public void setNotifCode(String notifCode) {
		this.notifCode = notifCode;
	}

	public String getNotifInfo() {
		if(!TextUtils.isEmpty(notifInfo) && notifInfo.contains("1005-")){
			notifInfo.replaceFirst("1005-", "");
			return notifInfo;
		}
		return notifInfo;
	}

	public void setNotifInfo(String notifInfo) {
		this.notifInfo = notifInfo;
	}

}
