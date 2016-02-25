package com.tr.ui.organization.model.param;

import java.io.Serializable;

public class OrgClientDetalisIncomParams implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8128716513428502800L;
	
    public long orgId ;
	
	@Override
	public String toString() {
		return "ClientDetialsIncomParams [orgId=" + orgId + "]";
	}

}
