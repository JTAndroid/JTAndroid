package com.tr.ui.tongren.model.project;

import java.io.Serializable;

public class ApplyParameter implements Serializable{
	/**
	 * projectId 项目ID 必填
		publisherId 申请者ID 必填
		organizationId 申请时间 非必填字段
	 */
	public String projectId = "";
	public String publisherId = "";
	public String organizationId ="";
}
