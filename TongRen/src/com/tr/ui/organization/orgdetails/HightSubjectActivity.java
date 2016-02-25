package com.tr.ui.organization.orgdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.tr.R;
import com.tr.ui.people.cread.BaseActivity;

public class HightSubjectActivity extends BaseActivity implements OnClickListener{

	private ImageView iv_industry_edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_industry_item);
		iv_industry_edit = (ImageView) findViewById(R.id.iv_industry_edit);
	 }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_industry_edit:
			Intent intent = new Intent(getApplicationContext(), EditIndustryTrendActivity.class);
			startActivity(intent);
			break;
			
		default:
			break;
		}
	}
}
