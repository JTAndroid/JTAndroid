package com.common.evebusmodel;

public class AddCategoryEvent {
	public final boolean bool;
	
	public AddCategoryEvent(Boolean bool){
		this.bool = bool;
	}

	public boolean isBool() {
		return bool;
	}

}
