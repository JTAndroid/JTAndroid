package com.tr.db;

import java.util.ArrayList;

import com.tr.model.obj.Connections;
import com.tr.model.obj.JTContactMini;

/**
 * 关系列表的数据缓存类。
 * @author gushi
 */
public class ConnectionsCacheData {

	ConnectionsDBManager db;
	/** 获得关系人数量 原来是100	 */
	private final int getSize = 100;
	/** 获得关系人数量 原来是100	 */
//	private final int getSize = 1000000;
	private int startIndex = 0;
	private int endindex = 0;
	private String keyword;
	private boolean qurryByFistChar;
	public ArrayList<Connections> arrData = new ArrayList<Connections>();
	ArrayList<Connections> arrDatatop = new ArrayList<Connections>();
	ArrayList<Connections> arrDataBottom = new ArrayList<Connections>();

	public ConnectionsCacheData(ConnectionsDBManager db) {
		this.db = db;
	}

	public void setTableName(String name) {
		db.setTableName(name);
	}
	
	public boolean isQurryByFistChar() {
		return qurryByFistChar;
	}

	public void setQurryByFistChar(boolean qurryByFistChar) {
		this.qurryByFistChar = qurryByFistChar;
	}

	public void init() {
		arrData = getDate(0, getSize);
		startIndex = 0;
		endindex = startIndex + getSize;
	}

	public synchronized Connections get(int at) {
		Connections connections = null;
		if (at >= startIndex && at < endindex&&at-startIndex<arrData.size()) {
			connections = arrData.get(at - startIndex);
		} 
		else {
			if (at < 0 || at >= size()) {
				return null;
			}
			int tempStartIndex = at / 100 * 100;
			arrData = getDate(tempStartIndex, getSize);
			endindex = tempStartIndex + getSize;
			startIndex = tempStartIndex;
			connections = arrData.get(at - startIndex);
		}
		return connections;
	}

	/** 关系过滤器	*/
	private int filter = FILTER_ALL;
	
	/**	所有关系	*/
	public static final int FILTER_ALL = 0;
	/**	所有人脉	*/
	public static final int FILTER_ALL_PEOPLE_OFFLINE = 1;
	/**	所有组织	*/
	public static final int FILTER_ALL_ORG_OFFLINE = 2;
	/** 个人好友	*/
	public static final int FILTER_FRIEND_PEOPLE = 3;
	/**	组织好友	*/
	public static final int FILTER_FRIEND_ORG = 4;
	/** 个人好友和机构好友  (通讯录 )*/
	public static final int FILTER_FRIEND_ALL = 5;
	/** 个人好友和人脉 */
	public static final int FILTER_PEOPLE_ALL = 6;
	
	/** 我创建的人脉 */
	public static final int FILTER_I_CREATE_OFFLINE_PEOPLE = 7;
	/** 我收藏的人脉 */
	public static final int FILTER_I_COLLECT_OFFLINE_PEOPLE = 8;
	/** 我保存的人脉 */
	public static final int FILTER_I_SAVE_OFFLINE_PEOPLE = 9;
	/** 组织和客户*/
	public static final int FILTER_ORG_ALL = 10;
	public void setFilterType(int type) {
		filter = type;
	}

	public int getFilter() {
		return filter;
	}

