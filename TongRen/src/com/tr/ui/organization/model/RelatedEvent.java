package com.tr.ui.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RelatedEvent implements Serializable{

	/**
	 * 关联事件
	 */
	public static final long serialVersionUID = 1L;
	
//	"tag":"关联关系",
//  "conn":[{
//          "type":"类型 1事件",
//          "id":"事件id",
//          "title":"事件标题",
//          "ownerid":"创建人id",
//          "owneriame":"创建人名称",
//          "requirementtype":"事件类型"
//  }]
	
	public String tag;//	"tag":"关联关系"
	
	public ArrayList<RelatedKEveContents> conn;//关联人脉/组织/知识/事件的具体内容的集合

	@Override
	public String toString() {
		return "RelatedEvent [tag=" + tag + ", conn=" + conn + "]";
	}

}
