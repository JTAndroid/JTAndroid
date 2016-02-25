package com.tr.ui.tongren.model.task;

import java.io.Serializable;

public class AddOrganizationTask implements Serializable{
	/**
	 * title:”任务名称”(必填项)
startTime : 开始时间(必填项)
endTime : 结束时间(必填项)
attachId : 上传文档taskId,
performerId : 执行人id(必填项)
organizationId:组织id(必填项)
taskDescription : 任务说明,
	 */
	public String title;
	public String startTime;
	public String endTime;
	public String attachId;
	public String performerId="";
	public String organizationId;
	public String taskDescription;
}
