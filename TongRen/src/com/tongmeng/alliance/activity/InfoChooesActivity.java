package com.tongmeng.alliance.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.tongmeng.alliance.dao.Label;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tongmeng.alliance.view.InfoDialog;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

public class InfoChooesActivity extends JBaseActivity{
	
	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;
	
	TextView  textview;
	GridView gridview;
	GAdapter adapter;
	AlertDialog dialog;
	ArrayList<String> chooseList;
	public static ArrayList<String> infoList = new ArrayList<String>();

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				SetAdapter();
				break;
			case 1:
				Toast.makeText(InfoChooesActivity.this, msg.obj + "", 0).show();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_choose);
		ArrayList<String> tempList = new ArrayList<String>();
		tempList = getIntent().getStringArrayListExtra("tagList");
		if (tempList == null || tempList.size() == 0) {
			chooseList = new ArrayList<String>();
		} else {
			chooseList = tempList;
		}

		gridview = (GridView) findViewById(R.id.activity_tag_choose_gridview);

		getListViewData();
	}
	
	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		KeelLog.e("ContactsMainPageActivity", "initJabActionBar");
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(R.layout.org_firstpage_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("报名表标签");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("确定");
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isContainsName = false;
				boolean isContainsSex = false;

				for (String str : chooseList) {
					if (str.equals("姓名")) {
						isContainsName = true;
					}
					if (str.equals("性别")) {
						isContainsSex = true;
					}
				}
				
				KeelLog.e(TAG, "isContainsName:"+isContainsName+",isContainsSex:"+isContainsSex);
				if ((isContainsName && isContainsSex)
						|| (!isContainsName && !isContainsSex)) {
					Intent intent = new Intent();
					intent.putStringArrayListExtra("infoList", chooseList);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Toast.makeText(InfoChooesActivity.this, "姓名和性别是双选字段", 0).show();
				}
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}
	
	private void getListViewData() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				// String typeUrl =
				// "http://192.168.101.140:8801/label/searchApply.json";
				// String typeParam = "";
				String result = HttpRequestUtil.sendPost(Constant.tagPath, "",
						InfoChooesActivity.this);
				KeelLog.e(TAG, "获取报名信息result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				
				if (dao.getNotifyCode().equals("0001")) {
					if (dao.getResponseData() == null
							|| "".equals(dao.getResponseData())) {
						Message message = new Message();
						message.what = 1;
						message.obj = "获取报名信息列表失败";
						handler.sendMessage(message);
					} else {
						infoList = getLabelList(dao.getResponseData());
						if (infoList == null || infoList.size() == 0) {
							Message message = new Message();
							message.what = 1;
							message.obj = "获取报名信息列表失败";
							handler.sendMessage(message);
						} else {
							infoList.add("自定义");
							handler.sendEmptyMessage(0);
						}
					}
				} else {
					Message message = new Message();
					message.what = 1;
					message.obj = "获取报名信息列表失败，失败原因：" + dao.getNotifyInfo()
							+ "，请重试！";
					handler.sendMessage(message);
				}
				// infoList = Utils.message.what = 1;
				// message.obj = "获取报名信息列表失败";
				// handler.sendMessage(message);(result);
				// if (infoList == null || infoList.size() == 0) {
				// handler.sendEmptyMessage(1);
				// } else {
				// infoList.add("自定义");
				// handler.sendEmptyMessage(0);
				// }
			};
		}.start();
	}

	private void SetAdapter() {
		adapter = new GAdapter();

		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == infoList.size() - 1) {
					Intent intetn = new Intent(InfoChooesActivity.this,
							InfoDialog.class);
					startActivityForResult(intetn, 1);
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.activity_info_choose_item2_text);
					if (tv.getCurrentTextColor() == Color.parseColor("#4ea3cc")) {
						tv.setTextColor(Color.parseColor("#ffffff"));
						tv.setBackgroundResource(R.drawable.bg_field2);
						chooseList.add(infoList.get(position));
					} else if (tv.getCurrentTextColor() == Color
							.parseColor("#ffffff")) {
						tv.setTextColor(Color.parseColor("#4ea3cc"));
						tv.setBackgroundResource(R.drawable.bg_field1);
						chooseList.remove(infoList.get(position));
					}
				}
			}
		});
	};
	
	private class GAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return infoList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.e("", "position:" + position + "--------infoList.size():"
					+ infoList.size());
			if (convertView == null) {
				convertView = LayoutInflater.from(InfoChooesActivity.this)
						.inflate(R.layout.activity_info_choose_item2, null);
				final TextView tv = (TextView) convertView
						.findViewById(R.id.activity_info_choose_item2_text);
				if (position == infoList.size() - 1) {
					tv.setBackgroundResource(R.drawable.bg_field_add);
				} else {
					tv.setText(infoList.get(position));
				}
				if (chooseList.size() != 0
						&& chooseList.contains(infoList.get(position))) {
					tv.setTextColor(Color.parseColor("#ffffff"));
					tv.setBackgroundResource(R.drawable.bg_field2);
				}
			}
			return convertView;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK) {
			String s = data.getStringExtra("custom");
			if (infoList.contains(s)) {
				Toast.makeText(this, "已存在您定义的字段，请添加其他字段", 0).show();
			} else {
				infoList.remove(infoList.size() - 1);
				infoList.add(s);
				infoList.add("自定义");
				adapter = new GAdapter();
				gridview.setAdapter(adapter);
			}
		}
	}
	
	public void creatDialog() {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.addinfo_dialog, null);
		final AlertDialog dialog = new AlertDialog.Builder(
				InfoChooesActivity.this).create();
		dialog.show();
		dialog.setContentView(contentView);
		final EditText edit = (EditText) contentView
				.findViewById(R.id.addinfo_text);
		Button cancleBtn = (Button) contentView
				.findViewById(R.id.addinfo_cancel);
		Button okBtn = (Button) contentView.findViewById(R.id.addinfo_ok);
		cancleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = edit.getText().toString();
				if (str.equals("字段名称不超过6个汉字")) {
					Toast.makeText(InfoChooesActivity.this, "请输入您要添加的字段", 0)
							.show();
				} else if (str.length() > 6) {
					Toast.makeText(InfoChooesActivity.this, "您输入的字段过长", 0)
							.show();
				} else if (infoList.contains(str)) {
					Toast.makeText(InfoChooesActivity.this,
							"已存在您定义的字段，请添加其他字段", 0).show();
				} else {
					infoList.remove(infoList.size() - 1);
					infoList.add(str);
					infoList.add("自定义");
					adapter.notifyDataSetChanged();
					dialog.dismiss();
				}
			}
		});
	}
	
	public ArrayList<String> getLabelList(String responseData) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			JSONObject job = new JSONObject(responseData);
			JSONArray arr = job.getJSONArray("labelList");
			Gson gson = new Gson();
			for (int i = 0; i < arr.length(); i++) {
				Label label = gson.fromJson(arr.opt(i).toString(), Label.class);
				KeelLog.e(TAG, "label::" + label.toString());
				list.add(label.getName().trim());
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
