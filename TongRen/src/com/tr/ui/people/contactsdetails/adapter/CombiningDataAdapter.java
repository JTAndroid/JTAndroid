package com.tr.ui.people.contactsdetails.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.people.contactsdetails.bean.CombiningPersonData;

public class CombiningDataAdapter extends BaseAdapter {

	private Context context;

	private List<CombiningPersonData> personDataList;

	private ViewHolder holder;

	public CombiningDataAdapter(Context context,
			List<CombiningPersonData> personDataList) {
		super();
		this.context = context;
		this.personDataList = personDataList;
	}

	@Override
	public int getCount() {
		return personDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return personDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(
					R.layout.people_combiningdata_listview, null);

			holder = new ViewHolder(convertView);

			convertView.setTag(holder);

		}

		holder = (ViewHolder) convertView.getTag();
		
		holder.person_head.setImageResource(personDataList.get(position).getPersonHeadImage());
		
		holder.person_name.setText(personDataList.get(position).getPerosnName());

		holder.person_position.setText(personDataList.get(position).getPersonPersonPosition());
	
		return convertView;
	}

	public static class ViewHolder {

		private ImageView person_head;

		private TextView person_name,  person_position;

		public ViewHolder(View convertView) {
			
			person_head = (ImageView) convertView.findViewById(R.id.person_head);

			person_name = (TextView) convertView.findViewById(R.id.person_name);
			
			
			person_position = (TextView) convertView.findViewById(R.id.person_position);
			
			
			
		}

	}

}
