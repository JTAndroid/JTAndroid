package com.tr.ui.common;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.model.joint.JointResource;
import com.tr.model.joint.JointResource_kno;
import com.tr.model.joint.JointResource_org;
import com.tr.model.joint.JointResource_people;
import com.tr.model.joint.JointResource_req;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.NewJointResourceMainFragment.ResourceSource;
import com.tr.ui.widgets.NoScrollListview;
import com.utils.common.EConsts;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 资源对接界面
 * 
 * @author leon
 */
public class NewJointResourceFragment extends JBaseFragment implements IBindData, OnClickListener {
	// 事件相关
	private LinearLayout affParentLl;
	private TextView affTitleTv, reqMore;
	private NoScrollListview affElv;
	// 人脉相关
	private LinearLayout peoParentLl;
	private TextView peoTitleTv, peoMore;
	private NoScrollListview peoElv;
	// 组织相关
	private LinearLayout orgParentLl;
	private TextView orgTitleTv, orgMore;
	private NoScrollListview orgElv;
	// 知识相关
	private LinearLayout knoParentLl;
	private TextView knoTitleTv, knoMore;
	private NoScrollListview knoElv;
	
	// 变量
	private ResourceSource mResSrc; // 资源来源
	private ResourceType_new mTarResType; // 目标资源类型
	private int currentPage;//要显示的frg
	private String userid;
	
	private ResourceAdapter mAffairAdapter; // 事件适配器
	private ResourceAdapter mPeopleAdapter; // 人脉适配器
	private ResourceAdapter mOrganizationAdapter; // 组织适配器
	private ResourceAdapter mKnowledgeAdapter; // 知识适配器
	
	private ArrayList<JointResource_kno> knos;
	private ArrayList<JointResource_org> orgs;
	private ArrayList<JointResource_people> peoples;
	private ArrayList<JointResource_req> reqs;
	
	private int kno_total;
	private int org_total;
	private int peo_total;
	private int req_total;
	
	private int index = 0;
	
