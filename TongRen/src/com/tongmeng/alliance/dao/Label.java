package com.tongmeng.alliance.dao;

public class Label {
//标签对象
	public int id;
	public String name;
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
	
	@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "id="+id+",name="+name;
		}
}
