package com.tr.ui.adapter.conference;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.baidumapsdk.BaiduNavi;
import com.tr.model.conference.MMeetingMemberListQuery;

public class ListMeetingHomeTransmitAdapter extends BaseAdapter {

	/**
	 * sunjiananan 20141117 copy from dc
	 */
	private Context mContext;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader = null;
	private ArrayList<MMeetingMemberListQuery> mMeetingHomeList = null;

	private String type_meeting_tile = "0";
	private String type_invition_tile = "1";
	private String type_notice_tile = "2";

	private String type_fold_toolbar = "1";
	private String type_expand_toolbar = "2";

	private String type_notstart_meeting = "1";
	private String type_togoing_meeting = "2";
	private String type_terminal_meeting = "3";

	private BaiduNavi mBaiduNavi = null;

	public ListMeetingHomeTransmitAdapter(Context context) {
		mContext = context;
		mMeetingHomeList = new ArrayList<MMeetingMemberListQuery>();
		mImageLoader = ImageLoader.getInstance();
		mBaiduNavi = new BaiduNavi((Activity) context);
	}

	public void updateList(ArrayList<MMeetingMemberListQuery> arrayList) {

		if (null == arrayList) {
			return;
		}

		int iCount = arrayList.size();
		if (0 == iCount) {
			return;
		}
		mMeetingHomeList.clear();
		for(int i = 0;i<iCount;++i){
			MMeetingMemberListQuery aMeeting = arrayList.get(i);
			if(aMeeting.getType().equals(type_meeting_tile)){
				mMeetingHomeList.add(aMeeting);
			}
		
		}
		//mMeetingHomeList.addAll(arrayList);
		if (mMeetingHomeList.size() > 0) {
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		if (null != mMeetingHomeList) {
			return mMeetingHomeList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (null != mMeetingHomeList) {
			if (position >= 0 && position < mMeetingHomeList.size()) {
				return mMeetingHomeList.get(position);
			}
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder;
		if (null == mInflater) {
			mInflater = LayoutInflater.from(mContext);
		}
		if (convertView == null) {
			holder = new viewHolder();
			convertView = mInflater.inflate(R.layout.hy_list_item_meeting_tile,
					null);
			holder.rlHeader = (RelativeLayout) convertView
					.findViewById(R.id.hy_meeting_tile_ll_time);
			holder.tvSession = (TextView) convertView
					.findViewById(R.id.hy_meeting_tile_tv_session);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.hy_meeting_tile_tv_time);
			holder.tvMeetingStatus = (TextView) convertView
					.findViewById(R.id.hy_meeting_tile_tv_status);
			holder.ivLogo = (ImageView) convertView
					.findViewById(R.id.hy_meeting_tile_iv_logo);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.hy_meeting_tile_tv_name);
			holder.tvDesc = (TextView) convertView
					.findViewById(R.id.hy_meeting_tile_tv_desc);
			holder.tvNumber = (TextView) convertView
					.findViewById(R.id.hy_meeting_tile_tv_counter);
			holder.rlPull = (RelativeLayout) convertView
					.findViewById(R.id.hy_meeting_tile_ll_arrow);
			holder.llPull = (LinearLayout) convertView
					.findViewById(R.id.hy_meeting_tile_ll_actions);
			holder.llPull.setTag("1");
			holder.rlPull.setTag(holder.llPull);
			holder.rlnavigation = (RelativeLayout) convertView
					.findViewById(R.id.hy_home_tile_meeting_rl_navigation);
			holder.rlNote = (RelativeLayout) convertView
					.findViewById(R.id.hy_home_tile_meeting_rl_note);
			holder.rlAlarm = (RelativeLayout) convertView
					.findViewById(R.id.hy_home_tile_meeting_rl_alarm);
			holder.rlCancel = (RelativeLayout) convertView
					.findViewById(R.id.hy_home_tile_meeting_rl_cancel);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		if (position >= 0 && position < mMeetingHomeList.size()) {
			holder.llPull.setVisibility(View.GONE);
			holder.tvNumber.setVisibility(View.GONE);
			final MMeetingMemberListQuery aMeeting = mMeetingHomeList.get(position);
			if (null != aMeeting) {
				if (aMeeting.getType().equals(type_meeting_tile)) {
					holder.tvTime.setText(aMeeting.getStartTime());
					if (aMeeting.getMeetingStatus().toString()
							.equals(type_notstart_meeting)) {
						holder.tvMeetingStatus.setText("未开始");
						holder.rlnavigation.setVisibility(View.VISIBLE);
						holder.rlNote.setVisibility(View.VISIBLE);
						holder.rlAlarm.setVisibility(View.VISIBLE);
						holder.rlCancel.setVisibility(View.VISIBLE);
					} else if (aMeeting.getMeetingStatus().equals(
							type_togoing_meeting)) {
						holder.tvMeetingStatus.setText("会议中");
						holder.rlnavigation.setVisibility(View.VISIBLE);
						holder.rlNote.setVisibility(View.VISIBLE);
						holder.rlAlarm.setVisibility(View.VISIBLE);
						holder.rlCancel.setVisibility(View.GONE);
					} else if (aMeeting.getMeetingStatus().equals(
							type_terminal_meeting)) {
						holder.tvMeetingStatus.setText("已结束");
						holder.rlnavigation.setVisibility(View.GONE);
						holder.rlNote.setVisibility(View.VISIBLE);
						holder.rlAlarm.setVisibility(View.GONE);
						holder.rlCancel.setVisibility(View.GONE);
					}
					if (aMeeting.getCreateImage().isEmpty()) {
						holder.ivLogo.setImageResource(R.drawable.hy_home_tile_meeting_default);
					} else {
						if (null != mImageLoader) {
							holder.ivLogo.setImageResource(R.drawable.hy_home_tile_meeting_default);
							mImageLoader.displayImage(aMeeting.getCreateImage(), holder.ivLogo);
						}
					}
					holder.tvTitle.setText(aMeeting.getMeetingName());
					holder.tvDesc.setText(aMeeting.getMeetingDesc());

					holder.rlHeader.setVisibility(View.VISIBLE);
					holder.tvSession.setVisibility(View.VISIBLE);
					
					holder.rlPull.setVisibility(View.GONE);
					holder.rlnavigation.setVisibility(View.GONE);
					holder.rlNote.setVisibility(View.GONE);
					holder.rlAlarm.setVisibility(View.GONE);
					holder.rlCancel.setVisibility(View.GONE);
					
					
					

					convertView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO 弹出dialog 说点什么
							final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
							RelativeLayout dialogView = (RelativeLayout) View.inflate(mContext,R.layout.hy_dialog_meeting_regist_transmit,null);
							EditText content = (EditText) dialogView.findViewById(R.id.content_et);
							((Button) dialogView.findViewById(R.id.cancel_btn)).setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();
										}
									});
							((Button) dialogView.findViewById(R.id.confirm_btn)).setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO 确定键
											Toast.makeText(mContext, "分享成功", 0).show();
											dialog.dismiss();
										}
									});

							dialog.setView(dialogView, 0, 0, 0, 0);
							dialog.show();
						}
					});
				} 
			}
		}
		return convertView;
	}

	private class viewHolder {
		private RelativeLayout rlHeader;
		private TextView tvSession;;
		private TextView tvTime;
		private TextView tvMeetingStatus;
		private ImageView ivLogo;
		private TextView tvTitle;
		private TextView tvDesc;
		private TextView tvNumber;
		private RelativeLayout rlPull;
		private LinearLayout llPull;
		private RelativeLayout rlnavigation;
		private RelativeLayout rlNote;
		private RelativeLayout rlAlarm;
		private RelativeLayout rlCancel;
	}
}
