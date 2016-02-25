package com.tr.ui.people.contactsdetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;

/**
 * 会面模块->重复间隔
 * @author John
 *
 */
public class RepeatActivity extends Activity implements OnClickListener{

	private RadioButton  checked_no_repeat,
			checked_repeat_day, checked_repeat_week, checked_repeat_month,
			checked_repeat_year;
	
	private ImageView choose_repeat_back;
	
	private TextView repeat_complete;
	
	
	private Intent intent;
	
	private String repeatStr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_repeat);
		
		choose_repeat_back = (ImageView) findViewById(R.id.choose_repeat_back);
		choose_repeat_back.setOnClickListener(this);
		
		repeat_complete = (TextView) findViewById(R.id.repeat_complete);
		repeat_complete.setOnClickListener(this);
		
		checked_no_repeat = (RadioButton) findViewById(R.id.noChecked);
		checked_no_repeat.setOnClickListener(this);
		
		checked_repeat_day = (RadioButton) findViewById(R.id.checkedDay);
		checked_repeat_day.setOnClickListener(this);
		
		
		checked_repeat_week = (RadioButton) findViewById(R.id.checkedweek);
		checked_repeat_week.setOnClickListener(this);
		
		checked_repeat_month = (RadioButton) findViewById(R.id.checkedmonth);
		checked_repeat_month.setOnClickListener(this);
		
		checked_repeat_year = (RadioButton) findViewById(R.id.checkedyear);
		checked_repeat_year.setOnClickListener(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.repeat, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
			case R.id.choose_repeat_back:
				
				finish();
				
				break;
				
			case R.id.repeat_complete:
				
				if(!repeatStr.equals("")){
					
					intent = new Intent(RepeatActivity.this, TimeActivity.class);
					
					intent.putExtra("REPEAT", repeatStr);
					
					setResult(12, intent);
					
					finish();
					
				}else{
					
					Toast.makeText(this, "请选择重复项", Toast.LENGTH_SHORT)
					.show();
					
				}
				
				
				
				break;
				
			case R.id.noChecked:
				
				repeatStr ="不重复";
				
				break;
				
			case R.id.checkedDay:
				
				repeatStr ="每天";
				
				break;
				
				
			case R.id.checkedweek:
				
				repeatStr ="每周";
				
				break;
				
				
			case R.id.checkedmonth:
				
				repeatStr ="每月";
				
				break;
				
			case R.id.checkedyear:
				
				repeatStr ="每年";
				
				break;
		
		}
		
	}

}
