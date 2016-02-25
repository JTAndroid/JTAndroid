package com.tr.ui.people.cread;

import com.tr.R;
import com.tr.ui.people.cread.utils.MakeListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
/**
 * 籍贯
 * @author Wxh07151732
 *
 */
public class NativePlaceAtivity extends BaseActivity{
	private String[] native_place = {
			"北京",
			"天津",
			"上海",
			"重庆",
			"河北",
			"河南",
			"云南",
			"辽宁",
			"黑龙江" ,
			"湖南",
			"安徽",
			"山东",
			"新疆",
			"江苏",
			"浙江",
			"江西",
			"湖北",
			"广西",
			"甘肃",
			"山西",
			"内蒙古",
			"陕西",
			"吉林",
			"福建",
			"贵州",
			"广东",
			"青海",
			"西藏",
			"四川",
			"宁夏",
			"海南",
			"台湾",
			"香港",
			"澳门"};
	private ListView native_place_Lv;
	private RelativeLayout quit_native_Rl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_native_place);
		init();
		initData();
	}

	private void initData() {
		MakeListView.makelistviewAdapter(this, native_place_Lv, native_place);	
		native_place_Lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = native_place[position];
				Intent intent = new Intent(NativePlaceAtivity.this, PersonalInformationActivity.class);
				intent.putExtra("nation_place", text);
				setResult(3, intent);
				finish();
			}
		});
	}

	private void init() {
		native_place_Lv = (ListView) findViewById(R.id.native_place_Lv);
		quit_native_Rl = (RelativeLayout) findViewById(R.id.quit_native_Rl);
		quit_native_Rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
