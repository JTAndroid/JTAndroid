package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.tr.R;
import com.tr.ui.organization.model.Customer;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerInfo;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerProfile;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.utils.Utils;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyGridView;
import com.tr.ui.people.cread.view.WordWrapView;
//import com.tr.ui.people.cread.view.WordWrapView;
/**
 * 一般企业
 * @author Wxh07151732
 *
 */
public class EnterpriseActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout quit_enterprise_Rl;
	private MyEditTextView enterprise_capital_Etv;
	private MyEditTextView enterprise_custom_Etv;
	private MyEditTextView enterprise_history_Etv;
	private MyEditTextView enterprise_describe_Etv;
	private MyEditTextView enterprise_url_Etv;
	private MyEditTextView enterprise_fox_Etv;
	private MyEditTextView enterprise_holding_Etv;
	private MyEditTextView enterprise_legal_Etv;
	private ArrayList<String> list;
	private String enterprise_Tv;
	private TextView enterprise_Tv2;
	private LinearLayout enterprise_Ll;
	private View line_Ll;
	private String[] strs = {"推荐岁id卡到爆比较卡是必备道具卡视角看snna","三啊肯德基拉萨空间","接受的话好卡","帮你吧","内部教案看","斯达"};
	private MyDeleteView enterprise_delete_Etv;
	private List<CustomerPersonalLine> custom;
	private ArrayList<CustomerPersonalLine> myEditTextView;
	private CustomerInfo customerInfo;
	private MyEditTextView editor_in_chief;
	private MyEditTextView proprieter;
	private MyEditTextView issues;
	private MyEditTextView unit;
	private MyEditTextView column;
	private MyEditTextView url_type;
	private WordWrapView wordWrapView;
	private WordWrapView wordWrapView1;
	private Bundle bundle;
	private List<String> columnList;
	private List<String> typesList;
	private boolean isNull;
	private ArrayList<MyEditTextView> editTextViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_enterprise);
		editTextViews = new ArrayList<MyEditTextView>();
		list =  new ArrayList<String>();
		wordWrapView = new WordWrapView(this);
		 wordWrapView1 = new WordWrapView(this);
		 unit = new MyEditTextView(this);
		 editor_in_chief = new MyEditTextView(this);
			proprieter = new MyEditTextView(this);
			issues = new MyEditTextView(this);
		enterprise_Tv = this.getIntent().getStringExtra("Enterprise");
		custom = new ArrayList<CustomerPersonalLine>();
		columnList =  new ArrayList<String>();
		typesList = new ArrayList<String>();
		init();
		initData();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_enterprise_Rl:
			finish();
			break;
		case R.id.enterprise_custom_Etv:
			Intent intent = new Intent(this, CustomActivity.class);
			intent.putExtra("fengxing", true);
			
			if (myEditTextView!=null) {
				if (!isNull) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("Customer_Bean", myEditTextView);
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
		enterprise_Tv2.setText(enterprise_Tv);
		if ("报纸期刊".equals(enterprise_Tv)) {
			
			editor_in_chief.setTextLabel("主编");
			proprieter.setTextLabel("社长");
			issues.setTextLabel("刊号");
			enterprise_Ll.addView(editor_in_chief,enterprise_Ll.indexOfChild(enterprise_url_Etv)+1);
			enterprise_Ll.addView(proprieter,enterprise_Ll.indexOfChild(enterprise_url_Etv)+2);
			enterprise_Ll.addView(issues,enterprise_Ll.indexOfChild(enterprise_url_Etv)+3);
		}
		if ("互联网媒体".equals(enterprise_Tv)) {
			unit = new MyEditTextView(this);		
			unit.setTextLabel("主办单位");
			 column = new MyEditTextView(this);			
			column.setTextLabel("主要栏目");
			column.setReadOnly(true);
			url_type = new MyEditTextView(this);
			url_type.setTextLabel("网址类型");
			url_type.setReadOnly(true);
			
//			 for (int i = 0; i < strs.length; i++) {
//			      TextView textview = new TextView(this);
//			      textview.setText(strs [i]);
//			      wordWrapView.addView(textview);
//			    }
			 ImageView imageView = new ImageView(this);
			 imageView.setBackgroundResource(R.drawable.org_wholeaddlabel);
			 LayoutParams layoutParams =new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			 layoutParams.leftMargin=(int)Utils.convertDpToPixel(20);
			 layoutParams.bottomMargin=(int)Utils.convertDpToPixel(10);
			 layoutParams.topMargin =(int) Utils.convertDpToPixel(10);
			 imageView.setOnClickListener(new OnClickListener() {
				
				private AlertDialog create;
				private TextView alertdialog_Tv;
				private TextView alertdialog_Yes;
				private TextView alertdialog_No;
				private EditText alertdialog_Et;

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(EnterpriseActivity.this);
					View view2 = View.inflate(EnterpriseActivity.this, R.layout.people_alertdialog_module, null);
					builder.setView(view2);
					create = builder.create();
					alertdialog_Tv = (TextView) view2.findViewById(R.id.alertdialog_module_Tv);
					alertdialog_Yes = (TextView) view2.findViewById(R.id.alertdialog_module_Yes);
					alertdialog_No = (TextView) view2.findViewById(R.id.alertdialog_module_No);
					alertdialog_Et = (EditText) view2.findViewById(R.id.alertdialog_module_Et);
					alertdialog_Tv.setText("添加新的标签");	
					alertdialog_Yes.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String text = alertdialog_Et.getText().toString().trim();
							TextView view = new TextView(EnterpriseActivity.this);
							view.setText(text);
							wordWrapView.addView(view);
							create.dismiss();
						}
					});
					alertdialog_No.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							create.dismiss();
						}
					});
					create.show();
				}
			});
			 View view = new View(this);
			 view.setBackgroundColor(Color.LTGRAY);
			
			 ImageView imageView1 = new ImageView(this);
			 imageView1.setBackgroundResource(R.drawable.org_wholeaddlabel);
			 LayoutParams layoutParams1 =new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			 layoutParams1.leftMargin=(int)Utils.convertDpToPixel(20);
			 layoutParams1.bottomMargin=(int)Utils.convertDpToPixel(10);
			 layoutParams1.topMargin =(int) Utils.convertDpToPixel(10);
			 imageView1.setOnClickListener(new OnClickListener() {
				
				private AlertDialog create;
				private TextView alertdialog_Tv;
				private TextView alertdialog_Yes;
				private TextView alertdialog_No;
				private EditText alertdialog_Et;

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(EnterpriseActivity.this);
					View view2 = View.inflate(EnterpriseActivity.this, R.layout.people_alertdialog_module, null);
					builder.setView(view2);
					create = builder.create();
					alertdialog_Tv = (TextView) view2.findViewById(R.id.alertdialog_module_Tv);
					alertdialog_Yes = (TextView) view2.findViewById(R.id.alertdialog_module_Yes);
					alertdialog_No = (TextView) view2.findViewById(R.id.alertdialog_module_No);
					alertdialog_Et = (EditText) view2.findViewById(R.id.alertdialog_module_Et);
					alertdialog_Tv.setText("添加新的标签");	
					alertdialog_Yes.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String text = alertdialog_Et.getText().toString().trim();
							TextView view = new TextView(EnterpriseActivity.this);
							view.setText(text);
							wordWrapView1.addView(view);
							create.dismiss();
						}
					});
					alertdialog_No.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							create.dismiss();
						}
					});
					create.show();
				}
			});
			
			enterprise_Ll.addView(unit,enterprise_Ll.indexOfChild(enterprise_url_Etv)+1);
			
			enterprise_Ll.addView(column,enterprise_Ll.indexOfChild(enterprise_url_Etv)+2);
			
			enterprise_Ll.addView(wordWrapView,enterprise_Ll.indexOfChild(column)+1);
			enterprise_Ll.addView(imageView,enterprise_Ll.indexOfChild(column)+2,layoutParams);
			enterprise_Ll.addView(view,enterprise_Ll.indexOfChild(column)+3,new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 1));
			enterprise_Ll.addView(url_type,enterprise_Ll.indexOfChild(enterprise_url_Etv)+6);
			enterprise_Ll.addView(wordWrapView1,enterprise_Ll.indexOfChild(enterprise_url_Etv)+7);
			enterprise_Ll.addView(imageView1,enterprise_Ll.indexOfChild(wordWrapView1)+1,layoutParams1);
			
		}
		quit_enterprise_Rl.setOnClickListener(this);
		enterprise_custom_Etv.setOnClickListener(this);
	}
	
	
	
	
	public void finish(View v){
		if (!TextUtils.isEmpty(enterprise_capital_Etv.getText())) {
			list.add(enterprise_capital_Etv.getTextLabel()+"_"+enterprise_capital_Etv.getText());
		}
		if (!TextUtils.isEmpty(enterprise_legal_Etv.getText())) {
			list.add(enterprise_legal_Etv.getTextLabel()+"_"+enterprise_legal_Etv.getText());
		}
		if (!TextUtils.isEmpty(enterprise_holding_Etv.getText())) {
			list.add(enterprise_holding_Etv.getTextLabel()+"_"+enterprise_holding_Etv.getText());
		}
		if (!TextUtils.isEmpty(enterprise_fox_Etv.getText())) {
			list.add(enterprise_fox_Etv.getTextLabel()+"_"+enterprise_fox_Etv.getText());
		}
		if (!TextUtils.isEmpty(enterprise_url_Etv.getText())) {
			list.add(enterprise_url_Etv.getTextLabel()+"_"+enterprise_url_Etv.getText());
		}
		if (!TextUtils.isEmpty(enterprise_describe_Etv.getText())) {
			list.add(enterprise_describe_Etv.getTextLabel()+"_"+enterprise_describe_Etv.getText());
		}
		if (!TextUtils.isEmpty(enterprise_history_Etv.getText())) {
			list.add(enterprise_history_Etv.getTextLabel()+"_"+enterprise_history_Etv.getText());
		}
		
		if ("互联网媒体".equals(enterprise_Tv)) {
			CustomerInfo customerInfo = new CustomerInfo();
			customerInfo.capital = enterprise_capital_Etv.getText();
			Relation controler = new Relation();
			Relation corpn = new Relation();
			Relation relation = new Relation();
			controler.relation=enterprise_holding_Etv.getText();
			corpn.relation = enterprise_legal_Etv.getText();
			relation.relation = unit.getText();
			customerInfo.relation = relation;
			customerInfo.controler = controler;
			for (int j = 0; j < wordWrapView.getChildCount(); j++) {
				TextView childAt = (TextView) wordWrapView.getChildAt(j);
				typesList.add(childAt.getText().toString().trim());
			}
			for (int j = 0; j < wordWrapView1.getChildCount(); j++) {
				TextView childAt = (TextView) wordWrapView1.getChildAt(j);
				columnList.add(childAt.getText().toString().trim());
			}
			customerInfo.typesList = typesList;
			customerInfo.columnList = columnList;
			customerInfo.corpn = corpn;
			customerInfo.fax = enterprise_fox_Etv.getText();
			customerInfo.website = enterprise_url_Etv.getText();
			customerInfo.product = enterprise_describe_Etv.getText();
			customerInfo.history =  enterprise_history_Etv.getText();
			CustomerPersonalLine customerPersonalLine = new  CustomerPersonalLine();
			CustomerPersonalLine customerPersonalLine1 = new  CustomerPersonalLine();
				customerPersonalLine.name=enterprise_custom_Etv.getTextLabel();
				customerPersonalLine.content = enterprise_custom_Etv.getText();
				customerPersonalLine.type = "1";
//			if (myEditTextView!=null) {
//				customerPersonalLine1.name=myEditTextView.getTextLabel();
//				customerPersonalLine1.content=myEditTextView.getText();
//				customerPersonalLine1.type = "2";
//				custom.add(customerPersonalLine1);
//			}
			bundle = new Bundle();
			bundle.putSerializable("Enterprise_Bean", customerInfo);
			custom.add(customerPersonalLine);
			customerInfo.propertyList = custom; 
		}
		else if ("报纸期刊".equals(enterprise_Tv)) {
			CustomerInfo customerInfo = new CustomerInfo();
			customerInfo.capital = enterprise_capital_Etv.getText();
			Relation controler = new Relation();
			Relation corpn = new Relation();
			controler.relation=enterprise_holding_Etv.getText();
			corpn.relation = enterprise_legal_Etv.getText();
			customerInfo.controler = controler;
			customerInfo.corpn = corpn;
			customerInfo.fax = enterprise_fox_Etv.getText();
			customerInfo.website = enterprise_url_Etv.getText();
			customerInfo.product = enterprise_describe_Etv.getText();
			customerInfo.history =  enterprise_history_Etv.getText();
			Relation leader = new Relation();
			leader.relation = editor_in_chief.getText();
			Relation hostess = new Relation();
			hostess.relation = proprieter.getText();
			customerInfo.number = issues.getText();
			customerInfo.hostess =hostess;
			customerInfo.leader = leader;
			
			CustomerPersonalLine customerPersonalLine = new  CustomerPersonalLine();
			CustomerPersonalLine customerPersonalLine1 = new  CustomerPersonalLine();
			customerPersonalLine.name=enterprise_custom_Etv.getTextLabel();
			customerPersonalLine.content = enterprise_custom_Etv.getText();
			customerPersonalLine.type = "1";
//			if (myEditTextView!=null) {
//				customerPersonalLine1.name=myEditTextView.getTextLabel();
//				customerPersonalLine1.content=myEditTextView.getText();
//				customerPersonalLine1.type = "2";
//				custom.add(customerPersonalLine1);
//			}
			bundle = new Bundle();
			bundle.putSerializable("Enterprise_Bean", customerInfo);
			custom.add(customerPersonalLine);
			customerInfo.propertyList = custom; 
		}else {
			CustomerInfo customerInfo = new CustomerInfo();
			customerInfo.capital = enterprise_capital_Etv.getText();
			Relation controler = new Relation();
			Relation corpn = new Relation();
			controler.relation=enterprise_holding_Etv.getText();
			corpn.relation = enterprise_legal_Etv.getText();
			customerInfo.controler = controler;
			customerInfo.corpn = corpn;
			customerInfo.fax = enterprise_fox_Etv.getText();
			customerInfo.website = enterprise_url_Etv.getText();
			
			customerInfo.product = enterprise_describe_Etv.getText();
			customerInfo.history =  enterprise_history_Etv.getText();
			CustomerPersonalLine customerPersonalLine = new  CustomerPersonalLine();
			CustomerPersonalLine customerPersonalLine1 = new  CustomerPersonalLine();
			customerPersonalLine.name=enterprise_custom_Etv.getTextLabel();
			customerPersonalLine.content = enterprise_custom_Etv.getText();
			customerPersonalLine.type = "1";
//			if (myEditTextView!=null) {
//				customerPersonalLine1.name=myEditTextView.getTextLabel();
//				customerPersonalLine1.content=myEditTextView.getText();
//				customerPersonalLine1.type = "2";
//				custom.add(customerPersonalLine1);
//			}
			custom.add(customerPersonalLine);
			customerInfo.propertyList = custom; 
			bundle = new Bundle();
			bundle.putSerializable("Enterprise_Bean", customerInfo);
		}
		if (!list.isEmpty()) {
			Intent intent = new Intent(this, CreateClienteleActivity.class);
			intent.putStringArrayListExtra("Enterprise", list);
			
			intent.putExtras(bundle);
			setResult(7001, intent);
		}
		finish();
	}
	@Override
	protected void onStart() {
		super.onStart();
		
		
	}
	private void init() {
		line_Ll = findViewById(R.id.line_Ll);
		enterprise_delete_Etv = (MyDeleteView) findViewById(R.id.enterprise_delete_Etv);
		enterprise_delete_Etv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		enterprise_Tv2 = (TextView) findViewById(R.id.enterprise_Tv);
		enterprise_Ll = (LinearLayout) findViewById(R.id.enterprise_Ll);
		quit_enterprise_Rl = (RelativeLayout) findViewById(R.id.quit_enterprise_Rl);
		enterprise_capital_Etv = (MyEditTextView) findViewById(R.id.enterprise_capital_Etv);
		enterprise_legal_Etv = (MyEditTextView) findViewById(R.id.enterprise_legal_Etv);
		enterprise_holding_Etv = (MyEditTextView) findViewById(R.id.enterprise_holding_Etv);
		enterprise_fox_Etv = (MyEditTextView) findViewById(R.id.enterprise_fox_Etv);
		enterprise_url_Etv = (MyEditTextView) findViewById(R.id.enterprise_url_Etv);
		enterprise_describe_Etv = (MyEditTextView) findViewById(R.id.enterprise_describe_Etv);
		enterprise_history_Etv = (MyEditTextView) findViewById(R.id.enterprise_history_Etv);
		enterprise_custom_Etv = (MyEditTextView) findViewById(R.id.enterprise_custom_Etv);
		
		customerInfo = (CustomerInfo) this.getIntent().getSerializableExtra("Enterprise_Bean");
		if (customerInfo!=null) {
			enterprise_capital_Etv.setText(customerInfo.capital);
			Relation controler = customerInfo.controler;
			Relation corpn = customerInfo.corpn;
			enterprise_holding_Etv.setText(controler.relation);
			enterprise_legal_Etv.setText(corpn.relation);
			enterprise_fox_Etv.setText(customerInfo.fax);
			enterprise_url_Etv.setText(customerInfo.website);
			enterprise_describe_Etv.setText(customerInfo.product);
			 enterprise_history_Etv.setText(customerInfo.history );
			 if ("报纸期刊".equals(enterprise_Tv)) {
				  editor_in_chief.setText(customerInfo.leader.relation);
					proprieter.setText(customerInfo.hostess.relation);
					issues.setText(customerInfo.number );
			 }
			 if ("互联网媒体".equals(enterprise_Tv)) {
				 if (!customerInfo.typesList.isEmpty()) {
					 for (int i = 0; i < customerInfo.typesList.size(); i++) {
						 TextView textView = new TextView(context);
						 textView.setText(customerInfo.typesList.get(i));
						 wordWrapView.addView(textView);
					}
				}
				if (!customerInfo.columnList.isEmpty()) {
					for (int i = 0; i < customerInfo.columnList.size(); i++) {
						 TextView textView = new TextView(context);
						 textView.setText(customerInfo.columnList .get(i));
						 wordWrapView1.addView(textView);
					}
				}
				 
				unit.setText(customerInfo.relation.relation );
			}
			if (customerInfo.propertyList!=null) {
				if(!customerInfo.propertyList.isEmpty()){
					for (int i = 0; i < customerInfo.propertyList.size(); i++) {
						enterprise_custom_Etv.setText(customerInfo.propertyList.get(0).content);
						enterprise_custom_Etv.setTextLabel(customerInfo.propertyList.get(0).name);
						enterprise_custom_Etv.setAddMore_hint(false);
						if (customerInfo.propertyList.size()==2) {
							MyEditTextView  editTextView = new MyEditTextView(context);
							editTextView.setCustom_Text(true);
							editTextView.setText(customerInfo.propertyList.get(1).content);
							editTextView.setTextLabel(customerInfo.propertyList.get(1).name);
							enterprise_Ll.addView(editTextView,enterprise_Ll.indexOfChild(enterprise_custom_Etv)+1);
						}
					}
					
			}
		}
	}
	
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null) {
			switch (arg1) {
			case 999:
				myEditTextView = custom2(arg2, enterprise_custom_Etv, enterprise_Ll,isNull,editTextViews);
				break;

			default:
				break;
			}
		}
	}
}
