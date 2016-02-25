package com.tr.ui.organization.create_clientele;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.ui.demand.MyView.MyGridView;
import com.tr.ui.organization.orgdetails.EditIndustryTrendActivity;
import com.tr.ui.organization.orgdetails.HightSubjectActivity;
import com.tr.ui.people.cread.BaseActivity;
/**
 * 行业动态
 * @author Wxh07151732
 *
 */
public class IndustryTrendsActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout rl_trend;
	private ImageView edit_industry_iv;
	private Intent intent;
	private RelativeLayout quit_industry_trends_Rl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_industry_trends);
		rl_trend = (RelativeLayout) findViewById(R.id.rl_trend);
		rl_trend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent(IndustryTrendsActivity.this, HightSubjectActivity.class);
				startActivity(intent);
			}
		});
		edit_industry_iv = (ImageView) findViewById(R.id.edit_industry_iv);
		quit_industry_trends_Rl = (RelativeLayout) findViewById(R.id.quit_industry_trends_Rl);
		quit_industry_trends_Rl.setOnClickListener(this);
	}
	public void edit(View v){
		intent = new Intent(IndustryTrendsActivity.this, EditIndustryTrendActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.rl_trend:
//			intent = new Intent(Industry_trends_Activity.this, Hight_Subject_Activity.class);
//			startActivity(intent);
//			break;
//		case R.id.edit_industry_iv:
//			intent = new Intent(Industry_trends_Activity.this, Edit_IndustryTrend_Activity.class);
//			startActivity(intent);
//			break;
		case R.id.quit_industry_trends_Rl:
			finish();
			break;
		default:
			break;
		}
	}
	
	
}
