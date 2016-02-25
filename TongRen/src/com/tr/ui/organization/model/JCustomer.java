package com.tr.ui.organization.model;

import java.io.Serializable;
import java.util.List;

import com.tr.ui.organization.personalPlateList;
import com.tr.ui.organization.model.industry.CustomerIndustry;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerPersonalPlate;
import com.tr.ui.organization.model.profile.CustomerPhone;
import com.tr.ui.organization.model.template.CustomerColumn;

public class JCustomer implements Serializable {

	/**
	 * 
	 */
//	private static final long serialVersionUID = 1928737414276972503L;
//	public String name;
//	public String shotName;
//	public String type;
//	
//	public List<CustomerIndustry> industrys;
//	public String isListing;
//	public String stockNum;
//	public String linkMobile;
//	public String linkEmail;
//	public String picLogo;
//	public String banner;
//	public String discribe;
//	
//	public List<CustomerColumn> columns;
//	public String auth;
//	public int createdid;
//	public List<CustomerPersonalPlate> personalPlateList;
//	public List<CustomerPersonalLine> propertyList;
//	public String virtual;
//	public boolean friends;
	
	/**
	 * 
	 */
//	private static final long serialVersionUID = -5781119406406734952L;
	public class TestCustomer
	{
			
	public int customerId;
	public String name;
	public String shotName;
	public String type;
	
	public List<CustomerIndustry> industrys;
	public String isListing;
	public String stockNum;
	public String linkMobile;
	public String linkEmail;
	public String picLogo;
	public String banner;
	public String discribe;
	
	public List<CustomerColumn> columns;
	public String auth;
	public int createdid;
	public List<CustomerPersonalPlate> personalPlateList;
	public List<CustomerPersonalLine> propertyList;
	public String virtual;
	public boolean friends;
	

	}

	public class OuterCustomer
	{
	public TestCustomer customer;
	public boolean success;
	}
}
