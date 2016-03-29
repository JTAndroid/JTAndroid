package com.tongmeng.alliance.dao;

public class MyCustomerList {
	int id;
	String  name ;
	String company ;
	String position; 
	String  mobile ;
	String avatar;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public MyCustomerList() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MyCustomerList(int id, String name, String company, String position,
			String mobile, String avatar) {
		super();
		this.id = id;
		this.name = name;
		this.company = company;
		this.position = position;
		this.mobile = mobile;
		this.avatar = avatar;
	} 
	
}
