package com.tr.ui.tongren.model.task;

import java.io.Serializable;
import java.util.List;

public class OrganizationTask implements Serializable{
	/**
attachUrl 任务附件的url
id 任务id
projectUndertakenId 承接项目id
title 任务名称
startTime 任务开始时间
endTime 任务结束时间
createTime 任务创建时间
taskPid 父任务id
createId 创建人id
taskStatus 任务状态 (0 准备中 1 已开始 2 已完成 3 已过期)
organizationId 组织id
taskDescription 任务说明
taskType 任务类型 (0 项目任务 1 组织任务)
children 该任务的子任务
rejectReason 退回原因
	 */
	public String attachUrl;
	public String id;
	public String projectUndertakenId;
	public String title;
	public String startTime;
	public String endTime;
	public String createTime;
	public String taskPid;
	public String createId;
	public String taskStatus;
	public String organizationId;
	public String taskDescription;
	public String taskType;
	public List<OrganizationTask> children;
 	public String rejectReason;
	public String getAttachUrl() {
		return attachUrl;
	}
	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProjectUndertakenId() {
		return projectUndertakenId;
	}
	public void setProjectUndertakenId(String projectUndertakenId) {
		this.projectUndertakenId = projectUndertakenId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getTaskPid() {
		return taskPid;
	}
	public void setTaskPid(String taskPid) {
		this.taskPid = taskPid;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public List<OrganizationTask> getChildren() {
		return children;
	}
	public void setChildren(List<OrganizationTask> children) {
		this.children = children;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
 	
}
