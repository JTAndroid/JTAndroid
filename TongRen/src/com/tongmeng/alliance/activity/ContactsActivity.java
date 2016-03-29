package com.tongmeng.alliance.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactsActivity extends Activity implements OnClickListener{
	/**
	 *  联系人
	 */
	
	//头部控件
	ImageView backImg,moreImg;
	TextView titletext;
	
	//界面控件
	LinearLayout scan,phone,weixin,qq,sina;
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_addcontacts);
//		
//		//头部
//		backImg = (ImageView) findViewById(R.id.index_top_seartchImg);
//		moreImg = (ImageView) findViewById(R.id.index_top_moreImg);
//		titletext = (TextView) findViewById(R.id.index_top_nameText);
//		moreImg.setVisibility(View.GONE);
//		backImg.setBackgroundResource(R.drawable.back);
//		titletext.setText("添加联系人");
//		backImg.setOnClickListener(this);
//		
//		//界面
//		scan = (LinearLayout) findViewById(R.id.activity_addcontacts_scan); 
//		phone = (LinearLayout) findViewById(R.id.activity_addcontacts_phone); 
//		weixin = (LinearLayout) findViewById(R.id.activity_addcontacts_weixin); 
//		qq = (LinearLayout) findViewById(R.id.activity_addcontacts_qq); 
//		sina = (LinearLayout) findViewById(R.id.activity_addcontacts_weibo); 
//		
//		scan.setOnClickListener(this);
//		phone.setOnClickListener(this);
//		weixin.setOnClickListener(this);
//		qq.setOnClickListener(this);
//		sina.setOnClickListener(this);
//	}
//	
	@Override
	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		Intent intent = new Intent(ContactsActivity.this,ContactsListActivity.class);
//		switch (v.getId()) {
//		case R.id.index_top_seartchImg:
//			finish();
//			break;
//		case R.id.activity_addcontacts_scan:
//			Intent scanIntent = new Intent(ContactsActivity.this,CameraActivity.class);
//			startActivity(scanIntent);
//			break;
//		case R.id.activity_addcontacts_phone:
//			intent.putExtra("from", "phone");
//			startActivity(intent);
//			break;
//		case R.id.activity_addcontacts_weixin:
//			intent.putExtra("from", "weixin");
//			startActivity(intent);
//			break;
//		case R.id.activity_addcontacts_qq:
//			intent.putExtra("from", "qq");
//			startActivity(intent);
//			break;
//		case R.id.activity_addcontacts_weibo:
//			intent.putExtra("from", "weibo");
//			startActivity(intent);
//			break;
//		default:
//			break;
//		}
	}
}
