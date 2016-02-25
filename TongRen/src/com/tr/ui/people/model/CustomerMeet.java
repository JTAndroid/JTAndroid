package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tr.ui.organization.model.meet.CustomerMeetingDetail;

public class CustomerMeet  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;//主键id 客户id
	private List<CustomerMeetingDetail> meetingList;//会议列表
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<CustomerMeetingDetail> getMeetingList() {
		if(meetingList == null){
			return new ArrayList<CustomerMeetingDetail>();
		}else{
			return meetingList;
		}
	}
	public void setMeetingList(List<CustomerMeetingDetail> meetingList) {
		if(meetingList == null){
			this.meetingList = new ArrayList<CustomerMeetingDetail>();
		}else{
			this.meetingList = meetingList;
		}
	}
	
	

}
