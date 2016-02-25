package com.tr.ui.knowledge;

import java.util.ArrayList;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.model.knowledge.Column;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.people.homepage.ContactsMainPageActivity;
import com.tr.ui.widgets.KnoColumnPopupWindow;
import com.tr.ui.widgets.KnoColumnPopupWindow.OnOperateListener;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.IBindData;
import com.viewpagerindicator.TabPageIndicator;
//import java.util.List;

/**
 * 知识广场
 * @author leon
 */
public class KnowledgeSquareActivity extends JBaseFragmentActivity implements IBindData, OnOperateListener {
	
	private final String TAG = getClass().getSimpleName();
	
	public static final int REQUEST_CODE_GLOBAL_KNOWLEDGE_COLUMN_ACTIVITY = 1001;
	
	private Context context;
	private ArrayList<KnowledgeBaseFragment> knowledgeBaseFragments;//
	private TabPageIndicator tabTpi; // 指示条
	private ViewPager contentVp; // 内容
	private ImageView columnIv; // 栏目按钮
//	private RelativeLayout editParentRl;
//	private TextView editHintTv;  //编辑提示
//	private TextView editTv;  // 编辑/完成按钮
	private KnoColumnPopupWindow columnPw;
	private ActionBar actionBar;
	private View topView; 
	
	
	private KnowledgeModulePagerAdapter knowledgeModulePagerAdapter;
	private ArrayList<Column> listColumn;
                  
	@Override
	public void initJabActionBar() {
//		actionBar = jabGetActionBar();
//		actionBar.setTitle("知识");
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(R.layout.org_firstpage_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		TextView myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		TextView create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ENavigate.startCreateKnowledgeActivity(context);
			}
		});
		myTitle.setText("知识");
		ImageView search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ENavigate.startNewSearchActivity(KnowledgeSquareActivity.this,3);
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_knowledge_square);
		context = this;
		initComponent();
