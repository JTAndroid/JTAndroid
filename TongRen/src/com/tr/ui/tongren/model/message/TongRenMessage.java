package com.tr.ui.tongren.model.message;

import java.io.Serializable;

public class TongRenMessage implements Serializable{
	/**
	 *  messageReceiveID:消息接收ID
     messageType:消息类型（0普通消息、1项目消息、2邀请消息、3申请消息、4文档提交消息）
     messageTypeRes:消息类型结果
     sendTime:消息时间
     sendUserID:用户ID
     sendUserName:用户名称
     sendUserPic:用户头像
     title:消息标题
     content:消息内容
	 */
	public String messageReceiveID;
	public int messageType;
	public String messageTypeRes;
	public long sendTime;
	public String sendUserID;
	public String sendUserName;
	public String sendUserPic;
	public String title;
	public String content;
	
}
