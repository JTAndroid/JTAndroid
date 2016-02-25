package com.tr.ui.adapter;

import com.tr.R;
import com.tr.ui.adapter.NewsAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CompetitionAdapter extends BaseAdapter {
	
	

	private Context context;
	private LayoutInflater inflater;

	public CompetitionAdapter() {
		super();

	};

	public CompetitionAdapter(Context context) {
		super();
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.competition_item, parent, false);
			holder = new ViewHolder();
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
		return convertView;

	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
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

}
