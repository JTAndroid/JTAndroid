package com.tr.model.conference;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MMeetingSignLabel implements Serializable {
	private static final long serialVersionUID = -6331695063233911159L;
	// columns START
	/**
	 * 会议报名必填标签 db_column: id
	 */

	private java.lang.Long id;
	/**
	 * 会议ID db_column: meeting_id
	 */

	private java.lang.Long meetingId;
	/**
	 * 标签名字 db_column: label_name
	 */

	private java.lang.String labelName;
	/**
	 * 是否自定义 db_column: is_custom
	 */

	private java.lang.Integer isCustom;
	/**
	 * 创建者ID db_column: create_id
	 */

	private java.lang.Long createId;
	/**
	 * 创建者名字 db_column: create_Name
	 */

	private java.lang.String createName;
	/**
	 * 创建时间 db_column: create_time
	 */

	private String createTime;

	// columns END

	public void setId(java.lang.Long arg0) {
		this.id = arg0;
	}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setMeetingId(java.lang.Long arg0) {
		this.meetingId = arg0;
	}

	public java.lang.Long getMeetingId() {
		return this.meetingId;
	}

	public void setLabelName(java.lang.String arg0) {
		this.labelName = arg0;
	}

	public java.lang.String getLabelName() {
		if (null == this.labelName) {
			return "";
		}
		return this.labelName;
	}

	public void setIsCustom(java.lang.Integer arg0) {
		this.isCustom = arg0;
	}

	public java.lang.Integer getIsCustom() {
		return this.isCustom;
	}

	public void setCreateId(java.lang.Long arg0) {
		this.createId = arg0;
	}

	public java.lang.Long getCreateId() {
		return this.createId;
	}

	public void setCreateName(java.lang.String arg0) {
		this.createName = arg0;
	}

	public java.lang.String getCreateName() {
		if (null == this.createName) {
			return "";
		}
		return this.createName;
	}

	public void setCreateTime(String arg0) {
		this.createTime = arg0;
	}

	public String getCreateTime() {
		if (null == this.createName) {
			return "";
		}
		return this.createTime;
	}

	private static String sJsonArrayKeyName = "listMeetingSignLabel";

	public static Object createFactory(JSONObject jsonObject) {
		ArrayList<MMeetingSignLabel> arraylistRet = new ArrayList<MMeetingSignLabel>();
		if (!jsonObject.has(sJsonArrayKeyName)) {
			return null;
		}
		JSONArray joReqArry = null;
		try {
			joReqArry = jsonObject.getJSONArray(sJsonArrayKeyName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (null == joReqArry) {
			return null;

		}

		int iArrSize = joReqArry.length();
		MMeetingSignLabel aObject = null;
		JSONObject aObj = null;
		for (int i = 0; i < iArrSize; i++) {
			try {
				aObj = joReqArry.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (null == aObj) {
				continue;
			}
			aObject = new MMeetingSignLabel();
			if (null == aObject) {
				continue;
			}
			aObject.id = aObj.optLong("id");
			aObject.meetingId = aObj.optLong("meetingId");
			aObject.labelName = aObj.optString("labelName");
			aObject.isCustom = aObj.optInt("isCustom");
			aObject.createId = aObj.optLong("createId");
			aObject.createName = aObj.optString("createName");
			aObject.createTime = aObj.optString("createTime");
			arraylistRet.add(aObject);
		}
		return arraylistRet;
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("labelName", labelName);
		jObject.put("isCustom", isCustom);
		jObject.put("createId", createId);
		jObject.put("createName", createName);
		jObject.put("createTime", createTime);
		
		jObject.put("id", id);
		jObject.put("meetingId", meetingId);
		return jObject;
	}
}
