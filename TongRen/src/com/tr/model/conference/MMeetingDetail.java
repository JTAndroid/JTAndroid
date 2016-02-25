package com.tr.model.conference;

import java.io.Serializable;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.utils.time.Util;

public class MMeetingDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1611228388871164187L;
	private MMeetingQuery meeting;
	private MMeetingQuery meetingQuery;

	public MMeetingQuery getMeetingQuery() {
		return meetingQuery;
	}

	public void setMeetingQuery(MMeetingQuery meetingQuery) {
		this.meetingQuery = meetingQuery;
	}

	public MMeetingQuery getMeeting() {
		return meeting;
	}

	public void setMeeting(MMeetingQuery meeting) {
		this.meeting = meeting;
	}

	/**
	 * @param jsonObject
	 * @return MeetingData的集合链表
	 */
	public static MMeetingDetail createFactory(JSONObject jsonObject) {
		MMeetingDetail detail = null;
		try {
			if (jsonObject != null) {
//				detail = JSON.parseObject(jsonObject.toString(), MMeetingDetail.class);
				detail = (MMeetingDetail) Util.getParseJsonObject(jsonObject, MMeetingDetail.class);
				Gson gson = new Gson();
				gson.fromJson(jsonObject.toString(), MMeetingDetail.class);
			}
			return detail;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
