package com.tr.model.home;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

import com.utils.time.Util;

/** @author sunjianan */
public class MListCountry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3443603470087768278L;

	private List<MCountry> listCountry;
	private List<String> listNameFirst;

	public static MListCountry createFactory(JSONObject jsonObject) {
		MListCountry self = null;

		try {
			if (jsonObject != null) {
//				self = JSON.parseObject(jsonObject.toString(), MListCountry.class);
				self = (MListCountry) Util.getParseJsonObject(jsonObject,MListCountry.class);
			}
			return self;
		} catch (Exception e) {
		}
		return null;
	}

	public List<MCountry> getListCountry() {
		return listCountry;
	}

	public void setListCountry(List<MCountry> listCountry) {
		this.listCountry = listCountry;
	}

	public List<String> getListNameFirst() {
		return listNameFirst;
	}

	public void setListNameFirst(List<String> listNameFirst) {
		this.listNameFirst = listNameFirst;
	}

}
