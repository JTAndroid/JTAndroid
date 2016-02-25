package com.tr.model.obj;

import java.io.Serializable;

public class JTRegion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id = 0;
	private int parentId = 0;
	private String cname = "";
	private String ename = "";
	private int orders = 0;
	
	public JTRegion(){
		
	}
	
	public JTRegion(String cname){
		this.cname = cname;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public int getOrders() {
		return orders;
	}
	public void setOrders(int orders) {
		this.orders = orders;
	}
}
