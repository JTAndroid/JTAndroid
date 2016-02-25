package com.tr.ui.adapter.demand;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ResourceNode;
import com.tr.model.obj.AffairsMini;
import com.tr.ui.demand.util.TextStrUtil;
import com.utils.common.ViewHolder;

/**
 * @ClassName: ResourceAdapter.java
 * @author fxtx
 * @Date 2015年3月18日 上午11:44:48
 * @Description: 事件信息适配器
 */

public class AffairResourceAdapter extends BaseAdapter {
	private List<AffairNode> resourceNode;
	private Context context;
	private boolean isSize = false;
	private OnClickListener onclick;

	public AffairResourceAdapter(Context context, List<AffairNode> resourData,OnClickListener onclick) {
		this.context = context;
		this.resourceNode=resourData;
		this.onclick = onclick;
	}

	public void setData(List<AffairNode> resourData) {
		if(resourceNode!=null)
			resourceNode.clear();
		this.resourceNode = resourData;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.demand_include_associated_details, null);
		}
		AffairNode affairNode = resourceNode.get(position);
		TextView text = ViewHolder.get(convertView, R.id.demand_details_tv);
		ImageView delete = ViewHolder.get(convertView, R.id.demand_details_delete);
		delete.setTag(affairNode);
		delete.setOnClickListener(onclick);
		if (resourceNode.size() > 1) {
			text.setMaxLines(1);
		} else {
			text.setMaxLines(2);
		}
		
		String key = affairNode.getMemo();
		ArrayList<AffairsMini> listaAffairsMini = affairNode
				.getListAffairMini();
		StringBuilder valueSb = new StringBuilder();
		isSize = false;
		for (int i = 0; i < listaAffairsMini.size(); i++) {
			if (isSize) {
				valueSb.append("、");
			}
			valueSb.append(listaAffairsMini.get(i).title);
			isSize = true;
		}
		TextStrUtil.setHtmlText(text, key, valueSb.toString());

		return convertView;
	}

	@Override
	public int getCount() {
		return resourceNode == null ? 0 : resourceNode.size();
	}

	@Override
	public ResourceNode getItem(int position) {
		return resourceNode.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
