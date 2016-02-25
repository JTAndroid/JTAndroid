package com.tr.ui.organization.create_clientele;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.NumberPicker.OnScrollListener;

import com.tr.R;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.WorkExperienceActivity;
//import com.tr.ui.people.cread.WorkExperienceActivity;
/**
 * 上市时间
 * @author Wxh07151732
 *
 */
public class MarkeyDataActivity extends BaseActivity {
	private NumberPicker numberPicker;
	String[] year;
	String[] month;
	String[] day;
	private String startTemp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_markey_data);
		setActionBarLayout(R.layout.people_actionbar);
		numberPicker = (NumberPicker) findViewById(R.id.year_markey);
		
		clickNumberPicker(numberPicker);
		
		numberPicker = (NumberPicker) findViewById(R.id.month_markey);
		clickNumberPicker(numberPicker);
		
		numberPicker = (NumberPicker) findViewById(R.id.day_markey);
		clickNumberPicker(numberPicker);
		initDialogView();
	}
	public void clickNumberPicker(NumberPicker numberPicker){
		
		numberPicker.getChildAt(0).setFocusable(false);
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
			view.setText("上市时间");
			ImageView reminder_time_backs = (ImageView) v.findViewById(R.id.reminder_time_backs);
			reminder_time_backs.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			TextView time_click_complete = (TextView) v.findViewById(R.id.time_click_complete);
			time_click_complete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (!TextUtils.isEmpty(startTemp)) {
						Intent intent = new Intent(context, WorkExperienceActivity.class);
						intent.putExtra("Markey_Data", startTemp);
						setResult(9000,intent);
						finish();
					}
				}
			});
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			actionBar.setCustomView(v, layout);
		}
	}
	private void initDialogView() {
		Calendar c = Calendar.getInstance();
		String label[] = { "年", "", "月", "", "日"};
		final List<NumberPicker> wheelViews1 = new ArrayList<NumberPicker>();
		LinearLayout linearWheel1 = (LinearLayout) findViewById(R.id.linear_markey_wheel1);
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH + 1);
		int curDay = c.get(Calendar.DAY_OF_MONTH);
		
//		Toast.makeText(getApplicationContext(),
//				curYear + "年" + curMonth + "月" + curDay + "日", 0).show();
		for (int i = 0; i < linearWheel1.getChildCount(); i += 2) {
			NumberPicker view1 = (NumberPicker) linearWheel1.getChildAt(i);
			// view1.setDividerDrawable(getResources().getDrawable(R.drawable.divider));
			switch (i) {
			case 0:
				year = new String[500];
				for (int j = 0; j < year.length; j++) {
					year[j] = (j + 1900) + "年";
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
			wheelViews1.add(view1);
			
		}
		
		for (int i = 0; i < wheelViews1.size(); i++) {
			wheelViews1.get(i).setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChange(NumberPicker view,
						int scrollState) {
					Log.v("sdss", view.getValue() + "");
					getStartTime(wheelViews1);
				}
			});
		}
		
		getStartTime(wheelViews1);
	}
	
	public void getStartTime(final List<NumberPicker> wheelViews) {
//		String week[] = { "  星期一", "  星期二", "  星期三", "  星期四", "  星期五", "  星期六", "  星期日" };
		
		for (int i = 0; i < wheelViews.size(); i++) {
			switch (i) {
			case 0:
				startTemp = "";
				startTemp += year[wheelViews.get(i).getValue()];
				break;
			case 1:
				startTemp += month[wheelViews.get(i).getValue()];
				break;
			case 2:
				try {
					startTemp += startTemp = day[wheelViews.get(i).getValue()]
							;
				} catch (Exception e) {
					e.printStackTrace();
				}
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
}
