package com.tr.ui.tongren.home.fragment;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.tr.App;
import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.tongren.home.AddTaskActivity;
import com.tr.ui.tongren.home.AddTaskActivity.TaskType;
import com.tr.ui.tongren.home.ProjectTaskActivtiy;
import com.tr.ui.tongren.model.task.AssignUserInfo;
import com.tr.ui.tongren.model.task.Task;
import com.tr.ui.tongren.model.task.TaskDesignatedParameter;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.tr.ui.widgets.title.menu.popupwindow.ActionItem;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup.OnPopuItemOnClickListener;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.TongRenRequestType.TongRenInfoType;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

public class TaskDetailfragment extends JBaseFragment implements IBindData {
	public final static int EDITTASKINTENT_REQUESTCODE = 1001;
	private Task task;
	private TextView taskNameTv;
	private TextView taskTimeTv;
	private TextView beginTimeTv;
	private TextView endTimeTv;
	private TextView designatedTv;
	private TextView fromTv;
	private Button completeBt;
	private String projectName;
	private ImageView taskStateIv;
	private TextView headerVi;
	private TitlePopup titlePopup;
	private TongRenInfoType tongRenInfoType;
	private TextView taskRejectReasonTv;
	private LinearLayout taskRejectReasonLl;

	public TaskDetailfragment (Task task, String projectName, TongRenInfoType tongRenInfoType){
		this.task = task;
		this.projectName = projectName;
		this.tongRenInfoType = tongRenInfoType;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View inflate = View.inflate(getActivity(), R.layout.fragment_taskdetail, null);
		return inflate;
	}
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		headerVi =  (TextView) view.findViewById(R.id.text_transparent_line);
		taskRejectReasonTv=(TextView) view.findViewById(R.id.taskRejectReasonTv);
		taskRejectReasonLl = (LinearLayout) view.findViewById(R.id.taskRejectReasonLl);
		taskNameTv = (TextView) view.findViewById(R.id.TaskNameTv);
		taskTimeTv = (TextView) view.findViewById(R.id.TaskTimeTv);
		beginTimeTv = (TextView) view.findViewById(R.id.beginTimeTv);
		endTimeTv = (TextView) view.findViewById(R.id.endTimeTv);
		designatedTv = (TextView) view.findViewById(R.id.designatedTv);
		fromTv = (TextView) view.findViewById(R.id.fromTv);
		completeBt = (Button) view.findViewById(R.id.completeBt);
		taskStateIv = (ImageView) view.findViewById(R.id.taskStateIv);
		initPopWindow();
		initData();
	}
	private void initData() {
		showLoadingDialog();
		if (task!=null) {
			if (task.getTaskStatus()==6) {
				taskRejectReasonLl.setVisibility(View.VISIBLE);
				taskRejectReasonTv.setText(task.rejectReason);
			}
			taskNameTv.setText(task.getTitle());
			taskTimeTv.setText("创建于"+task.getCreateTime());
			beginTimeTv.setText(task.getStartTime());
			endTimeTv.setText(task.getEndTime());
			if (!TextUtils.isEmpty(projectName)) {
				fromTv.setText(projectName);
			}
			if (task.getTaskStatus()==0) {//准备中
				taskStateIv.setImageResource(R.drawable.task_notstarted);
				if (App.getUserID().equals(task.getCreateId()+"")) {
					titlePopup.cleanAction();
					if (tongRenInfoType == TongRenInfoType.PROJECT) {
						titlePopup.addAction(new ActionItem(getActivity(), "编辑任务"));
						titlePopup.addAction(new ActionItem(getActivity(), "删除任务"));
					}
					
				}
			}else if (task.getTaskStatus()==1) {//已开始
				taskStateIv.setImageResource(R.drawable.task_underway);
				
				if (App.getUserID().equals(task.getCreateId()+"")) {
					completeBt.setVisibility(View.VISIBLE);
					
					completeBt.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							TaskDesignatedParameter parameter = new TaskDesignatedParameter();
							parameter.taskId = task.getId()+"";
							parameter.organizationId =  task.getOrganizationId()+"";
							TongRenReqUtils.doRequestWebAPI(getActivity(), TaskDetailfragment.this, parameter, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_COMPLETETASK);
							completeBt.setVisibility(View.GONE);
							taskStateIv.setImageResource(R.drawable.task_complete);
						}
					});
				}else{
					completeBt.setVisibility(View.GONE);
				}
				
			}else if (task.getTaskStatus()==2) {//已完成
				taskStateIv.setImageResource(R.drawable.task_complete);
			}
			
		}
		if (task.extend!=null&&task.extend.assignInfo!=null&&!task.extend.assignInfo.isEmpty()) {
			new AsyncTask<Void, Void, String>() {

				@Override
				protected String doInBackground(Void... params) {
					StringBuffer stringBuffer = new StringBuffer();
					for (int i = 0; i < task.extend.assignInfo.size(); i++) {
						AssignUserInfo userinfo = task.extend.assignInfo.get(i);
						if (i == task.extend.assignInfo.size()) {
							stringBuffer.append(userinfo.name);
						}else{
							stringBuffer.append(userinfo.name+" ");
						}
					}
					return stringBuffer.toString();
				}
				protected void onPostExecute(String result) {
					designatedTv.setText(result);
				};
			}.execute();
		}
