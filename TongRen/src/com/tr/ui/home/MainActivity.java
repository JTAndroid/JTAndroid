package com.tr.ui.home;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushManager;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.CommunityReqUtil;
import com.tr.api.ConnectionsReqUtil;
import com.tr.db.ConnectionsCacheData;
import com.tr.db.SocialityDBManager;
import com.tr.imservice.ReceiveMsgService;
import com.tr.imservice.ReceiveMsgService.GetConnectState;
import com.tr.model.SimpleResult;
import com.tr.model.conference.MSociality;
import com.tr.model.home.MIndustrys;
import com.tr.model.home.MUserQRUrl;
import com.tr.model.im.ChatDetail;
import com.tr.model.im.MGetListIMRecord;
import com.tr.model.im.MJTPushMessage;
import com.tr.model.im.MNotifyMessageBox;
import com.tr.model.obj.DynamicComment;
import com.tr.model.obj.DynamicPraise;
import com.tr.model.obj.IMRecord;
import com.tr.model.obj.JTFile;
import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.service.GetConnectionsListService.RequestType;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.communities.home.JoinCommumitiesAvtivity;
import com.tr.ui.communities.model.Community;
import com.tr.ui.communities.model.CommunityApply;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.communities.model.Notification;
import com.tr.ui.conference.square.SquareActivity;
import com.tr.ui.home.frg.FrgFlow;
import com.tr.ui.home.frg.FrgFlow.FlowSelectType;
import com.tr.ui.home.frg.FrgFound;
import com.tr.ui.home.frg.FrgMyHomePage;
import com.tr.ui.home.frg.FrgSociality;
import com.tr.ui.home.frg.GintongMainFragment;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.TongRenFragment;
import com.tr.ui.widgets.CustomViewPager;
import com.tr.ui.widgets.EditTextAlertDialog;
import com.tr.ui.widgets.EditTextAlertDialog.OnEditDialogClickListener;
import com.tr.ui.widgets.GodCreatedPopupWindow;
import com.tr.ui.widgets.GuidePopupWindow;
import com.tr.ui.widgets.NoticeDialog;
import com.tr.ui.widgets.floatmeune.FloatingActionMenu;
import com.tr.ui.widgets.floatmeune.FloatingActionMenu.Builder;
import com.tr.ui.widgets.floatmeune.SubActionButton;
import com.tr.ui.widgets.title.menu.popupwindow.ActionItem;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup.OnPopuItemOnClickListener;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableFragmentListener;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableListener;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.CommunityReqType;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;
import com.zxing.android.CaptureActivity;

/**
 * @Update 2015-1-12 -框架修改
 * @description 登陆后主页面
 */
public class MainActivity extends JBaseFragmentActivity implements IBindData, View.OnClickListener, ScrollableFragmentListener {

	private final String TAG = "MainActivity";
	private Context mContext;
	public static final int REQUEST_CODE_PHOTO_GRAPH = 30001;// 拍照
	public static final int REQUEST_CODE_PHOTO_TRIM = 30002; // 裁剪
	public static final int REQUEST_CODE_PHOTO_RESOULT = 30003;// 结果
	public static final int REQUEST_CODE_PHOTO_ALBUM = 30004;// 相册

	public static byte ITEM_NEWConnections = 0;
	public static byte ITEM_GROUP = 1;

	/** ViewPager容器 */
	private CustomViewPager mPager;
	/** ViewPager容器中的Fragment */
	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	/* 首页动态 */
	private FrgFlow frgTogether;
	/* 发现 */
	private FrgFound mFrgFound;
	/* 桐人 */
	private TongRenFragment frgTongRen;
	/* 我的 */
	private FrgMyHomePage mMyHomePageFragment;
	/* 社交 */
	private FrgSociality mSocialityFragment;
	/* 程序退出标记 */
	private boolean isExit = false;
	/* 记录返回键数据 */
	private long mFirstTime = 0;
	/* 环信Intent */
	private Intent iMIntent;
	private FrameLayout mInletDynamicFL;
	private FrameLayout mInletCreateFL;//创建
	private FrameLayout mInletGamFL;
	private FrameLayout mInletAffFl; // 事务
	private FrameLayout mRedDotAff; // 事务下标题栏上的红点
	private FrameLayout mInletMeFL;
	private TextView headerVi;
	private boolean floatMenuisMove;
	private String data;
	private ReceiveMsgService receiveMsgService;
	private boolean conncetState = true; // 记录当前连接状态，因为广播会接收所有的网络状态改变wifi/3g等等,所以需要一个标志记录当前状态
	private JTFile shareInfo;
	private MIndustrys mIndustrys;
	private FrameLayout mGlobalCreatButton;
	private FloatingActionMenu itemMenu;
	private Builder builder;
	private boolean isFaile;
	private int containerHeight;
	private int containerWidth;
	private android.app.ActionBar actionbar;
	private int subSizeRadius;
	private int radiusSize;
	private FrameLayout rootLl;
	private TextView biggerButton;
	private TextView smallerbutton;
	private ImageView bottom_aff_red_dot;// 红点图片
	private int awesomeMenuAlphaCounter;
	private String meetingId;
	private TextView mInletDynamicTv;
	private ImageView mInletDynamicIv;
	private ImageView mInletCreateIv;
	private TextView mInletGamTv;
	private ImageView mInletGamIv;
	private ImageView mInletAffIv; // 事务
	private TextView mInletAffTv;
	private TextView mInletMeTv;
	private ImageView mInletMeIv;
	private LinearLayout mBottomLL;
	private LinearLayout mInletDynamicRemind;
	private FrameLayout inlet_gam_remind_fl;
	private SocialityDBManager dbManager;
	private SharedPreferences sp;
	private boolean isShowCreateButton;
	private HomeFrgPagerAdapter homeFrgPagerAdapter;
	private TextView navigateNumTv;
	private TitlePopup titlePopup;
	
	private SharedPreferences firstuse_sp;
	private SharedPreferences.Editor firstuse_edtior;
	private SharedPreferences.Editor firstuse_edtior_time;
	private SharedPreferences firstuse_time;
	private ArrayList mListViews = new ArrayList<View>();// 将要分页显示的View装入数组中  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		// 测试，获取可用内存大小和最大可用内存大小
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		int nowMemory = mActivityManager.getMemoryClass();
		int maxMemory = mActivityManager.getLargeMemoryClass();
		KeelLog.d(TAG, "nowM = " + nowMemory + ":maxM=" + maxMemory);
		setContentView(R.layout.activity_home_main);
		sp = getApplication().getSharedPreferences(EConsts.share_invisible_create_button, Context.MODE_PRIVATE);
		isShowCreateButton = sp.getBoolean(EConsts.share_invisible_create_button, true);
		isFaile = getIntent().getBooleanExtra(ENavConsts.IsFaile, true);
		if (!isFaile) {
			new AlertDialog.Builder(MainActivity.this).setMessage("").setTitle("聊天服务器登录失败，请重新登录重试或联系客服(4000-7000-11)").setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

		}
		initializeControl();
		initJabActionBar();
		/*记录第一次登陆的时间*/
		firstuse_time = getSharedPreferences(GlobalVariable.SHARED_PREFERENCES_FIRST_LOGIN_TIME,MODE_PRIVATE);
		firstuse_edtior_time = firstuse_time.edit();
		Calendar nowCalendar = Calendar.getInstance();
		String loginTimeString = firstuse_time.getString(
				GlobalVariable.MAIN_FIRST_TIME, "");
		if (loginTimeString.equals("")) {
			firstuse_edtior_time.putString(GlobalVariable.MAIN_FIRST_TIME,TimeUtil.TimeMillsToStringWithMinute(nowCalendar.getTimeInMillis()));
			firstuse_edtior_time.commit();
		}
		
