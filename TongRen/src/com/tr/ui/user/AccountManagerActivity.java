package com.tr.ui.user;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushManager;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tr.App;
import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.db.AppDataDBManager;
import com.tr.model.api.DataBox;
import com.tr.model.user.JTMember;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.user.modified.LoginActivity;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

//import com.baidu.navisdk.util.common.StringUtils;

/**
 * 帐号管理页面
 * 
 * @author gushi
 */
public class AccountManagerActivity extends JBaseFragmentActivity {

	/** 退出按钮 */
	private TextView loginOutTv;
//	/** 修改密码按钮 */
//	private TextView changPasswordTv;
	private Context context;
	private ImageView avatar;
	private TextView myName;
//	private TextView myCounter;
	private RelativeLayout myBaseInfoRl;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "帐号管理", false, null, false, true);
//		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 环信登出成功，调用本地服务器登出
			case 0:
				UserReqUtil.doLoginOut(AccountManagerActivity.this, mBindData, new JSONObject(), null);
				break;
			// 环信登出失败
			case 1:
				Toast.makeText(getApplicationContext(), "退出失败，请重试", 0).show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		setContentView(R.layout.activity_account_manager);

		avatar = (ImageView) findViewById(R.id.avatar);
		myName = (TextView) findViewById(R.id.my_name);
//		myCounter = (TextView) findViewById(R.id.my_counter);
		myBaseInfoRl = (RelativeLayout) findViewById(R.id.my_base_info);
		myBaseInfoRl.setOnClickListener(mClickListener);

		loginOutTv = (TextView) findViewById(R.id.loginOutTv);
		loginOutTv.setOnClickListener(mClickListener);
