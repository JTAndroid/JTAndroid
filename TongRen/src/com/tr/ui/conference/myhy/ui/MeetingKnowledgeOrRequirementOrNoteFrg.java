package com.tr.ui.conference.myhy.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
import com.tr.model.conference.MMeetingData;
import com.tr.model.conference.MMeetingNoteQuery;
import com.tr.model.conference.MeetingOfMineListQuery;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.conference.home.MeetingMasterActivity;
import com.tr.ui.conference.home.RTMeetingNoteActivity;
import com.tr.ui.conference.myhy.utils.Utils;
import com.tr.ui.widgets.pulltorefreshExpandableListView.PullToRefreshLayout;
import com.tr.ui.widgets.pulltorefreshExpandableListView.PullToRefreshLayout.OnRefreshListener;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.AnimateFirstDisplayListener;

/**
 * 我的会议——知识列表tab——分开写便于维护
 * 
 * @author zhongshan
 * 
 */
public class MeetingKnowledgeOrRequirementOrNoteFrg extends JBaseFragment implements IBindData, OnChildClickListener, OnGroupClickListener {
	private Context mContext;
	private ExpandableListView expandableListView;
	private PullToRefreshLayout pullToRefreshLayout;
	private ArrayList<MeetingOfMineListQuery> meetinglist;
	private MeetingKnowExpandableListAdapter adapter;
	private int index;

	public static enum KnowlwdgeOrRequireMentType {
		knowledge, requirement,note
	}

	private KnowlwdgeOrRequireMentType type;
	private int reqType;

