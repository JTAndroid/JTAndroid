package com.tr.ui.tongren.model.project;

import java.io.Serializable;

public class Operation implements Serializable{
	/**
	 *   "id":3911278141898758,
                        "operactionTime":1448004840000,
                        "operationCode":"02",
                        "operationUid":200827,
                        "organizationTaskId":0,
                        "projectId":3908370281267205,
                        "remark":"hanxifa 创建了 项目模块开发 子任务"
	 */
	public String id;
	public long operactionTime;
	public String operationCode;
	public String operationUid;
	public String organizationTaskId;
	public String projectId;
	public String remark;
	public String name;
}
