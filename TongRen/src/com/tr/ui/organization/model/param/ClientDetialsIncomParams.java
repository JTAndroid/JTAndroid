package com.tr.ui.organization.model.param;

import java.io.Serializable;

public class ClientDetialsIncomParams implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5441975853440817992L;
	
	public long orgId ;
	public int view;
	

	@Override
	public String toString() {
		return "ClientDetialsIncomParams [orgId=" + orgId + "]";
	}
	
	

}
