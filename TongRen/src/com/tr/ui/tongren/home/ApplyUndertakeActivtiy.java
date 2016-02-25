package com.tr.ui.tongren.home;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.model.project.Apply;
import com.tr.ui.tongren.model.project.ApplyParameter;
import com.tr.ui.tongren.model.project.Organization;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.TongRenRequestType;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

public class ApplyUndertakeActivtiy extends JBaseActivity implements OnClickListener, IBindData {

	private static final int SELECTORGANIZATION = 1001;
	private TextView projectNameTv;
	private TextView undertakeModeTv;
	private TextView organizationNameTv;
	private RelativeLayout organizationNameRl;
	private RelativeLayout undertakeModeRl;
	private AlertDialog dlg;
	private String projectId;
	private Organization organization;
	private String publisherId;
	private List<Organization> orgs_myCreate;
	private ImageView organizationNameIv;
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "我要承接", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applyundertake);
		initView();
		initData();
	}
	private void initData() {
		String projectName = getIntent().getStringExtra("projectName");
		projectId = getIntent().getStringExtra("projectId");
		publisherId = getIntent().getStringExtra("publisherId");
//		if (!TextUtils.isEmpty(projectName)) {
//			projectNameTv.setText(projectName);
//		}
		organizationNameRl.setOnClickListener(this);
		undertakeModeRl.setOnClickListener(this);
		getData();
	}
	private void getData() {
		showLoadingDialog();
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("status", "0");
			TongRenReqUtils.doRequestOrg(this, this, jsonObj, new Handler(), EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYCREATEORGANIZATIONS);// 组织
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void initView() {
		projectNameTv = (TextView) findViewById(R.id.projectNameTv);
		undertakeModeTv = (TextView) findViewById(R.id.undertakeModeTv);
		organizationNameTv = (TextView) findViewById(R.id.organizationNameTv);
		organizationNameRl = (RelativeLayout) findViewById(R.id.organizationNameRl);
		undertakeModeRl = (RelativeLayout) findViewById(R.id.undertakeModeRl);
		organizationNameIv = (ImageView)  findViewById(R.id.organizationNameIv);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_createflow, menu);
		menu.findItem(R.id.flow_create).setTitle("完成");
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.flow_create:
			showLoadingDialog();
			if ("组织".equals(undertakeModeTv.getText())) {
				if (organization==null) {
					ToastUtil.showToast(ApplyUndertakeActivtiy.this, "请选择承接组织");
					dismissLoadingDialog();
					return false;
				}
			}
			ApplyParameter applyParameter = new ApplyParameter();
			applyParameter.projectId = projectId;
			applyParameter.publisherId=  publisherId;
			if (organization!=null) {
				applyParameter.organizationId = organization.getId()+"";
			}
			TongRenReqUtils.doRequestWebAPI(this, this, applyParameter, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_APPLY);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data!=null) {
			switch (requestCode) {
			case SELECTORGANIZATION:
				organization = null;
				organization = (Organization) data.getSerializableExtra("organization");
				organizationNameTv.setText(organization.getName());
				organizationNameTv.setTextColor(R.color.projecttextcolor1);
				break;

			default:
				break;
			}
		}
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.organizationNameRl:
			Intent intent = new Intent(ApplyUndertakeActivtiy.this, SelectOrganizationActivity.class);
			startActivityForResult(intent, SELECTORGANIZATION);
			break;
		case R.id.undertakeModeRl:
			dlg = new AlertDialog.Builder(this).create();
			dlg.show();
			dlg.setCanceledOnTouchOutside(true);
			Window window = dlg.getWindow();
			window.setContentView(R.layout.alertdialog_applyundertake);
			final TextView  alertdialog_applyundertake_organization = (TextView) window.findViewById(R.id.alertdialog_applyundertake_organization);
			alertdialog_applyundertake_organization.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dlg.dismiss();
					organizationNameRl.setVisibility(View.VISIBLE);
					undertakeModeTv.setText(alertdialog_applyundertake_organization.getText().toString());
				}
			});
			final TextView  alertdialog_applyundertake_person = (TextView) window.findViewById(R.id.alertdialog_applyundertake_person);
			alertdialog_applyundertake_person.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dlg.dismiss();
					organization = null; 
					organizationNameRl.setVisibility(View.GONE);
					undertakeModeTv.setText(alertdialog_applyundertake_person.getText().toString());
				}
			});
			break;
		default:
			break;
		}
	}
	@Override
	public void bindData(int tag, Object object) {
		if (object!=null) {
			switch (tag) {
			case TongRenRequestType.TONGREN_REQ_APPLY:
				Apply apply = (Apply) object;
				if (apply.getStatus()==1) {
					ToastUtil.showToast(ApplyUndertakeActivtiy.this, "申请成功,请等待审核");
					Intent intent = new Intent(this,ProjectDetailsActivity.class);
					intent.putExtra("ApplyStatus",true);
					setResult(99, intent);
					finish();
				}
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYCREATEORGANIZATIONS:// 创建
				orgs_myCreate = (List<Organization>) object;
				if (orgs_myCreate==null||orgs_myCreate.isEmpty()) {
					organizationNameRl.setEnabled(false);
					organizationNameTv.setText("您尚未创建过组织");
					organizationNameIv.setVisibility(View.GONE);
					undertakeModeTv.setText("人");
					organization = null; 
					organizationNameRl.setVisibility(View.GONE);
				}
				break;

			default:
				break;
			}
		}else{
			if (orgs_myCreate==null||orgs_myCreate.isEmpty()) {
				organizationNameRl.setEnabled(false);
				organizationNameTv.setText("您尚未创建过组织");
				organizationNameIv.setVisibility(View.GONE);
				undertakeModeTv.setText("人");
				organization = null; 
				organizationNameRl.setVisibility(View.GONE);
			}
		}
		dismissLoadingDialog();
	}
}