//		initSimulateData();
		initData();
		
	}
	
	private void initComponent(){
		topView = findViewById(R.id.topView);
		knowledgeBaseFragments = new ArrayList<KnowledgeBaseFragment>();
		tabTpi = (TabPageIndicator) findViewById(R.id.indicatorTpi);
		contentVp = (ViewPager) findViewById(R.id.contentVp);
		columnIv = (ImageView) findViewById(R.id.columnIv);
		columnIv.setOnClickListener(mClickListener);
		
		knowledgeModulePagerAdapter = new KnowledgeModulePagerAdapter(getSupportFragmentManager(), knowledgeBaseFragments);
		contentVp.setAdapter(knowledgeModulePagerAdapter);
		tabTpi.setViewPager(contentVp);
		
//		editParentRl = (RelativeLayout) findViewById(R.id.editParentRl);
//		editHintTv = (TextView) findViewById(R.id.editHintTv);
//		editTv = (TextView) findViewById(R.id.editTv);
//		editTv.setOnClickListener(mClickListener);
		
		columnPw = new KnoColumnPopupWindow(this, listColumn);
		columnPw.setOnOperateListener(this);
	}
	
	
	/*
	private void initSimulateData(){
		ArrayList<String> simulateColumnNames = new ArrayList<String>();
		simulateColumnNames.add("基本信息");
		simulateColumnNames.add("联系方式");
		simulateColumnNames.add("个人情况");
		simulateColumnNames.add("投资意向");
		simulateColumnNames.add("基本信息");
		simulateColumnNames.add("联系方式");
		simulateColumnNames.add("个人情况");
		simulateColumnNames.add("投资意向");
		simulateColumnNames.add("基本信息");
		simulateColumnNames.add("联系方式");
		
//		for (int i = 0; i < simulateColumnNames.size(); i++) {
//			KnowledgeBaseFragment knowledgeBaseFragment = new KnowledgeBaseFragment();
//			knowledgeBaseFragment.setFragmentName(simulateColumnNames.get(i));
//			knowledgeBaseFragments.add(knowledgeBaseFragment);
//		}
//		
		knowledgeModulePagerAdapter.setKnowledgeBaseFragments(knowledgeBaseFragments);
		knowledgeModulePagerAdapter.notifyDataSetChanged();
		tabTpi.notifyDataSetChanged();
	}
	*/
	
	private void initData(){
		KnowledgeReqUtil.doGetSubscribedColumnByUserId(context, this, App.getUserID() , null);
		
	}
	
	
	class KnowledgeModulePagerAdapter extends FragmentStatePagerAdapter {
		
		private ArrayList<KnowledgeBaseFragment> knowledgeBaseFragments;//

		public KnowledgeModulePagerAdapter(FragmentManager fm, ArrayList<KnowledgeBaseFragment> knowledgeBaseFragments) {
			super(fm);
			if( knowledgeBaseFragments != null ){
				this.knowledgeBaseFragments = knowledgeBaseFragments;  
			}
			else {
				this.knowledgeBaseFragments = new ArrayList<KnowledgeBaseFragment>();
			}
		}
		
		public ArrayList<KnowledgeBaseFragment> getKnowledgeBaseFragments() {
			return knowledgeBaseFragments;
		}

		public void setKnowledgeBaseFragments(ArrayList<KnowledgeBaseFragment> knowledgeBaseFragments) {
			this.knowledgeBaseFragments = knowledgeBaseFragments;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return knowledgeBaseFragments.get(position).getFragmentName();
		}
		
		/**
		 * 根据Title返回对应的Fragment
		 */
		@Override
		public Fragment getItem(int arg0) {
			return knowledgeBaseFragments.get(arg0);
		}

		@Override
		public int getCount() {
			return knowledgeBaseFragments.size();
		}
		
		 @Override
		 public Object instantiateItem(ViewGroup container, int position) {
			 KnowledgeBaseFragment fragment = (KnowledgeBaseFragment) super.instantiateItem(container, position);
			 fragment.setColumn(listColumn.get(position));
		     return fragment;
		 }
		 
		 @Override
		 public int getItemPosition(Object object) {
		     return POSITION_NONE;
		 }
	}
	
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			if( columnIv == v ){ // 栏目按钮
				columnPw.showAsDropDown(topView);
			}
		}
	};
	
	
	

	@Override
	public void onColumnSelect(Column column) {
		String MSG = "onColumnSelect()";
		int index = listColumn.indexOf(column);
		contentVp.setCurrentItem(index);
	}

	// 订阅新栏目
	@Override
	public void onAddColumn() {
		Intent intent = new Intent(KnowledgeSquareActivity.this, GlobalKnowledgeColumnActivity.class);
		intent.putExtra("listColumn", listColumn);
		startActivityForResult(intent, REQUEST_CODE_GLOBAL_KNOWLEDGE_COLUMN_ACTIVITY);
	}

	@Override
	public void onStopEdit() {
//		updateEditModeText();
		updateAllUiData();
	}
	
	
	@Override
	public void onStartEdit() {
//		updateEditModeText();
	}
	

	@Override
	public void bindData(int tag, Object object) {
		
		if( object == null ){
			return;
		}
		
		if( KnoReqType.GetSubscribedColumnByUserId == tag ){
			Map<String, Object> hm = (Map<String, Object>) object;
			listColumn = (ArrayList<Column>) hm.get("listColumn");
			updateAllUiData();
			
		}
		
	}
	
	
	void updateAllUiData(){
		knowledgeBaseFragments.clear();
		if(listColumn != null){
			for (int i = 0; i < listColumn.size(); i++) {
				Column column = listColumn.get(i);
				KnowledgeBaseFragment knowledgeBaseFragment = new KnowledgeBaseFragment();
				knowledgeBaseFragment.setColumn(column);
				knowledgeBaseFragments.add(knowledgeBaseFragment);
			}
		}
		
		knowledgeModulePagerAdapter.setKnowledgeBaseFragments(knowledgeBaseFragments);
		knowledgeModulePagerAdapter.notifyDataSetChanged();
		tabTpi.notifyDataSetChanged();
		
		//更新 选择栏目对话框 数据
		columnPw.updateColumnData(listColumn);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
		if( REQUEST_CODE_GLOBAL_KNOWLEDGE_COLUMN_ACTIVITY == requestCode ){
			if (Activity.RESULT_OK == resultCode){
				listColumn = (ArrayList<Column>) intent.getSerializableExtra("listSubscribedColumn");
				updateAllUiData();
			}
		}
		
	}

	
	
}
