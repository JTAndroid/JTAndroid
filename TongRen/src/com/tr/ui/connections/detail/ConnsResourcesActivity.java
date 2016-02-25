package com.tr.ui.connections.detail;

import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.model.model.PeopleDemandCommon;
import com.tr.ui.base.JBaseFragmentActivity;
import com.viewpagerindicator.TabPageIndicator2;

/**
 * 人脉的资源
 * @author leon
 *
 */
public class ConnsResourcesActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();
	
	// 变量
	private ResAdapter adapter;
	private DemandListFragment expertFrg; // 专家需求
	private RelationshipFragment selfFrg; // 与我的关系
	private RelationshipFragment friendFrg; // 与我的好友的关系
	private RelationshipFragment siteFrg; // 与金桐网的关系
	
	// 控件
	private TabPageIndicator2 tabTpi; // 指示条
	private ViewPager contentVp; // 内容
	
	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("资源");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conns_act_resources);
		initVars();
		initControls();
	}
	
	@SuppressWarnings("unchecked")
	private void initVars(){
		// 专家需求
		expertFrg = new DemandListFragment();
		ArrayList<PeopleDemandCommon> listDemand = (ArrayList<PeopleDemandCommon>) getIntent().getSerializableExtra("listDemand");
		Bundle bundle = new Bundle();
		bundle.putSerializable("listDemand", listDemand);
		expertFrg.setArguments(bundle);
		// 关系
		String id = getIntent().getStringExtra("id");
		boolean isOnline = getIntent().getBooleanExtra("isOnline", false);
		// 与我的关系
		selfFrg = new RelationshipFragment();
		Bundle bundle1 = new Bundle();
		bundle1.putString("id", id);
		bundle1.putBoolean("isOnline", isOnline);
		bundle1.putString("range", "me");
		selfFrg.setArguments(bundle1);
		// 与我好友的关系
		friendFrg = new RelationshipFragment();
		Bundle bundle2 = new Bundle();
		bundle2.putString("id", id);
		bundle2.putBoolean("isOnline", isOnline);
		bundle2.putString("range", "friends");
		friendFrg.setArguments(bundle2);
		// 与金桐网的关系
		siteFrg = new RelationshipFragment();
		Bundle bundle3 = new Bundle();
		bundle3.putString("id", id);
		bundle3.putBoolean("isOnline", isOnline);
		bundle3.putString("range", "site");
		siteFrg.setArguments(bundle3);
		// 适配器
		adapter = new ResAdapter(getSupportFragmentManager());
	}
	
	private void initControls(){
		
		contentVp = (ViewPager) findViewById(R.id.contentVp);
        contentVp.setAdapter(adapter);
        
        tabTpi = (TabPageIndicator2) findViewById(R.id.tabTpi);
        tabTpi.setViewPager(contentVp);
	}
	
	class ResAdapter extends FragmentPagerAdapter {
    	    	
		String[] Tabs = new String[] { "专家", "与我的关系", "与我好友的关系", "与金桐网的关系" };
		
        public ResAdapter(FragmentManager fm) {
            super(fm);
        }
        
        @Override
        public Fragment getItem(int position) {     
        	switch(position){
        	case 0:
        		return expertFrg;
        	case 1:
        		return selfFrg;
        	case 2:
        		return friendFrg;
        	case 3:
        		return siteFrg;
        	default:
        		return expertFrg;
        	}
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	return Tabs[position];
        }

        @Override
        public int getCount() {
        	return 4;
        }
    }
}
