package com.tr.ui.people.cread;

import java.util.ArrayList;

import com.tr.R;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.model.Basic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 联系方式(子模块)
 * 初始化对象ArrayList<Basic>
 * 
 * @author Wxh07151732
 * 
 */
public class ContactInformationActivity extends BaseActivity implements
		OnClickListener {
	private TextView finish_contact_Tv;
	private MyEditTextView phone_contact_Etv;
	private MyEditTextView fixphone_contact_Etv;
	private MyEditTextView fox_contact_Etv;
	private MyEditTextView email_contact_Etv;
	private MyEditTextView homepage_contact_Etv;
	private MyEditTextView communication_contact_Etv;
	private MyEditTextView address_contact_Etv;
	private MyEditTextView custom_contact_Etv;
	private String[] phone = { "手机", "电话", "商务电话", "主要电话", "自定义" };
	private String[] fixphone = { "固话", "家庭电话", "办公电话", "主要固话", "自定义" };
	private String[] fox = { "住宅传真", "商务传真", "自定义" };
	private String[] email = { "主要邮箱", "商务邮箱", "自定义" };
	private String[] homepage = { "商务主页", "个人主页", "自定义" };
	private String[] communication = { "QQ", "微信", "微博", "Skype", "facebook",
			"twitter", "自定义" };
	private String[] address = { "住宅地址", "商务地址", "自定义" };
	private int Contact_information = 1;
	private LinearLayout contact_ll;
	private Intent intent;
	private LinearLayout delete_Ll;
	private RelativeLayout quit_contact_Rl;
	private ArrayList<MyEditTextView> Custom_list;
	private ArrayList<MyEditTextView> phone_list;
	private ArrayList<MyEditTextView> fixphone_list;
	private ArrayList<MyEditTextView> fox_list;
	private ArrayList<MyEditTextView> email_list;
	private ArrayList<MyEditTextView> homepage_list;
	private ArrayList<MyEditTextView> communication_list;
	private ArrayList<MyEditTextView> address_list;
	private ArrayList<Basic> basic_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_contact_information);
		basic_list = new ArrayList<Basic>();
		Custom_list = new ArrayList<MyEditTextView>();
		phone_list =new ArrayList<MyEditTextView>();
		fixphone_list =new ArrayList<MyEditTextView>();
		fox_list =new ArrayList<MyEditTextView>();
		email_list =new ArrayList<MyEditTextView>();
		homepage_list =new ArrayList<MyEditTextView>();
		communication_list =new ArrayList<MyEditTextView>();
		address_list =new ArrayList<MyEditTextView>();
		init();
		initData();
	}

	private void initData() {
		phone_contact_Etv.MakeAddMoreMethod(phone_contact_Etv,
				ContactInformationActivity.this,1, contact_ll, "手机", 
				phone_list);
		fixphone_contact_Etv.MakeAddMoreMethod(fixphone_contact_Etv,
				ContactInformationActivity.this,2, contact_ll, "固话", 
				fixphone_list);
		fox_contact_Etv.MakeAddMoreMethod(fox_contact_Etv,
				ContactInformationActivity.this,3, contact_ll, "住宅传真", 
				fox_list);
		email_contact_Etv.MakeAddMoreMethod(email_contact_Etv,
				ContactInformationActivity.this,4, contact_ll, "主要邮箱", 
				email_list);
		homepage_contact_Etv.MakeAddMoreMethod(homepage_contact_Etv,
				ContactInformationActivity.this,5, contact_ll, "个人主页",
				homepage_list);
		communication_contact_Etv.MakeAddMoreMethod(communication_contact_Etv,
				ContactInformationActivity.this,6, contact_ll, "QQ",
				communication_list);
		address_contact_Etv.MakeAddMoreMethod(address_contact_Etv,
				ContactInformationActivity.this,7, contact_ll, "住宅地址",
				address_list);
		custom_contact_Etv.MakeAddMoreMethod(custom_contact_Etv, context,0,
				contact_ll, "自定义", Custom_list);
		
		
	}
	
	/**
	 * 获取自定义对象
	 * @param myEdit  
	 * @param myEdit_List
	 * @param type
	 * @param basiclist
	 */
	private void getBasic(MyEditTextView myEdit, ArrayList<MyEditTextView> myEdit_List, String type, ArrayList<Basic> basiclist) {
		Basic basic = new Basic();
		basic.name = myEdit.getTextLabel();
		basic.content = myEdit.getText();
		basic.type = type;
		
		for (int i = 0; i < myEdit_List.size(); i++) {
			MyEditTextView myEditTextView = myEdit_List.get(i);
			Basic basic_list = new Basic();
			basic_list.name = myEditTextView.getTextLabel();
			basic_list.content = myEditTextView.getText();
			basic_list.type = type;
			basiclist.add(basic_list);
		}
		
		basiclist.add(basic);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		address_list.clear();
		address_list =null;
		communication_list.clear();
		communication_list =null;
		homepage_list.clear();
		homepage_list =null;
		email_list.clear();
		email_list =null;
		fox_list.clear();
		fox_list =null;
		fixphone_list.clear();
		fixphone_list =null;
		phone_list.clear();
		phone_list =null;
		Custom_list.clear();
		Custom_list =null;
	}
	/**
	 * 初始化控件和数据
	 */
	private void init() {
		quit_contact_Rl = (RelativeLayout) findViewById(R.id.quit_contact_Rl);
		finish_contact_Tv = (TextView) findViewById(R.id.finish_contact_Tv);
		phone_contact_Etv = (MyEditTextView) findViewById(R.id.phone_contact_Etv);
		fixphone_contact_Etv = (MyEditTextView) findViewById(R.id.fixphone_contact_Etv);
		fox_contact_Etv = (MyEditTextView) findViewById(R.id.fox_contact_Etv);
		email_contact_Etv = (MyEditTextView) findViewById(R.id.email_contact_Etv);
		homepage_contact_Etv = (MyEditTextView) findViewById(R.id.homepage_contact_Etv);
		communication_contact_Etv = (MyEditTextView) findViewById(R.id.communication_contact_Etv);
		address_contact_Etv = (MyEditTextView) findViewById(R.id.address_contact_Etv);
		custom_contact_Etv = (MyEditTextView) findViewById(R.id.custom_contact_Etv);
		contact_ll = (LinearLayout) findViewById(R.id.contact_ll);

		ArrayList<Basic> basic = (ArrayList<Basic>) this.getIntent().getSerializableExtra("Contact_information");
		if (basic!=null&&!basic.isEmpty()) {
			for (int i = 0; i < basic.size(); i++) {
				Basic basic_item = basic.get(i);
				if ("1".equals(basic_item.type)) {
					
					if (TextUtils.isEmpty(phone_contact_Etv.getText())) {
						phone_contact_Etv.setText(basic_item.content);
					}else{
						MyEditTextView phone = new MyEditTextView(context);
						phone.setText(basic_item.content);
						phone.setTextLabel(basic_item.name);
						phone.setDelete(true);
						contact_ll.addView(phone, contact_ll.indexOfChild(phone_contact_Etv));
					}
				}else if("2".equals(basic_item.type)) {
					if (TextUtils.isEmpty(fixphone_contact_Etv.getText())) {
						fixphone_contact_Etv.setText(basic_item.content);
					}else{
						MyEditTextView fixphone = new MyEditTextView(context);
						fixphone.setText(basic_item.content);
						fixphone.setTextLabel(basic_item.name);
						fixphone.setDelete(true);
						contact_ll.addView(fixphone, contact_ll.indexOfChild(fixphone_contact_Etv)+1);
					}
				}else if("3".equals(basic_item.type)) {
					if (TextUtils.isEmpty(fox_contact_Etv.getText())) {
						fox_contact_Etv.setText(basic_item.content);
					}else{
						MyEditTextView fox = new MyEditTextView(context);
						fox.setText(basic_item.content);
						fox.setTextLabel(basic_item.name);
						fox.setDelete(true);
						contact_ll.addView(fox, contact_ll.indexOfChild(fox_contact_Etv)+1);
					}
				}else if("4".equals(basic_item.type)) {
					if (TextUtils.isEmpty(email_contact_Etv.getText())) {
						email_contact_Etv.setText(basic_item.content);
					}else{
						MyEditTextView email = new MyEditTextView(context);
						email.setText(basic_item.content);
						email.setTextLabel(basic_item.name);
						email.setDelete(true);
						contact_ll.addView(email, contact_ll.indexOfChild(email_contact_Etv)+1);
					}
				}else if("5".equals(basic_item.type)) {
					if (TextUtils.isEmpty(homepage_contact_Etv.getText())) {
						homepage_contact_Etv.setText(basic_item.content);
					}else{
						MyEditTextView homepage = new MyEditTextView(context);
						homepage.setText(basic_item.content);
						homepage.setTextLabel(basic_item.name);
						homepage.setDelete(true);
						contact_ll.addView(homepage, contact_ll.indexOfChild(homepage_contact_Etv)+1);
					}
				}else if("6".equals(basic_item.type)) {
					if (TextUtils.isEmpty(communication_contact_Etv.getText())) {
						communication_contact_Etv.setText(basic_item.content);
					}else{
						MyEditTextView communication = new MyEditTextView(context);
						communication.setText(basic_item.content);
						communication.setTextLabel(basic_item.name);
						communication.setDelete(true);
						contact_ll.addView(communication, contact_ll.indexOfChild(communication_contact_Etv)+1);
					}
				}else if("7".equals(basic_item.type)) {
					if (TextUtils.isEmpty(address_contact_Etv.getText())) {
						address_contact_Etv.setText(basic_item.content);
					}else{
						MyEditTextView address = new MyEditTextView(context);
						address.setText(basic_item.content);
						address.setTextLabel(basic_item.name);
						address.setDelete(true);
						contact_ll.addView(address, contact_ll.indexOfChild(address_contact_Etv)+1);
					}
				}else if("0".equals(basic_item.type)) {
					if (TextUtils.isEmpty(custom_contact_Etv.getText())) {
						custom_contact_Etv.setText(basic_item.content);
						custom_contact_Etv.setTextLabel(basic_item.name);
					}else{
						MyEditTextView custom = new MyEditTextView(context);
						custom.setText(basic_item.content);
						custom.setTextLabel(basic_item.name);
						custom.setDelete(true);
						contact_ll.addView(custom, contact_ll.indexOfChild(custom_contact_Etv)+1);
					}
					
				}
			}
		}
		
		finish_contact_Tv.setOnClickListener(this);
		quit_contact_Rl.setOnClickListener(this);
//		fixphone_contact_Etv.setOnClickListener(this);
//		phone_contact_Etv.setOnClickListener(this);
//		fox_contact_Etv.setOnClickListener(this);
//		email_contact_Etv.setOnClickListener(this);
//		homepage_contact_Etv.setOnClickListener(this);
//		communication_contact_Etv.setOnClickListener(this);
//		custom_contact_Etv.setOnClickListener(this);
//		address_contact_Etv.setOnClickListener(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (resultCode) {
			case 999://自定义
				ArrayList<String> Custom = data
						.getStringArrayListExtra("Custom");
				if (Custom != null) {
					if (Custom.size() == 1) {
						String text0 = Custom.get(0);
						String[] split = text0.split("_");
						String key0 = split[0];
						String value0 = split[1];
						custom_contact_Etv.setText(value0);
						custom_contact_Etv.setTextLabel(key0);
						custom_contact_Etv.setAddMore_hint(false);
						custom_contact_Etv.setChoose(false);
						custom_contact_Etv.setReadOnly(true);
					} else {
						String text0 = Custom.get(0);
						String[] split = text0.split("_");
						String key0 = split[0];
						String value0 = split[1];
						custom_contact_Etv.setText(value0);
						custom_contact_Etv.setTextLabel(key0);
						custom_contact_Etv.setAddMore_hint(false);
						custom_contact_Etv.setChoose(false);
						custom_contact_Etv.setReadOnly(true);
						String text1 = Custom.get(1);
						String[] split1 = text1.split("_");
						String key1 = split1[0];
						String value1 = split1[1];
						MyEditTextView editTextView = new MyEditTextView(this);
						editTextView.setCustomtextLabel(value1);
						editTextView.setTextLabel(key1);
						editTextView.setReadOnly(true);
						editTextView.setCustom_Text(true);
						contact_ll.addView(editTextView);
					}
				}
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_contact_Rl:
			finish();
			break;
		case R.id.finish_contact_Tv:
			getBasic(phone_contact_Etv,phone_list,"1",basic_list);
			getBasic(fixphone_contact_Etv,fixphone_list,"2",basic_list);
			getBasic(fox_contact_Etv,fox_list,"3",basic_list);
			getBasic(email_contact_Etv,email_list,"4",basic_list);
			getBasic(homepage_contact_Etv,homepage_list,"5",basic_list);
			getBasic(communication_contact_Etv,communication_list,"6",basic_list);
			getBasic(address_contact_Etv,address_list,"7",basic_list);
			getBasic(custom_contact_Etv,Custom_list,"N",basic_list);
			
			if (basic_list!=null&&!basic_list.isEmpty()) {
				intent = new Intent(context, NewConnectionsActivity.class);
				intent.putExtra("Contact_information", basic_list);
				setResult(0, intent);
			}
			
//			if (!TextUtils.isEmpty(phone_contact_Etv.getText())
//					&& !TextUtils.isEmpty(phone_contact_Etv.getTextLabel())) {
//				list.add(phone_contact_Etv.getTextLabel() + "_"
//						+ phone_contact_Etv.getText());
//			}
//			if (!TextUtils.isEmpty(fixphone_contact_Etv.getText())
//					&& !TextUtils.isEmpty(fixphone_contact_Etv.getTextLabel())) {
//				list.add(fixphone_contact_Etv.getTextLabel() + "_"
//						+ fixphone_contact_Etv.getText());
//			}
//			if (!TextUtils.isEmpty(fox_contact_Etv.getText())
//					&& !TextUtils.isEmpty(fox_contact_Etv.getTextLabel())) {
//				list.add(fox_contact_Etv.getTextLabel() + "_"
//						+ fox_contact_Etv.getText());
//			}
//			if (!TextUtils.isEmpty(email_contact_Etv.getText())
//					&& !TextUtils.isEmpty(email_contact_Etv.getTextLabel())) {
//				list.add(email_contact_Etv.getTextLabel() + "_"
//						+ email_contact_Etv.getText());
//
//			}
//			if (!TextUtils.isEmpty(homepage_contact_Etv.getText())
//					&& !TextUtils.isEmpty(homepage_contact_Etv.getTextLabel())) {
//				list.add(homepage_contact_Etv.getTextLabel() + "_"
//						+ homepage_contact_Etv.getText());
//
//			}
//			if (!TextUtils.isEmpty(communication_contact_Etv.getText())
//					&& !TextUtils.isEmpty(communication_contact_Etv
//							.getTextLabel())) {
//				list.add(communication_contact_Etv.getTextLabel() + "_"
//						+ communication_contact_Etv.getText());
//
//			}
//			if (!TextUtils.isEmpty(custom_contact_Etv.getText())
//					&& !TextUtils.isEmpty(custom_contact_Etv.getTextLabel())) {
//				list.add(custom_contact_Etv.getTextLabel() + "_"
//						+ custom_contact_Etv.getText());
//
//			}
//			if (!TextUtils.isEmpty(address_contact_Etv.getText())
//					&& !TextUtils.isEmpty(address_contact_Etv.getTextLabel())) {
//				list.add(address_contact_Etv.getTextLabel() + "_"
//						+ address_contact_Etv.getText());
//			}
//			if (!MyEdit_list.isEmpty()) {
//				for (int i = 0; i < MyEdit_list.size(); i++) {
//					MyEditTextView myEditTextView = MyEdit_list.get(i);
//					list.add(myEditTextView.getTextLabel() + "_"
//							+ myEditTextView.getText());
//				}
//			}
//			if (!"[]".equals(list.toString())) {
//				intent = new Intent(context, NewConnections_Activity.class);
//				intent.putStringArrayListExtra("Contact_information", list);
//				setResult(0, intent);
//			}
			
			
			finish();
			break;
		case R.id.phone_contact_Etv:
//			phone_contact_Etv.makePopupWindows(
//					Contact_information_Activity.this, v, phone);
			break;
		case R.id.fixphone_contact_Etv:
//			fixphone_contact_Etv.makePopupWindows(
//					Contact_information_Activity.this, v, fixphone);
			break;
		case R.id.fox_contact_Etv:
//			fox_contact_Etv.makePopupWindows(Contact_information_Activity.this,
//					v, fox);
			break;
		case R.id.email_contact_Etv:
//			email_contact_Etv.makePopupWindows(
//					Contact_information_Activity.this, v, email);
			break;
		case R.id.homepage_contact_Etv:
//			homepage_contact_Etv.makePopupWindows(
//					Contact_information_Activity.this, v, homepage);
			break;
		case R.id.custom_contact_Etv:
//			Intent intent = new Intent(Contact_information_Activity.this,
//					Custom_Activity.class);
//			startActivityForResult(intent, 0);
			break;
		case R.id.communication_contact_Etv:
//			communication_contact_Etv.makePopupWindows(
//					Contact_information_Activity.this, v, communication);
			break;
		case R.id.address_contact_Etv:
//			address_contact_Etv.makePopupWindows(
//					Contact_information_Activity.this, v, address);
			break;
		
		}
	}

}
