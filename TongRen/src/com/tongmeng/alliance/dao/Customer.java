package com.tongmeng.alliance.dao;

import java.util.List;

public class Customer {
	private int id;
	private int clientUserId;
	private String name;
	private String mobile;
	private String email;
	private String company;
	private String position;
	private List<Category> category;
	private List<DemandTag> demangTag;
	private List<SupplyTag> supplyTag;
	private String remark;
	private List<Properties> properties;
	private String history;
	private String historyCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getClientUserId() {
		return clientUserId;
	}

	public void setClientUserId(int clientUserId) {
		this.clientUserId = clientUserId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public List<Category> getCategory() {
		return category;
	}

	public void setCategory(List<Category> category) {
		this.category = category;
	}

	public List<DemandTag> getDemangTag() {
		return demangTag;
	}

	public void setDemangTag(List<DemandTag> demangTag) {
		this.demangTag = demangTag;
	}

	public List<SupplyTag> getSupplyTag() {
		return supplyTag;
	}

	public void setSupplyTag(List<SupplyTag> supplyTag) {
		this.supplyTag = supplyTag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Properties> getProperties() {
		return properties;
	}

	public void setProperties(List<Properties> properties) {
		this.properties = properties;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getHistoryCount() {
		return historyCount;
	}

	public void setHistoryCount(String historyCount) {
		this.historyCount = historyCount;
	}

}