//		changPasswordTv = (TextView) findViewById(R.id.changPasswordTv);
//		changPasswordTv.setOnClickListener(mClickListener);
		initData();

	}

	public Context getContext() {
		return context;
	}

	// 初始化数据
	private void initData() {
		// if (!StringUtils.isEmpty(App.getUser().getImage())) {
		// ImageLoader.getInstance().displayImage(App.getUser().getImage(),
		// avatar);
		// }
		// else {
		// avatar.setImageResource(R.drawable.ic_know_people);
		// }
		// if (!StringUtils.isEmpty(App.getUserName())) {
		// myName.setText(App.getNick());
		// }
		// if (!StringUtils.isEmpty(App.getUser().getmEmail())) {
		// myCounter.setText("账号：" + App.getUser().getmEmail());
		// } else {
		// myCounter.setText("账号：" + App.getUser().getmMobile());
		// }

		if (App.getApp().getAppData().getUser().getmUserType() == JTMember.UT_PERSON) {
			if (!StringUtils.isEmpty(App.getUser().getImage())) {
				ImageLoader.getInstance().displayImage(
						App.getUser().getImage(),
						avatar,
						new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
								/*.displayer(new RoundedBitmapDisplayer(10))*/
								.showImageOnFail(R.drawable.default_people_avatar).build());
			} else {
				avatar.setImageResource(R.drawable.default_people_avatar);
			}
			// 个人用户

			if (!StringUtils.isEmpty(App.getUser().getmNick())) {
				myName.setText(App.getUser().getmNick());
			} else if (!StringUtils.isEmpty(App.getUser().getmUserName())) {
				myName.setText(App.getUser().getmUserName());
			} else {
				myName.setText("");
			}

		}
		// 机构用户
		else {
			if (!StringUtils.isEmpty(App.getUser().getImage())) {
				ImageLoader.getInstance().displayImage(
						App.getUser().getImage(),
						avatar,
						new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
								/*.displayer(new RoundedBitmapDisplayer(10))*/
								.showImageOnFail(R.drawable.default_portrait116).build());
			} else {
				avatar.setImageResource(R.drawable.default_portrait116);
			}
			if (!StringUtils.isEmpty(App.getUser().mNick)) {
				myName.setText(App.getUser().mNick);
			} else if (!StringUtils.isEmpty(App.getUser().getmOrganizationInfo().mFullName)) {
				myName.setText(App.getUser().getmOrganizationInfo().mFullName);
			} else {
				myName.setText("");
			}
		}
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// 退出登录
			if (loginOutTv == v) {
				SharedPreferences upLoadParmar = getSharedPreferences("upLoad",Activity.MODE_PRIVATE);
				Editor editor = upLoadParmar.edit();
				editor.putBoolean("isComp", false);
				editor.putBoolean("isCompZonghe", false);
				editor.commit();
				showLoadingDialog();
				logout(new EMCallBack() {

					@Override
					public void onSuccess() {
						handler.sendEmptyMessage(0);
					}

					@Override
					public void onProgress(int arg0, String arg1) {
					}

					@Override
					public void onError(int arg0, String arg1) {
						handler.sendEmptyMessage(1);

					}
				});
			}

			// 我的详情
			else if (myBaseInfoRl == v) {
//				if (App.getUser().getmUserType() == JTMember.UT_PERSON) {
//					ENavigate.startRelationHomeActivity(AccountManagerActivity.this, App.getApp().getAppData()
//							.getUser().mID, true, ENavConsts.type_details_member);
//				}
//				if (App.getUser().getmUserType() == JTMember.UT_PERSON) {
//					ENavigate.startRelationHomeActivity(AccountManagerActivity.this, App.getApp().getAppData()
//							.getUser().mID, true, ENavConsts.type_details_member);
					//账号信息管理界面
					ENavigate.startAccountInformationActivity(AccountManagerActivity.this);
//				}
			}
			// // 修改密码
			// else if (changPasswordTv == v) {
			// ENavigate.startChangePasswordActivity(getContext());
			// }
		}
	};

	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int tag, Object object) {
			// TODO Auto-generated method stub
			dismissLoadingDialog();
			if (tag == EAPIConsts.ReqType.LOGIN_OUT) {
				if (object != null) {
					/*
					 * ConnectionsCache.getInstance().setmConnections(null);//
					 * 清除关系列表缓存 DataBox dataBox = (DataBox) object;
					 * mMainApp.mAppData.setSessionID(dataBox.mMessage); //
					 * 清除用户名密码信息 mMainApp.mAppData.setUserName("");
					 * mMainApp.mAppData.setPassword(""); // 清除用户对象信息
					 * PushManager.stopWork(getApplicationContext());//停止百度推送
					 * AppDataDBManager dbManager = new
					 * AppDataDBManager(SettingActivity.this);
					 * dbManager.delete(mMainApp.mAppData.getUser().mID + "");
					 * // 清除UserID mMainApp.mAppData.setUserID(""); //
					 * 发送广播销毁所有页面 sendBroadcast(new
					 * Intent(EConsts.Action.LOGIN_OUT)); // 转到登录界面 Intent
					 * intent = new Intent(SettingActivity.this,
					 * LoginActivity.class);
					 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					 * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					 * startActivity(intent); finish();
					 */
					DataBox dataBox = (DataBox) object;
					App.getApp().getAppData().setSessionID(dataBox.mMessage);
				}

				/*
				 * ConnectionsCache.getInstance().setmConnections(null);//清除关系列表缓存
				 * // 清除用户名密码信息 mMainApp.mAppData.setUserName("");
				 * mMainApp.mAppData.setPassword(""); // 清除用户对象信息
				 * PushManager.stopWork(getApplicationContext());//停止百度推送
				 * AppDataDBManager dbManager = new
				 * AppDataDBManager(SettingActivity.this);
				 * dbManager.delete(mMainApp.mAppData.getUser().mID + ""); //
				 * 清除UserID mMainApp.mAppData.setUserID(""); // 发送广播销毁所有页面
				 * sendBroadcast(new Intent(EConsts.Action.LOGIN_OUT)); //
				 * 转到登录界面 Intent intent = new Intent(SettingActivity.this,
				 * LoginActivity.class);
				 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 * intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				 * startActivity(intent); finish();
				 */
				exit();
			}
		}
	};

	public void logout(final EMCallBack callback) {
		// setPassword(null);
		try {
			EMChatManager.getInstance().endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
		EMChatManager.getInstance().logout(new EMCallBack() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.onSuccess();
				}
			}

			@Override
			public void onError(int code, String message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgress(int progress, String status) {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.onProgress(progress, status);
				}
			}

		});
	}

	void endCall() {
		try {
			EMChatManager.getInstance().endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 遍历所有Activity并finish
		public void exit() {

			// 清除用户名密码信息
			App.getApp().getAppData().setUserName("");
			App.getApp().getAppData().setPassword("");
			// 停止百度推送
			PushManager.stopWork(getApplicationContext());
			// 清除用户对象信息
			AppDataDBManager dbManager = new AppDataDBManager(this);
			dbManager.delete(App.getApp().getAppData().getUser().mID + "");
			// 清除UserID
			App.getApp().getAppData().setUserID("");
			// 发送广播销毁所有页面
			// sendBroadcast(new Intent(EConsts.Action.LOGIN_OUT));
			// 销毁所有Activity

			/*
			 * 这里是原来的代码 if (FrgConnections.connectionsDBManager != null) {
			 * FrgConnections.connectionsDBManager.clearTable(); }
			 */
			// 这里是新改的代码 20150119 by hanqi
			/*
			 * if (connectionsDBManager != null) {
			 * connectionsDBManager.clearTable(); }
			 */

			SharedPreferences sp = getApplicationContext().getSharedPreferences(EConsts.share_firstLoginGetConnections,
					getApplicationContext().MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putInt(EConsts.share_itemFirstLogin, 0);
			editor.putString(EConsts.share_itemUserTableName, "");
			editor.commit();
			for (Activity activity : App.activityList) {
				activity.finish();
			}

			// System.exit(0);
			// 跳转到登录界面
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			
		}

}
