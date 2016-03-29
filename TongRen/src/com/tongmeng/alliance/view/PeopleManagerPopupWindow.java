package com.tongmeng.alliance.view;

import com.tr.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class PeopleManagerPopupWindow extends PopupWindow {
	View view;
	private View contentView;
	LinearLayout signedLayout, nosignedLayout, allLayout;

	public PeopleManagerPopupWindow(final Activity activity,
			final Handler handler) {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(R.layout.peoplemanagerpopupwindow, null);
		signedLayout = (LinearLayout) contentView
				.findViewById(R.id.peoplemanager_signed);
		nosignedLayout = (LinearLayout) contentView
				.findViewById(R.id.peoplemanager_unsigned);
		allLayout = (LinearLayout) contentView
				.findViewById(R.id.peoplemanager_all);

		signedLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
				dismiss();
			}
		});
		nosignedLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
				dismiss();
			}
		});

		allLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = 2;
				handler.sendMessage(msg);
				dismiss();
			}
		});

		this.setContentView(contentView);
		this.setWidth(252);
		this.setHeight(354);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
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
