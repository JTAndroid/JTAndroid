package com.tr.ui.organization.firstpage;

import com.tr.R;
import com.tr.ui.organization.model.Customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class OrganizationIntroductionActivity extends Activity implements OnClickListener{
    private ImageView org_intro_iv;
    private String des;
    private EditText editText;
    private TextView finish;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_introduction);
		des = getIntent().getStringExtra("intro");
		init();
		initData();
		if(des != null){
			editText.setText(des);			
		}
	}

	private void initData() {
		finish.setOnClickListener(this);
		org_intro_iv.setOnClickListener(this);
	}

	private void init() {
		org_intro_iv = (ImageView) findViewById(R.id.org_intro_iv);
		editText = (EditText) findViewById(R.id.editText);
		finish = (TextView) findViewById(R.id.finish);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.org_intro_iv://点击后退
			finish();
			break;
		case R.id.finish://点击完成，将数据携带到编辑页面，并关闭当前页面
			String text = editText.getText().toString().trim();
			Intent intent = new Intent();
			intent.putExtra("introduction", text);
			setResult(100, intent);
			finish();
			break;
		}
	}
}
