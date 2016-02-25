package com.tr.ui.people.cread;

import com.tr.R;
import com.tr.ui.people.cread.utils.MakeListView;

import android.os.Bundle;
import android.widget.ListView;
/**
 * 选择省份
 * @author Wxh07151732
 *
 */
public class ProvinceActivity extends BaseActivity {
	private ListView province_lv;
	private String[] province = {
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_province);
		init();
		initData();
	}

	private void initData() {
		MakeListView.makelistviewAdapter(this, province_lv, province );
	}

	private void init() {
		province_lv = (ListView) findViewById(R.id.province_lv);
	}
}	
