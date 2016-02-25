package com.tr.ui.widgets;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.navigate.ENavigate;
import com.utils.common.EUtil;
import com.utils.common.GlobalVariable;
import com.utils.time.TimeUtil;

public class GodCreatedPopupWindow extends PopupWindow implements
OnClickListener, OnItemClickListener {

	private Context mContext;
	private String IMAGE_ITEM = "imgage_item";
	private TextView famous_quotes_tv;
	private SharedPreferences.Editor firstuse_edtior_time;
	private SharedPreferences firstuse_time;
	private Calendar nowCalendar;
	private TextView date_tv,famous_quotes_week,famous_quotes_ChineseLunar,famous_quotes_td;

	public GodCreatedPopupWindow(Context context) {
		// 设置布局的参数
		this(context, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	public GodCreatedPopupWindow(Context context, int width, int height) {
		this.mContext = context;
		// 设置可以获得焦点
		setFocusable(true);
		// 设置弹窗内可点击
		setTouchable(true);
		// 设置弹窗外可点击
		setOutsideTouchable(true);

		// 设置弹窗的宽度和高度
		setWidth(width);
		setHeight(height);
		ColorDrawable dw = new ColorDrawable(0000000);
		setBackgroundDrawable(dw);

		// 设置弹窗的布局界面
		setContentView(LayoutInflater.from(mContext).inflate(
				R.layout.popup_god_created, null));
		setAnimationStyle(R.style.AnimGodCreatedPopu);
		Resources res = context.getResources();
		String[] famousQuotesArr = res.getStringArray(R.array.famous_quotes);
		/* 记录第一次登陆的时间 */
		firstuse_time = mContext.getSharedPreferences(
				GlobalVariable.SHARED_PREFERENCES_FIRST_LOGIN_TIME,
				mContext.MODE_PRIVATE);
		firstuse_edtior_time = firstuse_time.edit();
		initUI();
		String loginTimeString = firstuse_time.getString(
				GlobalVariable.MAIN_FIRST_TIME, "");
		nowCalendar = Calendar.getInstance();
		Random random = new Random();
		if (loginTimeString.equals("") || !TimeUtil.isSameDay(nowCalendar.getTime(), loginTimeString)) {
			int length = famousQuotesArr.length;
			int index = random.nextInt(length);
			String famous_quotes_tv_contentString = famousQuotesArr[index];
			famous_quotes_tv.setText(famous_quotes_tv_contentString);
			firstuse_edtior_time.putInt(
					GlobalVariable.MAIN_FIRST_FAMOUS_QUOTES_INDEX, index);
			firstuse_edtior_time.putString(GlobalVariable.MAIN_FIRST_TIME,TimeUtil.TimeMillsToStringWithMinute(nowCalendar.getTimeInMillis()));
			firstuse_edtior_time.commit();
		} else {
			int length = famousQuotesArr.length;
			int indexRandom = random.nextInt(length);
			int index = firstuse_time.getInt(
					GlobalVariable.MAIN_FIRST_FAMOUS_QUOTES_INDEX, indexRandom);
			firstuse_edtior_time.putInt(
					GlobalVariable.MAIN_FIRST_FAMOUS_QUOTES_INDEX, index);
			firstuse_edtior_time.commit();
			famous_quotes_tv.setText(famousQuotesArr[index]);
		}
		date_tv.setText(EUtil.getData());
		famous_quotes_week.setText(EUtil.getWeek(mContext,nowCalendar));
		famous_quotes_ChineseLunar.setText(EUtil.getNoLi(1, nowCalendar));
		famous_quotes_td.setText(EUtil.getNoLi(2, nowCalendar));
	}

	/**
	 * 初始化弹窗列表
	 */
	private void initUI() {
		ImageView image_close = (ImageView) getContentView().findViewById(
				R.id.image_close);
		image_close.setOnClickListener(this);
		GridView mGridView = (GridView) getContentView().findViewById(
				R.id.grid_create_model);
		SimpleAdapter saImageItems = new SimpleAdapter(mContext,
				getGridViewData(), R.layout.gridview_creat_item,
				new String[] { IMAGE_ITEM }, new int[] { R.id.image_item_find });
		// 设置GridView的adapter。GridView继承于AbsListView。
		mGridView.setAdapter(saImageItems);
		mGridView.setOnItemClickListener(this);

		famous_quotes_tv = (TextView) getContentView().findViewById(
				R.id.famous_quotes_tv);
		date_tv = (TextView) getContentView().findViewById(R.id.famous_quotes_time);
		famous_quotes_week = (TextView) getContentView().findViewById(R.id.famous_quotes_week);
		famous_quotes_ChineseLunar = (TextView) getContentView().findViewById(R.id.famous_quotes_ChineseLunar);
		famous_quotes_td = (TextView) getContentView().findViewById(R.id.famous_quotes_td);
	}

	/**
	 * 获取GridView的数据
	 */
	private List<HashMap<String, Object>> getGridViewData() {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		// int[] arrImages = new int[] { R.drawable.icon_level,
		// R.drawable.icon_level, R.drawable.icon_level, R.drawable.icon_level,
		// R.drawable.icon_level, R.drawable.icon_level, R.drawable.icon_level,
		// R.drawable.icon_level };
		int[] arrImages = new int[] { R.drawable.icon_dynamic,
				R.drawable.icon_chat, R.drawable.icon_affair,
				R.drawable.icon_activity, R.drawable.icon_contacts,
				R.drawable.icon_org, R.drawable.icon_konwledge,
				R.drawable.icon_demand };
		for (int i = 0; i < arrImages.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(IMAGE_ITEM, arrImages[i]);
			list.add(map);
		}

		return list;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.image_close:// 关闭
			dismiss();
			break;

		default:
			break;
		}
	}

	/**
	 * 显示弹窗列表界面
	 */
	public void show() {
		showAtLocation(getContentView(), Gravity.BOTTOM, 0, 0);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long rowid) {
		switch (position) {
		case 0:// 动态
			dismiss();
			ENavigate.startCreateFlowActivity((Activity) mContext,
					App.getUserID());
			break;
		case 1:// 畅聊
			dismiss();
			ENavigate.startIMRelationSelectActivity((Activity) mContext, null,
					null, 0, null, null);
			break;
		case 2:// 事物
			dismiss();
			ENavigate.startNewAffarActivity((Activity) mContext);
			break;
		case 3:// 活动
			dismiss();
			ENavigate.startInitiatorHYActivity(mContext);
			break;
		case 4:// 人脉
			dismiss();
			ENavigate.startNewConnectionsActivity(mContext, 1, null, 9);
			break;
		case 5:// 组织
			dismiss();
			ENavigate.startOrganizationActivity(mContext);
			break;
		case 6:// 知识
			dismiss();
			ENavigate.startCreateKnowledgeActivity(mContext);
			break;
		case 7:// 需求
			dismiss();
			ENavigate.startDemandActivityForResult((Activity) mContext, 999);
			break;

		default:
			break;
		}
	}

}
