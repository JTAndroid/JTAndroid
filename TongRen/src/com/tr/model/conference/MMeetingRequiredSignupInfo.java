package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

import com.utils.time.Util;

public class MMeetingRequiredSignupInfo implements Serializable {

	private static final long serialVersionUID = 3918435132142230000L;
	
	private List<MMeetingSignLabelDataQuery> listMeetingSignLabelDataQuery;
	
	public static Object createFactory(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}
//		return JSON.parseObject(jsonObject.toString(), MMeetingRequiredSignupInfo.class);
		return Util.getParseJsonObject(jsonObject, MMeetingRequiredSignupInfo.class);
	}
	
	public List<MMeetingSignLabelDataQuery> getListMeetingSignLabelDataQuery() {
		return listMeetingSignLabelDataQuery;
	}

	public void setListMeetingSignLabelDataQuery(
			List<MMeetingSignLabelDataQuery> listMeetingSignLabelDataQuery) {
		this.listMeetingSignLabelDataQuery = listMeetingSignLabelDataQuery;
	}
	
	
	
}
