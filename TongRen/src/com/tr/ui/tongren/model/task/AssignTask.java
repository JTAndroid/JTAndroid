package com.tr.ui.tongren.model.task;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 项目任务分配表
 * @author Administrator
 *
 */

public class AssignTask implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private long id;//主键
	
	private long projectTaskId;//项目任务id
	
	private long assignerId;//分配人id
	
	private String assignTime;//分配时间
	
	private long performerId;//执行者id
	
	private long organizationId;//此任务所属组织的id
	
	private int status;//0 有效 1 无效
	
	private String remark;//备注
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getProjectTaskId() {
		return projectTaskId;
	}

	public void setProjectTaskId(long projectTaskId) {
		this.projectTaskId = projectTaskId;
	}
	
	public long getAssignerId() {
		return assignerId;
	}

	public void setAssignerId(long assignerId) {
		this.assignerId = assignerId;
	}

	public String getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(String assignTime) {
		this.assignTime = assignTime;
	}

	public long getPerformerId() {
		return performerId;
	}

	public void setPerformerId(long performerId) {
		this.performerId = performerId;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "AssignTask [id=" + id + ", projectTaskId=" + projectTaskId + ", assignerId=" + assignerId + ", assignTime=" + assignTime
				+ ", performerId=" + performerId + ", organizationId=" + organizationId + "]";
	}

}
