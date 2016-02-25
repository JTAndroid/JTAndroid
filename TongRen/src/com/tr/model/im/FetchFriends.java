package com.tr.model.im;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FetchFriends implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long createdUserId;
	private List<Friend> firends = new ArrayList<Friend>();

	public static FetchFriends createFactory(JSONObject jsonObject) {
		FetchFriends ffs = null;
		try {
			if (jsonObject != null) {
				ffs = new FetchFriends();
				ffs.createdUserId = jsonObject.optLong("createdUserId");
				JSONArray array = jsonObject.optJSONArray("firends");
				if (array != null) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject objFriend = array.getJSONObject(i);
						if (objFriend != null) {
							Friend friend = Friend.createFactory(objFriend);
							if (friend != null)
								ffs.firends.add(friend);
						}
					}
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ffs;
	}
	
	public long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public List<Friend> getFirends() {
		return firends;
	}

	public void setFirends(List<Friend> firends) {
		this.firends = firends;
	}


}
