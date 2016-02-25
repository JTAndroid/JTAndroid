package com.tr.model.home;

import java.io.Serializable;

public class MSuggestionType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3281768663543391564L;

//	{"id":2,"name":"无","type":0}
	
	private String id;
	private String name;
	private String type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
