package com.tr.ui.connections.revision20150122.detail;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.AppData;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.ConnectionsReqUtil;
import com.tr.api.PeopleReqUtil;
import com.tr.db.ConnectionsDBManager;
import com.tr.model.connections.ConnectionsCacheUtils;
import com.tr.model.connections.FriendRequest;
import com.tr.model.home.MUserQRUrl;
import com.tr.model.im.ChatDetail;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.obj.Connections;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.DynamicComment;
import com.tr.model.obj.DynamicPraise;
import com.tr.model.obj.JTContact2;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.connections.viewfrg.ListViewFragment;
import com.tr.ui.flow.frg.FrgFlow;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.knowledge.swipeback.SwipeBackActivity;
import com.tr.ui.knowledge.utils.ActivityCollector;
import com.tr.ui.people.contactsdetails.fragment.PeopleContactsdetailFragment;
import com.tr.ui.people.model.ConvertToPeople;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.params.PeopleDetialsIncomingParameters;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.tr.ui.widgets.RelationFriendHomeMenuPopupWindow;
import com.tr.ui.widgets.RelationFriendHomeMenuPopupWindow.OnFriendHomeMenuItemClickListener;
import com.tr.ui.widgets.RelationMyHomeMenuPopupWindow;
import com.tr.ui.widgets.RelationMyHomeMenuPopupWindow.OnMyHomeMenuItemClickListener;
import com.tr.ui.widgets.RelationOrtherHomeMenuPopupWindow;
import com.tr.ui.widgets.RelationOrtherHomeMenuPopupWindow.OnOrtherHomeMenuItemClickListener;
import com.tr.ui.widgets.viewpagerheaderscroll.SlidingTabLayout;
import com.tr.ui.widgets.viewpagerheaderscroll.SlidingTabLayout.TabColorizer;
import com.tr.ui.widgets.viewpagerheaderscroll.TouchCallbackLayout;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableFragmentListener;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableListener;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ViewPagerHeaderHelper;
import com.utils.common.EConsts;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 /**
 * 好友主页
 *   
 * 启动方法：ENavigate.startRelationHomeActivity
 * 
 * @param context
 * @param fromActivityType
 *        用来区分当前人的类型 
 * @param id
 *             用户id 
 * 
 * @author zhongshan
 * @since 2015-01-20
 * 
 */
