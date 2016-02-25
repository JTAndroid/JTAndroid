package com.tr.ui.relationship;

import java.util.ArrayList;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.tr.App;
import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.mystart.frg.FrgMyRequirement;
import com.utils.common.EUtil;

/**
 * @Filename
 * @Author xuxinjian
 * @Date 2014-3-17
 * @description 我发布的需求
 */
public class MyKnowledgeSelecteActivity extends JBaseFragmentActivity {

	private ViewPager mPager;// 页卡内容
	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	public String name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myrequiremens);
		
		HomeCommonUtils.initLeftCustomActionBar(this, actionBar, name + "选择知识", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
		
		String tempuserID = App.getApp().getAppData().getUser().mID + "";

		MyKnowledgeSelecteFrg myRequirement = new MyKnowledgeSelecteFrg();
		myRequirement.setType(FrgMyRequirement.TYPE_KNOWLEDGE, tempuserID + "");
		mFragments.add(myRequirement);
		mPager = (ViewPager) this.findViewById(R.id.mystartVPager);
		mPager.setAdapter(new HomeFrgPagerAdapter(getSupportFragmentManager(), mFragments));
	}

	ActionBar actionBar;

	@Override
	public void initJabActionBar() {
		actionBar = getActionBar();
	}

	public class HomeFrgPagerAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

		public HomeFrgPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public HomeFrgPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragment) {
			super(fm);
			mFragments = fragment;
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment f = mFragments.get(arg0);
			if (f == null) {
				EUtil.showToast("getItem = null");
			}
			return mFragments.get(arg0);
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

	}

}
