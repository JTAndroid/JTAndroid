package com.tr.ui.people.cread;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.tr.R;
import com.tr.ui.people.cread.utils.MakeGridview;

/**
 * 生活习惯
 * @author Wxh07151732
 *
 */
public class LivingHabitActivity extends BaseActivity {
	private GridView livinghabit_Gv;
	private String[] livingHabit ={"抽烟","喝酒","打牌","早睡早起","忌辛辣","＋"};
	private ArrayList<String> livingHabit_list;
	private ArrayList<String> makeGridviewAlertDialog;
	private RelativeLayout quit_hobby_Rl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_livinghabit);
		livingHabit_list = new ArrayList<String>();
		init();
		initData();
	}

	private void initData() {
		for (int i = 0; i < livingHabit.length; i++) {
			livingHabit_list.add(livingHabit[i]);
		}
		MakeGridview.makeGridviewAdapter(this, livinghabit_Gv, livingHabit_list);
		makeGridviewAlertDialog = MakeGridview.makeGridviewAlertDialog(this, livinghabit_Gv, livingHabit_list);
	}
	public void finish(View v){
		Intent intent = new Intent(this, PersonalInformationActivity.class);
		intent.putStringArrayListExtra("LivingHabit", makeGridviewAlertDialog);
		setResult(5,intent);
		finish();
	}
	private void init() {
		livinghabit_Gv = (GridView) findViewById(R.id.livinghabit_Gv);
		quit_hobby_Rl = (RelativeLayout) findViewById(R.id.quit_hobby_Rl);
		quit_hobby_Rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
