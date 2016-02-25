package com.tr.ui.organization.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.organization.model.notice.CustomerNotice;
import com.tr.ui.people.contactsdetails.adapter.CombiningDataAdapter.ViewHolder;

public class LatestAnnouncementAdapter extends BaseAdapter {

	private List<CustomerNotice> customerNoticeList;

	private Context context;
	
	private ViewHolder holder;

	public LatestAnnouncementAdapter(List<CustomerNotice> customerNoticeList,
			Context context) {
		super();
		this.customerNoticeList = customerNoticeList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return customerNoticeList.size();
	}

	@Override
	public Object getItem(int position) {
		return customerNoticeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(
					R.layout.org_latestannouncement_listview, null);

			holder = new ViewHolder(convertView);

			convertView.setTag(holder);

		}
		
		holder = (ViewHolder) convertView.getTag();
		
		holder.titleTv.setText(customerNoticeList.get(position).title);
		
		holder.dataTv.setText(customerNoticeList.get(position).stkcd);
		
		return convertView;
	}

	public static class ViewHolder {

		private TextView titleTv,dataTv;
		
		public ViewHolder(View convertView) {
			
			titleTv = (TextView) convertView.findViewById(R.id.titleTv);

			dataTv  = (TextView) convertView.findViewById(R.id.dataTv);
		}
	}
}
