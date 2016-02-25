package com.tr.ui.organization.model;

import java.io.Serializable;

public class CCustomer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1930333908063408708L;
	/**
	 * 
	 */
	public String auth;
	public String banner;
//	public List<String> columns;
	public int createid;
	public int customerId;
	public String discribe;
	public String isListing;
	public boolean friends;
	public String linkEmail;
	public String linkMobile;
	public String name;
	public String picLogo;
	public String shotName;
	public String stockNum;
	public String type;
	public String virtual;
	
	@Override
	public String toString() {
		return "CCustomer [auth=" + auth + ", banner=" + banner + ", createid="
				+ createid + ", customerId=" + customerId + ", discribe="
				+ discribe + ", isListing=" + isListing + ", friends="
				+ friends + ", linkEmail=" + linkEmail + ", linkMobile="
				+ linkMobile + ", name=" + name + ", picLogo=" + picLogo
				+ ", shotName=" + shotName + ", stockNum=" + stockNum
				+ ", type=" + type + ", virtual=" + virtual + "]";
	}
	
	

}
