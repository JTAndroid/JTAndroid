package com.tr.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.tencent.qq.m;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.home.MainImagesItem;
import com.tr.ui.widgets.title.menu.popupwindow.ViewHolder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GintongMainAdapter extends BaseAdapter {
	private Context mContext;
	private String IMAGE_ITEM = "imgage_item";
	private List<MainImagesItem> list;
	private static final int[] imagesBottom = new int[] {
		R.drawable.image_capital, R.drawable.image_project,
		R.drawable.image_stock, R.drawable.image_gt_project,
		R.drawable.image_clue, R.drawable.image_tongmeng_activity,
		R.drawable.image_gintong_think_tank, R.drawable.image_perpetual_calendar,
		R.drawable.image_express_delivery, R.drawable.image_housing_loan,
		R.drawable.image_ticket, R.drawable.image_kitchen };

	public GintongMainAdapter(Context context, List<MainImagesItem> list) {
		this.mContext = context;
		this.list = new ArrayList<MainImagesItem>();
		this.list.clear();
		this.list.addAll(list);
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void updateList(List<MainImagesItem> mlist) {
		if (this.list != null){
			this.list.clear();
			this.list.addAll(mlist);
		}
	}

	public String getAlt(int position) {
		return this.list.get(position).getAlt();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GridViewItemHolder mGridViewItemHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.gridview_find_item, null);
			mGridViewItemHolder = new GridViewItemHolder();
			mGridViewItemHolder.image_item_find_iv = (ImageView) convertView.findViewById(R.id.image_item_find_iv);
			convertView.setTag(mGridViewItemHolder);
		}else {
			mGridViewItemHolder  = (GridViewItemHolder) convertView.getTag();
		}
//		if (TextUtils.isEmpty(this.list.get(position).getImages().getLarge())) {
//			int resourceID = this.list.get(position).getImages().getResourceID();
//			mGridViewItemHolder.image_item_find_iv.setImageDrawable(mContext.getResources().getDrawable(resourceID));
//		} else {
			/**
			 * 第三方在个别手机上容易使图片背景变黑
			 */
//			ImageLoader.getInstance().displayImage(this.list.get(position).getImages().getLarge(), mGridViewItemHolder.image_item_find_iv);
			/**
			 * com.tr.image.ImageLoader 网络更新有问题 ，APP1.1需要进行修改
			 */
			com.tr.image.ImageLoader.load(mGridViewItemHolder.image_item_find_iv, com.tr.image.ImageLoader.INDEX_BITMAP, this.list.get(position).getImages().getLarge(), imagesBottom[position]);
//		}
		return convertView;
	}
	
	class GridViewItemHolder{
		ImageView image_item_find_iv;
	}

}
