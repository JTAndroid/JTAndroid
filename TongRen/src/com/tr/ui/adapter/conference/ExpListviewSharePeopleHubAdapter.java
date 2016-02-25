package com.tr.ui.adapter.conference;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.conference.MExpFriendContact;
import com.tr.model.obj.JTContactMini;
import com.tr.model.user.OrganizationMini;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.utils.time.Util;

public class ExpListviewSharePeopleHubAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<MExpFriendContact> dataList = new ArrayList<MExpFriendContact>();
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private int SCALESIZE =100;
	
	public ExpListviewSharePeopleHubAdapter(Context context){
		this.context = context;
//		getImageLoader(context);
	}
	public ExpListviewSharePeopleHubAdapter(Context context, List<MExpFriendContact> dataList){
		this.context = context;
//		getImageLoader(context);
		if(!Util.isNull(dataList)){
			this.dataList = dataList;
		}
	}
	public void update(List<MExpFriendContact> dataList){
		if(Util.isNull(dataList)){
			this.dataList.clear();
		}else{
			this.dataList = dataList;
		}
//		this.notifyDataSetChanged();
	}
	public List<MExpFriendContact> getExpFriendContact(){
		return dataList;
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		if(Util.isNull(dataList)){
			return 0;
		}else{
			return dataList.size();
		}
	}
	/* public void initImageLoader(Context context) {
			// 缓存文件的目录
			File cacheDir = StorageUtils.getOwnCacheDirectory(context, "/gintong/image/");
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context)
					.memoryCacheExtraOptions(Utils.dipToPx(context, SCALESIZE  ),
							Utils.dipToPx(context, SCALESIZE ))
					// max width, max height，即保存的每个缓存文件的最大长宽
					.discCacheExtraOptions(240, 400, CompressFormat.PNG, 40, null)
					.threadPoolSize(3)
					// 线程池内加载的数量
					.threadPriority(Thread.NORM_PRIORITY - 2)
					.denyCacheImageMultipleSizesInMemory()
					.discCacheFileNameGenerator(new Md5FileNameGenerator()) // 将保存的时候的URI名称用MD5
																			// 加密
					.memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024)) // You
																					// can
																					// pass
																					// your
																					// own
																					// memory
																					// cache
																					// implementation/你可以通过自己的内存缓存实现
					.memoryCacheSize(4 * 1024 * 1024) // 内存缓存的最大值
					.discCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
					.tasksProcessingOrder(QueueProcessingType.LIFO)
					// 由原先的discCache -> diskCache
					.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
					.imageDownloader(
							new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
																					// (5
																					// s),
																					// readTimeout
																					// (30
																					// s)超时时间
					.writeDebugLogs() // Remove for release app
					.build();
			// 全局初始化此配置
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);
		}
	    
	    
	    public ImageLoader getImageLoader(Context context) {
	    	if (!imageLoader.isInited()) {
	    		initImageLoader(context);
			}
			return imageLoader;
		}*/
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return dataList.get(groupPosition);
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GroupHolderView holderView;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hy_item_invite_friend_explv_group_letter, parent, false);
			holderView = new GroupHolderView();
			holderView.letter = (TextView) convertView.findViewById(R.id.hy_itemInvitefriend_expLvGroup_letterText);
			convertView.setTag(holderView);
		}else{
			holderView = (GroupHolderView) convertView.getTag();
		}
		setGroupHolderView(holderView, groupPosition);
		return convertView;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		int count;
		if (Util.isNull(dataList)) {
			count = 0;
		}
		else {
			if (!Util.isNull(dataList.get(groupPosition).contactList)) {
				count = dataList.get(groupPosition).contactList.size();
			}
			else if (!Util.isNull(dataList.get(groupPosition).organizationList)) {
				count = dataList.get(groupPosition).organizationList.size();
			}
			else {
				count = 0;
			}
		}
		return count;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return dataList.get(groupPosition).contactList==null?dataList.get(groupPosition).organizationList.get(childPosition):dataList.get(groupPosition).contactList.get(childPosition);
	}

	

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChildHolderView holderView;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hy_item_invite_friend_lv, parent, false);
			holderView = new ChildHolderView();
			holderView.checkbox = (ImageView) convertView.findViewById(R.id.hy_itemInvitefriend_checkbox);
			holderView.avatar = (ImageView) convertView.findViewById(R.id.hy_itemInvitefriend_avatar);
			holderView.name = (TextView) convertView.findViewById(R.id.hy_itemInvitefriend_nameText);
			convertView.setTag(holderView);
		}else{
			holderView = (ChildHolderView) convertView.getTag();
		}
		setChildHolderView(holderView, groupPosition, childPosition);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}
	/**
	 * @param holderView
	 * @param groupPosition
	 */
	private void setGroupHolderView(GroupHolderView holderView, int groupPosition){
		holderView.letter.setText(dataList.get(groupPosition).nameCh);
	}
	private void setChildHolderView(ChildHolderView holderView, int groupPosition, int childPosition){
		if (dataList.get(groupPosition).contactList!=null) {
			JTContactMini item = dataList.get(groupPosition).contactList.get(childPosition);
		if(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.containsKey(item.id)){
			holderView.checkbox.setImageResource(R.drawable.hy_check_pressed);
		}else{
			holderView.checkbox.setImageResource(R.drawable.hy_check_norm);
		}
		holderView.avatar.setImageResource(R.drawable.hy_ic_default_friend_avatar);
		if(!Util.isNull(item.image) ){
			imageLoader.displayImage(item.image, holderView.avatar);
		}
		holderView.name.setText(item.name);
		} else if (dataList.get(groupPosition).organizationList!=null) {
		OrganizationMini item = dataList.get(groupPosition).organizationList.get(childPosition);
		if(InitiatorDataCache.getInstance().shareOrgHubSelectedMap.containsKey(item.id)){
			holderView.checkbox.setImageResource(R.drawable.hy_check_pressed);
		}else{
			holderView.checkbox.setImageResource(R.drawable.hy_check_norm);
		}
		holderView.avatar.setImageResource(R.drawable.hy_ic_default_friend_avatar);
		if(!Util.isNull(item.mLogo) ){
			imageLoader.displayImage(item.mLogo, holderView.avatar);
		}
		holderView.name.setText(item.fullName);
		}
	}
	private class GroupHolderView{
		public TextView letter;
	}
	private class ChildHolderView{
		public ImageView checkbox;
		public ImageView avatar;
		public TextView name;
	}
}
