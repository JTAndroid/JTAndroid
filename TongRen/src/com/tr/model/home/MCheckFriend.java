package com.tr.model.home;

import java.io.Serializable;

import org.json.JSONObject;

import com.utils.time.Util;

public class MCheckFriend implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3102609536966745581L;

	private boolean success;
	private long id;
	private boolean virtual;
	private int status;
	private String company;
	private String name;
	private String career;
	private String picPath;
	private long customerId;
	/**1. 仅好友可见  2.全部可见 */
	private int homePageVisible;

	public static MCheckFriend createFactory(JSONObject jsonObject) {
		MCheckFriend self = null;

		try {
			if (jsonObject != null) {
				// self = JSON.parseObject(jsonObject.toString(),
				// MCheckFriend.class);
				self = (MCheckFriend) Util.getParseJsonObject(jsonObject, MCheckFriend.class);
			}
			return self;
		} catch (Exception e) {
		}
		return null;
	}

	public int getHomePageVisible() {
		return homePageVisible;
	}

	public void setHomePageVisible(int homePageVisible) {
		this.homePageVisible = homePageVisible;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}
}
