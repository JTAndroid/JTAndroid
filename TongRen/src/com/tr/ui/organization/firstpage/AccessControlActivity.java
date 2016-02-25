package com.tr.ui.organization.firstpage;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.tr.R;
import com.tr.ui.knowledge.PermissionActivity;
import com.tr.ui.organization.create_clientele.CreateClienteleActivity;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.utils.MakeGridview;
import com.tr.ui.people.cread.utils.Utils;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyGridView;

public class AccessControlActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout quit_module_Rl;
	private Intent intent;
	private MyEditTextView org_details_Etv;
	private MyEditTextView module_resource_Etv;
	private MyEditTextView module_finance_Etv;
	private MyEditTextView module_high_rise_Etv;
	private MyEditTextView module_shareholder_Etv;
	private MyEditTextView module_research_Etv;
	private MyEditTextView module_industry_trends_Etv;
	private MyEditTextView module_contend_Etv;
	private LinearLayout org_module_Ll;
	private static ArrayList<String> Module_list;
	private String[] module ={"联系人资料","上市信息","相关当事人","执业资质","分支机构","业务分析","关联企业","专业团队"," "," "};
	private MyGridView gridview;
	private ArrayList<String> list;
	private ArrayList<CheckBox> MyCheckList;
	private static HashMap<Integer, Boolean> map;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_accesscontrol);
		map = new HashMap<Integer, Boolean>();
		Module_list= new ArrayList<String>();
		MyCheckList = new ArrayList<CheckBox>();
		list =new ArrayList<String>();
		String Create_Clientele_Type = this.getIntent().getStringExtra("Create_Clientele");
		if (TextUtils.isEmpty(Create_Clientele_Type)) {
			for (int i = 0; i < module.length; i++) {
				Module_list.add(module[i]);
			}
		}else if("中介机构".equals(Create_Clientele_Type)){
			Module_list.add(Create_Clientele_Type);
			Module_list.add("企业刊物");
			Module_list.add("主要客户");
			Module_list.add("合伙人");
			for (int i = 0; i < module.length; i++) {
				Module_list.add(module[i]);
			}
		}else if("政府组织".equals(Create_Clientele_Type)){
			Module_list.add("机构简介");
			Module_list.add("联系人资料");
			Module_list.add("地区概况");
			Module_list.add("主要职能部门");
			Module_list.add(" ");
			Module_list.add(" ");
		}else if("金融机构".equals(Create_Clientele_Type)){
			Module_list.add(Create_Clientele_Type);
			Module_list.add("金融产品");
			for (int i = 0; i < module.length; i++) {
				Module_list.add(module[i]);
			}
		}else{
			Module_list.add(Create_Clientele_Type);
			for (int i = 0; i < module.length; i++) {
				Module_list.add(module[i]);
			}
		}
		init();
		initData();
		
	}
	/**
	 * 选择模块完成去创建客户
	 * @param v
	 */
	public void finishs(View v){
		Intent intent = new Intent(this,PermissionActivity.class);
		startActivity(intent);
		finish();
	}
	
	//选择跳过页面
	public void skip(View view){
		Intent intent = new Intent(this,PermissionActivity.class);
		startActivity(intent);
		finish();
	}
	
	
	private void initData() {
		
//		makeModuleGridviewAdapter(this, gridview, Module_list,MyCheckList);
		gridview.setAdapter(new BaseAdapter() {
			
			ViewHolder holder;
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				if (convertView==null) {
					holder = new ViewHolder();
					convertView = View.inflate(context, R.layout.org_grid_item, null);
					holder.org_grid_item_Tv = (TextView) convertView.findViewById(R.id.org_grid_item_Tv);
					holder.org_grid_item_Cb = (CheckBox) convertView.findViewById(R.id.org_grid_item_Cb);	
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder) convertView.getTag();
				}
				
				if (position==Module_list.size()-2) {
					View view = View.inflate(context, R.layout.org_module_item, null);
					return view;
				}
				else if (position==Module_list.size()-1) {
					View view = View.inflate(context, R.layout.org_module_item2, null);
					return view;
				}else{
					holder.org_grid_item_Tv.setText(Module_list.get(position));
					holder.org_grid_item_Cb.setClickable(false);
					Boolean ischecked = map.get(position);
					if (ischecked==null) {
						holder.org_grid_item_Cb.setChecked(false);
					}else{
						holder.org_grid_item_Cb.setChecked(ischecked);
					}
					MyCheckList.add(holder.org_grid_item_Cb);
					return convertView;
				}
			}
			@Override
			public long getItemId(int position) {
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				return null;
			}
			
			@Override
			public int getCount() {
				return Module_list.size();
			}
		});
