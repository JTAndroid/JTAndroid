package com.tr.ui.organization.create_clientele;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.demand.Metadata;
import com.tr.ui.demand.FindProjectActivity;

/**
 * @ClassName: FindProjectAdapter.java
 * @author zcs
 * @Date 2015年3月29日 上午11:21:41
 * @Description: TODO(用一句话描述该文件做什么)
 */
public class FindProjectAdapter extends BaseAdapter {
	private FindProjectActivity.TypeItem typeItem;
	private int selectItem = 0;
	private Context cxt;
	List<Metadata> list;


	public FindProjectAdapter(FindProjectActivity.TypeItem typeItem, List<Metadata> list,Context cxt) {
		this.typeItem = typeItem;
		this.list = list;
		this.cxt = cxt;
	}

	@Override
	public int getCount() {
		return list == null || list.size() == 0 ? 0 : list.size();
	}

	/**
	 * 选中时的item
	 */
	public void setSelectItem(int itenIndex) {
		selectItem = itenIndex;
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoulder vh;
		if (convertView == null) {
			convertView = View.inflate(cxt,
					R.layout.demand_find_project_item_area, null);
			vh = new ViewHoulder();
			vh.division_line = convertView.findViewById(R.id.divisionLine);
			vh.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(vh);
		}
		vh = (ViewHoulder) convertView.getTag();
		//TextView tv_content = vh.get(convertView, R.id.tv_content);
		vh.tv_content.setText(list.get(position).name);
		vh.tv_content.setTag(list.get(position));
		switch (typeItem) {
		case item1:
			vh.division_line.setVisibility(View.VISIBLE);
			if (selectItem == position) {
				convertView.setBackgroundResource(R.color.find_project_view_bg);
				vh.division_line
						.setBackgroundResource(R.color.find_project_txt_orange);
			} else {
				convertView
						.setBackgroundResource(R.color.find_project_white_bg);
				vh.division_line.setBackgroundResource(R.color.find_project_bg);
			}
			break;
		case item2:
			vh.division_line.setVisibility(View.VISIBLE);
			if (selectItem == position) {// 如果选中提当前
				convertView
						.setBackgroundResource(R.color.find_project_white_bg);
				vh.division_line
						.setBackgroundResource(R.color.find_project_txt_orange);
				vh.tv_content.setTextColor(cxt.getResources()
						.getColor(R.color.find_project_txt_orange));
			} else {
				vh.tv_content.setTextColor(cxt.getResources()
						.getColor(R.color.find_project_txt_black));
				vh.division_line.setBackgroundResource(R.color.find_project_bg);
				convertView.setBackgroundResource(R.color.find_project_view_bg);
			}
			break;
		case item3:
			vh.division_line.setVisibility(View.VISIBLE);
			convertView.setBackgroundResource(R.color.find_project_white_bg);
			if (selectItem == position) {
				vh.tv_content.setTextColor(cxt.getResources()
						.getColor(R.color.find_project_txt_orange));
				vh.division_line
						.setBackgroundResource(R.color.find_project_txt_orange);
			} else {
				vh.tv_content.setTextColor(cxt.getResources()
						.getColor(R.color.find_project_txt_black));
				vh.division_line
						.setBackgroundResource(R.color.find_project_txt);
			}
			/*
			 * else{ inflate.setBackgroundResource(R.color.merchant_details_bg);
			 * }
			 */
			break;
		}
		return convertView;
	}
	class ViewHoulder {
		View division_line;
		TextView tv_content;
	}

}
