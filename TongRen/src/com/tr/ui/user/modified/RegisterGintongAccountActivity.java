package com.tr.ui.user.modified;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.model.home.MListCountry;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class RegisterGintongAccountActivity extends JBaseFragmentActivity {

	private RegisterPhoneFragment fragment;
	private MListCountry mListCountry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_gintong);
		fragment = new RegisterPhoneFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), " 加入金桐", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	public void showLoadingDialog() {
		showLoadingDialog("");
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode == 2056 && resultCode == RESULT_OK) {
			fragment.onActivityResult(requestCode, resultCode, intent);
		}
	}

	public MListCountry getmListCountry() {
		return mListCountry;
	}

	public void setmListCountry(MListCountry mListCountry) {
		this.mListCountry = mListCountry;
	}
}
