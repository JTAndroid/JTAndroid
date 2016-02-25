package com.tr.ui.user.modified;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.model.home.MListCountry;
import com.tr.model.home.McountryCode;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * 第三方登陆后完善资料-即是通过手机号注册一次
 * 
 * @author cui
 * 
 */
public class CompleteUserInfoByPhoneFragment extends JBaseFragment {
	private final String TAG = getClass().getSimpleName();
	private JBaseFragmentActivity mParentActivity; // 父Activity
	private App mMainApp; // App全局对象

	private boolean hasPhoneCall = false;
	private boolean hasVerifyCode = false;
	private boolean hasPassword = false;
	private boolean hasNickName = true;// 默认第三方昵称
	// 文字提示
	private final String TIP_REGET_VERIFY_CODE = "重获验证码";
	private final String TIP_GET_VERIFY_CODE = "获取验证码";
	private final String TIP_EMPTY_VCODE = "请输入验证码";
	private final String TIP_ILLEGAL_ACCOUNT = "请输入正确的手机号码或邮箱";
	private final String TIP_GET_VERIFY_CODE_SUCCESS = "验证码已发送到您的手机，请注意查收";

	private String mToken;
	private int mLoginType;
	private String mNickName;
	// 常量
	private final int EXPIRED_TIME = 60; // 手机验证码超时时间,60s
	private final int COUNTDOWN_INTERVAL = 1000; // 倒计时时间间隔
	// 消息类型
	private final int MSG_BASE = 100;
	private final int MSG_COUNT_DOWN = MSG_BASE + 1; // 倒计时的消息标识
	// 控件相关
	private EditText nickname;// 昵称 默认是第三方的 可以修改
	private EditText vcodeEt; // 输入验证码
	private TextView countSeconds; // 获取验证码
	private TextView vcodeTv; // 获取验证码
	// 变脸相关
	private int mCountdownLeft; // 倒计时剩余时间
	private Timer mTimer; // 计时任务
	private String mVerifyCode; // 验证码
	// 控件
	private TextView region_number;
	private EditText phone_call;
	private TextView gintong_aggreement;
	private ImageView delete_phone_call;
	private Button register_gintong_account;
	private EditText passwordEt;
	private ImageView viewPassword;
	private ImageView deletePassword;
	// 变量
	private static boolean flag = true;
	MListCountry mListCountry;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_complete_userinfo_by_phone,
				container, false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initVars();
		mMainApp = App.getApp();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mParentActivity = (JBaseFragmentActivity) activity;
	};

	@Override
	public void onViewCreated(View container, Bundle savedInstanceState) {
		initControls(container);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		// 这段可以解决fragment嵌套fragment崩溃的问题
		// 参数是固定的
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2056) {
			McountryCode mcountryCode = (McountryCode) data.getExtras().get(
					ENavConsts.ECountry);
			region_number.setText("+" + mcountryCode.getCode());
		}
	}

	// 初始化变量
	private void initVars() {
		mTimer = new Timer();
		mCountdownLeft = EXPIRED_TIME; // 60s倒计时时间
		mVerifyCode = ""; // 验证码
	}

	// 初始化控件
	private void initControls(View container) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		mToken = sp.getString("mToken", "");
		mLoginType = sp.getInt("mLoginType", 0);
		mNickName = sp.getString("mNickName", "");
		region_number = (TextView) container.findViewById(R.id.region_number);
		region_number.setOnClickListener(mClickListener);
		phone_call = (EditText) container.findViewById(R.id.phone_call);
		phone_call.addTextChangedListener(phoneCallTextWatcher);
		nickname = (EditText) container.findViewById(R.id.nickname);
		nickname.setText(mNickName);
		nickname.addTextChangedListener(new TextWatcher() {

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
				if (!StringUtils.isEmpty(s.toString()) && s.length() <= 25) {
					hasNickName = true;
				} else {
					hasNickName = false;
					if (s.length() > 25) {
						showToast("昵称过长");
					}
				}
				updateRegisterGintongAccount();
			}
		});
		gintong_aggreement = (TextView) container
				.findViewById(R.id.gintong_aggreement);
		gintong_aggreement.setOnClickListener(mClickListener);
		delete_phone_call = (ImageView) container
				.findViewById(R.id.delete_phone_call);
		delete_phone_call.setOnClickListener(mClickListener);
		register_gintong_account = (Button) container
				.findViewById(R.id.complete_by_phone);
		register_gintong_account.setOnClickListener(mClickListener);
		countSeconds = (TextView) container.findViewById(R.id.count_backwards);
		// 输入验证码
		vcodeEt = (EditText) container.findViewById(R.id.vcodeEt);
		vcodeEt.addTextChangedListener(new TextWatcher() {

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
					hasVerifyCode = true;
				} else {
					hasVerifyCode = false;
				}
				updateRegisterGintongAccount();
			}
		});
		// 获取验证码
		vcodeTv = (TextView) container.findViewById(R.id.vcodeTv);
		vcodeTv.setText(TIP_GET_VERIFY_CODE);
		vcodeTv.setOnClickListener(mClickListener);
		passwordEt = (EditText) container.findViewById(R.id.passwordEt);
		// 删除密码
		deletePassword = (ImageView) container
				.findViewById(R.id.delete_password);
		// 确认密码
		viewPassword = (ImageView) container.findViewById(R.id.view_password);

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

		deletePassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				passwordEt.setText("");
			}
		});

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
						showToast("请输入6-19位密码");
						deletePassword.setVisibility(View.VISIBLE);
					}
				}
				updateRegisterGintongAccount();

			}

		});

		register_gintong_account.setClickable(false);
	}

	private void updateRegisterGintongAccount() {
		if (hasPhoneCall && hasVerifyCode && hasNickName && hasPassword) {
			register_gintong_account.setClickable(true);
			register_gintong_account.setBackgroundResource(R.drawable.sign_in);
		} else {
			register_gintong_account.setClickable(false);
			register_gintong_account
					.setBackgroundResource(R.drawable.sign_in_normal);
		}
	}

	private TextWatcher phoneCallTextWatcher = new TextWatcher() {
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
				delete_phone_call.setVisibility(View.VISIBLE);
				vcodeTv.setClickable(true);
				hasPhoneCall = true;
			} else {
				delete_phone_call.setVisibility(View.GONE);
				vcodeTv.setClickable(false);
				hasPhoneCall = false;
			}
			if (hasPhoneCall && hasVerifyCode && hasPassword) {
				register_gintong_account.setClickable(true);
				register_gintong_account
						.setBackgroundResource(R.drawable.sign_in);
			} else {
				register_gintong_account.setClickable(false);
				register_gintong_account
						.setBackgroundResource(R.drawable.sign_in_normal);
			}
		}
	};
	// 点击事件监听器
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == vcodeTv) { // 开始倒计时
				// 获取验证码
				if (vcodeTv.getText().equals(TIP_GET_VERIFY_CODE)
						| vcodeTv.getText().equals(TIP_REGET_VERIFY_CODE)) {
					// 验证手机号是否正确
					if (EUtil.isMobileNO(region_number.getText().toString()
							.trim(), phone_call.getText().toString())) {
						// 发送获取验证码请求
						mParentActivity.showLoadingDialog();
						UserReqUtil.doGetVerifyCode(getActivity(), mBindData,
								UserReqUtil.getDoGetVerifyCodeParams(0,
										region_number.getText().toString()
												.trim(),//
										phone_call.getText().toString()), null);

					} else {
						if (StringUtils
								.isEmpty(phone_call.getText().toString())) {
							// 什么也不做
						} else {
							Toast.makeText(getActivity(), "您填写的号码格式不正确", 0)
									.show();
						}
					}
				}
				// else if (vcodeTv.getText().equals(TIP_REGET_VERIFY_CODE)) {
				// // 重获验证码
				//
				// // 不再需要提示，直接显示加载框
				// mParentActivity.showLoadingDialog();
				// // 发送获取验证码请求
				// UserReqUtil.doGetVerifyCode(getActivity(), mBindData,
				// UserReqUtil.getDoGetVerifyCodeParams(0,
				// region_number.getText().toString().trim(),
				// phone_call.getText().toString()), null);
				// }
			}
			if (v == region_number) {
				// afToast.makeText(getActivity(), "区号", 0).show();
				ENavigate.startCountryCodeActivity(getActivity(), mListCountry,
						2056);
			}
			if (v == delete_phone_call) {
				phone_call.setText("");
			}
			if (v == register_gintong_account) {
				nextStep();
			}
			if (v == gintong_aggreement) {
				// Toast.makeText(getActivity(), "金桐用户协议", 0).show();
				ENavigate.startAgreementActivity(getActivity());
			}

		}
	};

	// 检查信息的完整性（是否显示下一步按钮）
	private boolean infoIntegrityCheck() {

		if (EUtil.isMobileNO(region_number.getText().toString().trim(),
				phone_call.getText().toString())) { // 手机号和验证码----//后端决定此处由后台验证
			// 验证码的正确性
			if (vcodeEt.getText().toString().length() <= 0) {
				showToast(TIP_EMPTY_VCODE);
				return false;
			}
			// if (!mVerifyCode.equals(vcodeEt.getText().toString())) {
			// showToast(TIP_WRONG_VERIFY_CODE);
			// return false;
			// }
		} else if (EUtil.isEmail(phone_call.getText().toString())) {
			return true;
		} else {
			showToast(TIP_ILLEGAL_ACCOUNT);
			return false;
		}
		return true;
	}

	/** 下一步 */
	private void nextStep() {
		if (!infoIntegrityCheck()) {
			return;
		}
		if (EUtil.isMobileNO(region_number.getText().toString().trim(),
				phone_call.getText().toString())) { // 手机注册

			// 显示加载框
			mParentActivity.showLoadingDialog();
			UserReqUtil.doRegister(mParentActivity, mBindData, UserReqUtil
						.getDoRegisterParams(phone_call.getText().toString(),
								"", passwordEt.getText().toString(), vcodeEt
										.getText().toString(), 1, region_number
										.getText().toString().substring(1),
								mToken, nickname.getText().toString(),
								mLoginType), null);
		}

		else {
			Toast.makeText(getActivity(), "请输入正确的手机号", 0).show();
		}
	}

	// 接口回调函数
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int type, Object object) {
//			if (mParentActivity.isLoadingDialogShowing()) {
//				mParentActivity.dismissLoadingDialog();
//			}
			if (type == EAPIConsts.ReqType.GET_VERIFY_CODE) { // 获取验证码
				// 是否获取成功
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					mVerifyCode = dataBox.mVerifyCode;
					if (dataBox.mIsSuccess) { // 获取验证码成功，开始倒计时
						// 重置倒计时Timer
						if (mTimer != null) {
							mTimer.cancel();
							mTimer = null;
						}
						mTimer = new Timer();
						mTimer.schedule(new TimerTask() {

							@Override
							public void run() {
								mCountdownLeft--;
								mHandler.sendEmptyMessage(MSG_COUNT_DOWN);
							}
						}, 0, COUNTDOWN_INTERVAL);
						// 设置倒计时时间
						mCountdownLeft = EXPIRED_TIME;
						// 设置验证按钮状态
						vcodeTv.setEnabled(false);
						// 显示消息
						showToast(TIP_GET_VERIFY_CODE_SUCCESS);
					}
				}
				mParentActivity.dismissLoadingDialog();
			}
			if (type == EAPIConsts.ReqType.REGISTER) { // 注册成功
				// 是否进行下一步操作
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					mMainApp.getAppData().setUser(dataBox.mJTMember);
					mMainApp.getAppData().setUserName(
							phone_call.getText().toString());
					mMainApp.getAppData().setPassword(
							passwordEt.getText().toString());
					// 注册成功后 ，需要doLoginConfiguration下把账号与密码传给后台实现自动登录
					UserReqUtil.doLoginConfiguration(mParentActivity,
							mBindData,
							UserReqUtil.getDoLoginConfigurationParams("", "",
									EUtil.getDeviceID(mParentActivity),
									EUtil.getAppVersionName(mParentActivity),
									"", "", "", "android", "", "", phone_call.getText().toString().trim(), passwordEt.getText().toString().trim()),
							null);
				}
			} else if (type == EAPIConsts.ReqType.LOGIN_CONFIGURATION) {
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					// 以下字段均保存到数据库
					// SessionID
					mMainApp.getAppData().setSessionID(dataBox.mSessionID);
					// 货币范围
					if (dataBox.mListMoneyRange != null) {
						mMainApp.getAppData().setListMoneyRange(
								dataBox.mListMoneyRange);
					}
					// 货币类型
					if (dataBox.mListMoneyType != null) {
						mMainApp.getAppData().setListMoneyType(
								dataBox.mListMoneyType);
					}
					// 融资类型
					if (dataBox.mListInInvestType != null) {
						mMainApp.getAppData().setListInInvestType(
								dataBox.mListInInvestType);
					}
					// 保存投资类型
					if (dataBox.mListOutInvestType != null) {
						mMainApp.getAppData().setListOutInvestType(
								dataBox.mListOutInvestType);
					}
					// 投资区域
					if (dataBox.mListTrade != null) {
						mMainApp.getAppData().setListTrade(dataBox.mListTrade);
					}
					// 投资区域
					if (dataBox.mListArea != null) {
						mMainApp.getAppData().setListArea(dataBox.mListArea);
					}
					// 邀请语
					mMainApp.getAppData().mInviteJoinGinTongInfo = dataBox.mInviteJoinGinTongInfo;
						// 保存用户对象
						if (dataBox.mJTMember != null) {
							mMainApp.getAppData().setUser(dataBox.mJTMember);
							mMainApp.getAppData().setUserName(
									phone_call.getText().toString());
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
												dataBox.mJTMember
														.getListIndustry());
							}
							PushManager.startWork(getActivity()
									.getApplicationContext(),
									PushConstants.LOGIN_TYPE_API_KEY, EUtil
											.getMetaValue(getActivity(),
													"api_key"));

							// 个人用户直接登录
							verifyUserStatus(mMainApp.getAppData().getUser()
									.getUserStatus(), mMainApp.getAppData()
									.getUserName());
							// ENavigate.startMainActivity(LoginActivity.this,
							// null, null);
						}
				}
				mParentActivity.dismissLoadingDialog();
			}
		}
	};

	// 用户状态 0:邮箱未验证 1:信息未完善 2已完善个人信息
	private void verifyUserStatus(final int userStatus, String account) {
		// 登陆环信IM服务器 如果登陆成功 再请求自己的服务器
		EMChatManager.getInstance().login(mMainApp.getAppData().getUserID(),
				mMainApp.getAppData().getUserID(), new EMCallBack() {// 回调
					@Override
					public void onSuccess() {

						mParentActivity.dismissLoadingDialog();
						// 注册一个监听连接状态的listener
						EMChatManager.getInstance().addConnectionListener(
								new com.tr.imservice.MyConnectionListener(getActivity()));

						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								EMGroupManager.getInstance().loadAllGroups();
								EMChatManager.getInstance().loadAllConversations();
								Log.d("CompleteUserInfoByPhone", "登陆聊天服务器成功！");
								switch (userStatus) {
								case 0:
									Toast.makeText(getActivity(), "请先验证邮箱", 0)
											.show();
									break;
								case 1:
									ENavigate
											.startRegisterPersonalDetailActivity(getActivity());
									getActivity().finish();
									mParentActivity.dismissLoadingDialog();
									break;
								case 2:
									ENavigate.startMainActivity(getActivity(),
											null, null);
									getActivity().finish();
									mParentActivity.dismissLoadingDialog();
									break;
								default:
									ENavigate.startMainActivity(getActivity(),
											null, null);
									getActivity().finish();
									mParentActivity.dismissLoadingDialog();
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
						mParentActivity.dismissLoadingDialog();
					}
				});
	}

	// 消息处理器
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_COUNT_DOWN: // 倒计时消息
				if (mCountdownLeft <= 0) { // 倒计时结束
					// 取消倒计时任务
					if (mTimer != null) {
						mTimer.cancel();
						mTimer = null;
					}
					// 更改态验证码按钮状态和文字
					vcodeTv.setText(TIP_REGET_VERIFY_CODE);
					countSeconds.setVisibility(View.GONE);
					vcodeTv.setEnabled(true);
				} else { // 倒计时仍在进行
					countSeconds.setVisibility(View.VISIBLE);
					vcodeTv.setVisibility(View.VISIBLE);
					countSeconds.setText("" + mCountdownLeft);
					vcodeTv.setText("秒后可重发");
				}
				break;
			}
		}
	};

}
