package com.tr.ui.tongren.home;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.home.OrganizationMemberActivity.FromType;
import com.tr.ui.tongren.home.OrganizationMemberActivity.OrganizationMemberType;
import com.tr.ui.tongren.model.project.OrganizationSumup;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.TongRenRequestType.TongRenInfoType;
import com.utils.time.TimeUtil;

public class OrganizationSumupActivity extends JBaseActivity implements IBindData, OnClickListener {
	
	private static final int ORGANIZATIONMEMBER_REQUESTCODE = 1001;
	private static final int ORGANIZATIONACCESSORY_REQUESTCODE = 1002;
	private static final int ORGANIZATION_REQUESTCODE = 1003;
	private RelativeLayout orgTaskRl, orgResourceRl, orgMemberRl;
	private TextView orgTaskNumTv, orgResourceNumTv, orgMemberNumTv;
	private String oid;
	private String organizationName;
	
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "组织概况", false, null, true, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tongren_org_survey_activity);
		initView();
		
		oid = getIntent().getStringExtra("oid");
		organizationName = getIntent().getStringExtra("organizationName");
//		getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONSUMUP);
	}
	
	private void initView(){
		orgTaskRl = (RelativeLayout) findViewById(R.id.orgTaskRl);
		orgResourceRl = (RelativeLayout) findViewById(R.id.orgResourceRl);
		orgMemberRl = (RelativeLayout) findViewById(R.id.orgMemberRl);
		
		orgTaskNumTv = (TextView) findViewById(R.id.orgTaskNumTv);
		orgResourceNumTv = (TextView) findViewById(R.id.orgResourceNumTv);
		orgMemberNumTv = (TextView) findViewById(R.id.orgMemberNumTv);
		
		orgTaskRl.setOnClickListener(this);
		orgResourceRl.setOnClickListener(this);
		orgMemberRl.setOnClickListener(this);
	}
	
	private void initData(OrganizationSumup org){
		orgMemberNumTv.setText(org.getUserList().size()+"");
	}
	
	@Override
	public void bindData(int tag, Object object) {
		if(object == null){
			return;
		}
		switch(tag){
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONSUMUP:
			OrganizationSumup org = (OrganizationSumup) object;
			initData(org);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch(v.getId()){
		case R.id.orgTaskRl:
			Intent taskIntent = new Intent(this, ProjectTaskActivtiy.class);
			taskIntent.putExtra("organizationId",oid);
			taskIntent.putExtra(EAPIConsts.TongRenRequestType.TongRenInfoType, TongRenInfoType.ORGANIZATION);
			taskIntent.putExtra("projectName",organizationName);
			startActivityForResult(taskIntent,ORGANIZATION_REQUESTCODE);
			break;
		case R.id.orgResourceRl:
			Intent AccessoryIntent = new Intent(this, ProjectAccessoryActivtiy.class);
			AccessoryIntent.putExtra("organizationId",oid);
			AccessoryIntent.putExtra(EAPIConsts.TongRenRequestType.TongRenInfoType, TongRenInfoType.ORGANIZATION);
			startActivityForResult(AccessoryIntent, ORGANIZATIONACCESSORY_REQUESTCODE);
			break;
		case R.id.orgMemberRl:
			Intent memberIntent = new Intent(this, OrganizationMemberActivity.class);
			memberIntent.putExtra("organizationId",oid);
			memberIntent.putExtra("organizationMemberType",OrganizationMemberType.show);
			memberIntent.putExtra("fromType", FromType.project);
			startActivityForResult(memberIntent, ORGANIZATIONMEMBER_REQUESTCODE);
			break;
		}
	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case EAPIConsts.handler.show_err:
				break;
			}
		}
	};
	
	private void getDataFromServer(int requestType) {
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("oid", oid);
			switch (requestType) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGANIZATIONSUMUP:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		TongRenReqUtils.doRequestOrg(this, this, jobj, handler, requestType);
	}
}
