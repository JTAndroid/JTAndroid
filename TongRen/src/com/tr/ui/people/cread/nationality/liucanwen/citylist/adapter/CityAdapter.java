package com.tr.ui.people.cread.nationality.liucanwen.citylist.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.people.cread.nationality.liucanwen.citylist.widget.ContactItemInterface;
import com.tr.ui.people.cread.nationality.liucanwen.citylist.widget.ContactListAdapter;

public class CityAdapter extends ContactListAdapter
{

	public CityAdapter(Context _context, int _resource,
			List<ContactItemInterface> _items)
	{
		super(_context, _resource, _items);
	}

	public void populateDataForRow(View parentView, ContactItemInterface item,
			int position)
	{
		
		View infoView = parentView.findViewById(R.id.infoRowContainer);
		TextView nicknameView = (TextView) infoView
				.findViewById(R.id.cityName);
			nicknameView.setText(item.getDisplayInfo());
		
	}

}
