package com.tr.ui.people.cread;

import com.tr.R;
import com.tr.ui.people.cread.utils.MakeListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 信仰
 * @author Wxh07151732
 *
 */
public class BeliefActivity extends BaseActivity {
	private ListView belief_Lv;
	private String[] belief ={"佛教","道教","基督教","天主教","犹太教","伊斯兰教","印度教","无神论者","其他"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_belief);
		init();
		initData();
	}

	private void initData() {
		MakeListView.makelistviewAdapter(this, belief_Lv, belief );
		belief_Lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = belief[position];
				Intent intent = new Intent(BeliefActivity.this, PersonalInformationActivity.class);
				intent.putExtra("belief", text);
				setResult(4, intent);
				finish();
			}
		});
	}

	private void init() {
		belief_Lv = (ListView) findViewById(R.id.belief_Lv);
	}
}
