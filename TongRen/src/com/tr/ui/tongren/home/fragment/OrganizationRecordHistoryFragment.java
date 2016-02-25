package com.tr.ui.tongren.home.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.tr.App;
import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.model.record.RecordDetail;
import com.tr.ui.tongren.model.record.RecordRule;
import com.tr.ui.tongren.model.record.Records;
import com.tr.ui.work.CalendarLayout;
import com.tr.ui.work.CalendarView;
import com.tr.ui.work.Cell;
import com.tr.ui.work.WorkDatePickerDialog;
import com.tr.ui.work.CalendarLayout.OnDayClickListener;
import com.tr.ui.work.CalendarView.OnMonthChangeListener;
import com.tr.ui.work.CalendarView.OnTakeBackDayClickListener;
import com.tr.ui.work.WorkDatePickerDialog.OnDayChangeListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.time.TimeUtil;

public class OrganizationRecordHistoryFragment extends JBaseFragment implements
		IBindData, OnMonthChangeListener, OnDayChangeListener,
		OnDayClickListener {

	private RelativeLayout LinearCardy;
	/**日历一周的显示布局*/
	private LinearLayout CalendarViewLayout;
	/**一周日历的控件*/
	private CalendarView mCalendarView;
	/**日历的布局*/
	private LinearLayout CalendarRootLayout;
	/**存放整个日历的布局*/
	private CalendarLayout mCalendarLayout;
	/**日历状态下选择ActionBar天之后，改变的日期*/
	private String mCurrentDate;
	/**年月的字符串*/
	private String mShowDataMonth;
	/**年月日的字符串*/
	private String mShowDataDay;
	/**标题日期是否隐藏*/
	private boolean mIsTitleHide = false;
	private String currentFlowFrgTitle = "2015年";
	/**是否日历在收回状态下点击的天*/
	private boolean isTakeBackDayClick = false;
	/**日历控件*/
	private CalendarView mView;

	/**列表的全布局*/
	private LinearLayout mLayout;
	private ListView recordLv;
	private RecordAdapter recordAdapter;
	private List<RecordDetail> recorddetails = new ArrayList<RecordDetail>();
	private List<RecordDetail> temp_recorddetails = new ArrayList<RecordDetail>(); 
	private String oid;
	private String startWorkTime, workTimeOut;
	
	private String currentMonth;
	
	private boolean isButtonClick = false;//是否是点击的dialog设置的日期
	
	private Context mContext;
    private Menu mMenu;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = getActivity();
		
		currentMonth = TimeUtil.getDate(new Date(), TimeUtil.SDF_YYYY_MM);
		oid = getArguments().getString("oid");
		showLoadingDialog();
		getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETRULE);//获取考勤规则
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tongren_org_record_history_activity, null);
		mLayout = (LinearLayout) view.findViewById(R.id.layoutListView);
		recordLv = (ListView) view.findViewById(R.id.recordLv);
		recordAdapter = new RecordAdapter(mContext);
		recordLv.setAdapter(recordAdapter);
		
		//初始化日历
		initCalendarView(view);
		
		recordLv.setOnTouchListener(mOnTouchListener);//设置触摸监听
		recordLv.setOnScrollListener(mScrollListtener);//设置滑动监听
		return view;
	}
	
	private void initCalendarView(View view){
		LinearCardy = (RelativeLayout) view.findViewById(R.id.LinearCardy);
		CalendarViewLayout = (LinearLayout) view.findViewById(R.id.CalendarViewLayout);
		mCalendarView = (CalendarView) view.findViewById(R.id.CalendarView);
		CalendarRootLayout = (LinearLayout) view.findViewById(R.id.CalendarRootLayout);
		mCalendarLayout = (CalendarLayout) view.findViewById(R.id.CalendarLayout);
		
		/*初始化一个选中日期(今天)*/ 
		ArrayList<String> TodayselectDateList = new ArrayList<String>();
		TodayselectDateList.add(CalendarView.getTodayDate());
		
		mCalendarView.setSelectDateList(TodayselectDateList);
		//设置日历收回状态下点击的天
		mCalendarView.setTakeBackdayClickListener(new OnTakeBackDayClickListener() {

					@Override
					public void onTakeBackDayClick(Cell touchedCell) {
						final Cell day = mCalendarView.getmTouchedCell();//获取每一天的对象
						String vDate = day.getYear()
								+ WorkDatePickerDialog.intToStr2(day.getMonth())
								+ WorkDatePickerDialog.intToStr2(day
										.getDayOfMonth());//获取年、月、日
						Log.d("xmx", "onDayClick mCurrentDate:" + mCurrentDate);

						String vOldYearMonth = mCurrentDate.substring(0, 6);//获取旧的年、月
						String vNewYearMonth = vDate.substring(0, 6);//获取新的年、月

						mCurrentDate = vDate;
						if (!vOldYearMonth.equals(vNewYearMonth)) {
							
						}

						mShowDataMonth = mCalendarView.getYear() + "年"
								+ mCalendarView.getMonth() + "月";
						mShowDataDay = mCalendarView.getYear() + "年"
								+ mCalendarView.getMonth() + "月"
								+ mCalendarView.mSelDay + "日";
						ArrayList<String> selectDataList = new ArrayList<String>();//存放一周的年、月、日集合
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
						HomeCommonUtils.initLeftCustomActionBar(mContext,getActivity().getActionBar(),
								currentFlowFrgTitle, true, onSelectDayClick,
								true, true);//日历状态下ActionBar的显示
						
						recordLv.post(new Runnable() {
							public void run() {
								if(recorddetails.size()>0){
									if(day.getDayOfMonth()>recorddetails.size()){
										temp_recorddetails.clear();
										recordAdapter.setList(temp_recorddetails);
										recordAdapter.notifyDataSetChanged();
									}else{
										temp_recorddetails.clear();
										temp_recorddetails.add(recorddetails.get(day.getDayOfMonth()-1));
										recordAdapter.setList(temp_recorddetails);
										recordAdapter.notifyDataSetChanged();
									}
								}
							}
						});
					}
				});
		// 设置选中日期列表
		mCalendarLayout.setSelectDateList(TodayselectDateList);

		mView = mCalendarLayout.getMainView();//获取日历控件
		mView.setMonthChangeListener(this);//设置月改变的监听
		mCalendarLayout.setDayClickListener(this);//设置天改变的监听

		mCalendarLayout.refresh();//刷新日历

		mShowDataMonth = mView.getYear() + "年" + mView.getMonth() + "月";
		mShowDataDay = mView.getYear() + "年" + mView.getMonth() + "月"
				+ mView.mSelDay + "日";
		
		currentFlowFrgTitle = mShowDataMonth;
		
		Calendar vNow = Calendar.getInstance();//获取日历对象
		mCurrentDate = vNow.get(Calendar.YEAR)
				+ WorkDatePickerDialog.intToStr2(vNow.get(Calendar.MONTH) + 1)
				+ WorkDatePickerDialog.intToStr2(vNow.get(Calendar.DAY_OF_MONTH));//获取年、月、日
		
		ActionBar jabGetActionBar = getActivity().getActionBar();
		HomeCommonUtils.initLeftCustomActionBar(mContext, jabGetActionBar, currentFlowFrgTitle, true, onSelectDayClick, true, true);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		HomeCommonUtils.initLeftCustomActionBar(mContext, getActivity().getActionBar(), currentFlowFrgTitle, true, onSelectDayClick, true, true);
		if(mMenu!=null){
			mMenu.findItem(R.id.more).setIcon(R.drawable.people_contents);
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		this.mMenu = menu;
		mMenu.findItem(R.id.more).setIcon(R.drawable.people_contents);
	}
	
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
	

	/**日历状态下，选择ActionBar的天*/
	private OnClickListener onSelectDayClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			WorkDatePickerDialog datePicKDialog = new WorkDatePickerDialog(getActivity(), mCurrentDate);
			datePicKDialog.dateTimePicKDialog(0);

			datePicKDialog.setDayChangeListener(mDayChage);

			InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
			if (inputmanger.isActive()) {
				inputmanger.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);//隐藏输入法
			}

		}
	};
	
	/**日历状态下，ActionBar选择日期改变天的监听*/
	public OnDayChangeListener mDayChage = new OnDayChangeListener() {

		@Override
		public void onDayChagne(String outDay) {
			String vOldYearMonth = mCurrentDate.substring(0, 6);
			String vNewYearMonth = outDay.substring(0, 6);

			if (!vOldYearMonth.equals(vNewYearMonth)) {
				isButtonClick = true;
				currentMonth = outDay.substring(0, 4) + "-" + outDay.substring(4, 6);
				getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMONTHINFO);
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
			HomeCommonUtils.initLeftCustomActionBar(getActivity(), getActivity().getActionBar(), currentFlowFrgTitle, true,
					onSelectDayClick, true, true);
		}
	};
	
	class RecordAdapter extends BaseAdapter{

		private Context mContext;
		private List<RecordDetail> recorddetails = new ArrayList<RecordDetail>();
		
		public RecordAdapter(Context mContext){
			this.mContext = mContext;
		}
		
		public void setList(List<RecordDetail> recorddetails){
			this.recorddetails = recorddetails;
		}
		
		@Override
		public int getCount() {
			return recorddetails.size();
		}

		@Override
		public RecordDetail getItem(int position) {
			return recorddetails.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			RecordDetail recordDetail = recorddetails.get(position);
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.tongren_record_item, null);
				
				holder.addBegin_ll = (LinearLayout) convertView.findViewById(R.id.addBegin_ll);
				holder.beginTv = (TextView) convertView.findViewById(R.id.beginTv);
				holder.beginTimeTv = (TextView) convertView.findViewById(R.id.beginTimeTv);
				holder.recordBeginTimeTv = (TextView) convertView.findViewById(R.id.recordBeginTimeTv);
				holder.addressBeginTitleTv = (TextView) convertView.findViewById(R.id.addressBeginTitleTv);
				holder.addressBeginDetailTv = (TextView) convertView.findViewById(R.id.addressBeginDetailTv);

				holder.addEnd_ll = (LinearLayout) convertView.findViewById(R.id.addEnd_ll);
				holder.endTv = (TextView) convertView.findViewById(R.id.endTv);
				holder.endTimeTv = (TextView) convertView.findViewById(R.id.endTimeTv);
				holder.recordEndTimeTv = (TextView) convertView.findViewById(R.id.recordEndTimeTv);
				holder.addressEndTitleTv = (TextView) convertView.findViewById(R.id.addressEndTitleTv);
				holder.addressEndDetailTv = (TextView) convertView.findViewById(R.id.addressEndDetailTv);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			//上班
			holder.beginTimeTv.setText(startWorkTime);
			if(!TextUtils.isEmpty(recordDetail.getStart())){
				holder.recordBeginTimeTv.setText("已签到 "+TimeUtil.getDate(recordDetail.getStart(), "HH:mm"));
			}else{
				holder.recordBeginTimeTv.setText("");
			}
			if(!TextUtils.isEmpty(recordDetail.getExtend().getLonlatStart())){
				holder.addressBeginTitleTv.setText(recordDetail.getExtend().getLonlatStart().split(",")[0]);
				holder.addressBeginDetailTv.setText(recordDetail.getExtend().getLonlatStart().split(",")[1]);
			}else{
				holder.addressBeginTitleTv.setText("");
				holder.addressBeginDetailTv.setText("");
			}
			//下班
			holder.endTimeTv.setText(workTimeOut);
			if(!TextUtils.isEmpty(recordDetail.getEnd())){
				holder.recordEndTimeTv.setText("已签退 "+TimeUtil.getDate(recordDetail.getEnd(), "HH:mm"));
			}else{
				holder.recordEndTimeTv.setText("");
			}
			if(!TextUtils.isEmpty(recordDetail.getExtend().getLonlatEnd())){
				holder.addressEndTitleTv.setText(recordDetail.getExtend().getLonlatEnd().split(",")[0]);
				holder.addressEndDetailTv.setText(recordDetail.getExtend().getLonlatEnd().split(",")[1]);
			}else{
				holder.addressEndTitleTv.setText("");
				holder.addressEndDetailTv.setText("");
			}
			
			return convertView;
		}
		
		class ViewHolder{
			public LinearLayout addBegin_ll, addEnd_ll;
			public TextView beginTv, beginTimeTv, recordBeginTimeTv, addressBeginTitleTv, addressBeginDetailTv;
			public TextView endTv, endTimeTv, recordEndTimeTv, addressEndTitleTv, addressEndDetailTv;
		}
		
	}

	@Override
	public void bindData(int tag, Object object) {
		switch(tag){
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETRULE://考勤规则
			dismissLoadingDialog();
			if(object == null){
				showToast("尚未设置考勤规则，没有打卡记录");
				return;
			}
			RecordRule rule = (RecordRule) object;
			startWorkTime = rule.getStartWorkTime();
			workTimeOut = rule.getWorkTimeOut();

			getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMONTHINFO);//获取月打卡记录
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMONTHINFO:
			dismissLoadingDialog();
			if(object == null){
				recorddetails.clear();
				recordAdapter.setList(recorddetails);
				recordAdapter.notifyDataSetChanged();
				return;
			}
			Records records = (Records) object;
			recorddetails = records.getRecordDetail();
			recordAdapter.setList(recorddetails);
			recordAdapter.notifyDataSetChanged();
			break;
		}
		dismissLoadingDialog();
	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case EAPIConsts.handler.show_err:
				break;
			}
		}
	};

	private void getDataFromServer(int requestType) {
		showLoadingDialog();
		JSONObject jobj = new JSONObject();
		try {
			switch (requestType) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETRULE://考勤规则
				jobj.put("organizationId", oid);
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMONTHINFO://月打卡信息
				showLoadingDialog();
				jobj.put("organizationId", oid);
				jobj.put("userId", App.getUserID());
				jobj.put("month", currentMonth);
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		TongRenReqUtils.doRequestOrg(getActivity(), this, jobj, handler, requestType);
	}

	@Override
	public void onDayChagne(String outDay) {
		
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
		vStrMonth = vStrMonth.substring(vStrMonth.length() - 2,vStrMonth.length());

		HomeCommonUtils.initLeftCustomActionBar(getActivity(), getActivity().getActionBar(), currentFlowFrgTitle, true, onSelectDayClick, true, true);
		
		currentMonth = mView.getYear() + "-" + vStrMonth;
		if(!isButtonClick){//如果是点击的dialog设置的月份，不再次请求网络，滑动mCalendarView请求网络
			getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMONTHINFO);
		}else{
			isButtonClick = false;
		}
	}

	@Override
	public void onDayClick(Cell touchedCell, int row) {
		final Cell day = mView.getmTouchedCell();
		String vDate = day.getYear()
				+ WorkDatePickerDialog.intToStr2(day.getMonth())
				+ WorkDatePickerDialog.intToStr2(day.getDayOfMonth());
		Log.d("xmx", "onDayClick mCurrentDate:" + mCurrentDate);

		mCurrentDate = vDate;
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
		HomeCommonUtils.initLeftCustomActionBar(getActivity(), getActivity().getActionBar(), currentFlowFrgTitle, true, onSelectDayClick, true, true);
		isTakeBackDayClick = false;
		
		recordLv.post(new Runnable() {
			public void run() {
				if(recorddetails.size()>0){
					if(day.getDayOfMonth()>recorddetails.size()){
						temp_recorddetails.clear();
						recordAdapter.setList(temp_recorddetails);
						recordAdapter.notifyDataSetChanged();
					}else{
						temp_recorddetails.clear();
						temp_recorddetails.add(recorddetails.get(day.getDayOfMonth()-1));
						recordAdapter.setList(temp_recorddetails);
						recordAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}
	
	//滑动监听
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

	/**是否显示listview的顶部*/
	private boolean mShowListViewTop = false;
	/**是否有滑动动画*/
	private boolean mIsAnim = false;
	/**按下的X坐标*/
	private float lastX = 0;
	/**按下的Y坐标*/
	private float lastY = 0;
	/**是否向上滚动*/
	private boolean mIsTop = false;
	/**ListView布局的高*/
	private int mListViewLayoutHeight = 0;
	/**是否按下*/
	private boolean isDown = false;
	/**是否抬起*/
	private boolean isUp = false;
	private boolean mIsTakeBack = false;
	/**触摸事件的监听*/
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

			case MotionEvent.ACTION_DOWN://按下
				lastY = y;
				lastX = x;
				return false;
			case MotionEvent.ACTION_MOVE://移动
				float dY = Math.abs(y - lastY);
				float dX = Math.abs(x - lastX);
				boolean down = y > lastY ? true : false;
				lastY = y;
				lastX = x;
				isUp = dX < 8 && dY > 8 && !mIsTitleHide && !down && !mIsAnim;//移动的x轴小于8，y轴大于8，标题日期不隐藏，按下，滑动动画，日历状态下
				isDown = dX < 8 && dY > 8 && mIsTitleHide && down && !mIsAnim;
				if (isUp) {
					mCalendarView.setVisibility(View.VISIBLE);
					mShowListViewTop = true;
					// mCalendarLayout.setActionType(ActionType.TakeBack);
					View view = mCalendarLayout;
					float[] f = new float[2];
					f[0] = 0f;
					f[1] = -mCalendarLayout.getHeight();//整个日历的高度（负的）
					ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,
							"translationY", f);//动画
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
					if (mListViewLayoutHeight == 0)
						mListViewLayoutHeight = mLayout.getHeight();//
					LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							mListViewLayoutHeight + mCalendarLayout.getHeight()-mCalendarView.getHeight());
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
							getActivity().getActionBar(), currentFlowFrgTitle, true, onSelectDayClick, true, true);

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

					HomeCommonUtils.initLeftCustomActionBar(getActivity(),getActivity().getActionBar(), currentFlowFrgTitle,
							true, onSelectDayClick, true, true);

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
		public void onAnimationCancel(Animator arg0) { }

		@Override
		public void onAnimationEnd(Animator arg0) {

			if (mIsTakeBack) {

			} else {
				// mCalendarView.setVisibility(View.GONE);
			}

		}

		@Override
		public void onAnimationRepeat(Animator arg0) { }

		@Override
		public void onAnimationStart(Animator arg0) { }

	};

	Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {

		@Override
		public void onAnimationCancel(Animator arg0) {}

		@Override
		public void onAnimationEnd(Animator arg0) {
			if (mShowListViewTop) {
				Log.d("xmx", "show list top");
			}
			mIsAnim = false;
		}

		@Override
		public void onAnimationRepeat(Animator arg0) {}

		@Override
		public void onAnimationStart(Animator arg0) {}

	};
}
