package com.tr.ui.connections.detail;

import java.util.ArrayList;
import java.util.List;

import com.tr.R;
import com.tr.model.model.PeopleAddress;
import com.tr.model.model.PeopleDemandCommon;
import com.tr.model.model.PeoplePersonalLine;
import com.tr.model.model.PeopleSelectTag;
import com.tr.ui.base.JBaseFragment;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 投融资意、专家列表
 * @author leon
 *
 */
public class DemandListFragment extends JBaseFragment {

	private final String TAG = getClass().getSimpleName();
	
	private ListView demandLv;
	private List<PeopleDemandCommon> listDemand;
	private Activity context;
	private DemandAdapter adapter;

	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		listDemand = (ArrayList<PeopleDemandCommon>) getArguments().getSerializable("listDemand");
		if(listDemand == null){
			listDemand = new ArrayList<PeopleDemandCommon>();
		}
		context = getActivity();
		adapter = new DemandAdapter();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup root,Bundle savedInstance){
		return inflater.inflate(R.layout.conns_detail_frg_list_demand, root, false);
	}
	
	@Override
	public void onViewCreated(View root,Bundle savedInstanceState){
		demandLv = (ListView) root.findViewById(R.id.demandLv);
		demandLv.setAdapter(adapter);
	}
	
	class DemandAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return listDemand.size();
		}

		@Override
		public Object getItem(int position) {
			return listDemand.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PeopleDemandCommon demand = listDemand.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.conns_detail_list_item_demand, null);
				holder.typeIv = (ImageView) convertView.findViewById(R.id.typeIv);
				holder.areaTv = (TextView) convertView.findViewById(R.id.areaTv);
				holder.tradeTv = (TextView) convertView.findViewById(R.id.tradeTv);
				holder.typeTv = (TextView) convertView.findViewById(R.id.typeTv);
				holder.extraTv = (TextView) convertView.findViewById(R.id.extraTv);
				holder.customLl = (LinearLayout) convertView.findViewById(R.id.customLl);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			// 投融资类型
			if(demand.parentType != null){
				
				if(demand.parentType == 1){
					holder.typeIv.setImageResource(R.drawable.conns_ic_touzi);
				}
				else if(demand.parentType == 2){
					holder.typeIv.setImageResource(R.drawable.conns_ic_rongzi);
				}
				else if(demand.parentType == 3){
					holder.typeIv.setImageResource(R.drawable.conns_ic_zhuanjia_demand);
				}
				else if(demand.parentType == 4){
					holder.typeIv.setImageResource(R.drawable.conns_ic_zhuanjia);
				}
			}
			// 区域
			if(demand.address != null){
				String area = "";
				if(demand.address.stateName != null){
					area += demand.address.stateName;
				}
				if(demand.address.cityName != null){
					area += demand.address.cityName;
				}
				if(demand.address.countyName != null){
					area += demand.address.countyName;
				}
				holder.areaTv.setText(area);
			}
			else{
				holder.areaTv.setText("");
			}
			// 行业
			if(demand.industryNames != null){
				holder.tradeTv.setText(demand.industryNames.replace(",", " "));
			}
			else{
				holder.tradeTv.setText("");
			}
			// 类型
			if(demand.typeNames != null){
				holder.typeTv.setText(demand.typeNames.replace(",", " "));
			}
			else{
				holder.typeTv.setText("");
			}
			// 附加消息
			if(demand.otherInformation != null){
				holder.extraTv.setText(demand.otherInformation);
			}
			else{
				holder.extraTv.setText("");
			}
			// 自定义字段
			holder.customLl.removeAllViews();
			if(demand.personalLineList != null
					&& demand.personalLineList.size() > 0){
				for(PeoplePersonalLine tag : demand.personalLineList){
					LinearLayout parentLl = new LinearLayout(context);
					parentLl.setOrientation(LinearLayout.HORIZONTAL);
					TextView titleTv = new TextView(context);
					titleTv.setTextAppearance(context, R.style.common_text_style_large_black);
					titleTv.setText(tag.name + "：");
					parentLl.addView(titleTv);
					TextView contentTv = new TextView(context);
					contentTv.setTextAppearance(context, R.style.common_text_style_large_gray);
					contentTv.setText(tag.content);
					parentLl.addView(contentTv);
					// 设置参数
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					lp.setMargins(0, 5, 0, 0);
					parentLl.setLayoutParams(lp);
					holder.customLl.addView(parentLl);
				}
			}
			return convertView;
		}
		
		class ViewHolder{
			ImageView typeIv;
			TextView areaTv;
			TextView tradeTv;
			TextView typeTv;
			TextView extraTv;
			LinearLayout customLl;
		}
	}
}
