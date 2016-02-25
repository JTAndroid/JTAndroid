package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class MMeetingPeople implements Serializable{
	private static final long serialVersionUID = 4154416928579355996L;
	/**
		"id":"会议人脉ID",
        "isShare":"是否分享",
        "meetingId":"会议ID",
        "peopleDesc":"人脉描述 ",
        "peopleId":"人脉ID",
        "peopleName":"人员姓名",
        "peoplePhoto":"人员图片 "
	 * 
	 */
	private Long id;
	private Boolean isShare;
	private Long meetingId;

	private String peopleDesc;
	private Long peopleId;

	private String peopleName;
	private String peoplePhoto;
	/**"类型 1：人脉、2：用户"*/
	private int peopleType;
	
	public JSONObject toJSONObject() throws JSONException {
		JSONObject jObject = new JSONObject();
		jObject.put("peopleId", peopleId);
		jObject.put("peopleName", peopleName);
		jObject.put("peoplePhoto", peoplePhoto);		
		jObject.put("peopleDesc", peopleDesc);
		jObject.put("isShare", isShare);
		
		jObject.put("id", id);	
		jObject.put("meetingId", meetingId);
		
		jObject.put("peopleType", peopleType);
		return jObject;
	}
	


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsShare() {
		return isShare;
	}

	public void setIsShare(Boolean isShare) {
		this.isShare = isShare;
	}

	public Long getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}

	public String getPeopleDesc() {
		if(peopleDesc == null) 
			return "";
		return peopleDesc;
	}

	public void setPeopleDesc(String peopleDesc) {
		this.peopleDesc = peopleDesc;
	}

	public Long getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(Long peopleId) {
		this.peopleId = peopleId;
	}

	public String getPeopleName() {
		if(peopleName == null) 
			return "";
		return peopleName;
	}

	public void setPeopleName(String peopleName) {
		this.peopleName = peopleName;
	}

	public String getPeoplePhoto() {
		if(peoplePhoto == null) 
			return "";
		return peoplePhoto;
	}

	public void setPeoplePhoto(String peoplePhoto) {
		this.peoplePhoto = peoplePhoto;
	}


	/**"类型 1：人脉、2：用户"*/
	public int getPeopleType() {
		return peopleType;
	}


	/**"类型 1：人脉、2：用户"*/
	public void setPeopleType(int peopleType) {
		this.peopleType = peopleType;
	}
	
}
