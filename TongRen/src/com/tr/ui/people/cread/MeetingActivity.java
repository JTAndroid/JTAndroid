package com.tr.ui.people.cread;

import java.util.ArrayList;

import com.tr.R;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 会面情况(子模块)
 * 
 * @author Wxh07151732
 * 
 */
public class MeetingActivity extends BaseActivity implements OnClickListener {

	private MyEditTextView label_meeting_Etv;
	private MyEditTextView relevance_meeting_Etv;
	private MyEditTextView custom_meeting_Etv;
	private Intent intent;
	private TextView finish_meeting_Tv;
	private ArrayList<String> list;
	private MyEditTextView theme_meeting_Etv;
	private MyEditTextView data_meeting_Etv;
	private MyEditTextView address_meeting_Etv;
	private MyEditTextView content_meeting_Etv;
	private MyEditTextView phone_meeting_Etv;
	private LinearLayout continueadd_Ll;
	private LinearLayout delete_Ll;
	private LinearLayout meeting_Ll;
	private RelativeLayout quit_meeting_Rl;
	private ArrayList<String> list1;
	private ArrayList<MyEditTextView> list2;
	private ArrayList<MyLineraLayout> layouts;
	private int count;
	private LinearLayout meeting_main_Ll;
	private boolean isNull;
	private ArrayList<MyEditTextView> editTextViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_meeting);
		editTextViews = new ArrayList<MyEditTextView>();
		list = new ArrayList<String>();
		list1 = new ArrayList<String>();
		list2 = new ArrayList<MyEditTextView>();
		layouts = new ArrayList<MyLineraLayout>();
		init();

	}

	private void init() {
		label_meeting_Etv = (MyEditTextView) findViewById(R.id.label_meeting_Etv);
		relevance_meeting_Etv = (MyEditTextView) findViewById(R.id.relevance_meeting_Etv);
		custom_meeting_Etv = (MyEditTextView) findViewById(R.id.custom_meeting_Etv);
		quit_meeting_Rl = (RelativeLayout) findViewById(R.id.quit_meeting_Rl);
		theme_meeting_Etv = (MyEditTextView) findViewById(R.id.theme_meeting_Etv);
		data_meeting_Etv = (MyEditTextView) findViewById(R.id.data_meeting_Etv);
		address_meeting_Etv = (MyEditTextView) findViewById(R.id.address_meeting_Etv);
		content_meeting_Etv = (MyEditTextView) findViewById(R.id.content_meeting_Etv);
		phone_meeting_Etv = (MyEditTextView) findViewById(R.id.phone_meeting_Etv);
		label_meeting_Etv.setTextHintLabel("直接输入标签名");
		relevance_meeting_Etv.setTextHintLabel("可关联,组织,事件,知识");
		finish_meeting_Tv = (TextView) findViewById(R.id.finish_meeting_Tv);
		continueadd_Ll = (LinearLayout) findViewById(R.id.continueadd_Ll);
		meeting_Ll = (LinearLayout) findViewById(R.id.meeting_Ll);
		meeting_main_Ll = (LinearLayout) findViewById(R.id.meeting_main_Ll);
		delete_Ll = (LinearLayout) findViewById(R.id.delete_Ll);
		TextView continueadd_Tv = (TextView) findViewById(R.id.continueadd_Tv);
		continueadd_Tv.setText("新增会面情况");
		quit_meeting_Rl.setOnClickListener(this);
		delete_Ll.setOnClickListener(this);
		continueadd_Ll.setOnClickListener(this);

		custom_meeting_Etv.setOnClickListener(this);
		finish_meeting_Tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_meeting_Etv:
			intent = new Intent(this, CustomActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.finish_meeting_Tv:
			if (!TextUtils.isEmpty(label_meeting_Etv.getText())) {
				list.add(label_meeting_Etv.getTextLabel() + "_"
						+ label_meeting_Etv.getText());
			}
			if (!TextUtils.isEmpty(relevance_meeting_Etv.getText())) {
				list.add(relevance_meeting_Etv.getTextLabel() + "_"
						+ relevance_meeting_Etv.getText());
			}
			if (!TextUtils.isEmpty(custom_meeting_Etv.getText())) {
				list.add(custom_meeting_Etv.getTextLabel() + "_"
						+ custom_meeting_Etv.getText());
			}
			if (!TextUtils.isEmpty(theme_meeting_Etv.getText())) {
				list.add(theme_meeting_Etv.getTextLabel() + "_"
						+ theme_meeting_Etv.getText());
			}
			if (!TextUtils.isEmpty(data_meeting_Etv.getText())) {
				list.add(data_meeting_Etv.getTextLabel() + "_"
						+ data_meeting_Etv.getText());
			}
			if (!TextUtils.isEmpty(address_meeting_Etv.getText())) {
				list.add(address_meeting_Etv.getTextLabel() + "_"
						+ address_meeting_Etv.getText());
			}
			if (!TextUtils.isEmpty(content_meeting_Etv.getText())) {
				list.add(content_meeting_Etv.getTextLabel() + "_"
						+ content_meeting_Etv.getText());
			}
			if (!TextUtils.isEmpty(phone_meeting_Etv.getText())) {
				list.add(phone_meeting_Etv.getTextLabel() + "_"
						+ phone_meeting_Etv.getText());
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
				intent.putStringArrayListExtra("Meeting", list);
				setResult(9, intent);
			}
			finish();
			break;
		case R.id.quit_meeting_Rl:
			finish();
			break;
		case R.id.delete_Ll:
			if (!(count == 0)) {
				meeting_Ll.removeAllViews();
			} else {
				finish();
			}
			count--;
			break;
		case R.id.continueadd_Ll: //动态增加整个模块
			final MyLineraLayout layout = new MyLineraLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			list1.add(theme_meeting_Etv.getTextLabel());
			list1.add(data_meeting_Etv.getTextLabel());
			list1.add(address_meeting_Etv.getTextLabel());
			list1.add(content_meeting_Etv.getTextLabel());
			list1.add(phone_meeting_Etv.getTextLabel());
			list1.add(label_meeting_Etv.getTextLabel());
			list1.add(relevance_meeting_Etv.getTextLabel());
			list1.add(custom_meeting_Etv.getTextLabel());
			MyDeleteView deleteView = new MyDeleteView(context);
			for (int i = 0; i < list1.size(); i++) {
				final MyEditTextView MyEditTextView = new MyEditTextView(context);
				String text = list1.get(i);
				if (i == 5) {
					MyEditTextView.setTextHintLabel("直接输入标签名");
				} else if (i == 6) {
					MyEditTextView.setTextHintLabel("可关联,组织,事件,知识");
				}
				if (i == list1.size() - 1) {
					MyEditTextView.setAddMore_hint(true);
					MyEditTextView.setChoose(true);
					MyEditTextView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mDictionary.put(MyEditTextView.getMyEdit_Id(), MyEditTextView);
							mLineraDictionary.put(MyEditTextView.getMyEdit_Id(), layout);
							intent = new Intent(MeetingActivity.this,
									CustomActivity.class);
							intent.putExtra("Meeting_Custom_ID", MyEditTextView.getMyEdit_Id());
							if (intent != null) {
								startActivityForResult(intent, 1);
							}
						}
					});
				}
				MyEditTextView.setTextLabel(text);
				layout.addView(MyEditTextView, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				list2.add(MyEditTextView);

			}
			layout.addView(deleteView);
			deleteView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					meeting_main_Ll.removeView(layout);
					count--;
				}
			});
			meeting_main_Ll.addView(layout,2, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			layouts.add(layout);
			count++;
			list1.removeAll(list1);
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
				case 999:
					custom2(data,custom_meeting_Etv , meeting_Ll, isNull,editTextViews);
					break;

				default:
					break;
				}
			}
			if (requestCode == 1) {
				String Meeting_Custom_ID = data.getStringExtra("Meeting_Custom_ID");
				switch (resultCode) {
				case 999:
					custom2(data, mDictionary.get(Meeting_Custom_ID), mLineraDictionary.get(Meeting_Custom_ID), isNull,editTextViews);
					break;

				default:
					break;
				}
			}
		}
	}
}
