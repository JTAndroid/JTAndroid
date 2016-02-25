package com.tr.ui.organization.model;

import java.io.Serializable;

public class RelatedKEveContents implements Serializable{

	/**
	 * 关联事件
	 * 
	 * "type":"类型 1事件",
                                        "id":"事件id",
                                        "title":"事件标题",
                                        "ownerid":"创建人id",
                                        "owneriame":"创建人名称",
                                        "requirementtype":"事件类型"

	 */
	private static final long serialVersionUID = 1L;
	
	public String id;
	
	public String title;
	
	public String ownerid;
	
	public String owneriame;
	
	public String requirementtype;

	@Override
	public String toString() {
		return "RelatedKEveContents [id=" + id + ", title=" + title
				+ ", ownerid=" + ownerid + ", owneriame=" + owneriame
				+ ", requirementtype=" + requirementtype + "]";
	}
	
}
