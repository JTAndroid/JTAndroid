package com.tongmeng.alliance.dao;

public class Category {
	int id;
	String name;
	int count;

	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Category(int id, String name, int count) {
		super();
		this.id = id;
		this.name = name;
		this.count = count;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
