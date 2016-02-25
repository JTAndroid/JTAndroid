package com.tr.ui.people.cread.view;

import com.tr.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
/**
 * 删除控件
 * @author John
 *
 */
public class MyDeleteView extends LinearLayout {

	public MyDeleteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public MyDeleteView(Context context) {
		super(context);
		init(context);
	}
	private void init(Context context) {
		View view = View.inflate(context, R.layout.people_include_delete, null);
		this.addView(view);
	}

}
