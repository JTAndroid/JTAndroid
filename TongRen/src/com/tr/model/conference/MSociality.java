package com.tr.model.conference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MSociality implements Serializable {

	private static final long serialVersionUID = -8721757425793846755L;

	/** "social":{ "id":"私聊为对方的用户id，群聊为muc的id,会议为会议的id", "title":
	 * "社交显示标题 如果群聊，为群聊或者会议的主题；私聊，为好友名称，通知显示为“通知”",
	 * "type":"1-私聊，2-群聊，3-进行中的会议，4-未开始，5-已结束的会议，6-通知，7-邀请函" 0-没有会议,
	 * "compereName":"群聊：主持人姓名，会议 ：会议发起人", "orderTime":"排序时间", "time":"最后更新时间",
	 * "socialDetail":{} } */

	private long id;
	private String title;
	private int type;
	private int meetingType; // "会议类型 0：无主讲  1：有主讲"
	private String compereName;
	private String orderTime;
	private String time;
	/** 角标通知数量 */
	private int newCount;
	
	private MSocialityDetail socialDetail;
	private List<MMeetingTopic> listMeetingTopic;
	
	private int userType;  //0 个人好友  1组织好友

	private boolean isDelete; // 判断是否删除

	private String atName;
	private String atMsgId;
	private boolean atVisible;
	
	public void setMSociality(MSociality mMSociality){
		this.title = mMSociality.getTitle();
		this.meetingType = mMSociality.getMeetingType();
		this.compereName = mMSociality.getCompereName();
		this.orderTime = mMSociality.getOrderTime();
		this.time = mMSociality.getTime();
		this.listMeetingTopic = mMSociality.getListMeetingTopic();
		this.userType = mMSociality.getUserType();
		this.isDelete = mMSociality.isDelete;
		this.atName = mMSociality.atName;
		this.atMsgId = mMSociality.atMsgId;
	}
	
	/*私聊*/
	public static final int PRIVATE_CHAT = 1;
	/*群聊*/
	public static final int GROUP_CHAT = 2;
	/*进行中的会议*/
	public static final int MEETING = 3;
	/*未开始*/
	public static final int NOT_BEGINNING_MEETING = 4;
	/*已结束的会议*/
	public static final int ENDED_MEETING = 5;
	/*通知*/
	public static final int NOTICE= 6;
	/*邀请函*/
	public static final int INVITATION = 7;
	/*新关系*/
	public static final int NEW_RELATIONSHIP = 8;
	/*没有会议*/
	public static final int NO_MEETING = 0;
	/*社群*/
	public static final int COMMUNITY = 9;
	
	/**
	 * 是否是会议文件夹
	 * @param type
	 * @return
	 */
	public static boolean isMeeting(int type){
		switch (type) {
		case MEETING:
		case NOT_BEGINNING_MEETING:
		case ENDED_MEETING:
		case NO_MEETING:
			return true;
		default:
			break;
		}
		return false;
	}
	
	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public boolean isAtVisible() {
		return atVisible;
	}

	public void setAtVisible(boolean atVisible) {
		this.atVisible = atVisible;
	}

	public String getAtName() {
		return atName;
	}

	public void setAtName(String atName) {
		this.atName = atName;
	}

	public String getAtMsgId() {
		return atMsgId;
	}

	public void setAtMsgId(String atMsgId) {
		this.atMsgId = atMsgId;
	}

	public int getNewCount() {
		return newCount;
	}

	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
//	"1-私聊，2-群聊，3-进行中的会议，4-未开始，5-已结束的会议，6-通知，7-邀请函" 0-没有会议,
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(int meetingType) {
		this.meetingType = meetingType;
	}

	public String getCompereName() {
		return compereName;
	}

	public void setCompereName(String compereName) {
		this.compereName = compereName;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public MSocialityDetail getSocialDetail() {
		return socialDetail;
	}

	public void setSocialDetail(MSocialityDetail socialDetail) {
		this.socialDetail = socialDetail;
	}

	public List<MMeetingTopic> getListMeetingTopic() {
		if (listMeetingTopic == null) {
			listMeetingTopic = new ArrayList<MMeetingTopic>();
		}
		return listMeetingTopic;
	}

	public void setListMeetingTopic(List<MMeetingTopic> listMeetingTopic) {
		if (listMeetingTopic != null) {
			this.listMeetingTopic = listMeetingTopic;
		}
	}
}
