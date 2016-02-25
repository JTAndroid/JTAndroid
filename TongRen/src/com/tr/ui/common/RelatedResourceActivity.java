package com.tr.ui.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tr.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;

import com.tr.api.CommunityReqUtil;
import com.tr.model.demand.ASSOData;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.communities.home.GroupMembersManagementActivity;
import com.tr.ui.conference.home.MeetingNoticeActivity2;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.utils.common.EConsts;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 关联资源界面
 * @author leon
 */
public class RelatedResourceActivity extends JBaseFragmentActivity implements IBindData{

	private final String TAG = getClass().getSimpleName();
	
	private RadioGroup tabRgp;
	private ViewPager resourceVp;
	
	private ResourceAdapter mAdapter;
	private List<RelatedResourceFragment> mListFragment;
	
	private ConnectionNode mSelectPeopleNode; // 人脉组
	private ConnectionNode mSelectOrganizationNode; // 组织组
	private KnowledgeNode mSelectKnowledgeNode; // 知识组
	private AffairNode mSelectAffairNode; // 事件组
	private Map connectionAllNode;
	private ResourceType mResType; // 当前选中的资源类型
	private String mKeyword; // 筛选关键字

	private String removeId;
	private long communityId = -1;// 创建成功返回的社群id；
	private long createrId = -1;// 创建者id；
	
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(),"关联资源" ,false,null,false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_related_resources);
		initVars();
		initControls();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuItem item = menu.add(0, 1, 0, "完成");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		InputMethodManager m = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		m.hideSoftInputFromWindow(mListFragment.get(0).getEditTextView().getApplicationWindowToken(), 0);
		m.hideSoftInputFromWindow(mListFragment.get(1).getEditTextView().getApplicationWindowToken(), 0);
		m.hideSoftInputFromWindow(mListFragment.get(2).getEditTextView().getApplicationWindowToken(), 0);
		m.hideSoftInputFromWindow(mListFragment.get(3).getEditTextView().getApplicationWindowToken(), 0);
		if(item.getItemId() == android.R.id.home){ // 返回
			// 判断数据是否合法
			boolean intTag1 = mListFragment.get(0).infoEditCheck(false);
			boolean intTag2 = mListFragment.get(1).infoEditCheck(false);
			boolean intTag3 = mListFragment.get(2).infoEditCheck(false);
			boolean intTag4 = mListFragment.get(3).infoEditCheck(false);
			if (intTag1 || intTag2 || intTag3 || intTag4) {
				final MessageDialog messageDialog = new MessageDialog(RelatedResourceActivity.this);
				messageDialog.setContent("是否放弃保存编辑内容？");
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					@Override
					public void onFinish(String content) {
						if ("确定".equals(content)) {
							finish();
						}}

					@Override
					public void onCancel(String content) {
						messageDialog.cancel();
					}
				});
			}else{
				finish();
			}
			
		}
		else if(item.getItemId() == 1){ // 完成
			String id=String.valueOf(communityId);
			if(!id.contains("-1")){
				doModify();
			}else{
				dofinish();
			}
		}
		return true;
	}
	private void doModify() {
		// TODO Auto-generated method stub
		showLoadingDialog();
		//人脉
		List pList=new ArrayList<ASSOData>();
		mSelectPeopleNode= (ConnectionNode) mListFragment.get(0).getResourceNode();
		if(mSelectPeopleNode!=null)
		pList.add( mSelectPeopleNode.toASSOData());
		//组织
		List oList=new ArrayList<ASSOData>();
		mSelectOrganizationNode=(ConnectionNode) mListFragment.get(1).getResourceNode();
		if(mSelectOrganizationNode!=null)
		oList.add( mSelectOrganizationNode.toASSOData());
		//知识
		List kList=new ArrayList<ASSOData>();
		mSelectKnowledgeNode= (KnowledgeNode) mListFragment.get(2).getResourceNode();
		if(mSelectKnowledgeNode!=null)
		kList.add( mSelectKnowledgeNode.toASSOData());
		//事件
		List rList=new ArrayList<ASSOData>();
		mSelectAffairNode=  (AffairNode) mListFragment.get(3).getResourceNode();
		if(mSelectAffairNode!=null)
		rList.add( mSelectAffairNode.toASSOData());
		
		CommunityReqUtil.doModifyAsso(this, this, String.valueOf(communityId), String.valueOf(createrId), rList, pList,oList, kList, null);
	}

	private void dofinish(){
		// 判断数据是否合法
		boolean intTag1 = mListFragment.get(0).infoIntegrityCheck(false);
		boolean intTag2 = mListFragment.get(1).infoIntegrityCheck(false);
		boolean intTag3 = mListFragment.get(2).infoIntegrityCheck(false);
		boolean intTag4 = mListFragment.get(3).infoIntegrityCheck(false);
		
		if (!intTag1 && !intTag2 && !intTag3 && !intTag4) {
			showToast("请输入关系");
		}
		else{
			Intent intent = new Intent();
			if(intTag1){ // 人脉资源信息
				intent.putExtra(EConsts.Key.RELATED_PEOPLE_NODE, mListFragment.get(0).getResourceNode()); 
			}
			if(intTag2){ // 组织资源信息	
				intent.putExtra(EConsts.Key.RELATED_ORGANIZATION_NODE, mListFragment.get(1).getResourceNode());
			}
			if(intTag3){ // 知识资源信息
				intent.putExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE, mListFragment.get(2).getResourceNode()); 
			}
			if(intTag4){ // 事件关联信息
				intent.putExtra(EConsts.Key.RELATED_AFFAIR_NODE, mListFragment.get(3).getResourceNode()); 
			}
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}
	private void initVars(){
		// 页面列表
		mListFragment = new ArrayList<RelatedResourceFragment>();
		// 页面传来的参数
		communityId = getIntent().getLongExtra(GlobalVariable.COMMUNITY_ID, -1);
		createrId = getIntent().getLongExtra("createrId", -1);
		mResType = (ResourceType) getIntent().getSerializableExtra(EConsts.Key.RELATED_RESOURCE_TYPE); // 资源类型
//		mKeyword = getIntent().getStringExtra(EConsts.Key.KNOWLEDGE_KEYWORD); // 知识关键字
		removeId = getIntent().getStringExtra(EConsts.Key.RELATE_REMOVE_ID);
		switch(mResType){
		case People:
			mSelectPeopleNode = (ConnectionNode) getIntent().getSerializableExtra(EConsts.Key.RELATED_RESOURCE_NODE);
			break;
		case Organization:
			mSelectOrganizationNode = (ConnectionNode) getIntent().getSerializableExtra(EConsts.Key.RELATED_RESOURCE_NODE);
			break;
		case Knowledge:
			mSelectKnowledgeNode = (KnowledgeNode) getIntent().getSerializableExtra(EConsts.Key.RELATED_RESOURCE_NODE);
			break;
		case Affair:
			mSelectAffairNode = (AffairNode) getIntent().getSerializableExtra(EConsts.Key.RELATED_RESOURCE_NODE);
			break;
		default:
			break;
		}
		if(getIntent().hasExtra(EConsts.Key.RELATED_RESOURCE_ALL_NODE)){
			connectionAllNode=(Map) getIntent().getSerializableExtra(EConsts.Key.RELATED_RESOURCE_ALL_NODE);// 资源所有数据
			mSelectPeopleNode=(ConnectionNode) connectionAllNode.get("p");
			mSelectOrganizationNode=(ConnectionNode) connectionAllNode.get("o");
			mSelectKnowledgeNode=(KnowledgeNode) connectionAllNode.get("k");
			mSelectAffairNode=(AffairNode) connectionAllNode.get("r");
		}
		
		// 人脉
		RelatedResourceFragment peoFrg = new RelatedResourceFragment();
		Bundle peoBdl = new Bundle();
//		peoBdl.putString(EConsts.Key.KNOWLEDGE_KEYWORD, mKeyword);
		peoBdl.putSerializable(EConsts.Key.RELATED_RESOURCE_TYPE, ResourceType.People);
		peoBdl.putSerializable(EConsts.Key.RELATED_RESOURCE_NODE, mSelectPeopleNode);
		peoFrg.setArguments(peoBdl);
		peoFrg.setRemoveId(removeId);
		mListFragment.add(peoFrg);
		// 组织
		RelatedResourceFragment orgFrg = new RelatedResourceFragment();
		Bundle orgBdl = new Bundle();
//		orgBdl.putString(EConsts.Key.KNOWLEDGE_KEYWORD, mKeyword);
		orgBdl.putSerializable(EConsts.Key.RELATED_RESOURCE_TYPE, ResourceType.Organization);
		orgBdl.putSerializable(EConsts.Key.RELATED_RESOURCE_NODE, mSelectOrganizationNode);
		orgFrg.setArguments(orgBdl);
		mListFragment.add(orgFrg);
		// 知识
		RelatedResourceFragment knoFrg = new RelatedResourceFragment();
		Bundle knoBdl = new Bundle();
//		knoBdl.putString(EConsts.Key.KNOWLEDGE_KEYWORD, mKeyword);
		knoBdl.putSerializable(EConsts.Key.RELATED_RESOURCE_TYPE, ResourceType.Knowledge);
		knoBdl.putSerializable(EConsts.Key.RELATED_RESOURCE_NODE, mSelectKnowledgeNode);
		knoFrg.setArguments(knoBdl);
		mListFragment.add(knoFrg);
		// 事件
		RelatedResourceFragment affFrg = new RelatedResourceFragment();
		Bundle affBdl = new Bundle();
//		affBdl.putString(EConsts.Key.KNOWLEDGE_KEYWORD, mKeyword);
		affBdl.putSerializable(EConsts.Key.RELATED_RESOURCE_TYPE, ResourceType.Affair);
		affBdl.putSerializable(EConsts.Key.RELATED_RESOURCE_NODE, mSelectAffairNode);
		affFrg.setArguments(affBdl);
		mListFragment.add(affFrg);
		// 适配器
		mAdapter = new ResourceAdapter(getSupportFragmentManager());
	}

	private void initControls(){
		resourceVp = (ViewPager) findViewById(R.id.resourceVp);
		resourceVp.setAdapter(mAdapter);
		resourceVp.setOffscreenPageLimit(3); // 缓存页面数
		resourceVp.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageSelected(int arg0) {
				
				switch(arg0){
				case 0: // 人
					tabRgp.check(R.id.peoRbtn);
					break;
				case 1: // 组织
					tabRgp.check(R.id.orgRbtn);
					break;
				case 2: // 知识
					tabRgp.check(R.id.knoRbtn);
					break;
				case 3: // 事件
					tabRgp.check(R.id.affRbtn);
					break;
				}
			}
		});
		// Tab标题
		tabRgp = (RadioGroup) findViewById(R.id.tabRgp);
		tabRgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.peoRbtn:
					resourceVp.setCurrentItem(0);
					break;
				case R.id.orgRbtn:
					resourceVp.setCurrentItem(1);
					break;
				case R.id.knoRbtn:
					resourceVp.setCurrentItem(2);
					break;
				case R.id.affRbtn:
					resourceVp.setCurrentItem(3);
					break;
				}
			}
		});
		// 设置当前选中项
		switch (mResType) {
		case People:
			resourceVp.setCurrentItem(0);
			break;
		case Organization:
			resourceVp.setCurrentItem(0);
			break;
		case Knowledge:
			resourceVp.setCurrentItem(0);
			break;
		case Affair:
			resourceVp.setCurrentItem(0);
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
		case EConsts.ReqCode.CreateConnectionsForResult: // 创建人脉
			mListFragment.get(0).onActivityResult(requestCode, resultCode, data);
			break;
		case EConsts.ReqCode.CreateKnowledgeForResult: // 创建知识
			mListFragment.get(2).onActivityResult(requestCode, resultCode, data);
			break;
		case EConsts.ReqCode.CreateRequirementForResult: // 创建需求
			mListFragment.get(3).onActivityResult(requestCode, resultCode, data);
			break;
		}
	}
	
	class ResourceAdapter extends FragmentPagerAdapter{

		public ResourceAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return mListFragment.size();
			
		}

		/*
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		*/

		@Override
		public Fragment getItem(int arg0) {
			return mListFragment.get(arg0);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object){
			super.destroyItem(container, position, object);
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
		switch (tag) {
		case EAPIConsts.CommunityReqType.TYPE_MODIFY_ASSO:
			if (null != dataMap) {
				String notifCode = (String) dataMap.get("notifCode");
				if (notifCode.contains("1"))
					dofinish();
			}
			break;

		default:
			break;
		}
		dismissLoadingDialog();
	}
}