		// app初始化完毕
		EMChat.getInstance().setAppInited();
		// 社交页面本地数据库
		dbManager = SocialityDBManager.getInstance(this);
		List<String> tags = new ArrayList<String>();
		/** 从二维码返回的字符串取出id和type */
		data = getIntent().getStringExtra(ENavConsts.EFriendId);
		if (!StringUtils.isEmpty(data)) {
			String friendId = getIdFromQRCode(data);
			int type = getTypeFromQRCode(data);
			ENavigate.startInviteFriendByQRCodeActivity(this, friendId, type);
		}
		// 事务
		frgTongRen = new TongRenFragment(mRedDotAff);
		mInletDynamicFL.setOnClickListener(this);
		mInletCreateFL.setOnClickListener(this);
		mInletGamFL.setOnClickListener(this);
		mInletAffFl.setOnClickListener(this);
		mInletMeFL.setOnClickListener(this);
		initFrgment();
		InitTextView();
		InitViewPager();
		/* 首页发现入口 */
		// 以apikey的方式登录，一般放在主Activity的onCreate中
		// PushManager.startWork(getApplicationContext(),
		// PushConstants.LOGIN_TYPE_API_KEY,
		// EAPIConsts.getBaiduPushApiKey());

		tags.add("imtest");
		PushManager.setTags(getApplicationContext(), tags);
		// 处理消息盒子跳转
		if (getIntent().hasExtra(ENavConsts.ENotifyParam)) {
			getParam(getIntent());
		}
		// 处理分享跳转
		else if (getIntent().hasExtra(ENavConsts.EShareParam)) {
			getParamEx(getIntent());
		}
		// 请求用户二维码
		CommonReqUtil.doGetUserQRUrl(this, this, App.getUserID(), null);
		// 开启 获得关系列表服务
		ENavigate.startGetConnectionsListService(MainActivity.this, RequestType.All);
		bind();// 绑定服务
		initPopWindow();
	}
	
	
	
	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(mContext, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(mContext, R.string.chat_launch/*, R.drawable.chat_launch*/));
		titlePopup.addAction(new ActionItem(mContext, R.string.invite_friends/*, R.drawable.home_invitation_with_heart*/));
		titlePopup.addAction(new ActionItem(mContext, R.string.add_group/*, R.drawable.industry_love*/));
		titlePopup.addAction(new ActionItem(mContext, R.string.richscan/*, R.drawable.home_sweep*/));
