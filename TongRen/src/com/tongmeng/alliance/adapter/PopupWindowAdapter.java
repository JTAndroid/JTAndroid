package com.tongmeng.alliance.adapter;

import java.util.ArrayList;
import java.util.List;

import com.tr.R;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PopupWindowAdapter extends BaseAdapter {

	private List<String> list = null;
	private Activity activity = null;

	public PopupWindowAdapter(Activity activity, List<String> list) {
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position-1;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position-1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.findaction_popolist_item, null, false);
			viewHolder.tv = (TextView) convertView
					.findViewById(R.id.findaction_pop_list_text);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv.setText(list.get(position));
		return convertView;
	}
	
}

	class ViewHolder {
		TextView tv;
	}
