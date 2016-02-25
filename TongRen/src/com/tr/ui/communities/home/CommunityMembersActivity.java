package com.tr.ui.communities.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.model.im.FetchFriends;
import com.tr.model.im.Friend;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.communities.adapter.MemberAdapter;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.IMEditMumberGrid.HeadName;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts.CommunityReqType;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 社群群成员页面
 * 
 */
public class CommunityMembersActivity extends JBaseActivity implements IBindData {

	@ViewInject(R.id.community_member_sum)
	private TextView community_member_sum;// 群成员数量
	@ViewInject(R.id.see_more_member)
	private TextView see_more_member;// 群成员查看更多
	@ViewInject(R.id.grid_community_member)
	private GridView grid_community_member;// 群成员列表

	private MemberAdapter memberAdapter;

	private long communityId;// 社群id

	/**
	 * 成员详情实体
	 */
	private MUCDetail mucDetail;
	private FetchFriends ffs;
	private ArrayList<String> friendIds = new ArrayList<String>();
	private String createdUserId;
	private int count;// 社群所有成员数量

	/**
	 * 现有成员
	 */
	private List<ConnectionsMini> listConnectionsMini = new ArrayList<ConnectionsMini>();

	@Override
	public void initJabActionBar() {
		getBundle();
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "群成员", false, null, true, true);
		setContentView(R.layout.layout_item_momunity_member);
		ViewUtils.inject(this);
		getCommunityMemberList();
		initView();
		initData();
	}

	/**
	 * 获取成员详情
	 */
	private void getCommunityMemberList() {
		showLoadingDialog();
		CommunityReqUtil.doGetCommunityMemberList(this, communityId, this, null);
	}

	private void getBundle() {
		communityId = getIntent().getLongExtra(GlobalVariable.COMMUNITY_ID, -1);
	}

	private void initData() {
		memberAdapter = new MemberAdapter(this);
		grid_community_member.setAdapter(memberAdapter);
		
		grid_community_member.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HeadName headName = (HeadName) memberAdapter.getItem(position);
				ENavigate.startRelationHomeActivity(CommunityMembersActivity.this, headName.getUserID(),true,1);
			}
		});
	}

	private void initView() {
		see_more_member.setVisibility(View.GONE);

	}

	public JSONArray getFriendIds(MUCDetail mucdetail) {
		JSONArray friendIds = new JSONArray();
		for (ConnectionsMini conn : mucdetail.getListConnectionsMini2()) {
			friendIds.put(Long.valueOf(conn.getId()));
		}
		return friendIds;
	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case CommunityReqType.TYPE_GET_COMMUNITY_MEMBER_LIST:// 详情里的成员详情
			if (null != object) {
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				mucDetail = (MUCDetail) dataMap.get("mucDetail");
				CommunityReqUtil.fetchFirends(this, this, mucDetail.getId(), Long.valueOf(App.getUserID()), getFriendIds(mucDetail), null);
			}
			break;
		case EAPIConsts.IMReqType.IM_REQ_FETCHFIRENDS:
			ffs = (FetchFriends) object;
			if (ffs != null) {
				createdUserId = String.valueOf(ffs.getCreatedUserId());
				for (Friend friend : ffs.getFirends()) {
					friendIds.add(String.valueOf(friend.getFirendId()));
				}
				upDateUi();
				dismissLoadingDialog();
			}
			break;
		default:
			break;
		}
	}

	private void upDateUi() {
		if (null != mucDetail) {
			listConnectionsMini = mucDetail.getListConnectionsMini();
		}
		if (null != listConnectionsMini) {
			int size = listConnectionsMini.size();
			community_member_sum.setText("(" + size + ")");
			ArrayList<HeadName> data = new ArrayList<HeadName>();
			for (int i = 0; i < size; i++) {
				ConnectionsMini mini = listConnectionsMini.get(i);
				HeadName headname = new HeadName(mini);
				if (friendIds.contains(mini.getId())) {
					headname.setIsFriend(true);
				}
				if (mini.getId().equals(createdUserId)) {
					headname.setCreater(true);
					data.add(0, headname);
				} else {
					data.add(headname);
				}
			}
			memberAdapter.setDataNoAdd(data);
		} else
			community_member_sum.setText("(0)");
	}
}
