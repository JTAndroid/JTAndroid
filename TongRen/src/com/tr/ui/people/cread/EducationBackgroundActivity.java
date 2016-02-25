package com.tr.ui.people.cread;

import com.tr.R;
import com.tr.ui.people.cread.utils.MakeListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
/**
 * 学历
 * @author Wxh07151732
 *
 */
public class EducationBackgroundActivity extends BaseActivity{
	private ListView education_background_Lv;
	private String[] education_background={"MBA","博士","硕士","本科","专科","中专","高中"};
	private RelativeLayout quit_education_background_Rl;
	private String education_background_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_education_background);
		education_background_ID = this.getIntent().getStringExtra("Education_background_ID");
		init();
		initData();
	}

	private void initData() {
		MakeListView.makelistviewAdapter(context, education_background_Lv, education_background);
		education_background_Lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = education_background[position];
				Intent intent = new Intent(context, EducationActivity.class);
				intent.putExtra("Education_background", text);
				if (education_background_ID!=null) {
					intent.putExtra("education_background_ID", education_background_ID);
				}
				setResult(0, intent);
				finish();
			}
		});
	}
	public void finish(View v){
		finish();
	}
	private void init() {
		education_background_Lv = (ListView) findViewById(R.id.education_background_Lv);
		quit_education_background_Rl = (RelativeLayout) findViewById(R.id.quit_education_background_Rl);
		quit_education_background_Rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
