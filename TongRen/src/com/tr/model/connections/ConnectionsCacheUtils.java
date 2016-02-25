package com.tr.model.connections;

import com.tr.App;
import com.tr.model.BaseCacheUtils;

public class ConnectionsCacheUtils extends BaseCacheUtils {

	// 联系人详情路径
	public static final String PATH_CONTACT_DETAILS_CACHE = SAVE_FILE_PATH
			+ SAVE_FILE_PATH_DIRECTORY_IM + "ContactDetails";
	
	
	public static String getConnObjFileName(int type, boolean isonline, String userId){
		
		
		return App.getUserID() + "\\" + type + "\\" + isonline + "\\" + userId;
	} 
	
	public static void writeConnectionObj(int type, boolean isonline, String userId, Object obj){
		
		String fileName = getConnObjFileName(type, isonline, userId);
		
		writeObject(PATH_CONTACT_DETAILS_CACHE, fileName, obj);
		
	}
	
	
	public static Object readConnectionObj(int type, boolean isonline, String userId){
		
		String fileName = getConnObjFileName(type, isonline, userId);
		return readObject(PATH_CONTACT_DETAILS_CACHE, fileName);
		
	}
	
}
