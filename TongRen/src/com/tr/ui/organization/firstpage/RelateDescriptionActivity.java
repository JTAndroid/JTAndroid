package com.tr.ui.organization.firstpage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.view.MyEditTextView;

public class RelateDescriptionActivity extends BaseActivity implements OnClickListener {
	private static MyEditTextView custom_Text_Ttv;
	private MyEditTextView custom_Ttv;
	private LinearLayout custom_Ll;
	private ArrayList<String> list;
	private static boolean isChange;
	private RelativeLayout quit_custom_Rl;
	private ArrayList<MyEditTextView> list1;
	private static ArrayList<String> initialize_Custom;
	public static Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 50021:
				initialize_Custom = (ArrayList<String>) msg.obj;
				isChange = true;
				break;

			default:
				break;

			}
		};
	};
	private String custom;
	private TextView custom_Tv;
	private String custom_count;
	private String Work_Custom_ID;
	private String Education_Custom_ID;
	private String Meeting_Custom_ID;
	private String Society_Custom_ID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_custom);
		custom = this.getIntent().getStringExtra("Custom");
		custom_count = this.getIntent().getStringExtra("Custom_count");
		Work_Custom_ID = this.getIntent().getStringExtra("Work_Custom_ID");
		Education_Custom_ID = this.getIntent().getStringExtra("Education_Custom_ID");
		Meeting_Custom_ID = this.getIntent().getStringExtra("Meeting_Custom_ID");
		Society_Custom_ID = this.getIntent().getStringExtra("Society_Custom_ID");
		list1 = new ArrayList<MyEditTextView>();
		init();
		initData();
		list = new ArrayList<String>();
		//初始化数据
		if (isChange) {
			custom_Text_Ttv.setVisibility(View.GONE);
			isChange = true;
			if (initialize_Custom != null) {
				for (int i = 0; i < initialize_Custom.size(); i++) {
					final MyEditTextView editTextView = new MyEditTextView(
							context);
					editTextView.setCustom(true);
					editTextView.setDelete(true);
					String text0 = initialize_Custom.get(i);
					String[] split = text0.split("_");
					String key0 = split[0];
					editTextView.setTextLabel(key0);
					if (split.length == 2) {
						String value0 = split[1];
						editTextView.setText(value0);
					}
					if (editTextView != null) {
						list1.add(editTextView);
						custom_Ll.addView(editTextView);
					}
					editTextView.getMyedittext_Addmore_Rl().setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							custom_Ll.removeView(editTextView);
							list1.remove(editTextView);
						}
					});
				}
			}
		}
	}

	private void initData() {
		if (custom!=null) {
			custom_Tv.setText(custom);
		}
		custom_Text_Ttv.MakeAddMoreMethod(custom_Text_Ttv, RelateDescriptionActivity.this,0, custom_Ll,
				"自定义文本", list1);
		custom_Ttv.MakeAddMoreMethod(custom_Ttv, RelateDescriptionActivity.this,0, custom_Ll, "",
				 list1);
	}

	private void init() {
		custom_Tv = (TextView) findViewById(R.id.custom_Tv);
		custom_Text_Ttv = (MyEditTextView) findViewById(R.id.custom_Text_Ttv);
		custom_Text_Ttv.setReadOnly(true);
		quit_custom_Rl = (RelativeLayout) findViewById(R.id.quit_custom_Rl);
		custom_Ttv = (MyEditTextView) findViewById(R.id.custom_Ttv);
		custom_Ll = (LinearLayout) findViewById(R.id.custom_Ll);
		custom_Ttv.setOnClickListener(this);
		custom_Text_Ttv.setOnClickListener(this);
		quit_custom_Rl.setOnClickListener(this);
		// if (isChange) {
		// custom_Text_Ttv.setVisibility(View.GONE);
		// }
	}

	public void finish(View v) {
		if (!TextUtils.isEmpty(custom_Ttv.getText())
				&& !TextUtils.isEmpty(custom_Ttv.getTextLabel())) {
			list.add(custom_Ttv.getTextLabel() + "_" + custom_Ttv.getText());
		}
		if (!TextUtils.isEmpty(custom_Text_Ttv.getText())
				&& !TextUtils.isEmpty(custom_Text_Ttv.getTextLabel())) {
			list.add(custom_Text_Ttv.getTextLabel() + "_"
					+ custom_Text_Ttv.getText());
		}
		if (!list1.isEmpty()) {
			for (int i = 0; i < list1.size(); i++) {
				MyEditTextView myEditTextView = list1.get(i);
				list.add(myEditTextView.getTextLabel() + "_"
						+ myEditTextView.getText());
			}
			list1.removeAll(list1);
		}
		//用正则表达式来验证输入内容
		if (custom_Ttv.getTextLabel().matches(
				"^[a-zA-Z\u4e00-\u9fa5][a-zA-Z0-9\u4e00-\u9fa5]{0,12}$")
				) {
		} else {
			Toast.makeText(context, "输入格式有误！", 0).show();
			return;
		}
		//发送数据
		if (!list.isEmpty()) {
			Intent intent = new Intent();
			intent.putStringArrayListExtra("Custom", list);
			if (custom_count!=null) {
				intent.putExtra("custom_count", custom_count);
			}else if(Work_Custom_ID!=null){
				intent.putExtra("Work_Custom_ID", Work_Custom_ID);
			}else if(Education_Custom_ID!=null){
				intent.putExtra("Education_Custom_ID", Education_Custom_ID);
			}else if(Meeting_Custom_ID!=null){
				intent.putExtra("Meeting_Custom_ID", Meeting_Custom_ID);
			}else if(Society_Custom_ID!=null){
				intent.putExtra("Society_Custom_ID", Society_Custom_ID);
			}
			
			setResult(999, intent);
		}
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_custom_Rl:
			finish();
			break;
		default:
			break;
		}
	}
}
