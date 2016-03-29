package com.tongmeng.alliance.util;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

public class SoftKeyboardUtil {
    public static final String TAG = "SoftKeyboardUtil";

    public static void observeSoftKeyboard(Activity activity, final OnSoftKeyboardChangeListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
            	
            	Rect r = new Rect();
				decorView.getWindowVisibleDisplayFrame(r);

				int screenHeight = decorView.getRootView().getHeight();
				int heightDifference = screenHeight
						- (r.bottom - r.top);

            	
//                Rect rect = new Rect();
//                decorView.getWindowVisibleDisplayFrame(rect);
//                int displayHeight = rect.bottom - rect.top;
//                int height = decorView.getRootView().getHeight();
                boolean hide = (double) (r.bottom-r.top) / screenHeight > 0.8;
//                if (Log.isLoggable(TAG, Log.DEBUG)) {
//                    Log.d(TAG, "DecorView display hight = " + displayHeight);
//                    Log.d(TAG, "DecorView hight = " + height);
//                    Log.d(TAG, "softkeyboard visible = " + !hide);
//                }

                listener.onSoftKeyBoardChange(heightDifference, !hide);

            }
        });
    }

    /**
     * Note: if you change layout in this method, maybe this method will repeat because the ViewTreeObserver.OnGlobalLayoutListener will repeat So maybe you need do some handle(ex: add some flag to avoid repeat) in this callback
     * 
     * Example:
     * 
     * private int previousHeight = -1; private void setupKeyboardLayoutWhenEdit() { SoftKeyboardUtil.observeSoftKeyBoard(this, new OnSoftKeyboardChangeListener() {
     * 
     * @Override public void onSoftKeyBoardChange(int softkeybardHeight, boolean visible) { if (previousHeight == softkeybardHeight) { return; } previousHeight = softkeybardHeight; //code for change layout } }); }
     * 
     * 
     * 
     */
    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeybardHeight, boolean visible);
    }
}