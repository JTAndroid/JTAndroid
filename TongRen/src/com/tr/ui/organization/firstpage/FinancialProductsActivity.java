package com.tr.ui.organization.firstpage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.tr.R;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.view.MyAddMordView;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;
/**
 * 金融产品
 * @author Wxh07151732
 *
 */
public class FinancialProductsActivity extends BaseActivity implements OnClickListener{
	private Intent intent;
	private MyAddMordView linkman_MAMV;
	private MyEditTextView linkman_name_Etv,linkman_phone_Etv,linkman_email_Etv;
	private MyEditTextView linkman_location_Etv,linkman_area_Etv,linkman_status_Etv,custom_Text_Etv;
	private MyDeleteView linkman_delete_Mdv;
	private LinearLayout linkman_main_LL;
	private ArrayList<String> list1;
	private ArrayList<MyEditTextView> list2;
	private ArrayList<MyLineraLayout> layouts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_financial_products);
		
		linkman_main_LL = (LinearLayout) findViewById(R.id.linkman_main_LL);
		
		linkman_MAMV = (MyAddMordView) findViewById(R.id.linkman_MAMV);
		
		linkman_name_Etv = (MyEditTextView) findViewById(R.id.linkman_name_Etv);
		linkman_phone_Etv = (MyEditTextView) findViewById(R.id.linkman_phone_Etv);
		linkman_email_Etv = (MyEditTextView) findViewById(R.id.linkman_email_Etv);
		linkman_location_Etv = (MyEditTextView) findViewById(R.id.linkman_location_Etv);
		linkman_area_Etv = (MyEditTextView) findViewById(R.id.linkman_area_Etv);
		linkman_status_Etv = (MyEditTextView) findViewById(R.id.linkman_status_Etv);
		custom_Text_Etv = (MyEditTextView) findViewById(R.id.custom_Text_Etv);
		
		linkman_delete_Mdv = (MyDeleteView) findViewById(R.id.linkman_delete_Mdv);
		
        list1 = new ArrayList<String>();
		
		list2 = new ArrayList<MyEditTextView>();
		
		layouts = new ArrayList<MyLineraLayout>();
		
		linkman_MAMV.setOnClickListener(this);
		custom_Text_Etv.setOnClickListener(this);
		linkman_delete_Mdv.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linkman_MAMV://点击添加
			list1.add(linkman_name_Etv.getTextLabel());
			list1.add(linkman_phone_Etv.getTextLabel());
			list1.add(linkman_email_Etv.getTextLabel());
			list1.add(linkman_location_Etv.getTextLabel());
			list1.add(linkman_area_Etv.getTextLabel());
			list1.add(linkman_status_Etv.getTextLabel());
			list1.add(custom_Text_Etv.getTextLabel());
			ContinueAdd2(list1, linkman_main_LL, list2, layouts);
			break;
		case R.id.custom_Text_Etv://跳转到自定义页面
			intent = new Intent(this,CustomActivity.class);
			startActivity(intent);
			break;
		case R.id.linkman_delete_Mdv://点击删除
			break;

		}
		
	}
}
