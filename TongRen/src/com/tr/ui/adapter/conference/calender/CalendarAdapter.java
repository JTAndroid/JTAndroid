package com.tr.ui.adapter.conference.calender;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.model.conference.MCalendarSelectDateTime;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.tr.ui.conference.initiatorhy.IniviteUtil;
import com.utils.common.Util.DensityUtil;
import com.utils.time.Util;

/**
 * 日历gridview中的每一个item显示的textview
 * 
 * @author lmw
 * 
 */
public class CalendarAdapter extends BaseAdapter {
	private boolean isLeapyear = false; // 是否为闰年
	private int daysOfMonth = 0; // 某月的天数
	private int dayOfWeek = 0; // 具体某一天是星期几
	private int lastDaysOfMonth = 0; // 上一个月的总天数
	private Context context;
	private String[] dayNumber = new String[42]; // 一个gridview中的日期存入此数组中
	// private ArrayList<String> itmeDayList = new ArrayList<String>(42);
	// private static String week[] = {"周日","周一","周二","周三","周四","周五","周六"};
	private CalendarSpecial calenderSpecial = null;
	private CalendarLunar canlenderLunar = null;
	private int currentFlag = -1; // 用于标记当天
	private String showYear = ""; // 用于在头部显示的年份
	private String showMonth = ""; // 用于在头部显示的月份
	private String animalsYear = "";
	private String leapMonth = ""; // 闰哪一个月
	private String cyclical = ""; // 天干地支
	// 系统当前时间
	private int sys_year;
	private int sys_month;
	private int sys_day;

	private int currentYear;
	private int currentMonth;
	private int currentDay;

	private Calendar calendarInst = Calendar.getInstance();

	// private HashMap<Long, MCalendarSelectDateTime> selectedDTMap = new
	// HashMap<Long, MCalendarSelectDateTime>();
	private ArrayList<MCalendarSelectDateTime> timeSelectetedTList;
	// private boolean defaultSelectCurDay;
	private int selectType;

