package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.Hashtable;
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
import android.widget.LinearLayout.LayoutParams;

import com.tr.R;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerInfo;
import com.tr.ui.organization.model.profile.CustomerLinkMan;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerProfile;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.view.MyAddMordView;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;
/**
 * 联系人资料
 * @author Wxh07151732
 *
 */
public class LinkmanInformationActivity extends BaseActivity {
	private static ArrayList<MyEditTextView> MyEditlist = null;
	private MyEditTextView linkman_name_Etv;
	private MyEditTextView linkman_phone_Etv;
	private MyEditTextView linkman_email_Etv;
	private LinearLayout linkman_Ll;
	private RelativeLayout quit_linkman_information_Rl;
	private ArrayList<String> list;
	private MyAddMordView linkman_MAMV;
	private ArrayList<String> list1;
	private LinearLayout linkman_main_Ll;
	private ArrayList<MyEditTextView> mEditViewlist;
	private MyDeleteView linkman_delete_Mdv;
	private ArrayList<MyLineraLayout> mLineralist;
	private int continueAdd1;
	private ArrayList<CustomerLinkMan> linkMans;
	private ArrayList<CustomerLinkMan> linkMan_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_linkman_information);
		linkMans = new ArrayList<CustomerLinkMan>();
		MyEditlist = new ArrayList<MyEditTextView>();
		list = new ArrayList<String>();
		mEditViewlist = new ArrayList<MyEditTextView>();
		mLineralist = new ArrayList<MyLineraLayout>();
		linkMan_list = new ArrayList<CustomerLinkMan>();
		list1 = new ArrayList<String>();
		init();
		initData();
	}

	private void initData() {
		linkman_MAMV.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				list1.add(linkman_name_Etv.getTextLabel());
				list1.add(linkman_phone_Etv.getTextLabel());
				list1.add(linkman_email_Etv.getTextLabel());
				ContinueAdd1(list1,linkman_main_Ll,mEditViewlist,mLineralist);
			}
		});
	
	}
	public void finish(View v){
		if (!MyEditlist.isEmpty()) {
			for (int i = 0; i < MyEditlist.size(); i++) {
				MyEditTextView myEditTextView = MyEditlist.get(i);
				list.add(myEditTextView.getTextLabel()+"_"+myEditTextView.getText());
			}
		}
		if (!TextUtils.isEmpty(linkman_name_Etv.getText())) {
			list.add(linkman_name_Etv.getTextLabel()+"_"+linkman_name_Etv.getText());
		}
		if (!TextUtils.isEmpty(linkman_phone_Etv.getText())) {
			list.add(linkman_phone_Etv.getTextLabel()+"_"+linkman_phone_Etv.getText());
		}
		if (!TextUtils.isEmpty(linkman_email_Etv.getText())) {
			list.add(linkman_email_Etv.getTextLabel()+"_"+linkman_email_Etv.getText());
		}
		Parcel parcel = Parcel.obtain();
		CustomerLinkMan linkMan = new CustomerLinkMan(parcel);
		linkMan.name =linkman_name_Etv.getText();
		linkMan.email = linkman_email_Etv.getText();
		linkMan.mobile = linkman_phone_Etv.getText();
		if (!"[]".equals(linkMan.toString())) {
			linkMans.add(linkMan);
		}
		if (!mLineralist.isEmpty()) {
			for (int i = 0; i < mLineralist.size(); i++) {
				MyEditTextView name_Etv = (MyEditTextView) mLineralist.get(i).getChildAt(1);
				MyEditTextView phone_Etv = (MyEditTextView) mLineralist.get(i).getChildAt(2);
				MyEditTextView email_Etv = (MyEditTextView) mLineralist.get(i).getChildAt(3);
				CustomerLinkMan linkMan_more = new CustomerLinkMan(parcel);
				linkMan_more.name =name_Etv.getText();
				linkMan_more.email = email_Etv.getText();
				linkMan_more.mobile = phone_Etv.getText();
				linkMans.add(linkMan_more);
			}
		}
		if (!list.isEmpty()) {
			Intent intent = new Intent(context, CreateClienteleActivity.class);
			intent.putStringArrayListExtra("Linkman_information", list);
			intent.putParcelableArrayListExtra("Linkman_information_Bean", linkMans);
			setResult(7012, intent);
		}
		finish();
	}
	private void init() {
		linkman_delete_Mdv = (MyDeleteView) findViewById(R.id.linkman_delete_Mdv);
		linkman_Ll = (LinearLayout) findViewById(R.id.linkman_Ll);
		linkman_main_Ll = (LinearLayout) findViewById(R.id.linkman_main_Ll);
		linkman_MAMV = (MyAddMordView) findViewById(R.id.linkman_MAMV);
		quit_linkman_information_Rl = (RelativeLayout) findViewById(R.id.quit_linkman_information_Rl);
		linkman_name_Etv = (MyEditTextView) findViewById(R.id.linkman_name_Etv);
		linkman_phone_Etv = (MyEditTextView) findViewById(R.id.linkman_phone_Etv);
		linkman_email_Etv = (MyEditTextView) findViewById(R.id.linkman_email_Etv);
		quit_linkman_information_Rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		linkman_delete_Mdv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mLineralist.size()==0) {
					finish();
				}else{
					linkman_Ll.removeAllViews();
				}
				
			}
		});
		linkMan_list = this.getIntent().getParcelableArrayListExtra("Linkman_information_Bean");
		if (linkMan_list!=null) {
		for (int i = 0; i < linkMan_list.size(); i++) {
			CustomerLinkMan linkMan = linkMan_list.get(i);
			if (i==0) {
				linkman_name_Etv.setText(linkMan.name );
				linkman_email_Etv.setText(linkMan.email );
				linkman_phone_Etv.setText(linkMan.mobile);
			}
			
			
			if(i>=1) {
				list1.add(linkman_name_Etv.getTextLabel());
				list1.add(linkman_phone_Etv.getTextLabel());
				list1.add(linkman_email_Etv.getTextLabel());
				
				ArrayList<MyEditTextView> continueAdd12 = ContinueAdd1(list1,linkman_main_Ll,mEditViewlist,mLineralist);
				if (continueAdd12!=null) {
						continueAdd12.get(i*3-3).setText(linkMan.name );
						continueAdd12.get(i*3+1-3).setText(linkMan.email );
						continueAdd12.get(i*3+2-3).setText(linkMan.mobile );
				}
				
			}
		}
		}
		
	}
}
