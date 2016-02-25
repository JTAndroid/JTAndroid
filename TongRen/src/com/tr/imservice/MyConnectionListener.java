package com.tr.imservice;

import android.app.Activity;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.NetUtils;
import com.tr.App;

//环信自动重连机制
// 实现ConnectionListener接口
public class MyConnectionListener implements EMConnectionListener {

	private String simpleName = "环信集成： " + MyConnectionListener.class.getSimpleName();
	private Activity mActivity;
	public MyConnectionListener(Activity mActivity){
		this.mActivity = mActivity;
	}

	@Override
	public void onDisconnected(int error) {
		if (error == EMError.USER_REMOVED) {
			onCurrentAccountRemoved();
		} else if (error == EMError.CONNECTION_CONFLICT) {
			onConnectionConflict();
		} else {
			if (NetUtils.hasNetwork(App.getApplicationConxt())){//连接不到聊天服务器
				loginEasemob();//重新登陆环信
			}else{////当前网络不可用，请检查网络设置
				
			}
			onConnectionDisconnected(error);
		}
	}
	
	/**
	 * 重新登录环信
	 */
	private void loginEasemob() {
		EMChatManager.getInstance().login(
				App.getApp().getAppData().getUserID(),
				App.getApp().getAppData().getUserID(), new EMCallBack() {// 回调
					@Override
					public void onSuccess() {
						if(mActivity != null){
							mActivity.runOnUiThread(new Runnable() {
								public void run() {
									EMGroupManager.getInstance().loadAllGroups();
									EMChatManager.getInstance()
											.loadAllConversations();
								}
							});
						}
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
					}
				});
	}

	@Override
	public void onConnected() {
		onConnectionConnected();
	}

	/**
	 * the developer can override this function to handle connection conflict
	 * error
	 */
	protected void onConnectionConflict() {
		Log.i(simpleName, "onConnectionConflict");
	}

	/**
	 * the developer can override this function to handle user is removed error
	 */
	protected void onCurrentAccountRemoved() {
		Log.i(simpleName, "onCurrentAccountRemoved");
	}

	/**
	 * handle the connection connected
	 */
	protected void onConnectionConnected() {
		Log.i(simpleName, "onConnectionConnected");
	}

	/**
	 * handle the connection disconnect
	 * 
	 * @param error
	 *            see {@link EMError}
	 */
	protected void onConnectionDisconnected(int error) {
		Log.i(simpleName, "onConnectionDisconnected");
	}

}