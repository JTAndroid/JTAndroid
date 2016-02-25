package com.tr.ui.tongren.home;

import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.common.view.MyXListView.IXListViewListener;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.adapter.ProjectApplyAdapter;
import com.tr.ui.tongren.adapter.ProjectApplyAdapter.CollaborationOnClickListener;
import com.tr.ui.tongren.home.fragment.ProjectDetailfragment;
import com.tr.ui.tongren.model.project.ProjectApply;
import com.tr.ui.tongren.model.project.ProjectId;
import com.tr.ui.tongren.model.project.UndertakeParameter;
import com.tr.ui.tongren.model.project.Undertaken;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

public class ProjectApplyActivity extends JBaseActivity implements IBindData {

	private MyXListView projectApplyLv;
	private ProjectApplyAdapter projectApplyAdapter;
	private String projectId;
	private Button btn;
	private LinearLayout rootLl;
	private String organizationId;
	private String recipientName;
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "承接申请", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projectapply);
		initView();
		initData();
	}
	private void initData() {
		projectId = getIntent().getStringExtra("projectId");
		projectApplyAdapter = new ProjectApplyAdapter(this,projectId,new CollaborationOnClickListener() {
			@Override
			public void onClick(View v, final ProjectApply projectApply) {
				btn = (Button) v;
				
				MessageDialog messageDialog = new MessageDialog(ProjectApplyActivity.this);
				messageDialog.setContent("确定合作吗？");
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					

					

					@Override
					public void onFinish(String content) {
						if ("确定".equals(content)) {
						if (projectApply.isOrganization) {
							organizationId = projectApply.organizationId;
							recipientName = projectApply.organizationName;
						}else{
							recipientName = projectApply.proposerName;
						}
						UndertakeParameter undertakeParameter = new UndertakeParameter();
						undertakeParameter.projectId  = projectId;
						undertakeParameter.organizationId = projectApply.organizationId;
						undertakeParameter.recipientId = projectApply.proposerId;
						TongRenReqUtils.doRequestWebAPI(ProjectApplyActivity.this, ProjectApplyActivity.this, undertakeParameter, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_UNDERTAKEPROJECT);	
					
					}}

					@Override
					public void onCancel(String content) {
						// TODO Auto-generated method stub
						
					}
				});
				
			
			}
		});
		projectApplyLv.setAdapter(projectApplyAdapter);
		getData();
	}
	private void getData() {
		showLoadingDialog();
		ProjectId obj = new ProjectId(projectId);
//		根据项目Id查询项目申请列表 /project/manage/getMyProjectApply.json
		TongRenReqUtils.doRequestWebAPI(this, this, obj, new Handler(), EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYPROJECTAPPLY);
	}
	private void initView() {
		projectApplyLv = (MyXListView) findViewById(R.id.projectApplyLv);
		rootLl = (LinearLayout) findViewById(R.id.rootLl);
		projectApplyLv.setPullLoadEnable(false);
		projectApplyLv.setPullRefreshEnable(true);
		projectApplyLv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				getData();
			}

			@Override
			public void onLoadMore() {
			}
		});
	}

	@Override
	public void bindData(int tag, Object object) {
		if (object!=null) {
			switch (tag) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYPROJECTAPPLY:// 根据项目Id查询项目申请列表
				rootLl.setBackgroundResource(R.color.project_bg);
				projectApplyLv.stopRefresh();
					List<ProjectApply> listProjectApply = (List<ProjectApply>) object;
					if (listProjectApply.isEmpty()) {
						rootLl.setBackgroundResource(R.drawable.empty);
					}else{
						projectApplyAdapter.setApplies(listProjectApply);
						projectApplyAdapter.notifyDataSetChanged();
					}
				
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_UNDERTAKEPROJECT:
				Undertaken  undertaken = (Undertaken) object;
				if (undertaken.getProjectId()!=0) {
					if (btn !=null) {
						btn.setText("合作中");
						btn.setEnabled(false);
					}
					ToastUtil.showToast(ProjectApplyActivity.this, "项目启动");
					Intent intent = new Intent(ProjectApplyActivity.this, ProjectDetailsActivity.class) ;
					intent.putExtra("isUndertake", true);
					intent.putExtra("organizationId", organizationId);
					intent.putExtra("recipientName", recipientName);
					setResult(RESULT_OK, intent);
					finish();
					
				}
				break;
			default:
				break;
			}
			
		}else{
			if (projectApplyAdapter.getApplies().isEmpty()) {
				rootLl.setBackgroundResource(R.drawable.empty);
			}
			dismissLoadingDialog();
		}
		dismissLoadingDialog();
	}
	
}
