package com.tr.ui.conference.square;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 
 * @author sunjianan
 *
 */
public class MeetingGridView extends GridView{

	public MeetingGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MeetingGridView(Context context) {
		super(context);
	}

	public MeetingGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
