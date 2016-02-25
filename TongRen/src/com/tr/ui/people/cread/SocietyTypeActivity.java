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
 * 活动类型
 */
public class SocietyTypeActivity extends BaseActivity{
	private ListView society_type_Lv;
	private String[] society_type={"社团","组织","党派","政治团体","慈善机构"};
	private RelativeLayout quit_society_type_Rl;
	private String society_Type_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_society_type);
		society_Type_ID = this.getIntent().getStringExtra("Society_Type_ID");
		init();
		initData();
	}

	private void initData() {
		MakeListView.makelistviewAdapter(context, society_type_Lv, society_type);
		society_type_Lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = society_type[position];
				Intent intent = new Intent(context, SocietyActivity.class);
				intent.putExtra("society_type", text);
				if (society_Type_ID!=null) {
					intent.putExtra("society_Type_ID", society_Type_ID);
				}
				setResult(0, intent);
				finish();
			}
		});
	}

	private void init() {
		quit_society_type_Rl = (RelativeLayout) findViewById(R.id.quit_society_type_Rl);
		society_type_Lv = (ListView) findViewById(R.id.society_type_Lv);
		quit_society_type_Rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
