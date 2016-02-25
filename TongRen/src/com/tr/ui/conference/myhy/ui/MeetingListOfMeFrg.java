package com.tr.ui.conference.myhy.ui;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.model.SimpleResult;
import com.tr.model.conference.MMeetingDetail;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MeetingOfMineListQuery;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.conference.initiatorhy.InitiatorHYActivity;
import com.tr.ui.conference.myhy.utils.Utils;
import com.tr.ui.widgets.EditOrDeletePopupWindow;
import com.tr.ui.widgets.EditOrDeletePopupWindow.OnMeetingOptItemClickListener;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.AnimateFirstDisplayListener;
import com.utils.string.StringUtils;

/**
 * 我的会议——会议列表tab——分开写便于维护
 * 
 * @author zhongshan
 * 
 */
public class MeetingListOfMeFrg extends JBaseFragment implements IBindData, OnItemClickListener, OnItemLongClickListener {

	private XListView listView;
	private int index;
	/** 会议列表 */
	private ArrayList<MeetingOfMineListQuery> meetinglist;
	private MyMeetingAdapter adapter;
	private AnimateFirstDisplayListener animateFirstDisplayListener;
	private EditOrDeletePopupWindow popupWindow;
	private DisplayImageOptions option;
	private int deletePosition = -1;
	private boolean overMeetingReCreate;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = View.inflate(getActivity(), R.layout.hy_mymeeting_frg_layout, null);
		listView = (XListView) root.findViewById(R.id.listView);
		return root;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setXlistViewConfig(listView);
		meetinglist = new ArrayList<MeetingOfMineListQuery>();
		showLoadingDialog();
		ConferenceReqUtil.doNewGetMyMeeting(getActivity(), MeetingListOfMeFrg.this, App.getUserID(), 0, 10, "", 1, null);
		adapter = new MyMeetingAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}

	private class MyMeetingAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return meetinglist == null ? 0 : meetinglist.size();
		}

		@Override
		public Object getItem(int position) {
			return meetinglist == null ? null : meetinglist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(getActivity(), R.layout.hy_mymeeting_frg_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.meetingIv = (ImageView) convertView.findViewById(R.id.meetingIv);
				viewHolder.meetingTitleTv = (TextView) convertView.findViewById(R.id.meetingTitleTv);
				viewHolder.meetingTimeTv = (TextView) convertView.findViewById(R.id.meetingTimeTv);
				viewHolder.meetingDesTv = (TextView) convertView.findViewById(R.id.meetingDesTv);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			MeetingOfMineListQuery meeting = meetinglist.get(position);
			if (meeting != null) {
				/** 会议状态 0：草稿，1：发起，2：会议进行中，3：会议结束 */
				int meetingStatus = meeting.getMeetingStatus();
				Drawable drawable = null;
				switch (meetingStatus) {
				case 0:
					drawable = getResources().getDrawable(R.drawable.hy_status_draft);
					break;
				case 1:
					drawable = getResources().getDrawable(R.drawable.hy_status_not_start);
					break;
				case 2:
					drawable = getResources().getDrawable(R.drawable.hy_status_doing);
					break;
				case 3:
					drawable = getResources().getDrawable(R.drawable.hy_status_done);
					break;
				}
				if (drawable != null) {
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					viewHolder.meetingTitleTv.setCompoundDrawablePadding(20);
					viewHolder.meetingTitleTv.setCompoundDrawables(drawable, null, null, null);
				}
				viewHolder.meetingTitleTv.setEllipsize(TruncateAt.END);
				viewHolder.meetingTitleTv.setTextColor(Color.BLACK);
				viewHolder.meetingTitleTv.setText(meeting.getMeetingName());
				viewHolder.meetingTimeTv.setText(Utils.parseTime(meeting.getStartTime()));
				if (StringUtils.isEmpty(meeting.getMeetingDesc())) {
					viewHolder.meetingDesTv.setVisibility(View.GONE);
				}else {
					viewHolder.meetingDesTv.setVisibility(View.VISIBLE);
					viewHolder.meetingDesTv.setText(meeting.getMeetingDesc());
				}
				if (option == null) {
					option = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.meeting_logo_b).showImageForEmptyUri(R.drawable.meeting_logo_b)
							.showImageOnFail(R.drawable.meeting_logo_b)/*.displayer(new RoundedBitmapDisplayer(5)).*/.cacheInMemory(true).cacheOnDisc(true).considerExifParams(false).build();
				}
				if (animateFirstDisplayListener == null)
					animateFirstDisplayListener = new AnimateFirstDisplayListener();
				ImageLoader.getInstance().displayImage(meeting.getPath(), viewHolder.meetingIv, option, animateFirstDisplayListener);
//				com.tr.image.ImageLoader.load(viewHolder.meetingIv, meeting.getPath(), R.drawable.meeting_logo_b);
			}

			return convertView;
		}

	}

	class ViewHolder {
		/** 会议封面图片 */
		ImageView meetingIv;
		/** 会议主题 */
		TextView meetingTitleTv;
		/** 会议时间 */
		TextView meetingTimeTv;
		/** 会议介绍 */
		TextView meetingDesTv;
	}

	/** 设置XListView的参数 */
	private void setXlistViewConfig(XListView xListView) {
		xListView.showFooterView(false);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				ConferenceReqUtil.doNewGetMyMeeting(getActivity(), MeetingListOfMeFrg.this, App.getUserID(), 0, 10, "", 1, null);
			}

			@Override
			public void onLoadMore() {
				startGetData();
			}
		});
	}

	/**
	 * 获取页数据
	 */
	public void startGetData() {
		int nowIndex = 0;
		nowIndex = index + 1;
		showLoadingDialog();
		ConferenceReqUtil.doNewGetMyMeeting(getActivity(), this, App.getUserID(), nowIndex, 10, "", 1, null);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		/** 我的会议列表 */
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MY_MEETING) {
			listView.stopLoadMore();
			listView.stopRefresh();
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
						meetinglist.addAll(list);
					}
					adapter.notifyDataSetChanged();
				}
			}
		}
		/** 会议详情 */
		else if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_DETAIL) {
			MMeetingDetail meetingDetail = (MMeetingDetail) object;
			MMeetingQuery meetingData = meetingDetail.getMeeting();
			if (meetingData != null) {
				Intent intent = new Intent(getActivity(), InitiatorHYActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("meetingData", meetingData);
				if (overMeetingReCreate) {
					bundle.putBoolean("OverMeetingReCreate", overMeetingReCreate);
				}
				intent.putExtras(bundle);
				getActivity().startActivity(intent);
			}
		}
		/** 删除会议 */
		else if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETENOTBEGINMEETING) {
			boolean sr = (Boolean) object;
			if (!sr) {
				showToast("删除会议失败");
			} else if (sr) {
				showToast("删除成功");
				if (deletePosition != -1) {
					meetinglist.remove(deletePosition);
					adapter.notifyDataSetChanged();
				}
			}

		}

	}

	/** 进入会议详情或草稿 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position!=0) {
			if (popupWindow != null && popupWindow.isShowing()) {
				return;
			}
			final MeetingOfMineListQuery meeting = meetinglist.get(position - 1);
			if (meeting.getMeetingStatus() == 0) {
				/** 获取详情，进入编辑页面 */
				ConferenceReqUtil.doGetMeetingDetail(getActivity(), MeetingListOfMeFrg.this, meeting.getId(), App.getUserID(), null);
				showLoadingDialog();
			} else {
				/** 跳转详情页面 */
				ENavigate.startSquareActivity(getActivity(), meeting.getId(),0);
			}
		}
	}

	/** 对此会议操作：编辑/删除 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		final MeetingOfMineListQuery meeting = meetinglist.get(position - 1);
		if (popupWindow == null) {
			popupWindow = new EditOrDeletePopupWindow(getActivity());
		}
		dismissPopWindow();
		View dropDownView = ((MyMeetingActivity) getActivity()).getDropDownView();
		/** 0.1草稿/未开始——编辑和删除操作 */
		/** 3.已结束——重新发起和删除操作 */
		/** 2.正在进行——编辑操作 */
		/** 会议状态 0：草稿，1：发起（未开始），2：会议进行中，3：会议结束 */
		if (meeting.getMeetingStatus() == 2) {
			popupWindow.hideDeleteButton("编辑");
		} else if(meeting.getMeetingStatus() == 3){
			popupWindow.showAllButton("重新发起","删除");
		}else if (meeting.getMeetingStatus() == 0 || meeting.getMeetingStatus() == 1) {
			popupWindow.showAllButton("编辑","删除");
		}
		deletePosition = position-1;
		popupWindow.setOnItemClickListener(new OnMeetingOptItemClickListener() {

			@Override
			public void edit(String editStr) {
				overMeetingReCreate = "重新发起".equals(editStr)?true:false;
				ConferenceReqUtil.doGetMeetingDetail(getActivity(), MeetingListOfMeFrg.this, meeting.getId(), App.getUserID(), null);
				showLoadingDialog();
				dismissPopWindow();
			}

			@Override
			public void delete(String deleteStr) {
				new MessageDialog(getActivity(), "删除后将退出会议,会议对应的人脉、\n资料、笔记也将删除", new OnDialogFinishListener() {
					@Override
					public void onFinish(String content) {
						ConferenceReqUtil.doDeleteNotBeginMeeting(getActivity(), MeetingListOfMeFrg.this, meeting.getId(), Integer.valueOf(App.getUserID()), null);
						showLoadingDialog();
					}

					@Override
					public void onCancel(String content) {
						// TODO Auto-generated method stub
						
					}
				}).show();
				dismissPopWindow();
			}
		});
		if (dropDownView != null) {
			popupWindow.showAsDropDown(dropDownView);
		}
		return false;
	}

	private void dismissPopWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}
}
