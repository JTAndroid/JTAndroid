package com.tr.ui.user.modified;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class InputVerifyCodeActivity extends JBaseActivity {

	private final String TAG = getClass().getSimpleName();

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_verifycode);

		phoneCall = getIntent().getStringExtra(ENavConsts.phoneCall);
		countSeconds = (TextView) findViewById(R.id.count_backwards);
		initVars();
		initControls();
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "重置密码", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(0, 101, 0, "下一步");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 101:
			nextStep();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// 初始化变量
	private void initVars() {
		mTimer = new Timer();
		mCountdownLeft = EXPIRED_TIME; // 60s倒计时时间
		mVerifyCode = ""; // 验证码
	}

	// 初始化控件
	private void initControls() {
		// 输入验证码
		vcodeEt = (EditText) findViewById(R.id.vcodeEt);
		// vcodeEt.addTextChangedListener(mVerifyCodeWatcher);
		// 获取验证码
		vcodeTv = (TextView) findViewById(R.id.vcodeTv);
		vcodeTv.setText(TIP_GET_VERIFY_CODE);
		vcodeTv.setOnClickListener(mClickListener);
	}

	// 检查信息的完整性（是否显示下一步按钮）
	private boolean infoIntegrityCheck() {

		if (EUtil.isMobileNO("", phoneCall)) { // 手机号和验证码
			if (vcodeEt.getText().toString().length() <= 0) {
				showToast(TIP_EMPTY_VCODE);
				return false;
			}
			if (!mVerifyCode.equals(vcodeEt.getText().toString())) {
				showToast(TIP_WRONG_VERIFY_CODE);
				return false;
			}
		}
		else if (EUtil.isEmail(phoneCall)) {
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
	 * if(EUtil.isEmail(phoneCall)){ return true; } return false; }
	 */

	// 点击事件监听器
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == vcodeTv) { // 开始倒计时
				// 获取验证码
				if (vcodeTv.getText().equals(TIP_GET_VERIFY_CODE)) {
					// 弹出提示
//					new AlertDialog.Builder(InputVerifyCodeActivity.this, R.style.phone_dialog)
					
					//modified by zhongshan 去掉对话框，直接发送验证
//					new AlertDialog.Builder(InputVerifyCodeActivity.this).setTitle("确认手机号码").setMessage("我们将发送验证码到这个号码：" + phoneCall).setPositiveButton("确定", mDlgClickListener)
//							.setNegativeButton("取消", null).create().show();
					showLoadingDialog();
					UserReqUtil.doGetVerifyCode(InputVerifyCodeActivity.this, mBindData, UserReqUtil.getDoGetVerifyCodeParams(1, "+86", phoneCall), null);
					
				}
				else if (vcodeTv.getText().equals(TIP_REGET_VERIFY_CODE)) { // 重获验证码

					// 不再需要提示，直接显示加载框
					showLoadingDialog();
					// 发送获取验证码请求
					UserReqUtil.doGetVerifyCode(InputVerifyCodeActivity.this, mBindData, UserReqUtil.getDoGetVerifyCodeParams(1, "+86", phoneCall), null);
				}
			}
		}
	};

	/** 下一步 */
	private void nextStep() {
		if (!infoIntegrityCheck()) {
			return;
		}

		if (EUtil.isMobileNO("", phoneCall)) { // 手机注册

			Intent intent = new Intent(InputVerifyCodeActivity.this, ResetPasswordActivity.class);
			intent.putExtra(EConsts.Key.MOBILE, phoneCall);
			startActivity(intent);
		}
		else if (EUtil.isEmail(phoneCall)) { // 邮箱注册

			// 显示加载框
			showLoadingDialog();
			// 发送邮件请求
			UserReqUtil.doSendValidateEmail(InputVerifyCodeActivity.this, mBindData, UserReqUtil.getDoSendValidateEmailParams(phoneCall), null);
		}
	}

	// 发送验证码
//	private DialogInterface.OnClickListener mDlgClickListener = new DialogInterface.OnClickListener() {
//
//		@Override
//		public void onClick(DialogInterface dialog, int which) {
//			// TODO Auto-generated method stub
//			// 显示加载框
//			showLoadingDialog();
//			// 发送获取验证码请求
//			UserReqUtil.doGetVerifyCode(InputVerifyCodeActivity.this, mBindData, UserReqUtil.getDoGetVerifyCodeParams(1, "+86", phoneCall), null);
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
			if (!isLoadingDialogShowing()) {
				return;
			}
			else {
				dismissLoadingDialog();
			}
			// TODO Auto-generated method stub
			if (type == EAPIConsts.ReqType.SEND_VALIDATE_EMAIL) { // 发送验证邮件
				// 跳转邮件验证页面
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					if (dataBox.mIsSuccess) {
						Intent intent = new Intent(InputVerifyCodeActivity.this, SendVerifyEmailActivity.class);
						intent.putExtra(EConsts.Key.EMAIL, phoneCall);
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
								// TODO Auto-generated method stub
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

	private String phoneCall;

}
