package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.obj.Connections;
import com.tr.model.obj.DynamicComment;
import com.tr.model.obj.DynamicPraise;
import com.tr.model.obj.JTFile;
import com.tr.model.user.OrganizationMini;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.connections.viewfrg.ListViewFragment;
import com.tr.ui.demand.MyView.MyViewPager;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.home.frg.FrgFlow;
import com.tr.ui.knowledge.swipeback.SwipeBackActivity;
import com.tr.ui.organization.model.Area;
import com.tr.ui.organization.model.Collect;
import com.tr.ui.organization.model.GetId;
import com.tr.ui.organization.model.Org_de_id;
import com.tr.ui.organization.model.param.ClientDetailsParams;
import com.tr.ui.organization.model.param.ClientDetialsIncomParams;
import com.tr.ui.organization.model.param.CustomerClientParams;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.orgfragment.OrgClientFileFragment;
import com.tr.ui.organization.orgfragment.OrgClientRelationEvaluationFrg;
import com.tr.ui.widgets.CircleImageView;
import com.tr.ui.widgets.viewpagerheaderscroll.SlidingTabLayout;
import com.tr.ui.widgets.viewpagerheaderscroll.SlidingTabLayout.TabColorizer;
import com.tr.ui.widgets.viewpagerheaderscroll.TouchCallbackLayout;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableFragmentListener;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableListener;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ViewPagerHeaderHelper;
import com.utils.common.EConsts;
import com.utils.common.GlobalVariable;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

/**
 * 客户详情页面 我的客户/他人客户
  * 启动方法：ENavigate.startClientDedailsActivity
 * 
	 * @param context 
	 * @param customerId   客户ID
	 * @param view		是否需要进行权限控制 0 受权限控制 1 不受权限控制
	 * @param label     是否需要进行权限控制 6 受权限控制 
 * 
 */
