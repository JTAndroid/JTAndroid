package com.tr.ui.people.cread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.tr.R;
import com.tr.ui.people.cread.MoreModuleActivity.ViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ResourceActivity extends BaseActivity {
	private ListView resource_Lv;
	private String[] resource = {"投资意向","融资意向","专家身份","专家需求"};
	private HashMap<Integer, Boolean> map;
	private HashMap<CheckBox, String> checkbox_map;
	private HashMap<String,Integer> module_map;
	private ArrayList<String> module;
	private ArrayList<String> resource_list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_resource);
		map = new HashMap<Integer, Boolean>();
		checkbox_map = new HashMap<CheckBox, String>();
		module_map = new HashMap<String, Integer>();
		module = new ArrayList<String>();
		resource_list = new ArrayList<String>();
		module = this.getIntent().getStringArrayListExtra("resource_list");
		init();
		initData();
	}

	private void initData() {
		ResourceAdapter adapter = new ResourceAdapter();
		resource_Lv.setAdapter(adapter);
		resource_Lv.setOnItemClickListener(new OnItemClickListener() {
			
			private ViewHolder holder;
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				holder = (ViewHolder) view.getTag();
				if (holder.module_Cb.isChecked()) {
						holder.module_Cb.setChecked(false);
						map.put(position, false);
				} else {
					holder.module_Cb.setChecked(true);
					map.put(position, true);
				}
				
				if (holder.module_Cb.isChecked()) {
					module_map.put(holder.module_Tv.getText().toString(),position);
				}else{
					module_map.remove(holder.module_Tv.getText().toString());
				}
				
				
			}
		});
	}
	public void  finishs(View v){
		if(!checkbox_map.isEmpty()){
			Set<CheckBox> check_set = checkbox_map.keySet();
			for (CheckBox checkBox : check_set) {
				String resource = checkbox_map.get(checkBox);
				if (checkBox.isChecked()) {
					resource_list.add(resource);
				}
				
			}
		}
		Intent intent = new Intent();
		intent.putExtra("resource_list", resource_list);
		setResult(RESULT_OK, intent);
		finish();
	}
	private void init() {
		RelativeLayout quit_resource_Rl = (RelativeLayout) findViewById(R.id.quit_resource_Rl);
		resource_Lv = (ListView) findViewById(R.id.resource_Lv);
		quit_resource_Rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	class ResourceAdapter extends BaseAdapter {

		private ViewHolder holder;
		

		@Override
		public int getCount() {
			return resource.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context,
						R.layout.people_list_item_module, null);
				holder.module_Tv = (TextView) convertView
						.findViewById(R.id.module_Tv);
				holder.module_Cb = (CheckBox) convertView
						.findViewById(R.id.module_Cb);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.module_Tv.setText(resource[position]);
			holder.module_Cb.setFocusable(false);
			holder.module_Cb.setClickable(false);
			Boolean ischecked = map.get(position);
			if (ischecked==null) {
				holder.module_Cb.setChecked(false);
			}else{
				holder.module_Cb.setChecked(ischecked);
			}
			if (module!=null) {
				if (!module.isEmpty()) {
					if (module.contains(resource[position])) {
						holder.module_Cb.setChecked(true);
					}
				}
			}
			checkbox_map.put(holder.module_Cb, resource[position]);
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

	public  class ViewHolder {
		CheckBox module_Cb;
		TextView module_Tv;
	}
	
}
