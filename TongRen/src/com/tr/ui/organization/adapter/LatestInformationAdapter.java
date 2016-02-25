package com.tr.ui.organization.adapter;

import java.util.List;

import com.tr.R;
import com.tr.ui.organization.adapter.LatestAnnouncementAdapter.ViewHolder;
import com.tr.ui.organization.model.PushKnowledge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LatestInformationAdapter extends BaseAdapter {

	private List<PushKnowledge> pushKnowledgeNoticeList;

	private Context context;

	private ViewHolder holder;
	
	public LatestInformationAdapter(
			List<PushKnowledge> pushKnowledgeNoticeList, Context context) {
		super();
		this.pushKnowledgeNoticeList = pushKnowledgeNoticeList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return pushKnowledgeNoticeList.size();
	}

	@Override
	public Object getItem(int position) {
		return pushKnowledgeNoticeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(
					R.layout.org_latestinformation_listview, null);

			holder = new ViewHolder(convertView);

			convertView.setTag(holder);

		}
		
		holder = (ViewHolder) convertView.getTag();
		
		holder.infoTitleTv.setText(pushKnowledgeNoticeList.get(position).title);
		
		holder.infoDataTv.setText(pushKnowledgeNoticeList.get(position).ctime);
		
		return convertView;
	}
	
	public static class ViewHolder {

		private TextView infoTitleTv,infoDataTv;
		
		public ViewHolder(View convertView) {
			
			infoTitleTv = (TextView) convertView.findViewById(R.id.infoTitleTv);

			infoDataTv  = (TextView) convertView.findViewById(R.id.infoDataTv);
		}
	}

}
