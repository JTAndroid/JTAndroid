package com.tongmeng.alliance.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class MysettingActivity extends JBaseActivity implements OnClickListener{
	
	RelativeLayout actionLayout,notesLayout,accountLayout;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.myactivity:
		Intent actionIntent = new Intent(this,MyActionsActivity.class);
		startActivity(actionIntent);
		break;
	case R.id.mynote:
		Intent notesIntent = new Intent(this,MyNotesActivity.class);
		startActivity(notesIntent);
		break;
	case R.id.mycustomer:
		Intent accountIntent = new Intent(this,AccountUserSetActivity.class);
		startActivity(accountIntent);
		break;

	default:
		break;
	}	
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.mysetting);
		initTitle();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		actionLayout = (RelativeLayout) findViewById(R.id.myactivity);
		notesLayout = (RelativeLayout) findViewById(R.id.mynote);
		accountLayout = (RelativeLayout) findViewById(R.id.mycustomer);
		actionLayout.setOnClickListener(this);
		notesLayout.setOnClickListener(this);
		accountLayout.setOnClickListener(this);
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "我的活动",
				false, null, false, true);
	}
	
}
