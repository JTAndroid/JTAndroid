package com.tr.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class ExpandableListViewForScrollView extends ExpandableListView {

	public ExpandableListViewForScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ExpandableListViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public ExpandableListViewForScrollView(Context context,AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 重写该方法，达到使ExpandableListView适应ScrollView的效果
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
