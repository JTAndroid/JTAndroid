package com.tr.ui.people.contactsdetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;

/**
 * 会面模块->提醒时间
 * @author John
 *
 */
public class ReminderTimeActivity extends Activity implements OnClickListener {

	private RadioButton checkedMinute, checkedHour, checkedDay, noChecked;

	private ImageView choose_reminder_time_back;

	private TextView choose_complete;

	private Intent intent;

	private EditText addReminderTime;

	private String chooseStr = "";

	private String time = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_reminder_time);

		choose_reminder_time_back = (ImageView) findViewById(R.id.choose_reminder_time_back);
		choose_reminder_time_back.setOnClickListener(this);

		choose_complete = (TextView) findViewById(R.id.choose_complete);
		choose_complete.setOnClickListener(this);

		checkedMinute = (RadioButton) findViewById(R.id.checkedMinute);
		checkedMinute.setOnClickListener(this);

		checkedHour = (RadioButton) findViewById(R.id.checkedHour);
		checkedHour.setOnClickListener(this);

		checkedDay = (RadioButton) findViewById(R.id.checkedDay);
		checkedDay.setOnClickListener(this);

		noChecked = (RadioButton) findViewById(R.id.noChecked);
		noChecked.setOnClickListener(this);
		// 获取提醒的时间
		addReminderTime = (EditText) findViewById(R.id.addReminderTime);
		addReminderTime.setOnFocusChangeListener(mOnFocusChangeListener);
		
		addReminderTime.setOnClickListener(this);
	}
	 private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
         @Override
         public void onFocusChange(View v, boolean hasFocus)
         {
             EditText textView = (EditText)v;
             String hint;
             if (hasFocus) {
                 textView.addTextChangedListener(new TextWatcher(){

			         @Override
			         public void afterTextChanged(Editable arg0) {
			                 
			         }

			         @Override
			         public void beforeTextChanged(CharSequence s, int start, int count,
			                         int after) {
			         }

			         @Override
			         public void onTextChanged(CharSequence s, int start, int before, int count) {
			                 Log.d("length", s+"#"+s.length());
			                 if(s.length()==6){
			                  Toast.makeText(ReminderTimeActivity.this,"最多只能输入18个数字", Toast.LENGTH_SHORT).show();
			                 }
			         }

	});
             } else {
                 hint = textView.getTag().toString();
                 textView.setHint(hint);
             }
         }
     };
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.reminder_time, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.choose_reminder_time_back:

			finish();

			break;

		case R.id.addReminderTime:

			break;
		// 点击完成携带数据返回时间选择器界面
		case R.id.choose_complete:

			time = addReminderTime.getText().toString();

			if (!chooseStr.equals("不提醒") || chooseStr.equals("")) {

				if (!time.equals("")) {

					intent = new Intent(ReminderTimeActivity.this,
							TimeActivity.class);

					intent.putExtra("REMINDERTIME", "提前" + time + chooseStr);
					long parseLong = Long.parseLong(time);
					intent.putExtra("TIME", parseLong );
					setResult(11, intent);

					finish();

				} else {

					Toast.makeText(this, "提醒时间不能为空，请输入", Toast.LENGTH_SHORT)
							.show();

				}

			} else if(chooseStr.equals("不提醒")){

				intent = new Intent(ReminderTimeActivity.this,
						TimeActivity.class);

				intent.putExtra("REMINDERTIME", "不提醒");

				setResult(11, intent);

				finish();

			}

			break;
		case R.id.checkedMinute:

			chooseStr = "分钟";

			break;
		case R.id.checkedHour:

			chooseStr = "小时";

			break;
		case R.id.checkedDay:

			chooseStr = "天";

			break;
		case R.id.noChecked:

			chooseStr = "不提醒";

			break;

		}

	}

}
