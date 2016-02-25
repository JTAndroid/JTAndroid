package com.tr.ui.organization.model;

import java.io.Serializable;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 
 * @author sunjianan
 * 
 */
public class RegisteOrgDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9070823335206157720L;

	public boolean success;

	public RegistOrgInfo orgInfo;

	public static Object createFactory(JSONObject jsonObject) {
		Gson gson = new Gson();
		return gson.fromJson(jsonObject.toString(), RegisteOrgDetail.class);
	}

}
