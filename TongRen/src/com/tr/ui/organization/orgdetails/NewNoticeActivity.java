package com.tr.ui.organization.orgdetails;

import com.tr.R;
import com.tr.ui.adapter.NewNoticeAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class NewNoticeActivity extends FragmentActivity implements OnClickListener {
	
	private ImageView mNewnoticeBackIv;

	@Override
    protected void onCreate(Bundle arg0) {
    	super.onCreate(arg0);
    	
    	setContentView(R.layout.activity_new_notice);
    	mNewnoticeBackIv = (ImageView) findViewById(R.id.newnoticeBackIv);
    	ListView lv_newnotice = (ListView) findViewById(R.id.lv_newnotice);
    	mNewnoticeBackIv.setOnClickListener(this);
    	
    	NewNoticeAdapter mNewNoticeAdapter = new NewNoticeAdapter(getApplicationContext());
    	lv_newnotice.setAdapter(mNewNoticeAdapter);
    }

	@Override
	public void onClick(View view) {
		if(view == mNewnoticeBackIv){
			finish();
		}
	}

}
