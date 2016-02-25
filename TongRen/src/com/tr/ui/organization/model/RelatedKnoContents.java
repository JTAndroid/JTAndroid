package com.tr.ui.organization.model;

import java.io.Serializable;

public class RelatedKnoContents implements Serializable{

	/**
	 * 关联知识
	 * 
	 * "type":"类型 6知识",
                                        "id":"知识id S",
                                        "title":"知识标题",
                                        "ownerId":"创建人id S",
                                        "ownerName":"创建人姓名",
                                        "columntype":"知识类型",
                                        "columnpath":"知识目录路径"

	 */
	private static final long serialVersionUID = 1L;
	
	public String id;
	
	public String title;
	
	public String ownerId;
	
	public String ownerName;
	
	public String columntype;
	
	public String columnpath;

	@Override
	public String toString() {
		return "RelatedKnoContents [id=" + id + ", title=" + title
				+ ", ownerId=" + ownerId + ", ownerName=" + ownerName
				+ ", columntype=" + columntype + ", columnpath=" + columnpath
				+ "]";
	}
	
	
}
