package com.tr.model.conference;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class MMeetingTopic implements Serializable {
	private static final long serialVersionUID = -9037743884784769247L;

	/**
	 * "id":"议题ID", "meetingId":"会议ID", "memberId":"主讲人ID",
	 * "memberName":"主讲人名字", "memberDesc":"主讲人描述", "taskId":"附件id",
	 * "topicContent":"议题内容", "topicDesc":"议题介绍", "topicEndTime":"议题结束时间",
	 * "topicStartTime":"议题开始时间"
	 * -------------------------------
	 * "memberPic":"主讲人头像",
	 * "createId":"创建人Id",
	 * "createName":"创建人名字",
	 * "createTime":"创建时间",
	 * "updateTime":"更新时间",
	 */
	private long id;
	private long meetingId;
	private long memberId;
	private String memberName;
	private String memberDesc;
	private String taskId;
	
	private String topicContent;
	private String topicDesc;
	
	private String topicStartTime;
	private String topicEndTime;
	
	private String memberPic;
	private long createId;
	private String createName;
	private String createTime;
	private String updateTime;
	
	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("id", id);
		jObject.put("meetingId", meetingId);
		jObject.put("memberId", memberId);
		jObject.put("memberName", memberName);
		jObject.put("memberDesc", memberDesc);
		jObject.put("taskId", taskId);
		jObject.put("topicContent", topicContent);
		jObject.put("topicDesc", topicDesc);
		jObject.put("topicEndTime", topicEndTime);
		jObject.put("topicStartTime", topicStartTime);
		jObject.put("memberPic", memberPic);
		jObject.put("createId", createId);
		jObject.put("createName", createName);
		jObject.put("createTime", createTime);
		jObject.put("updateTime", updateTime);
		return jObject;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getMemberName() {
		if(memberName == null) 
			return "";
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberDesc() {
		if(memberDesc == null) 
			return "";
		return memberDesc;
	}

	public void setMemberDesc(String memberDesc) {
		this.memberDesc = memberDesc;
	}

	public String getTaskId() {
		if(taskId == null) 
			return "";
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTopicContent() {
		if(topicContent == null) 
			return "";
		return topicContent;
	}

	public void setTopicContent(String topicContent) {
		this.topicContent = topicContent;
	}

	public String getTopicDesc() {
		if(topicDesc == null) 
			return "";
		return topicDesc;
	}

	public void setTopicDesc(String topicDesc) {
		this.topicDesc = topicDesc;
	}

	public String getTopicEndTime() {
		if(topicEndTime == null) 
			return "";
		return topicEndTime;
	}

	public void setTopicEndTime(String topicEndTime) {
		this.topicEndTime = topicEndTime;
	}

	public String getTopicStartTime() {
		if(topicStartTime == null) 
			return "";
		return topicStartTime;
	}

	public void setTopicStartTime(String topicStartTime) {
		this.topicStartTime = topicStartTime;
	}

	public String getMemberPic() {
		if(memberPic == null) 
			return "";
		return memberPic;
	}

	public void setMemberPic(String memberPic) {
		this.memberPic = memberPic;
	}

	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}

	public String getCreateName() {
		if(createName == null) 
			return "";
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getCreateTime() {
		if(createTime == null) 
			return "";
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		if(updateTime == null) 
			return "";
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
