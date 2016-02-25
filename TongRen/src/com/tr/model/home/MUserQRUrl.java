package com.tr.model.home;

import java.io.Serializable;

import org.json.JSONObject;

import com.google.gson.Gson;

public class MUserQRUrl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7731100533276221789L;

	private boolean success;
	private String url;

	public static MUserQRUrl createFactory(JSONObject jsonObject) {
		MUserQRUrl self = null;

		try {
			if (jsonObject != null) {
				self = new MUserQRUrl();
				if (jsonObject.has("success")) {
					self.success = jsonObject.getBoolean("success");
				}
				if (jsonObject.has("url")) {
					self.url = jsonObject.getString("url");
				}
//				self = JSON.parseObject(jsonObject.toString(), MUserQRUrl.class);
//				self = (MUserQRUrl) Util.getParseJsonObject(jsonObject, MUserQRUrl.class);
				Gson gson = new Gson();
				gson.fromJson(jsonObject.toString(), MUserQRUrl.class);
			}
			return self;
		} catch (Exception e) {
		}
		return null;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
