package com.tr.ui.conference.initiatorhy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.api.DemandReqUtil;
import com.tr.api.KnowledgeReqUtil;
import com.tr.db.ConnectionsDBManager;
import com.tr.db.DBHelper;
import com.tr.model.demand.NeedItemData;
import com.tr.model.demand.NeedItemListItem;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTContact2;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.RequirementMini;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.conference.ListViewSearchAdapter;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.conference.im.MeetingChatActivity;
import com.tr.ui.conference.initiatorhy.search.SearchEditWatcher;
import com.tr.ui.conference.initiatorhy.search.SearchEditWatcher.OnSearchListener;
import com.tr.ui.conference.initiatorhy.search.SearchUtil;
import com.tr.ui.widgets.hy.PagerSlidingTabStrip;
import com.tr.ui.widgets.hy.PagerSlidingTabStrip.OnSlidePagerChangeListener;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.time.Util;

/**
 * 分享选择页面
 * @author leon
 */
public class ShareActivity extends JBaseFragmentActivity implements IBindData, OnSearchListener, OnSlidePagerChangeListener {

	private final String TAG = getClass().getSimpleName();

	public final static int TAB_PEOPLEHUB = 0;
	public final static int TAB_DEMAND = 1;
	public final static int TAB_KNOWLEADGE = 2;
	public final static int TAB_ORGANIZATION = 3;

	private SharePeopleHubFragment peoplehubFragment;
	private SharePeopleHubFragment orghubFragment;
	private ShareDemandFragment demandFragment;
	private ShareKnowleadgeFragment knowleadgeFragment;
	private PagerSlidingTabStrip mainTabs;
	private ViewPager viewPager;
	private DisplayMetrics dm;
	private int tabType;
	int currFinishNum = 0;

