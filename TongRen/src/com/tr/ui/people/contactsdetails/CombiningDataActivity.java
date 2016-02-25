package com.tr.ui.people.contactsdetails;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.people.contactsdetails.adapter.CombiningDataAdapter;
import com.tr.ui.people.contactsdetails.bean.CombiningPersonData;

/**
 * 合并资料 (目前尚未有此功能)
 * @author John
 *
 */
public class CombiningDataActivity extends Activity implements
		OnItemClickListener,OnClickListener {

	private ListView combiningDataPullListView;

	private List<CombiningPersonData> personDataList;

	private CombiningDataAdapter combiningDataAdapter;

	private AlertDialog dlg;

	private Window window;

	private TextView yes, no;
	
	private ImageView click_back_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_combining_data);
		
		click_back_btn = (ImageView) findViewById(R.id.click_back_btn);
		click_back_btn.setOnClickListener(this);
		
		personDataList = new ArrayList<CombiningPersonData>();

		for (int i = 0; i < 20; i++) {

			CombiningPersonData personData = new CombiningPersonData();

			personData.setPersonHeadImage(R.drawable.default_people_avatar);

			personData.setPerosnName("陈志武");

			personData.setPersonPersonPosition("著名经济学家");

			personDataList.add(personData);
		}

		combiningDataPullListView = (ListView) findViewById(R.id.combiningDataPullListView);
		
		ImageView dividerImage = new ImageView(this); 
		
        dividerImage.setImageResource(R.drawable.people_listview_dividing_line); 
        
		combiningDataPullListView.setDivider(dividerImage.getDrawable());
		
		combiningDataPullListView.setOnItemClickListener(this);


		combiningDataAdapter = new CombiningDataAdapter(this, personDataList);

		combiningDataPullListView.setAdapter(combiningDataAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.combining_data, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {

		dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		window = dlg.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.people_combining_data_dialog);

		// WindowManager.LayoutParams lp = window.getAttributes();

		// window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

		// lp.y = 20;

		// window.setAttributes(lp);

		// WindowManager m = getWindowManager();
		// Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		// WindowManager.LayoutParams p = window.getAttributes(); //
		// 获取对话框当前的参数值
		// p.height = (int) (d.getHeight() * 0.3); // 高度设置为屏幕的0.6
		// p.width = (int) (d.getWidth() * 0.85); // 宽度设置为屏幕的0.65
		// window.setAttributes(p);

		// 为确认按钮添加事件,执行退出应用操作
		yes = (TextView) window.findViewById(R.id.yes);
		yes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(CombiningDataActivity.this,
						MergeConnectionsDataActivity.class);

				startActivity(intent);

				dlg.dismiss();

			}
		});

		no = (TextView) window.findViewById(R.id.no);
		no.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				dlg.dismiss();

			}
		});

	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
			case R.id.click_back_btn:
				
				finish();
			
			break;
		
		}
		
	}


}
