package com.tr.ui.tongren.home;

import java.util.ArrayList;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.home.fragment.OrganizationApplyNameFragment;
import com.tr.ui.tongren.home.fragment.OrganizationApplyTypeFragment;
import com.tr.ui.tongren.model.review.Process;

public class OrganizationApplyNameAndTypeActivity extends JBaseFragmentActivity {

	private String applyName, reviewProcessId;
	private ArrayList<Process> listProcess;
	private OrganizationApplyNameFragment org_applyName;
	private OrganizationApplyTypeFragment org_applyType;

	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "新建申请",
				false, null, true, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_flow);

		applyName = getIntent().getStringExtra("applyName");
		reviewProcessId = getIntent().getStringExtra("reviewProcessId");
		listProcess = (ArrayList<Process>) getIntent().getSerializableExtra("listProcess");

		if(applyName!=null){
			org_applyName = new OrganizationApplyNameFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("listProcess", listProcess);
			bundle.putString("applyName", applyName);
			org_applyName.setArguments(bundle);
			showFragment(getSupportFragmentManager(), org_applyName,
					"OrganizationApplyNameFragment", true);
			
		}else{
			org_applyType = new OrganizationApplyTypeFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("listProcess", listProcess);
			bundle.putString("reviewProcessId", reviewProcessId);
			org_applyType.setArguments(bundle);
			showFragment(getSupportFragmentManager(), org_applyType,
					"OrganizationApplyTypeFragment", true);
		}
	}

	public static void showFragment(FragmentManager fragmentManager, Fragment fragment, String tag, boolean addToBackStack) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (fragment.isAdded()) {
			transaction.show(fragment);
			fragment.onResume();
		} else {
			if (fragmentManager.getFragments() != null) {
				if (addToBackStack) {
					transaction.addToBackStack(tag);
				}
			}
			transaction.add(R.id.fragment_conainer, fragment, tag);
			transaction.commitAllowingStateLoss();
		}
	}
}
