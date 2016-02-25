package com.tr.ui.widgets.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tr.R;
import com.tr.model.obj.EduExperience;
import com.tr.ui.widgets.time.NumericWheelAdapter;
import com.tr.ui.widgets.time.OnWheelChangedListener;
import com.tr.ui.widgets.time.WheelView;
import com.utils.log.KeelLog;
import com.utils.string.StringUtils;

public class SelectBirthday extends PopupWindow implements OnClickListener {

	private Activity mContext;
	private View mMenuView;
	private ViewFlipper viewfipper;
	private Button btn_submit, btn_cancel;
	private String age;//当前日期
	private DateNumericAdapter monthAdapter, dayAdapter, yearAdapter, hourAdapter, minuteAdapter;
	private WheelView year, month, day,hour,minute;
	private int mCurYear = 80, mCurMonth = 5, mCurDay = 14, mCurHour = 23, mMinute = 59;
	private String[] dateType;
	private SelectBirthdayListener mCallback;
	
	public interface SelectBirthdayListener{
		public void onSelectBirthdayListener(String time);
	};
	
	int type=0;
	public final static int type_all=1;
	public final static int type_ymd=2;
	Activity context;
	public SelectBirthday(Activity context, SelectBirthdayListener callback,int type) {
		super(context);
		this.context=context;
		this.type=type;
		mCallback = callback;
		mContext = context;
		this.age = "2012-9-25";
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.birthday, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		year = (WheelView) mMenuView.findViewById(R.id.year);
		month = (WheelView) mMenuView.findViewById(R.id.month);
		day = (WheelView) mMenuView.findViewById(R.id.day);
		hour = (WheelView) mMenuView.findViewById(R.id.hour);
		minute = (WheelView) mMenuView.findViewById(R.id.minute);
		
		if(type_ymd==type){
			hour.setVisibility(View.GONE);
			minute.setVisibility(View.GONE);
		}
		
		btn_submit = (Button) mMenuView.findViewById(R.id.submit);
		btn_cancel = (Button) mMenuView.findViewById(R.id.cancel);
		btn_submit.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		Calendar calendar = Calendar.getInstance();
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day,hour,minute);
			}
		};
		int curYear = calendar.get(Calendar.YEAR);
		if (age != null && age.contains("-")) {
			String str[] = age.split("-");
			mCurYear = 100 - (curYear - Integer.parseInt(str[0]));
			mCurMonth = Integer.parseInt(str[1]) - 1;
			mCurDay = Integer.parseInt(str[2]) - 1;
			;
		}
		dateType = mContext.getResources().getStringArray(R.array.date); 
		
		//minute
		minuteAdapter = new DateNumericAdapter(context, 0, 59, 5);
		minuteAdapter.setTextType(dateType[4]);
		minute.setViewAdapter(minuteAdapter);
		minute.setCurrentItem(mMinute);
		minute.addChangingListener(listener);		
		//hour
		hourAdapter = new DateNumericAdapter(context, 0, 23, 5);
		hourAdapter.setTextType(dateType[3]);
		hour.setViewAdapter(hourAdapter);
		hour.setCurrentItem(mCurHour);
		hour.addChangingListener(listener);
		//month
		monthAdapter = new DateNumericAdapter(context, 1, 12, 5);
		monthAdapter.setTextType(dateType[1]);
		month.setViewAdapter(monthAdapter);
		month.setCurrentItem(mCurMonth);
		month.addChangingListener(listener);
		// year
		yearAdapter = new DateNumericAdapter(context, curYear - 100, curYear+100,
				100 - 20);
		yearAdapter.setTextType(dateType[0]);
		year.setViewAdapter(yearAdapter);
		year.setCurrentItem(mCurYear);
		year.addChangingListener(listener);
		// day
		updateDays(year, month, day,hour,minute);
		day.setCurrentItem(mCurDay);
		updateDays(year, month, day,hour,minute);
		day.addChangingListener(listener);

		viewfipper.addView(mMenuView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x88000000);
		this.setBackgroundDrawable(dw);
		this.update();

	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		viewfipper.startFlipping();
	}


	private void updateDays(WheelView year, WheelView month, WheelView day, WheelView hour, WheelView minute) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());
		//根据月份设置当月天数
		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		dayAdapter = new DateNumericAdapter(mContext, 1, maxDays,
				calendar.get(Calendar.DAY_OF_MONTH) - 1);
		dayAdapter.setTextType(dateType[2]);
		day.setViewAdapter(dayAdapter);
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
		//计算年份
		int years = calendar.get(Calendar.YEAR) - 100;
		calendar.set(Calendar.HOUR, hour.getCurrentItem());
		calendar.set(Calendar.MINUTE, minute.getCurrentItem());
		if(type_all==type){
			age = years + "-" + (month.getCurrentItem() + 1) + "-"
					+ (day.getCurrentItem() + 1) + " "+(hour.getCurrentItem() ) + ":"+(minute.getCurrentItem() )+":00";
		}else {
			int tempYears=years;
			age = tempYears + "-" + (month.getCurrentItem() + 1) + "-"
					+ (day.getCurrentItem() + 1) ;
		}
		Log.v("4444", age);
	}

	/**
	 * Adapter for numeric wheels. Highlights the current value.
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue,
				int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(24);
		}

		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			// view.setTypeface(Typeface.SANS_SERIF);
			view.setTextSize(14);
		}

		public CharSequence getItemText(int index) {
			currentItem = index;
			return super.getItemText(index);
		}

	}

	public void onClick(View v) {
		if(v.getId() == btn_submit.getId()){
//			//完成, 选择了时间
//			
//			mCurYear = year.getCurrentItem();
//			mCurMonth = month.getCurrentItem();
//			mCurDay = day.getCurrentItem();
//			
//			mCurHour = hour.getCurrentItem();
//			mMinute = minute.getCurrentItem();
//			String curTime = mCurYear + "-" + mCurMonth + "-" + mCurDay + " " + mCurHour + ":" +mMinute + ":00";
//			KeelLog.d(curTime + ":");
			mCallback.onSelectBirthdayListener(age);
		}
		this.dismiss();
	}
	
	public void initTime(){
		Time time = new Time("GMT+8");  
		int year = time.year;   
        int month = time.month;   
        int day = time.monthDay;   
        int minute = time.minute;   
        int hour = time.hour;   
        int sec = time.second;
	    String data=year+"-"+month+"-"+day+" "+hour+":"+minute+":"+sec;
	    showTime(data);
	}
	
	public void showTime(String time){
		if(StringUtils.isEmpty(time)||time.equals(EduExperience.today)){
			SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd hh:mm:ss");     
			time  =   sDateFormat.format(new   java.util.Date()); 
		}
		//12-12-12
		//12-12-12-12-12
			int mCurYear=0,mCurMonth=0, mCurDay=0, mCurHour=0, mMinute=0;
			String starttimes[]=time.split(" ");
			if(starttimes.length==1){
				String starttimes1[]=starttimes[0].split("-");
				mCurYear=Integer.parseInt(starttimes1[0]);
				mCurMonth=Integer.parseInt(starttimes1[1]);
				mCurDay=Integer.parseInt(starttimes1[2]);
			}
			if(starttimes.length==2){
				String starttimes1[]=starttimes[0].split("-");
				mCurYear=Integer.parseInt(starttimes1[0]);
				mCurMonth=Integer.parseInt(starttimes1[1]);
				mCurDay=Integer.parseInt(starttimes1[2]);
				
				String starttimes2[]=starttimes[1].split(":");
				mCurHour=Integer.parseInt(starttimes2[0]);
				mMinute=Integer.parseInt(starttimes2[1]);
			}
				minute.setCurrentItem(mMinute);
				//hour
				hour.setCurrentItem(mCurHour);
				//month
				month.setCurrentItem(mCurMonth-1);
				// year
				year.setCurrentItem(mCurYear);
				// day
				day.setCurrentItem(mCurDay-1);
				updateDays(year, month, day,hour,minute);
	}
	/**至对比 1200-12-12 的时间格式*/
	public static boolean isTimeOk(String starttime ,String endTime){
		if(StringUtils.isEmpty(starttime)||StringUtils.isEmpty(endTime)||endTime.equals(EduExperience.today)){
			return true;
		}
		String starttimes[]=starttime.split("-");
		String endTimes[]=endTime.split("-");
		for(int i=0;i<starttimes.length;i++){
			int a=Integer.parseInt(starttimes[i]);
			int b=Integer.parseInt(endTimes[i]);
			if(a<b){
				return true;
			}else if(a==b){
				continue;
			}else{
				return false;
			}
		}
		return true;
	}
	

}
