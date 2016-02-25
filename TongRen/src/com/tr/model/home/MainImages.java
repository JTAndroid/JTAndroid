package com.tr.model.home;

import java.io.Serializable;

public class MainImages implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1306152909741076715L;
	private String small;
	private String large;
	private String medium;
	private int resourceID;
	
	public String getSmall() {
		return small;
	}
	public void setSmall(String small) {
		this.small = small;
	}
	public String getLarge() {
		return large;
	}
	public void setLarge(String large) {
		this.large = large;
	}
	public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
	public int getResourceID() {
		return resourceID;
	}
	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}
	
}
