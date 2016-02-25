package com.tr.ui.adapter.conference;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.obj.RequirementMini;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.utils.time.Util;

public class ListViewSahreDemandAdapter extends BaseAdapter {
	private Context context;
	private List<RequirementMini> demandList;
	
	public ListViewSahreDemandAdapter(Context context, List<RequirementMini> dataList){
		this.context = context;
		this.demandList = dataList;
	}
	public void update(List<RequirementMini> dataList){
		this.demandList = dataList;
		notifyDataSetChanged();
	}
	public List<RequirementMini> getDemandList(){
		return demandList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(Util.isNull(demandList)){
			return 0;
		}else{
			return demandList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return demandList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView holderView;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hy_item_share_check, parent, false);
			holderView = new HolderView();
			holderView.checkbox = (ImageView) convertView.findViewById(R.id.hy_item_shareCheck_checkbox);
			holderView.avatar = (ImageView) convertView.findViewById(R.id.hy_item_shareCheck_avatar);
			holderView.name = (TextView) convertView.findViewById(R.id.hy_item_shareCheck_nameText);
			holderView.time = (TextView) convertView.findViewById(R.id.hy_item_shareCheck_rightText);
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
		setHolderView(holderView, position);
		return convertView;
	}
	private void setHolderView(HolderView holderView, int position){
		RequirementMini item = demandList.get(position);
		if(InitiatorDataCache.getInstance().shareDemandSelectedMap.containsKey(item.mID)){
			holderView.checkbox.setImageResource(R.drawable.hy_check_pressed);
		}else{
			holderView.checkbox.setImageResource(R.drawable.hy_check_norm);
		}
		if(!Util.isNull(item.mTypeName)){
			if(item.mTypeName.equals("我要投资")){
				item.setReqType(0);
				holderView.avatar.setImageResource(R.drawable.hy_icon_meeting_detail_invest);
			}else if(item.mTypeName.equals("我要融资")){
				item.setReqType(1);
				holderView.avatar.setImageResource(R.drawable.demand_me_need02);
			}else{
				holderView.avatar.setImageResource(R.drawable.ic_avatar);
			}
		}else{
			holderView.avatar.setImageResource(R.drawable.ic_avatar);
		}
		
		holderView.name.setText(item.mTitle);
		if(!Util.isNull(item.mTime)){
			String[] arr = item.mTime.split("\\ ");
			if(arr != null && arr.length > 0){
				holderView.time.setText(arr[0]);
			}else{
				holderView.time.setText("");
			}
		}else{
			holderView.time.setText("");
		}
	}
	private class HolderView{
		public ImageView checkbox;
		public ImageView avatar;
		public TextView name;
		public TextView time;
	}
}
