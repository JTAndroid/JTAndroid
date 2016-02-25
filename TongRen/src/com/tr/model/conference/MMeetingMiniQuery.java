     package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;

public class MMeetingMiniQuery implements Serializable {

	/**
	 * sunjianan 20141117
	 */
	private static final long serialVersionUID = -8637799399672073819L;
	
	/**
	  			"meetingId":"会议ID",
                "meetingName":"会议名称",
                "peopleCount":"人脉数量",
                "requirementCount":"需求数量",
                "knowledgeCount":"知识数量",
                "noteCount":"会议笔记数量",
                "startTime":"开始时间",
                "listMeetingRequirement":[],
                "listMeetingKnowledge":[].
                "listMeetingNoteQuery":[],
                "listMeetingPeople":[]
	 */
	
	private Long meetingId;
	private String meetingName;
	private Integer peopleCount;
	private Integer requirementCount;
	private Integer knowledgeCount;
	private Integer noteCount;
	
	private String startTime;
	private List<MMeetingData> listMeetingRequirement;
	private List<MMeetingData> listMeetingKnowledge;
	private List<MMeetingNoteQuery> listMeetingNoteQuery;
	private List<MMeetingPeople> listMeetingPeople;
	
	/** 会议中的群组 */
	private String groupId;
	
	
	/**
	 * @param jsonObject
	 * @return MMonthMeetingMiniQuery
	 */
//	public static List<MMeetingMiniQuery> createFactory(JSONObject jsonObject, int position) {
//		MMyMeeting myMeeting = null;
//		try {
//			if (jsonObject != null) {
//				Gson gson = new Gson();
//				myMeeting = gson.fromJson(jsonObject.toString(), MMyMeeting.class);
//			}
//
//			return  myMeeting.getListMonthMeetingQuery().get(position).getListMeetingMiniQuery();
//
//		} catch (Exception e) {
//		}
//		return null;
//	}
	
	
	public long getMeetingId() {
		return meetingId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Integer getPeopleCount() {
		return peopleCount;
	}
	public void setPeopleCount(Integer peopleCount) {
		this.peopleCount = peopleCount;
	}
	public Integer getRequirementCount() {
		return requirementCount;
	}
	public void setRequirementCount(Integer requirementCount) {
		this.requirementCount = requirementCount;
	}
	public Integer getKnowledgeCount() {
		return knowledgeCount;
	}
	public void setKnowledgeCount(Integer knowledgeCount) {
		this.knowledgeCount = knowledgeCount;
	}
	public Integer getNoteCount() {
		return noteCount;
	}
	public void setNoteCount(Integer noteCount) {
		this.noteCount = noteCount;
	}
	public List<MMeetingData> getListMeetingRequirement() {
		return listMeetingRequirement;
	}
	public void setListMeetingRequirement(List<MMeetingData> listMeetingRequirement) {
		this.listMeetingRequirement = listMeetingRequirement;
	}
	public List<MMeetingData> getListMeetingKnowledge() {
		return listMeetingKnowledge;
	}
	public void setListMeetingKnowledge(List<MMeetingData> listMeetingKnowledge) {
		this.listMeetingKnowledge = listMeetingKnowledge;
	}
	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}
	public void setMeetingId(long meetingId) {
		this.meetingId = meetingId;
	}
	public String getMeetingName() {
		if(meetingName == null) {
			return "";
		}
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	public String getStartTime() {
		if(startTime == null) 
			return "";
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public List<MMeetingNoteQuery> getListMeetingNoteQuery() {
		return listMeetingNoteQuery;
	}
	public void setListMeetingNoteQuery(List<MMeetingNoteQuery> listMeetingNoteQuery) {
		this.listMeetingNoteQuery = listMeetingNoteQuery;
	}
	public List<MMeetingPeople> getListMeetingPeople() {
		return listMeetingPeople;
	}
	public void setListMeetingPeople(List<MMeetingPeople> listMeetingPeople) {
		this.listMeetingPeople = listMeetingPeople;
	}
	
	

}
