package com.tr.ui.communities.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.communities.frag.FragOnCreatCommunity;
import com.tr.ui.communities.frag.FragStartCreatCommunity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.GlobalVariable;

public class CreateCommunityAvctivity extends JBaseFragmentActivity {
	private int CREAT_SATATE = 0;// 0 为创建社群 1编辑社群详情信息 默认为0
	private String barTitle = "";
	private long communityId;// 创建成功返回的社群id；

	@Override
	public void initJabActionBar() {
		getBundle();
		initBarTitle();
		setContentView(R.layout.activity_createcommunity);
		initView();
	}

	private void initBarTitle() {
		// TODO Auto-generated method stub
		if (CREAT_SATATE == 0)//
			barTitle = "创建社群";
		else
			barTitle = "编辑社群详情";
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), barTitle, false, null, true, true);
	}


	private void getBundle() {
		// TODO 获取传过的数据
		CREAT_SATATE = getIntent().getIntExtra("CREAT_SATATE", 0);
		communityId = getIntent().getLongExtra(GlobalVariable.COMMUNITY_ID, -1);
	}

	private void initView() {

		if (CREAT_SATATE == 0) {
			FragOnCreatCommunity faOnCreatCommunity = new FragOnCreatCommunity();
			getSupportFragmentManager().beginTransaction().replace(R.id.fragment_oncreat_community, faOnCreatCommunity, "onCreate").commitAllowingStateLoss();
		} else {
			FragStartCreatCommunity startCreatCommunity = new FragStartCreatCommunity();
			Bundle bundle=new Bundle();
			bundle.putLong(GlobalVariable.COMMUNITY_ID, communityId);
			bundle.putInt("CREAT_SATATE", CREAT_SATATE);
			startCreatCommunity.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.fragment_oncreat_community, startCreatCommunity, "startCreate").commitAllowingStateLoss();
		}
		// Bundle bundle = new Bundle();
		// startCreatCommunity.setArguments(bundle);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (null != intent) {
			FragStartCreatCommunity startCreatCommunity = (FragStartCreatCommunity) getSupportFragmentManager().findFragmentByTag("startCreate");
			startCreatCommunity.handerRequsetCode(requestCode, intent);
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}
}
