package com.tr.ui.adapter.demand;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.joint.ResourceNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.ui.demand.util.TextStrUtil;
import com.utils.common.ViewHolder;

/**
 * @ClassName: ResourceAdapter.java
 * @author fxtx
 * @Date 2015年3月18日 上午11:44:48
 * @Description: 知识适配器
 */

public class KnowledgeResourceAdapter extends BaseAdapter {
	private List<KnowledgeNode> resourceNode;
	private Context context;
	boolean isSize ;
	private OnClickListener onclick;
	public KnowledgeResourceAdapter(Context context, List<KnowledgeNode> resourData,OnClickListener onclick) {
		this.context = context;
		this.resourceNode=resourData;
		this.onclick = onclick;
	}
	public void setData(List<KnowledgeNode> resourData){
		if (resourceNode != null)
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
		KnowledgeNode knowledgeNode = resourceNode.get(position);
		TextView text = ViewHolder.get(convertView, R.id.demand_details_tv);
		ImageView delete = ViewHolder.get(convertView, R.id.demand_details_delete);
		delete.setTag(knowledgeNode);
		delete.setOnClickListener(onclick);
		if (getCount() > 1) {
			text.setMaxLines(1);
		} else {
			text.setMaxLines(2);
		}
		String key = knowledgeNode.getMemo();
		ArrayList<KnowledgeMini2> listKnowledgeMini2 = knowledgeNode.getListKnowledgeMini2();
		StringBuilder valueSb = new StringBuilder();
		 isSize = false;
		for (int i = 0; i < listKnowledgeMini2.size(); i++) {
			if (isSize) {
				valueSb.append("、");
			}
			valueSb.append(listKnowledgeMini2.get(i).title);
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
