package com.tr.model.home;

import java.io.Serializable;

import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * @author chewei
 * @description TODO
 * @parameter 
 */
public class PeoplePageIndustry implements Serializable{

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
	
	private PeopleIndustrys page;

	public PeopleIndustrys getPage() {
		return page;
	}

	public void setPage(PeopleIndustrys page) {
		this.page = page;
	}
}
