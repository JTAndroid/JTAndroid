package com.tr.ui.user.modified;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.baidu.navisdk.util.common.StringUtils;
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
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

public class RegisterPhoneFragment extends JBaseFragment {

	private boolean hasPhoneCall = false;
	private boolean hasVerifyCode = false;
	private boolean hasPassword = false;

	private LinearLayout register_by_email_ll;
  
	private final String TAG = getClass().getSimpleName();
	private JBaseFragmentActivity mParentActivity; // 父Activity
	private App mMainApp; // App全局对象
	// 常量
	private final int EXPIRED_TIME = 60; // 验证码超时时间,60s
	private final int COUNTDOWN_INTERVAL = 1000; // 倒计时时间间隔

	// 消息类型
	private final int MSG_BASE = 100;
	private final int MSG_COUNT_DOWN = MSG_BASE + 1; // 倒计时的消息标识

	// 文字提示
	private final String TIP_REGET_VERIFY_CODE = "重获验证码";
	private final String TIP_GET_VERIFY_CODE = "获取验证码";
	private final String TIP_WRONG_VERIFY_CODE = "验证码错误";
	// private final String TIP_LOADING_SEND_VERIFY_EMAIL = "正在请求发送验证邮件，请稍等...";
	// private final String TIP_LOADING_GET_VERIFY_CODE = "正在获取验证码，请稍等...";
	private final String TIP_EMPTY_VCODE = "请输入验证码";
	private final String TIP_ILLEGAL_ACCOUNT = "请输入正确的手机号码或邮箱";
	private final String TIP_GET_VERIFY_CODE_SUCCESS = "验证码已发送到您的手机，请注意查收";

	// 控件相关
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
	private ImageView view_password;
	// 变量
	private String mMobile; // 用户输入的手机号
	private static boolean flag = true;

	MListCountry mListCountry;

