package com.tr.ui.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @ClassName:     KnoDetailsNoScrollGv.java
 * @Description:   知识详情页自定义GridView
 * @Author         CJJ
 * @Version        v 1.0  
 * @Created        2014-11-04
 * @Updated 	   2014-11-11
 */
public class KnoDetailsNoScrollGv extends GridView {
	public KnoDetailsNoScrollGv(Context context) {
		super(context);
	}

	public KnoDetailsNoScrollGv(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
