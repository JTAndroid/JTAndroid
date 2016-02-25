package com.tr.ui.tongren.home;

import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.common.view.MyXListView.IXListViewListener;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.adapter.ProjectOperationAdapter;
import com.tr.ui.tongren.model.project.Operation;
import com.tr.ui.tongren.model.project.ProjectId;
import com.utils.http.EAPIConsts.TongRenRequestType;
import com.utils.http.IBindData;

public class ProjectOperationActivtiy extends JBaseActivity implements IBindData {

	private MyXListView projectOperationLv;
	private ProjectOperationAdapter projectOperationAdapter;
	private LinearLayout rootLl;
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "进度", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activtiy_projectoperation);
		initView();
		initData();
	}
	private void initData() {
		final String projectId = getIntent().getStringExtra("projectId");
		getData(projectId);
		projectOperationLv.setPullLoadEnable(false);
		projectOperationLv.setPullRefreshEnable(true);
		projectOperationLv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				getData(projectId);
			}

			@Override
			public void onLoadMore() {
			}
		});
	}
	private void getData(String projectId) {
		showLoadingDialog();
		ProjectId obj =new ProjectId(projectId);
		TongRenReqUtils.doRequestWebAPI(this, this, obj, new Handler(), TongRenRequestType.TONGREN_REQ_GETPROJECTOPERATION);
	}
	private void initView() {
		projectOperationLv = (MyXListView) findViewById(R.id.projectOperationLv);
		rootLl = (LinearLayout)findViewById(R.id.rootLl);
		projectOperationAdapter = new ProjectOperationAdapter(this);
		projectOperationLv.setAdapter(projectOperationAdapter);
		
	}
	@Override
	public void bindData(int tag, Object object) {
		projectOperationLv.stopRefresh();
		if (object!=null) {
			switch (tag) {
			case TongRenRequestType.TONGREN_REQ_GETPROJECTOPERATION:
				List<Operation> listOperation = (List<Operation>) object;
				rootLl.setBackgroundResource(R.color.project_bg);
				if (listOperation.isEmpty()) {
					rootLl.setBackgroundResource(R.drawable.empty);
				}else{
				projectOperationAdapter.setListOperation(listOperation);
				projectOperationAdapter.notifyDataSetChanged();
				}
				break;

			default:
				break;
			}
		}else{
			if (projectOperationAdapter.getListOperation().isEmpty()) {
				rootLl.setBackgroundResource(R.drawable.empty);
			}
		}
		dismissLoadingDialog();
	}
}
