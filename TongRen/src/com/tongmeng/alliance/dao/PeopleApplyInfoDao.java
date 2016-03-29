package com.tongmeng.alliance.dao;

import java.util.List;

/**
 * 成员管理中，成员具体信息类
 * 
 * @author Administrator
 * 
 */
public class PeopleApplyInfoDao {
	public int id;
	public String name;
	public String mobile;
	public String email;
	public String company;
	public String position;

	public List<PeopleApplyInfoPropertiesDao> propertiesList;
	public List<PeopleApplyInfoTagDao> demandTagList;
	public List<PeopleApplyInfoTagDao> supplyTagList;

	public String signInCode;
	public String isSignIn;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public List<PeopleApplyInfoPropertiesDao> getPropertiesList() {
		return propertiesList;
	}

	public void setPropertiesList(
			List<PeopleApplyInfoPropertiesDao> propertiesList) {
		this.propertiesList = propertiesList;
	}

	public List<PeopleApplyInfoTagDao> getDemandTagList() {
		return demandTagList;
	}

	public void setDemandTagList(List<PeopleApplyInfoTagDao> demandTagList) {
		this.demandTagList = demandTagList;
	}

	public List<PeopleApplyInfoTagDao> getSupplyTagList() {
		return supplyTagList;
	}

	public void setSupplyTagList(List<PeopleApplyInfoTagDao> supplyTagList) {
		this.supplyTagList = supplyTagList;
	}

	public String getSignInCode() {
		return signInCode;
	}

	public void setSignInCode(String signInCode) {
		this.signInCode = signInCode;
	}

	public String getIsSignIn() {
		return isSignIn;
	}

	public void setIsSignIn(String isSignIn) {
		this.isSignIn = isSignIn;
	}

}
