package com.tr.ui.user.modified;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tr.R;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class ResetPasswordSuccessActivity extends JBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_success_reset_password);

		Button btn = (Button) findViewById(R.id.sign_in);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ENavigate.startLoginActivity(ResetPasswordSuccessActivity.this, null);
				finish();
			}
		});
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "重置密码", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

}
