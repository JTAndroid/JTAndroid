package com.tr.ui.people.cread;

import java.util.ArrayList;

import com.tr.R;
import com.tr.ui.people.cread.utils.MakeGridview;

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
/**
 * 爱好
 * @author Wxh07151732
 *
 */
public class HobbyActivity extends BaseActivity {
	private GridView hobby_Gv;
	private String[] hobby={"游泳","爬山","钓鱼","读书","唱歌","电影","羽毛球","旅游","美食","＋"};
	private ArrayList<String> hobby_list;
	private ArrayList<String> makeGridviewAlertDialog;
	private RelativeLayout quit_hobby_Rl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_hobby);
		hobby_list = new ArrayList<String>();
		init();
		initData();
		
	}

	private void initData() {
		for (int i = 0; i < hobby.length; i++) {
			hobby_list.add(hobby[i]);
		}
		MakeGridview.makeGridviewAdapter(this, hobby_Gv, hobby_list);
		makeGridviewAlertDialog = MakeGridview.makeGridviewAlertDialog(this, hobby_Gv, hobby_list);
	}
	public void finish(View v){
		Intent intent = new Intent(this, PersonalInformationActivity.class);
		intent.putStringArrayListExtra("Hobby", makeGridviewAlertDialog);
		setResult(6,intent);
		finish();
	}
	private void init() {
		hobby_Gv = (GridView) findViewById(R.id.hobby_Gv);
		quit_hobby_Rl = (RelativeLayout) findViewById(R.id.quit_hobby_Rl);
		quit_hobby_Rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
