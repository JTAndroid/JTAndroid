package com.tr.db;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.tr.App;
import com.tr.model.obj.Connections;
import com.tr.model.obj.MobilePhone;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.JTPage;
import com.tr.model.user.OrganizationMini;
import com.utils.common.EConsts;
import com.utils.string.StringUtils;

/**
 * 关系数据库Manager
 */
public class ConnectionsDBManager {
	
	private String tableName=DBHelper.TABLE_APP_CONNECTIONS_BACK;
	private static ConnectionsDBManager connectionsDBManager = null;
	
	
	// 常量
	private final String TAG = getClass().getSimpleName();

	// 变量
	private DBHelper helper;
	private SQLiteDatabase db;
	private Context mContext=null;

	/** 加一个无表名的 构造函数 为了 写单例方法 */
	public ConnectionsDBManager(Context context) {
		mContext=context;
		helper = DBHelper.getInstance(context);
		synchronized (helper) {
			db = helper.getWritableDatabase();
		}
	}
	
	public ConnectionsDBManager(Context context,String name) {
		tableName=name;
		mContext=context;
		helper = DBHelper.getInstance(context);
		synchronized (helper) {
			db = helper.getWritableDatabase();
		}
	}
	
	/** 单例  */
	public static ConnectionsDBManager getInstance(Context context){
		String tableName = getConnectionsTableName(context);
		if(connectionsDBManager == null){
			connectionsDBManager = new ConnectionsDBManager(context);
		}
		connectionsDBManager.setTableName(tableName);
		return connectionsDBManager;
	}
	
	
	public String getTableName(){
		return tableName;
	}
	public void setTableName(String name){
		tableName=name;
	}
	
	/** 这个应该是工具类方法 以后找个公共的工具类 放里面 */
	private static String getConnectionsTableName(Context context){
		SharedPreferences sp = context.getSharedPreferences(EConsts.share_firstLoginGetConnections, Activity.MODE_PRIVATE);
		String tableName = sp.getString(EConsts.share_itemUserTableName, "");
		if(StringUtils.isEmpty(tableName)){
			tableName = DBHelper.TABLE_APP_CONNECTIONS;
		}
		else{
			if(tableName.equals(DBHelper.TABLE_APP_CONNECTIONS)){
				tableName = DBHelper.TABLE_APP_CONNECTIONS;
			}
			else if(tableName.equals(DBHelper.TABLE_APP_CONNECTIONS_BACK)){
				tableName = DBHelper.TABLE_APP_CONNECTIONS_BACK;
			}
			else{
				tableName = DBHelper.TABLE_APP_CONNECTIONS;
			}
		}
		
		return tableName;
	}
	
	
	/** 这是一个工具方法, 为了避免写联系人列表界面的时候写重复代码 */
	public static ConnectionsDBManager buildConnectionsDBManager(Context context){
		ConnectionsDBManager connectionsDBManager = null ;
		
		SharedPreferences sp = context.getSharedPreferences(EConsts.share_firstLoginGetConnections, Activity.MODE_PRIVATE);
		String tableName = sp.getString(EConsts.share_itemUserTableName, "");
		if(StringUtils.isEmpty(tableName)){
			connectionsDBManager = new ConnectionsDBManager(context, DBHelper.TABLE_APP_CONNECTIONS);
		}
		else{
			if(tableName.equals(DBHelper.TABLE_APP_CONNECTIONS)){
				connectionsDBManager = new ConnectionsDBManager(context, DBHelper.TABLE_APP_CONNECTIONS);
			}
			else if(tableName.equals(DBHelper.TABLE_APP_CONNECTIONS_BACK)){
				connectionsDBManager = new ConnectionsDBManager(context, DBHelper.TABLE_APP_CONNECTIONS_BACK);
			}
			else{
				connectionsDBManager = new ConnectionsDBManager(context, DBHelper.TABLE_APP_CONNECTIONS);
			}
		}
		// App.getApp().setConnectionsDBManager(connectionsDBManager);
		
		
		return connectionsDBManager;
	}
	
	
	
	public Context getContext(){
		return mContext;
	}
	
