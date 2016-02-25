package com.tr.db;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.tr.App;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MeetingMessage;

/**
 * 会议消息数据库管理类
 * @author leon
 */
public class MeetingRecordDBManager{

	private final String TAG = getClass().getSimpleName();
	
	private DBHelper helper;
	private SQLiteDatabase db;

	/**
	 * 构造函数
	 * @param context
	 */
	public MeetingRecordDBManager(Context context) {
		helper = DBHelper.getInstance(context);
		synchronized (helper) {
			db = helper.getWritableDatabase();
		}
	}
	
	/**
	 * 插入单条记录
	 * @param uid
	 * @param msg
	 */
	public void insert(String uid, MeetingMessage msg) {
		
		if(msg == null){
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction(); // 开始事务
			try {
				db.execSQL("INSERT INTO " + DBHelper.TABLE_MEETING_RECORD + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 
						new Object[] { msg.getMessageID(), App.getUserID(), msg.getRecvID(), msg.getTopicID(), msg.getIndex(),
						msg.getSenderID(),msg.getSenderName(),msg.getType(),msg.getContent(),msg.getTime(),
						DBHelper.objectToBytes(msg.getJtFile()), msg.getSendType(), msg.getSenderType(), msg.getImtype(), msg.isHide()});
				db.setTransactionSuccessful(); 
			} 
			catch (Exception e) {
				Log.d(TAG, e.getMessage());
			} 
			finally {
				db.endTransaction(); // 结束事务
				db.close();
			}
		}
	}
	
	/**
	 * 插入多条记录
	 * @param uid
	 * @param listMsg
	 */
	public void insert(String uid, List<MeetingMessage> listMsg) {
		if (listMsg != null && listMsg.size() > 0) {
			for (MeetingMessage msg : listMsg) {
				insert(uid, msg);
			}
		}
	}
	
