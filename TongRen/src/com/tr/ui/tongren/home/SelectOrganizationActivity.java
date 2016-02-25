package com.tr.ui.tongren.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.adapter.OrgAdapter;
import com.tr.ui.tongren.model.project.Organization;
import com.tr.ui.widgets.NoScrollListview;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class SelectOrganizationActivity extends JBaseActivity implements IBindData{

	private List<Organization> orgs_myCreate = new ArrayList<Organization>();
	private OrgAdapter orgMyCreateAdapter;
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "我的组织", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectorganization);
		ListView myOrg = (ListView) findViewById(R.id.myOrg);
		 orgMyCreateAdapter = new OrgAdapter(this, orgs_myCreate);
		 myOrg.setAdapter(orgMyCreateAdapter);
		 myOrg.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Organization organization = orgMyCreateAdapter.getItem(arg2);
				Intent intent  = new Intent();
				intent.putExtra("organization", organization);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		 showLoadingDialog();
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("status", "0");
			TongRenReqUtils.doRequestOrg(this, this, jsonObj, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYCREATEORGANIZATIONS);// 创建
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void bindData(int tag, Object object) {
		if (object!=null) {
	
		switch (tag) {
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYCREATEORGANIZATIONS:// 创建
			orgs_myCreate  = (List<Organization>) object;
			orgMyCreateAdapter .setOrgList(orgs_myCreate );
			orgMyCreateAdapter.notifyDataSetChanged();
			break;
		}
		}
		dismissLoadingDialog();
	}
}
