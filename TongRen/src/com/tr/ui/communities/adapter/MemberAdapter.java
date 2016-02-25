package com.tr.ui.communities.adapter;

import java.util.ArrayList;
import java.util.List;

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
public class MemberAdapter extends BaseAdapter {
	private ArrayList<HeadName> data = new ArrayList<HeadName>();
	Context mContext = null;
	boolean removeState = false;
	public final static String IME_ADD = "-100";// 增加
	public final static String IME_REMOVE = "-200";// 移除

	public MemberAdapter(Context c, ArrayList<HeadName> data) {
		mContext = c;
		this.data = data;
		populateData();
	}

	public MemberAdapter(Context c) {
		mContext = c;
	}

	public void setData(ArrayList<HeadName> data) {
		this.data = data;
		this.notifyDataSetChanged();
		populateData();
	}

	public void setDataNoAdd(ArrayList<HeadName> data) {
		if (null != this.data)
			this.data.clear();
		this.data = data;
		this.notifyDataSetChanged();
	}

	public void setRemoveState(boolean removeState) {
		this.removeState = removeState;
	}

	public boolean getRemoveState() {
		return removeState;
	}

	private void populateData() {
		data.add(new HeadName(R.drawable.chat_add, IME_ADD));
	}

	@Override
	public int getCount() {
		return null != data ? data.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.im_editmumber_grid_item, null);

			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.im_detai_grid_item_image);
			holder.textView = (TextView) convertView.findViewById(R.id.im_detail_grid_item_label);
			holder.deleteImg = (ImageView) convertView.findViewById(R.id.im_detail_grid_delete_image);
			holder.rl_tag = (RelativeLayout) convertView.findViewById(R.id.rl_tag);
			holder.tagIv = (ImageView) convertView.findViewById(R.id.im_detai_grid_item_tagIv);
			holder.tagTv = (TextView) convertView.findViewById(R.id.im_detai_grid_item_tagTv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HeadName item = data.get(position);
		Log.d("xmx", "pic im name:" + item.getName() + ",pic:" + item.getImage());

		holder.imageView.setVisibility(View.VISIBLE);
		holder.textView.setVisibility(View.VISIBLE);
		if (!item.getName().equals(IME_ADD)) {
			holder.textView.setText(item.getName());
			Util.initAvatarImage(mContext, holder.imageView, item.getName(), item.getImage(), 1, 1);
		} else {
			holder.textView.setText("");
			holder.imageView.setImageResource(R.drawable.chat_add);
		}
		if (item.isCreater()) {
			holder.rl_tag.setVisibility(View.VISIBLE);
			holder.tagIv.setImageResource(R.drawable.detail_create);
			holder.tagTv.setText("创建者");
		} else if (item.IsFriend()) {
			holder.rl_tag.setVisibility(View.VISIBLE);
			holder.tagIv.setImageResource(R.drawable.detail_friend);
			holder.tagTv.setText("好友");
		} else {
			holder.rl_tag.setVisibility(View.GONE);
		}

		if (removeState) {
			holder.deleteImg.setVisibility(View.VISIBLE);
			if (item.getHeadId() == R.drawable.im_add_persion) {
				holder.imageView.setVisibility(View.GONE);
				holder.textView.setVisibility(View.GONE);
				holder.deleteImg.setVisibility(View.GONE);
			}
		} else {
			holder.deleteImg.setVisibility(View.GONE);
			convertView.setVisibility(View.VISIBLE);
		}

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
