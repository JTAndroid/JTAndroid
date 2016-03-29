package com.tongmeng.alliance.view;

import com.tongmeng.alliance.activity.ActionScanActivity;
import com.tongmeng.alliance.activity.ContactsActivity;
import com.tongmeng.alliance.activity.FeedBackActivity;
import com.tongmeng.alliance.activity.ReleaseActivity;

import com.tr.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class MorePopupWindow extends PopupWindow {

	private View contentView;
	LinearLayout actionLayout, addLayout, scanLayout, feedbackLayout;
	Intent intent = new Intent();
	Activity activity;

	public MorePopupWindow(final Activity activity) {
		this.activity = activity;
		
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(R.layout.more_pupupwindow, null);
		actionLayout = (LinearLayout) contentView
				.findViewById(R.id.more_layout_action);
		addLayout = (LinearLayout) contentView
				.findViewById(R.id.more_layout_add);
		scanLayout = (LinearLayout) contentView
				.findViewById(R.id.more_layout_scan);
		feedbackLayout = (LinearLayout) contentView
				.findViewById(R.id.more_layout_feedback);
		actionLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.setClass(activity, ReleaseActivity.class);
				intent.putExtra("id", 0);
				activity.startActivity(intent);
				dismiss();
			}
		});
		addLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.setClass(activity, ContactsActivity.class);
				activity.startActivity(intent);
				dismiss();
			}
		});
		scanLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.setClass(activity, ActionScanActivity.class);
				intent.putExtra("activityId", "0");
				intent.putExtra("index", "more");
				intent.putExtra("role", "-1");
				activity.startActivity(intent);
				dismiss();
			}
		});
		feedbackLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.setClass(activity, FeedBackActivity.class);
				activity.startActivity(intent);
				dismiss();
			}
		});

		// 设置SelectPicPopupWindow的View
		this.setContentView(contentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		// this.setWidth(w / 2 + 50);
		 int h = contentView.getMeasuredHeight();
		 int w = contentView.getMeasuredWidth();
		Log.e("", "Width:"+w+"----Height:"+h);
		this.setWidth(w);
		// 设置SelectPicPopupWindow弹出窗体的高
		// this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setHeight(h);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
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
			this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
		} else {
			this.dismiss();
		}
	}
}