	/**
	 * 更新单条记录
	 * @param uid
	 * @param msg
	 */
	public void update(String uid, MeetingMessage msg){
		
		if(msg == null){
			return;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction(); 
			try {
				db.execSQL("UPDATE " + DBHelper.TABLE_MEETING_RECORD + " SET "
						+ DBHelper.COLUMN_MEETING_ID + " = ?, "
						+ DBHelper.COLUMN_TOPIC_ID + " = ?, "
						+ DBHelper.COLUMN_CHAT_MSG_INDEX + " = ?, " 
						+ DBHelper.COLUMN_CHAT_SENDER_ID + " = ?, " 
						+ DBHelper.COLUMN_CHAT_SENDER_NAME + " = ?, " 
						+ DBHelper.COLUMN_CHAT_TYPE + " = ?, " 
						+ DBHelper.COLUMN_CHAT_CONTENT + " = ?, " 
						+ DBHelper.COLUMN_CHAT_TIME + " = ?, " 
						+ DBHelper.COLUMN_CHAT_JTFILE + " = ?, " 
						+ DBHelper.COLUMN_CHAT_SEND_TYPE + " = ?, " 
						+ DBHelper.COLUMN_CHAT_SENDER_TYPE + " = ?, " 
						+ DBHelper.COLUMN_CHAT_HIDE + " = ?, "
						+ DBHelper.COLUMN_CHAT_IM_TYPE + " = ? WHERE "
						+ DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ? AND "
						+ DBHelper.COLUMN_UID + " = ?",
						new Object[] { msg.getRecvID(), msg.getTopicID(), msg.getIndex() ,
							msg.getSenderID(),msg.getSenderName(),msg.getType(),msg.getContent(),msg.getTime(),
							DBHelper.objectToBytes(msg.getJtFile()),msg.getSendType(),msg.getSenderType(), msg.isHide(), msg.getImtype(), 
							msg.getMessageID(), App.getUserID()});
				db.setTransactionSuccessful(); 
			} 
			catch (SQLException e) {
				Log.d(TAG, e.getMessage());
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
	 * 更新多条记录
	 * @param uid
	 * @param listMsg
	 */
	public void update(String uid, List<MeetingMessage> listMsg) {
		if (listMsg != null && listMsg.size() > 0) {
			for (MeetingMessage msg : listMsg) {
				
				if (queryExistence(uid, msg.getMessageID())) { // 存在更新
					if(queryVisibility(uid, msg.getMessageID())){ // 记录可见
						update(uid, msg);
					}
				} 
				else { // 不存在插入
					insert(uid, msg);
				}
			}
		}
	}
	
	/**
	 * 删除单条记录
	 * @param uid
	 * @param messageId
	 */
	public void delete(String uid, String messageId){
		
		synchronized (helper) {
			if(!db.isOpen()){
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("DELETE FROM " + DBHelper.TABLE_MEETING_RECORD
						+ " WHERE " + DBHelper.COLUMN_UID + " = ? AND "
						+ DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ?",
						new String[] { uid, messageId });
				db.setTransactionSuccessful();
			}
			catch(Exception e){
				Log.d(TAG, e.getMessage());
			}
			finally {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	/**
	 * 删除分会场的所有聊天记录
	 * @param uid
	 * @param chatId
	 * @param topicId
	 */
	public void deleteAll(String uid, String meetingId, String topicId){
		
		synchronized (helper) {
			if(!db.isOpen()){
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				db.execSQL("DELETE FROM " + DBHelper.TABLE_MEETING_RECORD
						+ " WHERE " + DBHelper.COLUMN_UID + " = ? AND "
						+ DBHelper.COLUMN_MEETING_ID + " = ? AND "
						+ DBHelper.COLUMN_TOPIC_ID + " = ?",
						new String[] { uid, meetingId, topicId });
				db.setTransactionSuccessful();
			}
			catch(Exception e){
				Log.e(TAG, "deleteAll" + e.getMessage());
			}
			finally {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	// 查询单条记录
	private boolean queryExistence(String uid, String messageId) {
		boolean result = false;
		synchronized(helper){
			if(!db.isOpen()){
				db = helper.getWritableDatabase();
			}
			Cursor cursor = queryTheCursor(uid, messageId);
			try {
				while (cursor.moveToNext()) {
					result = true;
				}
			} 
			catch (Exception e) {
				Log.e(TAG, e.getMessage());
			} 
			finally {
				cursor.close();
			}
		}
		return result;
	}
	
	// 查询记录是否隐藏
	public boolean queryVisibility(String uid, String messageId){
		boolean visible = true;
		MeetingMessage msg = query(uid, messageId);
		if(msg != null){ // 记录不存在
			visible = !msg.isHide();
		}
		return visible;
	}
	
	
	/**
	 * 分页返回指定项聊天记录
	 * @param uid
	 * @param chatId
	 * @param topicId
	 * @param index
	 * @param size
	 * @return
	 */
	/*
	public JTPage query(String uid, String meetingId, String topicId, int index, int size) {
		JTPage jtPage = null;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			jtPage = new JTPage();
			int total = queryCount(uid, meetingId, topicId);
			int totalPage = (int) Math.ceil(total * 1.0 / size);
			jtPage.setTotalPage(totalPage); // 总页数
			jtPage.setTotal(total); // 总数
			jtPage.setLists(new ArrayList<IPageBaseItem>()); // 数据项
			// 是否有更多数据
			if (index + 1 > totalPage) {
				jtPage.setIndex(index);
				return jtPage;
			}
			else{
				jtPage.setIndex(index + 1);
			}
			Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_MEETING_RECORD 
					+ " WHERE "
					+ DBHelper.COLUMN_UID + " = ? AND "
					+ DBHelper.COLUMN_MEETING_ID + " = ? AND "
					+ DBHelper.COLUMN_TOPIC_ID + " = ? AND "
					+ DBHelper.COLUMN_CHAT_HIDE + " = ? AND "
					+ "ASC LIMIT ?,?",
					new String[] { uid, meetingId, topicId, "false", index * size + "",  size + "" });
			try {
				while (c.moveToNext()) {
					MeetingMessage msg = createMessageFromCursor(c);
					jtPage.getLists().add(msg);
					jtPage.setSize(jtPage.getLists().size());
				}
			} 
			catch (Exception e) {
				Log.d(TAG, e.getMessage());
			} 
			finally {
				c.close();
			}
		}
		return jtPage;
	}
	*/
	
	/**
	 * 查找指定id的消息对象
	 * @param uid
	 * @param messageId
	 * @return
	 */
	public MeetingMessage query(String uid, String messageId){
		
		MeetingMessage msg = null;
		synchronized(helper){
			if(!db.isOpen()){
				db = helper.getWritableDatabase();
			}
			Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_MEETING_RECORD 
					+ " WHERE "
					+ DBHelper.COLUMN_UID + " = ? AND "
					+ DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ?",
					new String[] { uid, messageId});
			try {
				while (c.moveToNext()) {
					msg = createMessageFromCursor(c);
					break;
				}
			} 
			catch (Exception e) {
				Log.d(TAG, e.getMessage() + "");
			} 
			finally {
				c.close();
			}
		}
		return msg;
	}
	
	/**
	 * 返回指定分会场的全部记录
	 * @param uid
	 * @param chatId
	 * @param topicId
	 * @return
	 */
	public List<MeetingMessage> query(String uid, String meetingId, String topicId) {
		List<MeetingMessage> listMsg = new ArrayList<MeetingMessage>();
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_MEETING_RECORD 
					+ " WHERE "
					+ DBHelper.COLUMN_UID + " = ? AND "
					+ DBHelper.COLUMN_MEETING_ID + " = ? AND "
					+ DBHelper.COLUMN_TOPIC_ID + " = ? AND "
					+ DBHelper.COLUMN_CHAT_HIDE + " = ?"
					+ " ORDER BY " 
					+ DBHelper.COLUMN_CHAT_TIME,
					new String[] { uid, meetingId, topicId, "0"});
			try {
				while (c.moveToNext()) {
					MeetingMessage msg = createMessageFromCursor(c);
					listMsg.add(msg);
				}
			} 
			catch (Exception e) {
				Log.d(TAG, e.getMessage() + "");
			} 
			finally {
				c.close();
			}
		}
		return listMsg;
	}
	
	
	/**
	 * 返回包含指定关键字的全部记录（除系统消息外）
	 * @param uid
	 * @param chatId
	 * @param topicId
	 * @param keyword
	 * @return
	 */
	public List<MeetingMessage> query(String uid, String meetingId, String topicId, String keyword) {
		List<MeetingMessage> listMsg = new ArrayList<MeetingMessage>();
		if(TextUtils.isEmpty(keyword)){
			return listMsg;
		}
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_MEETING_RECORD 
					+ " WHERE "
					+ DBHelper.COLUMN_UID + " = ? AND "
					+ DBHelper.COLUMN_MEETING_ID + " = ? AND "
					+ DBHelper.COLUMN_TOPIC_ID + " = ? AND "
					+ DBHelper.COLUMN_CHAT_HIDE + " = ? AND "
					+ DBHelper.COLUMN_CHAT_SENDER_TYPE + " != ? AND "
				    + DBHelper.COLUMN_CHAT_CONTENT + " LIKE '%" + keyword + "%'"
					+ " ORDER BY " 
					+ DBHelper.COLUMN_CHAT_TIME,
					new String[] { uid, meetingId, topicId, "0", "2"});
			try {
				while (c.moveToNext()) {
					MeetingMessage msg = createMessageFromCursor(c);
					listMsg.add(msg);
				}
			} 
			catch (Exception e) {
				Log.d(TAG, e.getMessage() + "");
			} 
			finally {
				c.close();
			}
		}
		return listMsg;
	}
	
	/**
	 * 返回指定消息前后span条聊天记录
	 * @param uid
	 * @param meetingId
	 * @param topicId
	 * @param msgId
	 * @param span
	 * @return
	 */
	public Pair<Integer, List<MeetingMessage>> queryContext(String uid, String meetingId, String topicId, String messageId, int span){
		
		// 消息列表
		int index = 0;
		List<MeetingMessage> listMsg = new ArrayList<MeetingMessage>();
		// 前后消息列表
		List<MeetingMessage> listBefore = null;
		List<MeetingMessage> listAfter = null;
		// 指定消息
		MeetingMessage specifiedMsg = query(uid, messageId);
		// 指定消息位置
		if(specifiedMsg == null){
			return null;
		}
		listMsg.add(specifiedMsg);
		Cursor c1 = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_MEETING_RECORD  
				+ " WHERE "
				+ DBHelper.COLUMN_UID + " = ? AND "
				+ DBHelper.COLUMN_MEETING_ID + " = ? AND "
				+ DBHelper.COLUMN_TOPIC_ID + " = ? AND "
				+ DBHelper.COLUMN_CHAT_HIDE + " = ? AND "
				+ DBHelper.COLUMN_CHAT_TIME + " < ?",
				new String[] { uid, meetingId, topicId, "0", specifiedMsg.getTime()});
		try {
			while (c1.moveToNext()) {
				if(listBefore == null){
					listBefore = new ArrayList<MeetingMessage>();
				}
				MeetingMessage msg = createMessageFromCursor(c1);
				listBefore.add(0, msg);
				if(listBefore.size() >= span){
					break;
				}
			}
		} 
		catch (Exception e) {
			Log.d(TAG, e.getMessage() + "");
		} 
		finally {
			c1.close();
		}
		Cursor c2 = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_MEETING_RECORD  
				+ " WHERE "
				+ DBHelper.COLUMN_UID + " = ? AND "
				+ DBHelper.COLUMN_MEETING_ID + " = ? AND "
				+ DBHelper.COLUMN_TOPIC_ID + " = ? AND "
				+ DBHelper.COLUMN_CHAT_HIDE + " = ? AND "
				+ DBHelper.COLUMN_CHAT_TIME + " > ?",
				new String[] { uid, meetingId, topicId, "0", specifiedMsg.getTime()});
		try {
			while (c2.moveToNext()) {
				if(listAfter == null){
					listAfter = new ArrayList<MeetingMessage>();
				}
				MeetingMessage msg = createMessageFromCursor(c2);
				listAfter.add(msg);
				if(listAfter.size() >= span){
					break;
				}
			}
		} 
		catch (Exception e) {
			Log.d(TAG, e.getMessage() + "");
		} 
		finally {
			c2.close();
		}
		if(listBefore != null){
			listMsg.addAll(0, listBefore);
			index = listBefore.size();
		}
		if(listAfter != null){
			listMsg.addAll(listAfter);
		}
		// 返回pair对象
		Pair<Integer, List<MeetingMessage>> pair = new Pair<Integer, List<MeetingMessage>>(index, listMsg);
		return pair;
	}
	
	// 从Cursor创建Message对象
	private MeetingMessage createMessageFromCursor(Cursor cursor){
		MeetingMessage msg = new MeetingMessage();
		msg.setSenderName(cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_CHAT_SENDER_NAME)));
		msg.setContent(cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_CHAT_CONTENT)));
		msg.setMessageID(cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_CHAT_MESSAGE_ID)));
		msg.setIndex(cursor.getInt(cursor
				.getColumnIndex(DBHelper.COLUMN_CHAT_MSG_INDEX)));
		msg.setRecvID(cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_MEETING_ID)));
		msg.setTopicID(cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_TOPIC_ID)));
		msg.setSenderID(cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_CHAT_SENDER_ID)));
		msg.setType(cursor.getInt(cursor
				.getColumnIndex(DBHelper.COLUMN_CHAT_TYPE)));
		msg.setTime(cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_CHAT_TIME)));
		try {
			msg.setJtFile((JTFile) DBHelper.bytesToObject(cursor.getBlob(cursor
					.getColumnIndex(DBHelper.COLUMN_CHAT_JTFILE))));
		} 
		catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		msg.setSendType(cursor.getInt(cursor
				.getColumnIndex(DBHelper.COLUMN_CHAT_SEND_TYPE)));
		msg.setSenderType(cursor.getInt(cursor
				.getColumnIndex(DBHelper.COLUMN_CHAT_SENDER_TYPE)));
		msg.setImtype(cursor.getInt(cursor
				.getColumnIndex(DBHelper.COLUMN_CHAT_IM_TYPE)));
		int hide = cursor.getInt(cursor
				.getColumnIndex(DBHelper.COLUMN_CHAT_HIDE));
		msg.setHide(hide == 0 ? false : true);
		return msg;
	}
		
	
	/**
	 * 获取符合条件的记录数
	 * @param uid
	 * @param chatId
	 * @param topicId
	 * @return
	 */
	public int queryCount(String uid, String meetingId, String topicId) {
		int count = 0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}		
			Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + DBHelper.TABLE_MEETING_RECORD 
					+ " WHERE "
					+ DBHelper.COLUMN_UID + " = ? AND"
					+ DBHelper.COLUMN_MEETING_ID + " = ? AND"
					+ DBHelper.COLUMN_TOPIC_ID + " = ? AND"
					+ DBHelper.COLUMN_CHAT_HIDE + " = ?", 
					new String[] { uid, meetingId, topicId, "0"});
			try {
				while (c.moveToNext()) {
					count++;
				}
			} 
			catch (Exception e) {
				Log.d(TAG, e.getMessage());
			} 
			finally {
				c.close();
			}
		}
		return count;
	}

	
	/**
	 * 返回图片消息数
	 * @param uid
	 * @param meetingId
	 * @param topicId
	 * @param messageId
	 * @param span
	 * @return
	 */
	public Pair<Integer, List<MeetingMessage>> queryImageItem(String uid, String meetingId, String topicId, String messageId, int span){
		
		// 消息列表
		int index = 0;
		List<MeetingMessage> listMsg = new ArrayList<MeetingMessage>();
		// 前后消息列表
		List<MeetingMessage> listBefore = null;
		List<MeetingMessage> listAfter = null;
		// 指定消息
		MeetingMessage specifiedMsg = query(uid, messageId);
		// 指定消息位置
		if(specifiedMsg == null){
			return null;
		}
		listMsg.add(specifiedMsg);
		Cursor c1 = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_MEETING_RECORD  
				+ " WHERE "
				+ DBHelper.COLUMN_UID + " = ? AND "
				+ DBHelper.COLUMN_CHAT_TYPE + " = ? AND "
				+ DBHelper.COLUMN_MEETING_ID + " = ? AND "
				+ DBHelper.COLUMN_TOPIC_ID + " = ? AND "
				+ DBHelper.COLUMN_CHAT_HIDE + " = ? AND "
				+ DBHelper.COLUMN_CHAT_MSG_INDEX + " < ?",
				new String[] { uid, IMBaseMessage.TYPE_IMAGE + "", meetingId, topicId, "0", specifiedMsg.getIndex() + ""});
		try {
			while (c1.moveToNext()) {
				if(listBefore == null){
					listBefore = new ArrayList<MeetingMessage>();
				}
				MeetingMessage msg = createMessageFromCursor(c1);
				listBefore.add(0, msg);
				if(listBefore.size() >= span){
					break;
				}
			}
		} 
		catch (Exception e) {
			Log.d(TAG, e.getMessage() + "");
		} 
		finally {
			c1.close();
		}
		Cursor c2 = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_MEETING_RECORD  
				+ " WHERE "
				+ DBHelper.COLUMN_UID + " = ? AND "
				+ DBHelper.COLUMN_CHAT_TYPE + " = ? AND "
				+ DBHelper.COLUMN_MEETING_ID + " = ? AND "
				+ DBHelper.COLUMN_TOPIC_ID + " = ? AND "
				+ DBHelper.COLUMN_CHAT_HIDE + " = ? AND "
				+ DBHelper.COLUMN_CHAT_MSG_INDEX + " > ?",
				new String[] { uid, IMBaseMessage.TYPE_IMAGE + "", meetingId, topicId, "0", specifiedMsg.getIndex() + ""});
		try {
			while (c2.moveToNext()) {
				if(listAfter == null){
					listAfter = new ArrayList<MeetingMessage>();
				}
				MeetingMessage msg = createMessageFromCursor(c2);
				listAfter.add(msg);
				if(listAfter.size() >= span){
					break;
				}
			}
		} 
		catch (Exception e) {
			Log.d(TAG, e.getMessage() + "");
		} 
		finally {
			c2.close();
		}
		if(listBefore != null){
			listMsg.addAll(0, listBefore);
			index = listBefore.size();
		}
		if(listAfter != null){
			listMsg.addAll(listAfter);
		}
		// 返回pair对象
		Pair<Integer, List<MeetingMessage>> pair = new Pair<Integer, List<MeetingMessage>>(index, listMsg);
		return pair;
	}
	
	
	// 获取指向指定记录的Cursor
	private Cursor queryTheCursor(String uid, String messageId) {
		Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_MEETING_RECORD
				+ " WHERE " 
				+ DBHelper.COLUMN_UID + " = ? AND "
				+ DBHelper.COLUMN_CHAT_MESSAGE_ID + " = ?",
				new String[] { uid, messageId});		
		return c;
	}
	
}
