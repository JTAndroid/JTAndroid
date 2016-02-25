package com.tr.ui.people.cread;

import com.tr.R;
import com.tr.ui.people.cread.utils.MakeListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
/**
 * 学位
 * @author Wxh07151732
 *
 */
public class DegreeActivity extends BaseActivity{
	private ListView degree_Lv;
	private String[] degree={"小学","初中","中专/高中","专科","本科","硕士研究生","博士研究生"};
	private RelativeLayout quit_degree_Rl;
	private String degree_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_degree);
		degree_ID = this.getIntent().getStringExtra("Degree_ID");
		init();
		initData();
	}

	private void initData() {
		MakeListView.makelistviewAdapter(context, degree_Lv, degree);
		degree_Lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = degree[position];
				Intent intent = new Intent(context, EducationActivity.class);
				intent.putExtra("degree", text);
				if (degree_ID!=null) {
					intent.putExtra("degree_ID", degree_ID);
				}
				setResult(1, intent);
				finish();
			}
		});
	}

	private void init() {
		degree_Lv = (ListView) findViewById(R.id.degree_Lv);
		quit_degree_Rl = (RelativeLayout) findViewById(R.id.quit_degree_Rl);
		quit_degree_Rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
