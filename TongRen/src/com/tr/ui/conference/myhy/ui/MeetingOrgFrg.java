package com.tr.ui.conference.myhy.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.model.conference.MMeetingOrgan;
import com.tr.model.conference.MeetingOfMineListQuery;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.conference.myhy.utils.Utils;
import com.tr.ui.widgets.pulltorefreshExpandableListView.PullToRefreshLayout;
import com.tr.ui.widgets.pulltorefreshExpandableListView.PullToRefreshLayout.OnRefreshListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.AnimateFirstDisplayListener;

/**
 * 我的会议——组织列表tab——分开写便于维护
 * @author zhongshan
 *
 */
public class MeetingOrgFrg extends JBaseFragment implements OnGroupClickListener, OnChildClickListener, IBindData {
	private Context mContext;
	private ExpandableListView expandableListView;
	private PullToRefreshLayout pullToRefreshLayout;
	private ArrayList<MeetingOfMineListQuery> meetinglist;
	private MeetingExpandableListAdapter adapter;
	private AnimateFirstDisplayListener animateFirstDisplayListener;
	private int index;
	private DisplayImageOptions option;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.mContext = getActivity();
		View root = View.inflate(mContext, R.layout.hy_mymeeting_peoplelist_frg_layout, null);
		pullToRefreshLayout = (PullToRefreshLayout) root.findViewById(R.id.refresh_view);
		expandableListView = (ExpandableListView) root.findViewById(R.id.content_view);
		pullToRefreshLayout.setOnRefreshListener(new MyListener());
		return root;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		meetinglist = new ArrayList<MeetingOfMineListQuery>();
		expandableListView.setGroupIndicator(null);
		Drawable drawable = getResources().getDrawable(R.color.hy_activity_main_bg_color);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), 1);
		expandableListView.setChildDivider(drawable);
		expandableListView.setOnGroupClickListener(this);
		expandableListView.setOnChildClickListener(this);
		ConferenceReqUtil.doNewGetMyMeeting(mContext, MeetingOrgFrg.this, App.getUserID(), 0, 10, "", 3, null);
		adapter = new MeetingExpandableListAdapter();
		expandableListView.setAdapter(adapter);
	}

	/** 刷新的监听 */
	class MyListener implements OnRefreshListener {

		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
			ConferenceReqUtil.doNewGetMyMeeting(mContext, MeetingOrgFrg.this, App.getUserID(), 0, 10, "", 3, null);
		}

		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
			startGetData();
		}

	}

	/**
	 * 获取页数据
	 */
	public void startGetData() {
		int nowIndex = 0;
		nowIndex = index + 1;
		showLoadingDialog();
		ConferenceReqUtil.doNewGetMyMeeting(getActivity(), this, App.getUserID(), nowIndex, 10, "", 3, null);
	}

	/** 数据适配 */
	class MeetingExpandableListAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			return meetinglist != null && meetinglist.size() > 0 ? meetinglist.size() : 0;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			if (meetinglist != null && meetinglist.size() > 0 && meetinglist.get(groupPosition) != null && meetinglist.get(groupPosition).getListMeetingOrgan() != null
					&& meetinglist.get(groupPosition).getListMeetingOrgan().size() > 0) {
				return meetinglist.get(groupPosition).getListMeetingOrgan().size();
			} else {
				return 0;
			}
		}

		@Override
		public MeetingOfMineListQuery getGroup(int groupPosition) {
			return meetinglist == null ? null : meetinglist.get(groupPosition);
		}

		@Override
		public MMeetingOrgan getChild(int groupPosition, int childPosition) {
			return meetinglist != null && meetinglist.size() > 0 && meetinglist.get(groupPosition) != null && meetinglist.get(groupPosition).getListMeetingOrgan() != null
					&& meetinglist.get(groupPosition).getListMeetingOrgan().size() > 0 ? meetinglist.get(groupPosition).getListMeetingOrgan().get(childPosition) : null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			GroupViewHolder groupViewHolder;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.hy_my_meeting_item_relation_group_myhy, null);
				groupViewHolder = new GroupViewHolder();
				groupViewHolder.meetingTimeTv = (TextView) convertView.findViewById(R.id.hy_myhy_im_group_day);
				groupViewHolder.meetingNameTv = (TextView) convertView.findViewById(R.id.hy_myhy_im_group_title);
				groupViewHolder.meetingPeopleCountTv = (TextView) convertView.findViewById(R.id.hy_myhy_im_gruop_number);
				convertView.setTag(groupViewHolder);
			}
			groupViewHolder = (GroupViewHolder) convertView.getTag();
			MeetingOfMineListQuery meeting = meetinglist.get(groupPosition);
			if (meeting != null) {
				groupViewHolder.meetingTimeTv.setText(Utils.splitDate(meeting.getStartTime()));
				groupViewHolder.meetingNameTv.setText(meeting.getMeetingName());
				if (meeting.getListMeetingOrgan() != null && meeting.getListMeetingOrgan().size() > 0) {
					groupViewHolder.meetingPeopleCountTv.setText("" + meeting.getListMeetingOrgan().size());
				}
			}
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					return;
				}
			});
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			ChildViewHolder childViewHolder;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.hy_my_meeting_item_relation_child_myhy, null);
				childViewHolder = new ChildViewHolder();
				childViewHolder.peopleNameTv = (TextView) convertView.findViewById(R.id.hy_myhy_relation_child_name);
				childViewHolder.peoplePicIv = (ImageView) convertView.findViewById(R.id.hy_myhy_relation_child_showimg);
				childViewHolder.peopleOfferTv = (TextView) convertView.findViewById(R.id.hy_myhy_relation_child_status); // 单位
				convertView.setTag(childViewHolder);
			}
			childViewHolder = (ChildViewHolder) convertView.getTag();
			if (meetinglist.get(groupPosition) != null && meetinglist.get(groupPosition).getListMeetingOrgan() != null && meetinglist.get(groupPosition).getListMeetingOrgan().size() > 0) {
				final MMeetingOrgan mMeetingOrg = meetinglist.get(groupPosition).getListMeetingOrgan().get(childPosition);
				if (mMeetingOrg != null) {
					childViewHolder.peopleNameTv.setText(mMeetingOrg.getOrganName());
					childViewHolder.peopleOfferTv.setText("");
					if (animateFirstDisplayListener == null)
						animateFirstDisplayListener = new AnimateFirstDisplayListener();
					if (option == null) {
						option = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.org_default_orgnization).showImageForEmptyUri(R.drawable.org_default_orgnization)
								.showImageOnFail(R.drawable.org_default_orgnization)/*.displayer(new RoundedBitmapDisplayer(5))*/.cacheInMemory(true).cacheOnDisc(true).considerExifParams(false).build();
					}
					ImageLoader.getInstance().displayImage(mMeetingOrg.getOrganPhoto(), childViewHolder.peoplePicIv, option, animateFirstDisplayListener);
					convertView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (mMeetingOrg.getOrganType()==2) {
								ENavigate.startClientDedailsActivity(mContext, Long.valueOf(mMeetingOrg.getOrganId()));
							}else {
								ENavigate.startOrgMyHomePageActivity(mContext, Long.valueOf(mMeetingOrg.getOrganId()), 0, true, 0);
							}
						}
					});
				}
			}
			convertView.setClickable(true);
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	/** 子View的点击事件 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		return false;
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
		return false;
	}

	class GroupViewHolder {
		TextView meetingTimeTv;
		TextView meetingNameTv;
		TextView meetingPeopleCountTv;
	}

	class ChildViewHolder {
		TextView peopleNameTv;
		TextView peopleOfferTv;
		ImageView peoplePicIv;
	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		/** 我的会议列表 */
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MY_MEETING) {
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			if (object != null) {
				Map<String, Object> dataMap = (Map<String, Object>) object;
				if (dataMap != null) {
					int total = (Integer) dataMap.get("total");
					index = (Integer) dataMap.get("index");
					ArrayList<MeetingOfMineListQuery> list = (ArrayList<MeetingOfMineListQuery>) dataMap.get("listMeetingMemberListQuery");
					if (index == 0) {
						meetinglist.clear();
					}
					if (list != null && list.size() > 0) {
						List<MMeetingOrgan> tempList = new ArrayList<MMeetingOrgan>();
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i) != null && list.get(i).getListMeetingOrgan() != null && list.get(i).getListMeetingOrgan().size() > 0) {
								tempList.addAll(list.get(i).getListMeetingOrgan());
							}
						}
						if (tempList.size() > 0) {
							meetinglist.addAll(list);
						}
					}
					for (int i = 0; i < adapter.getGroupCount(); i++) {
						expandableListView.expandGroup(i);
					}
					adapter.notifyDataSetChanged();
				}
			}
		}
	}
}
