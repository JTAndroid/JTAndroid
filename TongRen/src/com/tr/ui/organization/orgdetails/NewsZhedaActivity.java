package com.tr.ui.organization.orgdetails;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.tr.R;
import com.tr.ui.adapter.CompetitionAdapter;
import com.tr.ui.conference.common.BaseActivity;

public class NewsZhedaActivity extends BaseActivity {

	private ListView mListView;
	private ImageView iv_zheda_back;

	@Override
	public void initView() {
      setContentView(R.layout.activity_news_zheda);
      mListView = (ListView) findViewById(R.id.lv_news_zheda);
      CompetitionAdapter mCompetitionAdapter = new CompetitionAdapter(getApplicationContext());
      iv_zheda_back = (ImageView) findViewById(R.id.iv_zheda_back);
      mListView.setAdapter(mCompetitionAdapter);
      iv_zheda_back.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	});
      
	}

	@Override
	public void initData() {
       
	}

}
