package com.tr.ui.people.cread;

import java.util.ArrayList;

import com.tr.R;
import com.tr.ui.people.cread.utils.MakeGridview;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 身体情况
 * @author Wxh07151732
 *
 */
public class PhysicalActivity extends BaseActivity {
	private GridView physical_Gv;
	private String[] physical = {"无其他疾病","健康","压健康","高血压","心脏病","＋"};
	private ArrayList<String> physical_list;
	private ArrayList<String> makeGridviewAlertDialog;
	private RelativeLayout quit_physical_Rl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_physical);
		physical_list= new ArrayList<String>();
		init();
		initData();
		
	}

	private void initData() {
		for (int i = 0; i < physical.length; i++) {
			physical_list.add(physical[i]);
		}
		MakeGridview.makeGridviewAdapter(this, physical_Gv, physical_list);
		makeGridviewAlertDialog = MakeGridview.makeGridviewAlertDialog(this, physical_Gv, physical_list);
	}
	public void finish(View v){
		Intent intent = new Intent(this, PersonalInformationActivity.class);
		intent.putStringArrayListExtra("Physical", makeGridviewAlertDialog);
		setResult(7,intent);
		finish();
	}
	private void init() {
		physical_Gv = (GridView) findViewById(R.id.physical_Gv);
		quit_physical_Rl = (RelativeLayout) findViewById(R.id.quit_physical_Rl);
		quit_physical_Rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
