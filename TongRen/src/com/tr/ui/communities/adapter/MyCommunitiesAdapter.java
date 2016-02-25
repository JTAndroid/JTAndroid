package com.tr.ui.communities.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.CommunityStateResult;
import com.tr.ui.communities.model.MyCommunitListData;
import com.tr.ui.widgets.title.menu.popupwindow.ViewHolder;

/**
 * @ClassName: MyCommunitiesAdapter
 * @Description: 我的社群列表适配器
 * @author cui
 * 
 */
public class MyCommunitiesAdapter extends BaseAdapter {
	private Context mcContext;
	private List<MyCommunitListData> list = new ArrayList<MyCommunitListData>();
	private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.avatar_community) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.avatar_community) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.avatar_community) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.build();
	private String communityShowRedStr;

	public MyCommunitiesAdapter(Context context, List<MyCommunitListData> items) {
		this.mcContext = context;
		this.list = items;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}
	
	public List<MyCommunitListData> getList() {
		return list;
	}

	public void setList(List<MyCommunitListData> list) {
		this.list = list;
	}

	public String getCommunityShowRedStr() {
		return communityShowRedStr;
	}

	public void setCommunityShowRedStr(String communityShowRedStr) {
		this.communityShowRedStr = communityShowRedStr;
	}

	@Override
	public MyCommunitListData getItem(int position) {
		return getList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyCommunitListData imMucinfo = new MyCommunitListData();
		if (list != null)
			imMucinfo = this.list.get(position);
		if (convertView == null) {
			convertView = View.inflate(mcContext, R.layout.new_xlistview_item, null);
		}
		// image_group_advocate群主的标志
		ImageView image_group_advocate = ViewHolder.get(convertView, R.id.image_group_advocate);
		FrameLayout community_list_item_red_fl = ViewHolder.get(convertView, R.id.community_list_item_red_fl);
		if (imMucinfo.getIsqz() == 1)
			image_group_advocate.setVisibility(View.VISIBLE);
		else
			image_group_advocate.setVisibility(View.GONE);

		// 头像
		ImageView circle_image = ViewHolder.get(convertView, R.id.circle_image);
		if (imMucinfo.getIsys() == 2)// 隐身
			circle_image.setImageResource(R.drawable.icon_hiding_bg);
		else
			ImageLoader.getInstance().displayImage(imMucinfo.getPic_path(), circle_image, options);

		TextView text_title = ViewHolder.get(convertView, R.id.text_title);

		TextView text_content = ViewHolder.get(convertView, R.id.text_content);

		text_title.setText(imMucinfo.getTitle());
		text_content.setText(imMucinfo.getSubject());
		if(communityShowRedStr.contains(imMucinfo.getId()+"")){//显示红点
			community_list_item_red_fl.setVisibility(View.VISIBLE);
		}else {//隐藏红点
			community_list_item_red_fl.setVisibility(View.GONE);
		}
		return convertView;
	}

	public void setKeyWord(String keyWord) {
		List<MyCommunitListData> keyWordCommunitListDatas = new ArrayList<MyCommunitListData>();
		for (int i = 0; i < getList().size(); i++) {
			MyCommunitListData myCommunitListData = getList().get(i);
			if (myCommunitListData.getTitle().contains(keyWord)) {
				keyWordCommunitListDatas.add(myCommunitListData);
			}
		}
		setList(keyWordCommunitListDatas);
		notifyDataSetChanged();
	}

}
