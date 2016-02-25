package com.tr.model.conference;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MMeetingSignLabelDataQuery implements Serializable {
	private static final long serialVersionUID = 6036227998587476691L;
	/** 用户报名信息主键 */
	private java.lang.Long id;
	/** 会议必填标签ID */
	private java.lang.Long mslabelId;
	/** 会议必填标签名字 */
	private java.lang.String mslabelName;
	/** 用户必填信息 */
	private java.lang.String labelContent;
	/** 参会成员ID */
	private java.lang.Long memberId;
	/** 参会成员名字 */
	private java.lang.String memberName;
	/** 是否自定义字段 */
	private java.lang.Integer isCustomer;
	/** 是否人脉数据 */
	private java.lang.Integer isPeopleData;
	/** 创建者ID */
	private java.lang.Long createId;
	/** 创建者名字 */
	private java.lang.String createName;
	/** 创建者时间 */
	private String createTime;

	public void setId(java.lang.Long arg0) {
		this.id = arg0;
	}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setMslabelId(java.lang.Long arg0) {
		this.mslabelId = arg0;
	}

	public java.lang.Long getMslabelId() {
		return this.mslabelId;
	}

	public void setMslabelName(java.lang.String arg0) {
		this.mslabelName = arg0;
	}

	public java.lang.String getMslabelName() {
		if (null == this.mslabelName) {
			return "";
		}
		return this.mslabelName;
	}

	public void setLabelContent(java.lang.String arg0) {
		this.labelContent = arg0;
	}

	public java.lang.String getLabelContent() {
		if (null == this.labelContent) {
			return "";
		}
		return this.labelContent;
	}

	public void setMemberId(java.lang.Long arg0) {
		this.memberId = arg0;
	}

	public java.lang.Long getMemberId() {
		return this.memberId;
	}

	public void setMemberName(java.lang.String arg0) {
		this.memberName = arg0;
	}

	public java.lang.String getMemberName() {
		if (null == this.memberName) {
			return "";
		}
		return this.memberName;
	}

	public void setIsCustomer(java.lang.Integer arg0) {
		this.isCustomer = arg0;
	}

	public java.lang.Integer getIsCustomer() {
		return this.isCustomer;
	}

	public void setIsPeopleData(java.lang.Integer arg0) {
		this.isPeopleData = arg0;
	}

	public java.lang.Integer getIsPeopleData() {
		return this.isPeopleData;
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
		if (null == this.createTime) {
			return "";
		}
		return this.createTime;
	}

	public static String sJsonArrayKeyName = "listMeetingSignLabelDataQuery";

	public static Object createFactoryByArray(JSONArray jsonArray) {
		if (null == jsonArray) {
			return null;
		}
		ArrayList<MMeetingSignLabelDataQuery> arraylistRet = new ArrayList<MMeetingSignLabelDataQuery>();
		int iArrSize = jsonArray.length();
		if (iArrSize <= 0) {
			return null;
		}
		MMeetingSignLabelDataQuery aObject = null;
		JSONObject aObj = null;
		for (int i = 0; i < iArrSize; i++) {
			try {
				aObj = jsonArray.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (null == aObj) {
				continue;
			}
			aObject = new MMeetingSignLabelDataQuery();
			if (null == aObject) {
				continue;
			}
			aObject.id = aObj.optLong("id");
			aObject.mslabelId = aObj.optLong("mslabelId");
			aObject.mslabelName = aObj.optString("mslabelName");
			aObject.labelContent = aObj.optString("labelContent");
			aObject.memberId = aObj.optLong("memberId");
			aObject.memberName = aObj.optString("memberName");
			aObject.isCustomer = aObj.optInt("isCustomer");
			aObject.isPeopleData = aObj.optInt("isPeopleData");
			aObject.createId = aObj.optLong("createId");
			aObject.createName = aObj.optString("createName");
			aObject.createTime = aObj.optString("createTime");
			arraylistRet.add(aObject);
		}
		return arraylistRet;
	}

	public static Object createFactory(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}
		ArrayList<MMeetingSignLabelDataQuery> arraylistRet = new ArrayList<MMeetingSignLabelDataQuery>();
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
		MMeetingSignLabelDataQuery aObject = null;
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
			aObject = new MMeetingSignLabelDataQuery();
			if (null == aObject) {
				continue;
			}
			aObject.id = aObj.optLong("id");
			aObject.mslabelId = aObj.optLong("mslabelId");
			aObject.mslabelName = aObj.optString("mslabelName");
			aObject.labelContent = aObj.optString("labelContent");
			aObject.memberId = aObj.optLong("memberId");
			aObject.memberName = aObj.optString("memberName");
			aObject.isCustomer = aObj.optInt("isCustomer");
			aObject.isPeopleData = aObj.optInt("isPeopleData");
			aObject.createId = aObj.optLong("createId");
			aObject.createName = aObj.optString("createName");
			aObject.createTime = aObj.optString("createTime");
			arraylistRet.add(aObject);
		}
		return arraylistRet;
	}
}
