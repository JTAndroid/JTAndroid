package com.tr.ui.tongren.model.task;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.utils.time.TimeUtil;


/**
 * 项目任务表
 * @author Administrator
 *
 */

public class Task  implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private long id;//主键
	
	private long projectUndertakenId;//承接项目id
	
	private String title;//名称
	
	private int cycle;//任务周期,单位天
	
	public long startTime;//起始时间
	
	public long endTime;//结束时间
	
	public long createTime;//创建时间
	
	public long taskPid;//父任务id
		
	private long createId;//创建人id
	
	private int taskStatus;//0 准备中 1 已开始 2 已完成 3 已过期 6已驳回 9(无效)已删除
	
	private float progress;//0-100 范围内的浮点数，不能超过这个范围
	
	private long organizationId;//此任务对应项目的所属组织id
	
	private String taskDescription;//
	
	private int taskType;//0 项目任务 1 组织任务
	public String rejectReason ; // 退回原因
	private String attachId;//附件id
	public AssignUser extend;
	private List<Task> children=new ArrayList<Task>();
	public long getId() {
		return id;
	}

	public List<Task> getChildren() {
		return children;
	}

	public void setChildren(List<Task> children) {
		this.children = children;
	}

	public void setId(long id) {
		this.id = id;
	}
	public long getProjectUndertakenId() {
		return projectUndertakenId;
	}

	public void setProjectUndertakenId(long projectUndertakenId) {
		this.projectUndertakenId = projectUndertakenId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	
	public String getStartTime() {
		if (startTime!=0) {
		String startTimeStr = TimeUtil.TimeMillsToString(startTime);
		return startTimeStr;
		}
		return "";
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		if (endTime!=0) {
			String endTimeStr = TimeUtil.TimeMillsToString(endTime);
			return endTimeStr;
		}
		return "";
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getTaskPid() {
		return taskPid;
	}

	public void setTaskPid(long taskPid) {
		this.taskPid = taskPid;
	}

	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}

	public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public String getCreateTime() {
		String createTimeStr = TimeUtil.TimeMillsToString(createTime);
		return createTimeStr;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", projectUndertakenId=" + projectUndertakenId + ", title=" + title + ", cycle=" + cycle + ", startTime="
				+ startTime + ", endTime=" + endTime + ", createTime=" + createTime + ", taskPid=" + taskPid + ", createId=" + createId
				+ ", taskStatus=" + taskStatus + ", progress=" + progress + ", organizationId=" + organizationId + ", taskDescription="
				+ taskDescription + ", taskType=" + taskType + ", attachId=" + attachId + "]";
	}

	 
}
