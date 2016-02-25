package com.tr.ui.people.cread.utils;

import java.util.ArrayList;
import java.util.List;

import com.tr.R;
import com.tr.ui.organization.model.hight.CustomerHightInfo;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.resource.CustomerDemandCommon;
import com.tr.ui.organization.model.stock.CustomerStock;
import com.tr.ui.organization.model.stock.CustomerStockInfo;
import com.tr.ui.organization.model.stock.CustomerTenStock;
import com.tr.ui.organization.orgdetails.EditSeniorManagementActivity;
import com.tr.ui.people.cread.CountryActivity;
import com.tr.ui.people.cread.MoreModuleActivity.ViewHolder;
import com.tr.ui.people.cread.view.MyEditTextView;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MakeListView {
	/**
	 * 动态填充listview
	 * 
	 * @param list
	 * @return
	 */
	public static void makelistviewAdapter(final Context context,
			ListView list, final String[] str) {
		list.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = View.inflate(context,
							R.layout.people_list_item_3, null);
					holder.list_Tv = (TextView) convertView
							.findViewById(R.id.list_Tv);
					holder.list_Ib = (ImageView) convertView
							.findViewById(R.id.list_Ib);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
			
				holder.list_Tv.setText(str[position]);
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return str[position];
			}

			@Override
			public int getCount() {
				return str.length;
			}
		});

	}

	public static class ViewHolder {
		public TextView list_Tv;
		public ImageView list_Ib;
	}

	public static void Custom(final Context context, ListView list,
			final ArrayList<CustomerPersonalLine> l) {
		list.setAdapter(new BaseAdapter() {
			Viewholder viewholder;

			@Override
			public int getCount() {
				return l.size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					viewholder = new Viewholder();
					convertView = View.inflate(context, R.layout.item_custom,
							null);
					viewholder.custom_item = (MyEditTextView) convertView
							.findViewById(R.id.custom_item);
					viewholder.custom_item_Text = (MyEditTextView) convertView
							.findViewById(R.id.custom_item_Text);
					convertView.setTag(viewholder);
				} else {
					viewholder = (Viewholder) convertView.getTag();
				}
				CustomerPersonalLine cLine = l.get(position);
				if ("1".equals(cLine.type)) {
					viewholder.custom_item.setText(cLine.content);
					viewholder.custom_item.setTextLabel(cLine.name);
				} else if ("2".equals(cLine.type)) {
					viewholder.custom_item_Text.setText(cLine.content);
					viewholder.custom_item_Text.setTextLabel(cLine.name);
				}
				return convertView;
			}

			@Override
			public Object getItem(int position) {
				return l.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			class Viewholder {
				MyEditTextView custom_item;
				MyEditTextView custom_item_Text;
			}

		});

	}

	public static void Hight(final Context context, ListView list,
			final ArrayList<CustomerHightInfo> l) {
		list.setAdapter(new BaseAdapter() {
			Viewholder viewholder;

			@Override
			public int getCount() {
				return l.size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					viewholder = new Viewholder();
					convertView = View.inflate(context,
							R.layout.item_senior_management, null);
					viewholder.senior_executive_name = (TextView) convertView
							.findViewById(R.id.senior_executive_name);
					viewholder.senior_executive_duty = (TextView) convertView
							.findViewById(R.id.senior_executive_duty);
					viewholder.item_data = (TextView) convertView
							.findViewById(R.id.item_data);
					viewholder.senior_executive_education = (TextView) convertView
							.findViewById(R.id.senior_executive_education);
					convertView.setTag(viewholder);
				} else {
					viewholder = (Viewholder) convertView.getTag();
				}
				CustomerHightInfo customerHightInfo = l.get(position);
				viewholder.senior_executive_name
						.setText(customerHightInfo.relation.relation);
				viewholder.senior_executive_duty.setText(customerHightInfo.job);
				viewholder.item_data.setText(customerHightInfo.birth);
				viewholder.senior_executive_education
						.setText(customerHightInfo.eduational);
				return convertView;
			}

			@Override
			public Object getItem(int position) {
				return l.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			class Viewholder {
				TextView senior_executive_name;
				TextView senior_executive_duty;
				TextView item_data;
				TextView senior_executive_education;
			}

		});

	}

	/**
	 * 动态填充listview
	 * 
	 * @param list
	 * @return
	 */
	public static ListView ToListviewAdapter(final Context context,
			final ArrayList<String> list, ListView listview) {

		listview.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder2 holder = null;
				if (convertView == null) {
					holder = new ViewHolder2();
					convertView = View.inflate(context,
							R.layout.people_list_item_basic, null);
					holder.list_key = (TextView) convertView
							.findViewById(R.id.list_key);
					holder.list_value = (TextView) convertView
							.findViewById(R.id.list_value);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder2) convertView.getTag();
				}
				System.out.println("ToListviewAdapter_position" + position);
				// String text = list.get(position);
				String item = getItem(position);
				String[] keyAndValue = item.split("_");
				String key = keyAndValue[0];
				if (keyAndValue.length == 2) {
					String value = keyAndValue[1];
					holder.list_value.setText(value);
				}
				holder.list_key.setText(key);
				// holder.list_key.setText(text);
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public String getItem(int position) {
				return list.get(position);
			}
		});
		return listview;
	}

	public static class ViewHolder2 {
		TextView list_key, list_value;

	}
	
	/**
	 * 动态填充listview
	 * 
	 * @param aaaaaaaaaa
	 * @return
	 */
	public static ListView ToListviewAdapters(final Context context,
			final List<CustomerDemandCommon> resourceRequirementsList, ListView listview,final String[] resource_needs) {

		listview.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return resourceRequirementsList.size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder2 holder = null;
				if (convertView == null) {
					holder = new ViewHolder2();
					convertView = View.inflate(context,
							R.layout.people_list_item_basic, null);
					holder.list_key = (TextView) convertView
							.findViewById(R.id.list_key);
					holder.list_value = (TextView) convertView
							.findViewById(R.id.list_value);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder2) convertView.getTag();
				}
				System.out.println("ToListviewAdapter_position" + position);
				// String text = list.get(position);
				CustomerDemandCommon common = getItem(position);
				for(int i = 0;i < resource_needs.length;i++){
					
					holder.list_key.setText(resource_needs[i]);
					
//					holder.list_value.setText(getItem(position).)
					
				}
				
				
