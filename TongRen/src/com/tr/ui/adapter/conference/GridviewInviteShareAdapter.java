package com.tr.ui.adapter.conference;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.conference.AttendeePeopleInfo;
import com.utils.time.Util;

public class GridviewInviteShareAdapter extends BaseAdapter {
	private Context context;

	private List<AttendeePeopleInfo> peopleList = 
			new ArrayList<AttendeePeopleInfo>();
	
	public GridviewInviteShareAdapter(Context context){
		this.context = context;
		AttendeePeopleInfo ap = new AttendeePeopleInfo();
		peopleList.add(ap);
	}
	public void update(List<AttendeePeopleInfo> datalist){
		this.peopleList.clear();
		AttendeePeopleInfo ap = new AttendeePeopleInfo();
		peopleList.add(ap);
		if(!Util.isNull(datalist)){
			this.peopleList.addAll(peopleList.size() - 1, datalist);
		}
		
//		notifyDataSetChanged();
	}
	
	public void update(){
		notifyDataSetChanged();
	}
	public int getListSize(int type){
		return peopleList.size();
	}
	public void clear(){
		this.peopleList.clear();
	}
	@Override
	public int getCount() {
		return peopleList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return peopleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hy_item_attendee, parent, false);
			holderView = new HolderView();
			holderView.image = (ImageView) convertView.findViewById(R.id.hy_item_attendee_image);
			holderView.name = (TextView) convertView.findViewById(R.id.hy_item_attendee_text);
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
		setHolderView(holderView, position);
		return convertView;
	}
	private void setHolderView(HolderView holderView, int position){
		if((peopleList.size() - 1) == position){
//			holderView.image.setBackgroundResource(R.drawable.hy_addimage_bg_selector);
			holderView.image.setImageResource(R.drawable.hy_addimage_bg_selector);
//			holderView.name.setVisibility(View.INVISIBLE);
			holderView.name.setVisibility(View.GONE);
		}else{
			holderView.image.setImageBitmap(peopleList.get(position).image);
			holderView.name.setText(peopleList.get(position).name);
			holderView.name.setVisibility(View.VISIBLE);
//			holderView.name.setVisibility(View.VISIBLE);
		}
	}
	private class HolderView{
		public ImageView image;
		public TextView name;
	}
}
