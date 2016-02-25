package com.tr.model.im;

import java.io.Serializable;

import org.json.JSONObject;

import com.tr.model.SimpleResult;

public class Friend implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long userId;
	private long firendId;
	private int type;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getFirendId() {
		return firendId;
	}

	public void setFirendId(long firendId) {
		this.firendId = firendId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static Friend createFactory(JSONObject objFriend) {
		Friend friend = null;
		try {
			if (objFriend != null) {
				friend = new Friend();
				friend.userId = objFriend.optLong("userId");
				friend.type = objFriend.getInt("type");
				friend.firendId = objFriend.optLong("firendId");
			}
		} 
		catch (Exception e) {
		}
		return friend;
	}

}
