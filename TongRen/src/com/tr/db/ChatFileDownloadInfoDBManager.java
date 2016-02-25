package com.tr.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import com.tr.App;

/**
 * 畅聊文件下载信息
 * @author leon
 */
public class ChatFileDownloadInfoDBManager {

	private final String TAG = getClass().getSimpleName();
	
	private DBHelper helper;
	private SQLiteDatabase db;

	public ChatFileDownloadInfoDBManager(Context context) {
		helper = DBHelper.getInstance(context);
		synchronized (helper) {
			db = helper.getWritableDatabase();
		}
	}
	
	/**
	 * 插入数据
	 * @param uid
	 * @param url
	 * @param downloadedSize
	 * @param size
	 */
	public void insert(String uid, String url, long downloadedSize, long totalSize) {
		
		if(TextUtils.isEmpty(url)){
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("INSERT INTO " + DBHelper.TABLE_CHAT_FILE_DOWNLOAD_INFO + " VALUES(?,?,?,?)", 
						new Object[] { App.getUserID(), url, downloadedSize, totalSize });
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
	
	/**
	 * 更新数据库
	 * @param uid
	 * @param url
	 * @param downloadedSize
	 * @param size
	 */
	public void update(String uid, String url, long downloadedSize, long totalSize) {
		
		if(TextUtils.isEmpty(url)){
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction(); 
			try {
				db.execSQL("UPDATE " + DBHelper.TABLE_CHAT_FILE_DOWNLOAD_INFO
						+ " SET " + DBHelper.COLUMN_CHAT_FILE_DOWNLOADED_SIZE + " = ?, "
						+ DBHelper.COLUMN_CHAT_FILE_TOTAL_SIZE + " = ? WHERE "
						+ DBHelper.COLUMN_CHAT_FILE_URL + " = ? AND "
						+ DBHelper.COLUMN_UID + " = ?", 
						new Object[] {downloadedSize, totalSize, url, uid});
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
	
	/**
	 * 同步（存在则更新，否则插入）
	 * @param uid
	 * @param url
	 * @param downloadedSize
	 * @param size
	 */
	public void synchronous(String uid, String url, long downloadedSize, long totalSize) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		if(queryExistence(uid, url)){
			insert(uid, url, downloadedSize, totalSize);
		}
		else{
			update(uid, url, downloadedSize, totalSize);
		}
	}
	
	/**
	 * 删除记录
	 * @param uid
	 * @param url
	 */
	public void delete(String uid, String url){
		
		synchronized (helper) {
			if(!db.isOpen()){
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("DELETE FROM " + DBHelper.TABLE_CHAT_FILE_DOWNLOAD_INFO 
						+ " WHERE " + DBHelper.COLUMN_UID + " = ? AND " 
						+ DBHelper.COLUMN_CHAT_FILE_URL + " = ?", 
						new String[] { uid, url});
				db.setTransactionSuccessful();
			}
			catch(Exception e){
			}
			finally {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	
	
	/**
	 * 查询条目存在与否
	 * @param uid
	 * @param url
	 * @return
	 */
	public boolean queryExistence(String uid, String url) {
		
		boolean exist = false;
		synchronized(helper){
			if(!db.isOpen()){
				db = helper.getWritableDatabase();
			}
			Cursor c = queryTheCursor(uid, url);
			try {
				while (c.moveToNext()) {
					exist = true;
				}
			} 
			catch (Exception e) {
				Log.d(TAG, e.getMessage());
			} 
			finally {
				c.close();
			}
		}
		return exist;
	}
	
	/**
	 * 查询下载进度
	 * @param uid
	 * @param url
	 * @return
	 */
	public long query(String uid, String url) {
		
		long downloadedSize = 0;
		synchronized(helper){
			if(!db.isOpen()){
				db = helper.getWritableDatabase();
			}
			Cursor c = queryTheCursor(uid, url);
			try {
				while (c.moveToNext()) {
					downloadedSize = c.getLong(c.getColumnIndex(DBHelper.COLUMN_CHAT_FILE_DOWNLOADED_SIZE));
					break;
				}
			} 
			catch (Exception e) {
				Log.d(TAG, e.getMessage());
			} 
			finally {
				c.close();
			}
		}
		return downloadedSize;
	}

	// 查询游标位置
	private Cursor queryTheCursor(String uid, String url) {

		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_FILE_DOWNLOAD_INFO
				+ " WHERE " + DBHelper.COLUMN_UID + " = ? AND "
				+ DBHelper.COLUMN_CHAT_FILE_URL + " = ?", new String[] { uid, url });
		return c;
	}
	
}
