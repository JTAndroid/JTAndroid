package com.tr.ui.tongren.model.task;

import java.io.Serializable;

public class TaskOrganizationParameter implements Serializable{
	/**
	 * time 时间戳，第一次查询去当前时间，以后(点击更多按钮)取最后一条记录的时间
		type 任务类型( 0 全部 1 我发起的任务 2 组织任务 3项目任务 4 被退回的任务 5已完成任务)
		orgId 组织id
	 */
	public long time;
	public String type;
	public String orgId;
}
