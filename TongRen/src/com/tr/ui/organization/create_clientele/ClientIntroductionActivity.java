package com.tr.ui.organization.create_clientele;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.people.cread.BaseActivity;

/**
 * 客户简介 
 * @author hzy
 *
 */

public class ClientIntroductionActivity extends BaseActivity implements OnClickListener {

	
    private ImageView org_intro_iv;
    private String des;
    private TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_introduction);
		des = (String) getIntent().getSerializableExtra("intro");
		org_intro_iv = (ImageView) findViewById(R.id.org_intro_iv);
		tv = (TextView) findViewById(R.id.tv);
		org_intro_iv.setOnClickListener(this);
		if(des != null){
			tv.setText(des);			
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.org_intro_iv://点击后退

			finish();
			break;
		}
	}
}
