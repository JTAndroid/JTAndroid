package com.tr.model.obj;

import java.io.Serializable;

import org.json.JSONObject;

public class DynamicIndustry implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;// id
	private String code;
	private String name;

	public static DynamicIndustry createFactory(JSONObject jsonObject) {
		try {
			DynamicIndustry self = new DynamicIndustry();
			self.id = jsonObject.optLong("id");
			self.code = jsonObject.optString("code");
			self.name = jsonObject.optString("name");
			return self;
		} catch (Exception e) {
			return null;
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}