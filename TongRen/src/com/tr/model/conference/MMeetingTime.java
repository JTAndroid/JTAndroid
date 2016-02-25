package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class MMeetingTime implements Serializable {
	private static final long serialVersionUID = -7663123788360341934L;
	/**
	 	
        "endTime":"结束时间 ",
        "id":"会议时间ID",
        "meetingId":"会议ID",
        "startTime":"开始时间 "
        
	 * 
	 */
	private Long id;
	private Long meetingId;
	private String startTime;
	private String endTime;

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("id", id);
		jObject.put("meetingId", meetingId);
		jObject.put("startTime", startTime);
		jObject.put("endTime", endTime);
		return jObject;
	}
	
	public String getStartTime() {
		if(startTime == null) {
			return "";
		}
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		if(endTime == null) {
			return "";
		}
		return endTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
