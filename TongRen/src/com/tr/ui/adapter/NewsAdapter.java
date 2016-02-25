package com.tr.ui.adapter;

import com.tr.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;

	public NewsAdapter() {
		super();

	};

	public NewsAdapter(Context context) {
		super();
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.news_item, parent, false);
			holder = new ViewHolder();
			holder.tv_news_item = (TextView) convertView
					.findViewById(R.id.tv_news_item);
			holder.news_time = (TextView) convertView
					.findViewById(R.id.news_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;

	}

	@Override
	public int getCount() {
		return 16;
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
		TextView tv_news_item;
		TextView news_time;
	}

}
