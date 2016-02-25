package com.tr.ui.people.contactsdetails.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.people.model.UserCommentList;
import com.tr.ui.people.model.UserCommentList.UserComment;

/**
 * 组织主页评价 赞同 自定义布局
 * 
 * @author zhongshan
 * @since 2015-01-13
 * 
 */
public class ContactsDoubleTextViewCombinLayout extends LinearLayout {
	public static final String TAG = "OrganizationBasicForm";

	private LinearLayout layout;
	// private TextView keyTv;
	private TextView numberTv;
	private Activity context;
	private UserComment userComment;
	private TextView contentTv;

	public ContactsDoubleTextViewCombinLayout(Context context) {
		super(context);
	}

	public ContactsDoubleTextViewCombinLayout(Activity context, UserCommentList.UserComment userComment) {
		super(context);
		this.context = context;
		this.userComment = userComment;
		
		Log.v("ADD", "内容---->"+userComment.toString());
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.relation_double_textview, this);

		layout = (LinearLayout) findViewById(R.id.layout);
		contentTv = (TextView) findViewById(R.id.contentTv);
		numberTv = (TextView) findViewById(R.id.numberTv);
		if (userComment!=null) {
			if (!TextUtils.isEmpty(userComment.userComment)) {
				contentTv.setText(userComment.userComment);
			}
		}
		
//		if(userComment.count != 0){
//			if (userComment.count>=1) {
//				numberTv.setText(userComment.count+"");
//			}
//		}
		if (userComment.evaluateStatus) {
			contentTv.setTextColor(Color.WHITE);
			layout.setBackgroundResource(R.drawable.relation_evaluationtag_bg_press_shape);
		}else {
			contentTv.setTextColor(Color.BLACK);
			layout.setBackgroundResource(R.drawable.relation_evaluationtag_bg_commen_shape);
		}
		

	}
	/**UserComment Object*/
	public UserCommentList.UserComment getUserComment() {
		return userComment;
	}

	public long getNumber() {
		return userComment.count;
	}

	public void setNumber(long number) {
		numberTv.setText(number+"");
	}

	public String getContentText() {
		return contentTv.getText().toString().trim();
	}

	public void setContent(String content) {
		contentTv.setText(content);
	}
	
	/**
	 * // true:此人已经赞同;false:此人未赞同
	 * @return
	 */
	public boolean isEvaluateStatus(){
		return userComment.evaluateStatus;
	}
	
	public void setEvaluateStatus(boolean evaluateStatus){
		userComment.evaluateStatus = evaluateStatus;
	}
	/**
	 * 改变布局的背景
	 * @param isApproval是否赞同评价 
	 */
	public void changeBackground(boolean isApproval){
		if (isApproval) {
			contentTv.setTextColor(Color.WHITE);
			layout.setBackgroundResource(R.drawable.relation_evaluationtag_bg_press_shape);
		}else {
			layout.setBackgroundResource(R.drawable.relation_evaluationtag_bg_commen_shape);
		}
	}

}
