package com.tr.ui.communities.home;


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
import com.tr.ui.communities.frag.FragCommunityMessage;
import com.tr.ui.communities.frag.FragCommunitySystemMessage;
import com.tr.ui.home.utils.HomeCommonUtils;

public class CommumitiesNotificationActivity extends JBaseFragmentActivity{

	private RadioGroup tagRg;
	private RadioButton communityRb;
	private FragmentManager fragmentManager;
	private FragCommunityMessage fragCommunityMessage;
	private FragCommunitySystemMessage fragCommunitySystemMessage;
	
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "社群消息", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commumitiesnotification);
		tagRg = (RadioGroup) findViewById(R.id.tagRg);
		communityRb = (RadioButton) findViewById(R.id.communityRb);
		communityRb.setChecked(true);
		tagRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.communityRb:
					showFragments(fragmentManager, fragCommunityMessage, "FragCommunityMessage");
					break;
				case R.id.systemRb:
					showFragments(fragmentManager, fragCommunitySystemMessage, "FragCommunitySystemMessage");
					break;
				}
			}
		});
		
		
		fragmentManager = getSupportFragmentManager();
		fragCommunityMessage = new FragCommunityMessage();
		fragCommunitySystemMessage = new FragCommunitySystemMessage();
		
		showFragments(fragmentManager, fragCommunityMessage, "FragCommunityMessage");
	}
	
	public void showFragments(FragmentManager fragmentManager, Fragment fragment, String tag) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (fragment.isAdded()) {
			if (tag.equals("FragCommunityMessage")) {
				transaction.hide(fragCommunitySystemMessage);
			} else {
				transaction.hide(fragCommunityMessage);
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
    	if(resultCode == RESULT_OK){
    		showFragments(fragmentManager, fragCommunityMessage, "FragCommunityMessage");
    	}
    }
	
}
