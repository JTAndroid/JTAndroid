package com.tr.ui.communities.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: CommunityApply
 * @Description: 通过群号加群
 * 
 */
public class CommunityApply implements Serializable {

	private static final long serialVersionUID = 5638717261982513903L;

	public static String APPLYTYPE_ALL = "1";//所有人
	public static String APPLYTYPE_REQ = "2";//申请加入
	
	private Community community;
	private String applayType;
	private boolean succeed;

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public String getApplayType() {
		return applayType;
	}

	public void setApplayType(String applayType) {
		this.applayType = applayType;
	}

	public boolean isSucceed() {
		return succeed;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}

}
