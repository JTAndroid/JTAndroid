package com.tr.ui.demand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.model.demand.CustomData;
import com.tr.model.demand.DemandASSO;
import com.tr.model.demand.DemandDetailsData;
import com.tr.model.demand.LableData;
import com.tr.model.joint.AffairNode;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.common.JointResourceMainFragment;
import com.tr.ui.demand.NewDemandActivity.DemandEnum;
import com.tr.ui.demand.MyView.MyViewPager;
import com.tr.ui.demand.fragment.MyFragmentPagerAdapter;
import com.tr.ui.demand.fragment.NeedCommentFragment;
import com.tr.ui.demand.fragment.NeedDetailsFragment;
import com.tr.ui.demand.popu.MyPopupWindow;
import com.tr.ui.demand.util.DemandAction;
import com.tr.ui.demand.util.OnNeedDetails;
import com.tr.ui.demand.util.OnNeedRefresh;
import com.tr.ui.demand.util.TextStrUtil;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.knowledge.swipeback.SwipeBackActivity;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * @ClassName: NeedDetails.java
 * @author ZCS
 * @Date 2015年3月10日 下午2:10:14
 * @Description: 需求页面
 */
public class NeedDetailsActivity extends SwipeBackActivity implements
		IBindData, OnNeedDetails {
	private static final int REQUEST_CODE_DEMAND_DEIT_ACTION = 4001; // 编辑需求回调参数
	private TextView view1;
	private TextView view2;
	private View barText;
	private MyViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private int currIndex;// 当前页卡编号
	private ActionBar actionBar;
	private MyPopupWindow popupW;
	private View headerVi;
	private TextView needDetailsTv;
	private TextView commentTv;
	private MyOnClick myOnClick;
	AutherType autherType = AutherType.small;// 权限控制器
	private String demandId;
	private int dynamicType;// 类型 来自动态的类型
	private int from;// 来源
	private DemandDetailsData detailsData;// 当前需求详情对象
	private View root;
	private MenuItem menuAdd;// 全部按钮对象
	private MenuItem menuForwarding;// 转发按钮
	private Dialog dialog;
	private List<OnNeedRefresh> onRefreshList = new ArrayList<OnNeedRefresh>();// 刷新接口数据
	private ArrayList<LableData> lablelist;
	private ArrayList<UserCategory> categorys;
	private Fragment fragment;
	/**
	 * 转发 分享 保存 对接 编辑 收藏 删除 评论 举报 查看发布人
	 */
	private final static int[] type = { 2015031001, 2015031002, 2015031003,
			2015031004, 2015031005, 2015031006, 2015031007, 2015031008,
			201503109, 2015031011 ,2015031012};

	private enum AutherType {
		self, // 自己
		big, // 大乐
		inthe, // 中乐
		small// 小乐
	}

	InputMethodManager imm;
	private String peopleOrorg;
	private MenuItem demand_menu_comment;
	private ActionBar mActionBar;
	private TextView myTitle;
	private ImageView demandHomeBackIv;
	private ImageView demandCommentRedPointIv;
	private ImageView demandCommentIv;
	private ImageView demandShareIv;
	private ImageView demandMoreIv;
	private TextView demandtypeTv;
	private MyTopBarOnClick myTopBarOnClick;
	private TextView demandCommentTv;
	private NeedDetailsFragment needFragment;
	private JointResourceMainFragment needJointResource;
	private ImageView demand_titlebar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		setContentView(R.layout.activity_demand_need_details);
		myOnClick = new MyOnClick();
		myTopBarOnClick = new MyTopBarOnClick();
		headerVi = findViewById(R.id.headerVi);
		needDetailsTv = (TextView) findViewById(R.id.needDetailsTv);// 需求详情
		commentTv = (TextView) findViewById(R.id.commentTv);// 评论
		root = findViewById(android.R.id.content);
		
		demandHomeBackIv = (ImageView) findViewById(R.id.demandHomeBackIv);
		demandCommentRedPointIv = (ImageView) findViewById(R.id.demandCommentRedPointIv);
		demandCommentIv = (ImageView) findViewById(R.id.demandCommentIv);
		demandShareIv = (ImageView) findViewById(R.id.demandShareIv);
		demandMoreIv = (ImageView) findViewById(R.id.demandMoreIv);
		demandtypeTv = (TextView) findViewById(R.id.demandtypeTv);
		demandCommentTv = (TextView) findViewById(R.id.demandCommentTv);
		demand_titlebar = (ImageView) findViewById(R.id.demand_titlebar);
		commentTv.setText("评论");
		demandHomeBackIv.setOnClickListener(myTopBarOnClick);
		demandShareIv.setOnClickListener(myTopBarOnClick);
		demandCommentIv.setOnClickListener(myTopBarOnClick);
		demandMoreIv.setOnClickListener(myTopBarOnClick);
		demand_titlebar .setOnClickListener(myTopBarOnClick);
		getParam();
		InitTextView();
		InitTextBar();
		InitViewPager();
		// View v = findViewById(R.id.btnV);//底层框
		// v.setVisibility(View.VISIBLE);//显示底层框

	}
	public class MyTopBarOnClick implements OnClickListener{

		private boolean isComment = false;

		@Override
		public void onClick(View v) {
			if (v.getId() == demandHomeBackIv.getId()) {
				if (currIndex==1) {
					mPager.setCurrentItem(0);
				}else{
					finish();
				}
				
			}else if(v.getId() == demandShareIv.getId()){
				if (detailsData != null) {
					JTFile jtfile = new JTFile();
//					jtfile.mFileName =App.getNick()+"分享了[需求] ";//+ detailsData.title.value;
					jtfile.fileName =detailsData.title.value;
					jtfile.mType = JTFile.TYPE_DEMAND;
					jtfile.setmUrl(detailsData.firstPicPath);
					jtfile.mTaskId = detailsData.id;
					jtfile.reserved1 = detailsData.createTime;
					jtfile.reserved2 = detailsData.demandType + "";
					jtfile.mSuffixName = detailsData.note.value;
					jtfile.reserved3 = detailsData.createrId;//创建者id
					FrameWorkUtils.showSharePopupWindow2(NeedDetailsActivity.this,
							jtfile);
				}
			}else if(v.getId() == demandCommentIv.getId()){
//				Intent intent = new Intent(NeedDetailsActivity.this,NeedCommentActivity.class);
//				intent.putExtra(ENavConsts.DEMAND_DETAILS_ID, demandId);
//				intent.putExtra(ENavConsts.DEMAND_EDIT, detailsData);
//				startActivity(intent);
				
					if (needFragment!=null&&needFragment.NeedDetailsSv!=null&&needFragment.NeedDetailsInformationLl!=null) {
						if (!isComment ) {
						needFragment.NeedDetailsSv.scrollTo(0, needFragment.NeedDetailsInformationLl.getHeight());
						needFragment.showBottomBar();
						needFragment.isShowInput = true;
						isComment = true;
					}else{
						needFragment.NeedDetailsSv.fullScroll(ScrollView.FOCUS_UP);
						isComment = false;
					}
				}
				
			}else if(v.getId() == demandMoreIv.getId()){
				if (detailsData != null) {
					showPopup(autherType);
				}
			}else if(v.getId() == demand_titlebar.getId()){
				if (needFragment!=null&&needFragment.NeedDetailsSv!=null) {
					needFragment.NeedDetailsSv.fullScroll(ScrollView.FOCUS_UP);
					isComment = true;
				}
			}
						
		}
		
	}
	private void getParam() {
		Intent intent = getIntent();
		demandId = intent.getStringExtra(ENavConsts.DEMAND_DETAILS_ID);
		from = intent.getIntExtra(ENavConsts.DEMAND_DETAILS_FROM, 1);// 默认
		dynamicType = intent.getIntExtra(ENavConsts.DEMAND_DETAILS_TYPE, 1);// 默认为创建
	}
	@Override
	public void onBackPressed() {
		 if(needJointResource.equals(fragment)) {  
			 mPager.setCurrentItem(0);
		    }  else{
		    	super.onBackPressed();
		    }
		
	}
	/*
	 * 初始化标签名
	 */
	public void InitTextView() {
		view1 = (TextView) findViewById(R.id.needDetailsTv);
		view2 = (TextView) findViewById(R.id.commentTv);
		view1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(0);
				hideKeyboard();
			}
		});
		view2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(2);
				hideKeyboard();
			}
		});
	}

	// /**
	// * 收起键盘
	// */
	public void hideKeyboard() {
		// if (imm != null) {
		// imm.hideSoftInputFromWindow(this.getWindow().getDecorView()
		// .getWindowToken(), 0);
		// //
		// imm.showSoftInputFromInputMethod(me.getWindow().getDecorView().getWindowToken(),0);
		// //
		// imm.hideSoftInputFromWindow(me.getWindow().getDecorView().getWindowToken(),InputMethodManager.HIDE_IMPLICIT_ONLY);
		// }
		if (NeedDetailsActivity.this.getCurrentFocus() != null) {
			if (NeedDetailsActivity.this.getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(NeedDetailsActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			if (NeedDetailsActivity.this.getCurrentFocus() != null) {
				if (NeedDetailsActivity.this.getCurrentFocus().getWindowToken() != null) {
					imm.hideSoftInputFromWindow(NeedDetailsActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		return super.onTouchEvent(event);
	}

	/*
	 * 初始化图片的位移像素
	 */
	public void InitTextBar() {
		barText = findViewById(R.id.cursor);
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		// 得到显示屏宽度
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		// 1/3屏幕宽度
		int tabLineLength = metrics.widthPixels / 2;
		LayoutParams lp = (LayoutParams) barText.getLayoutParams();
		lp.width = tabLineLength;
		barText.setLayoutParams(lp);

	}

	/*
	 * 初始化ViewPager
	 */
	public void InitViewPager() {
		mPager = (MyViewPager) findViewById(R.id.contentVp);
		mPager.isCanScroll = true;
		// mPager.requestDisallowInterceptTouchEvent(false);
		fragmentList = new ArrayList<Fragment>();
		needFragment = new NeedDetailsFragment(
				NeedDetailsActivity.this, mPager, this, from, dynamicType,
				demandId);
		AffairsMini affairsMini = new AffairsMini();
		try {
			affairsMini.id = Integer.parseInt(demandId);
		} catch (Exception e) {
			affairsMini.id=0;
		}
		needJointResource = new JointResourceMainFragment(
				);
		 needJointResource.setJointResourceResourceBase(ResourceType.Affair,affairsMini);
		fragmentList.add(needFragment);
		fragmentList.add(needJointResource);

		// 给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentList));
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
	}

	/**
	 * 页面变化时的监听器
	 * 
	 * @author Administrator
	 *
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// 取得该控件的实例
			LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) barText
					.getLayoutParams();

			if (currIndex == arg0) {
				ll.leftMargin = (int) (currIndex * barText.getWidth() + arg1
						* barText.getWidth());
			} else if (currIndex > arg0) {
				ll.leftMargin = (int) (currIndex * barText.getWidth() - (1 - arg1)
						* barText.getWidth());
			}
			barText.setLayoutParams(ll);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageSelected(int arg0) {
			hideKeyboard();
			currIndex = arg0;
			fragment = fragmentList.get(currIndex);
			int i = currIndex + 1;
			/*
			 * Toast.makeText(NeedDetailsActivity.this, "您选择了第" + i + "个页卡",
			 * Toast.LENGTH_SHORT).show();
			 */
			if (arg0 == 1) {
				demandtypeTv.setText("资源对接");
				demandCommentIv .setVisibility(View.GONE);
				demandMoreIv .setVisibility(View.GONE);
				demandShareIv .setVisibility(View.GONE);
				demandCommentTv .setVisibility(View.GONE);
			}else if(arg0==0){
				demandCommentIv .setVisibility(View.VISIBLE);
				demandShareIv .setVisibility(View.VISIBLE);
				demandMoreIv .setVisibility(View.VISIBLE);
				demandCommentTv .setVisibility(View.VISIBLE);
				demandtypeTv.setText("详情");
			}
		}
	}

	@Override
	protected void onStop() {
		hideKeyboard();
		super.onStop();
	}

	@Override
	public void initJabActionBar() {
		actionBar = getActionBar();
		actionBar.setTitle("详情");
		mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("详情");
		mCustomView.findViewById(R.id.titleIv).setVisibility(View.GONE);
		mActionBar.hide();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.demand_details, menu);
		menuAdd = menu.findItem(R.id.demand_overflow);
		menuForwarding = menu.findItem(R.id.demand_menu_share);
		demand_menu_comment = menu.findItem(R.id.demand_menu_comment);
		
		return true;
	}

	private void menuItem(int id) {
		if (type[5] == id) {// 收藏
			DemandReqUtil.othersCollectDemand(NeedDetailsActivity.this,
					NeedDetailsActivity.this, null, demandId);
		} else if (type[6] == id) {// 删除
			DemandReqUtil.deleteMyDemand(this, this, null, demandId);
		}
	}

	private void showDialog(String message, final int type) {
		View view = View.inflate(this, R.layout.demand_user_setting_dialog1,
				null);
		((TextView) view.findViewById(R.id.infoTv)).setText(message);
		dialog = new Dialog(this, R.style.MyDialog);
		// dialog.setCancelable(false);//是否允许返回
		dialog.addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		dialog.show();
		// 确定
		view.findViewById(R.id.confirmTv).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 确定修改
						// 删除
						menuItem(type);
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
	}

	/**
	 * 
	 * @param message
	 */
	private void showDialog(String message) {
		View upView = View.inflate(this, R.layout.demand_item_dialog, null);
		
		TextView CreateDemandname = (TextView) upView.findViewById(R.id.neetNameTv);
		if (detailsData.createrId.equals("0")) {
			CreateDemandname.setClickable(false);
		}
		CreateDemandname.setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!detailsData.createrId.equals("0")) {
							if (peopleOrorg.equals("2")) {
								// 跳转到用户个人信息
								ENavigate.startRelationHomeActivity(
										NeedDetailsActivity.this,
										detailsData.createrId, true,
										ENavConsts.type_details_other);
								dialog.dismiss();
							} else {
								// 跳转到组织详情界面 : 他人主页/我的主页
								ENavigate.startOrgMyHomePageActivityByUseId(NeedDetailsActivity.this,Long.parseLong(detailsData.createrId));
								
							}
						}
						
					}
				});
		((TextView) upView.findViewById(R.id.neetNameTv)).setText(message);
		dialog = new Dialog(this, R.style.MyDialog);
		dialog.addContentView(upView, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		hideKeyboard();
		switch (item.getItemId()) {
		// 第一个
		case R.id.demand_menu_comment:
			Intent intent = new Intent(this,NeedCommentActivity.class);
			intent.putExtra(ENavConsts.DEMAND_DETAILS_ID, demandId);
			intent.putExtra(ENavConsts.DEMAND_EDIT, detailsData);
			startActivity(intent);
			break;
		case R.id.demand_menu_share:
			if (detailsData != null) {
				JTFile jtfile = new JTFile();
//				jtfile.mFileName =App.getNick()+"分享了[需求] ";//+ detailsData.title.value;
				jtfile.mFileName =detailsData.title.value;
				jtfile.mType = JTFile.TYPE_DEMAND;
				jtfile.setmUrl(detailsData.firstPicPath);
				jtfile.mTaskId = detailsData.id;
				jtfile.reserved1 = detailsData.createTime;
				jtfile.reserved2 = detailsData.demandType + "";
				jtfile.mSuffixName = detailsData.note.value;
				jtfile.reserved3 = detailsData.createrId;//创建者id
				FrameWorkUtils.showSharePopupWindow2(NeedDetailsActivity.this,
						jtfile);
			}
			break;

		// 点击Menu键
		case R.id.demand_overflow:
			if (detailsData != null) {
				showPopup(autherType);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 打开菜单,分自己，大乐，小乐
	 * 
	 * @param self
	 */
	private void showPopup(AutherType self) {
		View v = View.inflate(NeedDetailsActivity.this,
				R.layout.demand_need_details_popup_menu, null);
		popupW = new MyPopupWindow(NeedDetailsActivity.this, v);
		LinearLayout ll = (LinearLayout) v.findViewById(R.id.containerLl);
		popupW.showAsDropDown(headerVi);
		switch (self) {
		case self:
			addPopuItem(ll, "编辑", true, type[4]);
//			addPopuItem(ll, "对接", true, type[3]);
			addPopuItem(ll, "删除", true, type[6]);
			addPopuItem(ll, "转换为事务", true, type[10]);
			break;
		case big:
			addPopuItem(ll, "保存", true, type[2]);
//			addPopuItem(ll, "对接", true, type[3]);
			addPopuItem(ll, "收藏", true, type[5]);
			addPopuItem(ll, "举报", true, type[8]);
			addPopuItem(ll, "转换为事务", true, type[10]);
			addPopuItem(ll, "查看发布人", false, type[9]);
			break;
		case inthe:
//			addPopuItem(ll, "对接", true, type[3]);
			addPopuItem(ll, "收藏", true, type[5]);
			addPopuItem(ll, "举报", true, type[8]);
			addPopuItem(ll, "转换为事务", true, type[10]);
			addPopuItem(ll, "查看发布人", false, type[9]);
			break;
		case small:
			// addPopuItem(ll, "收藏", true, type[5]);
			// addPopuItem(ll, "评论", true, type[7]);
			// addPopuItem(ll, "举报", true, type[8]);
			// addPopuItem(ll, "查看发布人", false, type[9]);
			break;
		}
	}

	/**
	 * 
	 * @param parent
	 *            父View
	 * @param str
	 *            要显示的字符串
	 * @param isShowLine
	 *            是否显下划线
	 * @param click
	 *            点击事件
	 */
	public void addPopuItem(LinearLayout parent, String str,
			boolean isShowLine, int id) {
		View item = View.inflate(NeedDetailsActivity.this,
				R.layout.demand_need_details_popup_item, null);
		TextView textView = (TextView) item.findViewById(R.id.txtTv);
		textView.setText(str);
		textView.setId(id);
		textView.setOnClickListener(myOnClick);
		item.findViewById(R.id.lineV).setVisibility(
				isShowLine ? View.VISIBLE : View.GONE);// 下划线
		parent.addView(item);

	}

	class MyOnClick implements View.OnClickListener {
		// 转发 分享 保存 对接 编辑 收藏 删除 评论 举报 查看发布人
		// type[1];
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (type[2] == id) {// 保存
				// showLoadingDialog();
				// DemandReqUtil.othersSaveDemand(NeedDetailsActivity.this,
				// NeedDetailsActivity.this, null, demandId);
				DemandDetailsData data = new DemandDetailsData();
				data.title = detailsData.title; // 标题
				if (detailsData.note != null && detailsData.note.isVisable) {
					data.note = detailsData.note;// 介绍内容
				}
				if (detailsData.type != null && detailsData.type.isVisable) {
					data.type = detailsData.type;// 类型
					data.typeObj = detailsData.typeObj;
				}
				if (detailsData.industry != null
						&& detailsData.industry.isVisable) {
					data.industry = detailsData.industry;// 行业
					data.industryObj = detailsData.industryObj;
				}
				if (detailsData.area != null && detailsData.area.isVisable) {
					data.area = detailsData.area;// 地区
					data.areaObj = detailsData.areaObj;
				}
				if (detailsData.amount != null && detailsData.amount.isVisable) {
					data.amount = detailsData.amount;// 金额
				}
				//自定义信息
				if (detailsData.customList != null) {
					data.customList = new ArrayList<CustomData>();
					for (CustomData custom : detailsData.customList) {
						if (custom.isVisable)
							data.customList.add(custom);
					}
				}

				ENavigate.startDemandEditActivity(NeedDetailsActivity.this,
						REQUEST_CODE_DEMAND_DEIT_ACTION,
						detailsData.demandType, data, null, null,
						DemandEnum.Add);
			} else if (type[3] == id) {// 对接
				if (detailsData != null) {
					AffairsMini affairsMini = new AffairsMini();
					affairsMini.id = Integer.parseInt(detailsData.id);
					affairsMini.title = detailsData.title.value;
					affairsMini.type = detailsData.demandType;
					affairsMini.name = detailsData.createrName;
					affairsMini.content = detailsData.note.value;
					ENavigate.startJointResourceActivity(
							NeedDetailsActivity.this, ResourceType.Affair,
							affairsMini);
				}
			} else if (type[4] == id) {//编辑
				ENavigate.startDemandEditActivity(NeedDetailsActivity.this,
						REQUEST_CODE_DEMAND_DEIT_ACTION,
						detailsData.demandType, detailsData, lablelist,
						categorys, DemandEnum.Edit);
			} else if (type[5] == id) { // 收藏
				showDialog("是否要收藏需求？", id);
			} else if (type[6] == id) {
				showDialog("是否要删除需求？", id);
			} else if (type[8] == id) { // 举报
				ENavigate.startReportMessageActivity(NeedDetailsActivity.this,
						demandId);
			} else if (type[9] == id) {// "查看发布人"
				if (detailsData != null
						&& !TextUtils.isEmpty(detailsData.createrName)) {
					showDialog(detailsData.createrName);
				} else {
					showDialog("未知");
				}
			}
			else if (type[10] == id) {
				AffairNode inAffairNode = new AffairNode();
				ArrayList<AffairsMini> listAffairMini = new ArrayList<AffairsMini>();
				AffairsMini affairsMini = new AffairsMini();
				affairsMini.id = Integer.parseInt(detailsData.id);
				affairsMini.title = detailsData.title.value;
				affairsMini.type = detailsData.demandType;
				affairsMini.name = detailsData.createrName;
				affairsMini.content = detailsData.note.value;
				listAffairMini.add(affairsMini);
				inAffairNode.setListAffairMini(listAffairMini);
				inAffairNode.setMemo("事务");
				ENavigate.startNewAffarActivityByRelation(NeedDetailsActivity.this, null, null, null, inAffairNode);
			}
			popupW.dismiss();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_DEMAND_DEIT_ACTION) {
				// 编辑需求 成功了
				// 发送广播刷新
				this.sendBroadcast(new Intent(
						DemandAction.DEMAND_DETAILS_ACTION));
				String refreshId = intent
						.getStringExtra(ENavConsts.DEMAND_DETAILS_ID);
				refreshId = refreshId == null ? this.demandId : refreshId;
				for (OnNeedRefresh refresh : onRefreshList) {
					refresh.getRefresh(refreshId);// 刷新详情
				}
			}
		}
	}

	@Override
	public void toNeedDetail(int type, Object obj) {
		if (type == 1) {
			// 详情1:独乐,本人创建 2:大乐 3:中乐 4:小乐
			int from = (Integer) obj;
			switch (from) {
			case -1:// 创建者
				autherType = AutherType.self;
				break;
			case 1:
				menuForwarding.setVisible(false);
				menuAdd.setVisible(false);
				autherType = AutherType.self;
				break;
			case 2:
				autherType = AutherType.big; // 大乐
				break;
			case 3:
				menuForwarding.setVisible(false);
				autherType = AutherType.inthe;// 中乐
				demandShareIv.setVisibility(View.GONE);
				break;
			case 4:
				menuForwarding.setVisible(false);
				autherType = AutherType.small;// 小乐
				demandShareIv.setVisibility(View.GONE);
				menuAdd.setVisible(false);
				break;
			default:
				menuForwarding.setVisible(false);
				menuAdd.setVisible(false);
				autherType = AutherType.small; // 独乐
				demandShareIv.setVisibility(View.GONE);
				break;
			}
		}
		if (type == 2) { // 评论
			if (commentTv != null)
				commentTv.setText(TextStrUtil
						.getCommentNum("评论", (Integer) obj));
			
			if ((Integer) obj>0) {
				demandCommentTv.setText(obj+"");
				demandCommentIv.setImageResource(R.drawable.comment_underthe100_full);
			}else{
				demandCommentIv.setImageResource(R.drawable.comment_overthe100_full);
			}
		}
		if (type == 3) { // 详情对象
			detailsData = (DemandDetailsData) obj;
		}
		if (type == 4) {
			// asso
			detailsData.asso = (DemandASSO) obj;
		}
		if (type == 5) {
			lablelist = (ArrayList<LableData>) obj;
			// 标签
		}
		if (type == 6) {
			// 目录
			categorys = (ArrayList<UserCategory>) obj;
		}
		if (type == 7) {
			peopleOrorg = (String) obj;
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (object == null) {
			return;
		}
		Map<Integer, Object> map = (Map<Integer, Object>) object;
		if (tag == EAPIConsts.demandReqType.demand_deleteMyDemand) {
			// 删除我的需求
			if ((Boolean) map.get(1)) { // 成功
				// 删除成功
				showToast("需求删除成功");
				NeedDetailsActivity.this.sendBroadcast(new Intent(
						DemandAction.DEMAND_DETAILS_ACTION));
				this.finish();// 关闭界面
			} else {
				// 删除失败
				String error = (String) map.get(2);
				if (TextUtils.isEmpty(error)) {
					error = "删除失败";
				}
				showToast(error);// 显示错误信息
			}
		} else if (tag == EAPIConsts.demandReqType.demand_collectOthersDemand) {
			if ((Boolean) map.get(1)) { // 成功
				showToast("收藏成功");
			} else {
				String error = (String) map.get(2);
				if (TextUtils.isEmpty(error)) {
					error = "收藏失败";
				}
				showToast(error);// 显示错误信息
			}
		} else if (tag == EAPIConsts.demandReqType.demand_saveOthersDemand) {
			if ((Boolean) map.get(1)) { // 成功
				showToast("保存成功");
			} else {
				String error = (String) map.get(2);
				if (TextUtils.isEmpty(error)) {
					error = "保存失败";
				}
				showToast(error);// 显示错误信息
			}
		}
	}

	/**
	 * 添加对象
	 */
	@Override
	public void getNeedRefresh(OnNeedRefresh refresh, int index) {
		onRefreshList.add(refresh);
	}
}
