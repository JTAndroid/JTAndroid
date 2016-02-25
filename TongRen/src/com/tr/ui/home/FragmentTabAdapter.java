package com.tr.ui.home;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FragmentTabAdapter implements OnClickListener {
	private List<Fragment> fragments; // 一个tab页面对应一个Fragment
	// private RadioGroup rgs; // 用于切换tab
	private Button[] btns; // 用于切换tab
	private FragmentActivity fragmentActivity; // Fragment所属的Activity
	private int fragmentContentId; // Activity中所要被替换的区域的id

	private int currentTab; // 当前Tab页面索引

	private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能

	public FragmentTabAdapter(FragmentActivity fragmentActivity,
			List<Fragment> fragments, int fragmentContentId, Button[] btns) {
		this.fragments = fragments;
		this.btns = btns;
		this.fragmentActivity = fragmentActivity;
		this.fragmentContentId = fragmentContentId;

		// 默认显示第一页
		FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
				.beginTransaction();
		ft.add(fragmentContentId, fragments.get(0));
		ft.commit();
		btns[0].setSelected(true);
		for (int i = 0; i < btns.length; i++) {
			btns[i].setOnClickListener(this);
		}

	}

	/**
	 * 切换tab
	 * 
	 * @param idx
	 */
	private void showTab(int idx) {
		for (int i = 0; i < fragments.size(); i++) {
			Fragment fragment = fragments.get(i);
			FragmentTransaction ft = obtainFragmentTransaction(idx);

			if (idx == i) {
				ft.show(fragment);
			} else {
				ft.hide(fragment);
			}
			ft.commit();
		}
		currentTab = idx; // 更新目标tab为当前tab
	}

	/**
	 * 获取一个带动画的FragmentTransaction
	 * 
	 * @param index
	 * @return
	 */
	private FragmentTransaction obtainFragmentTransaction(int index) {
		FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
				.beginTransaction();
		// 设置切换动画
		// if(index > currentTab){
		// ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
		// }else{
		// ft.setCustomAnimations(R.anim.slide_right_in,
		// R.anim.slide_right_out);
		// }
		return ft;
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public Fragment getCurrentFragment() {
		return fragments.get(currentTab);
	}

	public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
		return onRgsExtraCheckedChangedListener;
	}

	public void setOnRgsExtraCheckedChangedListener(
			OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
		this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
	}

	/**
	 * 切换tab额外功能功能接口
	 */
	public interface OnRgsExtraCheckedChangedListener {
		/**
		 * @param btns 按钮
		 * @param checkedId 按钮id
		 * @param index 索引
		 */
		public void OnRgsExtraCheckedChanged(Button[] btns, int checkedId,
				int index);
	}

	@Override
	public void onClick(View v) {
		for (int i = 0; i < btns.length; i++) {
			if (btns[i] == v) {
				btns[currentTab].setSelected(false);
				// 把当前tab设为选中状态
				btns[i].setSelected(true);
				currentTab = i;

				Fragment fragment = fragments.get(i);
				FragmentTransaction ft = obtainFragmentTransaction(i);
				// getCurrentFragment().onPause(); // 暂停当前tab
				// getCurrentFragment().onStop(); // 暂停当前tab

				if (fragment.isAdded()) {
					// fragment.onStart(); // 启动目标tab的onStart()
					fragment.onResume(); // 启动目标tab的onResume()
				} else {
					ft.add(fragmentContentId, fragment);
				}
				showTab(i); // 显示目标tab
				ft.commit();

				// 如果设置了切换tab额外功能功能接口
				if (null != onRgsExtraCheckedChangedListener) {
					onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(
							btns, v.getId(), i);
				}

			}
		}
	}

}