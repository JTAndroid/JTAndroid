package com.tr.model.home;

import java.io.Serializable;
import java.util.List;

/** @author sunjianan */
public class MCustomzation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8786359496295468070L;

	private List<String> listCareIndustryIds;
	private List<String> listCareIndustryNames;
	
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

}
