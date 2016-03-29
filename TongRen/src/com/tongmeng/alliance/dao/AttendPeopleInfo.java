package com.tongmeng.alliance.dao;

import java.util.List;

public class AttendPeopleInfo {
	public int id;
	public String name;
	public String phone;
	public String pic;
	public List<Apply> applyList;

	public AttendPeopleInfo(int id, String name, String phone, String pic,List<Apply> applyList) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.pic = pic;
		this.applyList = applyList;
	}

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public AttendPeopleInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Apply> getApplyList() {
		return applyList;
	}

	public void setApplyList(List<Apply> applyList) {
		this.applyList = applyList;
	}

}
