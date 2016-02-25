package com.tr.model;

import java.io.Serializable;

/**
 * 社群返回状态
 */
public class CommunityStateResult implements Serializable {

	private static final long serialVersionUID = 198009944223064422L;
	/* 社群id */
	private String mucId;
	/* 未读消息数 */
	private int newCount;
	/* 群消息提醒:0开启 1无声 2 关闭 */
	private int newMessageRemind;

	public String getMucId() {
		return mucId;
	}

	public void setMucId(String mucId) {
		this.mucId = mucId;
	}

	public int getNewCount() {
		return newCount;
	}

	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}

	public int getNewMessageRemind() {
		return newMessageRemind;
	}

	public void setNewMessageRemind(int newMessageRemind) {
		this.newMessageRemind = newMessageRemind;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
