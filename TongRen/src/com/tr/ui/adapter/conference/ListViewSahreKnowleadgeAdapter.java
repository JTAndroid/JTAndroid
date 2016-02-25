package com.tr.ui.adapter.conference;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.utils.time.Util;

public class ListViewSahreKnowleadgeAdapter extends BaseAdapter {
	private Context context;
	private List<KnowledgeMini2> knowleadgeList;   
	
	public ListViewSahreKnowleadgeAdapter(Context context, List<KnowledgeMini2> dataList){
		this.context = context;
		this.knowleadgeList = dataList;
	}
	public void update(List<KnowledgeMini2> dataList){
		this.knowleadgeList = dataList;
		notifyDataSetChanged();
	}
	public List<KnowledgeMini2> getKnowleadgeList(){
		return knowleadgeList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(Util.isNull(knowleadgeList)){
			return 0;
		}else{
			return knowleadgeList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return knowleadgeList.get(position);
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
					R.layout.hy_item_share_check_noavatar, parent, false);
			holderView = new HolderView();
			holderView.checkbox = (ImageView) convertView.findViewById(R.id.hy_item_shareCheck_checkbox);
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
		KnowledgeMini2 item = knowleadgeList.get(position);
		if(InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.containsKey(item.id)){
			holderView.checkbox.setImageResource(R.drawable.hy_check_pressed);
		}else{
			holderView.checkbox.setImageResource(R.drawable.hy_check_norm);
		}
		
		holderView.name.setText(item.title);
		holderView.time.setText("");
		String time = item.modifytime;
		if(!Util.isNull(time)){
			String[] arr = time.split("\\ ");
			if(arr != null && arr.length > 0){
				holderView.time.setText(arr[0]);
			}else{
				holderView.time.setText("");
			}
		}else{
			holderView.time.setText("");
		}
		if (time!=null&&"null".equals(time)) {
			holderView.time.setText("");
		}
	}
	private class HolderView{
		public ImageView checkbox;
		public TextView name;
		public TextView time;
	}
}
