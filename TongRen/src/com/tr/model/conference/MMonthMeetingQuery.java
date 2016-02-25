package com.tr.model.conference;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;

public class MMonthMeetingQuery implements Serializable{

	private static final long serialVersionUID = -5025377748834568470L;
	
	/**
		"month":"月份", [1-12]
        "listMeetingMiniQuery":[],
        "listMeetingMemberListQuery":[],
        "listMyCreateMeeting":[],
        "peopleCount":"人脉数量",
        "noteCount":"会议笔记数量",
        "requirementCount":"需求数量",
        "knowledgeCount":"知识数量 ",
        "attendAndCreateCount":"我创建和我参加会议数量",
        "createCount":"我创建数量"
	 */
	
	private List<MMeetingMiniQuery> listMeetingMiniQuery;
	
	private List<MMeetingMemberListQuery> listMeetingMemberListQuery;
	private List<MMeetingMemberListQuery> listMyCreateMeeting;
	
	private Integer month;
	private Integer peopleCount;
	private Integer noteCount;
	private Integer requirementCount;
	private Integer knowledgeCount;
	private Integer attendAndCreateCount;
	private Integer createCount;
	
	
	/**
	 * @param jsonObject
	 * @return MMonthMeetingQuery
	 */
	public static List<MMonthMeetingQuery> createFactory(JSONObject jsonObject) {
		MMyMeeting myMeeting = null;
		try {
			if (jsonObject != null) {
				Gson gson = new Gson();
				myMeeting = gson.fromJson(jsonObject.toString(), MMyMeeting.class);
			}

//			return  myMeeting.getListMonthMeetingQuery();

		} catch (Exception e) {
		}
		return null;
	}

	
	
	
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
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
	public List<MMeetingMiniQuery> getListMeetingMiniQuery() {
		return listMeetingMiniQuery;
	}
	public void setListMeetingMiniQuery(List<MMeetingMiniQuery> listMeetingMiniQuery) {
		this.listMeetingMiniQuery = listMeetingMiniQuery;
	}
	public List<MMeetingMemberListQuery> getListMeetingMemberListQuery() {
		return listMeetingMemberListQuery;
	}


	public void setListMeetingMemberListQuery(List<MMeetingMemberListQuery> listMeetingMemberListQuery) {
		this.listMeetingMemberListQuery = listMeetingMemberListQuery;
	}


	public List<MMeetingMemberListQuery> getListMyCreateMeeting() {
		return listMyCreateMeeting;
	}


	public void setListMyCreateMeeting(List<MMeetingMemberListQuery> listMyCreateMeeting) {
		this.listMyCreateMeeting = listMyCreateMeeting;
	}


	public Integer getPeopleCount() {
		return peopleCount;
	}


	public void setPeopleCount(Integer peopleCount) {
		this.peopleCount = peopleCount;
	}

	public Integer getNoteCount() {
		return noteCount;
	}


	public void setNoteCount(Integer noteCount) {
		this.noteCount = noteCount;
	}


	public Integer getAttendAndCreateCount() {
		return attendAndCreateCount;
	}


	public void setAttendAndCreateCount(Integer attendAndCreateCount) {
		this.attendAndCreateCount = attendAndCreateCount;
	}


	public Integer getCreateCount() {
		return createCount;
	}


	public void setCreateCount(Integer createCount) {
		this.createCount = createCount;
	}
	
	
}
