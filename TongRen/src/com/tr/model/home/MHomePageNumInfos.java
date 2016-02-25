package com.tr.model.home;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;

public class MHomePageNumInfos implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1763833192148911692L;

	private List<MHomePageNumInfo> list;

	public static Object createFactory(JSONObject jsonObject) {
		MHomePageNumInfos query = null;
		try {
			if (jsonObject != null) {
//				query = JSON.parseObject(jsonObject.toString(), MHomePageNumInfos.class);
//				query = (MHomePageNumInfos) Util.getParseJsonObject(jsonObject, MHomePageNumInfos.class);
				Gson gson = new Gson(); 
				query = gson.fromJson(jsonObject.toString(), MHomePageNumInfos.class);
			}
			return query;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<MHomePageNumInfo> getList() {
		return list;
	}

	public void setList(List<MHomePageNumInfo> list) {
		this.list = list;
	}

}
