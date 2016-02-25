package com.tr.ui.organization.model.industry;

import java.io.Serializable;

public class CustomerOrgIndustryParams implements Serializable{

	/**
	 * 查询行业动态传入的参数
	 */
	private static final long serialVersionUID = 1L;
	
	public String customerId;

	@Override
	public String toString() {
		return "CustomerOrgIndustryParams [customerId=" + customerId + "]";
	}
	
	

}
