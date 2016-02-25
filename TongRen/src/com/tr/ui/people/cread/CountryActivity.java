package com.tr.ui.people.cread;

import com.tr.R;
import com.tr.ui.people.cread.utils.MakeListView;

import android.os.Bundle;
import android.widget.ListView;
/**
 * 选择国家
 * @author Wxh07151732
 *
 */
public class CountryActivity extends BaseActivity {
	private ListView country_lv;
	private String[] country={"韩国","日本","新加坡","马来西亚","菲律宾","沙特阿拉伯","朝鲜"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_country);
		init();
		initData();
	
	}

	private void initData() {
		MakeListView.makelistviewAdapter(this, country_lv, country);
	}

	private void init() {
		country_lv = (ListView) findViewById(R.id.country_lv);		
	}
}
