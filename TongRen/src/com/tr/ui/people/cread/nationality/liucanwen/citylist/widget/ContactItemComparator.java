package com.tr.ui.people.cread.nationality.liucanwen.citylist.widget;

import java.util.Comparator;

public class ContactItemComparator implements Comparator<ContactItemInterface>
{

	@Override
	public int compare(ContactItemInterface lhs, ContactItemInterface rhs)
	{
		if (lhs.getItemForIndex() == null || rhs.getItemForIndex() == null)
			return -1;

		return (lhs.getItemForIndex().compareTo(rhs.getItemForIndex()));

	}

}
