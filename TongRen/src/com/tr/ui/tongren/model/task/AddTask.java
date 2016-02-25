package com.tr.ui.tongren.model.task;

import java.io.Serializable;

public class AddTask implements Serializable {
	/**
	 *  title 任务名称
	organizationId 组织id
	taskPid 父任务id
	description 描述(非必填)
	users 逗号分隔的多个用户id
	startDate 任务开始日期
	endDate 任务结束日期
	 */
	public String title = "";
	public String organizationId="";
	public String taskPid ;
	public String taskId ;
	public String description ="";
	public String users ="";
	public String startDate ="";
	public String endDate ="";
}
