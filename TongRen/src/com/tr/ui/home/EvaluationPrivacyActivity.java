package com.tr.ui.home;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class EvaluationPrivacyActivity extends JBaseActivity implements OnClickListener {

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_privacy_evaluation);
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "谁可以给我评价", false, null, false, true);
		ViewUtils.inject(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		default:
			break;
		}
	}

}
