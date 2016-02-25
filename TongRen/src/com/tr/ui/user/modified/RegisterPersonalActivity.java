package com.tr.ui.user.modified;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.user.frg.FrgRegisterPersonalOne;
import com.utils.common.EConsts;

public class RegisterPersonalActivity extends JBaseFragmentActivity {

	private FrgRegisterPersonalOne frgOne;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(),
				"填写基本资料", false, null, false, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_flow);

		frgOne = new FrgRegisterPersonalOne();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.fragment_conainer, frgOne, "FrgRegisterSecondOne");
		transaction.commitAllowingStateLoss();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == EConsts.REQ_CODE_TAKE_PICTURE
				|| requestCode == EConsts.REQ_CODE_PICK_PICTURE
				|| requestCode == EConsts.REQ_CODE_PICK_PICTURE
				|| requestCode == EConsts.REQ_CODE_CROP_PICTURE) { // 拍照结果
			frgOne.onActivityResult(requestCode, resultCode, intent);
		}
	}
	
}
