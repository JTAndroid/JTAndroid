package com.tr.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

/**
 * 畅聊本地文件数据库管理
 * @author leon
 */
public class ChatLocalFileDBManager {
	
	private final String TAG = getClass().getSimpleName();
	
	private DBHelper helper;
	private SQLiteDatabase db;

	/**
	 * 构造函数
	 * @param context
	 */
	public ChatLocalFileDBManager(Context context) {
		helper = DBHelper.getInstance(context);
		synchronized (helper) {
			db = helper.getWritableDatabase();
		}
	}
	
	/**
	 * 插入单条记录
	 * @param uid
	 * @param msgId
	 * @param filePath
	 */
	private void insert(String uid, String msgId, String filePath) {
		
		if(TextUtils.isEmpty(uid)
				|| TextUtils.isEmpty(msgId)
				|| TextUtils.isEmpty(filePath)){
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			// 开始事务
			db.beginTransaction(); 
			try {
				db.execSQL("INSERT INTO " + DBHelper.TABLE_CHAT_LOCAL_FILE + " VALUES(?,?,?)", 
						new String[] { uid, msgId, filePath});
				db.setTransactionSuccessful();
			} 
			catch (Exception e) {
				e.printStackTrace();
			} 
			finally {
				db.endTransaction(); 
				db.close();
			}
		}
	}
	
	// 查找指定消息是否存在
	private boolean queryExistence(String uid, String msgId){
		boolean exist = false;
		if(TextUtils.isEmpty(uid)
				|| TextUtils.isEmpty(msgId)){
			return exist;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction(); // 开始事务
			try {
				Cursor c = queryTheCursor(uid, msgId);
				while(c.moveToNext()){
					exist = true;
				}
			} 
			catch (Exception e) {
				Log.d(TAG, e.getMessage());
			} 
			finally {
				db.endTransaction();
				db.close();
			}
		}
		return exist;
	}
	
	/**
	 * 更新单条记录
	 * @param uid
	 * @param msgId
	 * @param filePath
	 */
	public void update(String uid, String msgId, String filePath){
		
		if(TextUtils.isEmpty(uid)
				|| TextUtils.isEmpty(msgId)
				|| TextUtils.isEmpty(filePath)){
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction(); 
			if(!queryExistence(uid, msgId)){
				insert(uid, msgId, filePath);
			}
			else{
				try {
					db.execSQL("UPDATE " + DBHelper.TABLE_CHAT_LOCAL_FILE + " SET "
							+ DBHelper.COLUMN_CHAT_LOCAL_FILE_PATH + " = ? WHERE "
							+ DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ? AND "
							+ DBHelper.COLUMN_UID + " = ?",
							new Object[] { filePath, msgId, uid});
					db.setTransactionSuccessful(); 
				} 
				catch (Exception e) {
					Log.d(TAG, e.getMessage());
				} 
				finally {
					db.endTransaction(); 
					db.close();
				}
			}
		}
	}
	
	/**
	 * 同步数据库
	 * @param uid
	 * @param msgId
	 * @param filePath
	 */
	public void synchronous(String uid, String msgId, String filePath){
		
		if(TextUtils.isEmpty(uid)
				|| TextUtils.isEmpty(msgId)
				|| TextUtils.isEmpty(filePath)){
			return;
		}
		// 删除存在的条目
		if (!queryExistence(uid, msgId)) { 
			insert(uid, msgId, filePath); // 插入
		}
		else{
			update(uid, msgId, filePath); // 更新
		}
	}
	
	/**
	 * 删除单条记录
	 * @param uid
	 * @param msgId
	 */
	public void delete(String uid, String msgId){
		
		synchronized (helper) {
			if(!db.isOpen()){
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("DELETE FROM " + DBHelper.TABLE_CHAT_LOCAL_FILE
						+ " WHERE " + DBHelper.COLUMN_UID + " = ? AND "
						+ DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ?",
						new String[] { uid, msgId });
				db.setTransactionSuccessful();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	/**
	 * 返回文件本地地址
	 * @param uid
	 * @param msgId
	 * @return
	 */
	public String query(String uid, String msgId) {
		String path = null;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c = queryTheCursor(uid, msgId);
			try {
				while (c.moveToNext()) {
					path = c.getString(c.getColumnIndex(DBHelper.COLUMN_CHAT_LOCAL_FILE_PATH));
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			} 
			finally {
				c.close();
			}
		}
		return path;
	}

	
	// 获取指向指定记录的Cursor
	private Cursor queryTheCursor(String uid, String msgId) {
		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_LOCAL_FILE
				+ " WHERE " + DBHelper.COLUMN_UID + " = ? AND " 
				+ DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ?", 
				new String[] { uid, msgId});
		return c;
	}
}
