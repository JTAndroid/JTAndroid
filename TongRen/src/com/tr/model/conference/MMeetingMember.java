package com.tr.model.conference;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class MMeetingMember implements Serializable {
	private static final long serialVersionUID = 8846638681235807118L;
	/**记录ID*/
	private long id;
	/**成员ID*/
	private long memberId;
	/**会议ID*/
	private long meetingId;
	/**角色 0：嘉宾 1：群众 2：创建*/
	private int memberType;
	/**人员姓名*/
	private String memberName;
	/**人员图片*/
	private String memberPhoto;
	/**人员处理会议的状态 0：默认 1：归档 2：删除*/
	private int memberMeetStatus;
	/**参会状态 0：未答复 1：接受邀请 2：拒绝邀请 4：报名 5：取消报名*/
	private int attendMeetStatus;
	/**是否签到 0：未签，1：已签*/
	private int isSign;
	/**签到距离*/
	private String signDistance;
	/**参会方式 0：邀请 1：报名*/
	private int attendMeetType;
	/**处理会议报名：0：未处理 1：同意报名 2：拒绝报名*/
	private int excuteMeetSign;
	/**参会时间*/
	private String attendMeetTime;
	/**是否显示邀请函，默认为1*/
	private int isShowInvitation;
	
	/**标记 这条信息 为动态邀请添加的*/
	private boolean isAttendInvite = false;
	public boolean isAttendInvite() {
		return isAttendInvite;
	}
	public void setAttendInvite(boolean isAttendInvite) {
		this.isAttendInvite = isAttendInvite;
	}


	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		/**新接口需要传参*/
		jObject.put("id", id);
		jObject.put("memberId", memberId);
		jObject.put("memberType", memberType);
		jObject.put("memberName", memberName);
		jObject.put("memberPhoto", memberPhoto);
		jObject.put("memberMeetStatus", memberMeetStatus);
		jObject.put("attendMeetStatus", attendMeetStatus);
		jObject.put("attendMeetType", attendMeetType);
		jObject.put("meetingId", meetingId);
		jObject.put("excuteMeetSign", excuteMeetSign);
		jObject.put("isSign", isSign);
		jObject.put("signDistance", signDistance);
		jObject.put("attendMeetTime", attendMeetTime);
		jObject.put("isShowInvitation", isShowInvitation);
		
		return jObject;
	}

	
	public int getIsShowInvitation() {
		return isShowInvitation;
	}



	public void setIsShowInvitation(int isShowInvitation) {
		this.isShowInvitation = isShowInvitation;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAttendMeetStatus() {
		return attendMeetStatus;
	}

	public void setAttendMeetStatus(int attendMeetStatus) {
		this.attendMeetStatus = attendMeetStatus;
	}

	public int getAttendMeetType() {
		return attendMeetType;
	}

	public void setAttendMeetType(int attendMeetType) {
		this.attendMeetType = attendMeetType;
	}

	public long getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(long meetingId) {
		this.meetingId = meetingId;
	}

	public long getMemberId() {
		return memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	public int getExcuteMeetSign() {
		return excuteMeetSign;
	}

	public void setExcuteMeetSign(int excuteMeetSign) {
		this.excuteMeetSign = excuteMeetSign;
	}

	public int getMemberMeetStatus() {
		return memberMeetStatus;
	}

	public void setMemberMeetStatus(int memberMeetStatus) {
		this.memberMeetStatus = memberMeetStatus;
	}

	public int getIsSign() {
		return isSign;
	}

	public void setIsSign(int isSign) {
		this.isSign = isSign;
	}

	public String getSignDistance() {
		if (signDistance == null)
			return "";
		return signDistance;
	}

	public void setSignDistance(String signDistance) {
		this.signDistance = signDistance;
	}

	public String getMemberName() {
		if (memberName == null)
			return "";
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberPhoto() {
		if (memberPhoto == null)
			return "";
		return memberPhoto;
	}

	public void setMemberPhoto(String memberPhoto) {
		this.memberPhoto = memberPhoto;
	}

	public int getMemberType() {
		return memberType;
	}

	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}

	public String getAttendMeetTime() {
		if (attendMeetTime == null)
			return "";
		if (attendMeetTime.equals("null")) {
			return "";
		}
		return attendMeetTime;
	}

	public void setAttendMeetTime(String attendMeetTime) {
		this.attendMeetTime = attendMeetTime;
	}
}
