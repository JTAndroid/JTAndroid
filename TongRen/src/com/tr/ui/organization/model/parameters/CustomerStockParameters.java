package com.tr.ui.organization.model.parameters;

import java.io.Serializable;

public class CustomerStockParameters implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String customerId;

	@Override
	public String toString() {
		return "CustomerStockParameters [customerId=" + customerId + "]";
	}
	
	

}
