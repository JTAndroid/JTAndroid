package com.tr.ui.work;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.tr.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;


public class WorkDateTimePickerDialog implements OnDateChangedListener,
		OnTimeChangedListener {
	private DatePicker datePicker;
	private TimePicker timePicker;
	private AlertDialog ad;
	private String dateTime;
	private String dateTimeEng;
	private String initDateTime;
	private Activity activity;

	private OnDayChangeListener dayChangeListener;

	/**
	 * 日期时间弹出选择框构造函数
	 * 
	 * @param activity
	 *            ：调用的父activity
	 * @param initDateTime
	 *            初始日期时间值，作为弹出窗口的标题和日期时间初始值
	 */
	public WorkDateTimePickerDialog(Activity activity, String initDateTime) {
		this.activity = activity;
		this.initDateTime = initDateTime;

	}

	public void init(DatePicker datePicker, TimePicker timePicker) {
		Calendar calendar = Calendar.getInstance();
		if (!(null == initDateTime || "".equals(initDateTime))) {
			Log.d("xmx","initDateTime not null");
			calendar = this.getCalendarByInintData(initDateTime);
		} else {
			initDateTime = calendar.get(Calendar.YEAR)
					+ WorkDateTimePickerDialog.intToStr2(calendar.get(Calendar.MONTH))
					+ WorkDateTimePickerDialog.intToStr2(calendar.get(Calendar.DAY_OF_MONTH)) + " "
					+ WorkDateTimePickerDialog.intToStr2(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
					+ WorkDateTimePickerDialog.intToStr2(calendar.get(Calendar.MINUTE));
		}
		Log.d("xmx","initDateTime:"+initDateTime);
		
		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), this);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		Log.d("xmx","Calendar.HOUR_OF_DAY):"+calendar.get(Calendar.HOUR_OF_DAY));
		
	}

	public static String intToStr2(int inInt) {
		String vStr = "0" + inInt;
		return vStr.substring(vStr.length() - 2, vStr.length());
	}

	/**
	 * 弹出日期时间选择框方法
	 * 
	 * @param inputDate
	 *            :为需要设置的日期时间文本编辑框
	 * @return
	 */
	public AlertDialog dateTimePicKDialog(long inMinDate) {
		LinearLayout dateTimeLayout = (LinearLayout) activity
				.getLayoutInflater().inflate(R.layout.work_date_time_picker_dialog, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
		

		timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
		
		
		//if (inMinDate > 0)
		//	datePicker.setMinDate(inMinDate);
		
		timePicker.setIs24HourView(true);
		init(datePicker, timePicker);
		timePicker.setOnTimeChangedListener(this);

		datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);  
		timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS); 

		
		ad = new AlertDialog.Builder(activity)
				.setTitle(initDateTime)
				.setView(dateTimeLayout)
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// inputDate.setText(dateTime);
						if (dayChangeListener != null)
							dayChangeListener.onDayChagne(dateTimeEng);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// inputDate.setText("");
					}
				}).show();

		onDateChanged(null, 0, 0, 0);
		

		View view = activity.getWindow().peekDecorView();
        if (view != null) {
        	Log.d("xmx","close input");
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        return ad;
	}

	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		onDateChanged(null, 0, 0, 0);
	}

	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// 获得日历实例
		Calendar calendar = Calendar.getInstance();

		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
				timePicker.getCurrentMinute());
		Log.d("xmx"," timePicker.getCurrentHour():"+timePicker.getCurrentHour()+"  timePicker.getCurrentMinute():"+timePicker.getCurrentMinute());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

		dateTime = sdf.format(calendar.getTime());
		dateTimeEng = sdf1.format(calendar.getTime());
		ad.setTitle(dateTime);
		Log.d("xmx", "dateTimeEng:" + dateTimeEng);
	}

	/**
	 * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
	 * 
	 * @param initDateTime
	 *            初始日期时间值 字符串型
	 * @return Calendar
	 */
	private Calendar getCalendarByInintData(String initDateTime) {
		Calendar calendar = Calendar.getInstance();

		// 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
		// String yearStr = spliteString(date, "年", "index", "front"); // 年份
		// 20150101 00:00:00
		Log.d("xmx","getCalendarByInintData:");
		
		String yearStr = initDateTime.substring(0, 4); // 年份
		String monthStr = initDateTime.substring(4, 6); // 月
		String dayStr = initDateTime.substring(6, 8); // 日

		String hourStr = initDateTime.substring(9, 11); // 时
		String minuteStr = initDateTime.substring(12, 14);
		; // 分

		int currentYear = Integer.valueOf(yearStr.trim()).intValue();
		int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
		int currentDay = Integer.valueOf(dayStr.trim()).intValue();
		int currentHour = Integer.valueOf(hourStr.trim()).intValue();
		int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

		calendar.set(currentYear, currentMonth, currentDay, currentHour,
				currentMinute,0);
		return calendar;
	}

	/**
	 * 截取子串
	 * 
	 * @param srcStr
	 *            源串
	 * @param pattern
	 *            匹配模式
	 * @param indexOrLast
	 * @param frontOrBack
	 * @return
	 */
	public static String spliteString(String srcStr, String pattern,
			String indexOrLast, String frontOrBack) {
		String result = "";
		int loc = -1;
		if (indexOrLast.equalsIgnoreCase("index")) {
			loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
		} else {
			loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
		}
		if (frontOrBack.equalsIgnoreCase("front")) {
			if (loc != -1)
				result = srcStr.substring(0, loc); // 截取子串
		} else {
			if (loc != -1)
				result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
		}
		return result;
	}

	public void setDayChangeListener(OnDayChangeListener dayChangeListener) {
		this.dayChangeListener = dayChangeListener;
	}

	public interface OnDayChangeListener {
		public void onDayChagne(String outDay);
	}
}
