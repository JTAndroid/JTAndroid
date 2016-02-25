package com.tr.ui.people.cread;

import com.tr.R;
import com.tr.ui.people.cread.utils.MakeListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 行业
 * @author Wxh07151732
 *
 */
public class IndustryActivity extends BaseActivity {
	private ListView industry_Lv;
	private ListAdapter adapter;
	private String[] industry = {"矿产资源","医药","消费品","金融","工商业","房地产","公共事业","农林牧渔","通信,媒体与科技"};
	private String industry_count;
	private String Work_Industry_ID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_industry);
		industry_count = this.getIntent().getStringExtra("Industry_count");
		Work_Industry_ID = this.getIntent().getStringExtra("Work_Industry_ID");
		init();
		initData();
	}

	private void initData() {
		MakeListView.makelistviewAdapter(this, industry_Lv, industry);
		industry_Lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = industry[position];
				Intent intent = new Intent();
				intent.putExtra("industry", text);
				if (industry_count!=null) {
					intent.putExtra("industry_count", industry_count);
				}else if(Work_Industry_ID!=null){
					intent.putExtra("Work_Industry_ID", Work_Industry_ID);
				}
				
				
				setResult(2, intent);
				finish();
			}
		});
	}

	private void init() {
		industry_Lv = (ListView) findViewById(R.id.industry_Lv);
	}

}
