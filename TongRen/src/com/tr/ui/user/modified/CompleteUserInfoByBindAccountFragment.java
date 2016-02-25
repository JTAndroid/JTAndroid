package com.tr.ui.user.modified;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.home.MainActivity;
import com.tr.ui.organization.model.RegisteOrgDetail;
import com.tr.ui.user.RegisterOrganizationContactActivity;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * 第三方登陆后完善资料-绑定已有账号功能
 * 
 * @author cui
 * 
 */
public class CompleteUserInfoByBindAccountFragment extends JBaseFragment {
	private ImageView deletePassword;
	private ImageView viewPassword;
	private EditText passwordEt;
	private EditText phoneOrEmail;
	private ImageView deletePhoneEmail;
	private Button complete_bind_account;
	private App mMainApp; // App全局对象
	// 变量
	private String friendId;
	private boolean hasPhoneOrEmail = false;
	private boolean hasPassword = false;
	private static boolean flag = true;
	private boolean mBlnFromNotifyBox;
	private int pushMessageType; // 推送的消息类型
	private boolean mFromShare; // 从分享启动
	private JTFile mShareInfo;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.frag_complete_userinfo_by_bind_account, container,
				false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainApp = App.getApp();
		friendId = getActivity().getIntent().getStringExtra(ENavConsts.EFriendId);
		// 处理消息
				boolean startFromNotify = getNotifyParam();
				if (startFromNotify) {
					if (checkTopActivity(getActivity())) {
						return;
					}
				}
				// 处理分享
				boolean startFromShare = getShareParam();
				if (startFromShare) {
					if (checkTopActivityEx(getActivity())) {
						return;
					}
				}
	}

	@Override
	public void onViewCreated(View container, Bundle savedInstanceState) {
		initControls(container);
	}
	/** 获取推送参数 */
	public boolean getNotifyParam() {
		mBlnFromNotifyBox = getActivity().getIntent().hasExtra(ENavConsts.ENotifyParam);
		if (mBlnFromNotifyBox) {
			pushMessageType = getActivity().getIntent().getIntExtra(
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
		mShareInfo = (JTFile) getActivity().getIntent().getSerializableExtra(
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
					.getApplicationConxt().getSystemService(getActivity().ACTIVITY_SERVICE);
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
					ENavigate.startMainActivity(getActivity(),
							MNotifyMessageBox.getInstance(), pushMessageType,
							friendId);
					getActivity().finish();
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
					.getApplicationConxt().getSystemService(getActivity().ACTIVITY_SERVICE);
			List<RunningTaskInfo> runningTaskInfos = manager
					.getRunningTasks(500);
			RunningTaskInfo myTask = null;

			for (int i = 0; i < runningTaskInfos.size(); i++) {
				RunningTaskInfo nowTask = runningTaskInfos.get(i);
				// 应用程序位于堆栈的顶层
				if (getActivity().getPackageName().equals(
						nowTask.topActivity.getPackageName())) {
					myTask = nowTask;
					break;
				}
			}
			if (myTask != null) {
				String baseName = myTask.baseActivity.getClassName();
				String mainName = MainActivity.class.getName();
				if (baseName.equalsIgnoreCase(mainName)) {
					ENavigate.startMainActivityEx(getActivity(),
							JTFile.createFromIntent(getActivity().getIntent()), friendId);
					getActivity().finish();
					return true;
				}
			}
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		return false;
	}
	// 初始化控件
	private void initControls(View container) {
		phoneOrEmail = (EditText) container.findViewById(R.id.phone_or_email);
		deletePhoneEmail = (ImageView) container
				.findViewById(R.id.delete_phone_email);
		deletePhoneEmail.setOnClickListener(mClickListener);
		phoneOrEmail.addTextChangedListener(new TextWatcher() {

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
					hasPhoneOrEmail = true;
					deletePhoneEmail.setVisibility(View.VISIBLE);
				} else {
					hasPhoneOrEmail = false;
					deletePhoneEmail.setVisibility(View.GONE);
				}
				updateCompleteBindAccount();

			}
		});
		passwordEt = (EditText) container.findViewById(R.id.passwordEt);
		passwordEt.setHint("登录密码");
		deletePassword = (ImageView) container
				.findViewById(R.id.delete_password);
		viewPassword = (ImageView) container.findViewById(R.id.view_password);
		TextView gintong_aggreement = (TextView) container
				.findViewById(R.id.gintong_aggreement);
		gintong_aggreement.setOnClickListener(mClickListener);
		complete_bind_account = (Button) container
				.findViewById(R.id.complete_bind_account);
		complete_bind_account.setOnClickListener(mClickListener);
		TextView registerNewAccount = (TextView) container
				.findViewById(R.id.register_new_account);
		registerNewAccount.setOnClickListener(mClickListener);
		// 对图标进行初始化操作
		passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		viewPassword.setBackgroundResource(R.drawable.show_password_bg_normal);
		// 根据点击改变图标状态
		viewPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flag) {
					// 以点点的形式显示密码
					passwordEt.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					passwordEt.setSelection(passwordEt.getText().toString().length());
					viewPassword
							.setBackgroundResource(R.drawable.show_password_bg_pressed);
					flag = false;

				} else {
					// 不以以点点的形式显示密码
					passwordEt
							.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					passwordEt.setSelection(passwordEt.getText().toString().length());
					viewPassword
							.setBackgroundResource(R.drawable.show_password_bg_normal);
					flag = true;
				}
			}
		});

		deletePassword.setOnClickListener(mClickListener);
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
				if (!StringUtils.isEmpty(s.toString()) && s.length() >= 6
						&& s.length() <= 19) {
					deletePassword.setVisibility(View.VISIBLE);
					hasPassword = true;
				} else {
					deletePassword.setVisibility(View.INVISIBLE);
					hasPassword = false;
					if (s.length() > 19) {
						Toast.makeText(getActivity(), "请输入6-19位密码", 0).show();
						deletePassword.setVisibility(View.VISIBLE);
					}
				}
				updateCompleteBindAccount();

			}

		});
		complete_bind_account.setClickable(false);
	}

	private void updateCompleteBindAccount() {
		if (hasPhoneOrEmail && hasPassword) {
			complete_bind_account.setClickable(true);
			complete_bind_account.setBackgroundResource(R.drawable.sign_in);
		} else {
			complete_bind_account.setClickable(false);
			complete_bind_account
					.setBackgroundResource(R.drawable.sign_in_normal);
		}
	}

	// 点击事件监听器
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.complete_bind_account:
				onComplete();//完成
				break;
			case R.id.register_new_account:
				ENavigate.startRegisterGintongAccountActivity(getActivity());
				// getActivity().finish();
				break;
			case R.id.gintong_aggreement:
				// 金桐用户协议
				ENavigate.startAgreementActivity(getActivity());
				break;
			case R.id.delete_phone_email:
				phoneOrEmail.setText("");
				break;
			case R.id.delete_password:
				passwordEt.setText("");
				break;
			default:
				break;
			}

		}

	};

	/**
	 * 完成按钮要处理的
	 */
	private void onComplete() {
		// 检查用户名密码是否合法
		if (EUtil.isUsernameFormatCorrect(getActivity(), phoneOrEmail.getText()
				.toString())
				&& EUtil.isPasswordFormatCorrect(getActivity(), passwordEt
						.getText().toString())) {
			// 显示加载框
			showLoadingDialog();
			// TODO 绑定完成todo 需添加一个参数Token与后台交互
			SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(getActivity());
					String mToken=sp.getString("mToken", "");
					int mLoginType=sp.getInt("mLoginType", 0);
			UserReqUtil.doLoginConfiguration(getActivity(), mIBindData,
						UserReqUtil.getDoLoginConfigurationParams("", "", EUtil
								.getDeviceID(getActivity()), EUtil
								.getAppVersionName(getActivity()), "", "", "",
								"android", "", "", phoneOrEmail.getText()
										.toString(), passwordEt.getText()
										.toString(),mToken,"",mLoginType), null);
		}
	}
	// 接口回调函数对象
		private IBindData mIBindData = new IBindData() {

			@Override
			public void bindData(int tag, Object object) {
				String MSG = "bindData()";
				// 隐藏加载框
//				dismissLoadingDialog();
				// 初始化数据
			 if (tag == EAPIConsts.ReqType.LOGIN_CONFIGURATION) {
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
									phoneOrEmail.getText().toString());
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
							PushManager.startWork(getActivity(),
									PushConstants.LOGIN_TYPE_API_KEY, EUtil
											.getMetaValue(getActivity(),
													"api_key"));

							MSG = " mMainApp.getAppData().getUser().mUserType = "
									+ mMainApp.getAppData().getUser().mUserType;
							Log.i(TAG, MSG);
//							ENavigate.startMainActivity(getActivity(), null, friendId);
//							getActivity().finish();
							if (mMainApp.getAppData().getUser().mUserType == JTMember.UT_ORGANIZATION) {
								verifyUserStatus(-1);
							}else{
								verifyUserStatus(mMainApp.getAppData().getUser().userStatus);
							}
						
						}
					}
				}
			 dismissLoadingDialog();
			}
		};
		
		// userStatus 用户状态 0:邮箱未验证 1:信息未完善 2已完善个人信息  
		private void verifyUserStatus(final int userStatus) {
			// 登陆环信IM服务器 如果登陆成功 再请求自己的服务器
			EMChatManager.getInstance().login(mMainApp.getAppData().getUserID(),
					mMainApp.getAppData().getUserID(), new EMCallBack() {// 回调
						@Override
						public void onSuccess() {

							// 注册一个监听连接状态的listener
							EMChatManager.getInstance().addConnectionListener(
									new com.tr.imservice.MyConnectionListener(getActivity()));

							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									EMGroupManager.getInstance().loadAllGroups();
									EMChatManager.getInstance()
											.loadAllConversations();
									switch (userStatus) {
									case 0:
										Toast.makeText(getActivity(), "请先验证邮箱", 0).show();
										break;
									case 1:
										ENavigate.startRegisterPersonalDetailActivity(getActivity());
										getActivity().finish();
										break;
									case 2:
										ENavigate.startMainActivity(getActivity(), null, friendId);
										getActivity().finish();
										dismissLoadingDialog();
										break;
									default:
										//组织用户 提示"组织账号不支持使用第三方登录，\r 无法绑定到第三方"
										showToast("组织账号不支持使用第三方登录，\r 无法绑定到第三方");
										dismissLoadingDialog();
										break;
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
							showToast("登录失败");
						}
					});
		}


}