	private EditText searchEdit;
	private SearchEditWatcher searchEditWatcher;
	private ListView searchLv;
	private ListViewSearchAdapter searchLvAdp;
	private boolean isSelectedInSearchList;
	// add by leon
	private String fromActivity;
	private int resType;
	private ConnectionsDBManager connsDBManager;
	private ConnectionsUpdateReceiver connsUpdateReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hy_activity_share);
		init();
	}

	private void init() {
		Bundle b = getIntent().getExtras();
		tabType = b.getInt(Util.IK_VALUE);
		
		// 判断需要加载的数据库
		SharedPreferences sp = getSharedPreferences(EConsts.share_firstLoginGetConnections, Activity.MODE_PRIVATE);
		String tableName = sp.getString(EConsts.share_itemUserTableName, "");
		if (TextUtils.isEmpty(tableName)) {
			connsDBManager = new ConnectionsDBManager(this, DBHelper.TABLE_APP_CONNECTIONS);
		}
		else if (tableName.equals(DBHelper.TABLE_APP_CONNECTIONS)) {
			connsDBManager = new ConnectionsDBManager(this, DBHelper.TABLE_APP_CONNECTIONS);
		} 
		else if (tableName.equals(DBHelper.TABLE_APP_CONNECTIONS_BACK)) {
			connsDBManager = new ConnectionsDBManager(this, DBHelper.TABLE_APP_CONNECTIONS_BACK);
		} 
		else {
			connsDBManager = new ConnectionsDBManager(this, DBHelper.TABLE_APP_CONNECTIONS);
		}

		// add by leon
		// 选取资源的参数
		fromActivity = getIntent().getStringExtra(ENavConsts.EFromActivityName);
		resType = getIntent().getIntExtra(ENavConsts.EShareParam, 0);

		findAndInitTitleViews();
		findAndInitTabViews();
		findAndInitSearchbViews();

		// 设置当前Tab
		if (!TextUtils.isEmpty(fromActivity) && fromActivity.equals(MeetingChatActivity.class.getSimpleName())) {
			switch (resType) {
			case 1: // 需求
				 tabType = 1;
				viewPager.setCurrentItem(1);
				if (!Util.isNull(InitiatorDataCache.getInstance().shareDemandList)) {
					InitiatorDataCache.getInstance().shareDemandList.clear();
				}
				startGetDemandList(0);
				break;
			case 2: // 知识
				 tabType = 2;
				viewPager.setCurrentItem(2);
				if (!Util.isNull(InitiatorDataCache.getInstance().shareKnowleadgeList)) {
					InitiatorDataCache.getInstance().shareKnowleadgeList.clear();
				}
				startGetKnowleadgeList(0);
				break;
			case 3: // 关系
				 tabType = 0;
				viewPager.setCurrentItem(0);
				if (!Util.isNull(InitiatorDataCache.getInstance().sharePeoplehubList)) {
					InitiatorDataCache.getInstance().sharePeoplehubList.clear();
				}
				startGetConnections();
				break;
			default:
				viewPager.setCurrentItem(0);
				break;
			}
			// 隐藏Tab
			mainTabs.setVisibility(View.GONE);
			return;
		}

		if (Util.isNull(InitiatorDataCache.getInstance().connectionsList) 
				|| Util.isNull(InitiatorDataCache.getInstance().sharePeoplehubList)) {
			startGetConnections();
		}
		if (Util.isNull(InitiatorDataCache.getInstance().shareOrghubList)) {
			startGetOrgConnections();
		}
		if (Util.isNull(InitiatorDataCache.getInstance().shareDemandList)) {
			startGetDemandList(0);
		}
		if (Util.isNull(InitiatorDataCache.getInstance().shareKnowleadgeList)) {
			startGetKnowleadgeList(0);
		}
		// 关系更新广播接收器
		connsUpdateReceiver = new ConnectionsUpdateReceiver();
		// 请求获取人脉列表服务
//		ENavigate.startGetConnectionsListService(this);
		
		int firstLogin = sp.getInt(EConsts.share_itemFirstLogin, 0);
		// 第一次登陆 (firstLogin == 0 为第一次登录)
		if (firstLogin == 0) {
			showLoadingDialog();
		}
	}

	private void findAndInitTitleViews() {
		LinearLayout backBtn = (LinearLayout) findViewById(R.id.hy_layoutTitle_backBtn);
		TextView title = (TextView) findViewById(R.id.hy_layoutTitle_title);
		TextView rightTextBtn = (TextView) findViewById(R.id.hy_layoutTitle_rightTextBtn);
		title.setText("分享");
		if (!TextUtils.isEmpty(fromActivity) && fromActivity.equals(MeetingChatActivity.class.getSimpleName())) {
			switch (resType) {
			case 1: // 需求
				title.setText("分享需求");
				break;
			case 2: // 知识
				title.setText("分享知识");
				break;
			case 3: // 关系
				title.setText("分享关系");
				break;
			default:
				break;
			}
		}
		rightTextBtn.setText("确定");
		backBtn.setOnClickListener(new MyOnClickListener());
		rightTextBtn.setOnClickListener(new MyOnClickListener());
	}

	private void findAndInitTabViews() {
		setOverflowShowAlways();
		dm = getResources().getDisplayMetrics();
		mainTabs = (PagerSlidingTabStrip) findViewById(R.id.hy_actShare_tabs);
		viewPager = (ViewPager) findViewById(R.id.hy_actShare_viewPager);
		viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mainTabs.setViewPager(viewPager);
		mainTabs.setCurrentItem(tabType);
		mainTabs.setOnSlidePagerChangeListener(this);
		setTabsValue();
		// viewPager.setCurrentItem(tabType);
	}

	private void findAndInitSearchbViews() {
		searchEdit = (EditText) findViewById(R.id.hy_layoutSearch_edit);
		searchLv = (ListView) findViewById(R.id.hy_actShare_searchListView);
		ImageView addBtn = (ImageView) findViewById(R.id.hy_actShare_addBtn);
		setSearchViewAdp();
		searchEditWatcher.setOnSearchListener(this);
		searchEdit.addTextChangedListener(searchEditWatcher);
		searchLv.setOnItemClickListener(new MyOnItemClickListener());
		addBtn.setOnClickListener(new MyOnClickListener());
	}

	private void setSearchViewAdp() {
		if (searchLvAdp != null) {
			searchLvAdp.release();
		}
		if (searchEditWatcher == null) {
			searchEditWatcher = new SearchEditWatcher(this);
		} else {
			searchEditWatcher.resetSearch();
		}
		if ((tabType == 0 && resType == 0) || resType == 3) {
			// searchEditWatcher = new SearchEditWatcher(this,
			// SearchEditWatcher.SEARCH_SHARE_PEOPLEHUB);
			searchEditWatcher.setSearchType(SearchEditWatcher.SEARCH_SHARE_PEOPLEHUB);
			searchLvAdp = new ListViewSearchAdapter(this, ListViewSearchAdapter.TYPE_SHARE_PEOPLEHUB_FRIEND);
		} else if ((tabType == 1 && resType == 0) || resType == 1) {
			// searchEditWatcher = new SearchEditWatcher(this,
			// SearchEditWatcher.SEARCH_SHARE_DEMAND);
			searchEditWatcher.setSearchType(SearchEditWatcher.SEARCH_SHARE_DEMAND);
			searchLvAdp = new ListViewSearchAdapter(this, ListViewSearchAdapter.TYPE_SHARE_DEMAND);
		} else if ((tabType == 2 && resType == 0) || resType == 2) {
			// searchEditWatcher = new SearchEditWatcher(this,
			// SearchEditWatcher.SEARCH_SHARE_KNOWLEADGE);
			searchEditWatcher.setSearchType(SearchEditWatcher.SEARCH_SHARE_KNOWLEADGE);
			searchLvAdp = new ListViewSearchAdapter(this, ListViewSearchAdapter.TYPE_SHARE_KNOWLEADGE);
		}
		searchLv.setAdapter(searchLvAdp);
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		mainTabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		mainTabs.setDividerColor(Color.parseColor("#cccccc"));
		// 设置Tab底部线的高度
		mainTabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高度
		mainTabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		mainTabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, dm));
		// 设置Tab Indicator的颜色
		mainTabs.setIndicatorColor(Color.parseColor("#f98512"));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		mainTabs.setSelectedTextColor(Color.parseColor("#f98512"));
		// 取消点击Tab时的背景色
		mainTabs.setTabBackground(0);
	}

	private void setOverflowShowAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startGetConnections() {
		/*
		JSONObject jb = new JSONObject();
		try {
			jb.put("type", "0");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showLoadingDialogHy();
		ConnectionsReqUtil.doGetConnectionsList(this, this, jb, null);
		*/
		showLoadingDialog();
		new GetCacheConnectionsTask().execute();
	}
	/**
	 * 获取组织列表
	 */
	private void startGetOrgConnections() {
		showLoadingDialog();
		new GetCacheConnectionsTask(2).execute();
	}

	public void startGetDemandList(int page) {
//		ConferenceReqUtil.getListRequirementByUserID(this, this, App.getUserID(), page, 30, null);
		DemandReqUtil.getMyNeedList(this,this,page, 30,0 , 2, "", null);
	}

	public void startGetKnowleadgeList(int page) {
		// ConferenceReqUtil.getListKnowleadgeByTypeAndKeywordByUserID(this,
		// this, App.getApp().getUserID(), page, 30, null);
		KnowledgeReqUtil.doGetKnowledgeByTypeAndKeyword(this, this, App.getUserID(), 3, "", page, 30, null);
	}

	public void refreshLv() {
		if (tabType == 0) {
			peoplehubFragment.refreshExpLv();
		} else if (tabType == 1) {
			demandFragment.demandLvAdp.notifyDataSetChanged();
		} else if(tabType == 2){
			knowleadgeFragment.knowleadgeLvAdp.notifyDataSetChanged();
		}else {
//			orghubFragment
		}
	}

	public void finishActivity() {
		this.finish();
	}

	// add by leon
	// 获取人脉或用户详情，以便获取分享所需要的信息
	public void requestConnectionDetail(String id, boolean isonline, int type, Activity a) {
		if (type == Connections.type_org) { // 机构
			showLoadingDialogHy();
			JSONObject jb = ConnectionsReqUtil.getOrganizationDetailJson(id, isonline);
			ConnectionsReqUtil.dogetOrganizationDetail(a, this, jb, null);
		} else { // 个人
			showLoadingDialogHy();
			JSONObject jb = ConnectionsReqUtil.getContactDetailJson(id, isonline);
			ConnectionsReqUtil.doContactDetail(a, this, jb, null);
		}
	}

	private class MyOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				boolean isOpen = imm.isActive();
				if (isOpen) {
					imm.hideSoftInputFromWindow(ShareActivity.this.getCurrentFocus().getWindowToken(), 0);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			int clickId = v.getId();
			switch (clickId) {
			case R.id.hy_layoutTitle_backBtn: {
				finishActivity();
			}
				break;
			case R.id.hy_layoutTitle_rightTextBtn: {
				if (Util.isNull(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap)
						&& Util.isNull(InitiatorDataCache.getInstance().shareDemandSelectedMap)
						&& Util.isNull(InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap)) {
					Toast.makeText(ShareActivity.this, "没有选择任何项", Toast.LENGTH_SHORT).show();
				} else {
					Bundle b = new Bundle();
					b.putBoolean(Util.IK_VALUE, true);
					// add by leon
					if (!TextUtils.isEmpty(fromActivity) && fromActivity.equals(MeetingChatActivity.class.getSimpleName())) {
						if (resType == 3) { // 人脉
							if (!Util.isNull(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap)) {
								// 请求用户或人脉的详情
								showLoadingDialog();
								JTContactMini con = null;
								Iterator<Entry<String, JTContactMini>> iterPHM = InitiatorDataCache.getInstance().sharePeopleHubSelectedMap
										.entrySet().iterator();
								while (iterPHM.hasNext()) {
									Map.Entry entry = (Map.Entry) iterPHM.next();
									con = (JTContactMini) entry.getValue();
									break;
								}
								if (con != null) {
									requestConnectionDetail(con.id, con.isOnline, Connections.type_persion, ShareActivity.this);
								}
							}
						} else if (resType == 1) { // 需求
							if (!Util.isNull(InitiatorDataCache.getInstance().shareDemandSelectedMap)) {
								setResult(Activity.RESULT_OK);
								finish();
							}
						} else if (resType == 2) { // 知识
							if (!Util.isNull(InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap)) {
								setResult(Activity.RESULT_OK);
								finish();
							}
						}
					} else {
						Util.activitySetResult(ShareActivity.this, InitiatorHYActivity.class, b);
						finishActivity();
					}
				}
			}
				break;
			case R.id.hy_actShare_addBtn: {//根据type选择跳转
				if (tabType == 0) {
					ENavigate.startNewConnectionsActivity(ShareActivity.this, 1, null, 0x123);
				} else if (tabType == 1) {
					ENavigate.startDemandActivityForResult(ShareActivity.this, 0);
				} else if (tabType == 2) {
					ENavigate.startCreateKnowledgeActivity(ShareActivity.this, true, ShareActivity.TAB_KNOWLEADGE);
				}

			}
				break;
			}
		}
	}

	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (tabType == 0) {

				JTContactMini item = ((JTContactMini) searchLvAdp.getDataList().get(arg2));
				ImageView cb = (ImageView) arg1.findViewById(R.id.hy_itemInvitefriend_checkbox);
				if (!TextUtils.isEmpty(fromActivity) && fromActivity.equals(MeetingChatActivity.class.getSimpleName())) { // 只能选择一个人脉或用户
					if (InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.containsKey(item.id)) {
						cb.setImageResource(R.drawable.hy_check_norm);
						InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.remove(item.id);
					} else {
						if (InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.isEmpty()) {
							cb.setImageResource(R.drawable.hy_check_pressed);
							InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.put(item.id, item);
						} else {
							InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.clear();
							cb.setImageResource(R.drawable.hy_check_pressed);
							InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.put(item.id, item);
							searchLvAdp.notifyDataSetChanged();
						}
					}
				} else {
					if (InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.containsKey(item.id)) {
						cb.setImageResource(R.drawable.hy_check_norm);
						InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.remove(item.id);
					} else {
						cb.setImageResource(R.drawable.hy_check_pressed);
						InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.put(item.id, item);
					}
				}
			} else if (tabType == 1) {
				ImageView cb = (ImageView) arg1.findViewById(R.id.hy_item_shareCheck_checkbox);
				RequirementMini item = ((RequirementMini) searchLvAdp.getDataList().get(arg2));
				if (InitiatorDataCache.getInstance().shareDemandSelectedMap.containsKey(item.mID)) {
					cb.setImageResource(R.drawable.hy_check_norm);
					InitiatorDataCache.getInstance().shareDemandSelectedMap.remove(item.mID);
				} else {
					cb.setImageResource(R.drawable.hy_check_pressed);
					InitiatorDataCache.getInstance().shareDemandSelectedMap.put(item.mID, item);
				}
			} else {
				ImageView cb = (ImageView) arg1.findViewById(R.id.hy_item_shareCheck_checkbox);
				KnowledgeMini2 item = ((KnowledgeMini2) searchLvAdp.getDataList().get(arg2));
				if (InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.containsKey(item.id)) {
					cb.setImageResource(R.drawable.hy_check_norm);
					InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.remove(item.id);
				} else {
					cb.setImageResource(R.drawable.hy_check_pressed);
					InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.put(item.id, item);
				}
			}
			isSelectedInSearchList = true;
		}
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] titles = { "人脉", "需求", "知识","组织" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			if (!TextUtils.isEmpty(fromActivity) && fromActivity.equals(MeetingChatActivity.class.getSimpleName())) { // 来自畅聊
				return 1;
			}
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (!TextUtils.isEmpty(fromActivity) && fromActivity.equals(MeetingChatActivity.class.getSimpleName())) { // 来自畅聊
				switch (resType) {
				case 1: // 需求
					return titles[1];
				case 2: // 知识
					return titles[2];
				case 3: // 关系
					return titles[0];
				}
			}
			return titles[position];
		}

		@Override
		public Fragment getItem(int arg0) {

			if (!TextUtils.isEmpty(fromActivity) && fromActivity.equals(MeetingChatActivity.class.getSimpleName())) { // 来自畅聊
				switch (resType) {
				case 1: // 需求
					if (demandFragment == null) {
						demandFragment = new ShareDemandFragment(ShareActivity.this);
					}
					return demandFragment;
				case 2: // 知识
					if (knowleadgeFragment == null) {
						knowleadgeFragment = new ShareKnowleadgeFragment(ShareActivity.this);
					}
					return knowleadgeFragment;
				case 3: // 关系
					if (peoplehubFragment == null) {
						peoplehubFragment = new SharePeopleHubFragment(ShareActivity.this,SharePeopleHubFragment.ShareType.people);
					}
					// add by leon
					Bundle args = new Bundle();
					args.putString(ENavConsts.EFromActivityName, fromActivity);
					peoplehubFragment.setArguments(args);
					return peoplehubFragment;
				}
			}

			switch (arg0) {
			case 0: {
				if (peoplehubFragment == null) {
					peoplehubFragment = new SharePeopleHubFragment(ShareActivity.this,SharePeopleHubFragment.ShareType.people);
				}
				// add by leon
				Bundle args = new Bundle();
				args.putString(ENavConsts.EFromActivityName, fromActivity);
				peoplehubFragment.setArguments(args);
				return peoplehubFragment;
			}

			case 1: {
				if (demandFragment == null) {
					demandFragment = new ShareDemandFragment(ShareActivity.this);
				}
				return demandFragment;
			}
			case 2: {
				if (knowleadgeFragment == null) {
					knowleadgeFragment = new ShareKnowleadgeFragment(ShareActivity.this);
				}
				return knowleadgeFragment;
			}
			case 3: {
				if (orghubFragment == null) {
					orghubFragment = new SharePeopleHubFragment(ShareActivity.this,SharePeopleHubFragment.ShareType.organization);
				}
				return orghubFragment;
			}
			default: {
				if (peoplehubFragment == null) {
					peoplehubFragment = new SharePeopleHubFragment(ShareActivity.this,SharePeopleHubFragment.ShareType.people);
				}
				return peoplehubFragment;
			}
			}
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		if(null!=demandFragment){
		demandFragment.demandLv.stopLoadMore();		
		demandFragment.demandLv.stopRefresh();}
		if(null!=knowleadgeFragment){
		knowleadgeFragment.knowleadgeLv.stopLoadMore();		
		knowleadgeFragment.knowleadgeLv.stopRefresh();}		
		
		dismissLoadingDialogHy();
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			boolean isOpen = imm.isActive();
			if (isOpen) {
				imm.hideSoftInputFromWindow(ShareActivity.this.getCurrentFocus().getWindowToken(), 0);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if (tag == EAPIConsts.concReqType.CONNECTIONSLIST) {
			currFinishNum++;
			if (currFinishNum == 3) {
				dismissLoadingDialog();
			}
			if (object != null) {
				/*
				InitiatorDataCache.getInstance().connectionsList = (List<Connections>) object;
				if (!TextUtils.isEmpty(fromActivity) && fromActivity.equals(MeetingChatActivity.class.getSimpleName())) {
					InitiatorDataCache.getInstance().sharePeoplehubList = IniviteUtil
							.sortExpFriendContact(InitiatorDataCache.getInstance().connectionsList);
				} else {
					InitiatorDataCache.getInstance().sharePeoplehubList = IniviteUtil.sortExpFriendContact(
							InitiatorDataCache.getInstance().connectionsList, false);
				}
				if (peoplehubFragment != null) {
					peoplehubFragment.update(InitiatorDataCache.getInstance().sharePeoplehubList);
				}
				*/
				new GetCacheConnectionsTask().execute();
			} 
			else {
				Toast.makeText(this, "没有获取到人脉", Toast.LENGTH_SHORT).show();
			}

		} 
		else if (tag == EAPIConsts.demandReqType.demand_getMyNeedList) {
			if (object != null) {
				

				NeedItemListItem item = (NeedItemListItem) object;
				JTPage jtpage = item.getJtPage();

				// ArrayList<NeedItemData> lists = jtPage2.getLists();
				if (jtpage == null) {
					return;
				}
				List<NeedItemData> needItemList = new ArrayList<NeedItemData>();
				if (jtpage.getIndex() == 1) {
					needItemList.clear();
				}
				
				if ((jtpage != null) && (jtpage.getLists() != null)) {
					for (int i = 0; i < jtpage.getLists().size(); i++) {
						needItemList.add((NeedItemData) jtpage.getLists().get(i));
					}
				}
				
				List<RequirementMini> requirementMinis = new ArrayList<RequirementMini>();
				for (int i = 0; i < needItemList.size(); i++) {
					NeedItemData needItemData = needItemList.get(i);
					RequirementMini requirementMini = new RequirementMini();
					requirementMini.mID = Integer.valueOf(needItemData.demandId);
					requirementMini.mTypeName = needItemData.demandType==1?"我要投资":"我要融资";
					requirementMini.mTitle = needItemData.title==null?"":needItemData.title.value;
					requirementMini.mTime = needItemData.createTime;
					requirementMinis.add(requirementMini);
				}
				if (requirementMinis!=null&&requirementMinis.size()>0) {
					InitiatorDataCache.getInstance().shareDemandList.addAll(requirementMinis);
					if (demandFragment != null) {
						demandFragment.update(InitiatorDataCache.getInstance().shareDemandList);
					}
				}
			} else {
				Toast.makeText(this, "没有获取到需求", Toast.LENGTH_SHORT).show();
			}
		} 
/*		else if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETREQUIREMENTLIST) {
			if (object != null) {
				InitiatorDataCache.getInstance().shareDemandList.addAll((List<RequirementMini>) object);
				if (demandFragment != null) {
					demandFragment.update(InitiatorDataCache.getInstance().shareDemandList);
				}
			} else {
				Toast.makeText(this, "没有获取到需求", Toast.LENGTH_SHORT).show();
			}
		} 
*/		else if (tag == EAPIConsts.KnoReqType.GetKnowledgeByTypeAndKeyword) {
			currFinishNum++;
			if (currFinishNum == 3) {
				dismissLoadingDialog();
			}
			if (object != null) {
				Map<String, Object> hm = (Map<String, Object>) object;
				ArrayList<KnowledgeMini2> newKnowledgeList = (ArrayList<KnowledgeMini2>) hm.get("listKnowledgeMini");
				InitiatorDataCache.getInstance().shareKnowleadgeList.addAll(newKnowledgeList);
				if (knowleadgeFragment != null) {
					knowleadgeFragment.update(InitiatorDataCache.getInstance().shareKnowleadgeList);
				}
			} else {
				Toast.makeText(this, "没有获取到知识", Toast.LENGTH_SHORT).show();
			}
		}
		// add by leon
		else if (tag == EAPIConsts.concReqType.ContactDetail) { // 获取人脉详情
			currFinishNum++;
			if (currFinishNum == 3) {
				dismissLoadingDialog();
			}
			if (object == null) {
				Toast.makeText(this, "选择人脉失败", Toast.LENGTH_SHORT).show();
				return;
			}
			JTFile jtFile = null;
			ArrayList returnData = (ArrayList) object;
			JTContact2 onlineTContact = null;
			JTContact2 offlineTContact = null;
			for (int i = 0; returnData.size() > i; i = i + 2) {
				int type = (Integer) returnData.get(i);
				if (type == 0) {
					onlineTContact = (JTContact2) returnData.get(i + 1);
				} else {
					offlineTContact = (JTContact2) returnData.get(i + 1);
				}
			}
			if (onlineTContact != null) { // 用户或线上机构
				jtFile = onlineTContact.toJTfile();
				jtFile.mType = JTFile.TYPE_JTCONTACT_ONLINE;
				// 返回值
				Intent intent = new Intent();
				intent.putExtra(ENavConsts.redatas, jtFile);
				setResult(Activity.RESULT_OK, intent);
				finish();
				// 清空数据
				InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.clear();
			} else if (offlineTContact != null) { // 人脉或线下机构
				jtFile = offlineTContact.toJTfile();
				jtFile.mType = JTFile.TYPE_JTCONTACT_OFFLINE;
				// 返回值
				Intent intent = new Intent();
				intent.putExtra(ENavConsts.redatas, jtFile);
				setResult(Activity.RESULT_OK, intent);
				finish();
				// 清空数据
				InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.clear();
			} else {
				Toast.makeText(this, "选择人脉失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		searchEditWatcher.resetSearch();
		searchLvAdp.release();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!SearchUtil.isEmpty(searchEdit.getText().toString())) {
				searchEdit.setText("");
				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onSlidePagerChangeListener(int position) {
		// TODO Auto-generated method stub
		tabType = position;
		if (searchLv.getVisibility() == View.VISIBLE) {
			searchLv.setVisibility(View.GONE);
			viewPager.setVisibility(View.VISIBLE);
			if (isSelectedInSearchList) {
				isSelectedInSearchList = false;
				refreshLv();
			}
		}

		if (!SearchUtil.isEmpty(searchEdit.getText().toString())) {
			searchEdit.setText("");
		}
		setSearchViewAdp();
	}

	@Override
	public void onSearchListener(int searchType, ArrayList<Object> resultList) {
		if (SearchUtil.isEmpty(searchEdit.getText().toString())) {
			searchLv.setVisibility(View.GONE);
			searchLvAdp.getDataList().clear();
			viewPager.setVisibility(View.VISIBLE);
			if (isSelectedInSearchList) {
				isSelectedInSearchList = false;
				refreshLv();
			}
			return;
		}
		isSelectedInSearchList = false;
		viewPager.setVisibility(View.GONE);
		searchLv.setVisibility(View.VISIBLE);
		searchLvAdp.update(resultList);
		searchEditWatcher.resetSearch();
	}

	@Override
	public void initJabActionBar() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0x123) {
			startGetConnections();
			startGetOrgConnections();
		}
		super.onActivityResult(requestCode, resultCode, intent);
		if (isSelectedInSearchList) {

		}
	}
	
	/**
	 * 获取人脉列表后台任务
	 * @author leon
	 */
	private class GetCacheConnectionsTask extends AsyncTask<Void, Integer, ArrayList<Connections>>{

		private int type;
		public GetCacheConnectionsTask(){
			
		}
		/**
		 * 获取组织列表事，传type=2
		 * @param type
		 */
		public GetCacheConnectionsTask(int type){
			this.type = type;
		}
		
		@Override
		protected ArrayList<Connections> doInBackground(Void... params) {
			ArrayList<Connections> listPeople = new ArrayList<Connections>();
			if (type==2) {
				int orgSize = connsDBManager.queryCount("", 2, true)+connsDBManager.queryCount("", 2, false);
				JTPage jtPage = connsDBManager.query("", 2, 0, orgSize);
				if(jtPage != null){
					for(IPageBaseItem item : jtPage.getLists()){
						listPeople.add((Connections) item);
					}
				}
			}
			else {
				int peopleSize = connsDBManager.queryOfflineSize(Connections.type_persion);
				listPeople = connsDBManager.queryOffline(0, peopleSize, Connections.type_persion);
			}
			return listPeople;
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
	         // 进度更新
	     }
		
		@Override
		protected void onPostExecute(ArrayList<Connections> result) { 
			dismissLoadingDialog();
			if(result == null){
				return;
			}
			// 更新列表
			InitiatorDataCache.getInstance().connectionsList = (List<Connections>) result;
			if (type==2) {
				InitiatorDataCache.getInstance().shareOrghubList = IniviteUtil.sortExpFriendContact(InitiatorDataCache.getInstance().connectionsList);
				if (orghubFragment != null) {
					orghubFragment.update(InitiatorDataCache.getInstance().shareOrghubList);
				}
			}
			else {
				InitiatorDataCache.getInstance().sharePeoplehubList = IniviteUtil.sortExpFriendContact(InitiatorDataCache.getInstance().connectionsList);
				if (peoplehubFragment != null) {
					peoplehubFragment.update(InitiatorDataCache.getInstance().sharePeoplehubList);
				}
			}
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		registerReceiver(connsUpdateReceiver, new IntentFilter(EConsts.Action.ACTION_GET_CONNECTIONS_LIST_FINISH)); // 注册接收器
	}
	
	@Override
	public void onPause(){
		super.onPause();
		try{
			unregisterReceiver(connsUpdateReceiver); // 取消接收器
		}
		catch(IllegalArgumentException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 人脉更新广播接收器
	 * @author leon
	 */
	private class ConnectionsUpdateReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			dismissLoadingDialog();
			new GetCacheConnectionsTask().execute();
			new GetCacheConnectionsTask(2).execute();
		}
	}
}
