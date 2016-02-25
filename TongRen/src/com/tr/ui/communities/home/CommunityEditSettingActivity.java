package com.tr.ui.communities.home;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.log.ToastUtil;
import com.utils.string.StringUtils;

public class CommunityEditSettingActivity extends JBaseActivity{

	private ActionBar jabGetActionBar;
	private EditText settingEditEt;
	private ImageView delete_settingEditIv;

	@Override
	public void initJabActionBar() {
		jabGetActionBar = jabGetActionBar();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_editsettting);
		initView();
		initData();
	}
	private void initData() {
		Intent intent = getIntent();
		String communityEditSettingData = intent.getStringExtra("communityEditSettingData");
		String communityEditSettingTitle = intent.getStringExtra("communityEditSettingTitle");
		if (!TextUtils.isEmpty(communityEditSettingData)) {
			settingEditEt.setText(communityEditSettingData);
			delete_settingEditIv.setVisibility(View.VISIBLE);
		}
		if (!TextUtils.isEmpty(communityEditSettingTitle)) {
			HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, communityEditSettingTitle, false, null, true, true);
		}
		settingEditEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (!StringUtils.isEmpty(s.toString())) {
					delete_settingEditIv.setVisibility(View.VISIBLE);
				} else {
					delete_settingEditIv.setVisibility(View.GONE);
				}
			}
		});
		delete_settingEditIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				settingEditEt.setText("");
			}
		});
	}
	private void initView() {
		settingEditEt = (EditText) findViewById(R.id.settingEditEt);
		delete_settingEditIv = (ImageView) findViewById(R.id.delete_settingEditIv);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_createflow, menu);
		MenuItem finish = menu.findItem(R.id.flow_create);
		finish.setTitle("完成");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.flow_create:
			if (TextUtils.isEmpty(settingEditEt.getText().toString().trim())) {
				ToastUtil.showToast(this, "不可为空");
				return false;
			}
			Intent intent = new Intent();
			intent.putExtra("communityEditSettingData", settingEditEt.getText().toString());
			setResult(RESULT_OK, intent);
			finish();
			break;
		case android.R.id.home:
			finish();
			break;
		}
		return true;
		}
	
}
