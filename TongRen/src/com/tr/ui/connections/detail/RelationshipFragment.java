package com.tr.ui.connections.detail;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.model.model.PeopleRelatedResource;
import com.tr.model.model.ResourceMini;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.*;
import com.tr.ui.widgets.ExpandableHeightGridView;
import com.utils.common.EUtil;
import com.utils.http.IBindData;

/**
 * 人脉资源-关系(与我的关系、与我好友的关系、与金桐网的关系)
 * @author leon
 *
 */
public class RelationshipFragment extends JBaseFragment{

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private LinearLayout peoParentLl;
	private ExpandableHeightGridView peoGv;
	private LinearLayout reqParentLl;
	private ListView reqLv;
	private LinearLayout knoParentLl;
	private ListView knoLv;
	private ProgressBar loadingPb;
	
	// 变量
	private List<ResourceMini> listPeo;
	private List<ResourceMini> listReq;
	private List<ResourceMini> listKno;
	private PeopleAdapter peoAdapter;
	private RequirementAdapter reqAdapter;
	private KnowledgeAdapter knoAdapter;
	private String id;
	private boolean isOnline;
	private String range;
	private Activity context;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initVars();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup root,Bundle savedInstance){
		return inflater.inflate(R.layout.conns_detail_frg_relationship, root, false);
	}
	
	@Override
	public void onViewCreated(View root,Bundle savedInstanceState){
		// 初始化控件
		initControls(root);
		// 进行网络请求
		ConnectionsReqUtil.getPeopleRelatedResources(context, bind,
				ConnectionsReqUtil.getPeopleRelatedResourcesJson(id, isOnline ? "user" : "people", range), null);
	}
	
	private void initVars(){
		context = getActivity();
		Bundle bundle = getArguments();
		id = bundle.getString("id");
		isOnline = bundle.getBoolean("isOnline");
		range = bundle.getString("range");
		listPeo = new ArrayList<ResourceMini>();
		listReq = new ArrayList<ResourceMini>();
		listKno = new ArrayList<ResourceMini>();
		peoAdapter = new PeopleAdapter();
		reqAdapter = new RequirementAdapter();
		knoAdapter = new KnowledgeAdapter();
	}
	
	private void initControls(View root){
		
		// 加载框
		loadingPb = (ProgressBar) root.findViewById(R.id.loadingPb);
		// 相关人脉
		peoParentLl = (LinearLayout) root.findViewById(R.id.peoParentLl);
		peoGv = (ExpandableHeightGridView) root.findViewById(R.id.peoGv);
		peoGv.setAdapter(peoAdapter);
		peoGv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ENavigate.startUserDetailsActivity(context, listPeo.get(arg2).id, listPeo.get(arg2).excepted.equals("user") ? true : false, ENavConsts.type_details_recommend);
			}
		});
		// 相关需求
		reqParentLl = (LinearLayout) root.findViewById(R.id.reqParentLl);
		reqLv = (ListView) root.findViewById(R.id.reqLv);
		reqLv.setAdapter(reqAdapter);
		reqLv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
			}
		});
		// 相关知识
		knoParentLl = (LinearLayout) root.findViewById(R.id.knoParentLl);
		knoLv = (ListView) root.findViewById(R.id.knoLv);
		knoLv.setAdapter(knoAdapter);
		knoLv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// ENavigate.startKnowledgeDetailActivity(context, listKno.get(arg2).getId());
			}
		});
	}
	
	private IBindData bind = new IBindData(){

		@Override
		public void bindData(int tag, Object object) {
			
			loadingPb.setVisibility(View.GONE);
			
			if(object != null){
				
				PeopleRelatedResource res = (PeopleRelatedResource) object;
				if(res.listPeople != null
						&& res.listPeople.size() > 0){
					listPeo = res.listPeople;
					peoAdapter.notifyDataSetChanged();
					peoGv.setVisibility(View.VISIBLE);
				}
				else{
					peoGv.setVisibility(View.GONE);
				}
				if(res.listRequirement != null
						&& res.listRequirement.size() > 0){
					listReq = res.listRequirement;
					reqAdapter.notifyDataSetChanged();
					EUtil.setListViewHeightBasedOnChildren(reqLv);
					reqLv.setVisibility(View.VISIBLE);
				}
				else{
					reqLv.setVisibility(View.GONE);
				}
				if(res.listKnowledge != null
						&& res.listKnowledge.size() > 0){
					listKno = res.listKnowledge;
					knoAdapter.notifyDataSetChanged();
					EUtil.setListViewHeightBasedOnChildren(knoLv);
					knoLv.setVisibility(View.VISIBLE);
				}				
				else{
					knoLv.setVisibility(View.GONE);
				}
			}
			else{
				
			}
		}
	};
	
	// 人脉
	class PeopleAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return listPeo.size();
		}

		@Override
		public Object getItem(int position) {
			return listPeo.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ResourceMini peo = listPeo.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.conns_detail_list_item_related_people, null);
				holder.avatarIv = (ImageView) convertView.findViewById(R.id.avatarIv);
				holder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
				holder.reasonTv = (TextView) convertView.findViewById(R.id.reasonTv);
				holder.rangeTv = (TextView) convertView.findViewById(R.id.rangeTv);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(peo.avatar, holder.avatarIv);
			holder.nameTv.setText(peo.name);
			holder.reasonTv.setText(peo.reason);
			holder.rangeTv.setText(peo.range);
			return convertView;
		}
		
		class ViewHolder{
			ImageView avatarIv;
			TextView nameTv;
			TextView reasonTv;
			TextView rangeTv;
		}
	}

	// 需求
	class RequirementAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return listReq.size();
		}

		@Override
		public Object getItem(int position) {
			return listReq.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ResourceMini req = listReq.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.conns_detail_list_item_related_req, null);
				holder.typeIv = (ImageView) convertView.findViewById(R.id.typeIv);
				holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
				holder.datetimeTv = (TextView) convertView.findViewById(R.id.datetimeTv);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.titleTv.setText(req.name);
			return convertView;
		}
		
		class ViewHolder{
			ImageView typeIv;
			TextView titleTv;
			TextView datetimeTv;
		}
	}
	
	// 知识
	class KnowledgeAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return listKno.size();
		}

		@Override
		public Object getItem(int position) {
			return listKno.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ResourceMini kno = listKno.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.conns_detail_list_item_related_kno, null);
				holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
				holder.sourceTv = (TextView) convertView.findViewById(R.id.sourceTv);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.titleTv.setText(kno.name); // 标题
			holder.sourceTv.setText("[" + kno.reason + "]"); // 知识来源
			return convertView;
		}
		
		class ViewHolder{
			TextView titleTv;
			TextView sourceTv;
		}
	}
}
