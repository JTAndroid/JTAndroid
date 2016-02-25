package com.tr.ui.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.db.ConnectionsCacheData;
import com.tr.db.ConnectionsDBManager;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.joint.ResourceNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.ResourceBase;
import com.tr.model.obj.ResourceBase.ResourceAttribute;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.widgets.MaxLengthWatcher;
import com.tr.ui.widgets.pulltorefreshExpandableListView.PullToRefreshLayout;
import com.tr.ui.widgets.pulltorefreshExpandableListView.PullToRefreshLayout.OnRefreshListener;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.GlobalVariable;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 关联资源
 * @author leon
 */
public class RelatedResourceFragment extends JBaseFragment implements IBindData{

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private EditText keywordEt; // 搜索
	private ImageView addIv; // 添加
//	private SwipeRefreshLayout refreshSrl; // 下拉刷新
	private ExpandableListView resourceElv; // 资源列表
	private EditText tagEt; //  标签
	
	// 变量
	private int page=1;//分页加载的页数从1开始
	private ResourceType mResType; // 资源类型
	private String mKeyword; // 搜索关键字
	private List<String> mGroupData; // 一级分类
	
	private List<Connections> mPlatformPeople ; // 推荐的人脉
	private List<Connections> mUserPeople; // 我的人脉
	private List<Connections> mPlatformOrganization; // 推荐的组织
	private List<Connections> mUserOrganization; // 我的组织
	private List<KnowledgeMini2> mPlatformKnowledge; // 推荐的知识
	private List<KnowledgeMini2> mUserKnowledge; // 我的知识
	private List<AffairsMini> mPlatformAffair; // 推荐的事件
	private List<AffairsMini> mUserAffair; // 我的事件
	
	private ResourceAdapter mResourceAdapter; // 适配器
	private ResourceNode mResNode; // 资源组对象
	// 人脉和用户数据库
	private ConnectionsDBManager mConnsDBManager;
	// 平台资源分页标识
	private int mPResIndex;
	private int mPResSize;
	private int mPResTotal;
	private int mPResTotalPage;
	// "我"的资源分页标识
	private int mUResIndex;
	private int mUResSize;
	private int mUResTotal;
	private int mUResTotalPage;
	DisplayImageOptions options;
	// 标识Fragment初始化类型
	private int mInitType; // 初始化类型，1-创建；2-添加返回；3-查看返回；4-其它返回（Home键等
	private String removeId ;//编辑自己资料 (关联人脉我的不显示自己)
	
	private View moreView; // 加载更多页面
	private TextView mvTextView;
	private View mvProgressBar;

	private int lastItem;

	private ConnectionsCacheData cnsCacheData;

	private PullToRefreshLayout pullToRefreshLayout;
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initVars();
		if(mResType == ResourceType.People){
			Log.d(TAG, "onCreate");
		}
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mResType == ResourceType.People){
			Log.d(TAG, "onCreateView");
		}
		
		View view = inflater.inflate(R.layout.frg_related_resource, container, false);
//		keywordEt = (EditText) view.findViewById(R.id.keywordEt);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_default_avatar)
		.showImageForEmptyUri(R.drawable.ic_default_avatar)
		.showImageOnFail(R.drawable.ic_default_avatar)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(false)
		.build();
		moreView = inflater.inflate(R.layout.pulldown_footer, null);
		//上拉加载
				mvTextView = (TextView) moreView
						.findViewById(R.id.pulldown_footer_text);
				mvProgressBar = (View) moreView
						.findViewById(R.id.pulldown_footer_loading);
				
				moreView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
//						mvProgressBar.setVisibility(View.VISIBLE);
						showLoadingDialog();
						page++;
						searchAllRelatedResource(mKeyword, page, 20);
//						searchPeopleAndOrganizationFromLoalDatabase(mResType, mKeyword);						
					}
				});
				moreView.setVisibility(View.GONE);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		if(mResType == ResourceType.People){
			Log.d(TAG, "onViewCreated");
		}
		initControls(view);
		// 创建时调用
		doInit();
	}
	
	// 初始化Fragment界面显示
	private void doInit(){
		// 如果从上一页面带过来了数据，直接显示，不请求网络和数据库
		switch(mResType){
		case People:
		case Organization:
			if(((ConnectionNode) mResNode).getListConnections().size() > 0){
				return;
			}
			break;
		case Knowledge:
			if(((KnowledgeNode) mResNode).getListKnowledgeMini2().size() > 0){
				return;
			}
			break;
		case Affair:
			if(((AffairNode) mResNode).getListAffairMini().size() > 0){
				return;
			}
			break;
		default:
			break;
		}

//		searchAllRelatedResource(mKeyword);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser) {
			showLoadingDialog();
			page=1;
			searchAllRelatedResource(mKeyword,1,20);
		}
	}
	
	// 将枚举资源类型转为int型
	private int convertResourceType2Int(ResourceType resType){
		int type = 0;
		switch(resType){
		case Affair:
			type = 1;
			break;
		case People:
			type = 2;
			break;
		case Organization:
			type = 3;
			break;
		case Knowledge:
			type = 4;
			break;
		default:
			break;
		}
		return type;
	}
	
	// 从本地数据库搜索人脉和组织  抛弃从本地数据库查询 改为从网络拿数据2015/10/19