public class RelationHomeActivity extends SwipeBackActivity implements TouchCallbackLayout.TouchEventListener, ScrollableFragmentListener, ViewPagerHeaderHelper.OnViewPagerTouchListener,
		OnClickListener {

	/** 用户id */
	private String userId;
	/** 用来区分当前人的类型 */
	private int type = 0;
	/** 线上：用户or线下：人脉 */
	private boolean isonline;
	/** 人脉对象：用户or人脉 */
	private JTContact2 onlineTContact;
	private JTContact2 offlineTContact;
	/**默认顶部布局高度*/
	private static final long DEFAULT_DURATION = 300L;
	private static final float DEFAULT_DAMPING = 1.5f;
	/**滑动监听器集合*/
	private SparseArrayCompat<ScrollableListener> mScrollableListenerArrays = new SparseArrayCompat<ScrollableListener>();
	/**资料，会面（目前没有），评论 */
	private ViewPager mViewPager;
	/**顶部布局父类View*/
	private View mHeaderLayoutView;
	/**顶部布局控制控件 */
	private ViewPagerHeaderHelper mViewPagerHeaderHelper;
	/**
	 * 资料模块
	 * */
	private PeopleContactsdetailFragment mPeopleContactsdetailFragment;
	/**pull>mTouchSlop=expand
	 * push>mTouchSlop=fold
	 * */
	private int mTouchSlop;
	/**滑动索引栏高度 */
	private int mTabHeight;
	/**顶部布局高度*/
	private int mHeaderHeight;
	/**滑动监听器*/
	private Interpolator mInterpolator = new DecelerateInterpolator();
	/**动态模块*/
	private FrgFlow frgFlow;
	/**评价模块*/
	private RelationEvaluationFrg relationEvaluationFrg;
	private ViewPagerAdapter viewPagerAdapter;

	private ImageView relationIv;
	private TextView relationPositionTv;
	private TextView relationNameTv;
	private ImageView relationBgRl;
	private ImageView qRCodeImage;

	private FrameLayout addFriendFLayout;
	private FrameLayout sendMessageFLayout;

	private boolean friendIsToPeople = false;
	/**人脉id*/
	private long peopleId;

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
	private String user_id;
	private TextView avatarText_RelationHome;

	public static final int result_recommend = 1;
	public static final int result_edit = 5;

	@Override
	public void initJabActionBar() {
		actionBar = getActionBar();
		actionBar.setTitle(" ");
		actionBar.hide();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relationhome);
		ActivityCollector.addActivity(this);

		userId = getIntent().getStringExtra(EConsts.Key.ID);
		if (StringUtils.isEmpty(userId)) {
			userId = App.getUserID();
		}

		type = getIntent().getIntExtra(ENavConsts.EFromActivityType, ENavConsts.type_details_other);
		isonline = getIntent().getBooleanExtra(EConsts.Key.isOnline, false); // online
		
		initFragment();

		initUI();

		initHeaderView();

		// init();
		initJsonReq();
	}

	private void initJsonReq() {
		PeopleDetialsIncomingParameters peopleDetialParam = new PeopleDetialsIncomingParameters();
		// peopleDetialParam.id = 20;//==id
		// peopleDetialParam.id = 72;//==id
		peopleDetialParam.id = Long.valueOf(userId);// ==id
		peopleDetialParam.personType = 1;// = personType//用户       
		PeopleReqUtil.doRequestWebAPI(this, ib, peopleDetialParam, null, PeopleRequestType.PEOPLE_REQ_GETPEOPLE);
	}

	private void initUI() {

		headerVi = findViewById(R.id.headerVi);

		// 初始化顶部titlebar
		/** titlebar的背景 */
		titlebar = (ImageView) findViewById(R.id.titlebar);
		/** 主页类型：我的，好友，他人 */
		hometypeTv = (TextView) findViewById(R.id.hometypeTv);
		/** 分享按钮 */
		relationShareIv = (ImageView) findViewById(R.id.relationShareIv);
		/** 更多操作按钮 */
		relationMoreIv = (ImageView) findViewById(R.id.relationMoreIv);
		/** 主页返回 */
		relationHomeBackIv = (ImageView) findViewById(R.id.relationHomeBackIv);

		/** 底部加好友 */
		addFriendFLayout = (FrameLayout) findViewById(R.id.addFriendFLayout);
		/** 底部发消息 */
		sendMessageFLayout = (FrameLayout) findViewById(R.id.sendMessageFLayout);
		/** 底部等待验证 */
		waitingPassFLayout = (FrameLayout) findViewById(R.id.waitingPassFLayout);

		relationShareIv.setOnClickListener(this);
		relationMoreIv.setOnClickListener(this);
		relationHomeBackIv.setOnClickListener(this);
		hometypeTv.setOnClickListener(this);

		titlebarHeight = getResources().getDimensionPixelSize(R.dimen.retion_title_height);
		mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
		mTabHeight = getResources().getDimensionPixelSize(R.dimen.tabs_height);
		mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.viewpager_header_height);

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
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(viewPagerAdapter);
		viewPagerAdapter.notifyDataSetChanged();

		slidingTabLayout.setViewPager(mViewPager);

		ViewCompat.setTranslationY(mViewPager, mHeaderHeight);
	}

	private void initFragment() {
//		frgFlow = new FrgFlow(0, this, Long.valueOf(userId.trim()));
//		frgFlow.setParent(RelationHomeActivity.this);
		frgFlow = new FrgFlow();
		frgFlow.type = FrgFlow.FLOW_PERSON;

		mPeopleContactsdetailFragment = PeopleContactsdetailFragment.newInstance(1 );

		relationEvaluationFrg = RelationEvaluationFrg.newInstance(2);
		relationEvaluationFrg.getArguments().putString(EConsts.Key.ID, userId);
		relationEvaluationFrg.upDate();
	}

	private void initHeaderView() {
		/** 头像 */
		relationIv = (ImageView) findViewById(R.id.RelationImage);
		/** */
		avatarText_RelationHome  =  (TextView) findViewById(R.id.avatarText_RelationHome);
		/** 公司职务 */
		relationPositionTv = (TextView) findViewById(R.id.relationPositionTv);
		/** 姓名 */
		relationNameTv = (TextView) findViewById(R.id.relationNameTv);
		/** 背景 */
		relationBgRl = (ImageView) findViewById(R.id.relationBgRl);
		/** 背景浮层 */
		relationBgRlCover = (ImageView) findViewById(R.id.relationBgRlCover);
		/** 二维码 */
		qRCodeImage = (ImageView) findViewById(R.id.QRCode);
		if (!userId.equals(App.getUserID())) {// 不是自己，隐藏二维码
			qRCodeImage.setVisibility(View.INVISIBLE);
		}
	}

	/*
	 * private void init() { if (ENavConsts.type_details_share == type) { //
	 * 分享的人脉 // 先读本地缓存 Object obj =
	 * ConnectionsCacheUtils.readConnectionObj(type, isonline, userId); if (obj
	 * != null) { initViewDetail(obj); } // 再联网取数据 JSONObject jsonObj = new
	 * JSONObject(); try { jsonObj.put("jtContactID", userId); } catch
	 * (JSONException e) { Log.d(TAG, e.getMessage() + ""); }
	 * showLoadingDialog(); ConnectionsReqUtil.getShareDetail(this, ib, jsonObj,
	 * null); } else { // 其它用户或人脉 // 先读本地缓存 Object obj =
	 * ConnectionsCacheUtils.readConnectionObj(type, isonline, userId); if (obj
	 * != null) { initViewDetail(obj); } // 再联网取数据 showLoadingDialog();
	 * JSONObject jsonObj = ConnectionsReqUtil.getContactDetailJson(userId,
	 * isonline); ConnectionsReqUtil.doContactDetail(this, ib, jsonObj, null); }
	 * 
	 * }
	 */
	/**
	 * 初始化数据
	 * @param person
	 */
	private void initNewViewDetail(Person person) {
		if (person != null) {
			initHeaderViewData(person);
			initBottomView(person);
//			frgFlow.showBottomNormalView();
			if (App.getUserID().equals(userId)) {// 用户自己
				hometypeTv.setText("我的主页");
				App.getUser().getJTContact().mcompany = person.company;
				new AppData().setNickName(personName);
				App.getUser().setNick(personName);
				App.getUser().setImage(person.portrait);
			}
			// 7-我的好友，8-待验证的好友，9-非好友，10-我自己
			else if (peopleDetails.type == 7) {
				hometypeTv.setText("好友主页");
			} else if (peopleDetails.type == 8 || peopleDetails.type == 9) {
				hometypeTv.setText("他人主页");
			}
		}
	}
	/**
	 * 初始化顶部布局数据
	 * @param person
	 */
	private void initHeaderViewData(Person person) {
		ImageLoader.getInstance().clearDiscCache();
		ImageLoader.getInstance().clearMemoryCache();
//		ImageLoader.getInstance().displayImage(person.portrait, relationIv);
		personName = person.peopleNameList != null && person.peopleNameList.size() > 0 && person.peopleNameList.get(0) != null ? /*personName = person.peopleNameList.get(0).lastname
				+ */person.peopleNameList.get(0).firstname : "";
		relationNameTv.setText(personName);
//		
//		if ("default.jpeg".equals(person.portrait.split("/")[person.portrait.split("/").length - 1])) {
//			if (person.getGender() == 1|| person.getGender() == 2) {
//				relationIv.setImageDrawable(getResources().getDrawable(R.drawable.no_avatar_but_gender));
//			} else {
//				relationIv.setImageDrawable(getResources().getDrawable(R.drawable.no_avatar_no_gender));
//			}		
//			if (StringUtils.isEmpty(personName)) {
//				avatarText_RelationHome.setText("");
//		} else {
//			char[] chs = null ; 
//			if (!StringUtils.isEmpty(personName)) {
//				chs = personName.toCharArray();
//			} 
//			avatarText_RelationHome.setText(String.valueOf(chs[chs.length - 1]));
//		}
//	} else {
//		ImageLoader.getInstance().displayImage(person.portrait, relationIv);
		ImageLoader.getInstance().displayImage(person.portrait, relationBgRl);// 设置头像背景
//		avatarText_RelationHome.setText("");
//	}
		Util.initAvatarImage(this, relationIv, personName, person.portrait,person.getGender(), 1);
	
		
		String companyName = person.company;
		String companyPosition = person.position;
		if (StringUtils.isEmpty(person.company)) {
			companyName = "";
		}
		if (StringUtils.isEmpty(person.position)) {
			companyPosition = "";
		}
		relationPositionTv.setText(companyName + companyPosition);
		// 人脉性别  1-男，2-女，3-未知
		if (person.gender == 2) {
			relationBgRlCover.setBackgroundResource(R.drawable.relation_girl_bg);// 设置粉红色浮层
		} else {
			relationBgRlCover.setBackgroundResource(R.drawable.relation_man_bg);// 设置蓝紫色浮层
		}

		qRCodeImage.setOnClickListener(this);
	}
	
	private void initViewDetail(Object object) {
		ArrayList returnData = (ArrayList) object;
		for (int i = 0; returnData.size() > i; i = i + 2) {
			int type = (Integer) returnData.get(i);
			if (type == 0) { // 用户
				onlineTContact = (JTContact2) returnData.get(i + 1);
				// initView(onlineTContact); 将用户对象传入fileFragment中
				if (onlineTContact != null) {

					initHeaderViewData(onlineTContact);
					// initBottomView(onlineTContact);
					/* 隐藏回复框 */
//					frgFlow.showBottomNormalView();
					viewPagerAdapter.notifyDataSetChanged();
				}
				if (this.type == ENavConsts.type_details_share) { // 分享的人脉

				} else if (onlineTContact.getID().equals(App.getUserID())) { // “我”的资料
					// actionBar.setTitle(" 我的主页");
					hometypeTv.setText("我的主页");
				} else if (onlineTContact.friendState == JTContactMini.type_friend) { // 好友的资料
					// 离线读缓存的时候，在oncreat()方法时调菜单里的布局控件
					// 由于生命周期，menu的控件还没有初使化，只能判断控件为空
					// 就不操作，导致菜单都为默认状态
					if (menuDeleteFriend != null) {
						menuDeleteFriend.setVisible(true);
					}
					if (menuBlackNumber != null) {
						menuBlackNumber.setVisible(true);
					}
					if (menuJoin != null) {
						menuJoin.setVisible(true);
					}
					// actionBar.setTitle(" 好友主页");
					hometypeTv.setText("好友主页");
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
					// actionBar.setTitle(" 他人主页");
					hometypeTv.setText("他人主页");
				}
			} else { // 人脉
				offlineTContact = (JTContact2) returnData.get(i + 1);
				if (offlineTContact != null) {
					viewPagerAdapter.notifyDataSetChanged();
				}
				if (this.type == ENavConsts.type_details_share) { // 转为“我”的人脉
				}
			}
		}
	}

	public void initBottomView(Person person) {
		if (!App.getUserID().equals(userId)) {// 非自己
			// 7-我的好友，8-待验证的好友，9-非好友，10-我自己
			if (peopleDetails.type == 7) {// 我的好友、发消息
				waitingPassFLayout.setVisibility(View.GONE);
				addFriendFLayout.setVisibility(View.GONE);
				sendMessageFLayout.setVisibility(View.VISIBLE);
				sendMessageFLayout.setOnClickListener(this);
			} else if (peopleDetails.type == 8) {// 等待验证
				addFriendFLayout.setVisibility(View.GONE);
				sendMessageFLayout.setVisibility(View.GONE);
				waitingPassFLayout.setVisibility(View.VISIBLE);
			} else if (peopleDetails.type == 9) {// 非好友、加好友
				sendMessageFLayout.setVisibility(View.GONE);
				waitingPassFLayout.setVisibility(View.GONE);
				addFriendFLayout.setVisibility(View.VISIBLE);
				addFriendFLayout.setOnClickListener(this);
			}
		}
	}

	public void hideAddFriendOrSendMessageBottomView() {
		addFriendFLayout.setVisibility(View.GONE);
		sendMessageFLayout.setVisibility(View.GONE);
		waitingPassFLayout.setVisibility(View.GONE);
		headerFold(0L);
	}

	/**
	 * 设置头布局数据
	 * 
	 * @param jTContact2
	 */
	private void initHeaderViewData(JTContact2 jTContact2) {
		ImageLoader.getInstance().clearDiscCache();
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().displayImage(jTContact2.getIconUrl(), relationIv);
		relationNameTv.setText(jTContact2.getName());
		String companyName = jTContact2.getCompany();
		String companyPosition = jTContact2.getUserJob();
		if (StringUtils.isEmpty(jTContact2.getCompany())) {
			companyName = "";
		}
		if (StringUtils.isEmpty(jTContact2.getUserJob())) {
			companyPosition = "";
		}
		if (App.getUserID().equals(jTContact2.getID())) {// 是自己
			App.getUser().getJTContact().mcompany = companyName;
			new AppData().setNickName(jTContact2.getName());
			App.getUser().setImage(jTContact2.getIconUrl());
		}
		relationPositionTv.setText(companyName + companyPosition);
		if (!TextUtils.isEmpty(jTContact2.getIconUrl())) {// 头像不为空
			ImageLoader.getInstance().displayImage(jTContact2.getIconUrl(), relationBgRl);// 设置头像
		}
		if (jTContact2.getSex() == "女士") {
			relationBgRlCover.setBackgroundResource(R.drawable.relation_girl_bg);// 设置粉红色浮层
		} else {
			relationBgRlCover.setBackgroundResource(R.drawable.relation_man_bg);// 设置蓝紫色浮层
		}

		qRCodeImage.setOnClickListener(this);
	}

	public Person person;
	private String qRCodeStr;
	public PeopleDetails peopleDetails;
	IBindData ib = new IBindData() {

		@Override
		public void bindData(int tag, Object object) {
			// 隐藏加载框
			dismissLoadingDialog();
			if (tag == EAPIConsts.concReqType.ContactDetail) { // 人脉详情

				if (object != null) {
					// 刷新页面
					initViewDetail(object);
					// 数据返回，写文件缓存
					ConnectionsCacheUtils.writeConnectionObj(type, isonline, userId, object);
					CommonReqUtil.doGetUserQRUrl(RelationHomeActivity.this, ib, userId, null);
					showLoadingDialogWithoutOnCancelListener();
				} else {
					finish();
				}
			} else if (tag == EAPIConsts.concReqType.editBlack) {

				if (object != null) {
					String str = (String) object;
					if (str.equals("true")) {
						showLongToast("已加入黑名单");
						IsChange = true;
						finish();
						return;
					} else {
						showToast("加入黑名单失败");
						IsChange = false;
					}
				}
				dismissLoadingDialog();
			} else if (tag == EAPIConsts.concReqType.im_addFriend) { // 添加好友
				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
						// showToast("请求发送成功");
						IsChange = true;
						addFriendFLayout.setVisibility(View.GONE);
						waitingPassFLayout.setVisibility(View.VISIBLE);
					} else {
						showToast("请求发送失败");
						IsChange = false;
					}
				}
			} else if (tag == EAPIConsts.concReqType.im_deleteFriend) { // 删除好友
				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
//						FrgConnections2.cnsCacheData.delete(userId, Connections.type_persion, onlineTContact.isOnline);
//						FrgConnections2.contactAdapter.dataChange();
						IsChange = true;
						ConnectionsDBManager.getInstance(RelationHomeActivity.this).delete(userId, Connections.type_persion, true);
						showToast("删除成功");
						Intent intent = new Intent();
						intent.putExtra("IsChange", IsChange);
						setResult(RESULT_OK, intent);
						finish();
					}
				} else {
					showToast("删除失败");
					IsChange = false;
				}
			} else if (tag == EAPIConsts.CommonReqType.getUserQRUrl) {// 获取二维码内容
				if (object != null) {
					MUserQRUrl userQRUrl = (MUserQRUrl) object;
					if (userQRUrl != null) {
						if (userQRUrl.isSuccess()) {
							qRCodeStr = userQRUrl.getUrl();
						}
					}
				}
			} else if (tag == PeopleRequestType.PEOPLE_REQ_GETPEOPLE) {
				if (object != null) {
					peopleDetails = (PeopleDetails) object;
					if (peopleDetails != null) {
						person = peopleDetails.people;
						if (peopleDetails.personIdAfterConvert != null&&peopleDetails.personIdAfterConvert!=0) {
							friendIsToPeople = true;
							peopleId = peopleDetails.personIdAfterConvert;
						}
						initNewViewDetail(person);
						CommonReqUtil.doGetUserQRUrl(RelationHomeActivity.this, ib, userId, null);
						// 填充资料详情
						if (mPeopleContactsdetailFragment != null) {
							mPeopleContactsdetailFragment.refreshView(peopleDetails);
						}
					}else {
						finish();
					}
				}else{
					finish();
					}
			} else if (tag == PeopleRequestType.PEOPLE_REQ_CONVERTPEOPLE) {
				if (object != null) {
					ConvertToPeople convertToPeople = (ConvertToPeople) object;
					boolean success = convertToPeople.success;
					peopleId = convertToPeople.personId;
					friendIsToPeople = success;
					if (success) {
						IsChange = true;
						showToast("转为人脉成功");
						ENavigate.startContactsDetailsActivity(RelationHomeActivity.this, 2, convertToPeople.personId, 0);
					} else {
						showToast("转为人脉失败");
						IsChange = false;
					}
				}

			}
		}
	};

	private class ViewPagerAdapter extends FragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			if (position == 0) {
				return frgFlow;
			} else if (position == 1) {
				return mPeopleContactsdetailFragment;
			} else if (position == 2) {
				return relationEvaluationFrg;
			} else {
				return ListViewFragment.newInstance(position);
			}

		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "动态";
			case 1:
				return "资料";
			case 2:
				return "评价";
			}
			return "";
		}
	}

	@Override
	public boolean isViewBeingDragged(MotionEvent event) {
		return mScrollableListenerArrays.valueAt(mViewPager.getCurrentItem()).isViewBeingDragged(event);
	}

	@Override
	public void onMoveStarted(float eventY) {

	}
	/**
	 * 头布局缩进
	 * @param duration
	 */
	private void headerFold(long duration) {
		ViewCompat.animate(mHeaderLayoutView).translationY(-(mHeaderHeight - titlebarHeight)).setDuration(duration).setInterpolator(mInterpolator).start();
		ViewCompat.animate(mViewPager).translationY(0 + mTabHeight).setDuration(duration).setInterpolator(mInterpolator).start();
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
		ViewCompat.animate(mHeaderLayoutView).translationY(0).setDuration(duration).setInterpolator(mInterpolator).start();
		ViewCompat.animate(mViewPager).translationY(mHeaderHeight).setDuration(duration).setInterpolator(mInterpolator).start();
		mViewPagerHeaderHelper.setHeaderExpand(true);
	}
	/** 移动布局触发的APi*/
	@Override
	public void onMove(float eventY, float yDx) {
		float headerTranslationY = ViewCompat.getTranslationY(mHeaderLayoutView) + yDx;
		if (headerTranslationY >= 0) { // pull end
			headerExpand(0L);
		} else if (headerTranslationY <= -mHeaderHeight + titlebarHeight) { // push
																			// end
			headerFold(0L);
		} else {
			ViewCompat.animate(mHeaderLayoutView).translationY(headerTranslationY).setDuration(0).start();
			ViewCompat.animate(mViewPager).translationY(headerTranslationY + mHeaderHeight).setDuration(0).start();
		}
	}
	/** 移动布局结束时触发的APi*/
	@Override
	public void onMoveEnded(boolean isFling, float flingVelocityY) {

		float headerY = ViewCompat.getTranslationY(mHeaderLayoutView); // 0到负数
		if (headerY == 0 || headerY == -mHeaderHeight) {
			return;
		}

		if (mViewPagerHeaderHelper.getInitialMotionY() - mViewPagerHeaderHelper.getLastMotionY() < -mTouchSlop) { // pull>mTouchSlop=expand
			headerExpand(headerMoveDuration(true, headerY, isFling, flingVelocityY));
		} else if (mViewPagerHeaderHelper.getInitialMotionY() - mViewPagerHeaderHelper.getLastMotionY() > mTouchSlop) { // push>mTouchSlop=fold
			headerFold(headerMoveDuration(false, headerY, isFling, flingVelocityY));
		} else {
			if (headerY > -mHeaderHeight / 2f) { // headerY > header/2 = expand
				headerExpand(headerMoveDuration(true, headerY, isFling, flingVelocityY));
			} else { // headerY < header/2= fold
				headerFold(headerMoveDuration(false, headerY, isFling, flingVelocityY));
			}
		}

	}

	private long headerMoveDuration(boolean isExpand, float currentHeaderY, boolean isFling, float velocityY) {

		long defaultDuration = DEFAULT_DURATION;

		if (isFling) {

			float distance = isExpand ? Math.abs(mHeaderHeight) - Math.abs(currentHeaderY) : Math.abs(currentHeaderY);
			velocityY = Math.abs(velocityY) / 1000;

			defaultDuration = (long) (distance / velocityY * DEFAULT_DAMPING);

			defaultDuration = defaultDuration > DEFAULT_DURATION ? DEFAULT_DURATION : defaultDuration;
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
		return mViewPagerHeaderHelper.onLayoutInterceptTouchEvent(ev, mTabHeight + mHeaderHeight);
	}

	@Override
	public boolean onLayoutTouchEvent(MotionEvent ev) {
		return mViewPagerHeaderHelper.onLayoutTouchEvent(ev);
	}

	public void doNewflow(Object object) {
	}

	boolean addFriendFLayoutBool = true;
	private boolean IsChange; //是否发生改变，

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.QRCode:// 二维码
			if (!StringUtils.isEmpty(qRCodeStr)) {
				ENavigate.startQRCodeActivity(RelationHomeActivity.this, qRCodeStr, personName, person.portrait);
			}
			break;
		case R.id.addFriendFLayout:// 加好友
			JSONObject jb = ConnectionsReqUtil.getReqNewFriend(String.valueOf(userId), FriendRequest.type_persion);
			ConnectionsReqUtil.doReqNewFriend(this, ib, jb, null);
			addFriendFLayoutBool = false;
			showLoadingDialog();
			break;
		case R.id.sendMessageFLayout:// 发消息
			ChatDetail chatDetail = new ChatDetail();
			chatDetail.setThatID(String.valueOf(userId));
			chatDetail.setThatImage(person.portrait);
			chatDetail.setThatName(personName);
			ENavigate.startIMActivity(RelationHomeActivity.this, chatDetail);
			break;
		case R.id.relationShareIv:// 分享
			if (person != null) {
				JTFile jtFile = new JTFile();
				jtFile.mType = JTFile.TYPE_JTCONTACT_ONLINE;
				jtFile.mTaskId = String.valueOf(userId);
//				jtFile.mFileName =App.getNick()+"分享了[人脉] ";//bug4653 分享到社交时在社交页面显示的信息 显示规则是：不用分好友人脉，都显示为人脉，不用分组织客户，都显示为客户
//				jtFile.mFileName =personName;
				jtFile.mFileName = person.peopleNameList.get(0).lastname
						+ person.peopleNameList.get(0).firstname;
				jtFile.mSuffixName = person.company;
				jtFile.mUrl = person.portrait;
				jtFile.reserved1 = person.position;
				if("0".equals(person.virtual)){//后台获取详情根据1、2判断，但给客户端是0、1
					jtFile.virtual = "1";
				}else{
					jtFile.virtual = "2";
				}
				FrameWorkUtils.showSharePopupWindow2(RelationHomeActivity.this, jtFile);
			}
			break;
		case R.id.relationMoreIv:// 更多
			if (person != null) {
				if (App.getUserID().equals(userId)) {// 自己
					RelationMyHomeMenuPopupWindow relationMyHomeMenuPopupWindow = new RelationMyHomeMenuPopupWindow(this);
					relationMyHomeMenuPopupWindow.showAsDropDown(headerVi);
					relationMyHomeMenuPopupWindow.setOnItemClickListener(new OnMyHomeMenuItemClickListener() {
						@Override
						public void joint() {// 对接
							ConnectionsMini connsMini = new ConnectionsMini();
							connsMini.setId(userId);
							connsMini.setType(1);
							connsMini.setName(personName);
							connsMini.setImage(person.portrait);
							ENavigate.startJointResourceActivity(RelationHomeActivity.this, ResourceType.User, connsMini);
						}

						@Override
						public void edit() {
							if (person != null) { // 编辑“我”的资料
								// fromActivitytype：1创建；2编辑；3-编辑"我"
								ENavigate.startNewConnectionsActivity(RelationHomeActivity.this, 3, peopleDetails, result_edit);
							}
						}
					});
				} else {
					// 7-我的好友，8-待验证的好友，9-非好友，10-我自己
					if (peopleDetails.type == 7) {// 好友
						RelationFriendHomeMenuPopupWindow relationFriendHomeMenuPopupWindow = new RelationFriendHomeMenuPopupWindow(this);
						relationFriendHomeMenuPopupWindow.showAsDropDown(headerVi);
						// 判断当前好友有没有转为人脉
						if (friendIsToPeople) {
							relationFriendHomeMenuPopupWindow.showLookPeopleView();// 显示查看人脉
						} else {
							relationFriendHomeMenuPopupWindow.showToPeopleView();// 显示转人脉
						}
						relationFriendHomeMenuPopupWindow.setOnItemClickListener(new OnFriendHomeMenuItemClickListener() {

							@Override
							public void joint() {
								ConnectionsMini connsMini = new ConnectionsMini();
								connsMini.setId(String.valueOf(userId));
								connsMini.setType(1);
								connsMini.setName(personName);
								connsMini.setImage(person.portrait);
								ENavigate.startJointResourceActivity(RelationHomeActivity.this, ResourceType.User, connsMini);
							}

							@Override
							public void deletFriend() {
								showLoadingDialog();
								ConnectionsReqUtil.dodeleteFriend(RelationHomeActivity.this, ib, ConnectionsReqUtil.getDeleteFriendJson(String.valueOf(userId), FriendRequest.type_persion), null);
							}

							@Override
							public void blackNum() {
								showAddBlackNumDialog();
							}

							@Override
							public void toRenMai() {// 好友转人脉
//								RequestConvertToPeople params = new RequestConvertToPeople();
//								params.personId = Long.valueOf(person.id);// 当前用户的id,需替换成穿过来的personId
//								PeopleReqUtil.doRequestWebAPI(RelationHomeActivity.this, ib, params, null, PeopleRequestType.PEOPLE_REQ_CONVERTPEOPLE);
								ENavigate.startNewConnectionsActivity(RelationHomeActivity.this, 5, peopleDetails, null);
							}

							@Override
							public void lookRenMai() {// 好友已转为人脉--查看人脉
								ENavigate.startContactsDetailsActivity(RelationHomeActivity.this, 2, Long.valueOf(peopleId), 0);
							}

							@Override
							public void toAffair() {//好友转为事务,转换为事务
								ConnectionNode inPeopleNode = new ConnectionNode();
								ArrayList<Connections>  listConnections = new ArrayList<Connections>();
								JTContactMini jtContact = new JTContactMini();
								jtContact.setId(userId);
								jtContact.setName(personName);
								jtContact.setImage(person.portrait);
								jtContact.setCompany(person.company);
								jtContact.setOnline(true);
								Connections connections = new Connections(jtContact);
								connections.id = userId;
								listConnections.add(connections);
								inPeopleNode.setListConnections(listConnections);
								inPeopleNode.setMemo("好友");
								ENavigate.startNewAffarActivityByRelation(RelationHomeActivity.this, inPeopleNode, null, null, null);
							}
							@Override
							public void toReport() {
								ENavigate.startReportActivity(RelationHomeActivity.this, peopleDetails.people);
							}
						});

					} else if (peopleDetails.type == 9) {// 非好友：2-非好友
						showAddFriendDialog();

					} else if (peopleDetails.type == 8) {// 等待验证:1,3
						addFriendFLayoutBool = false;
						showAddFriendDialog();
					}
				}
			}
			break;

		case R.id.relationHomeBackIv:// 返回
		case R.id.hometypeTv:
			
			finish();
			break;
		default:
			break;
		}
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
		RelationOrtherHomeMenuPopupWindow relationOrtherHomeMenuPopupWindow = new RelationOrtherHomeMenuPopupWindow(this);
		if (!addFriendFLayoutBool) {
			relationOrtherHomeMenuPopupWindow.hintAddFriendTv();
		}
		relationOrtherHomeMenuPopupWindow.showAsDropDown(headerVi);
		relationOrtherHomeMenuPopupWindow.setOnItemClickListener(new OnOrtherHomeMenuItemClickListener() {
			@Override
			public void blackNum() {
				showAddBlackNumDialog();
			}

			@Override
			public void addFriend() {
				JSONObject jb = ConnectionsReqUtil.getReqNewFriend(String.valueOf(userId), FriendRequest.type_persion);
				ConnectionsReqUtil.doReqNewFriend(RelationHomeActivity.this, ib, jb, null);
				showLoadingDialog();
			}

			@Override
			public void ToReport() {
				
			}
		});
	}

	/** 加入黑名单 */
	private void showAddBlackNumDialog() {
		new MessageDialog(RelationHomeActivity.this, "加入黑名单你将收不到对方的信息,\n并且你们相互看不到对方的主页", new OnDialogFinishListener() {
			@Override
			public void onFinish(String content) {
				if (person != null) {
					ArrayList<String> arrayList = new ArrayList<String>();
					arrayList.add(String.valueOf(userId));
					ConnectionsReqUtil.doEditBlack(RelationHomeActivity.this, ib, arrayList, "1", null);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);

		switch (requestCode) {
		case result_edit:// 编辑人脉
			if (resultCode == Activity.RESULT_OK) {
				showLoadingDialog();
				PeopleDetialsIncomingParameters peopleDetialParam = new PeopleDetialsIncomingParameters();
				peopleDetialParam.id = Long.valueOf(userId);
				peopleDetialParam.personType = 1;
				PeopleReqUtil.doRequestWebAPI(this, updataib, peopleDetialParam, null, PeopleRequestType.PEOPLE_REQ_GETPEOPLE);
			}
			break;
		case ENavConsts.ActivityReqCode.REQUEST_CODE_LOOK_MORE_COMMENT:// 动态
			Bundle bundle = intent.getExtras();
			List<DynamicComment> mListComment = (List<DynamicComment>) bundle.getSerializable(ENavConsts.KEY_FRG_CHANGE_COMMENTS);
			ArrayList<DynamicPraise> mDynamicPraises = (ArrayList<DynamicPraise>) bundle.getSerializable(ENavConsts.KEY_FRG_FLOW_DYNAMIC_PRAISES);
			int index = bundle.getInt(ENavConsts.KEY_FRG_FLOW_INDEX, 0);
//			frgFlow.changeDynamicListUI(mDynamicPraises, mListComment, index);
			break;
		case ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_MORE_EVALUATION:// 更多评价
		case ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_EDIT_EVALUATION:// 编辑评价
			relationEvaluationFrg.onActivityResult(requestCode, resultCode, intent);
			break;
		default:
			break;
		}
	}

	// 因为更新的特殊性，要单独写一个IBindData
	IBindData updataib = new IBindData() {
		@Override
		public void bindData(int tag, Object object) {
			dismissLoadingDialog();
			if (object != null) {
				if (peopleDetails != null) {
					peopleDetails = null;
					person = null;
				}
				peopleDetails = (PeopleDetails) object;
				person = peopleDetails.people;
				if (peopleDetails.personIdAfterConvert != null) {
					friendIsToPeople = true;
				}
				initNewViewDetail(person);
				CommonReqUtil.doGetUserQRUrl(RelationHomeActivity.this, ib, userId, null);
				// 填充资料详情
				if (mPeopleContactsdetailFragment != null) {
					mPeopleContactsdetailFragment.refreshView(peopleDetails);
				}
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
	private String personName;

	public JTContact2 getOnlineTContact() {
		return onlineTContact;
	}

	public Person getPerson() {
		return person;
	}

}
