package com.tr.ui.adapter.conference;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.conference.MExpFriendContact;
import com.tr.model.obj.JTContactMini;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.tr.ui.conference.initiatorhy.InviteFriendActivity;
import com.tr.ui.conference.myhy.utils.Utils;
import com.utils.common.GlobalVariable;
import com.utils.time.Util;

public class ExpListviewInviteFriendAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<MExpFriendContact> dataList = new ArrayList<MExpFriendContact>();
	private int inviteType;
//	private int SCALESIZE =100;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	public ExpListviewInviteFriendAdapter(Context context){
		this.context = context;
	}
	public ExpListviewInviteFriendAdapter(Context context, int inviteType, List<MExpFriendContact> dataList){
		this.context = context;
//		initImageLoader(context);
		this.inviteType = inviteType;
		if(!Util.isNull(dataList)){
			this.dataList = dataList;
		}
	}
	public void update(List<MExpFriendContact> dataList){
		if(Util.isNull(dataList)){
			this.dataList.clear();
		}else{
			this.dataList = dataList;
		}
//		this.notifyDataSetChanged();
	}
	public List<MExpFriendContact> getExpFriendContact(){
		return dataList;
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		if(Util.isNull(dataList)){
			return 0;
		}else{
			return dataList.size();
		}
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return dataList.get(groupPosition);
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GroupHolderView holderView;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hy_item_invite_friend_explv_group_letter, parent, false);
			holderView = new GroupHolderView();
			holderView.letter = (TextView) convertView.findViewById(R.id.hy_itemInvitefriend_expLvGroup_letterText);
			convertView.setTag(holderView);
		}else{
			holderView = (GroupHolderView) convertView.getTag();
		}
		if(groupPosition == 0){
			holderView.letter.setVisibility(View.GONE);
		}else{
			holderView.letter.setVisibility(View.VISIBLE);
		}
		setGroupHolderView(holderView, groupPosition);
		return convertView;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if(Util.isNull(dataList.get(groupPosition).contactList)){
			return 0;
		}else{
			return dataList.get(groupPosition).contactList.size();
		}
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return dataList.get(groupPosition).contactList.get(childPosition);
	}

	

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChildHolderView holderView;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hy_item_invite_friend_lv, parent, false);
			holderView = new ChildHolderView();
			holderView.checkbox = (ImageView) convertView.findViewById(R.id.hy_itemInvitefriend_checkbox);
			holderView.avatar = (ImageView) convertView.findViewById(R.id.hy_itemInvitefriend_avatar);
			holderView.name = (TextView) convertView.findViewById(R.id.hy_itemInvitefriend_nameText);
			holderView.bottom_line = (View) convertView.findViewById(R.id.bottom_line);
			convertView.setTag(holderView);
		}else{
			holderView = (ChildHolderView) convertView.getTag();
		}
		setChildHolderView(holderView, groupPosition, childPosition);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}
	/**
	 * @param holderView
	 * @param groupPosition
	 */
	private void setGroupHolderView(GroupHolderView holderView, int groupPosition){
		holderView.letter.setText(dataList.get(groupPosition).nameCh);
	}
	private void setChildHolderView(ChildHolderView holderView, int groupPosition, int childPosition){
		JTContactMini item = dataList.get(groupPosition).contactList.get(childPosition);
		boolean selected = false;
		if(inviteType == InviteFriendActivity.TYPE_INVITE_ATTEND_FRIEND){
			if(InitiatorDataCache.getInstance().inviteAttendSelectedMap.containsKey(item.id)){
				selected = true;
			}
		}else if(inviteType == InviteFriendActivity.TYPE_INVITE_SPEAKER_FRIEND){
			if(InitiatorDataCache.getInstance().inviteSpeakerSelectedMap.containsKey(item.id)){
				selected = true;
			}
		}else{
			if(InitiatorDataCache.getInstance().costomFriselectedMap.containsKey(item.id)){
				selected = true;
			}
		}
		if(inviteType == InviteFriendActivity.TYPE_INVITE_AT_FRIEND){
			holderView.checkbox.setVisibility(View.GONE);
		}
		if(selected){
			holderView.checkbox.setImageResource(R.drawable.demand_me_need_checkbox_activated);
		}else{
			holderView.checkbox.setImageResource(R.drawable.demand_me_need_checkbox_default);
		}
		if((childPosition+1) == dataList.get(groupPosition).contactList.size()){
			holderView.bottom_line.setVisibility(View.GONE);
		}else{
			holderView.bottom_line.setVisibility(View.VISIBLE);
		}
		holderView.avatar.setImageResource(R.drawable.hy_ic_default_friend_avatar);
		com.utils.common.Util.initAvatarImage(context, holderView.avatar, item.name, item.image, 0, 1);
		holderView.name.setText(item.name);
	}
	private class GroupHolderView{
		public TextView letter;
	}
	private class ChildHolderView{
		public ImageView checkbox;
		public ImageView avatar;
		public TextView name;
		public View bottom_line;
	}
}
