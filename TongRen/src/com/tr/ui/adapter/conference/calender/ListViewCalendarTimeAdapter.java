package com.tr.ui.adapter.conference.calender;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tr.R;
import com.tr.model.conference.MCalendarSelectDateTime;
import com.tr.ui.conference.initiatorhy.CalendarTimeSelectionActivity;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.tr.ui.conference.initiatorhy.IniviteUtil;
import com.utils.time.Util;

public class ListViewCalendarTimeAdapter extends BaseAdapter {
	private Context context;
	private List<MCalendarSelectDateTime> dataList;
//	private DatePickerDialog datePickDlg;
	private MyTimePickerDialog timePickDlg;
	private Toast toast;
	private MCalendarSelectDateTime sdt;
	private int selectType;
	
	public ListViewCalendarTimeAdapter(Context context,int selectType){
		this.context = context;
		this.selectType = selectType;
		this.dataList = InitiatorDataCache.getInstance().dateSelectetedTempList; 
		toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
	}
	public void update(List<MCalendarSelectDateTime> dataList){
		this.dataList = dataList;
		notifyDataSetChanged();
	}
	public void update(){
		notifyDataSetChanged();
	}
	public void clear(){
		this.dataList.clear();
	}
	private void setHolderView(HolderView holderView, int position){
		sdt = dataList.get(position);
		String date = sdt.month + "月" + sdt.day  + "日";
		holderView.selectDate.setText(date);
		sdt.startTime = sdt.startHour+":"+sdt.startMinute;
		if ((sdt.startHour+"").length()==1) {
			sdt.startTime = "0" + sdt.startHour + ":"
					+ sdt.startMinute;
		}
		if ((sdt.startMinute + "").length() == 1) {
			sdt.startTime = sdt.startHour + ":" + "0"
					+ sdt.startMinute;
		}
		if ((sdt.startHour + "").length() == 1
		&& (sdt.startMinute + "").length() == 1) {
			sdt.startTime = "0" + sdt.startHour + ":" + "0"
				+ sdt.startMinute;
		}
		
		sdt.endTime = sdt.endHour + ":" + sdt.endMinute;
		
		if ((sdt.endHour + "").length() == 1) {
			sdt.endTime = "0" + sdt.endHour + ":" + sdt.endMinute;
		}
		if ((sdt.endMinute + "").length() == 1) {
			sdt.endTime = sdt.endHour + ":" + "0" + sdt.endMinute;
		}
		if ((sdt.endHour + "").length() == 1
			&& (sdt.endMinute + "").length() == 1) {
			sdt.endTime = "0" + sdt.endHour + ":" + "0"
				+ sdt.endMinute;
		}
		if (0 <= sdt.endHour && sdt.endHour < 2) {
			sdt.endTime = 23 + ":" + 59;
		}
		
		holderView.startTime.setText(sdt.startTime);
		holderView.endTime.setText(sdt.endTime);
		holderView.startTime.setOnClickListener(new MyOnClickListener(position));
		holderView.endTime.setOnClickListener(new MyOnClickListener(position));
	}
	private void setDateTime(final View v, final int positin, final int flag){
		Calendar calendar = Calendar.getInstance();
		if (flag==1) {//开始
			calendar.setTimeInMillis(sdt.startDate);
		}else if(flag==2){//结束
			calendar.setTimeInMillis(sdt.endDate);
		}
		timePickDlg = new MyTimePickerDialog(context, 
				new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub;
					String time = hourOfDay + ":" + minute;
					if ((hourOfDay+"").length()==1) {
						time = "0"+hourOfDay + ":" + minute;
					}
					if ((minute+"").length()==1) {
						time = hourOfDay + ":" +"0"+ minute;
					}
					if ((hourOfDay+"").length()==1&&(minute+"").length()==1) {
						time = "0"+hourOfDay + ":" +"0"+ minute;
					}
					int h, m;
					String[] splitArr;
					
					
					List<MCalendarSelectDateTime> mettingListDT = null;
					MCalendarSelectDateTime mettingTempDT = null;
					MCalendarSelectDateTime mettingDT = null;
					long mettingStartDate = 0;//会议最早开始时间
					long mettingEndDate = 0;//会议最晚结束时间
					int mettingstartHour = 0;
					int mettingstartMinute = 0;
					int mettingendHour = 0;
					int mettingendMinute = 0;
					int day = 0;
					
					if (!Util.isNull(InitiatorDataCache.getInstance().timeSelectetedList)) {//会议开始时间
						mettingListDT = InitiatorDataCache.getInstance().timeSelectetedList;
						for (int i = 0; i < mettingListDT.size(); i++) {
							if (i==0) {
								day = mettingListDT.get(i).day;//会议日期
								mettingStartDate = mettingListDT.get(i).startDate;
								mettingEndDate = mettingListDT.get(i).endDate;
								mettingDT = mettingListDT.get(i);
								mettingstartHour = mettingListDT.get(i).startHour;
								mettingstartMinute = mettingListDT.get(i).startMinute;
								mettingendHour = mettingListDT.get(i).endHour;
								mettingendMinute = mettingListDT.get(i).endMinute;
							}else {
								long starttempDate = mettingListDT.get(i).startDate;
								long endtempDate = mettingListDT.get(i).endDate;
								
								if (starttempDate<=mettingStartDate) {
									mettingStartDate = starttempDate;    //获取会议最早开始的时间
									mettingstartHour = mettingListDT.get(i).startHour;
									mettingstartMinute = mettingListDT.get(i).startMinute;
								}
								if (endtempDate>=mettingEndDate) {
									day = mettingListDT.get(i).day;//会议最晚日期
									mettingEndDate = endtempDate;        //获取会议最晚结束时间
									mettingDT = mettingListDT.get(i);
									mettingendHour = mettingListDT.get(i).endHour;
									mettingendMinute = mettingListDT.get(i).endMinute;
								}
							}
						}
					}
					
					if(flag == 1){
						splitArr = dataList.get(positin).endTime.split("\\:");
						h = Integer.parseInt(splitArr[0]);
						m = Integer.parseInt(splitArr[1]);
						if(hourOfDay > h || 
								(hourOfDay == h &&  minute > m)){
							toast.setText("开始时间大于结束时间");
							toast.show();
							return;
						}
						if (selectType==1) {
							if (sdt.day==day) {
								if (mettingstartHour<hourOfDay||(mettingstartHour==hourOfDay&&mettingstartMinute>minute)) {
									toast.setText("分会场开始时间应大于会议开始时间");
									toast.show();
									return;
							}
							}
						}
						
						dataList.get(positin).startTime = time;
						dataList.get(positin).startHour = hourOfDay;
						dataList.get(positin).startMinute = minute;
						IniviteUtil.getFormatDTMillis(dataList.get(positin), 0);
					}else{
						splitArr = dataList.get(positin).startTime.split("\\:");
						h = Integer.parseInt(splitArr[0]);
						m = Integer.parseInt(splitArr[1]);
						if(hourOfDay < h || 
								(hourOfDay == h &&  minute < m)){
							toast.setText("结束时间小于于开始时间");
							toast.show();
							return;
						}
						if (selectType==1) {
							if (sdt.day==day) {//选择的时间比会议最晚日期
								if (mettingendHour<hourOfDay||(mettingendHour==hourOfDay&&mettingendMinute<minute)) {
									toast.setText("分会场结束时间应小于会议结束时间");
									toast.show();
									return;
								}
							}
						}
						
						dataList.get(positin).endTime = time;
						dataList.get(positin).endHour = hourOfDay;
						dataList.get(positin).endMinute = minute;
						IniviteUtil.getFormatDTMillis(dataList.get(positin), 1);
					}
					((TextView)v).setText(time);
					timePickDlg.dismiss();
				}
			}, 
			calendar.get(Calendar.HOUR_OF_DAY), 
			calendar.get(Calendar.MINUTE), 
			true);
		timePickDlg.show();
	}
	public void modifyTime(int startHour,int startMinute,int endHour,int endMinute,int positin/*,ArrayList<MCalendarSelectDateTime> mettingListDTs*/){
		//dataList=mettingListDTs;
		//View v=view;
		//int id = v.getId();
		//int flag = 0;
		//if (id == R.id.hy_itemCalendar_dateStartTimeText) {
		//	flag = 1;
		//} else if (id == R.id.hy_itemCalendar_dateEndTimeText) {
		//	flag = 2;
		//}
		
			String startTime = startHour + ":" + startMinute;
			if ((startHour + "").length() == 1) {
				startTime = "0" + startHour + ":" + startMinute;
			}
			if ((startMinute + "").length() == 1) {
				startTime = startHour + ":" + "0" + startMinute;
			}
			if ((startHour + "").length() == 1 && (startMinute + "").length() == 1) {
				startTime = "0" + startHour + ":" + "0" + startMinute;
			}
			
			String endTime = endHour + ":" + endMinute;
			if ((endHour + "").length() == 1) {
				endTime = "0" + endHour + ":" + endMinute;
			}
			if ((endMinute + "").length() == 1) {
				endTime = endHour + ":" + "0" + endMinute;
			}
			if ((endHour + "").length() == 1 && (endMinute + "").length() == 1) {
				endTime = "0" + endHour + ":" + "0" + endMinute;
			}
			
			
			int h, m;
			String[] splitArr;

			List<MCalendarSelectDateTime> mettingListDT = null;
			MCalendarSelectDateTime mettingTempDT = null;
			MCalendarSelectDateTime mettingDT = null;
			long mettingStartDate = 0;// 会议最早开始时间
			long mettingEndDate = 0;// 会议最晚结束时间
			int mettingstartHour = 0;
			int mettingstartMinute = 0;
			int mettingendHour = 0;
			int mettingendMinute = 0;
			int mettingendDay=0;
			int day = 0;

			if (!Util.isNull(InitiatorDataCache.getInstance().timeSelectetedList)) {// 会议开始时间
				mettingListDT = InitiatorDataCache.getInstance().timeSelectetedList;
				for (int i = 0; i < mettingListDT.size(); i++) {
					if (i == 0) {
						day = mettingListDT.get(i).day;// 会议日期
						mettingStartDate = mettingListDT.get(i).startDate;
						mettingEndDate = mettingListDT.get(i).endDate;
						mettingDT = mettingListDT.get(i);
						mettingstartHour = mettingListDT.get(i).startHour;
						mettingstartMinute = mettingListDT.get(i).startMinute;
						mettingendHour = mettingListDT.get(i).endHour;
						mettingendMinute = mettingListDT.get(i).endMinute;
						mettingendDay=mettingListDT.get(i).day;
					} else {
						long starttempDate = mettingListDT.get(i).startDate;
						long endtempDate = mettingListDT.get(i).endDate;

						if (starttempDate <= mettingStartDate) {
							mettingendDay=mettingListDT.get(i).day;
							mettingStartDate = starttempDate; // 获取会议最早开始的时间
							mettingstartHour = mettingListDT.get(i).startHour;
							mettingstartMinute = mettingListDT.get(i).startMinute;
						}
						if (endtempDate >= mettingEndDate) {
							day = mettingListDT.get(i).day;// 会议最晚日期
							mettingEndDate = endtempDate; // 获取会议最晚结束时间
							mettingDT = mettingListDT.get(i);
							mettingendHour = mettingListDT.get(i).endHour;
							mettingendMinute = mettingListDT.get(i).endMinute;
						}
					}
				}
			}

			//if (flag == 1) {
				splitArr = dataList.get(positin).endTime.split("\\:");
				h = Integer.parseInt(splitArr[0]);
				m = Integer.parseInt(splitArr[1]);
//				if(hourOfDay > h || 
//						(hourOfDay == h &&  minute > m)){
//					toast.setText("开始时间大于结束时间");
//					toast.show();
//					return;
//				}
//				if(startHour>m||(startHour==m&&startMinute<=m)){
//					toast.setText("开始时间大于结束时间");
//					toast.show();
//					return;
//				}
				if (startHour > endHour || (startHour == endHour && startMinute > endMinute)) {
					toast.setText("开始时间大于结束时间");
					toast.show();
					return;
				}
//				if (startHour > h || (startHour == h && startMinute < m)) {
//					toast.setText("startHour:"+startHour+"\n startMinute:"+startMinute +"\n h:"+h+"\n m:"+m);
//					//toast.setText("开始时间大于结束时间");
//					toast.show();
//					return;
//				}
				if (selectType == 1) {
					int aaa = 0;
					for (int nimabide = 0; nimabide < InitiatorDataCache.getInstance().timeSelectetedList.size(); nimabide++) {
						MCalendarSelectDateTime timesl =InitiatorDataCache.getInstance().timeSelectetedList.get(nimabide);
						if (0 == aaa) {
							aaa = timesl.day;
						}
						if (aaa > timesl.startDate) {
							aaa = timesl.day;
						}
					}
					if (aaa==sdt.day) {
					if (mettingstartHour > startHour || (mettingstartHour == startHour && mettingstartHour < startHour)) {
						toast.setText("分会场开始时间应大于会议开始时间");
						toast.show();
						return;

					}
					}
				}

				dataList.get(positin).startTime = startTime;
				dataList.get(positin).startHour = startHour;
				dataList.get(positin).startMinute = startMinute;
				IniviteUtil.getFormatDTMillis(dataList.get(positin), 0);
			//} else {
				splitArr = dataList.get(positin).startTime.split("\\:");
				h = Integer.parseInt(splitArr[0]);
				m = Integer.parseInt(splitArr[1]);
				if (endHour < h || (endHour == h && endMinute < m)) {
					toast.setText("结束时间小于于开始时间");
					toast.show();
					return;
				}
				if (selectType == 1) {
					if (sdt.day == day) {// 选择的时间比会议最晚日期
						if (mettingendHour < endHour || (mettingendHour == endHour && mettingendMinute < endMinute)) {
							toast.setText("分会场结束时间应小于会议结束时间");
							toast.show();
							return;
						}
					}
				}

				dataList.get(positin).endTime = endTime;
				dataList.get(positin).endHour = endHour;
				dataList.get(positin).endMinute = endMinute;
				IniviteUtil.getFormatDTMillis(dataList.get(positin), 1);
			//}
		}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(Util.isNull(dataList)){
			return 0;
		}else{
			return dataList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView holderView;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hy_item_calendar_time, parent, false);
			holderView = new HolderView();
			holderView.selectDate = (TextView) convertView.findViewById(R.id.hy_itemCalendar_dateText);
			holderView.startTime = (TextView) convertView.findViewById(R.id.hy_itemCalendar_dateStartTimeText);
			holderView.endTime = (TextView) convertView.findViewById(R.id.hy_itemCalendar_dateEndTimeText);
			holderView.addIv = (ImageView) convertView.findViewById(R.id.addIv);
			
			
			
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
		if(0 == selectType){
			holderView.addIv.setVisibility(View.VISIBLE);
		}
		else if(1 == selectType){
			holderView.addIv.setVisibility(View.GONE);
		}
		
		holderView.addIv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				MCalendarSelectDateTime curCalendarSelectDateTime = dataList.get(position);
				
				MCalendarSelectDateTime addMCalendarSelectDateTime = new MCalendarSelectDateTime();
				addMCalendarSelectDateTime.year = curCalendarSelectDateTime.year;
				addMCalendarSelectDateTime.month = curCalendarSelectDateTime.month;
				addMCalendarSelectDateTime.day = curCalendarSelectDateTime.day;
				addMCalendarSelectDateTime.weekIndex = curCalendarSelectDateTime.weekIndex;
				
				addMCalendarSelectDateTime.startHour = curCalendarSelectDateTime.startHour;
				addMCalendarSelectDateTime.startMinute = curCalendarSelectDateTime.startMinute;
				addMCalendarSelectDateTime.startTime = curCalendarSelectDateTime.startTime;
				
				addMCalendarSelectDateTime.endHour = curCalendarSelectDateTime.endHour;
				addMCalendarSelectDateTime.endMinute = curCalendarSelectDateTime.endMinute;
				addMCalendarSelectDateTime.endTime = curCalendarSelectDateTime.endTime;
				
				
				dataList.add(position, addMCalendarSelectDateTime);
				notifyDataSetChanged();
				
			}
		});
		
		setHolderView(holderView, position);
		return convertView;
	}
	
	private class HolderView{
		public TextView selectDate;
		public TextView startTime;
		public TextView endTime;
		ImageView addIv;
	}
	private class MyOnClickListener implements View.OnClickListener{
		private int position;
		public MyOnClickListener(int position){
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			int flag = 0;
			if(id == R.id.hy_itemCalendar_dateStartTimeText){
				flag = 1;
			}else if(id == R.id.hy_itemCalendar_dateEndTimeText){
				flag = 2;
			}
			if(flag > 0){
				//view=v;
				ArrayList<MCalendarSelectDateTime> mettingListDT = InitiatorDataCache.getInstance().timeSelectetedList;
				Intent intent = new Intent();
				intent.putExtra("mettingListDT", mettingListDT);  
				intent.putExtra("position", position);
				intent.setClass(context, CalendarTimeSelectionActivity.class);
				
				((Activity) context).startActivityForResult(intent,110);

				//setDateTime(v, position, flag);
			}
		}
	}
	
    class MyTimePickerDialog extends TimePickerDialog {
        
        public MyTimePickerDialog(Context context, int theme,
				OnTimeSetListener callBack, int hourOfDay, int minute,
				boolean is24HourView) {
			super(context, theme, callBack, hourOfDay, minute, is24HourView);
			// TODO Auto-generated constructor stub
		}

        
		public MyTimePickerDialog(Context context, OnTimeSetListener callBack,
				int hourOfDay, int minute, boolean is24HourView) {
			super(context, callBack, hourOfDay, minute, is24HourView);
			// TODO Auto-generated constructor stub
		}


		@Override
        protected void onStop() {
            //super.onStop();
        }
    }
	
}
