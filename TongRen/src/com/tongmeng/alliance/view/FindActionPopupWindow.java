package com.tongmeng.alliance.view;

import java.util.ArrayList;
import java.util.List;

import com.tongmeng.alliance.adapter.PopupWindowAdapter;
import com.tr.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

public class FindActionPopupWindow extends PopupWindow {

	View view;

	public FindActionPopupWindow(final Activity activity,List<String> list,
			final int typeID, final Handler handler) {

		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		view = inflater.inflate(R.layout.findaction_popuplist, null);
		ListView listview = (ListView) view
				.findViewById(R.id.findaction_pop_list);
		PopupWindowAdapter adapter = new PopupWindowAdapter(activity,list);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,  android.R.layout.simple_expandable_list_item_1, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				msg.setData(bundle);
				msg.what = typeID;
				handler.sendMessage(msg);
				dismiss();
			}
		});

		int h = activity.getWindowManager().getDefaultDisplay().getHeight();
		int w = activity.getWindowManager().getDefaultDisplay().getWidth();
		// 设置SelectPicPopupWindow的View
		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		this.setBackgroundDrawable(dw);
		// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimationPreview);
	}

	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			System.out.println(parent.getLayoutParams().width);
			System.out.println(parent.getLayoutParams().height);
			this.showAsDropDown(parent, parent.getLayoutParams().width/2, 18);
//			this.showAsDropDown(parent, 18, 18);
		} else {
			this.dismiss();
		}
	}
}
