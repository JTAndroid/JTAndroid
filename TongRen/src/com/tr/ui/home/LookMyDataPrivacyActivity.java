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
import com.tr.ui.user.UserSettingActivity;
/**
 * 谁可以看我的资料
 * @author cui
 *
 */
public class LookMyDataPrivacyActivity extends JBaseActivity implements OnClickListener {
	@ViewInject(R.id.self_info)
	private RelativeLayout self_info;// 个人电话、详细地址及邮箱

	@ViewInject(R.id.which_org)
	private RelativeLayout which_org;// 所在组织

	@ViewInject(R.id.member_information)
	private RelativeLayout member_information;// 会员信息

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_look_my_data_privacy);
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "谁可以看我的资料", false, null, false, true);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		self_info.setOnClickListener(this);
		which_org.setOnClickListener(this);
		member_information.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(LookMyDataPrivacyActivity.this, MydataPrivacyActivity.class);
		switch (v.getId()) {
		case R.id.self_info:// 个人电话、详细地址及邮箱
			intent.putExtra("title", "个人电话、详细地址及邮箱");
			startActivity(intent);
			break;
		case R.id.which_org:// 所在组织
			intent.putExtra("title", "所在组织");
			startActivity(intent);
			break;
		case R.id.member_information:// 会员信息
			intent.putExtra("title", "会员信息(认证信息、等级)");
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
