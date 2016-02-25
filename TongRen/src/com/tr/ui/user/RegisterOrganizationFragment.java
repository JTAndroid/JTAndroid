package com.tr.ui.user;

import java.util.Timer;
import java.util.TimerTask;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.ui.user.modified.RegisterPersonalDetailActivity;
import com.tr.App;
import com.tr.R;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/** @ClassName: FrgUserRegisterOrganization.java
 * @Description: 机构用户注册
 * @Author xuxinjian/leon
 * @Version v 1.0
 * @Created 2014-03-26
 * @LastEdit 2014-04-14 */
public class RegisterOrganizationFragment extends JBaseFragment {

	private final String TAG = getClass().getSimpleName();

	// 常量
	private final int EXPIRED_TIME = 60; // 验证码超时时间,60s
	private final int COUNTDOWN_INTERVAL = 1000; // 倒计时时间间隔

	// 消息类型
	private final int MSG_BASE = 100;
	private final int MSG_COUNT_DOWN = MSG_BASE + 1; // 倒计时的消息标识

	// 文字提示
	private final String TIP_EMPTY_PASSWORD = "密码不能为空";
	private final String TIP_ERROR_PASSWORD = "请输入6-19位密码";
	private final String TIP_EMPTY_VCODE = "请输入验证码";
	// private final String TIP_ERROR_ACCOUT = "请输入正确的手机号码或邮箱";
	private final String TIP_REGET_VERIFY_CODE = "重获验证码";
	private final String TIP_GET_VERIFY_CODE = "获取验证码";
	// private final String TIP_LOADING_REGISTER = "正在注册，请稍等...";
	// private final String TIP_LOADING_GET_VERIFY_CODE = "正在获取验证码，请稍等...";
	private final String TIP_ILLEGAL_ACCOUNT = "请输入正确的手机号或邮箱";
	private final String TIP_GET_VERIFY_CODE_SUCCESS = "验证码已发送到您的手机，请注意查收";

	// 控件相关
	// private TextView captionTv; // 文字提示
	private EditText accountEt; // 账户
	private EditText passwordEt; // 密码
	private LinearLayout vcodeParentLl; // 验证码
	private EditText vcodeEt; // 输入验证码
	private TextView vcodeTv; // 获取验证码
	private TextView agreementTv; // 同意协议
	private TextView registerTv; // 注册

