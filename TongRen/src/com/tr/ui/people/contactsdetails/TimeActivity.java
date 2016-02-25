package com.tr.ui.people.contactsdetails;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.ui.people.cread.BaseActivity;

/**
 * 会面模块->选择会面时间
 * @author John
 *
 */
public class TimeActivity extends BaseActivity implements OnClickListener {

	TextView timeText;
	private boolean timeChanged = false;
	private boolean timeScrolled = false;
	String[] year;
	String[] month;
	String[] day;
	String[] minutes;
	String[] hours;
	private MediaPlayer player;
	

	private NumberPicker numberPicker;

	private String temp, remindTime = "", repeatTime = "";

	private RelativeLayout remind_relativelayout, repeatRelativeLayout;

	private TextView remindTimeTv, repeatTimeTv;

	private Intent intent;
	private String hour;
	private long delay;
	private long period;
	private long time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_time);

		setActionBarLayout(R.layout.people_actionbar);

		remind_relativelayout = (RelativeLayout) findViewById(R.id.remind_relativelayout);
		remind_relativelayout.setOnClickListener(this);

		repeatRelativeLayout = (RelativeLayout) findViewById(R.id.repeatRelativeLayout);
		repeatRelativeLayout.setOnClickListener(this);

		remindTimeTv = (TextView) findViewById(R.id.remindTimeTv);

		repeatTimeTv = (TextView) findViewById(R.id.repeatTimeTv);

		numberPicker = (NumberPicker) findViewById(R.id.year);

		clickNumberPicker(numberPicker);

		numberPicker = (NumberPicker) findViewById(R.id.month);
		clickNumberPicker(numberPicker);

		numberPicker = (NumberPicker) findViewById(R.id.day);
		clickNumberPicker(numberPicker);

		numberPicker = (NumberPicker) findViewById(R.id.hour);
		clickNumberPicker(numberPicker);

		numberPicker = (NumberPicker) findViewById(R.id.min);
		clickNumberPicker(numberPicker);

		initDialogView();
	}

	public void setActionBarLayout(int layoutId) {
		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflator = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(layoutId, null);
			TextView view = (TextView) v.findViewById(R.id.time_Tv);
			view.setText("时间");
			ImageView reminder_time_backs = (ImageView) v
					.findViewById(R.id.reminder_time_backs);
			reminder_time_backs.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			TextView time_click_complete = (TextView) v
					.findViewById(R.id.time_click_complete);
			time_click_complete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!TextUtils.isEmpty(temp) && !TextUtils.isEmpty(hour)) {
						Intent intent = new Intent();
						intent.putExtra("TIMES", temp);
						intent.putExtra("HOURS", hour);
						intent.putExtra("REMINDERTIMES", remindTime);
						intent.putExtra("REPEATS", repeatTime);
						intent.putExtra("TIME", time);
						setResult(1, intent);
						finish();
					}
				}
			});
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			actionBar.setCustomView(v, layout);
		}
	}

	private void showToast(Context context, String msg) {
		if (null == context || TextUtils.isEmpty(msg)) {
			return;
		}

		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	private void initDialogView() {
		timeText = (TextView) findViewById(R.id.tv_time);
		Calendar c = Calendar.getInstance();
		String label[] = { "年", "", "月", "", "日", "", "时", "", "分" };
		final List<NumberPicker> wheelViews = new ArrayList<NumberPicker>();
		final List<NumberPicker> wheelViews1 = new ArrayList<NumberPicker>();
		LinearLayout linearWheel1 = (LinearLayout) findViewById(R.id.linear_wheel1);
		LinearLayout linearWheel2 = (LinearLayout) findViewById(R.id.linear_wheel2);
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH + 1);
		int curDay = c.get(Calendar.DAY_OF_MONTH);

		for (int i = 0; i < linearWheel1.getChildCount(); i += 2) {
			NumberPicker view1 = (NumberPicker) linearWheel1.getChildAt(i);
			switch (i) {
			case 0:
				year = new String[500];
				for (int j = 0; j < year.length; j++) {
					year[j] = (j + 2000) + "年";
				}
				view1.setDisplayedValues(year);
				view1.setMinValue(0);
				view1.setMaxValue(year.length - 1);
				view1.setValue(curYear
						- Integer.parseInt(year[0].substring(0, 4)));
				break;
			case 2:
				month = new String[12];
				for (int k = 0; k < month.length; k++) {
					month[k] = String.format("%02d", k + 1) + "月";
				}
				view1.setDisplayedValues(month);
				view1.setMinValue(0);
				view1.setMaxValue(month.length - 1);
				view1.setValue(curMonth - 1);
				break;
			case 4:
				day = new String[31];
				for (int l = 0; l < day.length; l++) {
					day[l] = String.format("%02d", l + 1) + "日";
				}
				view1.setDisplayedValues(day);
				view1.setMinValue(0);
				view1.setMaxValue(day.length - 1);
				view1.setValue(curDay - 1);
				break;
			}
			// view1.setLabel(label[i]);

			setDevider(view1);
			wheelViews.add(view1);
		}

		for (int i = 0; i < linearWheel2.getChildCount(); i += 2) {
			NumberPicker view1 = (NumberPicker) linearWheel2.getChildAt(i);
			switch (i) {
			case 0:
				int curHours = c.get(Calendar.HOUR_OF_DAY);
				hours = new String[24];
				for (int l = 0; l < hours.length; l++) {
					hours[l] = String.format("%02d", l);
				}
				view1.setDisplayedValues(hours);
				view1.setMinValue(0);
				view1.setMaxValue(hours.length - 1);
				view1.setValue(curHours);
				break;
			case 2:
				int curMinutes = c.get(Calendar.MINUTE);
				minutes = new String[60];
				for (int l = 0; l < minutes.length; l++) {
					minutes[l] = String.format("%02d", l);
				}
				view1.setDisplayedValues(minutes);
				view1.setMinValue(0);
				view1.setMaxValue(minutes.length - 1);
				view1.setValue(curMinutes);
				break;
			default:
				break;
			}
			setDevider(view1);
			wheelViews1.add(view1);
		}
		for (int i = 0; i < wheelViews.size(); i++) {
			wheelViews.get(i).setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChange(NumberPicker view,
						int scrollState) {
					Log.v("sdss", view.getValue() + "");
					setTime(wheelViews);
				}
			});
		}
		for (int i = 0; i < wheelViews1.size(); i++) {
			wheelViews1.get(i).setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChange(NumberPicker view,
						int scrollState) {
					Log.v("sdss", view.getValue() + "");
					setTime_Hour(wheelViews1);
				}
			});
		}
		setTime(wheelViews);
		setTime_Hour(wheelViews1);
	}

	public void clickNumberPicker(NumberPicker numberPicker) {

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(numberPicker.getWindowToken(), 0);

		numberPicker.getChildAt(0).setFocusable(false);

		numberPicker.requestFocus();
	}

	protected void setTime_Hour(List<NumberPicker> wheelViews1) {

		for (int i = 0; i < wheelViews1.size(); i++) {
			switch (i) {
			case 0:
				hour = "";
				hour += hours[wheelViews1.get(i).getValue()] + ":";
				break;
			case 1:
				hour += minutes[wheelViews1.get(i).getValue()];
				break;

			default:
				break;
			}
		}
	}

	private void setDevider(NumberPicker hourPicker) {
		Field[] pickerFields = NumberPicker.class.getDeclaredFields();
		for (Field pf : pickerFields) {
			if (pf.getName().equals("mSelectionDivider")) {
				pf.setAccessible(true);
				try {
					pf.set(hourPicker,
							getResources().getDrawable(R.drawable.divider));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NotFoundException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				break;
			}

		}
	}

	public void setTime(final List<NumberPicker> wheelViews) {
		String week[] = { "  星期一", "  星期二", "  星期三", "  星期四", "  星期五", "  星期六",
				"  星期日" };
		for (int i = 0; i < wheelViews.size(); i++) {
			temp = timeText.getText().toString();
			switch (i) {
			case 0:
				temp = "";
				temp += year[wheelViews.get(i).getValue()];
				break;
			case 1:
				temp += month[wheelViews.get(i).getValue()];
				break;
			case 2:
				try {
					temp += temp = day[wheelViews.get(i).getValue()];

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
			timeText.setText(temp);

		}
	}

	/**
	 * 判断当前日期是星期几<br>
	 * <br>
	 * 
	 * @param pTime
	 *            修要判断的时间<br>
	 * @return dayForWeek 判断结果<br>
	 * @Exception 发生异常<br>
	 */
	// String pTime = "2012-03-12";
	private String getWeek(String pTime) {

		String Week = "";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {

			c.setTime(format.parse(pTime));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "天";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}

		return Week;
	}

	public void onActionClick(View v) {

		switch (v.getId()) {

		case R.id.reminder_time_backs: {
			finish();
		}
			break;

		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.remind_relativelayout:

			intent = new Intent(TimeActivity.this, ReminderTimeActivity.class);

			startActivityForResult(intent, 11);

			break;
		case R.id.repeatRelativeLayout:

			intent = new Intent(TimeActivity.this, RepeatActivity.class);

			startActivityForResult(intent, 12);

			break;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {

		case 11:

			remindTime = data.getStringExtra("REMINDERTIME").toString();
			time = data.getLongExtra("TIME", 0);
			remindTimeTv.setText(remindTime);

			
			break;
		case 12:

			repeatTime = data.getStringExtra("REPEAT");

			repeatTimeTv.setText(repeatTime);
			
			break;
		
		}
	

		super.onActivityResult(requestCode, resultCode, data);

	}

}
