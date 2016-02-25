package com.tr.ui.tongren.home;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ActionBar;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.model.demand.Metadata;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.cread.EducationDataActivity;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.tongren.home.OrganizationMemberActivity.FromType;
import com.tr.ui.tongren.home.OrganizationMemberActivity.OrganizationMemberType;
import com.tr.ui.tongren.model.task.AddOrganizationTask;
import com.tr.ui.tongren.model.task.AddTask;
import com.tr.ui.tongren.model.task.Assign;
import com.tr.ui.tongren.model.task.AssignUserInfo;
import com.tr.ui.tongren.model.task.EditOrganizationTask;
import com.tr.ui.tongren.model.task.Task;
import com.tr.ui.tongren.model.task.UpdateSubTask;
import com.tr.ui.widgets.IMEditMumberGrid.HeadName;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.TongRenRequestType.TongRenInfoType;
import com.utils.log.ToastUtil;
import com.utils.time.TimeUtil;

public class AddTaskActivity extends JBaseActivity implements OnClickListener, IBindData{

	private static final int ORGANIZATIONMEMBER_REQUESTCODE = 1000;
	private MyEditTextView taskNameMetv;
	private MyEditTextView taskTimeMetv;
	private MyEditTextView taskDesignatedMetv;
	private String projectTime;
	private String organizationId;
	private long taskPid;
	private String code;
	private Task taskData;
	private String designated;
	private TaskType  taskType;
	private TongRenInfoType tongRenInfoType;
	private ArrayList<HeadName> useSelectIdList = new ArrayList<HeadName>();
	private ActionBar jabGetActionBar;
	public enum TaskType{
		Create,
		Edit;
		
	}
	@Override
	public void initJabActionBar() {
		jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "添加任务", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addtask);
		initView();
		initData();
	}
	private void initData() {
		taskPid = getIntent().getLongExtra("taskPid", 0L);
		organizationId = getIntent().getStringExtra("organizationId");
		taskData = (Task) getIntent().getSerializableExtra("taskData");
		tongRenInfoType = (TongRenInfoType) getIntent(). getSerializableExtra(EAPIConsts.TongRenRequestType.TongRenInfoType);
		designated =  getIntent().getStringExtra("designated");
		taskType =  (TaskType) getIntent().getSerializableExtra("taskType");
		
		if (taskData!=null) {
			HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "编辑任务", false, null, true, true);
			taskNameMetv.setText(taskData.getTitle());
			if (!TextUtils.isEmpty(taskData.getStartTime().trim())&&!TextUtils.isEmpty(taskData.getEndTime().trim())) {
				taskTimeMetv.setText(taskData.getStartTime()+"至"+taskData.getEndTime());
			}
			if (taskData.extend!=null&&taskData.extend.assignInfo!=null&&!taskData.extend.assignInfo.isEmpty()) {
				new AsyncTask<Void, Void, String>() {

					@Override
					protected String doInBackground(Void... params) {
						String extendName = "";
						for (int i = 0; i < taskData.extend.assignInfo.size(); i++) {
							AssignUserInfo userinfo = taskData.extend.assignInfo.get(i);
							if (i == taskData.extend.assignInfo.size()) {
								extendName+=userinfo.name;
							}else{
								extendName+=userinfo.name+" ";
							}
							HeadName headName = new HeadName();
							headName.setUserID(userinfo.id+"");
							headName.setName(userinfo.name);
							headName.setImage(userinfo.path);
							useSelectIdList.add(headName);
						}
						return extendName;
					}
					protected void onPostExecute(String result) {
						taskData.extend.assignInfo.clear();
						taskDesignatedMetv.setText(result);
					};
				}.execute();
			}
		}
		taskTimeMetv.setOnClickListener(this);
		taskDesignatedMetv.setOnClickListener(this);
	}
	private void initView() {
		taskNameMetv = (MyEditTextView) findViewById(R.id.taskNameMetv);
		taskTimeMetv = (MyEditTextView) findViewById(R.id.taskTimeMetv);
		taskDesignatedMetv = (MyEditTextView) findViewById(R.id.taskDesignatedMetv);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_createflow, menu);
		menu.findItem(R.id.flow_create).setTitle("完成");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.flow_create:
			if (TextUtils.isEmpty(taskNameMetv.getText())) {
				ToastUtil.showToast(AddTaskActivity.this, "请填写任务名称");
				return false;
			}
			if (TextUtils.isEmpty(taskTimeMetv.getText())) {
				ToastUtil.showToast(AddTaskActivity.this, "任务时间未填写");
				return false;
			}
			if (useSelectIdList.isEmpty()) {
				ToastUtil.showToast(AddTaskActivity.this, "任务指派人未填写");
				return false;
			}
			if (!TextUtils.isEmpty(taskNameMetv.getText())&&!TextUtils.isEmpty(taskTimeMetv.getText())&&!useSelectIdList.isEmpty()) {
				showLoadingDialog();
				if (tongRenInfoType == TongRenInfoType.PROJECT) {
					AddTask addTask = new AddTask();
					
					addTask.organizationId = organizationId;
					addTask.title = taskNameMetv.getText();
					 if (taskType==TaskType.Edit){
						 taskData.setTitle(taskNameMetv.getText());
					 }
					if (!TextUtils.isEmpty(taskTimeMetv.getText())) {
						String[] split = taskTimeMetv.getText().split("至");
						String startDatetime = split[0].replace("年", "-").replace("月", "-").replace("日", " ");
						String endDatetime = split[1].replace("年", "-").replace("月", "-").replace("日", " ");
						StringBuilder startDatebuilder = new StringBuilder(startDatetime+"00:00");
						StringBuilder endDatebuilder = new StringBuilder(endDatetime+"00:00");
						addTask.startDate = startDatebuilder.toString();
						addTask.endDate = endDatebuilder.toString();
						
						 if (taskType==TaskType.Edit){
							 long startTimeMillis = TimeUtil.getTimeMillisWithProject(split[0]);
								long endTimeMillis = TimeUtil.getTimeMillisWithProject(split[1]);
							 taskData.setStartTime(startTimeMillis);
							 taskData.setEndTime(endTimeMillis);
						 }
					}
					for (int i = 0; i < useSelectIdList.size(); i++) {
						HeadName headName = useSelectIdList.get(i);
						if (i==useSelectIdList.size()-1) {
							addTask.users+=headName.getUserID();
						}else{
							addTask.users+=headName.getUserID()+",";
						}
						
						 if (taskType==TaskType.Edit){
							 AssignUserInfo assignUserInfo = new AssignUserInfo();
								assignUserInfo.id = Long.parseLong(headName.getUserID());
								assignUserInfo.name = headName.getName();
								assignUserInfo.path = headName.getImage();
							 taskData.extend.assignInfo .add(assignUserInfo);
						 }
					}
					if (taskType==TaskType.Edit) {
						addTask.taskId = taskData.getId()+"";
						TongRenReqUtils.doRequestWebAPI(this, this, addTask, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_UPDATESUBTASK);	
						ToastUtil.showToast(AddTaskActivity.this, "编辑任务成功");
						Intent intent =  new Intent(AddTaskActivity.this, TaskDetailsActivity.class);
						intent.putExtra("AddTaskData", taskData);
						setResult(RESULT_OK, intent);
						finish();
					}else if (taskType==TaskType.Create){
						addTask.taskPid = taskPid+"";
						TongRenReqUtils.doRequestWebAPI(this, this, addTask, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_ADDSUBTASK);
						ToastUtil.showToast(AddTaskActivity.this, "创建任务成功");
						finish();
					}
				}else if(tongRenInfoType == TongRenInfoType.MY) {
					
					
					if (taskType==TaskType.Edit) {
						EditOrganizationTask editOrganizationTask = new EditOrganizationTask();
						editOrganizationTask.organizationId = organizationId;
						editOrganizationTask.organizationTaskId = taskData.getId()+"";
						 if (taskType==TaskType.Edit){
							 taskData.setTitle(taskNameMetv.getText());
						 }
						if (!TextUtils.isEmpty(taskTimeMetv.getText())) {
							String[] split = taskTimeMetv.getText().split("至");
							String startDatetime = split[0].replace("年", "-").replace("月", "-").replace("日", " ");
							String endDatetime = split[1].replace("年", "-").replace("月", "-").replace("日", " ");
							StringBuilder startDatebuilder = new StringBuilder(startDatetime+"00:00");
							StringBuilder endDatebuilder = new StringBuilder(endDatetime+"00:00");
							editOrganizationTask.startTime = startDatebuilder.toString();
							editOrganizationTask.endTime = endDatebuilder.toString();
							 if (taskType==TaskType.Edit){
								 long startTimeMillis = TimeUtil.getTimeMillisWithProject(split[0]);
									long endTimeMillis = TimeUtil.getTimeMillisWithProject(split[1]);
								 taskData.setStartTime(startTimeMillis);
								 taskData.setEndTime(endTimeMillis);
							 }
						}
						for (int i = 0; i < useSelectIdList.size(); i++) {
							HeadName headName = useSelectIdList.get(i);
							if (i==useSelectIdList.size()-1) {
								editOrganizationTask.performerId +=headName.getUserID();
							}else{
								editOrganizationTask.performerId+=headName.getUserID()+",";
							}
							 if (taskType==TaskType.Edit){
								 AssignUserInfo assignUserInfo = new AssignUserInfo();
									assignUserInfo.id = Long.parseLong(headName.getUserID());
									assignUserInfo.name = headName.getName();
									assignUserInfo.path = headName.getImage();
								 taskData.extend.assignInfo .add(assignUserInfo);
							 }
						}
						TongRenReqUtils.doRequestWebAPI(this, this, editOrganizationTask, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_ASSIGNORGANIZATIONTASK);

					}else if (taskType==TaskType.Create){
						AddOrganizationTask addOrganizationTask = new AddOrganizationTask();
						addOrganizationTask.organizationId = organizationId;
						addOrganizationTask.title =  taskNameMetv.getText();
						if (!TextUtils.isEmpty(taskTimeMetv.getText())) {
							String[] split = taskTimeMetv.getText().split("至");
							String startDatetime = split[0].replace("年", "-").replace("月", "-").replace("日", " ");
							String endDatetime = split[1].replace("年", "-").replace("月", "-").replace("日", " ");
							StringBuilder startDatebuilder = new StringBuilder(startDatetime+"00:00");
							StringBuilder endDatebuilder = new StringBuilder(endDatetime+"00:00");
							addOrganizationTask.startTime = startDatebuilder.toString();
							addOrganizationTask.endTime = endDatebuilder.toString();
						}
						for (int i = 0; i < useSelectIdList.size(); i++) {
							HeadName headName = useSelectIdList.get(i);
							if (i==useSelectIdList.size()-1) {
								addOrganizationTask.performerId +=headName.getUserID();
							}else{
								addOrganizationTask.performerId+=headName.getUserID()+",";
							}
							
						}
						TongRenReqUtils.doRequestWebAPI(this, this, addOrganizationTask, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_ORGANIZATIONTASKCREATE);

					}
					
				}
				
				
			}else{
				ToastUtil.showToast(this, "任务信息必填");
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.taskTimeMetv://时间
			Intent Timeintent = new Intent(this, EducationDataActivity.class);
			if (taskData!=null) {
				if (taskData.startTime!=0) {
					Timeintent.putExtra("startTime", taskData.getStartTime());
				}
				if (taskData.endTime!=0) {
					Timeintent.putExtra("endTime", taskData.getEndTime());
				}
			}
			startActivityForResult(Timeintent, 1001);
			break;
		case R.id.taskDesignatedMetv://指派给
			Intent memberIntent = new Intent(this, OrganizationMemberActivity.class);
			memberIntent.putExtra("organizationId",organizationId);
			memberIntent.putExtra("organizationMemberType",OrganizationMemberType.select);
			memberIntent.putExtra("fromType", FromType.project);
			memberIntent.putExtra("useSelectIdList", useSelectIdList);
			startActivityForResult(memberIntent, ORGANIZATIONMEMBER_REQUESTCODE);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data!=null) {
			switch (requestCode) {
			case 1001: //需求时间
				projectTime = data.getStringExtra("Education_Data");
				taskTimeMetv.setText(projectTime);
			break;
			case ORGANIZATIONMEMBER_REQUESTCODE:
				useSelectIdList.clear();
				useSelectIdList = (ArrayList<HeadName>) data.getSerializableExtra("useSelectIdList");
				if (useSelectIdList.isEmpty()) {
					taskDesignatedMetv.setText("");
				}else{
					for (int i = 0; i < useSelectIdList.size(); i++) {
						HeadName useSelectHeadName = useSelectIdList.get(i);
						taskDesignatedMetv.setText(useSelectHeadName.getName());
					}
				}
				
				
			break;
			default:
				break;
			}
		}
	}
	@Override
	public void bindData(int tag, Object object) {
		if (object!=null) {
			switch (tag) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_ADDSUBTASK: //新增子任务
				code = (String) object;
				if (!TextUtils.isEmpty(code)||"000000".equals(code)) {
					ToastUtil.showToast(this, "添加任务成功");
				}
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_ORGANIZATIONTASKCREATE://创建组织任务接口
				
				String id   = (String) object;
				if (!TextUtils.isEmpty(id)) {
					ToastUtil.showToast(AddTaskActivity.this, "创建任务成功");
					finish();
				}
				break;
			case  EAPIConsts.TongRenRequestType.TONGREN_REQ_ASSIGNORGANIZATIONTASK://重新分配组织任务接口 
				String editId   = (String) object;
				if (!TextUtils.isEmpty(editId)) {
					ToastUtil.showToast(AddTaskActivity.this, "编辑任务成功");
					Intent intent =  new Intent(AddTaskActivity.this, TaskDetailsActivity.class);
					intent.putExtra("AddTaskData", taskData);
					setResult(RESULT_OK, intent);
					finish();
				}
				break;
			default:
				break;
			}
		}
		dismissLoadingDialog();
	}
}
