package com.tr.ui.home;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class PrivacyActivity extends JBaseActivity implements OnClickListener {
	@ViewInject(R.id.look_my_profile)
	private RelativeLayout look_my_profile;// 谁可以看我的资料

	@ViewInject(R.id.add_me_as_friend)
	private RelativeLayout add_me_as_friend;// 谁可以加我为好友

	@ViewInject(R.id.give_me_a_evaluation)
	private RelativeLayout give_me_a_evaluation;// 谁可以给我评价

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_privacy);
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "隐私策略", false, null, false, true);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		look_my_profile.setOnClickListener(this);
		add_me_as_friend.setOnClickListener(this);
		give_me_a_evaluation.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.look_my_profile:// 谁可以看我的资料
			startActivity(new Intent(PrivacyActivity.this, LookMyDataPrivacyActivity.class));
			break;
		case R.id.add_me_as_friend:// 谁可以加我为好友
			startActivity(new Intent(PrivacyActivity.this, AddedfriendPrivacyActivity.class));
			break;
		case R.id.give_me_a_evaluation:// 谁可以给我评价
			startActivity(new Intent(PrivacyActivity.this, EvaluationPrivacyActivity.class));
			break;

		default:
			break;
		}
	}

}
