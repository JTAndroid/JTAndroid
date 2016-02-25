package com.tr.ui.communities.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.obj.ConnectionsMini;
import com.utils.image.LoadImage;

public class GroupMembersAdapter extends BaseAdapter {

	private List<ConnectionsMini> connectionsMinis = new ArrayList<ConnectionsMini>();
	private Context mContext;
	private GroupMembersOperationListener membersOperationListener;
	private boolean isBatch;
	private boolean isOwner;
	private GroupMembersBatchListener membersBatchListener;
	private boolean batchAll;
	
	
	public GroupMembersBatchListener getMembersBatchListener() {
		return membersBatchListener;
	}
	public void setMembersBatchListener(
			GroupMembersBatchListener membersBatchListener) {
		this.membersBatchListener = membersBatchListener;
		
	}
	public boolean isOwner() {
		return isOwner;
	}
	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}
	public GroupMembersAdapter(Context mContext,
			GroupMembersOperationListener membersOperationListener,GroupMembersBatchListener membersBatchListener) {
		super();
		this.mContext = mContext;
		this.membersOperationListener = membersOperationListener;
		this.membersBatchListener =membersBatchListener;
	}
	public boolean isBatch() {
		return isBatch;
	}
	public void setBatch(boolean isBatch) {
		this.isBatch = isBatch;
	}
	

	public List<ConnectionsMini> getConnectionsMinis() {
		return connectionsMinis;
	}
	public void setConnectionsMinis(List<ConnectionsMini> connectionsMinis) {
		this.connectionsMinis = connectionsMinis;
	}


	class ChlidViewHolder {
		public CheckBox groupMembersBatchCb;
		public RelativeLayout groupMembersCommunicationLl;
		ImageView groupMembers_avatar_iv,groupMembersMoreIv,groupMembersFriendTagIv,bannedToPostTag;
		TextView groupMembersName,groupMembersFrom,groupMembersCompanyTv;
	}
	public interface GroupMembersOperationListener{
		void bannedToPost(String userId,String status);
		void transfer(String userId, ConnectionsMini connectionsMini);
		void removeAndReport(String userId, ConnectionsMini connectionsMini);
		void remove(String userId, ConnectionsMini connectionsMini);
		
	}
	public interface GroupMembersBatchListener{
		void batchClick(String userId,boolean isCheck);
		
	}
	@Override
	public int getCount() {
		return connectionsMinis.size();
	}
	@Override
	public ConnectionsMini getItem(int position) {
		return connectionsMinis.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ChlidViewHolder chlidViewHolder;
		if (convertView==null) {
			chlidViewHolder = new ChlidViewHolder();
			convertView = View.inflate(mContext, R.layout.adapter_groupmembers_child, null);
			chlidViewHolder.groupMembers_avatar_iv = (ImageView) convertView.findViewById(R.id.groupMembers_avatar_iv);
			chlidViewHolder.groupMembersMoreIv = (ImageView) convertView.findViewById(R.id.groupMembersMoreIv);
			chlidViewHolder.groupMembersFriendTagIv = (ImageView) convertView.findViewById(R.id.groupMembersFriendTag);
			chlidViewHolder.groupMembersName = (TextView) convertView.findViewById(R.id.groupMembersName);
			chlidViewHolder.groupMembersFrom = (TextView) convertView.findViewById(R.id.groupMembersFrom);
			chlidViewHolder.groupMembersCommunicationLl = (RelativeLayout)convertView.findViewById(R.id.groupMembersCommunicationRl);
			chlidViewHolder.groupMembersBatchCb = (CheckBox) convertView.findViewById(R.id.groupMembersBatchCb);
			chlidViewHolder.groupMembersCompanyTv = (TextView) convertView.findViewById(R.id.groupMembersCompanyTv);
			chlidViewHolder.bannedToPostTag = (ImageView) convertView.findViewById(R.id.bannedToPostTag);
			convertView.setTag(chlidViewHolder);
		}else{
			chlidViewHolder = (ChlidViewHolder) convertView.getTag();
		}
		final ConnectionsMini connectionsMini = getItem(position);
		
		//该成员是未禁言状态
		if ("1".equals(connectionsMini.talkStatus)) {
			if (!TextUtils.isEmpty(connectionsMini.getImage())) {
				ImageLoader.getInstance().displayImage(connectionsMini.getImage(), chlidViewHolder.groupMembers_avatar_iv,LoadImage.mDefaultHead);
			}else{
				chlidViewHolder.groupMembers_avatar_iv.setImageResource(R.drawable.ic_default_avatar);
			}
		}//该成员是禁言状态
		else if ("2".equals(connectionsMini.talkStatus)) {
			chlidViewHolder.groupMembers_avatar_iv.setImageResource(R.drawable.community_notalk);
		}
		if (connectionsMini.getFriendState()==2) {
			chlidViewHolder.groupMembersFriendTagIv .setVisibility(View.VISIBLE);
		}else{
			chlidViewHolder.groupMembersFriendTagIv .setVisibility(View.GONE);
		}
		chlidViewHolder.groupMembersBatchCb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
					if (chlidViewHolder.groupMembersBatchCb.isChecked()) {
						membersBatchListener.batchClick(connectionsMini.getId(),true);
					}else{
						membersBatchListener.batchClick(connectionsMini.getId(),false);
					}
					
				}
		});
		chlidViewHolder.groupMembersMoreIv.setOnClickListener(new OnClickListener() {
			
			private PopupWindow popupWindow;
			private String status = "1";
			@Override
			public void onClick(View v) {
				View view = View.inflate(mContext, R.layout.moregroupmeberpop, null);
				popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
				popupWindow.setAnimationStyle(R.style.PupwindowAnimation);
				popupWindow.setBackgroundDrawable(new BitmapDrawable());
				popupWindow.setOutsideTouchable(true);
				popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
				
				
				LinearLayout bannedToPostLl = (LinearLayout) view.findViewById(R.id.bannedToPostLl);
				LinearLayout TransferLl = (LinearLayout) view.findViewById(R.id.TransferLl);
				LinearLayout removeAndReportLl = (LinearLayout) view.findViewById(R.id.removeAndReportLl);
				LinearLayout removeLl = (LinearLayout) view.findViewById(R.id.removeLl);
				
				TextView bannedToPostTv = (TextView)view.findViewById(R.id.bannedToPostTv);
				TextView cancel = (TextView)view.findViewById(R.id.cancel);
				//该成员是未禁言状态
				if ("1".equals(connectionsMini.talkStatus)) {
					bannedToPostTv.setText("禁言");
					status = "2";
				}//该成员是禁言状态
				else if ("2".equals(connectionsMini.talkStatus)) {
					bannedToPostTv.setText("取消禁言");
					status = "1";
				}
				cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
						
					}
				});
				bannedToPostLl.setOnClickListener(new OnClickListener() { //禁言
					
					@Override
					public void onClick(View v) {
						membersOperationListener.bannedToPost(connectionsMini.getId(),status );
						popupWindow.dismiss();
					}
				});
				TransferLl.setOnClickListener(new OnClickListener() {  //转让
					
					@Override
					public void onClick(View v) {
						membersOperationListener.transfer(connectionsMini.getId(),connectionsMini);
						popupWindow.dismiss();
					}
				});
				removeAndReportLl.setOnClickListener(new OnClickListener() { //移除并举报
		
					@Override
					public void onClick(View v) {
						membersOperationListener.removeAndReport(connectionsMini.getId(),connectionsMini);
						popupWindow.dismiss();
					}
				}); 
				removeLl.setOnClickListener(new OnClickListener() {  //移除
		
					@Override
					public void onClick(View v) {
						membersOperationListener.remove(connectionsMini.getId(),connectionsMini);
						popupWindow.dismiss();
					}
				});
			}
		});
		chlidViewHolder.groupMembersName.setText(TextUtils.isEmpty(connectionsMini.getShortName())?connectionsMini.getName():connectionsMini.getShortName());
			String companyName = TextUtils.isEmpty(connectionsMini.getCompanyName())?"":connectionsMini.getCompanyName();
			String Companyjob = TextUtils.isEmpty(connectionsMini.getCompanyJob())?"":connectionsMini.getCompanyJob();
			chlidViewHolder.groupMembersCompanyTv.setText(companyName+Companyjob);
			if (isOwner) {
				chlidViewHolder.groupMembersCommunicationLl.setVisibility(View.GONE);
			}else{
				chlidViewHolder.groupMembersCommunicationLl.setVisibility(View.VISIBLE);
			}
			
				if (isBatch) {
					chlidViewHolder.groupMembersBatchCb.setVisibility(View.VISIBLE);
					chlidViewHolder.groupMembersMoreIv.setVisibility(View.GONE);
					chlidViewHolder.groupMembersBatchCb.setChecked(batchAll);
				}else{
					chlidViewHolder.groupMembersBatchCb.setVisibility(View.GONE);
					chlidViewHolder.groupMembersMoreIv.setVisibility(View.VISIBLE);
				}
		return convertView;
	}
	public void setKeyWord(String keyword) {
		List<ConnectionsMini> keywordMinis = new ArrayList<ConnectionsMini>();
		List<ConnectionsMini> connectionsMinis2 = this.getConnectionsMinis();
		for (int i = 0; i < connectionsMinis2.size(); i++) {
			ConnectionsMini connectionsMini = connectionsMinis2.get(i);
			if (connectionsMini.getName().contains(keyword)) {
				keywordMinis.add(connectionsMini);
			}
		}
		setConnectionsMinis(keywordMinis);
		notifyDataSetChanged();
	}
	public void setBatchAll(boolean batchAll) {
		this.batchAll = batchAll;
		notifyDataSetChanged();
	}
}
