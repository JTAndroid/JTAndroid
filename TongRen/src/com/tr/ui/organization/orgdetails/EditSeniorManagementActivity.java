package com.tr.ui.organization.orgdetails;

import java.util.ArrayList;



import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.hight.CustomerHight;
import com.tr.ui.organization.model.hight.CustomerHightInfo;
import com.tr.ui.organization.model.profile.CustomerLinkMan;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.view.MyAddMordView;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;
/**
 * 高层治理  编辑页面
 * @author User
 *
 */

public class EditSeniorManagementActivity extends BaseActivity implements OnClickListener,IBindData {
	

	private ArrayList<String> list;
	private ArrayList<String> list1;
	private ArrayList<MyEditTextView> mEditViewlist;
	private ArrayList<MyLineraLayout> mLineralist;
	private LinearLayout continueadd_Ll;
	private LinearLayout edit_senoirmanager_Ll;
	private MyEditTextView eidt_name_Etv;
	private MyEditTextView eidt_date_Etv;
	private MyEditTextView eidt_xueli_Etv;
	private MyEditTextView eidt_position_Etv;
	private MyDeleteView edit_seniormanager_delete_Mdv;
	private MyAddMordView edit_seniormanager_MAMV;
	private LinearLayout edit_seniormanager_main_Ll;
	private RelativeLayout quit_senior_management_Rl;
	private TextView delete_Tv;
	private ArrayList<CustomerHightInfo> edit_senior;
	private ArrayList<CustomerHightInfo> Edit_Seniormanagement_Bean;
	private MyEditTextView eidt_custom_Etv;
	private ArrayList<CustomerPersonalLine> arrayList;
	private ArrayList<MyEditTextView> editTextViews;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_edit_seniormanager);
		 Edit_Seniormanagement_Bean = new ArrayList<CustomerHightInfo>();
		 initUI();
			editTextViews = new ArrayList<MyEditTextView>();
			list = new ArrayList<String>();
			list1 = new ArrayList<String>();
			mEditViewlist = new ArrayList<MyEditTextView>();
			mLineralist = new ArrayList<MyLineraLayout>();
			edit_senior = new ArrayList<CustomerHightInfo>();
	}

	private void initUI() {
		
		quit_senior_management_Rl = (RelativeLayout) findViewById(R.id.quit_senior_management_Rl);
		TextView continueadd_Tv = (TextView) findViewById(R.id.continueadd_Tv);
		continueadd_Ll = (LinearLayout) findViewById(R.id.continueadd_Ll);
		continueadd_Tv.setText("新增高管信息");
		
		edit_senoirmanager_Ll = (LinearLayout) findViewById(R.id.edit_senoirmanager_Ll);
		edit_seniormanager_main_Ll = (LinearLayout) findViewById(R.id.edit_seniormanager_main_Ll);
		
		eidt_name_Etv = (MyEditTextView) findViewById(R.id.eidt_name_Etv);
		eidt_date_Etv = (MyEditTextView) findViewById(R.id.eidt_date_Etv);
		eidt_xueli_Etv = (MyEditTextView) findViewById(R.id.eidt_xueli_Etv);
		eidt_position_Etv = (MyEditTextView) findViewById(R.id.eidt_position_Etv);
		delete_Tv = (TextView) findViewById(R.id.delete_Tv);
		eidt_custom_Etv= (MyEditTextView) findViewById(R.id.eidt_custom_Etv);
		
		edit_seniormanager_delete_Mdv = (MyDeleteView) findViewById(R.id.edit_seniormanager_delete_Mdv);
		edit_seniormanager_MAMV = (MyAddMordView) findViewById(R.id.edit_seniormanager_MAMV);
		
		eidt_custom_Etv.setOnClickListener(this);
		quit_senior_management_Rl.setOnClickListener(this);
		edit_seniormanager_delete_Mdv.setOnClickListener(this);
		edit_seniormanager_MAMV.setOnClickListener(this);
		
		Edit_Seniormanagement_Bean = this.getIntent().getParcelableArrayListExtra("Edit_Seniormanagement_Bean");
		if (Edit_Seniormanagement_Bean!=null) {
			
		for (int i = 0; i < Edit_Seniormanagement_Bean.size(); i++) {
			CustomerHightInfo customerHightInfo = Edit_Seniormanagement_Bean.get(i);
			if (i==0) {
				eidt_name_Etv.setText(customerHightInfo.relation.relation );
				  eidt_date_Etv.setText(customerHightInfo.birth);
				 eidt_xueli_Etv.setText(customerHightInfo.eduational );
				 eidt_position_Etv.setText(customerHightInfo.job);
			}else{
				list1.add(eidt_name_Etv.getTextLabel());
				list1.add(eidt_date_Etv.getTextLabel());
				list1.add(eidt_xueli_Etv.getTextLabel());
				list1.add(eidt_position_Etv.getTextLabel());
				ArrayList<MyEditTextView> continueAdd12 = ContinueAdd1(list1,edit_seniormanager_main_Ll,mEditViewlist,mLineralist);
				if (continueAdd12!=null) {
						continueAdd12.get(i*list1.size()-list1.size()).setText(customerHightInfo.relation.relation);
						continueAdd12.get(i*list1.size()+1-list1.size()).setText(customerHightInfo.birth);
						continueAdd12.get(i*list1.size()+2-list1.size()).setText(customerHightInfo.eduational);
						continueAdd12.get(i*list1.size()+3-list1.size()).setText(customerHightInfo.job);
				}
				
			}
		}
			}
	}
   
	 public void save(View v){
		 CustomerHightInfo hightInfo = new CustomerHightInfo();
		 Relation relation = new Relation();
		 relation.relation =eidt_name_Etv.getText(); 
		 hightInfo.relation = relation;
		 hightInfo.birth = eidt_date_Etv.getText();
		 hightInfo.eduational = eidt_xueli_Etv.getText();
		 hightInfo.job = eidt_position_Etv.getText();
		 edit_senior.add(hightInfo);
		 if (!mLineralist.isEmpty()) {
				for (int i = 0; i < mLineralist.size(); i++) {
					MyEditTextView name_Etv = (MyEditTextView) mLineralist.get(i).getChildAt(1);
					MyEditTextView date_Etv = (MyEditTextView) mLineralist.get(i).getChildAt(2);
					MyEditTextView xueli_Etv = (MyEditTextView) mLineralist.get(i).getChildAt(3);
					MyEditTextView position_Etv = (MyEditTextView) mLineralist.get(i).getChildAt(4);
					CustomerHightInfo HightInfo_more = new CustomerHightInfo();
					Relation relation_more = new Relation();
					relation_more.relation =name_Etv.getText(); 
					HightInfo_more.relation= relation_more;
					HightInfo_more.birth = date_Etv.getText();
					HightInfo_more.eduational = xueli_Etv.getText();
					HightInfo_more.job = position_Etv.getText();
					edit_senior.add(HightInfo_more);
				}
			}
		 
		 Bundle bundle = new Bundle();
		 bundle.putParcelableArrayList("Edit_Seniormanagement_Bean", edit_senior);
		 CustomerHight customerHight = new CustomerHight();
		 customerHight.gczlList= edit_senior;
		 customerHight.personalLineList = arrayList;
		 OrganizationReqUtil.doRequestWebAPI(context, this, customerHight, null, OrganizationReqType.ORGANIZATION_REQ_ADD_HIGHLEVEL);
		 Intent intent = new Intent();
		 bundle.putSerializable("Custom_Bean", arrayList);
		 intent.putExtras(bundle);
		 setResult(RESULT_OK, intent);
		 finish();
	 }
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.quit_senior_management_Rl:
			finish();
			break;
		case R.id.edit_seniormanager_delete_Mdv:
			if (mLineralist.isEmpty()) {
				finish();
			}else{
				edit_senoirmanager_Ll.removeAllViews();
			}					
			break;
		case R.id.eidt_custom_Etv:
			Intent intent = new Intent(context, CustomActivity.class);
			intent.putExtra("fengxing", true);
			Bundle bundle = new Bundle();
			bundle.putSerializable("Customer_Bean", arrayList);
//			intent.putParcelableArrayListExtra("Customer_Bean", arrayList);
			intent.putExtras(bundle);
			startActivityForResult(intent, 999);
			break;
		case R.id.edit_seniormanager_MAMV:
			list1.add(eidt_name_Etv.getTextLabel());
			list1.add(eidt_date_Etv.getTextLabel());
			list1.add(eidt_xueli_Etv.getTextLabel());
			list1.add(eidt_position_Etv.getTextLabel());
			list1.add(eidt_custom_Etv.getTextLabel());
			ContinueAdd2(list1, edit_seniormanager_main_Ll, mEditViewlist,mLineralist);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null) {
			switch (arg1) {
			case 999:
				boolean isNull = arg2.getBooleanExtra("isNull", false);
				arrayList = custom2(arg2, eidt_custom_Etv, edit_senoirmanager_Ll,isNull,editTextViews);
				
				break;

			default:
				break;
			}
		}
	}
	@Override
	public void bindData(int tag, Object object) {
		
	}

}
