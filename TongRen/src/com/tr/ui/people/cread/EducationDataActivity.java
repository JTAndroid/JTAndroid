package com.tr.ui.people.cread;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import com.baidu.navisdk.ui.routeguide.fsm.RouteGuideFSM.IFSMDestStateListener;
import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.home.AddTaskActivity;
import com.tr.ui.tongren.home.AddTaskActivity.TaskType;
import com.tr.ui.tongren.home.fragment.ProjectDetailfragment;
import com.tr.ui.tongren.model.task.AddTask;
import com.utils.http.EAPIConsts;
import com.utils.log.ToastUtil;
import com.utils.time.TimeUtil;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.TextView;

/**
 * 就读时间
 * @author Wxh07151732
 *
 */
public class EducationDataActivity extends JBaseActivity{

		private NumberPicker numberPicker;
		String[] year;
		String[] month;
		String[] day;
		private String startTemp;
		private String End_temp;
		private String education_Data_ID;
		private int curStartYear;
		private int curStartMonth;
		private int curStartDay;
		private String startTime;
		private String endTime;
		private int curEndYear;
		private int curEndMonth;
		private int curEndDay;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.people_education_data);
			startTime = getIntent().getStringExtra("startTime");
			endTime = getIntent().getStringExtra("endTime");
			education_Data_ID = this.getIntent().getStringExtra("Education_Data_ID");
			numberPicker = (NumberPicker) findViewById(R.id.year_start);
			clickNumberPicker(numberPicker);
			numberPicker = (NumberPicker) findViewById(R.id.month_start);
			clickNumberPicker(numberPicker);
			numberPicker = (NumberPicker) findViewById(R.id.day_start);
			clickNumberPicker(numberPicker);
			numberPicker = (NumberPicker) findViewById(R.id.year_end);
			clickNumberPicker(numberPicker);
			numberPicker = (NumberPicker) findViewById(R.id.month_end);
			clickNumberPicker(numberPicker);
			
			numberPicker = (NumberPicker) findViewById(R.id.day_end);
			clickNumberPicker(numberPicker);
			initDialogView();
		}
		public void clickNumberPicker(NumberPicker numberPicker){
			
			numberPicker.getChildAt(0).setFocusable(false);
		}
		
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.menu_createflow, menu);
			menu.findItem(R.id.flow_create).setTitle("完成");
			return true;
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.flow_create:
				if (!TextUtils.isEmpty(startTemp)&&!TextUtils.isEmpty(End_temp)) {
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");  
					try {
						Date startDate = sdf.parse(startTemp);
						Date endDate = sdf.parse(End_temp);
						if (endDate.before(startDate)) {
							ToastUtil.showToast(EducationDataActivity.this, "结束时间必须晚于开始时间");
							return false;
						}
						Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
						long diff = curDate.getTime() - startDate.getTime();
						long days = diff / (1000 * 60 * 60 * 24);
						if (ProjectDetailfragment.projectValidityLimitTime!=0&&startDate.getTime()>ProjectDetailfragment.projectValidityLimitTime) {
							ToastUtil.showToast(EducationDataActivity.this, "不可超过项目的时间限制范围");
							return false;
						}
						if (ProjectDetailfragment.projectValidityLimitTime!=0&&endDate.getTime()>ProjectDetailfragment.projectValidityLimitTime) {
							ToastUtil.showToast(EducationDataActivity.this, "不可超过项目的时间限制范围");
							return false;
						}
						if (days>1) {
							ToastUtil.showToast(EducationDataActivity.this, "开始时间必须晚于当前时间");
							return false;
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Intent intent = new Intent(EducationDataActivity.this, EducationActivity.class);
					intent.putExtra("Education_Data", startTemp+"至"+End_temp);
					if (education_Data_ID!=null) {
						intent.putExtra("education_Data_ID", education_Data_ID);
					}
					setResult(3,intent);
					finish();
				}
				break;
			}
			return super.onOptionsItemSelected(item);
		}
		private void initDialogView() {
			Calendar c = Calendar.getInstance();
			String label[] = { "年", "", "月", "", "日"};
			final List<NumberPicker> wheelViews1 = new ArrayList<NumberPicker>();
			final List<NumberPicker> wheelViews2 = new ArrayList<NumberPicker>();
			LinearLayout linearWheel1 = (LinearLayout) findViewById(R.id.linear_education_wheel1);
			LinearLayout linearWheel2 = (LinearLayout) findViewById(R.id.linear_education_wheel2);
			if (!TextUtils.isEmpty(startTime)) {
				Date dateTimeWithSDF_DATE_CHI = TimeUtil.getDateTimeWithSDF_DATE_CHI(startTime);
				curStartYear = 2016;
				curStartMonth=  dateTimeWithSDF_DATE_CHI.getMonth()+1;
				curStartDay = dateTimeWithSDF_DATE_CHI.getDate();
			}else{
				curStartYear = c.get(Calendar.YEAR);
				curStartMonth = (c.get(Calendar.MONTH))+1;
				curStartDay = c.get(Calendar.DAY_OF_MONTH);
			}
			
			
//			Toast.makeText(getApplicationContext(),
//					curYear + "年" + curMonth + "月" + curDay + "日", 0).show();
			
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
					view1.setValue(curStartYear
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
					view1.setValue(curStartMonth - 1);
					break;
				case 4:
					day = new String[31];
					for (int l = 0; l < day.length; l++) {
						day[l] = String.format("%02d", l + 1) + "日";
					}
					view1.setDisplayedValues(day);
					view1.setMinValue(0);
					view1.setMaxValue(day.length - 1);
					view1.setValue(curStartDay - 1);
					break;
				}
				// view1.setLabel(label[i]);

				setDevider(view1);
				wheelViews1.add(view1);
			}
			
			if (!TextUtils.isEmpty(endTime)) {
				Date dateTimeWithSDF_DATE_CHI = TimeUtil.getDateTimeWithSDF_DATE_CHI(endTime);
				curEndYear = 2016;
				curEndMonth=  dateTimeWithSDF_DATE_CHI.getMonth()+1;
				curEndDay = dateTimeWithSDF_DATE_CHI.getDate();
			}else{
				curEndYear = c.get(Calendar.YEAR);
				curEndMonth = (c.get(Calendar.MONTH))+1;
				curEndDay = c.get(Calendar.DAY_OF_MONTH);
			}
			
			for (int i = 0; i < linearWheel2.getChildCount(); i += 2) {
				NumberPicker view1 = (NumberPicker) linearWheel2.getChildAt(i);
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
					view1.setValue(curEndYear
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
					view1.setValue(curEndMonth - 1);
					break;
				case 4:
					day = new String[31];
					for (int l = 0; l < day.length; l++) {
						day[l] = String.format("%02d", l + 1) + "日";
					}
					view1.setDisplayedValues(day);
					view1.setMinValue(0);
					view1.setMaxValue(day.length - 1);
					view1.setValue(curEndDay - 1);
					break;
				}
				// view1.setLabel(label[i]);
				
				setDevider(view1);

				wheelViews2.add(view1);
				
				
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
			for (int i = 0; i < wheelViews2.size(); i++) {
				wheelViews2.get(i).setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScrollStateChange(NumberPicker view,
							int scrollState) {
						Log.v("sdss", view.getValue() + "");
						getEndTime(wheelViews2);
					}
				});
			}
			getEndTime(wheelViews2);
			getStartTime(wheelViews1);
		}
		
		
		private void getEndTime(List<NumberPicker> wheelViews) {
			for (int i = 0; i < wheelViews.size(); i++) {
				switch (i) {
				case 0:
					End_temp = "";
					End_temp += year[wheelViews.get(i).getValue()];
					break;
				case 1:
					End_temp += month[wheelViews.get(i).getValue()];
					break;
				case 2:
					try {
						End_temp += End_temp = day[wheelViews.get(i).getValue()]
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
		public void getStartTime(final List<NumberPicker> wheelViews) {
//			String week[] = { "  星期一", "  星期二", "  星期三", "  星期四", "  星期五", "  星期六", "  星期日" };
			
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
		@Override
		public void initJabActionBar() {
			ActionBar jabGetActionBar = jabGetActionBar();
			HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "时间", false, null, true, true);
		}
	}

