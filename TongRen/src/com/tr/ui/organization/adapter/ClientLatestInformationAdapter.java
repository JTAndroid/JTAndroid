package com.tr.ui.organization.adapter;

import java.util.List;

import com.tr.R;
import com.tr.ui.organization.adapter.LatestInformationAdapter.ViewHolder;
import com.tr.ui.organization.model.NewsBean;
import com.tr.ui.organization.model.PushKnowledge;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 最新资讯
 * @author User
 *
 */

public class ClientLatestInformationAdapter extends BaseAdapter {

	private List<NewsBean> newsList;

	private Context context;

	private ViewHolder holder;

	private String title;

	private String ctime;
	
	public ClientLatestInformationAdapter(
			List<NewsBean> newsList, Context context) {
		super();
		this.newsList = newsList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return newsList.size();
	}

	@Override
	public Object getItem(int position) {
		return newsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(
					R.layout.client_latestinformation_item, null);

			holder = new ViewHolder(convertView);

			convertView.setTag(holder);

		}
		
		holder = (ViewHolder) convertView.getTag();
		
		for (int i = 0; i < newsList.size(); i++) {
			NewsBean newsBean = newsList.get(i);
			title = newsBean.title;
			ctime = newsBean.ctime;
		}
		Log.e("MSG", "标题"+title);
		holder.infoTitleTv.setText(title);
		
		holder.infoDataTv.setText(ctime);
		
		
		
		return convertView;
	}
	
	public static class ViewHolder {

		private TextView infoTitleTv,infoDataTv;
		
		public ViewHolder(View convertView) {
			
			infoTitleTv = (TextView) convertView.findViewById(R.id.information_infoTitleTv);

			infoDataTv  = (TextView) convertView.findViewById(R.id.information_infoDataTv);
		}
	}

}
