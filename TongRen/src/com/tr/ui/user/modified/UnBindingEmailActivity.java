package com.tr.ui.user.modified;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 更改邮箱
 * 
 * @author cui
 * 
 */
public class UnBindingEmailActivity extends JBaseFragmentActivity {

	private String email;
	private final int EXPIRED_TIME = 60; // 手机验证码超时时间,60s
	private final int COUNTDOWN_INTERVAL = 1000; // 倒计时时间间隔
	private int mCountdownLeft; // 倒计时剩余时间
	private Timer mTimer; // 计时任务
	private Button mReSend;
	private App mMainApp;
	// 消息类型
	private final int MSG_BASE = 100;
	private final int MSG_COUNT_DOWN = MSG_BASE + 1; // 倒计时的消息标识

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "更改邮箱", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_send_verify_email);
		email = getIntent().getStringExtra("email");
		mCountdownLeft = EXPIRED_TIME; // 60s倒计时时间
		// 全局对象
		mMainApp = App.getApp();
		initViews();

	}

	private void initViews() {
		TextView emailTv = (TextView) findViewById(R.id.tv_emailNum);
		emailTv.setText(email);
		mReSend = (Button) findViewById(R.id.bt_re_send);
		mReSend.setOnClickListener(mClickListener);
		Button mNext = (Button) findViewById(R.id.bt_second);
		mNext.setVisibility(View.VISIBLE);
		mNext.setOnClickListener(mClickListener);
	}

	// 点击事件监听器
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_re_send:
				// 显示加载框
				showLoadingDialog();
				// 若邮箱正确执行下面，进行发送验证邮件
				// 请求数据
				UserReqUtil.doSet_SendValidateEmail(
						UnBindingEmailActivity.this, mBindData, "1", email,
						"1", null);
				break;
			case R.id.bt_second:
				// TODO 点击下一步,先验证邮箱是否验证成功，成功后 跳转到绑定邮箱页面
				UserReqUtil.doSet_CheckEmailStatus(UnBindingEmailActivity.this,
						mBindData, "1", email, null);
				break;
			default:
				break;
			}

		}

	};

	// 接口回调函数
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int type, Object object) {
			if (isLoadingDialogShowing()) {
				dismissLoadingDialog();
			}
			// 重新发送验证邮箱
			if (type == EAPIConsts.ReqType.SEND_VALIDATE_EMAIL) {
				// 处理数据
				if (object != null) {
					DataBox dataBox = (DataBox) object;

					if (dataBox.mIsSuccess) {
						showToast("发送成功");
						// 60秒之后重新发送验证邮件并且重新计时设置该按钮不可点
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

					} else {
						showToast("发送失败");

					}
				}
			}
			// 验证邮箱是否验证成功
			if (type == EAPIConsts.ReqType.SET_CHECK_EMAIL_STATUS) {
				// 处理数据
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					// "msg": "1:验证成功2:连接失效3:连接已验证"
					verifyUserStatus(Integer.valueOf(dataBox.mMessage));
				}
			}
		}
	};

	// "msg": "1:验证成功2:连接失效3:连接已验证"
	private void verifyUserStatus(int userStatus) {
		switch (userStatus) {
		case 3:
			showToast("连接已验证");
			// break;
		case 1:// TODO 邮箱验证成功跳转到绑定新邮箱页面
			showToast("验证成功");
			ENavigate.startBindingEmailActivity(UnBindingEmailActivity.this, 2);// 完成验证
			finish();
			break;
		case 2:
			showToast("连接失效");
			break;
		default:
			break;
		}
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
					mReSend.setText("重新发送");
					mReSend.setEnabled(true);
					mReSend.setBackgroundResource(R.drawable.sign_in);
				} else { // 倒计时仍在进行
					mReSend.setText("重新发送(" + mCountdownLeft + ")s");
					mReSend.setEnabled(false);
					mReSend.setBackgroundResource(R.drawable.sign_in_normal);
				}
				break;
			}
		}
	};
}
