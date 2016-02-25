package com.tr.ui.tongren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.tongren.model.project.Apply;
import com.tr.ui.tongren.model.project.Organization;
import com.tr.ui.tongren.model.project.ProjectApply;
import com.tr.ui.tongren.model.project.UndertakeParameter;
import com.utils.http.EAPIConsts;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProjectApplyAdapter extends BaseAdapter {
	private Context mContext;
	private List<ProjectApply> applies = new ArrayList<ProjectApply>();
	private String projectId;
	private CollaborationOnClickListener clickListener;

	public ProjectApplyAdapter(Context context,String projectId,CollaborationOnClickListener clickListener) {
		this.mContext = context;
		this.projectId =projectId;
		this.clickListener = clickListener;
	}

	public List<ProjectApply> getApplies() {
		return applies;
	}

	public void setApplies(List<ProjectApply> applies) {
		this.applies = applies;
	}

	@Override
	public int getCount() {
		return getApplies().size();
	}

	@Override
	public ProjectApply getItem(int arg0) {
		return getApplies().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ProjectApplyViewHolder holder;
		if (arg1 == null) {
			holder = new ProjectApplyViewHolder();
			arg1 = View.inflate(mContext, R.layout.adapter_projectapply, null);
			holder.applyIconIV = (ImageView) arg1
					.findViewById(R.id.applyIconIV);
			holder.applyLogoIv = (ImageView) arg1
					.findViewById(R.id.applyLogoIv);
			holder.applyNameTv = (TextView) arg1.findViewById(R.id.applyNameTv);
			holder.applyContentIV = (TextView) arg1.findViewById(R.id.applyContentIV);
			holder.collaborationBt = (Button) arg1
					.findViewById(R.id.collaborationBt);
			arg1.setTag(holder);
		} else {
			holder = (ProjectApplyViewHolder) arg1.getTag();
		}
		final ProjectApply projectApply = getItem(arg0);
		if (projectApply.isOrganization) {
			Drawable drawable = mContext.getResources().getDrawable(R.drawable.undertake_organization);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
			holder.applyNameTv .setCompoundDrawables(null, null, drawable, null);
			holder.applyNameTv .setCompoundDrawablePadding(20);
			holder.applyNameTv .setText(projectApply.organizationName);
			ImageLoader.getInstance().displayImage(projectApply.organizationLogo, holder.applyLogoIv);
			holder.applyIconIV .setImageResource(R.drawable.undertake_organization);
			holder.applyContentIV.setText("创建者："+projectApply.createrName+"   "+"成员："+projectApply.organizationMemberSize);
		}else{
			Drawable drawable = mContext.getResources().getDrawable(R.drawable.undertake_person);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
			holder.applyNameTv .setCompoundDrawables(null, null, drawable, null);
			holder.applyNameTv .setCompoundDrawablePadding(20);
			holder.applyNameTv .setText(projectApply.proposerName);
			ImageLoader.getInstance().displayImage(projectApply.proposerfigureurl, holder.applyLogoIv);
			holder.applyIconIV .setImageResource(R.drawable.undertake_person);
		}
		holder.collaborationBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clickListener.onClick(v,projectApply);
				
			}
		});
		
		return arg1;
	}
	public interface CollaborationOnClickListener{
		void onClick (View v, ProjectApply projectApply);
	}
	class ProjectApplyViewHolder {
		ImageView applyLogoIv, applyIconIV;
		TextView applyNameTv, applyContentIV;
		Button collaborationBt;
	}
}
