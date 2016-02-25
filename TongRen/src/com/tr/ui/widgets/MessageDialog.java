package com.tr.ui.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.widgets.EditTextAlertDialog.OnEditDialogClickListener;

/**
 * 显示 "提示信息" 的dialog
 * @author thinkpad
 *
 */
public class MessageDialog extends Dialog {

	// 控件
	private TextView titleTv;
	private TextView cancelTv; // 取消
	private TextView okTv; // 确定
	private TextView contentTv; // 内容
	
	// 变量
	private Activity mContext;
	private OnDialogFinishListener mListener;
	private String mContent;
	
	public MessageDialog(Activity context,String content, OnDialogFinishListener listener) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message_dialog);
		mContext = context;
		mContent = content;
		mListener = listener;
		initControls();
	}
	
	public MessageDialog(Activity context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message_dialog);
		mContext = context;
		initControls();
	}
	
	private void initControls(){
		titleTv = (TextView) findViewById(R.id.titleTv);
		// 取消
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(mClickListener);
		// 确定
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(mClickListener);
		// 内容
		contentTv = (TextView) findViewById(R.id.contentTv);
		contentTv.setText(mContent);
	}
	
	/**	隐藏取消Button和线	*/
	public MessageDialog goneCancleButton(){
		findViewById(R.id.lineView).setVisibility(View.GONE);
		cancelTv.setVisibility(View.GONE);
		return this;
	}
	
	public void goneButton(){
		findViewById(R.id.line_view).setVisibility(View.GONE);
		findViewById(R.id.ll).setVisibility(View.GONE);
	}
	
	public void setTitle(String title){
		if(TextUtils.isEmpty("")){
			titleTv.setVisibility(View.GONE);
		}
		titleTv.setText(title);
	}
	
	public void setContent(CharSequence content){
		contentTv.setText(content);
	}
	
	public void setOkTv(String ok){
		okTv.setText(ok);
	}
	
	public void setCancelTv(String cancel){
		cancelTv.setText(cancel);
	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cancelTv: // 取消
				if (mListener != null) {
					mListener.onCancel(cancelTv.getText().toString());
				}
				MessageDialog.this.dismiss();
				break;
			case R.id.okTv:  // 完成
				if (mListener != null) {
					mListener.onFinish(okTv.getText().toString());
				}
				MessageDialog.this.dismiss();
				break;
			}
		}
	};
	
	public interface OnDialogFinishListener{
		public void onFinish(String content);
		public void onCancel(String content);
	}
	
	/**
	 * 设置监听函数
	 * 
	 * @param listener
	 */
	public void setOnDialogFinishListener(OnDialogFinishListener listener) {
		mListener = listener;
	}

}
