package com.tr.ui.tongren;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.tr.App;
import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.home.MainActivity;
import com.tr.ui.tongren.adapter.ProjectAdapter;
import com.tr.ui.tongren.adapter.ProjectAdapter.ProjectType;
import com.tr.ui.tongren.home.CreateProjectActivity;
import com.tr.ui.tongren.home.RecommendProjectActivity;
import com.tr.ui.tongren.model.project.Apply;
import com.tr.ui.tongren.model.project.Project;
import com.tr.ui.tongren.model.project.ProjectStatus;
import com.tr.ui.tongren.model.project.Publish;
import com.tr.ui.tongren.model.project.Status;
import com.tr.ui.tongren.model.project.Undertaken;
import com.tr.ui.tongren.model.project.UserMessageType;
import com.tr.ui.widgets.NoScrollListview;
import com.tr.ui.widgets.RefreshScrollView;
import com.tr.ui.widgets.RefreshScrollView.OnRefreshScrollViewListener;
import com.tr.ui.widgets.title.menu.popupwindow.ActionItem;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup.OnPopuItemOnClickListener;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.TongRenRequestType;
import com.utils.http.IBindData;

public class ProjectFragment extends JBaseFragment implements OnClickListener, IBindData{
	private View headView;
	private TitlePopup titlePopup;
	private LinearLayout findProjectLl;
	private NoScrollListview myCreateProject;
	private NoScrollListview myAcceptProject;
	private TextView showMyAcceptButExpireProject;
	private TextView showMyCreateButExpireProject;
	private List<Publish> myCreatePublishList = new ArrayList<Publish>();
	private List<Publish> myCreateOverduePublishList = new ArrayList<Publish>();
	private List<Undertaken> myAcceptUndertakenList = new ArrayList<Undertaken>();
	private List<Undertaken> myAcceptOverdueUndertakenList = new ArrayList<Undertaken>();
	
