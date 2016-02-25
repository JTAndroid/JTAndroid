package com.tr.ui.connections.detail;

import com.tr.R;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.tr.api.HomeReqUtil;
import com.tr.model.home.MGetMyRequirement;
import com.tr.model.obj.RequirementMini;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.*;
import com.tr.ui.widgets.ListFooter;
import com.tr.ui.widgets.ListFooter.OnLoadMoreListener;
import com.utils.http.IBindData;

/**
 * 发布的需求列表
 * @author leon
 *
 */
public class RequirementListFragment extends JBaseFragment {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private SwipeRefreshLayout refreshSrl;
	private ListView requirementLv;
	private ListFooter moreLf;
	// 变量
	private List<RequirementMini> listRequirement;
	private RequirementAdapter adapter;
	private Activity context;
	private int pageIndex = 0;
	private int pageSize = 20;
	private int pageTotal = 0;
	private int total = 0;
	private String id = "";
	private boolean isLoading; // 下拉刷新或加载更多状态
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		listRequirement = new ArrayList<RequirementMini>();
		context = getActivity();
		id = getArguments().getString("id");
		adapter = new RequirementAdapter();
		isLoading = false;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup root,Bundle savedInstance){
		return inflater.inflate(R.layout.conns_detail_frg_list_requirement, root, false);
	}
	
	@Override
	public void onViewCreated(View root,Bundle savedInstanceState){
		
		refreshSrl = (SwipeRefreshLayout) root.findViewById(R.id.refreshSrl);
		refreshSrl.setColorSchemeResources(R.color.common_orange, R.color.common_orange1, R.color.common_orange2, R.color.common_orange3);
		refreshSrl.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				if(!isLoading){
					isLoading = true;
					HomeReqUtil.getListRequirementByUserID(context, bindData, null, id, 0, pageSize);
				}
			}
		});
		// 页脚
		moreLf = new ListFooter(context);
		moreLf.setOnLoadMoreListener(new OnLoadMoreListener(){

			@Override
			public void loadMore() {
				if(!isLoading){
					isLoading = true;
					HomeReqUtil.getListRequirementByUserID(context, bindData, null, id, pageIndex + 1, pageSize);
				}
			}
		});
		// 需求列表
		requirementLv = (ListView) root.findViewById(R.id.requirementLv);
		requirementLv.setAdapter(adapter);
		requirementLv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
			}
		});
		// 请求数据
		refreshSrl.setRefreshing(true);
		isLoading = true;
		HomeReqUtil.getListRequirementByUserID(context, bindData, null, id, pageIndex, pageSize);
	}
	
	private IBindData bindData = new IBindData(){

		@Override
		public void bindData(int tag, Object object) {
			
			isLoading = false;
			
			if(refreshSrl.isRefreshing()){
				refreshSrl.setRefreshing(false);
			}
			
			// 不再刷新
			moreLf.setLoading(false);
			
			if(object != null){
			
				MGetMyRequirement getMyRequirement = (MGetMyRequirement) object;

				total = getMyRequirement.getJtPage().getTotal(); // 条目总数
				pageTotal = (int) Math.ceil(total * 1.0/ pageSize);
				pageIndex = getMyRequirement.getJtPage().getIndex(); // 当前页
				
				if(pageIndex <= 0){ // 刷新
					listRequirement.clear();
				}
				
				for (int i = 0; i < getMyRequirement.getJtPage().getLists().size(); i++) {
					RequirementMini reqMini = (RequirementMini) getMyRequirement.getJtPage().getLists().get(i);
					listRequirement.add(reqMini);
				}

				if(pageIndex + 1 >= pageTotal){ // 没有更多
					if(requirementLv.getFooterViewsCount() > 0){
						requirementLv.removeFooterView(moreLf);
					}
					requirementLv.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
				else{
					if(requirementLv.getFooterViewsCount() > 0){
						moreLf.setLoading(false);
					}
					else{
						requirementLv.addFooterView(moreLf);
					}
					requirementLv.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
				
				requirementLv.setSelection(pageIndex * pageSize);
			}
			else{
				
			}
		}
	};
	
	class RequirementAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return listRequirement.size();
		}

		@Override
		public Object getItem(int position) {
			return listRequirement.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			RequirementMini req = listRequirement.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.conns_detail_list_item_requirement, null);
				holder.typeIv = (ImageView) convertView.findViewById(R.id.typeIv);
				holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
				holder.datetimeTv = (TextView) convertView.findViewById(R.id.datetimeTv);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			// 投融资类型
			if(req.mTypeName.equals("我要投资")){
				holder.typeIv.setImageResource(R.drawable.conns_ic_touzi);
			}
			else{
				holder.typeIv.setImageResource(R.drawable.conns_ic_rongzi);
			}
			// 标题
			holder.titleTv.setText(req.mTitle);
			// 时间
			holder.datetimeTv.setText(req.mTime);
			return convertView;
		}
		
		class ViewHolder{
			ImageView typeIv;
			TextView titleTv;
			TextView datetimeTv;
		}
	}
}
