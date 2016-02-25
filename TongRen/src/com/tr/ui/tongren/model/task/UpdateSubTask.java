package com.tr.ui.tongren.model.task;

import java.io.Serializable;

public class UpdateSubTask implements Serializable {
	/**
	 * taskId 任务id startDate 开始时间(非必填) endDate 结束时间(非必填) title 任务名称(非必填)
	 * organizationId 组织id
	 */
	public String taskId="";
	public String startDate="";
	public String endDate="";
	public String title="";
	public String organizationId="";
}
