package com.tr.ui.tongren.model.review;

import java.io.Serializable;

public class ProcessType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String createId;
	private String createTime;
	private String id;
	private String name;
	private String pid;

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