public class ClientDetailsActivity extends SwipeBackActivity implements
		TouchCallbackLayout.TouchEventListener, ScrollableFragmentListener,
		ViewPagerHeaderHelper.OnViewPagerTouchListener, OnClickListener,
		IBindData {

	/** 用户id */
	private String userId;
	/** 用来区分当前客户的类型 */
	private int type = 0;
	/** 线上：组织or线下：客户 */
	private boolean isonline;
	/**默认顶部布局高度*/
	private static final long DEFAULT_DURATION = 300L;
	private static final float DEFAULT_DAMPING = 1.5f;
	/**滑动监听器集合*/
	private SparseArrayCompat<ScrollableListener> mScrollableListenerArrays = new SparseArrayCompat<ScrollableListener>();
	/**资料，会面（目前没有），评论 */
	public MyViewPager mViewPager;
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
	private AlertDialog dlg;

	/**对话框中的Window*/
	private Window window;
	/**滑动监听器*/
	private Interpolator mInterpolator = new DecelerateInterpolator();

	private OrgClientFileFragment mOrgClientFrg;// 资料
//	private MeetingDetailsFragment mMeetingDetailsFragment;// 会面情况
	private FrgFlow frgFlow; //动态
	private OrgClientRelationEvaluationFrg relationEvaluationFrg;// 评价
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
	
	/** 客户对象*/
	public CustomerClientParams mjCustomer;

	public static final int result_recommend = 1;
	public static final int result_edit = 5;

	private TextView client_report_big_happy;
	/** 客户对象ID*/
	public long client_id;
	/** 客户对象收藏ID集合*/
	private ArrayList<Long> customerIds;
	private TextView avatarText_ClientDetails;
	@Override
	public void initJabActionBar() {
		actionBar = getActionBar();

		actionBar.setTitle(" ");
		actionBar.hide();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_org_clientdetails);

		OrganizationReqUtil.doRequestWebAPI(this, this, mjCustomer, null,
				OrganizationReqType.ACCESS_TO_THE_PRIMARY_KEY);
		
		custom = (ArrayList<CustomerPersonalLine>) getIntent().getSerializableExtra("Custom");
//		address = getIntent().getStringExtra("Address");
		address = (Area) getIntent().getSerializableExtra("Address");
		userId = getIntent().getStringExtra(EConsts.Key.ID);
		if (userId == null) {
			userId = App.getUserID();
		}
		type = getIntent().getIntExtra(ENavConsts.EFromActivityType,
				ENavConsts.type_details_other);
		isonline = getIntent().getBooleanExtra(EConsts.Key.isOnline, false); // online

		label = this.getIntent().getIntExtra("label", 1);
		Log.e("TAG", "_label------------->>>" + label);

		
		view2 = this.getIntent().getIntExtra(EConsts.Key.CONTROL, 1);//畅聊页面传来的参数
		Log.e("TAG", "control------------->>>" + view2);
		customerId = this.getIntent().getLongExtra(EConsts.Key.CUSTOMERID, -1); // 他人客户 参数
		Log.e("TAG", "customerd得到参数" + customerId);
		organ_Id = this.getIntent().getLongExtra("Organ_Id", -1); // 我的客户 参数
		Log.e("TAG", "organ_Id得到参数" + organ_Id);
//		orgcustomerId = this.getIntent().getLongExtra("orgcustomerId", 2);
//		Log.e("TAG", "orgcustomerId得到参数" + orgcustomerId);
		
		mParams = new ClientDetialsIncomParams();
		
		client_id = (customerId==-1?0:customerId)+(organ_Id==-1?0:organ_Id)+(orgcustomerId==-1?0:orgcustomerId);
		Log.v("WC", "client_id---->"+client_id);

		initFragment();

		initUI();
		

		initHeaderView();
		
		// init();
	}
	 @Override
	protected void onStart() {
		super.onStart();
		if (label == 0) {
			mParams.orgId = organ_Id;
			OrganizationReqUtil.doRequestWebAPI(this, this, mParams, null,
					OrganizationReqType.ORGANIZATION_CUSTOMER_DETILS);
		} else if (label == 1) {
			mParams.orgId = customerId;
			OrganizationReqUtil.doRequestWebAPI(this, this, mParams, null,
					OrganizationReqType.ORGANIZATION_CUSTOMER_DETILS);
			
		}else if (label == 2) {
			mParams.orgId = customerId;
			OrganizationReqUtil.doRequestWebAPI(this, this, mParams, null,
					OrganizationReqType.ORGANIZATION_CUSTOMER_DETILS);
			
		}else if (label == 3) {
			mParams.orgId = customerId;
			OrganizationReqUtil.doRequestWebAPI(this, this, mParams, null,
					OrganizationReqType.ORGANIZATION_CUSTOMER_DETILS);
			
		}else if (label == 4) {
			mParams.orgId = customerId;
			OrganizationReqUtil.doRequestWebAPI(this, this, mParams, null,
					OrganizationReqType.ORGANIZATION_CUSTOMER_DETILS);
			
		}else if (label == 5) {
			mParams.orgId = customerId;
			OrganizationReqUtil.doRequestWebAPI(this, this, mParams, null,
					OrganizationReqType.ORGANIZATION_CUSTOMER_DETILS);
			
		} else if(label == 6){
		//如果想让客户详情不受权限控制
			mParams.orgId = customerId;
			mParams.view = view2;
			OrganizationReqUtil.doRequestWebAPI(this, this, mParams, null,
					OrganizationReqType.ORGANIZATION_CUSTOMER_DETILS);
		}
		
		Log.v("WC", "client_id---->"+client_id);
	}
	 /**
	  * 初始化控件
	  */
	private void initUI() {

		client_tv = (TextView) findViewById(R.id.client_tv);

		headerVi = findViewById(R.id.headerVi);

		client_Image = (CircleImageView) findViewById(R.id.client_Image);

		// 初始化顶部titlebar
		/** titlebar的背景 */
		titlebar = (ImageView) findViewById(R.id.clienttitlebar);
		
		typeTv = (TextView) findViewById(R.id.client_typeTv);
		/** 分享按钮 */
		clientShareIv = (ImageView) findViewById(R.id.clientShareIv);
		/** 更多操作按钮 */
		clientMoreIv = (ImageView) findViewById(R.id.clientMoreIv);
		/** 主页返回 */
		orgBackIv = (ImageView) findViewById(R.id.Client_BackIv);
		iv_org_bagiamg = (ImageView) findViewById(R.id.iv_org_bagiamg);

		clientShareIv.setOnClickListener(this);

		orgBackIv.setOnClickListener(this);
		iv_org_bagiamg.setOnClickListener(this);

		titlebarHeight = getResources().getDimensionPixelSize(
				R.dimen.retion_title_height);
		mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
		mTabHeight = getResources().getDimensionPixelSize(R.dimen.tabs_height);
		mHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.viewpager_header_height);

		mViewPagerHeaderHelper = new ViewPagerHeaderHelper(this, this);

		TouchCallbackLayout touchCallbackLayout = (TouchCallbackLayout) findViewById(R.id.layout);
		touchCallbackLayout.setTouchEventListener(this);

		mHeaderLayoutView = findViewById(R.id.header);
		SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
		slidingTabLayout.setCustomTabColorizer(new TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return Color.rgb(251, 127, 5);
			}
		});
		mViewPager = (MyViewPager) findViewById(R.id.viewpager);
		viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(viewPagerAdapter);
		viewPagerAdapter.notifyDataSetChanged();

		slidingTabLayout.setViewPager(mViewPager);

		ViewCompat.setTranslationY(mViewPager, mHeaderHeight);
	}

	/**
	 * 判断是我的客户还是他人客户
	 */
	private void initclient() {
		
		
		if (createById == loginUserId) {
		
			Log.e("Client", "我的客户auth权限…………………………………………………………………………………………………………………………:"+ auth);
			
			typeTv.setText("我的客户");
			clientMoreIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 我的客户
					myClient();
					clientShareIv.setVisibility(View.VISIBLE);
				}
			});
			
		
		} else if (createById != loginUserId) {
			Log.e("Client", "他人客户auth权限："+auth);
			//大乐权限  可转发分享  可保存
			if(auth !=null){						
			 if(auth.equals("2")){
				 Log.e("Client", "大乐权限" + auth);
				 clientShareIv.setVisibility(View.VISIBLE);
			 }
			 //中乐权限  小乐权限  可转发分享 不可保存
			 if(auth.equals("3") || auth.equals("4") ){
				 Log.e("Client", "中乐权限" + auth);
				 clientShareIv.setVisibility(View.GONE);
			 }
			 //独乐权限  不可转发分享  不可保存
			 if(auth.equals("5")){
				 Log.e("Client", "独乐权限" + auth);
				 clientShareIv.setVisibility(View.GONE);
			 }
			}
			typeTv.setText("客户详情");
			clientMoreIv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					// 他人客户 客户详情
					otClient();
				}
			});
		}
	}
	/**
	 * 初始化子模块Fragment
	 */
	private void initFragment() {

		mOrgClientFrg = OrgClientFileFragment.newInstance(0);
		relationEvaluationFrg = OrgClientRelationEvaluationFrg.newInstance(1);
		
//		mMeetingDetailsFragment = MeetingDetailsFragment.newInstance(1);
//		if (label == myclient) {
//			mMeetingDetailsFragment.getArguments().putLong("orgId", organ_Id);
//			Log.e("MSG", "organ_Id传给会面" + organ_Id);
//		} else if (label == otherclient) {
//		mMeetingDetailsFragment = MeetingDetailsFragment.newInstance(1);
		if (label == myclient) {
//			mMeetingDetailsFragment.getArguments().putLong("orgId", organ_Id);
			Log.e("MSG", "organ_Id传给会面" + organ_Id);
		} else if (label == otherclient) {
//			mMeetingDetailsFragment.getArguments().putLong("customerId",
//					customerId);
//			Log.e("MSG", "customerId传给会面" + customerId);
		}

		
		

	}
	/**
	 * 初始化头部布局
	 */
	private void initHeaderView() {
		client_tv = (TextView) findViewById(R.id.client_tv);
		/** 公司职务 */
		relationPositionTv = (TextView) findViewById(R.id.relationPositionTv);
		/** 姓名 */
		nameTv = (TextView) findViewById(R.id.nameTv);
		/** 背景 */
		relationBgRl = (ImageView) findViewById(R.id.client_relationBgRl);
		/** 背景浮层 */
		relationBgRlCover = (ImageView) findViewById(R.id.relationBgRlCover);
		relationBgRlCover.setBackgroundResource(R.drawable.relation_man_bg);// 设置蓝紫色浮层

		/** 身份 */
		TextView mTextView = (TextView) findViewById(R.id.person_position);

	}


	/**
	 * 设置头布局头像数据
	 * 
	 */
	private void initHeaderViewData(ClientDetailsParams mjCustomer) {

//		if(mjCustomer.picLogo != null) {
//			ImageLoader.getInstance().clearMemoryCache();
//				Log.v("TOUXIANG", "组织客户列表---" + mjCustomer.picLogo);
//				if(TextUtils.isEmpty(mjCustomer.picLogo)||mjCustomer.picLogo.endsWith(GlobalVariable.ORG_DEFAULT_AVATAR)){
//					String last_char = "";
					String org_name = TextUtils.isEmpty(mjCustomer.shotName)?mjCustomer.name:mjCustomer.shotName;
//					if(!TextUtils.isEmpty(org_name)){
//						last_char = org_name.substring(org_name.length()-1);
//					}
//					Bitmap bm = Util.createBGBItmap(this, R.drawable.ic_group_default_avatar, R.color.avatar_text_color, R.dimen.avatar_text_size, last_char);
//					client_Image.setImageBitmap(bm);
//				}else{
//					ImageLoader.getInstance().displayImage( mjCustomer.picLogo,client_Image);
					ImageLoader.getInstance().displayImage(mjCustomer.picLogo,relationBgRl);// 背景头像
//				}
		Util.initAvatarImage(this, client_Image, org_name, mjCustomer.picLogo,0, 2);

			
//			ImageLoader.getInstance().displayImage(mjCustomer.picLogo, client_Image);
			
//		}

	}

	private String qRCodeStr;

	private class ViewPagerAdapter extends FragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			if (position == 0) {
				return mOrgClientFrg;
			} else if (position == 1) {
				return relationEvaluationFrg;

			}else {
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
		orgBackIv.setImageDrawable(getResources().getDrawable(R.drawable.relation_home_back_full));
		typeTv.setTextColor(Color.rgb(0, 0, 0));
		clientShareIv.setImageDrawable(getResources().getDrawable(R.drawable.forward_share_white_full));
		clientMoreIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_overflow_full));
	}
	/**
	 * 头布局展开
	 * @param duration
	 */
	private void headerExpand(long duration) {
		titlebar.setAlpha(0.01F);
		orgBackIv.setImageDrawable(getResources().getDrawable(R.drawable.left_arrow));
		typeTv.setTextColor(Color.rgb(255, 255, 255));
		clientShareIv.setImageDrawable(getResources().getDrawable(R.drawable.forward_share_white));
		clientMoreIv.setImageDrawable(getResources().getDrawable(R.drawable.more_whilte));
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
		// TODO Auto-generated method stub
	}

	boolean addFriendFLayoutBool = true;
	private LinearLayout ll_collect;
	private TextView client_collect;
	private TextView client_report;
	private boolean isCollect;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.clientShareIv:// 分享
			JTFile jtFile = new JTFile();
			jtFile.mTaskId = client_id + "";
			jtFile.fileName =mjCustomer.customer.shotName;
			if (TextUtils.isEmpty(mjCustomer.customer.shotName)) {
				jtFile.fileName = mjCustomer.customer.name;
			}
			jtFile.setmUrl(mjCustomer.customer.picLogo);
			jtFile.setmSuffixName(mjCustomer.customer.name);
			jtFile.setReserved1(mjCustomer.customer.discribe);
			jtFile.virtual = mjCustomer.customer.virtual;
			jtFile.mModuleType = 8;
			jtFile.mFileSize = 0;
			jtFile.setmType(JTFile.TYPE_CLIENT);

			FrameWorkUtils.showSharePopupWindow2(ClientDetailsActivity.this,
					jtFile);
			break;

		case R.id.Client_BackIv:// 返回
