package com.tr.model.home;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * @author chewei
 * @description TODO
 * @parameter 
 */
public class PeopleIndustrys implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Object createFactory(JSONObject jsonObject) {
		PeopleIndustrys query = null;
		try {
			if (jsonObject != null) {
				Gson gson = new Gson();
				query = gson.fromJson(jsonObject.toString(), PeopleIndustrys.class);
			}
			return query;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	List<PeopleIndustry> industryDirections;
	private int total;
	
	public List<PeopleIndustry> getIndustryDirections() {
		return industryDirections;
	}
	public void setIndustryDirections(List<PeopleIndustry> industryDirections) {
		this.industryDirections = industryDirections;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	
}
