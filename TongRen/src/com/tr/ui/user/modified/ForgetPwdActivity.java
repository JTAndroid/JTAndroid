package com.tr.ui.user.modified;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.MessageDialog;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class ForgetPwdActivity extends JBaseActivity implements OnClickListener{

	private EditText mobileEt, vcodeEt, pwdEt;
	private TextView getCodeTv, comitTv, emailTv;
	private final String TIP_REGET_VERIFY_CODE = "重获验证码";
	// 常量
	private final int EXPIRED_TIME = 60; // 验证码超时时间,60s
	private final int COUNTDOWN_INTERVAL = 1000; // 倒计时时间间隔
	// 消息类型
	private final int MSG_BASE = 100;
	private final int MSG_COUNT_DOWN = MSG_BASE + 1; // 倒计时的消息标识
	// 变脸相关
	private int mCountdownLeft; // 倒计时剩余时间
	private Timer mTimer; // 计时任务
	private String mVerifyCode; // 验证码
	private final String TIP_GET_VERIFY_CODE_SUCCESS = "验证码已发送到您的手机，请注意查收";
	
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "找回密码", false, null, false, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_act_forget_pwd);
		
		mobileEt = (EditText) findViewById(R.id.mobileEt);
		vcodeEt = (EditText) findViewById(R.id.vcodeEt);
		pwdEt = (EditText) findViewById(R.id.pwdEt);
		getCodeTv = (TextView) findViewById(R.id.getCodeTv);
		comitTv = (TextView) findViewById(R.id.comitTv);
		emailTv = (TextView) findViewById(R.id.emailTv);
		
		getCodeTv.setOnClickListener(this);
		comitTv.setOnClickListener(this);
		emailTv.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		String phoneCall = mobileEt.getText().toString();
		switch(v.getId()){
		case R.id.getCodeTv:
			if (EUtil.isMobileNO("", phoneCall)) { // 手机注册
				// 不再需要提示，直接显示加载框
				showLoadingDialog();
				// 发送获取验证码请求
				UserReqUtil.doGetVerifyCode(this, mBindData, UserReqUtil.getDoGetVerifyCodeParams(1, "+86", phoneCall), null);
			}else {
				showToast("请输入正确的手机号");
			}
			break;
		case R.id.comitTv:
			if(null==mVerifyCode){
				return;
			}
			if (!mVerifyCode.equals(vcodeEt.getText().toString())) {
				showToast("验证码错误");
				return;
			}
			if (EUtil.isPasswordFormatCorrect(this, pwdEt.getText().toString())) {
				// 显示加载框
				showLoadingDialog();
				// 请求数据
				UserReqUtil.doSetNewPassword(this, mBindData, UserReqUtil.getDoSetNewPasswordParams(phoneCall, "", "", pwdEt.getText().toString()), null);
			}
			break;
		case R.id.emailTv:
			String content = "为了更好的体验请打开网站\nwww.gintong.com\n在找回密码中找回";
			SpannableString spancontent = new SpannableString(content);
			spancontent.setSpan(new ForegroundColorSpan(0xff569ee2), 12, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			MessageDialog msgDialog = new MessageDialog(this);
			msgDialog.setTitle("");
			msgDialog.setContent(spancontent);
			msgDialog.goneButton();
			msgDialog.show();
			break;
		}
	}
	
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
					getCodeTv.setText(TIP_REGET_VERIFY_CODE);
					getCodeTv.setTextColor(getResources().getColor(R.color.btn_click));
					getCodeTv.setEnabled(true);
				}
				else { // 倒计时仍在进行
					getCodeTv.setText("" + mCountdownLeft+"秒后可重发");
				}
				break;
			}
		}
	};
	// 接口回调函数
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int type, Object object) {
			dismissLoadingDialog();
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
								// TODO Auto-generated method stub
								mCountdownLeft--;
								mHandler.sendEmptyMessage(MSG_COUNT_DOWN);
							}
						}, 0, COUNTDOWN_INTERVAL);
						// 设置倒计时时间
						mCountdownLeft = EXPIRED_TIME;
						// 设置验证按钮状态
						getCodeTv.setEnabled(false);
						getCodeTv.setTextColor(getResources().getColor(R.color.text_hint));
						// 显示消息
						showToast(TIP_GET_VERIFY_CODE_SUCCESS);
					}
				}
			}else if (type == EAPIConsts.ReqType.SET_NEW_PASSWORD) {
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					showToast(dataBox.mMessage);
					if (dataBox.mIsSuccess) { // 修改成功
						ENavigate.startResetPasswordSuccessActivity(ForgetPwdActivity.this);
						finish();
					}
				}
			}
		}
	};
}
