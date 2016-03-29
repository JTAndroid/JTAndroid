package com.tongmeng.alliance.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.LoadUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

public class TagChooseActivity extends JBaseActivity{

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;
	
	GridView gridview;
	GAdapter adapter;
	ArrayList<TextView> list = new ArrayList<TextView>();
	// public static ArrayList<String> ;
	ArrayList<String> infoList, chooseList;
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:
				// SetAdapter(isChanged);
				SetAdapter();
				break;
			case 1:
				Toast.makeText(TagChooseActivity.this, "获取标签列表失败", 0).show();
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
	
	private void SetAdapter() {
		// TODO Auto-generated method stub
		adapter = new GAdapter();
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (list != null && list.size() >= 3) {
					Toast.makeText(TagChooseActivity.this, "您只能选择三个标签", 0).show();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.activity_tag_choose_itemText);

					KeelLog.e("",
							"getCurrentTextColor::" + tv.getCurrentTextColor());
					if (tv.getCurrentTextColor() == Color.parseColor("#ffa656")) {// 当前字体颜色是蓝色，点击变为选中
						tv.setTextColor(Color.parseColor("#ffffff"));
						tv.setBackgroundResource(R.drawable.bg_tag2);

						if (list.contains(infoList.get(position))) {
							Log.e("", "已有" + infoList.get(position)
									+ "字段，不需要添加");
						} else {
							chooseList.add(infoList.get(position));
							list.add(tv);
						}
					} else if (tv.getCurrentTextColor() == Color
							.parseColor("#ffffff")) {
						tv.setTextColor(Color.parseColor("#ffa656"));
						tv.setBackgroundResource(R.drawable.bg_tag1);
						list.remove(tv);
						chooseList.remove(infoList.get(position));
					}

				}
			}
		});
	}

	private void getListViewData() {
		new Thread() {
			public void run() {
				String typeUrl = Constant.typePath;
				String typeParam = "";
				String result = HttpRequestUtil.sendPost(typeUrl, typeParam,
						TagChooseActivity.this);
				infoList = Utils.getListInfo(result);
				// infoList.add("自定义");
				if (infoList.size() == 0) {
					handler.sendEmptyMessage(1);
				} else {
					handler.sendEmptyMessage(0);
				}

			};
		}.start();
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
		myTitle.setText("设置标签");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("确定");
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (chooseList.size() > 3) {
					Toast.makeText(TagChooseActivity.this, "您选中的标签数已经大于3个", 0)
							.show();
				} else {

					Intent intent = new Intent();
					intent.putStringArrayListExtra("tagList", chooseList);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}

	private class GAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infoList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(TagChooseActivity.this)
						.inflate(R.layout.activity_tag_choose_item, null);
				final TextView tv = (TextView) convertView
						.findViewById(R.id.activity_tag_choose_itemText);
				tv.setText(infoList.get(position));
				if (chooseList.size() != 0
						&& chooseList.contains(infoList.get(position))) {
					tv.setTextColor(Color.parseColor("#ffffff"));
					tv.setBackgroundResource(R.drawable.bg_tag2);
					list.add(tv);
				}

			}
			return convertView;
		}

	}
}
