package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.List;

import com.tr.ui.organization.model.meet.CustomerMeetingDetail;

/*
 * 查询会面情况列表（数据解析）
 */
public class MeetList implements Serializable{
	public List<CustomerMeetingDetail> meetList;
	public Long meetid;
	public boolean success;
}
