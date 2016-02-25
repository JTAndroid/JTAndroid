package com.tr.ui.organization.model;

import java.io.Serializable;

public class NoticeBean implements Serializable{

	/**
	 * 最新公告
	 */
	private static final long serialVersionUID = -4400108273021157515L;
	/**
	 * id
	 */
	public long id;
	/**
	 * 知识id
	 */
	public long kownledgeId;
	/**
	 * 知识类型
	 */
	public int type;

	/**
	 * 标题
	 */
	public String title;
	/**
	 * 证券代码号
	 */
	public String stkcd;
	
	@Override
	public String toString() {
		return "NewsBean [id=" + id + ", kownledgeId=" + kownledgeId
				+ ", type=" + type + ", title=" + title + ", stkcd=" + stkcd
				+ "]";
	}
	
	
}
