package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

import com.utils.time.Util;

/**
 * 
 * @author sunjianan
 * 
 */
public class MListMeetingQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6497101505199813564L;

	private List<MMeetingQuery> listMeetingQuery;

	/**
	 * @param jsonObject
	 * @return MListMeetingQuery
	 */
	public static MListMeetingQuery createFactory(JSONObject jsonObject) {
		MListMeetingQuery query = null;
		try {
			if (jsonObject != null) {
//				query = JSON.parseObject(jsonObject.toString(), MListMeetingQuery.class);
				query = (MListMeetingQuery) Util.getParseJsonObject(jsonObject, MListMeetingQuery.class);
			}

			return query;

		} catch (Exception e) {
		}
		return null;
	}

	public List<MMeetingQuery> getListMeetingQuery() {
		return listMeetingQuery;
	}

	public void setListMeetingQuery(List<MMeetingQuery> listMeetingQuery) {
		this.listMeetingQuery = listMeetingQuery;
	}
}
