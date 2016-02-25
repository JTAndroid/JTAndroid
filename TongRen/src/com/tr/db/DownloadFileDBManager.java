package com.tr.db;

import java.util.ArrayList;
import java.util.List;

import com.tr.model.obj.JTFile;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 用户已下载的文件列表（包括下载完成和下载中断的）
 * @author leon
 */
public class DownloadFileDBManager {
	
	private final String TAG = getClass().getSimpleName();
	
	private DBHelper helper;
	private SQLiteDatabase db;

	public DownloadFileDBManager(Context context) {
		helper = DBHelper.getInstance(context);
		synchronized (helper) {
			db = helper.getWritableDatabase();
		}
	}
	
	// 插入
	private void insert(String uid, JTFile jtfile) {
		
		if(jtfile == null || jtfile.mUrl == null){
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction(); // 开始事务
			try {
				db.execSQL("INSERT INTO " + DBHelper.TABLE_DOWNLOAD_FILE + " VALUES(?,?,?)", 
						new Object[] { jtfile.mUrl, uid, DBHelper.objectToBytes(jtfile) });
				db.setTransactionSuccessful(); // 设置事务成功完成
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
				db.endTransaction(); // 结束事务
				db.close();
			}
		}
	}
	
	// 更新
	private void update(String uid, JTFile jtfile) {
		
		if (jtfile == null || jtfile.mUrl == null) {
			Log.d(TAG, "jtfile 包含的信息无效，更新数据库失败");
			return;
		}
		
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction(); // 开始事务
			try {
				db.execSQL("UPDATE " + DBHelper.TABLE_DOWNLOAD_FILE
						+ " SET jtfile = ? WHERE uid = ? AND url = ?", 
						new Object[] {DBHelper.objectToBytes(jtfile), uid , jtfile.mUrl});
				db.setTransactionSuccessful(); // 设置事务成功完成
			} 
			catch (SQLException e) {
				e.printStackTrace();
			} 
			catch (Exception e) {
				e.printStackTrace();
			} 
			finally {
				db.endTransaction(); // 结束事务
				db.close();
			}
		}
	}
	
	// 同步（存在则更新，否则插入）
	public void synchronous(String uid, JTFile jtfile) {
		if (jtfile == null || jtfile.mUrl == null) {
			return;
		}
		if(query(uid, jtfile.mUrl) == null){
			insert(uid, jtfile);
		}
		else{
			update(uid, jtfile);
		}
	}
	
	// 删除
	public void delete(String uid,String url){
		
		synchronized (helper) {
			if(!db.isOpen()){
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("DELETE FROM " + DBHelper.TABLE_DOWNLOAD_FILE + " WHERE uid = ? and url = ?", 
						new String[] { uid, url});
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
	
	// 查询
	public JTFile query(String uid,String url) {
		
		JTFile jtfile = null;
		synchronized(helper){
			if(!db.isOpen()){
				db = helper.getWritableDatabase();
			}
			Cursor c = queryTheCursor(uid, url);
			try {
				while (c.moveToNext()) {
					byte[] item = c.getBlob((c.getColumnIndex("jtfile")));
					jtfile = (JTFile) DBHelper.bytesToObject(item);
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			} 
			finally {
				c.close();
			}
		}
		return jtfile;
	}
	
	/**
	 * 获取所有的条目
	 * @param uid
	 * @return
	 */
	public List<JTFile> query(String uid) {
		
		List<JTFile> listJTfile = null;
		synchronized(helper){
			if(!db.isOpen()){
				db = helper.getWritableDatabase();
			}
			Cursor c = queryTheCursor(uid);
			try {
				while (c.moveToNext()) {
					if(listJTfile == null){
						listJTfile = new ArrayList<JTFile>();
					}
					byte[] item = c.getBlob((c.getColumnIndex("jtfile")));
					listJTfile.add((JTFile) DBHelper.bytesToObject(item));
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			} 
			finally {
				c.close();
			}
		}
		return listJTfile;
	}
	
	private Cursor queryTheCursor(String uid) {

		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_DOWNLOAD_FILE
				+ " WHERE uid = ?", new String[] { uid });
		return c;
	}
	
	private Cursor queryTheCursor(String uid, String url) {

		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_DOWNLOAD_FILE
				+ " WHERE uid = ? AND url = ?", new String[] { uid, url });
		return c;
	}
	
	/*
	private Cursor queryTheCursor(String... params) {
		
		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_DOWNLOAD_FILE + " WHERE uid = ? AND url = ?", 
				new String[]{ params[0] , params[1]});		
		return c;
	}
	*/
}