	private Toast mToast;// 这是QA要的特殊的土司
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(popupWindow!=null && popupWindow.isShowing()){
				popupWindow.dismiss();
			}
			if(mToast!=null){
				
			}
		}
		
	};

	public void showToast(String text) {
		
//		if (mToast == null) {
			mToast = MyToast.makeTextFactory(context, text, Toast.LENGTH_SHORT);
			
			if(mToast!=null){
				mToast.show();
			}
//		} else {
//			mToast.setText(text);
//			mToast.setDuration(Toast.LENGTH_SHORT);
//		}
//		mToast.show();
		handler.sendEmptyMessageDelayed(0, 1000);
//		showPop(text);
	}

	public void showPop(String text) {
		if(parent!=null && popupWindow!=null && !popupWindow.isShowing()){
			
//			TextView textView = new TextView(context);
			textView.setText(text);
		/*	System.out.println(" 宽："+textView.getWidth()+"  高："+textView.getHeight());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			textView.setLayoutParams(params);*/
			
			popupWindow.setContentView(textView);
			popupWindow.setWidth(DensityUtil.dip2px(context, textView.getWidth()));
			
			popupWindow.setHeight(DensityUtil.dip2px(context, textView.getHeight()));
			
			popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER, 0, 75);
			
			
		}
		
		}

	public void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
			
		}
		
		if(popupWindow!=null){
			popupWindow.dismiss();popupWindow=null;
		}
	}

	// public CalendarAdapter(Context context){
	// this.context= context;
	// InitiatorDataCache.getInstance().dateSelectetedTempList.clear();
	// this.timeSelectetedTList =
	// InitiatorDataCache.getInstance().dateSelectetedTempList;
	// currentFlag = -1;
	// calendarInst.setTimeInMillis(System.currentTimeMillis());
	// sys_year = calendarInst.get(Calendar.YEAR);
	// sys_month = calendarInst.get(Calendar.MONTH) + 1;
	// sys_day = calendarInst.get(Calendar.DAY_OF_MONTH);
	// currentYear = sys_year; //得到当前的年份
	// currentMonth = sys_month; //得到本月 （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
	// currentDay = sys_day;
	// calenderSpecial = new CalendarSpecial();
	// canlenderLunar = new CalendarLunar();
	// getCalendar(sys_year, sys_month);
	// }
	public CalendarAdapter(Context context, int selectType,
			ArrayList<MCalendarSelectDateTime> timeSelectetedTList) {
		this.context = context;
		this.timeSelectetedTList = timeSelectetedTList;// InitiatorDataCache.getInstance().dateSelectetedTempList;
		// this.defaultSelectCurDay = defaultSelectCurDay;
		this.selectType = selectType;
		currentFlag = -1;
		calendarInst.setTimeInMillis(System.currentTimeMillis());
		sys_year = calendarInst.get(Calendar.YEAR);
		sys_month = calendarInst.get(Calendar.MONTH) + 1;
		sys_day = calendarInst.get(Calendar.DAY_OF_MONTH);
		currentYear = sys_year; // 得到当前的年份
		currentMonth = sys_month; // 得到本月 （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
		currentDay = sys_day;
		calenderSpecial = new CalendarSpecial();
		canlenderLunar = new CalendarLunar();
		getCalendar(sys_year, sys_month);
		// if(defaultSelectCurDay){
		// setSelectDT(currentFlag);
		// }
		initPop();
	}

	private PopupWindow popupWindow;
	private TextView textView;
	private void initPop() {
		// TODO Auto-generated method stub
		popupWindow = new PopupWindow(context);
		View contentView = View.inflate(context, R.layout.conference_time_pop,
				null);
		textView=(TextView) contentView.findViewById(R.id.conference_time_pop);
		popupWindow.setContentView(contentView);

		popupWindow.setBackgroundDrawable(null);
		popupWindow.setAnimationStyle(0);

		/*popupWindow.setWidth(DensityUtil.dip2px(context, textView.getWidth()));
		popupWindow.setHeight(DensityUtil.dip2px(context, textView.getHeight()));*/
	}

	// public CalendarAdapter(Context context,
	// ArrayList<MCalendarSelectDateTime> timeSelectetedTList){
	// this.context= context;
	// this.timeSelectetedTList =
	// timeSelectetedTList;//InitiatorDataCache.getInstance().timeSelectetedTList;
	// currentFlag = -1;
	// calendarInst.setTimeInMillis(System.currentTimeMillis());
	// sys_year = calendarInst.get(Calendar.YEAR);
	// sys_month = calendarInst.get(Calendar.MONTH) + 1;
	// sys_day = calendarInst.get(Calendar.DAY_OF_MONTH);
	// currentYear = sys_year; //得到当前的年份
	// currentMonth = sys_month; //得到本月 （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
	// currentDay = sys_day;
	// calenderSpecial = new CalendarSpecial();
	// canlenderLunar = new CalendarLunar();
	// getCalendar(sys_year, sys_month);
	// }
	public void updateJump(int jumpMonth, int jumpYear) {
		// sys_year = calendarInst.get(Calendar.YEAR);
		// sys_month = calendarInst.get(Calendar.MONTH) + 1;
		// sys_day = calendarInst.get(Calendar.DAY_OF_MONTH);
		currentFlag = -1;
		int stepYear = sys_year + jumpYear;
		int stepMonth = sys_month + jumpMonth;
		if (stepMonth > 0) {
			// 往下一个月滑动
			if (stepMonth % 12 == 0) {
				stepYear = sys_year + stepMonth / 12 - 1;
				stepMonth = 12;
			} else {
				stepYear = sys_year + stepMonth / 12;
				stepMonth = stepMonth % 12;
			}
		} else {
			// 往上一个月滑动
			stepYear = sys_year - 1 + stepMonth / 12;
			stepMonth = stepMonth % 12 + 12;
			if (stepMonth % 12 == 0) {

			}
		}
		currentYear = stepYear; // 得到当前的年份
		currentMonth = stepMonth; // 得到本月 （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
		currentDay = sys_day; // 得到当前日期是哪天
		getCalendar(currentYear, currentMonth);
		notifyDataSetChanged();
	}

	// 得到某年的某月的天数且这月的第一天是星期几
	private void getCalendar(int year, int month) {
		isLeapyear = calenderSpecial.isLeapYear(year); // 是否为闰年
		daysOfMonth = calenderSpecial.getDaysOfMonth(isLeapyear, month); // 某月的总天数
		dayOfWeek = calenderSpecial.getWeekdayOfMonth(year, month); // 某月第一天为星期几
		lastDaysOfMonth = calenderSpecial.getDaysOfMonth(isLeapyear, month - 1); // 上一个月的总天数
		Log.d("DAY", isLeapyear + " ======  " + daysOfMonth
				+ "  ============  " + dayOfWeek + "  =========   "
				+ lastDaysOfMonth);
		getweek(year, month);
	}

	// 将一个月中的每一天的值添加入数组dayNuber中
	private void getweek(int year, int month) {
		int j = 1;
		int flag = 0;
		String lunarDay = "";

		// 得到当前月的所有日程日期(这些日期需要标记)

		for (int i = 0; i < dayNumber.length; i++) {
			// 周一
			// if(i<7){
			// dayNumber[i]=week[i]+"."+" ";
			// }
			if (i < dayOfWeek) { // 前一个月
				int temp = lastDaysOfMonth - dayOfWeek + 1;
				lunarDay = canlenderLunar.getLunarDate(year, month - 1, temp
						+ i, false);
				dayNumber[i] = (temp + i) + "." + lunarDay;

			} else if (i < daysOfMonth + dayOfWeek) { // 本月
				int day = i - dayOfWeek + 1; // 得到的日期
				lunarDay = canlenderLunar.getLunarDate(year, month, i
						- dayOfWeek + 1, false);
				dayNumber[i] = i - dayOfWeek + 1 + "." + lunarDay;
				// 对于当前月才去标记当前日期
				if (sys_year == year && sys_month == month && sys_day == day) {
					// 标记当前日期
					currentFlag = i;
				}
				setShowYear(String.valueOf(year));
				setShowMonth(String.valueOf(month));
				setAnimalsYear(canlenderLunar.animalsYear(year));
				setLeapMonth(canlenderLunar.leapMonth == 0 ? "" : String
						.valueOf(canlenderLunar.leapMonth));
				setCyclical(canlenderLunar.cyclical(year));
			} else { // 下一个月
				lunarDay = canlenderLunar.getLunarDate(year, month + 1, j,
						false);
				dayNumber[i] = j + "." + lunarDay;
				j++;
			}
		}

		// String abc = "";
		// for(int i = 0; i < dayNumber.length; i++){
		// abc = abc+dayNumber[i]+":";
		// }
		// Log.d("DAYNUMBER",abc);

	}

	public ArrayList<MCalendarSelectDateTime> getDTList() {
		return timeSelectetedTList;
	}

	private void setHolderView(HolderView holderView, int position) {
		String[] splitArr = dayNumber[position].split("\\.");
		String d = splitArr[0];
		String ld = splitArr[1];

		if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
			holderView.layout.setClickable(true);
			holderView.day.setText(d);
			holderView.lunarDay.setText(ld);
			holderView.day.setTextColor(context.getResources()
                    .getColor(R.color.black));
			if (position % 7 == 0 || (position + 1) % 7 == 0) {
				holderView.lunarDay.setTextColor(context.getResources()
						.getColor(R.color.hy_calendar_lunarday_special_color));
			} else {
				holderView.lunarDay.setTextColor(context.getResources()
						.getColor(R.color.hy_calendar_lunarday_color));
			}

			if (currentFlag == position) {
				holderView.day.setTypeface(Typeface
						.defaultFromStyle(Typeface.BOLD));
			} else {
				holderView.day.setTypeface(Typeface
						.defaultFromStyle(Typeface.NORMAL));
			}
			boolean selected = checkSelectDT(position);
			if (selected) {
				holderView.layout
						.setBackgroundResource(R.drawable.hy_calender_selected_bg);
			} else {
				// if(currentFlag == position){
				// //设置当天的背景
				// holderView.layout.setBackgroundResource(R.color.hy_calendar_curday_color);
				// }else{
				// holderView.layout.setBackgroundResource(R.color.transparent);
				// }
				holderView.layout.setBackgroundResource(R.color.transparent);
			}
			holderView.layout
					.setOnClickListener(new MyOnClickListener(position));
		} else {
		    holderView.layout.setBackgroundResource(R.color.transparent);
			holderView.layout.setClickable(false);
			holderView.day.setText(d);
            holderView.lunarDay.setText(ld);
            holderView.day.setTextColor(context.getResources()
                    .getColor(R.color.lightgray));
            if (position % 7 == 0 || (position + 1) % 7 == 0) {
                holderView.lunarDay.setTextColor(context.getResources()
                        .getColor(R.color.hy_calendar_lunarday_special_color));
            } else {
                holderView.lunarDay.setTextColor(context.getResources()
                        .getColor(R.color.lightgray));
            }
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dayNumber.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hy_item_calendar, parent, false);
			holderView = new HolderView();
			holderView.layout = (View) convertView
					.findViewById(R.id.hy_itmeCalendar_layout);
			holderView.day = (TextView) convertView
					.findViewById(R.id.hy_itmeCalendar_dayText);
			holderView.lunarDay = (TextView) convertView
					.findViewById(R.id.hy_itmeCalendar_lunarDayText);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		setHolderView(holderView, position);
		return convertView;
	}

	private class HolderView {
		public View layout;
		public TextView day;
		public TextView lunarDay;
	}
	private View parent;
	private class MyOnClickListener implements View.OnClickListener {
		private int position;

		public MyOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			parent=v;
			boolean selected = setSelectDT(position);
			System.out.println(selected+"backgroud");
//			showPop(v, 2, 2, 2);
			if (selected) {
				// if(currentFlag == position){
				// //设置当天的背景
				// // v.setBackgroundResource(R.color.hy_calendar_curday_color);
				// }else{
				// v.setBackgroundResource(R.color.transparent);
				// }
				v.setBackgroundResource(R.drawable.hy_calender_selected_bg);
			} else {
				v.setBackgroundResource(R.color.transparent);
			}
		}

	}

	public void matchScheduleDate(int year, int month, int day) {

	}

	/**
	 * 点击每一个item时返回item中的日期
	 * 
	 * @param position
	 * @return
	 */
	public String getDateByClickItem(int position) {
		return dayNumber[position];
	}

	/**
	 * 在点击gridView时，得到这个月中第一天的位置
	 * 
	 * @return
	 */
	public int getStartPositon() {
		return dayOfWeek + 7;
	}

	/**
	 * 在点击gridView时，得到这个月中最后一天的位置
	 * 
	 * @return
	 */
	public int getEndPosition() {
		return (dayOfWeek + daysOfMonth + 7) - 1;
	}

	public String getShowYear() {
		return showYear;
	}

	public void setShowYear(String showYear) {
		this.showYear = showYear;
	}

	public String getShowMonth() {
		return showMonth;
	}

	public void setShowMonth(String showMonth) {
		this.showMonth = showMonth;
	}

	public String getAnimalsYear() {
		return animalsYear;
	}

	public void setAnimalsYear(String animalsYear) {
		this.animalsYear = animalsYear;
	}

	public String getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(String leapMonth) {
		this.leapMonth = leapMonth;
	}

	public String getCyclical() {
		return cyclical;
	}

	public void setCyclical(String cyclical) {
		this.cyclical = cyclical;
	}

	public void clear() {
		// selectedDTMap.clear();
	}

	public MCalendarSelectDateTime getSelectDTYMD(int position) {
		MCalendarSelectDateTime sdt = new MCalendarSelectDateTime();
		sdt.year = Integer.parseInt(getShowYear());
		sdt.month = Integer.parseInt(getShowMonth());
		String d = dayNumber[position].split("\\.")[0];
		sdt.day = Integer.parseInt(d);
		switch (position % 7) {
		case 0:
			sdt.weekIndex = 0;
			break;
		case 1:
			sdt.weekIndex = 1;
			break;
		case 2:
			sdt.weekIndex = 2;
			break;
		case 3:
			sdt.weekIndex = 3;
			break;
		case 4:
			sdt.weekIndex = 4;
			break;
		case 5:
			sdt.weekIndex = 5;
			break;
		case 6:
			sdt.weekIndex = 6;
			break;
		}
		return sdt;
	}

	//
	public void setSelectDTItems(int position, String startTime, String endTime) {
		MCalendarSelectDateTime sdt = getSelectDTYMD(position);
//		sdt.position = position;
		sdt.startTime = startTime;
		sdt.endTime = endTime;
		timeSelectetedTList.add(sdt);
		// notifyDataSetChanged();
	}

	public void setUnselectDTItem(int position) {
		MCalendarSelectDateTime curDT = getSelectDTYMD(position);
		int listIndex = 0;
		for (MCalendarSelectDateTime sdt : timeSelectetedTList) {
//			if (position == sdt.position) {
				if (curDT.day == sdt.day && curDT.month == sdt.month
						&& curDT.year == sdt.year) {
					timeSelectetedTList.remove(listIndex);
					break;
				}
//			}
			listIndex++;
		}
		// notifyDataSetChanged();
	}

	public boolean checkSelectDT(int position) {
		MCalendarSelectDateTime curDT = getSelectDTYMD(position);
		boolean selected = false;
		for (MCalendarSelectDateTime sdt : timeSelectetedTList) {
//			if (position == sdt.position) {
				if (curDT.day == sdt.day && curDT.month == sdt.month
						&& curDT.year == sdt.year) {
					selected = true;
					break;
				}
//			}
		}
		return selected;
	}

	public boolean isLessSysDay(MCalendarSelectDateTime dt) {
		if (dt.year < sys_year) {
			return true;
		}
		if (dt.year == sys_year) {
			if (dt.month < sys_month) {
				return true;
			}
			if (dt.month == sys_month) {
				if (dt.day < sys_day) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean setSelectDT(int position) {
		if (position < 0) {
			return false;
		}
		MCalendarSelectDateTime curDT = getSelectDTYMD(position);
		boolean selected = false;
		int listIndex = 0;
		for (MCalendarSelectDateTime sdt : timeSelectetedTList) {
//			if (position == sdt.position) {
				if (curDT.day == sdt.day && curDT.month == sdt.month
						&& curDT.year == sdt.year) {
					selected = true;
					break;
				}
//			}
			listIndex++;
		}
		if (selected) {
//			timeSelectetedTList.remove(listIndex);
			// 删除一组相同日期不同时间的数据
			for(int i = 0; i < timeSelectetedTList.size() ; i++ ){
				MCalendarSelectDateTime sdt = timeSelectetedTList.get(i);
				if (curDT.day == sdt.day && curDT.month == sdt.month && curDT.year == sdt.year) {
					timeSelectetedTList.remove(sdt);
					i--;
				}
			}
			
			((CalendarOnDateSelectListener) context)
					.OnDateSelectListener(timeSelectetedTList);
		} else {
			if (isLessSysDay(curDT)) {
				// Toast.makeText(context, "所选日期小于当前日期",
				// Toast.LENGTH_SHORT).show();
				showToast("所选日期小于当前日期");
				return false;
			}
			Calendar c = Calendar.getInstance();
			long startMS = System.currentTimeMillis();
			c.setTimeInMillis(startMS);
			sys_year = calendarInst.get(Calendar.YEAR);
//			curDT.position = position;

			curDT.startHour = c.get(Calendar.HOUR_OF_DAY);
			curDT.startMinute = c.get(Calendar.MINUTE);

			curDT.startTime = curDT.startHour + ":" + curDT.startMinute;

			if ((curDT.startHour + "").length() == 1) {
				curDT.startTime = "0" + curDT.startHour + ":"
						+ curDT.startMinute;
			}
			if ((curDT.startMinute + "").length() == 1) {
				curDT.startTime = curDT.startHour + ":" + "0"
						+ curDT.startMinute;
			}
			if ((curDT.startHour + "").length() == 1
					&& (curDT.startMinute + "").length() == 1) {
				curDT.startTime = "0" + curDT.startHour + ":" + "0"
						+ curDT.startMinute;
			}

			c.setTimeInMillis(startMS + 7200000);
			curDT.endHour = c.get(Calendar.HOUR_OF_DAY);
			;
			curDT.endMinute = c.get(Calendar.MINUTE);
			;

			curDT.endTime = curDT.endHour + ":" + curDT.endMinute;

			if ((curDT.endHour + "").length() == 1) {
				curDT.endTime = "0" + curDT.endHour + ":" + curDT.endMinute;
			}
			if ((curDT.endMinute + "").length() == 1) {
				curDT.endTime = curDT.endHour + ":" + "0" + curDT.endMinute;
			}
			if ((curDT.endHour + "").length() == 1
					&& (curDT.endMinute + "").length() == 1) {
				curDT.endTime = "0" + curDT.endHour + ":" + "0"
						+ curDT.endMinute;
			}
			if (0 <= curDT.endHour && curDT.endHour < 2) {
				curDT.endTime = 23 + ":" + 59;
			}

			IniviteUtil.getFormatDTMillis(curDT);

			/**
			 * 分会场和会议时间校验 add by zhongshan
			 */
			List<MCalendarSelectDateTime> mettingListDT = null;
			MCalendarSelectDateTime mettingTempDT = null;
			MCalendarSelectDateTime mettingDT = null;
			long mettingStartDate = 0;// 会议最早开始时间
			long mettingEndDate = 0;// 会议最晚结束时间

			if (!Util
					.isNull(InitiatorDataCache.getInstance().timeSelectetedList)) {// 会议开始时间
				mettingListDT = InitiatorDataCache.getInstance().timeSelectetedList;
				for (int i = 0; i < mettingListDT.size(); i++) {
					if (i == 0) {
						mettingStartDate = mettingListDT.get(i).startDate;
						mettingEndDate = mettingListDT.get(i).endDate;
						mettingDT = mettingListDT.get(i);
					} else {
						long starttempDate = mettingListDT.get(i).startDate;
						long endtempDate = mettingListDT.get(i).endDate;
						if (starttempDate <= mettingStartDate) {
							mettingStartDate = starttempDate; // 获取会议最早开始的时间
						}
						if (endtempDate >= mettingEndDate) {
							mettingEndDate = endtempDate; // 获取会议最晚结束时间
							mettingDT = mettingListDT.get(i);
						}
					}
				}
			}
			if (!Util
					.isNull(InitiatorDataCache.getInstance().dateSelectetedTempList)) {// 分会场开始时间
				mettingTempDT = InitiatorDataCache.getInstance().dateSelectetedTempList
						.get(0);
			}
			if (selectType != 0) {// 分会场主题会议时间
				if (null != mettingListDT && curDT != null) {

					if (mettingStartDate > 0) {
						if ((mettingDT.year == curDT.year
								&& mettingDT.month == curDT.month && mettingDT.day == curDT.day)) {
						} else if (curDT.startDate <= mettingStartDate) {
							// Toast.makeText(context, "分会场开始时间应大于会议开始时间",
							// Toast.LENGTH_SHORT).show();
							showToast("分会场开始时间应大于会议开始时间");
							return false;
						}
					}
					if (mettingListDT.size() >= 1
							&& mettingEndDate > mettingStartDate) {
						if (curDT.endDate >= mettingEndDate) {
							if (!(mettingDT.year == curDT.year
									&& mettingDT.month == curDT.month && mettingDT.day == curDT.day)) {
								// Toast.makeText(context, "分会场结束时间应小于会议结束时间",
								// Toast.LENGTH_SHORT).show();
								showToast("分会场结束时间应小于会议结束时间");
								return false;
							}
						}
					}
					timeSelectetedTList.clear();
					timeSelectetedTList.add(curDT);
					notifyDataSetChanged();
				} else if (null == mettingListDT) {
					// Toast.makeText(context, "请先选择会议开始时间",
					// Toast.LENGTH_SHORT).show();
					showToast("请先选择会议开始时间");
					return false;
				} else {
					timeSelectetedTList.clear();
					timeSelectetedTList.add(curDT);
					notifyDataSetChanged();
				}
			} else {// 会议时间
			// if (mettingTempDT!=null&&curDT!=null) {
			// if (mettingTempDT.startDate<=curDT.startDate) {
			// Toast.makeText(context, "分会场开始时间应小于会议开始时间",
			// Toast.LENGTH_LONG).show();
			// return false;
			// }else {
			// timeSelectetedTList.add(curDT);
			// }
			// }else {
				timeSelectetedTList.add(curDT);
				// }
			}

			((CalendarOnDateSelectListener) context)
					.OnDateSelectListener(timeSelectetedTList);
		}

		// notifyDataSetChanged();
		return !selected;
	}

	public ArrayList<MCalendarSelectDateTime> getTimeSelectedList() {
		return timeSelectetedTList;
	}

	public interface CalendarOnDateSelectListener {
		public abstract void OnDateSelectListener(
				ArrayList<MCalendarSelectDateTime> selectedDTList);
	}
}
