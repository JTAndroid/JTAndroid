package com.tr.ui.conference.initiatorhy;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TimePicker;

import com.tr.R;
import com.tr.model.conference.MCalendarSelectDateTime;
import com.utils.time.Util;

public class CalendarTimeSelectionActivity extends Activity {
	private TimePicker time_Start;
	private TimePicker time_End;
	Calendar calendar = Calendar.getInstance();
	ArrayList<MCalendarSelectDateTime> mettingListDT=null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_calendar);
		time_Start = (TimePicker) findViewById(R.id.time_Start);
		time_End = (TimePicker) findViewById(R.id.time_End);
		time_Start.setIs24HourView(true);
		time_End.setIs24HourView(true);
		ObtainTime();
		}
	public void ObtainTime(){
		mettingListDT = (ArrayList<MCalendarSelectDateTime>) getIntent().getSerializableExtra("mettingListDT");
		for (int i = 0; i < mettingListDT.size(); i++) {
			time_Start.setCurrentHour(mettingListDT.get(i).startHour);
			time_Start.setCurrentMinute(mettingListDT.get(i).startMinute);
			time_End.setCurrentHour(mettingListDT.get(i).endHour);
			time_End.setCurrentMinute(mettingListDT.get(i).endMinute);
		}
	}
	public void click(View view) {
		MCalendarSelectDateTime md = new MCalendarSelectDateTime();
		md.startHour=time_Start.getCurrentHour();
		md.startMinute= time_Start.getCurrentMinute();
		md.endHour=time_End.getCurrentHour();
		md.endMinute=time_End.getCurrentMinute();
		Bundle b = new Bundle();
		b.putSerializable("md", md);
		b.putSerializable("position",getIntent().getSerializableExtra("position"));
		//b.putSerializable("mettingListDT",(ArrayList<MCalendarSelectDateTime>) getIntent().getSerializableExtra("mettingListDT"));
		//b.putInt("position", (Integer) getIntent().getSerializableExtra("position"));
		Util.activitySetResult(CalendarTimeSelectionActivity.this, CalendarActivity.class, b);
		finish();

	}
	public void onBackPressed() {
		MCalendarSelectDateTime md = new MCalendarSelectDateTime();
		for (int i = 0; i < mettingListDT.size(); i++) {
			md.startHour=mettingListDT.get(i).startHour;
			md.startMinute=mettingListDT.get(i).startMinute;
			md.endHour=mettingListDT.get(i).endHour;
			md.endMinute=mettingListDT.get(i).endMinute;
		}
		Bundle b = new Bundle();
		b.putSerializable("md", md);
		b.putSerializable("position",getIntent().getSerializableExtra("position"));
		Util.activitySetResult(CalendarTimeSelectionActivity.this, CalendarActivity.class, b);
		finish();
	}
}