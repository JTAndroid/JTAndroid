package com.tr.ui.communities.model;

import java.io.Serializable;

/**
 * 我的社群实体
 * 
 */
public class MyCommunitListData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3716416439468777744L;
	/**
	 * "pic_path":"社群头像",
	 */
	private String pic_path;
	/**
	 * ”user_Id“：”用户id“,
	 */
	private long user_Id;
	/**
	 * ”type“:"0-内部机构;1-普通群聊;2-会议; 4社群"
	 */
	private int type;
	/**
	 * "owner_id":"所有者id",
	 */
	private long owner_id;
	/**
	 * "id":"社群id",
	 */
	private long id;
	/**
	 * "title":"标题"，
	 */
	private String title;
	/**
	 * “time”：“时间”,
	 */
	private long time;
	/**
	 * "subject":"标题 介绍  ",
	 */
	private String subject;
	/**
	 * "isqz":"是否群主1：群主2不是群主"，
	 */
	private int isqz;
	/**
	 * "isys":"是否隐身1:不隐身2隐身"，
	 */
	private int isys;
	public String getPic_path() {
		return pic_path;
	}
	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}
	public long getUser_Id() {
		return user_Id;
	}
	public void setUser_Id(long user_Id) {
		this.user_Id = user_Id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(long owner_id) {
		this.owner_id = owner_id;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public int getIsqz() {
		return isqz;
	}
	public void setIsqz(int isqz) {
		this.isqz = isqz;
	}
	public int getIsys() {
		return isys;
	}
	public void setIsys(int isys) {
		this.isys = isys;
	}
	
	
}
