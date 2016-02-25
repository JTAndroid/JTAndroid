package com.tr.ui.people.cread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.tr.R;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;

import android.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 教育经历(子模块)
 * 
 * @author Wxh07151732
 * 
 */
public class EducationActivity extends BaseActivity implements OnClickListener {
	private MyEditTextView education_data_Etv;
	private TextView finish_education_Tv;
	private MyEditTextView school_Etv;
	private MyEditTextView major_Etv;
	private MyEditTextView college_Etv;
	private MyEditTextView education_Etv;
	private MyEditTextView degree_Etv;
	private MyEditTextView overseas_Etv;
	private MyEditTextView Language_Level_Etv;
	private MyEditTextView schoolmate_Etv;
	private MyEditTextView custom_education_Etv;
	private ArrayList<String> list;
	private String[] schoolmate = { "同学关系", "校友", "老师", "校长", "自定义" };
	private Intent intent;
	private LinearLayout education_Ll;
	private LinearLayout continueadd_Ll;
	private LinearLayout delete_Ll;
	private LinearLayout schoolmate_Ll;
	private RelativeLayout quit_education_Rl;
	private ArrayList<String> list1;
	private ArrayList<MyEditTextView> list2;
	private MyEditTextView editTextView;
	private ArrayList<MyLineraLayout> layouts;
	private int count = 0;
	private ArrayList<String> list3;
	private LinearLayout education_main_Ll;
	private boolean isNull;
	private ArrayList<MyEditTextView> editTextViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_education);
		list = new ArrayList<String>();
		list2 = new ArrayList<MyEditTextView>();
		list3 = new ArrayList<String>();
		layouts = new ArrayList<MyLineraLayout>();
		editTextViews = new ArrayList<MyEditTextView>();
		init();
		initData();
	}

	private void initData() {
		schoolmate_Etv.MakeAddMoreMethod(schoolmate_Etv,
				EducationActivity.this,0, schoolmate_Ll, "同学关系",list2);
	}

	private void init() {
		quit_education_Rl = (RelativeLayout) findViewById(R.id.quit_education_Rl);
		finish_education_Tv = (TextView) findViewById(R.id.finish_education_Tv);
		education_data_Etv = (MyEditTextView) findViewById(R.id.education_data_Etv);
		school_Etv = (MyEditTextView) findViewById(R.id.school_Etv);
		major_Etv = (MyEditTextView) findViewById(R.id.major_Etv);
		college_Etv = (MyEditTextView) findViewById(R.id.college_Etv);
		education_Etv = (MyEditTextView) findViewById(R.id.education_Etv);
		degree_Etv = (MyEditTextView) findViewById(R.id.degree_Etv);
		overseas_Etv = (MyEditTextView) findViewById(R.id.overseas_Etv);
		schoolmate_Etv = (MyEditTextView) findViewById(R.id.schoolmate_Etv);
		Language_Level_Etv = (MyEditTextView) findViewById(R.id.Language_Level_Etv);
		custom_education_Etv = (MyEditTextView) findViewById(R.id.custom_education_Etv);
		education_Ll = (LinearLayout) findViewById(R.id.education_Ll);
		education_main_Ll = (LinearLayout) findViewById(R.id.education_main_Ll);
		schoolmate_Ll = (LinearLayout) findViewById(R.id.schoolmate_Ll);
		continueadd_Ll = (LinearLayout) findViewById(R.id.continueadd_Ll);
		delete_Ll = (LinearLayout) findViewById(R.id.delete_Ll);
		TextView continueadd_Tv = (TextView) findViewById(R.id.continueadd_Tv);
		continueadd_Tv.setText("新增教育经历");
		quit_education_Rl.setOnClickListener(this);
		delete_Ll.setOnClickListener(this);
		continueadd_Ll.setOnClickListener(this);
		degree_Etv.setOnClickListener(this);
		overseas_Etv.setOnClickListener(this);
		education_Etv.setOnClickListener(this);
		schoolmate_Etv.setOnClickListener(this);
		custom_education_Etv.setOnClickListener(this);
		finish_education_Tv.setOnClickListener(this);
		education_data_Etv.setOnClickListener(this);
		Language_Level_Etv.setOnClickListener(this);
	}

	public void finish(View v) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.education_data_Etv:
			intent = new Intent(this, EducationDataActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.schoolmate_Etv:
			schoolmate_Etv.makePopupWindows(EducationActivity.this, v,
					schoolmate);
			break;
		case R.id.custom_education_Etv:
			intent = new Intent(this, CustomActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.education_Etv:
			intent = new Intent(this, EducationBackgroundActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.degree_Etv:
			intent = new Intent(this, DegreeActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.overseas_Etv:
			intent = new Intent(this, OverseasActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.quit_education_Rl:
			finish();
			break;
		case R.id.Language_Level_Etv:
			intent = new Intent(this, LanguageLevelActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.delete_Ll:
			if (!(count == 0)) {
				education_Ll.removeAllViews();
			} else {
				finish();
			}
			break;
		case R.id.continueadd_Ll: //动态增加整个模块
			list1 = new ArrayList<String>();
			list1.add(education_data_Etv.getTextLabel());
			list1.add(school_Etv.getTextLabel());
			list1.add(major_Etv.getTextLabel());
			list1.add(college_Etv.getTextLabel());
			list1.add(education_Etv.getTextLabel());
			list1.add(degree_Etv.getTextLabel());
			list1.add(overseas_Etv.getTextLabel());
			list1.add(schoolmate_Etv.getTextLabel());
			list1.add(Language_Level_Etv.getTextLabel());
			list1.add(custom_education_Etv.getTextLabel());
			final MyLineraLayout layout = new MyLineraLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			MyDeleteView myDeleteView = new MyDeleteView(context);
			for (int i = 0; i < list1.size(); i++) {
				final MyEditTextView MyEditTextView = new MyEditTextView(context);
				String text = list1.get(i);
				if (i == 0) {
					MyEditTextView.setChoose(true);
					MyEditTextView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(EducationActivity.this,
									EducationDataActivity.class);
							intent.putExtra("Education_Data_ID", MyEditTextView.MyEdit_Id);
							startActivityForResult(intent, 1);
						}
					});
				} else if (i == 4) {
					MyEditTextView.setChoose(true);
					MyEditTextView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							
							Intent intent = new Intent(EducationActivity.this,
									EducationBackgroundActivity.class);
							intent.putExtra("Education_background_ID", MyEditTextView.MyEdit_Id);
							startActivityForResult(intent, 1);
						}
					});
				} else if (i == 5) {
					MyEditTextView.setChoose(true);
					MyEditTextView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(EducationActivity.this,
									DegreeActivity.class);
							intent.putExtra("Degree_ID", MyEditTextView.MyEdit_Id);
							startActivityForResult(intent, 1);
						}
					});
				} else if (i == 6) {
					MyEditTextView.setChoose(true);
					MyEditTextView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(EducationActivity.this,
									OverseasActivity.class);
							intent.putExtra("Overseas_ID", MyEditTextView.MyEdit_Id);
							startActivityForResult(intent, 1);
						}
					});
				} else if (i == 7) {
					MyEditTextView.setAddMore(true);
					MyEditTextView.setPopupwindow_Text(true);
					MyEditTextView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							MyEditTextView.makePopupWindows(EducationActivity.this,
									MyEditTextView, schoolmate);
						}
					});
					MyEditTextView.MakeAddMoreMethod(MyEditTextView,
							EducationActivity.this,0, layout, "同学关系",
							list2);
				} else if (i == 8) {
					MyEditTextView.setChoose(true);
					MyEditTextView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(EducationActivity.this,
									LanguageLevelActivity.class);
							intent.putExtra("Language_level_ID", MyEditTextView.MyEdit_Id);
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
							Intent intent = new Intent(EducationActivity.this,
									CustomActivity.class);
							intent.putExtra("Education_Custom_ID", MyEditTextView.MyEdit_Id);
							startActivityForResult(intent, 1);
						}
					});
				}
				mDictionary.put(MyEditTextView.getMyEdit_Id(), MyEditTextView);
				mLineraDictionary.put(MyEditTextView.getMyEdit_Id(), layout);
				MyEditTextView.setTextLabel(text);
				list2.add(MyEditTextView);
				layout.addView(MyEditTextView, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
			myDeleteView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					education_main_Ll.removeView(layout);
					count--;
				}
			});
			layout.addView(myDeleteView);
			layouts.add(layout);
			education_main_Ll.addView(layout,2, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			list3 = list1;
			count++;
			break;
		case R.id.finish_education_Tv:
			if (!TextUtils.isEmpty(education_data_Etv.getText())
					&& !TextUtils.isEmpty(education_data_Etv.getTextLabel())) {
				list.add(education_data_Etv.getTextLabel() + "_"
						+ education_data_Etv.getText());
			}
			if (!TextUtils.isEmpty(school_Etv.getText())
					&& !TextUtils.isEmpty(school_Etv.getTextLabel())) {
				list.add(school_Etv.getTextLabel() + "_" + school_Etv.getText());
			}
			if (!TextUtils.isEmpty(major_Etv.getText())
					&& !TextUtils.isEmpty(major_Etv.getTextLabel())) {
				list.add(major_Etv.getTextLabel() + "_" + major_Etv.getText());
			}
			if (!TextUtils.isEmpty(college_Etv.getText())
					&& !TextUtils.isEmpty(college_Etv.getTextLabel())) {
				list.add(college_Etv.getTextLabel() + "_"
						+ college_Etv.getText());
			}
			if (!TextUtils.isEmpty(education_Etv.getText())
					&& !TextUtils.isEmpty(education_Etv.getTextLabel())) {
				list.add(education_Etv.getTextLabel() + "_"
						+ education_Etv.getText());
			}
			if (!TextUtils.isEmpty(degree_Etv.getText())
					&& !TextUtils.isEmpty(degree_Etv.getTextLabel())) {
				list.add(degree_Etv.getTextLabel() + "_" + degree_Etv.getText());
			}
			if (!TextUtils.isEmpty(overseas_Etv.getText())
					&& !TextUtils.isEmpty(overseas_Etv.getTextLabel())) {
				list.add(overseas_Etv.getTextLabel() + "_"
						+ overseas_Etv.getText());
			}
			if (!TextUtils.isEmpty(schoolmate_Etv.getText())
					&& !TextUtils.isEmpty(schoolmate_Etv.getTextLabel())) {
				list.add(schoolmate_Etv.getTextLabel() + "_"
						+ schoolmate_Etv.getText());
			}
			if (!TextUtils.isEmpty(Language_Level_Etv.getText())
					&& !TextUtils.isEmpty(Language_Level_Etv.getTextLabel())) {
				list.add(Language_Level_Etv.getTextLabel() + "_"
						+ Language_Level_Etv.getText());
			}
			if (!TextUtils.isEmpty(custom_education_Etv.getText())
					&& !TextUtils.isEmpty(custom_education_Etv.getTextLabel())) {
				list.add(custom_education_Etv.getTextLabel() + "_"
						+ custom_education_Etv.getText());
			}
			if (list2 != null) {
				for (int i = 0; i < list2.size(); i++) {
					if (!TextUtils.isEmpty(list2.get(i).getText())) {
						list.add(list2.get(i).getTextLabel() + "_"
								+ list2.get(i).getText());
					}
				}
			}
			System.out.println("list.toString()+" + list.toString());
			if (!"[]".equals(list.toString())) {
				intent = new Intent(context, NewConnectionsActivity.class);
				intent.putStringArrayListExtra("Education", list);
				setResult(6, intent);
			}

			this.finish();
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
					String Education_background = data
							.getStringExtra("Education_background");
					if (!TextUtils.isEmpty(Education_background)) {
						education_Etv.setText(Education_background);
					}
					break;
				case 1:
					String degree = data.getStringExtra("degree");
					if (!TextUtils.isEmpty(degree)) {
						degree_Etv.setText(degree);
					}
					break;
				case 2:
					String overseas = data.getStringExtra("overseas");
					if (!TextUtils.isEmpty(overseas)) {
						overseas_Etv.setText(overseas);
					}
					break;
				case 3:
					String Education_Data = data
							.getStringExtra("Education_Data");
					if (Education_Data != null) {
						education_data_Etv.setText(Education_Data);
					}
					break;
				case 4:
					String Language_level = data
					.getStringExtra("Language_level");
					if (Language_level != null) {
						Language_Level_Etv.setText(Language_level);
					}
			break;
				case 999:
					
					custom2(data,custom_education_Etv,education_Ll,isNull,editTextViews);
					break;
				default:
					break;
				}
			}
			if (requestCode == 1) {
				String Education_Data_ID = data.getStringExtra("education_Data_ID");
				String education_background_ID = data.getStringExtra("education_background_ID");
				String degree_ID = data.getStringExtra("degree_ID");
				String overseas_ID = data.getStringExtra("overseas_ID");
				String language_level_ID = data.getStringExtra("language_level_ID");
				String Education_Custom_ID = data.getStringExtra("Education_Custom_ID");
				
				switch (resultCode) {
				case 0:
					String Education_background = data
							.getStringExtra("Education_background");
					if (!TextUtils.isEmpty(Education_background)) {
						if (!"[]".equals(list2.toString())) {
							editTextView = mDictionary.get(education_background_ID);
							editTextView.setText(Education_background);
						}
					}
					break;
				case 1:
					String degree = data.getStringExtra("degree");
					if (!"[]".equals(degree)) {
						if (!TextUtils.isEmpty(list2.toString())) {
							editTextView = mDictionary.get(degree_ID);
							editTextView.setText(degree);
						}
					}
					break;
				case 2:
					String overseas = data.getStringExtra("overseas");
					if (!TextUtils.isEmpty(overseas)) {
						if (!"[]".equals(list2.toString())) {
							editTextView = mDictionary.get(overseas_ID);
							editTextView.setText(overseas);
						}
					}
					break;
				case 3:
					String Education_Data = data
							.getStringExtra("Education_Data");
					if (Education_Data != null) {
						editTextView = mDictionary.get(Education_Data_ID);
						editTextView.setText(Education_Data);
					}
					break;
				case 4:
					String Language_level = data
					.getStringExtra("Language_level");
					if (Language_level != null) {
						editTextView = mDictionary.get(language_level_ID);
						editTextView.setText(Language_level);
					}
					break;
				case 999:
					custom2(data,mDictionary.get(Education_Custom_ID),mLineraDictionary.get(Education_Custom_ID),isNull,editTextViews);
					break;
				default:
					break;
				}
			}
		}
	}
}
