package com.tr.model.home;

import java.io.Serializable;

public class PeopleProfession implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String firstIndustryDirection;
	int firstIndustryDirectionId;
	String secondIndustryDirection;
	int secondIndustryDirectionId;
	public String getFirstIndustryDirection() {
		return firstIndustryDirection;
	}
	public void setFirstIndustryDirection(String firstIndustryDirection) {
		this.firstIndustryDirection = firstIndustryDirection;
	}
	public int getFirstIndustryDirectionId() {
		return firstIndustryDirectionId;
	}
	public void setFirstIndustryDirectionId(int firstIndustryDirectionId) {
		this.firstIndustryDirectionId = firstIndustryDirectionId;
	}
	public String getSecondIndustryDirection() {
		return secondIndustryDirection;
	}
	public void setSecondIndustryDirection(String secondIndustryDirection) {
		this.secondIndustryDirection = secondIndustryDirection;
	}
	public int getSecondIndustryDirectionId() {
		return secondIndustryDirectionId;
	}
	public void setSecondIndustryDirectionId(int secondIndustryDirectionId) {
		this.secondIndustryDirectionId = secondIndustryDirectionId;
	}
	
	
}
