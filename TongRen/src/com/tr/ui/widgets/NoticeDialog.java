package com.tr.ui.widgets;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.tr.R;

public class NoticeDialog {

	Context context;
	android.app.AlertDialog ad;
	TextView titleView;
	TextView messageView;
	LinearLayout buttonLayout;

	public NoticeDialog(Context context) {
		this.context = context;
		ad = new android.app.AlertDialog.Builder(context).create();
		ad.show();
		// 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = ad.getWindow();
		window.setContentView(R.layout.layout_notice_dialog);
		titleView = (TextView) window.findViewById(R.id.notice_title);
		messageView = (TextView) window.findViewById(R.id.notice_message);
		buttonLayout = (LinearLayout) window.findViewById(R.id.buttonLayout);
	}

	public void setTitle(int resId) {
		titleView.setText(resId);
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	public void setMessage(int resId) {
		messageView.setText(resId);
	}

	public void setMessage(String message) {
		messageView.setText(message);
	}

	/**
	 * 设置确定按钮
	 * 
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text, final View.OnClickListener listener) {
		TextView textView = new TextView(context);
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		textView.setLayoutParams(params);
		textView.setGravity(Gravity.CENTER);
		textView.setText(text);
		textView.setTextColor(Color.BLACK);
		textView.setTextSize(20);
		textView.setOnClickListener(listener);
		buttonLayout.addView(textView);
	}

	// /**
	// * 设置取消按钮
	// *
	// * @param text
	// * @param listener
	// */
	// public void setNegativeButton(String text, final View.OnClickListener
	// listener) {
	// TextView textView = new TextView(context);
	// LinearLayout.LayoutParams params = new
	// LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	// params.gravity=Gravity.CENTER_VERTICAL;
	// textView.setLayoutParams(params);
	// textView.setGravity(Gravity.CENTER);
	// textView.setText(text);
	// textView.setTextColor(Color.BLACK);
	// textView.setTextSize(20);
	// textView.setOnClickListener(listener);
	// if (buttonLayout.getChildCount() > 0) {
	// params.setMargins(20, 0, 0, 0);
	// textView.setLayoutParams(params);
	// buttonLayout.addView(textView, 1);
	// } else {
	// textView.setLayoutParams(params);
	// buttonLayout.addView(textView);
	// }
	// }

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		ad.dismiss();
	}
}
