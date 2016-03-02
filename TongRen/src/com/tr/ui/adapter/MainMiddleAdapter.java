package com.tr.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.home.MainImagesItem;
import com.tr.ui.widgets.title.menu.popupwindow.ViewHolder;
import com.utils.common.Util.DensityUtil;

public class MainMiddleAdapter extends BaseAdapter {
	private Context mContext;
	private String IMAGE_ITEM = "imgage_item";
	private List<MainImagesItem> list;
	private static final int[] imagesTop = new int[] {
		R.drawable.image_contacts, R.drawable.image_customer,
		R.drawable.image_activitys, R.drawable.image_knowledges,
		R.drawable.image_tong_ren, R.drawable.image_demands };

	public MainMiddleAdapter(Context context, List<MainImagesItem> list) {
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
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void updateList(List<MainImagesItem> mlist) {
		if (this.list != null) {
			this.list.clear();
			this.list.addAll(mlist);
		}
	}

	public String getAlt(int position) {
		return this.list.get(position).getAlt();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.gridview_find_item,
					null);
		}
		ImageView image_logo = ViewHolder
				.get(convertView, R.id.image_item_find_iv);
		LayoutParams params = image_logo.getLayoutParams();
		if (position == 4) {//桐人模块UI单独处理
			params.height = params.WRAP_CONTENT;
			params.width = params.WRAP_CONTENT;
		}else{
			params.height = DensityUtil.dip2px(mContext, 50);
			params.width = DensityUtil.dip2px(mContext, 50);
		}
		image_logo.setLayoutParams(params);
//		if (TextUtils.isEmpty(this.list.get(position).getImages().getLarge())) {
//			// image_logo.setBackgroundResource(this.list.get(position).getImages().getResourceID());
//			int resourceID = this.list.get(position).getImages()
//					.getResourceID();
//			image_logo.setImageDrawable(mContext.getResources().getDrawable(
//					resourceID));
//		} else {
//			ImageLoader.getInstance().displayImage(
//					this.list.get(position).getImages().getLarge(), image_logo);
			/**
			 * 第三方在个别手机上容易使图片背景变黑
			 */
//			ImageLoader.getInstance().displayImage(this.list.get(position).getImages().getLarge(), image_logo);

			/**
			 * com.tr.image.ImageLoader 网络更新有问题 APP1.1需要进行修改
			 */
			com.tr.image.ImageLoader.load(image_logo,
					com.tr.image.ImageLoader.INDEX_BITMAP,
					this.list.get(position).getImages().getLarge(),
					imagesTop[position]);
//		}
		return convertView;
	}

}