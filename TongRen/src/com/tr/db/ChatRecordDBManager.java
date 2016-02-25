package com.tr.db;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.tr.App;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MeetingMessage;
import com.utils.string.StringUtils;

/**
 * 聊天记录数据库管理器（群聊和私聊）
 * 
 * @author leon
 */
public class ChatRecordDBManager {

	private final String TAG = getClass().getSimpleName();

	private DBHelper helper;
	private SQLiteDatabase db;

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public ChatRecordDBManager(Context context) {
		helper = DBHelper.getInstance(context);
		synchronized (helper) {
			db = helper.getWritableDatabase();
		}
	}

	/**
	 * 插入单条记录（推送消息调用）
	 * 
	 * @param uid
	 * @param chatId
	 * @param msg
	 */
	public void insert(String uid, String chatId, IMBaseMessage msg) {

		if (msg == null) {
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction(); // 开始事务
			try {
				db.execSQL(
						"INSERT INTO " + DBHelper.TABLE_CHAT_RECORD + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new Object[] { msg.getMessageID(), App.getUserID(), chatId, msg.getIndex(), msg.getSequence(),
								msg.getRecvID(), msg.getSenderID(), msg.getSenderName(), msg.getType(),
								msg.getContent(), msg.getTime(), DBHelper.objectToBytes(msg.getJtFile()),
								msg.getSendType(), msg.getSenderType(), msg.getImtype(), msg.isHide() });
				db.setTransactionSuccessful(); // 设置事务成功完成
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction(); // 结束事务
				db.close();
			}
		}
	}

	/**
	 * 更新单条记录
	 * 
	 * @param uid
	 * @param chatId
	 * @param msg
	 */
	private void update(String uid, String chatId, IMBaseMessage msg) {

		if (msg == null) {
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction(); // 开始事务
			try {
				db.execSQL(
						"UPDATE " + DBHelper.TABLE_CHAT_RECORD + " SET " + DBHelper.COLUMN_CHAT_ID + " = ?, "
								+ DBHelper.COLUMN_CHAT_MSG_INDEX + " = ?, " + DBHelper.COLUMN_CHAT_MSG_SEQUENCE
								+ " = ?, " + DBHelper.COLUMN_CHAT_RECV_ID + " = ?, " + DBHelper.COLUMN_CHAT_SENDER_ID
								+ " = ?, " + DBHelper.COLUMN_CHAT_SENDER_NAME + " = ?, " + DBHelper.COLUMN_CHAT_TYPE
								+ " = ?, " + DBHelper.COLUMN_CHAT_CONTENT + " = ?, " + DBHelper.COLUMN_CHAT_TIME
								+ " = ?, " + DBHelper.COLUMN_CHAT_JTFILE + " = ?, " + DBHelper.COLUMN_CHAT_SEND_TYPE
								+ " = ?, " + DBHelper.COLUMN_CHAT_SENDER_TYPE + " = ?, " + DBHelper.COLUMN_CHAT_IM_TYPE
								+ " = ?, " + DBHelper.COLUMN_CHAT_HIDE + " = ? WHERE "
								+ DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ? AND " + DBHelper.COLUMN_UID + " = ?",
						new Object[] { chatId, msg.getIndex(), msg.getSequence(), msg.getRecvID(), msg.getSenderID(),
								msg.getSenderName(), msg.getType(), msg.getContent(), msg.getTime(),
								DBHelper.objectToBytes(msg.getJtFile()), msg.getSendType(), msg.getSenderType(),
								msg.getImtype(), msg.isHide(), msg.getMessageID(), App.getUserID() });
				db.setTransactionSuccessful(); // 设置事务成功完成
			} catch (Exception e) {
				Log.d(TAG, e.getMessage() + "");
			} finally {
				db.endTransaction(); // 结束事务
				db.close();
			}
		}
	}

