package com.tr.ui.communities.home;

import java.util.Date;
import java.util.HashMap;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.model.obj.MUCDetail;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.communities.model.Community;
import com.tr.ui.communities.model.CommunityNotify;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class JoinCommumitiesAvtivity extends JBaseActivity implements OnClickListener,IBindData {

	private EditText joincommumitiesEt;
	/**
	 * 社群的社群实体
	 */
	private ImMucinfo community;
	private Community req_number_community;//群号加群
	private long communityId;// 社群id
	private String communityTitle;// 社群title
	private String communityLogo;// 社群logo
	private long createUserId;// 社群id

	@Override
	public void initJabActionBar() {
		getBundle();
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "申请加入群组", false, null, true, true);
	}

	private void getBundle() {
		community = (ImMucinfo) this.getIntent().getSerializableExtra("community");
		req_number_community = (Community) this.getIntent().getSerializableExtra("req_number_community");
		communityId = this.getIntent().getLongExtra(GlobalVariable.COMMUNITY_ID, -1);
		if(community!=null){
			communityTitle = community.getTitle();
			communityId = community.getId();
			createUserId = community.getCreateUserId();
			communityLogo = community.getPicPath();
		}else if(req_number_community != null){
			communityTitle = req_number_community.getTitle();
			communityId = req_number_community.getId();
			createUserId = req_number_community.getCreateUserId();
			communityLogo = req_number_community.getPicPath();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joincommumities);
		initView();
	}

	private void initView() {
		joincommumitiesEt = (EditText) findViewById(R.id.joincommumitiesEt);
		TextView joincommumitiesBt = (TextView) findViewById(R.id.joincommumitiesBt);
		joincommumitiesBt.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.joincommumitiesBt:
			if (!TextUtils.isEmpty(joincommumitiesEt.getText()))
				doJoin();
			else
				showToast("理由不能为空");
			break;

		default:
			break;
		}
	}

	private void doJoin() {
		showLoadingDialog();
		CommunityNotify notify = new CommunityNotify();
		notify.setCommunityId(communityId);
		notify.setCommunityLogo(communityLogo);
		notify.setCommunityName(communityTitle);
		notify.setApplicantId(Long.parseLong(String.valueOf(App.getApp().getUserID())));
		notify.setUserLogo(App.getApp().getUserAvatar());
		notify.setApplicantName(App.getNick());
		notify.setAttendType(1);
		notify.setAcceptStatus(0);
		notify.setApplyReason(joincommumitiesEt.getText().toString().trim());
		notify.setNoticeType(0);
		notify.setCreatedUserId(Long.parseLong(String.valueOf(App.getApp().getUserID())));
		notify.setCreatedUserLogo(App.getApp().getUserAvatar());
		notify.setCreatedUserName(App.getNick());
		notify.setCreatedTime(new Date().getTime());
		notify.setApplicantReadStatus(0);
		notify.setOwnerReadStatus(0);
		CommunityReqUtil.createNotice(JoinCommumitiesAvtivity.this, this, notify, null);
	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case EAPIConsts.CommunityReqType.TYPE_CREATE_COMMUNITY_NOTICE:
			if(null!=object){
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				String notifCode =  (String) dataMap.get("notifCode");
				if(notifCode.contains("1"))
					finish();
				else
					showToast("申请失败，请重新申请");
			}else
				showToast("申请失败，请重新申请");
			break;

		default:
			break;
		}
	}
}
