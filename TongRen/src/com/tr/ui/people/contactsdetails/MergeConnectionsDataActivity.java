package com.tr.ui.people.contactsdetails;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.ui.people.contactsdetails.bean.MergeConnectionsData;

/**
 * 合并人脉资料  (此功能尚未上线)
 * @author John
 *
 */
public class MergeConnectionsDataActivity extends Activity implements
		OnClickListener {

	private List<MergeConnectionsData> personDataList;


	private ImageView local_head, list_head, history_merge_delete,
			virtual_merge_delete, phone_merge_delete, phone_merge_delete2,
			company_merge_delete, companye_merge_delete2,merge_click_back_btn;

	private RelativeLayout history_relativeLayout, virtual_relativeLayout,
			phone_relativeLayout1, phone_relativeLayout2,
			company_relativeLayout, company_relativeLayout2;
	
	private TextView merge_completeTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_merge_connections_data);

		local_head = (ImageView) findViewById(R.id.local_head);
		local_head.setOnClickListener(this);

		list_head = (ImageView) findViewById(R.id.list_head);
		list_head.setOnClickListener(this);
		
		history_merge_delete = (ImageView) findViewById(R.id.history_merge_delete);
		history_merge_delete.setOnClickListener(this);
		
		virtual_merge_delete = (ImageView) findViewById(R.id.virtual_merge_delete);
		virtual_merge_delete.setOnClickListener(this);

		phone_merge_delete = (ImageView) findViewById(R.id.phone_merge_delete);
		phone_merge_delete.setOnClickListener(this);

		phone_merge_delete2 = (ImageView) findViewById(R.id.phone_merge_delete2);
		phone_merge_delete2.setOnClickListener(this);

		company_merge_delete = (ImageView) findViewById(R.id.company_merge_delete);
		company_merge_delete.setOnClickListener(this);

		companye_merge_delete2 = (ImageView) findViewById(R.id.companye_merge_delete2);
		companye_merge_delete2.setOnClickListener(this);

		history_relativeLayout = (RelativeLayout) findViewById(R.id.history_relativeLayout);
		virtual_relativeLayout = (RelativeLayout) findViewById(R.id.virtual_relativeLayout);

		phone_relativeLayout1 = (RelativeLayout) findViewById(R.id.phone_relativeLayout1);
		phone_relativeLayout2 = (RelativeLayout) findViewById(R.id.phone_relativeLayout2);

		company_relativeLayout = (RelativeLayout) findViewById(R.id.company_relativeLayout);
		company_relativeLayout2 = (RelativeLayout) findViewById(R.id.company_relativeLayout2);

		merge_click_back_btn = (ImageView) findViewById(R.id.merge_click_back_btn);
		merge_click_back_btn.setOnClickListener(this);
		
		merge_completeTv = (TextView) findViewById(R.id.merge_completeTv);
		merge_completeTv.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.merge_connections_data, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.local_head:

			local_head.setVisibility(View.GONE);

			break;
		case R.id.list_head:

			list_head.setVisibility(View.GONE);

			break;

		case R.id.history_merge_delete:

			history_relativeLayout.setVisibility(View.GONE);

			break;
		case R.id.virtual_merge_delete:

			virtual_relativeLayout.setVisibility(View.GONE);

			break;
		case R.id.phone_merge_delete:

			phone_relativeLayout1.setVisibility(View.GONE);

			break;
		case R.id.phone_merge_delete2:

			phone_relativeLayout2.setVisibility(View.GONE);

			break;
		case R.id.company_merge_delete:

			company_relativeLayout.setVisibility(View.GONE);

			break;
		case R.id.companye_merge_delete2:

			company_relativeLayout2.setVisibility(View.GONE);

			break;
			
		case R.id.merge_click_back_btn:

			finish();

			break;
			
		case R.id.merge_completeTv:

			Toast.makeText(this, "人脉资料合并成功", Toast.LENGTH_SHORT).show();
			
			break;	
			
			

		}

	}

}
