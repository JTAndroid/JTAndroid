package com.tr.ui.adapter.conference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.obj.JTContactMini;
import com.tr.ui.conference.utile.PeopleOrgknowleRequirmentLayoutUtil;
import com.utils.time.Util;

public class GridviewInviteSpeakerAdapter extends BaseAdapter {
	private Context context;

	private List<JTContactMini> inviteAttendList = new ArrayList<JTContactMini>(0);

	public GridviewInviteSpeakerAdapter(Context context) {
		this.context = context;
		JTContactMini fricontact = new JTContactMini();
		inviteAttendList.add(fricontact);
	}

	public void update(List<JTContactMini> dataList) {
		this.inviteAttendList.clear();
		JTContactMini fricontact = new JTContactMini();
		this.inviteAttendList.add(fricontact);
		if (!Util.isNull(dataList)) {
			this.inviteAttendList.addAll(this.inviteAttendList.size() - 1, dataList);
		}
		
	}

	public void update(Map<String, JTContactMini> dataMap) {
		this.inviteAttendList.clear();
		JTContactMini fricontact = new JTContactMini();
		this.inviteAttendList.add(fricontact);
		if (!Util.isNull(dataMap)) {
			Iterator<Entry<String, JTContactMini>> iterIAM = dataMap.entrySet().iterator();
			while (iterIAM.hasNext()) {
				Map.Entry entry = (Map.Entry) iterIAM.next();
				JTContactMini item = (JTContactMini) entry.getValue();
				this.inviteAttendList.add(this.inviteAttendList.size() - 1, item);
			}
		}
	}

	public void update() {
		notifyDataSetChanged();
	}

	public int getListSize() {
		return inviteAttendList.size();
	}

	public void clear() {
		inviteAttendList.clear();
	}

	@Override
	public int getCount() {
		return inviteAttendList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return inviteAttendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.hy_item_attendee, parent, false);
			holderView = new HolderView();
			holderView.addImage = (ImageView) convertView.findViewById(R.id.hy_item_add_image);
			holderView.image = (ImageView) convertView.findViewById(R.id.hy_item_attendee_image);
			holderView.name = (TextView) convertView.findViewById(R.id.hy_item_attendee_text);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		setHolderView(holderView, position);
		return convertView;
	}

	private void setHolderView(HolderView holderView, int position) {
		JTContactMini item = inviteAttendList.get(position);
		if ((inviteAttendList.size() - 1) == position) {
			holderView.addImage.setVisibility(View.VISIBLE);
			holderView.image.setVisibility(View.GONE);
			holderView.addImage.setImageResource(R.drawable.hy_addimage_bg_selector);
			holderView.name.setVisibility(View.GONE);
		} else {
			holderView.addImage.setVisibility(View.GONE);
			holderView.image.setVisibility(View.VISIBLE);
			holderView.image.setImageResource(R.drawable.hy_ic_default_friend_avatar);
			com.utils.common.Util.initAvatarImage(context, holderView.image, item.name, item.image,item.getGender(), 1);
			holderView.name.setText(item.name);
			holderView.name.setVisibility(View.VISIBLE);
		}
	}

	private class HolderView {
		public ImageView image;
		public TextView name;
		public ImageView addImage;
	}
}
