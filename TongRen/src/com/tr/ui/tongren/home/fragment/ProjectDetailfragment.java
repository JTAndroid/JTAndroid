package com.tr.ui.tongren.home.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.tr.App;
import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.tongren.adapter.PeojectAccessoryAdapter;
import com.tr.ui.tongren.adapter.ProjectAdapter.ProjectType;
import com.tr.ui.tongren.home.ApplyUndertakeActivtiy;
import com.tr.ui.tongren.home.OrganizationActivity;
import com.tr.ui.tongren.home.OrganizationMemberActivity;
import com.tr.ui.tongren.home.ProjectAccessoryActivtiy;
import com.tr.ui.tongren.home.ProjectApplyActivity;
import com.tr.ui.tongren.home.ProjectOperationActivtiy;
import com.tr.ui.tongren.home.ProjectTaskActivtiy;
import com.tr.ui.tongren.home.OrganizationMemberActivity.FromType;
import com.tr.ui.tongren.home.OrganizationMemberActivity.OrganizationMemberType;
import com.tr.ui.tongren.model.project.Project;
import com.tr.ui.tongren.model.project.ProjectAndOrganizationId;
import com.tr.ui.tongren.model.project.ProjectId;
import com.tr.ui.tongren.model.project.Resource;
import com.tr.ui.tongren.model.project.Undertaken;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.tr.ui.widgets.NoScrollListview;
import com.tr.ui.widgets.title.menu.popupwindow.ActionItem;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup.OnPopuItemOnClickListener;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.TongRenRequestType.TongRenInfoType;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.TongRenRequestType;
import com.utils.log.ToastUtil;
import com.utils.time.TimeUtil;

public class ProjectDetailfragment extends JBaseFragment implements IBindData, OnClickListener {
	private static final int APPLYUNDERTAKE_REQUESTCODE = 1001;
	private static final int PROJECTOPERATION_REQUESTCODE = 1002;
	private static final int PROJECTACCESSORY_REQUESTCODE = 1003;
	private static final int ORGANIZATIONMEMBER_REQUESTCODE = 1004;
	private static final int PROJECTAPPLY_REQUESTCODE = 1005;
	private static final int PROJECTTASK_REQUESTCODE = 1006;
	private String projectId;
	private boolean mIsVisibleToUser;
	private TextView projectDetailName;
	private TextView projectDetailState;
	private TextView projectDetailTime;
	private TextView projectDetailIntroduction;
	private TextView projectDetailValidity;
	private TextView projectDetailCycle;
	private TextView projectDetailArea;
	private TextView projectDetailIndustry;

