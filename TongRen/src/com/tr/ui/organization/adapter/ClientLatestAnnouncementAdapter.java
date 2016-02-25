package com.tr.ui.organization.adapter;

import java.util.List;

import com.tr.R;
import com.tr.ui.organization.model.NoticeBean;
import com.tr.ui.organization.model.notice.CustomerNotice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 最新公告
 * @author User
 *
 */

public class ClientLatestAnnouncementAdapter extends BaseAdapter {
	
	private Context context;
	private List<NoticeBean> NoticeList;
	private ViewHolder viewHolder;
	private ViewHolder holder;
	
	public ClientLatestAnnouncementAdapter(Context context,List<NoticeBean> NoticeList){
		super();
		this.context = context;
		this.NoticeList = NoticeList;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.client_latestannoucement_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
			holder = (ViewHolder) convertView.getTag();
		
			holder.notice_titleTv.setText(NoticeList.get(position).title);
			
			holder.notice_timeTv.setText(NoticeList.get(position).stkcd);
			
			return convertView;
	}

	@Override
	public int getCount() {
		return NoticeList.size();
	}

	@Override
	public Object getItem(int position) {
		return NoticeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder{
		
     private TextView notice_titleTv,notice_timeTv;
		
		public ViewHolder(View convertView) {
			
			notice_titleTv = (TextView) convertView.findViewById(R.id.notice_titleTv);

			notice_timeTv  = (TextView) convertView.findViewById(R.id.notice_timeTv);
		}
	}

}
