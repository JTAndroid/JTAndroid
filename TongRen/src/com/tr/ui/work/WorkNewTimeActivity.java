package com.tr.ui.work;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.model.work.BUAffar;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.work.WorkDateTimePickerDialog.OnDayChangeListener;
/**
 * 时间
 * @author Administrator
 *
 */
public class WorkNewTimeActivity extends JBaseActivity implements
		OnDayChangeListener {

	private TextView TextViewRemind;
	private TextView TextViewRepeat;
	private TextView TextViewBeginTime;
	private TextView TextViewEndTime;

	private int mRemindValue;
	private String mRemindType;
	private String mRepeatType;
	private String mBeginTime;
	private String mEndTime;

	private int mSetTimeFlag = 0; // 0 开始 1 结束

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_new_time_activity);

		mRemindType = "o";//提醒类型m:分钟，h：小时，d：天，其他为不提醒"
		mRemindValue = getIntent().getIntExtra("RemindValue", 0);
		mRemindType = getIntent().getStringExtra("RemindType");
		mRepeatType = getIntent().getStringExtra("RepeatType");

		mBeginTime = getIntent().getStringExtra("BeginTime");
		mEndTime = getIntent().getStringExtra("EndTime");

		if (mBeginTime == null)
			mBeginTime = "";
		if (mEndTime == null)
			mEndTime = "";

		initView();
		initData();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(0, 101, 0, "完成");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	public void initView() {
		TextViewRemind = (TextView) findViewById(R.id.TextViewRemind);
		TextViewRepeat = (TextView) findViewById(R.id.TextViewRepeat);

		TextViewBeginTime = (TextView) findViewById(R.id.TextViewBeginTime);
		TextViewEndTime = (TextView) findViewById(R.id.TextViewEndTime);
	}

	public void initData() {
		resetViewData();
	}

	public void resetViewData() {

		TextViewBeginTime.setText(WorkNewTimeActivity.getDateStrByDayNew(mBeginTime));
		TextViewEndTime.setText(WorkNewTimeActivity.getDateStrByDayNew(mEndTime));

		if (mRemindType.equals("m"))
			TextViewRemind.setText(mRemindValue + "分钟前提醒");
		if (mRemindType.equals("h"))
			TextViewRemind.setText(mRemindValue + "小时前提醒");
		if (mRemindType.equals("d"))
			TextViewRemind.setText(mRemindValue + "天前提醒");
		if (mRemindType.equals("o"))
			TextViewRemind.setText("不提醒");

		TextViewRepeat.setText(BUAffar.getRepeatTypeName(mRepeatType));
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "时间", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	public void onLinearBeginTimeClick(View v) {
		mSetTimeFlag = 0;
		String vStr = "";
		Calendar calendar = Calendar.getInstance();
		Date aa = calendar.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		vStr = df.format(aa);
		if (!mBeginTime.equals(""))
			vStr = mBeginTime;

		Log.d("xmx", "begintime:" + vStr);
		WorkDateTimePickerDialog datePicKDialog = new WorkDateTimePickerDialog(
				WorkNewTimeActivity.this, vStr);
		datePicKDialog.dateTimePicKDialog(0);

		datePicKDialog.setDayChangeListener(WorkNewTimeActivity.this);

        InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (inputmanger.isActive())
        {
        	inputmanger.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
	}

	public void onLinearEndTimeClick(View v) {
		Log.d("xmx", "onLinearEndTimeClick:" + mBeginTime + " endTime:"
				+ mEndTime);
		if (mBeginTime.equals("")) {
			Toast.makeText(this, "请先输入开始时间", Toast.LENGTH_SHORT).show();
		} else {
			try {
				mSetTimeFlag = 1;
				String vStr = "";
				if (!mEndTime.equals(""))
					vStr = mEndTime;
				else
					vStr = mBeginTime;

				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				Date vDateBegin = df.parse(mBeginTime);
				Date vDateEnd = df.parse(vStr);

				if (vDateEnd.getTime() < vDateBegin.getTime()) {
					vStr = mBeginTime;
				}
				Log.d("xmx", "vStr:" + vStr + " vDateBegin.getTime():"+vDateBegin.getTime());
				WorkDateTimePickerDialog datePicKDialog = new WorkDateTimePickerDialog(
						WorkNewTimeActivity.this, vStr);
				datePicKDialog.dateTimePicKDialog(vDateBegin.getTime());

				datePicKDialog.setDayChangeListener(WorkNewTimeActivity.this);
				 InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			        if (inputmanger.isActive())
			        {
			        	inputmanger.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			        }
			} catch (ParseException e) {
				Log.d("xmx", "cash endtime");
				e.printStackTrace();
			}
		}
	}

	public void onLinearRemainClick(View v) {
		Intent intent = new Intent(WorkNewTimeActivity.this,
				WorkNewRemaindActivity.class);
		intent.putExtra("RemindValue", mRemindValue);
		intent.putExtra("RemindType", mRemindType);
		startActivityForResult(intent, 100);
	}

	public void onLinearRepeatClick(View v) {
		Intent intent = new Intent(WorkNewTimeActivity.this,
				WorkNewRepeatActivity.class);
		intent.putExtra("RepeatType", mRepeatType);
		startActivityForResult(intent, 101);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean vCanReturn = true;
		if (101 == item.getItemId()) {
			if (vCanReturn) {
				try {
					if (mBeginTime.equals("")) {
						Toast.makeText(this, "请设置开始时间", Toast.LENGTH_SHORT)
								.show();
						return true;
					}
					
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyyMMdd HH:mm:ss");
					if (!mEndTime.equals("")) {
						Log.d("xmx","mAffar !=null");
						Date vDateBegin = df.parse(mBeginTime);
						Date vDateEnd = df.parse(mEndTime);
						if (vDateEnd.getTime()<vDateBegin.getTime())
						{
							Toast.makeText(this, "设置截止时间要大于开始时间", Toast.LENGTH_SHORT)
							.show();
							return true;
						}
					}
					
					
					Log.d("xmx", "type:" + mRemindType + ",value:"
							+ mRemindValue);
					Intent intent = getIntent();
					intent.putExtra("RemindValue", mRemindValue);
					intent.putExtra("RemindType", mRemindType);
					intent.putExtra("RepeatType", mRepeatType);

					intent.putExtra("BeginTime", mBeginTime);
					intent.putExtra("EndTime", mEndTime);

					setResult(1000, intent);
					finish();
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100 && resultCode == 1000) {
			mRemindValue = data.getIntExtra("RemindValue", 0);
			mRemindType = data.getStringExtra("RemindType");

			resetViewData();
		}
		if (requestCode == 101 && resultCode == 1000) {
			mRepeatType = data.getStringExtra("RepeatType");
			resetViewData();
		}
	}

	@Override
	public void onDayChagne(String outDay) {
		Log.d("xmx", "outDay:" + outDay);
		if (mSetTimeFlag == 0)
			mBeginTime = outDay;
		else
			mEndTime = outDay;

		resetViewData();
		/*
		 * int vYear=Integer.parseInt(mCurrentDate.substring(0, 4)); int
		 * vMonth=Integer.parseInt(mCurrentDate.substring(4, 6)); int
		 * vDay=Integer.parseInt(mCurrentDate.substring(6, 8));
		 * Log.d("xmx","vYear:"+mCurrentDate.substring(0, 4) +
		 * " vMonth:"+mCurrentDate.substring(4,
		 * 6)+" vDay:"+mCurrentDate.substring(6, 8));
		 * mCalendarLayout.setYearMonth(vYear, vMonth, vDay);
		 */

	}

	public static String getDateStrByDayNew(String inDate) {
		if (inDate==null)
			return "";
		if (inDate.equals(""))
			return "";
		try {
			Log.d("xmx", "getDataStrByDate:" + inDate);

			Calendar vcalendarInDate = Calendar.getInstance();
			int vInYear = Integer.parseInt(inDate.substring(0, 4));
			int vInMonth = Integer.parseInt(inDate.substring(4, 6));
			int vInDay = Integer.parseInt(inDate.substring(6, 8));
			vcalendarInDate.set(vInYear, vInMonth, vInDay);

			Calendar vcalendarNow = Calendar.getInstance();
			String vCurrentDate = vcalendarNow.get(Calendar.YEAR)
					+ WorkDatePickerDialog.intToStr2(vcalendarNow
							.get(Calendar.MONTH) + 1)
					+ WorkDatePickerDialog.intToStr2(vcalendarNow
							.get(Calendar.DAY_OF_MONTH));
			//Log.d("xmx", "vCurrentDate:" + vCurrentDate + " vcalendarNow:"
			//		+ vcalendarNow);

			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date vinDate = df.parse(inDate);
			Log.d("xmx", "vinDate:" + vinDate.toString());
			Date vCurDate = df.parse(vCurrentDate);
			Log.d("xmx", "vCurDate:" + vCurDate.toString());
			long diff = vinDate.getTime() - vCurDate.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);
			Log.d("xmx", "vinDate:" + vinDate + " vCurDate:" + " days:" + days);

			// 20150101 12:00:00
			if (days == -1) {
				return "昨天 " + inDate.substring(9, 14);
			} else if (days == 0) {
				return "今天 " + inDate.substring(9, 14);
			} else if (days == 1) {
				return "明天 " + inDate.substring(9, 14);
			} else if (vInYear == vcalendarNow.get(Calendar.YEAR)) {
				return vInMonth + "月" + vInDay + "日 " + inDate.substring(9, 14);
			} else {
				return vInYear + "年" + vInMonth + "月" + vInDay + "日 "
						+ inDate.substring(9, 14);
			}

		} catch (Exception e) {
			Log.d("xmx", "cash");
		}
		return "";
	}

	public String getDateStrByDay(String inDate) {
		if (inDate.equals(""))
			return "";
		else {
			try {
				Date vDateCur = new Date();

				Calendar vCalendarCur = Calendar.getInstance();
				Calendar vCalendarInTime = Calendar.getInstance();

				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mi:ss");
				Date vDateInTime;

				vDateInTime = df.parse(inDate);

				if (vCalendarCur.get(Calendar.YEAR) == vDateInTime.getYear()) {
					int vMonth = vDateInTime.getMonth() + 1;
					return vMonth + "月" + vDateInTime.getDay() + "日" + " "
							+ vDateInTime.getHours() + ":"
							+ vDateInTime.getMinutes();
				} else {
					int vMonth = vDateInTime.getMonth() + 1;
					return vDateInTime.getYear() + "年" + vMonth + "月"
							+ vDateInTime.getDay() + "日" + " "
							+ vDateInTime.getHours() + ":"
							+ vDateInTime.getMinutes();
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
			/*
			 * vDateInTyme.getYear()
			 * 
			 * int vYear=Integer.parseInt(inDate.substring(0, 4)); int
			 * vMonth=Integer.parseInt(inDate.substring(4, 6)); int
			 * vDay=Integer.parseInt(inDate.substring(6, 8));
			 * vCalendarInTime.set(vYear, vMonth,vDay);
			 * 
			 * if (vCalendarCur.get(Calendar.YEAR)==vYear) { return }
			 * Log.d("xmx","vYear:"+inDate.substring(0, 4) +
			 * " vMonth:"+inDate.substring(4, 6)+" vDay:"+inDate.substring(6,
			 * 8));
			 */

		}
	}
}
