package com.tr.ui.organization.create_clientele;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.tr.R;
import com.tr.ui.organization.orgdetails.EditClientBasicInfomationActivity;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.utils.MakeListView;

public class MediaEditClientTypeActivity extends BaseActivity{
	
	private ListView org_media_type_Lv;
	private RelativeLayout quit_org_media_type_Rl;
	private String[] media_type={"互联网媒体","研究报告","电视广播","报纸期刊"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_media_type);
		init();
		initData();
	}

	private void initData() {
		MakeListView.makelistviewAdapter(context, org_media_type_Lv, media_type);
		org_media_type_Lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String media = media_type[position];
//				Intent intent = new Intent(Media_Type_Activity.this,Create_Clientele_Activity.class);
//				intent.putExtra("media_type", media);
//				setResult(24, intent);
//				finish();
				Message message = Message.obtain();
				message.obj = media;
				EditClientBasicInfomationActivity.handler.sendMessage(message);
				finish();
			}
		});
	}

	private void init() {
		org_media_type_Lv = (ListView) findViewById(R.id.org_media_type_Lv);
		quit_org_media_type_Rl = (RelativeLayout) findViewById(R.id.quit_org_media_type_Rl);
		quit_org_media_type_Rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
