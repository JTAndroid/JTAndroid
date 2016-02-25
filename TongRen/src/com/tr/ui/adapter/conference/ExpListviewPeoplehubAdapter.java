package com.tr.ui.adapter.conference;

import com.tr.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpListviewPeoplehubAdapter extends BaseExpandableListAdapter {
	private Context context;
	
	
	public ExpListviewPeoplehubAdapter(Context context){
		this.context = context;
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
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
		setGroupHolderView(holderView, groupPosition);
		return convertView;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
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
			holderView.checkbox = (CheckBox) convertView.findViewById(R.id.hy_itemInvitefriend_checkbox);
			holderView.avatar = (ImageView) convertView.findViewById(R.id.hy_itemInvitefriend_avatar);
			holderView.name = (TextView) convertView.findViewById(R.id.hy_itemInvitefriend_nameText);
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
		return false;
	}
	
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	private void setGroupHolderView(GroupHolderView holderView, int groupPosition){
		
	}
	private void setChildHolderView(ChildHolderView holderView, int groupPosition, int childPosition){
		
	}
	private class GroupHolderView{
		public TextView letter;
	}
	private class ChildHolderView{
		public CheckBox checkbox;
		public ImageView avatar;
		public TextView name;
	}
}
