package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;
/***
 * 会议笔记列表
 */
public class MMeetingNoteQuery implements Serializable {

	/**
	 * sunjianan 20141117
	 */
	private static final long serialVersionUID = -8955529093582311531L;
	/**笔记ID*/
	private long id;
	/**会议ID*/
	private int meetingId;
	/**会议笔记标题*/
	private String meetingNoteTitle;
	/**创建人ID*/
	private String creater;
	/**笔记创建时间*/
	private String createTime;
	/**会议笔记详情*/
	private List<MMeetingNoteDetail> listMeetingNoteDetail;
	/**会议笔记详情列表*/
	private List<MMeetingNoteDetailQuery> listMeetingNoteDetailQuery;
	
	


	private String meetingName;

	public List<MMeetingNoteDetailQuery> getListMeetingNoteDetailQuery() {
		return listMeetingNoteDetailQuery;
	}

	public void setListMeetingNoteDetailQuery(List<MMeetingNoteDetailQuery> listMeetingNoteDetailQuery) {
		this.listMeetingNoteDetailQuery = listMeetingNoteDetailQuery;
	}

	public String getCreater() {
		if (creater == null)
			return "";
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreateTime() {
		if (createTime == null)
			return "";
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<MMeetingNoteDetail> getListMeetingNoteDetail() {
		return listMeetingNoteDetail;
	}

	public void setListMeetingNoteDetail(List<MMeetingNoteDetail> listMeetingNoteDetail) {
		this.listMeetingNoteDetail = listMeetingNoteDetail;
	}

	public void setMeetingId(int id) {
		this.meetingId = id;
	}

	public int getMeetingId() {
		return this.meetingId;
	}

	public String getMeetingName() {
		if (meetingName == null)
			return "";
		return meetingName;
	}

	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}

	public String getMeetingNoteTitle() {
		if (meetingNoteTitle == null)
			return "";
		return meetingNoteTitle;
	}

	public void setMeetingNoteTitle(String meetingNoteTitle) {
		this.meetingNoteTitle = meetingNoteTitle;
	}
}
