package com.tr.ui.user.modified;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/** @ClassName: SendVerifyEmailActivity.java
 * @Description: 发送验证邮件页面
 * @Author leon / modified by sunjianan
 * @Version v 1.0
 * @Created 2014-04-01
 * @LastEdit 2014-04-14 */
public class SendVerifyEmailActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();

	// 控件
	private TextView emailTv;
	private TextView checkEmailTv;
	private TextView resendTv;
	// 变量
	private String mEmail;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "邮箱验证", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_act_send_verify_email);

		initVars();
		initControls();
	}

	private void initVars() {
		mEmail = getIntent().getStringExtra(EConsts.Key.EMAIL);
	}

	private void initControls() {
		// 邮箱地址
		emailTv = (TextView) findViewById(R.id.emailTv);
		emailTv.setText(mEmail);
		// 验证邮箱
		checkEmailTv = (TextView) findViewById(R.id.checkEmailTv);
		checkEmailTv.setOnClickListener(mClickListener);
		// 重发邮件
		resendTv = (TextView) findViewById(R.id.resendTv);
		resendTv.setOnClickListener(mClickListener);
		// 确定
		confirm = (Button) findViewById(R.id.confirm);
		confirm.setOnClickListener(mClickListener);
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == checkEmailTv) { // 查看邮箱
				// 指定域名
				String url = "http://mail." + mEmail.substring(mEmail.indexOf("@") + 1);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
			else if (v == resendTv) { // 重新发送
				// 显示加载框
				showLoadingDialog();
				// 请求数据
				UserReqUtil.doSendValidateEmail(SendVerifyEmailActivity.this, mBindData, UserReqUtil.getDoSendValidateEmailParams(mEmail), null);
			}
			else if (v == confirm) {
				ENavigate.startLoginActivity(SendVerifyEmailActivity.this, null);
				finish();
			}
		}
	};

	// 接口回调
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int tag, Object object) {
			// TODO Auto-generated method stub
			if (!isLoadingDialogShowing()) {
				return;
			}
			else {
				dismissLoadingDialog();
			}
			if (tag == EAPIConsts.ReqType.SEND_VALIDATE_EMAIL) {
				// 处理数据
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					// showToast(dataBox.mMessage);
					if (dataBox.mIsSuccess) {
						showToast("发送成功");
					}
					else {
						showToast("发送失败");
					}
				}
			}
		}
	};

	private Button confirm;
}
