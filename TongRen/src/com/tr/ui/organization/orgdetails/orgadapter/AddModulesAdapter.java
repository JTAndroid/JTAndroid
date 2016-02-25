package com.tr.ui.organization.orgdetails.orgadapter;

import java.util.List;

import com.tr.R;
import com.tr.ui.organization.model.resource.CustomerDemandCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddModulesAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<CustomerDemandCommon> customerList;
	
	private ViewHolder holder;
	
	public AddModulesAdapter(Context context,
			List<CustomerDemandCommon> customerList) {
		super();
		this.context = context;
		this.customerList = customerList;
	}

	@Override
	public int getCount() {
		return customerList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return customerList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			
			convertView = LayoutInflater.from(context).inflate(R.layout.org_showlistview_view, null);
			
			holder = new ViewHolder(convertView);
			
			convertView.setTag(holder);
			
		}
		
		holder = (ViewHolder) convertView.getTag();
		
		holder.areaTv.setText(customerList.get(position).getAddress().getCountyName());
		
		holder.areaNameTv.setText(customerList.get(position).getAddress().getAddress());
		
		holder.industryTv.setText(customerList.get(position).getIndustryIds());
		
		holder.industryNameTv.setText(customerList.get(position).getIndustryNames());
		
		holder.typeTv.setText(customerList.get(position).getTypeIds());
		
		holder.typeNameTv.setText(customerList.get(position).getTypeNames());
		
		holder.custom_FieldsTv.setText(customerList.get(position).getPersonalLineList().get(position).getName());
		
		holder.custom_ContentTv.setText(customerList.get(position).getPersonalLineList().get(position).getContent());
		
		
		return convertView;
	}
	
	public static class ViewHolder {

		private TextView areaTv,areaNameTv,industryTv,industryNameTv,typeTv,
		typeNameTv,custom_FieldsTv,custom_ContentTv;

		public ViewHolder(View convertView) {
			
			areaTv = (TextView) convertView.findViewById(R.id.areaTv);
			
			areaNameTv = (TextView) convertView.findViewById(R.id.areaNameTv);
			
			industryTv = (TextView) convertView.findViewById(R.id.industryTv);
			
			industryNameTv = (TextView) convertView.findViewById(R.id.industryNameTv);
			
			typeTv = (TextView) convertView.findViewById(R.id.typeTv);
			
			typeNameTv = (TextView) convertView.findViewById(R.id.typeNameTv);
			
			custom_FieldsTv = (TextView) convertView.findViewById(R.id.custom_FieldsTv);
			
			custom_ContentTv = (TextView) convertView.findViewById(R.id.custom_ContentTv);
			
			
		}

	}
}
