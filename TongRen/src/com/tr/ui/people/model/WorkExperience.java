package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.List;

/**
 * 工作经历
 */
public class WorkExperience implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6513425390312538319L;
	/**
	 * 开始时间
	 */
	public String stime;
	/**
	 * 结束时间
	 */
	public String etime;
	/**
	 * 公司
	 */
	public String company;
	/**
	 * 公司行业
	 */
	public String companyIndustry;
	/**
	 * 部门
	 */
	public String department;
	/**
	 * 职位
	 */
	public String position;
	/**
	 * 证明人
	 */
	public String certifier;
	/**
	 * 证明人联系方式
	 */
	public String certifierPhone;
	/**
	 * 公司关系
	 */
	public List<Basic> colleagueRelationshipList;
	/**
	 * 工作经历自定义项，改为数组
	 */

	public List<Basic> custom;

	/**
	 * 新增4.27 将本经历作为当前身份
	 */
	public boolean currentStatus;

	/**
	 * 描述
	 */
	public String desc;

	public WorkExperience() {

	}

	public WorkExperience(String company, String position, String stime, String etime, String desc) {
		this.company = company;
		this.position = position;
		this.stime = stime;
		this.etime = etime;
		this.desc = desc;
	}

	public String getsTime() {
		return stime;
	}

	public void setsTime(String stime) {
		this.stime = stime;
	}

	public String geteTime() {
		return etime;
	}

	public void seteTime(String etime) {
		this.etime = etime;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyIndustry() {
		return companyIndustry;
	}

	public void setCompanyIndustry(String companyIndustry) {
		this.companyIndustry = companyIndustry;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCertifier() {
		return certifier;
	}

	public void setCertifier(String certifier) {
		this.certifier = certifier;
	}

	public String getCertifierPhone() {
		return certifierPhone;
	}

	public void setCertifierPhone(String certifierPhone) {
		this.certifierPhone = certifierPhone;
	}

	public List<Basic> getColleagueRelationshipList() {
		return colleagueRelationshipList;
	}

	public void setColleagueRelationshipList(List<Basic> colleagueRelationshipList) {
		this.colleagueRelationshipList = colleagueRelationshipList;
	}

	public List<Basic> getCustom() {
		return custom;
	}

	public void setCustom(List<Basic> custom) {
		this.custom = custom;
	}

	public boolean isCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(boolean currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