//		makeModuleGridviewAlertDialog = makeModuleGridviewAlertDialog(this, gridview, list);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			private EditText alertdialog_Et;
			private TextView alertdialog_No;
			private TextView alertdialog_Yes;
			private AlertDialog create;
			private TextView alertdialog_Tv;
			private ViewHolder holder;
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				final BaseAdapter adapter = (BaseAdapter) parent.getAdapter();
				//自定义模块
				if (position==gridview.getLastVisiblePosition()-1) {
					AlertDialog.Builder builder = new Builder(AccessControlActivity.this);
					View view2 = View.inflate(AccessControlActivity.this, R.layout.people_alertdialog_module, null);
					builder.setView(view2);
					create = builder.create();
					alertdialog_Tv = (TextView) view2.findViewById(R.id.alertdialog_module_Tv);
					alertdialog_Yes = (TextView) view2.findViewById(R.id.alertdialog_module_Yes);
					alertdialog_No = (TextView) view2.findViewById(R.id.alertdialog_module_No);
					alertdialog_Et = (EditText) view2.findViewById(R.id.alertdialog_module_Et);
					alertdialog_Tv.setText("添加新的模块");	
					alertdialog_Yes.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String text = alertdialog_Et.getText().toString().trim();
							if (!TextUtils.isEmpty(text)) {
								Module_list.add(Module_list.size()-2,text);
								list.add(text);
							}else{
								Module_list.add("自定义模块");
							}
							if (adapter != null && adapter instanceof BaseAdapter) {
								adapter.notifyDataSetChanged();
							}
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
				//管理模块
				else if (position==gridview.getLastVisiblePosition()) {
					
				}else{
					holder = (ViewHolder) view.getTag();
					if (holder.org_grid_item_Cb.isChecked()) {
							holder.org_grid_item_Cb.setChecked(false);
							map.put(position, false);
					} else {
						holder.org_grid_item_Cb.setChecked(true);
						map.put(position, true);
					}
					if (holder.org_grid_item_Cb.isChecked()) {
						list.add(holder.org_grid_item_Tv.getText().toString());
					}else{
						list.remove(holder.org_grid_item_Tv.getText().toString());
					}
				}
				
			}
		});
	}

	private void init() {
		quit_module_Rl = (RelativeLayout) findViewById(R.id.quit_module_Rl);
		org_details_Etv = (MyEditTextView) findViewById(R.id.org_details_Etv);
		module_resource_Etv = (MyEditTextView) findViewById(R.id.module_resource_Etv);
		module_finance_Etv = (MyEditTextView) findViewById(R.id.module_finance_Etv);
		module_high_rise_Etv = (MyEditTextView) findViewById(R.id.module_high_rise_Etv);
		module_shareholder_Etv = (MyEditTextView) findViewById(R.id.module_shareholder_Etv);
		module_contend_Etv = (MyEditTextView) findViewById(R.id.module_contend_Etv);
		module_industry_trends_Etv = (MyEditTextView) findViewById(R.id.module_industry_trends_Etv);
		module_research_Etv= (MyEditTextView) findViewById(R.id.module_research_Etv);
		org_module_Ll = (LinearLayout) findViewById(R.id.org_module_Ll);
		gridview = new MyGridView(AccessControlActivity.this);
		gridview.setNumColumns(2);
		gridview.setVerticalSpacing((int) Utils.convertDpToPixel(10));
		gridview.setHorizontalSpacing((int) Utils.convertDpToPixel(10));
		org_module_Ll.addView(gridview, org_module_Ll.indexOfChild(org_details_Etv)+1);
		
		onclicklistener();
	}
	private void onclicklistener() {
		quit_module_Rl.setOnClickListener(this);
		org_details_Etv.setOnClickListener(this);
		module_resource_Etv.setOnClickListener(this);
		module_finance_Etv.setOnClickListener(this);
		module_high_rise_Etv.setOnClickListener(this);
		module_shareholder_Etv.setOnClickListener(this);
		module_contend_Etv.setOnClickListener(this);
		module_industry_trends_Etv.setOnClickListener(this);
		module_research_Etv.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_module_Rl:
			finish();
			break;
		case R.id.org_details_Etv:
				if (org_details_Etv.getCheckbox_cb().isChecked()) {
					org_details_Etv.getCheckbox_cb().setChecked(false);
					for (int i = 0; i < MyCheckList.size(); i++) {
					 CheckBox checkBox = MyCheckList.get(i);
					 checkBox.setChecked(false);
					}
			}else{
				org_details_Etv.getCheckbox_cb().setChecked(true);
				for (int i = 0; i < MyCheckList.size(); i++) {
					CheckBox checkBox = MyCheckList.get(i);
					checkBox.setChecked(true);
				}
			}
			break;
		case R.id.module_resource_Etv:
			if(module_resource_Etv.getCheckbox_cb().isChecked()){
				module_resource_Etv.getCheckbox_cb().setChecked(false);
			}else{
				module_resource_Etv.getCheckbox_cb().setChecked(true);
			}
			break;
		case R.id.module_finance_Etv:
			if(module_finance_Etv.getCheckbox_cb().isChecked()){
				module_finance_Etv.getCheckbox_cb().setChecked(false);
			}else{
				module_finance_Etv.getCheckbox_cb().setChecked(true);
			}
			break;
		case R.id.module_high_rise_Etv:
			if(module_high_rise_Etv.getCheckbox_cb().isChecked()){
				module_high_rise_Etv.getCheckbox_cb().setChecked(false);
			}else{
				module_high_rise_Etv.getCheckbox_cb().setChecked(true);
			}
			break;
		case R.id.module_shareholder_Etv:
			if(module_shareholder_Etv.getCheckbox_cb().isChecked()){
				module_shareholder_Etv.getCheckbox_cb().setChecked(false);
			}else{
				module_shareholder_Etv.getCheckbox_cb().setChecked(true);
			}
			break;
		case R.id.module_contend_Etv:
			if(module_contend_Etv.getCheckbox_cb().isChecked()){
				module_contend_Etv.getCheckbox_cb().setChecked(false);
			}else{
				module_contend_Etv.getCheckbox_cb().setChecked(true);
			}
			break;
		case R.id.module_industry_trends_Etv:
			if(module_industry_trends_Etv.getCheckbox_cb().isChecked()){
				module_industry_trends_Etv.getCheckbox_cb().setChecked(false);
			}else{
				module_industry_trends_Etv.getCheckbox_cb().setChecked(true);
			}
			break;
		case R.id.module_research_Etv:
			if(module_research_Etv.getCheckbox_cb().isChecked()){
				module_research_Etv.getCheckbox_cb().setChecked(false);
			}else{
				module_research_Etv.getCheckbox_cb().setChecked(true);
			}
			break;
			
		
		default:
			break;
		}

	}
	public static class ViewHolder{
		TextView org_grid_item_Tv;
		CheckBox org_grid_item_Cb;
	}
}
