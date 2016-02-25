package com.tr.ui.tongren.model.project;

import java.io.Serializable;

public class Resource implements Serializable{
	/**
	 *   id:资源ID
     createId:创建者ID
     organizationId:组织者ID
     projectId:项目ID
     taskId:图片标识ID
     createTime:创建时间
     titleName:文件名称
     createName:创建人姓名
     path:路径
	 */
	public String id;
	public String createId;
	public String organizationId;
	public String projectId;
	public String taskId;
	public long createTime;
	public String titleName;
	public String createName;
	public String path;
}
