package com.tr.ui.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.tr.R;

public class UpLoadingDialog extends Dialog {

	public UpLoadingDialog(Context context) {
		super(context, R.style.upLoading_dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loading_dialog_layout);
		ImageView rocketImage = (ImageView) findViewById(R.id.loading);
		AnimationDrawable rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
		rocketAnimation.start();
		this.setCancelable(false);
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
