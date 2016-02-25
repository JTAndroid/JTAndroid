package com.tr.ui.conference.initiatorhy;

import java.util.List;

import com.tr.R;
import com.tr.model.obj.RequirementMini;
import com.tr.ui.adapter.conference.ListViewSahreDemandAdapter;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class ShareDemandFragment extends Fragment implements IXListViewListener{
	private Context context;
	public XListView demandLv;
	public ListViewSahreDemandAdapter demandLvAdp;
	
	public ShareDemandFragment(Context context){
		this.context = context;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.hy_layout_sharetab_demand, container, false);
		findAndInitViews(v);
		return v;
	}
	private void findAndInitViews(View v){
		demandLv = (XListView)v.findViewById(R.id.hy_layoutShareTab_demand_listview);
		demandLvAdp = new ListViewSahreDemandAdapter(context, InitiatorDataCache.getInstance().shareDemandList);
		demandLv.showFooterView(false);
		demandLv.setPullRefreshEnable(false);
		demandLv.setPullLoadEnable(false);
		demandLv.setXListViewListener(this);
		demandLv.setAdapter(demandLvAdp);
		demandLv.setOnItemClickListener(new MyOnItemClickListener());
	}
	public void update(List<RequirementMini> dataList){
		if(demandLvAdp != null){
			demandLvAdp.update(dataList);
		}
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		ShareActivity shateAct = (ShareActivity)context;
		if(InitiatorDataCache.getInstance().shareDemandList.size()%30 != 0){
			return;
		}
		shateAct.startGetDemandList(InitiatorDataCache.getInstance().shareDemandList.size()/30 + 1);
	}
	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2!=0) {
				// TODO Auto-generated method stub
				RequirementMini item = demandLvAdp.getDemandList().get(arg2 - 1);
				ImageView cb = (ImageView) arg1.findViewById(R.id.hy_item_shareCheck_checkbox);
				if(InitiatorDataCache.getInstance().shareDemandSelectedMap.containsKey(item.mID)) {
					cb.setImageResource(R.drawable.hy_check_norm);
					InitiatorDataCache.getInstance().shareDemandSelectedMap.remove(item.mID);
				} 
				else {
					cb.setImageResource(R.drawable.hy_check_pressed);
					InitiatorDataCache.getInstance().shareDemandSelectedMap.put(item.mID, item);
				}
			}
		}
	}
}
