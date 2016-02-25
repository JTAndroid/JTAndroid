package com.tr.model.conference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MMeetingNoticeRelation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7128373950086720837L;
	
	/** 通知内容 */
	private java.lang.String noticeContent;
	/** 会议ID */
	private java.lang.Long meetingId;
	/** 人ID */
	private java.lang.Long memberId;
	/** 会议名字 */
	private java.lang.String meetingName;
	/**会议图片*/
	private String meetingPic;
	/** 最后修改时间 */
	private String updateTime;
	/** 通知明细 */
	private TreeSet<MMeetingNoticeQuery> setMeetingNoticeQuery;
	/** 创建者ID */
	private java.lang.String meetingCreateId;
	/** 创建者名字 */
	private java.lang.String meetingCreateName;
	
	
	
	
	/** 主键 */
	private java.lang.Long id;
	/** 群聊id */
	private String groupId;

	public String getMeetingPic() {
		return meetingPic;
	}

	public void setMeetingPic(String meetingPic) {
		this.meetingPic = meetingPic;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public void setNoticeContent(java.lang.String arg0) {
		this.noticeContent = arg0;
	}

	public java.lang.String getNoticeContent() {
		if (null == this.noticeContent) {
			return "";
		}
		return this.noticeContent;
	}

	public void setMeetingId(java.lang.Long arg0) {
		this.meetingId = arg0;
	}

	public java.lang.Long getMeetingId() {
		return this.meetingId;
	}

	public void setMemberId(java.lang.Long arg0) {
		this.memberId = arg0;
	}

	public java.lang.Long getMemberId() {
		return this.memberId;
	}

	public void setMeetingName(java.lang.String arg0) {
		this.meetingName = arg0;
	}

	public java.lang.String getMeetingName() {
		if (null == this.meetingName) {
			return "";
		}
		return this.meetingName;
	}

	public void setUpdateTime(String arg0) {
		this.updateTime = arg0;
	}

	public String getUpdateTime() {
		if (null == this.updateTime) {
			return "";
		}
		return this.updateTime;
	}

	public void setMeetingNoticeQuery(TreeSet<MMeetingNoticeQuery> arg0) {
		this.setMeetingNoticeQuery = arg0;
	}

	public TreeSet<MMeetingNoticeQuery> getMeetingNoticeQuery() {
		if (null == setMeetingNoticeQuery) {
			return new TreeSet<MMeetingNoticeQuery>();
		}
		return this.setMeetingNoticeQuery;
	}

	public void setMeetingCreateId(java.lang.String arg0) {
		this.meetingCreateId = arg0;
	}

	public java.lang.String getMeetingCreateId() {
		if (null == this.meetingCreateId) {
			return "";
		}
		return this.meetingCreateId;
	}

	public void setMeetingCreateName(java.lang.String arg0) {
		this.meetingCreateName = arg0;
	}

	public java.lang.String getMeetingCreateName() {
		if (null == this.meetingCreateName) {
			return "";
		}
		if (meetingCreateName.equals("null")) {
			return "";
		}
		return this.meetingCreateName;
	}

	private static String sJsonArrayKeyName = "listMeetingNoticeRelation";

	public static Object createFactory(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}
		ArrayList<MMeetingNoticeRelation> arraylistRet = new ArrayList<MMeetingNoticeRelation>();
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
		if (iArrSize <= 0) {
			return null;
		}
		MMeetingNoticeRelation aObject = null;
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
			aObject = new MMeetingNoticeRelation();
			if (null == aObject) {
				continue;
			}
			aObject.noticeContent = aObj.optString("noticeContent");
			aObject.meetingId = aObj.optLong("meetingId");
			aObject.memberId = aObj.optLong("memberId");
			aObject.meetingName = aObj.optString("meetingName");
			aObject.meetingPic = aObj.optString("meetingPic");
			aObject.updateTime = aObj.optString("updateTime");
			
			
			aObject.id = aObj.optLong("id");
			aObject.setMeetingNoticeQuery = new TreeSet<MMeetingNoticeQuery>();
			JSONArray aAry = aObj.optJSONArray(MMeetingNoticeQuery.sJsonArrayKeyName);
			if (null != aAry) {
				aObject.setMeetingNoticeQuery = (TreeSet<MMeetingNoticeQuery>) MMeetingNoticeQuery.createFactoryByArray(aAry);
			}
			aObject.meetingCreateId = aObj.optString("meetingCreateId");
			aObject.meetingCreateName = aObj.optString("meetingCreateName");
			arraylistRet.add(aObject);
		}
		return arraylistRet;
	}
}
