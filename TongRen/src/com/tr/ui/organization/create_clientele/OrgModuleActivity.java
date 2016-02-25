package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

//import com.baidu.navisdk.ui.routeguide.fsm.RouteGuideFSM.IFSMDestStateListener;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.ui.organization.model.template.CustomerColumnVo;
import com.tr.ui.organization.model.template.CustomerColumnVo.Results;
import com.tr.ui.organization.model.template.CustomerColumn_re;
import com.tr.ui.organization.model.template.Delete;
import com.tr.ui.organization.model.template.Save;
import com.tr.ui.organization.model.template.SaveRelation;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.MoreModuleActivity;
import com.tr.ui.people.cread.MoreModuleActivity.ViewHolder;
import com.tr.ui.people.cread.utils.MakeGridview;
import com.tr.ui.people.cread.utils.Utils;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyGridView;
import com.tr.ui.widgets.BasicListView2;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;

public class OrgModuleActivity extends BaseActivity implements
		OnClickListener, IBindData {
	private RelativeLayout quit_module_Rl;
	private Intent intent;
	private MyEditTextView org_details_Etv;
	private LinearLayout org_module_Ll;
	private static ArrayList<String> Module_list;
	private String[] module = { "联系人资料", "上市信息", "相关当事人", "执业资质", "分支机构",
			"业务分析", "关联企业", "专业团队", " ", " " };
	private MyGridView gridview;
	private ArrayList<String> list;
	private ArrayList<CheckBox> MyCheckList;
	private HashMap<Integer, CheckBox> map;
	private HashSet<String> set;
	private HashMap<CheckBox, String> MyCheckMap;
	private ArrayList<String> list_more;
	private HashMap<Integer, Boolean> map_Lv;
	private HashMap<String, Integer> module_map;
	private ArrayList<String> modules;
	private ArrayList<String> columnIds;
	private static HashMap<CheckBox, String> Map_Lv = null;
	private static HashMap<CheckBox, Long> MyCheck_Lv = null;
	private HashMap<CheckBox, Long> MyCheck_Gv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_module);
		modules = new ArrayList<String>();
		columnIds = new ArrayList<String>();
		map = new HashMap<Integer, CheckBox>();
		Module_list = new ArrayList<String>();
		MyCheckMap = new HashMap<CheckBox, String>();
		MyCheckList = new ArrayList<CheckBox>();
		map_Lv = new HashMap<Integer, Boolean>();
		set = new HashSet<String>();
		save = new Save();
		list = new ArrayList<String>();
		columnVo2 = new CustomerColumnVo();
		list_more = new ArrayList<String>();
		Map_Lv = new HashMap<CheckBox, String>();
		delete = new Delete();
		module_map = new HashMap<String, Integer>();
		MyCheck_Lv = new HashMap<CheckBox, Long>();
		MyCheck_Gv = new HashMap<CheckBox, Long>();
		String Create_Clientele_Type = this.getIntent().getStringExtra(
				"Create_Clientele");
		column_re2 = (CustomerColumn_re) this.getIntent().getSerializableExtra(
				"Column_Bean");

		init();
		initData();
		if (column_re2 != null) {
			OrganizationReqUtil.doRequestWebAPI(this, this, column_re2, null,
					OrganizationReqType.ORGANIZATION_CUSTOMER_COLUMNLIST);
		}
	}

	/**
	 * 选择模块完成去创建客户
	 * 
	 * @param v
	 */
	public void finishs(View v) {
		if (org_details_Etv.getCheckbox_cb().isChecked()) {
			for (int i = 0; i < Module_list.size(); i++) {
				String module = Module_list.get(i).trim();
				if (!TextUtils.isEmpty(module)) {
					set.add(module);
				}
			}
		}
		if (MyCheckMap != null) {
			Set<CheckBox> keySet = MyCheckMap.keySet();
			for (CheckBox checkBox : keySet) {
				if (checkBox.isChecked()) {
					String text = MyCheckMap.get(checkBox);
					set.add(text);
				}
			}
		}

		if (Map_Lv != null) {
			Set<CheckBox> keySet = Map_Lv.keySet();
			for (CheckBox checkBox : keySet) {
				if (checkBox.isChecked()) {
					String m = Map_Lv.get(checkBox);
					set.add(m);
				}
			}
		}
		if (MyCheck_Lv != null) {
			Set<CheckBox> keySet = MyCheck_Lv.keySet();
			for (CheckBox checkBox : keySet) {
				if (checkBox.isChecked()) {
					Long m = MyCheck_Lv.get(checkBox);
					columnIds.add(m + "");
				}
			}
		}
		if (MyCheck_Gv != null) {
			Set<CheckBox> keySet = MyCheck_Gv.keySet();
			for (CheckBox checkBox : keySet) {
				if (checkBox.isChecked()) {
					Long m = MyCheck_Gv.get(checkBox);
					columnIds.add(m + "");
				}
			}
		}
		SaveRelation saveRelation = new SaveRelation();
		saveRelation.columnIds = columnIds;
		saveRelation.orgId = column_re2.orgId;

		if (saveRelation != null) {
			OrganizationReqUtil.doRequestWebAPI(this, this, saveRelation, null,
					OrganizationReqType.ORGANIZATION_CUSTOMER_SAVERELATION);
		}
		for (String text : set) {
			list.add(text);
		}
		if (!list.isEmpty()) {
			intent = new Intent();
			intent.putStringArrayListExtra("Module", list);
			setResult(23, intent);
		}
		finish();
	}

	private ViewHolder holder;
	private CheckBox check;
	private BasicListView2 module_Lv;
	private ModuleAdapter adapter;
	private CustomerColumnVo columnVo2;
	private CustomerColumn_re column_re2;
	private Save save;
	private Delete delete;
	private Long s;

	private void initData() {
		adapter = new ModuleAdapter();
		module_Lv.setAdapter(adapter);
		gridview.setAdapter(new BaseAdapter() {
			

			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View view = View.inflate(context, R.layout.org_grid_item, null);
				TextView org_grid_item_Tv = (TextView) view
						.findViewById(R.id.org_grid_item_Tv);
				CheckBox org_grid_item_Cb = (CheckBox) view
						.findViewById(R.id.org_grid_item_Cb);

				if (position == Module_list.size() - 2) {
					view = View
							.inflate(context, R.layout.org_module_item, null);
					return view;
				} else if (position == Module_list.size() - 1) {
					view = View.inflate(context, R.layout.org_module_item2,
							null);
					return view;
				} else {
					String text = Module_list.get(position);
					if (text != null) {
						org_grid_item_Tv.setText(text);
					}
					Results results = columnVo2.results.get(0);
					
					MyCheck_Lv.put(org_details_Etv.getCheckbox_cb(), results.id);	// add by ww 2015-04-10
					Map_Lv.put(org_details_Etv.getCheckbox_cb(), results.name);		// add by ww 2015-04-10
					if(results.isSelect.equals("1")){
						org_details_Etv.getCheckbox_cb().setChecked(true);
					}
					
					if (position<(results.child.size())) {
						if ("1".equals(results.child.get(position).isSelect)) {
							org_grid_item_Cb.setChecked(true);
						} else {
							org_grid_item_Cb.setChecked(false);
						}
						if (!results.child.isEmpty()) {
							long id = results.child.get(position).id;
							MyCheck_Gv.put(org_grid_item_Cb, id);
						}
					}
					
					org_grid_item_Cb.setClickable(false);

					map.put(position, org_grid_item_Cb);
					MyCheckMap.put(org_grid_item_Cb, text);
					return view;
				}
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return Module_list.get(position);
			}

			@Override
			public int getCount() {
				return Module_list.size();
			}
		});
		module_Lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				holder = (ViewHolder) view.getTag();
				if (holder.module_Cb.isChecked()) {
					holder.module_Cb.setChecked(false);
					map_Lv.put(position, false);
				} else {
					holder.module_Cb.setChecked(true);
					map_Lv.put(position, true);
				}

				if (holder.module_Cb.isChecked()) {
					module_map.put(holder.module_Tv.getText().toString(),
							position);
				} else {
					module_map.remove(holder.module_Tv.getText().toString());
				}

			}
		});
		gridview.setOnItemClickListener(new OnItemClickListener() {

			private EditText alertdialog_Et;
			private TextView alertdialog_No;
			private TextView alertdialog_Yes;
			private AlertDialog create;
			private TextView alertdialog_Tv;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				final BaseAdapter adapter = (BaseAdapter) parent.getAdapter();
				// 自定义模块
				if (position == gridview.getLastVisiblePosition() - 1) {
					AlertDialog.Builder builder = new Builder(
							OrgModuleActivity.this);
					View view2 = View.inflate(OrgModuleActivity.this,
							R.layout.people_alertdialog_module, null);
					builder.setView(view2);
					create = builder.create();
					alertdialog_Tv = (TextView) view2
							.findViewById(R.id.alertdialog_module_Tv);
					alertdialog_Yes = (TextView) view2
							.findViewById(R.id.alertdialog_module_Yes);
					alertdialog_No = (TextView) view2
							.findViewById(R.id.alertdialog_module_No);
					alertdialog_Et = (EditText) view2
							.findViewById(R.id.alertdialog_module_Et);
					alertdialog_Tv.setText("添加新的模块");
					alertdialog_Yes.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String text = alertdialog_Et.getText().toString()
									.trim();
							if (!TextUtils.isEmpty(text)) {
								if (Module_list.contains(text)) {
									Toast.makeText(context, "模块名称重复,不可多次添加", 1)
											.show();
									return;
								}
								Module_list.add(
										gridview.getLastVisiblePosition() - 1,
										text);
								list_more.add(text);
								save.name = text;
								save.orgId = column_re2.orgId;
								save.orgType =  column_re2.templateId+"";
							} else {
								Module_list.add("自定义模块");
								save.name = "自定义模块";
								save.orgId = column_re2.orgId;
								save.orgType =  column_re2.templateId+"";
							}
							
							if (adapter != null
									&& adapter instanceof BaseAdapter) {
								adapter.notifyDataSetChanged();
							}
							create.dismiss();
							if (save != null) {
								OrganizationReqUtil.doRequestWebAPI(OrgModuleActivity.this, OrgModuleActivity.this, save, null,
										OrganizationReqType.COLUMN_SAVE);
							}
							
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
				// 管理模块
				else if (position == gridview.getLastVisiblePosition()) {
					if (!list_more.isEmpty()) {
						for (int i = 0; i < list_more.size(); i++) {
							final String string = list_more.get(i);
							Set<CheckBox> keySet = MyCheckMap.keySet();
							for (CheckBox checkBox : keySet) {
								if (string.equals(MyCheckMap.get(checkBox))) {
									checkBox.setBackgroundResource(R.drawable.people_column_delete);
									checkBox.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											delete.columnId = s;
											if (delete != null) {
												OrganizationReqUtil.doRequestWebAPI(OrgModuleActivity.this, OrgModuleActivity.this, delete, null,
														OrganizationReqType.COLUMN_DELETE);
											}
											list_more.remove(string);
											Module_list.remove(string);
											if (adapter != null
													&& adapter instanceof BaseAdapter) {
												adapter.notifyDataSetChanged();
											}
										}
									});
								}
							}

						}
					}

				} else {
					RelativeLayout rl = (RelativeLayout) parent
							.getChildAt(position);
					LinearLayout layout = (LinearLayout) rl.getChildAt(0);
					CheckBox box = (CheckBox) layout.getChildAt(1);
					// CheckBox checkBox = map.get(position);
//					int size = layout.getChildCount();
//					System.out.println("-----------checkbox size="+size);
					if (box.isChecked()) {
						box.setChecked(false);
						org_details_Etv.getCheckbox_cb().setChecked(false);	// check all add by ww 2015-04-10
					} else {
						box.setChecked(true);
						// add by ww 2015-04-10
						Set<CheckBox> keySet1 = MyCheck_Gv.keySet();
						int i=0;
						for(CheckBox chk : keySet1){
							if(chk.isChecked()){
								i++;
							}
						}
						if(i == keySet1.size()){
							org_details_Etv.getCheckbox_cb().setChecked(true);
						}
					}
				}

			}
		});
	}

	private void init() {
		quit_module_Rl = (RelativeLayout) findViewById(R.id.quit_module_Rl);
		org_details_Etv = (MyEditTextView) findViewById(R.id.org_details_Etv);
		module_Lv = (BasicListView2) findViewById(R.id.module_Lv);
		org_module_Ll = (LinearLayout) findViewById(R.id.org_module_Ll);
		check = (CheckBox) findViewById(R.id.checkbox_cb);
		gridview = new MyGridView(OrgModuleActivity.this);
		gridview.setNumColumns(2);
		gridview.setPadding((int) Utils.convertDpToPixel(10),
				(int) Utils.convertDpToPixel(10),
				(int) Utils.convertDpToPixel(10),
				(int) Utils.convertDpToPixel(10));
		gridview.setVerticalSpacing((int) Utils.convertDpToPixel(10));
		gridview.setHorizontalSpacing((int) Utils.convertDpToPixel(10));
		org_module_Ll.addView(gridview, org_module_Ll
				.indexOfChild(org_details_Etv) + 1,
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));

		onclicklistener();
	}

	private void onclicklistener() {
		quit_module_Rl.setOnClickListener(this);
		org_details_Etv.setOnClickListener(this);
		check.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_module_Rl:
			finish();
			break;
		case R.id.org_details_Etv:
			boolean checked2 = org_details_Etv.getCheckbox_cb().isChecked();
			org_details_Etv.getCheckbox_cb().setChecked(!checked2);
			Set<CheckBox> keySet = MyCheckMap.keySet();
			for (CheckBox checkBox : keySet) {
				checkBox.setChecked(!checked2);
			}
			break;
		case R.id.checkbox_cb:
			boolean checked = org_details_Etv.getCheckbox_cb().isChecked();
			Set<CheckBox> keySet1 = MyCheckMap.keySet();
			for (CheckBox checkBox : keySet1) {
				checkBox.setChecked(checked);
			}
			break;

		default:
			break;
		}

	}

	class ModuleAdapter extends BaseAdapter {

		private ViewHolder holder;

		@Override
		public int getCount() {
			return modules.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(OrgModuleActivity.this,
						R.layout.people_list_item_module, null);
				holder.module_Tv = (TextView) convertView
						.findViewById(R.id.module_Tv);
				holder.module_Cb = (CheckBox) convertView
						.findViewById(R.id.module_Cb);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.module_Tv.setText(modules.get(position));
			holder.module_Cb.setFocusable(false);
			holder.module_Cb.setClickable(false);
			Boolean ischecked = map_Lv.get(position+1);		// add by ww 2015-04-10 (position+1)
			if (ischecked == null) {
				holder.module_Cb.setChecked(false);
			} else {
				holder.module_Cb.setChecked(ischecked);
			}
			if ("1".equals(columnVo2.results.get(position+1).isSelect)) {		// add by ww 2015-04-10 (position+1)
				holder.module_Cb.setChecked(true);
			} else {
				holder.module_Cb.setChecked(false);
			}
			System.out.println("---------------position="+position);
			System.out.println(columnVo2.results.get(position).name);
			MyCheck_Lv
					.put(holder.module_Cb, columnVo2.results.get(position+1).id);	// add by ww 2015-04-10 (position+1)
			Map_Lv.put(holder.module_Cb, modules.get(position));	// add by ww 2015-04-10 (position+1)
			return convertView;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}

	public static class ViewHolder {
		CheckBox module_Cb;
		TextView module_Tv;
	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case OrganizationReqType.ORGANIZATION_CUSTOMER_COLUMNLIST:// 获取客户类型下的模块
			Map<String, Object> data = (Map<String, Object>) object;
			CustomerColumnVo columnVo = (CustomerColumnVo) data
					.get("customerColumn");
			if (columnVo != null) {
				columnVo2 = columnVo;
				if (columnVo.success) {
					System.out.println(columnVo.results.size());
					for (int i = 0; i < columnVo.results.size(); i++) {
						Results results = columnVo.results.get(i);
						System.out.println(results.name);
						if ("组织详情".equals(results.name)) {
							if (!results.child.isEmpty()) {
								for (int j = 0; j < results.child.size(); j++) {
									String name = results.child.get(j).name;
									Module_list.add(name);
								}
								Module_list.add(" ");
								Module_list.add(" ");
							}
						} else {
							modules.add(results.name);
						}
					}
					BaseAdapter adapter_Lv = (BaseAdapter) module_Lv
							.getAdapter();
					if (adapter_Lv != null) {
						adapter_Lv.notifyDataSetChanged();
					}
					BaseAdapter adapter_Gv = (BaseAdapter) gridview
							.getAdapter();
					if (adapter_Gv != null) {
						adapter_Gv.notifyDataSetChanged();
					}
				}
				System.out.println(columnVo.toString());
			}

			break;
		case OrganizationReqType.COLUMN_SAVE:
			s = (Long) object;
			System.out.println( s+"vvv22v2v2v2v2");
			break;
		default:
			break;
		}

	}
}
