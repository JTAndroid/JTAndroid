package com.utils.log;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
	private static Toast mToast;

	@SuppressWarnings("static-access")
	public static void showToast(Context context, String text) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
//		mToast.setGravity(Gravity.CENTER_HORIZONTAL|(Gravity.CENTER_VERTICAL/2), 0, 0);
		mToast.show();
	}

	public static void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}
	}

}