//		titlePopup.addAction(new ActionItem(mContext, R.string.help/*, R.drawable.home_sweep*/));
	}
	//社群号
	private String communityNO;
	private Community community;
	private long communityId;
	private String notice;
	private OnPopuItemOnClickListener onitemClick = new OnPopuItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			// mLoadingDialog.show();
			switch (position) {
			case 0:// 发起畅聊
				ENavigate.startIMRelationSelectActivity(MainActivity.this, null, null, 0, null, null);
				break;
			case 1:// 添加朋友
				ENavigate.startMeetingInviteFriendsActivity(mContext);
				break;
			case 2:// 暗号加群
				EditTextAlertDialog etDialog = new EditTextAlertDialog(MainActivity.this);
				etDialog.setTitle("输入社群号加入社群");
				etDialog.setHint("");
				etDialog.setOkTv("加入");
				etDialog.setOnDialogClickListener(new OnEditDialogClickListener() {
					@Override
					public void onClick(String evaluationValue) {
						if(evaluationValue!=null){
							communityNO = evaluationValue;
							showLoadingDialog();
							CommunityReqUtil.existCommunityNo(MainActivity.this, MainActivity.this, communityNO, null);
						}
					}
				});
				etDialog.show();

				break;
			case 3:// 扫一扫
				 startActivityForResult(new Intent(mContext,CaptureActivity.class), 1000);
				break;
			case 4:// 帮助
					// TODO 跳转到帮助
				startActivity(new Intent(mContext, MainDrawerActivity.class));
				break;
			default:
				break;
			}
		}
	};
	/** 初始化控件 */
	public void initializeControl() {
		rootLl = (FrameLayout) findViewById(R.id.rootLl);
		mBottomLL = (LinearLayout) findViewById(R.id.home_frg_inlet_bottom);
		mInletDynamicTv = (TextView) findViewById(R.id.activity_inlet_dynamic_tv);
		mInletDynamicIv = (ImageView) findViewById(R.id.activity_inlet_dynamic_iv);
		mInletCreateIv = (ImageView) findViewById(R.id.activity_inlet_create_iv);
		mInletGamTv = (TextView) findViewById(R.id.activity_inlet_gam_tv);
		mInletGamIv = (ImageView) findViewById(R.id.activity_inlet_gam_iv);
		mInletAffIv = (ImageView) findViewById(R.id.activity_inlet_aff_iv);
		mInletAffTv = (TextView) findViewById(R.id.activity_inlet_aff_tv);
		mInletMeTv = (TextView) findViewById(R.id.activity_inlet_me_tv);
		mInletMeIv = (ImageView) findViewById(R.id.activity_inlet_me_iv);
		mInletDynamicFL = (FrameLayout) findViewById(R.id.activity_inlet_dynamic_fl);
		mInletCreateFL = (FrameLayout) findViewById(R.id.activity_inlet_create_fl);
		mInletGamFL = (FrameLayout) findViewById(R.id.activity_inlet_gam_fl);
		mInletMeFL = (FrameLayout) findViewById(R.id.activity_inlet_me_fl);
		mInletAffFl = (FrameLayout) findViewById(R.id.activity_inlet_aff_fl); // 事务
		mGlobalCreatButton = (FrameLayout) findViewById(R.id.home_global_creat_buttom);
		biggerButton = (TextView) findViewById(R.id.home_global_creat_buttom_bigger);
		smallerbutton = (TextView) findViewById(R.id.home_global_creat_buttom_smaller);
		awesomeMenuOpacityChange();// 开始计时
		mInletDynamicRemind = (LinearLayout) findViewById(R.id.inlet_dynamic_remind);
		inlet_gam_remind_fl = (FrameLayout) findViewById(R.id.inlet_gam_remind_fl);
		mRedDotAff = (FrameLayout) findViewById(R.id.aff_red_dot);// 下标题栏事务上的红点
		bottom_aff_red_dot = (ImageView) findViewById(R.id.bottom_aff_red_dot);// 红点图片
		inlet_gam_remind_fl.setVisibility(View.GONE);
		headerVi = (TextView) findViewById(R.id.text_transparent_line);
		initializeSizesExpandableSelector();
		navigateNumTv = (TextView) findViewById(R.id.inlet_gam_remind_num_tv);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// 这里来获取容器的宽和高
		if (hasFocus) {
			containerHeight = rootLl.getHeight() - mBottomLL.getHeight();
			containerWidth = rootLl.getWidth();
		}
	}

	private void awesomeMenuOpacityChange() {
		awesomeMenuAlphaCounter++;
		handler.sendEmptyMessageDelayed(3, 5 * 1000);
	}

	private View initSubButton(Context mContext, int id, String str, int viewId) {
		ImageView rlIcon = new ImageView(mContext);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			rlIcon.setBackground(getResources().getDrawable(id));
		} else {
			rlIcon.setBackgroundDrawable(getResources().getDrawable(id));
		}
		rlIcon.setScaleType(ScaleType.CENTER_CROP);
		rlIcon.setId(viewId);
		rlIcon.setOnClickListener(this);
		return rlIcon;
	}

	private void initializeSizesExpandableSelector() {
		subSizeRadius = getResources().getDimensionPixelSize(R.dimen.sub_action_button_size) / 2;
		radiusSize = getResources().getDimensionPixelSize(R.dimen.radius_medium);
		SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(mContext);
		int subsize = getResources().getDimensionPixelSize(R.dimen.sub_action_button_size);
		FrameLayout.LayoutParams subLayoutParams = new FrameLayout.LayoutParams(subsize, subsize);
		ImageView rlIcon1 = (ImageView) initSubButton(mContext, R.drawable.float_menu_item_knowle, "知识", 4);
		ImageView rlIcon2 = (ImageView) initSubButton(mContext, R.drawable.float_menu_item_affair, "事务", 3);
		ImageView rlIcon3 = (ImageView) initSubButton(mContext, R.drawable.float_menu_item_meet, "会议", 2);
		ImageView rlIcon4 = (ImageView) initSubButton(mContext, R.drawable.float_menu_item_chat, "畅聊", 1);
		rLSubBuilder.setLayoutParams(subLayoutParams);
		builder = new FloatingActionMenu.Builder(mContext);
		itemMenu = builder.setStartAngle(90).setEndAngle(270).setRadius(getResources().getDimensionPixelSize(R.dimen.radius_medium)).addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon2).build()).addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
				.addSubActionView(rLSubBuilder.setContentView(rlIcon4).build()).attachTo(mGlobalCreatButton).build();
		mGlobalCreatButton.setOnTouchListener(new OnTouchListener() {
			float lastX, lastY;
			float x1, y1, x2, y2;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (isShowCreateButton) {
					smallerbutton.getBackground().setAlpha(255);
					biggerButton.getBackground().setAlpha(255);
					AnimatorSet animatorSet = new AnimatorSet();
					ObjectAnimator x;
					ObjectAnimator y;
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if (itemMenu.isOpen()) {
							itemMenu.close(true);
						}
						lastX = event.getRawX();
						lastY = event.getRawY();
						x1 = event.getRawX();
						y1 = event.getRawY();
						biggerButton.setVisibility(View.VISIBLE);
						smallerbutton.setVisibility(View.GONE);
						awesomeMenuOpacityChange();
						break;
					case MotionEvent.ACTION_MOVE:
						if (itemMenu.isOpen()) {
							itemMenu.close(true);
						}
						float distanceX = lastX - event.getRawX();
						float distanceY = lastY - event.getRawY();
						float nextY = mGlobalCreatButton.getY() - distanceY;
						float nextX = mGlobalCreatButton.getX() - distanceX;
						// 不能移出屏幕
						if (nextY < 0) {
							nextY = 0;
						} else if (nextY > containerHeight - mGlobalCreatButton.getHeight()) {
							nextY = containerHeight - mGlobalCreatButton.getHeight();
						}
						if (nextX < 0)
							nextX = 0;
						else if (nextX > containerWidth - mGlobalCreatButton.getWidth())
							nextX = containerWidth - mGlobalCreatButton.getWidth();
						// 属性动画移动
						y = ObjectAnimator.ofFloat(mGlobalCreatButton, "y", mGlobalCreatButton.getY(), nextY);
						x = ObjectAnimator.ofFloat(mGlobalCreatButton, "x", mGlobalCreatButton.getX(), nextX);
						animatorSet.playTogether(x, y);
						animatorSet.setDuration(0);
						animatorSet.start();
						lastX = event.getRawX();
						lastY = event.getRawY();
						awesomeMenuOpacityChange();
						break;
					case MotionEvent.ACTION_UP:
						if (itemMenu.isOpen()) {
							itemMenu.close(true);
						}
						x2 = event.getRawX();
						y2 = event.getRawY();
						double distance = Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2) + Math.abs(y1 - y2) * Math.abs(y1 - y2));// 两点之间的距离
						biggerButton.setVisibility(View.GONE);
						smallerbutton.setVisibility(View.VISIBLE);
						if (distance < 15) {
							floatMenuisMove = false;
							if (!itemMenu.isOpen()) {
								// ————————位于上半部分————————
								if (mGlobalCreatButton.getY() < radiusSize + subSizeRadius - mGlobalCreatButton.getHeight() / 2) {
									y = ObjectAnimator.ofFloat(mGlobalCreatButton, "y", mGlobalCreatButton.getY(), radiusSize + subSizeRadius - mGlobalCreatButton.getHeight() / 2);
									x = ObjectAnimator.ofFloat(mGlobalCreatButton, "x", mGlobalCreatButton.getX(), mGlobalCreatButton.getX());
									animatorSet.playTogether(x, y);
									animatorSet.setDuration(0);
									animatorSet.start();
									itemMenu.open(true);
								} else if (containerHeight - mGlobalCreatButton.getY() < radiusSize + subSizeRadius + mGlobalCreatButton.getHeight() / 2) {
									y = ObjectAnimator.ofFloat(mGlobalCreatButton, "y", mGlobalCreatButton.getY(), containerHeight - radiusSize - subSizeRadius - mGlobalCreatButton.getHeight() / 2);
									x = ObjectAnimator.ofFloat(mGlobalCreatButton, "x", mGlobalCreatButton.getX(), mGlobalCreatButton.getX());
									animatorSet.playTogether(x, y);
									animatorSet.setDuration(0);
									animatorSet.start();
									itemMenu.open(true);
								}
							}
						} else {
							floatMenuisMove = true;
						}
						if (floatMenuisMove) {
							if (mGlobalCreatButton.getX() < containerWidth / 2) {
								itemMenu.notifiyAngle(90, -90);
								// 移动到左边 属性动画移动
								y = ObjectAnimator.ofFloat(mGlobalCreatButton, "y", mGlobalCreatButton.getY(), mGlobalCreatButton.getY());
								x = ObjectAnimator.ofFloat(mGlobalCreatButton, "x", mGlobalCreatButton.getX(), 0);
								animatorSet.playTogether(x, y);
								animatorSet.setDuration(200);
								animatorSet.start();
							} else {
								itemMenu.notifiyAngle(90, 270);
								y = ObjectAnimator.ofFloat(mGlobalCreatButton, "y", mGlobalCreatButton.getY(), mGlobalCreatButton.getY());
								x = ObjectAnimator.ofFloat(mGlobalCreatButton, "x", mGlobalCreatButton.getX(), containerWidth - mGlobalCreatButton.getWidth());
								animatorSet.playTogether(x, y);
								animatorSet.setDuration(200);
								animatorSet.start();
							}
						}
						awesomeMenuOpacityChange();
						break;
					}
