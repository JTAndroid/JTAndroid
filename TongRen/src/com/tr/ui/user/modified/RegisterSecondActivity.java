package com.tr.ui.user.modified;

import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.tr.R;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.user.frg.FrgRegisterSecondOne;

public class RegisterSecondActivity extends FragmentActivity {

	private FrgRegisterSecondOne frgOne;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_flow);

		initJActionBarImpl();
		
		String mobile = getIntent().getStringExtra("mobile");
		String mobileAreaCode = getIntent().getStringExtra("mobileAreaCode");
		frgOne = new FrgRegisterSecondOne();
		Bundle bundle = new Bundle();
		bundle.putString("mobile", mobile);
		bundle.putString("mobileAreaCode", mobileAreaCode);
		frgOne.setArguments(bundle);
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.fragment_conainer, frgOne, "FrgRegisterSecondOne");
		transaction.commitAllowingStateLoss();
	}
	
	private void initJActionBarImpl() {
		ActionBar actionbar = this.getActionBar();
		if (actionbar == null) {
			return;
		}
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setHomeButtonEnabled(false);
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setIcon(R.color.none);
		// 设置actionbar的背景图
		Drawable myDrawable = getResources().getDrawable(
				R.drawable.action_bar_bg);
		actionbar.setBackgroundDrawable(myDrawable); // 设置背景图片
		myDrawable.clearColorFilter();
		actionbar.setSplitBackgroundDrawable(getResources().getDrawable(
				R.drawable.action_bar_bg));
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "注册",
				false, null, false, true);
	}
}
