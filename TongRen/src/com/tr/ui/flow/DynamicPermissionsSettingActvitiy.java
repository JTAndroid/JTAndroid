package com.tr.ui.flow;

import android.app.ActionBar;
import android.os.Bundle;

import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class DynamicPermissionsSettingActvitiy extends JBaseActivity{

	private String userid;
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "谁可以看", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_whocan);
		userid = getIntent().getStringExtra("Userid");
	}
}
