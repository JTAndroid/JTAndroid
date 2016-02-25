package com.tr.ui.conference.initiatorhy;

import java.util.List;

import com.tr.R;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.ui.adapter.conference.ListViewSahreKnowleadgeAdapter;
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

public class ShareKnowleadgeFragment extends Fragment implements IXListViewListener{
	private Context context;
	public XListView knowleadgeLv;
	public ListViewSahreKnowleadgeAdapter knowleadgeLvAdp;
	public ShareKnowleadgeFragment(Context context){
		this.context = context;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.hy_layout_sharetab_knowledge, container, false);
		findAndInitViews(v);
		return v;
	}
	private void findAndInitViews(View v){
		knowleadgeLv = (XListView)v.findViewById(R.id.hy_layoutShareTab_knowledge_listview);
		knowleadgeLvAdp = new ListViewSahreKnowleadgeAdapter(context, InitiatorDataCache.getInstance().shareKnowleadgeList);
		knowleadgeLv.showFooterView(false);
		knowleadgeLv.setPullRefreshEnable(false);
		knowleadgeLv.setPullLoadEnable(false);
		knowleadgeLv.setXListViewListener(this);
		knowleadgeLv.setAdapter(knowleadgeLvAdp);
		knowleadgeLv.setOnItemClickListener(new MyOnItemClickListener());
	}
	public void update(List<KnowledgeMini2> dataList){
		if(knowleadgeLvAdp != null){
			knowleadgeLvAdp.update(dataList);
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
		if(InitiatorDataCache.getInstance().shareKnowleadgeList.size()%30 != 0){
			return;
		}
		shateAct.startGetKnowleadgeList(InitiatorDataCache.getInstance().shareKnowleadgeList.size()/30 + 1);
	}
	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2!=0) {
				// TODO Auto-generated method stub
				KnowledgeMini2 item = knowleadgeLvAdp.getKnowleadgeList().get(arg2 - 1);
				ImageView cb = (ImageView) arg1.findViewById(R.id.hy_item_shareCheck_checkbox);
				if(InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.containsKey(item.id)){
					cb.setImageResource(R.drawable.hy_check_norm);
					InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.remove(item.id);
				}else{
					cb.setImageResource(R.drawable.hy_check_pressed);
					InitiatorDataCache.getInstance().shareKnowleadgeSelectedMap.put(item.id, item);
				}
			}
		}
	}
}
