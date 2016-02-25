package com.tr.ui.widgets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.tr.R;
import com.tr.model.model.PeopleName;
import com.tr.model.obj.JTRegion;
import com.tr.ui.widgets.time.AbstractWheelTextAdapter;
import com.tr.ui.widgets.time.OnWheelChangedListener;
import com.tr.ui.widgets.time.WheelView;
import com.utils.common.EUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 人脉对话框(时间)
 * @author leon
 *
 */
public class ConnsDatetimeDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	
	private final int YearSpan = 100; // 时间跨度
	
	// 控件
	private TextView okTv; // 确定
	private TextView cancelTv; // 取消
	private WheelView yearWv; // 年
	private WheelView monthWv; // 月
	private WheelView dayWv; // 日
	private WheelView hourWv; // 时
	private WheelView minuteWv; // 分
	
	// 变量
	private Activity mContext;
	private Calendar mCalendar;
	private DatetimeAdapter mYearAdapter;
	private DatetimeAdapter mMonthAdapter;
	private DatetimeAdapter mDayAdapter;
	private DatetimeAdapter mHourAdapter;
	private DatetimeAdapter mMinuteAdapter;
	private OnFinishListener mListener;
	private String mDatetime;
	private boolean mShowTime;
	public final static String startTime="开始时间";
	public final static String endTime="结束时间";
	private View mView;
	
	
	public ConnsDatetimeDialog(Activity context, View view, String datetime,boolean showTime, OnFinishListener listener) {
		super(context,R.style.ConnsDialogTheme);
		setContentView(R.layout.widget_conns_datetime_dialog);
		mContext = context;
		mView = view;
		mListener = listener;
		mDatetime = datetime;
		mShowTime = showTime;
		initDialogStyle();
		initVars();
		initControls();
	}
	
	// 初始化对话框样式
	private void initDialogStyle(){
		getWindow().setWindowAnimations(R.style.ConnsDialogAnim);
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.width = mContext.getWindowManager().getDefaultDisplay().getWidth();
		wmlp.gravity = Gravity.BOTTOM ;
	}
	
	// 初始化变量
	private void initVars(){
		mCalendar = Calendar.getInstance();
	}
	
	// 初始化控件
	@SuppressLint("SimpleDateFormat")
	private void initControls(){
		// 取消
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(mClickListener);
		// 确定
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(mClickListener);
		// 年
		yearWv = (WheelView) findViewById(R.id.yearWv);
		yearWv.setVisibleItems(3);
		mYearAdapter = new DatetimeAdapter(mContext, 0);
		yearWv.setViewAdapter(mYearAdapter);
		yearWv.setCurrentItem(mYearAdapter.getItemsCount() / 2);
		// 月
		monthWv = (WheelView) findViewById(R.id.monthWv);
		monthWv.setVisibleItems(3);
		mMonthAdapter = new DatetimeAdapter(mContext, 1);
		monthWv.setViewAdapter(mMonthAdapter);
		monthWv.setCurrentItem(mCalendar.get(Calendar.MONTH));
		// 日
		dayWv = (WheelView) findViewById(R.id.dayWv);
		dayWv.setVisibleItems(3);
		mDayAdapter = new DatetimeAdapter(mContext, 2);
		dayWv.setViewAdapter(mDayAdapter);
		dayWv.setCurrentItem(mCalendar.get(Calendar.DAY_OF_MONTH) - 1);
		// 时
		hourWv = (WheelView) findViewById(R.id.hourWv);
		hourWv.setVisibility(3);
		mHourAdapter = new DatetimeAdapter(mContext, 3);
		hourWv.setViewAdapter(mHourAdapter);
		hourWv.setCurrentItem(mCalendar.get(Calendar.HOUR_OF_DAY));
		if(mShowTime){
			hourWv.setVisibility(View.VISIBLE);
		}
		else{
			hourWv.setVisibility(View.GONE);
		}
		// 分
		minuteWv = (WheelView) findViewById(R.id.minuteWv);
		minuteWv.setVisibleItems(3);
		mMinuteAdapter = new DatetimeAdapter(mContext, 4);
		minuteWv.setViewAdapter(mMinuteAdapter);
		minuteWv.setCurrentItem(mCalendar.get(Calendar.MINUTE));
		if(mShowTime){
			minuteWv.setVisibility(View.VISIBLE);
		}
		else{
			minuteWv.setVisibility(View.GONE);
		}
		
		yearWv.addChangingListener(new OnWheelChangedListener(){

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				
				List<String> listTag = new ArrayList<String>();
				for(int i = 0; i < EUtil.getDaysOfMonth(mCalendar.get(Calendar.YEAR) + (newValue - YearSpan/2), monthWv.getCurrentItem() + 1); i++){
					listTag.add((i + 1) + "日");
				}
				mDayAdapter.update(listTag);
				if(dayWv.getCurrentItem() >= mDayAdapter.getItemsCount()){
					dayWv.setCurrentItem(mDayAdapter.getItemsCount() - 1);
				}
			}
		});
		monthWv.addChangingListener(new OnWheelChangedListener(){

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				
				List<String> listTag = new ArrayList<String>();
				for(int i = 0; i < EUtil.getDaysOfMonth(mCalendar.get(Calendar.YEAR) + (yearWv.getCurrentItem() - YearSpan/2), newValue + 1); i++){
					listTag.add((i + 1) + "日");
				}
				mDayAdapter.update(listTag);
				if(dayWv.getCurrentItem() >= mDayAdapter.getItemsCount()){
					dayWv.setCurrentItem(mDayAdapter.getItemsCount() - 1);
				}
			}
		});
		
		if(mDatetime != null && !TextUtils.isEmpty(mDatetime)){
			if(mDatetime.equals(startTime)||mDatetime.equals(endTime)){
				
			}else{
				try {
					String format = mShowTime ? "yyyy-MM-dd kk:mm" : "yyyy-MM-dd";
					SimpleDateFormat df = new SimpleDateFormat(format);
					Date date = df.parse(mDatetime);
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(date.getTime());

					if(calendar.get(Calendar.YEAR) >= mCalendar.get(Calendar.YEAR) - YearSpan/2 
							&& calendar.get(Calendar.YEAR) <= mCalendar.get(Calendar.YEAR) + YearSpan/2){
						
						yearWv.setCurrentItem(mCalendar.get(Calendar.YEAR) + YearSpan/2 - calendar.get(Calendar.YEAR));
						monthWv.setCurrentItem(calendar.get(Calendar.MONTH));
						dayWv.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) -1);
						
						if(mShowTime){
							hourWv.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
							minuteWv.setCurrentItem(calendar.get(Calendar.MINUTE));
						}
					}
				} 
				catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cancelTv: // 取消
				ConnsDatetimeDialog.this.dismiss();
				break;
			case R.id.okTv: // 确定
				if(mListener != null){
					if(!mShowTime){
						mDatetime = ((mCalendar.get(Calendar.YEAR) + YearSpan/2) - yearWv.getCurrentItem())
								+ "-" + String.format("%02d", monthWv.getCurrentItem() + 1)
								+ "-" + String.format("%02d", dayWv.getCurrentItem() + 1);
					}
					else{
						mDatetime = ((mCalendar.get(Calendar.YEAR) + YearSpan/2) - yearWv.getCurrentItem())
								+ "-" + String.format("%02d", monthWv.getCurrentItem() + 1)
								+ "-" + String.format("%02d", dayWv.getCurrentItem() + 1)
								+ " " + String.format("%02d", hourWv.getCurrentItem() + 1)
								+ ":" + String.format("%02d", minuteWv.getCurrentItem() + 1);
					}
					mListener.onFinish(mView , mDatetime);
				}
				ConnsDatetimeDialog.this.dismiss();
				break;
			}
		}
	};
	
	private class DatetimeAdapter extends AbstractWheelTextAdapter {
        
		private List<String> mListTag = new ArrayList<String>();
		private int mType; // 0-年,1-月,2-日,3-时,4-分
		
        protected DatetimeAdapter(Context context,int type) {
            super(context, R.layout.widget_wheel_item, R.id.itemTv);
            mType = type;
            switch(mType){
            case 0:
            	for (int i = YearSpan; i >= 0; i--) {
            		mListTag.add((mCalendar.get(Calendar.YEAR) - YearSpan/2 + i) + "年");
        		}
            	break;
            case 1:
            	for (int i = 0; i < 12; i++) {
					mListTag.add((i + 1) + "月");
        		}
            	break;
            case 2:
        		for (int i = 0; i < EUtil.getDaysOfMonth(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1); i++) {
					mListTag.add((i + 1) + "日");
        		}
            	break;
            case 3:
            	for (int i = 0; i < 24; i++) {
        			mListTag.add(i + "时");
        		}
            	break;
            case 4:
            	for (int i = 0; i < 60; i++) {
        			mListTag.add(i + "分");
        		}
            	break;
            }
        }
        
        public void update(List<String> listTag){
  
        	if(listTag != null){
        		mListTag = listTag;
        	}
        	notifyDataChangedEvent();     
        	notifyDataInvalidatedEvent();
        }
        
        @Override
        public int getItemsCount() {
            return mListTag.size();
        }
        
        @Override
        protected CharSequence getItemText(int position) {
            return mListTag.get(position);
        }
    }
	
	
	public interface OnFinishListener{
		public void onFinish(View view, String datetime);
	}


}
