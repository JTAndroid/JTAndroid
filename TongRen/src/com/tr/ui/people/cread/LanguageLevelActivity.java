package com.tr.ui.people.cread;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.tr.R;

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
/**
 * 外语语种等级 
 */
public class LanguageLevelActivity extends BaseActivity {
	private NumberPicker numberPicker;
	private String language_level;
	private String languages[];
	private String levels[];
	private String language_level_ID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_language_level);
		language_level_ID = this.getIntent().getStringExtra("Language_level_ID");
		setActionBarLayout(R.layout.people_actionbar);
		numberPicker = (NumberPicker) findViewById(R.id.language);
		
		clickNumberPicker(numberPicker);
		
		numberPicker = (NumberPicker) findViewById(R.id.level);
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
			view.setText("外语语种等级");
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
					if (!TextUtils.isEmpty(language_level)) {
						Intent intent = new Intent(context, EducationActivity.class);
						intent.putExtra("Language_level",language_level);
						if (language_level_ID!=null) {
							intent.putExtra("language_level_ID",language_level_ID);
						}
						setResult(4,intent);
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
		final List<NumberPicker> wheelViews1 = new ArrayList<NumberPicker>();
		LinearLayout linearWheel1 = (LinearLayout) findViewById(R.id.linear_language_wheel1);
		languages = new String[] {"日语","英语","法语","德语","韩语","俄语","西班牙语","葡萄牙语","阿拉伯语","意大利语"};
		levels = new String[] {"一级","二级","三级","四级","五级","六级","七级","八级"};
//		Toast.makeText(getApplicationContext(),
//				curlanguage + "年" + curlevel + "月" + curDay + "日", 0).show();
		for (int i = 0; i < linearWheel1.getChildCount(); i ++) {
			NumberPicker view1 = (NumberPicker) linearWheel1.getChildAt(i);
			// view1.setDividerDrawable(getResources().getDrawable(R.drawable.divider));
			switch (i) {
			case 0:
				view1.setDisplayedValues(languages);
				view1.setMinValue(0);
				view1.setMaxValue(languages.length - 1);
				break;
			case 1:
				view1.setDisplayedValues(levels);
				view1.setMinValue(0);
				view1.setMaxValue(levels.length - 1);
				break;
			}
			// view1.setLabel(label[i]);
			setDevider(view1);
			wheelViews1.add(view1);
		}
			// view1.setLabel(label[i]);

		for (int i = 0; i < wheelViews1.size(); i++) {
			wheelViews1.get(i).setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChange(NumberPicker view,
						int scrollState) {
					Log.v("sdss", view.getValue() + "");
					getLanguage_level(wheelViews1);
				}
			});
		}
		getLanguage_level(wheelViews1);
	}
	public void getLanguage_level(final List<NumberPicker> wheelViews) {
		
		for (int i = 0; i < wheelViews.size(); i++) {
			switch (i) {
			case 0:
				language_level = "";
				language_level += languages[wheelViews.get(i).getValue()];
				break;
			case 1:
				language_level += levels[wheelViews.get(i).getValue()];
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
