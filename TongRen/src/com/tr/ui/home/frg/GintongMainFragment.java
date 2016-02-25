package com.tr.ui.home.frg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.model.home.MainImages;
import com.tr.model.home.MainImagesItem;
import com.tr.model.home.MainPageList;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.GintongMainAdapter;
import com.tr.ui.adapter.MainMiddleAdapter;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.communities.home.CommunitiesActivity;
import com.tr.ui.conference.home.MeetingPiazzaActivity;
import com.tr.ui.flow.frg.FrgFlow;
import com.tr.ui.home.JointResourceActivity;
import com.tr.ui.home.MainActivity;
import com.tr.ui.tongren.TongRenFragment;
import com.tr.ui.tongren.home.RecommendProjectActivity;
import com.tr.ui.widgets.CycleViewPager;
import com.tr.ui.widgets.CycleViewPager.ImageCycleViewListener;
import com.tr.ui.widgets.GuidePopupWindow;
import com.tr.ui.widgets.ImageCycleView.OnPageClickListener;
import com.utils.common.EUtil;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 主页——金桐首页
 * 
 */
public class GintongMainFragment extends JBaseFragment implements
		OnPageClickListener, OnClickListener, OnItemClickListener, IBindData {
	private Context mContext;
	private String IMAGE_ITEM = "imgage_item";
	@ViewInject(R.id.image_docking)
	private ImageView image_docking;// 对接
	@ViewInject(R.id.image_dynamic)
	private ImageView image_dynamic;// 动态
	@ViewInject(R.id.image_community)
	private ImageView image_community;// 社群
	@ViewInject(R.id.grid_find_model)
	private GridView mGridView;// 展示原发现模块的内容：人脉/组织/活动/知识/需求/外加一个桐人
	@ViewInject(R.id.grid_other_model)
	private GridView mOtherGridView;// 展示轮播图下方的
	// @ViewInject(R.id.fragment_cycle_viewpager_content)
	private CycleViewPager mCycleViewPager;// 轮播图容器

	private ArrayList<MainPageList> mainPageLists = new ArrayList<MainPageList>();
	private List<MainImagesItem> listB = new ArrayList<MainImagesItem>();
	private List<MainImagesItem> bannerList = new ArrayList<MainImagesItem>();
	private List<MainImagesItem> listA = new ArrayList<MainImagesItem>();

	private SharedPreferences firstuse_sp;
	private SharedPreferences.Editor firstuse_edtior;

	/*
	 * private static final int[] imagesMiddle = new int[] {
	 * R.drawable.icon_banner_default, R.drawable.icon_banner_default_tongren };
	 */
	private static final int[] imagesTop = new int[] {
			R.drawable.image_contacts, R.drawable.image_customer,
			R.drawable.image_activitys, R.drawable.image_knowledges,
			R.drawable.image_tong_ren, R.drawable.image_demands };// ,R.drawable.image_orgs
	private static final int[] imagesMiddle = new int[] {
			R.drawable.icon_banner_default_tongren,
			R.drawable.icon_banner_default };
	private static final int[] imagesBottom = new int[] {
			R.drawable.image_capital, R.drawable.image_project,
			R.drawable.image_stock, R.drawable.image_gt_project,
			R.drawable.image_clue, R.drawable.image_tongmeng_activity,
			R.drawable.image_gintong_think_tank, R.drawable.image_perpetual_calendar,
			R.drawable.image_express_delivery, R.drawable.image_housing_loan,
			R.drawable.image_ticket, R.drawable.image_kitchen };
	private static final String[] bannerArrayStrings = new String[] {
			"http://7xp3i9.com1.z0.glb.clouddn.com/16-1-8/42850983.jpg",
			"http://7xp3i9.com1.z0.glb.clouddn.com/16-1-8/68592540.jpg" };

	/* 首页人脉下标 */
	private static final int INDEX_CONTACT = 0;
	/* 首页客户下标 */
	private static final int INDEX_ORGANIZATION = 1;
	/* 首页活动下标 */
	private static final int INDEX_MEETING = 2;
	/* 首页知识下标 */
	private static final int INDEX_KNOWLEDGES = 3;
	/* 首页金桐脑推荐下标 */
	private static final int INDEX_GINTONG_RECOMMEND = 4;
	/* 首页需求推荐下标 */
	private static final int INDEX_DEMAND = 5;
	/* 首页找资金下标 */
	private static final int INDEX_DEMAND_BANKROLL = 6;
	/* 首页找项目下标 */
	private static final int INDEX_DEMAND_PROJECT = 7;
	/* 首页桐人项目下标 */
	private static final int INDEX_TONGREN = 8;
	/* 首页社群下标 */
	private static final int INDEX_COMMUNITY = 9;
	/* 首页事务下标 */
	private static final int INDEX_TRANSCATION = 10;

	private MainMiddleAdapter mGridAdapter;
	private GintongMainAdapter mOtherGridAdapter;
	private static Map<String, Integer> START_UI_MAP = new HashMap<String, Integer>();
	private MainActivity mainActivity;
	private SharedPreferences firstuse_sp_json;
	private SharedPreferences.Editor firstuse_edtior_json;

	/**
	 * 拿到MainActivity的引用
	 * 
	 * @param mainActivity
	 */
	public GintongMainFragment(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public GintongMainFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		START_UI_MAP.put("contactList", INDEX_CONTACT);// 人脉
		START_UI_MAP.put("organizationList", INDEX_ORGANIZATION);// 客户
		START_UI_MAP.put("activityList", INDEX_MEETING);// 活动
		START_UI_MAP.put("knowledgesList", INDEX_KNOWLEDGES);// 知识
		START_UI_MAP.put("gintongRecommend", INDEX_GINTONG_RECOMMEND);// 金桐脑推荐
		START_UI_MAP.put("demandList", INDEX_DEMAND);// 需求
		START_UI_MAP.put("demandList?demandType=2", INDEX_DEMAND_BANKROLL);// 找资金
		START_UI_MAP.put("demandList?demandType=1", INDEX_DEMAND_PROJECT);// 找项目
		START_UI_MAP.put("demandList/project/recommend", INDEX_TONGREN);// 桐人项目推荐
		START_UI_MAP.put("community", INDEX_COMMUNITY);// 社群
		START_UI_MAP.put("transcation", INDEX_TRANSCATION);// 事务
		spiltData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View gintongFragmentLayout = inflater.inflate(
				R.layout.fragment_gintongmain_layout, null);
		ViewUtils.inject(this, gintongFragmentLayout);

		return gintongFragmentLayout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.home_new_menu_more).setVisible(true);
		menu.findItem(R.id.home_new_menu_search).setVisible(true);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		/* 轮盘图初始化 */
		mCycleViewPager = (CycleViewPager) getActivity().getFragmentManager()
				.findFragmentById(R.id.fragment_cycle_viewpager_content);
		// 设置轮播
		mCycleViewPager.setWheel(true);
		// 初始化轮盘图
		initBannerList();
		// 初始化缓存数据,提高交互
		initData();
		/**
		 * 初始化
		 */
		mGridAdapter = new MainMiddleAdapter(mContext, listA);
		mGridView.setAdapter(mGridAdapter);
		mGridView.setOnItemClickListener(this);

		mOtherGridAdapter = new GintongMainAdapter(mContext, listB);
		mOtherGridView.setAdapter(mOtherGridAdapter);
		mOtherGridView.setOnItemClickListener(new mBottomOnItemClick());
		// HomeReqUtil.getMainPageList(getActivity(), this, null);

		firstuse_sp = getActivity().getSharedPreferences(
				GlobalVariable.SHARED_PREFERENCES_FIRST_USE,
				getActivity().MODE_PRIVATE);
		firstuse_edtior = firstuse_sp.edit();
		if (firstuse_sp.getBoolean(GlobalVariable.MAIN_FIRST_USE, true)) {
			GuidePopupWindow guidePop = new GuidePopupWindow(getActivity());
			guidePop.showViewPager();
			guidePop.show();
			firstuse_edtior.putBoolean(GlobalVariable.MAIN_FIRST_USE, false);
			firstuse_edtior.commit();
		} else {

		}
	}

	private void initData() {
		// 初始化假数据
		for (int i = 0; i < imagesTop.length; i++) {
			MainImagesItem item = new MainImagesItem();
			MainImages itemImages = new MainImages();
			item.setImages(itemImages);
			item.getImages().setResourceID(imagesTop[i]);
			listA.add(item);
		}
		for (int i = 0; i < imagesMiddle.length; i++) {
			MainImagesItem item = new MainImagesItem();
			MainImages itemImages = new MainImages();
			item.setImages(itemImages);
			item.getImages().setResourceID(imagesMiddle[i]);
			bannerList.add(item);
		}
		for (int i = 0; i < imagesBottom.length; i++) {
			MainImagesItem item = new MainImagesItem();
			MainImages itemImages = new MainImages();
			item.setImages(itemImages);
			item.getImages().setResourceID(imagesBottom[i]);
			listB.add(item);
		}
	}

	/**
	 * 填充轮盘图数据
	 */
	private void initBannerList() {
		bannerList.clear();
		for (int i = 0; i < imagesMiddle.length; i++) {
			MainImagesItem item = new MainImagesItem();
			MainImages itemImages = new MainImages();
			item.setImages(itemImages);
			item.getImages().setResourceID(imagesMiddle[i]);
			bannerList.add(item);
		}
		List<ImageView> views = new ArrayList<ImageView>();
		views.add(EUtil.getImageView(getActivity(),
				bannerList.get(bannerList.size() - 1).getImages()
						.getResourceID()));
		for (int i = 0; i < bannerList.size(); i++) {
			views.add(EUtil.getImageView(getActivity(),
					bannerList.get(bannerList.size() - 1).getImages()
							.getResourceID()));
		}
		// 将第一个ImageView添加进来
		views.add(EUtil.getImageView(getActivity(), bannerList.get(0)
				.getImages().getResourceID()));
		// 在加载数据前设置是否循环
		mCycleViewPager.setData(views, bannerList, mAdCycleViewListener);
	}

	private void initView(View view) {
		image_docking.setOnClickListener(this);
		image_dynamic.setOnClickListener(this);
		image_community.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			HomeReqUtil.getMainPageList(getActivity(), this, null);
		}
	}

	/**
	 * 首页入口UI_Middle填充本地数据
	 */
	private void initMiddleControlUIData() {
		/*
		 * bannerList.clear(); for (int i = 0; i < imagesMiddle.length; i++) {
		 * MainImagesItem mainImageItem = new MainImagesItem(); MainImages image
		 * = new MainImages(); image.setResourceID(imagesMiddle[i]);
		 * mainImageItem.setImages(image); bannerList.add(mainImageItem); }
		 */
	}

	/**
	 * 底部导航跳转
	 * 
	 * @author elephant
	 * 
	 */
	public class mBottomOnItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String keyString = splitURL(listB.get(position).getAlt());
			if (TextUtils.isEmpty(keyString)) {// 跳转到WEBVIEW
				ENavigate.startWebViewActivity(getActivity(),
						listB.get(position).getAlt(), listB.get(position)
								.getTitle());
				return;
			}
			Integer index = START_UI_MAP.get(keyString);
			switch (index) {
			case INDEX_DEMAND_BANKROLL:// 找资金
				ENavigate.startFindPricesActivity(getActivity(), 2);
				break;
			case INDEX_DEMAND_PROJECT:// 找项目
				ENavigate.startFindPricesActivity(getActivity(), 1);
				break;
			case INDEX_TONGREN:// 桐人项目推荐
				// mainActivity.showTongRenFrg();// TODO 跳转到桐人模块
				Intent intent = new Intent(getActivity(),
						RecommendProjectActivity.class);
				startActivityForResult(intent, TongRenFragment.REQ_PROJECT);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 原发现模块里的人脉/组织/活动（即会议）/知识/需求 ，外加桐人
	 */
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long rowid) {
		String keyString = splitURL(listA.get(position).getAlt());
		if (TextUtils.isEmpty(keyString)) {// 跳转到WEBVIEW
			ENavigate.startWebViewActivity(getActivity(), listA.get(position)
					.getAlt(), listA.get(position).getTitle());
			return;
		}
		Integer index = START_UI_MAP.get(keyString);
		if (index == null || index == -1) {
			return;
		}
		switch (index) {
		case INDEX_CONTACT:// 人脉
			ENavigate.startFindPeopleActivity(mContext, 1);
			break;
		case INDEX_ORGANIZATION:// 客户
			ENavigate.startOrganizationFirstPageActivity(mContext);
			break;
		case INDEX_MEETING:// 活动-->>会议
			startActivity(new Intent(mContext, MeetingPiazzaActivity.class));
			break;
		case INDEX_KNOWLEDGES:// 知识
			ENavigate.startKnowledgeSquareActivity(mContext);
			break;
		case INDEX_GINTONG_RECOMMEND:// 金桐脑推荐
			ENavigate.startFlowActivity(getActivity(), FrgFlow.FLOW_GT, -1);
			break;
		case INDEX_DEMAND:// 需求
			ENavigate.startFindDemandActivity(mContext, 5);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取GridView的数据
	 */
	private List<HashMap<String, Object>> getGridViewData(
			MainPageList mainPageList) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		ArrayList<MainImagesItem> mlist = mainPageList.getList();
		int[] arrImages = new int[] { R.drawable.image_contacts,
				R.drawable.image_orgs, R.drawable.image_activitys,
				R.drawable.image_knowledges, R.drawable.image_tong_ren,
				R.drawable.image_demands };
		for (int i = 0; i < mlist.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(IMAGE_ITEM, mlist.get(i).getImages().getLarge());
			list.add(map);
		}

		return list;
	}

	/**
	 * 轮播图点击事件
	 */
	@Override
	public void onClick(View imageView, MainImagesItem imageInfo) {

	}

	/**
	 * 顶部导航点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_docking:// 对接
			startActivity(new Intent(mContext, JointResourceActivity.class));
			break;
		case R.id.image_dynamic:// 动态
			ENavigate.startFlowActivity(getActivity(), FrgFlow.FLOW_FRIEND, -1);
			break;
		case R.id.image_community:// 社群
			startActivity(new Intent(mContext, CommunitiesActivity.class));
			break;

		default:
			break;
		}
	}

	/**
	 * 跳转到Activity或者URL
	 * 
	 * @param alt
	 */
	public String splitURL(String alt) {
		String key = "";
		try {
			int startIndex;
			if (!TextUtils.isEmpty(alt) && alt.contains("gtw://")) {
				startIndex = alt.lastIndexOf("//");
				key = alt.substring(startIndex + 2, alt.length());
			}
		} catch (Exception e) {
			return null;
		}
		return key;
	}

	/**
	 * 填充数据
	 */
	private void spiltData() {
		/**
		 * 逻辑需要优化
		 */
		int size = mainPageLists.size();
		int count = 0;
		for (int i = 0; i < size; i++) {
			MainPageList mainPageList = mainPageLists.get(i);
			if (mainPageList.getType().equals("banner")) {
				bannerList.clear();
				bannerList = mainPageList.getList();
			} else {
				if (count == 0 && mainPageList.getList() != null
						&& mainPageList.getList().size() > 0) {
					listA.clear();
					listA = mainPageList.getList();
				} else if (mainPageList.getList() != null
						&& mainPageList.getList().size() > 0) {
					listB.clear();
					listB = mainPageList.getList();
				}
				count++;
			}
		}

		/*
		 * if (bannerList.size() <= 0) { initMiddleControlUIData(); }
		 */

	}

	/**
	 * 更新主界面UI
	 */
	private void refreshUI() {
		mGridAdapter.updateList(listA);
		mGridAdapter.notifyDataSetChanged();
		// mImageCycleView.update(bannerList);
		List<ImageView> views = new ArrayList<ImageView>();
		views.add(EUtil.getImageView(getActivity(),
				bannerList.get(bannerList.size() - 1).getImages().getSmall()));
		for (int i = 0; i < bannerList.size(); i++) {
			views.add(EUtil.getImageView(getActivity(), bannerList.get(i)
					.getImages().getSmall()));
		}
		// 将第一个ImageView添加进来
		views.add(EUtil.getImageView(getActivity(), bannerList.get(0)
				.getImages().getSmall()));
		// 在加载数据前设置是否循环
		mCycleViewPager.setData(views, bannerList, mAdCycleViewListener);
		mOtherGridAdapter.updateList(listB);
		mOtherGridAdapter.notifyDataSetChanged();
	}

	/**
	 * 轮盘图点击事件
	 */
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void onImageClick(MainImagesItem info, int position,
				View imageView) {
			if (mCycleViewPager.isCycle()) {
				position = position - 1;
				String keyString = splitURL(bannerList.get(position).getAlt());
				if (TextUtils.isEmpty(keyString)) {// 跳转到WEBVIEW
					ENavigate.startWebViewActivity(getActivity(), bannerList
							.get(position).getAlt(), bannerList.get(position)
							.getTitle());
					return;
				}
				Integer index = START_UI_MAP.get(keyString);
				if (index == null || index == -1) {
					return;
				}
				switch (index) {
				case INDEX_TRANSCATION:// 事务
					mainActivity.showTongRenFrg();// TODO 跳转到桐人模块
					break;
				case INDEX_COMMUNITY:// 社群
					startActivity(new Intent(mContext,
							CommunitiesActivity.class));
					break;
				default:
					break;
				}
			}
		}
	};

	/**
	 * 数据访问回调函数
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {
		if(null!=getActivity()){
			firstuse_sp_json = getActivity().getSharedPreferences(
					GlobalVariable.SHARED_PREFERENCES_INDEX_JSON,
					getActivity().MODE_PRIVATE);
		}
		switch (tag) {
		case EAPIConsts.HomeReqType.HOME_PAGE_LIST:
			if (object != null) {
				mainPageLists = (ArrayList<MainPageList>) object;
			} else if (null!=firstuse_sp_json&&!TextUtils.isEmpty(firstuse_sp_json.getString(
					GlobalVariable.MAIN_INDEX_JSON, ""))) {// 取本地JSON数据
				String objString = firstuse_sp_json.getString(
						GlobalVariable.MAIN_INDEX_JSON, "");
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(objString);
					Gson gson = new Gson();
					if (jsonObject.has("indexAdvertiseList")) {
						JSONArray connJsArr = jsonObject
								.getJSONArray("indexAdvertiseList");
						mainPageLists = gson.fromJson(connJsArr.toString(),
								new TypeToken<List<MainPageList>>() {
								}.getType());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				try {
					/**
					 * 解析本地JSON
					 */
					Gson gson = new Gson();
					String fileName = "indexAdvertiseList.json";
					String jsonStr = EUtil.getJson(getActivity(), fileName);
					JSONObject response;
					response = new JSONObject(jsonStr);
					if (response.has("indexAdvertiseList")) {
						JSONArray connJsArr = response
								.getJSONArray("indexAdvertiseList");
						mainPageLists = gson.fromJson(connJsArr.toString(),
								new TypeToken<List<MainPageList>>() {
								}.getType());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			spiltData();
			refreshUI();
			break;
		default:
			break;
		}

	}
}