	private LinearLayout myAcceptProjectLl;
	private LinearLayout myCreateProjectLl;
	private LinearLayout rootLl;
	private LinearLayout myAcceptProjectTabLl;
	private LinearLayout myCreateProjectTabLl;
	private ImageView myAcceptProjectIv;
	private ImageView myCreateProjectIv;
	private ImageView common_image_empty;
	private TextView common_text_empty;
	public ProjectFragment() {
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View project_view = inflater.inflate(R.layout.fragment_project, null);
		RefreshScrollView refreshScrollView = new RefreshScrollView(getActivity());
		refreshScrollView.setupContainer(getActivity(), project_view);
		refreshScrollView.setEnableRefresh(true);
		
		refreshScrollView.setOnRefreshScrollViewListener(new OnRefreshScrollViewListener() {
			
			@Override
			public void onRefresh() {
				getData();
			}
		});
		return refreshScrollView;
	}
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initPopWindow();
	}
	private void initView(View view) {
		headView = view.findViewById(R.id.text_transparent_line);
		rootLl =  (LinearLayout)view.findViewById(R.id.rootLl);
		findProjectLl = (LinearLayout)view.findViewById(R.id.findProjectLl);
		myCreateProject = (NoScrollListview)view.findViewById(R.id.myCreateProject);
		myAcceptProject = (NoScrollListview)view.findViewById(R.id.myAcceptProject);
		myAcceptProjectTv = (TextView) view.findViewById(R.id.myAcceptProjectTv);
		myCreateProjectTv = (TextView) view.findViewById(R.id.myCreateProjectTv);
		myAcceptProjectIv = (ImageView) view.findViewById(R.id.myAcceptProjectIv);
		myCreateProjectIv = (ImageView) view.findViewById(R.id.myCreateProjectIv);
	    //数据为空时控件
		empty_layout = (RelativeLayout) view.findViewById(R.id.project_empty);
		common_text_empty = (TextView) empty_layout.findViewById(R.id.common_text_empty);
		common_image_empty = (ImageView) empty_layout.findViewById(R.id.common_image_empty);
		myCreateProjectAdapter = new ProjectAdapter(getActivity());
		myCreateProjectAdapter.setProjectType(ProjectType.CREATE);
		myAcceptProjectAdapter = new ProjectAdapter(getActivity());
		myAcceptProjectAdapter.setProjectType(ProjectType.ACCEPT);
		myAcceptProject.setAdapter(myAcceptProjectAdapter);
		myCreateProject.setAdapter(myCreateProjectAdapter);
		showMyAcceptButExpireProject =  (TextView)view.findViewById(R.id.showMyAcceptButExpireProject);
		showMyCreateButExpireProject =  (TextView)view.findViewById(R.id.showMyCreateButExpireProject);
		myAcceptProjectLl = (LinearLayout)view.findViewById(R.id.myAcceptProjectLl);
		myCreateProjectLl = (LinearLayout)view.findViewById(R.id.myCreateProjectLl);
		myAcceptProjectTabLl = (LinearLayout)view.findViewById(R.id.myAcceptProjectTabLl);
		myCreateProjectTabLl = (LinearLayout)view.findViewById(R.id.myCreateProjectTabLl);
		findProjectLl.setOnClickListener(this);
		myAcceptProjectTabLl.setOnClickListener(this);
		myCreateProjectTabLl.setOnClickListener(this);
		showMyAcceptButExpireProject.setOnClickListener(this);
		showMyCreateButExpireProject.setOnClickListener(this);
	
		myCreateProject.setOnItemClickListener(new OnItemClickListener() {

			private String recipientName;
			long organizationId = 0 ;
			private String publisherName = App.getNick();
			private long recipientTime;
			private String projectAcceptId;
			private int applySum;
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					int status = -1 ; 
					organizationId=  0;
					recipientName = "";
					Publish createPublish  = (Publish) myCreateProjectAdapter.getItem(arg2);
					if (createPublish!=null) {
						if (createPublish.getStatus()==1) {
							status = -1;
						}else if (createPublish.getStatus()==8) {
							status = 0;
						}else if (createPublish.getStatus()==4){
							status = 1;
						}else if (createPublish.getStatus()==3||createPublish.getStatus()==7){
							status = 3;
						}
						Set<Apply> applySet = createPublish.getApplySet();
						applySum = 0;
						for (Apply apply : applySet) {
							if (apply.getStatus()==2) {
								organizationId = apply.getOrganizationId();
								if (organizationId==0) {
									recipientName = apply.getUser().name;
								}else{
									recipientName = apply.getOrganizationMember().getOrganization().getName();
								}
								recipientTime = apply.getReviewTime();
								projectAcceptId =   apply.proposerId+"";
							}
							if (apply.getStatus()==1) {
								applySum++;
							}
						}
						ENavigate.startProjectDetailsActivityForResult(getActivity(), createPublish.getProjectId()+"",organizationId+"",recipientName,publisherName,ProjectType.CREATE,status,  createPublish.getPublisherId()+"",recipientTime,projectAcceptId,applySum,TongRenFragment.REQ_PROJECT);

					}
			}
		});
		myAcceptProject.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String recipientName;
				String publisherName;
					Undertaken acceptUndertaken  = (Undertaken) myAcceptProjectAdapter.getItem(arg2);
					if (acceptUndertaken!=null) {
						if (acceptUndertaken.organizationId==0) {
							recipientName = acceptUndertaken.recipientName;
						}else{
							recipientName = acceptUndertaken.organizationName;
						}
						publisherName = acceptUndertaken.createProjectName;
						ENavigate.startProjectDetailsActivityForResult(getActivity(), acceptUndertaken.getProjectId()+"",acceptUndertaken.organizationId+"",recipientName,publisherName,ProjectType.ACCEPT,acceptUndertaken.getStatus(),acceptUndertaken.getPublishId()+"",acceptUndertaken.startTime,acceptUndertaken.recipientId +"", 0,TongRenFragment.REQ_PROJECT);
					}
			}
		});
	}
	/**是否显示用户*/
	private boolean mIsVisibleToUser;
	private boolean isFirstStart = true;
	private int applyStatus = 1;
	/**
	 * 实现懒加载
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		mIsVisibleToUser = isVisibleToUser;
		if (mIsVisibleToUser /*&& !isFirstStart*/) {
			getData();
		}
	}
	private void getData() {
		ProjectStatus projectStatus = new ProjectStatus();
		TongRenReqUtils.doRequestWebAPI(getActivity(), this, projectStatus, new Handler(), EAPIConsts.TongRenRequestType.TONGREN_REQ_GETALLPUBLISHVALIDITY);
		Status status = new Status();
		status.status = "-1";
		TongRenReqUtils.doRequestWebAPI(getActivity(), this, status, new Handler(), EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYCREATEALL_UNDERTAKEN);
	}
	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(getActivity(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(getActivity(), R.string.create_project, R.drawable.chat_launch));
	}
	private OnPopuItemOnClickListener onitemClick = new OnPopuItemOnClickListener() {
		
		@Override
		public void onItemClick(ActionItem item, int position) {
			switch (position) {
			case 0:// 创建项目
				Intent intent =  new Intent(getActivity(), CreateProjectActivity.class);
				startActivityForResult(intent, TongRenFragment.REQ_PROJECT);
				break;
			default:
				break;
			}
		}
	};
	private ProjectAdapter myCreateProjectAdapter;
	private ProjectAdapter myAcceptProjectAdapter;
	private int AcceptExpireSum;
	private int CreateExpireSum;
	private TextView myAcceptProjectTv;
	private TextView myCreateProjectTv;
	private boolean isMyAcceptHideOverdue = true;
	private boolean isMyCreateHideOverdue = true;
	private boolean isMyCreateFold;
	private boolean isMyAcceptFold;
	private RelativeLayout empty_layout;
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.findProjectLl://发现项目
			Intent intent = new Intent(getActivity(), RecommendProjectActivity.class);
			startActivityForResult(intent, TongRenFragment.REQ_PROJECT);
			break;
		case R.id.showMyAcceptButExpireProject:
			isMyAcceptHideOverdue = false;
			if (!myAcceptOverdueUndertakenList.isEmpty()) {
				myAcceptUndertakenList.addAll(myAcceptOverdueUndertakenList);
			}
			myAcceptProjectAdapter.setListUndertaken(myAcceptUndertakenList);
			showMyAcceptButExpireProject.setVisibility(View.GONE);
			myAcceptProjectAdapter.notifyDataSetChanged();
			break;
		case R.id.showMyCreateButExpireProject:
			isMyCreateHideOverdue = false;
			if (!myCreateOverduePublishList.isEmpty()) {
				myCreatePublishList.addAll(myCreateOverduePublishList);
			}
			myCreateProjectAdapter.setListPublish(myCreatePublishList);
			showMyCreateButExpireProject.setVisibility(View.GONE);
			myCreateProjectAdapter.notifyDataSetChanged();
			break;
		case R.id.myCreateProjectTabLl:
