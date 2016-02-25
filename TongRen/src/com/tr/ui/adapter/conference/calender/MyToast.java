package com.tr.ui.adapter.conference.calender;

import android.content.Context;
import android.widget.Toast;

public class MyToast extends Toast {
	public static long LastTime = 0;
	public static String textLast = "";
	private static Toast lastToast;

	public MyToast(Context context) {
		super(context);
	}

	public static final Toast makeTextFactory(Context context, String text,
			int duration) {
		Toast toast = null;
		if (textLast == null) {
			toast = makeText(context, text, duration);
		}
		if (textLast.equals(text)) {
			if (System.currentTimeMillis() - LastTime > 1000) {
				toast = makeText(context, text, duration);
			}
		} else {
			toast = makeText(context, text, duration);
		}
		if (toast != null) {
			LastTime = System.currentTimeMillis();
			textLast=text;
		
			if (lastToast != null) {
				lastToast.cancel(); 
			}
			
			lastToast = toast;
		} 
		
		
		return toast;
	}
}
