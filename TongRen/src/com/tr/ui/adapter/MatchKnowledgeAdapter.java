package com.tr.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import com.tr.R;
import com.tr.model.obj.Connections;
import com.tr.model.obj.KnowledgeMini;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName:     MatchRelationshipAdapter.java
 * @Description:   匹配的知识列表适配器
 * @Author         leon
 * @Version        v 1.0  
 * @Created        2014-04-14
 * @Updated 	   2014-04-14
 */
public class MatchKnowledgeAdapter extends BaseAdapter {

	private List<KnowledgeMini> mListKnoMini = 
			new ArrayList<KnowledgeMini>(); // 数据源
	private Context mContext; // 数据上下文
	
	public MatchKnowledgeAdapter(Context context){
		mContext = context;
	}
	
	public MatchKnowledgeAdapter(Context context,
			List<KnowledgeMini> listKnoMini) {
		mContext = context;
		mListKnoMini = listKnoMini;
	}
	
	public void updateAdapter(List<KnowledgeMini> listKnoMini){
		if(listKnoMini!=null){
			mListKnoMini = listKnoMini;
		}
		else{
			mListKnoMini.clear();
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListKnoMini.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mListKnoMini.get(position);
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
					R.layout.list_item_match_knowledge, parent, false);
			holder = new ViewHolder();
			holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		initControls(holder, position);
		return convertView;
	}
	
	private void initControls(ViewHolder holder, int position) {
		holder.titleTv.setText(mListKnoMini.get(position).title);
	}

	class ViewHolder{
		public TextView titleTv; // 标题
	}

}
