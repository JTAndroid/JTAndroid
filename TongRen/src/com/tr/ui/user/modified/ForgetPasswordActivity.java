package com.tr.ui.user.modified;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

/** @ClassName: ForgetPasswordActivity.java
 * @Description: 找回密码
 * @Author leon
 * @Version v 1.0
 * @Created 2014-03-28
 * @Updated 2014-04-14 */
public class ForgetPasswordActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();

	// 常量
	private final int EXPIRED_TIME = 60; // 验证码超时时间,60s
	private View view;
	private TextView color_tv;

	private String[] emailSufixs = new String[] { "@qq.com", "@163.com", "@126.com", "@gmail.com", "@sina.com", "@hotmail.com", "@yahoo.cn", "@sohu.com", "@foxmail.com", "@139.com", "@yeah.net",
			"@vip.qq.com", "@vip.sina.com" };

	// 消息类型
	// private final String TIP_LOADING_SEND_VERIFY_EMAIL = "正在请求发送验证邮件，请稍等...";
	// private final String TIP_LOADING_GET_VERIFY_CODE = "正在获取验证码，请稍等...";

	// 定义字符串数组作为提示的文本

	// 控件相关
	private EditText accountEt; // 账户

	// ActionBar菜单栏字体颜色变化
	@Override
	public void onCreate(Bundle savedInstanceState) {
		getLayoutInflater().setFactory(new Factory() {


			@Override
			public View onCreateView(String name, Context context, AttributeSet attrs) {
				
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView") || name.equalsIgnoreCase("com.android.internal.view.menu.ActionMenuItemView")) {
					try {
						LayoutInflater f = getLayoutInflater();
						view = f.createView(name, null, attrs);

						if (view instanceof TextView) {
							color_tv = (TextView) view; 
							color_tv.setTextColor(Color.WHITE);
						}
						return view;
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (InflateException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		});
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_act_forget_password);
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
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	  {
	    // Inflate the menu; this adds items to the action bar if it is present.
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.forgetpassword_menu, menu);
	      return super.onCreateOptionsMenu(menu);
	  }*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 101:
			nextStep();

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/** 下一步 */
	private void nextStep() {
		if (EUtil.isMobileNO("", accountEt.getText().toString())) { // 手机注册
			ENavigate.startInputVerifyCodeActivity(ForgetPasswordActivity.this, accountEt.getText().toString());
		}
		else if (EUtil.isEmail(accountEt.getText().toString())) { // 邮箱注册

			// 显示加载框
			showLoadingDialog();
			// 发送邮件请求
			UserReqUtil.doSendValidateEmail(ForgetPasswordActivity.this, mBindData, UserReqUtil.getDoSendValidateEmailParams(accountEt.getText().toString()), null);
		}
		else {
			Toast.makeText(ForgetPasswordActivity.this, "请输入正确的手机号或邮箱", 0).show();
		}
	}

	// 初始化控件
	private void initControls() {
		// 账号
		accountEt = (EditText) findViewById(R.id.accountEt);
		accountEt.addTextChangedListener(mAccountWatcher);
	}

	/*
	 * // 检查信息的合法性（只检查邮箱格式，手机号错误不会显示下一步按钮） private boolean infoLegalityCheck(){
	 * if(EUtil.isEmail(accountEt.getText().toString())){ return true; } return
	 * false; }
	 */

	// 发送验证码
	private DialogInterface.OnClickListener mDlgClickListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			// 显示加载框
			showLoadingDialog();
			// 发送获取验证码请求
			UserReqUtil.doGetVerifyCode(ForgetPasswordActivity.this, mBindData, UserReqUtil.getDoGetVerifyCodeParams(1, "+86", accountEt.getText().toString()), null);
		}
	};

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
			try {
				if (StringUtils.isEmpty(s.toString())) {
					color_tv.setTextColor(Color.WHITE);
				}else {
					color_tv.setTextColor(0xFF65696A);
				}
				// isMobilePhone(s.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	//TODO
	private void showEmailPopupWindow() {
		//PopupWindow window = new PopupWindow(width, height);
	}

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

	public boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

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
						Intent intent = new Intent(ForgetPasswordActivity.this, SendVerifyEmailActivity.class);
						intent.putExtra(EConsts.Key.EMAIL, accountEt.getText().toString());
						startActivity(intent);
					}
				}
			}
		}
	};

	
}
