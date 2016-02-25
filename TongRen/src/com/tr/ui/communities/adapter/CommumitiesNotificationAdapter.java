package com.tr.ui.communities.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.ui.communities.model.CommunityNotify;
import com.tr.ui.widgets.title.menu.popupwindow.ViewHolder;

public class CommumitiesNotificationAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<CommunityNotify> communityNotifylist = new ArrayList<CommunityNotify>();
	private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_default_avatar) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.ic_default_avatar) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.ic_default_avatar) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.build();

	public CommumitiesNotificationAdapter(Context context) {
		this.mContext = context;
	}
	
	public void setData(ArrayList<CommunityNotify> communityNotifylist){
		this.communityNotifylist = communityNotifylist;
	}

	@Override
	public int getCount() {
		return communityNotifylist.size();
	}

	@Override
	public CommunityNotify getItem(int position) {
		return communityNotifylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommunityNotify communityNotify = getItem(position);
		if (convertView == null)
			convertView = View.inflate(mContext, R.layout.adapter_commumitiesnotification, null);
		ImageView notice_item_image = ViewHolder.get(convertView, R.id.notice_item_image);// 头像
		TextView notice_item_name = ViewHolder.get(convertView, R.id.notice_item_name);// 名字
		
		LinearLayout layout_apply_invite = ViewHolder.get(convertView, R.id.layout_apply_invite);// <!-- 申请或邀请  通过或拒绝 -->
		TextView notice_attendType = ViewHolder.get(convertView, R.id.notice_attendType);//加入方式
		TextView notice_item_notice = ViewHolder.get(convertView, R.id.notice_item_notice);//加入群名称
		
		LinearLayout layout_transfer = ViewHolder.get(convertView, R.id.layout_transfer);// <!--转让群 -->
		TextView notice_item_transfer = ViewHolder.get(convertView, R.id.notice_item_transfer);//转让者
		
		LinearLayout layout_transfer_refuse = ViewHolder.get(convertView, R.id.layout_transfer_refuse);// <!--转让群 回复状态-->
		TextView notice_item_transfer_refuse = ViewHolder.get(convertView, R.id.notice_item_transfer);//转让者
		TextView notice_item_notice_refuse = ViewHolder.get(convertView, R.id.notice_item_transfer);//转让通过或拒绝
		
		
		TextView notice_applyReason = ViewHolder.get(convertView, R.id.notice_applyReason);//申请理由
		
		LinearLayout ownerll = ViewHolder.get(convertView, R.id.ownerll);//群主显示忽略、同意布局
		TextView notice_item_refuse = ViewHolder.get(convertView, R.id.notice_item_refuse);// 忽略
		TextView notice_item_agree = ViewHolder.get(convertView, R.id.notice_item_agree);// 同意
		
		TextView notice_item_into = ViewHolder.get(convertView, R.id.notice_item_into);// 进入聊天
		
		ownerll.setVisibility(View.GONE);
		notice_item_into.setVisibility(View.GONE);
		notice_applyReason.setVisibility(View.GONE);
		
		switch(communityNotify.getNoticeType()){
		case 0://加群
			
			switch(communityNotify.getAttendType()){
			case 0://邀请加入
				notice_attendType.setText("邀请您加入");
				break;
			case 1://申请加入
				layout_apply_invite.setVisibility(View.VISIBLE);
				layout_transfer.setVisibility(View.GONE);
				layout_transfer_refuse.setVisibility(View.GONE);
				switch(communityNotify.getAcceptStatus()){
				case 0://未答复
					ImageLoader.getInstance().displayImage(communityNotify.getUserLogo(), notice_item_image,options);
					notice_item_name.setText(communityNotify.getApplicantName());
					notice_attendType.setText("申请加入");
					notice_item_notice.setText(communityNotify.getCommunityName());
					
					if(!TextUtils.isEmpty(communityNotify.getApplyReason())){
						notice_applyReason.setVisibility(View.VISIBLE);
						SpannableString spanusername = new SpannableString("加群理由："+communityNotify.getApplyReason());
						spanusername.setSpan(new ForegroundColorSpan(0xffb6b6b6), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						notice_applyReason.setText(spanusername);
					}
					ownerll.setVisibility(View.VISIBLE);
					break;
				case 1://接收
					ImageLoader.getInstance().displayImage(communityNotify.getCreatedUserLogo(), notice_item_image,options);
					notice_item_name.setText(communityNotify.getCommunityName());
					notice_attendType.setText("您的加入申请");
					notice_item_notice.setText("已通过");
					notice_item_notice.setTextColor(mContext.getResources().getColor(R.color.text_flow_content));
					notice_item_into.setVisibility(View.VISIBLE);
					break;
				case 2://拒绝
					ImageLoader.getInstance().displayImage(communityNotify.getCreatedUserLogo(), notice_item_image,options);
					notice_item_name.setText(communityNotify.getCommunityName());
					notice_attendType.setText("您的加入申请");
					notice_item_notice.setText("被拒绝");
					notice_item_notice.setTextColor(mContext.getResources().getColor(R.color.gintongyellow));
					break;
				}
				break;
			}
			break;
		case 1://转让
			ImageLoader.getInstance().displayImage(communityNotify.getCreatedUserLogo(), notice_item_image,options);
			notice_item_name.setText(communityNotify.getCommunityName());
			switch(communityNotify.getAcceptStatus()){
			case 0://未答复
				layout_apply_invite.setVisibility(View.GONE);
				layout_transfer.setVisibility(View.VISIBLE);
				layout_transfer_refuse.setVisibility(View.GONE);
				notice_item_transfer.setText(communityNotify.getApplicantName());
				ownerll.setVisibility(View.VISIBLE);
				break;
			case 1://接收
				layout_apply_invite.setVisibility(View.GONE);
				layout_transfer.setVisibility(View.GONE);
				layout_transfer_refuse.setVisibility(View.VISIBLE);
				notice_item_transfer_refuse.setText(communityNotify.getApplicantName());
				notice_item_notice_refuse.setText("已通过");
				notice_item_notice_refuse.setTextColor(mContext.getResources().getColor(R.color.text_flow_content));
				break;
			case 2://拒绝
				layout_apply_invite.setVisibility(View.GONE);
				layout_transfer.setVisibility(View.GONE);
				layout_transfer_refuse.setVisibility(View.VISIBLE);
				notice_item_transfer_refuse.setText(communityNotify.getApplicantName());
				notice_item_notice_refuse.setText("被拒绝");
				notice_item_notice_refuse.setTextColor(mContext.getResources().getColor(R.color.gintongyellow));
				break;
			}
			break;
		case 2://禁言
			break;
		}
		
		return convertView;
	}

}
