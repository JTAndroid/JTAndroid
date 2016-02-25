package com.tr.db;

import com.tr.AppData;
import com.tr.model.user.JTMember;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*
 * 存储用户数据
 */
public class AppDataDBManager {

	// 常量
	private final String TAG = getClass().getSimpleName();

	// 变量
	private DBHelper helper;
	private SQLiteDatabase db;

	public AppDataDBManager(Context context) {
		helper = DBHelper.getInstance(context);
		synchronized (helper) {
			db = helper.getWritableDatabase();
		}
	}

	// 插入
	private void insert(String key_id, AppData app_data) {

		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction(); // 开始事务
			try {
				db.execSQL("INSERT INTO " + DBHelper.TABLE_APP_DATA + " VALUES(?,?)",
						new Object[] { key_id, DBHelper.objectToBytes(app_data) });
				db.setTransactionSuccessful(); // 设置事务成功完成
			} catch (SQLException e) {
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			} finally {
				db.endTransaction(); // 结束事务
				db.close();
			}
		}
	}

	// 更新
	private void update(String key_id, AppData app_data) {

		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction(); // 开始事务
			try {
				db.execSQL("UPDATE " + DBHelper.TABLE_APP_DATA + " SET app_data = ? WHERE key_id = ?", new Object[] {
						DBHelper.objectToBytes(app_data), key_id });
				db.setTransactionSuccessful(); // 设置事务成功完成
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction(); // 结束事务
				db.close();
			}
		}
	}

	// 同步
	public void synchronous(String key_id, AppData app_data) {
		if (app_data == null) {
			return;
		}
		if (query(key_id) == null) {
			insert(key_id, app_data);
		} else {
			update(key_id, app_data);
		}
	}

	// 删除数据
	public void delete(String key_id) {

		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("DELETE FROM " + DBHelper.TABLE_APP_DATA + " WHERE key_id = ?", new String[] { key_id });
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
	}

	// 查询
	public AppData query(String key_id) {

		AppData app_data = null;
		synchronized (helper) {
			if (!db.isOpen()) {
				
				db = helper.getWritableDatabase();
			}
			Cursor c = queryTheCursor(key_id);
			try {
				while (c.moveToNext()) {
					byte[] item = c.getBlob((c.getColumnIndex("app_data")));
					app_data = (AppData) DBHelper.bytesToObject(item);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return app_data;
	}

	private Cursor queryTheCursor(String key_id) {

		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_APP_DATA + " WHERE key_id = ?",
				new String[] { key_id });
		return c;
	}
}
