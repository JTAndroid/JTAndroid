package com.tr.ui.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tr.R;

public class EProgressDialog extends Dialog {

	private TextView rocketText;

	public EProgressDialog(Context context) {
		super(context, R.style.dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_loading);
		ImageView rocketImage = (ImageView) findViewById(R.id.loading);
		rocketText = (TextView) findViewById(R.id.loading_text);
		AnimationDrawable rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
		rocketAnimation.start();
//		this.setCancelable(false);
	}

	public void setMessage(String message) {
		rocketText.setText(message);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void show() {
		try {
			super.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			super.dismiss();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}
}
