package com.tr.model.obj;

import java.util.ArrayList;

import org.json.JSONObject;

import com.tr.model.page.IPageBaseItem;

public class SearchResultList implements IPageBaseItem {

	private static final long serialVersionUID = -1420500329392831938L;
	private ArrayList<SearchResult> organList;
	private ArrayList<SearchResult> personList;
	private ArrayList<SearchResult> demandList;
	private ArrayList<SearchResult> meetingList;
	private ArrayList<SearchResult> knowledgeList;

	public ArrayList<SearchResult> getOrganList() {
		return organList;
	}

	public void setOrganList(ArrayList<SearchResult> organList) {
		this.organList = organList;
	}

	public ArrayList<SearchResult> getPersonList() {
		return personList;
	}

	public void setPersonList(ArrayList<SearchResult> personList) {
		this.personList = personList;
	}

	public ArrayList<SearchResult> getDemandList() {
		return demandList;
	}

	public void setDemandList(ArrayList<SearchResult> demandList) {
		this.demandList = demandList;
	}

	public ArrayList<SearchResult> getMeetingList() {
		return meetingList;
	}

	public void setMeetingList(ArrayList<SearchResult> meetingList) {
		this.meetingList = meetingList;
	}

	public ArrayList<SearchResult> getKnowledgeList() {
		return knowledgeList;
	}

	public void setKnowledgeList(ArrayList<SearchResult> knowledgeList) {
		this.knowledgeList = knowledgeList;
	}

}