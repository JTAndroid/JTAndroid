package com.tongmeng.alliance.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class AccountUserTypeActivity extends JBaseActivity implements OnClickListener{

	TextView apply_type, weixin_type;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.apply_type:
			Intent intent = new Intent(AccountUserTypeActivity.this,
					AccountUserDetailActivity.class);
			intent.putExtra("accountType", "支付宝");
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.weixin_type:
			Intent intent1 = new Intent(AccountUserTypeActivity.this,
					AccountUserDetailActivity.class);
			intent1.putExtra("accountType", "微信");
			setResult(RESULT_OK, intent1);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.accountusertype);
		initTitle();
		intiView();
	}

	private void intiView() {
		// TODO Auto-generated method stub
		apply_type = (TextView) findViewById(R.id.apply_type);
		weixin_type = (TextView) findViewById(R.id.weixin_type);
		apply_type.setOnClickListener(this);
		weixin_type.setOnClickListener(this);
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "收款账户类型",
				false, null, false, true);
	}

}
