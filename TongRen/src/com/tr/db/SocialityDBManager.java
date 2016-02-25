package com.tr.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.tr.App;
import com.tr.AppData;
import com.tr.model.conference.MSociality;
import com.tr.model.conference.MSocialityDetail;
import com.utils.common.SocialType;
import com.utils.common.Util;
import com.utils.time.TimeUtil;

/** @author sunjianan */
public class SocialityDBManager {

	// 常量
	private final String TAG = getClass().getSimpleName();

	// 变量
	private DBHelper helper;
	private SQLiteDatabase db;

	private Cursor allSocialityCursor;

	private SocialityDBManager(Context context) {
		helper = DBHelper.getInstance(context);
		synchronized (helper) {
			db = helper.getWritableDatabase();
			helper.createSOCIALITY_DATA2(db);
		}
	}

	/**
	 * 获取SocialityDBManager的实例
	 * 
	 * @param context
	 * @return
	 */
	public synchronized static SocialityDBManager getInstance(Context context) {
		return new SocialityDBManager(context);
	}

	/** 关闭dbhelper */
	public void closeDbHelper() {
		db.close();
	}

	/**
	 * 插入数据
	 * 
	 * @param uid
	 * @param url
	 * @param downloadedSize
	 * @param size
	 */
	public synchronized void insert(String userId, String _id, int type,
			MSociality sociality, int isdelete) {

		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("INSERT INTO " + DBHelper.TABLE_SOCIALITY_DATA
						+ " VALUES(?,?,?, ?,?)", new Object[] { userId, _id,
						type, DBHelper.objectToBytes(sociality), isdelete });
				db.setTransactionSuccessful();
			} catch (Exception e) {
				Log.d(TAG, e.getMessage());
			} finally {
				db.endTransaction();
				// db.close();
			}
		}
	}

	/**
	 * 
	 */

	/**
	 * @param _id
	 * @param type
	 * @param sociality
	 * @param isdelete
	 */
	public synchronized void update(String userId, String _id, int type,
			MSociality sociality, int isdelete) {

		// 社交数据库
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				if (type == 3 || type == 4 || type == 5) {
					db.execSQL("update " + DBHelper.TABLE_SOCIALITY_DATA
							+ " set " + DBHelper.COLUMN_SOCIATILY_OBJECT
							+ "= ?," + DBHelper.COLUMN_SOCIATILY_TYPE + "= ?,"
							+ DBHelper.COLUMN_SOCIATILY_ISDELETE
							+ "= ?  where " + DBHelper.COLUMN_SOCIATILY_USERID
							+ "= ? and " + DBHelper.COLUMN_SOCIATILY_ID
							+ " = ? and (" + DBHelper.COLUMN_SOCIATILY_TYPE
							+ "= 3 or " + DBHelper.COLUMN_SOCIATILY_TYPE
							+ " = 4 or " + DBHelper.COLUMN_SOCIATILY_TYPE
							+ " = 5" + ")",
							new Object[] { DBHelper.objectToBytes(sociality),
									type, isdelete, userId, _id });
				} else if (type == 6 || type == 7) {

					db.execSQL("update " + DBHelper.TABLE_SOCIALITY_DATA
							+ " set " + DBHelper.COLUMN_SOCIATILY_OBJECT
							+ "= ?," + DBHelper.COLUMN_SOCIATILY_ISDELETE
							+ "= ?  where " + DBHelper.COLUMN_SOCIATILY_USERID
							+ "= ? and " + DBHelper.COLUMN_SOCIATILY_TYPE
							+ "= ?",
							new Object[] { DBHelper.objectToBytes(sociality),
									isdelete, userId, type });

				} else {
					db.execSQL("update " + DBHelper.TABLE_SOCIALITY_DATA
							+ " set " + DBHelper.COLUMN_SOCIATILY_OBJECT
							+ "= ?," + DBHelper.COLUMN_SOCIATILY_ISDELETE
							+ "= ?  where " + DBHelper.COLUMN_SOCIATILY_USERID
							+ "= ? and " + DBHelper.COLUMN_SOCIATILY_ID
							+ " = ? and " + DBHelper.COLUMN_SOCIATILY_TYPE
							+ "= ?",
							new Object[] { DBHelper.objectToBytes(sociality),
									isdelete, userId, _id, type });
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				Log.d(TAG, e.getMessage());
			} finally {
				db.endTransaction();
				// db.close();
			}
		}
	}

	/**
	 * @param _id
	 * @param projectType
	 * @param sociality
	 * @param isdelete
	 */
	public synchronized void update(String userId, String _id,
			MSociality sociality, int isdelete) {

		// 社交数据库
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("update " + DBHelper.TABLE_SOCIALITY_DATA + " set "
						+ DBHelper.COLUMN_SOCIATILY_OBJECT + "=?,"
						+ DBHelper.COLUMN_SOCIATILY_ISDELETE + "=?  where "
						+ DBHelper.COLUMN_SOCIATILY_USERID + "=? and "
						+ DBHelper.COLUMN_SOCIATILY_ID + " =? and ("
						+ DBHelper.COLUMN_SOCIATILY_TYPE + "= 3 or "
						+ DBHelper.COLUMN_SOCIATILY_TYPE + " = 4 or "
						+ DBHelper.COLUMN_SOCIATILY_TYPE + " = 5" + ")",
						new Object[] { DBHelper.objectToBytes(sociality),
								isdelete, userId, _id });
				db.setTransactionSuccessful();
			} catch (Exception e) {
				Log.d(TAG, e.getMessage());
			} finally {
				db.endTransaction();
				// db.close();
			}
		}
	}

	/**
	 * @param userId
	 * @param _id
	 * @param projectType
	 * @param sociality
	 * @param isdelete
	 */
	public synchronized boolean synchronous(String userId, MSociality sociality) {
		if (TextUtils.isEmpty(userId)) {
			return false;
		}
		if (sociality == null) {
			return false;
		}
		// 已经存在
		MSociality mSociality = null;
		// 会议
		if (sociality.getType() == 5 || sociality.getType() == 4
				|| sociality.getType() == 3) {
			if (queryExistence(userId, sociality.getId() + "")) {
				mSociality = getPushMeetingMSocialityFromDB(userId,
						sociality.getId() + "");
				if (mSociality == null) {
					return false;
				}
				mSociality.setTime(sociality.getTime());
				mSociality.setNewCount(sociality.getNewCount());

				mSociality.setDelete(false);

				update(userId, sociality.getId() + "", mSociality, 0);
				return true;
			}
			return false;
		}
		// 通知
		else if (sociality.getType() == 6) {
			if (queryExistence(userId, sociality.getId() + "",
					sociality.getType())) {
				mSociality = getMSocialityFromDB(userId,
						sociality.getId() + "", sociality.getType());

				if (mSociality == null) {
					return false;
				}

				if (StringUtils.isNotEmpty(sociality.getOrderTime()))
					mSociality.setOrderTime(sociality.getOrderTime());

				mSociality.setTime(sociality.getTime());
				mSociality.setNewCount(sociality.getNewCount());

				if (StringUtils.isNotEmpty(sociality.getSocialDetail()
						.getContent()))
					mSociality.getSocialDetail().setContent(
							sociality.getSocialDetail().getContent());

				mSociality.setDelete(false);
				update(userId, sociality.getId() + "", sociality.getType(),
						mSociality, 0);
				return true;
			}
			return false;
		}
		// 邀请函
		else if (sociality.getType() == 7) {
			return false;
		} else {
			// 单聊私聊
			if (queryExistence(userId, sociality.getId() + "",
					sociality.getType())) {
				mSociality = getMSocialityFromDB(userId,
						sociality.getId() + "", sociality.getType());

				if (mSociality == null) {
					return false;
				}

				if (StringUtils.isNotEmpty(sociality.getOrderTime()))
					mSociality.setOrderTime(sociality.getOrderTime());

				mSociality.setTime(sociality.getTime());
				mSociality.setNewCount(sociality.getNewCount());

				if (StringUtils.isNotEmpty(sociality.getSocialDetail()
						.getContent()))
					mSociality.getSocialDetail().setContent(
							sociality.getSocialDetail().getContent());

				mSociality.getSocialDetail().setSenderID(
						sociality.getSocialDetail().getSenderID());

				if (StringUtils.isNotEmpty(sociality.getSocialDetail()
						.getSenderName()))
					mSociality.getSocialDetail().setSenderName(
							sociality.getSocialDetail().getSenderName());

				// 设置推送
				// 相同以本地数据库为准
				if (StringUtils.isNotEmpty(sociality.getAtMsgId())) {
					if (sociality.getAtMsgId().equals(mSociality.getAtMsgId())) {
						// 服务器端传来的@
						if (!sociality.isAtVisible()) {
							mSociality.setAtVisible(false);
						}
					}
					// 不同要显示
					else {
						mSociality.setAtVisible(true);
					}
					mSociality.setAtMsgId(sociality.getAtMsgId());
					mSociality.setAtName(sociality.getAtName());
				}
				// else {
				// mSociality.setAtVisible(false);
				// }

				mSociality.setDelete(false);
				update(userId, sociality.getId() + "", sociality.getType(),
						mSociality, 0);
				return true;
			} else {
				insert(userId, sociality.getId() + "", sociality.getType(),
						sociality, 0);
				return true;
			}
		}
	}

	/**
	 * 删除记录
	 * 
	 * @param uid
	 * @param url
	 */
	public synchronized void delete(String userId, String _id, int type) {
		if (TextUtils.isEmpty(userId)) {
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL(
						"DELETE FROM " + DBHelper.TABLE_SOCIALITY_DATA
								+ " WHERE " + DBHelper.COLUMN_SOCIATILY_USERID
								+ " = ? AND " + DBHelper.COLUMN_SOCIATILY_ID
								+ " = ? and " + DBHelper.COLUMN_SOCIATILY_TYPE
								+ " = ?",
						new String[] { userId, _id, type + "" });
				db.setTransactionSuccessful();
			} catch (Exception e) {
			} finally {
				db.endTransaction();
				// db.close();
			}
		}
	}

	/**
	 * 删除记录
	 * 
	 * @param uid
	 * @param url
	 */
	public synchronized void delete(String userId, int type) {
		if (TextUtils.isEmpty(userId)) {
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL(
						"DELETE FROM " + DBHelper.TABLE_SOCIALITY_DATA
								+ " WHERE " + DBHelper.COLUMN_SOCIATILY_USERID
								+ " = ? AND " + DBHelper.COLUMN_SOCIATILY_TYPE
								+ " = ?", new String[] { userId, type + "" });
				db.setTransactionSuccessful();
			} catch (Exception e) {
			} finally {
				db.endTransaction();
				// db.close();
			}
		}
	}

	/**
	 * 查询单一数据记录
	 * 
	 * @param userId
	 * @param _id
	 * @param type
	 * @return
	 */
	public synchronized MSociality getMSocialityFromDB(String userId,
			String _id, int type) {
		MSociality sociality = null;
		if (TextUtils.isEmpty(userId)) {
			return null;
		}
		Cursor cursor = queryTheCursor(userId, _id, type);
		try {
			while (cursor.moveToNext()) {
				byte[] blob = cursor.getBlob(cursor
						.getColumnIndex(DBHelper.COLUMN_SOCIATILY_OBJECT));
				sociality = (MSociality) DBHelper
						.bytesToObject(cursor.getBlob(cursor
								.getColumnIndex(DBHelper.COLUMN_SOCIATILY_OBJECT)));
			}
		} catch (Exception e) {
			Log.d(TAG, e.getMessage().toString());
		} finally {
			cursor.close();
		}
		return sociality;
	}

	/**
	 * 查询单一数据记录 -- 推送会议
	 * 
	 * @param userId
	 * @param _id
	 * @param projectType
	 * @return
	 */
	public synchronized MSociality getPushMeetingMSocialityFromDB(
			String userId, String _id) {
		MSociality sociality = null;
		if (TextUtils.isEmpty(userId)) {
			return null;
		}
		Cursor cursor = queryMeetingCursor(userId, _id);
		try {
			while (cursor.moveToNext()) {
				// byte[] blob =
				// cursor.getBlob(cursor.getColumnIndex(DBHelper.COLUMN_SOCIATILY_OBJECT));
				sociality = (MSociality) DBHelper
						.bytesToObject(cursor.getBlob(cursor
								.getColumnIndex(DBHelper.COLUMN_SOCIATILY_OBJECT)));
			}
		} catch (Exception e) {
			Log.d(TAG, e.getMessage().toString());
		} finally {
			cursor.close();
		}
		return sociality;
	}

	/**
	 * 查询条目存在与否
	 * 
	 * @param uid
	 * @param url
	 * @return
	 */
	public boolean queryExistence(String userId, String _id, int type) {
		if (TextUtils.isEmpty(userId)) {
			return false;
		}
		boolean exist = false;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c = queryTheCursor(userId, _id, type);
			try {
				while (c.moveToNext()) {
					exist = true;
				}
			} catch (Exception e) {
				Log.d(TAG, e.getMessage());
			} finally {
				c.close();
			}
		}
		return exist;
	}

	/**
	 * 查询条目存在与否
	 * 
	 * @param uid
	 * @param url
	 * @return
	 */
	public boolean queryExistence(String userId, String _id) {
		if (TextUtils.isEmpty(userId)) {
			return false;
		}
		boolean exist = false;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c = null;

			c = queryMeetingCursor(userId, _id);
			try {
				while (c.moveToNext()) {
					exist = true;
				}
			} catch (Exception e) {
				Log.d(TAG, e.getMessage());
			} finally {
				c.close();
			}
		}
		return exist;
	}

	// 查询游标位置
	private Cursor queryTheCursor(String userId, String _id, int type) {
		Cursor c = null;
		// 通知邀请函
		if (type == 6 || type == 7) {
			synchronized (helper) {
				if (!db.isOpen()) {
					db = helper.getWritableDatabase();
				}
				c = db.rawQuery("SELECT * FROM "
						+ DBHelper.TABLE_SOCIALITY_DATA + " WHERE "
						+ DBHelper.COLUMN_SOCIATILY_USERID + " = ? AND "
						+ DBHelper.COLUMN_SOCIATILY_TYPE + " = ?",
						new String[] { userId, type + "" });
			}
		}
		// 会议
		else if (type == 3 || type == 4 || type == 5) {
			c = queryMeetingCursor(userId, _id);
		}
		// 单聊私聊
		else {
			synchronized (helper) {
				if (!db.isOpen()) {
					db = helper.getWritableDatabase();
				}
				c = db.rawQuery("SELECT * FROM "
						+ DBHelper.TABLE_SOCIALITY_DATA + " WHERE "
						+ DBHelper.COLUMN_SOCIATILY_USERID + " = ? AND "
						+ DBHelper.COLUMN_SOCIATILY_ID + " = ? AND "
						+ DBHelper.COLUMN_SOCIATILY_TYPE + " = ?",
						new String[] { userId, _id, type + "" });
			}
		}
		return c;
	}

	// 查询游标位置 - 推送会议
	private Cursor queryMeetingCursor(String userId, String _id) {
		Cursor c = null;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_SOCIALITY_DATA
					+ " WHERE " + DBHelper.COLUMN_SOCIATILY_USERID
					+ " = ? AND " + DBHelper.COLUMN_SOCIATILY_ID + " = ? AND ("
					+ DBHelper.COLUMN_SOCIATILY_TYPE + " = 3 OR "
					+ DBHelper.COLUMN_SOCIATILY_TYPE + " = 4 OR "
					+ DBHelper.COLUMN_SOCIATILY_TYPE + " = 5" + ")",
					new String[] { userId, _id + "" });
		}
		return c;
	}

	/**
	 * 查询所有
	 * 
	 * @param userId
	 * @return
	 */
	private synchronized Cursor queryAll(String userId) {
		Cursor c = null;
		if (!db.isOpen()) {
			db = helper.getWritableDatabase();
		}
		c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_SOCIALITY_DATA
				+ " WHERE " + DBHelper.COLUMN_SOCIATILY_USERID + " = ?",
				new String[] { userId });
		return c;
	}

	public synchronized List<MSociality> getListMSocialityFromDB(String userId) {
		allSocialityCursor = queryAll(userId);
		List<MSociality> socialitys = new ArrayList<MSociality>();

		if (!db.isOpen()) {
			db = helper.getWritableDatabase();
		}
		try {
			if (allSocialityCursor != null && allSocialityCursor.getCount() > 0) {

				while (allSocialityCursor.moveToNext()) {
					MSociality sociality = (MSociality) DBHelper
							.bytesToObject(allSocialityCursor.getBlob(allSocialityCursor
									.getColumnIndex(DBHelper.COLUMN_SOCIATILY_OBJECT)));
					int isdel = allSocialityCursor
							.getInt(allSocialityCursor
									.getColumnIndex(DBHelper.COLUMN_SOCIATILY_ISDELETE));
					// 0false 1true
					if (isdel == 0) {
						sociality.setDelete(false);
					} else {
						sociality.setDelete(true);
					}
					socialitys.add(sociality);
				}
			}
			SortMSociality(socialitys);// android会议在上，邀请函、通知函、群聊混排；ios会议、邀请函、通知函在上，群聊在下，与ios一致所有屏蔽该方法
		} catch (Exception e) {
			e.printStackTrace();
		}
		// finally {
		// cursor.close();
		// }
		return socialitys;
	}

	public synchronized List<MSociality> getRandomListMSocialityFromDB(
			String userId) {
		allSocialityCursor = queryAll(userId);
		List<MSociality> socialitys = new ArrayList<MSociality>();

		if (!db.isOpen()) {
			db = helper.getWritableDatabase();
		}
		try {
			if (allSocialityCursor != null && allSocialityCursor.getCount() > 0) {

				while (allSocialityCursor.moveToNext()) {
					MSociality sociality = (MSociality) DBHelper
							.bytesToObject(allSocialityCursor.getBlob(allSocialityCursor
									.getColumnIndex(DBHelper.COLUMN_SOCIATILY_OBJECT)));
					int isdel = allSocialityCursor
							.getInt(allSocialityCursor
									.getColumnIndex(DBHelper.COLUMN_SOCIATILY_ISDELETE));
					// 0false 1true
					if (isdel == 0) {
						sociality.setDelete(false);
					} else {
						sociality.setDelete(true);
					}
					socialitys.add(sociality);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// finally {
		// cursor.close();
		// }
		return socialitys;
	}
/**
 * 把社交里的type分等级 1最高在最上头 
 * @param type
 * @return
 */
	private int getIndex(int type) {
		// "type":"1-私聊，2-群聊，3-进行中的会议，4-未开始，5-已结束的会议，6-通知，7-邀请函",
		int index = 0;
		switch (type) {
		case 1:
		case 2:
			index = 6;
			break;
		case 3:
			index = 1;
			break;
		case 4:
			index = 2;
			break;
		case 5:
			index = 3;
			break;
		case 6:
			index = 5;
			break;
		case 7:
			index = 4;
			break;

		default:
			break;
		}
		return index;
	}

	public void SortMSociality(List<MSociality> listResult) throws Exception{
		if (listResult != null) {
			Collections.sort(listResult, new Comparator<MSociality>() {
				public int compare(MSociality o1, MSociality o2) {
					// 有对象为null排序
					if (o1 == null || o2 == null) {
						Integer sortIndex = containsNullSort(o1, o2);
						return sortIndex;
					}
					// 有对象type为null排序 TODO
					if (getIndex(o1.getType()) - getIndex(o2.getType()) == 0) {
						// // 同为类别开始时间排序
						// // // 有对象时间为null排序在前
						if (StringUtils.isEmpty(o1.getOrderTime())
								|| StringUtils.isEmpty(o2.getOrderTime())) {
							Integer sortIndex = containsNullSort(
									o1.getOrderTime(), o2.getOrderTime());
							return sortIndex;
						}

						if (TimeUtil.getDateTime(o1.getOrderTime()).before(
								TimeUtil.getDateTime(o2.getOrderTime())))
							return 1;
						if (TimeUtil.getDateTime(o1.getOrderTime()).after(
								TimeUtil.getDateTime(o2.getOrderTime())))
							return -1;
					} else if (getIndex(o1.getType()) - getIndex(o2.getType()) < 0) {
						return -1;
					}
					return 1;
				}
			});
		}
	}

	/**
	 * 包含null值得对象排序
	 * 
	 * @return
	 */
	public Integer containsNullSort(Object obj1, Object obj2) {
		if (Util.isNullOrEmpty(obj1)) {
			if (!Util.isNullOrEmpty(obj2)) {
				return -1;
			} else {
				return 0;
			}
		} else {
			return 1;
		}
	}

	// 非MainActivity推送数据
	public boolean pushDBData(String userId, MSociality mSociality) {
		// 查询当前条目是否存在
		if (queryExistence(App.getUserID(), mSociality.getId() + "",
				mSociality.getType())) {
			MSociality sociality = getMSocialityFromDB(App.getUserID(),
					mSociality.getId() + "", mSociality.getType());

			// modified 99
			// mSociality.setPushDataNum((sociality.getPushDataNum() + 1));
			mSociality.setNewCount(sociality.getNewCount() + 1);
			if (synchronous(userId, mSociality)) {
				return true;
			}
			return false;
		} else {
			// modified 99
			// mSociality.setPushDataNum((mSociality.getPushDataNum() + 1));
			insert(App.getUserID(), mSociality.getId() + "",
					mSociality.getType(), mSociality, 0);
			return true;
		}
	}
	
	public synchronized void insert2(String userId, MSociality social) {

		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("INSERT INTO " + DBHelper.TABLE_SOCIALITY_DATA2
						+ " VALUES(?,?,?,?,?)", new Object[] { userId, social.getId()+"",
						social.getTime(), social.getNewCount(),"0" });
				db.setTransactionSuccessful();
			} catch (Exception e) {
				Log.d(TAG, e.getMessage());
			} finally {
				db.endTransaction();
				// db.close();
			}
		}
	}
	
	/**
	 * 删除记录
	 * 
	 * @param uid
	 * @param url
	 */
	public synchronized void delete2(String userId, String _id) {
		if (TextUtils.isEmpty(userId)) {
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL(
						"update " + DBHelper.TABLE_SOCIALITY_DATA2
						+ " set " + DBHelper.COLUMN_SOCIATILY_ISDELETE
						+ "= 1  where " + DBHelper.COLUMN_SOCIATILY_USERID
						+ "= ? and " + DBHelper.COLUMN_SOCIATILY_ID
						+ " = ? ",
						new String[] { userId, _id});
				db.setTransactionSuccessful();
			} catch (Exception e) {
			} finally {
				db.endTransaction();
				// db.close();
			}
		}
	}
	
	public synchronized void delete3(String userId){
		if (TextUtils.isEmpty(userId)) {
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL(
						"update " + DBHelper.TABLE_SOCIALITY_DATA2
						+ " set " + DBHelper.COLUMN_SOCIATILY_ISDELETE
						+ "= 1  where " + DBHelper.COLUMN_SOCIATILY_USERID
						+ "= ? ",
						new String[] { userId});
				db.setTransactionSuccessful();
			} catch (Exception e) {
			} finally {
				db.endTransaction();
				// db.close();
			}
		}
		
	}
	
	/**更新记录新消息条数*/
	public synchronized void update2(String userId, String id, int newcount) {
		if (TextUtils.isEmpty(userId)) {
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL(
						"update " + DBHelper.TABLE_SOCIALITY_DATA2
						+ " set " + DBHelper.COLUMN_SOCIATILY_NEWCOUNT
						+ "= "+newcount+","+ DBHelper.COLUMN_SOCIATILY_ISDELETE+" = 0  where " + DBHelper.COLUMN_SOCIATILY_USERID
						+ "= ? and " + DBHelper.COLUMN_SOCIATILY_ID
						+ " = ? ",
						new String[] { userId, id});
				db.setTransactionSuccessful();
			} catch (Exception e) {
			} finally {
				db.endTransaction();
				// db.close();
			}
		}
	}
	
	public synchronized boolean queryExistence(String userId, String id, String isdel) {
		boolean exist = false;
		Cursor c = null;
		if (!db.isOpen()) {
			db = helper.getWritableDatabase();
		}
		c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_SOCIALITY_DATA2
				+ " WHERE " + DBHelper.COLUMN_SOCIATILY_USERID + " = ? and " + DBHelper.COLUMN_SOCIATILY_ID + " = ? and " + DBHelper.COLUMN_SOCIATILY_ISDELETE + " = ?",
				new String[] { userId, id, isdel });
		try {
			while (c.moveToNext()) {
				exist = true;
			}
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		} finally {
			c.close();
		}
		return exist;
	}
	
	public synchronized MSociality querySociality(String userId, String id) {
		MSociality social = null;
		Cursor c = null;
		if (!db.isOpen()) {
			db = helper.getWritableDatabase();
		}
		c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_SOCIALITY_DATA2
				+ " WHERE " + DBHelper.COLUMN_SOCIATILY_USERID + " = ? and " + DBHelper.COLUMN_SOCIATILY_ID + " = ? ",
				new String[] { userId, id});
		try {
			while (c.moveToNext()) {
				social = new MSociality();
				social.setId(Long.valueOf(c.getString(c.getColumnIndex(DBHelper.COLUMN_SOCIATILY_ID))));
				social.setTime(c.getString(c.getColumnIndex(DBHelper.COLUMN_SOCIATILY_TIME)));
				social.setNewCount(Integer.valueOf(c.getString(c.getColumnIndex(DBHelper.COLUMN_SOCIATILY_NEWCOUNT))));
				social.setDelete(c.getString(c.getColumnIndex(DBHelper.COLUMN_SOCIATILY_ISDELETE)).equals("1"));
			}
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		} finally {
			c.close();
		}
		return social;
	}
	

	
	public synchronized List<MSociality> querySocialitys(String userId) {
		List<MSociality> socials = new ArrayList<MSociality>();
		MSociality social = null;
		Cursor c = null;
		if (!db.isOpen()) {
			db = helper.getWritableDatabase();
		}
		c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_SOCIALITY_DATA2
				+ " WHERE " + DBHelper.COLUMN_SOCIATILY_USERID + " = ?  and " + DBHelper.COLUMN_SOCIATILY_ISDELETE +" = 0",
				new String[] { userId});
		try {
			while (c.moveToNext()) {
				social = new MSociality();
				social.setId(Long.valueOf(c.getString(c.getColumnIndex(DBHelper.COLUMN_SOCIATILY_ID))));
				social.setTime(c.getString(c.getColumnIndex(DBHelper.COLUMN_SOCIATILY_TIME)));
				social.setNewCount(Integer.valueOf(c.getString(c.getColumnIndex(DBHelper.COLUMN_SOCIATILY_NEWCOUNT))));
				social.setDelete(c.getString(c.getColumnIndex(DBHelper.COLUMN_SOCIATILY_ISDELETE)).equals("1"));
				socials.add(social);
			}
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		} finally {
			c.close();
		}
		return socials;
	}

}
