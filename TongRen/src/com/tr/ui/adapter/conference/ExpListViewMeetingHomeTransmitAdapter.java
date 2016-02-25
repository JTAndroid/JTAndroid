package com.tr.ui.adapter.conference;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.conference.MMeetingQuery;

public class ExpListViewMeetingHomeTransmitAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<MMeetingQuery> meetingQuerys;

	private int type_meeting_tile = 0;

	private int type_notstart_meeting = 1;
	private int type_togoing_meeting = 2;
	private int type_terminal_meeting = 3;

	private int[] backgroundColors = new int[] { R.drawable.hy_explist_item_trans_meeting_blue, R.drawable.hy_explist_item_trans_meeting_pink,
			R.drawable.hy_explist_item_trans_meeting_green, R.drawable.hy_explist_item_trans_meeting_orange };
	private int SCALESIZE =100;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public ExpListViewMeetingHomeTransmitAdapter(Context mContext, List<MMeetingQuery> meetingQuerys, long meetingId) {
		this.mContext = mContext;
//		initImageLoader(mContext);
		Iterator<MMeetingQuery> iterator = meetingQuerys.iterator();
		while (iterator.hasNext()) {
			MMeetingQuery meeting = iterator.next();
			if (meeting.getId() == meetingId) {
				iterator.remove();
			}
		}
		this.meetingQuerys = meetingQuerys;
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
	public Object getChild(int groupPosition, int childPosition) {
		if (meetingQuerys != null && meetingQuerys.get(groupPosition) != null && meetingQuerys.get(groupPosition).getListMeetingTopicQuery() != null
				&& meetingQuerys.get(groupPosition).getListMeetingTopicQuery().get(childPosition) != null) {
			return meetingQuerys.get(groupPosition).getListMeetingTopicQuery().get(childPosition);
		}
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO
		ChildHolder childHolder = null;
		if (convertView == null) {
			childHolder = new ChildHolder();
			convertView = View.inflate(mContext, R.layout.hy_exp_list_item_trans_meeting_child, null);
			childHolder.childTime = (TextView) convertView.findViewById(R.id.hy_trans_meeting_time);
			childHolder.childTopic = (TextView) convertView.findViewById(R.id.hy_trans_meeting_topic);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}
		convertView.setBackgroundResource(backgroundColors[childPosition % 4]);
		if (meetingQuerys.get(groupPosition).getListMeetingTopic().get(childPosition).getCreateTime() != null) {
			childHolder.childTime.setText(meetingQuerys.get(groupPosition).getListMeetingTopic().get(childPosition).getCreateTime());
		}
		if (meetingQuerys.get(groupPosition).getListMeetingTopic().get(childPosition).getTopicContent() != null) {
			childHolder.childTopic.setText(meetingQuerys.get(groupPosition).getListMeetingTopic().get(childPosition).getTopicContent());
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (meetingQuerys != null && meetingQuerys.get(groupPosition) != null && meetingQuerys.get(groupPosition).getListMeetingTopic() != null) {
			return meetingQuerys.get(groupPosition).getListMeetingTopic().size();
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		if (meetingQuerys != null && meetingQuerys.size() > 0) {
			return meetingQuerys.get(groupPosition);
		}
		return null;
	}

	@Override
	public int getGroupCount() {
		if (meetingQuerys != null && meetingQuerys.size() > 0) {
			return meetingQuerys.size();
		}
		return 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupHolder holder;
		if (convertView == null) {
			holder = new GroupHolder();
			convertView = View.inflate(mContext, R.layout.hy_list_item_group_trans_meeting, null);
			holder.rlHeader = (RelativeLayout) convertView.findViewById(R.id.hy_trans2meeting_tile_ll_time);
			holder.tvSession = (TextView) convertView.findViewById(R.id.hy_trans2meeting_tile_tv_session);
			holder.tvTime = (TextView) convertView.findViewById(R.id.hy_trans2meeting_tile_tv_time);
			holder.tvMeetingStatus = (TextView) convertView.findViewById(R.id.hy_trans2meeting_tile_tv_status);
			holder.ivLogo = (ImageView) convertView.findViewById(R.id.hy_trans2meeting_tile_iv_logo);
			holder.ivExpand = (ImageView) convertView.findViewById(R.id.hy_trans2meeting_tile_iv_expand);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.hy_trans2meeting_tile_tv_name);
			holder.tvDesc = (TextView) convertView.findViewById(R.id.hy_trans2meeting_tile_tv_desc);
			convertView.setTag(holder);
		} else {
			holder = (GroupHolder) convertView.getTag();
		}
		if (groupPosition >= 0 && groupPosition < meetingQuerys.size()) {
			final MMeetingQuery aMeeting = meetingQuerys.get(groupPosition);
			if (null != aMeeting) {
				if (aMeeting.getType() == type_meeting_tile) {
					holder.tvTime.setText(aMeeting.getStartTime());
					if (aMeeting.getMeetingStatus() == type_notstart_meeting) {
						holder.tvMeetingStatus.setText("未开始");
					} else if (aMeeting.getMeetingStatus() == type_togoing_meeting) {
						holder.tvMeetingStatus.setText("会议中");
					} else if (aMeeting.getMeetingStatus() == type_terminal_meeting) {
						holder.tvMeetingStatus.setText("已结束");
					}
					if (!aMeeting.getPath().isEmpty()) {
						imageLoader.displayImage(aMeeting.getPath(), holder.ivLogo);
					} else {
						holder.ivLogo.setImageResource(R.drawable.hy_home_tile_meeting_default);
					}
					if (aMeeting.getMeetingName() != null) {
						holder.tvTitle.setText(aMeeting.getMeetingName());
					}
					if (aMeeting.getMeetingDesc() != null) {
						holder.tvDesc.setText(aMeeting.getMeetingDesc());
					}
					if (aMeeting.getMeetingType() == 0) {
						holder.ivExpand.setVisibility(View.GONE);
					}
					holder.rlHeader.setVisibility(View.VISIBLE);
					holder.tvSession.setVisibility(View.VISIBLE);
				}
			}
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private class GroupHolder {
		private RelativeLayout rlHeader;
		private TextView tvSession;;
		private TextView tvTime;
		private TextView tvMeetingStatus;
		private ImageView ivLogo;
		private TextView tvTitle;
		private TextView tvDesc;
		private ImageView ivExpand;

	}

	private class ChildHolder {
		private TextView childTime;
		private TextView childTopic;
	}
}
