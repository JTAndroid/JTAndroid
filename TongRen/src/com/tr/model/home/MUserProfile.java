package com.tr.model.home;

import java.io.Serializable;
import java.util.List;

public class MUserProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 433453159162497540L;

	private String image;
	private String shortName;
	private int gender;//1 男 2女 0未知
	private String companyName;
	private String industry;
	private List<String> listCareIndustryIds;
	private List<String> listCareIndustryNames;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public List<String> getListCareIndustryIds() {
		return listCareIndustryIds;
	}

	public void setListCareIndustryIds(List<String> listCareIndustryIds) {
		this.listCareIndustryIds = listCareIndustryIds;
	}

	public List<String> getListCareIndustryNames() {
		return listCareIndustryNames;
	}

	public void setListCareIndustryNames(List<String> listCareIndustryNames) {
		this.listCareIndustryNames = listCareIndustryNames;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

}
