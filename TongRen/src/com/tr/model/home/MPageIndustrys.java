package com.tr.model.home;

import java.io.Serializable;

import org.json.JSONObject;

import com.google.gson.Gson;
/**
 * 
 * @author sunjianan
 *
 */
public class MPageIndustrys implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2921374142730755314L;
	
	public static Object createFactory(JSONObject jsonObject) {
		MPageIndustrys query = null;
		try {
			if (jsonObject != null) {
//				query = JSON.parseObject(jsonObject.toString(), MPageIndustrys.class);
//				query = (MPageIndustrys) Util.getParseJsonObject(jsonObject, MPageIndustrys.class);
				Gson gson = new Gson();
				query = gson.fromJson(jsonObject.toString(), MPageIndustrys.class);
			}
			return query;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private MIndustrys page;

	public MIndustrys getPage() {
		return page;
	}

	public void setPage(MIndustrys page) {
		this.page = page;
	}

}
