package com.utils.file.downloader;

import java.util.HashMap;
import java.util.Map;

import com.tr.db.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FileService {
	private DBHelper openHelper;
	private SQLiteDatabase db;

	public FileService(Context context) {
		openHelper = DBHelper.getInstance(context);
		synchronized (openHelper) {
			db = openHelper.getWritableDatabase();
		}
	}

	/**
	 * 获取每条线程已经下载的文件长度
	 * 
	 * @param path
	 * @return
	 */
	public Map<Integer, Integer> getData(String path) {
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		synchronized (openHelper) {
			if (!db.isOpen()) {
				db = openHelper.getWritableDatabase();
			}
			Cursor cursor = db
					.rawQuery(
							"select threadid, downlength from filedownlog where downpath=?",
							new String[] { path });

			while (cursor.moveToNext()) {
				data.put(cursor.getInt(0), cursor.getInt(1));
			}

			cursor.close();
			db.close();
		}
		return data;
	}
	
	/**
	 * 获取已下载文件的大小
	 * @param path
	 * @return
	 */
	public int getDownLength(String path){
		int downlength = 0;
		Map<Integer, Integer> logdata = getData(path);
		if (logdata.size() > 0) {// 如果存在下载记录
			for (Map.Entry<Integer, Integer> entry : logdata.entrySet())
				downlength += entry.getValue();
		}
		return downlength;
	}

	/**
	 * 保存每条线程已经下载的文件长度
	 * 
	 * @param path
	 * @param map
	 */
	public void save(String path, Map<Integer, Integer> map) {// int threadid,
																// int position
		synchronized (openHelper) {
			if (!db.isOpen()) {
				db = openHelper.getWritableDatabase();
			}
			db.beginTransaction();

			try {
				for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
					db.execSQL(
							"insert into filedownlog(downpath, threadid, downlength) values(?,?,?)",
							new Object[] { path, entry.getKey(),
									entry.getValue() });
				}
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}

			db.close();
		}
	}

	/**
	 * 实时更新每条线程已经下载的文件长度
	 * 
	 * @param path
	 * @param map
	 */
	public void update(String path, Map<Integer, Integer> map) {
		synchronized (openHelper) {
			if (!db.isOpen()) {
				db = openHelper.getWritableDatabase();
			}
			db.beginTransaction();

			try {
				for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
					db.execSQL(
							"update filedownlog set downlength=? where downpath=? and threadid=?",
							new Object[] { entry.getValue(), path,
									entry.getKey() });
				}

				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}

			db.close();
		}
	}

	/**
	 * 当文件下载完成后，删除对应的下载记录
	 * 
	 * @param path
	 */
	public void delete(String path) {
		synchronized (openHelper) {
			if (!db.isOpen()) {
				db = openHelper.getWritableDatabase();
			}
			db.execSQL("delete from filedownlog where downpath=?",
					new Object[] { path });
			db.close();
		}
	}
}
