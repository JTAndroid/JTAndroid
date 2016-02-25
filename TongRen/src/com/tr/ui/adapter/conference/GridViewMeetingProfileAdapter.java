package com.tr.ui.adapter.conference;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.conference.MMeetingPic;

/**
 * 
 * @author sunjianan
 * 
 */
public class GridViewMeetingProfileAdapter extends BaseAdapter {

	private Context context;
	private List<MMeetingPic> listMeetingPic;
//	private ImageLoader imageLoader;
//	private int SCALESIZE =100;

	public GridViewMeetingProfileAdapter(Context context, List<MMeetingPic> listMeetingPic) {
		this.context = context;
		this.listMeetingPic = listMeetingPic;
//		initImageLoader(context);
	}

	@Override
	public int getCount() {
		if (listMeetingPic != null) {
			return listMeetingPic.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.hy_item_meeting_grid_square_img, null);
//		     /*根据parent动态设置convertview的大小*/  
//            convertView.setLayoutParams(new AbsListView.LayoutParams((int) (parent.getWidth() / 3) - 1, (int) (parent.getHeight() / 2)));// 动态设置item的高度  
			holder.img = (ImageView) convertView.findViewById(R.id.hy_meeting_item_img);
			convertView.setTag(holder);
		} else {
//			 /*解决动态设置convertview大小，第一项不显示的BUG*/  
//            convertView.setLayoutParams(new AbsListView.LayoutParams((int) (parent.getWidth() / 3) - 1, (int) (parent.getHeight() / 2)));// 动态设置item的高度  
			holder = (ViewHolder) convertView.getTag();
		}
		if (listMeetingPic.get(position).getPicPath() != null) {
			ImageLoader.getInstance().displayImage(listMeetingPic.get(position).getPicPath(), holder.img);
		}
		return convertView;
	}
	private class ViewHolder {
		private ImageView img;
	}

}
