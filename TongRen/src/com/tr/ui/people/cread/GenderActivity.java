package com.tr.ui.people.cread;

import com.tr.R;
import com.tr.ui.people.cread.utils.MakeListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
/**
 * 性别
 * @author Wxh07151732
 *
 */
public class GenderActivity extends BaseActivity{
	private String[] gender = { "男", "女", "保密" };
	private ListView gender_Lv;
	private RelativeLayout quit_gender_Rl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_gender);
		init();
		initData();
	}
	private void initData() {
		MakeListView.makelistviewAdapter(this, gender_Lv, gender);
		gender_Lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String text = gender[position];
					Intent intent = new Intent(GenderActivity.this, NewConnectionsActivity.class);
					intent.putExtra("gender", text);
					setResult(22, intent);
					finish();
				}
			});
	}
	private void init() {
		gender_Lv = (ListView) findViewById(R.id.gender_Lv);
		quit_gender_Rl = (RelativeLayout) findViewById(R.id.quit_gender_Rl);
		quit_gender_Rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
