package com.tr.ui.widgets;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.R.color;
import com.tr.model.obj.UserComment;

/**
 * 个人主页评价 赞同 自定义布局
 * 
 * @author zhongshan
 * @since 2015-01-13
 * 
 */
public class DoubleTextViewTagLayout extends LinearLayout {
	public static final String TAG = "OrganizationBasicForm";

	private LinearLayout layout;
	// private TextView keyTv;
	private TextView numberTv;
	private Context context;
	private TextView contentTv;
	private ImageView deleteIv;
	private boolean isChecked;

	public DoubleTextViewTagLayout(Context context) {
		super(context);
	}

	public DoubleTextViewTagLayout(Context context, String content, String num, boolean isChecked) {
		super(context);
		this.context = context;
		this.isChecked = isChecked;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.relation_double_textview, this);

		layout = (LinearLayout) findViewById(R.id.layout);
		contentTv = (TextView) findViewById(R.id.contentTv);
		numberTv = (TextView) findViewById(R.id.numberTv);
		deleteIv = (ImageView) findViewById(R.id.deleteIv);
		if (!TextUtils.isEmpty(content)) {
			contentTv.setText(content);
		}
		if (!TextUtils.isEmpty(num)) {
			numberTv.setText(num);
		}else{
			numberTv.setVisibility(View.GONE);
		}
		changeBackground(isChecked);

	}

	public int getNumber() {
		return Integer.parseInt( numberTv.getText().toString());
	}

	public void setNumber(String number) {
		numberTv.setText(number);
	}

	public String getContentText() {
		return contentTv.getText().toString().trim();
	}

	public void setContent(String content) {
		contentTv.setText(content);
	}
	
	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	/**
	 * 改变布局的背景
	 * @param isApproval是否赞同评价 
	 */
	public void changeBackground(boolean isChecked){
		if (isChecked) {
			contentTv.setTextColor(Color.WHITE);
			layout.setBackgroundResource(R.drawable.relation_evaluationtag_bg_press_shape);
		}else {
			layout.setBackgroundResource(R.drawable.relation_evaluationtag_bg_commen_shape);
		}
	}

}