//			返回详情
			finishForResult();
			break;

		case R.id.client_save://保存
			
			ENavigate.startCreateClienteleActivity(ClientDetailsActivity.this, mjCustomer, 3, mjCustomer.customer.customerId);
			dlg.dismiss();
			break;
			
		case R.id.iv_org_bagiamg:
			
			ENavigate.startOrgMyHomePageActivity(this, client_id, comeId, true, ENavConsts.type_details_org);
			break;
		default:
			break;
		}
	}
	
	private void finishForResult() {
		if(mjCustomer!=null){
			if(mjCustomer.customer!=null){
				Intent intent  = new Intent();
				intent.putExtra("clientDetil", mjCustomer.customer);
				setResult(RESULT_OK, intent);
			}
		}
		finish();
	}

	@Override
	public void onBackPressed() {
		finishForResult();
		super.onBackPressed();
	}

	// 我的客户
	public void myClient() {

		dlg = new AlertDialog.Builder(ClientDetailsActivity.this).create();
		dlg.show();
		dlg.setCanceledOnTouchOutside(true);
		window = dlg.getWindow();

		window.setContentView(R.layout.client_widget_creater_dialog);

		client_edit_creater = (TextView) window.findViewById(R.id.client_edit_creater);//编辑
		client_linked_creater = (TextView) window.findViewById(R.id.client_linked_creater);//对接
		client_docking_creater = (TextView) window.findViewById(R.id.client_docking_creater);//删除	
		client_change2affairs = (TextView) window.findViewById(R.id.client_change2affairs);//转换为事务
		client_invite_to_join_creater = (TextView) window.findViewById(R.id.client_invite_to_join_creater);//邀请加入
		
		client_linked_creater = (TextView) window.findViewById(R.id.client_linked_creater);
		// 编辑
		client_edit_creater.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dlg.dismiss();

				ENavigate.startCreateClienteleActivity(ClientDetailsActivity.this, mjCustomer,2,client_id);
			}
		});
		//对接
		client_linked_creater.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dlg.dismiss();
				Org_de_id de_id = new Org_de_id();
				de_id.id = client_id;
				ENavigate.startJointResourceActivity(ClientDetailsActivity.this, ResourceType.Organization, de_id);
			}
		});
		
		//删除
		client_docking_creater.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				

				View view = View.inflate(ClientDetailsActivity.this, R.layout.demand_user_setting_dialog1, null);
				((TextView) view.findViewById(R.id.infoTv)).setText("确认删除客户吗？");
				final Dialog dialog = new Dialog(ClientDetailsActivity.this, R.style.MyDialog);
				// dialog.setCancelable(false);//是否允许返回
				dialog.addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				dialog.show();
				// 确定
				view.findViewById(R.id.confirmTv).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// 确定
								OrganizationReqUtil.doDeleteOrgAndCustomer(ClientDetailsActivity.this, ClientDetailsActivity.this, client_id, null);
								dialog.dismiss();
							}
						});
				view.findViewById(R.id.containerLl).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
				dlg.dismiss();
			}
		});
		//转换为事务
		client_change2affairs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				ConnectionNode inOrganizNode = new ConnectionNode(); 
				ArrayList<Connections> listConnections = new ArrayList<Connections>();
				OrganizationMini organizationMini = new OrganizationMini();
				
				organizationMini.id = mjCustomer.customer.customerId+"";
				organizationMini.logo = picLogo;
				organizationMini.isOnline = false;
				organizationMini.fullName = TextUtils.isEmpty(mjCustomer.customer.shotName)?mjCustomer.customer.name:mjCustomer.customer.shotName;
				organizationMini.shortName = TextUtils.isEmpty(mjCustomer.customer.shotName)?mjCustomer.customer.name:mjCustomer.customer.shotName;
				Connections connections = new Connections(organizationMini);
				listConnections.add(connections);
				inOrganizNode.setListConnections(listConnections);
				inOrganizNode.setMemo("客户");
				ENavigate.startNewAffarActivityByRelation(ClientDetailsActivity.this, null, inOrganizNode, null, null);
				dlg.dismiss();
			}
		});
		
		//邀请加入
		client_invite_to_join_creater.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg.dismiss();
				ENavigate.startMeetingInviteFriendsActivity(ClientDetailsActivity.this);
			}
		});			
	}

	// 他人客户/ 客户详情
	public void otClient() {

		dlg = new AlertDialog.Builder(ClientDetailsActivity.this).create();
		dlg.show();
		dlg.setCanceledOnTouchOutside(true);
		window = dlg.getWindow();
		
		
		window.setContentView(R.layout.client_other_widget_creater_dialog);
		ll_save = (LinearLayout) window.findViewById(R.id.ll_save);
		ll_collect = (LinearLayout) window.findViewById(R.id.ll_collect);
		
		client_report =( TextView)window.findViewById(R.id.client_report);
		
		client_report.setOnClickListener(new OnClickListener() { //举报
			
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				ENavigate.startOrgReportActivity(ClientDetailsActivity.this,mjCustomer.customer.customerId );
			}
		});
		
		//大乐权限  可转发分享  可保存
		if(auth !=null){						
		 if(auth.equals("2")){
			 Log.e("Client", "大乐权限" + auth);
			 ll_save.setVisibility(View.VISIBLE);
			 ll_collect.setVisibility(View.VISIBLE);
		 }
		 //中乐权限  小乐权限  可转发分享 不可保存
		 if(auth.equals("3") || auth.equals("4") ){
			 Log.e("Client", "中乐权限" + auth);
			 ll_save.setVisibility(View.GONE); 
			 ll_collect.setVisibility(View.VISIBLE);
		 }
		 //独乐权限  不可转发分享  不可保存
		 if(auth.equals("5")){
			 Log.e("Client", "独乐权限" + auth);
			 ll_save.setVisibility(View.GONE);
		 }
		 
		}
		client_save = (TextView) window.findViewById(R.id.client_save);
		//保存
		client_save.setOnClickListener(this);
		
		
		client_collect = (TextView) window.findViewById(R.id.client_collect);
		if ("0".equals(mjCustomer.customer.isCollect)) {
			collect = new Collect();
			collect.type=1+"";
			customerIds= new ArrayList<Long>();
			customerIds.add(mjCustomer.customer.customerId);
			collect.customerIds  = customerIds;
			isCollect=false;
			
		}else if("1".equals(mjCustomer.customer.isCollect)){
			client_collect.setText("取消收藏");
			collect  = new Collect();
			collect.type=2+"";
			customerIds= new ArrayList<Long>();
			customerIds.add(mjCustomer.customer.customerId);
			collect.customerIds  = customerIds;
		}
		client_collect.setOnClickListener(new OnClickListener() { //收藏
			@Override
			public void onClick(View v) {
				isCollect=!isCollect;
				OrganizationReqUtil.doRequestWebAPI(ClientDetailsActivity.this, ClientDetailsActivity.this, collect, null, OrganizationReqType.CUSTOMER_COLLECT_PERATE);
				dlg.dismiss();
			}
		});
		
		
		// 对接
		client_report_big_happy = (TextView) window
				.findViewById(R.id.client_report_big_happy);
		
		client_report_big_happy
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dlg.dismiss();
						Org_de_id de_id = new Org_de_id();
						de_id.id = client_id;
						ENavigate.startJointResourceActivity(ClientDetailsActivity.this, ResourceType.Organization, de_id);
						dlg.dismiss();
					}
					
				});

		// 查看发布人
		findbody_big_happy = (TextView) window
				.findViewById(R.id.client_findbody_big_happy);
		findbody_big_happy.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				dlg.dismiss();
				int isSelfOrOther = -1;
				if (App.getUserID().equals(mjCustomer.customer.createById)) {
					isSelfOrOther = ENavConsts.type_details_member;
				} else {
					isSelfOrOther = ENavConsts.type_details_other;
				}

				if ("1".equals(mjCustomer.customer.createType)) {// 用户
					if (isSelfOrOther != -1) {
						ENavigate.startRelationHomeActivity(
								ClientDetailsActivity.this,
								mjCustomer.customer.createById+"", true, isSelfOrOther);
					}
				} else {
					ENavigate.startOrgMyHomePageActivityByUseId(ClientDetailsActivity.this,mjCustomer.customer.createById);
					
				}
				dlg.dismiss();
			}
		});
	}




	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case ENavConsts.ActivityReqCode.REQUEST_CODE_LOOK_MORE_COMMENT:// 动态
			Bundle bundle = intent.getExtras();
			List<DynamicComment> mListComment = (List<DynamicComment>) bundle
					.getSerializable(ENavConsts.KEY_FRG_CHANGE_COMMENTS);
			ArrayList<DynamicPraise> mDynamicPraises = (ArrayList<DynamicPraise>) bundle
					.getSerializable(ENavConsts.KEY_FRG_FLOW_DYNAMIC_PRAISES);
			int index = bundle.getInt(ENavConsts.KEY_FRG_FLOW_INDEX, 0);
			frgFlow.changeDynamicListUI(mDynamicPraises, mListComment, index);
			break;
		case ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_MORE_EVALUATION:// 更多评价
		case ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_EDIT_EVALUATION:// 编辑评价
			relationEvaluationFrg.onActivityResult(requestCode, resultCode,
					intent);
			break;
		default:
			break;
		}
	}

	private ImageView relationBgRlCover;
	private ActionBar actionBar;
	private View headerVi;
	private ImageView titlebar;
	private int titlebarHeight;
	private TextView typeTv;
	private ImageView clientShareIv;
	private ImageView clientMoreIv;
	private ImageView orgBackIv;
	private TextView findbody_big_happy;
	private TextView client_tv;
	private String auth;
	private ClientDetialsIncomParams mParams;
	public long customerId;
	public long organ_Id;
	private TextView client_edit_creater;
	private TextView client_docking_creater;
	private TextView client_change2affairs;
	private int otherclient;
	private int myclient;
	private int label;
	private long orgcustomerId;
	private CircleImageView client_Image;
	private String picLogo;
	public String friends;
	private TextView client_linked_creater;
	private TextView client_invite_to_join_creater;
	private long comeId;
	private long createById;
	private TextView client_save;
	private ImageView iv_org_bagiamg;
	private boolean isSuccess;
	private boolean result;
	public Area address;
	public ArrayList<CustomerPersonalLine> custom;
	private long loginUserId;
	private LinearLayout ll_save;
	private int view2;
	private Collect collect;


	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {

		switch (tag) {
		case OrganizationReqType.ORGANIZATION_CUSTOMER_DETILS:// 客户详情
			
			if ( object == null) {
				finish();
				return;
			}
			Map<String, Object> map = (Map<String, Object>) object;
			
			mjCustomer = (CustomerClientParams) map.get("customerclientparams");
			
			relationEvaluationFrg.getArguments().putLong(EConsts.Key.ID, client_id);
			Log.e("MSG", "传给评价customerId:"+client_id);

			
			relationEvaluationFrg.upDate();
			
			if (mjCustomer.customer != null) {
				if(mjCustomer.customer.auth != null && mjCustomer.customer != null&& !mjCustomer.customer.auth.equals("null")){
					relationEvaluationFrg.getArguments().putString("AUTH", mjCustomer.customer.auth);
					Log.e("MSG", "传给评价auth:"+mjCustomer.customer.auth);
				}
				
				if(!mjCustomer.customer.name.equals("") && !mjCustomer.customer.shotName.equals("")){
					
					client_tv.setText(mjCustomer.customer.shotName);
				}else if(mjCustomer.customer.shotName.equals("")){
					client_tv.setText(mjCustomer.customer.name);
				}else{
					client_tv.setText(mjCustomer.customer.shotName);
				}
				
				
				
			}
			if (mjCustomer.customer != null) {
				auth = mjCustomer.customer.auth;
				initclient();
				Log.e("Client", "auth@@@:" + auth);
			}

			if (mjCustomer.customer != null) {
				picLogo = mjCustomer.customer.picLogo;
				// 头像
				initHeaderViewData(mjCustomer.customer);
				Log.e("MSG", "头像客户url:" + picLogo);
			}
			if (mjCustomer.customer != null) {
				friends = mjCustomer.customer.friends;
				Log.e("MSG", "friends:"+friends);
				if (mjCustomer.customer.area != null) {
					address = mjCustomer.customer.area;
				}				
			}
			
			//客户资料页填充数据
			if (mOrgClientFrg != null) {
				
				mOrgClientFrg.refreshView(mjCustomer);
			}
				
			if(mjCustomer.customer != null){
				//来自组织id 不为0时 是组织转为客户的 客户
				comeId = mjCustomer.customer.comeId;
				Log.e("MSG", "组织转客户comeId:"+comeId);
				
				if(comeId !=0){
				iv_org_bagiamg.setVisibility(View.VISIBLE);
				//组织 转客户
				iv_org_bagiamg.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ENavigate.startOrgMyHomePageActivity(ClientDetailsActivity.this, client_id, createById, true, ENavConsts.type_details_org);
					}
				});
			}else {
				iv_org_bagiamg.setVisibility(View.GONE);
			}
				
			}
			//创建者id
			if(mjCustomer.customer != null){
				createById = mjCustomer.customer.createById;
				initclient();
				Log.e("Client", "createById:"+createById);
			}
			//当前用户登录id
			if(mjCustomer.customer != null){
				loginUserId = mjCustomer.customer.loginUserId;
				initclient();
				Log.e("Client", "loginUserId:"+loginUserId);
			}
		
			break;
			
		case OrganizationReqType.ACCESS_TO_THE_PRIMARY_KEY: // 获取客户主键ID
			if (object != null) {
				Map<String, Object> dataHm = (Map<String, Object>) object;
				GetId getId = (GetId) dataHm.get("getId");
				if (getId != null) {
					isSuccess = getId.success;
				}
				Log.e("MSG", "@@@@@@@客户主键ID" + getId);				
			}
			break;
			
		case EAPIConsts.OrganizationReqType.ORAGANIZATION_REQ_DELCUSANDORG:// 5.3删除组织.客户
			
			if (object == null) {
				return;
			}
			Map<String, Boolean> dataMap = (Map<String, Boolean>) object;
			result = dataMap.get("success");
			if(result == false){
				ToastUtil.showToast(ClientDetailsActivity.this, "删除失败");
			}
			else{
				ToastUtil.showToast(ClientDetailsActivity.this, "删除成功");
				setResult(100);
				finish();
			}
			
			break;
		case EAPIConsts.OrganizationReqType.CUSTOMER_COLLECT_PERATE://收藏
			if (object!=null) {
				Boolean success = (Boolean) object;
				if ("0".equals(mjCustomer.customer.isCollect)) {
					if (success) {
						ToastUtil.showToast(ClientDetailsActivity.this, "收藏成功");
					}else{
						ToastUtil.showToast(ClientDetailsActivity.this, "收藏失败");
					}
				}else if ("1".equals(mjCustomer.customer.isCollect)) {
					if (success) {
						ToastUtil.showToast(ClientDetailsActivity.this, "取消收藏成功");
					}else{
						ToastUtil.showToast(ClientDetailsActivity.this, "取消收藏失败");
					}
				}
				OrganizationReqUtil.doRequestWebAPI(this, this, mParams, null,   //重新请求一下数据，改变成取消收藏
						OrganizationReqType.ORGANIZATION_CUSTOMER_DETILS);
//				
			}
		
		default:
			break;

		}
	}
	
}
