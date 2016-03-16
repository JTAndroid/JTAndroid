package com.common.evebusmodel;

public class AddCategoryEvent {
	private boolean bool;
	public AddCategoryEvent(Boolean bool){
		this.bool = bool;
	}
	
	public boolean getBool(){
		return bool;
	}
}
