package com.tr.ui.tongren.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.tongren.model.task.AssignUserInfo;
import com.tr.ui.tongren.model.task.Task;
import com.tr.ui.tongren.model.task.TaskDesignatedParameter;
import com.utils.http.EAPIConsts;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProjectTaskAdapter extends BaseAdapter {
	private Context mContext;
	private List<Task> listTask = new ArrayList<Task>();
	private ColorStateList colorStateList;
	public ProjectTaskAdapter(Context context) {
		this.mContext = context;
	}
	

	public List<Task> getListTask() {
		return listTask;
	}
	public void setListTask(List<Task> listTask) {
		Collections.sort(listTask,new Comparator<Task>() {

			@Override
			public int compare(Task lhs, Task rhs) {
				 if(lhs.createTime>rhs.createTime){
				        return -1;
				    }else if(lhs.createTime<rhs.createTime){
				        return 1;
				    }else{
				        return 0;
				    }
			}
		});
		this.listTask = listTask;
	}
	public void setCompleteListTask(List<Task> listCompleteTask) {
		getListTask().addAll(listCompleteTask);
	}
	@Override
	public int getCount() {
		return getListTask().size();
	}

	@Override
	public Task getItem(int position) {
		
		return getListTask().get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ProjectTaskViewHolder holder;
		if (convertView==null) {
			holder= new ProjectTaskViewHolder();
			convertView = View.inflate(mContext, R.layout.adapter_projecttask, null);
			holder.adapterTaskAccessoryTv = (ImageView) convertView.findViewById(R.id.adapterTaskAccessoryTv);
			holder.adapterTaskTimeTv = (TextView) convertView.findViewById(R.id.adapterTaskTimeTv);
			holder.adapterTaskNameTv = (TextView) convertView.findViewById(R.id.adapterTaskNameTv);
			holder.adapterTaskPersonNameTv = (TextView) convertView.findViewById(R.id.adapterTaskPersonNameTv);
			holder.adapterTaskStatusIv = (ImageView) convertView.findViewById(R.id.adapterTaskStatusIv);
			holder.adapterTaskCompleteStatusTv = (TextView) convertView.findViewById(R.id.adapterTaskCompleteStatusTv);
			convertView.setTag(holder);
		}else{
			holder = (ProjectTaskViewHolder) convertView.getTag();
		}
		final Task itemTask = getItem(position);
		if (TextUtils.isEmpty(itemTask.getAttachId())) {
			holder.adapterTaskAccessoryTv.setVisibility(View.GONE);
		}else{
			holder.adapterTaskAccessoryTv.setVisibility(View.VISIBLE);
		}
		if (itemTask.getTaskStatus()==0) {//准备中
			holder.adapterTaskStatusIv.setImageResource(R.drawable.task_notstarted);
			holder.adapterTaskCompleteStatusTv.setVisibility(View.GONE);
		}else if (itemTask.getTaskStatus()==1) {//已开始
			holder.adapterTaskStatusIv.setImageResource(R.drawable.task_underway);
			holder.adapterTaskCompleteStatusTv.setVisibility(View.GONE);
		}else if (itemTask.getTaskStatus()==2) {//已完成
			holder.adapterTaskStatusIv.setVisibility(View.GONE);
			holder.adapterTaskCompleteStatusTv.setVisibility(View.VISIBLE);
			holder.adapterTaskCompleteStatusTv.setText("已完成");
			colorStateList = mContext.getResources().getColorStateList(R.color.projecttextorangebg);
			holder.adapterTaskCompleteStatusTv.setTextColor(colorStateList);
		}else if (itemTask.getTaskStatus()==3) {//已过期
			holder.adapterTaskStatusIv.setVisibility(View.GONE);
			holder.adapterTaskCompleteStatusTv.setVisibility(View.VISIBLE);
			colorStateList = mContext.getResources().getColorStateList(R.color.projecttextredbg);
			holder.adapterTaskCompleteStatusTv.setTextColor(colorStateList);
			holder.adapterTaskCompleteStatusTv.setText("已过期");
		}else if (itemTask.getTaskStatus()==6) {//已驳回
			holder.adapterTaskStatusIv.setVisibility(View.GONE);
			holder.adapterTaskCompleteStatusTv.setVisibility(View.VISIBLE);
			holder.adapterTaskCompleteStatusTv.setText("已驳回");
		}else{
			holder.adapterTaskCompleteStatusTv.setVisibility(View.GONE);
		}
		holder.adapterTaskTimeTv.setText(itemTask.getEndTime());
		holder.adapterTaskNameTv.setText(itemTask.getTitle());
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				StringBuffer stringBuffer = new StringBuffer();
				if (itemTask.extend!=null&&itemTask.extend.assignInfo!=null&&!itemTask.extend.assignInfo.isEmpty()) {
					for (int i = 0; i < itemTask.extend.assignInfo.size(); i++) {
						AssignUserInfo userinfo = itemTask.extend.assignInfo.get(i);
						if (i == itemTask.extend.assignInfo.size()-1) {
							stringBuffer.append(userinfo.name);
						}else{
							stringBuffer.append(userinfo.name+" ");
						}
					}
				}
				
				return stringBuffer.toString();
			}
			protected void onPostExecute(String result) {
				holder.adapterTaskPersonNameTv.setText(result);
			};
		}.execute();
		
		
		return convertView;
	}
	
	class ProjectTaskViewHolder{
		TextView  adapterTaskNameTv,adapterTaskTimeTv,adapterTaskPersonNameTv,adapterTaskCompleteStatusTv;
		ImageView adapterTaskAccessoryTv,adapterTaskStatusIv;
	}
}
