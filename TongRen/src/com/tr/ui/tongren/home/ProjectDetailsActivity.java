package com.tr.ui.tongren.home;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.adapter.ProjectAdapter.ProjectType;
import com.tr.ui.tongren.adapter.TongRenFrgPagerAdapter;
import com.tr.ui.tongren.home.fragment.ProjectDetailfragment;
import com.tr.ui.widgets.CustomViewPager;

public class ProjectDetailsActivity extends JBaseFragmentActivity {
	private ArrayList<Fragment> projectDetailfragments = new ArrayList<Fragment>();
	private String projectId = "";
	private String organizationId;
	private ProjectType projectType;
	private ProjectDetailfragment projectDetailfragment;
	private String organizationName;
	private int status;
	private String publisherId;
	private String recipientName;
	private boolean isApply;
	private String publisherName;
	private long recipientTime;
	private String projectAcceptId;
	private int applysum;
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "项目详情", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projectdetails);
		projectId  = getIntent().getStringExtra("projectId");
		organizationId = getIntent().getStringExtra("organizationId");
		publisherId = getIntent().getStringExtra("publisherId");
		projectType = (ProjectType) getIntent().getSerializableExtra("projectType");
		status =   getIntent().getIntExtra("status", -2);
		recipientName =  getIntent().getStringExtra("recipientName");
		projectAcceptId = getIntent().getStringExtra("projectAcceptId");
		publisherName = getIntent().getStringExtra("publisherName");
		recipientTime = getIntent().getLongExtra("recipientTime", 0);
		applysum = getIntent().getIntExtra("applysum", 0);
		initView();
	}
	private void initView() {
		 CustomViewPager projectDetailsVPager = (CustomViewPager) findViewById(R.id.projectDetailsVPager);
		 projectDetailfragment = new ProjectDetailfragment(projectId,organizationId,projectType,recipientName,publisherName,status,publisherId,recipientTime,projectAcceptId,applysum);
		 projectDetailfragments.add(projectDetailfragment);
		 TongRenFrgPagerAdapter pagerAdapter = new TongRenFrgPagerAdapter(getSupportFragmentManager(),projectDetailfragments);
		 projectDetailsVPager.setAdapter(pagerAdapter);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (intent!=null) {
		if (requestCode>=1000&&requestCode<1100) {
		projectDetailfragment.onActivityResult(requestCode, resultCode, intent);
		}
		}
	}
}
