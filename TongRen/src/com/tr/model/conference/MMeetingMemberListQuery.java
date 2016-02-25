package com.tr.model.conference;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的邀请函列表
 */
public class MMeetingMemberListQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1126268928110813372L;
	/** 会议序号 */
	private java.lang.Long id;
	/** 会议名称 */
	private java.lang.String meetingName;
	/** 会议地点 */
	private java.lang.String meetingAddress;
	/** 会议地点X坐标 */
	private java.lang.String meetingAddressPosX;
	/** 会议地点Y坐标 */
	private java.lang.String meetingAddressPosY;
	/** 会议地点国家 */
	public java.lang.Integer country;
	/** 会议地点省份 */
	public String province;
	/** 会议地点市 */
	public String city;
	/** 会议地点城镇 */
	public String town;
	/** 会议开始时间 */
	private String startTime;
	/** 会议结束时间 */
	private String endTime;
	/** 会议类型 0：无议题1：有议题 */
	private java.lang.Integer meetingType;
	/** 会议状态 0：草稿，1：发起,2会议进行中，3会议结束 */
	private java.lang.Integer meetingStatus;
	/** 是否保密 */
	private java.lang.Boolean isSecrecy;
	/** 会议人数 */
	private java.lang.Integer memberCount;
	/** 会议介绍 */
	private java.lang.String meetingDesc;
	/** 创建人ID */
	private java.lang.Long createId;
	/** 创建人姓名 */
	private String createName;
	/** 创建人头像 */
	private String createImage;
	/** 附件id */
	private java.lang.String taskId;
	/** 创建时间 */
	private String createTime;
	/** 类型（0 ：会议，1 ：邀请函，2:通知） */
	private String type;
	/** 是否报名 */
	private String isSignUp;
	/** 会议封面图片 */
	private String path;
	/** 是否归档 人员处理会议的状态 0：默认，1：归档，2：删除 */
	private String memberMeetStatus;
	/** 会议距离我的距离 */
	private Double distance;
	
	/** 群聊Id */
	private String groupId;
	/**参会时间*/
	private String attendMeetTime;
	/**会议删除标记 0：默认、1：删除*/
	private int meetingDeleteFlag;
	/**参会人删除标记 0：默认、1：删除*/
	private int memberDeleteFlag;
	/**参会状态 0:未答复 1:接受邀请 2:拒绝邀请*/
	private int attendMeetStatus;

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public void setMeetingName(java.lang.String arg0) {
		this.meetingName = arg0;
	}

	public java.lang.String getMeetingName() {
		if (null == this.meetingName) {
			return "";
		}
		if (this.meetingName.equals("null")) {
			return "";
		}
		return this.meetingName;
	}

	public void setMeetingAddress(java.lang.String arg0) {
		this.meetingAddress = arg0;
	}

	public java.lang.String getMeetingAddress() {
		if (null == this.meetingAddress) {
			return "";
		}
		if (this.meetingAddress.equals("null")) {
			return "";
		}
		return this.meetingAddress;
	}

	public void setMeetingAddressPosX(java.lang.String arg0) {
		this.meetingAddressPosX = arg0;
	}

	public java.lang.String getMeetingAddressPosX() {
		if (null == this.meetingAddressPosX) {
			return "0.0";
		}
		if (meetingAddressPosX.equals("null")) {
			return "0.0";
		}
		return this.meetingAddressPosX;
	}

	public void setMeetingAddressPosY(java.lang.String arg0) {
		this.meetingAddressPosY = arg0;
	}

	public java.lang.String getMeetingAddressPosY() {
		if (null == this.meetingAddressPosY) {
			return "0.0";
		}
		if (meetingAddressPosY.equals("null")) {
			return "0.0";
		}
		return this.meetingAddressPosY;
	}

	public void setCountry(java.lang.Integer arg0) {
		this.country = arg0;
	}

	public java.lang.Integer getCountry() {
		return this.country;
	}

	public void setProvince(String arg0) {
		this.province = arg0;
	}

	public String getProvince() {
		if (null == this.province) {
			return "";
		}
		if (this.province.equals("null")) {
			return "";
		}
		return this.province;
	}

	public void setCity(String arg0) {
		this.city = arg0;
	}

	public String getCity() {
		if (null == this.city) {
			return "";
		}
		if (this.city.equals("null")) {
			return "";
		}
		return this.city;
	}

	public void setTown(String arg0) {
		this.town = arg0;
	}

	public String getTown() {
		if (null == this.town) {
			return "";
		}
		if (this.town.equals("null")) {
			return "";
		}
		return this.town;
	}

	public void setStartTime(String arg0) {
		this.startTime = arg0;
	}

	public String getStartTime() {
		if (null == this.startTime) {
			return "";
		}
		if (this.startTime.equals("null")) {
			return "";
		}
		return this.startTime;
	}

	public void setEndTime(String arg0) {
		this.endTime = arg0;
	}

	public String getEndTime() {
		if (null == this.endTime) {
			return "";
		}
		if (this.endTime.equals("null")) {
			return "";
		}
		return this.endTime;
	}

	public void setMeetingType(java.lang.Integer arg0) {
		this.meetingType = arg0;
	}

	public java.lang.Integer getMeetingType() {
		return this.meetingType;
	}

	public void setMeetingStatus(java.lang.Integer arg0) {
		this.meetingStatus = arg0;
	}

	public java.lang.Integer getMeetingStatus() {
		return this.meetingStatus;
	}

	public void setIsSecrecy(java.lang.Boolean arg0) {
		this.isSecrecy = arg0;
	}

	public java.lang.Boolean getIsSecrecy() {
		return this.isSecrecy;
	}

	public void setMemberCount(java.lang.Integer arg0) {
		this.memberCount = arg0;
	}

	public java.lang.Integer getMemberCount() {
		return this.memberCount;
	}

	public void setMeetingDesc(java.lang.String arg0) {
		this.meetingDesc = arg0;
	}

	public java.lang.String getMeetingDesc() {
		if (null == this.meetingDesc) {
			return "";
		}
		if (this.meetingDesc.equals("null")) {
			return "";
		}
		return this.meetingDesc;
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
		if (this.createName.equals("null")) {
			return "";
		}
		return this.createName;
	}

	public void setCreateImage(java.lang.String arg0) {
		this.createImage = arg0;
	}

	public java.lang.String getCreateImage() {
		if (null == this.createImage) {
			return "";
		}
		if (this.createImage.equals("null")) {
			return "";
		}
		return this.createImage;
	}

	public void setTaskId(java.lang.String arg0) {
		this.taskId = arg0;
	}

	public java.lang.String getTaskId() {
		if (null == this.taskId) {
			return "";
		}
		if (this.taskId.equals("null")) {
			return "";
		}
		return this.taskId;
	}

	public void setCreateTime(String arg0) {
		this.createTime = arg0;
	}

	public String getCreateTime() {
		if (null == this.createTime) {
			return "";
		}
		if (this.createTime.equals("null")) {
			return "";
		}
		return this.createTime;
	}

	public void setType(java.lang.String arg0) {
		this.type = arg0;
	}

	public java.lang.String getType() {
		if (null == this.type) {
			return "";
		}
		if (this.type.equals("null")) {
			return "";
		}
		return this.type;
	}

	public void setIsSignUp(java.lang.String arg0) {
		this.isSignUp = arg0;
	}

	public java.lang.String getIsSignUp() {
		if (null == this.isSignUp) {
			return "";
		}
		if (this.isSignUp.equals("null")) {
			return "";
		}
		return this.isSignUp;
	}

	public void setPath(java.lang.String arg0) {
		this.path = arg0;
	}

	public java.lang.String getPath() {
		if (null == this.path) {
			return "";
		}
		if (this.path.equals("null")) {
			return "";
		}
		return this.path;
	}

	public void setMemberMeetStatus(java.lang.String arg0) {
		this.memberMeetStatus = arg0;
	}

	public java.lang.String getMemberMeetStatus() {
		if (null == this.memberMeetStatus) {
			return "";
		}
		if (this.memberMeetStatus.equals("null")) {
			return "";
		}
		return this.memberMeetStatus;
	}

	public void setDistance(java.lang.Double arg0) {
		this.distance = arg0;
	}

	public java.lang.Double getDistance() {
		return this.distance;
	}

	public String getAttendMeetTime() {
		if (null == this.attendMeetTime) {
			return "";
		}
		if (this.attendMeetTime.equals("null")) {
			return "";
		}
		return attendMeetTime;
	}

	public void setAttendMeetTime(String attendMeetTime) {
		this.attendMeetTime = attendMeetTime;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}



	public static String sJsonArrayKeyName = "listMeetingMemberListQuery";

	public static Object createFactory(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}
		ArrayList<MMeetingMemberListQuery> arraylistRet = new ArrayList<MMeetingMemberListQuery>();
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
		MMeetingMemberListQuery aObject = null;
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
			aObject = new MMeetingMemberListQuery();
			if (null == aObject) {
				continue;
			}
			aObject.groupId = aObj.optString("groupId");
			aObject.id = aObj.optLong("id");
			aObject.meetingName = aObj.optString("meetingName");
			aObject.meetingAddress = aObj.optString("meetingAddress");
			aObject.meetingAddressPosX = aObj.optString("meetingAddressPosX");
			aObject.meetingAddressPosY = aObj.optString("meetingAddressPosY");
			aObject.country = aObj.optInt("country");
			aObject.province = aObj.optString("province");
			aObject.city = aObj.optString("city");
			aObject.town = aObj.optString("town");
			aObject.startTime = aObj.optString("startTime");
			aObject.endTime = aObj.optString("endTime");
			aObject.meetingType = aObj.optInt("meetingType");
			aObject.meetingStatus = aObj.optInt("meetingStatus");
			aObject.isSecrecy = aObj.optBoolean("isSecrecy");
			aObject.memberCount = aObj.optInt("memberCount");
			aObject.meetingDesc = aObj.optString("meetingDesc");
			aObject.createId = aObj.optLong("createId");
			aObject.createName = aObj.optString("createName");
			aObject.createImage = aObj.optString("createImage");
			aObject.taskId = aObj.optString("taskId");
			aObject.createTime = aObj.optString("createTime");
			aObject.type = aObj.optString("type");
			aObject.isSignUp = aObj.optString("isSignUp");
			aObject.path = aObj.optString("path");
			aObject.memberMeetStatus = aObj.optString("memberMeetStatus");
			aObject.distance = aObj.optDouble("distance");
			aObject.attendMeetTime = aObj.optString("attendMeetTime");
			aObject.meetingDeleteFlag = aObj.optInt("meetingDeleteFlag");
			aObject.memberDeleteFlag = aObj.optInt("memberDeleteFlag");
			aObject.attendMeetStatus = aObj.optInt("attendMeetStatus");
			arraylistRet.add(aObject);
		}
		return arraylistRet;
	}

	public int getMeetingDeleteFlag() {
		return meetingDeleteFlag;
	}

	public void setMeetingDeleteFlag(int meetingDeleteFlag) {
		this.meetingDeleteFlag = meetingDeleteFlag;
	}

	public int getMemberDeleteFlag() {
		return memberDeleteFlag;
	}

	public void setMemberDeleteFlag(int memberDeleteFlag) {
		this.memberDeleteFlag = memberDeleteFlag;
	}

	public int getAttendMeetStatus() {
		return attendMeetStatus;
	}

	public void setAttendMeetStatus(int attendMeetStatus) {
		this.attendMeetStatus = attendMeetStatus;
	}
}
