package com.tr.ui.organization.model.resource;

import java.io.Serializable;
import java.util.List;

import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.resource.CustomerAddress;
/**
* <p>Title: CustomerDemandCommon.java<／p> 
* <p>Description: 投资、融资、专家需求、专家身份<／p> 
* @author wfl
* @date 2015-3-10 
* @version 1.0
 */
public class CustomerDemandCommon implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主键 */
	public Long id;
	/** 所属模块： 1-投资，2-融资，3-专家需求，4-专家身份 */
	public Integer parentType;
	/** 地址 */
	public CustomerAddress address;
	/** 行业 */
	public String industryIds;
	/** 行业 */
	public String industryNames;
	/** 类型 */
	public String typeIds;
	/** 类型 */
	public String typeNames;
	/** 自定义字段 */
	public List<CustomerPersonalLine> personalLineList;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getParentType() {
		return parentType;
	}
	public void setParentType(Integer parentType) {
		this.parentType = parentType;
	}
	public CustomerAddress getAddress() {
		return address;
	}
	public void setAddress(CustomerAddress address) {
		this.address = address;
	}
	public String getIndustryIds() {
		return industryIds;
	}
	public void setIndustryIds(String industryIds) {
		this.industryIds = industryIds;
	}
	public String getIndustryNames() {
		return industryNames;
	}
	public void setIndustryNames(String industryNames) {
		this.industryNames = industryNames;
	}
	public String getTypeIds() {
		return typeIds;
	}
	public void setTypeIds(String typeIds) {
		this.typeIds = typeIds;
	}
	public String getTypeNames() {
		return typeNames;
	}
	public void setTypeNames(String typeNames) {
		this.typeNames = typeNames;
	}
	public List<CustomerPersonalLine> getPersonalLineList() {
		return personalLineList;
	}
	public void setPersonalLineList(List<CustomerPersonalLine> personalLineList) {
		this.personalLineList = personalLineList;
	}
	@Override
	public String toString() {
		return "CustomerDemandCommon [id=" + id + ", parentType=" + parentType
				+ ", address=" + address + ", industryIds=" + industryIds
				+ ", industryNames=" + industryNames + ", typeIds=" + typeIds
				+ ", typeNames=" + typeNames + ", personalLineList="
				+ personalLineList + "]";
	}

}
