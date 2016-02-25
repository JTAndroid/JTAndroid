package com.tr.ui.organization.model.meet;

import java.io.Serializable;
import java.util.List;

import com.tr.ui.organization.model.meet.CustomerMeetingDetail;

public class CustomerMeet_Re  implements Serializable{

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	
	public long id;//主键id 客户id
	public long meetid;
	public List<CustomerMeetingDetail> meetingList;//会议列表
	
	

}
