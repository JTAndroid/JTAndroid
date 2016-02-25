package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * 相关当事人-保荐机构
 * 
 * @author Wxh07151732
 * 
 */
public class InstitutionActivity extends BaseActivity implements
		OnClickListener {
	private MyEditTextView institution_name_Etv;
	private MyEditTextView institution_legal_person_Etv;
	private MyEditTextView institution_sponsor_Etv;
	private MyEditTextView institution_company_address_Etv;
	private MyEditTextView institution_phone_Etv;
	private MyEditTextView institution_fax_Etv;
	private MyEditTextView institution_url_Etv;
	private TextView instituti_Tv;
	private String institution_Tv;
	private RelativeLayout quit_instituti_Rl;
	private ArrayList<String> list;
	private MyDeleteView institution_delete_Mdv;
	private MyAddMordView instituti_MAMV;
	private LinearLayout instituti_main_Ll;
	private LinearLayout instituti_Ll;
	private ArrayList<String> list1;
	private ArrayList<MyLineraLayout> mLineralist;
	private ArrayList<MyEditTextView> mEditViewlist;
	private ArrayList<CustomerBranch> sponsorBranchList;
	private Bundle bundle;
	private ArrayList<CustomerBranch> sponsorBranch_List;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_institution);
		institution_Tv = this.getIntent().getStringExtra("Institution");
		list = new ArrayList<String>();
		list1 = new ArrayList<String>();
		mLineralist = new ArrayList<MyLineraLayout>();
		mEditViewlist = new ArrayList<MyEditTextView>();
		sponsorBranchList = new ArrayList<CustomerBranch>();
		bundle = new Bundle();
		init();
		initData();
	}

	private void initData() {
		instituti_Tv.setText(institution_Tv);
		quit_instituti_Rl.setOnClickListener(this);
		institution_company_address_Etv.setOnClickListener(this);
		institution_delete_Mdv.setOnClickListener(this);
		instituti_MAMV.setOnClickListener(this);
	}

	public void finish(View v) {
		if (!TextUtils.isEmpty(institution_name_Etv.getText())) {
			list.add(institution_name_Etv.getTextLabel() + "_"
					+ institution_name_Etv.getText());
		}
		if (!TextUtils.isEmpty(institution_legal_person_Etv.getText())) {
			list.add(institution_legal_person_Etv.getTextLabel() + "_"
					+ institution_legal_person_Etv.getText());
		}
		if (!TextUtils.isEmpty(institution_sponsor_Etv.getText())) {
			list.add(institution_sponsor_Etv.getTextLabel() + "_"
					+ institution_sponsor_Etv.getText());
		}
		if (!TextUtils.isEmpty(institution_company_address_Etv.getText())) {
			list.add(institution_company_address_Etv.getTextLabel() + "_"
					+ institution_company_address_Etv.getText());
		}
		if (!TextUtils.isEmpty(institution_phone_Etv.getText())) {
			list.add(institution_phone_Etv.getTextLabel() + "_"
					+ institution_phone_Etv.getText());
		}
		if (!TextUtils.isEmpty(institution_fax_Etv.getText())) {
			list.add(institution_fax_Etv.getTextLabel() + "_"
					+ institution_fax_Etv.getText());
		}
		if (!TextUtils.isEmpty(institution_url_Etv.getText())) {
			list.add(institution_url_Etv.getTextLabel() + "_"
					+ institution_url_Etv.getText());
		}
		if (!mEditViewlist.isEmpty()) {
			for (int i = 0; i < mEditViewlist.size(); i++) {
				MyEditTextView myEditTextView = mEditViewlist.get(i);
				if (!TextUtils.isEmpty(myEditTextView.getText())) {
					list.add(myEditTextView.getTextLabel() + "_"
							+ myEditTextView.getText());
				}
			}
		}
		Parcel parcel = Parcel.obtain();
		if ("保荐机构".equals(institution_Tv)) {
			CustomerBranch branch = new CustomerBranch(parcel);
			branch.name = institution_name_Etv.getText();
			Relation leader = new Relation();
			leader.relation = institution_legal_person_Etv.getText();
			branch.leader = leader;
			Relation sponsor = new Relation();
			sponsor.relation = institution_sponsor_Etv.getText();
			branch.sponsor = sponsor;
			branch.phone = institution_phone_Etv.getText();
			branch.address = institution_company_address_Etv.getText();
			branch.fax = institution_fax_Etv.getText();
			branch.website = institution_url_Etv.getText();
			sponsorBranchList.add(branch);

			if (!mLineralist.isEmpty()) {
				CustomerBranch branch_more = null;
				for (int i = 0; i < mLineralist.size(); i++) {
					MyEditTextView name_Etv = (MyEditTextView) mLineralist.get(
							i).getChildAt(1);
					MyEditTextView legal_person_Etv = (MyEditTextView) mLineralist
							.get(i).getChildAt(2);
					MyEditTextView sponsor_Etv = (MyEditTextView) mLineralist
							.get(i).getChildAt(3);
					MyEditTextView company_address_Etv = (MyEditTextView) mLineralist
							.get(i).getChildAt(4);
					MyEditTextView fax_Etv = (MyEditTextView) mLineralist
							.get(i).getChildAt(5);
					MyEditTextView phone_Etv = (MyEditTextView) mLineralist
							.get(i).getChildAt(6);
					MyEditTextView url_Etv = (MyEditTextView) mLineralist
							.get(i).getChildAt(7);

					branch_more = new CustomerBranch(parcel);
					branch_more.name = name_Etv.getText();
					Relation leader_more = new Relation();
					leader_more.relation = legal_person_Etv.getText();
					branch_more.leader = leader;
					Relation sponsor_more = new Relation();
					sponsor_more.relation = sponsor_Etv.getText();
					branch_more.sponsor = sponsor;
					branch_more.address = company_address_Etv.getText();
					branch_more.fax = fax_Etv.getText();
					branch_more.phone = phone_Etv.getText();
					branch_more.website = url_Etv.getText();
					
					sponsorBranchList.add(branch_more);

				}
				
			}

			bundle.putParcelableArrayList("sponsorBranchList",
					sponsorBranchList);

		} else if ("律师事务所".equals(institution_Tv)) {
			instituti_main_Ll.removeView(instituti_MAMV);
			CustomerBranch lawFirm = new CustomerBranch(parcel);
			lawFirm.name = institution_name_Etv.getText();
			Relation leader = new Relation();
			leader.relation = institution_legal_person_Etv.getText();
			lawFirm.leader = leader;
			Relation sponsor = new Relation();
			sponsor.relation = institution_sponsor_Etv.getText();
			lawFirm.sponsor = sponsor;
			lawFirm.address = institution_company_address_Etv.getText();
			lawFirm.fax = institution_fax_Etv.getText();
			lawFirm.phone = institution_phone_Etv.getText();
			lawFirm.website = institution_url_Etv.getText();
			bundle.putParcelable("lawFirm", lawFirm);
		}

		if (!list.isEmpty()) {
			Intent intent = new Intent(this, RelevantPartiesActivity.class);
			intent.putStringArrayListExtra("Institution_Activity", list);
			intent.putExtras(bundle);
			setResult(0, intent);
		}
		finish();
	}

	private void init() {
		instituti_MAMV = (MyAddMordView) findViewById(R.id.instituti_MAMV);
		instituti_MAMV.setTextLabel(institution_Tv);
		institution_delete_Mdv = (MyDeleteView) findViewById(R.id.institution_delete_Mdv);
		instituti_Tv = (TextView) findViewById(R.id.instituti_Tv);
		quit_instituti_Rl = (RelativeLayout) findViewById(R.id.quit_instituti_Rl);
		institution_name_Etv = (MyEditTextView) findViewById(R.id.institution_name_Etv);
		institution_legal_person_Etv = (MyEditTextView) findViewById(R.id.institution_legal_person_Etv);
		institution_sponsor_Etv = (MyEditTextView) findViewById(R.id.institution_sponsor_Etv);
		institution_company_address_Etv = (MyEditTextView) findViewById(R.id.institution_company_address_Etv);
		institution_phone_Etv = (MyEditTextView) findViewById(R.id.institution_phone_Etv);
		institution_fax_Etv = (MyEditTextView) findViewById(R.id.institution_fax_Etv);
		institution_url_Etv = (MyEditTextView) findViewById(R.id.institution_url_Etv);
		instituti_main_Ll = (LinearLayout) findViewById(R.id.instituti_main_Ll);
		instituti_Ll = (LinearLayout) findViewById(R.id.instituti_Ll);
		if ("律师事务所".equals(institution_Tv)){
			CustomerBranch lawFirm = this.getIntent().getParcelableExtra("lawFirm");
			if (lawFirm != null) {
				institution_name_Etv.setText(lawFirm.name);
				institution_legal_person_Etv.setText(lawFirm.leader.relation);
				institution_sponsor_Etv.setText(lawFirm.sponsor.relation);
				institution_company_address_Etv.setText(lawFirm.address);
				institution_fax_Etv.setText(lawFirm.fax);
				institution_phone_Etv.setText(lawFirm.phone);
				institution_url_Etv.setText(lawFirm.website);
			}
		}
		if ("保荐机构".equals(institution_Tv)) {
		sponsorBranch_List = this.getIntent().getParcelableArrayListExtra(
				"sponsorBranchList");
		if (sponsorBranch_List != null) {
			for (int i = 0; i < sponsorBranch_List.size(); i++) {
				CustomerBranch sponsorBranch = sponsorBranch_List.get(i);
				if (i == 0) {
					institution_name_Etv
							.setText(sponsorBranch_List.get(0).name);
					institution_legal_person_Etv.setText(sponsorBranch_List
							.get(0).leader.relation);
					institution_sponsor_Etv
							.setText(sponsorBranch_List.get(0).sponsor.relation);
					institution_company_address_Etv.setText(sponsorBranch_List
							.get(0).address);
					institution_fax_Etv.setText(sponsorBranch_List.get(0).fax);
					institution_phone_Etv
							.setText(sponsorBranch_List.get(0).phone);
					institution_url_Etv
							.setText(sponsorBranch_List.get(0).website);
				}

				if (i >= 1) {
					list1.add(institution_name_Etv.getTextLabel());
					list1.add(institution_legal_person_Etv.getTextLabel());
					list1.add(institution_sponsor_Etv.getTextLabel());
					list1.add(institution_company_address_Etv.getTextLabel());
					list1.add(institution_phone_Etv.getTextLabel());
					list1.add(institution_fax_Etv.getTextLabel());
					list1.add(institution_url_Etv.getTextLabel());
					ArrayList<MyEditTextView> continueAdd1 = ContinueAdd1(
							list1, instituti_main_Ll, mEditViewlist,
							mLineralist);
					if (continueAdd1 != null) {
						continueAdd1.get(i * list1.size() - list1.size())
								.setText(sponsorBranch.name);
						continueAdd1.get(i * list1.size() - list1.size() + 1)
								.setText(sponsorBranch.leader.relation);
						continueAdd1.get(i * list1.size() - list1.size() + 2)
								.setText(sponsorBranch.sponsor.relation);
						continueAdd1.get(i * list1.size() - list1.size() + 3)
								.setText(sponsorBranch.address);
						continueAdd1.get(i * list1.size() - list1.size() + 4)
								.setText(sponsorBranch.fax);
						continueAdd1.get(i * list1.size() - list1.size() + 5)
								.setText(sponsorBranch.phone);
						continueAdd1.get(i * list1.size() - list1.size() + 6)
								.setText(sponsorBranch.website);
					}

				}
			}
		}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_instituti_Rl:
			finish();
			break;
		case R.id.institution_company_address_Etv:

			break;
		case R.id.institution_delete_Mdv:
			if (mLineralist.isEmpty()) {
				finish();
			} else {
				instituti_Ll.removeAllViews();
			}
			break;
		case R.id.instituti_MAMV:
			list1.add(institution_name_Etv.getTextLabel());
			list1.add(institution_legal_person_Etv.getTextLabel());
			list1.add(institution_sponsor_Etv.getTextLabel());
			list1.add(institution_company_address_Etv.getTextLabel());
			list1.add(institution_phone_Etv.getTextLabel());
			list1.add(institution_fax_Etv.getTextLabel());
			list1.add(institution_url_Etv.getTextLabel());
			ContinueAdd1(list1, instituti_main_Ll, mEditViewlist, mLineralist);
			break;
		default:
			break;
		}
	}
}
