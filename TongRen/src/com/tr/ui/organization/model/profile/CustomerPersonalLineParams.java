package com.tr.ui.organization.model.profile;

import java.io.Serializable;

public class CustomerPersonalLineParams implements Serializable{

	/**
	 * 查询研究报告
	 */
	private static final long serialVersionUID = 1L;
	
	public String customerId;

	@Override
	public String toString() {
		return "CustomerPersonalLineParams [customerId=" + customerId + "]";
	}
	
}
