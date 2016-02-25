package com.tr.ui.people.cread;

import java.util.ArrayList;

import com.tr.R;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 社会活动(子模块)
 * 
 * @author Wxh07151732
 * 
 */
public class SocietyActivity extends BaseActivity implements OnClickListener {
	private TextView finish_society_Tv;
	private MyEditTextView activity_Type_Etv;
	private MyEditTextView introducer_Etv;
	private MyEditTextView fellow_villager_Etv;
	private MyEditTextView custom_activity_Etv;
	private ArrayList<String> list;
	private Intent intent;
	private LinearLayout society_Ll;
	private LinearLayout continueadd_Ll;
	private LinearLayout delete_Ll;
	private RelativeLayout quit_society_Rl;
	private ArrayList<String> list1;
	private ArrayList<MyEditTextView> list2;
	private ArrayList<MyLineraLayout> layouts;
	private int count;
	private LinearLayout society_main_Ll;
	private boolean isNull;
	private ArrayList<MyEditTextView> editTextViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_society);
		init();
		editTextViews = new ArrayList<MyEditTextView>();
		list = new ArrayList<String>();
		list2 = new ArrayList<MyEditTextView>();
		layouts = new ArrayList<MyLineraLayout>();
	}

	private void init() {
		quit_society_Rl = (RelativeLayout) findViewById(R.id.quit_society_Rl);
		finish_society_Tv = (TextView) findViewById(R.id.finish_society_Tv);
		activity_Type_Etv = (MyEditTextView) findViewById(R.id.activity_Type_Etv);
		introducer_Etv = (MyEditTextView) findViewById(R.id.introducer_Etv);
		fellow_villager_Etv = (MyEditTextView) findViewById(R.id.fellow_villager_Etv);
		custom_activity_Etv = (MyEditTextView) findViewById(R.id.custom_activity_Etv);
		society_Ll = (LinearLayout) findViewById(R.id.society_Ll);
		society_main_Ll = (LinearLayout) findViewById(R.id.society_main_Ll);
		continueadd_Ll = (LinearLayout) findViewById(R.id.continueadd_Ll);
		delete_Ll = (LinearLayout) findViewById(R.id.delete_Ll);
		TextView continueadd_Tv = (TextView) findViewById(R.id.continueadd_Tv);
		continueadd_Tv.setText("新增社会活动");
		quit_society_Rl.setOnClickListener(this);
		delete_Ll.setOnClickListener(this);
		continueadd_Ll.setOnClickListener(this);

		activity_Type_Etv.setOnClickListener(this);
		finish_society_Tv.setOnClickListener(this);
		introducer_Etv.setOnClickListener(this);
		custom_activity_Etv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finish_society_Tv:
			if (!TextUtils.isEmpty(activity_Type_Etv.getText())) {
				list.add(activity_Type_Etv.getTextLabel() + "_"
						+ activity_Type_Etv.getText());

			}
			if (!TextUtils.isEmpty(introducer_Etv.getText())) {
				list.add(introducer_Etv.getTextLabel() + "_"
						+ introducer_Etv.getText());

			}

			if (!TextUtils.isEmpty(fellow_villager_Etv.getText())) {
				list.add(fellow_villager_Etv.getTextLabel() + "_"
						+ fellow_villager_Etv.getText());

			}
			if (!TextUtils.isEmpty(custom_activity_Etv.getText())) {
				list.add(custom_activity_Etv.getTextLabel() + "_"
						+ custom_activity_Etv.getText());

			}
			if (list2 != null) {
				for (int i = 0; i < list2.size(); i++) {
					if (!TextUtils.isEmpty(list2.get(i).getText())) {
						list.add(list2.get(i).getTextLabel() + "_"
								+ list2.get(i).getText());
					}
				}
			}
			if (!"[]".equals(list.toString())) {
				Intent intent = new Intent(context,
						NewConnectionsActivity.class);
				intent.putStringArrayListExtra("Society", list);
				setResult(8, intent);
			}
			finish();
			break;
		case R.id.custom_activity_Etv:
			intent = new Intent(this, CustomActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.quit_society_Rl:
			finish();
			break;
		case R.id.delete_Ll:
			if (!(count == 0)) {
				society_Ll.removeAllViews();
			} else {
				finish();
			}
			count--;
			break;
		case R.id.continueadd_Ll:
			final MyLineraLayout layout = new MyLineraLayout(context);
			MyDeleteView deleteView = new MyDeleteView(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			list1 = new ArrayList<String>();
			list1.add(activity_Type_Etv.getTextLabel());
			list1.add(introducer_Etv.getTextLabel());
			list1.add(fellow_villager_Etv.getTextLabel());
			list1.add(custom_activity_Etv.getTextLabel());
			for (int i = 0; i < list1.size(); i++) {
				final MyEditTextView MyEditTextView = new MyEditTextView(context);
				String text = list1.get(i);
				if (i == 0) {
					MyEditTextView.setChoose(true);
					MyEditTextView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mDictionary.put( MyEditTextView.getMyEdit_Id(), MyEditTextView);
							mLineraDictionary.put(MyEditTextView.getMyEdit_Id(), layout);
							Intent intent = new Intent(SocietyActivity.this,
									SocietyTypeActivity.class);
							intent.putExtra("Society_Type_ID", MyEditTextView.getMyEdit_Id());
							startActivityForResult(intent, 1);
						}
					});
				}
				if (i == list1.size() - 1) {
					MyEditTextView.setAddMore_hint(true);
					MyEditTextView.setChoose(true);
					MyEditTextView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mDictionary.put( MyEditTextView.getMyEdit_Id(), MyEditTextView);
							mLineraDictionary.put(MyEditTextView.getMyEdit_Id(), layout);
							Intent intent = new Intent(SocietyActivity.this,
									CustomActivity.class);
							intent.putExtra("Society_Custom_ID", MyEditTextView.getMyEdit_Id());
							startActivityForResult(intent, 1);
						}
					});
				}
				list2.add(MyEditTextView);
				MyEditTextView.setTextLabel(text);
				layout.addView(MyEditTextView, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
			layout.addView(deleteView);
			deleteView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					society_main_Ll.removeView(layout);
					count--;
				}
			});
			layouts.add(layout);
			society_main_Ll.addView(layout,2, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			count++;
			list1.removeAll(list1);
			break;
		case R.id.activity_Type_Etv:
			intent = new Intent(this, SocietyTypeActivity.class);
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode == 0) {
				switch (resultCode) {
				case 0:
					String society_type = data.getStringExtra("society_type");
					activity_Type_Etv.setText(society_type);
					break;
				case 999:
					custom2(data, custom_activity_Etv, society_Ll, isNull,editTextViews);
					break;
				default:
					break;
				}
			}
			if (requestCode == 1) {
				String Society_Custom_ID = data.getStringExtra("Society_Custom_ID");
				String society_Type_ID = data.getStringExtra("society_Type_ID");
				switch (resultCode) {
				case 0:
					String society_type = data.getStringExtra("society_type");
					MyEditTextView myEditTextView = mDictionary.get(society_Type_ID);
					myEditTextView.setText(society_type);
					break;
				case 999:
					custom2(data, mDictionary.get(Society_Custom_ID), mLineraDictionary.get(Society_Custom_ID), isNull,editTextViews);
					break;
				default:
					break;
				}
			}
		}
	}
}
