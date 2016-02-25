/*
 * create by roffee 
 */
package com.tr.ui.widgets.hy.damp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

public class DampListView extends ListView {
	private final static int MAX_Y_OVERSCROLL_PARAM = 200;
	private int maxYOverscrollDistance;
	public DampListView(Context context) {
		super(context);
		init();
	}
	public DampListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public DampListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	private void init(){
		final DisplayMetrics dm = this.getResources().getDisplayMetrics();
		maxYOverscrollDistance = (int)(dm.density * MAX_Y_OVERSCROLL_PARAM);
	}
	@SuppressLint("NewApi")
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, 
			int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, 
			boolean isTouchEvent){
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, 
				scrollRangeX, scrollRangeY, maxOverScrollX, maxYOverscrollDistance, isTouchEvent);
	}
}