//			RotateAnimation animation;
//			myCreateProjectIv.clearAnimation();
//			if (isMyCreateFold) {
//				animation = new RotateAnimation(-90f, 0f,
//						Animation.RELATIVE_TO_SELF, 0.5f,
//						Animation.RELATIVE_TO_SELF, 0.5f);
//				myCreateProject.setVisibility(View.VISIBLE);
//				if (isMyCreateHideOverdue) {
//					showMyCreateButExpireProject.setVisibility(View.VISIBLE);
//				}else{
//					showMyCreateButExpireProject.setVisibility(View.GONE);
//				}
//				
//			}else{
//				animation = new RotateAnimation(0f, -90f,
//						Animation.RELATIVE_TO_SELF, 0.5f,
//						Animation.RELATIVE_TO_SELF, 0.5f);
//				myCreateProject.setVisibility(View.GONE);
//				showMyCreateButExpireProject.setVisibility(View.GONE);
//			}
//			animation.setDuration(500);// 设置动画持续时间
//			animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
//			myCreateProjectIv.setAnimation(animation);
//			animation.startNow();
//			isMyCreateFold =!isMyCreateFold;
			break;
		case R.id.myAcceptProjectTabLl:
//			RotateAnimation Acceptanimation;
//			myAcceptProjectIv.clearAnimation();
//			
//			if (isMyAcceptFold) {
//				Acceptanimation = new RotateAnimation(-90f, 0f,
//						Animation.RELATIVE_TO_SELF, 0.5f,
//						Animation.RELATIVE_TO_SELF, 0.5f);
//				myAcceptProject.setVisibility(View.VISIBLE);
//				if (isMyAcceptHideOverdue) {
//					showMyAcceptButExpireProject.setVisibility(View.VISIBLE);
//				}else{
//					showMyAcceptButExpireProject.setVisibility(View.GONE);
//				}
//			}else{
//				Acceptanimation = new RotateAnimation(0f, -90f,
//						Animation.RELATIVE_TO_SELF, 0.5f,
//						Animation.RELATIVE_TO_SELF, 0.5f);
//				myAcceptProject.setVisibility(View.GONE);
//				showMyAcceptButExpireProject.setVisibility(View.GONE);
//			}
//			Acceptanimation.setDuration(500);// 设置动画持续时间
//			Acceptanimation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
//			myAcceptProjectIv.setAnimation(Acceptanimation);
//			Acceptanimation.startNow();
//			isMyAcceptFold =!isMyAcceptFold;
			break;
		default:
			break;
		}
	}
	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (object!=null) {
			
			switch (tag) {
			case TongRenRequestType.TONGREN_REQ_GETALLPUBLISHVALIDITY:
				rootLl.setBackgroundResource(R.color.project_bg);
				final List<Publish> listPublish = (List<Publish>) object;
				CreateExpireSum=  0;
				myCreatePublishList.clear();
				myCreateOverduePublishList.clear();
				
				new AsyncTask<Void, Void, Integer>() {
					@Override
					protected Integer doInBackground(Void... params) {
						if (!listPublish.isEmpty()) {
							for (int i = 0; i < listPublish.size(); i++) {
								Publish publish = listPublish.get(i);
									if (publish.getStatus() == 1||publish.getStatus() ==6||publish.getStatus() == 8) {
										myCreatePublishList.add(publish);
									}
									if (publish.getStatus() == 3) {
										myCreateOverduePublishList.add(publish);
										CreateExpireSum++;
									}
							}
						}
						return CreateExpireSum;
					}
					protected void onPostExecute(Integer result) {
						if (isMyCreateHideOverdue) {
							if (result==0) {
								showMyCreateButExpireProject.setVisibility(View.GONE);
							}else{
								showMyCreateButExpireProject.setVisibility(View.VISIBLE);
								showMyCreateButExpireProject.setText("已隐藏过期项目"+"("+result+")");
							}
							myCreateProjectAdapter.setListPublish(myCreatePublishList);
							myCreateProjectAdapter.notifyDataSetChanged();
						}else{
							showMyCreateButExpireProject.setVisibility(View.GONE);
							myCreatePublishList.addAll(myCreateOverduePublishList);
							myCreateProjectAdapter.setListPublish(myCreatePublishList);
							myCreateProjectAdapter.notifyDataSetChanged();
						}
						if (listPublish.isEmpty()) {
							myCreateProjectLl.setVisibility(View.GONE);
						}else{
							myCreateProjectLl.setVisibility(View.VISIBLE);
						}
						if (myAcceptUndertakenList.isEmpty()&&myCreateOverduePublishList.isEmpty()) {
//			rootLl.setBackgroundResource(R.drawable.project_empty);
							empty_layout.setVisibility(View.VISIBLE);
							common_image_empty.setImageResource(R.drawable.project_empty);
							common_text_empty.setText(R.string.common_text_empty);
						}else{
							empty_layout.setVisibility(View.GONE);
//			rootLl.setBackgroundResource(R.color.project_bg);
						}

					};
				}.execute();
				
				
				break;
			case TongRenRequestType.TONGREN_REQ_GETMYCREATEALL_UNDERTAKEN:
				rootLl.setBackgroundResource(R.color.project_bg);
				myAcceptOverdueUndertakenList.clear();
				myAcceptUndertakenList.clear();
				AcceptExpireSum =0;
				final List<Undertaken>  listUndertaken = (List<Undertaken>) object;
				new AsyncTask<Void, Void, Integer>() {
					@Override
					protected Integer doInBackground(Void... params) {
						
						for (int i = 0; i < listUndertaken.size(); i++) {
							Undertaken undertaken = listUndertaken.get(i);
							if (undertaken.getStatus()==3) {
								AcceptExpireSum++;
								myAcceptOverdueUndertakenList.add(undertaken);
							}else if (undertaken.getStatus()==0) {
								myAcceptUndertakenList.add(undertaken);
							}
						}
						return AcceptExpireSum;
					}
					protected void onPostExecute(Integer result) {
						if (isMyAcceptHideOverdue) {
						if (result==0) {
							showMyAcceptButExpireProject.setVisibility(View.GONE);
						}else{
							showMyAcceptButExpireProject.setVisibility(View.VISIBLE);
							showMyAcceptButExpireProject.setText("已隐藏过期项目"+"("+result+")");
						}
						myAcceptProjectAdapter.setListUndertaken(myAcceptUndertakenList);
						myAcceptProjectAdapter.notifyDataSetChanged();
						}else{
							showMyAcceptButExpireProject.setVisibility(View.GONE);
							myAcceptUndertakenList.addAll(myAcceptOverdueUndertakenList);
							myAcceptProjectAdapter.setListUndertaken(myAcceptUndertakenList);
							myAcceptProjectAdapter.notifyDataSetChanged();
						}
						if (!listUndertaken.isEmpty()) {
							myAcceptProjectLl.setVisibility(View.VISIBLE);
						}else{
							myAcceptProjectLl.setVisibility(View.GONE);
						}
						if (myAcceptUndertakenList.isEmpty()&&myCreateOverduePublishList.isEmpty()) {
//			rootLl.setBackgroundResource(R.drawable.project_empty);
							empty_layout.setVisibility(View.VISIBLE);
							common_image_empty.setImageResource(R.drawable.project_empty);
							common_text_empty.setText(R.string.common_text_empty);
						}else{
							empty_layout.setVisibility(View.GONE);
//			rootLl.setBackgroundResource(R.color.project_bg);
						}
					};
				}.execute();
				break;
			default:
				break;
			}
			
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TongRenFragment.REQ_PROJECT) {
			getData();
		}
	}
}
