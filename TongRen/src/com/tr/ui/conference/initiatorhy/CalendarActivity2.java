package com.tr.ui.conference.initiatorhy;



import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.conference.MCalendarSelectDateTime;
import com.tr.ui.adapter.conference.calender.CalendarAdapter;
import com.tr.ui.adapter.conference.calender.CalendarAdapter.CalendarOnDateSelectListener;
import com.tr.ui.adapter.conference.calender.ListViewCalendarTimeAdapter;
import com.tr.ui.conference.common.BaseActivity;
import com.tr.ui.widgets.EditOrDeletePopupWindow;
import com.tr.ui.widgets.EditOrDeletePopupWindow.OnMeetingOptItemClickListener;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.work.CalendarLayout;
import com.utils.http.IBindData;
import com.utils.time.Util;

public class CalendarActivity2 extends BaseActivity implements IBindData, OnGestureListener, CalendarOnDateSelectListener{
	public final static int TYPE_DATE_MULIT_SELECT = 0;
	public final static int TYPE_DATE_SINGLE_SELECT = 1;
	
	/******	旧日历	start	******/
	/**	当前日期题目	*/
	private TextView curDateText;
	private GridView calendarGridView;
	private CalendarAdapter calenderAdp;
	
	/******	旧日历	end	******/
	
	
	/******	新日历	start	******/
	/**	当前日期题目	*/
	private TextView yearMonthTitleTv;
	/**	日历控件	*/
	private CalendarLayout calendarLayout;
	
	/******	新日历	end	******/
	
	
	
	/**	下面的时间列表listview	*/
	private ListView timeListView;
	private ListViewCalendarTimeAdapter timeListViewAdp;
//	private GestureDetector gestureDetector = null;
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int selectDateType = 0;
	
	public Context context;
	private TextView title;
	