//	private void searchPeopleAndOrganizationFromLoalDatabase(final ResourceType resType, final String keyword ){
//		new AsyncTask<Void, Void, ArrayList<Connections>>() {
//
//			private ArrayList<Connections> date;
//			@Override
//			protected ArrayList<Connections> doInBackground(Void... params) {
//				if(resType == ResourceType.People){
//					cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_PEOPLE_ALL);
//					 date = cnsCacheData.getDate(mUResIndex, 20);
//					 if (date!=null&&!date.isEmpty()) {
//						 mUserPeople.addAll(date);
//						 mUResIndex+= date.size();
//					}
////					
////					JTPage jtPage = mConnsDBManager.query(keywordEt.getText().toString(), type, mUResIndex, mUResSize);
////					
////					if(jtPage != null){
////						mUResIndex = jtPage.getIndex();
////						mUResSize = jtPage.getSize();
////						mUResTotalPage = jtPage.getTotalPage();
////						mUResTotal = jtPage.getTotal();
////						for(IPageBaseItem item : jtPage.getLists()){
////							mUserPeople.add((Connections) item);
////						}
////					}
//				}
//				else{
//					cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_ORG_ALL);
//					date = cnsCacheData.getDate(mUResIndex, 20);
//					 if (date!=null&&!date.isEmpty()) {
//						 mUserOrganization.addAll(date);
//						 mUResIndex+= date.size();
//					}
////					JTPage jtPage = mConnsDBManager.query(keywordEt.getText().toString(), type, mUResIndex, mUResSize);
////					if(jtPage != null){
////						mUResIndex = jtPage.getIndex();
////						mUResSize = jtPage.getSize();
////						mUResTotalPage = jtPage.getTotalPage();
////						mUResTotal = jtPage.getTotal();
////						for(IPageBaseItem item : jtPage.getLists()){
////							mUserOrganization.add((Connections) item);
////						}
////					}
//				}
//				return date;
//			}
//			@Override
//			protected void onPostExecute(ArrayList<Connections> result) {
//				mvProgressBar.setVisibility(View.GONE);
//				if (result!=null&&!result.isEmpty()) {
//					mResourceAdapter.notifyDataSetChanged();
//				}
//				super.onPostExecute(result);
//			}
//		}.execute();
////		new AsyncTask<Void, Void, Void>(){
////			@Override
////			protected Void doInBackground(Void... params) {
////				if(resType == ResourceType.People){ // 用户自己的人脉和好友
////					mUResSize = 20;
////				//	JTPage jtPage = mConnsDBManager.query(keywordEt.getText().toString(), 1, mUResIndex, mUResSize);
////					JTPage jtPage = mConnsDBManager.query(keyword, 1, mUResIndex, mUResSize);
////					if(jtPage != null){
////						mUResIndex += jtPage.getIndex();
////						mUResSize = jtPage.getSize();
////						mUResTotalPage = jtPage.getTotalPage();
////						mUResTotal = jtPage.getTotal();
////						mUserPeople.clear();
////						for(IPageBaseItem item : jtPage.getLists()){
////							Connections conns = (Connections) item;
////							conns.setAttribute(ResourceAttribute.My);
////							mUserPeople.add(conns);
////						}
////					}else{
////						resourceElv.removeFooterView(moreView);
////					}
////				}
////				
////				else if(resType == ResourceType.Organization){ // 用户自己的组织
////					mUResIndex++;
////					mUResSize = 20;
////					JTPage jtPage = mConnsDBManager.query(keyword, 2, mUResIndex, mUResSize);
////					if(jtPage != null){
////						mUResIndex += jtPage.getIndex();
////						mUResSize = jtPage.getSize();
////						mUResTotalPage = jtPage.getTotalPage();
////						mUResTotal = jtPage.getTotal();
////						mUserOrganization.clear();
////						for(IPageBaseItem item : jtPage.getLists()){
////							Connections conns = (Connections) item;
////							conns.setAttribute(ResourceAttribute.My);
////							mUserOrganization.add(conns);
////						}
////					}else{
////						resourceElv.removeFooterView(moreView);
////					}
////				}
////				
////				
////				return null;
////			}
////			@Override
////			protected void onPostExecute(Void result) {
////				mvProgressBar.setVisibility(View.GONE);
////				mResourceAdapter.notifyDataSetChanged();
////				super.onPostExecute(result);
////			}
////		}.execute();		
////		
//		
//	}

	
	// 初始化变量
	private void initVars(){
		
		
		
		// 参数对象
		Bundle bundle = getArguments();
		// 关键字，必传
//		mKeyword = bundle.getString(EConsts.Key.KNOWLEDGE_KEYWORD); 
		// 资源类型，必传
		mResType = (ResourceType) bundle.getSerializable(EConsts.Key.RELATED_RESOURCE_TYPE);
		// 资源组对象，可选
		mResNode = (ResourceNode) bundle.getSerializable(EConsts.Key.RELATED_RESOURCE_NODE);
		// 初始化列表
		mPlatformPeople = new ArrayList<Connections>();
		mUserPeople = new ArrayList<Connections>();
		mPlatformOrganization = new ArrayList<Connections>();
		mUserOrganization = new ArrayList<Connections>();
		mPlatformKnowledge = new ArrayList<KnowledgeMini2>();
		mUserKnowledge = new ArrayList<KnowledgeMini2>();
		mPlatformAffair = new ArrayList<AffairsMini>();
		mUserAffair = new ArrayList<AffairsMini>();
		// 填充资源
		switch (mResType) {
		case People:
		case Organization:
			if (mResNode == null) {
				mResNode = new ConnectionNode();
			} 
			else {
				for (Connections conns : ((ConnectionNode) mResNode).getListConnections()) {
					if (conns.getAttribute() == ResourceAttribute.My) { // 我的资源
						mUserPeople.add(conns);
					} 
					else { // 金桐网资源
						mPlatformPeople.add(conns);
					}
				}
			}
			break;
		case Affair:
			if (mResNode == null) {
				mResNode = new AffairNode();
			} 
			else {
				for (AffairsMini affair : ((AffairNode) mResNode).getListAffairMini()) {
					if (affair.getAttribute() == ResourceAttribute.My) {
						mUserAffair.add(affair);
					}
					else {
						mPlatformAffair.add(affair);
					}
				}
			}
			break;
		case Knowledge:
			if (mResNode == null) {
				mResNode = new KnowledgeNode();
			} 
			else {
				for (KnowledgeMini2 knowledge : ((KnowledgeNode) mResNode).getListKnowledgeMini2()) {
					if (knowledge.getAttribute() == ResourceAttribute.My) {
						mUserKnowledge.add(knowledge);
					}
					else {
						mPlatformKnowledge.add(knowledge);
					}
				}
			}
			break;
		default:
			break;
		}
		// 资源适配器
		mGroupData = new ArrayList<String>();
		mGroupData.add("金桐脑推荐");
		mGroupData.add("我的");
		mResourceAdapter = new ResourceAdapter(getActivity());
		// 平台人脉和用户分页标识
		mPResIndex = 0;
		mPResSize = 20;
		mPResTotal = 0;
		mPResTotalPage = 0;
		// "我"的人脉和用户分页标识
		mUResIndex = 0;
		mUResSize = 20;
		mUResTotal = 0;
		mUResTotalPage = 0;
		// 关系数据库
		mConnsDBManager = EUtil.getConnectionsDBManager(getActivity());
		cnsCacheData = new ConnectionsCacheData(mConnsDBManager);
		// 初始化类型
		mInitType = 1; // 初始化
	}
	
	// 初始化控件
	@SuppressWarnings("deprecation")
	private void initControls(View container){
		keywordEt = (EditText) container.findViewById(R.id.keywordEt);
		keywordEt.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_SEARCH){
					page=1;
					mKeyword=keywordEt.getText().toString();
					searchAllRelatedResource(mKeyword,1,20);
				}
				return false;
			}
		});
		keywordEt.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				page=1;
				mKeyword=keywordEt.getText().toString();
				searchAllRelatedResource(mKeyword,1,20);
			}

			@Override
			public void afterTextChanged(Editable s) {
//				refreshSrl.setRefreshing(true);
			}

		});
		addIv = (ImageView) container.findViewById(R.id.addIv);
		addIv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				// 设置页面初始化状态
				if(mResType != ResourceType.Organization){
					mInitType = 2; // 添加返回
				}
				
				switch(mResType){
				case People: // 人脉
//					ENavigate.startNewConnectionsActivity(getActivity(), NewConnectionsActivity.type_create_for_result, null, 
//							EConsts.ReqCode.CreateConnectionsForResult);
					ENavigate.startNewConnectionsActivity(getActivity(), 1, null, 0x123);
					break;
				case Organization: // 组织
//					showToast("无法创建组织");
					ENavigate.startCreateClienteleActivity(getActivity(),null,1,0L);
					break;
				case Knowledge: // 知识
					ENavigate.startCreateKnowledgeActivity(getActivity(), true, EConsts.ReqCode.CreateKnowledgeForResult);
					break;
				case Affair: // 事件
//					ENavigate.startCreateRequirementActivity(getActivity(), EConsts.ReqCode.CreateRequirementForResult);
					ENavigate.startDemandActivityForResult(getActivity(), EConsts.ReqCode.CreateRequirementForResult);
					break;
				default:
					break;
				}
				
				
			}
		});
	/*	refreshSrl = (SwipeRefreshLayout) container.findViewById(R.id.refreshSrl); // 下拉刷新
		refreshSrl.setColorScheme(android.R.color.holo_blue_bright, 
	            android.R.color.holo_green_light, 
	            android.R.color.holo_orange_light, 
	            android.R.color.holo_red_light);
		refreshSrl.setOnRefreshListener(new OnRefreshListener(){
			@Override
			public void onRefresh() {
				CommonReqUtil.doRelatedResource(getActivity(), RelatedResourceFragment.this, 
						mKeyword, convertResourceType2Int(mResType),0,20, null);
				// 从本地数据库搜索人脉和用户
//				searchPeopleAndOrganizationFromLoalDatabase(mResType,mKeyword);
			}
		});*/
		
		pullToRefreshLayout = (PullToRefreshLayout) container.findViewById(R.id.refresh_view);
		pullToRefreshLayout.hintLoadMoreLayout(true);
		pullToRefreshLayout.setOnRefreshListener(new MyListener());
		
		resourceElv = (ExpandableListView) container.findViewById(R.id.resourceElv);
		resourceElv.setGroupIndicator(null); // 去掉左侧默认箭头
		resourceElv.setAdapter(mResourceAdapter); // 设置适配器
		resourceElv.addFooterView(moreView);
		resourceElv.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				// 设置Fragment 初始化状态
				mInitType = 3;
				
				switch(mResType){
				case People: /*客户、人脉*/
					if(groupPosition == 0 && mPlatformPeople != null && mPlatformPeople.get(childPosition) != null){
						long UID = EUtil.isEmpty(mPlatformPeople.get(childPosition).getId()) ? 0 : Long.valueOf(mPlatformPeople.get(childPosition).getId());
						if(mPlatformPeople.get(childPosition).isOnline()){//客户
//							ENavigate.startRelationHomeActivity(getActivity(), mPlatformPeople.get(childPosition).getId(), 
//									mPlatformPeople.get(childPosition).isOnline(), ENavConsts.type_details_other);
							ENavigate.startContactsDetailsActivity(getActivity(), 2,UID, 210,1);
						}
						//人脉
						else{
							ENavigate.startContactsDetailsActivity(getActivity(), 2,UID, 210,1);
						}
					}
					else{
						if(childPosition < mUserPeople.size() && mUserPeople != null && mUserPeople.get(childPosition) != null){
							long UID = EUtil.isEmpty(mUserPeople.get(childPosition).getId()) ? 0 : Long.valueOf(mUserPeople.get(childPosition).getId());
							if(mUserPeople.get(childPosition).isOnline()){//好友
//								ENavigate.startRelationHomeActivity(getActivity(), mUserPeople.get(childPosition).getId(), 
//										mUserPeople.get(childPosition).isOnline(), ENavConsts.type_details_other);
								ENavigate.startRelationHomeActivity(getActivity(),UID+"",true,ENavConsts.type_details_other);	
							}
							//人脉
							else{
								ENavigate.startContactsDetailsActivity(getActivity(), 2,UID, 210);
							}
						}
					}
					break;
				case Organization: /*组织、客户*/
					long orgId = 0,creaetById = 0;
					if(groupPosition == 0){
						if(mPlatformOrganization != null && mPlatformOrganization.size() > 0){
							orgId = Long.valueOf(mPlatformOrganization.get(childPosition).getOrganizationMini().id);
//							orgId = EUtil.isEmpty(mPlatformOrganization.get(childPosition).getId()) ? 0L : Long.valueOf(mPlatformOrganization.get(childPosition).getId())  ;
							creaetById = EUtil.isEmpty(mPlatformOrganization.get(childPosition).getOrganizationMini().ownerid) ? 0L : Long.valueOf(mPlatformOrganization.get(childPosition).getOrganizationMini().ownerid)  ;
						}
						ENavigate.startOrgMyHomePageActivity(getActivity(), orgId,creaetById ,true, ENavConsts.type_details_org);
					}
					else{
						if(mUserOrganization != null && childPosition < mUserOrganization.size()){
							orgId = Long.valueOf(mUserOrganization.get(childPosition).getOrganizationMini().id);
//							orgId = EUtil.isEmpty(mUserOrganization.get(childPosition).getId()) ?  0L : Long.valueOf(mUserOrganization.get(childPosition).getId());
							creaetById = EUtil.isEmpty(mUserOrganization.get(childPosition).getOrganizationMini().getOwnerid()) ?  0L :Long.valueOf(mUserOrganization.get(childPosition).getOrganizationMini().getOwnerid());
							if (mUserOrganization.get(childPosition).getOrganizationMini().isOnline)// 组织
								ENavigate.startOrgMyHomePageActivity(getActivity(), orgId, creaetById, true, ENavConsts.type_details_org);
							else
								ENavigate.startClientDedailsActivity(getActivity(), orgId);

						}
					}
					break;
				case Knowledge: // 知识详情
					if(groupPosition == 0){
						ENavigate.startKnowledgeOfDetailActivity(getActivity(), mPlatformKnowledge.get(childPosition).id, 
								mPlatformKnowledge.get(childPosition).type);
					}
					else{
						ENavigate.startKnowledgeOfDetailActivity(getActivity(), mUserKnowledge.get(childPosition).id, 
								mUserKnowledge.get(childPosition).type);
					}
					break;
				case Affair: // 事件详情
					if(groupPosition == 0){
					/*	ENavigate.startRequirementDetailActivity(getActivity(), mPlatformAffair.get(childPosition).id);*/
						ENavigate.startNeedDetailsActivity(getActivity(),mPlatformAffair.get(childPosition).id+"",2);
					}
					else{
						/*ENavigate.startRequirementDetailActivity(getActivity(), mUserAffair.get(childPosition).id);*/
						ENavigate.startNeedDetailsActivity(getActivity(),mUserAffair.get(childPosition).id+"",1);
					}
					break; 
				default:
					break;
				}
				return false;
			}
		});
		// 展开
		unfoldExpandableListView(resourceElv);
		// 标签
		tagEt = (EditText) container.findViewById(R.id.tagEt);
		tagEt.addTextChangedListener(new MaxLengthWatcher(getActivity(), 20, "字符数长度不能超过20", tagEt));
		tagEt.setText(mResNode.getMemo());
		tagEt.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(!checkResNode(true, true)){
					hideSoftInput();
					return true;
				}else{
					return false;
				}

			}
		});
		tagEt.setOnEditorActionListener(new OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				switch(actionId){  
		        case EditorInfo.IME_NULL:  
		        case EditorInfo.IME_ACTION_SEND:  
		        case EditorInfo.IME_ACTION_DONE:  
		        	if(infoIntegrityCheck(true)){
		        		Intent intent = new Intent();
		        		mResNode.setMemo(tagEt.getText().toString());
		        		switch(mResType){
		        		case Affair:
		        			intent.putExtra(EConsts.Key.RELATED_AFFAIR_NODE, mResNode); 
		        			break;
		        		case People:
		        			intent.putExtra(EConsts.Key.RELATED_PEOPLE_NODE, mResNode); 
		        			break;
		        		case Organization:
		        			intent.putExtra(EConsts.Key.RELATED_ORGANIZATION_NODE, mResNode); 
		        			break;
		        		case Knowledge:
		        			intent.putExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE, mResNode); 
		        			break;
		        		default:
		        			break;
		        		}
		        		hideSoftInput();
                      /*getActivity().setResult(Activity.RESULT_OK, intent);
		        		getActivity().finish();*/
		        	}
		            break;  
		        }  
		        return true;  
			}
		});
	}
	
	/**
	 * 查询全部关联数据
	 * @param keyWord
	 * @param page  2015/10/19新增分页加载的页数
	 * @param size  2015/10/19新增分页加载的数量
	 */
	private void searchAllRelatedResource(String keyWord,int page,int size) {
		CommonReqUtil.doRelatedResource(getActivity(), RelatedResourceFragment.this, 
				keyWord, convertResourceType2Int(mResType),page,size, null);
		// 从本地数据库搜索人脉和用户
//		searchPeopleAndOrganizationFromLoalDatabase(mResType, keyWord);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*
		 * 暂时屏蔽掉此操作，改为搜索获取
		if(resultCode != Activity.RESULT_OK){
			return;
		}
		switch(requestCode){
		case EConsts.ReqCode.CreateConnectionsForResult: // 创建人脉
			Connections people = (Connections) data.getSerializableExtra(EConsts.Key.CONNECTIONS);
			if(people != null){
				mUserPeople.add(0, people);
				resourceElv.collapseGroup(0);
				resourceElv.expandGroup(1, true);
				mResourceAdapter.notifyDataSetChanged();
			}
			break;
		case EConsts.ReqCode.CreateKnowledgeForResult: // 创建知识
			KnowledgeMini2 knowledge = (KnowledgeMini2) data.getSerializableExtra(EConsts.Key.KNOWLEDGE_MINI2);
			if(knowledge != null){
				mUserKnowledge.add(0, knowledge);
				resourceElv.collapseGroup(0);
				resourceElv.expandGroup(1, true);
				mResourceAdapter.notifyDataSetChanged();
			}
			break;
		case EConsts.ReqCode.CreateRequirementForResult: // 创建需求
			AffairsMini affair = (AffairsMini) data.getSerializableExtra(EConsts.Key.AFFAIR_MINI);
			if(affair != null){
				mUserAffair.add(0, affair);
				resourceElv.collapseGroup(0);
				resourceElv.expandGroup(1, true);
				mResourceAdapter.notifyDataSetChanged();
			}
			break;
		}
		*/
		
		// 刷新列表
		/*
		if(resultCode != Activity.RESULT_OK){
			return;
		}
		refreshSrl.setRefreshing(true);
		switch(requestCode){
		case EConsts.ReqCode.CreateConnectionsForResult: // 创建人脉
			searchPeopleAndOrganizationFromLoalDatabase(keywordEt.getText().toString()); // 搜本地
			refreshSrl.setRefreshing(false);
			break;
		case EConsts.ReqCode.CreateKnowledgeForResult: // 创建知识
		case EConsts.ReqCode.CreateRequirementForResult: // 创建需求
			CommonReqUtil.doRelatedResource(getActivity(), this, 
					keywordEt.getText().toString(), convertResourceType2Int(mResType), null);
			break;
		default:
			break;
		}
		*/
	}
	
	@Override
	public void onStart(){
		super.onStart();
		if(mResType == ResourceType.People){
			Log.d(TAG, "onStart");
		}
	}
	
