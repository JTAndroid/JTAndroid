package com.tr.model.conference;

import java.io.Serializable;

import org.json.JSONObject;

import com.utils.time.Util;

public class MMeetingTopicList implements Serializable {

	private static final long serialVersionUID = 1081451342899359438L;
	private MMeetingQuery meetingQuery;

	public MMeetingQuery getMeetingQuery() {
		return meetingQuery;
	}
	public void setMeetingQuery(MMeetingQuery meetingQuery) {
		this.meetingQuery = meetingQuery;
	}
	/**
	 * @param jsonObject
	 * @return MeetingData的集合链表
	 */
	public static MMeetingTopicList createFactory(JSONObject jsonObject) {
		MMeetingTopicList detail = null;
		try {
			if (jsonObject != null) {
//				detail = JSON.parseObject(jsonObject.toString(), MMeetingTopicList.class);
				detail = (MMeetingTopicList) Util.getParseJsonObject(jsonObject, MMeetingTopicList.class);
			}
			return detail;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