	/**
	 * 查询联系人
	 * @param index  起始位置
	 * @param size  数量
	 * @return 联系列表
	 */
	public synchronized ArrayList<Connections> getDate(int index, int size) {
		ArrayList<Connections> arrData = null;
		if (filter == FILTER_ALL) {
			arrData = db.query(index, size, keyword);
		} 
		else if (filter == FILTER_ALL_PEOPLE_OFFLINE) {
			arrData = db.queryOffline(index, size, Connections.type_persion);
		} 
		else if (filter == FILTER_ALL_ORG_OFFLINE) {
			arrData = db.queryOffline(index, size, Connections.type_org);
		}
		else if( filter == FILTER_ORG_ALL){
			arrData = db.queryOrganizationAll(keyword,index, size);
		}
		else if (filter == FILTER_FRIEND_PEOPLE) {
			arrData = db.queryFriend(index, size, Connections.type_persion);
		} 
		else if (filter == FILTER_FRIEND_ORG) {
			arrData = db.queryFriend(index, size, Connections.type_org);
		}
		else if (filter == FILTER_FRIEND_ALL) {
			arrData = db.queryFriendAll(index, size);
			// 主列表只查询个人好友, 组织好友单独查放item0里.
//			arrData = db.queryFriend(index, size, Connections.type_persion);
		}
		else if (filter == FILTER_PEOPLE_ALL) {
			if (qurryByFistChar) {
				arrData = db.queryPeopleAllByFirstChar(keyword, index, size);
			}else {
				arrData = db.queryPeopleAll(keyword, index, size);
			}
		}
		else if (filter == FILTER_I_CREATE_OFFLINE_PEOPLE) {
			arrData = db.queryOfflinePeople(keyword, JTContactMini.FROM_TYPE_I_CREATE,index, size);
			
		}
		else if (filter == FILTER_I_COLLECT_OFFLINE_PEOPLE) {
			arrData = db.queryOfflinePeople(keyword, JTContactMini.FROM_TYPE_I_COLLECT, index, size);
					
		}
		else if (filter == FILTER_I_SAVE_OFFLINE_PEOPLE) {
			arrData = db.queryOfflinePeople(keyword, JTContactMini.FROM_TYPE_I_SAVE, index, size);
		}
		return arrData;
	}

	/**
	 * 查询关系总数
	 * @return
	 */
	public int size() {
		int size = 0;
		if (filter == FILTER_ALL) {
			size = db.queryTotalSize(keyword);
		} 
		else if (filter == FILTER_ALL_PEOPLE_OFFLINE) {
			size = db.queryOfflineSize(Connections.type_persion);
		} 
		else if (filter == FILTER_ALL_ORG_OFFLINE) {
			size = db.queryOfflineSize(Connections.type_org);
		} 
		else if (filter == FILTER_FRIEND_PEOPLE) {
			size = db.queryFriendSize(Connections.type_persion);
		} 
		else if (filter == FILTER_FRIEND_ORG) {
			size = db.queryFriendSize(Connections.type_org);
		}
		else if (filter == FILTER_FRIEND_ALL) {
			size = db.queryFriendAllSize();
			//只查询好友个数  组织列表在item0里
//			size = db.queryFriendSize(Connections.type_persion);
		}
		else if (filter == FILTER_PEOPLE_ALL) {
			size = db.queryPeopleAllSize(keyword);
		}
		else if (filter == FILTER_I_CREATE_OFFLINE_PEOPLE) {
			size = db.queryOfflinePeopleSize(keyword, JTContactMini.FROM_TYPE_I_CREATE);
		}
		else if (filter == FILTER_I_COLLECT_OFFLINE_PEOPLE) {
			size = db.queryOfflinePeopleSize(keyword, JTContactMini.FROM_TYPE_I_COLLECT);
		}
		else if (filter == FILTER_I_SAVE_OFFLINE_PEOPLE) {
			size = db.queryOfflinePeopleSize(keyword, JTContactMini.FROM_TYPE_I_SAVE);
		}
		return size;
	}

	/**
	 * 查找对应的关系
	 * @param charAt
	 * @return
	 */
	public int getCharAt(char charAt) {
//		return db.queryCharAt(charAt);
		return db.queryCharAt(charAt, filter, keyword);	//查数据库方法
//		return db.queryCharAt(arrData, charAt);	// 直接查方法
	}

	public void clearData() {
		if (filter == FILTER_ALL) {
			db.clearTable();
		} 
		else if (filter == FILTER_PEOPLE_ALL){
			db.clearTableWithPeopleAll();
		}
		else if (filter == FILTER_FRIEND_ALL){
			db.clearTableWithFriendAll();
		}
		
	}

	public void updataDel(String id, int type, boolean isOnline,
			Connections insertCon) {
		db.delInsert(id, type, isOnline, insertCon);
	}

	public void delete(String id, int type, boolean isOnline) {
		db.delete(id, type, isOnline);
	}

	public void insert(ArrayList<Connections> cns_data) {
		db.insert(cns_data);
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
