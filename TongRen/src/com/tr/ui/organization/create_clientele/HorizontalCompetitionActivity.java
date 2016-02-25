package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.ui.adapter.CompetitionAdapter;
import com.tr.ui.organization.model.param.CustomerDataParam;
import com.tr.ui.organization.model.peer.CustomerPeerInfo;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.utils.CompetitionListView;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;

/**
 * 同业竞争
 * 
 * @author Wxh07151732
 * 
 */
public class HorizontalCompetitionActivity extends BaseActivity implements
		OnClickListener, IBindData {

	private ImageView edit_competition_iv;
	private RelativeLayout mNews_zheda;
	private RelativeLayout quit_competition_Rl;
	private ListView lv_competition;
	private CompetitionAdapter mCompetitionAdapter;
	private ArrayList<CustomerPeerInfo> peerList;
	private ArrayList<CustomerPeerInfo> rList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_horizontal_competition);
		edit_competition_iv = (ImageView) findViewById(R.id.edit_competition_iv);
		mNews_zheda = (RelativeLayout) findViewById(R.id.news_zheda);
		quit_competition_Rl = (RelativeLayout) findViewById(R.id.quit_competition_Rl);
		lv_competition = (ListView) findViewById(R.id.lv_competition);

		 CompetitionAdapter mCompetitionAdapter = new CompetitionAdapter();
		 lv_competition.setAdapter(mCompetitionAdapter);
		Intent intent = this.getIntent();
		intent.getStringExtra("customerId");

		CustomerDataParam obj = new CustomerDataParam();
		// 请求数据
		OrganizationReqUtil.doRequestWebAPI(context, this, obj.id, null,
				OrganizationReqType.ORGANIZATION_REQ_FINDPER);

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void bindData(int tag, Object object) {

		switch (tag) {
		case OrganizationReqType.ORGANIZATION_REQ_FINDPER:

			if (object == null) {

				return;

			}

			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) object;

			peerList = (ArrayList<CustomerPeerInfo>) map.get("peerList");
			rList = (ArrayList<CustomerPeerInfo>) map.get("list");

			if (peerList != null) {
				CompetitionListView.getdata(context, lv_competition, peerList);
			}

//			if (rList != null) {
//				CompetitionListView.getdata(context, lv_competition, rList);
//			}

			break;

		default:
			break;
		}

	}
}
