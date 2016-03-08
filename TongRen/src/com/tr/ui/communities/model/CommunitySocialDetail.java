package com.tr.ui.communities.model;

import java.io.Serializable;
import java.util.List;

public class CommunitySocialDetail implements Serializable {

	/**
	 * 
	 "senderID":"消息发送者ID", "senderName":"消息发送者名称", "listImageUrl":["图片路径"],
	 * "content":"消息内容"
	 */
	private static final long serialVersionUID = 1L;

	private String senderID;
	private String senderName;
	private List<String> listImageUrl;
	private String content;

	public String getSenderID() {
		return senderID;
	}

	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public List<String> getListImageUrl() {
		return listImageUrl;
	}

	public void setListImageUrl(List<String> listImageUrl) {
		this.listImageUrl = listImageUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
