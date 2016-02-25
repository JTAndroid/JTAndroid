package com.tr.model.conference;

import java.io.Serializable;

import org.json.JSONObject;

import com.utils.time.Util;

public class MMeetingNoteObj implements Serializable {

	private static final long serialVersionUID = 8019005660997566967L;
	
	private MMeetingNoteQuery meetingNoteQuery;

	

	public MMeetingNoteQuery getMeetingNoteQuery() {
		return meetingNoteQuery;
	}



	public void setMeetingNoteQuery(MMeetingNoteQuery meetingNoteQuery) {
		this.meetingNoteQuery = meetingNoteQuery;
	}



	/**
	 * @param jsonObject
	 * @return MeetingData的集合链表
	 */
	public static MMeetingNoteObj createFactory(JSONObject jsonObject) {
		MMeetingNoteObj detail = null;
		try {
			if (jsonObject != null) {
//				detail = JSON.parseObject(jsonObject.toString(), MMeetingNoteObj.class);
				detail = (MMeetingNoteObj) Util.getParseJsonObject(jsonObject,  MMeetingNoteObj.class);		}
			return detail;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
