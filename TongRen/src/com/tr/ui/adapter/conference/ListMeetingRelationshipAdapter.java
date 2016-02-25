package com.tr.ui.adapter.conference;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.conference.MMeetingQuery;

/**
 * 
 * @author sunjianan
 * 
 */
public class ListMeetingRelationshipAdapter extends BaseAdapter {

	private Context context;
	private MMeetingQuery meeting;
	private int relationType;
	private ImageLoader imageLoader; 

	public ListMeetingRelationshipAdapter(Context context, MMeetingQuery meeting,int relationType) {
		imageLoader = ImageLoader.getInstance();
		this.context = context;
		this.meeting = meeting;
		this.relationType = relationType;
	}

	@Override
	public int getCount() {
		if (meeting != null ) {
			if (relationType == 1 && meeting.getListMeetingPeople() != null) {
				return meeting.getListMeetingPeople().size();
			}else if (relationType == 2 && meeting.getListMeetingOrgan() != null) {
				return meeting.getListMeetingOrgan().size();
			}
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (meeting != null ) {
			if (relationType == 1 && meeting.getListMeetingPeople() != null) {
				return meeting.getListMeetingPeople().get(position);
			}else if (relationType == 2 && meeting.getListMeetingOrgan() != null) {
				return meeting.getListMeetingOrgan().get(position);
			}
		}
		return 0;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.hy_list_item_meeting_relation, null);
			holder.img = (ImageView) convertView.findViewById(R.id.hy_relation_iv_avatar);
			holder.name = (TextView) convertView.findViewById(R.id.hy_relation_tv_name);
			holder.desc = (TextView) convertView.findViewById(R.id.hy_relation_tv_desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (meeting != null) {
			if (relationType == 1 && meeting.getListMeetingPeople() != null) {
				if (meeting.getListMeetingPeople().get(position).getPeoplePhoto() != null) {
					imageLoader.displayImage(meeting.getListMeetingPeople().get(position).getPeoplePhoto(), holder.img);
				}
				if (meeting.getListMeetingPeople().get(position).getPeopleName() != null) {
					holder.name.setText(meeting.getListMeetingPeople().get(position).getPeopleName());
				}
				if (meeting.getListMeetingPeople().get(position).getPeopleDesc() != null) {
					holder.desc.setText(meeting.getListMeetingPeople().get(position).getPeopleDesc());
				}
			}else if (relationType == 2 && meeting.getListMeetingOrgan() != null) {

				if (meeting.getListMeetingOrgan().get(position).getOrganPhoto() != null) {
					imageLoader.displayImage(meeting.getListMeetingOrgan().get(position).getOrganPhoto(), holder.img);
				}
				if (meeting.getListMeetingOrgan().get(position).getOrganName() != null) {
					holder.name.setText(meeting.getListMeetingOrgan().get(position).getOrganName());
				}
				holder.desc.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView name;
		TextView desc;
	}

}
