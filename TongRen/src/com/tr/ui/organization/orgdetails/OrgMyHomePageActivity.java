package com.tr.ui.organization.orgdetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.db.ConnectionsDBManager;
import com.tr.db.DBHelper;
import com.tr.model.demand.ASSOData;
import com.tr.model.im.ChatDetail;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.obj.Connections;
import com.tr.model.obj.DynamicComment;
import com.tr.model.obj.DynamicPraise;
import com.tr.model.obj.JTFile;
import com.tr.model.user.OrganizationMini;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.connections.viewfrg.ListViewFragment;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.knowledge.swipeback.SwipeBackActivity;
import com.tr.ui.organization.firstpage.OrganizationDataActivity;
import com.tr.ui.organization.model.Customer;
import com.tr.ui.organization.model.CustomerProfileVo;
import com.tr.ui.organization.model.GetId;
import com.tr.ui.organization.model.Org_de_id;
import com.tr.ui.organization.model.PushKnowledge;
import com.tr.ui.organization.model.RelatedInformation;
import com.tr.ui.organization.model.finance.CustomerFinanceDetail;
import com.tr.ui.organization.model.hight.CustomerHightInfo;
import com.tr.ui.organization.model.industry.CustomerOrgIndustry;
import com.tr.ui.organization.model.notice.CustomerNotice;
import com.tr.ui.organization.model.param.CustomerOrganizatianParams;
import com.tr.ui.organization.model.parameters.OrganizationDetialsIncomingParameters;
import com.tr.ui.organization.model.peer.CustomerPeerInfo;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.resource.CustomerResource;
import com.tr.ui.organization.model.stock.CustomerStock;
import com.tr.ui.organization.model.stock.CustomerStockList;
import com.tr.ui.organization.model.stock.CustomerTenStock;
import com.tr.ui.organization.orgdetails.orgfragment.OrgMyHomePageDataFragment;
import com.tr.ui.organization.orgdetails.orgfragment.OrgRelationEvaluationFrg;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.tr.ui.widgets.viewpagerheaderscroll.SlidingTabLayout;
import com.tr.ui.widgets.viewpagerheaderscroll.SlidingTabLayout.TabColorizer;
import com.tr.ui.widgets.viewpagerheaderscroll.TouchCallbackLayout;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableFragmentListener;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableListener;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ViewPagerHeaderHelper;
import com.utils.common.EConsts;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;
import com.utils.string.StringUtils;

/**
 * 组织详情界面 : 他人主页/我的主页
 * 启动方法：ENavigate.startOrgMyHomePageActivity
	 * 
	 * @param context
	 * @param id
	 *            用户id
	 *        customerId 创建人ID
	 *        isOnline  是否线上组织
	 * @datatype:
 * @author yangwenbin
 * @since 2015-03-12 MyHomePageActivity 适应框架
 *        返回触摸事件的状态，必须要返回一个使用适应框架的Activity,否则listview刷新时候和scrollview发生冲突
 */
