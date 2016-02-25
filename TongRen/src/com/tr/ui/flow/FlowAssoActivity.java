package com.tr.ui.flow;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.TextView;

import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.model.demand.DemandASSOData;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.flow.model.FlowASSO;
import com.tr.ui.flow.model.FlowResult;
import com.tr.ui.home.HomePageActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.HorizontalListView;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class FlowAssoActivity extends JBaseFragmentActivity implements IBindData{

	private Context mContext;
	
	private long dynamicnewId;
	private LinearLayout personll,orgall,knowll,thingll;
	private HorizontalListView personGv,orgaGv;
	private ListView knowLv,thingLv;
	
	private FlowResult result;
	ConnectionsGroupAdapter peopleAdapter ;
	ConnectionsGroupAdapter organizationAdapter ;
	KnowledgeAffairGroupAdapter knowledgeGroupAdapter;
	KnowledgeAffairGroupAdapter affriaGroupAdapter;
	
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "关联关系", false, null, false, true);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_frg_flow_link);
		mContext = this;
		initView();
		initData();
	}
	
	private void initView(){
		personll = (LinearLayout) findViewById(R.id.personll);
		orgall = (LinearLayout) findViewById(R.id.orgall);
		knowll = (LinearLayout) findViewById(R.id.knowll);
		thingll = (LinearLayout) findViewById(R.id.thingll);
		
		personGv = (HorizontalListView) findViewById(R.id.personGv);
		orgaGv = (HorizontalListView) findViewById(R.id.orgaGv);
		
		knowLv = (ListView) findViewById(R.id.knowLv);
		thingLv = (ListView) findViewById(R.id.thingLv);
	}
	
	private void initData(){
		dynamicnewId = getIntent().getLongExtra("id", 0);
		
		peopleAdapter = new ConnectionsGroupAdapter();
		organizationAdapter = new ConnectionsGroupAdapter();
		knowledgeGroupAdapter = new KnowledgeAffairGroupAdapter();
		affriaGroupAdapter = new KnowledgeAffairGroupAdapter();
		
		personGv.setAdapter(peopleAdapter);
		orgaGv.setAdapter(organizationAdapter);
		knowLv.setAdapter(knowledgeGroupAdapter);
		thingLv.setAdapter(affriaGroupAdapter);
		
		HomeReqUtil.getAssoByDynamicId(this, this, null, dynamicnewId);
		
		personGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				DemandASSOData assoData = peopleAdapter.getItem(position);
				if (assoData.type == 2) {
					long assoDataID = TextUtils.isEmpty(assoData.id) ? 0L
							: Long.parseLong(assoData.id);
					ENavigate.startContactsDetailsActivity(mContext, 2,
							assoDataID, 0, 1);
				}
				if (assoData.type == 3) {
//					ENavigate.startRelationHomeActivity(mContext,
//							assoData.id, true, ENavConsts.type_details_other);
					Intent intent = new Intent(FlowAssoActivity.this, HomePageActivity.class);
					intent.putExtra(EConsts.Key.ID, assoData.id);
					startActivity(intent);
				}
				
			}
		});
		
		orgaGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DemandASSOData org_assoData = organizationAdapter.getItem(position);
				if (org_assoData.type == 4) {
					ENavigate.startOrgMyHomePageActivity(mContext,
							Long.parseLong(org_assoData.id),
							Long.parseLong(org_assoData.ownerid), false, 2);
				} else if (org_assoData.type == 5) {
					ENavigate.startClientDedailsActivity(mContext,
							Long.parseLong(org_assoData.id), 1, 6);
				}
			}
		});
		
		knowLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				DemandASSOData knowledgeNode = (DemandASSOData) knowledgeGroupAdapter.getItem(arg2);
				// 知识
				ENavigate.startKnowledgeOfDetailActivity(FlowAssoActivity.this,
						Long.parseLong(knowledgeNode.id),1
						/*Integer.parseInt(asso.columntype)*/);
			}
		});
		thingLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				DemandASSOData affairNode = (DemandASSOData) affriaGroupAdapter.getItem(arg2);
				ENavigate.startNeedDetailsActivity(mContext,
						affairNode.id, 1); // 跳转到需求详情界面
			}
		});
		
	}

	@Override
	public void bindData(int tag, Object object) {
		if(object == null){
			return;
		}
		if(tag == EAPIConsts.HomeReqType.HOME_REQ_GET_DYNAMIC_ASSO){
			result = (FlowResult) object;
			FlowASSO asso = result.getResult();
			if(asso.getP().size()>0){
				personll.setVisibility(View.VISIBLE);
				peopleAdapter.setAssoList(asso.getP().get(0).conn);
				peopleAdapter.notifyDataSetChanged();
			}
			if(asso.getO().size()>0){
				orgall.setVisibility(View.VISIBLE);
				organizationAdapter.setAssoList(asso.getO().get(0).conn);
				organizationAdapter.notifyDataSetChanged();
			}
			if(asso.getK().size()>0){
				knowll.setVisibility(View.VISIBLE);
				knowledgeGroupAdapter.setListRelatedKnowledgeNode(asso.getK().get(0).conn);
				knowledgeGroupAdapter.notifyDataSetChanged();
			}
			if(asso.getR().size()>0){
				thingll.setVisibility(View.VISIBLE);
				affriaGroupAdapter.setListRelatedKnowledgeNode(asso.getR().get(0).conn);
				affriaGroupAdapter.notifyDataSetChanged();
			}
			
		}
	}

	class ConnectionsGroupAdapter extends BaseAdapter{

		private List<DemandASSOData> assoList = new ArrayList<DemandASSOData>();
		
		public ConnectionsGroupAdapter(List<DemandASSOData> assoList) {
			this.assoList = assoList;
		}
		
		public List<DemandASSOData> getAssoList() {
			return assoList;
		}

		public void setAssoList(List<DemandASSOData> assoList) {
			this.assoList = assoList;
		}
		
		public ConnectionsGroupAdapter() {}
		
		@Override
		public int getCount() {
			return assoList.size();
		}

		@Override
		public DemandASSOData getItem(int position) {
			return assoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext,R.layout.demand_need_details_hori_item, null);
				holder. nameTv = (TextView) convertView.findViewById(R.id.nameTv);
				holder. describeTv = (TextView) convertView.findViewById(R.id.describeTv);
				holder. headImageIv = (ImageView) convertView.findViewById(R.id.headImgIv);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			DemandASSOData assoData = getItem(position);
			holder.nameTv.setText(assoData.name); // 姓名
			holder.describeTv.setText(assoData.tag); // 关系
			int type = 1;
			if (assoData.type==2||assoData.type==3) {
				type = 1;
			}else if(assoData.type==4||assoData.type==5) {
				type = 2;
			}
			com.utils.common.Util.initAvatarImage(mContext,holder.headImageIv,assoData.name,assoData.picPath,0, type);


			return convertView;
		}
		
		class ViewHolder {
			TextView nameTv;
			TextView describeTv;
			ImageView headImageIv;
		}
		
	}
	
	class KnowledgeAffairGroupAdapter extends BaseAdapter{

		private List<DemandASSOData> listRelatedKnowledgeNode = new ArrayList<DemandASSOData>();
		
		public KnowledgeAffairGroupAdapter() {}
		
		public KnowledgeAffairGroupAdapter(Context context, List<DemandASSOData> listRelatedKnowledgeNode) {
			this.listRelatedKnowledgeNode = listRelatedKnowledgeNode;
		}
		
		public List<DemandASSOData> getListRelatedKnowledgeNode() {
			return listRelatedKnowledgeNode;
		}
		
		public void setListRelatedKnowledgeNode(List<DemandASSOData> listRelatedKnowledgeNode) {
			this.listRelatedKnowledgeNode = listRelatedKnowledgeNode;
		}
		
		@Override
		public int getCount() {
			if (listRelatedKnowledgeNode!=null) {
				return listRelatedKnowledgeNode.size();
			}else{
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return listRelatedKnowledgeNode.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ViewHolder viewHolder;
			
			if(convertView == null ){
				viewHolder = new ViewHolder();
				
				convertView = LayoutInflater.from(mContext).inflate(R.layout.demand_need_details_label_info_item, null);
				viewHolder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
				viewHolder.contentsTv = (TextView) convertView.findViewById(R.id.contentsTv);
				convertView.setTag(viewHolder);
			}
			else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			
			DemandASSOData demandASSOData = (DemandASSOData) getItem(position);
			viewHolder.contentsTv.setText( demandASSOData.tag );
			viewHolder.titleTv.setText(demandASSOData.title);
			
			return convertView;
		}
		
		class ViewHolder {
			TextView titleTv;
			TextView contentsTv;
		}
		
	}
}
