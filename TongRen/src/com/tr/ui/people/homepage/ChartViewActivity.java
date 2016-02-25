package com.tr.ui.people.homepage;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.homepage.frg.FrgChartView;

public class ChartViewActivity extends JBaseFragmentActivity {

	private FrgChartView frgChartView;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "用户图谱", false, null, false, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_flow);

		frgChartView = new FrgChartView();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.fragment_conainer, frgChartView, "FrgChartView");
		transaction.commitAllowingStateLoss();
	}
}
