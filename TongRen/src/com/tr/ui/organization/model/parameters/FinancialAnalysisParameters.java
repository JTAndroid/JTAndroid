package com.tr.ui.organization.model.parameters;

import java.io.Serializable;

public class FinancialAnalysisParameters implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * 财务分析传入的参数
	 */
	public String customerId = "2";
	
	public String year = "2014";
	
	public String type = "1";
	
	public int quarter = 1;

	@Override
	public String toString() {
		return "FinancialAnalysisParameters [customerId=" + customerId
				+ ", year=" + year + ", type=" + type + ", quarter=" + quarter
				+ "]";
	}
	
	

}
