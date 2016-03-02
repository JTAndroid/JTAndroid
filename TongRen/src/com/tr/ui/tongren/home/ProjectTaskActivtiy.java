package com.tr.ui.tongren.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tr.App;
import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.common.view.MyXListView.IXListViewListener;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.adapter.ProjectTaskAdapter;
import com.tr.ui.tongren.home.AddTaskActivity.TaskType;
import com.tr.ui.tongren.model.project.OrganizationRole;
import com.tr.ui.tongren.model.project.ProjectAndOrganizationId;
import com.tr.ui.tongren.model.task.AssignUserInfo;
import com.tr.ui.tongren.model.task.Task;
import com.tr.ui.tongren.model.task.TaskDesignatedParameter;
import com.tr.ui.tongren.model.task.TaskOrganizationParameter;
import com.tr.ui.tongren.model.task.TaskParameter;
import com.tr.ui.tongren.model.task.TaskVO;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.TongRenRequestType.TongRenInfoType;

public class ProjectTaskActivtiy extends JBaseActivity implements OnClickListener, IBindData {

	private static final int ADDTASKINTENT_REQUESTCODE = 1001;
	private LinearLayout addTaskLl;
	private TextView completeTaskSumTv;
	private MyXListView projectTaskLv;
	private ProjectTaskAdapter projectTaskAdapter;
	private String projectId;
	private String organizationId;
	private int completeTaskSum = 0;
	private long taskPid;
	private String projectName;
	private StringBuffer buffer = new StringBuffer();
	private TongRenInfoType tongRenInfoType;
	private ActionBar jabGetActionBar;;
	private List<Task> myCompleteTaskList = new ArrayList<Task>();
	private LinearLayout rootLl;
	private String projectAcceptId;
	private int status;
	private View view_task;
	@Override
	public void initJabActionBar() {
		jabGetActionBar = jabGetActionBar();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projecttask);
		initView();
		initData();
	}
	private void initData() {
		tongRenInfoType = (TongRenInfoType) getIntent(). getSerializableExtra(EAPIConsts.TongRenRequestType.TongRenInfoType);
		projectId = getIntent().getStringExtra("projectId");
		organizationId = getIntent().getStringExtra("organizationId");
		projectName =  getIntent().getStringExtra("projectName");
		projectAcceptId = getIntent().getStringExtra("projectAcceptId");
		status = getIntent().getIntExtra("projectstatus", 0);
		addTaskLl.setOnClickListener(this);
		completeTaskSumTv.setOnClickListener(this);
		projectTaskAdapter = new ProjectTaskAdapter(this);
		projectTaskLv.setAdapter(projectTaskAdapter);
//		if (tongRenInfoType == TongRenInfoType.MY) {  //我的任务有分页，其他两个没有分页，所以下拉只请求我的任务列表数据
//			projectTaskLv.setPullLoadEnable(true);
//		}else{
			projectTaskLv.setPullLoadEnable(false);
//		}
		
		projectTaskLv.setPullRefreshEnable(true);
		projectTaskLv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				getData();
			}

			@Override
			public void onLoadMore() {
				 //我的任务有分页，其他两个没有分页，所以下拉只请求我的任务列表数据
				TaskParameter taskParameter = new TaskParameter();
				taskParameter.type = "0";
				taskParameter.organizationId = organizationId;
				taskParameter.time = System.currentTimeMillis();
				taskParameter.pageSize = "20";
				
				TongRenReqUtils.doRequestWebAPI(ProjectTaskActivtiy.this, ProjectTaskActivtiy.this, taskParameter, new Handler(), EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYTASKLIST);
			}
		});
	}
	@Override
	public void onResume() {
		getData();
		super.onResume();
	}
	private void getData() {
		showLoadingDialog();
		
		if (tongRenInfoType == TongRenInfoType.PROJECT) {
			if (App.getUserID().equals(projectAcceptId)) {
				if (status == 3) {
					view_task.setVisibility(View.GONE);
					addTaskLl.setVisibility(View.GONE);
				}else{
					addTaskLl.setVisibility(View.VISIBLE);
				}
			}else{
				view_task.setVisibility(View.GONE);
				addTaskLl.setVisibility(View.GONE);
			}
			ProjectAndOrganizationId projectAndOrganizationId = new ProjectAndOrganizationId();
			projectAndOrganizationId.organizationId = organizationId;
			projectAndOrganizationId.projectId =projectId;
			HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "任务", false, null, true, true);
			TongRenReqUtils.doRequestWebAPI(this, this, projectAndOrganizationId, new Handler(), EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPRIMARYTASK);
		}else if (tongRenInfoType == TongRenInfoType.ORGANIZATION){
			TaskOrganizationParameter taskParameter = new TaskOrganizationParameter();
			taskParameter.type = "-1";
			taskParameter.orgId = organizationId;
			addTaskLl.setVisibility(View.GONE);
			taskParameter.time = System.currentTimeMillis();
			HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "组织任务", false, null, true, true);
			TongRenReqUtils.doRequestWebAPI(this, this, taskParameter, new Handler(), EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGTASKLIST);
		}else if (tongRenInfoType == TongRenInfoType.MY){
			ProjectAndOrganizationId OrganizationId = new ProjectAndOrganizationId();
			OrganizationId.organizationId = organizationId;
			TongRenReqUtils.doRequestWebAPI(this, this, OrganizationId, new Handler(), EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYROLE);
			TaskParameter taskParameter = new TaskParameter();
			taskParameter.type = "0";
			taskParameter.organizationId = organizationId;
			taskParameter.time = System.currentTimeMillis();
			taskParameter.pageSize = "100";
			
			HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "我的任务", false, null, true, true);
			TongRenReqUtils.doRequestWebAPI(this, this, taskParameter, new Handler(), EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYTASKLIST);
		}
		
	}
	private void initView() {
		addTaskLl = (LinearLayout) findViewById(R.id.addTaskLl);
		view_task = findViewById(R.id.view_task);
		completeTaskSumTv  = (TextView) View.inflate(this, R.layout.bottom_task_complete, null);
		projectTaskLv = (MyXListView) findViewById(R.id.projectTaskLv);
		projectTaskLv.addFooterView(completeTaskSumTv);
		rootLl =  (LinearLayout) findViewById(R.id.rootLl);
		projectTaskLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2!=0) {
					Task itemTask = projectTaskAdapter.getItem(arg2-1);
					Intent intent = new Intent(ProjectTaskActivtiy.this, TaskDetailsActivity.class);
					intent.putExtra("TaskDetailsData", itemTask);
					intent.putExtra("projectName", projectName);
					intent.putExtra(EAPIConsts.TongRenRequestType.TongRenInfoType,tongRenInfoType);
					startActivity(intent);
				}
			}
		});
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.addTaskLl://添加任务
			Intent AddTaskintent  = new Intent(ProjectTaskActivtiy.this, AddTaskActivity.class);
			AddTaskintent.putExtra(EAPIConsts.TongRenRequestType.TongRenInfoType,tongRenInfoType);
			AddTaskintent.putExtra("organizationId",organizationId);
			AddTaskintent.putExtra("taskPid", taskPid);
			AddTaskintent.putExtra("taskType", TaskType.Create);
			startActivityForResult(AddTaskintent, ADDTASKINTENT_REQUESTCODE);
			break;
		case R.id.completeTaskSumTv://已完成任务
			projectTaskAdapter.setCompleteListTask(myCompleteTaskList);
			projectTaskAdapter.notifyDataSetChanged();
			completeTaskSumTv.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	@Override
	public void bindData(int tag, Object object) {
		if (object!=null) {
			rootLl.setBackgroundResource(R.color.white);
			switch (tag) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPRIMARYTASK: //获取项目主任务
				dismissLoadingDialog();
				projectTaskLv.stopRefresh();
				myCompleteTaskList.clear();
				completeTaskSum = 0;
				Task task = (Task) object;
				organizationId =  task.getOrganizationId()+"";
				taskPid = task.getId();
				List<Task> listTask = task.getChildren();
				
				for (int i = 0; i < listTask.size(); i++) {
					if (listTask.get(i).getTaskStatus()==2) {
						Task task2 = listTask.get(i);
						listTask.remove(task2);
						myCompleteTaskList.add(task2);
						completeTaskSum ++;
					}
					
				}
				
				if (completeTaskSum==0) {
					completeTaskSumTv.setVisibility(View.GONE);
				}else{
					completeTaskSumTv.setVisibility(View.VISIBLE);
				}
				completeTaskSumTv.setText(completeTaskSum+"个已完成任务");
				if (listTask.isEmpty()) {
					rootLl.setBackgroundResource(R.drawable.empty);
				}else{
					
				projectTaskAdapter.setListTask(listTask);
				}
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYTASKLIST:// 我的任务列表
				dismissLoadingDialog();
				projectTaskLv.stopRefresh();
				myCompleteTaskList.clear();
					completeTaskSum = 0;
					List<TaskVO> listTaskVO = (List<TaskVO>) object;
					List<Task> MylistTask =  new ArrayList<Task>();
					for (int i = 0; i < listTaskVO.size(); i++) {
						MylistTask.add(listTaskVO.get(i).getTask());
					}
					
					for (int i = 0; i < MylistTask.size(); i++) {
						if (MylistTask.get(i).getTaskStatus()==2) {
							Task taskMy = MylistTask.get(i);
							MylistTask.remove(taskMy);
							myCompleteTaskList.add(taskMy);
							completeTaskSum ++;
						}
						
					}
					if (completeTaskSum==0) {
						completeTaskSumTv.setVisibility(View.GONE);
					}else{
						completeTaskSumTv.setVisibility(View.VISIBLE);
					}
					completeTaskSumTv.setText(completeTaskSum+"个已完成任务");
					if (MylistTask.isEmpty()) {
						rootLl.setBackgroundResource(R.drawable.empty);
					}else{
					projectTaskAdapter.setListTask(MylistTask);
					}
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETORGTASKLIST:// 组织任务列表
				dismissLoadingDialog();
				projectTaskLv.stopRefresh();
				myCompleteTaskList.clear();
				completeTaskSum = 0;
				List<Task> listOrgTask = (List<Task>) object;
			
				for (int i = 0; i < listOrgTask.size(); i++) {
					if (listOrgTask.get(i).getTaskStatus()==2) {
						completeTaskSum ++;
						Task taskOrg = listOrgTask.get(i);
						listOrgTask.remove(taskOrg);
						myCompleteTaskList.add(taskOrg);
					}
					
				}
				if (completeTaskSum==0) {
					completeTaskSumTv.setVisibility(View.GONE);
				}else{
					completeTaskSumTv.setVisibility(View.VISIBLE);
				}
				completeTaskSumTv.setText(completeTaskSum+"个已完成任务");
				if (listOrgTask.isEmpty()) {
					rootLl.setBackgroundResource(R.drawable.empty);
				}else{
				projectTaskAdapter.setListTask(listOrgTask);
				}
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYROLE://获取我在组织中的角色 
				final List<OrganizationRole> listOrganizationRole = (List<OrganizationRole>) object;
				new AsyncTask<Void, Void, Boolean>() {

					@Override
					protected Boolean doInBackground(Void... params) {
						for (OrganizationRole organizationRole : listOrganizationRole) {
							if (organizationId.equals(organizationRole.getOrganizationId()+"")) {
								if ("管理者".equals(organizationRole.getDescription())||"创建者".equals(organizationRole.getDescription())) {
									return true;
								}
								
							}
						}
						return false;
					}
					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						if (result) {
							addTaskLl.setVisibility(View.VISIBLE);
						}else{
							addTaskLl.setVisibility(View.GONE);
						}
					}
				}.execute();
				break;
			default:
				break;
			}
		}else{
			if (projectTaskAdapter.getListTask().isEmpty()) {
				rootLl.setBackgroundResource(R.drawable.empty);
			}
			dismissLoadingDialog();
		}
		
		projectTaskAdapter.notifyDataSetChanged();
	}
}