public class OrgMyHomePageActivity extends SwipeBackActivity implements
		TouchCallbackLayout.TouchEventListener, ScrollableFragmentListener,
		ViewPagerHeaderHelper.OnViewPagerTouchListener, OnClickListener,
		IBindData {
	/** 是否成功*/
	private boolean isSuccess;
	/** 组织对象*/
	private Customer client_customer;
	/** 组织id */
	private long userId;
	/**默认顶部布局高度*/
	private static final long DEFAULT_DURATION = 300L;
	private static final float DEFAULT_DAMPING = 1.5f;
	/**滑动监听器集合*/
	private SparseArrayCompat<ScrollableListener> mScrollableListenerArrays = new SparseArrayCompat<ScrollableListener>();
	/**资料，动态，评论 */
	private ViewPager mViewPager;
	/**顶部布局父类View*/
	private View mHeaderLayoutView;
	/**顶部布局控制控件 */
	private ViewPagerHeaderHelper mViewPagerHeaderHelper;
	/**pull>mTouchSlop=expand
	 * push>mTouchSlop=fold
	 * */
	private int mTouchSlop;
	/**滑动索引栏高度 */
	private int mTabHeight;
	/**顶部布局高度*/
	private int mHeaderHeight;
	/**点击更多的弹出对话框*/
	private AlertDialog dlg, dialog;

	private AlertDialog.Builder builder;
	/**对话框中的Window*/
	private Window window;
	/**滑动监听器*/
	private Interpolator mInterpolator = new DecelerateInterpolator();

	// 组织详情的三个Fragment
	// 组织动态
//	private FrgFlow orgFrgFlow;
	// 资料
	private OrgMyHomePageDataFragment mOrgMyHomePageDataFragment;
	// 评价
	private OrgRelationEvaluationFrg relationEvaluationFrg;

	private ViewPagerAdapter viewPagerAdapter;

	private ImageView relationIv; //头像
	private TextView relationPositionTv; //职位
	private TextView nameTv;//姓名
	private ImageView relationBgRl; //背景
	private ImageView qRCodeImage; 
	/** 加好友的底部栏*/
	private FrameLayout addFriendFLayout;
	/** 发消息的底部栏*/
	private FrameLayout sendMessageFLayout;

	/** 编辑 */
	private MenuItem menuEdit;
	/** 解除好友关系 */
	private MenuItem menuDeleteFriend;
	/** 对接 */
	private MenuItem menuJoin;
	/** 加入黑名单 */
	private MenuItem menuBlackNumber;
	/** 消息 */
	private MenuItem menuMessage;
	/** 加为好友 */
	private MenuItem menuAddFriend;
	/** 评价 */
	private MenuItem menuEvalation;

	public static final int result_recommend = 1;
	public static final int result_edit = 5;

	private TextView homepage_organization, homepage_relationNameTv,
			orgReportTv, orgDockingTv, liftFriendTv, seeCustomerTv,
			addBlackList, noFriendsReportTv, noFriendsAddBlackList,
			noFriendsDocking, noFriendsToCustomer, org_add_preservation_yes,
			org_add_preservation_no, org_preservation_yes, org_preservation_no,
			chatTv, homepage_name_tv, org_edit, org_cancel;

	private ImageView homepage_QRCode;

	// 组织详情的类
	public CustomerOrganizatianParams org_customer;
	// 最新公告的集合
	private ArrayList<CustomerNotice> customerNoticeList;
	// 最新资讯的集合
	private ArrayList<PushKnowledge> pushKnowledgeNoticeList;
	// 投资，融资，专家需求，专家身份的类
	private CustomerResource resource;
	// 关联信息
	private RelatedInformation relatedInformation;

	private Map<String, Object> map;

	private boolean flag;// 是否是好友状态

	private int label = 5;

	// 组织：关联 人脉的集合
	private ArrayList<ASSOData> contactsList;
	// 组织：关联 组织的集合
	private ArrayList<ASSOData> organizationList;
	// 组织：关联 知识的集合
	private ArrayList<ASSOData> knowledgeList;
	// 组织：关联 事件的集合
	private ArrayList<ASSOData> eventList;
	// 财务分析
	private CustomerFinanceDetail customerFinanceDetail;
	// 股东研究
	private CustomerStock customerStock;
	// 是大股东列表
	private ArrayList<CustomerTenStock> customerTenStockList;
	// 流通股东
	private ArrayList<CustomerStockList> customerLtStockList;
	// 查询同业竞争详情
	private ArrayList<CustomerPeerInfo> peerList;
	// 查询高层治理
	private ArrayList<CustomerHightInfo> dshList;
	private ArrayList<CustomerHightInfo> jshList;
	private ArrayList<CustomerHightInfo> ggList;
	private ArrayList<CustomerHightInfo> ggjzList;
	// 4.16 查询行业动态
	private ArrayList<CustomerOrgIndustry> customerOrgIndustryList;
	// 4.22查询研究报告
	private ArrayList<CustomerPersonalLine> customerPersonalLineList;
	
	private ConnectionsDBManager connsDBManager;

	private String[] types = { "1", "2" };// 1：加入黑名单 2：移除黑名单


	@Override
	public void initJabActionBar() {
		actionBar = getActionBar();
		actionBar.setTitle(" ");
		actionBar.hide();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 设置actionbar悬浮透明
		// getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_activity_myhomepage);
		if (getIntent().hasExtra("userId")) {
			userId = getIntent().getLongExtra("userId", -1);
		} else if (getIntent().hasExtra("customerId")) {
			userId = getIntent().getLongExtra("customerId", -1);
		}
		jabHideActionBar();
		initFragment();

		initUI();

		initHeaderView();
		
		initVars();

	}

	// 再次进入界面是会自动请求接口，刷新数据
	@Override
	protected void onStart() {
		super.onStart();
		requestOrgJson();
	}
	/**
	 *  判断需要加载的数据库
	 */
	private void initVars(){
		
		SharedPreferences sp = getSharedPreferences(
				EConsts.share_firstLoginGetConnections, Activity.MODE_PRIVATE);
		String tableName = sp.getString(EConsts.share_itemUserTableName, "");
		if (TextUtils.isEmpty(tableName)) {
			connsDBManager = new ConnectionsDBManager(this,
					DBHelper.TABLE_APP_CONNECTIONS);
		} else if (tableName.equals(DBHelper.TABLE_APP_CONNECTIONS)) {
			connsDBManager = new ConnectionsDBManager(this,
					DBHelper.TABLE_APP_CONNECTIONS);
		} else if (tableName.equals(DBHelper.TABLE_APP_CONNECTIONS_BACK)) {
			connsDBManager = new ConnectionsDBManager(this,
					DBHelper.TABLE_APP_CONNECTIONS_BACK);
		} else {
			connsDBManager = new ConnectionsDBManager(this,
					DBHelper.TABLE_APP_CONNECTIONS);
		}
	}
	/**
	 * 请求网络数据
	 */
	public void requestOrgJson() {
		// 参数二：IbindData的实现类
		// 组织详情页面4.4 组织详情 /org/orgAndProInfo
		OrganizationDetialsIncomingParameters orgParameters = new OrganizationDetialsIncomingParameters();
		if (getIntent().hasExtra("userId")) {
			Log.v("DATA", "进入userId333");
			orgParameters.userId = userId;
		} else if (getIntent().hasExtra("customerId")) {
			Log.v("DATA", "进入customerId444");
			orgParameters.orgId = userId;
		}

		OrganizationReqUtil.doRequestWebAPI(this, this, orgParameters, null,
				OrganizationReqType.ORGANIZATION_REQ_ORGANDPROINFO);

		// 子模块在迭代里面做，暂时隐藏
		// // 组织详情页面4.11 按年份，报表类型,季度，获取财务分析详情
		// FinancialAnalysisParameters financialAnalysisParameters = new
		// FinancialAnalysisParameters();
		// OrganizationReqUtil.doRequestWebAPI(this, this,
		// financialAnalysisParameters, null,
		// OrganizationReqType.ORGANIZATION_REQ_DETAILS);
		//
		// // 4.12高层治理
		// CustomerHight_re customerHight_re = new CustomerHight_re();
		// OrganizationReqUtil.doRequestWebAPI(this, this, customerHight_re,
		// null, OrganizationReqType.ORGANIZATION_REQ_FINDHEGHTONE);
		//
		// // 4.14查询股东研究
		// CustomerStockParameters customerStockParameters = new
		// CustomerStockParameters();
		// customerStockParameters.customerId = 2+"";
		// OrganizationReqUtil.doRequestWebAPI(this, this,
		// customerStockParameters, null,
		// OrganizationReqType.ORGANIZATION_REQ_FINDSTOCKONE);
		//
		// // 4.16查询行业动态
		// CustomerOrgIndustryParams customerOrgIndustryParams = new
		// CustomerOrgIndustryParams();
		// // customerOrgIndustryParams.customerId = userId;
		// customerOrgIndustryParams.customerId = 1+"";
		// OrganizationReqUtil.doRequestWebAPI(this, this,
		// customerOrgIndustryParams, null,
		// OrganizationReqType.ORGANIZATION_REQ_FINDINDUSTRY);
		//
		// // 4.19 查询同业竞争列表 customer/peer/findPeer.json
		// CustomerPeerInfoParameters customerPeerInfoParameters = new
		// CustomerPeerInfoParameters();
		// customerPeerInfoParameters.customerId = 1 + "";
		// OrganizationReqUtil.doRequestWebAPI(this, this,
		// customerPeerInfoParameters, null,
		// OrganizationReqType.ORGANIZATION_REQ_FINDPER);
		//
		// // 4.22 查询研究报告
		// CustomerPersonalLineParams customerPersonalLineParams = new
		// CustomerPersonalLineParams();
		// customerPersonalLineParams.customerId = 1+"";
		// OrganizationReqUtil.doRequestWebAPI(this, this,
		// customerPersonalLineParams, null,
		// OrganizationReqType.ORGANIZATION_REQ_FINDREPORT);
	}
	/**
	 * 初始化布局
	 */
	private void initUI() {

		headerVi = findViewById(R.id.homepage_headerVi);

		// 初始化顶部titlebar
		/** titlebar的背景 */
		titlebar = (ImageView) findViewById(R.id.homepage_titlebar);

		/** 组织名称 */
		homepage_name_tv = (TextView) findViewById(R.id.homepage_name_tv);

		/** 头像 */
		relationIv = (ImageView) findViewById(R.id.homepage_RelationImage);

		/** 主页类型：我的，好友，他人 */
		hometypeTv = (TextView) findViewById(R.id.homepage_hometypeTv);

		/** 分享按钮 */
		relationShareIv = (ImageView) findViewById(R.id.homepage_relationShareIv);
		relationShareIv.setOnClickListener(this);

		/** 他人主页功能是更多操作按钮 */
		/** 我的主页的功能是编辑修改操作按钮 */
		relationMoreIv = (ImageView) findViewById(R.id.homepage_relationMoreIv);
		relationMoreIv.setOnClickListener(this);

		/** 主页返回 */
		relationHomeBackIv = (ImageView) findViewById(R.id.homepage_relationHomeBackIv);
		relationHomeBackIv.setOnClickListener(this);

		/** 二维码 */
		homepage_QRCode = (ImageView) findViewById(R.id.homepage_QRCode);

		titlebarHeight = getResources().getDimensionPixelSize(
				R.dimen.retion_title_height);
		mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
		mTabHeight = getResources().getDimensionPixelSize(R.dimen.tabs_height);
		mHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.viewpager_header_height);

		mViewPagerHeaderHelper = new ViewPagerHeaderHelper(this, this);

		TouchCallbackLayout touchCallbackLayout = (TouchCallbackLayout) findViewById(R.id.homepage_layout);
		touchCallbackLayout.setTouchEventListener(this);

		mHeaderLayoutView = findViewById(R.id.homepage_header);
		SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.homepage_tabs);
		slidingTabLayout.setCustomTabColorizer(new TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return Color.rgb(251, 127, 5);
			}
		});
		mViewPager = (ViewPager) findViewById(R.id.homepage_viewpager);
		viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(viewPagerAdapter);
		viewPagerAdapter.notifyDataSetChanged();

		slidingTabLayout.setViewPager(mViewPager);

		ViewCompat.setTranslationY(mViewPager, mHeaderHeight);
	}
	/**
	 * 初始化Fragment
	 */
	private void initFragment() {
		Long flowId = 0L;
		// 动态传入的是组织用户本身的id
		if (App.getApp().getAppData().getUser().getmOrganizationInfo().mLegalPersonIDCardImage.equals(userId+"")) {
			flowId = Long.valueOf(App.getUserID());
		}
		if(getIntent().hasExtra("createById")){
			flowId = getIntent().getLongExtra("createById", flowId);
		}
//		orgFrgFlow = new FrgFlow(0,OrgMyHomePageActivity.this,flowId);
//		orgFrgFlow.setParent(this);
		// 资料传入的是组织用户的customerId(他人主页和我的主页都一样)
		mOrgMyHomePageDataFragment = OrgMyHomePageDataFragment.newInstance(1);
		// 向mOrgMyHomePageDataFragment传递数据
		if (getIntent().hasExtra("userId")) {
			mOrgMyHomePageDataFragment.getArguments().putLong("userId", userId);

		} else if (getIntent().hasExtra("customerId")) {
			mOrgMyHomePageDataFragment.getArguments().putLong("customerId",
					userId);

		}

		// 他人主页 用customer.userId取id
		// 我的主页用App.getUserID()取id
		relationEvaluationFrg = OrgRelationEvaluationFrg.newInstance(2);

	}
	/**
	 * 初始化HeaderView
	 */
	private void initHeaderView() {

		/** 公司职务 */
		relationPositionTv = (TextView) findViewById(R.id.homepage_relationPositionTv);
		/** 姓名 */
		nameTv = (TextView) findViewById(R.id.nameTv);
		// 背景
		relationBgRl = (ImageView) findViewById(R.id.homepage_relationBgRl);

		/** 客户的图片 */
		mLinearLayout = (ImageView) findViewById(R.id.homepage_layout_his_homePage);

		mLinearLayout.setOnClickListener(this);
		/** 背景浮层 */
		relationBgRlCover = (ImageView) findViewById(R.id.homepage_relationBgRlCover);
		relationBgRlCover.setBackgroundResource(R.drawable.relation_man_bg);// 设置蓝紫色浮层
		/** 客户公司的名字 */
		homepage_relationNameTv = (TextView) findViewById(R.id.homepage_relationNameTv);

	}

	public interface setLongArgument {

		public void setLong(long dynamicId);

	}

	private void initViewDetail(Object object) {

		if (org_customer != null) {
			if (org_customer.customer != null) {

				initHeaderViewData(org_customer.customer);
				initBottomView(org_customer.customer);
//				/* 隐藏回复框 */
//				orgFrgFlow.showBottomNormalView();
				// orgFrgFlow.getArguments().putLong("userId",
				// org_customer.customer.createById);
//				orgFrgFlow.getArguments().putLong("userId", userId);
//				orgFrgFlow.upDate();
				viewPagerAdapter.notifyDataSetChanged();
				if (org_customer.customer.comeId != 0) {
					// 该用户为我的客户时显示 我的客户这张图片mLinearLayout
					mLinearLayout.setVisibility(View.VISIBLE);
				}

				if (org_customer.customer.createById == Long.valueOf(App
						.getUserID())) {

					hometypeTv.setText("我的主页");
					// 传递评价id
					relationEvaluationFrg.getArguments().putString(
							EConsts.Key.ID,
							"" + App.getUserID() );
					relationEvaluationFrg.upDate();
					//
					if (menuEdit != null) { // 可编辑
						menuEdit.setVisible(true);
					}
					if (menuJoin != null) {
						menuJoin.setVisible(true);
					}
				} else {
					if (menuMessage != null) {
						menuMessage.setVisible(true);
					}
					if (menuAddFriend != null) {
						menuAddFriend.setVisible(true);
					}
					if (menuEvalation != null) {
						menuEvalation.setVisible(true);
					}
					if (menuBlackNumber != null) {
						menuBlackNumber.setVisible(true);
					}
					hometypeTv.setText("他人主页");
					// 传递评价id
					relationEvaluationFrg.getArguments().putString(
							EConsts.Key.ID,
							"" + org_customer.customer.userId );
					relationEvaluationFrg.upDate();
				}
			}
		}
	}

	// 底部的悬浮层
	public void initBottomView(CustomerProfileVo customer) {
		/** 底部加好友 */
		addFriendFLayout = (FrameLayout) findViewById(R.id.homepage_addFriendFLayout);
		/** 底部发消息 */
		sendMessageFLayout = (FrameLayout) findViewById(R.id.homepage_sendMessageFLayout);
		/** 底部等待验证 */
		waitingPassFLayout = (FrameLayout) findViewById(R.id.homepage_waitingPassFLayout);

		// Log.v("TAG", customer.friends + "");

		if (org_customer.customer.createById != Long.valueOf(App.getUserID())) {// 非自己
			// 是大数据推送过来的数据未注册的组织，底部不显示任何操作
			if ("2".equals(customer.virtual)) {
				addFriendFLayoutBool = false;
				return;
			}
			if (customer.friends.equals("3")) {// 加好友
				sendMessageFLayout.setVisibility(View.GONE);
				waitingPassFLayout.setVisibility(View.GONE);
				addFriendFLayout.setVisibility(View.VISIBLE);
				addFriendFLayout.setOnClickListener(this);
			} else if (customer.friends.equals("2")) {// 好友、发消息
				waitingPassFLayout.setVisibility(View.GONE);
				addFriendFLayout.setVisibility(View.GONE);
//				sendMessageFLayout.setVisibility(View.VISIBLE);//2016/01/20 产品新要求进行隐藏该功能
				sendMessageFLayout.setOnClickListener(this);

			} else if (customer.friends.equals("1")) {// 等待状态 、
				addFriendFLayout.setVisibility(View.GONE);
				sendMessageFLayout.setVisibility(View.GONE);
				waitingPassFLayout.setVisibility(View.VISIBLE);
			}

		}
	}
	/**
	 * 隐藏底部的悬浮层 
	 */
	public void hideAddFriendOrSendMessageBottomView() {
		addFriendFLayout.setVisibility(View.GONE);
		sendMessageFLayout.setVisibility(View.GONE);
		waitingPassFLayout.setVisibility(View.GONE);
		headerFold(0L);
	}

	/**
	 * 设置头布局数据 initHeaderViewData
	 * 
	 * @param jTContact2
	 */
	private void initHeaderViewData(CustomerProfileVo customer) {

		if (customer != null) {

			
//			if(customer.picLogo != null) {
//				ImageLoader.getInstance().clearMemoryCache();
//					Log.v("TOUXIANG", "组织客户列表---" + customer.picLogo);
//					if(TextUtils.isEmpty(customer.picLogo)||customer.picLogo.endsWith(GlobalVariable.ORG_DEFAULT_AVATAR)){
//						String last_char = "";
						String org_name = TextUtils.isEmpty(customer.shotName)?customer.name:customer.shotName;
//						if(!TextUtils.isEmpty(org_name)){
//							last_char = org_name.substring(org_name.length()-1);
//						}
//						Bitmap bm = Util.createBGBItmap(this, R.drawable.ic_group_default_avatar, R.color.avatar_text_color, R.dimen.avatar_text_size, last_char);
//						relationIv.setImageBitmap(bm);
//					}else{
//						ImageLoader.getInstance().displayImage( customer.picLogo,relationIv);
						ImageLoader.getInstance().displayImage( customer.picLogo,relationBgRl);// 背景头像
//					}
//			}
//			
			Util.initAvatarImage(this, relationIv, org_name, customer.picLogo,0, 2);

//			ImageLoader.getInstance().displayImage( customer.picLogo,relationIv);

			if (!customer.shotName.equals("") && customer.shotName != null
					&& !customer.shotName.equals("null")) {
				homepage_name_tv.setText(customer.shotName);
			} else {
				homepage_name_tv.setText(customer.name);
			}
			if (!"2".equals(customer.virtual)) {
				homepage_QRCode.setVisibility(View.VISIBLE);
				homepage_QRCode.setOnClickListener(this);
			}

		}

	}

	private String qRCodeStr;

	private class ViewPagerAdapter extends FragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

		/*	if (position == 0) {
				return orgFrgFlow;
			} else*/ if (position == 0) {
				return mOrgMyHomePageDataFragment;
			} else if (position == 1) {
				return relationEvaluationFrg;
			} else {
				return ListViewFragment.newInstance(position);
			}

		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