	private String organizationId;
	private ProjectType projectType;
	private TextView applyUndertakeTv;
	private LinearLayout undertakerMetv;
	private LinearLayout scheduleMetv;
	private LinearLayout taskMetv;
	private LinearLayout memberMetv;
	private LinearLayout documentMetv;
	private LinearLayout accessoryLl;
	private TextView undertakenName,undertakenNums;
	private LinearLayout organizatianUndertakenLl;
	//@param "status": "-1未开始,0项目进行中、1完成、2、放弃、3已过期",
	private int status;
	private TextView headerVi;
	private TitlePopup titlePopup;
	private String actionItem;
	private NoScrollListview accessoryLv;
	private String recipientName;
	private RelativeLayout undertakenNameLl;
	private String publisherName;
	private TextView projectDetailValidityKey;
	public static long projectValidityLimitTime = 0;
	private long recipientTime;
	public ProjectDetailfragment(String projectId, String organizationId, ProjectType projectType, String recipientName, String publisherName, int status,String publisherId, long recipientTime,String projectAcceptId,int applysum) {
		this.projectId = projectId;
		this.organizationId =  organizationId;
		this.projectType = projectType;
		this.recipientName = recipientName;
		this.status = status;
		this.publisherId = publisherId ;
		this.publisherName = publisherName;
		this.recipientTime = recipientTime ;
		this.projectAcceptId = projectAcceptId;
		this.applysum = applysum;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_projectdetails, null);
		return view;
	}
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		headerVi =  (TextView) view.findViewById(R.id.text_transparent_line);
		applyUndertakeTv= (TextView) view.findViewById(R.id.applyUndertakeTv);
		projectDetailName = (TextView) view.findViewById(R.id.projectDetailName);
		projectDetailState = (TextView) view.findViewById(R.id.projectDetailState);
		projectDetailValidityKey = (TextView) view.findViewById(R.id.projectDetailValidityKey);
		projectDetailTime = (TextView) view.findViewById(R.id.projectDetailTime);
		projectDetailIntroduction = (TextView) view.findViewById(R.id.projectDetailIntroduction);
		projectDetailValidity = (TextView) view.findViewById(R.id.projectDetailValidity);
		projectDetailCycle = (TextView) view.findViewById(R.id.projectDetailCycle);
		projectDetailArea = (TextView) view.findViewById(R.id.projectDetailArea);
		projectDetailIndustry = (TextView) view.findViewById(R.id.projectDetailIndustry);
		accessoryLv = (NoScrollListview) view.findViewById(R.id.accessoryLv);
		undertakenName = (TextView) view.findViewById(R.id.undertakenName);
		undertakenNums = (TextView) view.findViewById(R.id.undertakenNums);
		undertakerMetv = (LinearLayout) view.findViewById(R.id.undertakerMetv);
		scheduleMetv = (LinearLayout) view.findViewById(R.id.scheduleMetv);
		taskMetv = (LinearLayout) view.findViewById(R.id.taskMetv);
		memberMetv = (LinearLayout) view.findViewById(R.id.memberMetv);
		documentMetv = (LinearLayout) view.findViewById(R.id.documentMetv);
		accessoryLl = (LinearLayout)view.findViewById(R.id.accessoryLl);
		organizatianUndertakenLl = (LinearLayout)view.findViewById(R.id.organizatianUndertakenLl);
		undertakenNameLl = (RelativeLayout)view.findViewById(R.id.undertakenNameLl);
		undertakerMetv.setOnClickListener(this);
		scheduleMetv.setOnClickListener(this);
		taskMetv.setOnClickListener(this);
		memberMetv.setOnClickListener(this);
		documentMetv.setOnClickListener(this);
		applyUndertakeTv.setOnClickListener(this);
		initPopWindow();
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (status==0) {
			inflater.inflate(R.menu.activity_more, menu);
			super.onCreateOptionsMenu(menu, inflater);
		}else if(status==-1&&App.getUserID().equals(publisherId)){
			inflater.inflate(R.menu.activity_more, menu);
			super.onCreateOptionsMenu(menu, inflater);
		}
	}

	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(getActivity(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		
	}
	private OnPopuItemOnClickListener onitemClick = new OnPopuItemOnClickListener() {
		@Override
		public void onItemClick(ActionItem item, int position) {
			if(item.mTitle.equals("删除项目")){
				MessageDialog messageDialog = new MessageDialog(getActivity());
				messageDialog.setContent("确定删除吗？");
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					@Override
					public void onFinish(String content) {
						if ("确定".equals(content)) {
							ProjectId parameter = new ProjectId(projectId);
							TongRenReqUtils.doRequestWebAPI(getActivity(), ProjectDetailfragment.this, parameter, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_DELPROJECT);
							ToastUtil.showToast(getActivity(), "删除成功");
							getActivity().finish();
						}}

					@Override
					public void onCancel(String content) {
						// TODO Auto-generated method stub
						
					}
				});
			}else if(item.mTitle.equals("结束项目")){
				MessageDialog messageDialog = new MessageDialog(getActivity());
				messageDialog.setContent("确定结束吗？");
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					@Override
					public void onFinish(String content) {
						if ("确定".equals(content)) {
						ProjectId parameter = new ProjectId(projectId);
						parameter.type =1;
							TongRenReqUtils.doRequestWebAPI(getActivity(), ProjectDetailfragment.this, parameter, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_PROJECTOPERATION);
							ToastUtil.showToast(getActivity(), "结束成功");
							getActivity().finish();
					}}

					@Override
					public void onCancel(String content) {
						// TODO Auto-generated method stub
						
					}
				});
			}else if(item.mTitle.equals("放弃项目")){
				MessageDialog messageDialog = new MessageDialog(getActivity());
				messageDialog.setContent("确定放弃吗？");
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					@Override
					public void onFinish(String content) {
						if ("确定".equals(content)) {
						ProjectId parameter = new ProjectId(projectId);
						parameter.type =2;
							TongRenReqUtils.doRequestWebAPI(getActivity(), ProjectDetailfragment.this, parameter, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_PROJECTOPERATION);
							ToastUtil.showToast(getActivity(), "放弃成功");
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
	private Project project;
	private String publisherId;
	private ColorStateList colorStateList;
	private String projectAcceptId;
	private int applysum;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()== R.id.more) {
			titlePopup.show(headerVi);
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		mIsVisibleToUser = isVisibleToUser;
		if (mIsVisibleToUser) {
			getData();
		}
	}
	private void getData() {
		showLoadingDialog();
		ProjectId projectObj  = new ProjectId(projectId);
			TongRenReqUtils.doRequestWebAPI(getActivity(), this, projectObj, null, EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPROJECTDETAIL);
		
	}
	
	@Override
	public void bindData(int tag, Object object) {
		if(object !=null){
			switch (tag) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPROJECTDETAIL://获取我的项目详情
				project = (Project) object;
				refreshView(project);
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_DELPROJECT://删除项目
				boolean status = (Boolean) object;
				if (status) {
					
				}
				break;
			default:
				break;
			}
		}
		dismissLoadingDialog();
	}
	private void refreshView(Project project) {
		titlePopup.cleanAction();
		if ("0".equals(organizationId)) {
			organizatianUndertakenLl.setVisibility(View.GONE);
		}else{
			organizatianUndertakenLl.setVisibility(View.VISIBLE);
		}
		
		if (-1==status) {
			projectDetailState.setText("未承接");
			colorStateList = getActivity().getResources().getColorStateList(R.color.projecttextgraybg);
			projectDetailValidity.setText(project.getValidityStartTime()+"至"+project.getValidityEndTime());
			projectValidityLimitTime = project.validityEndTime;
			projectDetailValidityKey.setText("需求有效期：");
			if (App.getUserID().equals(project.getCreaterId()+"")) {
				undertakerMetv.setVisibility(View.VISIBLE);
				applyUndertakeTv.setVisibility(View.GONE);
				if (applysum == 0) {
					undertakenNums.setText("");
				}else{
					undertakenNums.setText(applysum+ "条承接申请");
					colorStateList = getActivity().getResources().getColorStateList(R.color.projecttextgraybg);
					undertakenName.setTextColor(colorStateList);
				}
				actionItem = "删除项目";
				// 给标题栏弹窗添加子类
				titlePopup.addAction(new ActionItem(getActivity(), actionItem));
			}else{
				undertakerMetv.setVisibility(View.GONE);
				applyUndertakeTv.setVisibility(View.VISIBLE);
			}
		}else if(0==status) {
			colorStateList = getActivity().getResources().getColorStateList(R.color.projecttextgreenbg);
			projectDetailState.setText("进行中");
			
			long cycleTime = (long)project.getCycle()*3600*1000*24;
			projectValidityLimitTime = recipientTime+cycleTime;
			projectDetailValidity.setText(TimeUtil.TimeMillsToString(recipientTime)+"至"+TimeUtil.TimeMillsToString(recipientTime+cycleTime));
			projectDetailValidityKey.setText("项目时间：");
			if (TextUtils.isEmpty(recipientName)) {
				undertakenNameLl . setVisibility(View.GONE);
			}else{
				undertakenNameLl . setVisibility(View.VISIBLE);
			}
			if (App.getUserID().equals(project.getCreaterId()+"")) {
				actionItem = "结束项目";
				// 给标题栏弹窗添加子类
				titlePopup.addAction(new ActionItem(getActivity(), actionItem));
			}else{
				actionItem = "放弃项目";
				// 给标题栏弹窗添加子类
				titlePopup.addAction(new ActionItem(getActivity(), actionItem));
			}
			applyUndertakeTv.setVisibility(View.GONE);
		}else if(1==status) {
			colorStateList = getActivity().getResources().getColorStateList(R.color.projecttextorangebg);
			projectDetailState.setText("已完成");
			applyUndertakeTv.setVisibility(View.GONE);
			projectValidityLimitTime = project.validityEndTime;
			projectDetailValidity.setText(project.getValidityStartTime()+"至"+project.getValidityEndTime());
			projectDetailValidityKey.setText("需求有效期:");
		}else if(3==status) {
			colorStateList = getActivity().getResources().getColorStateList(R.color.projecttextgraybg);
			projectDetailState.setText("已过期");
			applyUndertakeTv.setVisibility(View.GONE);
			projectValidityLimitTime = project.validityEndTime;
			projectDetailValidity.setText(project.getValidityStartTime()+"至"+project.getValidityEndTime());
			projectDetailValidityKey.setText("需求有效期:");
		}else if(2==status) {
			colorStateList = getActivity().getResources().getColorStateList(R.color.projecttextredbg);
			projectDetailState.setText("已放弃");
			applyUndertakeTv.setVisibility(View.GONE);
			projectValidityLimitTime = project.validityEndTime;
			projectDetailValidity.setText(project.getValidityStartTime()+"至"+project.getValidityEndTime());
			projectDetailValidityKey.setText("需求有效期:");
		}else if(4==status){
			applyUndertakeTv.setText("我已申请");
			applyUndertakeTv.setEnabled(false);
			applyUndertakeTv.setVisibility(View.VISIBLE);
			projectValidityLimitTime = project.validityEndTime;
			projectDetailValidity.setText(project.getValidityStartTime()+"至"+project.getValidityEndTime());
			projectDetailValidityKey.setText("需求有效期:");
			projectDetailState.setText("未承接");
			colorStateList = getActivity().getResources().getColorStateList(R.color.projecttextgraybg);
		}else{
			projectDetailState.setText("");
		}
		projectDetailState.setTextColor(colorStateList);
		projectDetailName.setText(project.getName());
		projectDetailTime.setText(publisherName+"创建于"+project.getValidityStartTime());
		projectDetailCycle.setText("自项目被承接起"+project.getCycle()+"天");
		projectDetailArea.setText(project.getArea());
		projectDetailIndustry.setText(project.getIndustry());
		if (!TextUtils.isEmpty(recipientName)) {
			undertakenName.setText(recipientName);
		}else{
				undertakenName.setText("");
		}
		
//		if (project.resourceAttachments!=null&&!project.resourceAttachments.isEmpty()) {
			PeojectAccessoryAdapter accessoryAdapter = new PeojectAccessoryAdapter(getActivity());
			accessoryAdapter.setListResource(project.resourceAttachments);
			accessoryLv.setAdapter(accessoryAdapter);
			accessoryAdapter.notifyDataSetChanged();
//			accessoryLl.setVisibility(View.VISIBLE);
//		}else{
//			accessoryLl.setVisibility(View.GONE);
//		}
		projectDetailIntroduction.setText(project.getIntroduction());
		
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.applyUndertakeTv://我要承接
			Intent intent = new Intent(getActivity(), ApplyUndertakeActivtiy.class);
			intent.putExtra("projectName", projectDetailName.getText().toString());
			intent.putExtra("projectId", projectId);
			intent.putExtra("publisherId", publisherId);
			getActivity().startActivityForResult(intent, APPLYUNDERTAKE_REQUESTCODE);
			break;
		case R.id.undertakerMetv://承接方
			Intent ProjectApplyintent = new Intent(getActivity(), ProjectApplyActivity.class);
			ProjectApplyintent.putExtra("projectId", projectId);
			getActivity().startActivityForResult(ProjectApplyintent, PROJECTAPPLY_REQUESTCODE);
			break;
		case R.id.scheduleMetv://进度
			Intent ProjectOperationintent = new Intent(getActivity(), ProjectOperationActivtiy.class);
			ProjectOperationintent.putExtra("projectId", projectId);
			getActivity().startActivityForResult(ProjectOperationintent, PROJECTOPERATION_REQUESTCODE);
			
			break;
		case R.id.taskMetv://任务
			Intent taskIntent = new Intent(getActivity(), ProjectTaskActivtiy.class);
			taskIntent.putExtra("organizationId",organizationId);
			taskIntent.putExtra("projectId", projectId);
			taskIntent.putExtra("projectName", "项目-"+projectDetailName.getText().toString());
			taskIntent.putExtra("projectAcceptId", projectAcceptId);
			taskIntent.putExtra("projectstatus", status);
			taskIntent.putExtra(EAPIConsts.TongRenRequestType.TongRenInfoType, TongRenInfoType.PROJECT);
			getActivity().startActivityForResult(taskIntent,PROJECTTASK_REQUESTCODE);
			break;
		case R.id.memberMetv://成员
			Intent memberIntent = new Intent(getActivity(), OrganizationMemberActivity.class);
			memberIntent.putExtra("organizationId",organizationId);
			memberIntent.putExtra("organizationMemberType",OrganizationMemberType.show);
			memberIntent.putExtra("fromType", FromType.project);
			getActivity().startActivityForResult(memberIntent, ORGANIZATIONMEMBER_REQUESTCODE);
			break;
		case R.id.documentMetv://文档
			Intent AccessoryIntent = new Intent(getActivity(), ProjectAccessoryActivtiy.class);
			AccessoryIntent.putExtra("organizationId",organizationId);
			AccessoryIntent.putExtra("projectId", projectId);
			AccessoryIntent.putExtra(EAPIConsts.TongRenRequestType.TongRenInfoType, TongRenInfoType.PROJECT);
			getActivity().startActivityForResult(AccessoryIntent, PROJECTACCESSORY_REQUESTCODE);
			break;
		default:
			break;
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data!=null) {
			switch (requestCode) {
			case APPLYUNDERTAKE_REQUESTCODE:
				boolean ApplyStatus = data.getBooleanExtra("ApplyStatus", false);
				if (ApplyStatus) {
					applyUndertakeTv.setText("我已申请");
					applyUndertakeTv.setEnabled(false);
				}
				break;
			case PROJECTAPPLY_REQUESTCODE:
				boolean isUndertake = data.getBooleanExtra("isUndertake", false);
				if (isUndertake) {
					organizationId = data.getStringExtra("organizationId");
					recipientName = data.getStringExtra("recipientName");
					titlePopup.cleanAction();
					if (TextUtils.isEmpty(organizationId)) {
						organizatianUndertakenLl.setVisibility(View.GONE);
					}else{
						organizatianUndertakenLl.setVisibility(View.VISIBLE);
					}
					if (TextUtils.isEmpty(recipientName)) {
						undertakenNameLl . setVisibility(View.GONE);
					}else{
						undertakenNameLl . setVisibility(View.VISIBLE);
					}
					undertakerMetv.setVisibility(View.GONE);
					
						colorStateList = getActivity().getResources().getColorStateList(R.color.projecttextgreenbg);
						projectDetailState.setText("进行中");
						long cycleTime = project.getCycle()*3600*1000*24;
						projectDetailValidity.setText(TimeUtil.TimeMillsToString(recipientTime)+"至"+TimeUtil.TimeMillsToString(recipientTime+cycleTime));
						projectDetailValidityKey.setText("项目时间:");
						
						if (App.getUserID().equals(project.getCreaterId()+"")) {
							actionItem = "结束项目";
							// 给标题栏弹窗添加子类
							titlePopup.addAction(new ActionItem(getActivity(), actionItem));
						}else{
							actionItem = "放弃项目";
							// 给标题栏弹窗添加子类
							titlePopup.addAction(new ActionItem(getActivity(), actionItem));
						}
						applyUndertakeTv.setVisibility(View.GONE);
					projectDetailState.setTextColor(colorStateList);
					if (!TextUtils.isEmpty(recipientName)) {
						undertakenName.setText(recipientName);
					}else{
						undertakenName.setText("");
					}
					
				}
				break;

			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
