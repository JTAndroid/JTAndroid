package com.tr.ui.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RelatedOrganization implements Serializable {

	/**
	 * 关联组织
	 */
	private static final long serialVersionUID = 1L;
	
	public  String tag;// "tag":"关联关系"

	public ArrayList<RelatedOrgContents> conn;// 关联人脉/组织/知识/事件的具体内容的集合

	@Override
	public String toString() {
		return "RelatedOrganization [tag=" + tag + ", conn=" + conn + "]";
	}

	

}
