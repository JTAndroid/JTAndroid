package com.tr.service;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.CursorJoiner.Result;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.tr.App;
import com.tr.api.ConnectionsReqUtil;
import com.tr.db.ConnectionsCacheData;
import com.tr.db.ConnectionsDBManager;
import com.tr.model.obj.Connections;
import com.tr.ui.home.frg.FrgConnections2;
import com.tr.ui.widgets.KnoCategoryAlertDialog.OperType;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 获得联系人列表 服务
 * 
 * @author gushi
 * 
 */
public class GetConnectionsListService extends Service implements IBindData {

	public static final String TAG = "GetConnectionsListService";

	private ConnectionsDBManager connectionsDBManager;
	public RequestType requestType = RequestType.All;
	public static Context mContext;

	public enum RequestType {
		/** 所有好友 (用户好友 和 组织好友) ( 通讯录 ) */
		FriendAll,
		/** 好友和 人脉 */
		PeopleAll,
		/**
		 * 根据类型获得指定类型关系及 全部类型关系 列表 {'organizationFriend':true,
		 * 'personFriend':true, 'customer':false, 'person':false}
		 */
		All
	}

	/** 失败重连次数 */
	private int reConnectionCount;

	/** 联网获取到的关系数据列表 */
	private ArrayList<Connections> connArr;

	/** 启动 获得联系人列表 服务 */
	public static void startGetConnectionsListService(Context context, RequestType requestType) {
		Intent intent = new Intent(EConsts.Action.ACTION_GET_CONNECTIONS_LIST_FINISH);
		intent.setClass(context, GetConnectionsListService.class);
		intent.putExtra(EConsts.Key.REQUEST_TYPE, requestType);
		context.startService(intent);
		mContext = context;
	}

	/** Binder，用来获取服务对象 */
	private ServiceBinder binder;

	public ServiceBinder getBinder() {
		if (binder == null) {
			binder = new ServiceBinder();
		}
		return binder;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		String MSG = "onCreate()";
		Log.i(TAG, MSG);
		App.connectionDataBaseWriteOver = false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			String MSG = "onStartCommand()";
			Log.i(TAG, MSG);
			if (intent != null && intent.hasExtra(EConsts.Key.REQUEST_TYPE)) {
				requestType = (RequestType) intent.getSerializableExtra(EConsts.Key.REQUEST_TYPE);
			}
			startGetConnections();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	public class ServiceBinder extends Binder {
		/**获取服务对象*/
		public GetConnectionsListService getService() {
			return GetConnectionsListService.this;
		}
	}

	private void startGetConnections() {

		if (requestType == RequestType.PeopleAll) {

			JSONObject jb = new JSONObject();
			try {
				jb.put("type", "0");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ConnectionsReqUtil.doGetConnectionsList(getApplication(), GetConnectionsListService.this, jb, null);

		} else if (requestType == RequestType.FriendAll) {

			JSONObject jb = new JSONObject();
			try {
				jb.put("type", "0");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ConnectionsReqUtil.doGetFriends(getApplication(), GetConnectionsListService.this, jb, null);

		} else if (requestType == RequestType.All) {
			ConnectionsReqUtil.doGetAllRelations(getApplication(), GetConnectionsListService.this, RequestType.All, null);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {

		// 获取联系人列表
		if (tag == EAPIConsts.concReqType.CONNECTIONSLIST || tag == EAPIConsts.concReqType.getFriends || tag == EAPIConsts.concReqType.getAllRelations) {
			final String MSG = "bindData()-- EAPIConsts.concReqType.CONNECTIONSLIST";
			Log.i(TAG, MSG);
			App.getUserID();
			if (object != null) {
				Log.i(TAG, MSG + " object != null ");
				connArr = (ArrayList<Connections>) object;
				Activity activity = (Activity) mContext;
				if (activity != null) {
					// activity.runOnUiThread(writeDataBaseRunnable);
					connectionsDBManager = ConnectionsDBManager.getInstance(getApplicationContext());
					final String tableName = connectionsDBManager.getTableName();

					AsyncTask<RequestType, Integer, Boolean> task = new AsyncTask<RequestType, Integer, Boolean>() {

						@Override
						protected Boolean doInBackground(RequestType... params) {

							synchronized (connectionsDBManager) {

								ConnectionsCacheData tempConnectionsCacheData = new ConnectionsCacheData(connectionsDBManager);
								if (requestType == RequestType.PeopleAll) {
									tempConnectionsCacheData.setFilterType(ConnectionsCacheData.FILTER_PEOPLE_ALL);
								} else if (requestType == RequestType.FriendAll) {
									tempConnectionsCacheData.setFilterType(ConnectionsCacheData.FILTER_FRIEND_ALL);
								} else if (requestType == RequestType.All) {
									tempConnectionsCacheData.setFilterType(ConnectionsCacheData.FILTER_ALL);
								}
								tempConnectionsCacheData.clearData();
								ArrayList<Connections> insertData = new ArrayList<Connections>();
								for (int i = 0; i < connArr.size(); i++) {
									insertData.add(connArr.get(i));
									if (insertData.size() >= 100) {
										tempConnectionsCacheData.insert(insertData);
										insertData.clear();
									}
								}
								if (insertData.size() != 0) {
									tempConnectionsCacheData.insert(insertData);
									insertData.clear();
								}
							}

							return true;
						}

						@Override
						protected void onPostExecute(Boolean result) {
							super.onPostExecute(result);
							// 写入完成
							App.connectionDataBaseWriteOver = true;

							SharedPreferences sp = getApplication().getSharedPreferences(EConsts.share_firstLoginGetConnections, getApplicationContext().MODE_PRIVATE);
							int firstLogin = sp.getInt(EConsts.share_itemFirstLogin, 0);
							// 第一次登陆 (firstLogin == 0 为第一次登录)
							if (firstLogin == 0) {
								Editor editor = sp.edit();
								editor.putInt(EConsts.share_itemFirstLogin, 1);
								editor.commit();
							}

							// 发送广播
							Intent intent = new Intent(EConsts.Action.ACTION_GET_CONNECTIONS_LIST_FINISH);
							intent.putExtra(EConsts.Key.TABLE_NAME, tableName);
							intent.putExtra(EConsts.Key.SUCCESS, true);
							sendBroadcast(intent);
							Log.i(TAG, MSG + " EConsts.Key.SUCCESS = " + true);
						}
					};
					task.execute();
				}
			}
			// 如果为空 请求失败
			else {
				if (reConnectionCount < 3) {
					reConnectionCount++;
					startGetConnections();
					Log.i(TAG, MSG + " EConsts.Key.SUCCESS = " + false + " reConnectionCAount = " + reConnectionCount);
				} else {
					Intent intent = new Intent(EConsts.Action.ACTION_GET_CONNECTIONS_LIST_FINISH);
					intent.putExtra(EConsts.Key.SUCCESS, false);
					sendBroadcast(intent);
					Log.i(TAG, MSG + " EConsts.Key.SUCCESS = " + false);
				}
			}
			// 停止服务
			stopSelf();

		}

	}

}
