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
 * 海外学习经验
 * @author Wxh07151732
 *
 */
public class OverseasActivity extends BaseActivity {
	private ListView overseas_Lv;
	private String[] overseas={"是","否"};
	private RelativeLayout quit_overseas_Rl;
	private String overseas_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_overseas);
		overseas_ID = this.getIntent().getStringExtra("Overseas_ID");
		init();
		initData();
	}

	private void initData() {
		MakeListView.makelistviewAdapter(context, overseas_Lv, overseas);
		overseas_Lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = overseas[position];
				Intent intent = new Intent(context, EducationActivity.class);
				intent.putExtra("overseas", text);
				if (overseas_ID!=null) {
					intent.putExtra("overseas_ID", overseas_ID);
				}
				setResult(2, intent);
				finish();
			}
		});
	}

	private void init() {
		overseas_Lv = (ListView) findViewById(R.id.overseas_Lv);
		quit_overseas_Rl = (RelativeLayout) findViewById(R.id.quit_overseas_Rl);
		quit_overseas_Rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
