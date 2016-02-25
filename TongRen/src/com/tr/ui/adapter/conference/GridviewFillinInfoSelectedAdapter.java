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
import com.tr.model.conference.MFillinInfo;
import com.utils.time.Util;

public class GridviewFillinInfoSelectedAdapter extends BaseAdapter {
	private Context context;
	
	private List<MFillinInfo> fillinfoList = 
			new ArrayList<MFillinInfo>();
	
	public GridviewFillinInfoSelectedAdapter(Context context){
		this.context = context;
	}
//	public GridviewFillinInfoAdapter(Context context, int type,
//			List<FillinInfo> fillinfoList){
//		this.context = context;
//		if(!Util.isNull(fillinfoList)){
////			this.fillinfoList = fillinfoList; 
//			this.fillinfoList.addAll(fillinfoList);
//		}
//	}
	public List<MFillinInfo> getFillinfoList(){
		return fillinfoList;
	}
	public void update(List<MFillinInfo> fillinfoList){
		if(Util.isNull(fillinfoList)){
			this.fillinfoList.clear();
		}else{
			this.fillinfoList.clear();
//			this.fillinfoList = fillinfoList;
			this.fillinfoList.addAll(fillinfoList);
		}
		notifyDataSetChanged();
	}
	public void update(){
		notifyDataSetChanged();
	}
	public void clear(){
		this.fillinfoList.clear();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fillinfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return fillinfoList.get(position);
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
					R.layout.hy_item_fillinfo, parent, false);
			holderView = new HolderView();
			holderView.name = (TextView) convertView.findViewById(R.id.hy_item_fillinfo_text);
			holderView.deleteIcon = (ImageView) convertView.findViewById(R.id.hy_item_fillinfo_deleteIcon);
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
		setHolderView(holderView, position);
		return convertView;
	}
	private void setHolderView(HolderView holderView, int position){
		holderView.name.setTextColor(context.getResources().getColor(R.color.hy_fillinfo_selected_color));
		holderView.name.setBackgroundResource(R.drawable.hy_shape_fillinfo_selected);
		holderView.name.setText(fillinfoList.get(position).name);
		holderView.name.setClickable(true);
		holderView.deleteIcon.setVisibility(View.VISIBLE);
		holderView.name.setOnClickListener(new MyOnClickListener(position));
	}
	private class HolderView{
		public TextView name;
		public ImageView deleteIcon;
	}
	private class MyOnClickListener implements View.OnClickListener{
		private int pos;
		public MyOnClickListener(int pos){
			this.pos = pos;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FillInfoOnCancelSelectListener selectListener = (FillInfoOnCancelSelectListener)context;
			if(selectListener == null){
				return;
			}
			selectListener.fillInfoOnCancelSelectListener(fillinfoList.get(pos));
			fillinfoList.remove(pos);
			notifyDataSetChanged();
		}
	}
	public interface FillInfoOnCancelSelectListener{
		 public abstract void fillInfoOnCancelSelectListener(MFillinInfo fi);
	}
}
