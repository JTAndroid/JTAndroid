package com.tr.ui.adapter.conference;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.conference.MFillinInfo;
import com.utils.time.Util;

public class GridviewFillinInfoToSelectAdapter extends BaseAdapter {
	private static String[] selectArr = {"姓名", "性别", "职务", "手机", "固话", "邮箱", "地址", "传真", 
			"单位名称", "所属部门"};
	private Context context;
	
	private List<MFillinInfo> fillinfoList = 
			new ArrayList<MFillinInfo>();
	
	public GridviewFillinInfoToSelectAdapter(Context context){
		this.context = context;
		MFillinInfo fi;
		int index = 0;
		for(String str : selectArr){
			fi = new MFillinInfo();
			fi.name = str;
			fi.id = index;
			fillinfoList.add(fi);
			index++;
		}
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
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
		setHolderView(holderView, position);
		return convertView;
	}
	private void setHolderView(HolderView holderView, int position){
		holderView.name.setTextColor(context.getResources().getColor(R.color.hy_fillinfo_toselect_color));
		if(fillinfoList.get(position).selected){
			holderView.name.setBackgroundResource(R.drawable.hy_shape_fillinfo_select_pressed);
			holderView.name.setClickable(false);
		}else{
			holderView.name.setBackgroundResource(R.drawable.hy_shape_fillinfo_select_normal);
			holderView.name.setClickable(true);
		}
		holderView.name.setText(fillinfoList.get(position).name);
		holderView.name.setOnClickListener(new MyOnClickListener(position));
	}
	private class HolderView{
		public TextView name;
	}
	private class MyOnClickListener implements View.OnClickListener{
		private int pos;
		public MyOnClickListener(int pos){
			this.pos = pos;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FillInfoOnToSelectListener selectListener = (FillInfoOnToSelectListener)context;
			if(selectListener == null){
				return;
			}
			TextView tv = (TextView)v;
//			fillinfoList.get(pos).selected = !fillinfoList.get(pos).selected;
			if(!fillinfoList.get(pos).selected){
				fillinfoList.get(pos).selected = !fillinfoList.get(pos).selected;
				v.setClickable(false);
				tv.setBackgroundResource(R.drawable.hy_shape_fillinfo_select_pressed);
				selectListener.fillInfoOnToSelectListener(fillinfoList.get(pos));
			}
//			else{
//				tv.setBackgroundResource(R.drawable.hy_shape_fillinfo_select_normal);
//				selectListener.fillInfoOnCancelToSelectListener(fillinfoList.get(pos));
//			}
		}
	}
	public interface FillInfoOnToSelectListener{
		 public abstract void fillInfoOnToSelectListener(MFillinInfo fi);
//		 public abstract void fillInfoOnCancelToSelectListener(MFillinInfo fi);
//		 public abstract void fillInfoOnCancelToSelectListener(int id);
	}
}
