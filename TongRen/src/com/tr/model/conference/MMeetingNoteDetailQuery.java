package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;

public class MMeetingNoteDetailQuery implements Serializable {
	private static final long serialVersionUID = -2182934843454772602L;

	/**笔记明细ID*/
	private long id;
	/**笔记ID*/
	private long meetingNoteId;
	/**笔记明细内容*/
	private String meetingNoteContent;
	/**明细创建时间*/
	private String meetingNoteTime;
	/**聊天记录ID*/
	private long meetingChatId;
	/**聊天记录*/
	private MTopicChat topicChat;
	/**任务ID*/
	private String taskId;
	/**带格式的笔记明细内容*/
	private String formatedContent;
	/**文件列表*/
	private List<JTFile2ForHY> listJTFile;


	public String getFormatedContent() {
		return formatedContent;
	}

	public void setFormatedContent(String formatedContent) {
		this.formatedContent = formatedContent;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMeetingNoteContent() {
		return meetingNoteContent;
	}

	public void setMeetingNoteContent(String meetingNoteContent) {
		this.meetingNoteContent = meetingNoteContent;
	}

	public long getMeetingNoteId() {
		return meetingNoteId;
	}

	public void setMeetingNoteId(long meetingNoteId) {
		this.meetingNoteId = meetingNoteId;
	}

	public String getMeetingNoteTime() {
		return meetingNoteTime;
	}

	public void setMeetingNoteTime(String meetingNoteTime) {
		this.meetingNoteTime = meetingNoteTime;
	}

	public long getMeetingChatId() {
		return meetingChatId;
	}

	public void setMeetingChatId(long meetingChatId) {
		this.meetingChatId = meetingChatId;
	}

	public MTopicChat getTopicChat() {
		return topicChat;
	}

	public void setTopicChat(MTopicChat topicChat) {
		this.topicChat = topicChat;
	}

	public List<JTFile2ForHY> getListJTFile() {
		return listJTFile;
	}

	public void setListJTFile(List<JTFile2ForHY> listJTFile) {
		this.listJTFile = listJTFile;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
