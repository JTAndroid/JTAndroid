package com.tr.model.home;

import java.io.Serializable;
import java.util.ArrayList;

public class MainPageList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8176955422526075633L;
	String type;// list:
	ArrayList<MainImagesItem> list;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<MainImagesItem> getList() {
		return list;
	}

	public void setList(ArrayList<MainImagesItem> list) {
		this.list = list;
	}

}
