package com.tr.ui.people.model.params;

import com.tr.ui.organization.model.Contacts;

public class ContactsS implements Comparable<ContactsS>{
    
	private String name;
	private String index;
	
	public ContactsS(String name,String index){
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	@Override
	public int compareTo(ContactsS another) {	    
		return this.index.compareTo(another.index);
	}
	
	
	
	
}

