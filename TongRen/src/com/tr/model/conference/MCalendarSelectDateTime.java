package com.tr.model.conference;

import java.io.Serializable;

/**
 * 会议日期时间对象
 */
public class MCalendarSelectDateTime implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3587270832959191803L;
	/**
	 * 
	 */
//	public int position;
	public int year;
	public int month;
	public int day;
	public int weekIndex;
	
	public long startDate;
	public int startHour;
	public int startMinute;
	public String startTime;
	
	public long endDate;
	public int endHour;
	public int endMinute;
	public String endTime;
	//alter meet
	public boolean isAlterMeeting;
	public MMeetingTime alterMeetingTime;
//	public Long meetingId;
}
