package com.tr.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class CursorTraceableEditText extends EditText {

	public static final String TAG = CursorTraceableEditText.class.getSimpleName();
	
	public CursorTraceableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onSelectionChanged(int selStart, int selEnd) {
		// TODO Auto-generated method stub
		super.onSelectionChanged(selStart, selEnd);
		Log.d(TAG, "start:" + selStart + ",end:" + selEnd);
	}
}
