package com.tr.ui.organization;

import java.util.List;

import com.tr.ui.organization.model.profile.CustomerPersonalLine;

public class personalPlateList {

	public String name;
	public List<CustomerPersonalLine> propertyList; // 客户分组
	@Override
	public String toString() {
		return "personalPlateList [name=" + name + ", propertyList="
				+ propertyList + "]";
	}	
}
