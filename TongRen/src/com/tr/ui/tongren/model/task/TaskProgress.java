package com.tr.ui.tongren.model.task;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 任务进度表
 * @author Administrator
 *
 */
public class TaskProgress implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;//主键
	
	private long assignTaskId;//
	
	private String startTime;//开始时间
	
	private String costingTime;//耗时天
	
	private String completeTime;//完成时间
	
	private float completeProcess;//完成进度

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAssignTaskId() {
		return assignTaskId;
	}

	public void setAssignTaskId(long assignTaskId) {
		this.assignTaskId = assignTaskId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getCostingTime() {
		return costingTime;
	}

	public void setCostingTime(String costingTime) {
		this.costingTime = costingTime;
	}

	public String getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}

	public float getCompleteProcess() {
		return completeProcess;
	}

	public void setCompleteProcess(float completeProcess) {
		this.completeProcess = completeProcess;
	}
	
	
}
