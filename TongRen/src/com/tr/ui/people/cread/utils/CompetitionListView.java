package com.tr.ui.people.cread.utils;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.organization.model.peer.CustomerPeerInfo;

public class CompetitionListView {
	/**
	 * 填充特定的适配器
	 * @param context
	 * @param list  
	 * @param l   数据源
	 */
	public static void getdata(final Context context, ListView list,
			final ArrayList<CustomerPeerInfo> l) {
		list.setAdapter(new BaseAdapter() {
			ViewHolder holder;
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = View.inflate(context,
							R.layout.competition_item, null);
					holder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
					
					holder.tv_news_zheda = (TextView) convertView
							.findViewById(R.id.tv_news_zheda);
					holder.tv_news_zheda_content = (TextView) convertView
							.findViewById(R.id.tv_news_zheda_content);
					holder.news_zheda_content_time = (TextView) convertView
							.findViewById(R.id.news_zheda_content_time);
					holder.tv_jindiananli_zheda = (TextView) convertView
							.findViewById(R.id.tv_jindiananli_zheda);
					holder.tv_jindiananli_zheda_content = (TextView) convertView
							.findViewById(R.id.tv_jindiananli_zheda_content);
					holder.jindiananli_content_zheda_time = (TextView) convertView
							.findViewById(R.id.jindiananli_content_zheda_time);
					holder.tv_news_zheda_fl = (TextView) convertView
							.findViewById(R.id.tv_news_zheda_fl);
					holder.tv_flnews_zheda_content = (TextView) convertView
							.findViewById(R.id.tv_flnews_zheda_content);
					holder.news_flcontent_zheda_time = (TextView) convertView
							.findViewById(R.id.news_flcontent_zheda_time);
					holder.tv_zixun_zheda = (TextView) convertView.findViewById(R.id.tv_zixun_zheda);
					holder.tv_zixun_content = (TextView) convertView.findViewById(R.id.tv_zixun_content);
					holder.tv_zixun_time = (TextView) convertView.findViewById(R.id.tv_zixun_time);
					
					holder.tv_zheda_jdal = (TextView) convertView.findViewById(R.id.tv_zheda_jdal);
					holder.tv_jdal_content_zheda = (TextView) convertView.findViewById(R.id.tv_jdal_content_zheda);
					holder.tv_jdal_time_zheda = (TextView) convertView.findViewById(R.id.tv_jdal_time_zheda);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
					
//				CustomerPeerInfo mCustomerPeerInfo = l.get(position);	
//				Log.i("TAG", "MSG"+mCustomerPeerInfo.name);
//				holder.tv_company.setText(mCustomerPeerInfo.name);
//				Log.i("competiton", "competiton"+mCustomerPeerInfo.name);
				
//				holder.tv_news_zheda.setText(mCustomerPeerInfo.list.)
				
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				return l.get(position);
			}
			
			@Override
			public int getCount() {
				return l.size();
			}
			class ViewHolder {
				@SuppressWarnings("unused")
				
				TextView tv_company;
				TextView tv_news_zheda;

				TextView tv_news_zheda_content;

				TextView news_zheda_content_time;

				TextView tv_jindiananli_zheda;

				TextView tv_jindiananli_zheda_content;

				TextView jindiananli_content_zheda_time;

				TextView tv_news_zheda_fl;

				TextView tv_flnews_zheda_content;

				TextView news_flcontent_zheda_time;

				TextView tv_zixun_zheda;

				TextView tv_zixun_content;

				TextView tv_zixun_time;

				TextView tv_zheda_jdal;
				TextView tv_jdal_content_zheda;
				TextView tv_jdal_time_zheda;

			}
		});
	}
	
	
}
