package com.tongmeng.alliance.activity;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

public class ChooseObjActivity extends JBaseActivity implements OnClickListener{

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;
	// 界面
			LinearLayout signedLayout, nosignLayout, allLayout;
			TextView signedText,nosignText,allText;
	
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				// TODO Auto-generated method stub
				super.onCreate(savedInstanceState);
				setContentView(R.layout.chooseobj);
				initView();
			}
			
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if(v.getId() == R.id.chooseobj_signedLayout){
			intent.putExtra("obj", "未签到");
			intent.putExtra("code", "1");
		}else if(v.getId() == R.id.chooseobj_nosignLayout){
			intent.putExtra("obj", "签到");
			intent.putExtra("code", "2");
		}else if(v.getId() == R.id.chooseobj_allLayout){
			intent.putExtra("obj", "全部");
			intent.putExtra("code", "0");
		}
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		KeelLog.e("ContactsMainPageActivity", "initJabActionBar");
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.org_firstpage_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("发送对象");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setVisibility(View.GONE);
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}

	public void initView() {

		// 界面
		signedLayout = (LinearLayout) findViewById(R.id.chooseobj_signedLayout);
		nosignLayout = (LinearLayout) findViewById(R.id.chooseobj_nosignLayout);
		allLayout = (LinearLayout) findViewById(R.id.chooseobj_allLayout);
		signedText = (TextView) findViewById(R.id.chooseobj_signedText);
		nosignText = (TextView) findViewById(R.id.chooseobj_nosignText);
		allText = (TextView) findViewById(R.id.chooseobj_allText);
		signedLayout.setOnClickListener(this);
		nosignLayout.setOnClickListener(this);
		allLayout.setOnClickListener(this);
	}
}