	public MeetingKnowledgeOrRequirementOrNoteFrg(KnowlwdgeOrRequireMentType type) {
		super();
		this.type = type;
		if (type == KnowlwdgeOrRequireMentType.knowledge) {
			reqType = 4;
		} else if (type == KnowlwdgeOrRequireMentType.requirement) {
			reqType = 5;
		} else if (type == KnowlwdgeOrRequireMentType.note) {
			reqType = 6;
		}
	}

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
		ConferenceReqUtil.doNewGetMyMeeting(mContext, MeetingKnowledgeOrRequirementOrNoteFrg.this, App.getUserID(), 0, 10, "", reqType, null);
		adapter = new MeetingKnowExpandableListAdapter();
		expandableListView.setAdapter(adapter);
	}

	/** 刷新的监听 */
	class MyListener implements OnRefreshListener {

		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
			ConferenceReqUtil.doNewGetMyMeeting(mContext, MeetingKnowledgeOrRequirementOrNoteFrg.this, App.getUserID(), 0, 10, "", reqType, null);
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
		ConferenceReqUtil.doNewGetMyMeeting(getActivity(), this, App.getUserID(), nowIndex, 10, "", reqType, null);
	}

	/** 数据适配 */
	class MeetingKnowExpandableListAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			return meetinglist != null && meetinglist.size() > 0 ? meetinglist.size() : 0;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			if (KnowlwdgeOrRequireMentType.knowledge == type) {
				if (meetinglist != null && meetinglist.size() > 0 && meetinglist.get(groupPosition) != null && meetinglist.get(groupPosition).getListMeetingKnowledge() != null
						&& meetinglist.get(groupPosition).getListMeetingKnowledge().size() > 0) {
					return meetinglist.get(groupPosition).getListMeetingKnowledge().size();
				} else {
					return 0;
				}
			} 
			else if (KnowlwdgeOrRequireMentType.requirement == type) {
				if (meetinglist != null && meetinglist.size() > 0 && meetinglist.get(groupPosition) != null && meetinglist.get(groupPosition).getListMeetingRequirement() != null
						&& meetinglist.get(groupPosition).getListMeetingRequirement().size() > 0) {
					return meetinglist.get(groupPosition).getListMeetingRequirement().size();
				} else {
					return 0;
				}
			} 
			else if (KnowlwdgeOrRequireMentType.note == type) {
				if (meetinglist != null && meetinglist.size() > 0 && meetinglist.get(groupPosition) != null && meetinglist.get(groupPosition).getListMeetingNoteQuery() != null
						&& meetinglist.get(groupPosition).getListMeetingNoteQuery().size() > 0) {
					return meetinglist.get(groupPosition).getListMeetingNoteQuery().size();
				} else {
					return 0;
				}
			} 
			else {
				return 0;
			}
		}

		@Override
		public MeetingOfMineListQuery getGroup(int groupPosition) {
			return meetinglist == null ? null : meetinglist.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			if (KnowlwdgeOrRequireMentType.knowledge == type) {
				return meetinglist != null && meetinglist.size() > 0 && meetinglist.get(groupPosition) != null && meetinglist.get(groupPosition).getListMeetingKnowledge() != null
						&& meetinglist.get(groupPosition).getListMeetingKnowledge().size() > 0 ? meetinglist.get(groupPosition).getListMeetingKnowledge().get(childPosition) : null;
			} 
			else if (KnowlwdgeOrRequireMentType.requirement == type) {
				return meetinglist != null && meetinglist.size() > 0 && meetinglist.get(groupPosition) != null && meetinglist.get(groupPosition).getListMeetingRequirement() != null
						&& meetinglist.get(groupPosition).getListMeetingRequirement().size() > 0 ? meetinglist.get(groupPosition).getListMeetingRequirement().get(childPosition) : null;
			} 
			else if (KnowlwdgeOrRequireMentType.note == type) {
				return meetinglist != null && meetinglist.size() > 0 && meetinglist.get(groupPosition) != null && meetinglist.get(groupPosition).getListMeetingNoteQuery() != null
						&& meetinglist.get(groupPosition).getListMeetingNoteQuery().size() > 0 ? meetinglist.get(groupPosition).getListMeetingNoteQuery().get(childPosition) : null;
			} 
			else {
				return null;
			}
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
				if (KnowlwdgeOrRequireMentType.knowledge == type) {
					if (meeting.getListMeetingKnowledge() != null && meeting.getListMeetingKnowledge().size() > 0) {
						groupViewHolder.meetingPeopleCountTv.setText("" + meeting.getListMeetingKnowledge().size());
					}
				} 
				else if(KnowlwdgeOrRequireMentType.requirement == type) {
					if (meeting.getListMeetingRequirement() != null && meeting.getListMeetingRequirement().size() > 0) {
						groupViewHolder.meetingPeopleCountTv.setText("" + meeting.getListMeetingRequirement().size());
					}
				}else if(KnowlwdgeOrRequireMentType.note == type){
					if (meeting.getListMeetingNoteQuery() != null && meeting.getListMeetingNoteQuery().size() > 0) {
						groupViewHolder.meetingPeopleCountTv.setText("" + meeting.getListMeetingNoteQuery().size());
					}
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
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.hy_my_meeting_knowledge_or_org_item_layout, null);
			}
			TextView desTv = (TextView) convertView.findViewById(R.id.desTv);
			TextView contentTv = (TextView) convertView.findViewById(R.id.contentTv);
			contentTv.setVisibility(View.GONE);
			desTv.setTextColor(Color.BLACK);
			int dip10 = DisplayUtil.dip2px(mContext, 10);
			desTv.setPadding(dip10, dip10, dip10, dip10);
			contentTv.setPadding(dip10, 0, dip10, dip10);
			if (KnowlwdgeOrRequireMentType.knowledge == type) {
				if (meetinglist.get(groupPosition) != null && meetinglist.get(groupPosition).getListMeetingKnowledge() != null && meetinglist.get(groupPosition).getListMeetingKnowledge().size() > 0) {
					final MMeetingData mMeetingData = meetinglist.get(groupPosition).getListMeetingKnowledge().get(childPosition);
					if (mMeetingData != null) {
						desTv.setText(mMeetingData.getDataName());
						convertView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								ENavigate.startKnowledgeOfDetailActivity(getActivity(), mMeetingData.getDataId(), mMeetingData.getDataReqType());
							}
						});
					}
				}
			} 
			else if (KnowlwdgeOrRequireMentType.requirement == type) {
				if (meetinglist.get(groupPosition) != null && meetinglist.get(groupPosition).getListMeetingRequirement() != null
						&& meetinglist.get(groupPosition).getListMeetingRequirement().size() > 0) {
					final MMeetingData mMeetingData = meetinglist.get(groupPosition).getListMeetingRequirement().get(childPosition);
					if (mMeetingData != null) {
						Drawable drawable = null;
						if (mMeetingData.getDataReqType() != 1) {
							drawable = getResources().getDrawable(R.drawable.demand_me_need01);
						} else if (mMeetingData.getDataReqType() == 1) {
							drawable = getResources().getDrawable(R.drawable.demand_financing);
						}
						if (drawable != null) {
							drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
							desTv.setCompoundDrawables(drawable, null, null, null);
						}
						desTv.setCompoundDrawablePadding(20);
						desTv.setText(mMeetingData.getDataName());
						convertView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								ENavigate.startNeedDetailsActivity(mContext, mMeetingData.getDataId() + "", 2);
							}
						});
					}
				}
			}
			else if (KnowlwdgeOrRequireMentType.note == type) {
				if (meetinglist.get(groupPosition) != null && meetinglist.get(groupPosition).getListMeetingNoteQuery() != null
						&& meetinglist.get(groupPosition).getListMeetingNoteQuery().size() > 0) {
					final MMeetingNoteQuery mMeetingNoteQuery = meetinglist.get(groupPosition).getListMeetingNoteQuery().get(childPosition);
					if (mMeetingNoteQuery != null) {
						contentTv.setVisibility(View.VISIBLE);
						desTv.setText(mMeetingNoteQuery.getMeetingNoteTitle()+"");
						if (mMeetingNoteQuery.getListMeetingNoteDetail()!=null&&!mMeetingNoteQuery.getListMeetingNoteDetail().isEmpty()) {
							String string = mMeetingNoteQuery.getListMeetingNoteDetail().get(0).getMeetingNoteContent();
							string = string.replace("￼", "");
							contentTv.setText(string.trim());
						}
						convertView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								//跳转到会议笔记页面
								Intent intent = new Intent(getActivity(), RTMeetingNoteActivity.class);
								intent.putExtra("meeting_id", mMeetingNoteQuery.getMeetingId());
//								intent.putExtra("is_edit", false);
								startActivity(intent);
							}
						});
					}
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
					/**过滤*/
					if (list != null && list.size() > 0) {
						boolean listNotNull = false;
						for (int i = 0; i < list.size(); i++) {
							if (KnowlwdgeOrRequireMentType.knowledge == type) {
								if (list.get(i) != null && list.get(i).getListMeetingKnowledge() != null && list.get(i).getListMeetingKnowledge().size() > 0) {
									listNotNull =true;
								}
							} else if (KnowlwdgeOrRequireMentType.requirement == type) {
								if (list.get(i) != null && list.get(i).getListMeetingRequirement() != null && list.get(i).getListMeetingRequirement().size() > 0) {
									listNotNull =true;
								}
							}else if (KnowlwdgeOrRequireMentType.note == type) {
								if (list.get(i) != null && list.get(i).getListMeetingNoteQuery() != null && list.get(i).getListMeetingNoteQuery().size() > 0) {
									listNotNull =true;
								}
							}
						}
						if (listNotNull) {
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
