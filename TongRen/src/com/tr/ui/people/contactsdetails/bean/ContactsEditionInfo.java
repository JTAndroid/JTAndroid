package com.tr.ui.people.contactsdetails.bean;

public class ContactsEditionInfo {

	private String contactsEditioninfo;
	
	private String contactsDepartmentinfo; 
	
	private String contactsJobinfo;
	
	private String contactsinfo;
	
	private int contactsimage;

	public String getContactsEditioninfo() {
		return contactsEditioninfo;
	}

	public void setContactsEditioninfo(String contactsEditioninfo) {
		this.contactsEditioninfo = contactsEditioninfo;
	}

	public String getContactsDepartmentinfo() {
		return contactsDepartmentinfo;
	}

	public void setContactsDepartmentinfo(String contactsDepartmentinfo) {
		this.contactsDepartmentinfo = contactsDepartmentinfo;
	}

	public String getContactsJobinfo() {
		return contactsJobinfo;
	}

	public void setContactsJobinfo(String contactsJobinfo) {
		this.contactsJobinfo = contactsJobinfo;
	}

	public String getContactsinfo() {
		return contactsinfo;
	}

	public void setContactsinfo(String contactsinfo) {
		this.contactsinfo = contactsinfo;
	}

	public int getContactsimage() {
		return contactsimage;
	}

	public void setContactsimage(int contactsimage) {
		this.contactsimage = contactsimage;
	}

	@Override
	public String toString() {
		return "ContactsEditionInfo [contactsEditioninfo="
				+ contactsEditioninfo + ", contactsDepartmentinfo="
				+ contactsDepartmentinfo + ", contactsJobinfo="
				+ contactsJobinfo + ", contactsinfo=" + contactsinfo
				+ ", contactsimage=" + contactsimage + "]";
	}
	
}
