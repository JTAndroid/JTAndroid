package com.tr.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import m.framework.utils.Utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.image.ImageLoader;
import com.tr.model.conference.MSociality;
import com.tr.ui.conference.home.MeetingNoticeActivity2;
import com.tr.ui.im.MeetListActivity;
import com.tr.ui.widgets.CircleImageView;
import com.utils.common.GlobalVariable;
import com.utils.common.Util;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;

public class MListSocialityAdapter extends BaseAdapter {

	private List<MSociality> listSocial;
	private Context context;
	private int newConnectionsCount;
	private int topIndex = 4;
	public boolean isPushNew = false;// 是否是推送来的新数据

	public int getNewConnectionsCount() {
		return newConnectionsCount;
	}

	public void setNewConnectionsCount(int newConnectionsCount) {
		this.newConnectionsCount = newConnectionsCount;
	}

	public List<MSociality> getListSocial() {
		return listSocial;
	}

	public void setListSocial(List<MSociality> listSocial) {
		this.listSocial.clear();
		this.listSocial.addAll(listSocial);
	}

	/**
	 * 推送添加数据
	 * 
	 * @param mSociality
	 */
	@SuppressWarnings("static-access")
	public List<MSociality> setListAddSocial(MSociality mSociality) {
		try {
			int index = -1;
			if (mSociality.getType() == MSociality.PRIVATE_CHAT || mSociality.getType() == MSociality.GROUP_CHAT) {
				for (int i = 0; i < listSocial.size(); i++) {// 单聊、群聊
					if (listSocial.get(i).getId() == mSociality.getId()) {// 推送的群聊或单聊原来的下标
						index = i;
						break;
					}
				}

				for (int i = 0; i < listSocial.size(); i++) {
					int listType = listSocial.get(i).getType();
					if (listType == MSociality.PRIVATE_CHAT || listType == MSociality.GROUP_CHAT) {// 寻找第一个群聊、单聊的下标
						topIndex = i;
						break;
					}
				}
			} else if (mSociality.getType() == MSociality.NEW_RELATIONSHIP)// 新关系
			{
				index = 0;
			}else if(mSociality.getType() == MSociality.COMMUNITY){//社群
				index = -1;
			} else if (mSociality.isMeeting(mSociality.getType()))// 会议
			{
				index = 2;
				// 写入数
				SharedPreferences mySharedPreferences = context.getSharedPreferences(GlobalVariable.SHARED_PREFERENCES_MEETING_NEW_COUNT, MeetListActivity.MODE_PRIVATE);
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				editor.putInt(GlobalVariable.MEETING_NEW_COUNT_KEY, listSocial.get(index).getNewCount() + 1);
				editor.commit();
			} else if (mSociality.getType() == MSociality.INVITATION) {// 邀请函
				index = 3;
			} else if (mSociality.getType() == MSociality.NOTICE) {// 通知
				index = 4;
				// 写入数
				SharedPreferences mySharedPreferences = context.getSharedPreferences(GlobalVariable.SHARED_PREFERENCES_NOTICE_NEW_COUNT, MeetingNoticeActivity2.MODE_PRIVATE);
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				editor.putInt(GlobalVariable.NOTICE_NEW_COUNT_KEY, listSocial.get(index).getNewCount() + 1);
				editor.commit();
			}
			if (index != -1) {
				int newCount = listSocial.get(index).getNewCount() + 1;
				if (listSocial.get(index).getSocialDetail() != null) {
					listSocial.get(index).getSocialDetail().setContent(mSociality.getSocialDetail().getContent());
					listSocial.get(index).getSocialDetail().setSenderID(mSociality.getSocialDetail().getSenderID());
					listSocial.get(index).getSocialDetail().setSenderName(mSociality.getSocialDetail().getSenderName());
				}
				listSocial.get(index).setNewCount(newCount);
				if (mSociality.getType() == MSociality.PRIVATE_CHAT || mSociality.getType() == MSociality.GROUP_CHAT) {// 单聊、群聊
					// 第四个是最新的群聊或者单聊
					listSocial = Util.getList(listSocial, index, topIndex);
				}
			} else {
				isPushNew = true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listSocial;
	}

	@SuppressWarnings("unused")
	public MListSocialityAdapter(Context context, List<MSociality> listSocial) {
		this.context = context;
		this.listSocial = new ArrayList<MSociality>();
		this.listSocial.clear();
		this.listSocial.addAll(listSocial);
	}

	@Override
	public int getCount() {
		return listSocial.size();// 默认第一个item是新的关系
	}

	@Override
	public Object getItem(int position) {
		return listSocial.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// "type":"1-私聊，2-群聊，3-进行中的会议，4-未开始，5-已结束的会议，6-通知，7-邀请函",
		Holder holder = null;

		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.activity_frgsociality, null);

			holder.im_new_contactlist = (RelativeLayout) convertView.findViewById(R.id.im_new_contactlist);
			holder.cnsSizeTvNew = (TextView) convertView.findViewById(R.id.cnsSizeTvNew);
			holder.cnsSizeTvNew_control = (FrameLayout) convertView.findViewById(R.id.cnsSizeTvNew_control);
			holder.tv_newcontact = (TextView) convertView.findViewById(R.id.tv_newcontact);
			holder.iv_newcontact_bg = (CircleImageView) convertView.findViewById(R.id.iv_newcontact_bg);
			
			// 畅聊布局
			holder.layout_item = (LinearLayout) convertView.findViewById(R.id.layout_item);
			holder.chat = (RelativeLayout) convertView.findViewById(R.id.chat);
			holder.conference = (RelativeLayout) convertView.findViewById(R.id.conference);
			holder.notification = (RelativeLayout) convertView.findViewById(R.id.notification);
			holder.invitation = (RelativeLayout) convertView.findViewById(R.id.invitation);
			holder.image_rl = (RelativeLayout) convertView.findViewById(R.id.image_rl);
			holder.image_gv_rl = (RelativeLayout) convertView.findViewById(R.id.image_gv_rl);
			holder.chat_sociality_iv = (CircleImageView) convertView.findViewById(R.id.sociality_iv);
			holder.chat_name = (TextView) convertView.findViewById(R.id.chat_name);
			holder.chat_time = (TextView) convertView.findViewById(R.id.chat_time);
			holder.the_last_time_of_chat = (TextView) convertView.findViewById(R.id.the_last_time_of_chat);
			holder.sociality_gv = (GridView) convertView.findViewById(R.id.sociality_gv);
			holder.chat_push_data_num = (TextView) convertView.findViewById(R.id.chat_push_data_num);
			holder.chat_push_data_num_gv = (TextView) convertView.findViewById(R.id.chat_push_data_num_gv);
			holder.chat_push_data_num_control = (FrameLayout) convertView.findViewById(R.id.chat_push_data_num_control);
			holder.chat_push_data_num_gv_control = (FrameLayout) convertView.findViewById(R.id.chat_push_data_num_gv_control);

			// 会议布局
			holder.conference_sociality_iv = (ImageView) convertView.findViewById(R.id.conference_sociality_iv);
			holder.conference_status = (TextView) convertView.findViewById(R.id.conference_status);
			holder.conference_status.setVisibility(View.GONE);
			holder.conference_name = (TextView) convertView.findViewById(R.id.conference_name);
			holder.conference_time = (TextView) convertView.findViewById(R.id.conference_time);
			holder.the_last_time_of_conference = (TextView) convertView.findViewById(R.id.the_last_time_of_conference);
			holder.conference_push_data_num = (TextView) convertView.findViewById(R.id.conference_push_data_num);
			holder.conference_push_data_num_control = (FrameLayout) convertView.findViewById(R.id.conference_push_data_num_control);

			// 通知
			holder.notification_sociality_iv = (ImageView) convertView.findViewById(R.id.sociality_iv);
			holder.notification_name = (TextView) convertView.findViewById(R.id.notification_name);
			holder.notification_time = (TextView) convertView.findViewById(R.id.notification_time);
			holder.the_last_time_of_notification = (TextView) convertView.findViewById(R.id.the_last_time_of_notification);
			holder.notification_push_data_num = (TextView) convertView.findViewById(R.id.notification_push_data_num);
			holder.notification_push_data_num_control = (FrameLayout) convertView.findViewById(R.id.notification_push_data_num_control);

			// 邀请函
			holder.invitation_sociality_iv = (ImageView) convertView.findViewById(R.id.sociality_iv);
			holder.invitation_name = (TextView) convertView.findViewById(R.id.invitation_name);
			holder.invitation_time = (TextView) convertView.findViewById(R.id.invitation_time);
			holder.the_last_time_of_invitation = (TextView) convertView.findViewById(R.id.the_last_time_of_invitation);
			holder.invitation_push_data_num = (TextView) convertView.findViewById(R.id.invitation_push_data_num);
			holder.invitation_push_data_num_control = (FrameLayout) convertView.findViewById(R.id.invitation_push_data_num_control);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		MSociality mSociality = listSocial.get(position);
		/*
		 * "1-私聊，2-群聊，3-进行中的会议，4-未开始，5-已结束的会议，6-通知，7-邀请函，8-新的关系，9-社群消息",
		 * http://192.168.101.131:8000/newFrame/Object.html#social
		 */
		
		if (mSociality.getType() == MSociality.NEW_RELATIONSHIP) {// listview头条放新关系
			holder.layout_item.setVisibility(View.GONE);
			holder.im_new_contactlist.setVisibility(View.VISIBLE);
			holder.iv_newcontact_bg.setImageResource(R.drawable.im_relation_new_contact);
			holder.tv_newcontact.setText("新的关系");
			if (mSociality.getNewCount() == 0) {
				holder.cnsSizeTvNew_control.setVisibility(View.GONE);
				holder.cnsSizeTvNew.setVisibility(View.GONE);
			} else {
				holder.cnsSizeTvNew_control.setVisibility(View.VISIBLE);
				holder.cnsSizeTvNew.setVisibility(View.VISIBLE);
				holder.cnsSizeTvNew.setText(String.valueOf(mSociality.getNewCount()));
			}
		}
		
		if (mSociality.getType() == MSociality.COMMUNITY) {// 社群消息
			holder.layout_item.setVisibility(View.GONE);
			holder.im_new_contactlist.setVisibility(View.VISIBLE);
			holder.iv_newcontact_bg.setImageResource(R.drawable.social_community);
			holder.tv_newcontact.setText("社群消息");
			if (mSociality.getNewCount() == 0) {
				holder.cnsSizeTvNew_control.setVisibility(View.GONE);
				holder.cnsSizeTvNew.setVisibility(View.GONE);
			} else {
				holder.cnsSizeTvNew_control.setVisibility(View.VISIBLE);
				holder.cnsSizeTvNew.setVisibility(View.VISIBLE);
				holder.cnsSizeTvNew.setText(String.valueOf(mSociality.getNewCount()));
			}
		}
		
		/* 3-进行中的会议，4-未开始，5-已结束的会议 0-没有会议 */
		if (MSociality.isMeeting(mSociality.getType())) {// listview第二条放会议
			holder.layout_item.setVisibility(View.VISIBLE);
			holder.im_new_contactlist.setVisibility(View.GONE);

			holder.chat.setVisibility(View.GONE);
			holder.conference.setVisibility(View.VISIBLE);
			holder.notification.setVisibility(View.GONE);
			holder.invitation.setVisibility(View.GONE);

			if (!StringUtils.isEmpty(mSociality.getTitle())) {
				holder.conference_name.setText(mSociality.getTitle());
			} else {
				holder.conference_name.setText("会议");
			}
			if (!StringUtils.isEmpty(mSociality.getCompereName())) {
				holder.conference_time.setText("发起人：" + mSociality.getCompereName());
			} else {
				holder.conference_time.setText("暂无内容");
			}
			if (!StringUtils.isEmpty(mSociality.getTime())) {
				holder.the_last_time_of_conference.setText(TimeUtil.TimeFormat(mSociality.getTime()));
			} else {
				holder.the_last_time_of_conference.setText("");
			}
			// holder.conference_status.setVisibility(View.GONE);
			int newCount = listSocial.get(position).getNewCount();
			if (newCount > 0) {
				holder.conference_push_data_num_control.setVisibility(View.VISIBLE);
				holder.conference_push_data_num.setText((newCount > 99 ? 99 : newCount) + "");
			} else {
				holder.conference_push_data_num_control.setVisibility(View.GONE);
			}
		}
		if (mSociality.getType() == MSociality.INVITATION) {// listview第三条放邀请函

			holder.layout_item.setVisibility(View.VISIBLE);
			holder.im_new_contactlist.setVisibility(View.GONE);

			holder.chat.setVisibility(View.GONE);
			holder.conference.setVisibility(View.GONE);
			holder.notification.setVisibility(View.GONE);
			holder.invitation.setVisibility(View.VISIBLE);

			if (!StringUtils.isEmpty(mSociality.getTitle())) {
				holder.invitation_name.setText(mSociality.getTitle());
			} else {
				holder.invitation_name.setText("邀请函");
			}
			if (mSociality.getSocialDetail() != null && !StringUtils.isEmpty(mSociality.getSocialDetail().getContent())) {
				holder.invitation_time.setText(mSociality.getSocialDetail().getContent());
			} else {
				holder.invitation_time.setText("暂无内容");
			}
			if (!StringUtils.isEmpty(mSociality.getTime())) {
				holder.the_last_time_of_invitation.setText(TimeUtil.TimeFormat(mSociality.getTime()));
			} else {
				holder.the_last_time_of_invitation.setText("");
			}
			if (mSociality.isDelete()) {
				holder.invitation.setVisibility(View.GONE);
			} else {
				holder.invitation.setVisibility(View.VISIBLE);
			}
			int newCount = listSocial.get(position).getNewCount();
			if (newCount > 0) {
				holder.invitation_push_data_num_control.setVisibility(View.VISIBLE);
				holder.invitation_push_data_num.setText((newCount > 99 ? 99 : newCount) + "");
			} else {
				holder.invitation_push_data_num_control.setVisibility(View.GONE);
			}
		}
		if (mSociality.getType() == MSociality.NOTICE) {// 通知
			holder.layout_item.setVisibility(View.VISIBLE);
			holder.im_new_contactlist.setVisibility(View.GONE);

			holder.chat.setVisibility(View.GONE);
			holder.conference.setVisibility(View.GONE);
			holder.invitation.setVisibility(View.GONE);
			holder.notification.setVisibility(View.VISIBLE);

			if (!StringUtils.isEmpty(mSociality.getTitle())) {
				holder.notification_name.setText(mSociality.getTitle());
			} else {
				holder.notification_name.setText("通知");
			}
			if (mSociality.getSocialDetail() != null && !StringUtils.isEmpty(mSociality.getSocialDetail().getContent())) {
				holder.notification_time.setText(mSociality.getSocialDetail().getContent());
			} else {
				holder.notification_time.setText("暂无内容");
			}
			if (!StringUtils.isEmpty(mSociality.getTime())) {
				holder.the_last_time_of_notification.setText(TimeUtil.TimeFormat(mSociality.getTime()));
			} else {
				holder.the_last_time_of_notification.setText("");
			}
			if (mSociality.isDelete()) {
				holder.notification.setVisibility(View.GONE);
			} else {
				holder.notification.setVisibility(View.VISIBLE);
			}
			int newCount = listSocial.get(position).getNewCount();
			if (newCount > 0) {
				holder.notification_push_data_num_control.setVisibility(View.VISIBLE);
				holder.notification_push_data_num.setText((newCount > 99 ? 99 : newCount) + "");
			} else {
				holder.notification_push_data_num_control.setVisibility(View.GONE);
			}
		}
		if (mSociality.getType() == MSociality.PRIVATE_CHAT || mSociality.getType() == MSociality.GROUP_CHAT) {
			holder.layout_item.setVisibility(View.VISIBLE);
			holder.im_new_contactlist.setVisibility(View.GONE);
			initItemData(holder, position);
		}

		return convertView;
	}

	/**
	 * 群聊 、 单聊
	 * 
	 * @param holder
	 * @param position
	 */
	private void initItemData(Holder holder, int position) {
		/* clearHolderCache(holder); */
		switch (listSocial.get(position).getType()) {
		// 私聊+群聊
		case MSociality.PRIVATE_CHAT:
		case MSociality.GROUP_CHAT:
			holder.conference.setVisibility(View.GONE);
			holder.notification.setVisibility(View.GONE);
			holder.invitation.setVisibility(View.GONE);
			// 私聊
			if (listSocial.get(position).getType() == 1) {
				holder.image_rl.setVisibility(View.VISIBLE);
				holder.image_gv_rl.setVisibility(View.GONE);

				if (listSocial.get(position).getSocialDetail().getListImageUrl() != null && !StringUtils.isEmpty(listSocial.get(position).getSocialDetail().getListImageUrl().get(0))) {
					ImageLoader.setContext(context);
					ImageLoader.load(holder.chat_sociality_iv, ImageLoader.CHAT_BITMAP, listSocial.get(position).getSocialDetail().getListImageUrl().get(0), R.drawable.chat_im_img_user);
				} else {
					// 显示默认图片
					holder.chat_sociality_iv.setImageResource(R.drawable.chat_im_img_user);
				}
				if (!StringUtils.isEmpty(listSocial.get(position).getTitle())) {
					holder.chat_name.setText(listSocial.get(position).getTitle());
				} else {
					holder.chat_name.setText("");
				}
				if (listSocial.get(position).getNewCount() > 0) {
					holder.chat_push_data_num_control.setVisibility(View.VISIBLE);
					holder.chat_push_data_num.setText((listSocial.get(position).getNewCount() > 99 ? 99 : listSocial.get(position).getNewCount()) + "");
				} else {
					holder.chat_push_data_num_control.setVisibility(View.GONE);
				}
			}
			// 群聊
			else if (listSocial.get(position).getType() == 2) {
				holder.image_gv_rl.setVisibility(View.VISIBLE);
				holder.image_rl.setVisibility(View.GONE);
				if (listSocial != null && !listSocial.isEmpty()) {

					if (listSocial.get(position).getSocialDetail().getListImageUrl() != null && !listSocial.get(position).getSocialDetail().getListImageUrl().isEmpty()) {
						// ImageLoader.getInstance().displayImage(listSocial.get(position).getSocialDetail().getListImageUrl().get(0),
						// chatHolder.sociality_iv);
						holder.sociality_gv.setClickable(false);
						holder.sociality_gv.setPressed(false);
						holder.sociality_gv.setEnabled(false);
						holder.sociality_gv.setFocusable(false);
						holder.sociality_gv.setFocusableInTouchMode(false);
						holder.sociality_gv.setAdapter(new GridViewAdapter(listSocial.get(position).getSocialDetail().getListImageUrl()));
					} else {
						// 显示默认图片
						holder.chat_sociality_iv.setImageResource(R.drawable.chat_im_img_user);
					}
					if (!StringUtils.isEmpty(listSocial.get(position).getTitle())) {
						holder.chat_name.setText(listSocial.get(position).getTitle());
					} else {
						holder.chat_name.setText("");
					}
				}
				if (listSocial.get(position).getNewCount() > 0) {
					holder.chat_push_data_num_gv_control.setVisibility(View.VISIBLE);
					holder.chat_push_data_num_gv.setText((listSocial.get(position).getNewCount() > 99 ? 99 : listSocial.get(position).getNewCount()) + "");
				} else {
					holder.chat_push_data_num_gv_control.setVisibility(View.GONE);
				}
			}
			// 判断当前是否有人@
			if (listSocial.get(position).isAtVisible()) {
				holder.chat_time.setTextColor(context.getResources().getColor(R.color.im_content_red));
				holder.chat_time.setText("[" + listSocial.get(position).getAtName() + "@了我]");
			}
			// 没人@ 正常显示内容
			else if (!StringUtils.isEmpty(listSocial.get(position).getSocialDetail().getContent())) {
				holder.chat_time.setTextColor(context.getResources().getColor(R.color.hy_input_content_text_color));
				holder.chat_time.setText(listSocial.get(position).getSocialDetail().getContent());
			} else {
				holder.chat_time.setText("");
			}
			if (!StringUtils.isEmpty(listSocial.get(position).getTime())) {
				holder.the_last_time_of_chat.setText(TimeUtil.TimeFormat(listSocial.get(position) != null ? listSocial.get(position).getTime() : ""));
			} else {
				holder.the_last_time_of_chat.setText("");
			}
			if (listSocial.get(position).isDelete()) {
				holder.chat.setVisibility(View.GONE);
			} else {
				holder.chat.setVisibility(View.VISIBLE);
			}
			break;
		}
	}

	public class Holder {

		private LinearLayout layout_item;
		

		private RelativeLayout im_new_contactlist;// 新关系
		public TextView cnsSizeTvNew;// 新关系数目
		public FrameLayout cnsSizeTvNew_control;
		public TextView tv_newcontact;
		public CircleImageView iv_newcontact_bg;
		
		private RelativeLayout chat;
		private RelativeLayout conference;
		private RelativeLayout notification;
		private RelativeLayout invitation;

		private CircleImageView chat_sociality_iv;
		private TextView chat_time;
		private TextView chat_name;
		private TextView the_last_time_of_chat;
		private GridView sociality_gv;
		private TextView chat_push_data_num;
		private TextView chat_push_data_num_gv;
		private RelativeLayout image_rl;
		private RelativeLayout image_gv_rl;
		public FrameLayout chat_push_data_num_control;
		public FrameLayout chat_push_data_num_gv_control;

		private ImageView conference_sociality_iv;
		private TextView conference_status;
		private TextView conference_name;
		private TextView conference_time;
		private TextView the_last_time_of_conference;
		private TextView conference_push_data_num;
		private FrameLayout conference_push_data_num_control;

		private ImageView notification_sociality_iv;
		private TextView notification_time;
		private TextView notification_name;
		private TextView the_last_time_of_notification;
		private TextView notification_push_data_num;
		private FrameLayout notification_push_data_num_control;

		private ImageView invitation_sociality_iv;
		private TextView invitation_time;
		private TextView invitation_name;
		private TextView the_last_time_of_invitation;
		private TextView invitation_push_data_num;
		private FrameLayout invitation_push_data_num_control;
	}

	private class GridViewAdapter extends BaseAdapter {
		List<String> imgs = null;

		private GridViewAdapter(List<String> imgs) {
			this.imgs = imgs;
		}

		@Override
		public int getCount() {
			return imgs.size() > 4 ? 4 : imgs.size();
		}

		@Override
		public Object getItem(int position) {
			return imgs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CircleImageView view = new CircleImageView(context);
			ListView.LayoutParams params = new ListView.LayoutParams(Utils.dipToPx(context, 19), Utils.dipToPx(context, 19));
			view.setLayoutParams(params);
			try {
				if (!StringUtils.isEmpty(imgs.get(position))) {
					// ImageLoader.getInstance().displayImage(imgs.get(position),
					// view, LoadImage.mDefaultHead);
					// ImageLoader.load(view, imgs.get(position),
					// R.drawable.ic_default_avatar);
					ImageLoader.setContext(context);
					ImageLoader.load(view, ImageLoader.MUCCHAT_BITMAP, imgs.get(position), R.drawable.chat_im_img_user);
				} else {
					view.setImageResource(R.drawable.chat_im_img_user);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}
	}

}
