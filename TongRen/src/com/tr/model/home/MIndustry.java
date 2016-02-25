package com.tr.model.home;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class MIndustry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -615829412983433958L;

	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public JSONObject tojson() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}
}
