package com.tr.ui.organization.orgdetails.bean;

import java.io.Serializable;

public class OrgAddModules implements Serializable{
	
	private String area,trade,type,customFields,customContent;

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCustomFields() {
		return customFields;
	}

	public void setCustomFields(String customFields) {
		this.customFields = customFields;
	}

	public String getCustomContent() {
		return customContent;
	}

	public void setCustomContent(String customContent) {
		this.customContent = customContent;
	}

	@Override
	public String toString() {
		return "OrgAddModules [area=" + area + ", trade=" + trade + ", type="
				+ type + ", customFields=" + customFields + ", customContent="
				+ customContent + "]";
	}
	

}
