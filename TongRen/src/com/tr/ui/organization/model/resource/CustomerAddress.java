package com.tr.ui.organization.model.resource;

import java.io.Serializable;


public class CustomerAddress implements Serializable{

public static final long serialVersionUID = -3376130394695782352L;
	
	/**主键*/
	public Long id;
	/**通讯类型： 1-联系方式，2-投资需求，3-融资需求，4-专家需求，5-专家身份*/
	public Integer parentType;
	/**地区类型：0-国内，1-国外*/
	public Integer areaType;
	/**省/洲*/
	public String stateName;
	/**城市/国*/
	public String cityName;
	/**县区*/
	public String countyName;
	/**地址*/
	public String address;
	/**邮编*/
	public String postalCode;
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
	public Integer getAreaType() {
		return areaType;
	}
	public void setAreaType(Integer areaType) {
		this.areaType = areaType;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "CustomerAddress [id=" + id + ", parentType=" + parentType
				+ ", areaType=" + areaType + ", stateName=" + stateName
				+ ", cityName=" + cityName + ", countyName=" + countyName
				+ ", address=" + address + ", postalCode=" + postalCode + "]";
	}
	
}
