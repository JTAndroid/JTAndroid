package com.tr.ui.communities.home;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.R;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.obj.ConnectionsMini;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.communities.ModulesType;
import com.tr.ui.communities.adapter.CommumitiesRelevanceAdapter;
import com.tr.ui.communities.adapter.MemberAdapter;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EConsts;
import com.utils.common.EUtil;

/**
 * 社群详情关联的知识/公司即组织/人脉/需求
 * 
 */
public class CommunityRelevanceActivity extends JBaseActivity  {

	@ViewInject(R.id.community_people)
	private TextView community_people;// 内容标题
	@ViewInject(R.id.community_people_sum)
	private TextView community_people_sum;// 群人脉数
	@ViewInject(R.id.see_more_people)
	private TextView see_more_people;// 群人脉查看更多
	@ViewInject(R.id.list_community_people)
	private ListView list_community_people;// 群人脉列表


	private ModulesType mModulesType;
	private MemberAdapter memberAdapter;
	private CommumitiesRelevanceAdapter peopleAdapter;// 人脉
	private List<DemandASSOData> conn;
	private int count;// 社群所有成员数量
	String title="";

	/**
	 * 现有成员
	 */
	private List<ConnectionsMini> listConnectionsMini = new ArrayList<ConnectionsMini>();

	@Override
	public void initJabActionBar() {
		getBundle();
		initTitle();
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), title, false, null, true, true);
		setContentView(R.layout.activity_relevance);
		ViewUtils.inject(this);
		initView();
		initData();
	}

	private void initTitle() {
		
		switch (mModulesType) {
		case PeopleModules:
			title = "群人脉";
			break;
		case OrgAndCustomModules:
			title = "群企业";
			break;
		case DemandModules:
			title = "群需求";
			break;
		case KnowledgeModules:
			title = "群知识";
			break;

		default:
			break;
		}
	
	}
	private void initItemClick() {
		switch (mModulesType) {
		case PeopleModules:
			list_community_people.setOnItemClickListener(peopleItemClick);
			break;
		case OrgAndCustomModules:
			list_community_people.setOnItemClickListener(companyItemClick);
			break;
		case DemandModules:
			list_community_people.setOnItemClickListener(demandItemClick);
			break;
		case KnowledgeModules:
			list_community_people.setOnItemClickListener(KnowledgeItemClick);
			break;
			
		default:
			break;
		}
		
	}

	private void getBundle() {
		mModulesType = (ModulesType) getIntent().getSerializableExtra(EConsts.Key.MODULES_TYPE);
		List<DemandASSOData> assoDatas = (List<DemandASSOData>) getIntent().getSerializableExtra("conn");
		conn = null != assoDatas ? assoDatas : new ArrayList<DemandASSOData>();
	}

	private void initData() {
		community_people.setText(title);
		peopleAdapter = new CommumitiesRelevanceAdapter(this, mModulesType);
		list_community_people.setAdapter(peopleAdapter);
		initItemClick();
		peopleAdapter.setData(conn);
	}

	private void initView() {
		community_people_sum.setVisibility(View.GONE);
		see_more_people.setVisibility(View.GONE);
	}
	private OnItemClickListener peopleItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (conn.get(position).type == 3)// 好友
				ENavigate.startRelationHomeActivity(CommunityRelevanceActivity.this, conn.get(position).id, true, ENavConsts.type_details_other);
			else
				ENavigate.startRelationHomeActivity(CommunityRelevanceActivity.this, conn.get(position).id, false, ENavConsts.TYPE_CONNECTIONS_HOME_PAGE);
		}
	};
	private OnItemClickListener companyItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			long orgId = Long.valueOf(conn.get(position).id);
			long creaetById = EUtil.isEmpty(conn.get(position).ownerid) ?  0L :Long.valueOf(conn.get(position).ownerid);
			if (conn.get(position).type==4)// 组织
				ENavigate.startOrgMyHomePageActivity(CommunityRelevanceActivity.this, orgId, creaetById, true, ENavConsts.type_details_org);
			else
				ENavigate.startClientDedailsActivity(CommunityRelevanceActivity.this, orgId);
		}
	};
	private OnItemClickListener KnowledgeItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ENavigate.startKnowledgeOfDetailActivity(CommunityRelevanceActivity.this, Long.parseLong(conn.get(position).id), conn.get(position).type);

		}
	};
	private OnItemClickListener demandItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ENavigate.startNeedDetailsActivity(CommunityRelevanceActivity.this, conn.get(position).id, 1);
		}
	};
}