//				String key = keyAndValue[0];
//				if (keyAndValue.length == 2) {
//					String value = keyAndValue[1];
//					holder.list_value.setText(value);
//				}
//				holder.list_key.setText(key);
				// holder.list_key.setText(text);
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public CustomerDemandCommon getItem(int position) {
				return resourceRequirementsList.get(position);
			}
		});
		return listview;
	}

	public static class ViewHolder3 {
		TextView list_keys, list_values;

	}
	
	

	public static ListView Stock(final Context context, ListView list,
			final ArrayList<CustomerStockInfo> l) {
		list.setAdapter(new BaseAdapter() {
			Viewholder viewholder;

			@Override
			public int getCount() {
				return l.size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					viewholder = new Viewholder();
					convertView = View.inflate(context,
							R.layout.item_ten_stock, null);
					viewholder.name = (TextView) convertView
							.findViewById(R.id.name);
					viewholder.count = (TextView) convertView
							.findViewById(R.id.count);
					viewholder.change = (TextView) convertView
							.findViewById(R.id.change);
					convertView.setTag(viewholder);
				} else {
					viewholder = (Viewholder) convertView.getTag();
				}
				CustomerStockInfo Stock = l.get(position);
				viewholder.name.setText(Stock.name);
				viewholder.count.setText(Stock.stockQty);
				viewholder.change.setText(Stock.stockChange);
				return convertView;
			}

			@Override
			public Object getItem(int position) {
				return l.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			class Viewholder {
				TextView name;
				TextView count;
				TextView change;
			}

		});
		return list;

	}

}
