
package com.tr.ui.people.contactsdetails.bean;

import java.io.Serializable;

public class News implements Serializable{
	
	private String title;
	private String name;
	private String time;
	public News() {
		super();
		// TODO Auto-generated constructor stub
	}
	public News(String title, String name, String time) {
		super();
		this.title = title;
		this.name = name;
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "News [title=" + title + ", name=" + name + ", time=" + time
				+ "]";
	}
	

}
