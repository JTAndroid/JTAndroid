package com.tr.ui.tongren.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.tongren.model.project.Apply;
import com.tr.ui.tongren.model.project.Project;
import com.tr.ui.tongren.model.project.Publish;
import com.tr.ui.tongren.model.project.Undertaken;

public class ProjectAdapter extends BaseAdapter{
	
	private Context mContext;
	private List<Publish> listPublish = new ArrayList<Publish>();
	private List<Undertaken> listUndertaken = new ArrayList<Undertaken>();
	private boolean showExpire;
	private ProjectType projectType;
	
	public enum ProjectType {
		CREATE, //创建
		ACCEPT, //承接
		RECOMMEND //推荐
	}
	public ProjectAdapter(Context context) {
		this.mContext = context;
	}
	
	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	@Override
	public int getCount() {
		if (getProjectType()==ProjectType.CREATE) {
			return getListPublish().size();
		}else if(getProjectType()==ProjectType.ACCEPT){
			return getListUndertaken().size();
		}else if(getProjectType()==ProjectType.RECOMMEND){
			return getListPublish().size();
		}
		return 0;
	}

	public List<Publish> getListPublish() {
		return listPublish;
	}

	public void setListPublish(List<Publish> PublishList) {
		this.listPublish = PublishList;
	}

	public List<Undertaken> getListUndertaken() {
		return listUndertaken;
	}

	public void setListUndertaken(List<Undertaken> listUndertaken) {
		this.listUndertaken = listUndertaken;
	}

	public boolean isShowExpire() {
		return showExpire;
	}

	public void setShowExpire(boolean showExpire) {
		this.showExpire = showExpire;
	}

