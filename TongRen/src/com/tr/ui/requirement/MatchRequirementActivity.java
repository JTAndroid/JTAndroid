package com.tr.ui.requirement;

import com.tr.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.model.obj.KnowledgeMini;
import com.tr.model.obj.RequirementMini;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.MatchKnowledgeAdapter;
import com.tr.ui.adapter.MatchRequirementAdapter;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.knowledge.MatchKnowledgeActivity;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class MatchRequirementActivity extends JBaseFragmentActivity {
	
	private final String TAG = getClass().getSimpleName();
	
	// 常量
	private final int MENU_ITEM_ID_BASE = 100;
	private final int MENU_ITEM_ID_REFRESH = MENU_ITEM_ID_BASE + 1;
	
	// 控件
	private ListView matchReqLv;
	
	// 变量
	private int mType;
	private int mID;
	private MatchRequirementAdapter mAdapter;
	
	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("匹配的需求");
		jabGetActionBar().setDisplayShowTitleEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_requirement);
		initVars();
		initControls();
		doInit();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // 返回
			break;
		case MENU_ITEM_ID_REFRESH: // 刷新
			doInit();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// 创建菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem shareItem = menu.add(0, MENU_ITEM_ID_REFRESH, 0, "刷新");
		// shareItem.setIcon(R.drawable.ic_action_share);
		shareItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	private void initVars(){
		mType = getIntent().getIntExtra(EConsts.Key.TYPE, 0);
		mID = getIntent().getIntExtra(EConsts.Key.ID, 0);
		mAdapter = new MatchRequirementAdapter(this);
	}
	
	private void initControls(){
		matchReqLv = (ListView) findViewById(R.id.matchReqLv);
		matchReqLv.setAdapter(mAdapter);
		matchReqLv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void doInit(){
		showLoadingDialog();
		UserReqUtil.doGetMatchRequirementMini(this, mBindData, 
				UserReqUtil.getDoGetMatchRequirementMiniParams(mType, mID), null);
	}
	
	private IBindData mBindData = new IBindData(){

		@Override
		public void bindData(int tag, Object object) {
			// TODO Auto-generated method stub
			dismissLoadingDialog();
			if(tag == EAPIConsts.ReqType.GET_MATCH_REQUIREMENT_MINI){
				if(object != null){
					DataBox dataBox = (DataBox) object;
					if(dataBox.mListRequirementMini != null){
						mAdapter.updateAdapter(dataBox.mListRequirementMini);
					}
				}
			}
		}
	};
}
