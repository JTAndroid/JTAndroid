package com.tr.ui.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RelatedContacts implements Serializable {

	/**
	 * 关联人脉
	 */
	public static final long serialVersionUID = 1L;

	public String tag;// "tag":"关联关系"

	public ArrayList<RelatedContents> conn;// 关联人脉/组织/知识/事件的具体内容的集合

	@Override
	public String toString() {
		return "RelatedContacts [tag=" + tag + ", conn=" + conn + "]";
	}

	
}
