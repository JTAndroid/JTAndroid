package com.tr.ui.conference.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.model.conference.MMeetingMemberListQuery;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.CircleImageView;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.AnimateFirstDisplayListener;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;

/**
 * 最新 邀请函列表
 * 
 * @author 钟山
 * 
 */
public class MeetingInvitationCardActivity extends JBaseActivity implements
		OnItemLongClickListener, OnItemClickListener, OnClickListener,
		IBindData {
	private XListView listview;
	private MenuItem selectAll;
	private MenuItem cancleAll;
	private TextView deleteButton;
	private boolean showChectBox = false;
	private boolean isChectAll = false;
	ArrayList<MMeetingMemberListQuery> meetingMemberListQueries;
	private MeetingInvitationCardAdapter adapter;
	private DisplayImageOptions options;
	AnimateFirstDisplayListener animateFirstDisplayListener;
	private RelativeLayout.LayoutParams lp;
	/** 存放删除会议邀请函的id：key列表中的位置 value会议id */
	private Map<Integer, Long> deleteInvitationCardMap;
	private Context mContext;
	private String currentFlowFrgTitle="会议邀请函";

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(MeetingInvitationCardActivity.this, getActionBar(),currentFlowFrgTitle ,false,null,false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initView();
		initContent();
	}

	private void initView() {
		setContentView(R.layout.hy_activity_meeting_invitation_card);
		listview = (XListView) findViewById(R.id.invitationCardLv);
		lp = (android.widget.RelativeLayout.LayoutParams) listview
				.getLayoutParams();
		deleteButton = (TextView) findViewById(R.id.deleteButton);
		listview.setOnItemLongClickListener(this);
		listview.setOnItemClickListener(this);
		deleteButton.setOnClickListener(this);
		listview.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				ConferenceReqUtil.doGetMeetingInvitionList(mContext, MeetingInvitationCardActivity.this, App.getUserID(),
						null);
				listview.stopRefresh();
			}

			@Override
			public void onLoadMore() {

			}
		});
	}

	private void initContent() {
		// 请求邀请函列表数据
		showLoadingDialog();
		ConferenceReqUtil.doGetMeetingInvitionList(this, this, App.getUserID(),
				null);
		meetingMemberListQueries = new ArrayList<MMeetingMemberListQuery>();
		adapter = new MeetingInvitationCardAdapter(meetingMemberListQueries);
		listview.setAdapter(adapter);
	}

	@SuppressWarnings("unused")
	private class MeetingInvitationCardAdapter extends BaseAdapter {
		ArrayList<MMeetingMemberListQuery> meetingInvitationCardlist;

		public MeetingInvitationCardAdapter(
				ArrayList<MMeetingMemberListQuery> meetingMemberListQueries) {
			super();
			this.meetingInvitationCardlist = meetingMemberListQueries;
		}

		public void setData(
				ArrayList<MMeetingMemberListQuery> meetingMemberListQueries) {
			meetingInvitationCardlist = meetingMemberListQueries;
		}

		@Override
		public int getCount() {
			return meetingInvitationCardlist.size();
		}

		@Override
		public MMeetingMemberListQuery getItem(int position) {
			return meetingInvitationCardlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return meetingInvitationCardlist.get(position).getId();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.hy_meeting_notice_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.meetingIv = (CircleImageView) convertView
						.findViewById(R.id.meetingIv);
				viewHolder.meetingTitleTv = (TextView) convertView
						.findViewById(R.id.meetingTitleTv);
				viewHolder.meetingOperateTv = (TextView) convertView
						.findViewById(R.id.meetingOperateTv);
				viewHolder.meetingTimeTv = (TextView) convertView
						.findViewById(R.id.meetingTimeTv);
				viewHolder.meetingInviteCardCb = (CheckBox) convertView
						.findViewById(R.id.meetingNoticeCb);
				viewHolder.noticeDetialCountFL = (FrameLayout) convertView
						.findViewById(R.id.noticeDetialCountFL);
				viewHolder.countTv = (TextView) convertView
						.findViewById(R.id.countTv);
//				RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.image_gv_rl);
//				android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(DisplayUtil.dip2px(mContext, 54), DisplayUtil.dip2px(mContext, 54));
//				rl.setLayoutParams(layoutParams);
//				LayoutParams lp = new LayoutParams(DisplayUtil.dip2px(mContext, 50), DisplayUtil.dip2px(mContext, 50));
//				viewHolder.meetingIv.setLayoutParams(lp);
				
				convertView.setTag(viewHolder);
			}
			viewHolder = (ViewHolder) convertView.getTag();
			MMeetingMemberListQuery mMeetingMemberListQuery = meetingInvitationCardlist
					.get(position);
			final Long meetingId = mMeetingMemberListQuery.getId();
			if (mMeetingMemberListQuery != null) {
				viewHolder.meetingTitleTv.setTextColor(Color.rgb(0, 0, 0));
				viewHolder.meetingTitleTv.setText(mMeetingMemberListQuery
						.getMeetingName());
				viewHolder.meetingTimeTv.setText(TimeUtil
						.formartTimeWithOutWeek(mMeetingMemberListQuery
								.getStartTime()));
				if (options == null) {
					options = new DisplayImageOptions.Builder()
							.showImageOnLoading(
									R.drawable.meeting_invition_logo_a)
							.showImageForEmptyUri(
									R.drawable.meeting_invition_logo_a)
							.showImageOnFail(R.drawable.meeting_invition_logo_a)
							.cacheInMemory(true).cacheOnDisc(true)
							.considerExifParams(false).build();
				}
				if (animateFirstDisplayListener == null) {
					animateFirstDisplayListener = new AnimateFirstDisplayListener();
				}
				ImageLoader.getInstance().displayImage(
						mMeetingMemberListQuery.getPath(),
						viewHolder.meetingIv, options,
						animateFirstDisplayListener);
//				ImageLoader.load(viewHolder.meetingIv, mMeetingMemberListQuery.getPath(), R.drawable.meeting_invition_logo_b);
				if (TextUtils.isEmpty(mMeetingMemberListQuery.getMeetingDesc()))
					viewHolder.meetingOperateTv.setVisibility(View.GONE);
				else{
					viewHolder.meetingOperateTv.setVisibility(View.VISIBLE);
					viewHolder.meetingOperateTv.setTextColor(Color.rgb(0, 0, 0));
					viewHolder.meetingOperateTv.setText(mMeetingMemberListQuery.getMeetingDesc());
				}
				if (showChectBox) {
					viewHolder.meetingInviteCardCb.setVisibility(View.VISIBLE);
					if (!deleteInvitationCardMap.containsValue(meetingId)) {
						viewHolder.meetingInviteCardCb.setChecked(false);
					} else {
						viewHolder.meetingInviteCardCb.setChecked(true);
					}
				} else {
					viewHolder.meetingInviteCardCb.setVisibility(View.GONE);
				}
				viewHolder.meetingInviteCardCb
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (!deleteInvitationCardMap
										.containsValue(meetingId)) {
									deleteInvitationCardMap.put(position,
											meetingId);
								} else {
									deleteInvitationCardMap.remove(position);
								}
								notifyDataSetChanged();
							}
						});
			}
			return convertView;
		}
	}

	class ViewHolder {
		CircleImageView meetingIv;
		TextView meetingTitleTv;
		TextView meetingOperateTv;
		TextView meetingTimeTv;
		CheckBox meetingInviteCardCb;
		FrameLayout noticeDetialCountFL;
		TextView countTv;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_filter, menu);
		selectAll = menu.findItem(R.id.filter_select_all);
		cancleAll = menu.findItem(R.id.filter_reverse_selection);
		menu.findItem(R.id.filter_confirm_selection).setVisible(false);
		selectAll.setVisible(false);
		cancleAll.setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/** 全选——》取消全选 */
		case R.id.filter_select_all:
			showSelectOrCancleAllMenuItem(false);
			isChectAll = true;
			int position = 0;
			deleteInvitationCardMap.clear();
			for (MMeetingMemberListQuery meetingMemberListQuery : meetingMemberListQueries) {
				deleteInvitationCardMap.put(position,
						meetingMemberListQuery.getId());
				position++;
			}
			break;
		/** 取消全选——》全选 */
		case R.id.filter_reverse_selection:
			showSelectOrCancleAllMenuItem(true);
			isChectAll = false;
			deleteInvitationCardMap.clear();
			break;
		}
		adapter.notifyDataSetChanged();
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 长按显示出 邀请函删除选择按钮
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (showChectBox) {
			return false;
		}
		if (deleteInvitationCardMap == null) {
			deleteInvitationCardMap = new HashMap<Integer, Long>();
		} else {
			deleteInvitationCardMap.clear();
		}
		long meetingId = adapter.getItemId(position - 1);
		if (meetingId != 0) {
			deleteInvitationCardMap.put(position - 1, meetingId);
		}
		deleteButton.setVisibility(View.VISIBLE);
		lp.setMargins(0, 0, 0, DisplayUtil.dip2px(mContext, 50));
		listview.setLayoutParams(lp);
		showSelectOrCancleAllMenuItem(true);
		showChectBox = true;
		adapter.notifyDataSetChanged();
		return false;
	}

	/**
	 * 点击查看邀请函详情
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
			if (position != 0) {
				/** 会议删除标记 0：默认、1：删除 */
				if (adapter.getItem(position-1).getMeetingDeleteFlag() != 1) {
					long meetingId = adapter.getItemId(position - 1);
					if (meetingId == 0) {
						showToast("会议不存在");
						return;
					}
					/** 参会人删除标记 0：默认、1：删除 */
					if (adapter.getItem(position-1).getMemberDeleteFlag() != 1) {
						ENavigate.startSquareActivity(mContext, meetingId, 1);
					}
					// 参会人被请出, 显示普通的报名
					else {
						ENavigate.startSquareActivity(mContext, meetingId, 0);
					}
				} else {
					showToast("会议已删除");
				}

			}
	}

	/**
	 * 批量删除邀请函
	 */
	@Override
	public void onClick(View v) {// 删除
		StringBuffer sb = new StringBuffer();
		int i = 0;
		Iterator<Entry<Integer, Long>> iterator = deleteInvitationCardMap
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Long> entry = iterator.next();
			Long id = entry.getValue();
			if (id != 0) {
				if (i == deleteInvitationCardMap.size() - 1) {
					sb.append(id + "");
				} else {
					sb.append(id + ",");
				}
				i++;
				/** 暂时这样处理，后台接口支持批量删除后改回来 */
				ConferenceReqUtil.doDeleteMyInvitation(mContext, this, id + "",
						Long.valueOf(App.getUserID()), null);
				IMReqUtil.doclearUnreadMessageNumber(mContext, MeetingInvitationCardActivity.this, Long.valueOf(App.getUserID()), 0, 0, 4, null, false);
			}
		}
		if (!StringUtils.isEmpty(sb.toString())) {

		}
		/** 暂时这样处理，后台接口支持批量删除后改回来 */
		Iterator<Entry<Integer, Long>> iterator1 = deleteInvitationCardMap
				.entrySet().iterator();
		ArrayList<MMeetingMemberListQuery> temps = new ArrayList<MMeetingMemberListQuery>();
		while (iterator1.hasNext()) {
			Entry<Integer, Long> entry = iterator1.next();
			Integer position = entry.getKey();
			temps.add(meetingMemberListQueries.get(position.intValue()));
		}
		meetingMemberListQueries.removeAll(temps);
		deleteInvitationCardMap.clear();
		adapter.notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (object == null) {
			return;
		}
		switch (tag) {
		// 邀请函列表
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MY_INVITION:
			meetingMemberListQueries = (ArrayList<MMeetingMemberListQuery>) object;
			adapter.setData(meetingMemberListQueries);
			adapter.notifyDataSetChanged();
			break;
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_INVITION_CARD:
			boolean b = (Boolean) object;
			if (b) {
				// Iterator<Entry<Integer, Long>> iterator1 =
				// deleteInvitationCardMap
				// .entrySet().iterator();
				// ArrayList<MMeetingMemberListQuery> temps = new
				// ArrayList<MMeetingMemberListQuery>();
				// while (iterator1.hasNext()) {
				// Entry<Integer, Long> entry = iterator1.next();
				// Integer position = entry.getKey();
				// temps.add(meetingMemberListQueries.get(position.intValue()));
				// }
				// deleteInvitationCardMap.clear();
				// meetingMemberListQueries.removeAll(temps);
				// adapter.notifyDataSetChanged();
			}
			break;
		}
	}

	/**
	 * 取消和全选显隐
	 * 
	 * @param b
	 */
	private void showSelectOrCancleAllMenuItem(boolean b) {
		selectAll.setVisible(b);
		cancleAll.setVisible(!b);
	}

	@Override
	public void onBackPressed() {
		if (showChectBox) {
			selectAll.setVisible(false);
			cancleAll.setVisible(false);
			showChectBox = false;
			isChectAll = false;
			deleteButton.setVisibility(View.GONE);
			lp.setMargins(0, 0, 0, 0);
			listview.setLayoutParams(lp);
			adapter.notifyDataSetChanged();
			return;
		}
		super.onBackPressed();
	}
}
