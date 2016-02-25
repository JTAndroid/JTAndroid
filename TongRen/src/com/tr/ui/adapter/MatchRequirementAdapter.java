package com.tr.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.tr.model.obj.RequirementMini;
import com.tr.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @ClassName:     MatchRequirementAdapter.java
 * @Description:   匹配的需求列表适配器
 * @Author         leon
 * @Version        v 1.0  
 * @Created        2014-04-14
 * @Updated 	   2014-04-14
 */
public class MatchRequirementAdapter extends BaseAdapter {

	private List<RequirementMini> mListReqMini = 
			new ArrayList<RequirementMini>(); // 数据源
	private Context mContext; // 数据上下文
	
	public MatchRequirementAdapter(Context context){
		mContext = context;
	}
	
	public MatchRequirementAdapter(Context context,List<RequirementMini> listReqMini){
		mContext = context;
		mListReqMini = listReqMini;
	}
	
	/**
	 * 更新列表
	 * @param listReqMini
	 */
	public void updateAdapter(List<RequirementMini> listReqMini) {
		if (listReqMini != null) {
			mListReqMini = listReqMini;
		} 
		else {
			mListReqMini.clear();
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListReqMini.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mListReqMini.get(position);
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
					R.layout.list_item_match_requirement, parent, false);
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
		holder.titleTv.setText(mListReqMini.get(position).mTitle);
		holder.timeTv.setText(mListReqMini.get(position).mTime);
	}

	class ViewHolder{
		public TextView titleTv; // 标题
		public TextView timeTv; // 发布时间
	}
}
