package com.tr.ui.home;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class MydataPrivacyActivity extends JBaseActivity implements OnClickListener {

	@Override
	public void initJabActionBar() {
		getBundle();
		setContentView(R.layout.activity_my_data_privacy);
		ViewUtils.inject(this);
	}

	private void getBundle() {
		String title=getIntent().getStringExtra("title");
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), title, false, null, false, true);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

}
