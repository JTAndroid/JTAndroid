package com.tr.model.im;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.tr.App;
import com.tr.model.BaseCacheUtils;
import com.tr.model.obj.ChatMessage;
import com.tr.model.obj.Connections;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.IMRecord;
import com.tr.model.obj.MUCDetail;
import com.tr.model.obj.MUCMessage;

/**
 * @ClassName: IMCacheUtils.java
 * @Description: 缓存im相关数据到磁盘中
 * @author xuxinjian
 * @version V1.0
 * @Date 2014-4-17 下午6:46:27
 */
public class IMCacheUtils extends BaseCacheUtils {
	
	public static final String PATH_IM_MUC_CACHE = SAVE_FILE_PATH
			+ SAVE_FILE_PATH_DIRECTORY_IM + "/muc/";
	public static final String PATH_IM_CHAT_CACHE = SAVE_FILE_PATH
			+ SAVE_FILE_PATH_DIRECTORY_IM + "/chat/";
	public static final String PATH_CONTACT_CACHE = SAVE_FILE_PATH
			+ SAVE_FILE_PATH_DIRECTORY_IM + "/Contact/";
	
	
	

	public static String getMucFileName(String mucID) {
		return App.getUserID() + "\\muc\\" + mucID;
	}
	
	//获取群聊详情文件保存的文件全路径
	public static String getMucDetailFileName(String mucID) {
		return App.getUserID() + "\\mucdetail\\" + mucID;
	}
	
	public static String getChatFileName(String mucID) {
		return App.getUserID() + "\\chat\\" + mucID;
	}
	
	public static String getRecFileName(String mucID) {
		return App.getUserID() + "\\imrecord\\" + mucID;
	}
	
	public static String getContactName( ) {
		return  "/contact/cs";
	}

	/**
	 * 读取指定会议id的会议的聊天记录
	 * 
	 * @param mucID
	 * @return 会议记录列表
	 */
	public static List<IMBaseMessage> readMUCMessageList(String mucID) {
		try{
			String filename = getMucFileName(mucID);
			List<IMBaseMessage> list = readObjectAppend(PATH_IM_MUC_CACHE, filename);
			if (list == null)
				list = new ArrayList<IMBaseMessage>();
			return list;
		}catch(Exception e){
			return  new ArrayList<IMBaseMessage>();
		}
	}

