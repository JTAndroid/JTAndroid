package com.tr.ui.user.modified;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

/**
 * 第三方登陆后完善用户资料或进行原有账号绑定
 * 
 * @author Administrator
 * 
 */
public class CompleteUserInfoActivity extends JBaseFragmentActivity {

	private TextView completeDataTv;
	private TextView bindAccountTv;
	private View complete_dataV;
	private View bind_accountV;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "进入金桐", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comlete_user_info);
		initViews();

	}

	private void initViews() {
		complete_dataV = findViewById(R.id.complete_dataV);
		completeDataTv = (TextView) findViewById(R.id.complete_dataTv);
		completeDataTv.setOnClickListener(mClickListener);

		bind_accountV = findViewById(R.id.bind_accountV);
		bindAccountTv = (TextView) findViewById(R.id.bind_accountTv);
		bindAccountTv.setOnClickListener(mClickListener);
		CompleteUserInfoByPhoneFragment byPhoneFrag = new CompleteUserInfoByPhoneFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_complete_user_info, byPhoneFrag)
				.commitAllowingStateLoss();
	}

	// 点击事件监听器
	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 完善资料
			case R.id.complete_dataTv:
				updateBg(0);
				CompleteUserInfoByPhoneFragment byPhoneFrag = new CompleteUserInfoByPhoneFragment();
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_complete_user_info, byPhoneFrag)
						.commitAllowingStateLoss();
				break;
			// 绑定账户
			case R.id.bind_accountTv:
				updateBg(1);
				CompleteUserInfoByBindAccountFragment byBindAccountFrag = new CompleteUserInfoByBindAccountFragment();
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fragment_complete_user_info,
								byBindAccountFrag).commitAllowingStateLoss();
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 更新字体颜色及下标颜色
	 * 
	 * @param arg
	 *            0表示完善资料 1表示绑定已有账号
	 */
	private void updateBg(int arg) {
		if (arg == 0) {
			// 选中的是完善资料
			// 完善资料字体颜色及字体下标变orange，
			complete_dataV.setBackgroundColor(getResources().getColor(
					R.color.find_project_txt_orange));
			completeDataTv.setTextColor(getResources().getColor(
					R.color.find_project_txt_orange));
			// 绑定账号字体颜色及下标颜色变black
			bind_accountV.setBackgroundColor(Color.TRANSPARENT);
			bindAccountTv.setTextColor(getResources().getColor(
					R.color.find_project_txt_black));
		} else if (arg == 1) {
			// 选中的是绑定账号
			complete_dataV.setBackgroundColor(Color.TRANSPARENT);
			completeDataTv.setTextColor(getResources().getColor(
					R.color.find_project_txt_black));

			bind_accountV.setBackgroundColor(getResources().getColor(
					R.color.find_project_txt_orange));
			bindAccountTv.setTextColor(getResources().getColor(
					R.color.find_project_txt_orange));
		}
	}
}
