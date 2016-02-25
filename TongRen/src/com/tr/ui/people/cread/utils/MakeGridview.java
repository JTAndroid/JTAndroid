package com.tr.ui.people.cread.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.tr.R;
import com.tr.ui.people.cread.HobbyActivity;
import com.tr.ui.people.cread.PhysicalActivity;
import com.tr.ui.people.cread.MoreModuleActivity.ViewHolder;
import com.tr.ui.people.cread.view.MyEditTextView;

import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MakeGridview {
	private static HashMap<Integer, Boolean> map;
	/**
	 * 填充GridviewAdapter
	 * @param context 上下文
	 * @param grid   所需填充数据的Gridview
	 * @param list   被填充的数据
	 */
	public static void makeGridviewAdapter(final Context context ,final GridView grid,final ArrayList<String> list){
		grid.setAdapter(new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final TextView text = new TextView(context);
				text.setText(list.get(position));
				text.setGravity(Gravity.CENTER);
				Boolean isclick = map.get(position);
				if (isclick==null) {
					text.setBackgroundResource(R.drawable.people_label_bg_default);
				}else{
					if (!isclick) {
						text.setBackgroundResource(R.drawable.people_label_bg_default);
					}
					else{
						text.setBackgroundResource(R.drawable.people_label_bg_click);
					}
				}
				return text;
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
				return list.size();
			}
		});
		
	}
	
	/**
	 * 动态显示对话框
	 * @param context  上下文
	 * @param grid    所需显示对话框的GridView
	 * @param list    数据集合
	 * @return
	 */
	public static ArrayList<String> makeGridviewAlertDialog(final Context context ,final GridView grid,final ArrayList<String> list){
		map = new HashMap<Integer, Boolean>();
		final ArrayList<String> GridviewAlert_list = new ArrayList<String>();
		grid.setOnItemClickListener(new OnItemClickListener() {

			private EditText alertdialog_Et;
			private TextView alertdialog_No;
			private TextView alertdialog_Yes;
			private AlertDialog create;
			private TextView alertdialog_Tv;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final BaseAdapter adapter = (BaseAdapter) parent.getAdapter();
				if (position==grid.getLastVisiblePosition()) {
					AlertDialog.Builder builder = new Builder(context);
					View view2 = View.inflate(context, R.layout.people_alertdialog_title, null);
					builder.setView(view2);
					create = builder.create();
					alertdialog_Tv = (TextView) view2.findViewById(R.id.alertdialog_Tv);
					alertdialog_Yes = (TextView) view2.findViewById(R.id.alertdialog_Yes);
					alertdialog_No = (TextView) view2.findViewById(R.id.alertdialog_No);
					alertdialog_Et = (EditText) view2.findViewById(R.id.alertdialog_Et);
						
					alertdialog_Yes.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String text = alertdialog_Et.getText().toString().trim();
							if (!TextUtils.isEmpty(text)) {
								GridviewAlert_list.add(text);
								
							}
							list.add(list.size()-1,text);
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
				TextView childAt = (TextView) grid.getChildAt(position);
				Boolean isSelected = map.get(position);
				if (isSelected==null) {
					map.put(position, true);
					if (adapter != null && adapter instanceof BaseAdapter) {
						adapter.notifyDataSetChanged();
					}
					GridviewAlert_list.add(childAt.getText().toString().trim());
				}else{
					if (isSelected) {
						map.put(position, false);
						if (adapter != null && adapter instanceof BaseAdapter) {
							adapter.notifyDataSetChanged();
						}
						GridviewAlert_list.remove(childAt.getText().toString().trim());
					}else{
						map.put(position, true);
						if (adapter != null && adapter instanceof BaseAdapter) {
							adapter.notifyDataSetChanged();
						}
						GridviewAlert_list.add(childAt.getText().toString().trim());
					}
				}
				
			}
		});
		if (GridviewAlert_list.toString().contains("+")) {
			GridviewAlert_list.remove("+");
		}
		return GridviewAlert_list;
	}
}
