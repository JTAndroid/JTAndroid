package com.tr.ui.adapter.conference;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.RequirementMini;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.utils.time.Util;

public class ListViewSearchAdapter extends BaseAdapter {
	public final static int TYPE_INVITE_ATTEND_FRIEND = 0;
	public final static int TYPE_INVITE_SPEAKER_FRIEND = 1;
	public final static int TYPE_SHARE_PEOPLEHUB_FRIEND = 2;
	public final static int TYPE_SHARE_DEMAND = 3;
	public final static int TYPE_SHARE_KNOWLEADGE = 4;
	public final static int TYPE_INVITE_AT_FRIEND = 5;
	public final static int TYPE_INVITE_AT_FLOW = 6;
	private Context context;
	private int type;
	
	private List<Object> dataList = new ArrayList<Object>();
	
	public ListViewSearchAdapter(Context context){
		this.context = context;
	}
	public ListViewSearchAdapter(Context context, int type){
		this.context = context;
		this.type = type;
	}
	public void update(List<Object> dataList){
		this.dataList.clear();
		this.dataList.addAll(dataList);
		notifyDataSetChanged();
	}
	public List<Object> getDataList(){
		return dataList;
	}
	public void release(){
		dataList.clear();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(type < TYPE_SHARE_DEMAND||type==TYPE_INVITE_AT_FLOW){
			FriHolderView holderView;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.hy_item_invite_friend_lv, parent, false);
				holderView = new FriHolderView();
				holderView.checkbox = (ImageView) convertView.findViewById(R.id.hy_itemInvitefriend_checkbox);
				holderView.avatar = (ImageView) convertView.findViewById(R.id.hy_itemInvitefriend_avatar);
				holderView.name = (TextView) convertView.findViewById(R.id.hy_itemInvitefriend_nameText);
				convertView.setTag(holderView);
			}else{
				holderView = (FriHolderView) convertView.getTag();
			}
			setHolderView(holderView, position);
		}else if(type == TYPE_SHARE_DEMAND){
			DemandHolderView holderView;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.hy_item_share_check, parent, false);
				holderView = new DemandHolderView();
				holderView.checkbox = (ImageView) convertView.findViewById(R.id.hy_item_shareCheck_checkbox);
				holderView.avatar = (ImageView) convertView.findViewById(R.id.hy_item_shareCheck_avatar);
				holderView.name = (TextView) convertView.findViewById(R.id.hy_item_shareCheck_nameText);
				holderView.time = (TextView) convertView.findViewById(R.id.hy_item_shareCheck_rightText);
				convertView.setTag(holderView);
			}else{
				holderView = (DemandHolderView) convertView.getTag();
			}
			setHolderView(holderView, position);
		}else if(type == TYPE_INVITE_AT_FRIEND){
			FriHolderView holderView;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.hy_item_invite_friend_lv, parent, false);
				holderView = new FriHolderView();
				holderView.checkbox = (ImageView) convertView.findViewById(R.id.hy_itemInvitefriend_checkbox);
				holderView.avatar = (ImageView) convertView.findViewById(R.id.hy_itemInvitefriend_avatar);
				holderView.name = (TextView) convertView.findViewById(R.id.hy_itemInvitefriend_nameText);
				convertView.setTag(holderView);
			}else{
				holderView = (FriHolderView) convertView.getTag();
			}
			setHolderView(holderView, position);
			
		}else{
			KnowleadgeHolderView holderView;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.hy_item_share_check_noavatar, parent, false);
				holderView = new KnowleadgeHolderView();
				holderView.checkbox = (ImageView) convertView.findViewById(R.id.hy_item_shareCheck_checkbox);
				holderView.name = (TextView) convertView.findViewById(R.id.hy_item_shareCheck_nameText);
				holderView.time = (TextView) convertView.findViewById(R.id.hy_item_shareCheck_rightText);
				convertView.setTag(holderView);
			}else{
				holderView = (KnowleadgeHolderView) convertView.getTag();
			}
			setHolderView(holderView, position);
		}
		return convertView;
	}
	private void setHolderView(FriHolderView holderView, int position){
		JTContactMini item = (JTContactMini)dataList.get(position);
		if(type == TYPE_INVITE_ATTEND_FRIEND){
			if(InitiatorDataCache.getInstance().inviteAttendSelectedMap.containsKey(item.id)){
				holderView.checkbox.setImageResource(R.drawable.hy_check_pressed);
			}else{
				holderView.checkbox.setImageResource(R.drawable.hy_check_norm);
			}
		}else if(type == TYPE_INVITE_AT_FLOW){
			if(InitiatorDataCache.getInstance().inviteAttendSelectedMap.containsKey(item.id)){
				holderView.checkbox.setImageResource(R.drawable.hy_check_pressed);
			}else{
				holderView.checkbox.setImageResource(R.drawable.hy_check_norm);
			}
		}else if(type == TYPE_INVITE_SPEAKER_FRIEND){
			if(InitiatorDataCache.getInstance().inviteSpeakerSelectedMap.containsKey(item.id)){
				holderView.checkbox.setImageResource(R.drawable.hy_check_pressed);
			}else{
				holderView.checkbox.setImageResource(R.drawable.hy_check_norm);
			}
		}else if(type == TYPE_INVITE_AT_FRIEND){
			holderView.checkbox.setVisibility(View.GONE);
		}else{
			if(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.containsKey(item.id)){
				holderView.checkbox.setImageResource(R.drawable.hy_check_pressed);
			}else{
				holderView.checkbox.setImageResource(R.drawable.hy_check_norm);
			}
		}
		holderView.avatar.setImageResource(R.drawable.hy_ic_default_friend_avatar);
		if(!Util.isNull(item.image) ){
			ImageLoader.getInstance().displayImage(item.image, holderView.avatar);
		}
		holderView.name.setText(item.name);
	}
	private void setHolderView(DemandHolderView holderView, int position){
		RequirementMini item = (RequirementMini)dataList.get(position);
		if(InitiatorDataCache.getInstance().shareDemandSelectedMap.containsKey(item.mID)){
			holderView.checkbox.setImageResource(R.drawable.hy_check_pressed);
		}else{
			holderView.checkbox.setImageResource(R.drawable.hy_check_norm);
		}
		if(!Util.isNull(item.mTypeName)){
			if(item.mTypeName.equals("我要投资")){
				holderView.avatar.setImageResource(R.drawable.hy_icon_meeting_detail_invest);
			}else if(item.mTypeName.equals("我要融资")){
				holderView.avatar.setImageResource(R.drawable.hy_icon_meeting_detail_finacing);
			}else{
				holderView.avatar.setImageResource(R.drawable.ic_avatar);
			}
		}else{
			holderView.avatar.setImageResource(R.drawable.ic_avatar);
		}
		
		holderView.name.setText(item.mTitle);
		if(!Util.isNull(item.mTime)){
			String[] arr = item.mTime.split("\\ ");
			if(arr != null && arr.length > 0){
				holderView.time.setText(arr[0]);
			}else{
				holderView.time.setText("");
			}
		}else{
			holderView.time.setText("");
		}
	}
	private void setHolderView(KnowleadgeHolderView holderView, int position){
		KnowledgeMini2 item = (KnowledgeMini2)dataList.get(position);
		if(InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.containsKey(item.id)){
			holderView.checkbox.setImageResource(R.drawable.hy_check_pressed);
		}else{
			holderView.checkbox.setImageResource(R.drawable.hy_check_norm);
		}
		
		holderView.name.setText(item.title);
		holderView.time.setText("");
	}
	private class FriHolderView{
		public ImageView checkbox;
		public ImageView avatar;
		public TextView name;
	}
	private class DemandHolderView{
		public ImageView checkbox;
		public ImageView avatar;
		public TextView name;
		public TextView time;
	}
	private class KnowleadgeHolderView{
		public ImageView checkbox;
		public TextView name;
		public TextView time;
	}
}
