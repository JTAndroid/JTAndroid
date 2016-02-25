package com.tr.ui.common;

import java.util.ArrayList;
import java.util.HashMap;

import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.model.joint.JointResource;
import com.tr.model.joint.JointResource_kno;
import com.tr.model.joint.JointResource_org;
import com.tr.model.joint.JointResource_people;
import com.tr.model.joint.JointResource_req;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.NewJointResourceFragment.ResourceType_new;
import com.tr.ui.common.NewJointResourceMainFragment.ResourceSource;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EConsts;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 对接资源
 * 
 * @author leon
 */
public class MoreJointResourceActivity extends JBaseFragmentActivity implements
		IBindData {

	private TextView titleTv;
	private XListView xlv;

	private ResourceAdapter mAdapter; // 适配器

	private ArrayList<JointResource_kno> knos;
	private ArrayList<JointResource_org> orgs;
	private ArrayList<JointResource_people> peoples;
	private ArrayList<JointResource_req> reqs;
	
	private int kno_total;
	private int org_total;
	private int peo_total;
	private int req_total;
	
	private String userid;
	private ResourceType_new mTarResType, flag;
	private ResourceSource mResSrc;
	private int index = 0;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "资源对接",
				false, null, false, true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_joint_resource_more);
		getBundle();
		intVars();
		getData();
	}
	
	private void getBundle(){
		mTarResType = (ResourceType_new) getIntent().getSerializableExtra(EConsts.Key.RELATED_RESOURCE_TYPE); // 目标资源类型
		flag = (ResourceType_new) getIntent().getSerializableExtra(EConsts.Key.TARGET_RESOURCE_TYPE); // 目标资源类型
		mResSrc = (ResourceSource) getIntent().getSerializableExtra(EConsts.Key.RELATED_RESOURCE_SOURCE); // 对接资源来源
		userid = getIntent().getStringExtra(EConsts.Key.ID);
	}

	private void intVars() {
		titleTv = (TextView) findViewById(R.id.titleTv);
		xlv = (XListView) findViewById(R.id.xlv);
		
		peoples = new ArrayList<JointResource_people>();
		orgs = new ArrayList<JointResource_org>();
		knos = new ArrayList<JointResource_kno>();
		reqs = new ArrayList<JointResource_req>();
		
		mAdapter = new ResourceAdapter(this, flag);
		xlv.setAdapter(mAdapter);
		xlv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				index = 0;
				getData();
			}
			
			@Override
			public void onLoadMore() {
				getData();
			}
		});
		xlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				switch(flag){
				case Affair:
					ENavigate.startNeedDetailsActivity(MoreJointResourceActivity.this,reqs.get(position-1).getId(),2);
					break;
				case People:
					if(peoples.get(position-1).getPersonType().equals("1")){
						ENavigate.startRelationHomeActivity(MoreJointResourceActivity.this,peoples.get(position-1).getId(),true,1);
					}else{
						ENavigate.startRelationHomeActivity(MoreJointResourceActivity.this,peoples.get(position-1).getId(),false,2);
					}
					break;
				case Organization:
					if (TextUtils.isEmpty(orgs.get(position-1).getCreateUserId()))// 组织
						ENavigate.startOrgMyHomePageActivity(MoreJointResourceActivity.this, Long.valueOf(orgs.get(position-1).getId()), Long.valueOf(orgs.get(position-1).getCreateUserId()), true, ENavConsts.type_details_org);
					else
						ENavigate.startClientDedailsActivity(MoreJointResourceActivity.this, Long.valueOf(orgs.get(position-1).getId()));
					break;
				case Knowledge:
					ENavigate.startKnowledgeOfDetailActivity(MoreJointResourceActivity.this, Long.valueOf(knos.get(position-1).getId()), 
							Integer.valueOf(knos.get(position-1).getKnowledgeType()));
					break;
				default:
					break;
				}
			}
		});
	}

	private void getData() {
		CommonReqUtil.doGetJointResource_new(this,this, userid,convertResourceType2Int(mTarResType), convertResourceSource2Int(mResSrc), 20, index++, null);
	}
	
	// 资源类型转为int
	public static int convertResourceType2Int(ResourceType_new resType){
		int type = 0;
		//新"targetType":[
//		        1  知识
//		        2  需求
//		        3  组织
//		        4  人脉
//		        5  会议],
		switch(resType){
		case Affair:
			type = 2;
			break;
		case People:
			type = 4;
			break;
		case Organization:
			type = 3;
			break;
		case Knowledge:
			type = 1;
			break;
		default:
			break;
		}
		return type;
	}
	
	// 对接来源转为int
	public static int convertResourceSource2Int(ResourceSource resSrc){
		int src = 0;
		switch(resSrc){
		case My:
			src = 1;
			break;
		case Friend:
			src = 2;
			break;
		case Platform:
			src = 3;
			break;
		}
		return src;
	}
	
	private void refreshUI(ArrayList<JointResource_kno> knos,
			ArrayList<JointResource_org> orgs,
			ArrayList<JointResource_people> peoples,
			ArrayList<JointResource_req> reqs) {
		// 需求
		titleTv.setText(getGroupTitleSpannableString(flag));
		mAdapter.notifyDataSetChanged(); 
	}
	
	// 获取组标题样式字符串
	private SpannableString getGroupTitleSpannableString(ResourceType_new resType){
		
		SpannableString spanString = null;
		int count = 0;
		switch(resType){
		case Affair:
			count = reqs.size();
			if(reqs.size()<req_total){
				xlv.setPullLoadEnable(true);
			}else{
				xlv.setPullLoadEnable(false);
			}
			spanString = new SpannableString(String.format("有%d个需求可对接", count)); 
			break;
		case People:
			count = peoples.size();
			if(peoples.size()<peo_total){
				xlv.setPullLoadEnable(true);
			}else{
				xlv.setPullLoadEnable(false);
			}
			spanString = new SpannableString(String.format("有%d个人可对接", count)); 
			break;
		case Organization:
			count = orgs.size();
			if(orgs.size()<org_total){
				xlv.setPullLoadEnable(true);
			}else{
				xlv.setPullLoadEnable(false);
			}
			spanString = new SpannableString(String.format("有%d个组织可对接", count)); 
			break;
		case Knowledge:
			count = knos.size();
			if(knos.size()<kno_total){
				xlv.setPullLoadEnable(true);
			}else{
				xlv.setPullLoadEnable(false);
			}
			spanString = new SpannableString(String.format("有%d个知识可对接", count)); 
			break;
		default:
			break;
		}
	    ForegroundColorSpan span = new ForegroundColorSpan(0xfff37800);    
		spanString.setSpan(span, 1, 1 + (count + "").length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return spanString;
	}
	class ResourceAdapter extends BaseAdapter {

		private Context mContext;
		private ResourceType_new resType; // 资源类型

		public ResourceAdapter(Context context, ResourceType_new resType) {
			this.mContext = context;
			this.resType = resType;
		}

		@Override
		public int getCount() {
			switch (resType) {
			case Affair:
				return reqs.size();
			case People:
				return peoples.size();
			case Organization:
				return orgs.size();
			case Knowledge:
				return knos.size();
			default:
				break;
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			switch (resType) {
			case Affair:
				return reqs.get(position);
			case People:
				return peoples.get(position);
			case Organization:
				return orgs.get(position);
			case Knowledge:
				return knos.get(position);
			default:
				break;
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Object obj = getItem(position);
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				switch (resType) {
				case People:
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.list_item_related_resource_people, parent,
							false);
					holder.init(convertView);
					break;
				case Organization:
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.list_item_related_resource_organization,
							parent, false);
					holder.init(convertView);
					break;
				case Knowledge:
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.list_item_related_resource_knowledge,
							parent, false);
					holder.init(convertView);
					break;
				case Affair:
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.list_item_related_resource_affair, parent,
							false);
					holder.init(convertView);
					break;
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.build(resType, obj);
			return convertView;
		}

		class ViewHolder {

			CheckBox selectCb;
			ImageView avatarIv;
			TextView titleTv;
			TextView tagTv;
			TextView timeTv;
			TextView authorTv;
			TextView nameTv;
			// TextView moreTv; // 更多
			ProgressBar loadingPb; // 加载框
			ImageView tagIv;

			public void init(View container) {
				selectCb = (CheckBox) container.findViewById(R.id.selectCb);
				selectCb.setVisibility(View.GONE);
				avatarIv = (ImageView) container.findViewById(R.id.avatarIv);
				titleTv = (TextView) container.findViewById(R.id.titleTv);
				tagTv = (TextView) container.findViewById(R.id.tagTv);
				timeTv = (TextView) container.findViewById(R.id.timeTv);
				authorTv = (TextView) container.findViewById(R.id.authorTv);
				nameTv = (TextView) container.findViewById(R.id.nameTv);
				loadingPb = (ProgressBar) container
						.findViewById(R.id.loadingPb);
				tagIv = (ImageView) container.findViewById(R.id.tagIv);
			}

			public void build(ResourceType_new resType, Object obj) {
				switch (resType) {
				case People:
					if (avatarIv == null || titleTv == null) {
						return;
					}
					JointResource_people people = (JointResource_people) obj;
					titleTv.setText(people.getName());
					if (!TextUtils.isEmpty(people.getPortrait())) {
						Util.initAvatarImage(mContext, avatarIv, titleTv
								.getText().toString(), people.getPortrait(), 1,
								1);
					} else {
						avatarIv.setImageResource(R.drawable.ic_default_avatar);
					}
					if (people.getPersonType().equals("1")) {
						tagIv.setImageResource(R.drawable.contactusertag);
					} else {
						tagIv.setImageResource(R.drawable.contactpeopletag);
					}
					// if(source == 0){
					// tagTv.setText("");
					// tagTv.setVisibility(View.GONE);
					// }
					// else{
					// tagTv.setText(people.isOnline() ? "我的好友" : "我的人脉");
					// }
					break;
				case Organization:
					JointResource_org org = (JointResource_org) obj;
					titleTv.setText(!TextUtils.isEmpty(org.getName()) ? org
							.getShotName() : org.getName());
					if (!TextUtils.isEmpty(org.getPicLogo())) {
						Util.initAvatarImage(mContext, avatarIv, titleTv
								.getText().toString(), org.getPicLogo(), 1, 1);
					} else {
						avatarIv.setImageResource(R.drawable.default_portrait116);
					}
					// if(organization.getOrganizationMini().isOnline){
					tagIv.setImageResource(R.drawable.contactorganizationtag);
					// }else{
					// tagIv.setImageResource(R.drawable.contactclienttag);
					// }
					tagTv.setText("");
					break;
				case Affair:
					JointResource_req req = (JointResource_req) obj;
					titleTv.setText(req.getName());
					// if(source == 0){
					// tagTv.setText(affair.name);
					// }
					// else{
					tagTv.setText(req.getIndustry());
					// }
					// timeTv.setText(affair.time);
					break;
				case Knowledge:
					JointResource_kno kno = (JointResource_kno) obj;
					titleTv.setText(kno.getTitle());
					// if(source == 0){
					// tagTv.setText(knowledge.desc);
					// }
					// else{
					tagTv.setText(kno.getTags());
					// }
					// timeTv.setText(knowledge.modifytime);
					// nameTv.setText(knowledge.connections != null ?
					// knowledge.connections.getMiniName():"");
					break;
				default:
					break;
				}
			}
		}

	}

	@Override
	public void bindData(int tag, Object object) {
		xlv.stopLoadMore();
		xlv.stopRefresh();
		switch(tag){
		case EAPIConsts.CommonReqType.GetJointResource_MY:
		case EAPIConsts.CommonReqType.GetJointResource_FRIEND:
		case EAPIConsts.CommonReqType.GetJointResource_GT:
			if(index == 1){
				knos.clear();
				orgs.clear();
				peoples.clear();
				reqs.clear();
			}
			if(object != null){
				HashMap<String, Object> databox = (HashMap<String, Object>) object;
				JointResource jr = (JointResource) databox.get("JointResource");
				knos.addAll(jr.getKnos());
				orgs.addAll(jr.getOrgs());
				peoples.addAll(jr.getPeoples());
				reqs.addAll(jr.getReqs());
				
				kno_total = jr.getKnosTotal();
				org_total = jr.getOrgsTotal();
				peo_total = jr.getPeoplesTotal();
				req_total = jr.getReqsTotal();
				
				refreshUI(knos, orgs, peoples, reqs);
			}
			break;
		}
	}
}