//	@Override 2015/10/20不用这个进行拿数据，用setUserVisibleHint 避免造成同时多处地方进行网络请求 
//	public void onResume(){
//		super.onResume();
//		switch(mInitType){
//		case 1: // 创建
//			break;
//		case 2: // 添加返回
//		case 3: // 查看返回
//			refreshSrl.setRefreshing(true);
//			CommonReqUtil.doRelatedResource(getActivity(), this, 
//					mKeyword, convertResourceType2Int(mResType), null);
//			// 搜索本地人脉和组织数据库
//			searchPeopleAndOrganizationFromLoalDatabase(mResType, keywordEt.getText().toString());
//			break;
//		case 4: // 其它
//			break;
//		}
//	}
	
	@Override
	public void onPause(){
		super.onPause();
		if(mResType == ResourceType.People){
			Log.d(TAG, "onPause");
		}
	}
	
	@Override
	public void onStop(){
		super.onStop();
		if(mResType == ResourceType.People){
			Log.d(TAG, "onStop");
		}
	}
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState){
		super.onViewStateRestored(savedInstanceState);
		if(mResType == ResourceType.People){
			Log.d(TAG, "onViewStateRestored");
		}
	}
	
	@Override
	public void onDestroyView(){
		super.onDestroy();
		if(mResType == ResourceType.People){
			Log.d(TAG, "onDestroyView");
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(mResType == ResourceType.People){
			Log.d(TAG, "onDestroy");
		}
	}
	
	@Override
	public void onDetach(){
		super.onDetach();
		if(mResType == ResourceType.People){
			Log.d(TAG, "onDetach");
		}
	}
	
	// 展开ExpandableListView
	private void unfoldExpandableListView(ExpandableListView elv){
		for(int i = 0; i < elv.getExpandableListAdapter().getGroupCount(); i++){
			if (i>0) {
				elv.expandGroup(i);
			}
		}
	}
	
	// 隐藏软键盘
	private void hideSoftInput(){
		
		View view = null;
		if(tagEt.hasFocus()){
			view = tagEt;
		}
		else if(keywordEt != null && keywordEt.hasFocus()){
			view = keywordEt;
		}
		if(view != null){
			((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
			.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);   
		}
	}
	
	/**
	 * 信息完整性检查
	 * @return
	 */
	public boolean infoIntegrityCheck(boolean showTip){
		boolean integral = true;
		integral = checkTagET(showTip, integral) && checkResNode(showTip, integral);
		return integral;
	}
	
	/**
	 * 信息是否编辑检查
	 * @return
	 */
	public boolean infoEditCheck(boolean showTip){
		boolean integral = true;
		integral = checkTagET(showTip, integral) || checkResNode(showTip, integral);
		return integral;
	}
	/** 获取到输入框View  */
	public View getEditTextView(){
		return tagEt;
	}

	/**
	 * 检测资源
	 * @param showTip 是否显示消息信息
	 * @param integral  
	 * @return
	 */
	private boolean checkResNode(boolean showTip, boolean integral) {
		switch(mResType){
		case People:
			integral = ((ConnectionNode) mResNode).getListConnections().size() > 0 ? true : false;
			if(!integral && showTip){
				showToast("请选择关联人脉");
			}
			break;
		case Organization:
			integral = ((ConnectionNode) mResNode).getListConnections().size() > 0 ? true : false;
			if(!integral && showTip){
				showToast("请选择关联组织");
			}
			break;
		case Knowledge:
			integral = ((KnowledgeNode) mResNode).getListKnowledgeMini2().size() > 0 ? true : false;
			if(!integral && showTip){
				showToast("请选择关联知识");
			}
			break;
		case Affair:
			integral = ((AffairNode) mResNode).getListAffairMini().size() > 0 ? true : false;
			if(!integral && showTip){
				showToast("请选择关联事件");
			}
			break;
		default:
			break;
		}
		return integral;
	}

	/**
	 * 检测文本
	 * @param showTip 是否显示消息信息
	 * @param integral
	 * @return
	 */
	private boolean checkTagET(boolean showTip, boolean integral) {
		if(tagEt == null || TextUtils.isEmpty(tagEt.getText().toString())){
			integral = false;
			if(showTip){
				showToast("请填写关系标签");
			}
		}
		return integral;
	}
	
	/**
	 * 获取资源组
	 * @return
	 */
	public ResourceNode getResourceNode(){
		if(!infoIntegrityCheck(false)){
			return null;
		}
		mResNode.setType(convertResourceType2Int(mResType)+"");
		mResNode.setMemo(tagEt.getText().toString());
		return mResNode;
	}
	
	// 该人脉是否选中
	private boolean isPeopleSelect(Connections targetConns){
		boolean select = false;
		if(targetConns.type != Connections.type_persion 
				|| targetConns.jtContactMini == null){ // 非人脉对象
			return select;
		}
		for(Connections conns : ((ConnectionNode) mResNode).getListConnections()){
			if(conns.jtContactMini != null){
				if(targetConns.getId()!=null && conns.getId().equals(targetConns.getId())){
					select = true;
					break;
				}
			}
		}
		return select;
	}
	
	// 编辑选中的人脉
	private void editSelectedPeople(Connections targetConns, boolean select){
		
		if(targetConns.type != Connections.type_persion 
				|| targetConns.jtContactMini == null){ // 非人脉
			return;
		}
		if(select && !isPeopleSelect(targetConns)){ // 选择
			((ConnectionNode) mResNode).getListConnections().add(targetConns);
		}
		else if(!select && isPeopleSelect(targetConns)){ // 取消选择
			for(Connections conns : ((ConnectionNode) mResNode).getListConnections()){
				if(conns.jtContactMini != null){
					if(conns.getId().equals(targetConns.getId())){
						((ConnectionNode) mResNode).getListConnections().remove(conns);
						break;
					}
				}
			}
		}
	}
	
	// 该组织是否选中
	private boolean isOrganizationSelect(Connections targetConns){
		
		boolean select = false;
		if(targetConns.type != Connections.type_org 
				|| targetConns.organizationMini == null){ // 非组织
			return select;
		}
		for(Connections conns :  ((ConnectionNode) mResNode).getListConnections()){
			if(conns.organizationMini != null){
				if(targetConns.getId() != null && conns.getId().equals(targetConns.getId())){
					select = true;
					break;
				}
			}
		}
		return select;
	}
	
	// 编辑选中的组织
	private void editSelectedOrganization(Connections targetConns, boolean select) {
		
		if(targetConns.type != Connections.type_org
				|| targetConns.organizationMini == null){ // 非组织
			return;
		}
		if (select && !isOrganizationSelect(targetConns)) { // 选择
			 ((ConnectionNode) mResNode).getListConnections().add(targetConns);
		} 
		else if (!select && isOrganizationSelect(targetConns)) { // 取消选择

			for (Connections conns :  ((ConnectionNode) mResNode).getListConnections()) {
				if (conns.organizationMini != null) {
					if (conns.getId().equals(targetConns.getId())) {
						((ConnectionNode) mResNode).getListConnections().remove(conns);
						break;
					}
				}
			}
		}
	}
	
	// 该知识是否选中
	private boolean isKnowledgeSelect(KnowledgeMini2 targetKno){
		boolean select = false;
		if(targetKno == null){
			return select;
		}
		for(KnowledgeMini2 kno : ((KnowledgeNode) mResNode).getListKnowledgeMini2()){
			if(kno.id == targetKno.id){
				select = true;
				break;
			}
		}
		return select;
	}
	
	// 编辑选中的知识
	private void editSelectedKnowledge(KnowledgeMini2 targetKno, boolean select) {

		if(targetKno == null){
			return;
		}
		if (select && !isKnowledgeSelect(targetKno)) { // 选择
			((KnowledgeNode) mResNode).getListKnowledgeMini2().add(targetKno);
		} 
		else if (!select && isKnowledgeSelect(targetKno)) { // 取消选择
			for (KnowledgeMini2 kno : ((KnowledgeNode) mResNode).getListKnowledgeMini2()) {
				if(kno.id == targetKno.id){
					((KnowledgeNode) mResNode).getListKnowledgeMini2().remove(kno);
					break;
				}
			}
		}
	}
	
	// 该事件是否选中
	private boolean isAffairSelect(AffairsMini targetAff){
		boolean select = false;
		if(targetAff == null){
			return select;
		}
		for(AffairsMini aff : ((AffairNode) mResNode).getListAffairMini()){
			if(aff.id == targetAff.id){
				select = true;
				break;
			}
		}
		return select;
	}
	
	// 编辑选中的事件
	private void editSelectedAffair(AffairsMini targetAff, boolean select) {

		if(targetAff == null){
			return;
		}
		if (select && !isAffairSelect(targetAff)) { // 选择
			((AffairNode) mResNode).getListAffairMini().add(targetAff);
		} 
		else if (!select && isAffairSelect(targetAff)) { // 取消选择
			for (AffairsMini aff : ((AffairNode) mResNode).getListAffairMini()) {
				if(aff.id == targetAff.id){
					((AffairNode) mResNode).getListAffairMini().remove(aff);
					break;
				}
			}
		}
	}
	
	// 列表适配器
	class ResourceAdapter extends BaseExpandableListAdapter{

		private final int ITEM_TYPE_NORMAL = 0; // 正常样式
		private final int ITEM_TYPE_MORE = 1; // 底部更多
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		
		private Context mContext;
		
		public ResourceAdapter(Context context){
			mContext = context;
		}

		@Override
		public int getGroupCount() {
			return mGroupData.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			switch (mResType) {
			/*
			case People:
				return groupPosition == 0 ? mPlatformPeople.size()
						: mUserPeople.size();
			case Organization:
				return groupPosition == 0 ? mPlatformOrganization.size()
						: mUserOrganization.size();
			*/
			case People:
				return groupPosition == 0 ? mPlatformPeople.size()
						: (mUResIndex >= mUResTotalPage ? mUserPeople.size() : mUserPeople.size() + 1);
			case Organization:
				return groupPosition == 0 ? mPlatformOrganization.size()
						: (mUResIndex >= mUResTotalPage ? mUserOrganization.size() : mUserOrganization.size() + 1);
			case Knowledge:
				return groupPosition == 0 ? mPlatformKnowledge.size()
						: mUserKnowledge.size();
			case Affair:
				return groupPosition == 0 ? mPlatformAffair.size()
						: mUserAffair.size();
			default:
				return 0;
			}
			
		}

		@Override
		public Object getGroup(int groupPosition) {
			return mGroupData.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}
		
		@Override
		public int getChildTypeCount(){
			return 2;
		}
	
		@Override
		public int getChildType(int groupPosition, int childPosition){
			if(groupPosition == 0){
				return ITEM_TYPE_NORMAL;
			}
			else{
				switch(mResType){
				case People:
					if(childPosition == mUserPeople.size()){
						return ITEM_TYPE_MORE;
					}
					else{
						return ITEM_TYPE_NORMAL;
					}
				case Organization:
					if(childPosition == mUserOrganization.size()){
						return ITEM_TYPE_MORE;
					}
					else{
						return ITEM_TYPE_NORMAL;
					}
				default:
					return ITEM_TYPE_NORMAL;
				}
			}
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_related_resource_title, parent, false);
				holder = new GroupViewHolder();
				holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
				holder.arrowIv = (ImageView) convertView.findViewById(R.id.arrowIv);
				convertView.setTag(holder);
			}
			else{
				holder = (GroupViewHolder) convertView.getTag();
			}
			holder.titleTv.setText(mGroupData.get(groupPosition));
			if(isExpanded){
				holder.arrowIv.setImageResource(R.drawable.related_res_arrow_down);
			}
			else{
				holder.arrowIv.setImageResource(R.drawable.related_res_arrow_up);
			}
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			
			int childType = getChildType(groupPosition, childPosition);
			if(childType == ITEM_TYPE_MORE){ // 更多
				ChildViewHolder holder = null;
				if(convertView == null){
					holder = new ChildViewHolder();
					// 人脉和组织
					convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_footer, parent, false);
					holder.init(convertView);
					convertView.setTag(holder);
				}
				else{
					holder = (ChildViewHolder) convertView.getTag();
				}
//				if(mResType == ResourceType.People){
//					holder.moreTv.setTag(1); 
//				}
//				else{
//					holder.moreTv.setTag(2); 
//				}
			}
			else{ // 正常
				ChildViewHolder holder = null;
				if(convertView == null){
					holder = new ChildViewHolder();
					switch(mResType){
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
					holder = (ChildViewHolder) convertView.getTag();
				}
				if(groupPosition == 0){ // 平台资源
					switch(mResType){ 
					case People:
						if(childPosition < mPlatformPeople.size()){
							holder.build(mResType, mPlatformPeople.get(childPosition), groupPosition);
						}
						break;
					case Organization:
						if(childPosition < mPlatformOrganization.size()){
							holder.build(mResType, mPlatformOrganization.get(childPosition), groupPosition);
						}
						break;
					case Knowledge:
						if(childPosition < mPlatformKnowledge.size()){
							holder.build(mResType, mPlatformKnowledge.get(childPosition), groupPosition);
						}
						break;
					case Affair:
						if(childPosition < mPlatformAffair.size()){
							holder.build(mResType, mPlatformAffair.get(childPosition), groupPosition);
						}
						break;
					}
				}
				else{ // "我"的资源
					switch(mResType){ 
					case People:
						if(childPosition < mUserPeople.size()){
							if(!(mUserPeople.get(childPosition) != null && mUserPeople.get(childPosition).getId().equals(removeId))){
								holder.build(mResType, mUserPeople.get(childPosition), groupPosition);
							}
						}
						break;
					case Organization:
						if(childPosition < mUserOrganization.size()){
							holder.build(mResType, mUserOrganization.get(childPosition), groupPosition);
						}
						break;
					case Knowledge:
						if(childPosition < mUserKnowledge.size()){
							holder.build(mResType, mUserKnowledge.get(childPosition), groupPosition);
						}
						break;
					case Affair:
						if(childPosition < mUserAffair.size()){
							holder.build(mResType, mUserAffair.get(childPosition), groupPosition);
						}
						break;
					}
				}
			}
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
		class GroupViewHolder{
			
			TextView titleTv;
			ImageView arrowIv;
		}
		
		class ChildViewHolder implements OnClickListener{
			
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
				if(selectCb != null){
					selectCb.setOnClickListener(this);
				}
				avatarIv = (ImageView) container.findViewById(R.id.avatarIv);
				titleTv = (TextView) container.findViewById(R.id.titleTv);
				tagTv = (TextView) container.findViewById(R.id.tagTv);
				timeTv = (TextView) container.findViewById(R.id.timeTv);
				authorTv = (TextView) container.findViewById(R.id.authorTv);
			//	keywordEt = (EditText) container.findViewById(R.id.keywordEt);
				nameTv = (TextView) container.findViewById(R.id.nameTv);
				// 加载更多
//				moreTv = (TextView) container.findViewById(R.id.moreTv);
//				if(moreTv != null){
//					moreTv.setOnClickListener(this);
//				}
				loadingPb = (ProgressBar) container.findViewById(R.id.loadingPb);
				tagIv = (ImageView) container.findViewById(R.id.tagIv);
			}
			
			public void build(ResourceType resType, ResourceBase res, int source){
				switch(resType){
				case People:
					if(avatarIv == null || titleTv == null){
						return;
					}
					Connections people = (Connections) res;
					titleTv.setText(people.getName());
					if(!TextUtils.isEmpty(people.getImage())){
						Util.initAvatarImage(mContext, avatarIv, titleTv.getText().toString(), people.getImage(), 1, 1);
					}else{
						avatarIv.setImageResource(R.drawable.ic_default_avatar);
					}
					if(people.getJtContactMini().getFriendState()==0){
						tagIv.setImageResource(R.drawable.contactfriendtag);
					}else{
						tagIv.setImageResource(R.drawable.contactpeopletag);
					}
					if(source == 0){
						tagTv.setText("");
						tagTv.setVisibility(View.GONE);
					}
					else{
						tagTv.setText(people.isOnline() ? "我的好友" : "我的人脉");
					}
					selectCb.setChecked(isPeopleSelect(people));
					selectCb.setTag(people);
					break;
				case Organization:
					Connections organization = (Connections) res;
					titleTv.setText(!TextUtils.isEmpty(organization.organizationMini.shortName)?organization.organizationMini.shortName:organization.organizationMini.fullName);
					if(!TextUtils.isEmpty(organization.getImage())){
						Util.initAvatarImage(mContext, avatarIv, titleTv.getText().toString(), organization.organizationMini.getLogo(), 1, 1);
					}else{
						avatarIv.setImageResource(R.drawable.default_portrait116);
					}
					if(organization.getOrganizationMini().isOnline){
						tagIv.setImageResource(R.drawable.contactorganizationtag);
					}else{
						tagIv.setImageResource(R.drawable.contactclienttag);
					}
					tagTv.setText("");
					selectCb.setChecked(isOrganizationSelect(organization));
					selectCb.setTag(organization);
					break;
				case Affair:
					AffairsMini affair = (AffairsMini) res;
					titleTv.setText(affair.title.trim());
					if(source == 0){
						tagTv.setText(affair.name);
					}
					else{
						tagTv.setText(affair.content);
					}
					timeTv.setText(affair.time);
//					authorTv.setText(affair.name);
					selectCb.setChecked(isAffairSelect(affair));
					selectCb.setTag(affair);
					break;
				case Knowledge:
					KnowledgeMini2 knowledge = (KnowledgeMini2) res;
					titleTv.setText(knowledge.title.trim());
					if(source == 0){
						tagTv.setText(knowledge.desc);
					}
					else{
						tagTv.setText(knowledge.desc);
					}
					timeTv.setText(knowledge.modifytime);
					nameTv.setText(knowledge.connections != null ? knowledge.connections.getMiniName():"");
					selectCb.setChecked(isKnowledgeSelect(knowledge));
					selectCb.setTag(knowledge);
					break;
				default:
					break;
				}
			}

			@Override
			public void onClick(View v) {
				
				if(v.getId() == R.id.selectCb){ // 选择
					
					switch(mResType){
					case People:
						editSelectedPeople((Connections) v.getTag(), ((CheckBox) v).isChecked());
						break;
					case Organization:
						editSelectedOrganization((Connections) v.getTag(), ((CheckBox) v).isChecked());
						break;
					case Affair:
						editSelectedAffair((AffairsMini) v.getTag(), ((CheckBox) v).isChecked());
						break;
					case Knowledge:
						editSelectedKnowledge((KnowledgeMini2) v.getTag(), ((CheckBox) v).isChecked());
						break;
					default:
						break;
					}
				}
//				else if(v.getId() == R.id.moreTv){ // 加载更多
//					
//					moreTv.setVisibility(View.INVISIBLE);
//					loadingPb.setVisibility(View.VISIBLE);
//					
//					final int type =  (Integer) v.getTag();
//					
//					new AsyncTask<Void, Void, Void>() {
//
//						@Override
//						protected Void doInBackground(Void... params) {
//							if(type == 1){
//								cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_PEOPLE_ALL);
//								 ArrayList<Connections> date = cnsCacheData.getDate(mUResIndex, 20);
//								 if (date!=null&&!date.isEmpty()) {
//									 mUserPeople.addAll(date);
//									 mUResIndex+= date.size();
//								}
////								
////								JTPage jtPage = mConnsDBManager.query(keywordEt.getText().toString(), type, mUResIndex, mUResSize);
////								
////								if(jtPage != null){
////									mUResIndex = jtPage.getIndex();
////									mUResSize = jtPage.getSize();
////									mUResTotalPage = jtPage.getTotalPage();
////									mUResTotal = jtPage.getTotal();
////									for(IPageBaseItem item : jtPage.getLists()){
////										mUserPeople.add((Connections) item);
////									}
////								}
//							}
//							else{
//								cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_ALL_ORG);
//								 ArrayList<Connections> date = cnsCacheData.getDate(mUResIndex, 20);
//								 if (date!=null&&!date.isEmpty()) {
//									 mUserOrganization.addAll(date);
//									 mUResIndex+= date.size();
//								}
////								JTPage jtPage = mConnsDBManager.query(keywordEt.getText().toString(), type, mUResIndex, mUResSize);
////								if(jtPage != null){
////									mUResIndex = jtPage.getIndex();
////									mUResSize = jtPage.getSize();
////									mUResTotalPage = jtPage.getTotalPage();
////									mUResTotal = jtPage.getTotal();
////									for(IPageBaseItem item : jtPage.getLists()){
////										mUserOrganization.add((Connections) item);
////									}
////								}
//							}
//							return null;
//						}
//					}.execute();					
//					
//					moreTv.setVisibility(View.VISIBLE);
//					loadingPb.setVisibility(View.GONE);
//					Log.d(TAG, "子项数量：" + getChildrenCount(1));
//					notifyDataSetChanged();
//				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {
		// 停止刷新
		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
		
		// 处理返回数据
		if(tag == EAPIConsts.CommonReqType.GetRelatedResource){
			if(object != null){
				HashMap<String,Object> dataBox = (HashMap<String, Object>) object;
				switch(mResType){
				case Affair: 
					// 平台推荐
					List<AffairsMini> listPlatformAffair = (List<AffairsMini>) dataBox.get("listPlatformAffair"); 
					if(listPlatformAffair != null){
						for(AffairsMini affair : listPlatformAffair){
							affair.setAttribute(ResourceAttribute.Platform); // 来源
							affair.setSelected(isAffairSelect(affair)); // 是否已选择
						}
						if(page==1)
						mPlatformAffair = listPlatformAffair;
					}
					// "我"的
					List<AffairsMini> listUserAffair = (List<AffairsMini>) dataBox.get("listUserAffair"); 
					if(listUserAffair != null&&listUserAffair.size()>0){
						moreView.setVisibility(View.VISIBLE);
						for(AffairsMini affair : listUserAffair){
							affair.setAttribute(ResourceAttribute.My); // 来源
							affair.setSelected(isAffairSelect(affair)); // 是否已选择
						}
						if(page==1)
							mUserAffair=listUserAffair;
						else{
						    mUserAffair.addAll(listUserAffair);
						    page++;
						}
					}
					else
						moreView.setVisibility(View.GONE);
					break;
				case People:
					List<Connections> listPlatformPeople = (List<Connections>) dataBox.get("listPlatformPeople"); 
					if(listPlatformPeople != null){
						for(Connections people : listPlatformPeople){
							people.setAttribute(ResourceAttribute.Platform); // 来源
							people.setSelected(isPeopleSelect(people)); // 是否已选择
						}
						if(page==1)
						mPlatformPeople = listPlatformPeople;
					}
					List<Connections> listUserPeople = (List<Connections>) dataBox.get("listUserPeople"); 
					if(listUserPeople != null&&listUserPeople.size()>0){
						if (listUserPeople.size() != 20) {
							moreView.setVisibility(View.GONE);
						}else {
							moreView.setVisibility(View.VISIBLE);
						}
						for(Connections people : listUserPeople){
							people.setAttribute(ResourceAttribute.My); // 来源
							people.setSelected(isPeopleSelect(people)); // 是否已选择
						}
						if(page==1)
							mUserPeople = listUserPeople;
						else
							mUserPeople.addAll(listUserPeople);
					}
					else
						moreView.setVisibility(View.GONE);
					break;
				case Organization:
					List<Connections> listPlatformOrganization = (List<Connections>) dataBox.get("listPlatformOrganization");
					
					if(listPlatformOrganization != null){
						for(Connections organization : listPlatformOrganization){
							organization.setAttribute(ResourceAttribute.Platform); // 来源
							organization.setSelected(isOrganizationSelect(organization)); // 是否已选择
						}
						if(page==1)
						mPlatformOrganization = listPlatformOrganization;
					}
					List<Connections> listUserOrganization = (List<Connections>) dataBox.get("listUserOrganization"); 
					if(listUserOrganization != null&&listUserOrganization.size()>0){
						//当最后一页时去掉更多按钮
						if (listUserOrganization.size() != 20) {
							moreView.setVisibility(View.GONE);
						}else {
							moreView.setVisibility(View.VISIBLE);
						}
						for(Connections organization : listUserOrganization){
							organization.setAttribute(ResourceAttribute.My); // 来源
							organization.setSelected(isOrganizationSelect(organization)); // 是否已选择
						}
						if(page==1)
							mUserOrganization = listUserOrganization;
						else
							mUserOrganization.addAll(listUserOrganization);
					}
					else
						moreView.setVisibility(View.GONE);
					break;
					
				case Knowledge:
					List<KnowledgeMini2> listPlatformKnowledge = (List<KnowledgeMini2>) dataBox.get("listPlatformKnowledge"); 
					if(listPlatformKnowledge != null){
						for(KnowledgeMini2 knowledge : listPlatformKnowledge){
							knowledge.setAttribute(ResourceAttribute.Platform); // 来源
							knowledge.setSelected(isKnowledgeSelect(knowledge)); // 是否已选择
						}
						if(page==1)
						mPlatformKnowledge = listPlatformKnowledge;
					}
					List<KnowledgeMini2> listUserKnowledge = (List<KnowledgeMini2>) dataBox.get("listUserKnowledge"); 
					if(listUserKnowledge != null&&listUserKnowledge.size()>0){
						moreView.setVisibility(View.VISIBLE);
						for(KnowledgeMini2 knowledge : listUserKnowledge){
							knowledge.setAttribute(ResourceAttribute.My); // 来源
							knowledge.setSelected(isKnowledgeSelect(knowledge)); // 是否已选择
						}
						if(page==1)
							mUserKnowledge = listUserKnowledge;
						else
							mUserKnowledge.addAll(listUserKnowledge);
					}
					else
						moreView.setVisibility(View.GONE);;
					break;
				}
			}
			// 更新列表
			mResourceAdapter.notifyDataSetChanged();
			unfoldExpandableListView(resourceElv);
			dismissLoadingDialog();
		}
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	public void setRemoveId(String removeId) {
		this.removeId = removeId;
	}
	/** 刷新的监听 */
	class MyListener implements OnRefreshListener {

		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

			CommonReqUtil.doRelatedResource(getActivity(), RelatedResourceFragment.this, 
					mKeyword, convertResourceType2Int(mResType),0,20, null);
			// 从本地数据库搜索人脉和用户
//			searchPeopleAndOrganizationFromLoalDatabase(mResType,mKeyword);
		
		}

		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
			pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
		}
	}

}