//			case 0:
//				return "动态";
			case 0:
				return "资料";
			case 1:
				return "评价";
			}
			return "";
		}
	}

	@Override
	public boolean isViewBeingDragged(MotionEvent event) {
		return mScrollableListenerArrays.valueAt(mViewPager.getCurrentItem())
				.isViewBeingDragged(event);
	}

	@Override
	public void onMoveStarted(float eventY) {

	}
	/**
	 * 头布局缩进
	 * @param duration
	 */
	private void headerFold(long duration) {
		ViewCompat.animate(mHeaderLayoutView)
				.translationY(-(mHeaderHeight - titlebarHeight))
				.setDuration(duration).setInterpolator(mInterpolator).start();

		ViewCompat.animate(mViewPager).translationY(0 + mTabHeight)
				.setDuration(duration).setInterpolator(mInterpolator).start();
		titlebar.setAlpha(1F);
		mViewPagerHeaderHelper.setHeaderExpand(false);
		relationHomeBackIv.setImageDrawable(getResources().getDrawable(R.drawable.relation_home_back_full));
		hometypeTv.setTextColor(Color.rgb(0, 0, 0));
		relationShareIv.setImageDrawable(getResources().getDrawable(R.drawable.forward_share_white_full));
		relationMoreIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_overflow_full));
	}
	/**
	 * 头布局展开
	 * @param duration
	 */
	private void headerExpand(long duration) {
		titlebar.setAlpha(0.01F);
		relationHomeBackIv.setImageDrawable(getResources().getDrawable(R.drawable.left_arrow));
		hometypeTv.setTextColor(Color.rgb(255, 255, 255));
		relationShareIv.setImageDrawable(getResources().getDrawable(R.drawable.forward_share_white));
		relationMoreIv.setImageDrawable(getResources().getDrawable(R.drawable.more_whilte));
		ViewCompat.animate(mHeaderLayoutView).translationY(0)
				.setDuration(duration).setInterpolator(mInterpolator).start();
		ViewCompat.animate(mViewPager).translationY(mHeaderHeight)
				.setDuration(duration).setInterpolator(mInterpolator).start();
		mViewPagerHeaderHelper.setHeaderExpand(true);
	}
	/**
	 * 移动时触发的API
	 */
	@Override
	public void onMove(float eventY, float yDx) {
		float headerTranslationY = ViewCompat
				.getTranslationY(mHeaderLayoutView) + yDx;
		if (headerTranslationY >= 0) { // pull end
			headerExpand(0L);
		} else if (headerTranslationY <= -mHeaderHeight) { // push end
			headerFold(0L);
		} else {
			ViewCompat.animate(mHeaderLayoutView)
					.translationY(headerTranslationY).setDuration(0).start();
			ViewCompat.animate(mViewPager)
					.translationY(headerTranslationY + mHeaderHeight)
					.setDuration(0).start();
		}
	}
	/**
	 * 移动结束时触发的API
	 */
	@Override
	public void onMoveEnded(boolean isFling, float flingVelocityY) {

		float headerY = ViewCompat.getTranslationY(mHeaderLayoutView); // 0到负数
		if (headerY == 0 || headerY == -mHeaderHeight) {
			return;
		}

		if (mViewPagerHeaderHelper.getInitialMotionY()
				- mViewPagerHeaderHelper.getLastMotionY() < -mTouchSlop) { // pull>mTouchSlop=expand
			headerExpand(headerMoveDuration(true, headerY, isFling,
					flingVelocityY));
		} else if (mViewPagerHeaderHelper.getInitialMotionY()
				- mViewPagerHeaderHelper.getLastMotionY() > mTouchSlop) { // push>mTouchSlop=fold
			headerFold(headerMoveDuration(false, headerY, isFling,
					flingVelocityY));
		} else {
			if (headerY > -mHeaderHeight / 2f) { // headerY > header/2 = expand
				headerExpand(headerMoveDuration(true, headerY, isFling,
						flingVelocityY));
			} else { // headerY < header/2= fold
				headerFold(headerMoveDuration(false, headerY, isFling,
						flingVelocityY));
			}
		}

	}

	private long headerMoveDuration(boolean isExpand, float currentHeaderY,
			boolean isFling, float velocityY) {

		long defaultDuration = DEFAULT_DURATION;

		if (isFling) {

			float distance = isExpand ? Math.abs(mHeaderHeight)
					- Math.abs(currentHeaderY) : Math.abs(currentHeaderY);
			velocityY = Math.abs(velocityY) / 1000;

			defaultDuration = (long) (distance / velocityY * DEFAULT_DAMPING);

			defaultDuration = defaultDuration > DEFAULT_DURATION ? DEFAULT_DURATION
					: defaultDuration;
		}

		return defaultDuration;
	}

	@Override
	public void onFragmentAttached(ScrollableListener listener, int position) {
		mScrollableListenerArrays.put(position, listener);
	}

	@Override
	public void onFragmentDetached(ScrollableListener fragment, int position) {
		mScrollableListenerArrays.remove(position);
	}

	@Override
	public boolean onLayoutInterceptTouchEvent(MotionEvent ev) {
		return mViewPagerHeaderHelper.onLayoutInterceptTouchEvent(ev,
				mTabHeight + mHeaderHeight);
	}

	@Override
	public boolean onLayoutTouchEvent(MotionEvent ev) {
		return mViewPagerHeaderHelper.onLayoutTouchEvent(ev);
	}

	public void doNewflow(Object object) {

	}

	boolean addFriendFLayoutBool = false;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.homepage_addFriendFLayout:// 加好友
			sendMessageFLayout.setVisibility(View.GONE);
			waitingPassFLayout.setVisibility(View.VISIBLE);
			addFriendFLayout.setVisibility(View.GONE);

			JSONObject jb = OrganizationReqUtil
					.getReqNewFriend(org_customer.customer.createById);
			OrganizationReqUtil.doReqNewFriend(this, this, jb, null);
			addFriendFLayoutBool = true;
			showLoadingDialog();
			break;
		case R.id.homepage_sendMessageFLayout:// 发消息
			ChatDetail chatDetail = new ChatDetail();
			chatDetail.setThatID(org_customer.customer.userId + "");
			chatDetail.setThatImage(org_customer.customer.picLogo);
			chatDetail.setThatName(org_customer.customer.name);
			ENavigate.startIMActivity(OrgMyHomePageActivity.this, chatDetail);
			break;
		case R.id.homepage_relationShareIv:// 分享
			if (org_customer != null) {
				if (org_customer.customer!=null) {
				
				JTFile jTFile = new JTFile();
				jTFile.mTaskId =org_customer.customer.userId+"";
				jTFile.reserved2 = org_customer.customer.createById + "";
				jTFile.reserved3 = org_customer.customer.comeId + "";
				jTFile.setmUrl(org_customer.customer.picLogo);
				jTFile.fileName =org_customer.customer.shotName;
				if (StringUtils.isEmpty(org_customer.customer.shotName)) {
					jTFile.fileName =org_customer.customer.name;
				}
				jTFile.setmSuffixName(org_customer.customer.discribe);
				// if (customer.industrys.size() != 0) {
				// jTFile.setReserved1(customer.industrys.toString());
				// }
//				jTFile.setReserved1(org_customer.customer.discribe);//Reserved1字段后台有字数限制，iOS及前段都把discribe传到mSuffixName里
				jTFile.mModuleType =9;
				jTFile.mFileSize = 0;
				jTFile.setmType(JTFile.TYPE_ORGANIZATION);
				jTFile.virtual = org_customer.customer.virtual;
				FrameWorkUtils.showSharePopupWindow2(
						OrgMyHomePageActivity.this, jTFile);
				}
			}
			break;
		case R.id.homepage_relationMoreIv:
			// 分：更多 弹出对话框 编辑功能跳转到组织资料
			// 分:我的好友和非好友，我的客户和他人客户
			if (org_customer != null) {
				if (org_customer.customer != null) {
					if (org_customer.customer.createById == Long.valueOf(App
							.getUserID())) {// 我的主页
						// ：编辑，取消功能

						dlg = new AlertDialog.Builder(this).create();
						dlg.show();
						dlg.setCanceledOnTouchOutside(true);
						window = dlg.getWindow();
						// *** 主要就是在这里实现这种效果的.
						// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
						window.setContentView(R.layout.org_myhomepage_more_view);

						/*
						 * WindowManager.LayoutParams lp =
						 * window.getAttributes();
						 * 
						 * window.setGravity(Gravity.BOTTOM |
						 * Gravity.CENTER_HORIZONTAL);
						 * 
						 * lp.y = 20;
						 * 
						 * window.setAttributes(lp);
						 */
						// 编辑
						org_edit = (TextView) window
								.findViewById(R.id.org_edit);

						org_edit.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								dlg.dismiss();

								intent = new Intent(OrgMyHomePageActivity.this,
										OrganizationDataActivity.class);
								// 传递组织详情数据
								intent.putExtra("customer",
										org_customer.customer);

								// 传递customerId
								intent.putExtra("customerId", userId);

								Log.v("TAG", "传入的customerId---->" + userId);
								//
								// // 传递最新公告
								// intent.putExtra("customerNoticeList",
								// customerNoticeList);
								//
								// // 传递最新资讯
								// intent.putExtra("pushKnowledgeNoticeList",
								// pushKnowledgeNoticeList);
								//
								// // 传递 投资意向/融资/专家需求/专家身份
								// intent.putExtra("resource", resource);
								//
								// // 传递关联4大组键信息
								// intent.putExtra("contactsList",
								// contactsList);
								// intent.putExtra("organizationList",
								// organizationList);
								// intent.putExtra("knowledgeList",
								// knowledgeList);
								// intent.putExtra("eventList", eventList);
								//
								// // 传递高层治理4个集合数据
								// intent.putExtra("dshList", dshList);
								// intent.putExtra("jshList", jshList);
								// intent.putExtra("ggList", ggList);
								// intent.putExtra("ggjzList", ggjzList);
								//
								// // 传递股东研究数据
								// // 股东研究:customerStock
								// 流通股东:customerTenStockList
								// // 十大股东:customerLtStockList
								// intent.putExtra("customerStock",
								// customerStock);
								// intent.putExtra("customerTenStockList",
								// customerTenStockList);
								// intent.putExtra("customerLtStockList",
								// customerLtStockList);
								//
								// // 传递财务分析数据
								// intent.putExtra("customerFinanceDetail",
								// customerFinanceDetail);
								//
								// // 传递同业竞争详情数据
								// intent.putExtra("peerList", peerList);
								//
								// // 传递研究报告数据
								// intent.putExtra("customerPersonalLineList",
								// customerPersonalLineList);

								startActivity(intent);

							}
						});
						// 取消
						org_cancel = (TextView) window
								.findViewById(R.id.org_cancel);

						org_cancel
								.setOnClickListener(new View.OnClickListener() {
									public void onClick(View v) {

										dlg.dismiss();

									}
								});

					} else {// 他人主页 :好友 ,非好友
						// if (customer != null) {

						if (org_customer.customer.friends.equals("2")) {// 好友:分能转为客户，和不能转为客户
							if (org_customer.customer.comeId != 0) {

								// 该用户为我的客户时显示 我的客户这张图片mLinearLayout
								mLinearLayout.setVisibility(View.VISIBLE);

								dlg = new AlertDialog.Builder(this).create();
								dlg.show();
								dlg.setCanceledOnTouchOutside(true);
								window = dlg.getWindow();

								//
								window.setContentView(R.layout.widget_popup_relation_friend_menu_mycustomer);
								/*
								 * WindowManager.LayoutParams lp =
								 * window.getAttributes();
								 * 
								 * window.setGravity(Gravity.CENTER |
								 * Gravity.CENTER_HORIZONTAL);
								 * 
								 * lp.y = 20;
								 * 
								 * window.setAttributes(lp);
								 */

								// 查看客户
								TextView org_seeCustomerTv = (TextView) window
										.findViewById(R.id.org_seeCustomerTv);
								if (org_customer.customer.comeId != 0) {
									org_seeCustomerTv.setText("查看客户");
								}
								org_seeCustomerTv
										.setOnClickListener(new OnClickListener() {// 先是转为客户，再是查看客户
											@Override
											public void onClick(View v) {
												dlg.dismiss();
												if (org_customer.customer.comeId != 0) {
													ENavigate
															.startClientDedailsActivity(
																	OrgMyHomePageActivity.this,
																	org_customer.customer.comeId);
												} else {
													ENavigate
															.startCreateClienteleActivity(
																	OrgMyHomePageActivity.this,
																	org_customer,
																	4,
																	org_customer.customer.customerId);
												}
											}
										});
								// 解除好友
								TextView org_deletFriendTv = (TextView) window
										.findViewById(R.id.org_deletFriendTv);
								org_deletFriendTv
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dlg.dismiss();
												showLoadingDialog();
												// 解除好友关系客户图片隐藏
												mLinearLayout.setVisibility(View.GONE);
												// 删除好友
												OrganizationReqUtil.dodeleteFriend(OrgMyHomePageActivity.this,OrgMyHomePageActivity.this,org_customer.customer.createById,1, null);
												
											}
										});
								// 对接
								org_jointTv = (TextView) window
										.findViewById(R.id.org_jointTv);
								org_jointTv
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dlg.dismiss();
												Org_de_id de_id = new Org_de_id();
												de_id.id = userId;
												ENavigate
														.startJointResourceActivity(
																OrgMyHomePageActivity.this,
																ResourceType.Organization,
																de_id);
											}
										});
								// 举报
								TextView org_report = (TextView) window
										.findViewById(R.id.org_friend_report);
								org_report
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dlg.dismiss();
												ENavigate
														.startOrgReportActivity(
																OrgMyHomePageActivity.this,
																org_customer.customer.customerId);
											}
										});
								// 转换为事务
								TextView org_change2work = (TextView) window
										.findViewById(R.id.org_change2work);
								org_change2work
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dlg.dismiss();
												ConnectionNode inOrganizNode = new ConnectionNode();
												ArrayList<Connections> listConnections = new ArrayList<Connections>();
												
												OrganizationMini organizationMini = new OrganizationMini();
												organizationMini.id = org_customer.customer.customerId
														+ "";
												organizationMini.isOnline = true;
												organizationMini.logo = org_customer.customer.picLogo;
												organizationMini.fullName = org_customer.customer.name;
												organizationMini.shortName = org_customer.customer.shotName;
												Connections connections = new Connections(organizationMini);
												listConnections
														.add(connections);
												inOrganizNode
														.setListConnections(listConnections);
												inOrganizNode.setMemo("组织");
												ENavigate
														.startNewAffarActivityByRelation(
																OrgMyHomePageActivity.this,
																null,
																inOrganizNode,
																null, null);
											}
										});
								// 加入黑名单
								org_blackNumTv = (TextView) window
										.findViewById(R.id.org_blackNumTv);
								org_blackNumTv
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dlg.dismiss();
												showAddBlackNumDialog();// 加入黑名单
											}
										});

							} else {// 不能转为客户 功能： 解除好友 ，存为客户，对接，加入黑名单

								dlg = new AlertDialog.Builder(this).create();
								dlg.show();
								dlg.setCanceledOnTouchOutside(true);
								window = dlg.getWindow();

								//
								window.setContentView(R.layout.widget_popup_relation_friend_menu_hiscustomer);
								/*
								 * WindowManager.LayoutParams lp =
								 * window.getAttributes();
								 * 
								 * window.setGravity(Gravity.CENTER |
								 * Gravity.CENTER_HORIZONTAL);
								 * 
								 * lp.y = 20;
								 * 
								 * window.setAttributes(lp);
								 */

								// 存为客户
								TextView org_scaveCustomerTv = (TextView) window
										.findViewById(R.id.org_scaveCustomerTv);
								org_scaveCustomerTv
										.setOnClickListener(new OnClickListener() {// 存为客户(跳转到创建客户界面)
											@Override
											public void onClick(View v) {
												dlg.dismiss();
												mLinearLayout
														.setVisibility(View.VISIBLE);
												if (org_customer.customer.comeId != 0) {
													ENavigate
															.startClientDedailsActivity(
																	OrgMyHomePageActivity.this,
																	org_customer.customer.comeId);
												} else {
													ENavigate
															.startCreateClienteleActivity(
																	OrgMyHomePageActivity.this,
																	org_customer,
																	4,
																	org_customer.customer.customerId);
												}
											}
										});
								// 解除好友
								TextView org_his_deletFriendTv = (TextView) window
										.findViewById(R.id.org_his_deletFriendTv);
								org_his_deletFriendTv
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dlg.dismiss();
												showLoadingDialog();
												// 解除好友关系客户图片隐藏
												mLinearLayout
														.setVisibility(View.GONE);
												// 删除好友
												OrganizationReqUtil.dodeleteFriend(OrgMyHomePageActivity.this,OrgMyHomePageActivity.this,org_customer.customer.createById,1, null);
											}
										});
								// 对接
								TextView org_his_jointTv = (TextView) window
										.findViewById(R.id.org_his_jointTv);
								org_his_jointTv
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dlg.dismiss();
												Org_de_id de_id = new Org_de_id();
												de_id.id = userId;
												ENavigate
														.startJointResourceActivity(
																OrgMyHomePageActivity.this,
																ResourceType.Organization,
																de_id);
											}
										});
								// 举报
								TextView org_report = (TextView) window
										.findViewById(R.id.org_nofriend_report);
								org_report
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dlg.dismiss();
												ENavigate
														.startOrgReportActivity(
																OrgMyHomePageActivity.this,
																org_customer.customer.customerId);
											}
										});
								// 转换为事务
								TextView org_change2work = (TextView) window
										.findViewById(R.id.org_change2work);
								org_change2work
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dlg.dismiss();
												ConnectionNode inOrganizNode = new ConnectionNode();
												ArrayList<Connections> listConnections = new ArrayList<Connections>();
												OrganizationMini organizationMini = new OrganizationMini();
												
												organizationMini.id = org_customer.customer.userId+"";
												organizationMini.logo = org_customer.customer.picLogo;
												organizationMini.isOnline = true;
												organizationMini.fullName = TextUtils.isEmpty(org_customer.customer.shotName)?org_customer.customer.name:org_customer.customer.shotName;
												organizationMini.shortName = TextUtils.isEmpty(org_customer.customer.shotName)?org_customer.customer.name:org_customer.customer.shotName;
												Connections connections = new Connections(organizationMini);
												listConnections
														.add(connections);
												inOrganizNode
														.setListConnections(listConnections);
												inOrganizNode.setMemo("组织");
												ENavigate
														.startNewAffarActivityByRelation(
																OrgMyHomePageActivity.this,
																null,
																inOrganizNode,
																null, null);
											}
										});
								// 加入黑名单
								TextView org_his_blackNumTv = (TextView) window
										.findViewById(R.id.org_his_blackNumTv);
								org_his_blackNumTv
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dlg.dismiss();
												showAddBlackNumDialog();// 加入黑名单
											}
										});

								/*
								 * final
								 * RelationFriendHomeMenuPopupWindowHisCustomer
								 * relationFriendHomeMenuPopupWindow = new
								 * RelationFriendHomeMenuPopupWindowHisCustomer(
								 * this); relationFriendHomeMenuPopupWindow
								 * .showAsDropDown(headerVi); if
								 * (org_customer.customer.comeId!=0) {
								 * relationFriendHomeMenuPopupWindow
								 * .ChangeSeeCustomer(); }
								 * relationFriendHomeMenuPopupWindow
								 * .setOnItemClickListener(new
								 * OnFriendHomeMenuItemClickListenerHisCustomer
								 * () {
								 * 
								 * @Override public void scaveCustomer() {//
								 * 存为客户(跳转到创建客户界面) // OrganizationReqUtil //
								 * .doRequestWebAPI( //
								 * OrgMyHomePageActivity.this, //
								 * OrgMyHomePageActivity.this, // org_customer,
								 * // null, //
								 * OrganizationReqType.ACCESS_TO_THE_PRIMARY_KEY
								 * );
								 * 
								 * mLinearLayout .setVisibility(View.VISIBLE);
								 * if (org_customer.customer.comeId!=0) {
								 * ENavigate .startClientDedailsActivity(
								 * OrgMyHomePageActivity.this,
								 * org_customer.customer.comeId); }else{
								 * ENavigate.startCreateClienteleActivity(
								 * OrgMyHomePageActivity.this, org_customer, 4,
								 * org_customer.customer.customerId); }
								 * 
								 * // intent = new Intent( //
								 * OrgMyHomePageActivity.this, //
								 * Create_Clientele_Activity.class); // // //
								 * 传递组织详情数据 // intent.putExtra("customer", //
								 * org_customer); // // // 传递customerId· //
								 * intent.putExtra("customerId", // userId); //
								 * // Log.v("TAG", "传入的customerId---->" // +
								 * userId); // // // 传递最新公告 // intent.putExtra(
								 * // "customerNoticeList", //
								 * customerNoticeList); // // // 传递最新资讯 //
								 * intent.putExtra( //
								 * "pushKnowledgeNoticeList", //
								 * pushKnowledgeNoticeList); // // // 传递
								 * 投资意向/融资/专家需求/专家身份 //
								 * intent.putExtra("resource", // resource); //
								 * // // 传递关联4大组键信息 //
								 * intent.putExtra("contactsList", //
								 * contactsList); //
								 * intent.putExtra("organizationList", //
								 * organizationList); //
								 * intent.putExtra("knowledgeList", //
								 * knowledgeList); //
								 * intent.putExtra("eventList", // eventList);
								 * // // // 传递高层治理4个集合数据 //
								 * intent.putExtra("dshList", dshList); //
								 * intent.putExtra("jshList", jshList); //
								 * intent.putExtra("ggList", ggList); //
								 * intent.putExtra("ggjzList", // ggjzList); //
								 * // // 传递股东研究数据 // // 股东研究:customerStock // //
								 * 流通股东:customerTenStockList // //
								 * 十大股东:customerLtStockList //
								 * intent.putExtra("customerStock", //
								 * customerStock); // intent.putExtra( //
								 * "customerTenStockList", //
								 * customerTenStockList); // intent.putExtra( //
								 * "customerLtStockList", //
								 * customerLtStockList); // // // 传递财务分析数据 //
								 * intent.putExtra( // "customerFinanceDetail",
								 * // customerFinanceDetail); // // //
								 * 传递同业竞争详情数据 // intent.putExtra("peerList", //
								 * peerList); // // // 传递研究报告数据 //
								 * intent.putExtra( //
								 * "customerPersonalLineList", //
								 * customerPersonalLineList);
								 * 
								 * }
								 * 
								 * @Override public void deletFriend() {//
								 * 解除好友关系 showLoadingDialog(); // 解除好友关系客户图片隐藏
								 * mLinearLayout .setVisibility(View.GONE); //
								 * 删除好友 OrganizationReqUtil.dodeleteFriend(
								 * OrgMyHomePageActivity.this,
								 * OrgMyHomePageActivity.this,
								 * org_customer.customer.createById, 1, null);
								 * 
								 * }
								 * 
								 * @Override public void joint() {// 对接
								 * Org_de_id de_id = new Org_de_id(); de_id.id =
								 * userId; ENavigate
								 * .startJointResourceActivity(
								 * OrgMyHomePageActivity.this,
								 * ResourceType.Organization, de_id); }
								 * 
								 * @Override public void blackNum() {
								 * showAddBlackNumDialog();// 加入黑名单 }
								 * 
								 * //组织转换为事务
								 * 
								 * @Override public void change2work() {
								 * ConnectionNode inOrganizNode = new
								 * ConnectionNode(); ArrayList<Connections>
								 * listConnections = new
								 * ArrayList<Connections>(); Connections
								 * connections = new Connections();
								 * connections.type =
								 * Integer.parseInt(org_customer
								 * .customer.type.equals
								 * ("")?"2":org_customer.customer.type);
								 * connections.id =
								 * org_customer.customer.userId+"";
								 * connections.organizationMini.logo =
								 * org_customer.customer.picLogo;
								 * connections.organizationMini.fullName =
								 * org_customer.customer.name;
								 * connections.organizationMini.shortName =
								 * org_customer.customer.shotName;
								 * listConnections.add(connections);
								 * inOrganizNode
								 * .setListConnections(listConnections);
								 * inOrganizNode.setMemo("组织");
								 * ENavigate.startNewAffarActivityByRelation
								 * (OrgMyHomePageActivity.this, null,
								 * inOrganizNode, null, null); }
								 * 
								 * @Override public void report() { //举报
								 * ENavigate
								 * .startOrgReportActivity(OrgMyHomePageActivity
								 * .this,org_customer.customer.customerId ); }
								 * });
								 */
							}

						} else if (org_customer.customer.friends.equals("3")) {// 非好友：2-非好友
							addFriendFLayoutBool = false;
							showAddFriendDialog();

						} else if (org_customer.customer.friends.equals("1")) {// 等待验证:1,3
							addFriendFLayoutBool = true;
							showAddFriendDialog();
						}

					}
				}
			}

			break;
		case R.id.homepage_relationHomeBackIv:// 返回
			finish();
			break;
		// 点击客户图片跳转到客户详情(该客户为我的客户的情况下)
		case R.id.homepage_layout_his_homePage:

			// intent = new Intent(this, Client_DetailsActivity.class);
			// intent.putExtra(EConsts.Key.CUSTOMERID, userId);
			// intent.putExtra("label", label);
			//
			// startActivityForResult(intent, 12345);
			ENavigate.startClientDedailsActivity(this,
					org_customer.customer.comeId);
			break;

		// 跳转到二维码界面
		case R.id.homepage_QRCode:

			qRCodeStr = EAPIConsts.getTMSUrl() + "org/qr?customerId=" + userId;
			if (!StringUtils.isEmpty(qRCodeStr)) {
				ENavigate.startQRCodeActivity(OrgMyHomePageActivity.this,
						qRCodeStr, org_customer.customer.name,
						org_customer.customer.picLogo);
			}
			break;

		default:
			break;
		}
	}

	// 是好友同时是客户

	public void MyFriendsAndCustomer() {

		window.setContentView(R.layout.org_friends_dialog_view);

		// 畅聊，用金桐的
		chatTv = (TextView) window.findViewById(R.id.chatTv);

		chatTv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				dlg.dismiss();

			}
		});

		// 举报 风行在做
		orgReportTv = (TextView) window.findViewById(R.id.orgReportTv);

		orgReportTv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				dlg.dismiss();
				ENavigate.startOrgReportActivity(OrgMyHomePageActivity.this,
						org_customer.customer.customerId);

			}
		});

		// 对接 用金桐
		orgDockingTv = (TextView) window.findViewById(R.id.orgDockingTv);

		orgDockingTv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				dlg.dismiss();
				Org_de_id de_id = new Org_de_id();
				de_id.id = userId;
				ENavigate.startJointResourceActivity(
						OrgMyHomePageActivity.this, ResourceType.Organization,
						de_id);
			}
		});

		// 解除好友
		liftFriendTv = (TextView) window.findViewById(R.id.liftFriendTv);

		liftFriendTv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				dlg.dismiss();

				builder = new Builder(OrgMyHomePageActivity.this);

				dialog = builder.show();

				window = dialog.getWindow();

				window.setContentView(R.layout.org_preservation_dialog);

				org_preservation_yes = (TextView) window
						.findViewById(R.id.org_preservation_yes);
				org_preservation_yes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialog.dismiss();

						ToastUtil.showToast(OrgMyHomePageActivity.this, "解除成功");
					}
				});

				org_preservation_no = (TextView) window
						.findViewById(R.id.org_preservation_no);
				org_preservation_no.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialog.dismiss();

					}
				});

			}
		});

		// 查看客户
		seeCustomerTv = (TextView) window.findViewById(R.id.seeCustomerTv);
		seeCustomerTv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				dlg.dismiss();

				ENavigate.startClientDedailsActivity(
						OrgMyHomePageActivity.this,
						org_customer.customer.comeId, 2, 6);

				// Intent intent = new Intent(OrgMyHomePageActivity.this,
				// InviteFriendsActivity.class);
				//
				// startActivity(intent);

			}
		});

		// 加入黑名单
		addBlackList = (TextView) window.findViewById(R.id.addBlackList);
		addBlackList.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				dlg.dismiss();

				builder = new Builder(OrgMyHomePageActivity.this);

				dialog = builder.show();

				window = dialog.getWindow();

				window.setContentView(R.layout.org_add_black_list);

				org_add_preservation_yes = (TextView) window
						.findViewById(R.id.org_add_preservation_yes);
				org_add_preservation_yes
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								dialog.dismiss();
								ToastUtil.showToast(OrgMyHomePageActivity.this,
										"该客户已被加入黑名单");
							}
						});

				org_add_preservation_no = (TextView) window
						.findViewById(R.id.org_add_preservation_no);
				org_add_preservation_no
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								dialog.dismiss();

							}
						});

			}
		});

	}

	// 是好友但不一定是客户

	public void NoMyFriendsAndCustomer() {

		window.setContentView(R.layout.org_nofriends_dialog_view);

		// 举报
		noFriendsReportTv = (TextView) window
				.findViewById(R.id.noFriendsReportTv);

		noFriendsReportTv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				dlg.dismiss();

				ENavigate.startOrgReportActivity(OrgMyHomePageActivity.this,
						org_customer.customer.customerId);

			}
		});

		// 加入黑名单
		noFriendsAddBlackList = (TextView) window
				.findViewById(R.id.noFriendsAddBlackList);

		noFriendsAddBlackList.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				dlg.dismiss();

				builder = new Builder(OrgMyHomePageActivity.this);

				dialog = builder.show();

				window = dialog.getWindow();

				window.setContentView(R.layout.org_add_black_list);

				org_add_preservation_yes = (TextView) window
						.findViewById(R.id.org_add_preservation_yes);
				org_add_preservation_yes
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								dialog.dismiss();
								ToastUtil.showToast(OrgMyHomePageActivity.this,
										"该客户已被加入黑名单");
							}
						});

				org_add_preservation_no = (TextView) window
						.findViewById(R.id.org_add_preservation_no);
				org_add_preservation_no
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								dialog.dismiss();

							}
						});

			}
		});

		// 对接
		noFriendsDocking = (TextView) window
				.findViewById(R.id.noFriendsDocking);

		noFriendsDocking.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				dlg.dismiss();
				Org_de_id de_id = new Org_de_id();
				de_id.id = userId;
				ENavigate.startJointResourceActivity(
						OrgMyHomePageActivity.this, ResourceType.Organization,
						de_id);

			}
		});

		// 转为客户
		noFriendsToCustomer = (TextView) window
				.findViewById(R.id.noFriendsToCustomer);
		noFriendsToCustomer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.dismiss();
				ENavigate.startCreateClienteleActivity(
						OrgMyHomePageActivity.this, org_customer, 4,
						org_customer.customer.customerId);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.relationdhome, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

	private void showAddFriendDialog() {

		Log.v("TAG", "------------------------------------------------------");
		dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		dlg.setCanceledOnTouchOutside(true);
		window = dlg.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.widget_popup_relation_other_menu_org);
		/*
		 * WindowManager.LayoutParams lp = window.getAttributes();
		 * window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL); lp.y =
		 * 20; window.setAttributes(lp);
		 */

		org_jointTv = (TextView) window.findViewById(R.id.org_jointTv);// 对接
		org_addFriendTv = (TextView) window.findViewById(R.id.org_addFriendTv);// 加好友
		org_blackNumTv = (TextView) window.findViewById(R.id.org_blackNumTv);// 加黑名单
		org_ReportTv = (TextView) window.findViewById(R.id.org_ReportTv);// 举报
		lineView1 = window.findViewById(R.id.lineView1);
		lineView2 = window.findViewById(R.id.lineView2);
		if (addFriendFLayoutBool == false) {
			hintAddFriendTv();
			hintAddBlackListTv();
		}
		// 加好友
		org_addFriendTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				JSONObject jb = OrganizationReqUtil
						.getReqNewFriend(org_customer.customer.createById);
				OrganizationReqUtil.doReqNewFriend(OrgMyHomePageActivity.this,
						OrgMyHomePageActivity.this, jb, null);
				showLoadingDialog();
			}
		});
		// 对接
		org_jointTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				Org_de_id de_id = new Org_de_id();
				de_id.id = userId;
				ENavigate.startJointResourceActivity(
						OrgMyHomePageActivity.this, ResourceType.Organization,
						de_id);
			}
		});
		// 举报
		org_ReportTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				ENavigate.startOrgReportActivity(OrgMyHomePageActivity.this,
						org_customer.customer.customerId);
			}
		});
		// 加入黑名单
		org_blackNumTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				showAddBlackNumDialog();// 加黑名单
			}
		});

		/*
		 * RelationOrtherHomeMenuPopupWindowOrganization
		 * relationOrtherHomeMenuPopupWindowOrganization = new
		 * RelationOrtherHomeMenuPopupWindowOrganization( this);
		 * relationOrtherHomeMenuPopupWindowOrganization
		 * .showAsDropDown(headerVi); // .showAsDropDown(headerVi); if
		 * (addFriendFLayoutBool==false) {
		 * relationOrtherHomeMenuPopupWindowOrganization.hintAddFriendTv();
		 * relationOrtherHomeMenuPopupWindowOrganization.hintAddBlackListTv(); }
		 * relationOrtherHomeMenuPopupWindowOrganization
		 * .setOnItemClickListener(new OnOrtherHomeMenuItemClickListeners() {
		 * 
		 * @Override public void joint() {// 对接
		 * 
		 * Org_de_id de_id = new Org_de_id(); de_id.id = userId;
		 * ENavigate.startJointResourceActivity( OrgMyHomePageActivity.this,
		 * ResourceType.Organization, de_id);
		 * 
		 * 
		 * }
		 * 
		 * @Override public void blackNum() {// 加黑名单 showAddBlackNumDialog(); }
		 * 
		 * @Override public void addFriend() {// 加好友 JSONObject jb =
		 * OrganizationReqUtil
		 * .getReqNewFriend(org_customer.customer.createById);
		 * OrganizationReqUtil.doReqNewFriend( OrgMyHomePageActivity.this,
		 * OrgMyHomePageActivity.this, jb, null); showLoadingDialog(); }
		 * 
		 * @Override public void ToReport() { //举报
		 * ENavigate.startOrgReportActivity
		 * (OrgMyHomePageActivity.this,org_customer.customer.customerId ); } });
		 */
	}

	/** 加入黑名单 */
	private void showAddBlackNumDialog() {
		new MessageDialog(OrgMyHomePageActivity.this,
				"加入黑名单你将收不到对方的信息,\n并且你们相互看不到对方的主页",
				new OnDialogFinishListener() {
					@Override
					public void onFinish(String content) {
						if (org_customer != null) {
							ArrayList<String> arrayList = new ArrayList<String>();
							arrayList.add(org_customer.customer.userId + "");
							OrganizationReqUtil.doEditBlack(
									OrgMyHomePageActivity.this,
									OrgMyHomePageActivity.this, arrayList,
									types[0], null);
							showLoadingDialog();
						}
					}

					@Override
					public void onCancel(String content) {
						// TODO Auto-generated method stub
						
					}
				}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		switch (requestCode) {

		case result_edit:// 编辑人脉
			// if (resultCode == Activity.RESULT_OK) {
			// showLoadingDialog();
			// JSONObject jsonObj = ConnectionsReqUtil.getContactDetailJson(
			// userId + "", isonline);
			// ConnectionsReqUtil.doContactDetail(this, updataib, jsonObj,
			// null);
			// }
			break;
		case ENavConsts.ActivityReqCode.REQUEST_CODE_LOOK_MORE_COMMENT:// 动态
			Bundle bundle = intent.getExtras();
			List<DynamicComment> mListComment = (List<DynamicComment>) bundle
					.getSerializable(ENavConsts.KEY_FRG_CHANGE_COMMENTS);
			ArrayList<DynamicPraise> mDynamicPraises = (ArrayList<DynamicPraise>) bundle
					.getSerializable(ENavConsts.KEY_FRG_FLOW_DYNAMIC_PRAISES);
			int index = bundle.getInt(ENavConsts.KEY_FRG_FLOW_INDEX, 0);
//			orgFrgFlow.changeDynamicListUI(mDynamicPraises, mListComment, index);
			break;
		case ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_MORE_EVALUATION:// 更多评价
		case ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_EDIT_EVALUATION:// 编辑评价
			relationEvaluationFrg.onActivityResult(requestCode, resultCode,
					intent);
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	// 因为更新的特殊性，要单独写一个IBindData
	IBindData updataib = new IBindData() {
		@Override
		public void bindData(int tag, Object object) {
			dismissLoadingDialog();
			if (object != null) {
				// 刷新页面
				initViewDetail(object);
				// 数据返回，写文件缓存
				// ConnectionsCacheUtils.writeConnectionObj(type, isonline,
				// userId
				// + "", object);
			}
		}
	};
	private ImageView relationBgRlCover;
	private ActionBar actionBar;
	private View headerVi;
	private FrameLayout waitingPassFLayout;
	private ImageView titlebar;
	private int titlebarHeight;
	private TextView hometypeTv;
	private ImageView relationShareIv;
	private ImageView relationMoreIv;
	private ImageView relationHomeBackIv;
	private ImageView mLinearLayout;
	private Intent intent;
	private TextView edit_creater;
	private TextView docking_creater;
	private TextView delete_creater;
	private TextView findbody_big_happy;
	private TextView invite_to_join_big_happy;
	private TextView collection_big_happy;
	private TextView preservation_big_happy;
	private TextView report_big_happy;
	private GetId getId;
	private String msg2;
	private TextView org_jointTv;
	private TextView org_addFriendTv;
	private TextView org_blackNumTv;
	private TextView org_ReportTv;
	private View lineView1;
	private View lineView2;

	// 网络请求返回的数据都在bindData接口中
	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {

		switch (tag) {

		case OrganizationReqType.ORGANIZATION_REQ_ORGANDPROINFO:// 查询组织详情4.4
			Log.v("TAG", "进入1");

			if (object == null) {
				Log.v("TAG", "进入2");
				ToastUtil.showToast(OrgMyHomePageActivity.this, "没有请求到数据");
				finish();
				return;
			}
			Log.v("TAG", "进入3");

			map = (Map<String, Object>) object;
			// 组织用户的类
			org_customer = (CustomerOrganizatianParams) map
					.get("customer_organization_params");
			if (org_customer == null) {
				Log.v("MSG", "组织为null");
				ToastUtil.showToast(OrgMyHomePageActivity.this,
						"org_customer没有请求到数据");
				Log.v("WC", "好友状态---->" + org_customer.customer.friends);
				Log.v("WC", "加好友的id---->" + org_customer.customer.createById);
				return;

			}

			// if(createById == 0){
			// orgFrgFlow.upData(customer.createById);
			// }

			// 刷新页面
			initViewDetail(object);

			// 0 客户 1 用户注册组织 2 未注册的组织，先屏蔽掉有确定数据再打开
			// switch(Integer.parseInt(customer.virtual)){
			//
			// case 0://客户
			//
			// mLinearLayout.setVisibility(View.VISIBLE);
			//
			// break;
			// case 1://用户注册组织
			//
			// mLinearLayout.setVisibility(View.GONE);
			// 自定义
			// break;
			// case 2://未注册的组织
			//
			// mLinearLayout.setVisibility(View.GONE);
			//
			// break;
			//
			// }

			// customerNoticeList = (List<CustomerNotice>)
			// map.get("noticeList");
			//
			// pushKnowledgeNoticeList = (List<PushKnowledge>) map
			// .get("orgNewList");
			//
			// resource = (CustomerResource) map.get("result");
			//
			// relatedInformation = (RelatedInformation) map.get("relevance");
			//
			// Log.v("TAG", customer.toString());
			//
			// Log.v("TAG",
			// "customerNoticeList--->" + customerNoticeList.toString());
			//
			// Log.v("TAG", pushKnowledgeNoticeList.toString());
			//
			// Log.v("TAG", resource.toString());
			//
			// Log.v("TAG", relatedInformation.toString());

			// 最新公告
			// customerNoticeList = (ArrayList<CustomerNotice>) map
			// .get("noticeList");

			// Log.v("TAG", "最新公告<---->" + customerNoticeList.toString());

			// 最新资讯
			// pushKnowledgeNoticeList = (ArrayList<PushKnowledge>) map
			// .get("orgNewList");
			// Log.v("TAG", "最新资讯<---->" + pushKnowledgeNoticeList.toString());

			// 投资/融资/专家需求/专家身份
			// resource = (CustomerResource) map.get("result");
			// Log.v("TAG", "投资/融资/专家需求/专家身份<---->" + resource);

			/**
			 * 关联4大组键详情
			 **/

			// relatedInformation = (RelatedInformation) map.get("relevance");
			// Log.v("TAG", "关联信息数据<---->" + relatedInformation.toString());
			// if (relatedInformation != null) {
			//
			// if (relatedInformation.p != null) {
			// contactsList = (ArrayList<ASSOData>) org_customer.relevance.p;//
			// 关联人脉
			// }
			//
			// if (relatedInformation.o != null) {
			// organizationList = (ArrayList<ASSOData>)
			// org_customer.relevance.o;// 关联组织
			// }
			//
			// if (relatedInformation.k != null) {
			// knowledgeList = (ArrayList<ASSOData>) org_customer.relevance.k;//
			// 关联知识
			// }
			//
			// if (relatedInformation.r != null) {
			// eventList = (ArrayList<ASSOData>) org_customer.relevance.r;//
			// 关联事件
			// }
			//
			// }

			// Log.v("TAG", resource.toString());
			//
			// Log.v("TAG", relatedInformation.toString());

			break;

		case EAPIConsts.OrganizationReqType.ORAGANIZATION_REQ_ADD_FRIENDS:// 加好友

			if (object != null) {

				map = (Map<String, Object>) object;

				flag = (Boolean) map.get("succeed");

				if (flag) {
					ToastUtil.showToast(OrgMyHomePageActivity.this, "请求发送成功");
					// ConnectionsDBManager.getInstance(this).insert(connections);
					addFriendFLayout.setVisibility(View.GONE);
					waitingPassFLayout.setVisibility(View.VISIBLE);
				} else {
					ToastUtil.showToast(OrgMyHomePageActivity.this, "请求发送失败");
				}
			}

			break;
		case OrganizationReqType.ACCESS_TO_THE_PRIMARY_KEY: // 获取客户主键ID
			if (object != null) {
				Map<String, Object> dataHm = (Map<String, Object>) object;
				getId = (GetId) dataHm.get("getId");
				Log.e("MSG", "客户主键ID" + getId);
				isSuccess = getId.success;
				client_customer = new Customer();
				client_customer.name = org_customer.customer.name;
				client_customer.shotName = org_customer.customer.shotName;
				client_customer.auth = Integer
						.parseInt(org_customer.customer.auth);
				client_customer.createById = org_customer.customer.createById;
				client_customer.type = org_customer.customer.type;
				client_customer.industrys = org_customer.customer.industrys;
				client_customer.isListing = org_customer.customer.isListing;
				client_customer.stockNum = org_customer.customer.stockNum;
				client_customer.linkMobile = org_customer.customer.linkMobile;
				client_customer.linkEmail = org_customer.customer.linkEmail;
				client_customer.phoneList = org_customer.customer.phoneList;
				client_customer.picLogo = org_customer.customer.picLogo
						.substring(org_customer.customer.picLogo
								.indexOf("m", 0) + 1);// 要传递相对路径
				client_customer.banner = org_customer.customer.banner;
				client_customer.discribe = org_customer.customer.discribe;
				client_customer.userId = org_customer.customer.userId;
				if ("2".equals(org_customer.customer.friends)) {
					client_customer.friends = true;
				} else {
					client_customer.friends = false;
				}
				client_customer.comeId = org_customer.customer.customerId + "";
				client_customer.virtual = org_customer.customer.virtual;

				if (isSuccess) {

					if (client_customer != null) {
						OrganizationReqUtil
								.doRequestWebAPI(
										OrgMyHomePageActivity.this,
										OrgMyHomePageActivity.this,
										client_customer,
										null,
										OrganizationReqType.ORGANIZATION_REQ_CUSTOMER_SAVECUSPROFILE);

					}
				}
			}

			break;

		case OrganizationReqType.ORGANIZATION_REQ_DETAILS:// 4.11按年份，报表类型,季度，获取财务分析详情

			// Log.v("TAG", "进入5");

			if (object == null) {
				// Log.v("TAG", "进入6");
				return;
			}
			// Log.v("TAG", "进入7");

			map = (Map<String, Object>) object;

			customerFinanceDetail = (CustomerFinanceDetail) map.get("listRow");

			// Log.v("TAG", "财务分析<---->" + customerFinanceDetail.toString());

			break;
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_CUSTOMER_SAVECUSPROFILE:
			Boolean Success = (Boolean) object;
			if (Success) {
				// ENavigate.startClientDedailsActivity(OrgMyHomePageActivity.this,
				// Long.parseLong(getId.orgId));
				finish();
			}
			break;
		case OrganizationReqType.ORGANIZATION_REQ_FINDHEGHTONE:// 4.12查询高层治理
			if (object == null) {
				return;
			}

			map = (Map<String, Object>) object;
			// 查询高层治理里面的4个数据集合
			dshList = (ArrayList<CustomerHightInfo>) map.get("dshList");
			// Log.v("TAG", "查询高层治理<----dshList---->" + dshList);
			jshList = (ArrayList<CustomerHightInfo>) map.get("jshList");
			// Log.v("TAG", "查询高层治理<----jshList---->" + jshList);
			ggList = (ArrayList<CustomerHightInfo>) map.get("ggList");
			// Log.v("TAG", "查询高层治理<----ggList---->" + ggList);
			ggjzList = (ArrayList<CustomerHightInfo>) map.get("ggjzList");
			// Log.v("TAG", "查询高层治理<----ggjzList---->" + ggjzList);

			break;

		case OrganizationReqType.ORGANIZATION_REQ_FINDSTOCKONE:// 4.14查询股东研究

			if (object == null) {
				return;
			}

			map = (Map<String, Object>) object;

			// 股东研究
			customerStock = (CustomerStock) map.get("stock");
			// Log.v("TAG", "股东研究---->" + customerStock.toString());

			// 流通股东
			customerTenStockList = (ArrayList<CustomerTenStock>) map
					.get("tenStockList");

			// Log.v("TAG", "流通股东---->" + customerTenStockList.toString());

			// 十大股东
			customerLtStockList = (ArrayList<CustomerStockList>) map
					.get("ltStockList");
			// Log.v("TAG", "十大股东---->" + customerLtStockList.toString());

			break;

		case OrganizationReqType.ORGANIZATION_REQ_FINDINDUSTRY:// 4.16 查询行业动态

			if (object == null) {
				return;
			}

			map = (Map<String, Object>) object;

			customerOrgIndustryList = (ArrayList<CustomerOrgIndustry>) map
					.get("peerList");

			// Log.v("TAG", "查询行业动态---->" + customerOrgIndustryList.toString());

			if (peerList != null) {
				// 点击行业动态跳转界面

			}

			break;

		case OrganizationReqType.ORGANIZATION_REQ_FINDPER:// 4.19 查询同业竞争列表
															// customer/peer/findPeer.json
			if (object == null) {
				return;
			}

			map = (Map<String, Object>) object;

			peerList = (ArrayList<CustomerPeerInfo>) map.get("peerList");

			// Log.v("TAG", "同业竞争---->" + peerList.toString());

			if (peerList != null) {
				// 点击同业竞争跳转界面

			}

			break;

		case OrganizationReqType.ORGANIZATION_REQ_FINDREPORT:// 4.22 查询研究报告

			if (object == null) {
				return;
			}

			map = (Map<String, Object>) object;

			customerPersonalLineList = (ArrayList<CustomerPersonalLine>) map
					.get("personalLineList");

			break;

		case OrganizationReqType.EDITBLACKLIST:// 编辑黑名单

			if (object != null) {
				String success = (String) object;
				if (success.equals("true")) {
					ToastUtil.showToast(OrgMyHomePageActivity.this, "已加入黑名单");
					finish();
					return;
				} else {
					ToastUtil.showToast(OrgMyHomePageActivity.this, "加入黑名单失败");
				}
			}
			dismissLoadingDialog();

			break;

		case EAPIConsts.OrganizationReqType.BLACKLIST:// 获取黑名单列表

			break;

		case EAPIConsts.OrganizationReqType.ORG_DELETEFRIED:// 解除好友关系

			if (object != null) {
				map = (Map<String, Object>) object;

				boolean flag = (Boolean) map.get("success");

				if (flag) {
					ToastUtil.showToast(OrgMyHomePageActivity.this, "解除好友成功");
					showToast("解除好友成功");
					connsDBManager.delete(org_customer.customer.createById);
					finish();
				}
			} else {
				ToastUtil.showToast(OrgMyHomePageActivity.this, "解除好友失败");
			}

			break;

		}

	}

	// public CustomerProfileVo getCustomer() {
	// return org_customer.customer;
	// }
	public void hintAddFriendTv() {
		lineView1.setVisibility(View.GONE);
		org_addFriendTv.setVisibility(View.GONE);
	}

	public void hintAddBlackListTv() {
		lineView2.setVisibility(View.GONE);
		org_blackNumTv.setVisibility(View.GONE);
	}

}
