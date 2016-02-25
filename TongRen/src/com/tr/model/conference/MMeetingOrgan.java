package com.tr.model.conference;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 会议中的组织
 */
public class MMeetingOrgan implements Serializable{
	private static final long serialVersionUID = -913687528292760598L;
	private long id;
	private Long meetingId;
	private String organId;
	private String organName;
	private String organPhoto;
	/**"organType":"类型 1：组织、2：机构"*/
	private int organType;
	
	/**"organType":"类型 1：组织、2：机构"*/
	public int getOrganType() {
		return organType;
	}
	/**"organType":"类型 1：组织、2：机构"*/
	public void setOrganType(int organType) {
		this.organType = organType;
	}
	public String getOrganId() {
		organId = organId==null?"":organId;
		return organId;
	}
	public void setOrganId(String organId) {
		organId = organId==null?"":organId;
		this.organId = organId;
	}
	public String getOrganName() {
		organName = organName==null?"":organName;
		return organName;
	}
	public void setOrganName(String organName) {
		organName = organName==null?"":organName;
		this.organName = organName;
	}
	public String getOrganPhoto() {
		organPhoto = organPhoto==null?"":organPhoto;
		return organPhoto;
	}
	public void setOrganPhoto(String organPhoto) {
		
		organPhoto = organPhoto==null?"":organPhoto;
		this.organPhoto = organPhoto;
	}
	public JSONObject toJsonObj() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("organId", organId);
		jsonObject.put("organName", organName);
		jsonObject.put("organPhoto", organPhoto);
		jsonObject.put("organType", organType);
		return jsonObject;
	}
	public Long getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	

}
