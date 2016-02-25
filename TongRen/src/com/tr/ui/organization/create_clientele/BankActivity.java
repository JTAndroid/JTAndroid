package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerBranch;
import com.tr.ui.organization.model.profile.CustomerProfile;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.view.MyAddMordView;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;

/**
 * 相关当事人-主办银行
 * 
 * @author Wxh07151732
 *
 */
public class BankActivity extends BaseActivity implements OnClickListener {
	private MyEditTextView bank_name_Etv;
	private MyEditTextView bank_linkman_Etv;
	private MyEditTextView bank_address_Etv;
	private MyEditTextView bank_phone_Etv;
	private RelativeLayout quit_bank_Rl;
	private ArrayList<String> list;
	private MyAddMordView bank_MAMV;
	private MyDeleteView bank_delete_Mdv;
	private ArrayList<String> list1;
	private LinearLayout bank_main_Ll;
	private LinearLayout bank_Ll;
	private ArrayList<MyEditTextView> mEditViewlist;
	private ArrayList<MyLineraLayout> mLineralist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_bank);
		list = new ArrayList<String>();
		list1 = new  ArrayList<String>();
		mEditViewlist = new ArrayList<MyEditTextView>();
		mLineralist = new ArrayList<MyLineraLayout>();
		init();
		initData();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_bank_Rl:
			finish();
			break;
		case R.id.bank_MAMV:
			list1.add(bank_name_Etv.getTextLabel());
			list1.add(bank_linkman_Etv.getTextLabel());
			list1.add(bank_address_Etv.getTextLabel());
			list1.add(bank_phone_Etv.getTextLabel());
			ContinueAdd1(list1,bank_main_Ll,mEditViewlist,mLineralist);
			
			break;
		case R.id.bank_delete_Mdv:
			if (mLineralist.isEmpty()) {
				finish();
			}else{
				bank_Ll.removeAllViews();
			}
			break;
		default:
			break;
		}
		
	}
	private void initData() {
		quit_bank_Rl.setOnClickListener(this);
		bank_MAMV.setOnClickListener(this);
		bank_delete_Mdv.setOnClickListener(this);
		
	}
	public void finish(View v){
		if (!TextUtils.isEmpty(bank_name_Etv.getText())) {
			list.add(bank_name_Etv.getTextLabel()+"_"+bank_name_Etv.getText());
		}
		if (!TextUtils.isEmpty(bank_linkman_Etv.getText())) {
			list.add(bank_linkman_Etv.getTextLabel()+"_"+bank_linkman_Etv.getText());
		}
		if (!TextUtils.isEmpty(bank_address_Etv.getText())) {
			list.add(bank_address_Etv.getTextLabel()+"_"+bank_address_Etv.getText());
		}
		if (!TextUtils.isEmpty(bank_phone_Etv.getText())) {
			list.add(bank_phone_Etv.getTextLabel()+"_"+bank_phone_Etv.getText());
		}
		Parcel parcel = Parcel.obtain();
		CustomerBranch sponsorBank = new CustomerBranch(parcel);
		sponsorBank.name = bank_name_Etv.getText();
		sponsorBank.address = bank_address_Etv.getText();
		sponsorBank.phone = bank_phone_Etv.getText();
		Relation leader = new Relation();
		leader.relation = bank_linkman_Etv.getText();
		sponsorBank.leader = leader ;
		Bundle bundle = new Bundle();
		bundle.putParcelable("sponsorBank", sponsorBank);
		if (!list.isEmpty()) {
			Intent intent = new Intent(this, RelevantPartiesActivity.class);
			intent.putStringArrayListExtra("Bank_Activity", list);
			intent.putExtras(bundle);
			setResult(1, intent);
		}
		finish();
	}
	private void init() {
		bank_MAMV = (MyAddMordView) findViewById(R.id.bank_MAMV);
		bank_delete_Mdv = (MyDeleteView) findViewById(R.id.bank_delete_Mdv);
		quit_bank_Rl = (RelativeLayout) findViewById(R.id.quit_bank_Rl);
		bank_name_Etv = (MyEditTextView) findViewById(R.id.bank_name_Etv);
		bank_linkman_Etv = (MyEditTextView) findViewById(R.id.bank_linkman_Etv);
		bank_address_Etv = (MyEditTextView) findViewById(R.id.bank_address_Etv);
		bank_phone_Etv = (MyEditTextView) findViewById(R.id.bank_phone_Etv);
		bank_main_Ll = (LinearLayout) findViewById(R.id.bank_main_Ll);
		bank_Ll = (LinearLayout) findViewById(R.id.bank_Ll);
		bank_main_Ll.removeView(bank_MAMV);
		
		CustomerBranch sponsorBank = this.getIntent().getParcelableExtra("sponsorBank");
		if (sponsorBank!=null) {
			  bank_name_Etv.setText(sponsorBank.name);
			bank_address_Etv.setText(sponsorBank.address );
			 bank_phone_Etv.setText(sponsorBank.phone);
			  bank_linkman_Etv.setText(sponsorBank.leader.relation);
		}
	}


}
