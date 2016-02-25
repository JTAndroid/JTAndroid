package com.tr.ui.organization.model.parameters;

import java.io.Serializable;

public class OrganizationDetialsIncomingParameters implements Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;

	public long orgId;
	
	public long userId;

	@Override
	public String toString() {
		return "OrganizationDetialsIncomingParameters [orgId=" + orgId
				+ ", userId=" + userId + "]";
	}

}
