package com.tr.ui.people.model.CommentApproverLists;

import java.io.Serializable;

public class ApproverMiNi implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 评价人id */
	public long userId;
	/** 头像地址 */
	public String imageUrl;
	/**用户/人脉*/
	public boolean isOnline;
	@Override
	public String toString() {
		return "ApproverMiNi [userId=" + userId + ", imageUrl=" + imageUrl
				+ ", isOnline=" + isOnline + "]";
	}
	
	
}
