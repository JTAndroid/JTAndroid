package com.tr.ui.tongren.model.project;

import java.io.Serializable;
import java.util.List;

public class OrganizationSumup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Organization organization;
	private List<OrganizationMember> userList;

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public List<OrganizationMember> getUserList() {
		return userList;
	}

	public void setUserList(List<OrganizationMember> userList) {
		this.userList = userList;
	}

}
