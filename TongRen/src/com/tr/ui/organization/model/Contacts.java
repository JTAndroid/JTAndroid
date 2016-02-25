package com.tr.ui.organization.model;

import android.text.TextUtils;

public class Contacts implements Comparable<Contacts>{
    
	private String nameFirst;
	public String name;
	public String picLogo;
	private String city;
	private String industrys;
	public int virtual;
	public long customerId;
	private String linkMobile;
	private String shortName;
	public long createById;

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getNameFirst() {
		return nameFirst;
	}

	public void setNameFirst(String nameFirst) {
		this.nameFirst = nameFirst;
	}

	public String getLinkMobile() {
		return linkMobile;
	}

	public void setLinkMobile(String linkMobile) {
		this.linkMobile = linkMobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicLogo() {
		return picLogo;
	}

	public void setPicLogo(String picLogo) {
		this.picLogo = picLogo;
	}

	public int getVirtual() {
		return virtual;
	}

	public void setVirtual(int virtual) {
		this.virtual = virtual;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIndustrys() {
		return TextUtils.isEmpty(industrys)?"":industrys;
	}

	public void setIndustrys(String industrys) {
		this.industrys = industrys;
	}
	
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	@Override
	public int compareTo(Contacts another) {	    
		return this.nameFirst.compareTo(another.nameFirst);
	}
	
	
	
	
}
