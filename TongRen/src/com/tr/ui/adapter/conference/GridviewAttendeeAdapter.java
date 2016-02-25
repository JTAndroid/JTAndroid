package com.tr.ui.adapter.conference;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.conference.AttendeePeopleInfo;
import com.tr.model.obj.JTContactMini;
import com.utils.time.Util;

public class GridviewAttendeeAdapter extends BaseAdapter {
	private Context context;
	private int type;

	private List<AttendeePeopleInfo> peopleList = 
			new ArrayList<AttendeePeopleInfo>();
	private List<JTContactMini> inviteAttendList = new ArrayList<JTContactMini>(0);
	public GridviewAttendeeAdapter(Context context){
		this.context = context;
	}
	public GridviewAttendeeAdapter(Context context, int type){
		this.context = context;
		this.type = type;
//		initImageLoader(context);
		if(type == 0){
			JTContactMini fricontact = new JTContactMini();
			inviteAttendList.add(fricontact);
		}else if(type == 1){
			AttendeePeopleInfo ap = new AttendeePeopleInfo();
			peopleList.add(ap);
		}else{
			AttendeePeopleInfo ap = new AttendeePeopleInfo();
			peopleList.add(ap);
		}
	}
//	public void update(List<AttendeePeopleInfo> peopleList){
//		if(Util.isNull(peopleList)){
//			this.peopleList.clear();
//		}else{
//			this.peopleList.clear();
////			this.peopleList = peopleList;
//			this.peopleList.addAll(peopleList);
//		}
//		notifyDataSetChanged();
//	}
	public void update(List<JTContactMini> inviteAttendList){
		this.inviteAttendList.clear();
		JTContactMini fricontact = new JTContactMini();
		inviteAttendList.add(fricontact);
		if(!Util.isNull(inviteAttendList)){
			this.inviteAttendList.addAll(inviteAttendList.size() - 1, inviteAttendList);
		}
//		notifyDataSetChanged();
//		InitiatorHYActivity activity = (InitiatorHYActivity)context;
//		activity.setGridViewParam(shareGridView, 92, 50, );
	}
	public void update(){
		notifyDataSetChanged();
	}
	public int getListSize(int type){
		if(type == 0){
			return inviteAttendList.size();
		}else if(type == 1){
			return peopleList.size();
		}else{
			return peopleList.size();
		}
	}
	public void clear(){
		inviteAttendList.clear();
		this.peopleList.clear();
	}
	@Override
	public int getCount() {
		if(type == 0){
			return inviteAttendList.size();
		}else if(type == 1){
			return peopleList.size();
		}else{
			return peopleList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(type == 0){
			return inviteAttendList.get(position);
		}else if(type == 1){
			return peopleList.get(position);
		}else{
			return peopleList.get(position);
		}
	}
	/* public void initImageLoader(Context context) {
			// 缓存文件的目录
			File cacheDir = StorageUtils.getOwnCacheDirectory(context, "/gintong/image/");
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context)
					.memoryCacheExtraOptions(Utils.dipToPx(context, SCALESIZE   ),
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
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hy_item_attendee, parent, false);
			holderView = new HolderView();
			holderView.image = (ImageView) convertView.findViewById(R.id.hy_item_attendee_image);
			holderView.name = (TextView) convertView.findViewById(R.id.hy_item_attendee_text);
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
		setHolderView(holderView, position);
		return convertView;
	}
	private void setHolderView(HolderView holderView, int position){
		if(type == 0){
			JTContactMini item = inviteAttendList.get(position);
			if((inviteAttendList.size() - 1) == position){
				holderView.image.setImageResource(R.drawable.hy_add_attendee_normal);
				holderView.name.setText("添加");
			}else{
				holderView.image.setImageResource(R.drawable.hy_ic_default_friend_avatar);
				if(!Util.isNull(item.image) ){
					ImageLoader.getInstance().displayImage(item.image, holderView.image);
				}
			}
		}else if(type == 1){
			if((peopleList.size() - 1) == position){
				holderView.image.setImageResource(R.drawable.hy_add_attendee_normal);
//				holderView.name.setVisibility(View.INVISIBLE);
				holderView.name.setText("添加");
			}else{
				holderView.image.setImageBitmap(peopleList.get(position).image);
				holderView.name.setText(peopleList.get(position).name);
//				holderView.name.setVisibility(View.VISIBLE);
			}
			
		}else{
			if((peopleList.size() - 1) == position){
				holderView.image.setImageResource(R.drawable.hy_add_attendee_normal);
//				holderView.name.setVisibility(View.INVISIBLE);
				holderView.name.setText("添加");
			}else{
				holderView.image.setImageBitmap(peopleList.get(position).image);
				holderView.name.setText(peopleList.get(position).name);
			}
		}
	}
	private class HolderView{
		public ImageView image;
		public TextView name;
	}
}
