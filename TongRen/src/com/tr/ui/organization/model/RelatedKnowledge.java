package com.tr.ui.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RelatedKnowledge implements Serializable {

	/**
	 * 关联知识
	 */
	public static final long serialVersionUID = 1L;

	public String tag;// "tag":"关联关系"

	public ArrayList<RelatedKnoContents> conn;// 关联人脉/组织/知识/事件的具体内容的集合

	@Override
	public String toString() {
		return "RelatedKnowledge [tag=" + tag + ", conn=" + conn + "]";
	}

	
}
