package com.tr.ui.tongren.model.task;

import java.io.Serializable;

public class EditOrganizationTask implements Serializable{
	/**
	 * organizationTaskId:”组织任务id”,(必填项)

performerId:”重新分配人id”,(必填项)

organizationId:”组织id”,(必填项)

startTime 任务开始时间
endTime 任务结束时间
	 */
	public String organizationTaskId;
	public String performerId;
	public String organizationId;
	public String startTime;
	public String endTime;
}
