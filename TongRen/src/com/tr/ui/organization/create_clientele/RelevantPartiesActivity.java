package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.ui.organization.model.profile.CustomerBranch;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.utils.MakeListView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyitemView;
/**
 * 相关当事人
 * @author Wxh07151732
 *
 */
public class RelevantPartiesActivity extends BaseActivity implements OnClickListener {
	private MyitemView myitem_institution;
	private MyitemView myitem_law_office;
	private MyitemView myitem_bank;
	private RelativeLayout quit_relevant_parties_Rl;
	private LinearLayout relevant_Ll;
	private MyEditTextView relevant_custom_Etv;
	private Intent intent;
	private ArrayList<String> institution;
	private String[] Relevant_parties ={"名称","法定代表人","保荐代表人","公司地址","电话","传真","网址"};
	private String[] banks={"名称","联系人","公司地址","电话"};
	private ArrayList<String> bank_list;
	private ArrayList<String> list;
	private CustomerBranch lawFirm;
	private CustomerBranch sponsorBank;
	private ArrayList<CustomerBranch> sponsorBranchList;
	private Bundle bundle;
	private ArrayList<String> institution_Activity;
	private ArrayList<String> bank_Activity;
	private ArrayList<String> law_office;
	private boolean isNull;
	private ArrayList<CustomerPersonalLine> arrayList;
	private ArrayList<MyEditTextView> editTextViews;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_relevant_parties);
		editTextViews = new ArrayList<MyEditTextView>();
		list = new ArrayList<String>();
		bundle = new Bundle();
		institution = new ArrayList<String>();
		bank_list = new ArrayList<String>();
		init();
		initData();
	}
	public void finish(View v){
		if (institution_Activity!=null) {
			intent.putStringArrayListExtra("Relevant_parties_Activity_institution", institution_Activity);
			
		}
		if (bank_Activity!=null) {
			intent.putStringArrayListExtra("Relevant_parties_Activity_bank_list", bank_Activity);
		}
		if (law_office!=null) {
			intent.putStringArrayListExtra("Relevant_parties_Activity_law_office", law_office);
		}
		if (sponsorBranchList!=null) {
			bundle.putParcelableArrayList("sponsorBranchList", sponsorBranchList);
		}
		if (sponsorBank!=null) {
			bundle.putParcelable("sponsorBank", sponsorBank);
		}
		if (lawFirm!=null) {
			bundle.putParcelable("lawFirm", lawFirm);
		}
		intent.putExtras(bundle);
		setResult(7030, intent);
		finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myitem_institution:
			intent = new Intent(this, InstitutionActivity.class);
			intent.putExtra("Institution", "保荐机构");
			bundle.putParcelableArrayList("sponsorBranchList", sponsorBranchList);
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);
			break;
		case R.id.myitem_bank:
			intent = new Intent(this, BankActivity.class);
			bundle.putParcelable("sponsorBank", sponsorBank);
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);
			break;
		case R.id.myitem_law_office:
			intent = new Intent(this, InstitutionActivity.class);
			intent.putExtra("Institution", "律师事务所");
			bundle.putParcelable("lawFirm", lawFirm);
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
			break;
		case R.id.quit_relevant_parties_Rl:
			finish();
			break;
		case R.id.relevant_custom_Etv:
			intent = new Intent(this, CustomActivity.class);
			if (arrayList!=null) {
				if (!isNull) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("Customer_Bean", arrayList);
					intent.putExtras(bundle);
				}
				
			}
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}
	private void initData() {
		for (int i = 0; i < Relevant_parties.length; i++) {
			institution.add(Relevant_parties[i]);
		}
		for (int i = 0; i < banks.length; i++) {
			bank_list.add(banks[i]);
		}
		myitem_institution.setOnClickListener(this);
		myitem_law_office.setOnClickListener(this);
		myitem_bank.setOnClickListener(this);
		quit_relevant_parties_Rl.setOnClickListener(this);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  institution,myitem_institution.getMyitemview_Lv()),displayMetrics);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  institution,myitem_law_office.getMyitemview_Lv()),displayMetrics);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  bank_list,myitem_bank.getMyitemview_Lv()),displayMetrics);
	}

	private void init() {
		myitem_institution = (MyitemView) findViewById(R.id.myitem_institution);
		myitem_law_office = (MyitemView) findViewById(R.id.myitem_law_office);
		myitem_bank = (MyitemView) findViewById(R.id.myitem_bank);
		quit_relevant_parties_Rl = (RelativeLayout) findViewById(R.id.quit_relevant_parties_Rl);
		relevant_Ll = (LinearLayout) findViewById(R.id.relevant_Ll);
		
		relevant_custom_Etv = (MyEditTextView) findViewById(R.id.relevant_custom_Etv);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null) {
			if (arg0==0) {
				switch (arg1) {
				case 0:
					institution_Activity = arg2.getStringArrayListExtra("Institution_Activity");
					AdaptiveListView(MakeListView.ToListviewAdapter(context,  institution_Activity,myitem_institution.getMyitemview_Lv()),displayMetrics);
					lawFirm = arg2.getParcelableExtra("lawFirm");
					sponsorBranchList = arg2.getParcelableArrayListExtra("sponsorBranchList");
					break;
				case 1:
					bank_Activity = arg2.getStringArrayListExtra("Bank_Activity");
					AdaptiveListView(MakeListView.ToListviewAdapter(context,  bank_Activity,myitem_bank.getMyitemview_Lv()),displayMetrics);
					sponsorBank = arg2.getParcelableExtra("sponsorBank");
					break;	
				case 999:
					arrayList = custom2(arg2, relevant_custom_Etv, relevant_Ll, isNull,editTextViews);
					break;	
				default:
					break;
				}
			}
			if (arg0==1) {
				law_office = arg2.getStringArrayListExtra("Institution_Activity");
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  law_office,myitem_law_office.getMyitemview_Lv()),displayMetrics);
			}
		}
	}
}