	// 对接资源类型
	public enum ResourceType_new {
		Affair, People, Organization, Knowledge
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_joint_resource_new,
				container, false);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initVars();
		initControls(view);
		index = 0;
		CommonReqUtil.doGetJointResource_new(getActivity(), NewJointResourceFragment.this, userid, convertResourceType2Int(mTarResType), convertResourceSource2Int(mResSrc), 20, index++, null);
	}
	
	// 初始化变量
	private void initVars(){
		
		Bundle bundle = this.getArguments();
		
		mTarResType = (ResourceType_new) bundle.getSerializable(EConsts.Key.TARGET_RESOURCE_TYPE); // 目标资源类型
		mResSrc = (ResourceSource) bundle.getSerializable(EConsts.Key.JOINT_RESOURCE_SOURCE); // 对接资源来源
		userid = bundle.getString(EConsts.Key.ID);
		currentPage = bundle.getInt("currentPage");
		
		peoples = new ArrayList<JointResource_people>();
		orgs = new ArrayList<JointResource_org>();
		knos = new ArrayList<JointResource_kno>();
		reqs = new ArrayList<JointResource_req>();
		
		mAffairAdapter = new ResourceAdapter(this.getActivity(), ResourceType_new.Affair);
		mPeopleAdapter = new ResourceAdapter(this.getActivity(), ResourceType_new.People);
		mOrganizationAdapter = new ResourceAdapter(this.getActivity(), ResourceType_new.Organization);
		mKnowledgeAdapter = new ResourceAdapter(this.getActivity(), ResourceType_new.Knowledge);
		
		// initVarsWithDemoData();
	}
	
	// 资源类型转为int
	public static int convertResourceType2Int(ResourceType_new resType){
		int type = 0;
		//新"targetType":[
//	        1  知识
//	        2  需求
//	        3  组织
//	        4  人脉
//	        5  会议],
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
	
	private void initControls(View container){
		// 事件
		affParentLl = (LinearLayout) container.findViewById(R.id.affParentLl);
		affTitleTv = (TextView) container.findViewById(R.id.affTitleTv);
		reqMore = (TextView) container.findViewById(R.id.reqMore);
		affElv = (NoScrollListview) container.findViewById(R.id.affElv);
		affElv.setAdapter(mAffairAdapter);
		// 人脉
		peoParentLl = (LinearLayout) container.findViewById(R.id.peoParentLl);
		peoTitleTv = (TextView) container.findViewById(R.id.peoTitleTv);
		peoMore = (TextView) container.findViewById(R.id.peoMore);
		peoElv = (NoScrollListview) container.findViewById(R.id.peoElv);
		peoElv.setAdapter(mPeopleAdapter);
		// 组织
		orgParentLl = (LinearLayout) container.findViewById(R.id.orgParentLl);
		orgTitleTv = (TextView) container.findViewById(R.id.orgTitleTv);
		orgMore = (TextView) container.findViewById(R.id.orgMore);
		orgElv = (NoScrollListview) container.findViewById(R.id.orgElv);
		orgElv.setAdapter(mOrganizationAdapter);
		// 知识
		knoParentLl = (LinearLayout) container.findViewById(R.id.knoParentLl);
		knoTitleTv = (TextView) container.findViewById(R.id.knoTitleTv);
		knoMore = (TextView) container.findViewById(R.id.knoMore);
		knoElv = (NoScrollListview) container.findViewById(R.id.knoElv);
		knoElv.setAdapter(mKnowledgeAdapter);
		
		peoMore.setOnClickListener(this);
		orgMore.setOnClickListener(this);
		knoMore.setOnClickListener(this);
		reqMore.setOnClickListener(this);

//		switch(type_resource){
//		case 0:
//			affParentLl.setVisibility(View.VISIBLE);
//			peoParentLl.setVisibility(View.VISIBLE);
//			orgParentLl.setVisibility(View.VISIBLE);
//			knoParentLl.setVisibility(View.VISIBLE);
//			break;
//		case 1:
//			affParentLl.setVisibility(View.GONE);
//			peoParentLl.setVisibility(View.GONE);
//			orgParentLl.setVisibility(View.GONE);
//			knoParentLl.setVisibility(View.VISIBLE);
//			break;
//		case 2:
//			affParentLl.setVisibility(View.VISIBLE);
//			peoParentLl.setVisibility(View.GONE);
//			orgParentLl.setVisibility(View.GONE);
//			knoParentLl.setVisibility(View.GONE);
//			break;
//		case 3:
//			affParentLl.setVisibility(View.GONE);
//			peoParentLl.setVisibility(View.GONE);
//			orgParentLl.setVisibility(View.VISIBLE);
//			knoParentLl.setVisibility(View.GONE);
//			break;
//		case 4:
//			affParentLl.setVisibility(View.GONE);
//			peoParentLl.setVisibility(View.VISIBLE);
//			orgParentLl.setVisibility(View.GONE);
//			knoParentLl.setVisibility(View.GONE);
//			break;
//		}
		
		affElv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ENavigate.startNeedDetailsActivity(getActivity(),reqs.get(arg2).getId(),2);
			}
		});
		peoElv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(peoples.get(arg2).getPersonType().equals("1")){
					ENavigate.startRelationHomeActivity(getActivity(),peoples.get(arg2).getId(),true,1);
				}else{
					ENavigate.startRelationHomeActivity(getActivity(),peoples.get(arg2).getId(),false,2);
				}
			}
		});
		orgElv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (TextUtils.isEmpty(orgs.get(arg2).getCreateUserId()))// 组织
					ENavigate.startOrgMyHomePageActivity(getActivity(), Long.valueOf(orgs.get(arg2).getId()), Long.valueOf(orgs.get(arg2).getCreateUserId()), true, ENavConsts.type_details_org);
				else
					ENavigate.startClientDedailsActivity(getActivity(), Long.valueOf(orgs.get(arg2).getId()));
			}
		});
		knoElv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ENavigate.startKnowledgeOfDetailActivity(getActivity(), Long.valueOf(knos.get(arg2).getId()), 
						Integer.valueOf(knos.get(arg2).getKnowledgeType()));
			}
		});
		
	}
	
	private void refreshUI(ArrayList<JointResource_kno> knos,
			ArrayList<JointResource_org> orgs,
			ArrayList<JointResource_people> peoples,
			ArrayList<JointResource_req> reqs) {
		// 需求
		if(reqs.size()>0){
			affParentLl.setVisibility(View.VISIBLE);
			if(reqs.size()>3){
				reqMore.setVisibility(View.VISIBLE);
			}
		}
		affTitleTv.setText(getGroupTitleSpannableString(ResourceType_new.Affair));
		mAffairAdapter.notifyDataSetChanged(); 
		// 人脉
		if(peoples.size()>0){
			peoParentLl.setVisibility(View.VISIBLE);
			if(peoples.size()>3){
				peoMore.setVisibility(View.VISIBLE);
			}
		}
		peoTitleTv.setText(getGroupTitleSpannableString(ResourceType_new.People));
		mPeopleAdapter.notifyDataSetChanged();
		// 组织
		if(orgs.size()>0){
			orgParentLl.setVisibility(View.VISIBLE);
			if(orgs.size()>3){
				orgMore.setVisibility(View.VISIBLE);
			}
		}
		
		orgTitleTv.setText(getGroupTitleSpannableString(ResourceType_new.Organization));
		mOrganizationAdapter.notifyDataSetChanged();
		// 知识
		if(knos.size()>0){
			knoParentLl.setVisibility(View.VISIBLE);
			if(knos.size()>3){
				knoMore.setVisibility(View.VISIBLE);
			}
		}
		knoTitleTv.setText(getGroupTitleSpannableString(ResourceType_new.Knowledge));
		mKnowledgeAdapter.notifyDataSetChanged();
	}
	
	// 获取组标题样式字符串
		private SpannableString getGroupTitleSpannableString(ResourceType_new resType){
			
			SpannableString spanString = null;
			int count = 0;
			switch(resType){
			case Affair:
				count = reqs.size();
				spanString = new SpannableString(String.format("有%d个需求可对接", count)); 
				break;
			case People:
				count = peoples.size();
				spanString = new SpannableString(String.format("有%d个人可对接", count)); 
				break;
			case Organization:
				count = orgs.size();
				spanString = new SpannableString(String.format("有%d个组织可对接", count)); 
				break;
			case Knowledge:
				count = knos.size();
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
	
	class ResourceAdapter extends BaseAdapter{
		
		private Context mContext;
		private ResourceType_new resType; // 资源类型
		
		public ResourceAdapter(Context context, ResourceType_new resType){
			this.mContext = context;
			this.resType = resType;
		}
		
		@Override
		public int getCount() {
			switch(resType){
			case Affair:
				return reqs.size()>3?3:reqs.size();
			case People:
				return peoples.size()>3?3:peoples.size();
			case Organization:
				return orgs.size()>3?3:orgs.size();
			case Knowledge:
				return knos.size()>3?3:knos.size();
			default:
				break;
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			switch(resType){
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
			if(convertView == null){
				holder = new ViewHolder();
				switch(resType){
				case People:
					convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_related_resource_people, parent, false);
					holder.init(convertView);
					break;
				case Organization:
					convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_related_resource_organization, parent, false);
					holder.init(convertView);
					break;
				case Knowledge:
					convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_related_resource_knowledge, parent, false);
					holder.init(convertView);
					break;
				case Affair:
					convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_related_resource_affair, parent, false);
					holder.init(convertView);
					break;
				}
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.build(resType, obj);
			return convertView;
		}
		
		class ViewHolder{
			
			CheckBox selectCb;
			ImageView avatarIv;
			TextView titleTv;
			TextView tagTv;
			TextView timeTv;
			TextView authorTv;
			TextView nameTv;
//			TextView moreTv; // 更多
			ProgressBar loadingPb; // 加载框
			ImageView tagIv;
			
			public void init(View container){
				selectCb = (CheckBox) container.findViewById(R.id.selectCb);
				selectCb.setVisibility(View.GONE);
				avatarIv = (ImageView) container.findViewById(R.id.avatarIv);
				titleTv = (TextView) container.findViewById(R.id.titleTv);
				tagTv = (TextView) container.findViewById(R.id.tagTv);
				timeTv = (TextView) container.findViewById(R.id.timeTv);
				authorTv = (TextView) container.findViewById(R.id.authorTv);
				nameTv = (TextView) container.findViewById(R.id.nameTv);
				loadingPb = (ProgressBar) container.findViewById(R.id.loadingPb);
				tagIv = (ImageView) container.findViewById(R.id.tagIv);
			}
			
			public void build(ResourceType_new resType, Object obj){
				switch(resType){
				case People:
					if(avatarIv == null || titleTv == null){
						return;
					}
					JointResource_people people = (JointResource_people) obj;
					titleTv.setText(people.getName());
					if(!TextUtils.isEmpty(people.getPortrait())){
						Util.initAvatarImage(mContext, avatarIv, titleTv.getText().toString(), people.getPortrait(), 1, 1);
					}else{
						avatarIv.setImageResource(R.drawable.ic_default_avatar);
					}
					if(people.getPersonType().equals("1")){
						tagIv.setImageResource(R.drawable.contactusertag);
					}else{
						tagIv.setImageResource(R.drawable.contactpeopletag);
					}
//					if(source == 0){
//						tagTv.setText("");
//						tagTv.setVisibility(View.GONE);
//					}
//					else{
//						tagTv.setText(people.isOnline() ? "我的好友" : "我的人脉");
//					}
					break;
				case Organization:
					JointResource_org org = (JointResource_org) obj;
					titleTv.setText(!TextUtils.isEmpty(org.getName())?org.getShotName():org.getName());
					if(!TextUtils.isEmpty(org.getPicLogo())){
						Util.initAvatarImage(mContext, avatarIv, titleTv.getText().toString(), org.getPicLogo(), 1, 1);
					}else{
						avatarIv.setImageResource(R.drawable.default_portrait116);
					}
//					if(organization.getOrganizationMini().isOnline){
						tagIv.setImageResource(R.drawable.contactorganizationtag);
//					}else{
//						tagIv.setImageResource(R.drawable.contactclienttag);
//					}
					tagTv.setText("");
					break;
				case Affair:
					JointResource_req req = (JointResource_req) obj;
					titleTv.setText(req.getName());
//					if(source == 0){
//						tagTv.setText(affair.name);
//					}
//					else{
						tagTv.setText(req.getIndustry());
//					}
//					timeTv.setText(affair.time);
					break;
				case Knowledge:
					JointResource_kno kno = (JointResource_kno) obj;
					titleTv.setText(kno.getTitle());
//					if(source == 0){
//						tagTv.setText(knowledge.desc);
//					}
//					else{
						tagTv.setText(kno.getTags());
//					}
//					timeTv.setText(knowledge.modifytime);
//					nameTv.setText(knowledge.connections != null ? knowledge.connections.getMiniName():"");
					break;
				default:
					break;
				}
			}
		}
		
	}

	@Override
	public void bindData(int tag, Object object) {
		switch(tag){
		case EAPIConsts.CommonReqType.GetJointResource_MY:
		case EAPIConsts.CommonReqType.GetJointResource_FRIEND:
		case EAPIConsts.CommonReqType.GetJointResource_GT:
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

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.peoMore:
			ENavigate.startMoreJointResourceActivity(getActivity(), userid, mTarResType, ResourceType_new.People, mResSrc);
			break;
		case R.id.orgMore:
			ENavigate.startMoreJointResourceActivity(getActivity(), userid, mTarResType, ResourceType_new.Organization, mResSrc);
			break;
		case R.id.knoMore:
			ENavigate.startMoreJointResourceActivity(getActivity(), userid, mTarResType, ResourceType_new.Knowledge, mResSrc);
			break;
		case R.id.reqMore:
			ENavigate.startMoreJointResourceActivity(getActivity(), userid, mTarResType, ResourceType_new.Affair, mResSrc);
			break;
		}
	}
}
