package com.tr.model.conference;


/** 
* Copyright (c) 2014 北京金桐网投资有限公司. 
* All Rights Reserved. 保留所有权利. 
*/ 

/** 
 * @date 2015年1月16日 下午2:11:38 
 * @desc 
 */
public class MeetingMini {
	
	private String image; //封面图
	private String commtent;         //内容
	private String title;            //标题
	private int status;              //会议的状态 1-未开始  2-正在进行  3-已结束
	private String create_time;      //创建时间
	private String start_time;       //会议开始时间
	private String end_time;         //会议结束时间
	private long meeting_id;         //会议ID
	private String last_chat_time;   //最后通话时间
	
	
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCommtent() {
		return commtent;
	}
	public void setCommtent(String commtent) {
		this.commtent = commtent;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public long getMeeting_id() {
		return meeting_id;
	}
	public void setMeeting_id(long meeting_id) {
		this.meeting_id = meeting_id;
	}
	public String getLast_chat_time() {
		return last_chat_time;
	}
	public void setLast_chat_time(String last_chat_time) {
		this.last_chat_time = last_chat_time;
	}
	
	

}
