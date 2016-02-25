package com.tr.ui.people.contactsdetails.dialog;

import com.tr.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;


public class EProgressDialogs extends Dialog {


	public EProgressDialogs(Context context) {
		super(context, R.style.dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.people_widget_loadings);
		ImageView rocketImage = (ImageView) findViewById(R.id.loading);
		AnimationDrawable rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
		rocketAnimation.start();
	}

	public void setMessage(String message) {

	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void show() {
		super.show();
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