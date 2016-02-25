package com.tr.ui.tongren.model.task;

import java.io.Serializable;

public class Assign implements Serializable {
	/**
	 * taskId 任务id orgId 组织id users 分配的用户id,多个用户以逗号分隔 organizationId 组织id
	 */
	public String taskId;
	public String orgId;
	public String users;
	public String organizationId;
}
