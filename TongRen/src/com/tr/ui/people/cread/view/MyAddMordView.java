package com.tr.ui.people.cread.view;

import com.tr.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 新增控件
 * @author John
 *
 */
public class MyAddMordView extends LinearLayout {

	private TextView continueadd_Tv;
	private String text_MyAddMordView_label;
	public MyAddMordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		 TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.MyAddMordView);   
		 text_MyAddMordView_label = typedArray.getString(R.styleable.MyAddMordView_text_MyAddMordView_label);
		init(context);
	}

	public MyAddMordView(Context context) {
		super(context);
		init(context);
	}
	public void setTextLabel(String text){
		continueadd_Tv.setText("新增"+text);
	}
	private void init(Context context) {
		View view = View.inflate(context, R.layout.people_include_continueadd, null);
		continueadd_Tv = (TextView) view.findViewById(R.id.continueadd_Tv);
		if (!TextUtils.isEmpty(text_MyAddMordView_label)) {
			continueadd_Tv.setText("新增"+text_MyAddMordView_label);
		}
		this.addView(view);
	}
}
