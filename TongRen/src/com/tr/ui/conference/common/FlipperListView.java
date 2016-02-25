package com.tr.ui.conference.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/*
 * 屏幕手势切换
 */
public class FlipperListView extends ListView {

	public FlipperListView(Context context) {
		super(context);
	}

	public FlipperListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FlipperListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}
}