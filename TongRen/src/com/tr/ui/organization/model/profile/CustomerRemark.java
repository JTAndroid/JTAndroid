package com.tr.ui.organization.model.profile;

import java.io.Serializable;
import java.util.List;


import com.tr.ui.organization.model.profile.CustomerPersonalLine;

public class CustomerRemark implements Serializable{
	
	/**
	 * 
	 */
	public static final long serialVersionUID = -412637381579392447L;
	public String id;
	public String remark;
	public String taskId;
	public List<CustomerPersonalLine> propertyList; //自定义属性
	
	
	
	
}
