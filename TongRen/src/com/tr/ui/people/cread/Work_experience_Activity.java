package com.tr.ui.people.cread;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;

/**
 * 工作经历
 * 
 * @author Wxh07151732
 * 
 */
public class Work_experience_Activity extends BaseActivity implements
		OnClickListener {
	private TextView finish_work_Tv;
	private MyEditTextView data_work_Etv;
	private MyEditTextView unit_work_Etv;
	private MyEditTextView industry_work_Etv;
	private MyEditTextView phone_work_Etv;
	private MyEditTextView certifier_work_Etv;
	private MyEditTextView section_work_Etv;
	private MyEditTextView duty_work_Etv;
	private MyEditTextView colleague_work_Etv;
	private MyEditTextView custom_work_Etv;
	private CheckBox work_experience_Cb;
	private ArrayList<String> list;
	private Intent intent;
	private LinearLayout continueadd_Ll;
	private LinearLayout delete_Ll;
	private LinearLayout work_Ll;
	private RelativeLayout work_experience_Rl;
	private RelativeLayout quit_work_Rl;
	private ArrayList<String> list1;
	private ArrayList<MyEditTextView> list2;
	private ArrayList<MyLineraLayout> layouts;
	private int count;
	private LinearLayout work_main_Ll;
	private boolean isNull;
	private ArrayList<MyEditTextView> editTextViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_work_experience);
		init();
		editTextViews = new ArrayList<MyEditTextView>();
		list = new ArrayList<String>();
		list1 = new ArrayList<String>();
		list2 = new ArrayList<MyEditTextView>();
		layouts = new ArrayList<MyLineraLayout>();
	}

	private void init() {
		quit_work_Rl = (RelativeLayout) findViewById(R.id.quit_work_Rl);
		finish_work_Tv = (TextView) findViewById(R.id.finish_work_Tv);
		data_work_Etv = (MyEditTextView) findViewById(R.id.data_work_Etv);
		unit_work_Etv = (MyEditTextView) findViewById(R.id.unit_work_Etv);
		industry_work_Etv = (MyEditTextView) findViewById(R.id.industry_work_Etv);
		certifier_work_Etv = (MyEditTextView) findViewById(R.id.certifier_work_Etv);
		phone_work_Etv = (MyEditTextView) findViewById(R.id.phone_work_Etv);
		section_work_Etv = (MyEditTextView) findViewById(R.id.section_work_Etv);
		duty_work_Etv = (MyEditTextView) findViewById(R.id.duty_work_Etv);
		colleague_work_Etv = (MyEditTextView) findViewById(R.id.colleague_work_Etv);
		custom_work_Etv = (MyEditTextView) findViewById(R.id.custom_work_Etv);
		work_experience_Cb = (CheckBox) findViewById(R.id.work_experience_Cb);
		continueadd_Ll = (LinearLayout) findViewById(R.id.continueadd_Ll);
		work_Ll = (LinearLayout) findViewById(R.id.work_Ll);
		work_main_Ll = (LinearLayout) findViewById(R.id.work_main_Ll);
		delete_Ll = (LinearLayout) findViewById(R.id.delete_Ll);
		work_experience_Rl = (RelativeLayout) findViewById(R.id.work_experience_Rl);
		TextView continueadd_Tv = (TextView) findViewById(R.id.continueadd_Tv);
		continueadd_Tv.setText("新增工作经历");
		work_experience_Rl.setOnClickListener(this);
		quit_work_Rl.setOnClickListener(this);
		delete_Ll.setOnClickListener(this);
		continueadd_Ll.setOnClickListener(this);

		finish_work_Tv.setOnClickListener(this);
		data_work_Etv.setOnClickListener(this);
		industry_work_Etv.setOnClickListener(this);
		custom_work_Etv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finish_work_Tv:
			if (!TextUtils.isEmpty(data_work_Etv.getText())) {
				list.add(data_work_Etv.getTextLabel() + "_"
						+ data_work_Etv.getText());

			}
			if (!TextUtils.isEmpty(unit_work_Etv.getText())) {
				list.add(unit_work_Etv.getTextLabel() + "_"
						+ unit_work_Etv.getText());

			}
			if (!TextUtils.isEmpty(industry_work_Etv.getText())) {
				list.add(industry_work_Etv.getTextLabel() + "_"
						+ industry_work_Etv.getText());

			}
			if (!TextUtils.isEmpty(certifier_work_Etv.getText())) {
				list.add(certifier_work_Etv.getTextLabel() + "_"
						+ certifier_work_Etv.getText());

			}
			if (!TextUtils.isEmpty(phone_work_Etv.getText())) {
				list.add(phone_work_Etv.getTextLabel() + "_"
						+ phone_work_Etv.getText());

			}

			if (!TextUtils.isEmpty(section_work_Etv.getText())) {
				list.add(section_work_Etv.getTextLabel() + "_"
						+ section_work_Etv.getText());

			}
			if (!TextUtils.isEmpty(duty_work_Etv.getText())) {
				list.add(duty_work_Etv.getTextLabel() + "_"
						+ duty_work_Etv.getText());

			}

			if (!TextUtils.isEmpty(colleague_work_Etv.getText())) {
				list.add(colleague_work_Etv.getTextLabel() + "_"
						+ colleague_work_Etv.getText());

			}
			if (!TextUtils.isEmpty(custom_work_Etv.getText())) {
				list.add(custom_work_Etv.getTextLabel() + "_"
						+ custom_work_Etv.getText());
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
				intent.putStringArrayListExtra("Work_experience", list);
				setResult(7, intent);
			}
			finish();
			break;
		case R.id.data_work_Etv:
			intent = new Intent(this, WorkDataActivity.class);
			if (intent != null) {
				startActivityForResult(intent, 0);
			}
			break;
		case R.id.industry_work_Etv:
			intent = new Intent(this, IndustryActivity.class);
			if (intent != null) {
				startActivityForResult(intent, 0);
			}
			break;
		case R.id.custom_work_Etv:
			intent = new Intent(this, CustomActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.quit_work_Rl:
			finish();
			break;
		case R.id.delete_Ll:
			if (!(count == 0)) {
				work_Ll.removeAllViews();
			} else {
				finish();
			}
			count--;
			break;
		case R.id.continueadd_Ll:
			final MyLineraLayout layout = new MyLineraLayout(context);
			MyDeleteView deleteView  = new MyDeleteView(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			list1.add(data_work_Etv.getTextLabel());
			list1.add(unit_work_Etv.getTextLabel());
			list1.add(industry_work_Etv.getTextLabel());
			list1.add(certifier_work_Etv.getTextLabel());
			list1.add(phone_work_Etv.getTextLabel());
			list1.add(section_work_Etv.getTextLabel());
			list1.add(duty_work_Etv.getTextLabel());
			list1.add(colleague_work_Etv.getTextLabel());
			list1.add(custom_work_Etv.getTextLabel());
			for (int i = 0; i < list1.size(); i++) {
				final MyEditTextView MyEditTextView = new MyEditTextView(context);
				String text = list1.get(i);
				if (i == 0) {
					MyEditTextView.setChoose(true);
					MyEditTextView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mLineraDictionary.put(MyEditTextView.getMyEdit_Id(), layout);
							mDictionary.put(MyEditTextView.getMyEdit_Id(), MyEditTextView);
							intent = new Intent(Work_experience_Activity.this,
									WorkDataActivity.class);
							intent.putExtra("Work_Data_ID", MyEditTextView.getMyEdit_Id());
							startActivityForResult(intent, 1);
						}
					});

				} else if (i == 2) {
					MyEditTextView.setChoose(true);
					MyEditTextView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mLineraDictionary.put(MyEditTextView.getMyEdit_Id(), layout);
							mDictionary.put(MyEditTextView.getMyEdit_Id(), MyEditTextView);
							intent = new Intent(Work_experience_Activity.this,
									IndustryActivity.class);
							intent.putExtra("Work_Industry_ID", MyEditTextView.getMyEdit_Id());
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
							mLineraDictionary.put(MyEditTextView.getMyEdit_Id(), layout);
							mDictionary.put(MyEditTextView.getMyEdit_Id(), MyEditTextView);
							intent = new Intent(Work_experience_Activity.this,
									CustomActivity.class);
							intent.putExtra("Work_Custom_ID", MyEditTextView.getMyEdit_Id());
							startActivityForResult(intent, 1);
						}
					});

				}
				
				MyEditTextView.setTextLabel(text);
				list2.add(MyEditTextView);
				layout.addView(MyEditTextView, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				
				
			}
			layout.addView(deleteView);
			deleteView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					work_main_Ll.removeView(layout);
					count--;
				}
			});
			layouts.add(layout);
			work_main_Ll.addView(layout,2, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			list1.removeAll(list1);
			count++;
			break;
		case R.id.work_experience_Rl:
			if (work_experience_Cb.isChecked()) {
				work_experience_Cb.setChecked(false);
			} else {
				work_experience_Cb.setChecked(true);
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode == 0) {
				switch (resultCode) {
				case 999:
					custom2(data,custom_work_Etv,work_Ll,isNull,editTextViews);
					break;
				case 2:
					String industry = data.getStringExtra("industry");
					industry_work_Etv.setText(industry);
					break;
				case 3:
					String Work_Data = data.getStringExtra("Work_Data");
					if (!TextUtils.isEmpty(Work_Data)) {
						data_work_Etv.setText(Work_Data);
					}
					break;
				default:
					break;
				}
			}
			if (requestCode == 1) {
				String work_Data_ID = data.getStringExtra("work_Data_ID");
				String Work_Industry_ID = data.getStringExtra("Work_Industry_ID");
				String Work_Custom_ID = data.getStringExtra("Work_Custom_ID");
				switch (resultCode) {
				case 999:
					custom2(data,mDictionary.get(Work_Custom_ID),mLineraDictionary.get(Work_Custom_ID),isNull,editTextViews);
					break;
				case 3:
					String Work_Data = data.getStringExtra("Work_Data");
					if (!TextUtils.isEmpty(Work_Data)) {
						if (work_Data_ID != null) {
						MyEditTextView myEditTextView = mDictionary.get(work_Data_ID);
						myEditTextView.setText(Work_Data);
						}
					}
					break;
				case 2:
					String industry = data.getStringExtra("industry");
					if (!TextUtils.isEmpty(industry)) {
						if (Work_Industry_ID!=null) {
						MyEditTextView myEditTextView = mDictionary.get(Work_Industry_ID);
						myEditTextView.setText(industry);
						}
					}
					break;
				default:
					break;
				}
			}
		}
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
