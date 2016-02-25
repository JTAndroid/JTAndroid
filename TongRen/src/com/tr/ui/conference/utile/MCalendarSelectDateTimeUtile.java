package com.tr.ui.conference.utile;

import com.tr.model.conference.MCalendarSelectDateTime;

public class MCalendarSelectDateTimeUtile {
	
	
	public static String toCalendarDataString(MCalendarSelectDateTime calendarSelectDateTime){
		StringBuilder dateSb = new StringBuilder();
		//	年
		dateSb.append(calendarSelectDateTime.year);
		//	月
		String vMonth= "0" + calendarSelectDateTime.month;
		vMonth=vMonth.substring(vMonth.length()-2, vMonth.length());
		dateSb.append(vMonth);
		//	日
		String vDay = "0" + calendarSelectDateTime.day;
		vDay=vDay.substring(vDay.length()-2, vDay.length());
		dateSb.append(vDay);
		
		return dateSb.toString();
	}
	

}
