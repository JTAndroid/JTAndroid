package com.tr.ui.user.modified;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
//import com.baidu.navisdk.util.common.StringUtils;

/** @ClassName: ResetPasswordActivity.java
 * @Description: 重置密码页面
 * @Author leon
 * @Version v 1.0
 * @Created 2014-04-01
 * @LastEdit 2014-04-14 */
public class ResetPasswordActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();

	// 常量
	// private final String TIP_LOADING = "正在提交，请稍等...";

	// 控件
	private EditText passwordEt;
	private boolean flag = true;

	// 变量
	private String mMobile; // 用户输入的手机号

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "重置密码", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(0, 111, 0, "完成");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 111:
			resetPassword();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_act_reset_password);
		initVars();
		initControls();
	}

	private void initVars() {
		mMobile = getIntent().getStringExtra(EConsts.Key.MOBILE);
	}

	private void initControls() {
		// 密码
		passwordEt = (EditText) findViewById(R.id.passwordEt);
		// 删除密码
		deletePassword = (ImageView) findViewById(R.id.delete_password);
		// 确认密码
		viewPassword = (ImageView) findViewById(R.id.view_password);
		//对图标进行初始化操作
		passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		viewPassword.setBackgroundResource(R.drawable.show_password_bg_normal);
		//根据点击改变图标状态
		viewPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (flag) {
					//以点点的形式显示密码
					passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					viewPassword.setBackgroundResource(R.drawable.show_password_bg_pressed);
					flag = false;
					
				} else {
					//不以以点点的形式显示密码
					passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					viewPassword.setBackgroundResource(R.drawable.show_password_bg_normal);
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
				if (!StringUtils.isEmpty(s.toString()) && s.length() >= 6) {
					if(s.length() > 19){
						Toast.makeText(ResetPasswordActivity.this, "请输入6-19位密码", 0).show();
					}
					deletePassword.setVisibility(View.VISIBLE);
				}
				else {
					deletePassword.setVisibility(View.GONE);
				}
			}
		});
	}

	// 检查用户信息的合法性
	private boolean infoLegalityCheck() {
		// 密码是否符合规范
		if (!EUtil.isPasswordFormatCorrect(ResetPasswordActivity.this, passwordEt.getText().toString())) {
			return false;
		}
		return true;
	}

	private void resetPassword() {
		if(passwordEt.getText().length() <= 6 && passwordEt.getText().length() >= 19) {
			Toast.makeText(ResetPasswordActivity.this, "请输入6-19位密码", 0).show();
			return;
		}
		// TODO Auto-generated method stub
		if (!infoLegalityCheck()) {
			return;
		}
		// 显示加载框
		showLoadingDialog();
		// 请求数据
		UserReqUtil.doSetNewPassword(ResetPasswordActivity.this, mBindData, UserReqUtil.getDoSetNewPasswordParams(mMobile, "", "", passwordEt.getText().toString()), null);
	}

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
			if (tag == EAPIConsts.ReqType.SET_NEW_PASSWORD) {
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					showToast(dataBox.mMessage);
					if (dataBox.mIsSuccess) { // 修改成功
					// Intent intent = new
					// Intent(ResetPasswordActivity.this,LoginActivity.class);
					// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					// startActivity(intent);
						ENavigate.startResetPasswordSuccessActivity(ResetPasswordActivity.this);
						finish();
					}
				}
			}
		}
	};

	private ImageView viewPassword;
	private ImageView deletePassword;
}
