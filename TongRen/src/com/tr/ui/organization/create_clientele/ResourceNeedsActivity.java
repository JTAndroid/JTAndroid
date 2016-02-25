package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.ui.organization.model.resource.CustomerResource;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.FinancingActivity;
import com.tr.ui.people.cread.InvestmentActivity;
import com.tr.ui.people.cread.SpecialistIdentityActivity;
import com.tr.ui.people.cread.SpecialistNeedsActivity;
import com.tr.ui.people.cread.utils.MakeListView;
import com.tr.ui.people.cread.view.MyitemView;
/**
 * 资源需求
 * @author Wxh07151732
 *
 */
public class ResourceNeedsActivity extends BaseActivity implements OnClickListener {
	private MyitemView myitem_needs_institution;
	private MyitemView myitem_needs_financing;
	private MyitemView myitem_needs_specialist;
	private MyitemView myitem_needs_specialist_identity;
	private RelativeLayout quit_resource_needs_Rl;
	private String [] resource_needs = {"地区","行业","类型","自定义"};
	private ArrayList<String> resource_needs_list;
	private Intent intent;
	private ArrayList<String> list;
	//融资意向中4个类型的数据
	private CustomerResource resource;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_resource_needs);
		
		resource = (CustomerResource) getIntent().getSerializableExtra("resource");
		
		list = new ArrayList<String>();
		resource_needs_list = new ArrayList<String>();
		try {
			init();
			initData();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void finish(View v){
		if (!list.isEmpty()) {
			intent = new Intent();
			intent.putStringArrayListExtra("Resource_needs_Activity", list);
			setResult(7031, intent);
		}
		finish();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_resource_needs_Rl:
			finish();
			break;
		case R.id.myitem_needs_institution:
			intent = new Intent(this,InvestmentActivity.class);
			startActivityForResult(intent, 8000);
			break;
		case R.id.myitem_needs_financing:
			intent = new Intent(this,FinancingActivity.class);
			startActivityForResult(intent, 8001);
			break;
		case R.id.myitem_needs_specialist:
			intent = new Intent(this,SpecialistNeedsActivity.class);
			startActivityForResult(intent, 8002);
			break;
		case R.id.myitem_needs_specialist_identity:
			intent = new Intent(this,SpecialistIdentityActivity.class);
			startActivityForResult(intent, 8003);
			break;
			
		default:
			break;
		}
	}
	private void initData() {
		for (int i = 0; i < resource_needs.length; i++) {
			resource_needs_list.add(resource_needs[i]);
		}
		myitem_needs_institution.setOnClickListener(this);
		myitem_needs_financing.setOnClickListener(this);
		myitem_needs_specialist.setOnClickListener(this);
		myitem_needs_specialist_identity.setOnClickListener(this);
		quit_resource_needs_Rl.setOnClickListener(this);
		
		if(resource.investmentdemandList != null && resource.investmentdemandList.size() != 0){
			AdaptiveListView(MakeListView.ToListviewAdapters(context,resource.investmentdemandList,myitem_needs_institution.getMyitemview_Lv(),resource_needs),displayMetrics);
		}
		
		
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  resource_needs_list,myitem_needs_financing.getMyitemview_Lv()),displayMetrics);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  resource_needs_list,myitem_needs_specialist.getMyitemview_Lv()),displayMetrics);
		AdaptiveListView(MakeListView.ToListviewAdapter(context,  resource_needs_list,myitem_needs_specialist_identity.getMyitemview_Lv()),displayMetrics);
	}
	private void init() {
		myitem_needs_institution = (MyitemView) findViewById(R.id.myitem_needs_institution);
		myitem_needs_financing = (MyitemView) findViewById(R.id.myitem_needs_financing);
		myitem_needs_specialist = (MyitemView) findViewById(R.id.myitem_needs_specialist);
		myitem_needs_specialist_identity = (MyitemView) findViewById(R.id.myitem_needs_specialist_identity);
		quit_resource_needs_Rl = (RelativeLayout) findViewById(R.id.quit_resource_needs_Rl);
		
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null) {
			if (arg0==8000) {
				ArrayList<String> Investment = arg2.getStringArrayListExtra("Investment");
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  Investment,myitem_needs_institution.getMyitemview_Lv()),displayMetrics);
			}
			if (arg0==8001) {
				ArrayList<String> Financing = arg2.getStringArrayListExtra("Financing");
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  Financing,myitem_needs_financing.getMyitemview_Lv()),displayMetrics);
			}
			if (arg0==8002) {
				ArrayList<String> Specialist_needs = arg2.getStringArrayListExtra("Specialist_needs");
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  Specialist_needs,myitem_needs_specialist.getMyitemview_Lv()),displayMetrics);
			}
			if (arg0==8003) {
				ArrayList<String> Specialist_identity = arg2.getStringArrayListExtra("Specialist_identity");
				AdaptiveListView(MakeListView.ToListviewAdapter(context,  Specialist_identity,myitem_needs_specialist_identity.getMyitemview_Lv()),displayMetrics);
			}
		}
	}
	
}
