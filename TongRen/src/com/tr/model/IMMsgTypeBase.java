package com.tr.model;

import java.io.Serializable;

public class IMMsgTypeBase implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7313085974111844472L;
	
	
	protected String mFrom;
	protected String mTimestamp;
	protected String nickName;
	

	public void setFrom(String from) {
		this.mFrom = from;
	}

	public String getFrom() {
		return mFrom;
	}
	
	
	public String getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(String mTimestamp) {
		this.mTimestamp = mTimestamp;
	}

	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String mNickname) {
		this.nickName = mNickname;
	}

}
