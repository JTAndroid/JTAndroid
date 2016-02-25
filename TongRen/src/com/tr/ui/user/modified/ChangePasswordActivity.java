package com.tr.ui.user.modified;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.model.api.DataBox;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EUtil;
import com.utils.common.MBase64;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class ChangePasswordActivity extends JBaseFragmentActivity implements
		OnClickListener, IBindData {

	private Context context;
	private MenuItem finishMenuItem;
	/* New密码是否显示 */
	private CheckBox mShowNewPasswordCb;
	/* Old密码是否显示 */
	private CheckBox mShowOldPasswordCb;
	/* 新密码输入框 */
	private EditText mNewPasswordEt;
	/* 旧密码输入框 */
	private EditText mOldPasswordEt;
	/* 清空新密码 */
	private ImageView mDeleteNewPasswordIv;
	/* 清Old新密码 */
	private ImageView mDeleteOldPasswordIv;

	public Context getContext() {
		return context;
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "修改密码", false, null, false, true);
//		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_change_password);

		mShowNewPasswordCb = (CheckBox) findViewById(R.id.showNewPasswordCb);
		mShowOldPasswordCb = (CheckBox) findViewById(R.id.showOldPasswordCb);
		mNewPasswordEt = (EditText) findViewById(R.id.newPasswordEt);
		mOldPasswordEt = (EditText) findViewById(R.id.oldPasswordEt);
		mDeleteNewPasswordIv = (ImageView) findViewById(R.id.deleteNewPasswordIv);
		mDeleteOldPasswordIv = (ImageView) findViewById(R.id.deleteOldPasswordIv);
		mShowNewPasswordCb.setOnClickListener(this);
		mShowOldPasswordCb.setOnClickListener(this);
		mDeleteNewPasswordIv.setOnClickListener(this);
		mDeleteOldPasswordIv.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		(finishMenuItem = menu.add(0, Menu.NONE, 0, "保存"))
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// 这里要写跳转到框架 然后结束自己.
		if (finishMenuItem == item) {
//			Toast.makeText(getContext(), "点了 完成按钮", 1).show();
			String oldPwdStr = mOldPasswordEt.getText().toString();
			String newPwdStr = mNewPasswordEt.getText().toString();
			if(checkSubmit(oldPwdStr, newPwdStr)){
				String oldPwdStr64 = MBase64.encode(oldPwdStr.getBytes());
				String newPwdStr64 = MBase64.encode(newPwdStr.getBytes());
				// 访问服务器修改密码
				HomeReqUtil.changeUserPwd(getContext(), this, null,newPwdStr64,oldPwdStr64);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 检测输入文本
	 * @param oldPwdStr
	 * @param newPwdStr
	 */
	private boolean checkSubmit(String oldPwdStr, String newPwdStr) {
		if(TextUtils.isEmpty(oldPwdStr) || TextUtils.isEmpty(newPwdStr)){
			showToast("密码不能为空!");
			return false;
		}
		if(newPwdStr.length() < 6){
			showToast("密码不能小于6位!");
			return false;
		}
		if(newPwdStr.length() > 19){
			showToast("密码不能大于19位!");
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.showNewPasswordCb:
			if (mShowNewPasswordCb.isChecked()) {
				mNewPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			} else {
				mNewPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			}
			break;
		case R.id.deleteNewPasswordIv:
			mNewPasswordEt.setText("");
			break;
		case R.id.deleteOldPasswordIv:
			mOldPasswordEt.setText("");
			break;
		case R.id.showOldPasswordCb:
			if (mShowOldPasswordCb.isChecked()) {
				mOldPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
			} else {
				mOldPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		// 修改密碼
		if (tag == EAPIConsts.ReqType.CHANGE_USER_PWD) {
			DataBox dataBox = (DataBox) object;
			if(dataBox!=null && dataBox.isChangeSucceed){
				showToast("修改成功!");
				finish();
			}else{
				showToast("密码修改失败!");
			}
		}
	}
}
