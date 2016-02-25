package com.tr.ui.work;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.tr.R;

public class WorkDatePickerDialog implements OnDateChangedListener, OnTimeChangedListener {
	private DatePicker datePicker;
	private AlertDialog ad;
	private String dateTime;
	private String dateTimeEng;
	private String initDateTime;
	private Activity activity;

	private OnDayChangeListener dayChangeListener;
	private SimpleDateFormat sdf= new SimpleDateFormat("yyyy年MM月dd日");
	
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 日期时间弹出选择框构造函数
	 * 
	 * @param activity
	 *            ：调用的父activity
	 * @param initDateTime
	 *            初始日期时间值，作为弹出窗口的标题和日期时间初始值
	 */
	public WorkDatePickerDialog(Activity activity, String initDateTime) {
		this.activity = activity;
		this.initDateTime = initDateTime;

	}

	public void setSimpleDateFormat(SimpleDateFormat format) {
		this.sdf1 = format;
	}

	public void init(DatePicker datePicker, TimePicker timePicker) {
		Calendar calendar = Calendar.getInstance();
		if (!(null == initDateTime || "".equals(initDateTime))) {
			calendar = this.getCalendarByInintData(initDateTime);
		} else {
			initDateTime = calendar.get(Calendar.YEAR) + WorkDatePickerDialog.intToStr2(calendar.get(Calendar.MONTH)) + WorkDatePickerDialog.intToStr2(calendar.get(Calendar.DAY_OF_MONTH));
		}

		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
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
		LinearLayout dateTimeLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.work_date_picker_dialog, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);

		// mTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

		init(datePicker, null);

		ad = new AlertDialog.Builder(activity).setTitle(initDateTime).setView(dateTimeLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// inputDate.setText(dateTime);
				if (dayChangeListener != null)
					dayChangeListener.onDayChagne(dateTimeEng);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// inputDate.setText("");
			}
		}).show();

		View view = activity.getWindow().peekDecorView();
		if (view != null) {
			// ((InputMethodManager)activity.getSystemService(activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
			// InputMethodManager.HIDE_NOT_ALWAYS);
			Log.d("xmx", "close input");
			InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}

		datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

		TextView text_notuse = (TextView) dateTimeLayout.findViewById(R.id.text_notuse);
		text_notuse.requestFocus();

		onDateChanged(null, 0, 0, 0);
		return ad;
	}

	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		onDateChanged(null, 0, 0, 0);
	}

	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// 获得日历实例
		Calendar calendar = Calendar.getInstance();

		calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
		

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
		String yearStr = initDateTime.substring(0, 4); // 年份
		String monthStr = initDateTime.substring(4, 6); // 月
		String dayStr = initDateTime.substring(6, 8); // 日

		int currentYear = Integer.valueOf(yearStr.trim()).intValue();
		int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
		int currentDay = Integer.valueOf(dayStr.trim()).intValue();

		calendar.set(currentYear, currentMonth, currentDay);
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
	public static String spliteString(String srcStr, String pattern, String indexOrLast, String frontOrBack) {
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
