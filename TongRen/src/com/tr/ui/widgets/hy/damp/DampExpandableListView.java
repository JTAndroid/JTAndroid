package com.tr.ui.widgets.hy.damp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;

public class DampExpandableListView extends ExpandableListView {
	private final static int MAX_Y_OVERSCROLL_PARAM = 200;
	private Context mContext;
	private int mMaxYOverscrollDistance;
	public DampExpandableListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	public DampExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	public DampExpandableListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}
	private void init(){
		final DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		mMaxYOverscrollDistance = (int)(dm.density * MAX_Y_OVERSCROLL_PARAM);
	}
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, 
			int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, 
			boolean isTouchEvent){
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, 
				scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
	}
}
