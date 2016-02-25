package com.tr.ui.tongren.model.project;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrganizationDetail implements Serializable {

	private static final long serialVersionUID = 2209748151579526684L;

	private List<String> industryName;
	private List<String> classificationName;
	private List<String> areaName;
	private String createName;
	private Organization success;

	public List<String> getIndustryName() {
		return industryName;
	}

	public void setIndustryName(List<String> industryName) {
		this.industryName = industryName;
	}

	public List<String> getClassificationName() {
		return classificationName;
	}

	public void setClassificationName(List<String> classificationName) {
		this.classificationName = classificationName;
	}

	public List<String> getAreaName() {
		return areaName;
	}

	public void setAreaName(List<String> areaName) {
		this.areaName = areaName;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Organization getSuccess() {
		return success;
	}

	public void setSuccess(Organization success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
