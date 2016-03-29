package com.tongmeng.alliance.dao;

public class GroupManagerDao {
	public String id;
	public String picPath;
	public boolean isQunzhu;
	public String name;
	public boolean isQiandao;
	public String position;
	public String company;
	public String phone;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public boolean isQunzhu() {
		return isQunzhu;
	}

	public void setQunzhu(boolean isQunzhu) {
		this.isQunzhu = isQunzhu;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isQiandao() {
		return isQiandao;
	}

	public void setQiandao(boolean isQiandao) {
		this.isQiandao = isQiandao;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