	/**
	 * 讲聊天记录列表写入文件中
	 * 
	 * @param name
	 * @param msgList
	 */
	public static void writeMUCMessageList(String mucID,
			List<IMBaseMessage> msgList) {
		try{
			String filename = getMucFileName(mucID);
			writeObjectListAppend(PATH_IM_MUC_CACHE, filename, msgList, false);
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 读取指定mucID的mucDetail
	 * 
	 * @param mucID
	 * @return MUCDetail
	 */
	public static MUCDetail readMUCDetail(String mucID) {
		try{
			String filename = getMucDetailFileName(mucID);
			MUCDetail detail = (MUCDetail) readObject(PATH_IM_MUC_CACHE, filename);
			return detail;
		}catch(Exception e){
			return  null;
		}
	}

	/**
	 * 讲群聊详情写入文件中
	 * 
	 * @param name
	 * @param detail
	 */
	public static void writeMUCDetail(MUCDetail detail) {
		try{
			String filename = getMucDetailFileName(detail.getId() + "");
			writeObject(PATH_IM_MUC_CACHE, filename, detail);
		}catch(Exception e){
			
		}
	}

	/**
	 * 讲聊天记录添加到文件尾部
	 * 
	 * @param mucID
	 * @param msgList
	 */
	public static void appendMUCMessageList(String mucID,
			List<IMBaseMessage> msgList) {
		try{
			String filename = getMucFileName(mucID);
			writeObjectListAppend(PATH_IM_MUC_CACHE, filename, msgList, true);
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 删除聊天记录
	 * 
	 * @param mucID
	 * @param msgList
	 */
	public static void deleteMUCMessageList(String mucID) {
		try{
			String filename = getMucFileName(mucID);
			deleteFileOld(PATH_IM_MUC_CACHE, filename);
		}catch(Exception e){
			
		}
	}

	/**
	 * 讲聊天记录列表写入文件中
	 * @param name
	 * @param msgList
	 */
	public static void writeChatMessageList(String chatID,
			List<IMBaseMessage> msgList) {
		String filename = getChatFileName(chatID);
		writeObjectListAppend(PATH_IM_CHAT_CACHE, filename, msgList, false);
	}

	
	
	/**
	 * 讲聊天记录添加到文件尾部
	 * @param chatID
	 * @param msgList
	 */
	public static void appendChatMessageList(String chatID,
			List<IMBaseMessage> msgList) {
		String filename = getChatFileName(chatID);
		writeObjectListAppend(PATH_IM_CHAT_CACHE, filename, msgList, true);

	}
	
	/**
	 * 删除聊天记录
	 * 
	 * @param chatID
	 */
	public static void deleteChatMessageList(String chatID) {
		try{
			String filename = getChatFileName(chatID);
			deleteFileOld(PATH_IM_CHAT_CACHE, filename);
		}catch(Exception e){
			
		}
	}
	

	/**
	 * 读取指定好友的聊天记录
	 * @param chatID
	 * @return 好友的聊天记录列表
	 */
	public static List<IMBaseMessage> readChatMessageList(String chatID) {
		String filename = getChatFileName(chatID);
		List<IMBaseMessage> list = readObjectAppend(PATH_IM_CHAT_CACHE, filename);
		if (list == null)
			list = new ArrayList<IMBaseMessage>();
		return list;
	}
	
	/**
	 * 私聊聊天记录文件是否存在
	 * @param chatID
	 * @return
	 */
	public static boolean isChatMessageCacheFileExist(String chatID){
		String path = checkFilePath(PATH_IM_CHAT_CACHE, getChatFileName(chatID));
		if(path != null){
			return new File(path).exists();
		}
		else{
			return false;
		}
	}
	
	/**
	 * 群聊聊天记录文件是否存在
	 * @param mucID
	 * @return
	 */
	public static boolean isMUCMessageCacheFileExist(String mucID){
		String path = checkFilePath(PATH_IM_MUC_CACHE, getMucFileName(mucID));
		if(path != null){
			return new File(path).exists();
		}
		else{
			return false;
		}
	}
	
	/**
	 * 讲畅聊记录列表写入文件中
	 * @param name
	 * @param msgList
	 */
	public static void writeIMRecordList(String recordID,
			List<IMRecord> msgList) {
		String filename = getRecFileName(recordID);
		writeObjectListAppend(PATH_IM_CHAT_CACHE, filename, msgList, false);
	}

	/**
	 * 讲聊天记录添加到文件尾部
	 * @param chatID
	 * @param msgList
	 */
	public static void appendIMRecordList(String recordID,
			List<IMRecord> msgList) {
		String filename = getRecFileName(recordID);
		writeObjectListAppend(PATH_IM_CHAT_CACHE, filename, msgList, true);

	}
	
//	/**
//	 * 保存关系列表文件
//	 * 
//	 * @param mucID
//	 * @param msgList
//	 */
//	public static String lock_connections = "lock_connections";
//	public static void saveContacts(List msgList,Context context) {
//		
//		String path=context.getApplicationContext().getFilesDir().getAbsolutePath()+"/contacts.data";
//		synchronized (lock_connections) {
//			try {
//				File dirFile = new File(path);
//				if (dirFile.exists()) {
//					dirFile.delete();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//			try {
//				FileOutputStream fo = new FileOutputStream(path);
//				ObjectOutputStream os = new ObjectOutputStream(fo);
//				for (Object object : msgList) {
//					os.writeObject(object);
//				}
//				os.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 读取关系列表
//	 */
//	public static List<Connections> readContacts(Context context) {
//		String path=context.getApplicationContext().getFilesDir().getAbsolutePath()+"/contacts.data";
//		
//		synchronized (lock_connections) {
//			ArrayList objectList = new ArrayList();
//			try {
//				File file=new File(path);
//				if(!file.exists()){
//					return objectList;
//				}
//				FileInputStream fin = new FileInputStream(file);
//				ObjectInputStream in = new ObjectInputStream(fin);
//				while (fin.available() > 0) {
//					objectList.add(in.readObject());
//				}
//				return objectList;
//			} catch (Exception e) {
//				e.printStackTrace();
//	
//				// deleteFileOld(path);
//				disposeException(path, objectList);
//			}
//		}
//		return null;
//	}
//	
//	/**
//	 * 删除
//	 * 
//	 * @param mucID
//	 * @param msgList
//	 */
//	public static void deleteContacts(Context context) {
//		String path=context.getApplicationContext().getFilesDir().getAbsolutePath()+"/contacts.data";
//		//String path=PATH_CONTACT_CACHE+"contacts.data";
//		synchronized (lock_connections) {
//			try {
//				File dirFile = new File(path);
//				if (dirFile.exists()) {
//					dirFile.delete();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	/**
	 * 读取指定好友的聊天记录
	 * @param chatID
	 * @return 好友的聊天记录列表
	 */
	public static List<IMRecord> readIMRecordList(String recordID) {
		String filename = getRecFileName(recordID);
		List<IMRecord> list = readObjectAppend(PATH_IM_CHAT_CACHE, filename);
		if (list == null)
			list = new ArrayList<IMRecord>();
		return list;
	}

	

}
