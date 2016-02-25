package com.tr.ui.people.contactsdetails;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.PeopleReqUtil;
import com.tr.db.ConnectionsCacheData;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.obj.Connections;
import com.tr.model.obj.DynamicComment;
import com.tr.model.obj.DynamicPraise;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.connections.viewfrg.ListViewFragment;
import com.tr.ui.demand.MyView.MyViewPager;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.home.frg.FrgFlow;
import com.tr.ui.knowledge.swipeback.SwipeBackActivity;
import com.tr.ui.knowledge.utils.ActivityCollector;
import com.tr.ui.people.contactsdetails.fragment.ContactsRelationEvaluationFrg;
import com.tr.ui.people.contactsdetails.fragment.PeopleContactsdetailFragment;
import com.tr.ui.people.model.BaseResult;
import com.tr.ui.people.model.ConvertToPeople;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.PersonName;
import com.tr.ui.people.model.PersonCollectId;
import com.tr.ui.people.model.PersonId;
import com.tr.ui.people.model.params.PeopleDetialsIncomingParameters;
import com.tr.ui.widgets.viewpagerheaderscroll.SlidingTabLayout;
import com.tr.ui.widgets.viewpagerheaderscroll.SlidingTabLayout.TabColorizer;
import com.tr.ui.widgets.viewpagerheaderscroll.TouchCallbackLayout;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableFragmentListener;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableListener;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ViewPagerHeaderHelper;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;

/**
 * 人脉详情    
 * 启动方法：ENavigate.startContactsDetailsActivity
 * 
 * @param context
 * @param fromActivityType
 *            "type":"1-创建  2-其他"
 * @param id
 *            人脉对象id
 * @param view 1 查看权限  是否需要进行权限控制 0 受权限控制 1 不受权限控制

 * @param requestCode  请求码
 * 
 */
