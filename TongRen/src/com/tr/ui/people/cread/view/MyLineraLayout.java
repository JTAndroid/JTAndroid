package com.tr.ui.people.cread.view;

import com.tr.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
/**
 * 线性布局（添加子模块）
 * @author Wxh07151732
 *
 */
public class MyLineraLayout extends LinearLayout {

	private LinearLayout myLineraLaout_Ll;

	public MyLineraLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public LinearLayout getMyLineraLaout_Ll() {
		return myLineraLaout_Ll;
	}

	public void setMyLineraLaout_Ll(LinearLayout myLineraLaout_Ll) {
		this.myLineraLaout_Ll = myLineraLaout_Ll;
	}

	private void init(Context context) {
		View view = View.inflate(context, R.layout.people_weiget_mylineralayout, null);
		myLineraLaout_Ll = (LinearLayout) view.findViewById(R.id.myLineraLaout_Ll);
		this.addView(view,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));  
	}

	public MyLineraLayout(Context context) {
		super(context);
		init(context);
	}
	
}
