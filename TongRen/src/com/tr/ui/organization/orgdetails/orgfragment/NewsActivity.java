package com.tr.ui.organization.orgdetails.orgfragment;

import com.tr.R;
import com.tr.ui.adapter.NewsAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;


public class NewsActivity extends FragmentActivity implements OnClickListener{

	private ImageView newsBackIv;
	private ListView lv_news;

	@Override
    protected void onCreate(Bundle arg0) {
    	super.onCreate(arg0);
    	
    	setContentView(R.layout.activity_news);
    	newsBackIv = (ImageView) findViewById(R.id.newsBackIv);
    	lv_news = (ListView) findViewById(R.id.lv_news);
    	
        NewsAdapter mNewsAdapter = new NewsAdapter(getApplicationContext());
        lv_news.setAdapter(mNewsAdapter);
    	
    	newsBackIv.setOnClickListener(this);
    }

	@Override
	public void onClick(View view) {
		if(view == newsBackIv){
			finish();
		}
	}

}
