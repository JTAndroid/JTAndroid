package com.tr.ui.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class RelatedInformation implements Serializable{

	/**
	 * 关联信息
	 */
	public static final long serialVersionUID = 1L;
	
	public ArrayList<RelatedContacts>  p;//关联人脉

	public ArrayList<RelatedOrganization> o;//关联组织
	
	public ArrayList<RelatedKnowledge> k;//关联知识

	public ArrayList<RelatedEvent> r;//关联事件

	@Override
	public String toString() {
		return "RelatedInformation [p=" + p + ", o=" + o + ", k=" + k + ", r="
				+ r + "]";
	}

}