	@Override
	public Object getItem(int arg0) {
		if (projectType == ProjectType.CREATE) {
			return getListPublish().get(arg0);
		}else if(projectType == ProjectType.ACCEPT){
			return getListUndertaken().get(arg0);
		}else if(projectType == ProjectType.RECOMMEND){
			return getListPublish().get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	@Override
	public int getViewTypeCount() {
		return 3;
	}
	@Override
	public int getItemViewType(int position) {
		Object object = getItem(position);
		ProjectType type = null;
		if (object!=null) {
			type  = doDetectionDataType(object);
			switch (type) {
			case CREATE:
				return 0;
			case ACCEPT:
				return 1;
			case RECOMMEND:
				return 2;
			}
		}
		
		return 0;
	}
	/**
	 * 检测数据类
	 * 
	 * @param object
	 * @return
	 */
	private ProjectType doDetectionDataType(Object object) {
		if (object instanceof Publish&&getProjectType() ==  ProjectType.CREATE) {
			projectType = ProjectType.CREATE;
		}else if (object instanceof Undertaken&&getProjectType() ==  ProjectType.ACCEPT) {
			projectType = ProjectType.ACCEPT;
		}else if (object instanceof Publish&&getProjectType() ==  ProjectType.RECOMMEND) {
			projectType = ProjectType.RECOMMEND;
		}
		return projectType;
	}
	int colorStatus = 0 ;
	private ColorStateList colorStateList;
	private int applySum;
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		int itemViewType = getItemViewType(arg0);
		final ProjectViewHolder projectViewHolder ;
		
		if (itemViewType!=2) {
			
		if (arg1==null) {
			projectViewHolder = new ProjectViewHolder();
			arg1 = View.inflate(mContext, R.layout.adapter_project, null);
			projectViewHolder. adapterProjectName = (TextView) arg1.findViewById(R.id.adapterProjectName);
			projectViewHolder. adapterProjectStatus = (TextView) arg1.findViewById(R.id.adapterProjectStatus);
			projectViewHolder. adapterProjectApplySum = (TextView) arg1.findViewById(R.id.adapterProjectApplySum);
			arg1.setTag(projectViewHolder);
		}else{
			projectViewHolder = (ProjectViewHolder) arg1.getTag();
		}
		projectViewHolder. adapterProjectStatus.setVisibility(View.VISIBLE);
		if (itemViewType == 0) { //我创建的列表
			final Publish publishItem = (Publish) getItem(arg0);
			final Project projectItem = publishItem.getProject();
			
			if (publishItem.getStatus()==3||publishItem.getStatus()==7) {
					projectViewHolder. adapterProjectName.setText(projectItem.getName());
					projectViewHolder. adapterProjectStatus.setText("已过期");
					projectViewHolder. adapterProjectApplySum.setVisibility(View.GONE);
					colorStateList = mContext.getResources().getColorStateList(R.color.projecttextgraybg);
					projectViewHolder. adapterProjectStatus.setTextColor(colorStateList);
			}else{
						applySum = 0;
						if (publishItem.getApplySet().isEmpty()) {
						}else{
							for (Apply apply : publishItem.getApplySet()) {
								if (apply.getStatus()==2) {
								}else if(apply.getStatus()==1){
									applySum++;
								}
							}
						}
						if (publishItem.getStatus()==8) {
							projectViewHolder. adapterProjectStatus.setText("进行中");
							projectViewHolder. adapterProjectApplySum.setVisibility(View.GONE);
							colorStateList = mContext.getResources().getColorStateList(R.color.projecttextgreenbg);
							projectViewHolder. adapterProjectStatus.setTextColor(colorStateList);
						}else if (publishItem.getStatus()==1) {
							if (applySum==0) {
								projectViewHolder. adapterProjectApplySum.setVisibility(View.GONE);
							}else{
								projectViewHolder. adapterProjectApplySum.setVisibility(View.VISIBLE);
								projectViewHolder. adapterProjectApplySum.setText(applySum+"条承接申请");
							}
							projectViewHolder. adapterProjectStatus.setText("未承接");
							colorStateList = mContext.getResources().getColorStateList(R.color.projecttextgraybg);
							projectViewHolder. adapterProjectStatus.setTextColor(colorStateList);
						}else if(publishItem.getStatus()==4){
							projectViewHolder. adapterProjectStatus.setText("已完成");
							projectViewHolder. adapterProjectApplySum.setVisibility(View.GONE);
							colorStateList = mContext.getResources().getColorStateList(R.color.projecttextorangebg);
							projectViewHolder. adapterProjectStatus.setTextColor(colorStateList);
						}
						projectViewHolder. adapterProjectName.setText(projectItem.getName());
					}
					
		}else if(itemViewType == 1){//我承接的列表
			final Undertaken undertakenItem = (Undertaken) getItem(arg0);
			if (undertakenItem.getStatus() == 3) {
					projectViewHolder. adapterProjectName.setText(undertakenItem.name);
					projectViewHolder. adapterProjectStatus.setText("已过期");
					projectViewHolder. adapterProjectApplySum.setVisibility(View.GONE);
					colorStateList = mContext.getResources().getColorStateList(R.color.projecttextgraybg);
					projectViewHolder. adapterProjectStatus.setTextColor(colorStateList);
			}else{
				if (undertakenItem.getStatus() == 0) {
						if (!TextUtils.isEmpty(undertakenItem.lDays)) {
							projectViewHolder. adapterProjectStatus.setText("剩"+undertakenItem.lDays+"天");
							projectViewHolder. adapterProjectStatus.setVisibility(View.VISIBLE);
						}else{
							projectViewHolder. adapterProjectStatus.setVisibility(View.GONE);
						}
						colorStateList = mContext.getResources().getColorStateList(R.color.projecttextgreenbg);
						projectViewHolder. adapterProjectApplySum.setVisibility(View.GONE);
						projectViewHolder. adapterProjectStatus.setTextColor(colorStateList);
				}else if (undertakenItem.getStatus() == 	1) {
					colorStateList = mContext.getResources().getColorStateList(R.color.projecttextorangebg);
					projectViewHolder. adapterProjectStatus.setText("已完成");
					projectViewHolder. adapterProjectApplySum.setVisibility(View.GONE);
					projectViewHolder. adapterProjectStatus.setTextColor(colorStateList);
			}else if (undertakenItem.getStatus() == 	2) {
				projectViewHolder. adapterProjectApplySum.setVisibility(View.GONE);
				projectViewHolder. adapterProjectStatus.setText("已放弃");
				colorStateList = mContext.getResources().getColorStateList(R.color.projecttextredbg);
				projectViewHolder. adapterProjectStatus.setTextColor(colorStateList);
			}
				projectViewHolder. adapterProjectName.setText(undertakenItem.name);
			}
		}

		return arg1;
		}else if(itemViewType == 2){//推荐项目
			if (arg1==null) {
				projectViewHolder = new ProjectViewHolder();
				arg1 = View.inflate(mContext, R.layout.adapter_recommendproject, null);
				projectViewHolder.adapterProjectName = (TextView) arg1.findViewById(R.id.recommendProjectName);
				projectViewHolder.recommendProjectIntroduce = (TextView) arg1.findViewById(R.id.recommendProjectIntroduce);
				projectViewHolder.recommendProjectTime = (TextView) arg1.findViewById(R.id.recommendProjectTime);
				projectViewHolder.adapterProjectApplySum = (TextView) arg1.findViewById(R.id.recommendProjectApplySum);
				projectViewHolder.recommendProjectAccessoryStatus = (ImageView) arg1.findViewById(R.id.recommendProjectAccessoryStatus);
				arg1.setTag(projectViewHolder);
			}else{
				projectViewHolder =(ProjectViewHolder) arg1.getTag();
			}
			final Publish publishItem = (Publish) getItem(arg0);
			Project projectItem = publishItem.getProject();
			projectViewHolder.adapterProjectName.setText(projectItem.getName());
			if (projectItem.resourceAttachments!=null&&!projectItem.resourceAttachments.isEmpty()) {
				projectViewHolder.recommendProjectAccessoryStatus.setVisibility(View.VISIBLE);
			}else{
				projectViewHolder.recommendProjectAccessoryStatus.setVisibility(View.GONE);
			}
			projectViewHolder.recommendProjectTime .setText("截止至"+projectItem.getValidityEndTime());
			if(TextUtils.isEmpty(projectItem.getIntroduction())){
				projectViewHolder.recommendProjectIntroduce.setVisibility(View.GONE);
			}else{
				projectViewHolder.recommendProjectIntroduce.setVisibility(View.VISIBLE);
				projectViewHolder.recommendProjectIntroduce.setText(projectItem.getIntroduction());
			}
			projectViewHolder.adapterProjectApplySum.setText(publishItem.getApplySet().size()+"人申请承接");
			return arg1;
		}
		return null;
	}
	class ProjectViewHolder{
		TextView adapterProjectName;
		TextView adapterProjectStatus;
		TextView adapterProjectApplySum;
		TextView  recommendProjectTime;
		TextView recommendProjectIntroduce;
		ImageView recommendProjectAccessoryStatus;
	}
}
