package com.tr.ui.communities.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.communities.model.MyCommunitListData;
import com.tr.ui.widgets.title.menu.popupwindow.ViewHolder;

/**
 * @ClassName: CommunitiesAdapter
 * @Description: 社群列表适配器
 * @author cui
 * @date 2015-11-26 下午4:01:13
 * 
 */
public class CommunitiesAdapter extends BaseAdapter {
	private Context mcContext;
	private List<ImMucinfo> list = new ArrayList<ImMucinfo>();
	private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.avatar_community) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.avatar_community) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.avatar_community) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.build();

	public CommunitiesAdapter(Context context) {
		this.mcContext = context;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImMucinfo community = new ImMucinfo();
		if (list != null)
			community = this.list.get(position);
		if (convertView == null) {
			convertView = View.inflate(mcContext, R.layout.new_xlistview_item, null);
		}
		// image_group_advocate群主的标志
		ImageView image_group_advocate = ViewHolder.get(convertView, R.id.image_group_advocate);
		if (community.getIsqz() == 1)//群主
			image_group_advocate.setVisibility(View.VISIBLE);
		else
			image_group_advocate.setVisibility(View.GONE);
		
		// 头像
		ImageView circle_image = ViewHolder.get(convertView, R.id.circle_image);
		if (community.getIsys() == 2)//隐身
			circle_image.setImageResource(R.drawable.icon_hiding_bg);
		else
			ImageLoader.getInstance().displayImage(community.getPicPath(), circle_image,options);
		
		TextView text_title = ViewHolder.get(convertView, R.id.text_title);

		TextView text_content = ViewHolder.get(convertView, R.id.text_content);

		text_title.setText(community.getTitle());
		text_content.setText(community.getSubject());
		return convertView;
	}

	 public void setCommunities(List<ImMucinfo> mlist) {
	 this.list = mlist;
	 }
	 public List<ImMucinfo> getCommunities() {
		 return list;
	 }
	 public void addCommunities(List<ImMucinfo> mlist) {
	 if (this.list != null) {
	 this.list.addAll(mlist);
	 notifyDataSetChanged();
	 }
	 }

	public void setKeyWord(String keyWord) {
		List<ImMucinfo> keyWordImMucinfo = new ArrayList<ImMucinfo>();
		for (int i = 0; i < list.size(); i++) {
			ImMucinfo myImMucinfo = list.get(i);
			if (myImMucinfo.getTitle().contains(keyWord)) {
				keyWordImMucinfo.add(myImMucinfo);
			}
		}
		setCommunities(keyWordImMucinfo);
		notifyDataSetChanged();
	}
}
