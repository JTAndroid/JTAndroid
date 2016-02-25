package com.tr.ui.communities.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.demand.DemandASSOData;
import com.tr.ui.widgets.title.menu.popupwindow.ViewHolder;

public class CommumitiesDemandAdapter extends BaseAdapter {

	private Context mContext;
	private List<DemandASSOData> conn;

	public CommumitiesDemandAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return null != conn ? conn.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setData(List<DemandASSOData> conn) {
		if (this.conn != null)
			this.conn.clear();
		if (null != conn)
			this.conn = conn;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DemandASSOData adata = conn.get(position);
		if (convertView == null)
			convertView = View.inflate(mContext, R.layout.adapter_commumitiescontent, null);
		ImageView people_type = ViewHolder.get(convertView, R.id.people_type);
		ImageView imageView = ViewHolder.get(convertView, R.id.picture_Iv);
		TextView title = ViewHolder.get(convertView, R.id.contentNameTv);
		TextView content = ViewHolder.get(convertView, R.id.contentTv);
		LinearLayout layout_TimeTV = ViewHolder.get(convertView, R.id.layout_TimeTV);
		TextView time = ViewHolder.get(convertView, R.id.commumitiesNumTv);
		people_type.setVisibility(View.VISIBLE);
		imageView.setVisibility(View.VISIBLE);
		content.setVisibility(View.GONE);
		layout_TimeTV.setVisibility(View.VISIBLE);
		if (adata.type == 1)// 融资事件
			people_type.setBackgroundResource(R.drawable.demand_me_need02);
		if (adata.type == 0)// 投资事件
			people_type.setBackgroundResource(R.drawable.demand_me_need01);
		title.setText(adata.title);
		time.setText("时间");
		ImageLoader.getInstance().displayImage(adata.picPath, imageView);
		return convertView;
	}

}
