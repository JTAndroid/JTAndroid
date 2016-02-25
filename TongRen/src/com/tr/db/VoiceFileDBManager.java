package com.tr.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.tr.App;

/**
 * 语音文件管理器
 * @author leon
 * 
 */
public class VoiceFileDBManager {

	private final String TAG = getClass().getSimpleName();

	private DBHelper helper;
	private SQLiteDatabase db;

	public VoiceFileDBManager(Context context) {
		helper = DBHelper.getInstance(context);
		synchronized (helper) {
			db = helper.getWritableDatabase();
		}
	}

	// 插入记录
	public void insert(String url, long id) {

		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			// 开始事务
			db.beginTransaction(); 
			try {
				db.execSQL(
						"INSERT INTO " + DBHelper.TABLE_VOICE_FILE + " VALUES(?,?,?)",
						new Object[] { App.getUserID(), url, id});
				db.setTransactionSuccessful();
			} 
			catch (SQLException e) {
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			} 
			catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			} 
			finally {
				db.endTransaction(); 
				db.close();
			}
		}
	}
	

	// 删除记录
	public void delete(String url) {

		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("DELETE FROM " + DBHelper.TABLE_VOICE_FILE
						+ " WHERE " + DBHelper.COLUMN_VOICE_URL + " = ?", new String[] { url });
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
	
	// 删除记录
	public void delete(long id) {

		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("DELETE FROM " + DBHelper.TABLE_VOICE_FILE
						+ " WHERE " + DBHelper.COLUMN_VOICE_ID + " = ?",
						new Object[] { id });
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

	// 查询记录
	public long query(String url) {

		long id = -1;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c = queryTheCursor(url);
			try {
				while (c.moveToNext()) {
					id = c.getLong((c.getColumnIndex(DBHelper.COLUMN_VOICE_ID)));
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			} 
			finally {
				c.close();
			}
		}
		return id;
	}
	
	// 查询记录
	public String query(long id) {

		String url = null;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c = queryTheCursor(id);
			try {
				while (c.moveToNext()) {
					url = c.getString((c.getColumnIndex(DBHelper.COLUMN_VOICE_URL)));
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			} 
			finally {
				c.close();
			}
		}
		return url;
	}

	private Cursor queryTheCursor(String url) {

		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_VOICE_FILE
				+ " WHERE " + DBHelper.COLUMN_VOICE_URL + " = ?", new String[] { url });
		return c;
	}
	
	private Cursor queryTheCursor(long id) {

		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_VOICE_FILE
				+ " WHERE " + DBHelper.COLUMN_VOICE_ID + " = ?", new String[] { id + ""});
		return c;
	}
}
