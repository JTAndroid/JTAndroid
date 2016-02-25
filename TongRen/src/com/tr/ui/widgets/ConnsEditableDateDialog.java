package com.tr.ui.widgets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.tr.R;
import com.tr.model.model.PeopleImportantDate;
import com.tr.model.model.PeopleSelectTag;
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
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 人脉对话框(重要日期)
 * @author leon
 *
 */
public class ConnsEditableDateDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	private final int YearSpan = 100; // 时间跨度
	
	// 控件
	private WheelView tagWv; // 滚轮
	private WheelView yearWv; // 年
	private WheelView monthWv; // 月
	private WheelView dayWv; // 日
	private TextView cancelTv; // 取消
	private TextView okTv; // 确定
	private ImageView sendIv; // 确定添加
	private EditText customEt; // 自定义标签值
	private TextView customTv; // 创建自定义标签
	
	// 变量
	private Activity mContext;
	private CommonAdapter mAdapter;
	private DatetimeAdapter mYearAdapter;
	private DatetimeAdapter mMonthAdapter;
	private DatetimeAdapter mDayAdapter;
	private List<PeopleSelectTag> mListTag; // 标签列表
	private PeopleImportantDate mPeopleDate;
	private OnFinishListener mListener;
	private Calendar mCalendar; 
	
	
	public ConnsEditableDateDialog(Activity context,List<PeopleSelectTag> listTag, PeopleImportantDate peopleDate, OnFinishListener listener) {
		super(context,R.style.ConnsDialogTheme);
		setContentView(R.layout.widget_conns_editable_date_dialog);
		mContext = context;
		mPeopleDate = peopleDate;
		mListener = listener;
		mListTag = getDefaultTagList();
		if(listTag != null && listTag.size() > 0){
			mListTag.addAll(listTag);
		}
//		mPeopleDate = peopleDate;
		initDialogStyle();
		initVars();
		initControls();
	}
	
	// 初始化对话框样式
	@SuppressWarnings("deprecation")
	private void initDialogStyle(){
		getWindow().setWindowAnimations(R.style.ConnsDialogAnim);
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.width = mContext.getWindowManager().getDefaultDisplay().getWidth();
		wmlp.gravity = Gravity.BOTTOM ;
	}
	
	// 初始化变量
	private void initVars(){
		mAdapter = new CommonAdapter(mContext);
		mCalendar = Calendar.getInstance();
		mYearAdapter = new DatetimeAdapter(mContext, 0);
		mMonthAdapter = new DatetimeAdapter(mContext, 1);
		mDayAdapter = new DatetimeAdapter(mContext, 2);
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
		// 确定添加
		sendIv = (ImageView) findViewById(R.id.sendIv);
		sendIv.setOnClickListener(mClickListener);
		// 自定义标签
		customEt = (EditText) findViewById(R.id.customEt);
		customTv = (TextView) findViewById(R.id.customTv);
		customTv.setOnClickListener(mClickListener);
		// 滚轮
		tagWv = (WheelView) findViewById(R.id.tagWv);
		tagWv.setVisibleItems(3);
		tagWv.setViewAdapter(mAdapter);
		// 年
		yearWv = (WheelView) findViewById(R.id.yearWv);
		yearWv.setVisibleItems(3);
		yearWv.setViewAdapter(mYearAdapter);
		yearWv.setCurrentItem(mYearAdapter.getItemsCount() / 2);
		// 月
		monthWv = (WheelView) findViewById(R.id.monthWv);
		monthWv.setVisibleItems(3);
		monthWv.setViewAdapter(mMonthAdapter);
		monthWv.setCurrentItem(mCalendar.get(Calendar.MONTH));
		// 日
		dayWv = (WheelView) findViewById(R.id.dayWv);
		dayWv.setVisibleItems(3);
		dayWv.setViewAdapter(mDayAdapter);
		dayWv.setCurrentItem(mCalendar.get(Calendar.DAY_OF_MONTH)-1);

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
		
		if(mPeopleDate != null){ // 编辑
			
			if(mPeopleDate.typeTag != null 
					&& mPeopleDate.typeTag.name != null){
				
				for(int i = 0;i < mListTag.size(); i++){
					if(mPeopleDate.typeTag.name.equals(mListTag.get(i).name)){
						tagWv.setCurrentItem(i);
						break;
					}
				}
			}
			if(mPeopleDate.date != null
					&& mPeopleDate.date.length() > 0){
				
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date date = df.parse(mPeopleDate.date);
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(date.getTime());

					if(calendar.get(Calendar.YEAR) >= mCalendar.get(Calendar.YEAR) - YearSpan/2 
							&& calendar.get(Calendar.YEAR) <= mCalendar.get(Calendar.YEAR) + YearSpan/2){
						
						yearWv.setCurrentItem(mCalendar.get(Calendar.YEAR) + YearSpan/2 - calendar.get(Calendar.YEAR));
						monthWv.setCurrentItem(calendar.get(Calendar.MONTH));
						dayWv.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) -1);
					}
				} 
				catch (ParseException e) {
					e.printStackTrace();
				}
			}
			// 滚轮滑到指定项
			okTv.setVisibility(View.VISIBLE);
			sendIv.setVisibility(View.GONE);
		}
	}
	
	// 获取默认标签列表
	private List<PeopleSelectTag> getDefaultTagList() {
		List<PeopleSelectTag> listTag = new ArrayList<PeopleSelectTag>();
		String [] data = mContext.getResources().getStringArray(R.array.conns_keydates);
		for (int i = 0; i < data.length; i++) {
			PeopleSelectTag peopleSelectTag=new PeopleSelectTag(PeopleSelectTag.type_default,(i + 1) + "", data[i]);
			listTag.add(peopleSelectTag);
		}
		return listTag;
	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cancelTv: // 取消
				ConnsEditableDateDialog.this.dismiss();
				break;
			case R.id.okTv:  // 完成
			case R.id.sendIv:
				if(mPeopleDate == null){
					mPeopleDate = new PeopleImportantDate();
				}
				if (mListener != null) {
					mPeopleDate.typeTag =(mListTag.get(tagWv.getCurrentItem()));
					mPeopleDate.date = (((mCalendar.get(Calendar.YEAR) + YearSpan/2) - yearWv.getCurrentItem())
							+ "-" + String.format("%02d", monthWv.getCurrentItem() + 1)
							+ "-" + String.format("%02d", dayWv.getCurrentItem() + 1));
					mListener.onFinish(mPeopleDate);
				}
				ConnsEditableDateDialog.this.dismiss();
				break;
			case R.id.customTv: // 自定义
				String tagName = customEt.getText().toString();
				// 判断标签是否为空
				if(TextUtils.isEmpty(tagName)){
					EUtil.showToast(mContext, "请输入标签名");
				}
				else if(tagName.length() > 5){
					EUtil.showToast(mContext, "标签名长度不能超过5个字符");
				}
				else{
					// 判断标签是否已经存在
					for(PeopleSelectTag tag : mListTag){
						if(tag.name.equalsIgnoreCase(tagName)){
							EUtil.showToast(mContext,"标签名已存在，请使用其它标签");
							return;
						}
					}
					// 创建标签
					PeopleSelectTag tag = new PeopleSelectTag(PeopleSelectTag.type_custom,"",tagName);
					// 显示到标签列表
					mListTag.add(tag);
					tagWv.setCurrentItem(mListTag.size() - 1);
					customEt.setText("");
				}
				break;
			}
		}
	};
	
	private class CommonAdapter extends AbstractWheelTextAdapter {
        
        protected CommonAdapter(Context context) {
            super(context, R.layout.widget_wheel_item, R.id.itemTv);
        }
        
        @Override
        public int getItemsCount() {
            return mListTag.size();
        }
        
        @Override
        protected CharSequence getItemText(int position) {
            return mListTag.get(position).name;
        }
    }
	
	private class DatetimeAdapter extends AbstractWheelTextAdapter {
        
		private List<String> mListTime = new ArrayList<String>();
		private int mType; // 0-年,1-月,2-日
		
		
        protected DatetimeAdapter(Context context,int type) {
            super(context, R.layout.widget_wheel_item, R.id.itemTv);
            mType = type;
            switch(mType){
            case 0:
            	for (int i = YearSpan; i >= 0; i--) {
            		mListTime.add((mCalendar.get(Calendar.YEAR) - YearSpan/2 + i) + "年");
        		}
            	break;
            case 1:
            	for (int i = 0; i < 12; i++) {
            		mListTime.add((i + 1) + "月");
        		}
            	break;
            case 2:
        		for (int i = 0; i < EUtil.getDaysOfMonth(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1); i++) {
        			mListTime.add((i + 1) + "日");
        		}
            	break;
            }
        }
        
        public void update(List<String> listTag){
  
        	if(listTag != null){
        		mListTime = listTag;
        	}
        	notifyDataChangedEvent();     
        	notifyDataInvalidatedEvent();
        }
        
        @Override
        public int getItemsCount() {
            return mListTime.size();
        }
        
        @Override
        protected CharSequence getItemText(int position) {
            return mListTime.get(position);
        }
    }
	
	
	public interface OnFinishListener{
		public void onFinish(PeopleImportantDate peopleDate);
	}

}
