package com.tr.ui.demand;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;

/**
 * @ClassName: LimitsActivity.java
 * @author zcs
 * @Date 2015年3月17日 下午1:36:17
 * @Description: 权限控制
 */
public class LimitsActivity extends JBaseActivity implements OnClickListener {
	private LinearLayout itemLl;
	private ArrayList<String> listStr = new ArrayList<String>();// 所有的item
	private HashMap<String, String> listStred;// 已经选择的字段
	ArrayList<String> customList;// 自定义的字段
	private MyOnClick myOnClick;
	private View rightCb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_limits);
		itemLl = (LinearLayout) findViewById(R.id.itemLl);
		findViewById(R.id.relevanceLl).setOnClickListener(this);
		rightCb = findViewById(R.id.rightCb);// 关联
		myOnClick = new MyOnClick();
		getParam();
		initData();
	}

	/**
	 * 获取上级传递 的数据
	 */
	private void getParam() {
		Intent intent = getIntent();
		listStred = (HashMap<String, String>) intent
				.getSerializableExtra(ENavConsts.DEMAND_PERMISSION_DATA); // 选中的字段
		if (listStred == null) {
			listStred = new HashMap<String, String>();// 没有选中的对象
		}
		customList = intent
				.getStringArrayListExtra(ENavConsts.DEMAND_PERMISSION_CUSTOM);// 自定义的字段
	}

	private void initList() {
		for (String str : NewDemandActivity.limits) {
			listStr.add(str);
		}
		listStr.remove("关联");//去除关联信息
	}

	private void initData() {
		itemLl.removeAllViews();
		initList();
		if (customList != null) {
			for (String custom : customList) {
				listStr.add(custom);
			}
		}
		for (String str : listStr) {
			View view = View.inflate(LimitsActivity.this,
					R.layout.demand_choose_item, null);
			view.findViewById(R.id.rightCb).setSelected(
					listStred.get(str) != null ? true : false);// 如果已经选中，显示选中状态
			view.setOnClickListener(myOnClick);
			view.setTag(str);
			TextView tv = (TextView) view.findViewById(R.id.nameTv);
			view.findViewById(R.id.rightIv).setVisibility(View.GONE);
			view.findViewById(R.id.rightCb).setVisibility(View.VISIBLE);
			tv.setText(str);
			itemLl.addView(view);
		}
		rightCb.setSelected(listStred.get("关联") != null ? true : false);
	}

	// 点击条目事件
	class MyOnClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			View imageV = v.findViewById(R.id.rightCb);
			String tag = (String) v.getTag();
			if (imageV.isSelected()) {
				imageV.setSelected(false);
				listStred.remove(tag);
			} else {
				imageV.setSelected(true);
				listStred.put(tag, tag);
			}
		}

	}

	@Override
	public void initJabActionBar() {
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(true);
		final View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		TextView myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		mCustomView.findViewById(R.id.titleIv).setVisibility(View.GONE);
		myTitle.setText("选择权");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_limits_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.save_limits:// 完成
			Intent intent = new Intent();
			intent.putExtra(ENavConsts.DEMAND_PERMISSION_DATA, listStred);
			setResult(Activity.RESULT_OK, intent);
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relevanceLl:// 关联
			if (rightCb.isSelected()) {
				rightCb.setSelected(false);
				listStred.remove("关联");
			} else {
				rightCb.setSelected(true);
				listStred.put("关联", "关联");
			}
			break;

		default:
			break;
		}
	}
}
