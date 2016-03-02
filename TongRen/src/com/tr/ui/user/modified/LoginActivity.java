package com.tr.ui.user.modified;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.tr.App;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.model.im.MNotifyMessageBox;
import com.tr.model.obj.JTFile;
import com.tr.model.user.JTMember;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.MainActivity;
import com.tr.ui.home.ShareConfig;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.organization.create_clientele.CreateOrganizationActivity;
import com.tr.ui.organization.model.RegisteOrgDetail;
import com.tr.ui.user.RegisterOrganizationContactActivity;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.utils.time.Util;

//import com.baidu.navisdk.util.common.StringUtils;

/**
 * @ClassName: LoginActivity.java
 * @Description: 登陆页面
 * @Author xuxinjian/leon
 * @Version v 1.0
 * @Created 2014-03-28
 * @Updated 2014-04-14
 */
public class LoginActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();

	// 控件
	private TextView registerTv; // 注册
	private TextView loginTv; // 登录
	private EditText userNameEt; // 用户名
	private EditText passwordEt; // 密码
	private TextView forgetPsdTv; // 忘记密码
	private boolean mBlnFromNotifyBox;
	private boolean isFirstFaile = false;
	private String firstFaileText = "";
	private int pushMessageType; // 推送的消息类型
	private boolean mFromShare; // 从分享启动
	// 用户名和密码图标
	private ImageView login_user_acountIv;
	private ImageView login_user_passwordIv;
	// 第三方登陆图标
	private ImageView login_qq_Iv;
	private ImageView login_sinaweibo_Iv;

	private static final int MSG_AUTH_COMPLETE = 1;
	private static final int MSG_AUTH_ERROR = 2;
	private static final int MSG_AUTH_CANCEL = 3;
	// 变量
	private App mMainApp;
	private JTFile mShareInfo;
	private int mLoginType;// 第三方登陆类型 100为QQ 200为新浪微博
	private boolean hasUsername = false;
	private boolean hasPassword = false;
	private String userId, token;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_act_login);

		Log.d("xmx", "LoginActivity");
		delUsername = (ImageView) findViewById(R.id.delete_input_username);
		delPassword = (ImageView) findViewById(R.id.delete_input_password);

		friendId = getIntent().getStringExtra(ENavConsts.EFriendId);
		System.out.println("---------------LoginActivity--------friendId"
				+ friendId);
		// 处理消息
		boolean startFromNotify = getNotifyParam();
		if (startFromNotify) {
			if (checkTopActivity(this)) {
				return;
			}
		}
		// 处理分享
		boolean startFromShare = getShareParam();
		if (startFromShare) {
			if (checkTopActivityEx(this)) {
				return;
			}
		}
		initVars();
		initComponent();
		initShareSDKLogin();

	}

	/**
	 * 利用shareSDK实现第三方登陆
	 */
	private void initShareSDKLogin() {
		login_qq_Iv = (ImageView) findViewById(R.id.login_qq);
		login_sinaweibo_Iv = (ImageView) findViewById(R.id.login_sinaweibo);
		login_qq_Iv.setOnClickListener(mClickListener);
		login_sinaweibo_Iv.setOnClickListener(mClickListener);
	}

	/** 获取推送参数 */
	public boolean getNotifyParam() {
		mBlnFromNotifyBox = getIntent().hasExtra(ENavConsts.ENotifyParam);
		if (mBlnFromNotifyBox) {
			pushMessageType = getIntent().getIntExtra(
					ENavConsts.EPushMessageType, 0);
		}
		return mBlnFromNotifyBox;
	}

	/**
	 * 获取分享参数
	 * 
	 * @return
	 */
	public boolean getShareParam() {
		mShareInfo = (JTFile) getIntent().getSerializableExtra(
				ENavConsts.EShareParam);
		if (mShareInfo != null) {
			mFromShare = true;
		} else {
			mFromShare = false;
		}
		return mFromShare;
	}

	/**
	 * 检查目前的activity栈, 如果当前程序主页面位于栈顶,则将它提到最前,否则走正常启动流程
	 * 
	 * @param context
	 * @return
	 */
	private boolean checkTopActivity(Activity context) {
		try {
			ActivityManager manager = (ActivityManager) App
					.getApplicationConxt().getSystemService(ACTIVITY_SERVICE);
			List<RunningTaskInfo> runningTaskInfos = manager
					.getRunningTasks(500);
			RunningTaskInfo myTask = null;

			for (int i = 0; i < runningTaskInfos.size(); i++) {
				RunningTaskInfo nowTask = runningTaskInfos.get(i);
				System.out.println("---------------包名-----------"
						+ nowTask.topActivity.getPackageName());
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
					ENavigate.startMainActivity(LoginActivity.this,
							MNotifyMessageBox.getInstance(), pushMessageType,
							friendId);
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
			ActivityManager manager = (ActivityManager) App
					.getApplicationConxt().getSystemService(ACTIVITY_SERVICE);
			List<RunningTaskInfo> runningTaskInfos = manager
					.getRunningTasks(500);
			RunningTaskInfo myTask = null;

			for (int i = 0; i < runningTaskInfos.size(); i++) {
				RunningTaskInfo nowTask = runningTaskInfos.get(i);
				// 应用程序位于堆栈的顶层
				if (getPackageName().equals(
						nowTask.topActivity.getPackageName())) {
					myTask = nowTask;
					break;
				}
			}
			if (myTask != null) {
				String baseName = myTask.baseActivity.getClassName();
				String mainName = MainActivity.class.getName();
				if (baseName.equalsIgnoreCase(mainName)) {
					ENavigate.startMainActivityEx(LoginActivity.this,
							JTFile.createFromIntent(getIntent()), friendId);
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

	public void initComponent() {
		login_user_acountIv = (ImageView) findViewById(R.id.ruler1Iv);
		login_user_passwordIv = (ImageView) findViewById(R.id.ruler2Iv);
		// 登录
		loginTv = (TextView) findViewById(R.id.loginTv);
		loginTv.setOnClickListener(mClickListener);
		// 用户名
		loginTv.setClickable(false);

		// username and password
		userNameEt = (EditText) findViewById(R.id.userNameEt);
		userNameEt.setText(mMainApp.getAppData().getUserName());
		passwordEt = (EditText) findViewById(R.id.passwordEt);
		passwordEt.setText(mMainApp.getAppData().getPassword());

		if (!StringUtils.isEmpty(userNameEt.getText().toString())) {
			hasUsername = true;
			delUsername.setVisibility(View.VISIBLE);
		}
		if (!StringUtils.isEmpty(passwordEt.getText().toString())
				&& (passwordEt.getText().toString()).length() >= 6) {
			hasPassword = true;
			delPassword.setVisibility(View.VISIBLE);
		}

		if (!StringUtils.isEmpty(mMainApp.getAppData().getUserName())
				&& !StringUtils.isEmpty(mMainApp.getAppData().getPassword())) {
			loginTv.setBackgroundResource(R.drawable.button_circle_click);
			loginTv.setClickable(true);
		}
		userNameEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!StringUtils.isEmpty(s.toString())) {
					hasUsername = true;
					delUsername.setVisibility(View.VISIBLE);
					login_user_acountIv
							.setImageResource(R.drawable.login_user_acount);
				} else {
					hasUsername = false;
					delUsername.setVisibility(View.INVISIBLE);
					login_user_acountIv
							.setImageResource(R.drawable.login_user_acount_default);
				}
				if (hasUsername == true && hasPassword == true) {
					loginTv.setBackgroundResource(R.drawable.button_circle_click);
					loginTv.setClickable(true);
				} else {
					loginTv.setBackgroundResource(R.drawable.button_circle_noclick);
					loginTv.setClickable(false);
				}
			}
		});
		// 密码
		passwordEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (StringUtils.isEmpty(s.toString())) {
					login_user_passwordIv
							.setImageResource(R.drawable.login_user_password_default);
				} else {
					login_user_passwordIv
							.setImageResource(R.drawable.login_user_password);
				}
				if (!StringUtils.isEmpty(s.toString()) && s.length() >= 6) {
					hasPassword = true;
					delPassword.setVisibility(View.VISIBLE);
				} else {
					hasPassword = false;
					delPassword.setVisibility(View.INVISIBLE);
				}
				if (hasUsername == true && hasPassword == true) {
					loginTv.setBackgroundResource(R.drawable.button_circle_click);
					loginTv.setClickable(true);
				} else {
					loginTv.setBackgroundResource(R.drawable.button_circle_noclick);
					loginTv.setClickable(false);
				}
			}
		});
		// userNameEt.setText("12010001000");
		// passwordEt.setText("111111");
		// 删除
		delPassword.setOnClickListener(mClickListener);
		delUsername.setOnClickListener(mClickListener);
		// 注册
		registerTv = (TextView) findViewById(R.id.registerTv);
		registerTv.setOnClickListener(mClickListener);
		// 忘记密码
		forgetPsdTv = (TextView) findViewById(R.id.forgetPsdTv);
		forgetPsdTv.setOnClickListener(mClickListener);

	}

	// 点击事件监听器
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v == registerTv) { // 注册
				//传统注册
//				ENavigate.startRegisterGintongAccountActivity(LoginActivity.this);
				//组织完善资料
//				ENavigate.startCreateOrgNullActivityForResult(LoginActivity.this);
				//改版注册
				ENavigate.startRegisterGintongAllPathActivity(LoginActivity.this);
			} else if (v == loginTv) { // 登陆

				// 检查用户名密码是否合法
				if (EUtil.isUsernameFormatCorrect(LoginActivity.this,
						userNameEt.getText().toString())
						&& EUtil.isPasswordFormatCorrect(LoginActivity.this,
								passwordEt.getText().toString())) {
					// 显示加载框
					showLoadingDialog();
					// 登录
					if (mMainApp.getAppData().getSessionID().length() > 0) {
						UserReqUtil.doLogin(LoginActivity.this, mIBindData, UserReqUtil.getDoLoginParams(userNameEt.getText().toString(), passwordEt.getText().toString()), null);
					} else {
						UserReqUtil
								.doLoginConfiguration(
										LoginActivity.this,
										mIBindData,
										UserReqUtil.getDoLoginConfigurationParams(
												"",
												"",
												EUtil.getDeviceID(LoginActivity.this),
												EUtil.getAppVersionName(LoginActivity.this),
												"",
												"",
												"",
												"android",
												"",
												"",
												userNameEt.getText().toString(),
												passwordEt.getText().toString()),
										null);
					}
				}
			} else if (v == forgetPsdTv) { // 忘记密码
//				startActivity(new Intent(LoginActivity.this,
//						ForgetPasswordActivity.class));
				startActivity(new Intent(LoginActivity.this,
						ForgetPwdActivity.class));
			} else if (v == delPassword) {
				// 删除密码
				passwordEt.setText("");

			} else if (v == delUsername) {
				// 删除账号
				userNameEt.setText("");
			} else if (v == login_qq_Iv) {
				// QQ登陆
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("AppId", ShareConfig.APPID_QQFRIEND);
				map.put("AppKey", ShareConfig.APPKEY_QQFRIEND);
				map.put("Enable", ShareConfig.ENABLE_QQFRIEND);

				ShareSDK.setPlatformDevInfo(QQ.NAME, map);
				ShareSDK.initSDK(LoginActivity.this, ShareConfig.APPKEY);
				Platform qq = ShareSDK.getPlatform(QQ.NAME);
				mLoginType = 100;
				authorize(qq);
			} else if (v == login_sinaweibo_Iv) {
				// 新浪微博登陆
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("AppKey", ShareConfig.APPKEY_SINA_WEIBO);
				map.put("AppSecret", ShareConfig.APPSECRET_SINA_WEIBO);
				map.put("RedirectUrl", ShareConfig.REDIRECTURL_SINA_WEIBO);
				map.put("ShareByAppClient",
						ShareConfig.SHAREBYAPPCLIENT_SINA_WEIBO);
				map.put("Enable", ShareConfig.ENABLE_SINA_WEIBO);
				ShareSDK.setPlatformDevInfo(SinaWeibo.NAME, map);
				ShareSDK.initSDK(LoginActivity.this, ShareConfig.APPKEY);
				Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
				mLoginType = 200;
				authorize(sina);
			}

		}
	};

	/**
	 * 执行授权,获取用户信息
	 * 
	 * @param plat
	 */
	private void authorize(Platform plat) {
		// 判断指定平台是否已经完成授权
		if (plat.isValid()) {
			userId = plat.getDb().getUserId();
			String nickName = plat.getDb().getUserName();
			token = plat.getDb().getToken();
			saveUserData(token, nickName);
			if (userId != null) {
				// 显示加载框
				showLoadingDialog();
				// 已授权，验证是否已绑定已有账号或已完善完资料
				UserReqUtil.doThird_Login(LoginActivity.this, mIBindData, "",
						"", EUtil.getDeviceID(LoginActivity.this),
						EUtil.getAppVersionName(LoginActivity.this), "", "",
						"", "android", "", "", token, userId,
						String.valueOf(mLoginType), null);
//				Util.toast(LoginActivity.this, plat.getName() + "已登陆", true);
//				ENavigate.startCompleteuserInfoActivity(LoginActivity.this);
				return;
			}
		}
		MyPlatformActionListener listener = new MyPlatformActionListener();
		plat.setPlatformActionListener(listener);
		String name = plat.getName();
		if ("QQ".equals(name))
			// true不使用SSO授权，false使用SSO授权
			plat.SSOSetting(false);
		else
			// 微博不使用客户端登录（登录得升级SDK）
			plat.SSOSetting(true);
		// 执行登录，登录后在回调里面获取用户资料
		plat.showUser(null);
		// plat.showUser(“3189087725”);//获取账号为“3189087725”的资料
	}

	/**
	 * 
	 * 授权登陆监听
	 */
	private class MyPlatformActionListener implements PlatformActionListener {

		@Override
		public void onCancel(Platform platform, int action) {
			// TODO 取消授权
			Message msg = new Message();
			msg.what = MSG_AUTH_CANCEL;
			handler.sendMessage(msg);

		}

		@Override
		public void onComplete(Platform platform, int action,
				HashMap<String, Object> res) {

			// 解析部分用户资料字段
			if (action == Platform.ACTION_USER_INFOR) {
				String  nickName, gender, profile_image_url;
				
				userId = platform.getDb().getUserId();// ID
				token = platform.getDb().getToken();
				gender = platform.getDb().getUserGender();// 性别 m是男 反之则为女
				nickName = platform.getDb().getUserName();// 用户名
				profile_image_url = platform.getDb().getUserIcon();// 头像
				
				saveUserData(token,nickName);
				
				Message msg = new Message();
				msg.what = MSG_AUTH_COMPLETE;
				handler.sendMessage(msg);
			}

		}

		@Override
		public void onError(Platform platform, int action, Throwable t) {
			// TODO 授权失败
			platform.removeAccount();
			Message msg = new Message();
			msg.what = MSG_AUTH_ERROR;
			handler.sendMessage(msg);
		}

	}

	/**
	 * 保存第三方QQ/微博的Token/UserName
	 * 
	 * @param token
	 * @param nickName
	 */
	private void saveUserData(String token, String nickName) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(LoginActivity.this);
			sp.edit().putString("mToken", token).commit();
			sp.edit().putInt("mLoginType", mLoginType).commit();
			sp.edit().putString("mNickName", nickName).commit();
	}

	// 接口回调函数对象
	private IBindData mIBindData = new IBindData() {

		@Override
		public void bindData(int tag, Object object) {
			String MSG = "bindData()";

			// 隐藏加载框
//			dismissLoadingDialog();
			if (tag == EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_REGIST_ORG_DETAIL
					&& object != null) {
				RegisteOrgDetail detail = (RegisteOrgDetail) object;
				ENavigate.startCreateOrgActivity(LoginActivity.this, detail);
			}
			if (tag == EAPIConsts.ReqType.FINDORG) { // 请求组织信息
				// 是否进行下一步操作
				dismissLoadingDialog();
				// 跳转到个人信息完善界面
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					Intent intent = new Intent(LoginActivity.this, CreateOrganizationActivity.class);
					intent.putExtra(EConsts.Key.ORGINFOVO, dataBox.orgInfoVo);
					if (isFirstFaile) {
						intent.putExtra(EConsts.Key.ISSHOWDIALOG, true);
						intent.putExtra(EConsts.Key.FAILETEXT, firstFaileText);
				}
					// 跳转到组织完善信息页面
					startActivity(intent);
//					finish();
				}
			}
			// 初始化数据
			if (tag == EAPIConsts.ReqType.LOGIN) {
				MSG = " tag == EAPIConsts.ReqType.LOGIN  ";
				Log.i(TAG, MSG);
				if (object != null) {
					DataBox dataBox = (DataBox) object;

					if (dataBox.mJTMember != null) {
						if (dataBox.mJTMember.userStatus == 0) {
							Toast.makeText(LoginActivity.this, "邮箱未验证，请登录邮箱验证", 1).show();
							dismissLoadingDialog();
							return ;
						}
//						if (dataBox.mJTMember.mOrganizationInfo.mState == 0) {
//							Toast.makeText(LoginActivity.this, "组织资料正在审核,请等待审核通过后再登录", 1).show();
//							return ;
//						}
						PushManager.startWork(getApplicationContext(),PushConstants.LOGIN_TYPE_API_KEY, EUtil.getMetaValue(LoginActivity.this,"api_key"));
						mMainApp.getAppData().setUser(dataBox.mJTMember);
						// mMainApp.mAppData.mInviteJoinGinTongInfo=dataBox.mInviteJoinGinTongInfo;
						mMainApp.getAppData().setUserName(userNameEt.getText().toString());
						mMainApp.getAppData().setPassword(passwordEt.getText().toString());
						mMainApp.getAppData().setmBrowseHomepageType(dataBox.mJTMember.getHomePageVisible());
						mMainApp.getAppData().setmFriendsAppraiseType(dataBox.mJTMember.getEvaluateVisible());
						mMainApp.getAppData().setLogin_timeout(false);
						if (dataBox.mJTMember.getListIndustry() != null) {
							mMainApp.getAppData().getmIndustrys().setListIndustry(dataBox.mJTMember.getListIndustry());
						}
						MSG = " mMainApp.getAppData().getUser().mUserType = "+ mMainApp.getAppData().getUser().mUserType;
						Log.i(TAG, MSG);
						if (mMainApp.getAppData().getUser().mUserType == JTMember.UT_ORGANIZATION) {
							MSG = "mMainApp.getAppData().getUser().mUserType == JTMember.UT_ORGANIZATION";
							Log.i(TAG, MSG);
							MSG = "mMainApp.getAppData().getUser().mOrganizationInfo.mState = "+ mMainApp.getAppData().getUser().mOrganizationInfo.mState;
							Log.i(TAG, MSG);
							// 机构用户当前状态
							if (mMainApp.getAppData().getUser().mOrganizationInfo.mState == -1) {
								// 未进行第一次认证（跳转到认证页面）
//								startActivity(new Intent(LoginActivity.this,RegisterPersonalDetailActivity.class));
								UserReqUtil.doFindOrg(LoginActivity.this, mIBindData, dataBox.mJTMember.mOrganizationInfo.mLegalPersonIDCardImage, null);
							}
							// 第一次认证进行中
							else if (mMainApp.getAppData().getUser().mOrganizationInfo.mState == 0) {
								Toast.makeText(LoginActivity.this, "组织资料正在审核,请等待审核通过后再登录", 1).show();
								
							}
							// 1第一次认证失败(跳转到创建组织)
							else if (mMainApp.getAppData().getUser().mOrganizationInfo.mState == 1) {

								MSG = "mMainApp.getAppData().getUser().mOrganizationInfo.mState == 1";
								Log.i(TAG, MSG);
								
								UserReqUtil.doFindOrg(LoginActivity.this, mIBindData, dataBox.mJTMember.mOrganizationInfo.mLegalPersonIDCardImage, null);
								isFirstFaile = true;
								firstFaileText = mMainApp.getAppData().getUser().getmOrganizationInfo().mFailInfo;
							}
							// 2第一次认证成功
							else if (mMainApp.getAppData().getUser().mOrganizationInfo.mState == 2) {

								if (mBlnFromNotifyBox) {
									ENavigate.startMainActivity(LoginActivity.this,MNotifyMessageBox.getInstance(),pushMessageType, friendId);
								} else if (mFromShare) {
									ENavigate.startMainActivityEx(LoginActivity.this, mShareInfo,friendId);
								} else {
									verifyUserStatus(mMainApp.getAppData().getUser().getUserStatus(),userNameEt.getText().toString());
								}
							}

						} else {
							// 个人用户直接登录
							if (mBlnFromNotifyBox) {
								ENavigate.startMainActivity(LoginActivity.this,MNotifyMessageBox.getInstance(),pushMessageType, friendId);
							} else if (mFromShare) {
								ENavigate.startMainActivityEx(LoginActivity.this, mShareInfo,friendId);
							} else {
								verifyUserStatus(mMainApp.getAppData()
										.getUser().getUserStatus(), userNameEt
										.getText().toString());
							}
						}
					}

				}

			} else if (tag == EAPIConsts.ReqType.LOGIN_CONFIGURATION) {
				MSG = " tag == EAPIConsts.ReqType.LOGIN_CONFIGURATION  ";
				Log.i(TAG, MSG);
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					// 以下字段均保存到数据库
					// 保存SessionID
					mMainApp.getAppData().setSessionID(dataBox.mSessionID);
					// 保存货币范围
					if (dataBox.mListMoneyRange != null) {
						mMainApp.getAppData().setListMoneyRange(
								dataBox.mListMoneyRange);
					}
					// 保存货币类型
					if (dataBox.mListMoneyType != null) {
						mMainApp.getAppData().setListMoneyType(
								dataBox.mListMoneyType);
					}
					// 保存融资类型
					if (dataBox.mListInInvestType != null) {
						mMainApp.getAppData().setListInInvestType(
								dataBox.mListInInvestType);
					}
					// 保存投资类型
					if (dataBox.mListOutInvestType != null) {
						mMainApp.getAppData().setListOutInvestType(
								dataBox.mListOutInvestType);
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
						mMainApp.getAppData().setUser(dataBox.mJTMember);
						mMainApp.getAppData().setUserName(
								userNameEt.getText().toString());
						mMainApp.getAppData().setPassword(
								passwordEt.getText().toString());
						mMainApp.getAppData().setmBrowseHomepageType(
								dataBox.mJTMember.getHomePageVisible());
						mMainApp.getAppData().setmFriendsAppraiseType(
								dataBox.mJTMember.getEvaluateVisible());
						if (dataBox.mJTMember.getListIndustry() != null) {
							mMainApp.getAppData()
									.getmIndustrys()
									.setListIndustry(
											dataBox.mJTMember.getListIndustry());
						}
						PushManager.startWork(getApplicationContext(),
								PushConstants.LOGIN_TYPE_API_KEY, EUtil
										.getMetaValue(LoginActivity.this,
												"api_key"));

						MSG = " mMainApp.getAppData().getUser().mUserType = "
								+ mMainApp.getAppData().getUser().mUserType;
						Log.i(TAG, MSG);

						if (mMainApp.getAppData().getUser().mUserType == JTMember.UT_ORGANIZATION) {
							// 机构用户当前状态
							if (mMainApp.getAppData().getUser().mOrganizationInfo.mState == -1) {
								// 未进行第一次认证（跳转到认证页面）
								startActivity(new Intent(
										LoginActivity.this,
										RegisterOrganizationContactActivity.class));
							}
							// else if
							// (mMainApp.getAppData().getUser().mOrganizationInfo.mState
							// == 0 ||
							// mMainApp.getAppData().getUser().mOrganizationInfo.mState
							// == 1) {
							else if (mMainApp.getAppData().getUser().mOrganizationInfo.mState == 0) {
								// 第一次认证进行中
								// startActivity(new Intent(LoginActivity.this,
								// AuditResultActivity.class));
							} else if (mMainApp.getAppData().getUser().mOrganizationInfo.mState == 1) {
								// 第一次认证失败(跳转到创建组织)
								MSG = "mMainApp.getAppData().getUser().mOrganizationInfo.mState == 1";
								Log.i(TAG, MSG);
								// Intent intent = new
								// Intent(LoginActivity.this,
								// Create_organization_Activity.class);
								// startActivity(intent);
								// startActivity(new Intent(LoginActivity.this,
								// AuditResultActivity.class));
								OrganizationReqUtil
										.doGetRegistOrgDetail(
												LoginActivity.this,
												mIBindData,
												mMainApp.getAppData().getUser()
														.getmOrganizationInfo().mLegalPersonIDCardImage,
												null);
							} else if (mMainApp.getAppData().getUser().mOrganizationInfo.mState == 2) {
								// 第一次认证成功
								if (mBlnFromNotifyBox) {
									ENavigate.startMainActivity(
											LoginActivity.this,
											MNotifyMessageBox.getInstance(),
											pushMessageType, friendId);
								} else if (mFromShare) {
									ENavigate.startMainActivityEx(
											LoginActivity.this, mShareInfo,
											friendId);
								} else {
									ENavigate.startMainActivity(
											LoginActivity.this, null, friendId);
								}
								finish();
							}
						} else {
							// 个人用户直接登录
							if (mBlnFromNotifyBox) {
								ENavigate.startMainActivity(LoginActivity.this,
										MNotifyMessageBox.getInstance(),
										pushMessageType, friendId);
							} else if (mFromShare) {
								ENavigate.startMainActivityEx(
										LoginActivity.this, mShareInfo,
										friendId);
							} else {
								verifyUserStatus(mMainApp.getAppData()
										.getUser().getUserStatus(), mMainApp
										.getAppData().getUserName());
								// ENavigate.startMainActivity(LoginActivity.this,
								// null, null);
							}
						}
					}
				}
			}
			if (tag == EAPIConsts.ReqType.THIRD_LOGIN) {
				MSG = " tag == EAPIConsts.ReqType.THIRD_LOGIN  ";
				Log.i(TAG, MSG);
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					// 以下字段均保存到数据库
					// 保存SessionID
					mMainApp.getAppData().setSessionID(dataBox.mSessionID);
					// 保存货币范围
					if (dataBox.mListMoneyRange != null) {
						mMainApp.getAppData().setListMoneyRange(
								dataBox.mListMoneyRange);
					}
					// 保存货币类型
					if (dataBox.mListMoneyType != null) {
						mMainApp.getAppData().setListMoneyType(
								dataBox.mListMoneyType);
					}
					// 保存融资类型
					if (dataBox.mListInInvestType != null) {
						mMainApp.getAppData().setListInInvestType(
								dataBox.mListInInvestType);
					}
					// 保存投资类型
					if (dataBox.mListOutInvestType != null) {
						mMainApp.getAppData().setListOutInvestType(
								dataBox.mListOutInvestType);
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
						mMainApp.getAppData().setUser(dataBox.mJTMember);
						mMainApp.getAppData().setUserName(
								userNameEt.getText().toString());
						mMainApp.getAppData().setPassword(
								passwordEt.getText().toString());
						mMainApp.getAppData().setmBrowseHomepageType(
								dataBox.mJTMember.getHomePageVisible());
						mMainApp.getAppData().setmFriendsAppraiseType(
								dataBox.mJTMember.getEvaluateVisible());
						if (dataBox.mJTMember.getListIndustry() != null) {
							mMainApp.getAppData()
									.getmIndustrys()
									.setListIndustry(
											dataBox.mJTMember.getListIndustry());
						}
						PushManager.startWork(getApplicationContext(),
								PushConstants.LOGIN_TYPE_API_KEY, EUtil
										.getMetaValue(LoginActivity.this,
												"api_key"));

						MSG = " mMainApp.getAppData().getUser().mUserType = "
								+ mMainApp.getAppData().getUser().mUserType;
						Log.i(TAG, MSG);

					}
					if(dataBox.mBindingStatus){
						// 已绑定或完善资料跳转到main页面
//						ENavigate.startMainActivity(LoginActivity.this);
						verifyUserStatus(mMainApp.getAppData().getUser().getUserStatus(), null);
					}else{
						// TODO 没有绑定已有账号或未完善资料跳转到完善资料页面
						ENavigate.startCompleteuserInfoActivity(LoginActivity.this);
						dismissLoadingDialog();
					}
					if(mLoginType==100){
						ShareSDK.getPlatform(QQ.NAME).removeAccount(true);
					}else{
						ShareSDK.getPlatform(SinaWeibo.NAME).removeAccount(true);
					}
				}else{//避免登录--新浪/qq--点击图标时，如果数据为空，提示服务器连接失败，请重试，清除授权信息。
					if(mLoginType==100){
						ShareSDK.getPlatform(QQ.NAME).removeAccount(true);
					}else{
						ShareSDK.getPlatform(SinaWeibo.NAME).removeAccount(true);
					}
					dismissLoadingDialog();
				}
			}
			dismissLoadingDialog();
		}
	};

	private ImageView delUsername;

	private ImageView delPassword;

	private String friendId;

	@Override
	public void initJabActionBar() {
		jabGetActionBar().show();
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "登录", false, null, false,false);

	}

	// 用户状态 0:邮箱未验证 1:信息未完善 2已完善个人信息
	private void verifyUserStatus(final int userStatus, String account) {
		// 登陆环信IM服务器 如果登陆成功 再请求自己的服务器
		EMChatManager.getInstance().login(mMainApp.getAppData().getUserID(),
				mMainApp.getAppData().getUserID(), new EMCallBack() {// 回调
					@Override
					public void onSuccess() {

						// 注册一个监听连接状态的listener
						EMChatManager.getInstance().addConnectionListener(
								new com.tr.imservice.MyConnectionListener(LoginActivity.this));

						runOnUiThread(new Runnable() {
							public void run() {
								EMGroupManager.getInstance().loadAllGroups();
								EMChatManager.getInstance().loadAllConversations();
								Log.d("main", "登陆聊天服务器成功！");
								switch (userStatus) {
								case 0:
									Toast.makeText(LoginActivity.this,"请先验证邮箱", 0).show();
									dismissLoadingDialog();
									break;
								case 1:
									ENavigate.startRegisterPersonalDetailActivity(LoginActivity.this);
									dismissLoadingDialog();
									break;
								case 2:
									ENavigate.startMainActivity(LoginActivity.this, null, friendId);
									dismissLoadingDialog();
									break;
								default:
									ENavigate.startMainActivity(LoginActivity.this, null, friendId);
									dismissLoadingDialog();
								}
							}
						});
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						Log.d("main", "登陆聊天服务器失败！");
//						handler.sendEmptyMessage(0);
						
						runOnUiThread(new Runnable() {
							public void run() {
								switch (userStatus) {
								case 0:
									Toast.makeText(LoginActivity.this,"请先验证邮箱", 0).show();
									dismissLoadingDialog();
									break;
								case 1:
									ENavigate.startRegisterPersonalDetailActivity(LoginActivity.this);
									dismissLoadingDialog();
									break;
								case 2:
									ENavigate.startIMFaileMainActivity(LoginActivity.this, null, friendId,false);
									dismissLoadingDialog();
									break;
								default:
									ENavigate.startIMFaileMainActivity(LoginActivity.this, null, friendId,false);
									dismissLoadingDialog();
								}
							}
						});
						
					}
				});
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(LoginActivity.this, "登录失败", 0).show();
				break;
			case MSG_AUTH_COMPLETE:
				// 显示加载框
				showLoadingDialog();
				// 已授权，验证是否已绑定已有账号或已完善完资料
				UserReqUtil.doThird_Login(LoginActivity.this, mIBindData, "",
						"", EUtil.getDeviceID(LoginActivity.this),
						EUtil.getAppVersionName(LoginActivity.this), "", "",
						"", "android", "", "", token, userId,
						String.valueOf(mLoginType), null);
//				ENavigate.startCompleteuserInfoActivity(LoginActivity.this);
				break;
			case MSG_AUTH_CANCEL:
//				Util.toast(LoginActivity.this, "取消授权", false);
				break;
			case MSG_AUTH_ERROR:
				Util.toast(LoginActivity.this, "授权失败", false);
				break;
			}
		};
	};
}
