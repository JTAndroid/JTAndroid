package com.tongmeng.alliance.view;

import java.util.Calendar;

import com.tongmeng.alliance.adapter.ArrayWheelAdapter;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SimpleBottomDialog extends Dialog {

	private Context context;
	String[] years = { "2015", "2016", "2017", "2018", "2019", "2020" };
	String[] months = { "01", "02", "03", "04", "05", "06", "07", "08", "09",
			"10", "11", "12" };
	String[] days = { "01", "02", "03", "04", "05", "06", "07", "08", "09",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
			"21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
	String[] hours = { "00", "01", "02", "03", "04", "05", "06", "07", "08",
			"09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23" };
	String[] minutes = { "00", "05", "10", "15", "20", "25", "30", "35", "40",
			"45", "50", "55" };
	WheelView wheelview1, wheelview2, wheelview3, wheelview4, wheelview5;
	ArrayWheelAdapter adapter1, adapter2, adapter3, adapter4, adapter5;
	TextView cancleBtn, okBtn;
	TextView view;

	public SimpleBottomDialog(Context context, TextView view) {
		this(context, R.style.Theme_Dialog_From_Bottom);
		// TODO Auto-generated constructor stub
		this.view = view;
	}

	public SimpleBottomDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	private void init() {
		this.setCanceledOnTouchOutside(true);
		this.setCancelable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);

		initViews();
		initValues();
	}

	private void initViews() {
		// findViewById(R.id.btn_0).setOnClickListener(clickListener);
		// findViewById(R.id.btn_1).setOnClickListener(clickListener);
		wheelview1 = (WheelView) findViewById(R.id.wheelview1);
		wheelview2 = (WheelView) findViewById(R.id.wheelview2);
		wheelview3 = (WheelView) findViewById(R.id.wheelview3);
		wheelview4 = (WheelView) findViewById(R.id.wheelview4);
		wheelview5 = (WheelView) findViewById(R.id.wheelview5);

		adapter1 = new ArrayWheelAdapter<String>(years);
		adapter2 = new ArrayWheelAdapter<String>(months);
		adapter3 = new ArrayWheelAdapter<String>(days);
		adapter4 = new ArrayWheelAdapter<String>(hours);
		adapter5 = new ArrayWheelAdapter<String>(minutes);

		wheelview1.setAdapter(adapter1);
		wheelview2.setAdapter(adapter2);
		wheelview3.setAdapter(adapter3);
		wheelview4.setAdapter(adapter4);
		wheelview5.setAdapter(adapter5);

		cancleBtn = (TextView) findViewById(R.id.activity_time_cancleBtn);
		cancleBtn.setOnClickListener(clickListener);

		okBtn = (TextView) findViewById(R.id.activity_time_okBtn);
		okBtn.setOnClickListener(clickListener);
	}

	private void initValues() {
		// 不能写在init()中
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		lp.width = dm.widthPixels;// 让dialog的宽占满屏幕的宽
		lp.gravity = Gravity.BOTTOM;// 出现在底部
		window.setAttributes(lp);
	}

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.activity_time_okBtn:
				String year = years[wheelview1.getCurrentItem()];
				String month = months[wheelview2.getCurrentItem()];
				String day = days[wheelview3.getCurrentItem()];
				String hour = hours[wheelview4.getCurrentItem()];
				String munite = minutes[wheelview5.getCurrentItem()];
				Log.e("StartTimeActivity::", "year:" + year);
				Log.e("StartTimeActivity::", "month:" + month);
				Log.e("StartTimeActivity::", "day:" + day);
				Log.e("StartTimeActivity::", "hour:" + hour);
				Log.e("StartTimeActivity::", "munite:" + munite);
				String returnTime = year + "-" + month + "-" + day + " " + hour
						+ ":" + munite;
				Log.e("", "返回的时间是：：" + returnTime);
				Calendar calendar = Calendar.getInstance();
				String currentTime = calendar.get(Calendar.YEAR) + "-"
						+ calendar.get(Calendar.MONTH) + "-"
						+ calendar.get(Calendar.DAY_OF_MONTH) + " "
						+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
						+ calendar.get(Calendar.MINUTE);

				if (Utils.isValidDate(returnTime)) {
					if (Utils.compare_date(currentTime, returnTime.toString()) == 1) {
						Toast.makeText(context, "您所选的时间应该大于当前时间", 0).show();
					} else {
						view.setText(returnTime);
					}
				} else {
					Toast.makeText(context, "日期不合法，请选择正确的日期", 0).show();
				}

				dismiss();
				break;

			case R.id.activity_time_cancleBtn:
				dismiss();
				break;
			}
		}

	};

	private OnOKClickListener onOKClickListener;

	public interface OnOKClickListener {
		public void onOKClick(View v);
	}

	public void setOnOKClickListener(OnOKClickListener onOKClickListener) {
		this.onOKClickListener = onOKClickListener;
	}
}
