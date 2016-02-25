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
 * 选择洲
 * @author Wxh07151732
 *
 */
public class ContinentActivity extends BaseActivity {
	private ListView continent_lv;
	private String[] continent={"亚洲","非洲","北美洲","南美洲","南极洲","欧洲","大洋洲"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_continent);
		init();
		initData();
		
	}

	private void initListener() {
			continent_lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(ContinentActivity.this, CountryActivity.class);
					startActivity(intent);
				}
			});		
	}

	private void initData() {
		MakeListView.makelistviewAdapter(this, continent_lv, continent);
		initListener();
	}

	private void init() {
		continent_lv = (ListView) findViewById(R.id.continent_lv);
	}
}
