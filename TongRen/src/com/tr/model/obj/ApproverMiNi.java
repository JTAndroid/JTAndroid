package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * 赞同评价的人mini对象
 * @author zhongshan
 *
 */
public class ApproverMiNi implements Serializable {
	/** 评价人id */
	private long userId;
	/** 头像地址 */
	private String imageUrl;
	/**用户/人脉*/
	private boolean isOnline;

	public ApproverMiNi() {
		super();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public void initWithJson(JSONObject jsonObject) throws JSONException, MalformedURLException, ParseException {
		String str_key = null;

		str_key = "userId";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			setUserId(jsonObject.getLong(str_key));
		}

		str_key = "imageUrl";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			setImageUrl(jsonObject.getString(str_key));
		}

		str_key = "isonline";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			setOnline(jsonObject.getBoolean(str_key));
		}

	}

}
