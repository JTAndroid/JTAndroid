package com.tr.ui.flow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.obj.DynamicLocation;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.flow.frg.FrgFlow;
import com.tr.ui.home.utils.HomeCommonUtils;

public class FlowLocationActivity extends JBaseFragmentActivity {

	private TextView company;
	private TextView companyType;
	private TextView address;
	private TextView mobile;
	private LinearLayout mobile_ll;
	DynamicLocation location;
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "位置详情", false, null, false, true);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_flow_location);
		initView();
		initData();
	}
	
	private void initView(){
		company = (TextView) findViewById(R.id.company);
		companyType = (TextView) findViewById(R.id.companyType);
		address = (TextView) findViewById(R.id.address);
		mobile = (TextView) findViewById(R.id.mobile);
		mobile_ll = (LinearLayout) findViewById(R.id.mobile_ll);
	}
	
	private void initData(){
		location = (DynamicLocation) getIntent().getSerializableExtra("location");
		company.setText(location.getName());
		companyType.setText(location.getType());
		address.setText(location.getDetailName());
		if(TextUtils.isEmpty(location.getMobile())){
			mobile_ll.setVisibility(View.GONE);
		}else{
			mobile_ll.setVisibility(View.VISIBLE);
			mobile.setText(location.getMobile());
		}

		address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FlowLocationActivity.this, FlowMapActivity.class);
				intent.putExtra("location", location);
				startActivity(intent);
			}
		});
	}
	
}
