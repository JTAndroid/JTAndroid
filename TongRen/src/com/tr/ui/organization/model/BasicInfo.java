package com.tr.ui.organization.model;

import java.io.Serializable;

public class BasicInfo implements Serializable {

	private static final long serialVersionUID = -1752294409746263516L;
	
	private String picLogo;
	private String name;
	private String industrys;
	private String email;
	private String linkedMobile;
	private String stockNum;
	private String discribe;
	
	public String getPicLogo() {
		return picLogo;
	}
	public void setPicLogo(String picLogo) {
		this.picLogo = picLogo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIndustrys() {
		return industrys;
	}
	public void setIndustrys(String industrys) {
		this.industrys = industrys;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLinkedMobile() {
		return linkedMobile;
	}
	public void setLinkedMobile(String linkedMobile) {
		this.linkedMobile = linkedMobile;
	}
	public String getStockNum() {
		return stockNum;
	}
	public void setStockNum(String stockNum) {
		this.stockNum = stockNum;
	}
	public String getDiscribe() {
		return discribe;
	}
	public void setDiscribe(String discribe) {
		this.discribe = discribe;
	}

}
