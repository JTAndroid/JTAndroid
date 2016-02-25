package com.tr.ui.demand;

import android.os.Bundle;

import com.tr.R;
import com.tr.model.demand.DemandDetailsData;
import com.tr.navigate.ENavConsts;
import com.tr.ui.demand.fragment.NeedCommentFragment;
import com.tr.ui.demand.util.OnNeedDetails;
import com.tr.ui.demand.util.OnNeedRefresh;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.knowledge.swipeback.SwipeBackActivity;

public class NeedCommentActivity extends SwipeBackActivity implements OnNeedDetails{
	private NeedCommentFragment commentFragment;
	@Override
	public void initJabActionBar() {
		super.initJabActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "评论", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_needcomment);
		initData();
		
	}

	private void initData() {
		String demandId = getIntent().getStringExtra(ENavConsts.DEMAND_DETAILS_ID);
		DemandDetailsData detailsData = (DemandDetailsData) getIntent().getSerializableExtra(ENavConsts.DEMAND_EDIT);
		commentFragment = new NeedCommentFragment(this, demandId, this);
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_needcomment, commentFragment).commit();
	}

	@Override
	public void toNeedDetail(int type, Object obj) {
		
	}

	@Override
	public void getNeedRefresh(OnNeedRefresh refresh, int index) {
		// TODO Auto-generated method stub
		
	}
}
