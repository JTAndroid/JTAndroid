package com.tr.ui.people.cread.view;

import com.tr.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
/**
 * 模块控件
 * @author Wxh07151732
 *
 */
public class MyitemView extends LinearLayout {

	private ImageView addmore_myitemview_iv;
	private TextView myitemview_Tv;
	private LinearLayout myitemview_Ll;
	private String text_MyitemView_label;
	private boolean edit;
	private ListView myitemview_Lv;
	private LinearLayout ll_zong;
	private ImageButton iv_del;

	public MyitemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		 TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.MyitemView);   
		 text_MyitemView_label = typedArray.getString(R.styleable.MyitemView_text_MyitemView_label);
		 edit = typedArray.getBoolean(R.styleable.MyitemView_edit, false);
		 init(context);
	}

	public MyitemView(Context context) {
		super(context);
		 init(context);
	}

	public String getText_MyitemView_label() {
		return text_MyitemView_label;
	}

	public ListView getMyitemview_Lv() {
		return myitemview_Lv;
	}

	public void setMyitemview_Lv(ListView myitemview_Lv) {
		this.myitemview_Lv = myitemview_Lv;
	}

	public void setText_MyitemView_label(String text_MyitemView_label) {
		this.text_MyitemView_label = text_MyitemView_label;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}
	public void setText_label(String input){
		myitemview_Tv.setText(input);
	}
	public String getText_label(){
		return myitemview_Tv.getText().toString().trim();
	}
	
	private void init(Context context) {
		View view = View.inflate(context, R.layout.people_weiget_myitemview, null);
		myitemview_Ll = (LinearLayout) view.findViewById(R.id.myitemview_Ll);
		myitemview_Tv = (TextView) view.findViewById(R.id.myitemview_Tv);
		//myitemview_Lv = (ListView) view.findViewById(R.id.myitemview_Lv);
		addmore_myitemview_iv = (ImageView) view.findViewById(R.id.myitemview_Iv);
		ll_zong = (LinearLayout) view.findViewById(R.id.ll_zong);
		iv_del = (ImageButton) view.findViewById(R.id.iv_del);
		iv_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ll_zong.setVisibility(View.GONE);
			}
		});
		
		
		if (!TextUtils.isEmpty(text_MyitemView_label)) {
			myitemview_Tv.setText(text_MyitemView_label);
		}
		this.addView(view,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));  
	}

}
