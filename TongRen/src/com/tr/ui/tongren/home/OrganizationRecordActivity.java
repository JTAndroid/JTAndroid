package com.tr.ui.tongren.home;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.home.fragment.OrganizationRecordFragment;
import com.tr.ui.tongren.home.fragment.OrganizationRecordHistoryFragment;


public class OrganizationRecordActivity extends JBaseFragmentActivity{

	private String oid;
	private OrganizationRecordFragment recordFrg;
	private OrganizationRecordHistoryFragment historyFrg;
	private FragmentManager fragmentManager;
	private Menu mMenu;
	private boolean isRecord = true;
	
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "我的考勤", false, null, true, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_flow);
		oid = getIntent().getStringExtra("oid");
		Bundle bundle = new Bundle();
		bundle.putString("oid", oid);
		
		fragmentManager = getSupportFragmentManager();
		historyFrg = new OrganizationRecordHistoryFragment();
		recordFrg = new OrganizationRecordFragment();
		historyFrg.setArguments(bundle);
		recordFrg.setArguments(bundle);
		
		showFragments(fragmentManager, recordFrg, "OrganizationRecordFragment");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.mMenu = menu;
		getMenuInflater().inflate(R.menu.activity_more, mMenu);
		mMenu.findItem(R.id.more).setIcon(R.drawable.affairs_calendar_top);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()== R.id.more) {
			if(isRecord){
				isRecord = !isRecord;
				showFragments(fragmentManager, historyFrg, "OrganizationRecordHistoryFragment");
			}else{
				isRecord = !isRecord;
				showFragments(fragmentManager, recordFrg, "OrganizationRecordFragment");
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void showFragments(FragmentManager fragmentManager, Fragment fragment, String tag) {
    	FragmentTransaction transaction = fragmentManager.beginTransaction();
    	if(fragment.isAdded()){
    		if(tag.equals("OrganizationRecordFragment")){
    			transaction.hide(historyFrg);
    		}else{
    			transaction.hide(recordFrg);
    		}
    		fragment.onResume();
    		transaction.show(fragment);
    	}else{
            transaction.add(R.id.fragment_conainer, fragment, tag);
    	}
    	transaction.commitAllowingStateLoss();
    }
	
}