//					mGlobalCreatButton.clearFocus();
					return floatMenuisMove;
				} else {
					return false;
				}
			}
		});
	}

	private void initFrgment() {
		SharedPreferences sp = getSharedPreferences(
						GlobalVariable.SHARED_PREFERENCES_SOCIAL_ISFISTLOAD,
						this.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(GlobalVariable.SOCIAL_ISFISTLOAD, true);
		editor.commit();
		frgTogether = new FrgFlow(mBottomLL, this, mGlobalCreatButton, itemMenu, mInletDynamicRemind);
		frgTogether.setSelectType(FlowSelectType.all, false);
		frgTogether.setParent(MainActivity.this);
//		mFragments.add(frgTogether);// 动态
		mFragments.add(new GintongMainFragment(MainActivity.this));// 首页
		mSocialityFragment = new FrgSociality(inlet_gam_remind_fl);
		mFragments.add(mSocialityFragment);// 社交
		// 事务-->>桐人
		mFragments.add(frgTongRen);// 事务
//		mFrgFound = new FrgFound();// 发现Frg
//		mFragments.add(mFrgFound);// 发现
		mMyHomePageFragment = new FrgMyHomePage();
		mFragments.add(mMyHomePageFragment);// 我
	}

	/** 初始化ActionBar */
	public void initJabActionBar() {
		actionbar = jabGetActionBar();
		actionbar.setDisplayHomeAsUpEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);
		HomeCommonUtils.initLeftMainActionBar(MainActivity.this, actionbar, "首页", false, null, R.color.registration_total_color);
		actionbar.setSplitBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg));
		Drawable myDrawable = getResources().getDrawable(R.drawable.action_bar_bg);
		actionbar.setBackgroundDrawable(myDrawable); // 设置背景图片
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		KeelLog.d(TAG, "onSaveInstanceState");
	}

	@Override
	public void onNewIntent(Intent intent) {
		getParam(intent); // 处理消息盒子跳转
		getParamEx(intent); // 处理分享跳转
	}

	public void getParam(Intent intent) {
		KeelLog.d(TAG, "getParam BEGIN");
		try {
			MNotifyMessageBox box = MNotifyMessageBox.getInstance();
			if (box != null) {
				// 推送的消息类型
				int pushMessageType = intent.getIntExtra(ENavConsts.EPushMessageType, 0);
				// Log.i(TAG, MSG + " pushMessageType = " + pushMessageType);
				if (pushMessageType == MJTPushMessage.TYPE_CHAT) { // 私聊
					MJTPushMessage pm = null;
					for (MJTPushMessage msg : box.getListMessage()) {
						if (msg.getType() == MJTPushMessage.TYPE_CHAT) {
							pm = msg;
							break;
						}
					}
					if (pm != null) {
						ChatDetail chatDetail = new ChatDetail();
						chatDetail.setThatID(pm.getSendUserID());
						chatDetail.setThatImage("");
						chatDetail.setThatName(pm.getSendName());
						ENavigate.startIMActivity(this, chatDetail);
					} else {
						Log.d(TAG, "不跳转，停留在此页面");
					}
					MNotifyMessageBox.clearMessage();
				} else if (pushMessageType == MJTPushMessage.TYPE_MUC) { // 群聊
					if (box.getChatCount() == 1) {
						String mucId = "";
						for (MJTPushMessage msg : box.getListMessage()) {
							if (msg.getType() == MJTPushMessage.TYPE_MUC) {
								mucId = msg.getMucID();
								break;
							}
						}
						ENavigate.startIMGroupActivity(this, mucId);
					} else {
						Log.d(TAG, "不跳转，停留在此页面");
					}
					MNotifyMessageBox.clearMessage();
				}else if(pushMessageType == MJTPushMessage.TYPE_COMMUNITY) { // 社群聊天
					if (box.getChatCount() == 1) {
						ImMucinfo community = new ImMucinfo();
						for (MJTPushMessage msg : box.getListMessage()) {
							if (msg.getType() == MJTPushMessage.TYPE_COMMUNITY) {
								community.setId(Long.valueOf(msg.getMucID()));
								community.setTitle(msg.getTitle());
								community.setPicPath("");
								break;
							}
						}
						ENavigate.startIMCommunityActivity(this, community);
					} else {
						Log.d(TAG, "不跳转，停留在此页面");
					}
					MNotifyMessageBox.clearCommuNityMessage();
				} else if (pushMessageType == MJTPushMessage.TYPE_CONF) { // 会议消息
					if (box.getConferenceCount() == 1) { // 单个会议
						MJTPushMessage pushMsg = null;
						for (MJTPushMessage msg : box.getListMessage()) {
							if (msg.getType() == MJTPushMessage.TYPE_CONF) {
								pushMsg = msg;
								break;
							}
						}
						if (pushMsg != null) { // 跳转到主会场页面（有无主持人）
							if (pushMsg.isHasTopic()) {
								// 有议题跳转到主会场
								ENavigate.startMeetingMasterActivityForResult(mContext, Long.parseLong(pushMsg.getMucID()), -1);
							} else {
								// 无议题直接跳转到会议详情
								ENavigate.startMeetingDetailsSquareActivity(mContext, Long.parseLong(pushMsg.getMucID()));
							}
						}
					} else if (box.getConferenceCount() > 1) { // 多个会议
						// 停留在此页面
					}
					MNotifyMessageBox.clearConferenceMessage();
				} else if (pushMessageType == MJTPushMessage.TYPE_CONF_NOTI) { // 会议通知
					MNotifyMessageBox.clearConferenceNotification();
				} else if (pushMessageType == MJTPushMessage.TYPE_APPLY_FRIEND) { // 好友申请
					// 跳转到申请页面
					ENavigate.startNewConnectionActivity(this);
				} else if (pushMessageType == MJTPushMessage.TYPE_AGREE_FRIEND) { // 好友通过
					// 跳转到通讯录页面
					ENavigate.startMyFriendAll(this, ConnectionsCacheData.FILTER_FRIEND_ALL);
				} else if (pushMessageType == MJTPushMessage.TYPE_AFFAIR) {
					// 跳转到事物详情
					// bottom_aff_red_dot.setVisibility(View.VISIBLE);
					MJTPushMessage pm = null;
					for (MJTPushMessage msg : box.getListMessage()) {
						if (msg.getType() == MJTPushMessage.TYPE_AFFAIR) {
							pm = msg;
							break;
						}
					}
					ENavigate.startAffarDeatilActivity(this, pm.getAffairId());
				}
				mPager.setCurrentItem(1);
				setPageIndex(1);
				// MNotifyMessageBox.clear(); // 清除消息盒子
			} else {
				KeelLog.d(TAG, "getParam box == null");
			}
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
	}

	public void getParamEx(Intent intent) {
		shareInfo = (JTFile) intent.getSerializableExtra(ENavConsts.EShareParam);
		if (shareInfo != null) {
			ENavigate.startSocialShareActivity(MainActivity.this, shareInfo); // 启动分享页面
		}
	}

	public void pushMessage(String userId, MSociality mSociality) {
		mSocialityFragment.onPushMessage(userId, mSociality);
	}

	// actionbar 中菜单点击事件
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home_new_menu_search:// 全局搜索
/*<<<<<<< HEAD
			ENavigate.startSearchActivity(this, 1);
//			ENavigate.startNewSearchActivity(this, 1);
=======*/
//			ENavigate.startSearchActivity(this, 1);
			ENavigate.startNewSearchActivity(this);
			break;
		case R.id.home_new_menu_more:// 功能列表
			titlePopup.show(headerVi);
		/*
		 * case R.id.home_scan: // 扫一扫 startActivityForResult(new Intent(this,
		 * CaptureActivity.class), 1000); break; case R.id.invite_friends: //
		 * 邀请朋友 ENavigate.startMeetingInviteFriendsActivity(MainActivity.this);
		 * break; case R.id.onekeyback: // 行业偏好 if (mIndustrys == null) {
		 * mIndustrys = new MIndustrys(); }
		 * mIndustrys.setListIndustry(App.getUser().getListIndustry());
		 * ENavigate.startChooseProfessionActivityForResult(this,
		 * ENavConsts.ActivityReqCode.REQUEST_CODE_SETTING_INDUSTRY_ACTIVITY, 1,
		 * mIndustrys); break; case R.id.test: // 测试到社交右上角的通讯录
		 * ENavigate.startMyFriendAll(MainActivity.this,
		 * ConnectionsCacheData.FILTER_PEOPLE_ALL); break; case
		 * R.id.test2recommend: // 测试到 注册推荐人脉页
		 * ENavigate.startWantPeopleActivity(MainActivity.this); break;
		 */
		case android.R.id.home:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// FrgMyHomePage
		try {
			if (requestCode > 30000 && requestCode < 30100 && resultCode == RESULT_OK) {
				mMyHomePageFragment.onActivityResult(requestCode, resultCode, data);
			}
			if (requestCode == 1000) {
				if (resultCode == Activity.RESULT_OK) {
					meetingId = data.getStringExtra("result");
					
					if (null == meetingId) {
						Toast.makeText(this, "无效的二维码", Toast.LENGTH_SHORT).show();
						return;
					} else if (meetingId.isEmpty()) {
						Toast.makeText(this, "无效的二维码", Toast.LENGTH_SHORT).show();
						return;
					}
					// TODO 这里用二维码访问网页 好有人脉
					else if (meetingId.contains("/invitation/")) {
						String substr = meetingId.substring(0, meetingId.length() - 1);
						substr = substr.substring(substr.lastIndexOf("/") + 1, substr.length());
						ENavigate.startInviteFriendByQRCodeActivity(MainActivity.this, substr, InviteFriendByQRCodeActivity.PeopleFriend);
						return;
					}
					// 组织
					else if (meetingId.contains("customerId")) {
						String substr = meetingId.substring(meetingId.lastIndexOf("=") + 1, meetingId.length());
						if (StringUtils.isEmpty(substr) || StringUtils.isEmpty(App.getUserID())) {
							Toast.makeText(MainActivity.this, "无效的二维码", 0).show();
							return;
						}
						ENavigate.startInviteFriendByQRCodeActivity(MainActivity.this, substr, InviteFriendByQRCodeActivity.OrgFriend);
						return;
					//社群
					}else if (meetingId.contains("communityId")) {
						String communityId = meetingId.substring(meetingId.indexOf("=")+1, meetingId.indexOf("&"));
						if (StringUtils.isEmpty(communityId) || StringUtils.isEmpty(App.getUserID())) {
							Toast.makeText(MainActivity.this, "无效的二维码", 0).show();
							return;
						}
						ENavigate.startCommunityDetailsActivity(MainActivity.this,Long.parseLong(communityId), false);
						return;

					}
					else if (meetingId.contains("http")){
						ENavigate.startNeturlKnowledgeActivity(MainActivity.this, meetingId);
						return;
					}
					try {
						// 启动
						long a = 0;
						a = Long.valueOf(meetingId).longValue();
						if (0 == a) {
							Uri uri = Uri.parse(meetingId);
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(intent);
							return;
						}
						Intent intent = new Intent(MainActivity.this, SquareActivity.class);
						intent.putExtra("meeting_id", Long.valueOf(meetingId));
						startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (resultCode == 1001) {
				this.getIntent().getScheme();// 获得Scheme名称
				this.getIntent().getDataString();// 获得Uri全部路径
			}

			if (requestCode == ENavConsts.ActivityReqCode.REQUEST_CODE_LOOK_MORE_COMMENT) {
				Bundle bundle = data.getExtras();
				List<DynamicComment> mListComment = (List<DynamicComment>) bundle.getSerializable(ENavConsts.KEY_FRG_CHANGE_COMMENTS);
				ArrayList<DynamicPraise> mDynamicPraises = (ArrayList<DynamicPraise>) bundle.getSerializable(ENavConsts.KEY_FRG_FLOW_DYNAMIC_PRAISES);
				int index = bundle.getInt(ENavConsts.KEY_FRG_FLOW_INDEX, 0);
				frgTogether.changeDynamicListUI(mDynamicPraises, mListComment, index);
			}
			if (ENavConsts.ActivityReqCode.REQUEST_CODE_SETTING_INDUSTRY_ACTIVITY == requestCode && data != null) {
				mIndustrys = (MIndustrys) data.getExtras().getSerializable(EConsts.Key.INDUSTRYS);
				App.getApp().getAppData().setmIndustrys(mIndustrys);
				App.getApp().getAppData().getUser().setListIndustry(mIndustrys.getListIndustry());
			}
			if(requestCode == mSocialityFragment.REQ_CHAT || requestCode == mSocialityFragment.REQ_MUC){
				mSocialityFragment.onActivityResult(requestCode, resultCode, data);
			}
			if(requestCode == TongRenFragment.REQ_ORG||requestCode == TongRenFragment.REQ_PROJECT){
				frgTongRen.onActivityResult(requestCode, resultCode, data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_more).setIcon(R.drawable.ic_actionbar_more);
		return true;
	}

	public Intent getShareIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TEXT, "这里是要分享的文字");
		intent.setType("text/plain");
		Intent.createChooser(intent, "Share");
		return intent;
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		KeelLog.d(TAG, "mPage mPage ==" + mPager);
		if (isExit) {
			System.exit(0);
			unbind();
			stopService(iMIntent);
		}
		dbManager.closeDbHelper();
		// 或者下面这种方式
		// android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void onBackPressed() {
		// 回到主界面
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			long secondTime = System.currentTimeMillis();
			if (secondTime - mFirstTime > 2000) {
				showToast(R.string.home_exit);
				mFirstTime = secondTime;
				return true;
			} else {
				onBackPressed();
			}
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	/** 初始化头标 */
	private void InitTextView() {
	}

	/** 初始化ViewPager */
	private void InitViewPager() {
		mPager = (CustomViewPager) findViewById(R.id.homeVPager);
		mPager.setPagingEnabled(false);// 禁止viewpager滑动
		mPager.setOffscreenPageLimit(2); // 状态表示
		homeFrgPagerAdapter = new HomeFrgPagerAdapter(getSupportFragmentManager(), mFragments);
		mPager.setAdapter(homeFrgPagerAdapter);
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		setPageIndex(0);
	}

	/** 页卡切换监听 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int index) {
			switch (index) {
			case 0:
				itemMenu.close(true);
				setPageIndex(0);
				HomeCommonUtils.initLeftMainActionBar(MainActivity.this, actionbar, "首页", false, null, R.color.registration_total_color);
//				HomeCommonUtils.initHorizontalCustomActionBar(MainActivity.this, actionbar, MainActivity.this, currentFlowFrgTitle);
				break;
			case 1:
				itemMenu.close(true);
				setPageIndex(1);
				HomeCommonUtils.initLeftMainActionBar(MainActivity.this,actionbar , "消息", false, null, R.color.registration_total_color);
				if (mSocialityFragment != null && (mSocialityFragment.getListSocial() == null || mSocialityFragment.getListSocial().size() <= 50)) {
					mSocialityFragment.startGetData();
				}
				break;
			case 2:
				itemMenu.close(true);
				setPageIndex(2);
//				HomeCommonUtils.initLeftMainActionBar(MainActivity.this, getActionBar(), "桐人", false, null, R.color.actionbar_title_textcolor);
				HomeCommonUtils.initHorizontalCustomActionBar(MainActivity.this, actionbar,frgTongRen.onClickListener, frgTongRen.getCurrentTongRenFrgTitle());

				break;
			case 3:
				itemMenu.close(true);
				setPageIndex(3);
				HomeCommonUtils.initLeftMainActionBar(MainActivity.this,actionbar, "我的", false, null, R.color.registration_total_color);

				break;
//			case 4:
//				itemMenu.close(true);
//				setPageIndex(4);
//				HomeCommonUtils.initLeftMainActionBar(MainActivity.this, getActionBar(), "我", false, null, R.color.actionbar_title_textcolor);
//
//				break;
			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private void setPageIndex(int index) {
		int selectColor =getResources().getColor(R.color.home_index_text_on_bg);
		initButtomButtonBg();
		switch (index) {
		case 0:// 动态设置为选中状态
			mInletDynamicTv.setTextColor(selectColor);
			mInletDynamicIv.setBackgroundResource(R.drawable.home_selected);
			break;
		case 1:
			mInletGamTv.setTextColor(selectColor);
			mInletGamIv.setBackgroundResource(R.drawable.social_selected);
			break;
		case 2:
			mInletAffTv.setTextColor(selectColor);
			mInletAffIv.setBackgroundResource(R.drawable.tongren_selected);
			break;
//		case 3:
//			mInletFindTv.setTextColor(selectColor);
//			mInletFindIv.setBackgroundResource(R.drawable.discover_selected);
//			break;
		case 3:
			mInletMeTv.setTextColor(selectColor);
			mInletMeIv.setBackgroundResource(R.drawable.me__selected);
			break;
		}
	}

	/** 头标点击监听 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	public void hideKeyBoard() {
		try {
			frgTogether.hideKeyboard();
		} catch (Exception e) {

		}
	}

	IBindData iBindData = new IBindData() {
		@Override
		public void bindData(int tag, Object object) {
			if (tag == EAPIConsts.concReqType.im_getNewDynamicCount) {
				doNewflow(object);
			} else if (tag == EAPIConsts.IMReqType.IM_REQ_GET_LISTIM) {
				if (object != null) {
					MGetListIMRecord getListIMRecord = (MGetListIMRecord) object;
					JTPage mPage = getListIMRecord.getJtPage();
					IMRecord iMRecord = null;
					if (mPage != null) {
						if ((mPage.getLists() != null) && (mPage.getLists().size() > 0)) {
							ArrayList<IPageBaseItem> itemsIPageBaseItem = mPage.getLists();
							boolean isHavs = false;
							for (IPageBaseItem iPageBaseItem : itemsIPageBaseItem) {
								iMRecord = (IMRecord) iPageBaseItem;
								if (iMRecord != null) {
									if (iMRecord.getNewCount() != 0) {
										isHavs = true;
									}
								}
							}
						}
					}
				}
			}
		}
	};
	
	private void showNoticeDialog() {
		final NoticeDialog dialog = new NoticeDialog(this);
		dialog.setTitle("社群公告");
		dialog.setMessage(notice);
		dialog.setPositiveButton("确定", new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, JoinCommumitiesAvtivity.class);
				intent.putExtra("req_number_community", community);
				startActivity(intent);
				dialog.dismiss();
			}
		});
	}

	public void doNewflow(Object object) {
	}

	public class HomeFrgPagerAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

		public HomeFrgPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public HomeFrgPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragment) {
			super(fm);
			mFragments = fragment;
		}

		public ArrayList<Fragment> getmFragments() {
			return mFragments;
		}

		public void setmFragments(ArrayList<Fragment> mFragments) {
			this.mFragments = mFragments;
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment f = mFragments.get(arg0);
			if (f == null) {
				EUtil.showToast("getItem = null");
			}
			return mFragments.get(arg0);
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}
		
	}

	public int newGroupCount = 0;
	public int newConnectionsCount = 0;

	boolean isFirstAtMain = true;// 第一次显示不加载new tag，
	Handler newMsgTagHandler = new Handler();
	Runnable newMsgTagRunnable = new Runnable() {
		@Override
		public void run() {
			JSONObject jb = new JSONObject();
			if (mPager == null) {
				newMsgTagHandler.postDelayed(newMsgTagRunnable, 120000);
				finish();// mpager不应该为空，如果为空，则应该
				return;
			}
			if (mPager.getCurrentItem() == 0) {
				// IMReqUtil.getNewGroupCount(MainActivity.this, iBindData, jb,
				// null);
				// ConnectionsReqUtil.getNewConnectionsCount(MainActivity.this,iBindData,
				// jb, null);
				// IMReqUtil.getListIMRecord(MainActivity.this, iBindData,
				// null,0,20);
			} else if (mPager.getCurrentItem() == 1) {
				// IMReqUtil.getNewGroupCount(MainActivity.this, iBindData,
				// jb,null);
				// ConnectionsReqUtil.getNewConnectionsCount(MainActivity.this,iBindData,
				// jb, null);
				ConnectionsReqUtil.getNewDynamicCount(MainActivity.this, iBindData, jb, null);
				// IMReqUtil.getListIMRecord(MainActivity.this, iBindData, null,
				// 0, 20);
			} else if (mPager.getCurrentItem() == 2) {
				// IMReqUtil.getNewGroupCount(MainActivity.this, iBindData, jb,
				// null);
				// ConnectionsReqUtil.getNewConnectionsCount(MainActivity.this,iBindData,
				// jb, null);
				// ConnectionsReqUtil.getNewDynamicCount(MainActivity.this,
				// iBindData, jb, null);
				// IMReqUtil.getListIMRecord(MainActivity.this, iBindData, null,
				// 0,20);
			}
			newMsgTagHandler.postDelayed(newMsgTagRunnable, 120000);
		}
	};

	// 接受im广播
	@Override
	public void onResume() {
		super.onResume();
		newMsgTagHandler.postDelayed(newMsgTagRunnable, 1000);
		// 调用我的页面的刷新 保证实时刷新
		if (mMyHomePageFragment.isHasPause()) {
			mMyHomePageFragment.setUserVisibleHint(true);
		}
		if (mSocialityFragment.isHasPause()) {
			mSocialityFragment.setUserVisibleHint(true);
		}
		mSocialityFragment.updateNewContactsTip();
		sp = getApplication().getSharedPreferences(EConsts.share_invisible_create_button, Context.MODE_PRIVATE);
		isShowCreateButton = sp.getBoolean(EConsts.share_invisible_create_button, true);
		showCreateButtonOrNot();
	}

	private void showCreateButtonOrNot() {
		mGlobalCreatButton.clearAnimation();
		smallerbutton.clearAnimation();
		biggerButton.clearAnimation();
		if (isShowCreateButton) {
//			mGlobalCreatButton.setVisibility(View.VISIBLE);
			smallerbutton.setVisibility(View.VISIBLE);
			biggerButton.setVisibility(View.GONE);
		} else if (!isShowCreateButton) {
			mGlobalCreatButton.setVisibility(View.GONE);
			smallerbutton.setVisibility(View.GONE);
			biggerButton.setVisibility(View.GONE);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		newMsgTagHandler.removeCallbacks(newMsgTagRunnable);
		if (itemMenu.isOpen()) {
			itemMenu.close(true);
		}
	}

	public void setIMSizeViewTag(int visible) {
	}

	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_INVITATIONBYFACETOFACE && object != null) {
			SimpleResult result = (SimpleResult) object;
			if (result.isSucceed()) {
			} else {
				Toast.makeText(MainActivity.this, "邀请失败(可能已经接受邀请)", 1).show();
			}
		}
		if (tag == EAPIConsts.CommonReqType.getUserQRUrl) {
			MUserQRUrl qrcode = (MUserQRUrl) object;
			if (qrcode != null && !TextUtils.isEmpty(qrcode.getUrl())) {
				String qRCodeStr = qrcode.getUrl();
				App.getApp().myQrString = qRCodeStr;
			}
		}
		if(tag == CommunityReqType.TYPE_EXIST_COMMUNITYNO){
			HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
			if (dataMap != null) {
				boolean isExist = (Boolean) dataMap.get("isExist");
				if(isExist){
					CommunityReqUtil.getCommunityByCommunityNo(MainActivity.this, MainActivity.this, communityNO, Long.valueOf(App.getUserID()), null);
				}else{
					dismissLoadingDialog();
					showToast("群号错误");
				}
			}else{
				dismissLoadingDialog();
				showToast("根据社群号获取社群基本信息失败！群号是："+communityNO);
			}	
		}else if(tag == CommunityReqType.TYPE_GET_COMMUNITY_BY_COMMUNITYNO){
			HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
			if (dataMap != null) {
				CommunityApply commuapply = (CommunityApply) dataMap.get("result");
				Notification noti = (Notification) dataMap.get("notification");
				if(commuapply == null && noti != null){
					dismissLoadingDialog();
					showToast(noti.getNotifInfo());
				}else if(commuapply != null){
					communityId = commuapply.getCommunity().getId();
					community = commuapply.getCommunity();
					if(commuapply.getApplayType().equals(CommunityApply.APPLYTYPE_ALL)){
						List<Long> list = new ArrayList<Long>();
						list.add(Long.parseLong(App.getApp().getUserID()));
						CommunityReqUtil.doInvite2Muc(MainActivity.this, communityId, list, this, null);
					}else if(commuapply.getApplayType().equals(CommunityApply.APPLYTYPE_REQ)){
						CommunityReqUtil.getNotice(MainActivity.this, MainActivity.this, communityId, null);
					}
				}
			}
		}else if(tag == CommunityReqType.TYPE_INVITE2MUC){
			if (null != object) {
				ENavigate.startCommunityDetailsActivity(MainActivity.this, communityId, true);
				// dismissLoadingDialog();
			} else {
//				showToast("加入不成功");
				dismissLoadingDialog();
			}
		}else if(tag == CommunityReqType.TYPE_GET_NOTICE){
			dismissLoadingDialog();
			HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
			if (null != dataMap) {
				notice = (String) dataMap.get("notice");
				showNoticeDialog();
			} else {
				Intent intent = new Intent(MainActivity.this, JoinCommumitiesAvtivity.class);
				intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
				intent.putExtra("req_number_community", community);
				startActivity(intent);
			}
		}
	}

	

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_inlet_dynamic_fl: // 动态
			initButtomButtonBg();
			mPager.setCurrentItem(0);
			mInletDynamicTv.setTextColor(getResources().getColor(R.color.home_index_text_on_bg));
			mInletDynamicIv.setBackgroundResource(R.drawable.home_selected);
			mInletDynamicRemind.setVisibility(View.GONE);
			break;
		case R.id.activity_inlet_gam_fl: // 社交
			initButtomButtonBg();
			mPager.setCurrentItem(1);
			mInletGamTv.setTextColor(getResources().getColor(R.color.home_index_text_on_bg));
			mInletGamIv.setBackgroundResource(R.drawable.social_selected);
			break;
		case R.id.activity_inlet_aff_fl: // 事务
			showTongRenFrg();
			break;
//		case R.id.activity_inlet_find_fl: // 发现
//			initButtomButtonBg();
//			mPager.setCurrentItem(3);
//			mInletFindTv.setTextColor(getResources().getColor(R.color.home_index_text_on_bg));
//			mInletFindIv.setBackgroundResource(R.drawable.discover_selected);
//			HomeCommonUtils.readFile(MainActivity.this);
//			HomeCommonUtils.readFileZonghe(MainActivity.this);
//			break;
		case R.id.activity_inlet_create_fl:
			// TODO 跳转到创建页面
			firstuse_sp = getSharedPreferences(GlobalVariable.SHARED_PREFERENCES_FIRST_USE,MODE_PRIVATE);
			firstuse_edtior = firstuse_sp.edit();
			if(firstuse_sp.getBoolean(GlobalVariable.MAIN_CREATE_FIRST_USE, true)){
				GuidePopupWindow guidePop = new GuidePopupWindow(MainActivity.this);
				guidePop.setImage(R.drawable.main_guide_06);
				guidePop.show();
				firstuse_edtior.putBoolean(GlobalVariable.MAIN_CREATE_FIRST_USE, false);
				firstuse_edtior.commit();
			}else{
				GodCreatedPopupWindow popupWindow=new GodCreatedPopupWindow(mContext);
				popupWindow.show();
			}
			break;
		case R.id.activity_inlet_me_fl: // 我的
			initButtomButtonBg();
			mPager.setCurrentItem(4);
			mInletMeTv.setTextColor(getResources().getColor(R.color.home_index_text_on_bg));
			mInletMeIv.setBackgroundResource(R.drawable.me_selected);
			HomeCommonUtils.readFile(MainActivity.this);
			HomeCommonUtils.readFileZonghe(MainActivity.this);
			break;

		case 1:// 畅聊
			itemMenu.close(true);
			ENavigate.startIMRelationSelectActivity((Activity) mContext, null, null, 0, null, null);
			break;
		case 2:// 会议
			itemMenu.close(true);
			ENavigate.startInitiatorHYActivity(mContext);
			break;
		case 3:// 事务
			itemMenu.close(true);
			ENavigate.startNewAffarActivity((Activity) mContext);
			break;
		case 4:// 知识
			itemMenu.close(true);
			ENavigate.startCreateKnowledgeActivity(mContext);
			break;
		}
	}
	
	/**
	 * 切换到桐人模块
	 */
	public void showTongRenFrg() {
		initButtomButtonBg();
		mPager.setCurrentItem(2);
		mInletAffTv.setTextColor(getResources().getColor(R.color.home_index_text_on_bg));
		mInletAffIv.setBackgroundResource(R.drawable.tongren_selected);
	}

	/** 初始化底部按钮背景 */
	private void initButtomButtonBg() {
		int blackColor = getResources().getColor(R.color.home_index_text_default);
		mInletDynamicTv.setTextColor(blackColor);
		mInletGamTv.setTextColor(blackColor);
		mInletAffTv.setTextColor(blackColor);
		mInletMeTv.setTextColor(blackColor);
		mInletDynamicIv.setBackgroundResource(R.drawable.home_bg);
		mInletGamIv.setBackgroundResource(R.drawable.social_bg);
		mInletAffIv.setBackgroundResource(R.drawable.tongren_bg);
		mInletMeIv.setBackgroundResource(R.drawable.me_bg);
	}

	@Override
	public void onFragmentAttached(ScrollableListener fragment, int position) {
	}

	@Override
	public void onFragmentDetached(ScrollableListener fragment, int position) {

	}

	/**
	 * 从服务器返回的二维码的字符串提取出Id
	 * 
	 * @param gintong
	 * @return
	 */
	public String getIdFromQRCode(String gintong) {
		String subStr = gintong.substring(0, gintong.length() - 1);
		subStr = subStr.substring(0, subStr.lastIndexOf("/"));
		subStr = subStr.substring(subStr.lastIndexOf("/") + 1);
		return subStr;
	}

	/**
	 * 从服务器返回的二维码的字符串提取出type
	 * @param gintong
	 * @return
	 */
	public int getTypeFromQRCode(String gintong) {
		if (gintong.contains("organ")) {
			return 0;
		}
		return 1;
	}

	private void bind() {
		iMIntent = new Intent(MainActivity.this, ReceiveMsgService.class);
		bindService(iMIntent, serviceConnection, Context.BIND_AUTO_CREATE);
		startService(iMIntent);
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			receiveMsgService = ((ReceiveMsgService.MyBinder) service).getService();
			receiveMsgService.setOnGetConnectState(new GetConnectState() { // 添加接口实例获取连接状态
						@Override
						public void GetState(boolean isConnected) {
							if (conncetState != isConnected) { // 如果当前连接状态与广播服务返回的状态不同才进行通知显示
								conncetState = isConnected;
								if (conncetState) {// 已连接
									handler.sendEmptyMessage(1);
								} else {// 未连接
									handler.sendEmptyMessage(2);
								}
							}
						}
					});
		}
	};

	private void unbind() {
		if (receiveMsgService != null) {
			unbindService(serviceConnection);
			Log.i("mylog", "执行unbind()");
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:// 已连接
					// 登陆环信IM服务器 如果登陆成功 再请求自己的服务器
				EMChatManager.getInstance().login(App.getApp().getAppData().getUserID(), App.getApp().getAppData().getUserID(), new EMCallBack() {// 回调
							@Override
							public void onSuccess() {
								// 注册一个监听连接状态的listener
								EMChatManager.getInstance().addConnectionListener(new com.tr.imservice.MyConnectionListener(MainActivity.this));
								runOnUiThread(new Runnable() {
									public void run() {
										EMGroupManager.getInstance().loadAllGroups();
										EMChatManager.getInstance().loadAllConversations();
									}
								});
							}

							@Override
							public void onProgress(int progress, String status) {
							}

							@Override
							public void onError(int code, String message) {
								Log.d("main", "登陆聊天服务器失败！");
								handler.sendEmptyMessage(0);
							}
						});
				break;
			case 2:// 未连接
				break;
			case 3:// 计时5秒后执行
				awesomeMenuAlphaCounter--;
				if (awesomeMenuAlphaCounter == 0 && mGlobalCreatButton != null && smallerbutton != null && biggerButton != null && !itemMenu.isOpen()) {
					smallerbutton.getBackground().setAlpha(160);
					biggerButton.getBackground().setAlpha(160);
				}
				break;
			default:
				break;
			}
			;
		};

	};

	public void updateNavigateNum(int pushNum) {
		if (pushNum <= 0) {
			inlet_gam_remind_fl.setVisibility(View.GONE);
		} else {
			inlet_gam_remind_fl.setVisibility(View.VISIBLE);
			navigateNumTv.setText((pushNum > 99 ? 99 : pushNum) + "");
		}
	}

	public void updateNavigateNum(List<MSociality> listSocial, MSociality social) {
		int pushNum = 0;

		// 增加判空操作
		if (listSocial != null) {
			for (MSociality sociality : listSocial) {
				if (sociality != null) {
					pushNum += sociality.getNewCount();
				}
			}
		}
		if(social!=null){
			pushNum += social.getNewCount();
		}
		updateNavigateNum(pushNum);
	}

//	public void onFlaying(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//		// velocityX表示横向的移动，根据手指移动的方向切换
//		if (mPager.getCurrentItem() == 0) {
//			if (velocityX < 0 && !CurrentFlowFrgTitle.third.equals(currentFlowFrgTitle)) {
//				if (CurrentFlowFrgTitle.first.equals(currentFlowFrgTitle)) {// 切换到动态
//					HomeCommonUtils.startTranslateAnimation(CurrentFlowFrgTitle.second);
//					changeFlowType(HomeCommonUtils.flowpageTv, CurrentFlowFrgTitle.second,  FlowSelectType.flow);
//				} else if (CurrentFlowFrgTitle.second.equals(currentFlowFrgTitle)) {// 切换到推荐
//					HomeCommonUtils.startTranslateAnimation(CurrentFlowFrgTitle.third);
//					changeFlowType(HomeCommonUtils.gintongpageTv, CurrentFlowFrgTitle.third, FlowSelectType.gintong);
//				}
//			} else if (velocityX > 0 && !CurrentFlowFrgTitle.first.equals(currentFlowFrgTitle)) {
//				if (CurrentFlowFrgTitle.second.equals(currentFlowFrgTitle)) {// 切换到首页
//					HomeCommonUtils.startTranslateAnimation(CurrentFlowFrgTitle.first);
//					changeFlowType(HomeCommonUtils.mainpageTv, CurrentFlowFrgTitle.first, FlowSelectType.all);
//				} else if (CurrentFlowFrgTitle.third.equals(currentFlowFrgTitle)) {// 切换到动态
//					HomeCommonUtils.startTranslateAnimation(CurrentFlowFrgTitle.second);
//					changeFlowType(HomeCommonUtils.flowpageTv, CurrentFlowFrgTitle.second,FlowSelectType.flow);
//				}
//			}
//		}
//	}

	
}
