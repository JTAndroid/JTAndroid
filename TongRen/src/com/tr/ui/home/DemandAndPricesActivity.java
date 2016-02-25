package com.tr.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class DemandAndPricesActivity extends JBaseActivity {

	private RelativeLayout demand;
	private RelativeLayout prices;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_demand_prices_item);
		initControl();
	}

	private void initControl() {
		demand = (RelativeLayout)findViewById(R.id.demand);
		demand.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ENavigate.startFindPricesActivity(DemandAndPricesActivity.this,2);
			}
		});
		prices = (RelativeLayout)findViewById(R.id.prices);
		prices.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ENavigate.startFindProjectActivity(DemandAndPricesActivity.this,1);
			}
		});
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "需求", false,null,true, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);

	}
	
	
	
	

}
