package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;

public class MSocialityDetail implements Serializable {

	private static final long serialVersionUID = 1233281497033932818L;

	/** "socialDetail":{ "senderID": "发送者id", "senderName":"发送方名称", "content":
	 * "社交显示内容，社交类型为聊天时（包含私聊、群聊）封装最后一条聊天记录，会议显示为“会议开始时间”，通知显示通知内容，邀请函显示邀请函内容",
	 * "listImageUrl":[] */

	private long senderID;
	private String senderName;
	private String content;
	private List<String> listImageUrl;
	
	//at

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getListImageUrl() {
		return listImageUrl;
	}

	public void setListImageUrl(List<String> listImageUrl) {
		this.listImageUrl = listImageUrl;
	}

}
