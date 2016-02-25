package com.tr.ui.tongren.home;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.home.fragment.OrganizationApplyFragment;
import com.tr.ui.tongren.home.fragment.OrganizationReviewFragment;

public class OrganizationReviewActivity extends JBaseFragmentActivity {

	private RadioGroup tagRg;
	private RadioButton myReviewRb;
	private OrganizationReviewFragment reviewFrg;
	private OrganizationApplyFragment applyFrg;
	private FragmentManager fragmentManager;
	private String oid;
	
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "审批",
				false, null, true, true);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tongren_org_review_activity);
		
		tagRg = (RadioGroup) findViewById(R.id.tagRg);
		myReviewRb = (RadioButton) findViewById(R.id.myReviewRb);
		myReviewRb.setChecked(true);
		tagRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.myReviewRb:
					showFragments(fragmentManager, reviewFrg, "OrganizationReviewFragment");
					break;
				case R.id.myApplyRb:
					showFragments(fragmentManager, applyFrg, "OrganizationApplyFragment");
					break;
				}
			}
		});
		
		oid = getIntent().getStringExtra("oid");
		Bundle bundle = new Bundle();
		bundle.putString("oid", oid);
		
		fragmentManager = getSupportFragmentManager();
		reviewFrg = new OrganizationReviewFragment();
		applyFrg = new OrganizationApplyFragment();
		reviewFrg.setArguments(bundle);
		applyFrg.setArguments(bundle);
		
		showFragments(fragmentManager, reviewFrg, "OrganizationReviewFragment");
	}
	
    public void showFragments(FragmentManager fragmentManager, Fragment fragment, String tag) {
    	FragmentTransaction transaction = fragmentManager.beginTransaction();
    	if(fragment.isAdded()){
    		if(tag.equals("OrganizationReviewFragment")){
    			transaction.hide(applyFrg);
    		}else{
    			transaction.hide(reviewFrg);
    		}
    		fragment.onResume();
    		transaction.show(fragment);
    	}else{
            transaction.add(R.id.fragment_container, fragment, tag);
    	}
    	transaction.commitAllowingStateLoss();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
    		Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	if(intent!=null){
    		showFragments(fragmentManager, applyFrg, "OrganizationApplyFragment");
    	}
    }

}