	/**
	 * 同步数据库单条记录
	 * 
	 * @param uid
	 * @param chatId
	 * @param msg
	 */
	public void synchronous(String uid, String chatId, IMBaseMessage msg) {

		if (msg == null) {
			return;
		}
		if (queryExistence(uid, chatId, msg.getMessageID())) {
			if (!isHide(uid, chatId, msg.getMessageID())) {
				update(uid, chatId, msg);
			}
		} else {
			if (msg.getSenderType() != 2) {
				insert(uid, chatId, msg);
			}
		}
	}

	/**
	 * 同步数据库
	 * 
	 * @param uid
	 * @param chatId
	 * @param listMsg
	 */
	public void synchronous(String uid, String chatId, List<IMBaseMessage> listMsg) {
		if (listMsg != null && listMsg.size() > 0) {
			for (IMBaseMessage msg : listMsg) {
				if (queryExistence(uid, chatId, msg.getMessageID())) {
					if (!isHide(uid, chatId, msg.getMessageID())) {
						update(uid, chatId, msg);
					}
				} else {
					if (msg.getSenderType() != 2) {
						insert(uid, chatId, msg);
					}
				}
			}
		}
	}

	/**
	 * 设置指定记录为不可见
	 * 
	 * @param uid
	 * @param chatId
	 * @param msgId
	 */
	public void hideMessage(String uid, String chatId, String msgId) {

		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("UPDATE " + DBHelper.TABLE_CHAT_RECORD + " SET " + DBHelper.COLUMN_CHAT_HIDE + " = ?"
						+ " WHERE " + DBHelper.COLUMN_UID + " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND "
						+ DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ?", new String[] { "1", uid, chatId, msgId });
				db.setTransactionSuccessful();
			} catch (Exception e) {
				Log.d(TAG, e.getMessage() + "");
			} finally {
				db.endTransaction();
				db.close();
			}
		}
	}

	/**
	 * 更新数据库中消息的发送状态
	 * 
	 * @param uid
	 * @param chatId
	 * @param msgId
	 * @param sendType
	 */
	public void setMessageSendType(String uid, String chatId, String msgId, int sendType) {
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("UPDATE " + DBHelper.TABLE_CHAT_RECORD + " SET " + DBHelper.COLUMN_CHAT_SEND_TYPE + " = ?"
						+ " WHERE " + DBHelper.COLUMN_UID + " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND "
						+ DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ?", new String[] { sendType + "", uid, chatId, msgId });
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
	}

	/**
	 * 删除单条记录
	 * 
	 * @param uid
	 * @param chatId
	 * @param msgId
	 */
	public void delete(String uid, String chatId, String msgId) {

		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("DELETE FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID + " = ? AND "
						+ DBHelper.COLUMN_CHAT_ID + " = ? AND " + DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ?",
						new String[] { uid, chatId, msgId });
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
	}

	/**
	 * 删除指定畅聊的所有聊天记录
	 * 
	 * @param uid
	 * @param chatId
	 */
	public void deleteAll(String uid, String chatId) {
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("DELETE FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID + " = ? AND "
						+ DBHelper.COLUMN_CHAT_ID + " = ?", new String[] { uid, chatId });
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
	}

	/**
	 * 查询消息记录是否存在
	 * 
	 * @param uid
	 * @param chatId
	 * @param msgId
	 * @return
	 */
	public boolean queryExistence(String uid, String chatId, String msgId) {
		boolean exist = false;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
					+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND " + DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ?",
					new String[] { uid, chatId, msgId });
			try {
				while (cursor.moveToNext()) {
					exist = true;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}
		return exist;
	}

	/**
	 * 查询消息是否可见
	 * 
	 * @param uid
	 * @param chatId
	 * @param msgId
	 * @return
	 */
	public boolean isHide(String uid, String chatId, String msgId) {
		boolean isHide = false;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
					+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND " + DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ?",
					new String[] { uid, chatId, msgId });
			try {
				while (cursor.moveToNext()) {
					IMBaseMessage msg = createIMBaseMessageFromCursor(cursor);
					if (msg != null) {
						isHide = msg.isHide();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}
		return isHide;
	}

	/**
	 * 查询单条消息记录
	 * 
	 * @param uid
	 * @param chatId
	 * @param msgId
	 * @return
	 */
	private IMBaseMessage queryMessageById(String uid, String chatId, String msgId) {
		IMBaseMessage msg = null;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
					+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND " + DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ?",
					new String[] { uid, chatId, msgId });
			try {
				while (cursor.moveToNext()) {
					msg = createIMBaseMessageFromCursor(cursor);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}
		return msg;
	}

	/**
	 * 返回指定畅聊的所有聊天记录
	 * 
	 * @param uid
	 * @param chatId
	 * @deprecated
	 * @return
	 */
	/*
	 * public List<IMBaseMessage> query(String uid, String chatId) {
	 * List<IMBaseMessage> listMsg = new ArrayList<IMBaseMessage>();
	 * synchronized(helper){ if(!db.isOpen()){ db =
	 * helper.getWritableDatabase(); } Cursor cursor =
	 * db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " +
	 * DBHelper.COLUMN_UID + " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND "
	 * + DBHelper.COLUMN_CHAT_HIDE + " = ?" + " ORDER BY " +
	 * DBHelper.COLUMN_CHAT_TIME, new String[] { uid, chatId, "0"}); try { while
	 * (cursor.moveToNext()) { IMBaseMessage msg =
	 * createIMBaseMessageFromCursor(cursor); if(msg != null){ listMsg.add(msg);
	 * } } } catch (Exception e) { e.printStackTrace(); } finally {
	 * cursor.close(); } } return listMsg; }
	 */

	/**
	 * 返回指定序号以前的指定条聊天记录（查找旧记录）
	 * 
	 * @param uid
	 * @param chatId
	 * @param sequence
	 * @param size
	 * @param isBackward
	 *            true-查找fromIndex之后的聊天记录，false-查找fromIndex之前的聊天记录
	 * @param includeHide
	 *            true-包括隐藏的消息记录，false-不包括隐藏的消息记录
	 * @return
	 */
	public ArrayList<IMBaseMessage> query(String uid, String chatId, int fromIndex, int size, boolean isBackward,
			boolean includeHide) {

		ArrayList<IMBaseMessage> listMsg = new ArrayList<IMBaseMessage>();
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor cursor = null;
			if (fromIndex < 0) { // 返回最新的size条数据
				int count = queryMessageCount(uid, chatId);
				int offset = 0;
				if(count<=size){
					if (!includeHide) {
						cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE "
								+ DBHelper.COLUMN_UID + " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND "
								+ DBHelper.COLUMN_CHAT_HIDE + " = ?" /*+ " ORDER BY " + DBHelper.COLUMN_CHAT_TIME
								+ " DESC"*/ + " LIMIT ?", new String[] { uid, chatId, "0", size + "" });
					} else {
						cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE "
								+ DBHelper.COLUMN_UID + " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ?" /*+ " ORDER BY "
								+ DBHelper.COLUMN_CHAT_TIME + " DESC"*/ + " LIMIT ?", new String[] { uid, chatId,
								size + "" });
					}
				}else{
					offset = count - size;
					if (!includeHide) {
						cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE "
								+ DBHelper.COLUMN_UID + " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND "
								+ DBHelper.COLUMN_CHAT_HIDE + " = ?" /*+ " ORDER BY " + DBHelper.COLUMN_CHAT_TIME
								+ " DESC"*/ + " LIMIT ? OFFSET ?", new String[] { uid, chatId, "0", size + "" ,offset +""});
					} else {
						cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE "
								+ DBHelper.COLUMN_UID + " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ?" /*+ " ORDER BY "
								+ DBHelper.COLUMN_CHAT_TIME + " DESC"*/ + " LIMIT ?", new String[] { uid, chatId,
								size + "" });
					}
				}
			} else { // 返回fromIndex之前的最新的size条数据
				if (isBackward) { // 往后查（size不起作用）
					if (!includeHide) {
						cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE "
								+ DBHelper.COLUMN_UID + " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND "
								+ DBHelper.COLUMN_CHAT_HIDE + " = ? AND " + DBHelper.COLUMN_CHAT_MSG_INDEX + " > ?"
								+ " ORDER BY " + DBHelper.COLUMN_CHAT_TIME /*+ " DESC"*/, new String[] { uid, chatId,
								"0", fromIndex + "" });
					} else {
						cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE "
								+ DBHelper.COLUMN_UID + " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND "
								+ DBHelper.COLUMN_CHAT_MSG_INDEX + " > ?" + " ORDER BY "
								+ DBHelper.COLUMN_CHAT_TIME /*+ " DESC"*/,
								new String[] { uid, chatId, fromIndex + "" });
					}
				} else { // 往前查
					if (!includeHide) {
						cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE "
								+ DBHelper.COLUMN_UID + " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND "
								+ DBHelper.COLUMN_CHAT_HIDE + " = ? AND " + DBHelper.COLUMN_CHAT_MSG_INDEX + " < ? AND " + DBHelper.COLUMN_CHAT_MSG_INDEX + "<> -1"
								+ " ORDER BY " + DBHelper.COLUMN_CHAT_TIME + " DESC" + " LIMIT ?", new String[] {
								uid, chatId, "0", fromIndex + "", size + "" });
					} else {
						cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE "
								+ DBHelper.COLUMN_UID + " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND "
								+ DBHelper.COLUMN_CHAT_MSG_INDEX + " < ? AND " + DBHelper.COLUMN_CHAT_MSG_INDEX + "<> -1" + " ORDER BY "
								+ DBHelper.COLUMN_CHAT_TIME + " DESC" + " LIMIT ?", new String[] { uid, chatId,
								fromIndex + "", size + "" });
					}
				}
			}
			try {
				while (cursor.moveToNext()) {
					IMBaseMessage msg = createIMBaseMessageFromCursor(cursor);
					if (msg != null) {
						if (isBackward || fromIndex<0) {
							listMsg.add(msg); // 查询结果需要升序排列
						}else{
							listMsg.add(0, msg); // 查询结果需要升序排列
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}
		return listMsg;
	}

	/**
	 * 本地数据库中是否还有更多数据 1-数据库中存有全部的聊天记录；2-数据库中还有未读取的聊天记录
	 * 
	 * @param uid
	 * @param chatId
	 * @param fromIndex
	 * @param isBackward
	 *            true-往后找；false-往前找
	 * @return
	 */
	public boolean hasMore(String uid, String chatId, int fromIndex, boolean isBackward) {
		boolean hasMore = false;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
					+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND " + DBHelper.COLUMN_CHAT_HIDE + " = ? AND "
					+ DBHelper.COLUMN_CHAT_MSG_INDEX + " < ?", new String[] { uid, chatId, "0", fromIndex + "" });
			try {
				while (cursor.moveToNext()) {
					IMBaseMessage msg = createIMBaseMessageFromCursor(cursor);
					if (msg != null) {
						hasMore = true;
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}
		return hasMore;
	}

	/**
	 * 根据fromIndex查找消息的Sequence值
	 * 
	 * @param uid
	 * @param chatId
	 * @param fromIndex
	 * @return
	 */
	public int querySequence(String uid, String chatId, int fromIndex) {
		int sequence = 0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
					+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND " + DBHelper.COLUMN_CHAT_MSG_INDEX + " = ?",
					new String[] { uid, chatId, fromIndex + "" });
			try {
				while (cursor.moveToNext()) {
					IMBaseMessage msg = createIMBaseMessageFromCursor(cursor);
					if (msg != null) {
						sequence = msg.getSequence();
					}
				}
			} catch (Exception e) {
				Log.d(TAG, e.getMessage() + "");
			} finally {
				cursor.close();
			}
		}
		return sequence;
	}

	/**
	 * 根据Sequence查找对应的消息
	 * 
	 * @param uid
	 * @param chatId
	 * @param sequence
	 * @param includeHide
	 * @return
	 */
	public IMBaseMessage queryMessageBySequence(String uid, String chatId, int sequence, boolean includeHide) {
		IMBaseMessage msg = null;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor cursor = null;
			if (includeHide) { // 查询范围包括隐藏的消息
				cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
						+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND " + DBHelper.COLUMN_CHAT_MSG_SEQUENCE
						+ " = ? AND " + DBHelper.COLUMN_CHAT_SEND_TYPE + " = ? OR " + DBHelper.COLUMN_CHAT_SEND_TYPE
						+ " = ?", new String[] { uid, chatId, sequence + "", IMBaseMessage.SEND_TYPE_PUSH + "",
						IMBaseMessage.SEND_TYPE_SENT + "" });
			} else { // 查询范围不包括隐藏的消息
				cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
						+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND " + DBHelper.COLUMN_CHAT_MSG_SEQUENCE
						+ " = ? AND " + DBHelper.COLUMN_CHAT_HIDE + " = ? AND " + DBHelper.COLUMN_CHAT_SEND_TYPE
						+ " IN (?,?)", new String[] { uid, chatId, sequence + "", "0",
						IMBaseMessage.SEND_TYPE_PUSH + "", IMBaseMessage.SEND_TYPE_SENT + "" });
			}
			try {
				while (cursor.moveToNext()) {
					msg = createIMBaseMessageFromCursor(cursor);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}
		return msg;
	}

	/**
	 * 查找数据库中最早的一条消息记录
	 * 
	 * @param uid
	 * @param chatId
	 * @param includeHide
	 *            是否包括隐藏的消息记录
	 * @return
	 */
	public IMBaseMessage queryEarliestMessage(String uid, String chatId, boolean includeHide) {
		IMBaseMessage msg = null;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor cursor = null;
			if (includeHide) { // 包括隐藏的消息
				cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
						+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ?" + " ORDER BY "
						+ DBHelper.COLUMN_CHAT_MSG_INDEX + " ASC" + " LIMIT 1", new String[] { uid, chatId });
			} else { // 不包括隐藏的消息
				cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
						+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND " + DBHelper.COLUMN_CHAT_HIDE + " = ?"
						+ " ORDER BY " + DBHelper.COLUMN_CHAT_MSG_INDEX + " ASC" + " LIMIT 1", new String[] { uid,
						chatId, "0" });
			}
			try {
				while (cursor.moveToNext()) {
					msg = createIMBaseMessageFromCursor(cursor);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}
		return msg;
	}

	/**
	 * 查找数据库中的消息记录数
	 * 
	 * @param uid
	 * @param chatId
	 * @return
	 */
	public int queryMessageCount(String uid, String chatId) {
		int count = 0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			count = (int) DatabaseUtils.queryNumEntries(db, DBHelper.TABLE_CHAT_RECORD, DBHelper.COLUMN_UID
					+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ?", new String[] { uid, chatId });
		}
		return count;
	}

	/**
	 * 查找数据库中最新的一条消息记录
	 * 
	 * @param uid
	 * @param chatId
	 * @param includeHide
	 *            是否包括隐藏的消息
	 * @return
	 */
	public IMBaseMessage queryLatestMessage(String uid, String chatId, boolean includeHide) {
		IMBaseMessage msg = null;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor cursor = null;
			if (includeHide) { // 返回结果包含隐藏的消息记录
				cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
						+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ?" + " ORDER BY "
						+ DBHelper.COLUMN_CHAT_MSG_INDEX + " DESC" + " LIMIT 1", new String[] { uid, chatId });
			} else { // 返回结果不包含隐藏的消息记录
				cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
						+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND " + DBHelper.COLUMN_CHAT_HIDE + " = ?"
						+ " ORDER BY " + DBHelper.COLUMN_CHAT_MSG_INDEX + " DESC" + " LIMIT 1", new String[] { uid,
						chatId, "0" });
			}
			try {
				while (cursor.moveToNext()) {
					msg = createIMBaseMessageFromCursor(cursor);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}
		return msg;
	}

	/**
	 * 从Cursor中创建IMBaseMessage对象
	 * 
	 * @param cursor
	 * @return
	 */
	private IMBaseMessage createIMBaseMessageFromCursor(Cursor cursor) {
		IMBaseMessage msg = new IMBaseMessage();
		try {
			msg.setSenderName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CHAT_SENDER_NAME)));
			msg.setContent(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CHAT_CONTENT)));
			msg.setMessageID(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CHAT_MESSAGE_ID)));
			msg.setIndex(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CHAT_MSG_INDEX)));
			msg.setRecvID(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CHAT_RECV_ID)));
			msg.setSenderID(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CHAT_SENDER_ID)));
			msg.setType(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CHAT_TYPE)));
			msg.setTime(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CHAT_TIME)));
			msg.setJtFile((JTFile) DBHelper.bytesToObject(cursor.getBlob(cursor
					.getColumnIndex(DBHelper.COLUMN_CHAT_JTFILE))));
			msg.setSendType(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CHAT_SEND_TYPE)));
			msg.setSenderType(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CHAT_SENDER_TYPE)));
			msg.setImtype(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CHAT_IM_TYPE)));
			int hide = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CHAT_HIDE));
			if (hide == 0) {
				msg.setHide(false);
			} else {
				msg.setHide(true);
			}
		} catch (Exception e) {
			msg = null;
		}
		return msg;
	}

	/**
	 * 返回图片消息数
	 * 
	 * @param uid
	 * @param chatId
	 * @param msgId
	 * @param span
	 * @return
	 */
	public Pair<Integer, List<IMBaseMessage>> queryImageItem(String uid, String chatId, String msgId, int span) {
		// 消息列表
		int index = 0;
		List<IMBaseMessage> listMsg = new ArrayList<IMBaseMessage>();
		// 前后消息列表
		List<IMBaseMessage> listBefore = null;
		List<IMBaseMessage> listAfter = null;
		// 指定消息
		IMBaseMessage specifiedMsg = queryMessageById(uid, chatId, msgId);
		if (specifiedMsg == null) {
			return null;
		}
		if (!db.isOpen()) {
			db = helper.getWritableDatabase();
		}
		listMsg.add(specifiedMsg);
		Cursor c1 = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
				+ " = ? AND " + DBHelper.COLUMN_CHAT_TYPE + " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND "
				+ DBHelper.COLUMN_CHAT_HIDE + " = ? AND " + DBHelper.COLUMN_CHAT_MSG_INDEX + " < ?", new String[] {
				uid, IMBaseMessage.TYPE_IMAGE + "", chatId, "0", specifiedMsg.getIndex() + "" });
		try {
			while (c1.moveToNext()) {
				if (listBefore == null) {
					listBefore = new ArrayList<IMBaseMessage>();
				}
				IMBaseMessage msg = createIMBaseMessageFromCursor(c1);
				listBefore.add(0, msg);
				if (listBefore.size() >= span) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c1.close();
		}
		
		Cursor c2 = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
				+ " = ? AND " + DBHelper.COLUMN_CHAT_TYPE + " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND "
				+ DBHelper.COLUMN_CHAT_HIDE + " = ? AND " + DBHelper.COLUMN_CHAT_MSG_INDEX + " > ?", new String[] {
				uid, IMBaseMessage.TYPE_IMAGE + "", chatId, "0", specifiedMsg.getIndex() + "" });
		try {
			while (c2.moveToNext()) {
				if (listAfter == null) {
					listAfter = new ArrayList<IMBaseMessage>();
				}
				IMBaseMessage msg = createIMBaseMessageFromCursor(c2);
				listAfter.add(msg);
				if (listAfter.size() >= span) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c2.close();
		}
		if (listBefore != null) {
			listMsg.addAll(0, listBefore);
			index = listBefore.size();
		}
		if (listAfter != null) {
			listMsg.addAll(listAfter);
		}
		// 返回pair对象
		Pair<Integer, List<IMBaseMessage>> pair = new Pair<Integer, List<IMBaseMessage>>(index, listMsg);
		return pair;
	}

	/**
	 * 查询包含指定关键字的所有聊天记录
	 * 
	 * @param uid
	 * @param chatId
	 * @param keyword
	 * @return
	 */
	public List<IMBaseMessage> queryByKeyword(String uid, String chatId, String keyword) {

		List<IMBaseMessage> listMsg = new ArrayList<IMBaseMessage>();
		if (TextUtils.isEmpty(keyword)) {
			return listMsg;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
					+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND " + DBHelper.COLUMN_CHAT_HIDE + " = ? AND "
					+ DBHelper.COLUMN_CHAT_CONTENT + " LIKE  ?" + " ORDER BY " + DBHelper.COLUMN_CHAT_MSG_INDEX,
					new String[] { uid, chatId, "0", "%" + keyword + "%" });
			try {
				while (cursor.moveToNext()) {
					IMBaseMessage msg = createIMBaseMessageFromCursor(cursor);
					if (msg != null) {
						listMsg.add(msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}
		return listMsg;
	}

	/**
	 * 查询前后文消息记录
	 * 
	 * @param uid
	 * @param chatId
	 * @param msgId
	 * @param span
	 * @return
	 */
	public Pair<Integer, ArrayList<IMBaseMessage>> queryContext(String uid, String chatId, String msgId, int span) {
		// 消息列表
		int index = 0;
		ArrayList<IMBaseMessage> listMsg = new ArrayList<IMBaseMessage>();
		// 前后消息列表
		ArrayList<IMBaseMessage> listBefore = null;
		ArrayList<IMBaseMessage> listAfter = null;
		// 指定消息
		IMBaseMessage specifiedMsg = queryMessageById(uid, chatId, msgId);
		if (specifiedMsg == null) {
			return null;
		}
		listMsg.add(specifiedMsg);
		if (!db.isOpen()) {
			db = helper.getWritableDatabase();
		}
		Cursor c1 = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
				+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND " + DBHelper.COLUMN_CHAT_HIDE + " = ? AND "
				+ DBHelper.COLUMN_CHAT_MSG_INDEX + " < ? " + " LIMIT ?",
				new String[] { uid, chatId, "0", specifiedMsg.getIndex() + "", span + "" });
		try {
			while (c1.moveToNext()) {
				if (listBefore == null) {
					listBefore = new ArrayList<IMBaseMessage>();
				}
				IMBaseMessage msg = createIMBaseMessageFromCursor(c1);
				listBefore.add(0, msg);
				if (listBefore.size() >= span) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c1.close();
		}
		Cursor c2 = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAT_RECORD + " WHERE " + DBHelper.COLUMN_UID
				+ " = ? AND " + DBHelper.COLUMN_CHAT_ID + " = ? AND " + DBHelper.COLUMN_CHAT_HIDE + " = ? AND "
				+ DBHelper.COLUMN_CHAT_MSG_INDEX + " > ? " + " LIMIT ?",
				new String[] { uid, chatId, "0", specifiedMsg.getIndex() + "", span + "" });
		try {
			while (c2.moveToNext()) {
				if (listAfter == null) {
					listAfter = new ArrayList<IMBaseMessage>();
				}
				IMBaseMessage msg = createIMBaseMessageFromCursor(c2);
				listAfter.add(msg);
				if (listAfter.size() >= span) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c2.close();
		}
		if (listBefore != null) {
			listMsg.addAll(0, listBefore);
			index = listBefore.size();
		}
		if (listAfter != null) {
			listMsg.addAll(listAfter);
		}
		// 返回pair对象
		Pair<Integer, ArrayList<IMBaseMessage>> pair = new Pair<Integer, ArrayList<IMBaseMessage>>(index, listMsg);
		return pair;
	}
}
