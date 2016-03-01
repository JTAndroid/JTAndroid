package com.tr.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private final String TAG = getClass().getSimpleName();

	// 数据库名称
	public final static String DATABASE_NAME = "gintong.db";

	// 数据表（状态恢复）
	public final static String TABLE_APP_DATA = "app_data";
	public final static String TABLE_DOWNLOAD_FILE = "download_file";
	public final static String TABLE_CHAT_FILE_DOWNLOAD_INFO = "chat_file_download_info"; // 畅聊文件下载情况
	public final static String TABLE_CHAT_RECORD = "chat_record";
	public final static String TABLE_MEETING_RECORD = "meeting_record"; // 会议记录
	public final static String TABLE_VOICE_FILE = "voice_file"; // 语音文件
	public final static String TABLE_CHAT_LOCAL_FILE = "chat_local_file"; // 畅聊对应的本地文件地址
	public final static String TABLE_SOCIALITY_DATA = "sociality_data"; // 社交本地数据
	public final static String TABLE_SOCIALITY_DATA2 = "sociality_data2"; // 社交本地数据

	/**
	 * 数据库版本号
	 * 
	 * @最后一次修改日期 2015/06/08
	 * @author gushi
	 */
	public final static int DATABASE_VERSION = 40;

	// 数据对象（状态恢复）
	public final static String TABLE_APP_CONNECTIONS = "app_connections";
	public final static String TABLE_APP_CONNECTIONS_BACK = "app_connectionsback";

	/********************************* 关系字段 start by gushi *********************************/

	/** 主键 */
	public final static String COLUMN_CON_KEY_ID = "key_id";
	/** 关系id */
	public final static String COLUMN_CON_ID = "id";
	/** name的首字母,用于按拼音排序 */
	public final static String COLUMN_CON_CHAR = "char";
	/** INTEGER 人或组织的类型. 1-人; 2-组织 */
	public final static String COLUMN_CON_TYPE = "type";
	/** 之前用于列表页的第二行显示 */
	public final static String COLUMN_CON_SOURCEFROM = "sourceFrom";
	/** 名字 */
	public final static String COLUMN_CON_NAME = "name";
	/** 头像地址 */
	public final static String COLUMN_CON_IMAGE = "image";
	/** 是否在线 boolean类型: int转boolean , 非0都是true; 0- false */
	public final static String COLUMN_CON_ISONLINE = "isonline";
	// public final static String TABLE_CON_ISOFFLINEONLINE = "isoffline"; //
	// 取消字段
	/** INTEGER 是否为好友. 0 好友 ; 1 非好友 */
	public final static String COLUMN_CON_FriendState = "FriendState";
	/** INTEGER 是否加入当前组织, 如果加入为 1 ,没有加入为 0 */
	public final static String COLUMN_CON_JoinState = "JoinState";
	/** 电话 */
	public final static String COLUMN_CON_PHONE = "phone";
	/** 邮箱 */
	public final static String COLUMN_CON_EMAIL = "email";
	/** 公司名称 */
	public final static String COLUMN_CON_COMPANY = "company";
	/** 职位名称 */
	public final static String COLUMN_CON_CAREER = "career";
	/** 人脉来源类型：1-我创建的；2-我保存的；3-我收藏的 */
	public final static String COLUMN_CON_FROM_TYPE = "fromType";
	/**人脉性别：1男，2女*/
	public final static String COLUMN_CON_GENDER = "gender";
	/** 手机号 列表 ( 二进至数据格式 ) */
	public final static String COLUMN_CON_MOBILE_PHONE_LIST = "mobilePhoneList";
	/** 固话号 列表 ( 二进至数据格式 ) */
	public final static String COLUMN_CON_FIXED_PHONE_LIST = "fixedPhoneList";

	/********************************* 关系字段 end *********************************/

	/** 全局变量 */
	public final static String COLUMN_UID = "uid";

	// 聊天相关
	public final static String COLUMN_CHAT_ID = "chat_id";
	public final static String COLUMN_MEETING_ID = "meeting_id"; // 主会场id
	public final static String COLUMN_TOPIC_ID = "topic_id"; // 分会场id
	public final static String COLUMN_CHAT_MSG_INDEX = "msg_index"; // 消息群聊中的序号（总)
	public final static String COLUMN_CHAT_MSG_SEQUENCE = "msg_sequence"; // 消息群聊中指定的序号(分）
	public final static String COLUMN_CHAT_RECV_ID = "recv_id"; // 接受方id，如果是群聊，
																// 为会议id
	public final static String COLUMN_CHAT_SENDER_ID = "sender_id";
	public final static String COLUMN_CHAT_SENDER_NAME = "sender_name";// 发送方名称
	public final static String COLUMN_CHAT_TYPE = "type"; // 参考 TYPE_ ,
															// 内容type，0-text；1-audio；2-image；3-video；4-file；5-JTContact(人脉）;6-knowledge(知识）;7-requirement",
	public final static String COLUMN_CHAT_CONTENT = "content";
	public final static String COLUMN_CHAT_TIME = "time"; // 发送时间
	public final static String COLUMN_CHAT_JTFILE = "jt_file";
	public final static String COLUMN_CHAT_MESSAGE_ID = "message_id";// 信息id，客户端生成的随机数
	public final static String COLUMN_CHAT_SEND_TYPE = "send_type";// 发送状态，从服务器获取的聊天记录的状态，都为
																	// SEND_TYPE_SENT
	public final static String COLUMN_CHAT_SENDER_TYPE = "sender_type";// 发送方种类，分
																		// 我，其他人，
																		// 系统发送的三种，参见
																		// MSG_MY_SEND
	public final static String COLUMN_CHAT_IM_TYPE = "im_type";// IM_TYPE_CHAT,
																// IM_TYPE_MUC
	public final static String COLUMN_CHAT_HIDE = "hide";// 是否显示在界面,如果为false,表示显示,默认显示
	public final static String COLUMN_CHAT_LOCAL_FILE_PATH = "local_path"; // 畅聊对应的本地文件路径
	public final static String COLUMN_CHAT_LOCAL_FILE_THUMBNAIL_PATH = "thumbnail_path"; // 畅聊对应的缩略图或截图

	public final static String COLUMN_CHAT_FILE_URL = "cf_url"; // 下载地址
	public final static String COLUMN_CHAT_FILE_TASK_ID = "cf_task_id"; // 任务id
	public final static String COLUMN_CHAT_FILE_DOWNLOADED_SIZE = "cf_downloaded_size"; // 已下载大小
	public final static String COLUMN_CHAT_FILE_TOTAL_SIZE = "cf_total_size"; // 文件大小

	// 语音文件相关
	public final static String COLUMN_VOICE_URL = "url";
	public final static String COLUMN_VOICE_ID = "id";

	// 社交相关
	/**
	 * "id":"私聊为对方的用户id，群聊为muc的id,会议为会议的id", "title":
	 * "社交显示标题 如果群聊，为群聊或者会议的主题；私聊，为好友名称，通知显示为“通知”",
	 * "type":"1-私聊，2-群聊，3-进行中的会议，4-未开始，5-已结束的会议，6-通知，7-邀请函",
	 * "meetingType":"会议类型 0：无主讲  1：有主讲", "listMeetingTopic":"会议对应的议题列表 ",
	 * "compereName":"群聊：主持人姓名，会议 ：会议发起人", "orderTime":"排序时间", "time":"最后更新时间",
	 * "socialDetail":{}
	 * 
	 * ------------------- "senderID": "发送者id", "senderName":"发送方名称", "content":
	 * "社交显示内容，社交类型为聊天时（包含私聊、群聊）封装最后一条聊天记录，会议显示为“会议开始时间”，通知显示通知内容，邀请函显示邀请函内容",
	 * "listImageUrl":[]
	 */

	public final static String COLUMN_SOCIATILY_USERID = "user_id";
	public final static String COLUMN_SOCIATILY_ID = "_id";
	public final static String COLUMN_SOCIATILY_TYPE = "type";

	public final static String COLUMN_SOCIATILY_OBJECT = "sociatily_object";

	public final static String COLUMN_SOCIATILY_ISDELETE = "isdelete"; // 判断该数据
	public final static String COLUMN_SOCIATILY_TIME = "time";
	public final static String COLUMN_SOCIATILY_NEWCOUNT = "newcount";// 新消息条数

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	private static DBHelper mInstance;

	public synchronized static DBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBHelper(context);
		}
		return mInstance;
	};

	/**
	 * Creates database the first time we try to open it.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		onUpgrade(db, 0, DATABASE_VERSION);
	}

	public void createSOCIALITY_DATA2(SQLiteDatabase db) {
		// 社交数据库
		db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_SOCIALITY_DATA2 + "("
				+ COLUMN_SOCIATILY_USERID + " text," + COLUMN_SOCIATILY_ID
				+ " text, " + COLUMN_SOCIATILY_TIME + " text, "
				+ COLUMN_SOCIATILY_NEWCOUNT + " int, "
				+ COLUMN_SOCIATILY_ISDELETE + " text);");
	}

	/**
	 * 
	 * Updates the database format when a content provider is used with a
	 * database that was created with a different format.
	 * 当更新内容供应商使用的是与不同的格式创建一个数据库的数据库格式。
	 * 
	 * Note: to support downgrades, creating a table should always drop it first
	 * if it already exists. 注：为支持降级，创建一个表应该总是先删除它，如果它已经存在。...
	 * 
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String MSG = "onUpgrade()";

		if (oldVersion < 37) {
			// no logic to upgrade from these older version, just recreate the
			// DB
			// 没有逻辑从这些旧版本升级，只是重新创建数据库
			oldVersion = 36;
		} else if (oldVersion > newVersion) {
			// user must have downgraded software; we have no way to know how to
			// downgrade the DB, so just recreate it
			// 用户必须降级软件;我们没有办法知道如何降级的数据库，所以才重新创建
			Log.i(TAG, MSG + "降级数据库从版本号version = " + oldVersion
					+ " (当前版本号version是 " + newVersion + ") 销毁所有旧数据");
			oldVersion = 36;
		}

		for (int version = oldVersion + 1; version <= newVersion; version++) {
			upgradeTo(db, version);
		}

	}

	/**
	 * Upgrade database from (version - 1) to version.
	 * 
	 */
	private void upgradeTo(SQLiteDatabase db, int version) {
		switch (version) {

		case 37:
			createTable(db);
			break;

		case 38:
			addColumn(db, TABLE_APP_CONNECTIONS, COLUMN_CON_FROM_TYPE,
					"INTEGER NOT NULL DEFAULT 0");
			addColumn(db, TABLE_APP_CONNECTIONS_BACK, COLUMN_CON_FROM_TYPE,
					"INTEGER NOT NULL DEFAULT 0");
			break;
		case 39:
			addColumn(db, TABLE_APP_CONNECTIONS, COLUMN_CON_GENDER,
					"INTEGER NOT NULL DEFAULT 0");
			addColumn(db, TABLE_APP_CONNECTIONS_BACK, COLUMN_CON_GENDER,
					"INTEGER NOT NULL DEFAULT 0");
			break;
		case 40:
			db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement, downpath varchar(100), threadid INTEGER, downlength INTEGER)");
			break;
		default:
			throw new IllegalStateException("Don't know how to upgrade to "
					+ version);
		}
	}

	/**
	 * Add a column to a table using ALTER TABLE.
	 * 
	 * @param dbTable
	 *            name of the table
	 * @param columnName
	 *            name of the column to add
	 * @param columnDefinition
	 *            SQL for the column definition
	 */
	private void addColumn(SQLiteDatabase db, String dbTable,
			String columnName, String columnDefinition) {
		db.execSQL("ALTER TABLE " + dbTable + " ADD COLUMN " + columnName + " "
				+ columnDefinition);
	}

	/**
	 * 创建表
	 * 
	 * @param db
	 */
	private void createTable(SQLiteDatabase db) {

		try {

			// 用户信息表
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_APP_DATA
					+ "(key_id INTEGER PRIMARY KEY , app_data BLOB)");

			// 文件下载表
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DOWNLOAD_FILE
					+ "(url CHAR PRIMARY KEY , uid CHAR , jtfile BLOB)");

			// 聊天记录数据库
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CHAT_RECORD + "("
					+ COLUMN_CHAT_MESSAGE_ID + " CHAR, " + COLUMN_UID
					+ " CHAR, " + COLUMN_CHAT_ID + " CHAR, "
					+ COLUMN_CHAT_MSG_INDEX + " INTEGER, "
					+ COLUMN_CHAT_MSG_SEQUENCE + " INTEGER, "
					+ COLUMN_CHAT_RECV_ID + " CHAR, " + COLUMN_CHAT_SENDER_ID
					+ " CHAR, " + COLUMN_CHAT_SENDER_NAME + " CHAR, "
					+ COLUMN_CHAT_TYPE + " INTEGER, " + COLUMN_CHAT_CONTENT
					+ " CHAR, " + COLUMN_CHAT_TIME + " CHAR, "
					+ COLUMN_CHAT_JTFILE + " BLOB, " + COLUMN_CHAT_SEND_TYPE
					+ " INTEGER, " + COLUMN_CHAT_SENDER_TYPE + " INTEGER, "
					+ COLUMN_CHAT_IM_TYPE + " INTEGER, " + COLUMN_CHAT_HIDE
					+ " BOOLEAN)");

			// 会议记录数据库
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MEETING_RECORD
					+ "(" + COLUMN_CHAT_MESSAGE_ID + " CHAR, " + COLUMN_UID
					+ " CHAR, " + COLUMN_MEETING_ID + " CHAR, "
					+ COLUMN_TOPIC_ID + " CHAR, " + COLUMN_CHAT_MSG_INDEX
					+ " INTEGER, " + COLUMN_CHAT_SENDER_ID + " CHAR, "
					+ COLUMN_CHAT_SENDER_NAME + " CHAR, " + COLUMN_CHAT_TYPE
					+ " INTEGER, " + COLUMN_CHAT_CONTENT + " CHAR, "
					+ COLUMN_CHAT_TIME + " CHAR, " + COLUMN_CHAT_JTFILE
					+ " BLOB, " + COLUMN_CHAT_SEND_TYPE + " INTEGER, "
					+ COLUMN_CHAT_SENDER_TYPE + " INTEGER, "
					+ COLUMN_CHAT_IM_TYPE + " INTEGER, " + COLUMN_CHAT_HIDE
					+ " BOOLEAN)");

			// 畅聊文件下载信息
			db.execSQL("CREATE TABLE IF NOT EXISTS "
					+ TABLE_CHAT_FILE_DOWNLOAD_INFO + "(" + COLUMN_UID
					+ " CHAR, " + COLUMN_CHAT_FILE_URL + " CHAR, "
					+ COLUMN_CHAT_FILE_DOWNLOADED_SIZE + " INTEGER, "
					+ COLUMN_CHAT_FILE_TOTAL_SIZE + " INTEGER)");

			// 畅聊本地文件地址（上传用）
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CHAT_LOCAL_FILE
					+ "(" + COLUMN_UID + " CHAR, " + COLUMN_CHAT_MESSAGE_ID
					+ " CHAR, " + COLUMN_CHAT_LOCAL_FILE_PATH + " CHAR)");

			// 关系数据库
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_CONNECTIONS);
			db.execSQL("CREATE TABLE " + TABLE_APP_CONNECTIONS + "("
					+ COLUMN_CON_KEY_ID + " INTEGER PRIMARY KEY ," + COLUMN_UID
					+ " CHAR , " + COLUMN_CON_ID + " char(30) ,"
					+ COLUMN_CON_CHAR + " char(3) ," + COLUMN_CON_TYPE
					+ " INTEGER ," + COLUMN_CON_SOURCEFROM + " char(80) ,"
					+ COLUMN_CON_NAME + " char(80) ," + COLUMN_CON_IMAGE
					+ " char(60) ," + COLUMN_CON_ISONLINE + " Boolean ,"
					+ COLUMN_CON_FriendState + " INTEGER ,"
					+ COLUMN_CON_JoinState + " INTEGER ," + COLUMN_CON_PHONE
					+ " char(30) ," + COLUMN_CON_EMAIL + " char(30),"

					+ COLUMN_CON_COMPANY + " char(80)," + COLUMN_CON_CAREER
					+ " char(80)," + COLUMN_CON_MOBILE_PHONE_LIST + " BLOB,"
					+ COLUMN_CON_FIXED_PHONE_LIST + " BLOB)");

			// 关系备份数据库
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_CONNECTIONS_BACK);
			db.execSQL("CREATE TABLE " + TABLE_APP_CONNECTIONS_BACK + "("
					+ COLUMN_CON_KEY_ID + " INTEGER PRIMARY KEY ," + COLUMN_UID
					+ " CHAR , " + COLUMN_CON_ID + " char(30) ,"
					+ COLUMN_CON_CHAR + " char(3) ," + COLUMN_CON_TYPE
					+ " INTEGER ," + COLUMN_CON_SOURCEFROM + " char(80) ,"
					+ COLUMN_CON_NAME + " char(80) ," + COLUMN_CON_IMAGE
					+ " char(60) ," + COLUMN_CON_ISONLINE + " Boolean ,"
					+ COLUMN_CON_FriendState + " INTEGER ,"
					+ COLUMN_CON_JoinState + " INTEGER ," + COLUMN_CON_PHONE
					+ " char(30) ," + COLUMN_CON_EMAIL + " char(30) ,"

					+ COLUMN_CON_COMPANY + " char(80)," + COLUMN_CON_CAREER
					+ " char(80)," + COLUMN_CON_MOBILE_PHONE_LIST + " BLOB,"
					+ COLUMN_CON_FIXED_PHONE_LIST + " BLOB)");

			// 语音文件数据表
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_VOICE_FILE + "("
					+ COLUMN_UID + " CHAR, " + COLUMN_VOICE_URL + " CHAR, "
					+ COLUMN_VOICE_ID + " INTEGER)");

			// 社交数据库
			db.execSQL("create table sociality_data(" + COLUMN_SOCIATILY_USERID
					+ " text," + COLUMN_SOCIATILY_ID + " text, "
					+ COLUMN_SOCIATILY_TYPE + " int," + COLUMN_SOCIATILY_OBJECT
					+ " BLOB, " + COLUMN_SOCIATILY_ISDELETE + " int);");

			// 社交数据库
			db.execSQL("create table sociality_data2("
					+ COLUMN_SOCIATILY_USERID + " text," + COLUMN_SOCIATILY_ID
					+ " text, " + COLUMN_SOCIATILY_TIME + " text, "
					+ COLUMN_SOCIATILY_NEWCOUNT + " int, "
					+ COLUMN_SOCIATILY_ISDELETE + " text);");

		} catch (SQLiteException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 对象转字节数组
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static byte[] objectToBytes(Object obj) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream sOut = new ObjectOutputStream(out);
		sOut.writeObject(obj);
		sOut.flush();
		byte[] bytes = out.toByteArray();
		return bytes;
	}

	/**
	 * 字节数组转对象
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static Object bytesToObject(byte[] bytes) throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		ObjectInputStream sIn = new ObjectInputStream(in);
		return sIn.readObject();
	}
}
