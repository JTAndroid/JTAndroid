package com.tr.ui.common;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.obj.Connections;
import com.tr.model.user.OrganizationMini;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.tr.ui.conference.initiatorhy.InviteFriendActivity;
import com.utils.common.GlobalVariable;
import com.utils.time.Util;

public class ContactFriendListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Connections> mOrgConnections;
	private boolean selected = false ;
	private int inviteType;
	
	public ContactFriendListAdapter(Context context ,ArrayList<Connections> orgConnections, int inviteType){
		this.context = context;
		this.mOrgConnections = orgConnections;
		this.inviteType = inviteType;
	}

	@Override
	public int getCount() {
		return mOrgConnections.size();
	}

	@Override
	public Object getItem(int position) {
		return mOrgConnections.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.contact_lists_common_item, parent, false);
			holderView = new HolderView();
			holderView.contactListsOrgGroupIv = (ImageView) convertView.findViewById(R.id.contactLists_orgGroup_Iv);
			holderView.contactListsOrgGroupNameTV = (TextView) convertView.findViewById(R.id.contactLists_orgGroup_name_TV);
			holderView.contactListsOrgGroupCheckBoxIV = (ImageView) convertView.findViewById(R.id.contactLists_orgGroup_checkBox_IV);
			convertView.setTag(holderView);
		}else{
			holderView = (HolderView) convertView.getTag();
		}
		OrganizationMini mOrganizationMini = mOrgConnections.get(position).getOrganizationMini();
		if(!Util.isNull(mOrganizationMini.mLogo) ){
			if(mOrganizationMini.mLogo.endsWith(GlobalVariable.ORG_DEFAULT_AVATAR)){
				String lastchar = "";
				if(!TextUtils.isEmpty(mOrganizationMini.fullName)){
					lastchar = mOrganizationMini.fullName.substring(mOrganizationMini.fullName.length()-1);
				}
				Bitmap bm = com.utils.common.Util.createBGBItmap(context, R.drawable.ic_group_default_avatar, R.color.avatar_text_color, R.dimen.avatar_text_size, lastchar);
				holderView.contactListsOrgGroupIv.setImageBitmap(bm);
			}else{
				ImageLoader.getInstance().displayImage(mOrganizationMini.mLogo, holderView.contactListsOrgGroupIv);
			}
		}
		holderView.contactListsOrgGroupNameTV.setText(mOrganizationMini.fullName);
		if(inviteType == InviteFriendActivity.TYPE_INVITE_AT_FRIEND){
			holderView.contactListsOrgGroupCheckBoxIV.setVisibility(View.GONE);
		}
		if(InitiatorDataCache.getInstance().forwardingAndSharingOrgMap.containsKey(mOrganizationMini.mID)){
			selected = true;
		}else {
			selected = false;
		}
		if(selected){
			holderView.contactListsOrgGroupCheckBoxIV.setImageResource(R.drawable.demand_me_need_checkbox_activated);
		}else{
			holderView.contactListsOrgGroupCheckBoxIV.setImageResource(R.drawable.demand_me_need_checkbox_default);
		}
		return convertView;
	}

	private class HolderView{
		public ImageView contactListsOrgGroupIv;
		public TextView contactListsOrgGroupNameTV;
		public ImageView contactListsOrgGroupCheckBoxIV;
	}

}
