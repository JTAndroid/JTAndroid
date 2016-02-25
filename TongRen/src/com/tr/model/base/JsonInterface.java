package com.tr.model.base;

import org.json.JSONObject;

public interface JsonInterface {
	public void initWithjson(JSONObject jsonObject);
	public JSONObject toJson(JSONObject jsonObject);
}
