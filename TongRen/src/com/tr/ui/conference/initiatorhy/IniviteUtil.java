package com.tr.ui.conference.initiatorhy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.tr.model.conference.MCalendarSelectDateTime;
import com.tr.model.conference.MExpFriendContact;
import com.tr.model.conference.MMediaVideo;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTContactMini;
import com.tr.model.user.OrganizationMini;
import com.utils.string.StringUtils;
import com.utils.time.Util;


public class IniviteUtil {
    public final static int FILE_TYPE_NONE = 0;
    public final static int FILE_TYPE_IMAGE = 1;
    public final static int FILE_TYPE_AUDIO = 2;
    public final static int FILE_TYPE_VIDEO = 3;
	private static StringBuffer startTime;
	private static StringBuffer endTime;
	public static int isContainsNameCh(List<MExpFriendContact> dataList, int ch){
		if(Util.isNull(dataList)){
			return -1;
		}
		int index = 0;
		for(MExpFriendContact item : dataList){
			if(item.ch == ch){
				return index;
			}
			index++;
		}
		return -1;
	}
	public static List<MExpFriendContact> sortExpFriendContact(List<Connections> connectionsList, boolean isOnline){
		List<MExpFriendContact> dataList;
		if(Util.isNull(connectionsList)){
			return null;
		}
		dataList = new ArrayList<MExpFriendContact>();
		//A-65 Z-90 a-97 z-122 #-35
		for(Connections item : connectionsList){
			if(isOnline){
				if(!item.jtContactMini.isOnline){
					continue;
				}
			}else{
				if(!item.jtContactMini.isOffline){
					continue;
				}
			}
			char c = item.jtContactMini.nameChar;
			int ci, dff;
			dff = 'a' - 'A';
			if(c >= 'a' && c < ('z' + 1)){
				ci = c - dff; 
			}else if(c >= 'A' && c < ('Z' + 1)){
				ci = c;
			}else{
				ci = 1001;//'#';
			}
			int index = isContainsNameCh(dataList, ci);
			if(index > -1){
				dataList.get(index).contactList.add(item.jtContactMini);
			}else{
				MExpFriendContact efc = new MExpFriendContact();
				efc.ch = ci;
				if(ci == 1001){
					efc.nameCh = "#";
				}else{
					efc.nameCh = String.valueOf((char)ci);
				}
				efc.contactList = new ArrayList<JTContactMini>();
				efc.contactList.add(item.jtContactMini);
				dataList.add(efc);
			}
		}
		Collections.sort(dataList, new Comparator<MExpFriendContact>() {
			@Override
			public int compare(MExpFriendContact lhs, MExpFriendContact rhs) {
				// TODO Auto-generated method stub
				if(lhs.ch > rhs.ch){
					return 1;
				}else if(lhs.ch < rhs.ch){
					return -1;
				}else{
					return 0;
				}
				
			}});
		return dataList;
	}
	
	/**
	 * 对好友进行排序
	 * @param connectionsList
	 * @author leon
	 * @return
	 */
	public static List<MExpFriendContact> sortExpFriendContact(List<Connections> connectionsList){
		List<MExpFriendContact> dataList;
		if(Util.isNull(connectionsList)){
			return null;
		}
		dataList = new ArrayList<MExpFriendContact>();
		//A-65 Z-90 a-97 z-122 #-35
		for(Connections item : connectionsList){		
			char c = (item.type == Connections.type_persion ? item.jtContactMini.nameChar : item.organizationMini.nameChar);
			int ci, dff;
			dff = 'a' - 'A';
			if(c >= 'a' && c < ('z' + 1)){
				ci = c - dff; 
			}else if(c >= 'A' && c < ('Z' + 1)){
				ci = c;
			}else{
				ci = 1001;//'#';
			}
			int index = isContainsNameCh(dataList, ci);
			if(index > -1){
				if (item.type == Connections.type_persion) {
					dataList.get(index).contactList.add(item.jtContactMini);
				}
				else if (item.type == Connections.type_org) {
					dataList.get(index).organizationList.add(item.organizationMini);
				}
			}else{
				MExpFriendContact efc = new MExpFriendContact();
				efc.ch = ci;
				if(ci == 1001){
					efc.nameCh = "#";
				}else{
					efc.nameCh = String.valueOf((char)ci);
				}
				if (item.type == Connections.type_persion) {
					efc.contactList = new ArrayList<JTContactMini>();
					efc.contactList.add(item.jtContactMini);
				}else if (item.type == Connections.type_org) {
					efc.organizationList = new ArrayList<OrganizationMini>();
					efc.organizationList.add(item.organizationMini);
				}
				dataList.add(efc);
			}
		}
		Collections.sort(dataList, new Comparator<MExpFriendContact>() {
			@Override
			public int compare(MExpFriendContact lhs, MExpFriendContact rhs) {
				// TODO Auto-generated method stub
				if(lhs.ch > rhs.ch){
					return 1;
				}else if(lhs.ch < rhs.ch){
					return -1;
				}else{
					return 0;
				}
				
			}});
		return dataList;
	}
	
