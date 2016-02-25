package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.ui.organization.model.hight.CustomerHight;
import com.tr.ui.organization.model.hight.CustomerHightInfo;
import com.tr.ui.organization.model.hight.CustomerHight_json;
import com.tr.ui.organization.model.hight.CustomerHight_re;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.orgdetails.EditSeniorManagementActivity;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.utils.MakeListView;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.OrganizationReqType;
/**
 * 高层治理
 * @author huangzhiyu
 *
 */
public class SeniorManagementActivity extends BaseActivity implements OnClickListener,IBindData{
	
	private RelativeLayout quit_senior_management_Rl;
	private ImageView edit_senior_management_Tv;
	private ListView senior_executive_Lv;
	private ListView board_of_directors_Lv;
	private ListView board_of_supervisors_Lv;
	private ListView dignitary_Lv;
	private CustomerHight CustomerHight;
	private ArrayList<CustomerHightInfo> dshList;
	private ArrayList<CustomerHightInfo> jshList;
	private ArrayList<CustomerHightInfo> ggList;
	private ArrayList<CustomerHightInfo> ggjzList;
	private ArrayList<CustomerHightInfo> gczlList;
	private ArrayList<CustomerHightInfo> Edit_Seniormanagement_Bean;
	private ListView custom_Lv;
	private ArrayList<CustomerPersonalLine> cuLines;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.org_senior_management);
		CustomerHight_re customerHight_re = new CustomerHight_re();
		//请求接口
		OrganizationReqUtil.doRequestWebAPI(context, this, customerHight_re, null, OrganizationReqType.ORGANIZATION_REQ_FINDHEGHTONE);
		
		quit_senior_management_Rl = (RelativeLayout) findViewById(R.id.quit_senior_management_Rl);
		edit_senior_management_Tv = (ImageView) findViewById(R.id.edit_senior_management_Tv);
		quit_senior_management_Rl.setOnClickListener(this);
		edit_senior_management_Tv.setOnClickListener(this);
		senior_executive_Lv = (ListView) findViewById(R.id.senior_executive_Lv);
		board_of_directors_Lv = (ListView) findViewById(R.id.board_of_directors_Lv);
		custom_Lv = (ListView) findViewById(R.id.custom_Lv);
		board_of_supervisors_Lv = (ListView) findViewById(R.id.board_of_supervisors_Lv);
		dignitary_Lv = (ListView) findViewById(R.id.dignitary_Lv);
		initData();
		
	}
	private void initData() {
//		if (dshList!=null) {
//			Senior_management_Adapter adapter = new Senior_management_Adapter();
//			dignitary_Lv.setAdapter(adapter);		
//		}
		
	}
	@Override
	public void onClick(View v) {
		if(v == quit_senior_management_Rl){
			finish();
		}else if(v == edit_senior_management_Tv){
			Intent intent = new Intent(getApplicationContext(), EditSeniorManagementActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("Edit_Seniormanagement_Bean", Edit_Seniormanagement_Bean);
//			intent.putParcelableArrayListExtra("Edit_Seniormanagement_Bean", Edit_Seniormanagement_Bean);
			intent.putExtras(bundle);
			startActivityForResult(intent,999);
		}
	}
	@Override
	public void bindData(int tag, Object object) {
		Map<String, Object> map = (Map<String, Object>) object;

		dshList = (ArrayList<CustomerHightInfo>) map
				.get("dshList");
		jshList = (ArrayList<CustomerHightInfo>) map
				.get("jshList");
		ggList = (ArrayList<CustomerHightInfo>) map
				.get("ggList");
		ggjzList = (ArrayList<CustomerHightInfo>) map
				.get("ggjzList");
		gczlList = (ArrayList<CustomerHightInfo>) map
				.get("gczlList");
		if (dshList!=null) {
			MakeListView.Hight(context, board_of_directors_Lv, dshList);
		}
		if (jshList!=null) {
			MakeListView.Hight(context, board_of_supervisors_Lv, jshList);
		}
		if (ggList!=null) {
			MakeListView.Hight(context, dignitary_Lv, ggList);
		}
		if (ggjzList!=null) {
			MakeListView.Hight(context, dignitary_Lv, ggjzList);
		}
		if (gczlList!=null) {
		
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null) {
			switch (arg1) {
			case RESULT_OK:
				Edit_Seniormanagement_Bean =  arg2.getParcelableArrayListExtra("Edit_Seniormanagement_Bean");
				MakeListView.Hight(context, senior_executive_Lv, Edit_Seniormanagement_Bean);
				cuLines = (ArrayList<CustomerPersonalLine>) arg2.getSerializableExtra("Custom_Bean");
				MakeListView.Custom(context, custom_Lv, cuLines);
				break;

			default:
				break;
			}
		}
	}
	
}