	public RegisterPhoneFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_register_gintong_acount_by_phone, null);
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

	// 初始化变量
	private void initVars() {
		mTimer = new Timer();
		mCountdownLeft = EXPIRED_TIME; // 60s倒计时时间
		mVerifyCode = ""; // 验证码
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
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
			McountryCode mcountryCode = (McountryCode) data.getExtras().get(ENavConsts.ECountry);
			region_number.setText("+" + mcountryCode.getCode());
		}
	}

	// 初始化控件
	private void initControls(View container) {
		view_password = (ImageView) container.findViewById(R.id.view_password);
		region_number = (TextView) container.findViewById(R.id.region_number);
		region_number.setOnClickListener(mClickListener);
		phone_call = (EditText) container.findViewById(R.id.phone_call);
		phone_call.addTextChangedListener(phoneCallTextWatcher);
		
		gintong_aggreement = (TextView) container.findViewById(R.id.gintong_aggreement);
		gintong_aggreement.setOnClickListener(mClickListener);
		delete_phone_call = (ImageView) container.findViewById(R.id.delete_phone_call);
		delete_phone_call.setOnClickListener(mClickListener);
		register_gintong_account = (Button) container.findViewById(R.id.register_gintong_account);
		register_gintong_account.setOnClickListener(mClickListener);
		countSeconds = (TextView) container.findViewById(R.id.count_backwards);
		register_by_email_ll = (LinearLayout) container.findViewById(R.id.register_by_email_ll);
		register_by_email_ll.setOnClickListener(mClickListener);
		// 输入验证码
		vcodeEt = (EditText) container.findViewById(R.id.vcodeEt);
		vcodeEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!StringUtils.isEmpty(s.toString())) {
					hasVerifyCode = true;
				}
				else {
					hasVerifyCode = false;
				}
				if (hasPhoneCall == true && hasVerifyCode == true && hasPassword == true) {
					register_gintong_account.setClickable(true);
					register_gintong_account.setBackgroundResource(R.drawable.sign_in);
				}
				else {
					register_gintong_account.setClickable(false);
					register_gintong_account.setBackgroundResource(R.drawable.sign_in_normal);
				}
			}
		});
		// 获取验证码
		vcodeTv = (TextView) container.findViewById(R.id.vcodeTv);
		vcodeTv.setText(TIP_GET_VERIFY_CODE);
		vcodeTv.setOnClickListener(mClickListener);
		passwordEt = (EditText) container.findViewById(R.id.passwordEt);
		// 删除密码
		deletePassword = (ImageView) container.findViewById(R.id.delete_password);
		// 确认密码
		viewPassword = (ImageView) container.findViewById(R.id.view_password);

		// 对图标进行初始化操作
		passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		view_password.setBackgroundResource(R.drawable.show_password_bg_normal);
		// 根据点击改变图标状态
		viewPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flag) {
					// 以点点的形式显示密码
					passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					view_password.setBackgroundResource(R.drawable.show_password_bg_pressed);
					flag = false;

				}
				else {
					// 不以以点点的形式显示密码
					passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					view_password.setBackgroundResource(R.drawable.show_password_bg_normal);
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
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!StringUtils.isEmpty(s.toString()) && s.length() >= 6 && s.length() <= 19) {
					deletePassword.setVisibility(View.VISIBLE);
					hasPassword = true;
				}
				else {
					deletePassword.setVisibility(View.INVISIBLE);
					hasPassword = false;
					if (s.length() > 19) {
						Toast.makeText(getActivity(), "请输入6-19位密码", 0).show();
						deletePassword.setVisibility(View.VISIBLE);
					}
				}
				if (hasPhoneCall == true && hasVerifyCode == true && hasPassword == true) {
					register_gintong_account.setClickable(true);
					register_gintong_account.setBackgroundResource(R.drawable.sign_in);
				}
				else {
					register_gintong_account.setClickable(false);
					register_gintong_account.setBackgroundResource(R.drawable.sign_in_normal);
				}
			}
		});

		register_gintong_account.setClickable(false);
	}

	private TextWatcher phoneCallTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (!StringUtils.isEmpty(s.toString())) {
				delete_phone_call.setVisibility(View.VISIBLE);
				vcodeTv.setClickable(true);
				hasPhoneCall = true;
			}
			else {
				delete_phone_call.setVisibility(View.GONE);
				vcodeTv.setClickable(false);
				hasPhoneCall = false;
			}
			if (hasPhoneCall == true && hasVerifyCode == true && hasPassword == true) {
				register_gintong_account.setClickable(true);
				register_gintong_account.setBackgroundResource(R.drawable.sign_in);
			}
			else {
				register_gintong_account.setClickable(false);
				register_gintong_account.setBackgroundResource(R.drawable.sign_in_normal);
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
		}
		else if (EUtil.isEmail(phone_call.getText().toString())) {
			return true;
		}
		else {
			 showToast(TIP_ILLEGAL_ACCOUNT);
			return false;
		}
		return true;
	}

	/*
	 * // 检查信息的合法性（只检查邮箱格式，手机号错误不会显示下一步按钮） private boolean infoLegalityCheck(){
	 * if(EUtil.isEmail(phone_call.getText().toString())){ return true; } return
	 * false; }
	 */

	// 点击事件监听器
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == vcodeTv) { // 开始倒计时
				// 获取验证码
				if (vcodeTv.getText().equals(TIP_GET_VERIFY_CODE)) {
					// 验证手机号或邮箱是否正确
					if (EUtil.isMobileNO(region_number.getText().toString().trim(), phone_call.getText().toString())) { // 手机号和验证码----//后端决定此处由后台验证
																				// 验证码的正确性
//					new AlertDialog.Builder(getActivity()).setTitle("确认手机号码").setMessage("我们将发送验证码到这个号码：" + phone_call.getText().toString()).setPositiveButton("确定", mDlgClickListener)
//							.setNegativeButton("取消", null).create().show();
						// 发送获取验证码请求
						mParentActivity.showLoadingDialog();
						UserReqUtil.doGetVerifyCode(getActivity(), mBindData, UserReqUtil.getDoGetVerifyCodeParams(0, region_number.getText().toString().trim(), phone_call.getText().toString()), null);
					}else {
						if (StringUtils.isEmpty(phone_call.getText().toString())) {
							//什么也不做
						} else {
							Toast.makeText(getActivity(), "您填写的号码格式不正确", 0).show();
						}
					}
				}
				else if (vcodeTv.getText().equals(TIP_REGET_VERIFY_CODE)) { // 重获验证码
 
					// 不再需要提示，直接显示加载框
					mParentActivity.showLoadingDialog();
					// 发送获取验证码请求
					UserReqUtil.doGetVerifyCode(getActivity(), mBindData, UserReqUtil.getDoGetVerifyCodeParams(0, region_number.getText().toString().trim(), phone_call.getText().toString()), null);
				}
			}
			if (v == region_number) {
				// afToast.makeText(getActivity(), "区号", 0).show();
				ENavigate.startCountryCodeActivity(getActivity(), mListCountry, 2056);
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
			if (v == register_by_email_ll) {
				// Toast.makeText(getActivity(), "邮箱注册", 0).show();
				RegisterEmailFragment fragment = new RegisterEmailFragment();
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
			}
		}
	};

	/** 下一步 */
	private void nextStep() {
		if (!infoIntegrityCheck()) {
			return;
		}
		System.out.println("phone=" + phone_call.getText().toString() + "-------boolean=" + EUtil.isMobileNO(region_number.getText().toString().trim(),phone_call.getText().toString()));
		if (EUtil.isMobileNO(region_number.getText().toString().trim(), phone_call.getText().toString())) { // 手机注册

			// 显示加载框
			mParentActivity.showLoadingDialog();
			// 是否有SessionID
			if (mMainApp.getAppData().getSessionID().length() <= 0) {
				UserReqUtil.doLoginConfiguration(mParentActivity, mBindData,
						UserReqUtil.getDoLoginConfigurationParams("", "", EUtil.getDeviceID(mParentActivity), EUtil.getAppVersionName(mParentActivity), "", "", "", "android", "", "", "", ""), null);
			}
			else {
				UserReqUtil.doRegister(
						mParentActivity,
						mBindData,
						UserReqUtil.getDoRegisterParams(phone_call.getText().toString(), "", passwordEt.getText().toString(), vcodeEt.getText().toString(), 1, region_number.getText().toString()
								.substring(1)), null);
			}
		}

		else {
			Toast.makeText(getActivity(), "请输入正确的手机号", 0).show();
		}
	}

	// 发送验证码::::modified by zhongshan 迭代需求：去掉此对话框，直接计入验证码倒计时
