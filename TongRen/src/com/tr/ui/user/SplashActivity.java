package com.tr.ui.user;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.networkbench.agent.impl.NBSAppAgent;
import com.tr.App;
import com.tr.AppData;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.UserReqUtil;
import com.tr.db.AppDataDBManager;
import com.tr.model.api.DataBox;
import com.tr.model.im.MNotifyMessageBox;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.obj.JTFile;
import com.tr.model.user.JTMember;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.MainActivity;
import com.tr.ui.user.modified.LoadingGuideActivity;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.CommonReqType;
import com.utils.http.IBindData;

public class SplashActivity extends JBaseFragmentActivity implements IBindData {

	private final String TAG = getClass().getSimpleName();

	// 变量
	private App mMainApp;
	private boolean mBlnFromNotifyBox;
	private boolean mFromShare;
	private int pushMessageType; // 推送的消息类型
	private JTFile mShareInfo;
	private String loginData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		String MSG = "onCreate()";
		// Log.i(TAG, MSG);
		Uri uri = getIntent().getData();
		if (uri != null) {
			loginData = getIntent().getDataString();
		}
		// 处理消息
		boolean startFromNotify = getNotifyParam();
		if (startFromNotify) {
			ENavigate.startMainActivity(SplashActivity.this, MNotifyMessageBox.getInstance(), pushMessageType, loginData);
			finish();
			// if (checkTopActivity(this)) {
			// return;
			// }
		}
		// 处理分享
		boolean startFromShare = getShareParam();
		if (startFromShare) {
			if (checkTopActivityEx(this)) {
				return;
			}
		}
		// 变量初始化
		initVars();
		// 嵌入基调透镜SDK
		NBSAppAgent.setLicenseKey(EConsts.BENCH_APP_KEY).withLocationServiceEnabled(true).start(this);

	}

	@Override
	public void onStart() {
		super.onStart();
		String MSG = "onStart()";
		// 获取是否是第一次请求登陆信息
		// Log.i(TAG, MSG);
		// 检查更新
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				switch (updateStatus) {
				case UpdateStatus.Yes: // 有更新
					UmengUpdateAgent.showUpdateDialog(SplashActivity.this, updateInfo);
					break;
				default: // 默认处理
					// 先检查本地数据库中是否有loginconfig
					AppDataDBManager appDataDBManager = new AppDataDBManager(App.getApplicationConxt());
					String USER_ID = EUtil.getStringFromAppSetting(SplashActivity.this,EConsts.Key.USER_ID);
					AppData appData = appDataDBManager.query(USER_ID);
					// 有，获取本地loginconfig，走登录流程
					if (!"0".equals(USER_ID)&&appData!=null&&appData.getUser()!=null) {
						doLogin(appData);
					}else {
						// 没有，请求获取loginconfig，走登录流程
						UserReqUtil.doLoginConfiguration(SplashActivity.this, mIBindData, UserReqUtil.getDoLoginConfigurationParams("", "", EUtil.getDeviceID(SplashActivity.this),
								EUtil.getAppVersionName(SplashActivity.this), "", "", "", "android", "", "", mMainApp.getAppData().getUserName(), mMainApp.getAppData().getPassword()), null);
					}
					break;
				}
			}
		});

		// 设置监听事件
		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
			@Override
			public void onClick(int status) {
				try {
					switch (status) {
					case UpdateStatus.Update:
						break;
					default:
						// 先检查本地数据库中是否有loginconfig
						AppDataDBManager appDataDBManager = new AppDataDBManager(App.getApplicationConxt());
						String USER_ID = EUtil.getStringFromAppSetting(SplashActivity.this,EConsts.Key.USER_ID);
						AppData appData = appDataDBManager.query(USER_ID);
						// 有，获取本地loginconfig，走登录流程
						if (!"0".equals(USER_ID)&&appData!=null&&appData.getUser()!=null) {
							doLogin(appData);
						}else {
							// 没有，请求获取loginconfig，走登录流程
							UserReqUtil.doLoginConfiguration(SplashActivity.this, mIBindData, UserReqUtil.getDoLoginConfigurationParams("", "", EUtil.getDeviceID(SplashActivity.this),
									EUtil.getAppVersionName(SplashActivity.this), "", "", "", "android", "", "", mMainApp.getAppData().getUserName(), mMainApp.getAppData().getPassword()), null);
						}
						break;
					}
				} catch (Exception e) {
				}

			}
		});
		// 请求更新
		UmengUpdateAgent.update(this);
	}

	/** 获取推送参数 */
	public boolean getNotifyParam() {
		mBlnFromNotifyBox = getIntent().hasExtra(ENavConsts.ENotifyParam);
		if (mBlnFromNotifyBox) {
			pushMessageType = getIntent().getIntExtra(ENavConsts.EPushMessageType, 0);
		}
		return mBlnFromNotifyBox;
	}

	/**
	 * 获取分享参数
	 * 
	 * @return
	 */
	public boolean getShareParam() {
		Intent intent = getIntent();
		Log.d(TAG, intent.toString());
		if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SEND)) {

			if (TextUtils.isEmpty(intent.getExtras().getString(Intent.EXTRA_SUBJECT)) && TextUtils.isEmpty(intent.getStringExtra(Intent.EXTRA_TITLE))
					&& TextUtils.isEmpty(intent.getStringExtra(Intent.EXTRA_TEXT))) {
				showToast("无法分享此类型的消息");
			} else {
				mShareInfo = JTFile.createFromIntent(getIntent());
				CommonReqUtil.doFetchExternalKnowledgeUrl(this, this, mShareInfo.mUrl, true, null);
			}
			mFromShare = true;
			mShareInfo.isOnlineUrl = true;
		} else {
			mFromShare = false;
		}
		return mFromShare;
	}

	/**
	 * 检查目前的activity栈, 如果当前程序主页面位于栈顶,则将它提到最前,否则走正常启动流程 处理消息盒子信息
	 * 
	 * @param context
	 * @return
	 */
	private boolean checkTopActivity(Activity context) {
		try {
			ActivityManager manager = (ActivityManager) App.getApplicationConxt().getSystemService(ACTIVITY_SERVICE);
			List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(500);
			RunningTaskInfo myTask = null;

			for (int i = 0; i < runningTaskInfos.size(); i++) {
				RunningTaskInfo nowTask = runningTaskInfos.get(i);
				System.out.println("---------------包名-----------" + nowTask.topActivity.getPackageName());
				// 应用程序位于堆栈的顶层
				if ("com.tr".equals(nowTask.topActivity.getPackageName())) {
					myTask = nowTask;
					break;
				}
			}

			if (myTask != null) {
				String baseName = myTask.baseActivity.getClassName();
				String mainName = MainActivity.class.getName();
				if (baseName.equalsIgnoreCase(mainName)) {
					//
					MNotifyMessageBox box = MNotifyMessageBox.getInstance();
					Log.d(TAG, box.toString());
					ENavigate.startMainActivity(SplashActivity.this, MNotifyMessageBox.getInstance(), pushMessageType, loginData);
					finish();
					return true;
				}
			}
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * 检查目前的activity栈, 如果当前程序主页面位于栈顶,则将它提到最前,否则走正常启动流程 处理分享信息
	 * 
	 * @param context
	 * @return
	 */
	private boolean checkTopActivityEx(Activity context) {
		try {
			ActivityManager manager = (ActivityManager) App.getApplicationConxt().getSystemService(ACTIVITY_SERVICE);
			List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(500);
			RunningTaskInfo myTask = null;

			for (int i = 0; i < runningTaskInfos.size(); i++) {
				RunningTaskInfo nowTask = runningTaskInfos.get(i);
				// 应用程序位于堆栈的顶层
				if (getPackageName().equals(nowTask.topActivity.getPackageName())) {
					myTask = nowTask;
					break;
				}
			}
			if (myTask != null) {
				String baseName = myTask.baseActivity.getClassName();
				String mainName = MainActivity.class.getName();
				if (baseName.equalsIgnoreCase(mainName)) {
					// TODO
					ENavigate.startMainActivityEx(SplashActivity.this, mShareInfo, loginData);
					finish();
					return true;
				}
			}
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		return false;
	}

	private void initVars() {
		// 全局对象
		mMainApp = App.getApp();
	}

	// 接口回调函数对象
	private IBindData mIBindData = new IBindData() {

		@Override
		public void bindData(int tag, Object object) {
			String MSG = "bindData()";

			// TODO Auto-generated method stub
			if (tag == EAPIConsts.ReqType.LOGIN_CONFIGURATION) {
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					// 以下字段均保存到数据库
					// 保存SessionID
					mMainApp.getAppData().setSessionID(dataBox.mSessionID);
					// 保存货币范围
					if (dataBox.mListMoneyRange != null) {
						mMainApp.getAppData().setListMoneyRange(dataBox.mListMoneyRange);
					}
					// 保存货币类型
					if (dataBox.mListMoneyType != null) {
						mMainApp.getAppData().setListMoneyType(dataBox.mListMoneyType);
					}
					// 保存融资类型
					if (dataBox.mListInInvestType != null) {
						mMainApp.getAppData().setListInInvestType(dataBox.mListInInvestType);
					}
					// 保存投资类型
					if (dataBox.mListOutInvestType != null) {
						mMainApp.getAppData().setListOutInvestType(dataBox.mListOutInvestType);
					}
					// 保存投资区域
					if (dataBox.mListTrade != null) {
						mMainApp.getAppData().setListTrade(dataBox.mListTrade);
					}
					// 保存投资区域
					if (dataBox.mListArea != null) {
						mMainApp.getAppData().setListArea(dataBox.mListArea);
					}
					mMainApp.getAppData().mInviteJoinGinTongInfo = dataBox.mInviteJoinGinTongInfo;
					// 保存用户对象
					if (dataBox.mJTMember != null) {
						PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, EUtil.getMetaValue(SplashActivity.this, "api_key"));
						mMainApp.getAppData().setUser(dataBox.mJTMember);
						doLogin(mMainApp.getAppData());
					} else {
						// 如果没有返回用户对象，则跳转到登录界面
						Intent intent = new Intent(SplashActivity.this, LoadingGuideActivity.class);
						if (mBlnFromNotifyBox) { // 消息盒子
							intent.putExtra("message", "info");
							intent.putExtra(ENavConsts.ENotifyParam, MNotifyMessageBox.getInstance());
							intent.putExtra(ENavConsts.EPushMessageType, pushMessageType); // 消息推送类型
						} else if (mFromShare) {
							intent.putExtra(ENavConsts.EShareParam, mShareInfo);
						}
						intent.putExtra(ENavConsts.EFriendId, loginData);
						intent.putExtra("mFromShare", mFromShare);
						intent.putExtra("mJTMember", "null");
						startActivity(intent);
						finish();
					}

				} else {
					// 如果没有返回用户对象，则跳转到登录界面
					Intent intent = new Intent(SplashActivity.this, LoadingGuideActivity.class);
					if (mBlnFromNotifyBox) { // 消息盒子
						intent.putExtra("message", "info");
						intent.putExtra(ENavConsts.ENotifyParam, MNotifyMessageBox.getInstance());
						intent.putExtra(ENavConsts.EPushMessageType, pushMessageType); // 消息推送类型
					} else if (mFromShare) {
						intent.putExtra(ENavConsts.EShareParam, mShareInfo);
					}
					intent.putExtra(ENavConsts.EFriendId, loginData);
					intent.putExtra("mFromShare", mFromShare);
					intent.putExtra("mJTMember", "null");
					startActivity(intent);
					finish();
				}
			}
		}
	};
	/**
	 * 执行登录流程
	 * @param mAppData
	 */
	private void doLogin(AppData mAppData) {
		String MSG;
		if (mAppData.getUser().mUserType == JTMember.UT_ORGANIZATION) {
			// 机构用户当前状态
			if (mAppData.getUser().mOrganizationInfo.mState == -1) {
				// 未进行第一次认证（跳转到登录页面）
				ENavigate.startLoadingGuideActivity(SplashActivity.this, loginData);
//				finish();
			}
			
			// 0第一次认证进行中（跳转到登录页面）
			else if (mAppData.getUser().mOrganizationInfo.mState == 0) {
				ENavigate.startLoadingGuideActivity(SplashActivity.this, loginData);
				finish();
			}
			// 1第一次认证失败（跳转到登录页面）
			else if (mAppData.getUser().mOrganizationInfo.mState == 1) {
				
				MSG = "mMainApp.getAppData().getUser().mOrganizationInfo.mState == 1";
				Log.i(TAG, MSG);
				ENavigate.startLoginActivity(SplashActivity.this, null);
				finish();
			}
			// 2第一次认证成功
			else if (mAppData.getUser().mOrganizationInfo.mState == 2) {
				
				// 第一次认证成功
				if (mBlnFromNotifyBox) { // 消息盒子
					ENavigate.startMainActivity(SplashActivity.this, MNotifyMessageBox.getInstance(), pushMessageType, loginData);
				} else if (mFromShare) { // 分享
					ENavigate.startMainActivityEx(SplashActivity.this, mShareInfo, loginData);
				} else {
					verifyUserStatus(mAppData/*.getUser().getUserStatus(), mAppData.getUserName()*/);
				}
			}
			
		} else {
			// 个人用户直接登录
			if (mBlnFromNotifyBox) { // 消息盒子
				ENavigate.startMainActivity(SplashActivity.this, MNotifyMessageBox.getInstance(), pushMessageType, loginData);
			} else if (mFromShare) { // 分享
				ENavigate.startMainActivityEx(SplashActivity.this, mShareInfo, loginData);
			} else {
				verifyUserStatus(mAppData/*.getUser().getUserStatus(), mAppData.getUserName()*/);
			}
		}
	}

	private int type;

	@Override
	public void initJabActionBar() {
		jabGetActionBar().hide(); // 隐藏ActionBar
	}

	// 用户状态 0:邮箱未验证 1:信息未完善 2已完善个人信息
	/**
	 * @param userStatus
	 * @param account
	 *            用户账号
	 */
	private void verifyUserStatus(AppData mAppData/*final int userStatus, String account*/) {
		loginHuanXinIMService(mAppData/*,userStatus*/);
	}

	/**
	 *  登陆环信IM服务器 如果登陆成功 再请求自己的服务器
	 * @param mAppData
	 * @param userStatus
	 */
	private void loginHuanXinIMService(AppData mAppData) {
		
		EMChatManager.getInstance().login(mAppData.getUserID(), mAppData.getUserID(), new EMCallBack() {// 回调
					@Override
					public void onSuccess() {

						// 注册一个监听连接状态的listener
						EMChatManager.getInstance().addConnectionListener(new com.tr.imservice.MyConnectionListener(SplashActivity.this));

						runOnUiThread(new Runnable() {
							public void run() {
								EMGroupManager.getInstance().loadAllGroups();
								EMChatManager.getInstance().loadAllConversations();
								Log.d("main", "登陆聊天服务器成功！");
							}
						});
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						Log.d("main", "登陆聊天服务器失败！");
//						ENavigate.startLoginActivity(SplashActivity.this, loginData);
					}
				});
		
		switch (mAppData.getUser().getUserStatus()) {
		case 0:
			ENavigate.startLoginActivity(SplashActivity.this, loginData);
			break;
		case 1:
			ENavigate.startLoginActivity(SplashActivity.this, loginData);
			break;
		case 2:
			ENavigate.startMainActivity(SplashActivity.this, null, loginData);
			break;
		default:
			ENavigate.startLoginActivity(SplashActivity.this, loginData);
		}
		finish();
	}

	@Override
	public void bindData(int tag, Object object) {
		// 解析Url类型的知识
		if (tag == CommonReqType.FetchExternalKnowledgeUrl) {
			String MSG = " case  CommonReqType.FetchExternalKnowledgeUrl ";
			Log.i(TAG, MSG);

			if (object == null) {
				Toast.makeText(SplashActivity.this, "解析失败", 0).show();
				return;
			}

			Map<String, Object> dataHm = (Map<String, Object>) object;
			Knowledge2 knowledge = (Knowledge2) dataHm.get("knowledge2");
			mShareInfo.setmSuffixName(knowledge.getTitle());
			mShareInfo.mFileName = knowledge.getTitle();
			mShareInfo.reserved2 = knowledge.getTitle();
			mShareInfo.mTaskId = knowledge.getId() + "";
			mShareInfo.setmType(13);// 知识
			mShareInfo.setReserved1("1");
		}
	}

}