//		getDesignatedData();
		dismissLoadingDialog();
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_more, menu);
		MenuItem more = menu.findItem(R.id.more);
		if (task!=null&&task.getTaskStatus()==0&&App.getUserID().equals(task.getCreateId()+"")&&tongRenInfoType == TongRenInfoType.PROJECT) {
			more.setVisible(true);
		}else{
			more.setVisible(false);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()== R.id.more) {
			titlePopup.show(headerVi);
		}
		return super.onOptionsItemSelected(item);
	}
	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(getActivity(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		
	}
	private OnPopuItemOnClickListener onitemClick = new OnPopuItemOnClickListener() {
		@Override
		public void onItemClick(ActionItem item, int position) {
			if(item.mTitle.equals("编辑任务")){
				Intent EditTaskintent  = new Intent(getActivity(), AddTaskActivity.class);
				EditTaskintent.putExtra("organizationId",task.getOrganizationId()+"");
				EditTaskintent.putExtra("taskPid", task.getTaskPid()+"");
				EditTaskintent.putExtra("taskData", task);
				EditTaskintent.putExtra(EAPIConsts.TongRenRequestType.TongRenInfoType, tongRenInfoType);
				EditTaskintent.putExtra("taskType", TaskType.Edit);
				getActivity().startActivityForResult(EditTaskintent, EDITTASKINTENT_REQUESTCODE);
			}else if(item.mTitle.equals("删除任务")){
				MessageDialog messageDialog = new MessageDialog(getActivity());
				messageDialog.setContent("确定删除吗？");
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					@Override
					public void onFinish(String content) {
						if ("确定".equals(content)) {
							
						TaskDesignatedParameter parameter = new TaskDesignatedParameter();
						parameter.taskId = task.getId()+"";
						if (tongRenInfoType == TongRenInfoType.PROJECT) {
							parameter.organizationId =  task.getOrganizationId()+"";
							TongRenReqUtils.doRequestWebAPI(getActivity(), TaskDetailfragment.this, parameter, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_REMOVETASK);
						}else if (tongRenInfoType == TongRenInfoType.ORGANIZATION) {
							TongRenReqUtils.doRequestWebAPI(getActivity(), TaskDetailfragment.this, parameter, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_ORGANIZATIONTASKDELETE);
						}
							
							ToastUtil.showToast(getActivity(), "删除成功");
							getActivity().finish();
					}}

					@Override
					public void onCancel(String content) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}
		
	};
	private void getDesignatedData() {
		TaskDesignatedParameter parameter = new TaskDesignatedParameter();
		parameter.taskId = task.getId()+"";
		parameter.organizationId =  task.getOrganizationId()+"";
		TongRenReqUtils.doRequestWebAPI(getActivity(), this, parameter, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_SELECTASSIGNTASKBYTASKID);
	}
	@Override
	public void bindData(int tag, Object object) {
		if (object!=null) {
			switch (tag) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_SELECTASSIGNTASKBYTASKID://查看任务分配人
				List<AssignUserInfo> listAssignUserInfo = (List<AssignUserInfo>) object;
				StringBuffer buffer = new StringBuffer();
				for (AssignUserInfo assignUserInfo : listAssignUserInfo) {
					buffer.append(assignUserInfo.name+" ");
				}
				if (!TextUtils.isEmpty(buffer.toString())) {
					designatedTv.setText(buffer.toString());
				}
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_COMPLETETASK://完成任务(不返回数据)
				break;
			default:
				break;
			}
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data!=null) {
		switch (requestCode) {
		case EDITTASKINTENT_REQUESTCODE:
			task= (Task) data.getSerializableExtra("AddTaskData");
			if (task!=null) {
				initData();
			}
			break;

		default:
			break;
		}
		}
	}
}
