package com.tr.ui.connections.detail;

import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.tr.R;
import com.tr.model.model.PeopleDemandCommon;
import com.tr.ui.base.JBaseFragmentActivity;
import com.viewpagerindicator.TabPageIndicator2;

/**
 * 人脉或用户需求
 * @author leon
 *
 */
public class ConnsRequirementActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private TabPageIndicator2 tabTpi;
	private ViewPager contentVp;
	// 变量
	private ArrayList<PeopleDemandCommon> listDemand; // 投融资意向
	private DemandListFragment demandFrg; 
	private RequirementListFragment requirementFrg;
	private boolean isOnline; // 人脉or用户
	private String id;
	
	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("需求");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conns_detail_act_req);
		initVars();
		initControls();
	}
	
	@SuppressWarnings("unchecked")
	private void initVars(){
		
		isOnline = getIntent().getBooleanExtra("isOnline", false);
		id = getIntent().getStringExtra("id");
		listDemand = (ArrayList<PeopleDemandCommon>) getIntent().getSerializableExtra("listDemand");
		if(listDemand == null){
			listDemand = new ArrayList<PeopleDemandCommon>();
		}
		demandFrg = new DemandListFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("listDemand", listDemand);
		demandFrg.setArguments(bundle);
		
		if(isOnline){ // 用户
			requirementFrg = new RequirementListFragment();
			Bundle bundle1 = new Bundle();
			bundle1.putString("id", id);
			requirementFrg.setArguments(bundle1);
		}
	}
	
	private void initControls(){
		
		contentVp = (ViewPager) findViewById(R.id.contentVp);
		contentVp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		
		if(isOnline){ // 用户
			tabTpi = (TabPageIndicator2) findViewById(R.id.tabTpi);
			tabTpi.setViewPager(contentVp);
			tabTpi.setVisibility(View.VISIBLE);
		}
	}
	
	class MyPagerAdapter extends FragmentPagerAdapter{

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if(position == 0){
				return demandFrg;
			}
			else{
				return requirementFrg;
			}
		}

		@Override
		public int getCount() {
			return (int) Math.pow(2, isOnline ? 1 : 0);
		}
		
		@Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
            	return "投融资意向";
            }
            else{
            	return "我的需求";
            }
        }
	}
}
