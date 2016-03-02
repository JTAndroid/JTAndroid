package com.tr.ui.communities.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.MUCMessage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.communities.frag.FragCommunityMessage;
import com.tr.ui.communities.frag.FragCommunitySystemMessage;
import com.tr.ui.communities.im.CommunityChatActivity;
import com.tr.ui.communities.model.CommunityNotify;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.communities.model.Notification;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.title.menu.popupwindow.ViewHolder;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

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
			transaction.show(fragment);
		}else{
            transaction.add(R.id.fragment_container, fragment, tag);
    	}
    	transaction.commitAllowingStateLoss();
	}
	
}
