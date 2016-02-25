package com.tr.ui.user.modified;

import android.app.ActionBar;
import android.os.Bundle;

import com.tr.App;
import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

/**
 * 绑定邮箱
 * 
 * @author Administrator
 * 
 */
public class BindingEmailActivity extends JBaseFragmentActivity {

	private int type;// 为1 表示直接绑定邮箱  2是解绑旧的后绑定新的

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "绑定邮箱", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_binding_email);
		type = getIntent().getIntExtra("type", 1);
		if(2==type){
			HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "更改邮箱", false, null, false, true);
		}
		initViews();

	}

	private void initViews() {
		BindingEmailFragment emailFrag = new BindingEmailFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		emailFrag.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_bind_email, emailFrag)
				.commitAllowingStateLoss();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		App.getApp().refresh_accountInfo = true;
	}
}
