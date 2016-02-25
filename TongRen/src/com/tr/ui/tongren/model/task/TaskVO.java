package com.tr.ui.tongren.model.task;

import java.io.Serializable;

/**
 * 我的任务裂表显示内容
 * @author hanxifa
 *
 */
public class TaskVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Task task;
	
	private int rtype;//0 我创建的任务,1 别人分配给我的任务
	
	private String createUserName;//任务创建人名称
	
	private String performUserName;//任务执行人名称，多个人已逗号分隔
	
	private String orgName;//组织名称
	
	private String rejectStr;//退回原因
	
	private String attachUrl;

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public int getRtype() {
		return rtype;
	}

	public void setRtype(int rtype) {
		this.rtype = rtype;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getPerformUserName() {
		return performUserName;
	}

	public void setPerformUserName(String performUserName) {
		this.performUserName = performUserName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getRejectStr() {
		return rejectStr;
	}

	public void setRejectStr(String rejectStr) {
		this.rejectStr = rejectStr;
	}

	public String getAttachUrl() {
		return attachUrl;
	}

	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}
	
	
}
