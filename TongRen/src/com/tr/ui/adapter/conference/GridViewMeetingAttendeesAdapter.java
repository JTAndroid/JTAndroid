package com.tr.ui.adapter.conference;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.conference.MMeetingMember;
import com.tr.ui.widgets.CircleImageView;
import com.utils.string.StringUtils;
//import com.baidu.navisdk.util.common.StringUtils;

/**
 * 
 * @author sunjianan
 * 
 */
public class GridViewMeetingAttendeesAdapter extends BaseAdapter {

	private Context context;
	private List<MMeetingMember> members;
	//0 签到页面跳转过来  1其他页面
	private int attendeeType;
//	private int SCALESIZE =100;
//	private ImageLoader imageLoader;
	
	public GridViewMeetingAttendeesAdapter(Context context, List<MMeetingMember> members, int attendeeType) {
		this.context = context;
		this.members = members;
//		initImageLoader(context);
		this.attendeeType = attendeeType;
	}

	@Override
	public int getCount() {
		return members.size();
	}

	@Override
	public Object getItem(int position) {
		return members.get(position);
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
			convertView = (LinearLayout) View.inflate(context, R.layout.hy_list_item_meeting_member, null);
			holder.logoIv = (CircleImageView) convertView.findViewById(R.id.hy_member_iv_logo);
			holder.registrationIv = (ImageView) convertView.findViewById(R.id.registrationIv);
			holder.nameTv = (TextView) convertView.findViewById(R.id.hy_member_tv_name);
			holder.locationIv = (ImageView) convertView.findViewById(R.id.hy_member_iv_location);
			holder.distanceTv = (TextView) convertView.findViewById(R.id.hy_member_tv_location);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (!StringUtils.isEmpty(members.get(position).getMemberPhoto())) {
			ImageLoader.getInstance().displayImage(members.get(position).getMemberPhoto(), holder.logoIv,new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.hy_ic_default_friend_avatar) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.hy_ic_default_friend_avatar) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.hy_ic_default_friend_avatar) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.build());
		} else {
			
		}
		if (!StringUtils.isEmpty(members.get(position).getMemberName())) {
			holder.nameTv.setText(members.get(position).getMemberName());
		}
		if (!StringUtils.isEmpty(members.get(position).getSignDistance())) {
			holder.distanceTv.setText(members.get(position).getSignDistance());
		}
//		if (attendeeType == 1) {
//			holder.locationIv.setVisibility(View.GONE);
//			holder.distanceTv.setVisibility(View.GONE);
//		}
		if (members.get(position).getIsSign()==1) {
			holder.registrationIv.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
//	 public void initImageLoader(Context context) {
//			// 缓存文件的目录
//			File cacheDir = StorageUtils.getOwnCacheDirectory(context, "/gintong/image/");
//			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//					context)
//					.memoryCacheExtraOptions(Utils.dipToPx(context, SCALESIZE   ),
//							Utils.dipToPx(context, SCALESIZE ))
//					// max width, max height，即保存的每个缓存文件的最大长宽
//					.discCacheExtraOptions(240, 400, CompressFormat.PNG, 40, null)
//					.threadPoolSize(3)
//					// 线程池内加载的数量
//					.threadPriority(Thread.NORM_PRIORITY - 2)
//					.denyCacheImageMultipleSizesInMemory()
//					.discCacheFileNameGenerator(new Md5FileNameGenerator()) // 将保存的时候的URI名称用MD5
//																			// 加密
//					.memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024)) // You
//																					// can
//																					// pass
//																					// your
//																					// own
//																					// memory
//																					// cache
//																					// implementation/你可以通过自己的内存缓存实现
//					.memoryCacheSize(4 * 1024 * 1024) // 内存缓存的最大值
//					.discCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
//					.tasksProcessingOrder(QueueProcessingType.LIFO)
//					// 由原先的discCache -> diskCache
//					.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
//					.imageDownloader(
//							new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
//																					// (5
//																					// s),
//																					// readTimeout
//																					// (30
//																					// s)超时时间
//					.writeDebugLogs() // Remove for release app
//					.build();
//			// 全局初始化此配置
//			imageLoader = ImageLoader.getInstance();
//			imageLoader.init(config);
//		}
	    
	    
//	    public ImageLoader getImageLoader(Context context) {
//	    	if (!imageLoader.isInited()) {
//	    		initImageLoader(context);
//			}
//			return imageLoader;
//		}
	class ViewHolder {
		private CircleImageView logoIv;
		private ImageView registrationIv;
		private TextView nameTv;
		private ImageView locationIv;
		private TextView distanceTv;
	}

}