public class ContactsDetailsActivity extends SwipeBackActivity implements
		TouchCallbackLayout.TouchEventListener, ScrollableFragmentListener,
		ViewPagerHeaderHelper.OnViewPagerTouchListener, OnClickListener,
		IBindData {

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
	/**资料*/
	private PeopleContactsdetailFragment mPeopleContactsdetailFragment;
	/**会面*/
	// private MeetingDetailsFragment mMeetingDetailsFragment;
	/**动态*/
	private FrgFlow frgFlow;
	/**人脉评价*/
	private ContactsRelationEvaluationFrg relationEvaluationFrg;
	/**ViewPagerAdapter适配器*/
	private ViewPagerAdapter viewPagerAdapter;

	/** header */
	private ImageView relationIv;
	private TextView relationPositionTv;// 职位
	private TextView nameTv; // 姓名
	private ImageView relationBgRl;
	// private ImageView qRCodeImage; //名片
	// private FrameLayout addFriendFLayout;
	// private FrameLayout sendMessageFLayout;
	private FrameLayout people_docking_resources;
	// /** 编辑 */
	// private MenuItem menuEdit;
	// /** 解除好友关系 */
	// private MenuItem menuDeleteFriend;
	// /** 对接 */
	// private MenuItem menuJoin;
	// /** 加入黑名单 */
	// private MenuItem menuBlackNumber;
	// /** 消息 */
	// private MenuItem menuMessage;
	// /** 加为好友 */
	// private MenuItem menuAddFriend;
	// /** 评价 */
	// private MenuItem menuEvalation;
	public static final int result_recommend = 1;
	public static final int result_edit = 5;
	/** 人脉Id*/
	public Long id;
	/** "eFromActivity":"1-创建  2-其他"*/
	private int eFromActivity;
	public Person person;// 新Api 对象
	private int type;// 接口返回的type
	private int view;// 转发人脉查看权限
	/** 是否为组织*/
	private boolean isOrg; 
	/** 人脉详情的标题*/
	private TextView people_hometypeTv;
	private TextView avatarText_ContactDetails;

	/**
	 * 隐藏ActionBar，无法使用ActionBar
	 */
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
		setContentView(R.layout.people_activity_contacts_detail);
		ActivityCollector.addActivity(this);

		Intent intent = getIntent();
		if (intent != null) {

			// 获取用户或者人脉id
			id = intent.getLongExtra(EConsts.Key.PERSON_ID, -1);
			//获取转发人脉查看权限
			view = intent.getIntExtra(EConsts.Key.VIEW, 0);
			System.out.println("创建人脉Id" + id);
			eFromActivity = intent.getIntExtra(ENavConsts.EFromActivityType, 0);
		}

		// type = getIntent().getIntExtra(ENavConsts.EFromActivityType,
		// ENavConsts.type_details_other);
		// isonline = getIntent().getBooleanExtra(EConsts.Key.isOnline, false);
		// // online

		/** 底部对接资源 */
		people_docking_resources = (FrameLayout) findViewById(R.id.people_docking_resources);
		people_docking_resources.setOnClickListener(this);
		people_docking_resources.setVisibility(View.VISIBLE);

		initFragment();

		initUI();

		initHeaderView();
	
		requestPeopleJson();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	/**
	 * 请求网络数据
	 */
	public void requestPeopleJson() {
		peopleDetialParam = new PeopleDetialsIncomingParameters();
		peopleDetialParam.id = id;// ==id
		peopleDetialParam.personType = 2;// = personType
		if (view == 1) {
			peopleDetialParam.view = view;
		} else {
			peopleDetialParam.view = view;
		}
		PeopleReqUtil.doRequestWebAPI(this, this, peopleDetialParam, null,
				PeopleRequestType.PEOPLE_REQ_GETPEOPLE);
		showLoadingDialog();
	}
	/**
	 * 根据人脉对象Person进行数据填充
	 * 
	 * @param person
	 */
	private void initHeaderViewData(Person person) {
		ImageLoader.getInstance().clearDiscCache();
		ImageLoader.getInstance().clearMemoryCache();
		// Bitmap bitmap = BitmapFactory.decodeFile(person.portrait);
		// relationIv.setImageBitmap(bitmap);
		/*if (!TextUtils.isEmpty(person.portrait)) {
			ImageLoader.getInstance().displayImage(person.portrait, relationIv);
		}*/
		
		// 姓名
		List<PersonName> personNames = person.peopleNameList;
		if (personNames != null && person.peopleNameList.size() > 0) {
			chineseName = "";
			PersonName chinese = personNames.get(0);
			if (!TextUtils.isEmpty(chinese.lastname)) {
				chineseName += chinese.lastname;
			}
//			if (!TextUtils.isEmpty(chinese.firstname)) {
//				chineseName += chinese.firstname;
//			}
			nameTv.setText(chineseName);
		}
//		if (TextUtils.isEmpty(person.portrait)||"default.jpeg".equals(person.portrait.split("/")[person.portrait.split("/").length - 1])) {
//			if (person.getGender() == 1|| person.getGender() == 2) {
//				relationIv.setImageDrawable(getResources().getDrawable(R.drawable.no_avatar_but_gender));
//			} else {
//				relationIv.setImageDrawable(getResources().getDrawable(R.drawable.no_avatar_no_gender));
//			}		
//			if (StringUtils.isEmpty(chineseName)) {
//			avatarText_ContactDetails.setText("");
//		} else {
//			char[] chs = null ; 
//			if (!StringUtils.isEmpty(chineseName)) {
//				chs = chineseName.toCharArray();
//			} 
//			avatarText_ContactDetails.setText(String.valueOf(chs[chs.length - 1]));
//		}
//	} else {
//		ImageLoader.getInstance().displayImage(person.portrait, relationIv);
//		ImageLoader.getInstance().displayImage(person.portrait,
//				relationBgRl);// 背景头像
//		avatarText_ContactDetails.setText("");
//	}
		com.utils.common.Util.initAvatarImage(this,relationIv,chineseName,person.portrait,person.getGender(),1);

		
		String companyName = person.company;
		String companyPosition = person.position;
		if (StringUtils.isEmpty(person.company)) {
			companyName = "";
		}
		if (StringUtils.isEmpty(person.position)) {
			companyPosition = "";
		}
		relationPositionTv.setText(companyName + " " + companyPosition);


		if (person.gender == 2) {// 女
			relationBgRlCover
					.setBackgroundResource(R.drawable.relation_girl_bg);// 设置粉红色浮层
		} else {
			relationBgRlCover.setBackgroundResource(R.drawable.relation_man_bg);// 设置蓝紫色浮层
		}
		//判断是否有查看他的主页
		if (person.fromUserId > 0 && type == 6) {
//			mLinearLayout.setVisibility(View.VISIBLE);
		} else {
//			mLinearLayout.setVisibility(View.INVISIBLE);
		}
	}

	private void initUI() {

		headerVi = findViewById(R.id.people_headerVi);

		// 初始化顶部titlebar
		/** titlebar的背景 */
		titlebar = (ImageView) findViewById(R.id.people_titlebar);
		/** 主页类型：我的，好友，他人 */
		hometypeTv = (TextView) findViewById(R.id.people_hometypeTv);
		hometypeTv.setText("人脉详情");
		/** 分享按钮 */
		relationShareIv = (ImageView) findViewById(R.id.people_relationShareIv);
		/** 更多操作按钮 */
		relationMoreIv = (ImageView) findViewById(R.id.people_relationMoreIv);
		/** 主页返回 */
		relationHomeBackIv = (ImageView) findViewById(R.id.people_relationHomeBackIv);
		people_hometypeTv = (TextView) findViewById(R.id.people_hometypeTv);
		/** 头像里的字*/
		avatarText_ContactDetails =  (TextView) findViewById(R.id.avatarText_ContactDetails);

		relationShareIv.setOnClickListener(this);
		relationMoreIv.setOnClickListener(this);
		relationHomeBackIv.setOnClickListener(this);
		people_hometypeTv.setOnClickListener(this);

		titlebarHeight = getResources().getDimensionPixelSize(
				R.dimen.retion_title_height);
		mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
		mTabHeight = getResources().getDimensionPixelSize(R.dimen.tabs_height);
		mHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.viewpager_header_height);

		mViewPagerHeaderHelper = new ViewPagerHeaderHelper(this, this);

		TouchCallbackLayout touchCallbackLayout = (TouchCallbackLayout) findViewById(R.id.people_layout);
		touchCallbackLayout.setTouchEventListener(this);

		mHeaderLayoutView = findViewById(R.id.people_header);
		SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.people_tabs);
		slidingTabLayout.setCustomTabColorizer(new TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return Color.rgb(251, 127, 5);
			}
		});

		mViewPager = (MyViewPager) findViewById(R.id.people_viewpager);
		viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(viewPagerAdapter);
		viewPagerAdapter.notifyDataSetChanged();

		slidingTabLayout.setViewPager(mViewPager);

		ViewCompat.setTranslationY(mViewPager, mHeaderHeight);

		boolean isMeeting = this.getIntent().getBooleanExtra("Meeting", false);
		if (isMeeting) {
			mViewPager.setCurrentItem(1);
		}
	}
	/**
	 * 初始化Fragment  目前有资料，评论模块
	 */
	private void initFragment() {
		relationEvaluationFrg = ContactsRelationEvaluationFrg.newInstance(1);

		mPeopleContactsdetailFragment = PeopleContactsdetailFragment
				.newInstance(0 );
		// mMeetingDetailsFragment = MeetingDetailsFragment.newInstance(1);
	}
	/**
	 * 初始化顶部布局
	 */
	private void initHeaderView() {
		/** 头像 */
		relationIv = (ImageView) findViewById(R.id.people_RelationImage);
		relationIv.setOnClickListener(this);

		/** 公司职务 */
		relationPositionTv = (TextView) findViewById(R.id.people_person_position);
		/** 姓名 */
		nameTv = (TextView) findViewById(R.id.people_name_tv);
		/** 背景 */
		relationBgRl = (ImageView) findViewById(R.id.people_relationBgRl);
		/** 背景浮层 */
		relationBgRlCover = (ImageView) findViewById(R.id.people_relationBgRlCover);
		/** 他的主页 */
		mLinearLayout = (LinearLayout) findViewById(R.id.people_layout_his_homePage);
		mLinearLayout.setOnClickListener(this);
		/** 身份 */
		// people_name_tv = (TextView) findViewById(R.id.people_name_tv);

	}

	IBindData ib = new IBindData() {

		@Override
		public void bindData(int tag, Object object) {
			dismissLoadingDialog();
			// 保存他人人脉为自己的人脉
			if (tag == EAPIConsts.PeopleRequestType.PEOPLE_REQ_CONVERTPEOPLE) {
				if (object != null) {

					ConvertToPeople convertToPeople = (ConvertToPeople) object;
					boolean success = convertToPeople.success;

					if (success) {
						showToast("保存人脉成功");
						// 保存成功，显示“他人主页”
						ENavigate.startMyFriendAll(
								ContactsDetailsActivity.this,
								ConnectionsCacheData.FILTER_PEOPLE_ALL);
						// mLinearLayout.setVisibility(View.VISIBLE);
					} else {
						showToast("保存人脉失败");
					}
				}
			}
			switch (tag) {
			case PeopleRequestType.PEOPLE_REQ_CREATE:

				if (object != null) {
					BaseResult result = (BaseResult) object;
					if (result.success) {

						showToast("保存人脉成功");
						finish();

					} else {
						Toast.makeText(ContactsDetailsActivity.this, "保存人脉失败！",
								0).show();
					}
					break;
				}
			}
		}
	};
	
	/**
	 * ViewPagerAdapter 设配器
	 *  资料，评价
	 * @author John
	 *
	 */
	private class ViewPagerAdapter extends FragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			if (position == 0) {

				return mPeopleContactsdetailFragment;
			}
			// else if (position == 1) {
			//
			// return mMeetingDetailsFragment;
			// }
			else if (position == 1) {

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
			case 0:
				return "资料";
				// case 1:
				// return "会面";
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
		// TODO Auto-generated method stub
	}

	boolean addFriendFLayoutBool = true;
	private boolean IsChange;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.people_relationShareIv:// 分享
			if (person != null) {
				// "jtFile":{
				// "fileName":"用户名",
				// "fileSize":"",
				// "url":"头像地址",
				// "suffixName":"公司名",
				// "type":"人脉-6,用户-10",
				// "moduleType":"",
				// "taskId":"用户id",
				// "reserved1":"公司职位",
				// "reserved2":"",
				// "reserved3":""
				// }

				JTFile jTFile = new JTFile();
				jTFile.mTaskId = person.id + "";
				jTFile.setmUrl(person.portrait);
//				Log.v("ADD", "人脉姓名---->"
//						+ person.peopleNameList.get(0).lastname
//						+ person.peopleNameList.get(0).firstname);
				if(person.peopleNameList.size() > 0){
					jTFile.mFileName = person.peopleNameList.get(0).lastname
							+ person.peopleNameList.get(0).firstname;
				}else{
					jTFile.mFileName = "" ;
				}
//				jTFile.mFileName = App.getNick()+"分享了[人脉] ";
				jTFile.setmSuffixName(person.company);
				jTFile.setReserved1(person.position);
				jTFile.setReserved2(person.company);
				jTFile.setReserved3(person.address);
				jTFile.mModuleType = 9;
				jTFile.mFileSize = 0;
				jTFile.setmType(6);
				jTFile.virtual = person.virtual;
				FrameWorkUtils.showSharePopupWindow2(
						ContactsDetailsActivity.this, jTFile);
			}
			break;
		case R.id.people_relationMoreIv:// 更多
			dlg = new AlertDialog.Builder(this).create();
			dlg.show();
			dlg.setCanceledOnTouchOutside(true);
			window = dlg.getWindow();
			peopleDialog(type, isOrg);
			break;
		case R.id.people_relationHomeBackIv:// 返回
			if (eFromActivity == 1) {
				// ENavigate.startFriend(this);
				TimeUtil.isFastDoubleClick();
//				ENavigate.startMyFriendAll(this,
//						ConnectionsCacheData.FILTER_PEOPLE_ALL);
			}
			finish();
			break;
		case R.id.people_hometypeTv: // 返回
			if (eFromActivity == 1) {
				// ENavigate.startFriend(this);
				TimeUtil.isFastDoubleClick();
//				ENavigate.startMyFriendAll(this,
//						ConnectionsCacheData.FILTER_PEOPLE_ALL);
			}
			finish();
			break;
		case R.id.people_layout_his_homePage:
			ENavigate.startRelationHomeActivity(this,
					String.valueOf(person.fromUserId));
			break;

		// 暂时人脉详情头像进入组织详情界面
		// 人脉资源下方的对接控件
		case R.id.people_docking_resources:
			try {
				PersonId person_Id = new PersonId();
				person_Id.id = person.id;
				ENavigate.startJointResourceActivity(ContactsDetailsActivity.this,
						ResourceType.People, person_Id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 人脉对话框
	 */
	public void peopleDialog(int type, boolean isOrg) {
		try {
		if (type == 2 || type == 3) {// 大乐/中乐
			window.setContentView(R.layout.people_widget_big_happy_dialog);
			TextView saveView = (TextView) window.findViewById(R.id.save);
			View saveLineView = window.findViewById(R.id.line);
			TextView duijieView = (TextView) window.findViewById(R.id.duijie);
			View duijieLineView = window.findViewById(R.id.duijie_line);
			TextView pubPeopleView = (TextView) window
					.findViewById(R.id.see_pub_people);
			TextView invite_to_join_big_happy = (TextView) window.findViewById(R.id.invite_to_join_big_happy);
			TextView see_friend_people = (TextView) window
					.findViewById(R.id.see_friend_people);
			if (peopleDetails.people.fromUserId != null
					&& peopleDetails.people.fromUserId != 0) {
				see_friend_people.setOnClickListener(new OnClickListener() { // 查看好友主页

							@Override
							public void onClick(View v) {
								dlg.dismiss();
								ENavigate.startRelationHomeActivity(
										ContactsDetailsActivity.this,
										String.valueOf(peopleDetails.people.fromUserId));
							}
						});
			} else {
				see_friend_people.setVisibility(View.GONE);
			}
			
			if (peopleDetails.people.fromPersonId != null
					&& peopleDetails.people.fromPersonId != 0) { //邀请加入
				invite_to_join_big_happy.setVisibility(View.VISIBLE);
			invite_to_join_big_happy.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
						ENavigate.startMeetingInviteFriendsActivity(ContactsDetailsActivity.this);
					}
				});
			}
			

			/** 收藏 */
			final TextView collectionTv = (TextView) window
					.findViewById(R.id.collection_big_happy);
			if (peopleDetails.fromType == 3) {
				collectionTv.setText("取消收藏");
			}

			TextView report = (TextView) window
					.findViewById(R.id.report_big_happy); // 举报
			if (type == 3) {// 中乐
				saveView.setVisibility(View.GONE);
				saveLineView.setVisibility(View.GONE);
			}

			if (isOrg) {
				duijieLineView.setVisibility(View.GONE);
				pubPeopleView.setVisibility(View.GONE);
			}
			report.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ENavigate.startReportActivity(ContactsDetailsActivity.this,
							peopleDetails.people);
				}
			});

			saveView.setOnClickListener(new OnClickListener() {// 将他人人脉保存为我的人脉

				private ArrayList<Long> tid;

				@Override
				public void onClick(View v) {
					dlg.dismiss();
					/*
					 * RequestConvertToPeople params = new
					 * RequestConvertToPeople(); params.personId = person.id;
					 * 
					 * PeopleReqUtil.doRequestWebAPI(ContactsDetailsActivity.this
					 * , ib, params, null,
					 * PeopleRequestType.PEOPLE_REQ_CONVERTPEOPLE);
					 * showLoadingDialog();
					 */
					// 需求 改为 先编辑再 保存 跳转到人脉编辑页 跳转前 把frompersonid补全
					peopleDetails.people.setFromPersonId();
					ENavigate.startNewConnectionsActivity(
							ContactsDetailsActivity.this, 4, peopleDetails,
							null);

					finish();
				}
			});

			collectionTv.setOnClickListener(new OnClickListener() {// 收藏 将他人人脉
																	// 收藏 为我的人脉

						private ArrayList<Long> tid;

						@Override
						public void onClick(View v) {
							dlg.dismiss();
							/*
							 * RequestConvertToPeople params = new
							 * RequestConvertToPeople(); params.personId =
							 * person.id;
							 * 
							 * PeopleReqUtil.doRequestWebAPI(ContactsDetailsActivity
							 * .this, ib, params, null,
							 * PeopleRequestType.PEOPLE_REQ_CONVERTPEOPLE);
							 * showLoadingDialog();
							 */
							PersonCollectId person_Id = new PersonCollectId();
							person_Id.personId = person.id;
							if (peopleDetails.fromType == 3) {
								PeopleReqUtil.doRequestWebAPI(
										ContactsDetailsActivity.this,
										ContactsDetailsActivity.this,
										person_Id, null,
										PeopleRequestType.CANCEL_COLLECT);
							} else {
								PeopleReqUtil.doRequestWebAPI(
										ContactsDetailsActivity.this,
										ContactsDetailsActivity.this,
										person_Id, null,
										PeopleRequestType.COLLECT_PEOPLE);
							}
						}
					});
			duijieView.setOnClickListener(new OnClickListener() {// 对接

						@Override
						public void onClick(View v) {
							dlg.dismiss();
							PersonId person_Id = new PersonId();
							person_Id.id = person.id;
							ENavigate.startJointResourceActivity(
									ContactsDetailsActivity.this,
									ResourceType.People, person_Id);
						}
					});

			pubPeopleView.setOnClickListener(new OnClickListener() {// 查看发布人

						@Override
						public void onClick(View v) {
							dlg.dismiss();
							ENavigate.startRelationHomeActivity(
									ContactsDetailsActivity.this,
									String.valueOf(person.createUserId));

						}
					});
		} else { // 我创建的
			window.setContentView(R.layout.people_widget_edit_dialog);
			TextView editView = (TextView) window
					.findViewById(R.id.edit_people);
			TextView change2work_people = (TextView) window
					.findViewById(R.id.change2work_people);
			TextView cancelView = (TextView) window.findViewById(R.id.cancel);
			TextView seeUserView = (TextView) window
					.findViewById(R.id.see_friend);
			View seeUserLineView = window.findViewById(R.id.line);
			
			TextView invite_join = (TextView) window.findViewById(R.id.invite_join);
			if (person != null) {
				long fromUserId = person.fromUserId;
				// if (fromUserId > 0) {// 保存的
				// // mLinearLayout.setVisibility(View.VISIBLE);
				// seeUserView.setVisibility(View.VISIBLE);
				// seeUserLineView.setVisibility(View.VISIBLE);
				// } else {// 创建来的
				// // mLinearLayout.setVisibility(View.INVISIBLE);
				// }
			}
				invite_join.setVisibility(View.VISIBLE);
			invite_join.setOnClickListener(new OnClickListener() { //邀请加入
				
				@Override
				public void onClick(View v) {
					ENavigate.startMeetingInviteFriendsActivity(ContactsDetailsActivity.this);
				}
			});
			editView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String removeIDString;
					if (id != null) {
						removeIDString = String.valueOf(id);
					} else {
						removeIDString = "";
					}
					ENavigate.startNewConnectionsActivity(
							ContactsDetailsActivity.this, 2, peopleDetails,
							result_edit, removeIDString);
					dlg.dismiss();
				}
			});
			change2work_people.setOnClickListener(new OnClickListener() {
				// 转换成事务
				@Override
				public void onClick(View v) {
					ConnectionNode inPeopleNode = new ConnectionNode();
					ArrayList<Connections> listConnections = new ArrayList<Connections>();
					JTContactMini jtContact = new JTContactMini();
					jtContact.setId(person.id + "");
					jtContact.setName(chineseName);
					jtContact.setImage(peopleDetails.people.portrait);
					jtContact.setCompany(person.company);
					jtContact.setOnline(false);
					Connections connections = new Connections(jtContact);
					listConnections.add(connections);
					inPeopleNode.setListConnections(listConnections);
					inPeopleNode.setMemo("人脉");
					ENavigate.startNewAffarActivityByRelation(
							ContactsDetailsActivity.this, inPeopleNode, null,
							null, null);

					dlg.dismiss();
				}
			});
			if (peopleDetails.people.fromUserId != null
					&& peopleDetails.people.fromUserId != 0) {
				seeUserView.setVisibility(View.VISIBLE);
				seeUserLineView.setVisibility(View.VISIBLE);
				seeUserView.setOnClickListener(new OnClickListener() {// 查看用户

							@Override
							public void onClick(View v) {
								dlg.dismiss();
								ENavigate.startRelationHomeActivity(
										ContactsDetailsActivity.this,
										String.valueOf(peopleDetails.people.fromUserId));
							}
						});
			}
			cancelView.setOnClickListener(new OnClickListener() {// 取消

						@Override
						public void onClick(View v) {
							dlg.dismiss();
						}
					});
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);

		switch (requestCode) {
		case result_edit:// 编辑人脉
			if (resultCode == Activity.RESULT_OK) {
				requestPeopleJson();
			}
			break;
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
	private FrameLayout waitingPassFLayout;
	private ImageView titlebar;
	private int titlebarHeight;
	private TextView hometypeTv;
	private ImageView relationShareIv;
	private ImageView relationMoreIv;
	private ImageView relationHomeBackIv;
	private LinearLayout mLinearLayout;
	private String chineseName;
	public PeopleDetails peopleDetails;
	private PeopleDetialsIncomingParameters peopleDetialParam;


	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case PeopleRequestType.PEOPLE_REQ_GETPEOPLE:
			dismissLoadingDialog();
			if (object != null) {
				peopleDetails = (PeopleDetails) object;
				person = peopleDetails.people;
				if (person == null || person.id <= 0) {
					finish();
					return;
				}
				// 填充资料详情
					mPeopleContactsdetailFragment.refreshView(peopleDetails);

				if (person != null) {
					initHeaderViewData(person);

					if (id != 0) {
						relationEvaluationFrg.getArguments().putLong(
								EConsts.Key.ID, id);
						Log.v("DATA", "评价id---->" + id);
					}
					type = peopleDetails.type;
					if (type == 3) {// 中乐
						relationShareIv.setVisibility(View.INVISIBLE);
					}
					isOrg = peopleDetails.isOrg;
					relationEvaluationFrg.getArguments().putInt(
							EConsts.Key.PERSON_TYPE, type);
					relationEvaluationFrg.upDate();
				}

			} else {
				finish();
			}
			break;
		case PeopleRequestType.COLLECT_PEOPLE:
			Boolean isSuccess = (Boolean) object;

			if (isSuccess != null) {

				if (isSuccess) {
					Toast.makeText(ContactsDetailsActivity.this, "收藏成功", 1)
							.show();
					IsChange =true;
					PeopleReqUtil.doRequestWebAPI(this, this,
							peopleDetialParam, null,
							PeopleRequestType.PEOPLE_REQ_GETPEOPLE); // 重新去请求详情的数据，以致于改变操作
				}
			}
			break;
		case PeopleRequestType.CANCEL_COLLECT:
			Boolean Success = (Boolean) object;
			if (Success != null) {

				if (Success) {
					Toast.makeText(ContactsDetailsActivity.this, "取消收藏成功", 1)
							.show();
					IsChange =true;
					PeopleReqUtil.doRequestWebAPI(this, this,
							peopleDetialParam, null,
							PeopleRequestType.PEOPLE_REQ_GETPEOPLE); // 重新去请求详情的数据，以致于改变操作
				}
			}
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (eFromActivity == 1) { //当创建时，点击返回键就开启我的好友/人脉页面
				ENavigate.startMyFriendAll(this,
						ConnectionsCacheData.FILTER_PEOPLE_ALL);
			}
			finish();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