	// 变脸相关
	private int mCountdownLeft; // 倒计时剩余时间
	private Timer mTimer; //
	private App mMainApp; // App全局对象
	private JBaseFragmentActivity mParentActivity; // 父Activity

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initVars();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mParentActivity = (JBaseFragmentActivity) activity;
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.user_frg_register_organization, container, false);
	}

	@Override
	public void onViewCreated(View container, Bundle savedInstanceState) {
		initControls(container);
	}

	// 初始化变量
	private void initVars() {
		mTimer = new Timer();
		mCountdownLeft = EXPIRED_TIME; // 60s倒计时时间
		mMainApp = App.getApp();
	}

	// 初始化控件
	private void initControls(View container) {
		// 提示文字
		// captionTv = (TextView) container.findViewById(R.id.captionTv);
		// 账号
		accountEt = (EditText) container.findViewById(R.id.accountEt);
		accountEt.addTextChangedListener(mAccountWatcher);
		// accountEt.setOnFocusChangeListener(mFocusChangeListener);
		// 密码
		passwordEt = (EditText) container.findViewById(R.id.passwordEt);
		// passwordEt.setOnFocusChangeListener(mFocusChangeListener);
		// passwordEt.addTextChangedListener(mPasswordWatcher);
		// 验证码
		vcodeParentLl = (LinearLayout) container.findViewById(R.id.vcodeParentLl);
		vcodeParentLl.setVisibility(View.INVISIBLE); // 隐藏，但保留位置
		vcodeEt = (EditText) container.findViewById(R.id.vcodeEt);
		// vcodeEt.setOnFocusChangeListener(mFocusChangeListener);
		// vcodeEt.addTextChangedListener(mVerifyCodeWatcher);
		vcodeTv = (TextView) container.findViewById(R.id.vcodeTv);
		vcodeTv.setText(TIP_GET_VERIFY_CODE);
		vcodeTv.setOnClickListener(mClickListener);
		// 协议
		agreementTv = (TextView) container.findViewById(R.id.agreementTv);
		agreementTv.setOnClickListener(mClickListener);
		// 注册
		registerTv = (TextView) container.findViewById(R.id.registerTv);
		registerTv.setOnClickListener(mClickListener);
	}

	// 检查信息的完整性（是否显示注册按钮）
	private boolean infoIntegrityCheck() {

		if (EUtil.isMobileNO(accountEt.getText().toString())) { // 手机号

			if (passwordEt.getText().length() <= 0) { // 密码
				showToast(TIP_EMPTY_PASSWORD);
				return false;
			}
			else if (passwordEt.getText().length() < 6 || passwordEt.getText().length() > 16) {
				showToast(TIP_ERROR_PASSWORD);
				return false;
			}
			if (vcodeEt.getText().length() <= 0) { // 验证码
				showToast(TIP_EMPTY_VCODE);
				return false;
			}
		}
		else if (EUtil.isEmail(accountEt.getText().toString())) { // 邮箱

			if (passwordEt.getText().length() <= 0) { // 密码
				showToast(TIP_EMPTY_PASSWORD);
				return false;
			}
			else if (passwordEt.getText().length() < 6 || passwordEt.getText().length() > 14) {
				showToast(TIP_ERROR_PASSWORD);
				return false;
			}
		}
		else {
			showToast(TIP_ILLEGAL_ACCOUNT);
		}
		return true;
	}

	// 点击事件监听器
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == vcodeTv) { // 开始倒计时

				if (vcodeTv.getText().equals(TIP_GET_VERIFY_CODE)) { // 获取验证码

					// 弹出提示
					new AlertDialog.Builder(getActivity()).setTitle("确认手机号码").setMessage("我们将发送验证码到这个号码：" + accountEt.getText()).setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 显示加载框
							mParentActivity.showLoadingDialog();
							// 发送获取验证码请求
							UserReqUtil.doGetVerifyCode(mParentActivity, mBindData, UserReqUtil.getDoGetVerifyCodeParams(0, "+86", accountEt.getText().toString()), null);
						}
					}).setNegativeButton("取消", null).create().show();
				}
				else if (vcodeTv.getText().equals(TIP_REGET_VERIFY_CODE)) { // 重获验证码

					// 不再需要提示，直接显示加载框
					mParentActivity.showLoadingDialog();
					// 发送获取验证码请求
					UserReqUtil.doGetVerifyCode(mParentActivity, mBindData, UserReqUtil.getDoGetVerifyCodeParams(0, "+86", accountEt.getText().toString()), null);
				}
			}
			else if (v == registerTv) { // 手机注册

				if (!infoIntegrityCheck()) {
					return;
				}

				if (EUtil.isMobileNO(accountEt.getText().toString())) {

					// 显示加载框
					mParentActivity.showLoadingDialog();
					// 是否有SessionID
					if (mMainApp.getAppData().getSessionID().length() <= 0) {
						UserReqUtil.doLoginConfiguration(mParentActivity, mBindData,
								UserReqUtil.getDoLoginConfigurationParams("", "", EUtil.getDeviceID(mParentActivity), EUtil.getAppVersionName(mParentActivity), "", "", "", "android", "", "", "", ""),
								null);
					}
					else {
//						UserReqUtil.doRegister(mParentActivity, mBindData,
//								UserReqUtil.getDoRegisterParams(accountEt.getText().toString(), "", passwordEt.getText().toString(), vcodeEt.getText().toString(), 2), null);
					}
				}
				else if (EUtil.isEmail(accountEt.getText().toString())) { // 邮箱注册

					// 显示加载框
					mParentActivity.showLoadingDialog();
					// 发送注册请求
					if (mMainApp.getAppData().getSessionID().length() <= 0) {
						UserReqUtil.doLoginConfiguration(mParentActivity, mBindData,
								UserReqUtil.getDoLoginConfigurationParams("", "", EUtil.getDeviceID(mParentActivity), EUtil.getAppVersionName(mParentActivity), "", "", "", "android", "", "", "", ""),
								null);
					}
					else {
//						UserReqUtil.doRegister(mParentActivity, mBindData,
//								UserReqUtil.getDoRegisterParams("", accountEt.getText().toString(), passwordEt.getText().toString(), vcodeEt.getText().toString(), 2), null);
					}
				}
			}
			else if (v == agreementTv) { // 注册协议
				startActivity(new Intent(mParentActivity, AgreementActivity.class));
			}
		}
	};

	// 焦点变化监听器
	/*
	 * private OnFocusChangeListener mFocusChangeListener=new
	 * OnFocusChangeListener(){
	 * @Override public void onFocusChange(View v, boolean hasFocus) { // TODO
	 * Auto-generated method stub if (v == passwordEt && hasFocus) {
	 * captionTv.setText(getActivity().getResources().getString(
	 * R.string.password_caption)); } else if (v == accountEt && hasFocus) {
	 * captionTv.setText(getActivity().getResources().getString(
	 * R.string.account_caption)); } else if (v == vcodeTv && hasFocus) {
	 * captionTv.setText(getActivity().getResources().getString(
	 * R.string.verify_code_caption)); } } };
	 */

	// 账号输入监听器
	private TextWatcher mAccountWatcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub

		}

		// 判断用户输入的是手机号还是邮箱（暂时只根据长度11位和是否是纯数字）
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (EUtil.isMobileNO(s.toString())) {
				// 验证码置空
				vcodeEt.setText("");
				// 提示文字改为"获取验证码"
				if (vcodeTv.getText().equals(TIP_REGET_VERIFY_CODE)) {
					vcodeTv.setText(TIP_GET_VERIFY_CODE);
				}
				// 显示验证码输入框
				vcodeParentLl.setVisibility(View.VISIBLE);
			}
			else {
				// 隐藏验证码输入框
				vcodeParentLl.setVisibility(View.INVISIBLE);
			}
			// @@deprecated 不能取消，否则会重复请求
			// 取消倒计时任务（如果正在进行）
			/*
			 * if(mTimer != null){ mTimer.cancel(); mTimer = null; }
			 */
			// 检查字段完整性（依此来判断注册按钮是否可点击）
			/*
			 * if(infoIntegrityCheck(accountEt.getText().toString())){
			 * registerTv.setEnabled(true);
			 * registerTv.setBackgroundResource(R.drawable.reg_btn_bg); } else{
			 * registerTv.setEnabled(false);
			 * registerTv.setBackgroundResource(R.drawable.reg_btn_off); }
			 */
		}
	};

	// 密码输入监听器
	/*
	 * private TextWatcher mPasswordWatcher = new TextWatcher() {
	 * @Override public void beforeTextChanged(CharSequence s, int start, int
	 * count, int after) { // TODO Auto-generated method stub }
	 * @Override public void onTextChanged(CharSequence s, int start, int
	 * before, int count) { // TODO Auto-generated method stub }
	 * @Override public void afterTextChanged(Editable s) { // TODO
	 * Auto-generated method stub // 字段完整性检查（依次来判断注册按钮是否可点击） if
	 * (infoIntegrityCheck(accountEt.getText().toString())) {
	 * registerTv.setEnabled(true);
	 * registerTv.setBackgroundResource(R.drawable.reg_btn_bg); } else {
	 * registerTv.setEnabled(false);
	 * registerTv.setBackgroundResource(R.drawable.reg_btn_off); } } };
	 */

	// 验证码输入监听器
	/*
	 * private TextWatcher mVerifyCodeWatcher = new TextWatcher(){
	 * @Override public void beforeTextChanged(CharSequence s, int start, int
	 * count, int after) { // TODO Auto-generated method stub }
	 * @Override public void onTextChanged(CharSequence s, int start, int
	 * before, int count) { // TODO Auto-generated method stub }
	 * @Override public void afterTextChanged(Editable s) { // TODO
	 * Auto-generated method stub // 检查各字段合法性 if
	 * (infoIntegrityCheck(accountEt.getText().toString())) {
	 * registerTv.setEnabled(true);
	 * registerTv.setBackgroundResource(R.drawable.reg_btn_bg); } else {
	 * registerTv.setEnabled(false);
	 * registerTv.setBackgroundResource(R.drawable.reg_btn_off); } } };
	 */

	// 接口回调函数
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int tag, Object object) {
			// TODO Auto-generated method stub
			// 如果用户取消Dialog，则不进行下一步操作
			if (tag == EAPIConsts.ReqType.REGISTER) { // 注册成功
				// 跳转到个人信息完善界面
				if (!mParentActivity.isLoadingDialogShowing()) {
					return;
				}
				else {
					mParentActivity.dismissLoadingDialog();
				}
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					mMainApp.getAppData().setUser(dataBox.mJTMember);
					// 跳转到机构联系人
					mParentActivity.startActivity(new Intent(getActivity(), RegisterOrganizationContactActivity.class));
					mParentActivity.finish();
				}
			}
			else if (tag == EAPIConsts.ReqType.GET_VERIFY_CODE) { // 获取验证码
				if (!mParentActivity.isLoadingDialogShowing()) {
					return;
				}
				else {
					mParentActivity.dismissLoadingDialog();
				}
				// 是否获取成功
				if (object != null) {
					DataBox dataBox = (DataBox) object;
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
								// TODO Auto-generated method stub
								mCountdownLeft--;
								mHandler.sendEmptyMessage(MSG_COUNT_DOWN);
							}
						}, 0, COUNTDOWN_INTERVAL);
						// 设置倒计时时间
						mCountdownLeft = EXPIRED_TIME;
						// 设置验证按钮状态
						vcodeTv.setEnabled(false);
						vcodeTv.setBackgroundResource(R.drawable.reg_vcode_btn_off);
						// 显示消息
						showToast(TIP_GET_VERIFY_CODE_SUCCESS);
					}
				}
			}
			else if (tag == EAPIConsts.ReqType.LOGIN_CONFIGURATION) {
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
					if (EUtil.isMobileNO(accountEt.getText().toString())) { // 手机注册
//						UserReqUtil.doRegister(mParentActivity, mBindData,
//								UserReqUtil.getDoRegisterParams(accountEt.getText().toString(), "", passwordEt.getText().toString(), vcodeEt.getText().toString(), 2), null);
					}
					else { // 邮箱注册
//						UserReqUtil.doRegister(mParentActivity, mBindData,
//								UserReqUtil.getDoRegisterParams("", accountEt.getText().toString(), passwordEt.getText().toString(), vcodeEt.getText().toString(), 2), null);
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
					vcodeTv.setEnabled(true);
					vcodeTv.setBackgroundResource(R.drawable.reg_vcode_btn_bg);
				}
				else { // 倒计时仍在进行
					vcodeTv.setText(String.format("剩余%d秒", mCountdownLeft));
				}
				break;
			}
		}
	};
}
