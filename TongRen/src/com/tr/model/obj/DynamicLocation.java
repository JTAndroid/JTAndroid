package com.tr.model.obj;

import java.io.Serializable;

import org.json.JSONObject;

public class DynamicLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String dimension;
	private String name;
	private String secondLevel;
	private String detailName;
	private String type;
	private String mobile;

	public static DynamicLocation createFactory(JSONObject jsonObject) {
		try {
			DynamicLocation self = new DynamicLocation();
			self.dimension = jsonObject.optString("dimension");
			self.name = jsonObject.optString("name");
			self.secondLevel = jsonObject.optString("secondLevel");
			self.detailName = jsonObject.optString("detailName");
			self.mobile = jsonObject.optString("mobile");
			self.type = jsonObject.optString("type");
			return self;
		} catch (Exception e) {
			return null;
		}
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondLevel() {
		return secondLevel;
	}

	public void setSecondLevel(String secondLevel) {
		this.secondLevel = secondLevel;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}