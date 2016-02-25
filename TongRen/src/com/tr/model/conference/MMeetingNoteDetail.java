package com.tr.model.conference;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

/***
 * 会议笔记详情
 *
 */
public class MMeetingNoteDetail implements Serializable{

	/**
	 * sunjianan 20141117
	 */
	private static final long serialVersionUID = 3532604769243896017L;
	
	/**笔记明细ID*/
	private long id;
	/**笔记ID*/
	private long meetingNoteId;
	/**引用聊天记录ID*/
	private String meetingChatId;
	/**笔记明细内容*/
	private String meetingNoteContent;
	/**带格式的笔记明细内容*/
	private String formatedContent;
	/**明细创建时间*/
	private String meetingNoteTime;
	/**任务ID*/
	private String taskId;
	/**创建时间 字符串格式*/
	private String meetingNoteTimeString;
	
	
	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("id", id);
		jObject.put("meetingNoteId", meetingNoteId);
		jObject.put("meetingChatId", meetingChatId);
		jObject.put("meetingNoteContent", meetingNoteContent);
		jObject.put("formatedContent", formatedContent);
		jObject.put("meetingNoteTime", meetingNoteTime);
		jObject.put("taskId", taskId);
		jObject.put("meetingNoteTimeString", meetingNoteTimeString);
		return jObject;
	}


	public String getFormatedContent() {
		return formatedContent;
	}


	public void setFormatedContent(String formatedContent) {
		this.formatedContent = formatedContent;
	}


	public String getMeetingNoteTimeString() {
		return meetingNoteTimeString;
	}


	public void setMeetingNoteTimeString(String meetingNoteTimeString) {
		this.meetingNoteTimeString = meetingNoteTimeString;
	}


	public void setId(long id) {
		this.id = id;
	}


	public void setMeetingNoteId(long meetingNoteId) {
		this.meetingNoteId = meetingNoteId;
	}


	public long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getMeetingNoteContent() {
		if(meetingNoteContent == null) 
			return "";
		return meetingNoteContent;
	}


	public void setMeetingNoteContent(String meetingNoteContent) {
		this.meetingNoteContent = meetingNoteContent;
	}


	public long getMeetingNoteId() {
		return meetingNoteId;
	}


	public void setMeetingNoteId(Long meetingNoteId) {
		this.meetingNoteId = meetingNoteId;
	}


	public String getMeetingNoteTime() {
		if(meetingNoteTime == null) 
			return "";
		return meetingNoteTime;
	}


	public void setMeetingNoteTime(String meetingNoteTime) {
		this.meetingNoteTime = meetingNoteTime;
	}


	public String getTaskId() {
		if(taskId == null) 
			return "";
		return taskId;
	}


	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getMeetingChatId() {
		if(meetingChatId == null) 
			return "";
		return meetingChatId;
	}


	public void setMeetingChatId(String meetingChatId) {
		this.meetingChatId = meetingChatId;
	}	
	
}
