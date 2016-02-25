package com.tr.ui.tongren.home;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.adapter.TongRenFrgPagerAdapter;
import com.tr.ui.tongren.home.fragment.TaskDetailfragment;
import com.tr.ui.tongren.model.task.Task;
import com.tr.ui.widgets.CustomViewPager;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.TongRenRequestType.TongRenInfoType;

public class TaskDetailsActivity extends JBaseFragmentActivity {

	private Task task;
	private TaskDetailfragment taskDetailfragment;
	private ArrayList<Fragment> taskDetailfragments = new ArrayList<Fragment>();
	private CustomViewPager taskDetailsVPager;
	private String projectName;
	private TongRenInfoType tongRenInfoType;
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "任务详情", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskdetails);
		task = (Task) getIntent().getSerializableExtra("TaskDetailsData");
		projectName = getIntent().getStringExtra("projectName");
		tongRenInfoType = (TongRenInfoType) getIntent(). getSerializableExtra(EAPIConsts.TongRenRequestType.TongRenInfoType);
		initView();
	}
	private void initView() {
		 taskDetailsVPager = (CustomViewPager) findViewById(R.id.taskDetailsVPager);
		 taskDetailfragment = new TaskDetailfragment(task,projectName,tongRenInfoType);
		 taskDetailfragments.add(taskDetailfragment);
		 TongRenFrgPagerAdapter pagerAdapter = new TongRenFrgPagerAdapter(getSupportFragmentManager(),taskDetailfragments);
		 taskDetailsVPager.setAdapter(pagerAdapter);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode==1001) {
			taskDetailfragment.onActivityResult(requestCode, resultCode, intent);
		}
	}
}
