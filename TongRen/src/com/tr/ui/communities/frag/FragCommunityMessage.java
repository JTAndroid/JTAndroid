package com.tr.ui.communities.frag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.communities.home.CommumitiesNotificationActivity;
import com.tr.ui.communities.home.CommunitiesDetailsActivity;
import com.tr.ui.communities.im.CommunityChatActivity;
import com.tr.ui.communities.model.CommunityNotify;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.communities.model.Notification;
import com.tr.ui.widgets.title.menu.popupwindow.ViewHolder;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class FragCommunityMessage extends JBaseFragment implements IBindData{
	
	private ArrayList<CommunityNotify> communityNotifylist;
	private CommumitiesNotificationAdapter adapter;
	private XListView commumitiesnotificationLv;
	private TextView NoContent;
	private CommunityNotify mCommunityNotify;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		communityNotifylist = (ArrayList<CommunityNotify>) getArguments().get("communityNotifylist");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_commumitiesnotification, null);
		initView(view);
		initData();
		return view;

	}
	
	private void initView(View view) {
		NoContent = (TextView) view.findViewById(R.id.NoContent);
		commumitiesnotificationLv = (XListView) view.findViewById(R.id.commumitiesnotificationLv);
		commumitiesnotificationLv.setPullLoadEnable(false);
		commumitiesnotificationLv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				CommunityReqUtil.doGetCommunityListByUserId(getActivity(), FragCommunityMessage.this, App.getUserID(), null);
			}
			
			@Override
			public void onLoadMore() {
				
			}
		});
	}
	
	private void initData() {
		adapter = new CommumitiesNotificationAdapter(getActivity());
		if(communityNotifylist!=null){
			if(communityNotifylist.size()>0){
				NoContent.setVisibility(View.GONE);
				adapter.setData(communityNotifylist);
			}else{
				CommunityReqUtil.doGetCommunityListByUserId(getActivity(), FragCommunityMessage.this, App.getUserID(), null);
			}
		}else{
			CommunityReqUtil.doGetCommunityListByUserId(getActivity(), FragCommunityMessage.this, App.getUserID(), null);
		}
		commumitiesnotificationLv.setAdapter(adapter);
	}
	
	@Override
	public void bindData(int tag, Object object) {
		commumitiesnotificationLv.stopRefresh();
		switch(tag){
		case EAPIConsts.CommunityReqType.TYPE_GET_NOTICE_LIST_BY_USERID:
			if (object != null) {
				HashMap<String, Object> dataBox = (HashMap<String, Object>) object;
				communityNotifylist = (ArrayList<CommunityNotify>) dataBox.get("list");
				if(communityNotifylist!=null){
					if(communityNotifylist.size()>0){
						Collections.sort(communityNotifylist, new ComparatorNotify());//排序
						NoContent.setVisibility(View.GONE);
						adapter.setData(communityNotifylist);
						adapter.notifyDataSetChanged();
					}else{
						NoContent.setVisibility(View.VISIBLE);
					}
				}else{
					NoContent.setVisibility(View.VISIBLE);
				}
			}
			break;
		case EAPIConsts.CommunityReqType.TYPE_HANDLE_APPLY:
			if(object != null){
				HashMap<String, Object> dataBox = (HashMap<String, Object>) object;
				Notification notif = (Notification) dataBox.get("notification");
				if(notif.getNotifCode().equals("0001")){
//					communityNotifylist.get(mIndex).setAcceptStatus(acceptStatus);
					adapter.notifyDataSetChanged();
				}
			}
			break;
		case EAPIConsts.CommunityReqType.TYPE_ASSIGNMENT_COMUNITY:
			HashMap<String, Object> dataBox= (HashMap<String, Object>) object;
			boolean isResponse = (Boolean) dataBox.get("isResponse");
			if (isResponse) {
				showToast("转让成功");
				CommunityReqUtil.doHandleApply(getActivity(), this, mCommunityNotify.getId(), 1, new Date().getTime(), null);
			}else{
				showToast("转让失败");
			}
			break;
		case EAPIConsts.CommunityReqType.TYPE_INVITE2MUC:
			if (null != object) {
				CommunityReqUtil.doHandleApply(getActivity(), this, mCommunityNotify.getId(), 1, new Date().getTime(), null);
			}
			break;
		}
		
	}
	
	class CommumitiesNotificationAdapter extends BaseAdapter {
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			final CommunityNotify communityNotify = getItem(position);
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
			TextView notice_item_transfer_refuse = ViewHolder.get(convertView, R.id.notice_item_transfer_refuse);//转让者
			TextView notice_item_notice_refuse = ViewHolder.get(convertView, R.id.notice_item_notice_refuse);//转让通过或拒绝
			
			LinearLayout layout_transfer_self_refuse = ViewHolder.get(convertView, R.id.layout_transfer_self_refuse);//自己操作后的效果
			TextView notice_item_transfer_self_text = ViewHolder.get(convertView, R.id.notice_item_transfer_self_text);
			TextView notice_item_transfer_self_refuse = ViewHolder.get(convertView, R.id.notice_item_transfer_self_refuse);
			TextView notice_item_transfer_self_refuse_person = ViewHolder.get(convertView, R.id.notice_item_transfer_self_refuse_person);
			TextView notice_item_notice_self_refuse = ViewHolder.get(convertView, R.id.notice_item_notice_self_refuse);
			
			
			TextView notice_applyReason = ViewHolder.get(convertView, R.id.notice_applyReason);//申请理由

			LinearLayout layout_choice = ViewHolder.get(convertView, R.id.layout_choice);
			LinearLayout ownerll = ViewHolder.get(convertView, R.id.ownerll);//群主显示忽略、同意布局
			TextView notice_item_refuse = ViewHolder.get(convertView, R.id.notice_item_refuse);// 忽略
			TextView notice_item_agree = ViewHolder.get(convertView, R.id.notice_item_agree);// 同意
			
			final TextView notice_item_into = ViewHolder.get(convertView, R.id.notice_item_into);// 进入聊天
			View bottom_line = ViewHolder.get(convertView, R.id.bottom_line);
			
			layout_transfer_self_refuse.setVisibility(View.GONE);
			bottom_line.setVisibility(View.GONE);
			layout_apply_invite.setVisibility(View.GONE);
			layout_choice.setVisibility(View.GONE);
			ownerll.setVisibility(View.GONE);
			notice_item_into.setVisibility(View.GONE);
			notice_applyReason.setVisibility(View.GONE);
			layout_transfer.setVisibility(View.GONE);
			layout_transfer_refuse.setVisibility(View.GONE);
			notice_item_name.setText("");

			switch(communityNotify.getNoticeType()){//通知类型：0加群 1转让群组 2禁言.
			case 0://0加群 
				switch(communityNotify.getAttendType()){//加入的方式：0邀请，1申请.
				case 0://0邀请
					layout_apply_invite.setVisibility(View.VISIBLE);
					
					ImageLoader.getInstance().displayImage(communityNotify.getCreatedUserLogo(), notice_item_image,options);
					notice_item_name.setText(communityNotify.getCreatedUserName());
					notice_attendType.setText("邀请您加入");
					notice_item_notice.setText(communityNotify.getCommunityName());
					notice_item_notice.setTextColor(mContext.getResources().getColor(R.color.text_flow_more));
					
					switch(communityNotify.getAcceptStatus()){// 0未答复 1接受 2拒绝.
					case 0:
						layout_choice.setVisibility(View.VISIBLE);
						ownerll.setVisibility(View.VISIBLE);
						break;
					case 1:
						if((communityNotify.getCreatedUserId()+"").equals(App.getUserID())){//自己创建的通知被别人拒绝
							ImageLoader.getInstance().displayImage(communityNotify.getUserLogo(), notice_item_image,options);
							notice_item_name.setText(communityNotify.getApplicantName());
							bottom_line.setVisibility(View.VISIBLE);
							layout_apply_invite.setVisibility(View.GONE);
							layout_transfer_self_refuse.setVisibility(View.VISIBLE);
							notice_item_transfer_self_text.setText("邀请加入");
							notice_item_transfer_self_refuse.setText(communityNotify.getCommunityName());
							notice_item_transfer_self_refuse.setTextColor(mContext.getResources().getColor(R.color.text_flow_more));
							notice_item_transfer_self_refuse_person.setText("已同意");
							notice_item_transfer_self_refuse_person.setTextColor(mContext.getResources().getColor(R.color.text_flow_more));
							notice_item_notice_self_refuse.setText("");
						}else{
							layout_choice.setVisibility(View.VISIBLE);
							notice_item_into.setVisibility(View.VISIBLE);
							notice_item_into.setText("已处理");
							notice_item_into.setTextColor(mContext.getResources().getColor(R.color.text_hint));
						}
						break;
					case 2:
						if((communityNotify.getCreatedUserId()+"").equals(App.getUserID())){//自己创建的通知被别人拒绝
							ImageLoader.getInstance().displayImage(communityNotify.getUserLogo(), notice_item_image,options);
							notice_item_name.setText(communityNotify.getApplicantName());
							bottom_line.setVisibility(View.VISIBLE);
							layout_apply_invite.setVisibility(View.GONE);
							layout_transfer_self_refuse.setVisibility(View.VISIBLE);
							notice_item_transfer_self_text.setText("邀请加入");
							notice_item_transfer_self_refuse.setText(communityNotify.getCommunityName());
							notice_item_transfer_self_refuse.setTextColor(mContext.getResources().getColor(R.color.text_flow_more));
							notice_item_transfer_self_refuse_person.setText("被拒绝");
							notice_item_transfer_self_refuse_person.setTextColor(mContext.getResources().getColor(R.color.gintongyellow));
							notice_item_notice_self_refuse.setText("");
						}else{
							layout_choice.setVisibility(View.VISIBLE);
							notice_item_into.setVisibility(View.VISIBLE);
							notice_item_into.setText("已处理");
							notice_item_into.setTextColor(mContext.getResources().getColor(R.color.text_hint));
						}
						break;
					}
					break;
				case 1://1申请.
					layout_apply_invite.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(communityNotify.getUserLogo(), notice_item_image,options);
					notice_item_name.setText(communityNotify.getCreatedUserName());
					notice_attendType.setText("申请加入");
					notice_item_notice.setText(communityNotify.getCommunityName());
					notice_item_notice.setTextColor(mContext.getResources().getColor(R.color.text_flow_more));
					
					switch(communityNotify.getAcceptStatus()){// 0未答复 1接受 2拒绝.
					case 0:
						notice_applyReason.setVisibility(View.VISIBLE);
						if(!TextUtils.isEmpty(communityNotify.getApplyReason())){
							SpannableString spanusername = new SpannableString("加群理由："+communityNotify.getApplyReason());
							spanusername.setSpan(new ForegroundColorSpan(0xffb6b6b6), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							notice_applyReason.setText(spanusername);
						}else{
							SpannableString spanusername = new SpannableString("加群理由："+communityNotify.getCreatedUserName()+"申请加入群");
							spanusername.setSpan(new ForegroundColorSpan(0xffb6b6b6), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							notice_applyReason.setText(spanusername);
						}

						layout_choice.setVisibility(View.VISIBLE);
						ownerll.setVisibility(View.VISIBLE);
						break;
					case 1:
						if((communityNotify.getCreatedUserId()+"").equals(App.getUserID())){//自己创建的通知被别人拒绝
							bottom_line.setVisibility(View.VISIBLE);
							ImageLoader.getInstance().displayImage(communityNotify.getCommunityLogo(), notice_item_image,options);
							notice_item_name.setText(communityNotify.getCommunityName());
							notice_attendType.setText("您的加入申请");
							notice_item_notice.setText("已通过");
							notice_item_notice.setTextColor(mContext.getResources().getColor(R.color.text_flow_content));
							layout_choice.setVisibility(View.VISIBLE);
							notice_item_into.setVisibility(View.VISIBLE);
							notice_item_into.setText("进入聊天");
							notice_item_into.setTextColor(mContext.getResources().getColor(R.color.gintongyellow));
						}else{
							layout_choice.setVisibility(View.VISIBLE);
							notice_item_into.setVisibility(View.VISIBLE);
							notice_item_into.setText("已处理");
							notice_item_into.setTextColor(mContext.getResources().getColor(R.color.text_hint));
						}
						break;
					case 2:
						if((communityNotify.getCreatedUserId()+"").equals(App.getUserID())){//自己创建的通知被别人拒绝
							bottom_line.setVisibility(View.VISIBLE);
							ImageLoader.getInstance().displayImage(communityNotify.getCommunityLogo(), notice_item_image,options);
							notice_item_name.setText(communityNotify.getCommunityName());
							notice_attendType.setText("您的加入申请");
							notice_item_notice.setText("被拒绝");
							notice_item_notice.setTextColor(mContext.getResources().getColor(R.color.gintongyellow));
						}else{
							layout_choice.setVisibility(View.VISIBLE);
							notice_item_into.setVisibility(View.VISIBLE);
							notice_item_into.setText("已处理");
							notice_item_into.setTextColor(mContext.getResources().getColor(R.color.text_hint));
						}
						break;
					}
					break;
				}
				break;
			case 1://1转让群组 
				ImageLoader.getInstance().displayImage(communityNotify.getCommunityLogo(), notice_item_image,options);
				notice_item_name.setText(communityNotify.getCommunityName());
				switch(communityNotify.getAcceptStatus()){// 0未答复 1接受 2拒绝.
				case 0:
					layout_transfer.setVisibility(View.VISIBLE);
					notice_item_transfer.setText(communityNotify.getCreatedUserName());
					layout_choice.setVisibility(View.VISIBLE);
					ownerll.setVisibility(View.VISIBLE);
					break;
				case 1:
					bottom_line.setVisibility(View.VISIBLE);
					if((communityNotify.getCreatedUserId()+"").equals(App.getUserID())){//自己创建的通知被别人拒绝
						layout_transfer_refuse.setVisibility(View.VISIBLE);
						notice_item_transfer_refuse.setText(communityNotify.getCreatedUserName());
						notice_item_notice_refuse.setText("已通过");
						notice_item_notice_refuse.setTextColor(mContext.getResources().getColor(R.color.text_flow_content));
					}else{
						layout_transfer_self_refuse.setVisibility(View.VISIBLE);
						notice_item_transfer_self_text.setText("您已经");
						notice_item_transfer_self_refuse.setText("同意了");
						notice_item_transfer_self_refuse.setTextColor(mContext.getResources().getColor(R.color.text_flow_more));
						notice_item_transfer_self_refuse_person.setText(communityNotify.getCreatedUserName());
						notice_item_transfer_self_refuse_person.setTextColor(mContext.getResources().getColor(R.color.text_flow_more));
						notice_item_notice_self_refuse.setText("的群转让申请");
					}
					break;
				case 2:
					bottom_line.setVisibility(View.VISIBLE);
					if((communityNotify.getCreatedUserId()+"").equals(App.getUserID())){//自己创建的通知被别人拒绝
						layout_transfer_refuse.setVisibility(View.VISIBLE);
						notice_item_transfer_refuse.setText(communityNotify.getCreatedUserName());
						notice_item_notice_refuse.setText("被拒绝");
						notice_item_notice_refuse.setTextColor(mContext.getResources().getColor(R.color.gintongyellow));
					}else{//别人创建的通知被自己拒绝
						layout_transfer_self_refuse.setVisibility(View.VISIBLE);
						notice_item_transfer_self_text.setText("您已经");
						notice_item_transfer_self_refuse.setText("拒绝了");
						notice_item_transfer_self_refuse.setTextColor(mContext.getResources().getColor(R.color.gintongyellow));
						notice_item_transfer_self_refuse_person.setText(communityNotify.getCreatedUserName());
						notice_item_transfer_self_refuse_person.setTextColor(mContext.getResources().getColor(R.color.text_flow_more));
						notice_item_notice_self_refuse.setText("的群转让申请");
					}
					
					break;
				}
				break;
			case 2://2禁言.
				break;
			}
			
			notice_item_refuse.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CommunityReqUtil.doHandleApply(mContext, FragCommunityMessage.this, communityNotify.getId(), 2, new Date().getTime(), null);
					communityNotify.setAcceptStatus(2);
					notifyDataSetChanged();
				}
			});
			
			notice_item_agree.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mCommunityNotify = communityNotify;
					switch(communityNotify.getNoticeType()){//通知类型：0加群 1转让群组 2禁言.
					case 0://0加群 
						List<Long> list = new ArrayList<Long>();
						switch(communityNotify.getAttendType()){//加入的方式：0邀请，1申请.
						case 0:
							list.add(Long.parseLong(App.getApp().getUserID()));
							CommunityReqUtil.doInvite2Muc(mContext, communityNotify.getCommunityId(), list, FragCommunityMessage.this, null);
							break;
						case 1:
							list.add(communityNotify.getCreatedUserId());
							CommunityReqUtil.doInvite2Muc(mContext, communityNotify.getCommunityId(), list, FragCommunityMessage.this, null);
							break;
						}
						break;
					case 1:
						CommunityReqUtil.doAssignmentCommunity(mContext, communityNotify.getCommunityId()+"", communityNotify.getApplicantId()+"", FragCommunityMessage.this, null);
						break;
					}
					communityNotify.setAcceptStatus(1);
					notifyDataSetChanged();
				}
			});
			
			notice_item_into.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(notice_item_into.getText().toString().equals("进入聊天")){
						Intent intent = new Intent(mContext, CommunityChatActivity.class);
						ImMucinfo community = new ImMucinfo();
						community.setId(communityNotify.getCommunityId());
						community.setPicPath(communityNotify.getCommunityLogo());
						community.setTitle(communityNotify.getCommunityName());
						intent.putExtra("community", community);
						intent.putExtra(ENavConsts.EFromActivityName, "CommumitiesNotificationActivity");
						startActivityForResult(intent, ENavigate.REQUSET_CODE_MUC);
					}
				}
			});
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(communityNotify.getNoticeType()==0){
						if(communityNotify.getAcceptStatus()!=0){
							Intent intent = new Intent(mContext, CommunitiesDetailsActivity.class);
							intent.putExtra(GlobalVariable.COMMUNITY_ID, communityNotify.getCommunityId());
							startActivity(intent);
						}
					}
				}
			});
			
			return convertView;
		}
	}

	class ComparatorNotify implements Comparator{

		@Override
		public int compare(Object lhs, Object rhs) {
			CommunityNotify communityNotify1 = (CommunityNotify) lhs;
			CommunityNotify communityNotify2 = (CommunityNotify) rhs;
			int flag=communityNotify2.getUpdatedTime().compareTo(communityNotify1.getUpdatedTime());
			if (flag == 0) {
				return communityNotify2.getCreatedTime().compareTo(communityNotify1.getCreatedTime());
			} else {
				return flag;
			}
		}
		
	}
}
