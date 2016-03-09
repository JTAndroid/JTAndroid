package com.tr.ui.work;

import java.net.IDN;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.tr.App;
import com.tr.R;
import com.tr.api.WorkReqUtil;
import com.tr.model.work.BUAffar;
import com.tr.model.work.BUAffarList;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.TongRenFragment;
import com.tr.ui.tongren.TongRenFragment.CurrentTongRenFrgTitle;
import com.tr.ui.work.CalendarLayout.OnDayClickListener;
import com.tr.ui.work.CalendarView.OnMonthChangeListener;
import com.tr.ui.work.CalendarView.OnTakeBackDayClickListener;
import com.tr.ui.work.WorkDatePickerDialog.OnDayChangeListener;
import com.utils.http.EAPIConsts.WorkReqType;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

/**
 * 事务
 * 
 * 所有事务列表：WorkReqType.AFFAIR_LIST_GET_ALL
 * 
 * @param jtContact
 *            数据对象 BUAffarList
 * @author Administrator
 * 
 */
public class WorkMainFragment extends JBaseFragment implements IBindData,
		OnMonthChangeListener, OnDayChangeListener, OnDayClickListener {
	/** 事务列表listview */
	private RelativeLayout LinearCardy;
	/** 事务列表的搜索 */
	private LinearLayout LinearSearch;
	// private TextView TextViewYearMonth;
	/** 日历控件 */
	private CalendarView mView;
	/** 存放整个日历的布局 */
	private CalendarLayout mCalendarLayout;
	/** 搜索输入框 */
	private EditText EditTextSearch;
	/** 筛选显示的文字 */
	private TextView TextViewSech;
	/** listview */
	private XListView ListViewWork;
	/** 事务列表的适配器 */
	private WorkMainAdapter mAdapter;
	/** 数据对象 */
	private BUAffarList mBUAffarList;
	/** 数据对象 */
	private BUAffarList mAffarListShow;
	/** 0 日历， 1 列表 */
	private int mShowType = 1;
	/** a：全部，进行中: b，未开始: r，已完成: d，过期未完成: e */
	private String mShowDataType;
	/** 日历状态下选择ActionBar天之后，改变的日期 */
	private String mCurrentDate;
	/** 用户Id */
	private long mUserID = 1;
	/** 筛选的弹窗 */
	// private PopupWindow mPopWindow;
	private AlertDialog mPopWindow;
	/** 搜索，传的字符串 */
	private String mKey = "";
	/** 点击切换事务页面样式 */
	private int flag = 0;//
	/** 记录有多少个时间数据 */
	int mPos = 0;
	/** 整体事务总布局 */
	private View mMainView;
	/** 列表的全布局 */
	private LinearLayout mLayout;
	/** 标题日期是否隐藏 */
	private boolean mIsTitleHide = false;
	/** 是否有滑动动画 */
	private boolean mIsAnim = false;
	/** 按下的X坐标 */
	private float lastX = 0;
	/** 按下的Y坐标 */
	private float lastY = 0;
	/** 是否向上滚动 */
	private boolean mIsTop = false;
	/** ListView布局的高 */
	private int mListViewLayoutHeight = 0;
	/** mLayout的高度 */
	private int mListOldHeight = 0;
	/** 年月的字符串 */
	private String mShowDataMonth;
	/** 年月日的字符串 */
	private String mShowDataDay;
	/** 是否显示listview的顶部 */
	private boolean mShowListViewTop = false;
	/** 日历的布局 */
	private LinearLayout CalendarRootLayout;
	/** 事务红点提醒 */
	private View affRemind;
	/** 一周日历的控件 */
	private CalendarView mCalendarView;
	/** 日历一周的显示布局 */
	private LinearLayout CalendarViewLayout;
	/** 是否日历在收回状态下点击的天 */
	private boolean isTakeBackDayClick = false;
	/** 是否是第一开始事务 */
	private boolean isFirstStart = true;
	/** 所有未读事务消息的id集合 */
	private ArrayList<String> isNewIds=new ArrayList<String>();

	/** MainActivityt 传过来的事务提醒的控件 */
	public WorkMainFragment(View view) {
		super();
		this.affRemind = view;
		if(affRemind != null){
			bottom_aff_red_dot = (ImageView) affRemind
					.findViewById(R.id.bottom_aff_red_dot);
		}
	}
	
	public WorkMainFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		getActivity().getWindow().invalidatePanelMenu(
				Window.FEATURE_OPTIONS_PANEL);
		View view = inflater.inflate(R.layout.work_main_activity, container,
				false);
		mMarkList = new ArrayList<String>(); // 存储备注信息的集合
		mMainView = view;
		/** 初始化布局 */
		initView();
		/** 初始化数据 */
		initData();
		/** 重置数据：根据日历，有日历获取当天的，没有则获取全部的数据 */
		resetViewData();
		/** 获取每月事务数量 */
		getMonthAffarDate(mCurrentDate);
		return view;
	}

	/** 初始化布局 */
	public void initView() {
		/* 初始化一个选中日期(今天) */
		ArrayList<String> TodayselectDateList = new ArrayList<String>();
		TodayselectDateList.add(CalendarView.getTodayDate());
		mLayout = (LinearLayout) mMainView.findViewById(R.id.layoutListView);
		LinearCardy = (RelativeLayout) mMainView.findViewById(R.id.LinearCardy);
		LinearSearch = (LinearLayout) mMainView.findViewById(R.id.LinearSearch);
		CalendarRootLayout = (LinearLayout) mMainView
				.findViewById(R.id.CalendarRootLayout);
		TextViewSech = (TextView) mMainView.findViewById(R.id.TextViewSech);
		ListViewWork = (XListView) mMainView.findViewById(R.id.ListViewWork);// 每一条事务的listview
		CalendarViewLayout = (LinearLayout) mMainView
				.findViewById(R.id.CalendarViewLayout);
		mCalendarView = (CalendarView) mMainView
				.findViewById(R.id.CalendarView);
		mCalendarView.setSelectDateList(TodayselectDateList);
		// 设置日历收回状态下点击的天
		mCalendarView
				.setTakeBackdayClickListener(new OnTakeBackDayClickListener() {

					@Override
					public void onTakeBackDayClick(Cell touchedCell) {
						Cell day = mCalendarView.getmTouchedCell();// 获取每一天的对象
						String vDate = day.getYear()
								+ WorkDatePickerDialog.intToStr2(day.getMonth())
								+ WorkDatePickerDialog.intToStr2(day
										.getDayOfMonth());// 获取年、月、日
						Log.d("xmx", "onDayClick mCurrentDate:" + mCurrentDate);

						String vOldYearMonth = mCurrentDate.substring(0, 6);// 获取旧的年、月
						String vNewYearMonth = vDate.substring(0, 6);// 获取新的年、月

						mCurrentDate = vDate;
						if (!vOldYearMonth.equals(vNewYearMonth)) {
							getMonthAffarDate(vNewYearMonth);
						}

						mShowDataMonth = mCalendarView.getYear() + "年"
								+ mCalendarView.getMonth() + "月";
						mShowDataDay = mCalendarView.getYear() + "年"
								+ mCalendarView.getMonth() + "月"
								+ mCalendarView.mSelDay + "日";
						ArrayList<String> selectDataList = new ArrayList<String>();// 存放一周的年、月、日集合
						selectDataList.add(getDayStrforDay(
								mCalendarView.getYear(),
								mCalendarView.getMonth(), mCalendarView.mSelDay));
						mCalendarLayout.setSelectDateList(selectDataList);
						mCalendarView.setSelectDateList(selectDataList);
						if (mIsTitleHide) {
							currentFlowFrgTitle = mShowDataDay;
						} else {
							currentFlowFrgTitle = mShowDataMonth;
						}
						isTakeBackDayClick = true;
						HomeCommonUtils.initLeftCustomActionBar(getActivity(),
								getActivity().getActionBar(),
								currentFlowFrgTitle, true, onSelectDayClick,
								true, false);// 日历状态下ActionBar的显示

						resetViewData();
					}
				});
		setXlistViewConfig();
		ListViewWork.setOnTouchListener(mOnTouchListener);// 设置触摸监听
		ListViewWork.setOnScrollListener(mScrollListtener);// 设置滑动监听
		ListViewWork.setDividerHeight(0);

		// 设置选中日期列表

		mCalendarLayout = (CalendarLayout) mMainView
				.findViewById(R.id.CalendarLayout);
		mCalendarLayout.setSelectDateList(TodayselectDateList);

		mView = mCalendarLayout.getMainView();// 获取日历控件
		mView.setMonthChangeListener(this);// 设置月改变的监听
		mCalendarLayout.setDayClickListener(this);// 设置天改变的监听

		mCalendarLayout.refresh();// 刷新日历

		mShowDataMonth = mView.getYear() + "年" + mView.getMonth() + "月";
		mShowDataDay = mView.getYear() + "年" + mView.getMonth() + "月"
				+ mView.mSelDay + "日";

		// TextViewYearMonth.setText(mShowDataMonth);
		currentFlowFrgTitle = mShowDataMonth;
		EditTextSearch = (EditText) mMainView.findViewById(R.id.EditTextSearch);

		EditTextSearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if ((arg1 == EditorInfo.IME_ACTION_UNSPECIFIED || arg1 == EditorInfo.IME_ACTION_SEARCH)
						&& arg2 != null) {// 手机软键盘的设置
					if (EditTextSearch.getText() != null
							&& !EditTextSearch.getText().toString().equals("")) {
						mKey = EditTextSearch.getText().toString();
						resetViewData();
						showLoadingDialog();
						mKey = "";
					} else {
						ToastUtil.showToast(getActivity(), "请输入查询内容");
					}
				}
				return false;
			}
		});

		Button ButtonChange = (Button) mMainView
				.findViewById(R.id.ButtonChange);
		ButtonChange.setOnClickListener(mChangeClick);

		Button ButtonCreate = (Button) mMainView
				.findViewById(R.id.ButtonCreate);
		ButtonCreate.setOnClickListener(mCreateClick);

		TextView ButtonPop = (TextView) mMainView.findViewById(R.id.ButtonPop);// 事务
																				// 筛选的按钮
		ButtonPop.setOnClickListener(mPopClick);

	}

	/** 设置XListView的参数 */
	private void setXlistViewConfig() {

		ListViewWork.showFooterView(false);
		// 设置xlistview可以加载、刷新
		ListViewWork.setPullRefreshEnable(true);
		ListViewWork.setPullLoadEnable(false);
		ListViewWork.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {// 刷新
				if (mShowType == 0) {
					getCurDateData();
				} else {
					getAllDateData();
				}
			}

			@Override
			public void onLoadMore() {// 加载更多
			}
		});

	}

	// 滑动监听
	public OnScrollListener mScrollListtener = new OnScrollListener() {

		@Override
		public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			// Log.d("xmx","scroll:"+arg1+",arg2:"+arg2+",arg3:"+arg3);
			if (arg1 == 0) {
				mIsTop = true;
			} else {
				mIsTop = false;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			switch (arg1) {
			case OnScrollListener.SCROLL_STATE_IDLE: //
				// mBusy = false;
				// Log.d("xmx","停止...");
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				// mBusy = true;
				// Log.d("xmx","正在滑动...");
				break;
			case OnScrollListener.SCROLL_STATE_FLING:
				// mBusy = true;
				// Log.d("xmx","开始滚动...");

				break;
			}
		}

	};
	/** 筛选按钮的点击 */
	private OnClickListener mPopClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mPopWindow.isShowing()) {
				mPopWindow.dismiss();
			} else {
				// mPopWindow.showAsDropDown(v, -150, -20);
				mPopWindow.show();
			}
		}
	};
	private String currentFlowFrgTitle = "2015年";
	// 查询按钮的点击
	private OnClickListener mChangeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mShowType == 0) {
				setShowType(1);

			} else {
				setShowType(0);

			}
		}
	};
	/** 创建事务跳转 */
	private OnClickListener mCreateClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ENavigate.startNewAffarActivity(getActivity());
		}
	};
	private CurrentTongRenFrgTitle currentTongRenFrgTitle = CurrentTongRenFrgTitle.first;

	/** 初始化数据 */
	public void initData() {

		Log.d("xmx", "WorkMainActivity initData");
		mShowDataType = "a";
		if (!TextUtils.isEmpty(App.getUserID())) {
			mUserID = Long.parseLong(App.getUserID());
		}
		// mUserID = 1;

		Calendar vNow = Calendar.getInstance();// 获取日历对象
		mCurrentDate = vNow.get(Calendar.YEAR)
				+ WorkDatePickerDialog.intToStr2(vNow.get(Calendar.MONTH) + 1)
				+ WorkDatePickerDialog.intToStr2(vNow
						.get(Calendar.DAY_OF_MONTH));// 获取年、月、日

		initPopupWindow();

		mBUAffarList = new BUAffarList();
		mAdapter = new WorkMainAdapter(getActivity(), mBUAffarList,
				ListViewWork);
		ListViewWork.setAdapter(mAdapter);// listView设置适配器
		ListViewWork
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {// 设置listview条目的点击监听

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (position != 0) {
							if (position >= 0
									&& position <= mAffarListShow.mAffarList
											.size()) {
								vAffar = mAffarListShow.mAffarList
										.get(position - 1);
							} else {
								vAffar = new BUAffar();
							}
							if (vAffar.getmShowType() == 1) {// 判断条目的类型，如果是1，不做跳转
								return;
							}

							// Log.d("xmx", "vAffarId:" + vAffar.id);
							Intent intent = new Intent(getActivity(),
									WorkNewActivity.class);// 跳转到详情页面
							intent.putExtra("OperType", "s"); // 查看
							intent.putExtra("UserId", mUserID); // 查看
							intent.putExtra("AffarId", vAffar.id + ""); // 查看

							startActivity(intent);
						}
					}
				});

	}

	public void setAllRedGone() {
		if(null!=bottom_aff_red_dot){
			bottom_aff_red_dot.setVisibility(View.GONE);
		}
		if(isNewIds!=null&&!isNewIds.isEmpty()){
			StringBuffer sb=new StringBuffer();
			for(String str:isNewIds){
				sb.append(str);
			}
			showLoadingDialog();
			WorkReqUtil.doAllMesReaded(getActivity(), this, sb.toString(), App.getApp().getUserID(), null);
		}
//		mAdapter.setAllRedGone(true);
	}
	/** 获取每月事务数量 */
	public void getMonthAffarDate(String inDate) {
		WorkReqUtil.getAffarMonthDateByDate(getActivity(), this, mUserID + "",
				inDate, null);

	}

	/**
	 * mShowType =0 日历模式 ，1 列表模式
	 */
	public void setShowType(int inType) {
		if (mListOldHeight == 0) {
			mListOldHeight = mLayout.getHeight();
			Log.d("xmx", "mListOldHeight:" + mListOldHeight);
		}
		if (mShowType == 0) {
			mShowType = 1;
			TongRenFragment parentFragment = (TongRenFragment) getParentFragment();
			HomeCommonUtils.initHorizontalCustomActionBar(getActivity(),
					getActivity().getActionBar(),
					parentFragment.onClickListener, currentTongRenFrgTitle);
			ListViewWork.setPullRefreshEnable(true);

		} else {
			mShowType = 0;
			HomeCommonUtils.initLeftCustomActionBar(getActivity(),
					getActivity().getActionBar(), currentFlowFrgTitle, true,
					onSelectDayClick, true, false);
			ListViewWork.setPullRefreshEnable(false);

		}
		resetViewData();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {// 创建选项菜单
		super.onCreateOptionsMenu(menu, inflater);
		// inflater.inflate(R.menu.myaffairspage_menu, menu);
		// menu.findItem(R.id.tongren_new_menu_msg).setVisible(false);

	}

	/*	*//**
	 * 设置菜单的显示与否
	 */
	/*
	 * @Override public void onPrepareOptionsMenu(Menu menu) {
	 * 
	 * menu.findItem(R.id.home_new_menu_more).setVisible(false);
	 * menu.findItem(R.id.home_new_menu_search).setVisible(false);
	 * 
	 * super.onPrepareOptionsMenu(menu); }
	 */

	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
	 * (item.getItemId()) { // 列表样式按钮 case R.id.affairs_new_menu_calendar: case
	 * R.id.affairs_new_menu_list:
	 * getActivity().getWindow().invalidatePanelMenu(
	 * Window.FEATURE_OPTIONS_PANEL); setShowType(1); flag++; if (flag % 2 == 0)
	 * { calendar.setVisible(false); list.setVisible(true); } else {
	 * calendar.setVisible(true); list.setVisible(false);
	 * 
	 * } break; case R.id.aff_create:
	 * ENavigate.startNewAffarActivity(getActivity()); break; }
	 * 
	 * return super.onOptionsItemSelected(item); }
	 */
	/**
	 * 重置数据：根据日历，有日历获取当天的，没有则获取全部的数据
	 */
	public void resetViewData() {
		if (mShowType == 0) {
			LinearCardy.setVisibility(View.VISIBLE);
			LinearSearch.setVisibility(View.GONE);
			if (mIsTitleHide) {
				mShowListViewTop = false;
				float[] f = new float[2];
				f[0] = 0.0F;
				f[1] = -mCalendarLayout.getHeight()
						+ CalendarViewLayout.getHeight();
				// mCalendarLayout.setActionType(ActionType.TakeBack);
				View view2 = mLayout;
				ObjectAnimator animator2 = ObjectAnimator.ofFloat(view2,
						"translationY", f);
				animator2
						.setInterpolator(new AccelerateDecelerateInterpolator());
				animator2.setDuration(50);
				animator2.start();
				LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						mListViewLayoutHeight + mCalendarLayout.getHeight());
				mLayout.setLayoutParams(layoutParam);
				if (mIsTakeBack) {
					float[] f1 = new float[2];
					f1[0] = -CalendarViewLayout.getHeight();
					f1[1] = 0.0F;
					View view3 = CalendarViewLayout;
					ObjectAnimator animator3 = ObjectAnimator.ofFloat(view3,
							"translationY", f1);
					animator3
							.setInterpolator(new AccelerateDecelerateInterpolator());
					animator3.setDuration(50);
					animator3.start();
				} else {
					float[] f1 = new float[2];
					f1[0] = 0.0F;
					f1[1] = -CalendarViewLayout.getHeight();
					View view3 = CalendarViewLayout;
					ObjectAnimator animator3 = ObjectAnimator.ofFloat(view3,
							"translationY", f1);
					animator3
							.setInterpolator(new AccelerateDecelerateInterpolator());
					animator3.setDuration(50);
					animator3.start();
				}

			}
			getCurDateData();
		} else {
			LinearSearch.setVisibility(View.VISIBLE);
			LinearCardy.setVisibility(View.GONE);
			if (mIsTitleHide) {
				mShowListViewTop = false;
				float[] f = new float[2];
				f[0] = -mCalendarLayout.getHeight()
						+ CalendarViewLayout.getHeight();
				f[1] = 0F;
				View view2 = mLayout;
				ObjectAnimator animator2 = ObjectAnimator.ofFloat(view2,
						"translationY", f);
				animator2
						.setInterpolator(new AccelerateDecelerateInterpolator());
				animator2.setDuration(50);
				animator2.start();

			}

			getAllDateData();
		}
	}

	/** 获取当天的事务数据 */
	public void getCurDateData() {
		if (mAffarListShow != null) {
			mAffarListShow.mAffarList.clear();
			mAdapter.setItemList(mAffarListShow.mAffarList);
		}

		WorkReqUtil.getAffarListByDate(getActivity(), this, mUserID + "",
				mCurrentDate, mShowDataType, null);

	}

	/**
	 * 获取当前时间
	 */
	public void getCurDateBack() {
		int i;
		String vDate = "";

		if (mAffarListShow == null)
			mAffarListShow = new BUAffarList();

		mAffarListShow.mAffarList.clear();// 清除列表
		if (mBUAffarList.mAffarList != null) {
			for (i = 0; i < mBUAffarList.mAffarList.size(); i++) {// 遍历事务列表
				BUAffar vAffar = mBUAffarList.mAffarList.get(i);
				Log.d("xmx", "vAffar:" + vAffar.id + ",type:"
						+ vAffar.titleType + ",title" + vAffar.title);
				mAffarListShow.mAffarList.add(vAffar);
			}
			mAdapter.setItemList(mAffarListShow.mAffarList);
		}
	}

	/** 获取全部事务的数据 */
	public void getAllDateData() {
		if (mAdapter != null)
			mAdapter.setAllRedGone(false);
		if (mAffarListShow != null) {
			mAffarListShow.mAffarList.clear();
			mAdapter.setItemList(mAffarListShow.mAffarList);
		}
		WorkReqUtil.getAffarListByAll(getActivity(), this, mUserID + "",
				mCurrentDate, mShowDataType, mKey, null);// 请求网络

		getAllDateBack();
	}

	/**
	 * 获取所有时间
	 */
	public void getAllDateBack() {
		int i;
		String vDate = "";
		if (mAffarListShow == null)
			mAffarListShow = new BUAffarList();
		if (mBUAffarList.mAffarList != null) {// 事务有列表
			mAffarListShow.mAffarList.clear();
			// final Date vDateTime = new Date();
			Date vDateTime = new Date();
			/** 格式化日期 */
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			String mCurDateStr = df.format(vDateTime);
			mPos = -1;
			int vCount = 0;
			// 遍历事务列表
			for (i = 0; i < mBUAffarList.mAffarList.size(); i++) {
				BUAffar vAffar = mBUAffarList.mAffarList.get(i);
				if (vAffar != null && !TextUtils.isEmpty(vAffar.getStartTime())) {
					String vAffDate = vAffar.getStartTime().substring(0, 8);// 获取开始时间
					Log.d("xmx", "vAffDate:" + vAffDate);
					if (!vAffDate.equals(vDate)) {// 对时间进行对比
						BUAffar vAddAffar = new BUAffar();
						vAddAffar.setmShowType(1);// 0 正常数据 1 日期数据
						vAddAffar.setStartTime(vAffDate);// 设置开始时间
						mAffarListShow.mAffarList.add(vAddAffar);
						vDate = vAffDate;
						// compareto两个比较字符串的asc码的差值
						if (mCurDateStr.compareTo(vDate) <= 0 && mPos == -1)
							mPos = vCount;
						vCount = vCount + 1;
					}
					mAffarListShow.mAffarList.add(vAffar);
					vCount = vCount + 1;
				}
			}
			if (mPos == -1)
				mPos = 0;
			mAdapter.setItemList(mAffarListShow.mAffarList);

			ListViewWork.post(new Runnable() //
					{ //
						public void run() //
						{ //
							ListViewWork.setSelection(mPos); //
						} //
					});
			ListViewWork.setSelection(mPos);
			mAdapter.notifyDataSetChanged();

		}
	}

	// 点击显示的筛选弹框
	public void onShowPopClick(View v) {
		if (mPopWindow.isShowing()) {
			mPopWindow.dismiss();
		} else {
			// mPopWindow.showAsDropDown(v, -200, -20);
			mPopWindow.show();
		}
	}

	/** 日历状态下，选择ActionBar的天 */
	private OnClickListener onSelectDayClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Calendar calendar = Calendar.getInstance();
			Date aa = calendar.getTime();
			WorkDatePickerDialog datePicKDialog = new WorkDatePickerDialog(
					getActivity(), mCurrentDate);
			datePicKDialog.dateTimePicKDialog(0);

			datePicKDialog.setDayChangeListener(mDayChage);

			InputMethodManager inputmanger = (InputMethodManager) getActivity()
					.getSystemService(getActivity().INPUT_METHOD_SERVICE);
			if (inputmanger.isActive()) {
				inputmanger.toggleSoftInput(0,
						InputMethodManager.HIDE_NOT_ALWAYS);// 隐藏输入法
			}

		}
	};
	/** 日历状态下，ActionBar选择日期改变天的监听 */
	public OnDayChangeListener mDayChage = new OnDayChangeListener() {

		@Override
		public void onDayChagne(String outDay) {
			String vOldYearMonth = mCurrentDate.substring(0, 6);
			String vNewYearMonth = outDay.substring(0, 6);

			if (!vOldYearMonth.equals(vNewYearMonth)) {
				getMonthAffarDate(vNewYearMonth);
			}
			mCurrentDate = outDay;

			int vYear = Integer.parseInt(mCurrentDate.substring(0, 4));
			int vMonth = Integer.parseInt(mCurrentDate.substring(4, 6));
			int vDay = Integer.parseInt(mCurrentDate.substring(6, 8));
			Log.d("xmx",
					"vYear:" + mCurrentDate.substring(0, 4) + " vMonth:"
							+ mCurrentDate.substring(4, 6) + " vDay:"
							+ mCurrentDate.substring(6, 8));
			mCalendarLayout.setYearMonth(vYear, vMonth, vDay);
			mShowDataMonth = mView.getYear() + "年" + mView.getMonth() + "月";
			mShowDataDay = mView.getYear() + "年" + mView.getMonth() + "月"
					+ mView.mSelDay + "日";
			if (mIsTitleHide) {
				// TextViewYearMonth.setText(mShowDataDay);
				currentFlowFrgTitle = mShowDataDay;
				ArrayList<String> mSelectDateList = new ArrayList<String>();
				mSelectDateList.add(getDayStrforDay(mView.getYear(),
						mView.getMonth(), mView.mSelDay));
				mCalendarView.setSelectDateList(mSelectDateList);
			} else {
				// TextViewYearMonth.setText(mShowDataMonth);
				currentFlowFrgTitle = mShowDataMonth;
			}
			HomeCommonUtils.initLeftCustomActionBar(getActivity(),
					getActivity().getActionBar(), currentFlowFrgTitle, true,
					onSelectDayClick, true, false);

			getCurDateData();
		}
	};
	private BUAffarList mAList;
	private ArrayList<String> mMarkList;

	/** 筛选的弹窗 */
	private void initPopupWindow() {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.work_main_pop_window, null);
		// mPopWindow = new PopupWindow(view,
		// ViewGroup.LayoutParams.WRAP_CONTENT,
		// ViewGroup.LayoutParams.WRAP_CONTENT);
		// mPopWindow.setOutsideTouchable(true);
		// mPopWindow.setBackgroundDrawable(new ColorDrawable(0));
		LinearLayout LinearLayoutAll = (LinearLayout) view
				.findViewById(R.id.LinearLayoutAll);
		LinearLayoutAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextViewSech.setText("全部");
				mShowDataType = "a"; // 全部:a，进行中: b，未开始: r，已完成: d，过期未完成: e
				resetViewData();
				Log.d("xmx", "v:LinearLayoutAll");
				mPopWindow.dismiss();
				showLoadingDialog();
			}
		});
		LinearLayout LinearLayoutMyCreate = (LinearLayout) view
				.findViewById(R.id.LinearLayoutMyCreate);
		LinearLayoutMyCreate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextViewSech.setText("进行中");
				mShowDataType = "b"; // 全部:a，进行中: b，未开始: r，已完成: d，过期未完成: e
				resetViewData();
				Log.d("xmx", "v:LinearLayoutMyCreate");
				mPopWindow.dismiss();
				showLoadingDialog();
			}
		});
		LinearLayout LinearLayoutMyLead = (LinearLayout) view
				.findViewById(R.id.LinearLayoutMyLead);
		LinearLayoutMyLead.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextViewSech.setText("未开始");
				mShowDataType = "r"; // 全部:a，进行中: b，未开始: r，已完成: d，过期未完成: e
				resetViewData();
				Log.d("xmx", "v:LinearLayoutMyLead");
				mPopWindow.dismiss();
				showLoadingDialog();
			}
		});

		LinearLayout LinearLayoutNOEnd = (LinearLayout) view
				.findViewById(R.id.LinearLayoutNOEnd);
		LinearLayoutNOEnd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextViewSech.setText("已完成");
				mShowDataType = "d"; // 全部:a，进行中: b，未开始: r，已完成: d，过期未完成: e
				resetViewData();
				Log.d("xmx", "v:LinearLayoutEnd");
				mPopWindow.dismiss();
				showLoadingDialog();
			}
		});

		LinearLayout LinearLayoutEnd = (LinearLayout) view
				.findViewById(R.id.LinearLayoutEnd);
		LinearLayoutEnd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextViewSech.setText("过期未完成");
				mShowDataType = "e"; // 全部:a，进行中: b，未开始: r，已完成: d，过期未完成: e
				resetViewData();
				Log.d("xmx", "v:LinearLayoutEnd");
				mPopWindow.dismiss();
				showLoadingDialog();
			}
		});

		mPopWindow = new AlertDialog.Builder(getActivity()).setView(view)
				.create();
		mPopWindow.setCanceledOnTouchOutside(true);

	}

	@Override
	public void bindData(int tag, Object object) {
		// TODO Auto-generated method stub
		// XlistView结束刷新时状态复位
		ListViewWork.stopLoadMore();
		ListViewWork.stopRefresh();
		Log.d("xmx", "bindData:" + tag);
		if (object != null) {
			Log.d("xmx", "bindData:" + object.toString());

		} else {
			// ToastUtil.showToast(getActivity(), "网络错误");
			return;
		}
		if (isFirstStart) {
			isFirstStart = false;
		}
		switch (tag) {
		case WorkReqType.AFFAIR_ALL_MES_READED:
			Boolean result=(Boolean) object;
			if(result)
				mAdapter.setAllRedGone(true);
			break;
		case WorkReqType.AFFAIR_LIST_GET: {
			dismissLoadingDialog();
			// 事务列表
			Log.d("xmx", "showType:" + mShowType);
			if (mShowType == 0) {
				mBUAffarList = (BUAffarList) object;
				getCurDateBack();
				HashMap<String, Boolean> hashMap_all = calendarIsRed(mergeBUAffarList(
						mAList, mBUAffarList));
				mCalendarLayout.addMarkRedMap(hashMap_all);
				mCalendarView.mMarkRedMap = hashMap_all;
				mCalendarView.resetRemarkMap();
			}
			if (mBUAffarList.isNew.equals("0")) {
				bottom_aff_red_dot.setVisibility(View.GONE);
			} else {
				bottom_aff_red_dot.setVisibility(View.VISIBLE);
			}
		}
			break;
		case WorkReqType.AFFAIR_LIST_GET_ALL:
			// 所有事务列表
			Log.d("xmx", "showType:" + mShowType);
			mAList = (BUAffarList) object;
			isNewIds.clear();
			if (mAList != null && mAList.mAffarList != null) {

				for (int i = 0; i < mAList.mAffarList.size(); i++) {
					String isNew = mAList.mAffarList.get(i).isNew;// 是否有新事物通知
					if (isNew.equals("1")) {
						if (null != isNewIds && isNewIds.size() == 0)
							isNewIds.add(String.valueOf(mAList.mAffarList.get(i).id));
						else
							isNewIds.add(","+ String.valueOf(mAList.mAffarList.get(i).id));
					} 
				}
			}
			Log.d("isNewIds.toString()", "isNewIds.toString():" + isNewIds.toString());
			mBUAffarList = (BUAffarList) object;
			getAllDateBack();
			HashMap<String, Boolean> hashMap_all = calendarIsRed(mAList);
			mCalendarLayout.addMarkRedMap(hashMap_all);
			mCalendarView.mMarkRedMap = hashMap_all;
			mCalendarView.resetRemarkMap();
			if (mBUAffarList.isNew.equals("0") && bottom_aff_red_dot != null) {
				bottom_aff_red_dot.setVisibility(View.GONE);
			} else if(bottom_aff_red_dot != null){
				bottom_aff_red_dot.setVisibility(View.VISIBLE);
			}
			break;
		case WorkReqType.AFFAIR_LIST_MONTH_DATE_GET:
			List<String> vDateList = (List<String>) object;
			Log.d("xmx", "listaa:" + vDateList);
			if (vDateList != null) {
				mCalendarLayout.addMarkList(vDateList);
				int i;

				if (vDateList.size() > 0) {
					String vStrOld = vDateList.get(0);
					String vStrMon = vStrOld.substring(0, 6);
					deleteYearMonth(vStrMon);
				}
				mMarkList.clear();
				for (i = 0; i < vDateList.size(); i++) {
					String vStr = vDateList.get(i);
					Log.d("xmx", "addmark:" + vStr);
					mMarkList.add(vStr);
				}
				mCalendarView.mMarkList = mMarkList;
				mCalendarView.resetRemark();
			}
			break;
		}
		dismissLoadingDialog();
	}

	public void deleteYearMonth(String inYearMon) {
		int i = mMarkList.size() - 1;
		while (i >= 0) {
			String vStr = mMarkList.get(i);
			if (vStr.substring(0, 6).equals(inYearMon)) {
				Log.d("xmx", "deleteYearMonth:" + vStr + ",inYearMon:"
						+ inYearMon);
				mMarkList.remove(i);
			}
			i = i - 1;
		}
	}

	/** 日历红点的显示 */
	private HashMap<String, Boolean> calendarIsRed(BUAffarList mAffarList) {
		HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();// 日历红点的显示
		Boolean isRed = false;
		if (mAffarList != null && mAffarList.mAffarList != null) {

			for (int i = 0; i < mAffarList.mAffarList.size(); i++) {
				String startTime = mAffarList.mAffarList.get(i).startTime;// 获取事务开始时间
				String[] split = startTime.split(" ");
				startTime = split[0];
				String isNew = mAffarList.mAffarList.get(i).isNew;// 是否有新事物通知
				if (isNew.equals("1")) {
					isRed = true;
					hashMap.put(startTime, isRed);
				} else {
					isRed = false;
				}
			}
		}
		return hashMap;
	}

	@Override
	public void onMonthChanged(boolean isNext) {

		mShowDataMonth = mView.getYear() + "年" + mView.getMonth() + "月";
		if (mIsTitleHide) {
			// TextViewYearMonth.setText(mShowDataDay);
			currentFlowFrgTitle = mShowDataDay;

		} else {
			// TextViewYearMonth.setText(mShowDataMonth);
			currentFlowFrgTitle = mShowDataMonth;
		}
		if (isNext) {
			mCalendarView.nextMonth();
		} else {
			mCalendarView.previousMonth();
		}
		String vStrMonth = "0" + mView.getMonth();
		Log.d("xmx", "vStr:" + vStrMonth);
		String vStr = mView.getYear()
				+ vStrMonth.substring(vStrMonth.length() - 2,
						vStrMonth.length());

		Log.d("xmx", "vStr:" + vStr);
		HomeCommonUtils.initLeftCustomActionBar(getActivity(), getActivity()
				.getActionBar(), currentFlowFrgTitle, true, onSelectDayClick,
				true, false);

		getMonthAffarDate(vStr);
	}

	// 日历 选择变化
	@Override
	public void onDayClick(Cell touchedCell, int row) {
		Cell day = mView.getmTouchedCell();
		String vDate = day.getYear()
				+ WorkDatePickerDialog.intToStr2(day.getMonth())
				+ WorkDatePickerDialog.intToStr2(day.getDayOfMonth());
		Log.d("xmx", "onDayClick mCurrentDate:" + mCurrentDate);

		String vOldYearMonth = mCurrentDate.substring(0, 6);
		String vNewYearMonth = vDate.substring(0, 6);

		mCurrentDate = vDate;
		if (!vOldYearMonth.equals(vNewYearMonth)) {
			getMonthAffarDate(vNewYearMonth);
		}

		mShowDataMonth = mView.getYear() + "年" + mView.getMonth() + "月";
		mShowDataDay = mView.getYear() + "年" + mView.getMonth() + "月"
				+ mView.mSelDay + "日";

		if (mIsTitleHide) {
			// TextViewYearMonth.setText(mShowDataDay);
			currentFlowFrgTitle = mShowDataDay;
			ArrayList<String> mSelectDateList = new ArrayList<String>();
			mSelectDateList.add(getDayStrforDay(mView.getYear(),
					mView.getMonth(), mView.mSelDay));
			mCalendarView.setSelectDateList(mSelectDateList);
		} else {
			// TextViewYearMonth.setText(mShowDataMonth);
			currentFlowFrgTitle = mShowDataMonth;
		}
		mCalendarView.setmRow(row);
		HomeCommonUtils.initLeftCustomActionBar(getActivity(), getActivity()
				.getActionBar(), currentFlowFrgTitle, true, onSelectDayClick,
				true, false);
		isTakeBackDayClick = false;
		resetViewData();
	}

	@Override
	public void onDayChagne(String outDay) {

	}

	/** 是否按下 */
	private boolean isDown = false;
	/** 是否抬起 */
	private boolean isUp = false;
	private boolean mIsTakeBack = false;
	/** 触摸事件的监听 */
	private OnTouchListener mOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// mIsTouchList = true;
			MotionEvent event = arg1;
			final int action = event.getAction();
			float x = event.getX();
			float y = event.getY();
			// Log.d("xmx","y:"+y);
			switch (action) {

			case MotionEvent.ACTION_DOWN:// 按下
				lastY = y;
				lastX = x;
				return false;
			case MotionEvent.ACTION_MOVE:// 移动
				float dY = Math.abs(y - lastY);
				float dX = Math.abs(x - lastX);
				boolean down = y > lastY ? true : false;
				lastY = y;
				lastX = x;
				isUp = dX < 8 && dY > 8 && !mIsTitleHide && !down && !mIsAnim
						&& (mShowType == 0);// 移动的x轴小于8，y轴大于8，标题日期不隐藏，按下，滑动动画，日历状态下
				isDown = dX < 8 && dY > 8 && mIsTitleHide && down && !mIsAnim
						&& (mShowType == 0);
				if (isUp) {
					mCalendarView.setVisibility(View.VISIBLE);
					mShowListViewTop = true;
					// mCalendarLayout.setActionType(ActionType.TakeBack);
					View view = mCalendarLayout;
					float[] f = new float[2];
					f[0] = 0f;
					f[1] = -mCalendarLayout.getHeight();// 整个日历的高度（负的）
					ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,
							"translationY", f);// 动画
					animator1
							.setInterpolator(new AccelerateDecelerateInterpolator());
					animator1.setDuration(400);
					animator1.start();
					animator1.addListener(mAnimatorListener);
					float[] f2 = new float[2];
					f2[0] = 0f;
					f2[1] = -mCalendarLayout.getHeight()
							+ mCalendarView.getHeight();

					View view2 = mLayout;
					ObjectAnimator animator2 = ObjectAnimator.ofFloat(view2,
							"translationY", f2);
					animator2
							.setInterpolator(new AccelerateDecelerateInterpolator());
					animator2.setDuration(400);
					animator2.start();
					// LinearLayout.LayoutParams layoutParam = new
					// LinearLayout.LayoutParams(
					// LinearLayout.LayoutParams.MATCH_PARENT,
					// LinearLayout.LayoutParams.MATCH_PARENT);
					if (mListViewLayoutHeight == 0)
						mListViewLayoutHeight = mLayout.getHeight();//
					LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							mListViewLayoutHeight + mCalendarLayout.getHeight());
					mLayout.setLayoutParams(layoutParam);
					mLayout.invalidate();
					mIsTitleHide = !mIsTitleHide;
					mIsAnim = true;
					mIsTakeBack = true;
					if (mIsTitleHide) {
						// TextViewYearMonth.setText(mShowDataDay);
						currentFlowFrgTitle = mShowDataDay;
						// ArrayList mSelectDateList = new ArrayList<String>();
						// mSelectDateList.add(mView.getYear() +
						// mView.getMonth()
						// +mView.mSelDay);
						// mCalendarView.setSelectDateList(mSelectDateList);
						if (isTakeBackDayClick) {
							ArrayList<String> mSelectDateList = new ArrayList<String>();
							mSelectDateList.add(getDayStrforDay(
									mCalendarView.getYear(),
									mCalendarView.getMonth(),
									mCalendarView.mSelDay));
							mCalendarView.setSelectDateList(mSelectDateList);
						} else {
							ArrayList<String> mSelectDateList = new ArrayList<String>();
							mSelectDateList.add(getDayStrforDay(
									mView.getYear(), mView.getMonth(),
									mView.mSelDay));
							mCalendarView.setSelectDateList(mSelectDateList);
						}

					} else {
						// TextViewYearMonth.setText(mShowDataMonth);
						currentFlowFrgTitle = mShowDataMonth;
					}

					HomeCommonUtils.initLeftCustomActionBar(getActivity(),
							getActivity().getActionBar(), currentFlowFrgTitle,
							true, onSelectDayClick, true, false);

					float[] f1 = new float[2];
					f1[0] = -CalendarViewLayout.getHeight();
					f1[1] = 0.0F;
					// mCalendarLayout.setActionType(ActionType.TakeBack);
					View view3 = CalendarViewLayout;
					ObjectAnimator animator3 = ObjectAnimator.ofFloat(view3,
							"translationY", f1);
					animator3
							.setInterpolator(new AccelerateDecelerateInterpolator());
					animator3.setDuration(400);
					animator3.start();
					animator3.addListener(mCalendarAnimatorListener);

				} else if (isDown && mIsTop) {
					mShowListViewTop = true;
					// mCalendarLayout.setActionType(ActionType.PullDown);
					View view = mCalendarLayout;
					float[] f = new float[2];
					f[0] = -mCalendarLayout.getHeight();
					f[1] = 0f;
					ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,
							"translationY", f);
					animator1.setDuration(400);
					animator1
							.setInterpolator(new AccelerateDecelerateInterpolator());
					animator1.start();
					animator1.addListener(mAnimatorListener);

					float[] f2 = new float[2];
					f2[0] = -mCalendarLayout.getHeight()
							+ mCalendarView.getHeight();
					f2[1] = 0f;
					View view2 = mLayout;
					ObjectAnimator animator2 = ObjectAnimator.ofFloat(view2,
							"translationY", f2);
					animator2
							.setInterpolator(new AccelerateDecelerateInterpolator());
					animator2.setDuration(400);
					animator2.start();
					mIsAnim = true;
					mIsTitleHide = !mIsTitleHide;
					if (mIsTitleHide) {
						// TextViewYearMonth.setText(mShowDataDay);
						currentFlowFrgTitle = mShowDataDay;
					} else {
						// TextViewYearMonth.setText(mShowDataMonth);
						currentFlowFrgTitle = mShowDataMonth;
					}
					mIsTakeBack = false;
					float[] f1 = new float[2];
					f1[0] = 0.0F;
					f1[1] = -CalendarViewLayout.getHeight();
					// mCalendarLayout.setActionType(ActionType.TakeBack);
					View view3 = CalendarViewLayout;
					ObjectAnimator animator3 = ObjectAnimator.ofFloat(view3,
							"translationY", f1);
					animator3
							.setInterpolator(new AccelerateDecelerateInterpolator());
					animator3.setDuration(400);
					animator3.start();
					animator3.addListener(mCalendarAnimatorListener);

					HomeCommonUtils.initLeftCustomActionBar(getActivity(),
							getActivity().getActionBar(), currentFlowFrgTitle,
							true, onSelectDayClick, true, false);

				} else {
					return false;
				}

				return false;
			default:
				return false;
			}
			// return false;
		}

	};
	Animator.AnimatorListener mCalendarAnimatorListener = new Animator.AnimatorListener() {

		@Override
		public void onAnimationCancel(Animator arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationEnd(Animator arg0) {

			if (mIsTakeBack) {

			} else {
				// mCalendarView.setVisibility(View.GONE);
			}

		}

		@Override
		public void onAnimationRepeat(Animator arg0) {
			// mCalendarView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationStart(Animator arg0) {
			// TODO Auto-generated method stub

		}

	};

	/**
	 * 把年月日拼成字符串格式 ####年##月##日
	 * 
	 * @param inYear
	 * @param inMonth
	 * @param inDay
	 * @return
	 */
	private static String getDayStrforDay(int inYear, int inMonth, int inDay) {
		String vMonth = "0" + inMonth;
		vMonth = vMonth.substring(vMonth.length() - 2, vMonth.length());
		String vDay = "0" + inDay;
		vDay = vDay.substring(vDay.length() - 2, vDay.length());
		return inYear + vMonth + vDay;
	}

	Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {

		@Override
		public void onAnimationCancel(Animator arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationEnd(Animator arg0) {
			if (mShowListViewTop) {

				Log.d("xmx", "show list top");
				ListViewWork.post(new Runnable() //
						{ //
							public void run() //
							{ //
								ListViewWork.setSelection(0); //
							} //
						});
			}
			mIsAnim = false;

		}

		@Override
		public void onAnimationRepeat(Animator arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animator arg0) {
			// TODO Auto-generated method stub

		}

	};
	private BUAffar vAffar;
	private MenuItem aff_time;
	private TextView actionBarAfftitleTv;
	private ImageView bottom_aff_red_dot;// 红点的图片
	/** 是否显示用户 */
	private boolean mIsVisibleToUser;
	private MenuItem calendar;
	private MenuItem list;

	/**
	 * 实现懒加载
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		mIsVisibleToUser = isVisibleToUser;
		if (mIsVisibleToUser && !isFirstStart) {
			resetViewData();
			getMonthAffarDate(mCurrentDate);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mIsVisibleToUser) {
			resetViewData();
			getMonthAffarDate(mCurrentDate);
		}
	}

	public BUAffarList mergeBUAffarList(BUAffarList allAList,
			BUAffarList currentAList) {
		if (allAList != null && currentAList != null) {
			int count1 = allAList.mAffarList.size();
			int count2 = currentAList.mAffarList.size();
			if (count1 >= count2) {
				for (int i = 0; i < count1; i++) {
					for (int j = 0; j < count2; j++) {
						if (allAList.mAffarList.get(i).getId() == currentAList.mAffarList
								.get(j).getId()) {
							if (currentAList.mAffarList.get(j).isNew
									.equals("0")
									&& allAList.mAffarList.get(i).isNew
											.equals("1")) {
								allAList.mAffarList.get(i).isNew = "0";
							} else if (currentAList.mAffarList.get(j).isNew
									.equals("1")
									&& allAList.mAffarList.get(i).isNew
											.equals("0")) {
								allAList.mAffarList.get(i).isNew = "1";
							}
						}
					}
				}
			}
		}
		return allAList;
	}

}
