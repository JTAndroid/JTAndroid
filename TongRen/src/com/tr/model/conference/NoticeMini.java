package com.tr.model.conference;


/** 
* Copyright (c) 2014 北京金桐网投资有限公司. 
* All Rights Reserved. 保留所有权利. 
*/ 

/** 
 * @date 2015年1月16日 下午2:36:53 
 * @desc 
 */
public class NoticeMini {
	
	private long  receiver;  //接受人ID
	private String create_time; //创建时间
	private String receiver_name; //接受人名字
	private String notice_content;//通知内容
	private long   meeting_id;    //会议ID
	
	
	public long getReceiver() {
		return receiver;
	}
	public void setReceiver(long receiver) {
		this.receiver = receiver;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getReceiver_name() {
		return receiver_name;
	}
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	public String getNotice_content() {
		return notice_content;
	}
	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}
	public long getMeeting_id() {
		return meeting_id;
	}
	public void setMeeting_id(long meeting_id) {
		this.meeting_id = meeting_id;
	}
	
	

}
