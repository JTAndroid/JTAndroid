package com.tr.ui.tongren.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.utils.common.EUtil;

public class TongRenFrgPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

	public TongRenFrgPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public TongRenFrgPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragment) {
		super(fm);
		mFragments = fragment;
	}

	public ArrayList<Fragment> getmFragments() {
		return mFragments;
	}

	public void setmFragments(ArrayList<Fragment> mFragments) {
		this.mFragments = mFragments;
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

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	}
}