//	private DialogInterface.OnClickListener mDlgClickListener = new DialogInterface.OnClickListener() {
//
//		@Override
//		public void onClick(DialogInterface dialog, int which) {
//			// TODO Auto-generated method stub
//			// 显示加载框
//			mParentActivity.showLoadingDialog();
//			// 发送获取验证码请求
//			UserReqUtil.doGetVerifyCode(getActivity(), mBindData, UserReqUtil.getDoGetVerifyCodeParams(0, region_number.getText().toString().trim(), phone_call.getText().toString()), null);
//		}
//	};

	// 验证码输入监听器
	/*
	 * private TextWatcher mVerifyCodeWatcher = new TextWatcher() {
	 * @Override public void beforeTextChanged(CharSequence s, int start, int
	 * count, int after) { // TODO Auto-generated method stub }
	 * @Override public void onTextChanged(CharSequence s, int start, int
	 * before, int count) { // TODO Auto-generated method stub }
	 * @Override public void afterTextChanged(Editable s) { // TODO
	 * Auto-generated method stub // 检查字段完整性（依此来判断下一步按钮是否可点击） if
	 * (s.toString().length() > 0) { nextTv.setEnabled(true);
	 * nextTv.setBackgroundResource(R.drawable.reg_btn_bg); } else {
	 * nextTv.setEnabled(false);
	 * nextTv.setBackgroundResource(R.drawable.reg_btn_off); } } };
	 */

	// 接口回调函数
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int type, Object object) {
			if (mParentActivity.isLoadingDialogShowing()) {
				mParentActivity.dismissLoadingDialog();
			}
			if (type == EAPIConsts.ReqType.SEND_VALIDATE_EMAIL) { // 发送验证邮件
				// 跳转邮件验证页面
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					if (dataBox.mIsSuccess) {
						Intent intent = new Intent(getActivity(), SendVerifyEmailActivity.class);
						intent.putExtra(EConsts.Key.EMAIL, phone_call.getText().toString());
						startActivity(intent);
					}
				}
			}
			else if (type == EAPIConsts.ReqType.GET_VERIFY_CODE) { // 获取验证码
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
			}
			if (type == EAPIConsts.ReqType.REGISTER) { // 注册成功
				// 是否进行下一步操作
				if (mParentActivity.isLoadingDialogShowing()) {
					mParentActivity.dismissLoadingDialog();
				}
				// 跳转到个人信息完善界面
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					mMainApp.getAppData().setUser(dataBox.mJTMember);
					// 跳转到完善个人信息页面
					mParentActivity.startActivity(new Intent(getActivity(), RegisterPersonalDetailActivity.class));
					mParentActivity.finish();
				}
			}
			else if (type == EAPIConsts.ReqType.LOGIN_CONFIGURATION) {
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					// 以下字段均保存到数据库
					// SessionID
					mMainApp.getAppData().setSessionID(dataBox.mSessionID);
					// 货币范围
					if (dataBox.mListMoneyRange != null) {
						mMainApp.getAppData().setListMoneyRange(dataBox.mListMoneyRange);
					}
					// 货币类型
					if (dataBox.mListMoneyType != null) {
						mMainApp.getAppData().setListMoneyType(dataBox.mListMoneyType);
					}
					// 融资类型
					if (dataBox.mListInInvestType != null) {
						mMainApp.getAppData().setListInInvestType(dataBox.mListInInvestType);
					}
					// 保存投资类型
					if (dataBox.mListOutInvestType != null) {
						mMainApp.getAppData().setListOutInvestType(dataBox.mListOutInvestType);
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
					// 用户注册
					if (EUtil.isMobileNO(region_number.getText().toString().trim(), phone_call.getText().toString())) { // 手机注册
						UserReqUtil.doRegister(
								mParentActivity,
								mBindData,
								UserReqUtil.getDoRegisterParams(phone_call.getText().toString(), "", passwordEt.getText().toString(), vcodeEt.getText().toString(), 1, region_number.getText()
										.toString().substring(1)), null);
					}
				}
			}
		}
	};

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
				}
				else { // 倒计时仍在进行
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
