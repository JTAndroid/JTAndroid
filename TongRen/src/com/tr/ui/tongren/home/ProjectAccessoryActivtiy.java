package com.tr.ui.tongren.home;

import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.model.user.OrganizationInfo;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.common.view.MyXListView.IXListViewListener;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.adapter.AccessoryAdapter;
import com.tr.ui.tongren.model.project.ProjectAndOrganizationId;
import com.tr.ui.tongren.model.project.Resource;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.TongRenRequestType.TongRenInfoType;
import com.utils.http.IBindData;

public class ProjectAccessoryActivtiy extends JBaseActivity implements IBindData {
	private AccessoryAdapter accessoryAdapter;
	private MyXListView projectAccessoryLv;
	private String projectId;
	private String organizationId;
	private TongRenInfoType tongRenInfoType;
	private ActionBar jabGetActionBar;
	private LinearLayout rootLl;
	@Override
	public void initJabActionBar() {
		jabGetActionBar = jabGetActionBar();
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activtiy_projectaccessory);
		initView();
		initData();
	}
	private void initData() {
		projectId = getIntent().getStringExtra("projectId");
		organizationId = getIntent().getStringExtra("organizationId");
		
		accessoryAdapter = new AccessoryAdapter(this);
		projectAccessoryLv.setAdapter(accessoryAdapter);
		projectAccessoryLv.setPullLoadEnable(false);
		projectAccessoryLv.setPullRefreshEnable(true);
		projectAccessoryLv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				getData();
			}

			@Override
			public void onLoadMore() {
			}
		});
		getData();
	}
	private void getData() {
		showLoadingDialog();
		tongRenInfoType = (TongRenInfoType) getIntent(). getSerializableExtra(EAPIConsts.TongRenRequestType.TongRenInfoType);
		if (tongRenInfoType == TongRenInfoType.PROJECT) {
			ProjectAndOrganizationId projectAndOrganizationId = new ProjectAndOrganizationId();
			projectAndOrganizationId.organizationId = organizationId;
			projectAndOrganizationId.projectId =projectId;
			HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "文档", false, null, true, true);
			TongRenReqUtils.doRequestWebAPI(this, this, projectAndOrganizationId, new Handler(), EAPIConsts.TongRenRequestType.TONGREN_REQ_GETRESOURCEPROJECT);
		}else if (tongRenInfoType == TongRenInfoType.ORGANIZATION){
			ProjectAndOrganizationId projectAndOrganizationId = new ProjectAndOrganizationId();
			projectAndOrganizationId.organizationId = organizationId;
			HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "组织资源", false, null, true, true);
			TongRenReqUtils.doRequestWebAPI(this, this, projectAndOrganizationId, new Handler(), EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGRESOURCE);
		}else if (tongRenInfoType == TongRenInfoType.MY){
			ProjectAndOrganizationId projectAndOrganizationId = new ProjectAndOrganizationId();
			projectAndOrganizationId.organizationId = organizationId;
			HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "我的资源", false, null, true, true);
			TongRenReqUtils.doRequestWebAPI(this, this, projectAndOrganizationId, new Handler(), EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYORGRESOURCE);
		}
		
	}
	private void initView() {
		projectAccessoryLv = (MyXListView) findViewById(R.id.projectAccessoryLv);
		rootLl = (LinearLayout) findViewById(R.id.rootLl);
	}
	@Override
	public void bindData(int tag, Object object) {
		if (object!=null) {
			switch (tag) {
			case  EAPIConsts.TongRenRequestType.TONGREN_REQ_GETRESOURCEPROJECT: //获取项目中的文档资源
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYORGRESOURCE://获得我的资源列表 
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGRESOURCE: 
				rootLl.setBackgroundResource(R.color.project_bg);
				projectAccessoryLv.stopRefresh();
				List<Resource> listResource =  (List<Resource>) object;
				if (listResource.isEmpty()) {
					rootLl.setBackgroundResource(R.drawable.empty);
				}else{
				accessoryAdapter.setListResource(listResource);
				accessoryAdapter.notifyDataSetChanged();
				}
				break;

			default:
				break;
			}
		}else{
			if (accessoryAdapter.getListResource().isEmpty()) {
				rootLl.setBackgroundResource(R.drawable.empty);
			}
			dismissLoadingDialog();
		}
		dismissLoadingDialog();
	}
}
