package com.tr.ui.work;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
/**
 * 事务重复
 * 
 * @param RepeatType
 * 			提醒重复类型， d:天，w:周，m：月，y:年，其他为不重复",
 * @author Administrator
 *
 */
public class WorkNewRepeatActivity extends JBaseActivity {
	
	private ImageView ImageViewNo;
	private ImageView ImageViewDay;
	private ImageView ImageViewWeek;
	private ImageView ImageViewMonth;
	private ImageView ImageViewYear;

	private String mRepeatType;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_new_repeat_activity);
		
		mRepeatType="o";
		mRepeatType=getIntent().getStringExtra("RepeatType");
		initView();
		initData();

	}


	public void initView() {
		ImageViewNo = (ImageView) findViewById(R.id.ImageViewNo);
		ImageViewDay = (ImageView) findViewById(R.id.ImageViewDay);
		ImageViewWeek = (ImageView) findViewById(R.id.ImageViewWeek);
		ImageViewMonth = (ImageView) findViewById(R.id.ImageViewMonth);
		ImageViewYear = (ImageView) findViewById(R.id.ImageViewYear);
		
	}

	public void initData() {
		resetViewData();
	}
	
	public void resetViewData()
	{
		ImageViewNo.setVisibility(View.GONE);
		ImageViewDay.setVisibility(View.GONE);
		ImageViewWeek.setVisibility(View.GONE);
		ImageViewMonth.setVisibility(View.GONE);
		ImageViewYear.setVisibility(View.GONE);
		if (mRepeatType.equals("y"))
			ImageViewYear.setVisibility(View.VISIBLE);
		if (mRepeatType.equals("m"))
			ImageViewMonth.setVisibility(View.VISIBLE);
		if (mRepeatType.equals("w"))
			ImageViewWeek.setVisibility(View.VISIBLE);
		if (mRepeatType.equals("d"))
			ImageViewDay.setVisibility(View.VISIBLE);
		if (mRepeatType.equals("o"))
			ImageViewNo.setVisibility(View.VISIBLE);
		
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "重复", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}
	
	
	public void onLinearTypeClick(View v) {
		mRepeatType=v.getTag().toString();
		Intent intent=getIntent();  
        intent.putExtra("RepeatType", mRepeatType); 
		setResult(1000,intent);
		finish();
	}
}
