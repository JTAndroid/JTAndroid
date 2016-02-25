package com.tr.ui.conference.square;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.model.conference.MMeetingOrgan;
import com.tr.model.conference.MMeetingPeople;
import com.tr.model.conference.MMeetingQuery;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.conference.ListMeetingRelationshipAdapter;
import com.tr.ui.conference.common.BaseActivity;

/**
 * 会议发起人分享的人脉
 * @author sunjianan
 */
public class RelationshipActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.hy_meeting_realation_titlebar)
	private FrameLayout layoutTitle;
	// 标题返回键
	private LinearLayout backBtn;
	// 标题题目
	private TextView titleTv;

	@ViewInject(R.id.hy_meeting_relation_lv)
	private ListView relationLv;

	private MMeetingQuery meeting;

	private ListMeetingRelationshipAdapter adapter;
	/**关系：1，人脉；2，组织*/
	private int relationType;

	// 初始化布局
	@Override
	public void initView() {
		setContentView(R.layout.hy_activity_meeting_relationship);
		ViewUtils.inject(this);

		Intent relationshipIntent = getIntent();
		meeting = (MMeetingQuery) relationshipIntent.getSerializableExtra(ENavConsts.EMeetingDetail);
		relationType = relationshipIntent.getIntExtra("relationType", 1);

		backBtn = (LinearLayout) layoutTitle.findViewById(R.id.hy_layoutTitle_backBtn);
		titleTv = (TextView) layoutTitle.findViewById(R.id.hy_layoutTitle_title);

		adapter = new ListMeetingRelationshipAdapter(this, meeting,relationType);
		// 启动人脉详情页面
		if (meeting != null) {
			relationLv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (relationType == 1) {
						MMeetingPeople mMeetingPeople = meeting.getListMeetingPeople().get(position);
						if (mMeetingPeople.getPeopleType()==2) {
							ENavigate.startRelationHomeActivity(RelationshipActivity.this, mMeetingPeople.getPeopleId()+"");
						}else {
							ENavigate.startContactsDetailsActivity(RelationshipActivity.this, 2, mMeetingPeople.getPeopleId(), 0, 1);
						}
					}else if (relationType == 2) {
						MMeetingOrgan mMeetingOrgan = meeting.getListMeetingOrgan().get(position);
						if (mMeetingOrgan.getOrganType()==2) {
							ENavigate.startClientDedailsActivity(RelationshipActivity.this, Long.valueOf(mMeetingOrgan.getOrganId()));
						}else {
							ENavigate.startOrgMyHomePageActivity(RelationshipActivity.this, Long.valueOf(mMeetingOrgan.getOrganId()), 0, true, 0);
						}
////						ENavigate.startOrgMyHomePageActivity(RelationshipActivity.this, Long.valueOf(meeting.getListMeetingOrgan().get(position).getOrganId()));
//						ENavigate.startOrgMyHomePageActivityByUseId(RelationshipActivity.this, Long.valueOf(.getOrganId()));
					}
				}
			});
		}
		relationLv.setAdapter(adapter);
		setListener();
	}

	private void setListener() {
		backBtn.setOnClickListener(this);
	}

	@Override
	public void initData() {
		if (relationType == 1) {
			titleTv.setText("人脉");
		}else if (relationType == 2) {
			titleTv.setText("组织");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hy_layoutTitle_backBtn:
			finish();
			break;
		}
	}
}
