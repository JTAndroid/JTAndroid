package com.tongmeng.alliance.dao;

/**
 * 成员管理，成员具体信息类中的标签类
 * 
 * @author Administrator
 * 
 */
public class PeopleApplyInfoTagDao {

	public int id;
	public String name;
	public String isEditable;

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

	public String getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(String isEditable) {
		this.isEditable = isEditable;
	}

}