	public Context getContext() {
		return context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.hy_activity_calendar);
		jumpMonth = 0;
//		gestureDetector = new GestureDetector(this);
		jumpMonth = 0;
		jumpYear = 0;
		findAndInitViews();
	}
	
	private void findAndInitViews(){
		Bundle b = getIntent().getExtras();
		if(b != null){
			selectDateType = b.getInt(Util.IK_VALUE);
		}
		findAndInitTitleViews();
		findAndInitDateGridViews();
		findAndInitTopDateViews();
		findAndInitTimeListViews();
	}
	private void findAndInitTitleViews(){
		LinearLayout backBtn = (LinearLayout)findViewById(R.id.hy_layoutTitle_backBtn);
		title = (TextView)findViewById(R.id.hy_layoutTitle_title);
		TextView rightTextBtn = (TextView)findViewById(R.id.hy_layoutTitle_rightTextBtn);
		title.setText("选择时间");
		rightTextBtn.setText("完成");
		backBtn.setOnClickListener(new MyOnClickListener());
		rightTextBtn.setOnClickListener(new MyOnClickListener());
	}
	private void findAndInitTopDateViews(){
		ImageView arrowLeft = (ImageView)findViewById(R.id.hy_actCalendar_arrowLeft_btn);
		ImageView arrowRight = (ImageView)findViewById(R.id.hy_actCalendar_arrowRight_btn);
		curDateText = (TextView)findViewById(R.id.hy_actCalendar_arrowRight_dateText);
		curDateText.setText(getCurDate());
		arrowLeft.setOnClickListener(new MyOnClickListener());
		arrowRight.setOnClickListener(new MyOnClickListener());
	}
	private void findAndInitDateGridViews(){
		calendarGridView = (GridView)findViewById(R.id.hy_actCalendar_gridview);
		calendarGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(selectDateType == 0){
			calenderAdp = new CalendarAdapter(this, selectDateType, InitiatorDataCache.getInstance().timeSelectetedList);
		}else{
			calenderAdp = new CalendarAdapter(this, selectDateType, InitiatorDataCache.getInstance().dateSelectetedTempList);
		}
		calendarGridView.setAdapter(calenderAdp);
//		calendarGridView.setOnTouchListener(new OnTouchListener(){
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				return CalendarActivity.this.gestureDetector.onTouchEvent(event);
//			}});
//		calendarGridView.setOnItemClickListener(new MyOnItemClickListener());
	}
	/**	初始化新日历控件	*/
	private void findAndInitCalendarViews(){
		yearMonthTitleTv = (TextView) findViewById(R.id.yearMonthTitleTv);
		calendarLayout = (CalendarLayout) findViewById(R.id.calendarLayout);
		
		
		
		
		
		
	}
	private void findAndInitTimeListViews(){
		timeListView = (ListView)findViewById(R.id.hy_actCalendar_time_listView);
		timeListViewAdp = new ListViewCalendarTimeAdapter(this,selectDateType);
		timeListView.setAdapter(timeListViewAdp);
		timeListViewAdp.update(calenderAdp.getDTList());
		timeListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				
				
				EditOrDeletePopupWindow meetingEditOrDeletePopupWindow = new EditOrDeletePopupWindow(getContext());
				meetingEditOrDeletePopupWindow.setOnItemClickListener(new OnMeetingOptItemClickListener() {
					
					@Override
					public void edit(String editStr) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void delete(String deleteStr) {
						// TODO Auto-generated method stub
						if(selectDateType == 0){
							InitiatorDataCache.getInstance().timeSelectetedList.remove(position);
						}else{
							InitiatorDataCache.getInstance().dateSelectetedTempList.remove(position);
						}
						
						timeListViewAdp.notifyDataSetChanged();
						calenderAdp.notifyDataSetChanged();
						
					}
				});
				meetingEditOrDeletePopupWindow.showDeleteButton();
				meetingEditOrDeletePopupWindow.showAsDropDown(title);
				
				
				return false;
			}
			
		});
	}
	private String getCurDate(){
		StringBuffer date = new StringBuffer();
		date.append(calenderAdp.getShowYear()).append("年").append(
				calenderAdp.getShowMonth()).append("月");
		return date.toString();
	}
	private void flingCalendar(boolean isAdd){
		if (isAdd) {
            //像左滑动
			jumpMonth++;     //下一个月
			calenderAdp.updateJump(jumpMonth, jumpYear);
			curDateText.setText(getCurDate());
//			jumpMonth = 0;
		} else {
            //向右滑动
			jumpMonth--;     //上一个月
			calenderAdp.updateJump(jumpMonth, jumpYear);
			curDateText.setText(getCurDate());
//			jumpMonth = 0;
		}
	}
	public void finishActivity(){
		this.finish();
	}
	private class MyOnClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			try {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				boolean isOpen = imm.isActive();
				if (isOpen) {
					imm.hideSoftInputFromWindow(CalendarActivity2.this.getCurrentFocus().getWindowToken(), 0);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			int clickId = v.getId();
			switch (clickId) {
				case R.id.hy_layoutTitle_backBtn:{
					finishActivity();
				}break;
				
				case R.id.hy_layoutTitle_rightTextBtn:{
					
					Bundle b = new Bundle();
					b.putBoolean(Util.IK_VALUE, true);
					if(selectDateType == 0){
						// 先判断是否有重复日期时间
						
						HashMap<String, Object> hm = isHasRepetition();
						
						if(!(Boolean) hm.get("result")){
							Util.activitySetResult(CalendarActivity2.this, InitiatorHYActivity.class, b);
						}
						else {
							new MessageDialog((Activity) getContext(), hm.get("repetitionDate") + "存在相同时间请修改", new MessageDialog.OnDialogFinishListener() {
	
								@Override
								public void onFinish(String content) {
									// TODO Auto-generated method stub
	
								}

								@Override
								public void onCancel(String content) {
									// TODO Auto-generated method stub
									
								}
							}).goneCancleButton().show();
							return;
						}
						
					}else{
						if (!Util.isNull(InitiatorDataCache.getInstance().dateSelectetedTempList)) {
							Util.activitySetResult(CalendarActivity2.this, MeetingSpeakerListActivity.class, b);
						}
					}
					finishActivity();
				}
				break;
				
				case R.id.hy_actCalendar_arrowLeft_btn:{
					flingCalendar(false);
				}break;
				case R.id.hy_actCalendar_arrowRight_btn:{
					flingCalendar(true);
				}break;
			}
		}
	}
	
	/**
	 * 检查是否有重复日期和时间
	 * 
	 *  * @return hm 是否重复  和 重复日期
	 */
	public HashMap<String, Object> isHasRepetition(){
		HashMap<String, Object> hm = new HashMap<String, Object>();
		boolean b = false;
		String repetitionDate = null;
		ArrayList<String> templist = new ArrayList<String>();
		for(MCalendarSelectDateTime time : InitiatorDataCache.getInstance().timeSelectetedList){
			String formatDate = IniviteUtil.formatDate(time);
			if (!templist.contains(formatDate)) {
				templist.add(formatDate);
			}
			else {
				b = true;
				repetitionDate =  time.year + "年" + time.month + "月" + time.day + "日" ;
				break ;
			}
		}
		hm.put("result", b);
		hm.put("repetitionDate", repetitionDate);
		return hm;
	}
	
	
	
	private class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
		}
	}
	@Override
	public void bindData(int tag, Object object) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onDestroy() {
		calenderAdp.cancelToast();
		super.onDestroy();
		finishActivity();
	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		return this.gestureDetector.onTouchEvent(event);
//	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > 40) {
            //像左滑动
			flingCalendar(true);
			return true;
		} else if (e1.getX() - e2.getX() < -40) {
            //向右滑动
			flingCalendar(false);
			return true;
		}
		return false;
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//ListViewCalendarTimeAdapter lc= new ListViewCalendarTimeAdapter(CalendarActivity.this,selectDateType);
		MCalendarSelectDateTime md=(MCalendarSelectDateTime) data.getSerializableExtra("md");
		//ArrayList<MCalendarSelectDateTime> mettingListDT =(ArrayList<MCalendarSelectDateTime>) data.getSerializableExtra("mettingListDT");
		int position=(Integer) data.getSerializableExtra("position");
		switch (requestCode) {  
        case 110:  
        	timeListViewAdp.modifyTime(md.startHour,md.startMinute,md.endHour,md.endMinute,position/*,mettingListDT*/);
        	timeListViewAdp.update();
            break;  

		}
}

	@Override
	public void OnDateSelectListener(
			ArrayList<MCalendarSelectDateTime> selectedDTList) {
		if(timeListViewAdp != null){
			timeListViewAdp.update(selectedDTList);
		}
	}
}