	/**
	 * 插入联系人
	 * @param connections
	 */
	public void insert(Connections connections) {
		try {
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction(); // 开始事务
			try {	
				ContentValues cv =  makeCV(connections);
				db.insert(tableName, null, cv);
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
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
		/**
		 *  插入联系人列表
		 * @param cns_data
		 */
		public void insert(ArrayList<Connections> cns_data) {
			synchronized (helper) {
				if (!db.isOpen()) {
					db = helper.getWritableDatabase();
				}
				db.beginTransaction(); // 开始事务
				try {	
					ContentValues cv = null;
					for(Connections connections:cns_data){
						cv = makeCV(connections);
						db.insert(tableName, null, cv);
					}
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
	

	// 更新,只是替换数据的，够用了。如果名字改了，删除重新添加
	public void update(Connections connections) {
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction(); // 开始事务
			try {
				ContentValues cv=makeCV(connections);
				int isOnLine=0;
				if(connections.isOnline()){
					isOnLine=1;
				}else{
					isOnLine=0;
				}
//				int isOfflineLine=0;
//				if(connections.isOffline()){
//					isOfflineLine=1;
//				}else{
//					isOfflineLine=0;
//				}
				db.update(tableName, cv
						 , DBHelper.COLUMN_UID + " = ? and " 
						+ DBHelper.COLUMN_CON_ID + " = ? and " 
						+ DBHelper.COLUMN_CON_TYPE + " = ? and "
						+ DBHelper.COLUMN_CON_ISONLINE + " = ? "
						, new String[] { 
						App.getUserID()
						, connections.getId() + ""
						, connections.type + ""
						, isOnLine + ""
						});
				
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

		public void delInsert(String id,int type,boolean isOnline ,Connections insertCon) {
			synchronized (helper) {
				if (!db.isOpen()) {
					db = helper.getWritableDatabase();
				}
				db.beginTransaction(); // 开始事务
				try {
					int isOnLineint=0;
					if(isOnline){
						isOnLineint=1;
					}else{
						isOnLineint=0;
					}
//					int isOfflineLine=0;
//					if(isOffline){
//						isOfflineLine=1;
//					}else{
//						isOfflineLine=0;
//					}
					db.delete(tableName
							, DBHelper.COLUMN_UID+" = ? and " 
							+ DBHelper.COLUMN_CON_ID+" = ? and " 
							+ DBHelper.COLUMN_CON_TYPE +" = ? and "
							+ DBHelper.COLUMN_CON_ISONLINE + " = ? ",
							new String[] { 
							App.getUserID()
							, id +""
							, type+""
							, isOnLineint+""
							} );
					ContentValues cv=makeCV(insertCon);
					db.insert(tableName, null, cv);
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

		
	public ContentValues makeCV(Connections connections){
		ContentValues cv = new ContentValues();	
		cv.put(DBHelper.COLUMN_UID, App.getUserID());
		cv.put(DBHelper.COLUMN_CON_ID, connections.getId());
		cv.put(DBHelper.COLUMN_CON_CHAR, connections.getCharName()+"");
		cv.put(DBHelper.COLUMN_CON_TYPE, connections.type);
		cv.put(DBHelper.COLUMN_CON_SOURCEFROM, connections.getmSourceFrom());
		cv.put(DBHelper.COLUMN_CON_NAME, connections.getName());
		cv.put(DBHelper.COLUMN_CON_IMAGE, connections.getImage());
		cv.put(DBHelper.COLUMN_CON_ISONLINE, connections.isOnline());
		//cv.put(DBHelper.TABLE_CON_ISOFFLINEONLINE, connections.isOffline());
		cv.put(DBHelper.COLUMN_CON_FriendState, connections.getFriendState());
		cv.put(DBHelper.COLUMN_CON_JoinState, connections.organizationMini.mJoinState);
		cv.put(DBHelper.COLUMN_CON_PHONE, connections.getMobilePhone());
		cv.put(DBHelper.COLUMN_CON_EMAIL, connections.getEmail());
		
		cv.put(DBHelper.COLUMN_CON_COMPANY, connections.getCompany());
		cv.put(DBHelper.COLUMN_CON_CAREER, connections.getCareer());
		cv.put(DBHelper.COLUMN_CON_FROM_TYPE, connections.getFromType());
		cv.put(DBHelper.COLUMN_CON_GENDER, connections.getGender());
		try {
			cv.put(DBHelper.COLUMN_CON_MOBILE_PHONE_LIST, DBHelper.objectToBytes(connections.getMobilePhoneList()));
			cv.put(DBHelper.COLUMN_CON_FIXED_PHONE_LIST, DBHelper.objectToBytes(connections.getFixedPhoneList()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return cv;
	}

	// 根据Cursor生成Connections对象
	public Connections getConnection(Cursor cursor) {
		Connections connections = new Connections();
		connections.sqlDBKeyId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CON_KEY_ID));
		connections.type = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CON_TYPE));
		String tempCharStr = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CON_CHAR));
		if (!StringUtils.isEmpty(tempCharStr)) {
			connections.setCharName(tempCharStr.charAt(0));
		}
		connections.setID(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CON_ID)));
		connections.setmSourceFrom(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CON_SOURCEFROM)));
		connections.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CON_NAME)));
		connections.setImage(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CON_IMAGE)));
		connections.setGender(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CON_GENDER)));
		int booleanInt = 0;
		booleanInt = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CON_ISONLINE));
		if (booleanInt != 0) {
			connections.setOnline(true);
		}
		else {
			connections.setOnline(false);
		}
		// booleanInt=cursor.getInt(
		// cursor.getColumnIndex(DBHelper.TABLE_CON_ISOFFLINEONLINE));
		// if(booleanInt==1){
		// connections.setOffline(true);
		// }else{
		// connections.setOffline(false);
		// }
		connections.setFriendState(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CON_FriendState)));
		connections.setJoinState(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CON_JoinState)));
		connections.setEMail(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CON_EMAIL)));
		connections.setPhone(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CON_PHONE)));
		
		connections.setCompany(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CON_COMPANY)));
		connections.setCareer(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CON_CAREER)));
		try {
			connections.setMobilePhoneList(
					(ArrayList<MobilePhone>) DBHelper.bytesToObject(
							cursor.getBlob(cursor.getColumnIndex(DBHelper.COLUMN_CON_MOBILE_PHONE_LIST)
							)
					)
			);
			connections.setFixedPhoneList(
					(ArrayList<MobilePhone>) DBHelper.bytesToObject(
							cursor.getBlob(cursor.getColumnIndex(DBHelper.COLUMN_CON_FIXED_PHONE_LIST)
									)
							)
					);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		finally {
//			cursor.close();
//		}
		
		return connections;
	}
	
	/**
	 * 根据Cursor生成Connections对象
	 * @param cursor
	 * @return
	 * @author leon
	 */
	public Connections getConnectionEx(Cursor cursor) {
		Connections connections = new Connections();
		connections.setSqlDBKeyId(cursor.getInt(cursor
				.getColumnIndex(DBHelper.COLUMN_CON_KEY_ID)));
		connections.setType((cursor.getInt(cursor
				.getColumnIndex(DBHelper.COLUMN_CON_TYPE)) + ""));
		String tempCharStr = cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_CON_CHAR));
		if (!StringUtils.isEmpty(tempCharStr)) {
			connections.setCharName(tempCharStr.charAt(0));
		}
		connections.setID(cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_CON_ID)));
		connections.setSourceFrom(cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_CON_SOURCEFROM)));
		connections.setName(cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_CON_NAME)));
		connections.setImage(cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_CON_IMAGE)));
		int booleanInt = 0;
		booleanInt = cursor.getInt(cursor
				.getColumnIndex(DBHelper.COLUMN_CON_ISONLINE));
		connections.setOnline(booleanInt != 0 ? true : false);
		connections.setFriendState(cursor.getInt(cursor
				.getColumnIndex(DBHelper.COLUMN_CON_FriendState)));
		connections.setJoinState(cursor.getInt(cursor
				.getColumnIndex(DBHelper.COLUMN_CON_JoinState)));
		connections.setEMail(cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_CON_EMAIL)));
		connections.setPhone(cursor.getString(cursor
				.getColumnIndex(DBHelper.COLUMN_CON_PHONE)));
		return connections;
	}
		
	// 删除数据
	public void delete(String id,int type,boolean isOnline ) {
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				int isOnLineint=0;
				if(isOnline){
					isOnLineint=1;
				}else{
					isOnLineint=0;
				}
//				int isOfflineLine=0;
//				if(isOffline){
//					isOfflineLine=1;
//				}else{
//					isOfflineLine=0;
//				}
				
				db.delete(tableName
						, 
						DBHelper.COLUMN_UID + " = ? and " 
						+ DBHelper.COLUMN_CON_ID + " = ? and " 
						+DBHelper.COLUMN_CON_TYPE + " = ? and "
						+DBHelper.COLUMN_CON_ISONLINE + " = ? "
						,
						new String[] { 
						App.getUserID()
						, id + ""
						, type + ""
						, isOnLineint + ""
						} );
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	//根据组织id删除数据
	public void delete(long id) {
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			db.beginTransaction();
			try {
				
				db.delete(tableName
						, 
						DBHelper.COLUMN_UID + " = ? and " 
						+ DBHelper.COLUMN_CON_ID + " = ? " 
						,
						new String[] { 
						App.getUserID()
						, id + ""
						} );
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
			}
		}
	}


	// 查询某一条
			public Connections query(int id,int type,boolean isOnline) {
				synchronized (helper) {
					if (!db.isOpen()) {
						db = helper.getWritableDatabase();
					}
					db.beginTransaction();
					try {
						int isOnLineint=0;
						if(isOnline){
							isOnLineint=1;
						}else{
							isOnLineint=0;
						}
//						int isOfflineLine=0;
//						if(isOffline){
//							isOfflineLine=1;
//						}else{
//							isOfflineLine=0;
//						}
						Cursor c = db.rawQuery("SELECT * FROM " + tableName 
								+ " WHERE "
								+DBHelper.COLUMN_UID + " = ? and "  
								+DBHelper.COLUMN_CON_ID+" = ? and " 
								+DBHelper.COLUMN_CON_TYPE+" = ? and "
								+DBHelper.COLUMN_CON_ISONLINE+" = ? ", 
								new String[] { App.getUserID(), id +"",type+"",isOnLineint+""});
						Connections connections=null;
						if(c.moveToNext()){
							 connections= getConnection(c);
						}
						db.setTransactionSuccessful();
						return connections;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						db.endTransaction();
						db.close();
					}
				}
				return null;
			}
			
			/**
			 * 获取机构好友，用户好友。
			 * @param start
			 * @param size
			 * @param projectType
			 * @return
			 */
			public ArrayList<Connections> queryFriend(int start,int size) {
				ArrayList<Connections> connectionsArr=new ArrayList<Connections>();
				synchronized (helper) {
					if (!db.isOpen()) {
						db = helper.getWritableDatabase();
					}
					int isOnLineint=1;
					//int isOffLineint=0;
					Cursor c = db.rawQuery("SELECT * FROM " + tableName + " WHERE "
							+ DBHelper.COLUMN_UID + " = ? and " 
							+DBHelper.COLUMN_CON_ISONLINE+" = ? and "
							+DBHelper.COLUMN_CON_FriendState+" = ? "
							+ " order by "+DBHelper.COLUMN_CON_CHAR
							+" asc limit ?,?  ",
							new String[] {
							App.getUserID(),
							String.valueOf(isOnLineint),
							String.valueOf(OrganizationMini.type_fri_friend),
							String.valueOf(start),  
		                    String.valueOf(size) });
					try {
						while (c.moveToNext()) {
							//Connections  app_data = new Connections();
							//byte[] item = c.getBlob(c.getColumnIndex("cns_data"));
							Connections connections= getConnection(c);
							connectionsArr.add(connections);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						c.close();
					}
				}
				return connectionsArr;
			}	
			
			
			/**
			 * 查询 用户好友。
			 * @param start
			 * @param size
			 * @param projectType
			 * @return
			 */
			public ArrayList<Connections> queryUserFriend(int start,int size) {
				ArrayList<Connections> connectionsArr=new ArrayList<Connections>();
				synchronized (helper) {
					if (!db.isOpen()) {
						db = helper.getWritableDatabase();
					}
					int isOnLineint=1;
					//int isOffLineint=0;
					Cursor c = db.rawQuery("SELECT * FROM " + tableName + " WHERE "
							+ DBHelper.COLUMN_UID + " = ? and " 
							+DBHelper.COLUMN_CON_ISONLINE+" = ? and "
							+DBHelper.COLUMN_CON_TYPE+" = ? and "
							+DBHelper.COLUMN_CON_FriendState+" = ? "
							+ " order by "+DBHelper.COLUMN_CON_CHAR
							+ " asc limit ?,?  ",
							new String[] {
							App.getUserID(),
							String.valueOf(isOnLineint),
							String.valueOf(Connections.type_persion),
							String.valueOf(OrganizationMini.type_fri_friend),
							String.valueOf(start),  
		                    String.valueOf(size) });
					try {
						while (c.moveToNext()) {
							//Connections  app_data = new Connections();
							//byte[] item = c.getBlob(c.getColumnIndex("cns_data"));
							Connections connections= getConnection(c);
							connectionsArr.add(connections);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						c.close();
					}
				}
				return connectionsArr;
			}	
			
			
			/**
			 * 获取机构好友，用户好友。
			 * @param start
			 * @param size
			 * @param type
			 * @return
			 */
			public ArrayList<Connections> queryFriend(int start, int size, int type) {
				ArrayList<Connections> connectionsArr = new ArrayList<Connections>();
				synchronized (helper) {
					if (!db.isOpen()) {
						db = helper.getWritableDatabase();
					}
					int isOnLineint = 1;//int转boolean , 非0都是true; 0是false;
					// int isOffLineint=0;
					Cursor c = db.rawQuery("SELECT * FROM " + tableName + " WHERE " 
							+ DBHelper.COLUMN_UID + " = ? and "
							+ DBHelper.COLUMN_CON_TYPE + " = ? and " 
							+ DBHelper.COLUMN_CON_ISONLINE + " = ? and " 
							+ DBHelper.COLUMN_CON_FriendState + " = ? " 
							+ " order by " 
							+ DBHelper.COLUMN_CON_CHAR + " asc limit ?,?  ",
							new String[] { 
							App.getUserID(),
							String.valueOf(type), 
							String.valueOf(isOnLineint),
							String.valueOf(OrganizationMini.type_fri_friend), 
							String.valueOf(start), 
							String.valueOf(size) });
					try {
						while (c.moveToNext()) {
							// Connections app_data = new Connections();
							// byte[] item = c.getBlob(c.getColumnIndex("cns_data"));
							Connections connections = getConnection(c);
							connectionsArr.add(connections);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						c.close();
					}
				}
				return connectionsArr;
			}	
			
			
			/**
			 * 获取机构好友和用户好友。
			 * @param start
			 * @param size
			 * @param projectType
			 * @return
			 */
			public ArrayList<Connections> queryFriendAll(int start, int size) {
				ArrayList<Connections> connectionsArr = new ArrayList<Connections>();
				synchronized (helper) {
					if (!db.isOpen()) {
						db = helper.getWritableDatabase();
					}
					int isOnLineint = 1;//int转boolean , 非0都是true; 0- false;
					// int isOffLineint=0;
					Cursor c = db.rawQuery("SELECT * FROM " + tableName + " WHERE " 
							+ DBHelper.COLUMN_UID + " = ? and "
							/*+ DBHelper.COLUMN_CON_TYPE + " = ? and " */
							+ DBHelper.COLUMN_CON_ISONLINE + " = ? and " 
							+ DBHelper.COLUMN_CON_FriendState + " = ? " 
							+ " order by " + DBHelper.COLUMN_CON_CHAR 
							+ " asc limit ?,?  ",
							new String[] {
							App.getUserID(),
							/*String.valueOf(type), */
							String.valueOf(isOnLineint),
							String.valueOf(OrganizationMini.type_fri_friend), 
							String.valueOf(start), 
							String.valueOf(size) });
					try {
						while (c.moveToNext()) {
							// Connections app_data = new Connections();
							// byte[] item = c.getBlob(c.getColumnIndex("cns_data"));
							Connections connections = getConnection(c);
							connectionsArr.add(connections);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						c.close();
					}
				}
				return connectionsArr;
			}	
			
			/**
			 * 获取 我的所有的人(我的好友和 人脉)。 FILTER_PEOPLE_ALL
			 * @param start
			 * @param size
			 * @param projectType
			 * @return
			 */
			/*public ArrayList<Connections> queryPeopleAll(int start, int size) {
				ArrayList<Connections> connectionsArr = new ArrayList<Connections>();
				synchronized (helper) {
					if (!db.isOpen()) {
						db = helper.getWritableDatabase();
					}
//					int isOnLineint = 1;//int转boolean , 非0都是true; 0- false;
					// int isOffLineint=0;
					Cursor c = db.rawQuery("SELECT * FROM " + tableName + " WHERE " 
							+ DBHelper.COLUMN_CON_TYPE + " = ? " 
							+ " order by " + DBHelper.COLUMN_CON_CHAR 
							+ " asc limit ?,?  ",
							new String[] {
							String.valueOf(Connections.type_persion), 
							String.valueOf(start), 
							String.valueOf(size) });
					try {
						while (c.moveToNext()) {
							// Connections app_data = new Connections();
							// byte[] item = c.getBlob(c.getColumnIndex("cns_data"));
							Connections connections = getConnection(c);
							connectionsArr.add(connections);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						c.close();
					}
				}
				return connectionsArr;
			}	*/
			
			
			/**
			 * 获取 我的所有的人(我的好友和 人脉)  带关系字查询 。 FILTER_PEOPLE_ALL
			 * @param start
			 * @param size
			 * @param projectType
			 * @return
			 */
			public ArrayList<Connections> queryPeopleAll( String keyword, int start, int size ) {
				ArrayList<Connections> connectionsArr = new ArrayList<Connections>();
				synchronized (helper) {
					if (!db.isOpen()) {
						db = helper.getWritableDatabase();
					}
					Cursor c;
					if(StringUtils.isEmpty(keyword)){
						//	int isOnLineint = 1;//int转boolean , 非0都是true; 0- false;
						// int isOffLineint=0;
						c = db.rawQuery("SELECT * FROM " + tableName + " WHERE " 
								+ DBHelper.COLUMN_UID + " = ? and "
								+ DBHelper.COLUMN_CON_TYPE + " = ? " 
								+ " order by " + DBHelper.COLUMN_CON_CHAR 
								+ " asc limit ?,?  ",
								new String[] {
								App.getUserID(),
								String.valueOf(Connections.type_persion), 
								String.valueOf(start), 
								String.valueOf(size) });
					}
					else {
						c = db.rawQuery("SELECT * FROM " + tableName + " WHERE " 
								+ DBHelper.COLUMN_UID + " = ? and "
								+ DBHelper.COLUMN_CON_NAME
								+" LIKE '%"+ keyword + "%' and "
								+ DBHelper.COLUMN_CON_TYPE + " = ? " 
								+ " order by " + DBHelper.COLUMN_CON_CHAR 
								+ " asc limit ?,?  ",
								new String[] {
								App.getUserID(),
								String.valueOf(Connections.type_persion), 
								String.valueOf(start), 
								String.valueOf(size) });
					}
					
					try {
						synchronized (helper) {
							while (c.moveToNext()) {
								// Connections app_data = new Connections();
								// byte[] item = c.getBlob(c.getColumnIndex("cns_data"));
								Connections connections = getConnection(c);
								connectionsArr.add(connections);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						c.close();
					}
				}
				return connectionsArr;
			}	
			/**
			 * 获取 我的所有的人(我的好友和 人脉)  带关系字查询 。 FILTER_PEOPLE_ALL  根据首字母查询
			 * @param start
			 * @param size
			 * @param projectType
			 * @return
			 */
			public ArrayList<Connections> queryPeopleAllByFirstChar( String keyword, int start, int size ) {
				ArrayList<Connections> connectionsArr = new ArrayList<Connections>();
				synchronized (helper) {
					if (!db.isOpen()) {
						db = helper.getWritableDatabase();
					}
					Cursor c;
					if(StringUtils.isEmpty(keyword)){
						//	int isOnLineint = 1;//int转boolean , 非0都是true; 0- false;
						// int isOffLineint=0;
						c = db.rawQuery("SELECT * FROM " + tableName + " WHERE " 
								+ DBHelper.COLUMN_UID + " = ? and "
								+ DBHelper.COLUMN_CON_TYPE + " = ? " 
								+ " order by " + DBHelper.COLUMN_CON_CHAR 
								+ " asc limit ?,?  ",
								new String[] {
								App.getUserID(),
								String.valueOf(Connections.type_persion), 
								String.valueOf(start), 
								String.valueOf(size) });
					}
					else {
						c = db.rawQuery("SELECT * FROM " + tableName + " WHERE " 
								+ DBHelper.COLUMN_UID + " = ? and "
								+ DBHelper.COLUMN_CON_CHAR
								+ " = '"+keyword +"' and "
								+ DBHelper.COLUMN_CON_TYPE + " = ? " 
								+ " order by " + DBHelper.COLUMN_CON_NAME
								+ " asc limit ?,?  ",
								new String[] {
								App.getUserID(),
								String.valueOf(Connections.type_persion), 
								String.valueOf(start), 
								String.valueOf(size) });
					}
					
					try {
						while (c.moveToNext()) {
							// Connections app_data = new Connections();
							// byte[] item = c.getBlob(c.getColumnIndex("cns_data"));
							Connections connections = getConnection(c);
							connectionsArr.add(connections);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						c.close();
					}
				}
				return connectionsArr;
			}	
			
			
			/**
			 * 通过关键字 获取 指定来源类型的 人脉  。 <br>
			 * 
			 * @param keyword	关键字
			 * @param fromType	人脉来源类型：1-我创建的；2-我保存的；3-我收藏的
			 * @param start	起始位置
			 * @param size	查询数量
			 * @return	ArrayList<Connections> 关系列表
			 * @author gushi
			 * @date	2015/06/10
			 */
			public ArrayList<Connections> queryOfflinePeople( String keyword, int fromType, int start, int size ) {
				ArrayList<Connections> connectionsArr = new ArrayList<Connections>();
				synchronized (helper) {
					if (!db.isOpen()) {
						db = helper.getWritableDatabase();
					}
					Cursor c;
					if(StringUtils.isEmpty(keyword)){
						//	int isOnLineint = 1;//int转boolean , 非0都是true; 0- false;
						// int isOffLineint=0;
						c = db.rawQuery("SELECT * FROM " + tableName + " WHERE " 
								+ DBHelper.COLUMN_UID + " = ? and "
								+ DBHelper.COLUMN_CON_TYPE + " = ? and " 
								+ DBHelper.COLUMN_CON_FROM_TYPE + " = ? " 
								+ " order by " + DBHelper.COLUMN_CON_CHAR 
								+ " asc limit ?,?  ",
								new String[] {
								App.getUserID(),
								String.valueOf(Connections.type_persion), 
								String.valueOf(fromType), 
								String.valueOf(start), 
								String.valueOf(size) });
					}
					else {
						c = db.rawQuery("SELECT * FROM " + tableName + " WHERE " 
								+ DBHelper.COLUMN_UID + " = ? and "
								+ DBHelper.COLUMN_CON_NAME
								+" LIKE '%"+ keyword + "%' and "
								+ DBHelper.COLUMN_CON_TYPE + " = ? and " 
								+ DBHelper.COLUMN_CON_FROM_TYPE + " = ? " 
								
								+ " order by " + DBHelper.COLUMN_CON_CHAR 
								+ " asc limit ?,?  ",
								new String[] {
								App.getUserID(),
								String.valueOf(Connections.type_persion), 
								String.valueOf(fromType), 
								String.valueOf(start), 
								String.valueOf(size) });
					}
					
					try {
						while (c.moveToNext()) {
							// Connections app_data = new Connections();
							// byte[] item = c.getBlob(c.getColumnIndex("cns_data"));
							Connections connections = getConnection(c);
							connectionsArr.add(connections);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						c.close();
					}
				}
				return connectionsArr;
			}	
			
			
			/**
			 * 获取 我的所有的组织(我的组织和 客户)  带关系字查询 。 FILTER_ORG_ALL
			 * @param start
			 * @param size
			 * @param projectType
			 * @return
			 */
			public ArrayList<Connections> queryOrganizationAll( String keyword, int start, int size ) {
				ArrayList<Connections> connectionsArr = new ArrayList<Connections>();
				synchronized (helper) {
					if (!db.isOpen()) {
						db = helper.getWritableDatabase();
					}
					Cursor c;
					if(StringUtils.isEmpty(keyword)){
						//	int isOnLineint = 1;//int转boolean , 非0都是true; 0- false;
						// int isOffLineint=0;
						c = db.rawQuery("SELECT * FROM " + tableName + " WHERE " 
								+ DBHelper.COLUMN_UID + " = ? and "
								+ DBHelper.COLUMN_CON_TYPE + " = ? " 
								+ " order by " + DBHelper.COLUMN_CON_CHAR 
								+ " asc limit ?,?  ",
								new String[] {
								App.getUserID(),
								String.valueOf(Connections.type_org), 
								String.valueOf(start), 
								String.valueOf(size) });
					}
					else {
						c = db.rawQuery("SELECT * FROM " + tableName + " WHERE " 
								+ DBHelper.COLUMN_UID + " = ? and "
								+ DBHelper.COLUMN_CON_NAME
								+" LIKE '%"+ keyword + "%' and "
								+ DBHelper.COLUMN_CON_TYPE + " = ? " 
								+ " order by " + DBHelper.COLUMN_CON_CHAR 
								+ " asc limit ?,?  ",
								new String[] {
								App.getUserID(),
								String.valueOf(Connections.type_org), 
								String.valueOf(start), 
								String.valueOf(size) });
					}
					
					try {
						synchronized (helper) {
							while (c.moveToNext()) {
								// Connections app_data = new Connections();
								// byte[] item = c.getBlob(c.getColumnIndex("cns_data"));
								Connections connections = getConnection(c);
								connectionsArr.add(connections);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						c.close();
					}
				}
				return connectionsArr;
			}
			/**
			 * 人脉
			 * @param start
			 * @param size
			 * @param type
			 * @return
			 */
			public ArrayList<Connections> queryOffline(int start,int size,int type) {
				ArrayList<Connections> connectionsArr=new ArrayList<Connections>();
				synchronized (helper) {
					if (!db.isOpen()) {
						db = helper.getWritableDatabase();
					}
					int isOnLineint=0;
					Cursor c = db.rawQuery("SELECT * FROM " + tableName 
							+ " WHERE "
							+ DBHelper.COLUMN_UID + " = ? and "
							+DBHelper.COLUMN_CON_TYPE+" = ? and "
							+DBHelper.COLUMN_CON_ISONLINE+" = ? "
							+ " order by "+DBHelper.COLUMN_CON_CHAR+" asc limit ?,?  "
							,
							new String[] {
							App.getUserID(),
							String.valueOf(type) ,
							String.valueOf(isOnLineint),String.valueOf(start),  
		                    String.valueOf(size) });
					try {
						while (c.moveToNext()) {
							//Connections  app_data = new Connections();
							//byte[] item = c.getBlob(c.getColumnIndex("cns_data"));
							Connections connections= getConnection(c);
							connectionsArr.add(connections);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						c.close();
					}
				}
				return connectionsArr;
			}
	
			
	/**
	 * 根据 关键字 分页 获得 联系人列表
	 * @param start
	 * @param size
	 * @param select
	 * @return
	 */
	public ArrayList<Connections> query(int start,int size, String select) {
		ArrayList<Connections> connectionsArr=new ArrayList<Connections>();
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c =null;
			if(StringUtils.isEmpty(select)){
				c = db.rawQuery("SELECT * FROM " + tableName 
						+ " WHERE "
						+ DBHelper.COLUMN_UID + " = ? "
						+ " order by "+DBHelper.COLUMN_CON_CHAR+" asc limit ?,?  "
						, 
						new String[] { 
						App.getUserID(),
						String.valueOf(start),  
	                    String.valueOf(size) });
			}
			else {
				c = db.rawQuery("SELECT * FROM " + tableName
						+ " where "
						+ DBHelper.COLUMN_UID + " = ? and "
						+ DBHelper.COLUMN_CON_NAME
						+" LIKE '%"+select+ "%' "
						+ " order by "+DBHelper.COLUMN_CON_CHAR+" asc limit ?,?  "
						, new String[] { 
						App.getUserID(),
						String.valueOf(start),  
	                    String.valueOf(size) });
			}
			try {
				while (c.moveToNext()) {
					//Connections  app_data = new Connections();
					//byte[] item = c.getBlob(c.getColumnIndex("cns_data"));
					Connections connections= getConnection(c);
					connectionsArr.add(connections);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return connectionsArr;
	}
	
	
	/**
	 * 获得 联系人列表, 根据 关键字,类型,是否在线, 是否为好友, 分页 
	 * @param select  关键字   查询人名或组织名用
	 * @param type   INTEGER 人或组织的类型.  1-人; 2-组织
	 * @param isOnline  boolean  true-用户和组织，false-人脉和机构
	 * @param friendState  int  0 - 好友 , 1 - 不是好友  OrganizationMini.type_fri_friend=0
	 * @param start   起始位置
	 * @param size		每页查询数量
	 * @return  联系人列表
	 * 
	 * @author gushi
	 */
	public ArrayList<Connections> query( String select, int type, boolean isOnline, int friendState, int start, int size) {
		ArrayList<Connections> connectionsArr=new ArrayList<Connections>();
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c =null;
			if(StringUtils.isEmpty(select)){
				c = db.rawQuery("SELECT * FROM " + tableName 
						+" where "
						+ DBHelper.COLUMN_UID + " = ? and "
//						+ DBHelper.COLUMN_CON_NAME +" LIKE '%"+select+ "%' "
						+ DBHelper.COLUMN_CON_TYPE + " = ? " 
						+ " and "
						+ DBHelper.COLUMN_CON_ISONLINE + " = ? " 
						+ " and "
						+ DBHelper.COLUMN_CON_FriendState + " = ? " 
						+ " order by "+DBHelper.COLUMN_CON_CHAR
						+" asc limit ?,?  ", 
						new String[] {
						App.getUserID(),
						String.valueOf(type),  
						String.valueOf(boolean2int(isOnline)),  
						String.valueOf(friendState),  
						String.valueOf(start),  
						String.valueOf(size) });
			}else{
				c = db.rawQuery("SELECT * FROM " + tableName 
						+" where "
						+ DBHelper.COLUMN_CON_NAME +" LIKE '%"+select+ "%' "
						+ " and "
						+ DBHelper.COLUMN_UID + " = ? and "
						+ DBHelper.COLUMN_CON_TYPE + " = ? " 
						+ " and "
						+ DBHelper.COLUMN_CON_ISONLINE + " = ? " 
						+ " and "
						+ DBHelper.COLUMN_CON_FriendState + " = ? " 
						+ " order by "+DBHelper.COLUMN_CON_CHAR
						+" asc limit ?,?  ", 
						new String[] {
						App.getUserID(),
						String.valueOf(type),  
						String.valueOf(boolean2int(isOnline)),  
						String.valueOf(friendState),  
						String.valueOf(start),  
						String.valueOf(size) });
			}
			try {
				while (c.moveToNext()) {
					//Connections  app_data = new Connections();
					//byte[] item = c.getBlob(c.getColumnIndex("cns_data"));
					Connections connections= getConnection(c);
					connectionsArr.add(connections);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return connectionsArr;
	}
	
	/**
	 * 获得 联系人列表, 根据 关键字,类型,是否在线, 是否为好友, 分页 
	 * @param keyword  关键字   查询人名或组织名用
	 * @param type   人或组织的类型.  1-人; 2-组织
	 * @param index   起始位置
	 * @param size		每页查询数量
	 * @return  联系人列表
	 * @author leon
	 */
	public JTPage query(String keyword, int type, int index, int size) {
	
		JTPage jtPage = null;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			keyword = keyword != null ? keyword.replace("'", "") : "";
			jtPage = new JTPage();
			int total = queryCount(keyword, type, true) + queryCount(keyword, type, false);
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
			Cursor c = null;
			if (TextUtils.isEmpty(keyword)) {
				c = db.rawQuery(
						"SELECT * FROM " + tableName + " WHERE "
								+ DBHelper.COLUMN_UID + " = ? and "
								+ DBHelper.COLUMN_CON_TYPE + " = ? "
								+ " ORDER BY " + DBHelper.COLUMN_CON_CHAR
								+ " ASC LIMIT ?,?",
								new String[] { App.getUserID(), type + "", index * size + "", size + "" });
			} 
			else {
				c = db.rawQuery(
						"SELECT * FROM " + tableName + " WHERE "
								+ DBHelper.COLUMN_CON_NAME + " LIKE '%"
								+ keyword + "%' " + " AND "
								+ DBHelper.COLUMN_UID + " = ? and "
								+ DBHelper.COLUMN_CON_TYPE + " = ? "
								+ " ORDER BY " + DBHelper.COLUMN_CON_CHAR
								+ " ASC LIMIT ?,?",
						new String[] { App.getUserID(), type + "", index * size + "",  size + "" });
				
			}
			try {
				while (c.moveToNext()) {
					Connections connections = getConnectionEx(c);
					jtPage.getLists().add(connections);
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
	
	
	/**
	 * 查询出加入的组织
	 * @return
	 */
	public ArrayList<Connections> queryJoinOrg() {
		ArrayList<Connections> connectionsArr=new ArrayList<Connections>();
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c = db.rawQuery("SELECT * FROM " + tableName 
					+" where " 
					+ DBHelper.COLUMN_UID + " = ? and "
					+ DBHelper.COLUMN_CON_JoinState+" = ? "
					+ " and "
					+ DBHelper.COLUMN_CON_TYPE+" = ? "
					+ " order by "+DBHelper.COLUMN_CON_CHAR+" asc limit ?,?  "
					, 
					new String[] {
					App.getUserID(),
					String.valueOf(OrganizationMini.type_join_ok),
					String.valueOf(Connections.type_org),
					String.valueOf(0),  String.valueOf(1000) });
			try {
				while (c.moveToNext()) {
					//Connections  app_data = new Connections();
					//byte[] item = c.getBlob(c.getColumnIndex("cns_data"));
					Connections connections= getConnection(c);
					connectionsArr.add(connections);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return connectionsArr;
	}
	
	// 这里是原来的算法 20150206以前的
	/*public int queryCharAt(char charAt) {
		int ddd=0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c = db.rawQuery("SELECT count (*) FROM " + tableName 
					+ " where "
					+ DBHelper.COLUMN_UID + " = ? and "
					+DBHelper.COLUMN_CON_CHAR+" < '"+ charAt + "' order by "+DBHelper.COLUMN_CON_CHAR
					,
					new String[] {
					App.getUserID()
					});
			try {
				while (c.moveToNext()) {
					 ddd=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return ddd;
	}*/
	
	/**
	 * 算字母在关系列表的里位置(不查数据库)
	 * @param arrData	关系列表
	 * @param charAt	查询字母
	 * @return 位置
	 */
	public int queryCharAt(ArrayList<Connections> arrData, char charAt) {
		for (int i = 0; i < arrData.size() ; i++) {
			Connections connections = arrData.get(i);
			char charName = connections.getCharName();
			if(charAt == charName){
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * 当前查询字母索引  根据类型  在数据库里的位置
	 * @param charAt
	 * @param filter
	 * @param keyword
	 * @return 当前查询字母索引 在数据库里的位置
	 * gushi
	 */
	public int queryCharAt(char charAt, int filter, String keyword) {
		/** 默认位置   -2为了区别于查结果 */
		int position = -2;
		int charSize = -1;
		Cursor c = null;
		
		
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			
			
			// 这里是原来的算法 20150206以前的
			
			/*Cursor c = db.rawQuery("SELECT count (*) FROM " + tableName 
					+ " where "
					+ DBHelper.COLUMN_UID + " = ? and "
					+DBHelper.COLUMN_CON_CHAR+" < '"+ charAt + "' order by "+DBHelper.COLUMN_CON_CHAR
					,
					new String[] {
						App.getUserID()
					});*/
			
			
			// 下面是我的新算法 by hanqi
			// 查询分两种情况   并且查询时 应该带上 当前的查询类型  比如 只查人脉  只查用户  或 几种条件的混排  
			
			//查询 当前索引 当前条件 在数据库中 是否存在
			
			
			// 个人好友和机构好友
			if( ConnectionsCacheData.FILTER_FRIEND_ALL == filter ){
				charSize = queryFriendAllSize(charAt);
//				charSize = queryFriendSize(charAt, Connections.type_persion);
				
				// 1 当所给字母在数据库里没有这个字母 的数据  返回一个 -1, 调用的方法 判断当为 -1时, listview 不应该滚动 , 而且应该 提示用户 当前没有这个索引 数据, 或者 在右边的索引条初使化的时候 不应该出现这个索引字母, 改为 动态生成索引数组列表
				
				if( charSize == 0 ){
					position = -1;
					return position;
				}
				// 2. 当所给字母在 数据库里 有这个字母的数据时 .   返回当前 索引 所在的第一个数据的 位置 
				
				else if ( charSize > 0 ){
					int isOnLineint=1;
					
					c = db.rawQuery("SELECT count (*) FROM " + tableName 
					+ " where "
					+ DBHelper.COLUMN_UID + " = ? and "
					+ DBHelper.COLUMN_CON_CHAR + " < '"+ charAt + "' order by "+DBHelper.COLUMN_CON_CHAR + " and "
					
					//  这里要加上 类型
					+DBHelper.COLUMN_CON_ISONLINE + " = ? and "
					+DBHelper.COLUMN_CON_FriendState +" = ? and "
					+DBHelper.COLUMN_CON_TYPE +" = ?"
					
					,
					new String[] {
						App.getUserID(),
						String.valueOf(isOnLineint),
						String.valueOf(OrganizationMini.type_fri_friend),
						String.valueOf(Connections.type_persion)
						
					});
					
				}
				
			}
			// 个人好友和人脉 
			else if( ConnectionsCacheData.FILTER_PEOPLE_ALL == filter ){
				charSize = queryPeopleAllSize(keyword, charAt);
				
				if( charSize == 0 ){
					position = -1;
					return position;
				}
				
				else if (charSize > 0) {
					
					if(StringUtils.isEmpty(keyword)){
					
						c = db.rawQuery("SELECT count (*) FROM " + tableName 
								+ " where "
								+ DBHelper.COLUMN_UID + " = ? and "
								+ DBHelper.COLUMN_CON_CHAR + " < '"+ charAt + "' order by "+DBHelper.COLUMN_CON_CHAR + " and "
								
								//  这里要加上 类型
								+DBHelper.COLUMN_CON_TYPE+" = ? "
								, 
								new String[] {
								App.getUserID(),
								String.valueOf(Connections.type_persion)
								});
						
					}
					else {
						
						c = db.rawQuery("SELECT count (*) FROM " + tableName 
								+ " where "
								+ DBHelper.COLUMN_UID + " = ? and "
								+ DBHelper.COLUMN_CON_CHAR + " < '"+ charAt + "' order by "+DBHelper.COLUMN_CON_CHAR + " and "
								+ DBHelper.COLUMN_CON_NAME + " LIKE '%"+keyword+ "%' and " 
								//  这里要加上 类型
								+DBHelper.COLUMN_CON_TYPE+" = ? "
								, 
								new String[] {
								App.getUserID(),
								String.valueOf(Connections.type_persion)
						});
						
					}
					
				}
				
			}
			
			try {
				while (c.moveToNext()) {
					position=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(c != null){
					c.close();
				}
			}
		}
		return position;
	}
	
	/**
	 * 查询有该 关键字 的联系人总数
	 * @param keyword  关键字
	 * @return 该关键字的总数
	 */
	public  int queryTotalSize(String keyword) {
		int size=0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c =null;
			
			if(StringUtils.isEmpty(keyword)){
				c = db.rawQuery("SELECT count (*) FROM " + tableName +" where "
						+ DBHelper.COLUMN_UID + " = ? "
						,
						new String[] { App.getUserID() }
						);
			}else{
				c = db.rawQuery("SELECT count (*) FROM " + tableName +" where "
						+ DBHelper.COLUMN_UID + " = ? and "
						+ DBHelper.COLUMN_CON_NAME+" LIKE '%"+keyword+ "%' "
						, 
						new String[] { App.getUserID() }
						);
			}
			
			try {
				while (c.moveToNext()) {
					size=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return size;
	}
	
	public int queryOfflineSize(int type) {
		int size=0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			int isOnLineint=0;
			Cursor c = db.rawQuery("SELECT count (*) FROM " + tableName 
					+ " WHERE "
					+ DBHelper.COLUMN_UID + " = ? and "
					+DBHelper.COLUMN_CON_TYPE+" = ? and "
					+DBHelper.COLUMN_CON_ISONLINE+" = ?  ", 
					new String[] { 
					App.getUserID(),
					type+"",
					String.valueOf(isOnLineint)
					});

			try {
				while (c.moveToNext()) {
					size=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return size;
	}
	
	public int queryFriendSize(int type) {
		int size=0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}		
			int isOnLineint=1;
			//int isOffLineint=0;
			Cursor c = db.rawQuery("SELECT count (*) FROM " + tableName 
					+ " WHERE "
					+ DBHelper.COLUMN_UID + " = ? and "
					+DBHelper.COLUMN_CON_TYPE+" = ? and "
					+DBHelper.COLUMN_CON_ISONLINE+" = ? and "
					+DBHelper.COLUMN_CON_FriendState+" = ? "
					, 
					new String[] { 
					App.getUserID(),
					String.valueOf(type),
					String.valueOf(isOnLineint),
					String.valueOf(OrganizationMini.type_fri_friend)
					});
			try {
				while (c.moveToNext()) {
					size=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return size;
	}
	
	/**
	 * 查询用户好友和组织好友的总数
	 * @param projectType
	 * @return
	 */
	public int queryFriendAllSize() {
		int size=0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}		
			int isOnLineint=1;
			//int isOffLineint=0;
			Cursor c = db.rawQuery("SELECT count (*) FROM " + tableName 
					+ " WHERE "
					+ DBHelper.COLUMN_UID + " = ? and "
					+DBHelper.COLUMN_CON_ISONLINE + " = ? and "
					+DBHelper.COLUMN_CON_FriendState +" = ? ", 
					new String[] { 
					App.getUserID(),
					String.valueOf(isOnLineint),
					String.valueOf(OrganizationMini.type_fri_friend)});
			try {
				while (c.moveToNext()) {
					size=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return size;
	}
	
	/**
	 * 查询用户好友和组织好友的包含 指定拼音索引总数
	 * @param charAt
	 * @return
	 */
	public int queryFriendAllSize(char charAt) {
		int size=0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}		
			int isOnLineint=1;
			//int isOffLineint=0;
			Cursor c = db.rawQuery("SELECT count (*) FROM " + tableName 
					+ " WHERE "
					+ DBHelper.COLUMN_UID + " = ? and "
					+DBHelper.COLUMN_CON_ISONLINE + " = ? and "
					+DBHelper.COLUMN_CON_FriendState +" = ? and "
					+DBHelper.COLUMN_CON_CHAR +" = ? "
					, 
					new String[] { 
					App.getUserID(),
					String.valueOf(isOnLineint),
					String.valueOf(OrganizationMini.type_fri_friend),
					charAt + ""
					});
			try {
				while (c.moveToNext()) {
					size=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return size;
	}
	
	
	/**
	 * 查询指定类型 好友 包含 指定拼音索引总数
	 * @param charAt
	 * @return
	 */
	public int queryFriendSize(char charAt, int type) {
		int size=0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}		
			int isOnLineint=1;
			//int isOffLineint=0;
			Cursor c = db.rawQuery("SELECT count (*) FROM " + tableName 
					+ " WHERE "
					+ DBHelper.COLUMN_UID + " = ? and "
					+DBHelper.COLUMN_CON_ISONLINE + " = ? and "
					+DBHelper.COLUMN_CON_FriendState +" = ? and "
					+DBHelper.COLUMN_CON_CHAR +" = ? and "
					+DBHelper.COLUMN_CON_TYPE +" = ?"
					, 
					new String[] { 
					App.getUserID(),
					String.valueOf(isOnLineint),
					String.valueOf(OrganizationMini.type_fri_friend),
					charAt + "",
					String.valueOf(type)
					
			});
			try {
				while (c.moveToNext()) {
					size=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return size;
	}
	
	/**
	 * 查询 我的 好友和 人脉 的总数
	 * @param type
	 * @return
	 */
	/*public int queryPeopleAllSize() {
		int size=0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}		
			int isOnLineint=1;
			//int isOffLineint=0;
			Cursor c = db.rawQuery("SELECT count (*) FROM " + tableName 
					+ " WHERE "
//					+DBHelper.COLUMN_CON_ISONLINE+" = ? and "
//					+DBHelper.COLUMN_CON_FriendState+" = ? "
					+DBHelper.COLUMN_CON_TYPE+" = ? "
					, 
					new String[] { 
//					String.valueOf(isOnLineint),
//					String.valueOf(OrganizationMini.type_fri_friend)
					String.valueOf(Connections.type_persion)
					
			});
			try {
				while (c.moveToNext()) {
					size=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return size;
	}*/
	
	/** 这个是加关键字的查询总数  */
	public int queryPeopleAllSize( String keyword ){
		int size=0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}		
			Cursor c = null;
			
			if(StringUtils.isEmpty(keyword)){
				 c = db.rawQuery("SELECT count (*) FROM " + tableName + " WHERE "
						 + DBHelper.COLUMN_UID + " = ? and "
						 +DBHelper.COLUMN_CON_TYPE+" = ? "
						 , 
						new String[] { 
						 App.getUserID(),
						 String.valueOf(Connections.type_persion)});
			}
			else {
				c = db.rawQuery("SELECT count (*) FROM " + tableName + " WHERE " 
						+ DBHelper.COLUMN_UID + " = ? and "
						+ DBHelper.COLUMN_CON_NAME+" LIKE '%"+keyword+ "%' and " 
						+ DBHelper.COLUMN_CON_TYPE+" = ? "
						, 
						new String[] { 
						App.getUserID(),
						String.valueOf(Connections.type_persion)
						});
			}
			
			try {
				while (c.moveToNext()) {
					size=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return size;
	}
	
	
	/**
	 * 通过关键字 获取 指定来源类型的 人脉 总数
	 * @param keyword	关键字
	 * @param fromType	人脉来源类型：1-我创建的；2-我保存的；3-我收藏的
	 * @return 总数
	 */
	public int queryOfflinePeopleSize( String keyword ,int fromType){
		int size=0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}		
			Cursor c = null;
			
			if(StringUtils.isEmpty(keyword)){
				c = db.rawQuery("SELECT count (*) FROM " + tableName + " WHERE "
						+ DBHelper.COLUMN_UID + " = ? and "
						+ DBHelper.COLUMN_CON_TYPE + " = ? and "
						+ DBHelper.COLUMN_CON_FROM_TYPE + " = ?"
						, 
						new String[] { 
						App.getUserID(),
						String.valueOf(Connections.type_persion),
						String.valueOf(fromType)
				});
			}
			else {
				c = db.rawQuery("SELECT count (*) FROM " + tableName + " WHERE " 
						+ DBHelper.COLUMN_UID + " = ? and "
						+ DBHelper.COLUMN_CON_NAME + " LIKE '%"+keyword+ "%' and " 
						+ DBHelper.COLUMN_CON_TYPE + " = ? and "
						+ DBHelper.COLUMN_CON_FROM_TYPE + " = ? "
						, 
						new String[] { 
						App.getUserID(),
						String.valueOf(Connections.type_persion),
						String.valueOf(fromType)
						
				});
			}
			
			try {
				while (c.moveToNext()) {
					size=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return size;
	}
	
	/**
	 * 这个是加关键字的查询 包含指定拼音索引的 总数
	 * @param keyword
	 * @param charAt
	 * @return
	 */
	public int queryPeopleAllSize( String keyword, char charAt ){
		int size=0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c = null;
			
			if(StringUtils.isEmpty(keyword)){
				c = db.rawQuery("SELECT count (*) FROM " + tableName + " WHERE "
						+ DBHelper.COLUMN_UID + " = ? and "
						+DBHelper.COLUMN_CON_TYPE+" = ? and "
						+DBHelper.COLUMN_CON_CHAR+" = ? "
						, 
						new String[] {
						App.getUserID(),
						String.valueOf(Connections.type_persion),
						charAt + ""
						});
			}
			else {
				c = db.rawQuery("SELECT count (*) FROM " + tableName + " WHERE " 
						+ DBHelper.COLUMN_UID + " = ? and "
						+ DBHelper.COLUMN_CON_NAME + " LIKE '%"+keyword+ "%' and " 
						+ DBHelper.COLUMN_CON_TYPE + " = ? and "
						+DBHelper.COLUMN_CON_CHAR+" = ? "
						, 
						new String[] { 
						App.getUserID(),
						String.valueOf(Connections.type_persion),
						charAt + ""
				});
			}
			
			try {
				while (c.moveToNext()) {
					size=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return size;
	}
	
	
	/**
	 * 
	 * 查询总数, 根据 关键字,类型, 是否在线, 是否为好友
	 * 
	 * @param select  关键字   查询人名或组织名用
	 * @param type   INTEGER 人或组织的类型.  1-人; 2-组织
	 * @param isOnline  boolean  true-用户和组织，false-人脉和机构
	 * @param friendState  int  0 - 好友 , 1 - 不是好友  OrganizationMini.type_fri_friend=0
	 * @return 总数
	 * 
	 * @author gushi
	 */
	public int queryCount(String keyWord, int type, boolean isOnline, int friendState ) {
		int size=0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}		
			Cursor c = null;
			if(StringUtils.isEmpty(keyWord)){
				c = db.rawQuery("SELECT count (*) FROM " + tableName 
						+ " WHERE "
//						+ DBHelper.COLUMN_CON_NAME+" LIKE '%"+keyWord+ "%' "
//						+ " and "
						+ DBHelper.COLUMN_UID + " = ? and "
						+DBHelper.COLUMN_CON_TYPE +" = ? "
						+ " and "
						+DBHelper.COLUMN_CON_ISONLINE+" = ? "
						+ " and "
						+DBHelper.COLUMN_CON_FriendState+" = ? ", 
						new String[] { 
						App.getUserID(),
						String.valueOf(type),
						String.valueOf(boolean2int(isOnline)),
						String.valueOf(friendState)});
			}
			else {
			c = db.rawQuery("SELECT count (*) FROM " + tableName 
					+ " WHERE "
					+ DBHelper.COLUMN_UID + " = ? and "
					+ DBHelper.COLUMN_CON_NAME+" LIKE '%"+keyWord+ "%' "
					+ " and "
					+DBHelper.COLUMN_CON_TYPE +" = ? "
					+ " and "
					+DBHelper.COLUMN_CON_ISONLINE+" = ? "
					+ " and "
					+DBHelper.COLUMN_CON_FriendState+" = ? ", 
					new String[] { 
					App.getUserID(),
					String.valueOf(type),
					String.valueOf(boolean2int(isOnline)),
					String.valueOf(friendState)});
			}
			try {
				while (c.moveToNext()) {
					size=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return size;
	}
	
	/**
	 * 
	 * 查询总数, 根据 关键字,类型, 是否在线, 是否为好友
	 * 
	 * @param select  关键字   查询人名或组织名用
	 * @param type   INTEGER 人或组织的类型.  1-人; 2-组织
	 * @param isOnline  boolean  true-用户和组织，false-人脉和机构
	 * @param friendState  int  0 - 好友 , 1 - 不是好友  OrganizationMini.type_fri_friend=0
	 * @return 总数
	 * 
	 * @author gushi
	 */
	public int queryCount(String keyWord, int type, boolean isOnline ) {
		int size=0;
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}		
			Cursor c = null;
			if(StringUtils.isEmpty(keyWord)){
				c = db.rawQuery("SELECT count (*) FROM " + tableName 
						+ " WHERE "
//						+ DBHelper.COLUMN_CON_NAME+" LIKE '%"+keyWord+ "%' "
//						+ " and "
						+ DBHelper.COLUMN_UID + " = ? and "
						+DBHelper.COLUMN_CON_TYPE +" = ? "
						+ " and "
						+DBHelper.COLUMN_CON_ISONLINE+" = ? ",
//						+ " and "
//						+DBHelper.COLUMN_CON_FriendState+" = ? ", 
						new String[] { 
						App.getUserID(),
						String.valueOf(type),
						String.valueOf(boolean2int(isOnline))});
			}
			else {
			c = db.rawQuery("SELECT count (*) FROM " + tableName 
					+ " WHERE "
					+ DBHelper.COLUMN_UID + " = ? and "
					+ DBHelper.COLUMN_CON_NAME+" LIKE '%"+keyWord+ "%' "
					+ " and "
					+DBHelper.COLUMN_CON_TYPE +" = ? "
					+ " and "
					+DBHelper.COLUMN_CON_ISONLINE+" = ? ",
//					+ " and "
//					+DBHelper.COLUMN_CON_FriendState+" = ? ", 
					new String[] { 
					App.getUserID(),
					String.valueOf(type),
					String.valueOf(boolean2int(isOnline))
					});
			}
			try {
				while (c.moveToNext()) {
					size=c.getInt(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return size;
	}
	
	
	/**
	 *  清除 全部类型表数据
	 */
	public  void clearTable() {
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			try {
				db.delete(tableName, DBHelper.COLUMN_UID+" = ?", new String[] { App.getUserID() });
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
	}
	
	/**
	 *  清除 人类型表数据(个人好友和人脉 不包括组织)	
	 */
	public  void clearTableWithPeopleAll() {
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			try {
				db.delete(
						tableName, 
						DBHelper.COLUMN_UID + " = ? and "
						+ DBHelper.COLUMN_CON_TYPE + " = ?"
						, 
				new String[] { 
						App.getUserID(),
						String.valueOf(Connections.type_persion),
				});
			} 
			catch (Exception e) {
				e.printStackTrace();
			} 
			finally {
			}
		}
	}
	
	/**
	 *  清除 全部好友 类型表数据(个人好友和组织好友 )	
	 */
	public  void clearTableWithFriendAll() {
		synchronized (helper) {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			int isOnLineint = 1;
			
			try {
				db.delete(
						tableName, 
						DBHelper.COLUMN_UID + " = ? and "
						+DBHelper.COLUMN_CON_ISONLINE + " = ? and "
						+DBHelper.COLUMN_CON_FriendState + " = ?"
						, 
						new String[] { 
						App.getUserID(),
						String.valueOf(isOnLineint),
						String.valueOf(OrganizationMini.type_fri_friend)
						});
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
	}
	
	public static int boolean2int( boolean b){
		return b ? 1 : 0 ;
	}
	
}
