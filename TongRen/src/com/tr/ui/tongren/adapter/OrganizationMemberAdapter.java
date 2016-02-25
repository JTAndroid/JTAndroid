package com.tr.ui.tongren.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.widgets.IMEditMumberGrid.HeadName;
import com.utils.common.Util;

/** 选人用的 */
public class OrganizationMemberAdapter extends BaseAdapter {
	private ArrayList<HeadName> data = new ArrayList<HeadName>();
	Context mContext = null;
	HashMap<Integer, Boolean> selectPositionState =  new HashMap<Integer, Boolean>();
	public OrganizationMemberAdapter(Context c, ArrayList<HeadName> data) {
		mContext = c;
		this.data = data;
	}

	public OrganizationMemberAdapter(Context c) {
		mContext = c;
	}

	public void setData(ArrayList<HeadName> data) {
		this.data = data;
		this.notifyDataSetChanged();
	}
	public void setRemoveState(boolean removeState,int selectPosition) {
		selectPositionState.clear();
		selectPositionState.put(selectPosition, removeState);
	}

	public boolean getRemoveState(int selectPosition) {
		Boolean state = selectPositionState.get(selectPosition);
		if (state!=null) {
			return state;
		}
		return false;
	}


	public int getCount() {

		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.im_editmumber_grid_item,
					null);

			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.im_detai_grid_item_image);
			holder.textView = (TextView) convertView
					.findViewById(R.id.im_detail_grid_item_label);
			holder.deleteImg = (ImageView) convertView
					.findViewById(R.id.im_detail_grid_delete_image);
			holder.rl_tag = (RelativeLayout) convertView
					.findViewById(R.id.rl_tag);
			holder.tagIv = (ImageView) convertView
					.findViewById(R.id.im_detai_grid_item_tagIv);
			holder.tagTv = (TextView) convertView
					.findViewById(R.id.im_detai_grid_item_tagTv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HeadName item = data.get(position);

		holder.imageView.setVisibility(View.VISIBLE);
		holder.textView.setVisibility(View.VISIBLE);
		holder.textView.setText(item.getName());
		if (item.isCreater()) {
			holder.rl_tag.setVisibility(View.VISIBLE);
			holder.tagIv.setImageResource(R.drawable.detail_create);
			holder.tagTv.setText("创建者");
		} else if (item.IsFriend()) {
			holder.rl_tag.setVisibility(View.VISIBLE);
			holder.tagIv.setImageResource(R.drawable.detail_friend);
			holder.tagTv.setText("管理者");
		} else {
			holder.rl_tag.setVisibility(View.GONE);
		}

		if (getRemoveState(position)) {
			// Display delete icon
			holder.deleteImg.setVisibility(View.VISIBLE);
			// remove add friend icon from the gridview
		} else {
			holder.deleteImg.setVisibility(View.GONE);
			convertView.setVisibility(View.VISIBLE);
		}
		// holder.imageView.setAdjustViewBounds(true);
		// holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		Log.d("xmx",
				"pic im name:" + item.getName() + ",pic:" + item.getImage());
		Util.initAvatarImage(mContext, holder.imageView, item.getName(),
				item.getImage(), 1, 1);

		return convertView;
	}

	class ViewHolder {
		public ImageView imageView;
		public TextView textView;
		public ImageView deleteImg;
		public RelativeLayout rl_tag;
		public ImageView tagIv;
		public TextView tagTv;

	}
}
