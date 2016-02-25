package com.tr.model.conference;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
/***
 * 会议议题聊天对象
 *
 */
public class MTopicChat implements Serializable{

	private static final long serialVersionUID = -4318062140765358649L;
	
	public String getMemberPic() {
		return memberPic;
	}

	public void setMemberPic(String memberPic) {
		this.memberPic = memberPic;
	}

	/**内容序号*/
	private Long id;
	/**会议序号*/
	private Long meetingId;
	/**0-系统用户发送，此时userID无效；1-普通用户发送，具体用户此时参考meeting_id*/
	private Integer senderType;
	/**议题序号*/
	private Long topicId;
	/**聊天内容*/
	private String chatContent;
	/**	聊天类型 内容 type,0-text,1-audio,2-image,3-video,4-file,5-JTContact(人脉),6-knowledge(知识),7-requirement(需求),8-机构客户,9-机构用户(线上金桐网用户),10-个人用户(线上个人用户),11-knowledge2新知识,12-会议(conference)13名片 14 邀请函	*/
	private Integer chatType;
	/**消息id串，客户端随机生成，每条记录唯一*/
	private String messageId;
	/**人员ID*/
	private Long memberId;
	/**人员名字*/
	private String memberName;
	/**人员图片*/
	private String memberPic;
	/**文件地址*/
	private String jtfileUrl;
	/**后缀名 jpg,png,amr,pdf等*/
	private String jtfileSuffixName;
	/**文件类型*/
	private String jtfileType;
	/**文件名*/
	private String jtfileName;
	/**文件大小*/
	private Long jtfileSize;
	/**0：需求，1：业务需求，2：公司客户，3：公司项目，4：会员，5：名片，6：公司名片，7：资讯，8：客户，9：人脉分享，10：机构*/
	private Integer jtFileModuleType;
	/**附件id*/
	private String jtfileTaskId;
	/**发布时间*/
	private String publishTime;
	
	
	
	private String jtFileReserved1;
	private String jtFileReserved2;
	private String jtFileReserved3;	
	
	
	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("chatContent", chatContent);
		jObject.put("chatType", chatType);
		jObject.put("id", id);
		jObject.put("jtFileModuleType", jtFileModuleType);
		jObject.put("jtfileName", jtfileName);
		jObject.put("jtfileSize", jtfileSize);
		jObject.put("jtfileSuffixName", jtfileSuffixName);
		jObject.put("jtfileTaskId", jtfileTaskId);
		jObject.put("jtfileType", jtfileType);
		jObject.put("jtfileUrl", jtfileUrl);
		jObject.put("meetingId", meetingId);
		jObject.put("memberId", memberId);
		jObject.put("messageId", messageId);
		jObject.put("publishTime", publishTime);
		jObject.put("senderType", senderType);
		jObject.put("topicId", topicId);
		jObject.put("memberName", memberName);
		jObject.put("memberPic", memberPic);
		jObject.put("jtFileReserved1", jtFileReserved1);
		jObject.put("jtFileReserved2", jtFileReserved2);
		jObject.put("jtFileReserved3", jtFileReserved3);
		return jObject;
	}
	
	/**
	 * @param jsonObject
	 * @return MeetingData
	 *//*
	public static List<TopicChat> createFactory(JSONObject jsonObject) {
		MeetingDetail detail = null;
		try {
			if (jsonObject != null) {
				Gson gson = new Gson();
				detail = gson.fromJson(jsonObject.toString(),MeetingDetail.class);
			}

			return detail.getMeeting().getListMeetingTopicQuery().

		} catch (Exception e) {
		}
		return null;
	}*/
	
	public String getChatContent() {
		if(chatContent == null) 
			return "";
		return chatContent;
	}

	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}

	public Integer getChatType() {
		return chatType;
	}

	public void setChatType(Integer chatType) {
		this.chatType = chatType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getJtFileModuleType() {
		return jtFileModuleType;
	}

	public void setJtFileModuleType(Integer jtFileModuleType) {
		this.jtFileModuleType = jtFileModuleType;
	}

	public String getJtFileReserved1() {
		if(jtFileReserved1 == null) 
			return "";
		return jtFileReserved1;
	}

	public void setJtFileReserved1(String jtFileReserved1) {
		this.jtFileReserved1 = jtFileReserved1;
	}

	public String getJtFileReserved2() {
		if(jtFileReserved2 == null) 
			return "";
		return jtFileReserved2;
	}

	public void setJtFileReserved2(String jtFileReserved2) {
		this.jtFileReserved2 = jtFileReserved2;
	}

	public String getJtFileReserved3() {
		if(jtFileReserved3 == null) 
			return "";
		return jtFileReserved3;
	}

	public void setJtFileReserved3(String jtFileReserved3) {
		this.jtFileReserved3 = jtFileReserved3;
	}

	public String getJtfileName() {
		if(jtfileName == null) 
			return "";
		return jtfileName;
	}

	public void setJtfileName(String jtfileName) {
		this.jtfileName = jtfileName;
	}

	public Long getJtfileSize() {
		return jtfileSize;
	}

	public void setJtfileSize(Long jtfileSize) {
		this.jtfileSize = jtfileSize;
	}

	public String getJtfileSuffixName() {
		if(jtfileSuffixName == null) 
			return "";
		return jtfileSuffixName;
	}

	public void setJtfileSuffixName(String jtfileSuffixName) {
		this.jtfileSuffixName = jtfileSuffixName;
	}

	public String getJtfileTaskId() {
		return jtfileTaskId;
	}

	public void setJtfileTaskId(String jtfileTaskId) {
		this.jtfileTaskId = jtfileTaskId;
	}

	public String getJtfileType() {
		if(jtfileType == null) 
			return "";
		return jtfileType;
	}

	public void setJtfileType(String jtfileType) {
		this.jtfileType = jtfileType;
	}

	public String getJtfileUrl() {
		if(jtfileUrl == null) 
			return "";
		return jtfileUrl;
	}

	public void setJtfileUrl(String jtfileUrl) {
		this.jtfileUrl = jtfileUrl;
	}

	public Long getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getPublishTime() {
		if(publishTime == null) 
			return "";
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public Integer getSenderType() {
		return senderType;
	}

	public void setSenderType(Integer senderType) {
		this.senderType = senderType;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
}
