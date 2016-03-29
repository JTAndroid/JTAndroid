package com.tongmeng.alliance.dao;

/**
 * 成员信息类
 * 
 * @author Administrator
 * 
 */
public class PeopleNumberDao {

	public int id;
	public String nickname;
	public String picPath;
	public String company;
	public String position;
	public String type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "id:" + id + ",nickname:" + nickname + ",picPath:" + picPath
				+ ",company:" + company + ",position:" + position + ",type:"
				+ type;
	}
}
