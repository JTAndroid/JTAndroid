package com.tr.ui.home;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class AddedfriendPrivacyActivity extends JBaseActivity implements OnClickListener {
	@ViewInject(R.id.text_my_friend)
	private TextView text_my_friend;

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_privacy_evaluation);
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "谁可以加我为好友", false, null, false, true);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		text_my_friend.setText("手机通讯录的好友");
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
