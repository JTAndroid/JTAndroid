package com.tr.ui.common;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.model.obj.RequirementMini;
import com.tr.ui.adapter.RequirementAdapter;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class SearchRequirementActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();
	
	// 常量
	private final int MENU_ITEM_ID_BASE = 100;
	private final int MENU_ITEM_ID_SEARCH = MENU_ITEM_ID_BASE + 2;
	
	// 变量
	private ListView reqLv;
	private RequirementAdapter mAdapter;
	private int mIndex;
	private int mSize;
	private int mTotal;
	
	@Override
	public void initJabActionBar() {
		ActionBar actionBar = this.jabGetActionBar();
		actionBar.setTitle("选择需求");
		actionBar.setDisplayShowTitleEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_requirement);
		initVars();
		initControls();
		doInit();
	}
	
	private void initVars(){
		mIndex = 0;
		mSize = 20;
		mTotal = 0;
		mAdapter = new RequirementAdapter(this);
	}
	
	private void initControls(){
		reqLv = (ListView) findViewById(R.id.reqLv);
		reqLv.setAdapter(mAdapter);
		reqLv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(EConsts.Key.REQUIREMENT_MINI,
						(RequirementMini) mAdapter.getItem(arg2));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
	
	private void doInit(){
		showLoadingDialog();
		UserReqUtil.doGetListRequirement(this, mBindData, 
				UserReqUtil.getDoGetListRequirementParams("", 0, 20), null);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // 返回
			finish();
			break;
		case MENU_ITEM_ID_SEARCH: // 搜索
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem searchItem = menu.add(0, MENU_ITEM_ID_SEARCH, 0, "");
		searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		searchItem.setIcon(R.drawable.jt_dt_search);
		SearchView sv = new SearchView(this);
		sv.setOnQueryTextListener(mQueryTextListener);
		searchItem.setActionView(sv);
		return true;
	}
	
	private OnQueryTextListener mQueryTextListener = new OnQueryTextListener(){

		@Override
		public boolean onQueryTextSubmit(String query) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			// TODO Auto-generated method stub
			UserReqUtil.doGetListRequirement(SearchRequirementActivity.this, mBindData, 
					UserReqUtil.getDoGetListRequirementParams(newText, 0, 20), null);
			return false;
		}
	};
	
	private IBindData mBindData = new IBindData(){

		@Override
		public void bindData(int tag, Object object) {
			// TODO Auto-generated method stub
			dismissLoadingDialog();
			if(tag == EAPIConsts.ReqType.GET_LIST_REQUIREMENT){
				if(object != null){
					DataBox dataBox = (DataBox) object;
					mIndex = dataBox.mIndex;
					mSize = dataBox.mSize;
					mTotal = dataBox.mTotal;
					mAdapter.updateAdapter(dataBox.mListRequirementMini);
				}
			}
		}
	};
}
