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
 * 分类
 * @author Wxh07151732
 *
 */
public class ClassifyActivity extends BaseActivity{
	private ListView classify_Lv;
	private String[] classify = { "娱乐人物", "政治人物", "体育人物", "历史人物", "文化人物",
			"虚拟人物", "科学家","行业人物", "话题人物","其他人物" };
	private RelativeLayout quit_classify_Rl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_classify);
		init();
		initData();
	}

	private void initData() {
		 MakeListView.makelistviewAdapter(this, classify_Lv, classify );
		 classify_Lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = classify[position];
				Intent intent = new Intent(ClassifyActivity.this, NewConnectionsActivity.class);
				intent.putExtra("classify", text);
				setResult(11, intent);
				finish();
			}
		});
	}

	private void init() {
		classify_Lv = (ListView) findViewById(R.id.classify_Lv);
		quit_classify_Rl = (RelativeLayout) findViewById(R.id.quit_classify_Rl);
		quit_classify_Rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
