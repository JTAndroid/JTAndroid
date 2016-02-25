package com.tr.ui.organization.adapter;

import java.util.List;

import com.tr.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	private List<String> all_Phone;
	private Context context;
	public ListViewAdapter(List<String> all_Phone,Context context){
		this.all_Phone = all_Phone;
		this.context = context;
	}
	@Override
	public int getCount() {
		return all_Phone.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(context, R.layout.organdcus_listviewitem, null);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView phone = (TextView) convertView.findViewById(R.id.phone);
		name.setText("电话");
		phone.setText(all_Phone.get(position));
		return convertView;
	}

}
