package com.tr.model.conference;


/** 
* Copyright (c) 2014 北京金桐网投资有限公司. 
* All Rights Reserved. 保留所有权利. 
*/ 

import java.util.List;

import com.tr.model.obj.JTFile;

/** 
 * @date 2015年1月16日 下午3:08:18 
 * @desc 
 */
public class ChatMini {
	
	private String commtent;         //内容
	private String title;            //标题
	
	private List<String> list_image; //图片
	private long senderID;     //发送者ID
	private String senderName; //发送者名字
	private long meeting_id;
	private String last_chat_time; //最后通话时间
	
	private long  recvID;      //接收者ID  私聊时
	private int messageType;   //内容类型
	private long index;        //内部聊天序号
	private String messageID;  //消息ID串  由客户端生产
	private int  newCount;     //未读消息数
	private JTFile  jtFile;    //发送的 附件文件
	private int type;          //文件类型
	
	
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
	public long getSenderID() {
		return senderID;
	}
	public void setSenderID(long senderID) {
		this.senderID = senderID;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public long getRecvID() {
		return recvID;
	}
	public void setRecvID(long recvID) {
		this.recvID = recvID;
	}
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public String getMessageID() {
		return messageID;
	}
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	public int getNewCount() {
		return newCount;
	}
	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}
	public JTFile getJtFile() {
		return jtFile;
	}
	public void setJtFile(JTFile jtFile) {
		this.jtFile = jtFile;
	}
	public List<String> getList_image() {
		return list_image;
	}
	public void setList_image(List<String> list_image) {
		this.list_image = list_image;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
	
	
	

}
