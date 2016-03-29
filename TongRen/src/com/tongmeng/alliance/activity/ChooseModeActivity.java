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

/**
 * 选择模版界面，从通知界面跳转过来
 * 
 * @author Administrator
 * 
 */
public class ChooseModeActivity extends JBaseActivity implements
		OnClickListener {

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;
	// 界面
	LinearLayout beforeLayout, afterLayout, downLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosemode);
		initView();
	}

	public void initView() {
		// 界面
		beforeLayout = (LinearLayout) findViewById(R.id.choosemode_beforeLayout);
		afterLayout = (LinearLayout) findViewById(R.id.choosemode_afterLayout);
		downLayout = (LinearLayout) findViewById(R.id.choosemode_downLayout);
		beforeLayout.setOnClickListener(this);
		afterLayout.setOnClickListener(this);
		downLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if (v.getId() == R.id.choosemode_beforeLayout) {
			intent.putExtra("mode", "会前提醒");
			intent.putExtra("code", "1");
		} else if (v.getId() == R.id.choosemode_afterLayout) {
			intent.putExtra("mode", "会后感谢");
			intent.putExtra("code", "2");
		} else if (v.getId() == R.id.choosemode_downLayout) {
			intent.putExtra("mode", "下载APP");
			intent.putExtra("code", "3");
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
		myTitle.setText("选择模版");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setVisibility(View.GONE);
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}
}
