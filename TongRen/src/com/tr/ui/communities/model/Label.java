package com.tr.ui.communities.model;

import java.io.Serializable;

/**
 * 标签
 * 
 * @author cui
 * 
 */
public class Label implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2255787800403877412L;
	private long id;// 标签id,
	private String name;// 标签名称，
	private String createdUserId;// 创建者id
	private long createdTime;// 创建时间
	private long updatedTime;// 更新时间

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(String createdUserId) {
		this.createdUserId = createdUserId;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public long getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}

}
