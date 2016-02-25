package com.tr.ui.user.modified;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * 绑定手机号页面-解绑原手机号
 * 
 * @author Administrator
 * 
 */
public class UnBindingMobileActivity extends JBaseActivity {

	private EditText mInMoblieNum;// 原手机号
	private TextView mGetCode;// 获取验证码按钮
	private EditText mInputCode;// 输入验证码控件
	private Button mNext;// 下一步
	private Context context;
	// 文字提示
	private final String TIP_REGET_VERIFY_CODE = "重获验证码";
	private final String TIP_GET_VERIFY_CODE = "获取验证码";
	private final String TIP_EMPTY_VCODE = "请输入验证码";
	private final String TIP_ILLEGAL_ACCOUNT = "请输入正确的手机号码";
	private final String TIP_GET_VERIFY_CODE_SUCCESS = "验证码已发送到您的手机，请注意查收";
	// 变量
//	private boolean hasMoblieNum = false;// 是否输入手机号
	private boolean hasVCode = false;// 是否输入验证码
	// 消息类型
	private final int MSG_BASE = 100;
	private final int MSG_COUNT_DOWN = MSG_BASE + 1; // 倒计时的消息标识

	private final int EXPIRED_TIME = 60; // 手机验证码超时时间,60s
	private final int COUNTDOWN_INTERVAL = 1000; // 倒计时时间间隔
	private int mCountdownLeft; // 倒计时剩余时间
	private Timer mTimer; // 计时任务
	private String mVerifyCode; // 验证码，方便调试时查看验证码 
	private String mobile;//原手机号
	private String mobile_point;//原手机号中间四位变成小星点后的手机号
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "更改手机", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_binding_mobile);
		mobile = getIntent().getStringExtra("mobile");
		mobile_point=mobile.substring(0,3)+"****"+mobile.substring(7,mobile.length());
		context = this;
		initViews();
		initVars();
	}

	private void initViews() {
		TextView mMoblieNum = (TextView) findViewById(R.id.tv_moblie_num);
		mMoblieNum.setText("原手机号");
		mInMoblieNum = (EditText) findViewById(R.id.et_in_moblie_num);
		mInMoblieNum.setHint("输入原手机号");
		mInMoblieNum.setText(mobile_point);
		mInMoblieNum.setFocusable(false);

		mGetCode = (TextView) findViewById(R.id.tv_get_vcode);
		mGetCode.setText(TIP_GET_VERIFY_CODE);
		mGetCode.setOnClickListener(mClickListener);

		TextView mVcode = (TextView) findViewById(R.id.tv_vcode);
		mVcode.setText("验证码\u3000");
		mInputCode = (EditText) findViewById(R.id.et_in_vcode);
		mInputCode.setHint("请输入验证码");
		mInputCode.addTextChangedListener(new TextWatcher() {

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
					hasVCode = true;
				} else {
					hasVCode = false;
				}
				if ( hasVCode) {
					mNext.setClickable(true);
					mNext.setBackgroundResource(R.drawable.sign_in);
				} else {
					mNext.setClickable(false);
					mNext.setBackgroundResource(R.drawable.sign_in_normal);
				}
			}
		});
		mNext = (Button) findViewById(R.id.bt_on_binding);
		mNext.setOnClickListener(mClickListener);
		mNext.setText("下一步");
		mNext.setClickable(false);
		findViewById(R.id.tv_note).setVisibility(View.VISIBLE);

	}


	// 初始化变量
	private void initVars() {
		mTimer = new Timer();
		mCountdownLeft = EXPIRED_TIME; // 60s倒计时时间
		mVerifyCode = ""; // 验证码
	}

	// 点击事件监听器
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_get_vcode:
				// 点击获取验证码  
				if (mGetCode.getText().toString().equals(TIP_GET_VERIFY_CODE)
						| mGetCode.getText().equals(TIP_REGET_VERIFY_CODE)) {
					// 验证手机号或邮箱是否正确
					if (EUtil.isMobileNO(mobile)) {
						// 发送获取验证码请求
						showLoadingDialog();
						//这个验证码请求接口type 设为1，0为注册，2为组织注册 ，
						UserReqUtil.doGetVerifyCode(context, mBindData,
								UserReqUtil.getDoGetVerifyCodeParams(1, null,
										mobile), null);
					} else {
						if (StringUtils
								.isEmpty(mobile)) {
							// 什么也不做
						} else {
							Toast.makeText(context, "您填写的号码格式不正确", 0).show();
						}
					}
				}

				break;
			case R.id.bt_on_binding:
				// 点击下一步
				nextStep();
				break;

			default:
				break;
			}
		}

	};

	/** 下一步 */
	private void nextStep() {
		if (!infoIntegrityCheck()) {
			return;
		}
		if (EUtil.isMobileNO(mobile)) { // 手机注册

			// 显示加载框
			showLoadingDialog();
			//进行验证手机
			UserReqUtil.doSet_CheckMobileStatus(UnBindingMobileActivity.this, mBindData, "1", mInputCode.getText().toString().trim(), mobile, null);
		}

		else {
			Toast.makeText(context, "请输入正确的手机号", 0).show();
		}
	}

	// 检查信息的完整性（是否显示下一步按钮）
	private boolean infoIntegrityCheck() {

		if (EUtil.isMobileNO(mobile)) {
			// 验证码不能为空  后台验证是否正确
			if (mInputCode.getText().toString().length() <= 0) {
				showToast(TIP_EMPTY_VCODE);
				return false;
			}
		} else {
			showToast(TIP_ILLEGAL_ACCOUNT);
			return false;
		}
		return true;
	}

	// 接口回调函数
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int type, Object object) {
			if (isLoadingDialogShowing()) {
				dismissLoadingDialog();
			}
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
						mGetCode.setEnabled(false);
						// 显示消息
						showToast(TIP_GET_VERIFY_CODE_SUCCESS);
					}
				}
			}
			if (type == EAPIConsts.ReqType.SET_CHECK_MOBILE_STATUS) { // 手机号验证成功
				// 是否进行下一步操作
				if (isLoadingDialogShowing()) {
					dismissLoadingDialog();
				}
				if (object != null) {
					// 初始化全局变量
					DataBox dataBox = (DataBox) object;
					if (dataBox.mIsSuccess) {
						// 验证成功跳转到绑定手机号页面，重新绑定一个新号后才算真正的更改手机号
						ENavigate.startBindingMobileActivity(UnBindingMobileActivity.this, 2);
						finish();
					}else {
						showToast("验证失败");
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
					mGetCode.setText(TIP_REGET_VERIFY_CODE);
					mGetCode.setEnabled(true);
				} else { // 倒计时仍在进行
					mGetCode.setText("" + mCountdownLeft + "秒后可重发");
				}
				break;
			}
		}
	};
	
}
