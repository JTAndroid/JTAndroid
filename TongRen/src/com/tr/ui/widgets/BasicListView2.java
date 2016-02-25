package com.tr.ui.widgets;


import com.utils.log.KeelLog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;



public class BasicListView2 extends ListView {
	

	/**
	 * @param context
	 */
	public BasicListView2(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public BasicListView2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public BasicListView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void layoutChildren() {
		
		try {
			super.layoutChildren();
		} catch (Exception e) {
			if (KeelLog.DEBUG) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
				super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
