package com.tr.ui.people.cread;

import com.tr.R;
import com.tr.ui.people.cread.utils.MakeListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 类型
 * @author Wxh07151732
 *
 */
public class TypeActivity extends BaseActivity {
	private ListView type_Lv;
	private ListAdapter adapter;
	private String[] type = {"股权","债权","金融衍生产品及另类投资","艺术品","项目"};
	private String type_count;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_type);
		type_count = this.getIntent().getStringExtra("Type_count");
		init();
		initData();
	}

	private void initData() {
		MakeListView.makelistviewAdapter(this, type_Lv, type);
		type_Lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = type[position];
				Intent intent = new Intent();
				intent.putExtra("type", text);
				intent.putExtra("type_count", type_count);
				setResult(3, intent);
				finish();
			}
		});
	}

	private void init() {
		type_Lv = (ListView) findViewById(R.id.type_Lv);
	}
}
