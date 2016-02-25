package com.tr.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.tr.R;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.RequirementMini;
import com.tr.ui.adapter.RequirementAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AffairAdapter extends BaseAdapter {

	private List<AffairsMini> mListAffMini = 
			new ArrayList<AffairsMini>(); // 数据源
	private Context mContext; // 数据上下文
	
	public AffairAdapter(Context context){
		mContext = context;
	}
	
	public AffairAdapter(Context context,List<AffairsMini> listAffMini){
		mContext = context;
		mListAffMini = listAffMini;
	}
	
	/**
	 * 更新列表
	 * @param listReqMini
	 */
	public void updateAdapter(List<AffairsMini> listAffMini) {
		if (listAffMini != null) {
			mListAffMini = listAffMini;
		} 
		else {
			mListAffMini.clear();
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListAffMini.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mListAffMini.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_item_affair, parent, false);
			holder = new ViewHolder();
			holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
			holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		initControls(holder, position);
		return convertView;
	}
	
	private void initControls(ViewHolder holder, int position) {
		holder.titleTv.setText(mListAffMini.get(position).title);
		holder.timeTv.setText(mListAffMini.get(position).time);
	}

	class ViewHolder{
		public TextView titleTv; // 标题
		public TextView timeTv; // 发布时间
	}

}