	public static List<Map.Entry<String, JTContactMini>> sortMapByFriendNameFisrtLetter(Map<String, JTContactMini> srcMap){
		if(Util.isNull(srcMap)){
			return null;
		}
		try {
			List<Map.Entry<String, JTContactMini>> tempList =
				    new ArrayList<Map.Entry<String, JTContactMini>>(srcMap.entrySet());
			Collections.sort(tempList, new Comparator<Map.Entry<String, JTContactMini>>() {   
			    public int compare(Map.Entry<String, JTContactMini> o1, Map.Entry<String, JTContactMini> o2) {
			    	char c1 = o1.getValue().getNameChar();
			    	char c2 = o2.getValue().getNameChar();
			        return (c1 - c2); 
			        //return (o1.getKey()).toString().compareTo(o2.getKey());
			    }
			}); 
			return tempList;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public static String format(List<JTContactMini> dataList, String style){
		if(Util.isNull(dataList) || Util.isNull(style)){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(JTContactMini item : dataList){
			if(i == dataList.size() - 1){
				sb.append(item.name);
			}else{
				sb.append(item.name + style);
			}
			i++;
		}
		return sb.toString();
	}
	public static String format(Map<String, JTContactMini> dataMap, String style){
		if(Util.isNull(dataMap) || Util.isNull(style)){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int i = 0;
		Iterator<Entry<String,JTContactMini>> iterPHM = dataMap.entrySet().iterator();  
		while (iterPHM.hasNext()) {  
		    Map.Entry entry = (Map.Entry) iterPHM.next();
		    JTContactMini item = (JTContactMini) entry.getValue();
		    
			if(i == dataMap.size() - 1){
				sb.append(item.name);
			}else{
				sb.append(item.name + style);
			}
			i++;
		}
		return sb.toString();
	}
	public static String formatOrg(Map<String, OrganizationMini> dataMap, String style){
		if(Util.isNull(dataMap) || Util.isNull(style)){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int i = 0;
		Iterator<Entry<String,OrganizationMini>> iterPHM = dataMap.entrySet().iterator();  
		while (iterPHM.hasNext()) {  
			Map.Entry entry = (Map.Entry) iterPHM.next();
			OrganizationMini item = (OrganizationMini) entry.getValue();
			String name =StringUtils.isEmpty(item.shortName)?item.getFullName():item.getShortName();
			if(i == dataMap.size() - 1){
				sb.append(name);
			}else{
				sb.append(name + style);
			}
			i++;
		}
		return sb.toString();
	}
	public static long getSelectDTMillis(MCalendarSelectDateTime sdt){
		Calendar ci = Calendar.getInstance();
		ci.set(sdt.year, sdt.month, sdt.day);
		long theMs = ci.getTimeInMillis();
		return theMs;
	}
	public static MCalendarSelectDateTime getFormatDTMillis(MCalendarSelectDateTime sdt){
		Calendar ci = Calendar.getInstance();
		ci.set(sdt.year, sdt.month - 1, sdt.day, sdt.startHour, sdt.startMinute);
		sdt.startDate = ci.getTimeInMillis();
		ci.set(sdt.year, sdt.month - 1, sdt.day, sdt.endHour, sdt.endMinute);
		sdt.endDate = ci.getTimeInMillis();
		return sdt;
	}
	public static MCalendarSelectDateTime getFormatDTMillis(MCalendarSelectDateTime sdt, int isStart){
		Calendar ci = Calendar.getInstance();
		if(isStart == 0){
			ci.set(sdt.year, sdt.month - 1, sdt.day, sdt.startHour, sdt.startMinute);
			sdt.startDate = ci.getTimeInMillis();
		}else if(isStart == 1){
			ci.set(sdt.year, sdt.month - 1, sdt.day, sdt.endHour, sdt.endMinute);
			sdt.endDate = ci.getTimeInMillis();
		}else{
			ci.set(sdt.year, sdt.month - 1, sdt.day, sdt.startHour, sdt.startMinute);
			sdt.startDate = ci.getTimeInMillis();
			ci.set(sdt.year, sdt.month - 1, sdt.day, sdt.endHour, sdt.endMinute);
			sdt.endDate = ci.getTimeInMillis();
		}
		return sdt;
	}
	public static String formatDate(MCalendarSelectDateTime dateTime){
		if(dateTime == null){
			return "";
		}
		return dateTime.year + "年" + dateTime.month + "月" + dateTime.day + "日" 
				+ " " + Util.week[dateTime.weekIndex] 
				+ " " + dateTime.startTime + "-" + dateTime.endTime;
	}
	/**
	 * 对主讲人时间进行修改
	 * 
	 */
	public static String formatDateMeeting(MCalendarSelectDateTime dateTime){
		if(dateTime == null){
			return "";
		}
		dateTime.startTime = dateTime.startHour + ":" + dateTime.startMinute;
		if ((dateTime.startHour + "").length() == 1) {
			dateTime.startTime = "0" + dateTime.startHour + ":"
					+ dateTime.startMinute;
 		}
		if ((dateTime.startMinute + "").length() == 1) {
			dateTime.startTime = dateTime.startHour + ":" + "0"
					+ dateTime.startMinute;
 		}
		if ((dateTime.startHour + "").length() == 1&& (dateTime.startMinute + "").length() == 1) {
			dateTime.startTime = "0" + dateTime.startHour + ":" + "0"
					+ dateTime.startMinute;
		}
		
		dateTime.endTime = dateTime.endHour + ":" + dateTime.endMinute;

		if ((dateTime.endHour + "").length() == 1) {
			dateTime.endTime = "0" + dateTime.endHour + ":" + dateTime.endMinute;
		}
		if ((dateTime.endMinute + "").length() == 1) {
			dateTime.endTime = dateTime.endHour + ":" + "0" + dateTime.endMinute;
		}
		if ((dateTime.endHour + "").length() == 1
				&& (dateTime.endMinute + "").length() == 1) {
			dateTime.endTime = "0" + dateTime.endHour + ":" + "0"
					+ dateTime.endMinute;
		}
		if (0 <= dateTime.endHour && dateTime.endHour < 2) {
			dateTime.endTime = 23 + ":" + 59;
		}
		return dateTime.year + "年" + dateTime.month + "月" + dateTime.day + "日" 
				+ " " + Util.week[dateTime.weekIndex] 
				+ " " + dateTime.startTime + "-" + dateTime.endTime;
		
	}
	public static MMediaVideo getMediaVideoInfo(Context context, Uri uri){
		if(uri == null){
			return null;
		}
		Cursor cursor = null, thumbCursor = null ;
		MMediaVideo mv = null;
		try {
//        	MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//			clumon: [_id, _data, _display_name, _size, mime_type, date_added, date_modified, title, duration, artist, album, 
//					resolution, description, isprivate, tags, category, language, mini_thumb_data, latitude, longitude, datetaken, 
//					mini_thumb_magic, bucket_id, bucket_display_name, bookmark, width, height, resumePos, isPlayed, sync_time, subtitle_lan, title_pinyin]
        	
        	String[] proj = { MediaStore.Video.Media.DATA};
        	//uri: content://media/external/video/media/222630
            String[] mediaColumns = new String[]{
            		MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.TITLE,
                    MediaStore.Video.Media.MIME_TYPE};
        	cursor = context.getContentResolver().query(uri, mediaColumns, null, null, null);
        	if(cursor != null && cursor.moveToFirst()){
        		mv = new MMediaVideo();
        		mv.id = cursor.getLong(0);
        		mv.path = cursor.getString(1);
        		mv.title = cursor.getString(2);
        		mv.mimeType = cursor.getString(3); 
        		
//        		String[] thumbColumns = new String[]{
//            			MediaStore.Video.Thumbnails.VIDEO_ID,
//                        MediaStore.Video.Thumbnails.DATA};  
//        		thumbCursor = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, 
//        				MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id, selectionArgs, null);
//        		thumbCursor = context.managedQuery(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id, selectionArgs, null);
        		
//        		if(thumbCursor != null && thumbCursor.moveToFirst()){
//        			mv.thumbPath = cursor.getString(1);
//        		}
        	}
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }finally{
        	if(cursor != null){
        		cursor.close();
        	}
        	if(thumbCursor != null){
        		thumbCursor.close();
        	}
        }
		return mv;
	}
	public static int getFriendContactMatchIndex(List<JTContactMini> dataList, long id){
		if(Util.isNull(dataList)){
			return -1;
		}
		int i = 0;
		for(JTContactMini item : dataList){
			long iid = Long.valueOf(item.id);
			if(id == iid){
				return i;
			}
			i++;
		}
		return -1;
	}
	public static int getFriendContactMatchIndex(Map<String, JTContactMini> dataMap, long id){
		if(Util.isNull(dataMap)){
			return -1;
		}
		int i = 0;
		Iterator<Entry<String,JTContactMini>> iter = dataMap.entrySet().iterator();  
		while (iter.hasNext()) {  
		    Map.Entry entry = (Map.Entry) iter.next();
		    JTContactMini item = (JTContactMini) entry.getValue();
			long iid = Long.valueOf(item.id);
			if(id == iid){
				return i;
			}
			i++;
		}
		return -1;
	}
	public static int getFileType(String suffixName){
	    if(Util.isNull(suffixName)){
	        return FILE_TYPE_NONE;
	    }
	    String low = suffixName.toLowerCase();
	    if(low.equals("jpg") || low.equals("png") || low.equals("gif") || low.equals("bmp") || low.equals("jpeg")){
	        return FILE_TYPE_IMAGE;
	    }else if(low.equals("mp4") || low.equals("3gp")){
            return FILE_TYPE_VIDEO;
        }else if(low.equals("amr")){
            return FILE_TYPE_AUDIO;
        }
	    return FILE_TYPE_NONE;
	}
	
	public static MCalendarSelectDateTime getMSDT(MMeetingTopicQuery mMeetingTopicQuery) {
		MCalendarSelectDateTime sdt = new MCalendarSelectDateTime();
		Date d1 = null, d2 = null;
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ("yyyy-MM-dd HH:mm:ss");
		try {
			d1 = dateFormat.parse(mMeetingTopicQuery.getTopicStartTime());
			d2 = dateFormat.parse(mMeetingTopicQuery.getTopicEndTime());
			sdt.startDate = d1.getTime();
			sdt.endDate = d2.getTime();
			c.setTimeInMillis(sdt.startDate);
			sdt.year = c.get(Calendar.YEAR);
			sdt.month = c.get(Calendar.MONTH) + 1;
			sdt.day = c.get(Calendar.DAY_OF_MONTH);
			sdt.weekIndex = c.get(Calendar.WEEK_OF_MONTH);
			sdt.startHour = c.get(Calendar.HOUR_OF_DAY);
			sdt.startMinute = c.get(Calendar.MINUTE);
			sdt.startTime = sdt.startHour + ":" + sdt.startMinute;
			c.setTimeInMillis(sdt.endDate);
			sdt.endHour = c.get(Calendar.HOUR_OF_DAY);
			sdt.endMinute = c.get(Calendar.MINUTE);
			sdt.endTime = sdt.endHour + ":" + sdt.endMinute;
		} catch (Exception e) {
			return null;
		}
		return sdt;
	}
}
