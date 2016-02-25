package com.tr.ui.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.networkbench.agent.impl.a.a;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.model.im.ChatDetail;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.joint.ResourceNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.ResourceBase;
import com.tr.model.user.OrganizationMini;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.JointResourceMainFragment.ResourceSource;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.PersonId;
import com.tr.ui.share.ShareActivity;
import com.tr.ui.widgets.ExpandableListViewForScrollView;
import com.tr.ui.widgets.JointResourcePeopleOperateDialog;
import com.tr.ui.widgets.JointResourcePeopleOperateDialog.OnOperateSelectListener;
import com.tr.ui.widgets.JointResourcePeopleOperateDialog.OperateType;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 资源对接界面
 * @author leon
 */
public class JointResourceFragment extends JBaseFragment implements 
					IBindData, OnGroupClickListener, OnChildClickListener, OnItemLongClickListener{
	
	private final String TAG = getClass().getSimpleName();
	private final String TIP_NO_DATA = "没有更多数据了";
	
	// 控件
	private SwipeRefreshLayout refreshSrl;
	// 事件相关
	private LinearLayout affParentLl;
	private TextView affTitleTv;
	private ExpandableListViewForScrollView affElv;
	// 人脉相关
	private LinearLayout peoParentLl;
	private TextView peoTitleTv;
	private ExpandableListViewForScrollView peoElv;
	// 组织相关
	private LinearLayout orgParentLl;
	private TextView orgTitleTv;
	private ExpandableListViewForScrollView orgElv;
	// 知识相关
	private LinearLayout knoParentLl;
	private TextView knoTitleTv;
	private ExpandableListViewForScrollView knoElv;
	
	// 变量
	private ResourceSource mResSrc; // 资源来源
	private ResourceType mTarResType; // 目标资源类型
	private ResourceBase mTarRes; // 目标资源
	private int type_resource;//要显示的对接内容1  知识 2  需求 3  组织  4  人脉
	
	private ResourceAdapter mAffairAdapter; // 事件适配器
	private ResourceAdapter mPeopleAdapter; // 人脉适配器
	private ResourceAdapter mOrganizationAdapter; // 组织适配器
	private ResourceAdapter mKnowledgeAdapter; // 知识适配器
	private ArrayList<AffairNode> mListJointAffairNode; // 对接事件
	private ArrayList<ConnectionNode> mListJointPeopleNode; // 对接人脉
	private ArrayList<ConnectionNode> mListJointOrganizationNode; // 对接组织
	private ArrayList<KnowledgeNode> mListJointKnowledgeNode; // 对接知识
	private String mName;//目标资源 来源名称
	private String mColumnType; //目标资源栏目类型
	private String mMyColumn="需求";
	private long packedPosition;
	private List<ResourceNode> listNode;//纠错的数据集合
	private int childPosition;
	private int groupPosition;
	private View mView;
	
	// 对接资源类型
	public enum ResourceType {
		Affair, People, Organization, Knowledge, User, Client
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		 if(mView != null){
             ViewGroup parent = (ViewGroup) mView.getParent();  
             if (parent != null) {  
            	 parent.removeView(mView);  
             }   
             return mView;
		 }
		 mView= inflater.inflate(R.layout.fragment_joint_resource, container, false);
		if(null != savedInstanceState){
			mListJointPeopleNode = (ArrayList<ConnectionNode>) savedInstanceState.getSerializable("mListJointPeopleNode");
			mListJointOrganizationNode = (ArrayList<ConnectionNode>) savedInstanceState.getSerializable("mListJointOrganizationNode");
			mListJointKnowledgeNode = (ArrayList<KnowledgeNode>) savedInstanceState.getSerializable("mListJointKnowledgeNode");
			mListJointAffairNode = (ArrayList<AffairNode>) savedInstanceState.getSerializable("mListJointAffairNode");
			groupPosition = savedInstanceState.getInt("groupPosition");
	    }
		return mView;
	}
	
	@Override
	public void onViewCreated(View container,Bundle savedInstanceState){
		initVars();
		initControls(container);
		refreshSrl.setRefreshing(true);
		mName = getResourceName(mTarRes);
		CommonReqUtil.doGetJointResource(getActivity(), this,
				getResourceId(mTarRes), convertResourceType2Int(mTarResType),
				convertResourceSource2Int(mResSrc), 20,0 ,null);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		 if(null != savedInstanceState){
			 mListJointPeopleNode = (ArrayList<ConnectionNode>) savedInstanceState.getSerializable("mListJointPeopleNode");
			 mListJointOrganizationNode = (ArrayList<ConnectionNode>) savedInstanceState.getSerializable("mListJointOrganizationNode");
			 mListJointKnowledgeNode = (ArrayList<KnowledgeNode>) savedInstanceState.getSerializable("mListJointKnowledgeNode");
			 mListJointAffairNode = (ArrayList<AffairNode>) savedInstanceState.getSerializable("mListJointAffairNode");
			 groupPosition = savedInstanceState.getInt("groupPosition");
	    }
		super.onActivityCreated(savedInstanceState);
	}
	
	// 初始化变量
	private void initVars(){
		
		Bundle bundle = this.getArguments();
		
		mTarResType = (ResourceType) bundle.getSerializable(EConsts.Key.TARGET_RESOURCE_TYPE); // 目标资源类型
		mTarRes = (ResourceBase) bundle.getSerializable(EConsts.Key.TARGET_RESOURCE); // 目标资源
		mResSrc = (ResourceSource) bundle.getSerializable(EConsts.Key.JOINT_RESOURCE_SOURCE); // 对接资源来源
		type_resource = bundle.getInt("type_resource");
		
		mListJointAffairNode = new ArrayList<AffairNode>();
		mListJointPeopleNode = new ArrayList<ConnectionNode>();
		mListJointOrganizationNode = new ArrayList<ConnectionNode>();
		mListJointKnowledgeNode = new ArrayList<KnowledgeNode>();
		
		mAffairAdapter = new ResourceAdapter(this.getActivity(), ResourceType.Affair);
		mPeopleAdapter = new ResourceAdapter(this.getActivity(), ResourceType.People);
		mOrganizationAdapter = new ResourceAdapter(this.getActivity(), ResourceType.Organization);
		mKnowledgeAdapter = new ResourceAdapter(this.getActivity(), ResourceType.Knowledge);
		
		// initVarsWithDemoData();
	}
	
	
	// 初始化控件
	@SuppressWarnings("deprecation")
	private void initControls(View container){
		
		// 下拉刷新
		refreshSrl = (SwipeRefreshLayout) container.findViewById(R.id.refreshSrl);
		refreshSrl.setColorScheme(android.R.color.holo_blue_bright, 
	            android.R.color.holo_green_light, 
	            android.R.color.holo_orange_light, 
	            android.R.color.holo_red_light);
		refreshSrl.setOnRefreshListener(new OnRefreshListener(){
			@Override
			public void onRefresh() {
				CommonReqUtil.doGetJointResource(getActivity(), JointResourceFragment.this,
						getResourceId(mTarRes), convertResourceType2Int(mTarResType),
						convertResourceSource2Int(mResSrc), 20,0 ,null);
			}
		});
		// 事件
		affParentLl = (LinearLayout) container.findViewById(R.id.affParentLl);
		affTitleTv = (TextView) container.findViewById(R.id.affTitleTv);
		affElv = (ExpandableListViewForScrollView) container.findViewById(R.id.affElv);
		affElv.setGroupIndicator(null);
		affElv.setAdapter(mAffairAdapter);
		affElv.setOnGroupClickListener(this);
		affElv.setOnChildClickListener(this);
		affElv.setOnItemLongClickListener(this);
		// 人脉
		peoParentLl = (LinearLayout) container.findViewById(R.id.peoParentLl);
		peoTitleTv = (TextView) container.findViewById(R.id.peoTitleTv);
		peoElv = (ExpandableListViewForScrollView) container.findViewById(R.id.peoElv);
		peoElv.setGroupIndicator(null);
		peoElv.setAdapter(mPeopleAdapter);
		peoElv.setOnGroupClickListener(this);
		peoElv.setOnChildClickListener(this);
		peoElv.setOnItemLongClickListener(this);
		// 组织
		orgParentLl = (LinearLayout) container.findViewById(R.id.orgParentLl);
		orgTitleTv = (TextView) container.findViewById(R.id.orgTitleTv);
		orgElv = (ExpandableListViewForScrollView) container.findViewById(R.id.orgElv);
		orgElv.setGroupIndicator(null);
		orgElv.setAdapter(mOrganizationAdapter);
		orgElv.setOnGroupClickListener(this);
		orgElv.setOnChildClickListener(this);
		orgElv.setOnItemLongClickListener(this);
		// 知识
		knoParentLl = (LinearLayout) container.findViewById(R.id.knoParentLl);
		knoTitleTv = (TextView) container.findViewById(R.id.knoTitleTv);
		knoElv = (ExpandableListViewForScrollView) container.findViewById(R.id.knoElv);
		knoElv.setGroupIndicator(null);
		knoElv.setAdapter(mKnowledgeAdapter);
		knoElv.setOnGroupClickListener(this);
		knoElv.setOnChildClickListener(this);
		knoElv.setOnItemLongClickListener(this);

		switch(type_resource){
		case 0:
			affParentLl.setVisibility(View.VISIBLE);
			peoParentLl.setVisibility(View.VISIBLE);
			orgParentLl.setVisibility(View.VISIBLE);
			knoParentLl.setVisibility(View.VISIBLE);
			break;
		case 1:
			affParentLl.setVisibility(View.GONE);
			peoParentLl.setVisibility(View.GONE);
			orgParentLl.setVisibility(View.GONE);
			knoParentLl.setVisibility(View.VISIBLE);
			break;
		case 2:
			affParentLl.setVisibility(View.VISIBLE);
			peoParentLl.setVisibility(View.GONE);
			orgParentLl.setVisibility(View.GONE);
			knoParentLl.setVisibility(View.GONE);
			break;
		case 3:
			affParentLl.setVisibility(View.GONE);
			peoParentLl.setVisibility(View.GONE);
			orgParentLl.setVisibility(View.VISIBLE);
			knoParentLl.setVisibility(View.GONE);
			break;
		case 4:
			affParentLl.setVisibility(View.GONE);
			peoParentLl.setVisibility(View.VISIBLE);
			orgParentLl.setVisibility(View.GONE);
			knoParentLl.setVisibility(View.GONE);
			break;
		}
	}
	
	// 请求数据
	private String getResourceId(ResourceBase res){
		String id = null;
		if(res instanceof AffairsMini){
			id = ((AffairsMini) res).id + "";
		}
		else if(res instanceof ConnectionsMini){
			id = ((ConnectionsMini) res).getId();
		}
		else if(res instanceof Connections){
			id = ((Connections) res).getId();
		}
		else if(res instanceof KnowledgeMini2){
			id = ((KnowledgeMini2) res).id + "";
		
		}
		else if(res instanceof PersonId){
			id = ((PersonId) res).id + "";
		}
		return id;
	}
	
	//获取资源来源姓名
	private String getResourceName(ResourceBase res){
		String name = "";
		if(res instanceof AffairsMini){
			name = ((AffairsMini) res).name;//发布者姓名
		}
		else if(res instanceof ConnectionsMini){
			name = ((ConnectionsMini) res).getName();//个人或机构姓名
		}
		else if(res instanceof Connections){
			name = ((Connections) res).getName();//个人或机构姓名
		}
		else if(res instanceof KnowledgeMini2){
			if (res!=null) {
				if (((KnowledgeMini2) res).connections!=null) {
					name = ((KnowledgeMini2) res).connections.getName();//获取发布人姓名
				}else {
					name = "知识";
				}
			}
		}
		return name;
	}
	//获取栏目的类型
	private String getResourceColumnType(ResourceNode resNode){
		String type = resNode.getType();
		String resourceColumnType = "";
		if (type.equals("fin")) {
			resourceColumnType = "投资需求";
			mMyColumn = "融资需求";
		}else if(type.equals("inv")){
			resourceColumnType = "融资需求";
			mMyColumn = "投资需求";
		}else if (type.equals("exp")) {
			resourceColumnType = "专家需求";
			mMyColumn = "专家资源";
		}else {
			resourceColumnType="需求";
		}
		return resourceColumnType;
	}
	
	
	// 资源类型转为int
	public static int convertResourceType2Int(ResourceType resType){
		int type = 0;
		//新"targetType":[
//        1  知识
//        2  需求
//        3  组织
//        4  人脉
//        5  会议],
		switch(resType){
		case Affair:
			type = 2;
			break;
		case People:
			type = 4;
			break;
		case User:
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
	
	// 刷新Fragment
	private void refreshFragment(ArrayList<AffairNode> listAffairNode, ArrayList<ConnectionNode> listPeopleNode,
			ArrayList<ConnectionNode> listOrganizationNode, ArrayList<KnowledgeNode> listKnowledgeNode){
		
		if(listAffairNode != null){
			mListJointAffairNode = listAffairNode;
			//事务：过滤垃圾数据
			List<AffairsMini> tempListAffairsMini = new ArrayList<AffairsMini>();
			ArrayList<AffairsMini> listAffairMini =null;
			for (AffairNode affairNode : mListJointAffairNode) {
				listAffairMini = affairNode.getListAffairMini();
				for (AffairsMini affairsMini : listAffairMini) {
					Connections connections = affairsMini.connections;
					if (connections.organizationMini==null&&connections.jtContactMini==null) {
						//TODO:垃圾数据
						tempListAffairsMini.add(affairsMini);
					}
				}
			}
			if (listAffairMini!=null) {
				listAffairMini.removeAll(tempListAffairsMini);
			}
		}
		if(listPeopleNode != null){
			mListJointPeopleNode = listPeopleNode;
			//组织：过滤垃圾数据
			List<Connections> templistConnections = new ArrayList<Connections>();
			ArrayList<Connections> listConnections =null;
			for (ConnectionNode connectionNode : mListJointPeopleNode) {
				listConnections = connectionNode.getListConnections();
				for (Connections connections : listConnections) {
					if (connections.getOrganizationMini()==null&&connections.getJtContactMini()==null) {
						//TODO:垃圾数据
						templistConnections.add(connections);
					}
				}
			}
			if (listConnections!=null) {
				listConnections.removeAll(templistConnections);
			}
		}
		if(listOrganizationNode != null){
			mListJointOrganizationNode = listOrganizationNode;
			//组织：过滤垃圾数据
			List<Connections> templistConnections = new ArrayList<Connections>();
			ArrayList<Connections> listConnections =null;
			for (ConnectionNode connectionNode : mListJointOrganizationNode) {
				listConnections = connectionNode.getListConnections();
				for (Connections connections : listConnections) {
					if (connections.getOrganizationMini()==null&&connections.getJtContactMini()==null) {
						//TODO:垃圾数据
						templistConnections.add(connections);
					}
				}
			}
			if (listConnections!=null) {
				listConnections.removeAll(templistConnections);
			}
		}
		if(listKnowledgeNode != null){
			mListJointKnowledgeNode = listKnowledgeNode;
			//知识：过滤垃圾数据
			List<KnowledgeMini2> tempListKnowledgeMini2 = new ArrayList<KnowledgeMini2>();
			ArrayList<KnowledgeMini2> listKnowledgeMini2 =null;
			for (KnowledgeNode knowledgeNode : mListJointKnowledgeNode) {
				listKnowledgeMini2 = knowledgeNode.getListKnowledgeMini2();
				for (KnowledgeMini2 knowledgeMini2 : listKnowledgeMini2) {
					Connections connections = knowledgeMini2.connections;
					if (connections.organizationMini==null&&connections.jtContactMini==null) {
						//TODO:垃圾数据
						tempListKnowledgeMini2.add(knowledgeMini2);
					}
				}
			}
			if (listKnowledgeMini2!=null) {
				listKnowledgeMini2.removeAll(tempListKnowledgeMini2);
			}
		}
		
		// 需求
		affTitleTv.setText(getGroupTitleSpannableString(ResourceType.Affair));
		for (int i=0; i< mAffairAdapter.getGroupCount(); i++) {
			affElv.expandGroup(i);
		}
		mAffairAdapter.notifyDataSetChanged();
		// 人脉
		peoTitleTv.setText(getGroupTitleSpannableString(ResourceType.People));
		for (int i=0; i< mPeopleAdapter.getGroupCount(); i++) {
			peoElv.expandGroup(i);
		}
		mPeopleAdapter.notifyDataSetChanged();
		// 组织
		orgTitleTv.setText(getGroupTitleSpannableString(ResourceType.Organization));
		for (int i = 0; i < mOrganizationAdapter.getGroupCount(); i++) {
			orgElv.expandGroup(i);
		}
		mOrganizationAdapter.notifyDataSetChanged();
		// 知识
		knoTitleTv.setText(getGroupTitleSpannableString(ResourceType.Knowledge));
		for (int i=0; i< mKnowledgeAdapter.getGroupCount(); i++) {
			knoElv.expandGroup(i);
		}
		mKnowledgeAdapter.notifyDataSetChanged();
	}

	
	// 获取组标题样式字符串
	private SpannableString getGroupTitleSpannableString(ResourceType resType){
		
		SpannableString spanString = null;
		int count = 0;
		switch(resType){
		case Affair:
			for(AffairNode node : mListJointAffairNode){
				count += node.getListAffairMini().size();
			}
			spanString = new SpannableString(String.format("有%d个需求可对接", count)); 
			break;
		case People:
			count = 0;
			for(ConnectionNode node : mListJointPeopleNode){
				count += node.getListConnections().size();
			}
			spanString = new SpannableString(String.format("有%d个人可对接", count)); 
			break;
		case Organization:
			count = 0;
			for(ConnectionNode node : mListJointOrganizationNode){
				count += node.getListConnections().size();
			}
			spanString = new SpannableString(String.format("有%d个组织可对接", count)); 
			break;
		case Knowledge:
			count = 0;
			for(KnowledgeNode node : mListJointKnowledgeNode){
				count += node.getListKnowledgeMini2().size();
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
	
	// 获取栏目标题样式字符串
	private SpannableString getColumnSpannableString(ResourceType resType, ResourceNode resNode){
		
		SpannableString spanString = null;
		
		int count = 0;
		switch(resType){
		case Affair:
			count = ((AffairNode) resNode).getListAffairMini().size();
//			spanString = new SpannableString( String.format("有%d个"+getResourceColumnType(resNode)+"与"+mName+"的"+mMyColumn+"对接", count));    
//			spanString.setSpan(span, 1, 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			break;
		case People:
		case Organization:
			count = ((ConnectionNode) resNode).getListConnections().size();
//			spanString = new SpannableString(resNode.getMemo()+ String.format("有%d个"+"可与"+mName+"对接", count));   
//			spanString.setSpan(span, resNode.getMemo().length()+1,resNode.getMemo().length() + (count + "").length()+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
			break;
		case Knowledge:
			count = ((KnowledgeNode) resNode).getListKnowledgeMini2().size();
			break;
		default:
			break;
		}		
		ForegroundColorSpan span = new ForegroundColorSpan(0xfff37800);
		spanString = new SpannableString(resNode.getMemo()+ String.format("有%d个"+"可"+"对接", count));   
		spanString.setSpan(span, resNode.getMemo().length()+1, resNode.getMemo().length() + (count + "").length()+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
		
		return spanString;
	}
	
	class ResourceAdapter extends BaseExpandableListAdapter{

		private Context context;
		private ResourceType resType; // 资源类型
		
		public ResourceAdapter(Context context, ResourceType resType){
			this.context = context;
			this.resType = resType;
		}
		
		@Override
		public int getGroupCount() {
			switch(resType){
			case Affair:
				return mListJointAffairNode.size();
			case People:
				return mListJointPeopleNode.size();
			case Organization:
				return mListJointOrganizationNode.size();
			case Knowledge:
				return mListJointKnowledgeNode.size();
			default:
				break;
			}
			return 0;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			switch(resType){
			case Affair:
				if(mListJointAffairNode.get(groupPosition).getListAffairMini() != null){
					return Math.min(mListJointAffairNode.get(groupPosition).getListAffairMini().size(), 3);
				}
			case People:
				if(mListJointPeopleNode.get(groupPosition).getListConnections() != null){
					return 1; // 人脉只返回一个子项；mListJointPeopleNode.get(groupPosition).getListConnections().size();
				}
			case Organization:
				if(mListJointOrganizationNode.get(groupPosition).getListConnections() != null){
					return Math.min(mListJointOrganizationNode.get(groupPosition).getListConnections().size(), 3);
				}
			case Knowledge:
				if(mListJointKnowledgeNode.get(groupPosition).getListKnowledgeMini2() != null){
					return Math.min(mListJointKnowledgeNode.get(groupPosition).getListKnowledgeMini2().size(), 3);
				}
			default:
				break;
			}
			return 0;
		}

		@Override
		public Object getGroup(int groupPosition) {
			switch(resType){
			case Affair:
				return mListJointAffairNode.get(groupPosition);
			case People:
				return mListJointPeopleNode.get(groupPosition);
			case Organization:
				return mListJointOrganizationNode.get(groupPosition);
			case Knowledge:
				return mListJointKnowledgeNode.get(groupPosition);
			default:
				break;
			}
			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			switch(resType){
			case Affair:
				if(mListJointAffairNode.get(groupPosition).getListAffairMini() != null){
					return mListJointAffairNode.get(groupPosition).getListAffairMini().get(childPosition);
				}
			case People:
				if(mListJointPeopleNode.get(groupPosition).getListConnections() != null){
					return mListJointPeopleNode.get(groupPosition).getListConnections().get(childPosition);
				}
			case Organization:
				if(mListJointOrganizationNode.get(groupPosition).getListConnections() != null){
					return mListJointOrganizationNode.get(groupPosition).getListConnections().get(childPosition);
				}
			case Knowledge:
				if(mListJointKnowledgeNode.get(groupPosition).getListKnowledgeMini2() != null){
					return mListJointKnowledgeNode.get(groupPosition).getListKnowledgeMini2().get(childPosition);
				}
			default:
				break;
			}
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.list_item_joint_res_title, parent, false);
				holder = new GroupViewHolder();
				holder.init(convertView);
				convertView.setTag(holder);
			}
			else{
				holder = (GroupViewHolder) convertView.getTag();
			}
			switch(resType){
			case Affair:
				holder.build(resType, mListJointAffairNode.get(groupPosition));
				break;
			case People:
				holder.build(resType, mListJointPeopleNode.get(groupPosition));
				break;
			case Organization:
				holder.build(resType, mListJointOrganizationNode.get(groupPosition));
				break;
			case Knowledge:
				holder.build(resType, mListJointKnowledgeNode.get(groupPosition));
				break;
			default:
				break;
			}
			
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ChildViewHolder holder;
			if(convertView == null){
				switch(resType){
				case Affair:
					convertView = LayoutInflater.from(context).inflate(R.layout.list_item_joint_aff, parent, false);
					break;
				case People:
					convertView = LayoutInflater.from(context).inflate(R.layout.list_item_joint_peo, parent, false);
					break;
				case Organization:
					convertView = LayoutInflater.from(context).inflate(R.layout.list_item_joint_org, parent, false);
					break;
				case Knowledge:
					convertView = LayoutInflater.from(context).inflate(R.layout.list_item_joint_kno, parent, false);
					break;
				default:
					break;
				}
				holder = new ChildViewHolder(groupPosition);
				holder.init(convertView);
				convertView.setTag(holder);
			}
			else{
				holder = (ChildViewHolder) convertView.getTag();
			}
			switch(resType){
			case Affair:
				holder.build(resType, mListJointAffairNode.get(groupPosition).getListAffairMini().get(childPosition));
				break;
			case People:
				holder.build(resType, mListJointPeopleNode.get(groupPosition));
				break;
			case Organization:
				holder.build(resType, mListJointOrganizationNode.get(groupPosition).getListConnections().get(childPosition));
				break;
			case Knowledge:
				holder.build(resType, mListJointKnowledgeNode.get(groupPosition).getListKnowledgeMini2().get(childPosition));
				break;
			default:
				break;
			}
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
		class GroupViewHolder{
			
			TextView titleTv; // 标题
			ImageView moreIv; // 更多
			
			public void init(View view){
				titleTv = (TextView) view.findViewById(R.id.titleTv);
				moreIv = (ImageView) view.findViewById(R.id.moreIv);
			}
			
			public void build(ResourceType resType, ResourceNode resNode){
				titleTv.setText(getColumnSpannableString(resType, resNode));
			}
		}
		
		class ChildViewHolder implements OnItemClickListener, OnItemLongClickListener, OnClickListener{
			
			ImageView avatarIv; // 头像
			TextView typeTv; // 类型
			TextView titleTv; // 标题
			TextView nameTv; // 姓名
			TextView tradeTv; // 行业
			GridView peopleGv; // 人脉列表
			int parentPosition;
			
			public ChildViewHolder(int parentPosition) {
				this.parentPosition = parentPosition;
			}

			public void init(View view){
				avatarIv = (ImageView) view.findViewById(R.id.avatarIv);
				typeTv = (TextView) view.findViewById(R.id.typeTv);
				titleTv = (TextView) view.findViewById(R.id.titleTv);
				nameTv = (TextView) view.findViewById(R.id.nameTv);
				tradeTv = (TextView) view.findViewById(R.id.tradeTv);
				peopleGv = (GridView) view.findViewById(R.id.peopleGv);
			}
			
			public void build(ResourceType resType, Object res){
				switch(resType){
				case Affair:
					AffairsMini affair = (AffairsMini) res;
					titleTv.setText(affair.title);
					if (affair.connections!=null) {
						nameTv.setText(affair.connections.getName());
					}
					nameTv.setOnClickListener(this);
					nameTv.setTag(affair);
					break;
				case People:
					ConnectionNode node = (ConnectionNode) res;
					PeopleAdapter adapter = new PeopleAdapter(context, node);
					peopleGv.setAdapter(adapter);
					peopleGv.setOnItemClickListener(this);
					peopleGv.setOnItemLongClickListener(this);
					break;
				case Organization:
					Connections connections = (Connections) res;
					if (connections.organizationMini!=null) {
						if (connections.organizationMini.mLogo!=null&&!connections.organizationMini.mLogo.contains("default.jpeg")&&!connections.organizationMini.mLogo.isEmpty()) {
							ImageLoader.getInstance().displayImage(connections.organizationMini.mLogo, avatarIv);
						}else {
							avatarIv.setImageResource(R.drawable.ic_organization);
						}
						nameTv.setText(connections.organizationMini.fullName!=null?connections.organizationMini.fullName:"");
						tradeTv.setText(connections.organizationMini.mTrade);	
					}
					
					break;
				case Knowledge:
					KnowledgeMini2 knowledge = (KnowledgeMini2) res;
					typeTv.setText(getKnoType(knowledge.type));
					titleTv.setText(knowledge.title);
					nameTv.setText(knowledge.connections.getOrganizationMini()!=null?knowledge.connections.getOrganizationMini().getFullName():knowledge.connections.getJtContactMini().getName());
					nameTv.setOnClickListener(this);
					nameTv.setTag(knowledge);
					break;
				default:
					break;
				}
			}
			//获取知识类型
			private String getKnoType(int type) {
				switch (type) {
				case 1:
					return "【资讯】";
				case 2:
					return "【投融工具】";
				case 3:
					return "【行业】";
				case 4:
					return "【经典案例】";
				case 5:
					return "【图书报告】";
				case 6:
					return "【资产管理】";
				case 7:
					return "【宏观】";
				case 8:
					return "【观点】";
				case 9:
					return "【判例】";
				case 10:
					return "【法律法规】";
				case 11:
					return "【文章】";
				default:
					break;
				}
				return "";
			}

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}

			@Override
			public boolean onItemLongClick(final AdapterView<?> parent, View view,
					final int position, long id) {
				// 弹出纠错提示框
				new AlertDialog.Builder(context)
					.setTitle("提示")
					.setMessage("对本条目纠错？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							groupPosition = parentPosition;
							listNode = new ArrayList<ResourceNode>();
							ConnectionNode peopleNode = new ConnectionNode();
			            	peopleNode.setMemo(((PeopleAdapter) parent.getAdapter()).getColumn());
			            	peopleNode.getListConnections().add((Connections) parent.getAdapter().getItem(position));
			            	peopleNode.setType(((PeopleAdapter) parent.getAdapter()).getColumnType());
			            	listNode.add(peopleNode);
							((JBaseFragmentActivity) getActivity()).showLoadingDialog();
				    		CommonReqUtil.doCorrectJointResource(getActivity(), JointResourceFragment.this, convertResourceType2Int(mTarResType), 
				    				getResourceId(mTarRes), listNode, null);
						}
					})
					.setNegativeButton("取消", null)
					.create()
					.show();
				return true;
			}

			@Override
			public void onClick(View v) {
				JointResourcePeopleOperateDialog dialog = new JointResourcePeopleOperateDialog(context);
				ResourceBase resource = (ResourceBase) v.getTag();
				dialog.setOnSelectListener(new OnOperateSelectListener(){

					private JTFile jtFile;

					@Override
					public void onOperateSelect(OperateType operType, ResourceBase resource) {
						String phoneNumber ="";
						String desc = "";
						String thatID = "";
						String thatImage = "";
						String thatName = "";
						if(resource instanceof KnowledgeMini2){
							KnowledgeMini2 knowledge = (KnowledgeMini2) resource;
							phoneNumber = knowledge.connections.getMobilePhone();
							desc = knowledge.desc;
							jtFile = knowledge.toJTFile();
							thatID = knowledge.connections.getId();
							thatImage = knowledge.connections.getImage();
							thatName = knowledge.connections.getName();
						}
						else if(resource instanceof AffairsMini){
							AffairsMini affairsMini = (AffairsMini) resource;
							phoneNumber = affairsMini.connections.getMobilePhone();
							desc = affairsMini.title;
//							
//							jtFile = new JTFile();
//							jtFile.mFileName = jtFile.mSuffixName = affairsMini.title;
//							jtFile.reserved1 = affairsMini.time; // 发布时间
//							jtFile.mTaskId = affairsMini.id + "";
//							jtFile.mType = JTFile.TYPE_REQUIREMENT;
							thatID = affairsMini.connections.getId();
							thatImage = affairsMini.connections.getImage();
							thatName = affairsMini.connections.getName();
						}
						
						switch(operType){
						case SMS: // 短信
							if (TextUtils.isEmpty(phoneNumber)) {
								showToast("请完善联系方式");
							}
								Uri smsToUri = Uri.parse("smsto:"+phoneNumber);  
								Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);  
								startActivity(intent);
							break;
						case TEL: // 拨号
							if (TextUtils.isEmpty(phoneNumber)) {
								showToast("请完善联系方式");
							}
							Intent call = new Intent();
							call.setAction(Intent.ACTION_DIAL);
							call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							call.setData(Uri.parse("tel: "+phoneNumber));
							startActivity(call);
							break;
						case FORWARD: // 转发/畅聊
								ChatDetail chartWithPeople = new ChatDetail();
								chartWithPeople.setThatID(thatID);
								chartWithPeople.setThatImage(thatImage);
								chartWithPeople.setThatName(thatName);
								ENavigate.startIMActivity(getActivity(), chartWithPeople);
							break;
//						case INVITE: // 邀请
//							Uri smToUri = Uri.parse("smsto:"+phoneNumber);  
//							Intent intent1 = new Intent(Intent.ACTION_SENDTO, smToUri);  
//							intent1.putExtra(Intent.EXTRA_TEXT, "我正在使用金桐app，一款集商务社交、投融资项目对接、个人资源管理的商务应用神器！ 推荐给你，快来哦，轻点 http://app.gintong.com 即可下载。");    
//							startActivity(intent1);   
//							showToast("邀请");
//							break;
						case SHARE: // 分享
							 Intent share=new Intent(Intent.ACTION_SEND);    
							 share.setType("image/*");    
							 share.putExtra(Intent.EXTRA_SUBJECT, "Share");    
							 share.putExtra(Intent.EXTRA_TEXT, "我正在使用金桐app，一款集商务社交、投融资项目对接、个人资源管理的商务应用神器！ 推荐给你，快来哦，轻点 http://app.gintong.com 即可下载。");    
							 share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
				             startActivity(share);    
//							showToast("功能暂未开放");
							break;
						default:
							break;
						}
					}
				});
				
				if(resource instanceof KnowledgeMini2){
					resource.setResourceType(ResourceBase.ResourceType.Knowledge);
				}
				else if(resource instanceof AffairsMini){
					resource.setResourceType(ResourceBase.ResourceType.Affair);
				}
				dialog.show(v, resource);
			}
		}
		
		// 人脉GridView适配器
		class PeopleAdapter extends BaseAdapter{

			private final int MAX_ITEM = 5;
			
			private Context context;
			private String column;
			private String columnType;
			private ArrayList<Connections> listPeople;
			
			public PeopleAdapter(Context context, ConnectionNode node){
				this.context = context;
				this.listPeople = node.getListConnections(); 
				this.column = node.getMemo();
				this.columnType = node.getType();
			}
			
			// 获取栏目名
			public String getColumn(){
				return this.column;
			}
			
			// 获取栏目类型
			public String getColumnType(){
				return this.columnType;
			}
			
			@Override
			public int getCount() {
				return Math.min(listPeople.size(), MAX_ITEM);
			}

			@Override
			public Connections getItem(int position) {
				return listPeople.get(position);
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder;
				if(convertView == null){
					convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_joint_people, parent, false);
					holder = new ViewHolder();
					holder.init(convertView);
					convertView.setTag(holder);
				}
				else{
					holder = (ViewHolder) convertView.getTag();
				}
				holder.build(listPeople.get(position));
				return convertView;
			}
			
			class ViewHolder{
				
				ImageView avatarIv;
				TextView nameTv;
				
				public void init(View view){
					avatarIv = (ImageView) view.findViewById(R.id.avatarIv);
					nameTv = (TextView) view.findViewById(R.id.nameTv);
				}
				
				public void build(Connections people){
					if (people.jtContactMini.getImage()!=null&&!people.jtContactMini.getImage().isEmpty()&&!people.jtContactMini.getImage().contains("default.jpeg")) {
						ImageLoader.getInstance().displayImage(people.jtContactMini.getImage(), avatarIv);
					}else {
						avatarIv.setBackgroundResource(R.drawable.ic_know_people);
					}
					nameTv.setText(people.jtContactMini.name);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {
		if(tag == EAPIConsts.CommonReqType.GetJointResource){ // 获取关联资源
			refreshSrl.setRefreshing(false);
			if(object != null){
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				ArrayList<AffairNode> listAffairNode = null;
				ArrayList<ConnectionNode> listPeopleNode = null;
				ArrayList<ConnectionNode> listOrganizationNode = null;
				ArrayList<KnowledgeNode> listKnowledgeNode = null;
				if(dataMap.containsKey("listJointAffairNode")){
					listAffairNode = (ArrayList<AffairNode>) dataMap.get("listJointAffairNode");
				}
				if(dataMap.containsKey("listJointPeopleNode")){
					listPeopleNode = (ArrayList<ConnectionNode>) dataMap.get("listJointPeopleNode");
				}
				if(dataMap.containsKey("listJointOrganizationNode")){
					listOrganizationNode = (ArrayList<ConnectionNode>) dataMap.get("listJointOrganizationNode");
				}
				if(dataMap.containsKey("listJointKnowledgeNode")){
					listKnowledgeNode = (ArrayList<KnowledgeNode>) dataMap.get("listJointKnowledgeNode");
				}
				// 刷新界面
				refreshFragment(listAffairNode, listPeopleNode, listOrganizationNode, listKnowledgeNode);
			}
		}
		else if(tag == EAPIConsts.CommonReqType.CorrectJointResult){ // 生态对接纠错
			((JBaseFragmentActivity) getActivity()).dismissLoadingDialog();
			if(object != null){
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				boolean success = (Boolean) dataMap.get("success");
				if(success&&listNode!=null){
					//纠错成功，删除对应资源,刷新界面
	    			refreshExpandableList(listNode);
					showToast("纠错成功");
				}
				else{
					showToast("纠错失败");
				}
			}
		}
	}
	//纠错成功，刷新列表
	private void refreshExpandableList(List<ResourceNode> listResourceNode) {
		ResourceNode resourceNode = listResourceNode.get(0);
		if (resourceNode instanceof AffairNode) {//事务
			for (ResourceNode node:listResourceNode) {
				ArrayList<AffairsMini> lisAffairsMinis = ((AffairNode) node).getListAffairMini();
				if (!lisAffairsMinis.isEmpty()) {
					ArrayList<AffairsMini> aFfairsMiniTemp = new ArrayList<AffairsMini>();
					//遍历相同元素
					for(AffairsMini affairsMini:mListJointAffairNode.get(groupPosition).getListAffairMini()){
						for (AffairsMini mAffairsMini:lisAffairsMinis) {
							if (affairsMini.id ==mAffairsMini.id) {
								aFfairsMiniTemp.add(affairsMini);
							}
						}
					}
					mListJointAffairNode.get(groupPosition).getListAffairMini().removeAll(aFfairsMiniTemp);
				}
			}
			affTitleTv.setText(getGroupTitleSpannableString(ResourceType.Affair));
			mAffairAdapter.notifyDataSetChanged();
		}else if(resourceNode instanceof ConnectionNode){//人脉或组织
			for (ResourceNode node:listResourceNode) {
				ArrayList<Connections> liscConnections = ((ConnectionNode) node).getListConnections();
				if (!liscConnections.isEmpty()) {
					if (liscConnections.get(0).jtContactMini!=null) {//人脉
						ArrayList<Connections> peopleTemps = new ArrayList<Connections>();
						//遍历相同元素
						for(Connections connections:mListJointPeopleNode.get(groupPosition).getListConnections()){
							for (Connections mConnections:liscConnections) {
								if (mConnections.getId().equals(connections.getId())) {
									peopleTemps.add(connections);
								}
							}
						}
						mListJointPeopleNode.get(groupPosition).getListConnections().removeAll(peopleTemps);
					}
					if (liscConnections.get(0).organizationMini!=null) {//组织
						ArrayList<Connections> organizationTemps = new ArrayList<Connections>();
						//遍历相同元素
						for(Connections connections:mListJointOrganizationNode.get(groupPosition).getListConnections()){
							for (Connections mConnections:liscConnections) {
								if (mConnections.getId().equals(connections.getId())) {
									organizationTemps.add(connections);
								}
							}
						}
						mListJointOrganizationNode.get(groupPosition).getListConnections().removeAll(organizationTemps);
					}
				}
			}
			peoTitleTv.setText(getGroupTitleSpannableString(ResourceType.People));
			orgTitleTv.setText(getGroupTitleSpannableString(ResourceType.Organization));
			mOrganizationAdapter.notifyDataSetChanged();
			mPeopleAdapter.notifyDataSetChanged();
		}else if(resourceNode instanceof KnowledgeNode){//知识
			for(ResourceNode node:listResourceNode){
				ArrayList<KnowledgeMini2> listKnowledgeMini2 = ((KnowledgeNode) node).getListKnowledgeMini2();
				if (!listKnowledgeMini2.isEmpty()) {
					ArrayList<KnowledgeMini2> knowledgeMini2Temps = new ArrayList<KnowledgeMini2>();
					//遍历相同元素
					for(KnowledgeMini2 knowledgeMini2:mListJointKnowledgeNode.get(groupPosition).getListKnowledgeMini2()){
						for (KnowledgeMini2 mKnowledgeMini2:listKnowledgeMini2) {
							if (knowledgeMini2.id==mKnowledgeMini2.id) {
								knowledgeMini2Temps.add(knowledgeMini2);
							}
						}
					}
					mListJointKnowledgeNode.get(groupPosition).getListKnowledgeMini2().removeAll(knowledgeMini2Temps);
				}
			}
			knoTitleTv.setText(getGroupTitleSpannableString(ResourceType.Knowledge));
			mKnowledgeAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) { // 处理跳转
		if(parent == affElv){ // 事件
			if(mListJointAffairNode.get(groupPosition).getListAffairMini().size() > 3){
				ENavigate.startJointColumnActivity(getActivity(),
						ResourceType.Affair,
						mListJointAffairNode.get(groupPosition), mTarResType,
						getResourceId(mTarRes),100);
			}
			else{
				showToast(TIP_NO_DATA);
			}
		}
		else if(parent == peoElv){ // 人脉
			if(mListJointPeopleNode.get(groupPosition).getListConnections().size() > 5){
				ENavigate.startJointColumnActivity(getActivity(),
						ResourceType.People,
						mListJointPeopleNode.get(groupPosition), mTarResType,
						getResourceId(mTarRes),100);
			}
			else{
				showToast(TIP_NO_DATA);
			}
		}
		else if(parent == orgElv){ // 组织
			if(mListJointOrganizationNode.get(groupPosition).getListConnections().size() > 3){
				ENavigate.startJointColumnActivity(getActivity(),
						ResourceType.Organization,
						mListJointOrganizationNode.get(groupPosition),
						mTarResType, getResourceId(mTarRes),100);
			}
			else{
				showToast(TIP_NO_DATA);
			}
		}
		else{ // 知识
			if(mListJointKnowledgeNode.get(groupPosition).getListKnowledgeMini2().size() > 3){
				ENavigate.startJointColumnActivity(getActivity(),
						ResourceType.Knowledge,
						mListJointKnowledgeNode.get(groupPosition),
						mTarResType, getResourceId(mTarRes),100);
			}
			else{
				showToast(TIP_NO_DATA);
			}
		}
		return true;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		switch(parent.getId()){
		case R.id.affElv: // 需求详情
			break;
		case R.id.peoElv: // 人脉详情
			break;
		case R.id.orgElv: // 组织详情
			break;
		case R.id.knoElv: // 知识详情
			ENavigate.startKnowledgeOfDetailActivity(getActivity(), 
					mListJointKnowledgeNode.get(groupPosition).getListKnowledgeMini2().get(childPosition).id, 
					mListJointKnowledgeNode.get(groupPosition).getListKnowledgeMini2().get(childPosition).type);
			break;
		}
		return true;
	}

	@Override
	public boolean onItemLongClick(final AdapterView<?> parent, View view,
			int position, long id) {
		if (parent.equals(affElv)) {
			packedPosition = affElv.getExpandableListPosition(position);
		}else if(parent.equals(peoElv)){
			packedPosition=peoElv.getExpandableListPosition(position);
		}else if(parent.equals(orgElv)){
			packedPosition=orgElv.getExpandableListPosition(position);
		}else if (parent.equals(knoElv)) {
			packedPosition=knoElv.getExpandableListPosition(position);
		}
		
		int itemType = ExpandableListView.getPackedPositionType(packedPosition);
        if ( itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
        	
            childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
            groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
            
            new AlertDialog.Builder(getActivity())
            	.setTitle("提示")
            	.setMessage("对本条目纠错？")
            	.setPositiveButton("确定", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						listNode = new ArrayList<ResourceNode>();
			            switch(parent.getId()){
			            case R.id.affElv:
			            	AffairNode affairNode = new AffairNode();
			            	affairNode.setMemo(mListJointAffairNode.get(groupPosition).getMemo());
			            	affairNode.setType(mListJointAffairNode.get(groupPosition).getType());
			            	affairNode.getListAffairMini().add(mListJointAffairNode.get(groupPosition).getListAffairMini().get(childPosition));
			            	listNode.add(affairNode);
			            	break;
//			            case R.id.peoElv:
			            	/*
			            	ConnectionNode peopleNode = new ConnectionNode();
			            	peopleNode.setMemo(mListJointPeopleNode.get(groupPosition).getMemo());
			            	peopleNode.getListConnections().add(mListJointPeopleNode.get(groupPosition).getListConnections().get(childPosition));
			            	listNode.add(peopleNode);
			            	*/
//			            	break;
			            case R.id.orgElv:
			            	ConnectionNode organizationNode = new ConnectionNode();
			            	organizationNode.setMemo(mListJointOrganizationNode.get(groupPosition).getMemo());
			            	organizationNode.setType(mListJointOrganizationNode.get(groupPosition).getType());
			            	organizationNode.getListConnections().add(mListJointOrganizationNode.get(groupPosition).getListConnections().get(childPosition));
			            	listNode.add(organizationNode);
			            	break;
			            case R.id.knoElv:
			            	KnowledgeNode knowledgeNode = new KnowledgeNode();
			            	knowledgeNode.setMemo(mListJointKnowledgeNode.get(groupPosition).getMemo());
			            	knowledgeNode.setType(mListJointKnowledgeNode.get(groupPosition).getType());
			            	knowledgeNode.getListKnowledgeMini2().add(mListJointKnowledgeNode.get(groupPosition).getListKnowledgeMini2().get(childPosition));
			            	listNode.add(knowledgeNode);
			            	break;
			            }
			            ((JBaseFragmentActivity) getActivity()).showLoadingDialog();
			    		CommonReqUtil.doCorrectJointResource(getActivity(), JointResourceFragment.this, convertResourceType2Int(mTarResType), 
			    				getResourceId(mTarRes), listNode, null);
					}
            	})
            	.setNegativeButton("取消", null)
            	.create()
            	.show();
            // 转为目标数组
            return true; 
        } 
        else if(itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
            new AlertDialog.Builder(getActivity())
            	.setTitle("提示")
            	.setMessage("对本组条目纠错？")
            	.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						listNode = new ArrayList<ResourceNode>();
			            switch(parent.getId()){
			            case R.id.affElv:
			            	listNode.add(mListJointAffairNode.get(groupPosition));
			            	break;
			            case R.id.peoElv:
			            	listNode.add(mListJointPeopleNode.get(groupPosition));
			            	break;
			            case R.id.orgElv:
			            	listNode.add(mListJointOrganizationNode.get(groupPosition));
			            	break;
			            case R.id.knoElv:
			            	listNode.add(mListJointKnowledgeNode.get(groupPosition));
			            	break;
			            }
			            ((JBaseFragmentActivity) getActivity()).showLoadingDialog();
			            CommonReqUtil.doCorrectJointResource(getActivity(), JointResourceFragment.this, convertResourceType2Int(mTarResType), 
			    				getResourceId(mTarRes), listNode, null);
					}
				})
				.setNegativeButton("取消", null)
				.create()
				.show();
            return true; 
        } 
        else {
            return false;
        }
	}

	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
			outState.putSerializable("mListJointPeopleNode", mListJointPeopleNode);
			outState.putSerializable("mListJointOrganizationNode", mListJointOrganizationNode);
			outState.putSerializable("mListJointKnowledgeNode", mListJointKnowledgeNode);
			outState.putSerializable("mListJointAffairNode", mListJointAffairNode);
			outState.putInt("groupPosition", groupPosition);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode==Activity.RESULT_OK) {
			ArrayList<ResourceNode> correctionResource= (ArrayList<ResourceNode>) data.getSerializableExtra("correctionResource");
			if (!correctionResource.isEmpty()) {
				refreshExpandableList(correctionResource);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
