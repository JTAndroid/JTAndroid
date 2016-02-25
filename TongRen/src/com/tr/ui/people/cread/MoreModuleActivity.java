package com.tr.ui.people.cread;

import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tr.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 选择更多模块
 */
public class MoreModuleActivity extends BaseActivity {
	private ListView module_lv;
	private ModuleAdapter adapter;
	public static final String[] modules = { "联系方式", "个人情况", "资源需求", "教育经历",
			"工作经历", "社会活动" };
	private TextView module_choose_Tv;
	private TextView confirm_Tv;
	private ViewHolder holder;
	private HashMap<String, Integer> module_map;
	private ArrayList<String> module_list;
	private HashMap<Integer, Boolean> map;
	private RelativeLayout quit_moreModule_Rl;
	private ArrayList<String> module;
	private HashMap<CheckBox, String> checkbox_map;
	private ArrayList<String> resource_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_moremodule);
		module = this.getIntent().getStringArrayListExtra("Module_List"); // 从基本信息页面来的数据，进行初始化
		resource_list = this.getIntent().getStringArrayListExtra("resource_list");
		
		
		module_list = new ArrayList<String>();

		quit_moreModule_Rl = (RelativeLayout) findViewById(R.id.quit_moreModule_Rl);
		module_choose_Tv = (TextView) findViewById(R.id.Module_choose_Tv);
		confirm_Tv = (TextView) findViewById(R.id.confirm_Tv);
		module_lv = (ListView) findViewById(R.id.module_lv);
		initData();
		map = new HashMap<Integer, Boolean>();
		module_map = new HashMap<String, Integer>();

		checkbox_map = new HashMap<CheckBox, String>();
		quit_moreModule_Rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		module_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
			
				if (position == 2) {
					Intent intent = new Intent(context, ResourceActivity.class);
					intent.putExtra("resource_list", resource_list);
					startActivityForResult(intent, 999);
				}else{
					holder = (ViewHolder) view.getTag();
					if (holder.module_Cb.isChecked()) {
						holder.module_Cb.setChecked(false);
						map.put(position, false);
					} else {
						holder.module_Cb.setChecked(true);
						map.put(position, true);
					}

					if (holder.module_Cb.isChecked()) {
						module_map.put(holder.module_Tv.getText().toString(),
								position);
					} else {
						module_map.remove(holder.module_Tv.getText().toString());
					}
				}

			}
		});
	}

	private void initData() {
		adapter = new ModuleAdapter();
		module_lv.setAdapter(adapter);
	}

	public void confirm(View view) {
		// Set<String> keySet = module_map.keySet();
		// for (String str : keySet) {
		// module_list.add(str);
		// }
		// if (module!=null) {
		// module_list.addAll(module);
		// }

		if (!checkbox_map.isEmpty()) {
			Set<CheckBox> check_set = checkbox_map.keySet();
			for (CheckBox checkBox : check_set) {
				String module = checkbox_map.get(checkBox);
				if (checkBox.isChecked()) {
					module_list.add(module);
				}
			}
		}
		Intent intent = new Intent(this, NewConnectionsActivity.class);
		intent.putStringArrayListExtra("module", module_list);
		intent.putStringArrayListExtra("resource_list", resource_list);
		setResult(99, intent);
		finish();
	}

	class ModuleAdapter extends BaseAdapter {

		private static final int Arraws = 0;
		private static final int Check_Box = 1;

		@Override
		public int getCount() {
			return modules.length;
		}
		@Override
		public int getItemViewType(int position) {
			if (position==2) {
				return Arraws;
			}else{
				return Check_Box;
			}
					
		}
		
		@Override
		public int getViewTypeCount() {
			return 2;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type = getItemViewType(position);

			
				if (convertView == null) {
					switch (type) {
					case Arraws:
						View view = View.inflate(context, R.layout.list_item_arrows, null);
						return view;
					default:
						holder = new ViewHolder();
						convertView = View.inflate(MoreModuleActivity.this,
								R.layout.people_list_item_module, null);
						holder.module_Tv = (TextView) convertView
								.findViewById(R.id.module_Tv);
						holder.module_Cb = (CheckBox) convertView
								.findViewById(R.id.module_Cb);
						convertView.setTag(holder);
						break;
					}
					
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				
				switch (type) {
				case Arraws:
					
					break;

				default:
					holder.module_Tv.setText(modules[position]);
					holder.module_Cb.setFocusable(false);
					holder.module_Cb.setClickable(false);
					Boolean ischecked = map.get(position);
					if (ischecked == null) {
						holder.module_Cb.setChecked(false);
					} else {
						holder.module_Cb.setChecked(ischecked);
					}
					if (module != null) {
						if (!module.isEmpty()) {
							if (module.contains(modules[position])) {
								holder.module_Cb.setChecked(true);
							}
						}
					}
					checkbox_map.put(holder.module_Cb, modules[position]);
					
					break;
				}
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode == 999) {
				resource_list = data.getStringArrayListExtra("resource_list");
				
			}
		}
	}
}
