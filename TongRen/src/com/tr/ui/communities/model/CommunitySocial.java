package com.tr.ui.communities.model;

import java.io.Serializable;
import java.util.List;

public class CommunitySocial implements Serializable {

	/**
	 * 
	 "id":“社群ID”, "title":"社群名称", "type":"类型，社群的标识为4", "meetingType":null,
	 * "compereId":null, "compereName":"谢玉龙", "time":"2016-01-23 10:32:30",
	 * "orderTime":"2016-01-23 10:32:30", "listMeetingTopic":null,
	 * "socialDetail":{ "senderID":"消息发送者ID", "senderName":"消息发送者名称",
	 * "listImageUrl":["图片路径"], "content":"消息内容" }, "path":null, "newCount":0,
	 * "atName":null, "atMsgId":null, "userType":null,
	 * "newMessageRemind":"群消息提醒:0开启 1无声 2 关闭", "nickname":"社群里的昵称",
	 * "showNickname":"是否显示群成员昵称：0是 1否"
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String title;
	private String type;
	private String meetingType;
	private String compereId;
	private String compereName;
	private String time;
	private String orderTime;
	private String listMeetingTopic;
	private CommunitySocialDetail socialDetail;
	private String path;
	private int newCount;
	private String atName;
	private String atMsgId;
	private String userType;
	private String newMessageRemind;
	private String nickname;
	private String showNickname;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(String meetingType) {
		this.meetingType = meetingType;
	}

	public String getCompereId() {
		return compereId;
	}

	public void setCompereId(String compereId) {
		this.compereId = compereId;
	}

	public String getCompereName() {
		return compereName;
	}

	public void setCompereName(String compereName) {
		this.compereName = compereName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getListMeetingTopic() {
		return listMeetingTopic;
	}

	public void setListMeetingTopic(String listMeetingTopic) {
		this.listMeetingTopic = listMeetingTopic;
	}

	public CommunitySocialDetail getSocialDetail() {
		return socialDetail;
	}

	public void setSocialDetail(CommunitySocialDetail socialDetail) {
		this.socialDetail = socialDetail;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getNewCount() {
		return newCount;
	}

	public void setNewCount(int newCount) {
		this.newCount = newCount;
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getNewMessageRemind() {
		return newMessageRemind;
	}

	public void setNewMessageRemind(String newMessageRemind) {
		this.newMessageRemind = newMessageRemind;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getShowNickname() {
		return showNickname;
	}

	public void setShowNickname(String showNickname) {
		this.showNickname = showNickname;
	}

}
