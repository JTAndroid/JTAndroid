package com.tr.ui.organization.model.industry;

import java.io.Serializable;
import java.util.ArrayList;

import com.tr.ui.organization.model.PushKnowledge;

/**
 *组织行业动态 
 *
 **/
public class CustomerOrgIndustry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String name;
	
	public ArrayList<PushKnowledge> list;

	@Override
	public String toString() {
		return "CustomerOrgIndustry [name=" + name + ", list=" + list + "]";
	}
	
}
