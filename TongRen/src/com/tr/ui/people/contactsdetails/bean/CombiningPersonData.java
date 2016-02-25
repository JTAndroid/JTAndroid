package com.tr.ui.people.contactsdetails.bean;

public class CombiningPersonData {

	private int personHeadImage;

	private String perosnName;

	private String personConmpany;

	private String personPersonPosition;

	public int getPersonHeadImage() {
		return personHeadImage;
	}

	public void setPersonHeadImage(int personHeadImage) {
		this.personHeadImage = personHeadImage;
	}

	public String getPerosnName() {
		return perosnName;
	}

	public void setPerosnName(String perosnName) {
		this.perosnName = perosnName;
	}

	public String getPersonConmpany() {
		return personConmpany;
	}

	public void setPersonConmpany(String personConmpany) {
		this.personConmpany = personConmpany;
	}

	public String getPersonPersonPosition() {
		return personPersonPosition;
	}

	public void setPersonPersonPosition(String personPersonPosition) {
		this.personPersonPosition = personPersonPosition;
	}

	@Override
	public String toString() {
		return "CombiningPersonData [personHeadImage=" + personHeadImage
				+ ", perosnName=" + perosnName + ", personConmpany="
				+ personConmpany + ", personPersonPosition="
				+ personPersonPosition + "]";
	}


}
