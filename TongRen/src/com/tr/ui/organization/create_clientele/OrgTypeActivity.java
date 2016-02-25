package com.tr.ui.organization.create_clientele;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.organization.orgdetails.EditClientBasicInfomationActivity;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.utils.MakeListView;

public class OrgTypeActivity extends BaseActivity {
	private RelativeLayout quit_org_type_Rl;
	private ListView org_type_Lv;
	private String[] type={"金融机构","一般企业","中介机构","政府组织","专业媒体"};
	private boolean organ;
	private int type2;
	private boolean client;
	private boolean editClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_type_activity);
		organ = this.getIntent().getBooleanExtra("organ", false);
		client = this.getIntent().getBooleanExtra("client", false);
		editClient = this.getIntent().getBooleanExtra("editClient", false);
		type2 = this.getIntent().getIntExtra("Type", 2);
		Log.e("MSG", "Type:"+type2);
		init();
		initData();
	}

	private void initData() {
//		MakeListView.makelistviewAdapter(context, org_type_Lv, type);
		org_type_Lv.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder1 holder=null;
				if (convertView==null) {
					holder = new ViewHolder1();
					convertView = View.inflate(context, R.layout.people_list_item_3, null);
					holder.list_Tv1 = (TextView) convertView.findViewById(R.id.list_Tv);
					holder.list_Ib1 = (ImageView) convertView.findViewById(R.id.list_Ib);
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder1) convertView.getTag();
				}
				holder.list_Tv1.setText(type[position]);
				if (position==4) {
					holder.list_Ib1.setBackgroundResource(R.drawable.people_right_arrow);
				}else{
					holder.list_Ib1.setBackgroundDrawable(null);
				}
				return convertView;
			}
			@Override
			public long getItemId(int position) {
				return position;
			}
			@Override
			public Object getItem(int position) {
				return type[position];
			}
			@Override
			public int getCount() {
				return type.length;
			}
		});
		
		org_type_Lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position==org_type_Lv.getLastVisiblePosition()) {
					if (organ) {
						Intent intent = new Intent(OrgTypeActivity.this, MediaTypeOrgActivity.class);
						startActivity(intent);
						finish();
					}else if(client){
						Intent intent = new Intent(OrgTypeActivity.this, MediaTypeActivity.class);
						startActivity(intent);
						finish();
					}else if(editClient){						
						Intent intent = new Intent(OrgTypeActivity.this, MediaEditClientTypeActivity.class);
						startActivity(intent);
						finish();
					}
					
					finish();
				}else{
					String text = type[position];
					if(type2 == 0){
						Intent intent = new Intent(OrgTypeActivity.this, CreateClienteleActivity.class);
						intent.putExtra("type", text);
						setResult(21, intent);
						finish();
					}else if(type2 == 1){
						Intent intent = new Intent(OrgTypeActivity.this, EditClientBasicInfomationActivity.class);
						intent.putExtra("type", text);
						setResult(21, intent);
						finish();
					}else if(type2 == 2){
						Intent intent = new Intent(OrgTypeActivity.this, CreateOrganizationActivity.class);
						intent.putExtra("type", text);
						setResult(21, intent);
						finish();
					}
					
					
				}
			}
		});
	}
	class ViewHolder1{
		TextView list_Tv1;
		ImageView list_Ib1;
	}
	private void init() {
		quit_org_type_Rl = (RelativeLayout) findViewById(R.id.quit_org_type_Rl);
		org_type_Lv = (ListView) findViewById(R.id.org_type_Lv);
		
		quit_org_type_Rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
