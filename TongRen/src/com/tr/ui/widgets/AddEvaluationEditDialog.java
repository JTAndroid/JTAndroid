package com.tr.ui.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tr.R;

public class AddEvaluationEditDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private TextView cancelTv; // 取消
	private TextView okTv; // 确定
	private EditText contentEt; // 内容
	
	// 变量
	private Activity mContext;
	private OnDialogFinishListener mListener;
	private String mContent;
//	private int mRequestCode;
	private View mView;
	
	public AddEvaluationEditDialog(Activity context, View view, String content, OnDialogFinishListener listener) {
//		super(context,R.style.ConnsDialogTheme);
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_evaluation_edit_dialog);
		mContext = context;
		mView = view;
		mContent = content;
		mListener = listener;
//		initDialogStyle();
		initControls();
	}
//	// 初始化对话框样式
//	@SuppressWarnings("deprecation")
//	private void initDialogStyle(){
//		getWindow().setWindowAnimations(R.style.ConnsDialogAnim);
//		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
//		wmlp.width = mContext.getWindowManager().getDefaultDisplay().getWidth();
//		wmlp.gravity = Gravity.BOTTOM ;
//	}
	
	// 初始化控件
	private void initControls(){
		// 取消
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(mClickListener);
		// 确定
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(mClickListener);
		// 内容
		contentEt = (EditText) findViewById(R.id.contentEt);
		contentEt.setText(mContent);
	}
	
	public void setHint(String hintText){
		contentEt.setHint(hintText);
	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cancelTv: // 取消
				InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(
							 v.getApplicationWindowToken(), 0);
				}
				AddEvaluationEditDialog.this.dismiss();
				break;
			case R.id.okTv:  // 完成
				InputMethodManager immm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (immm.isActive()) {
					immm.hideSoftInputFromWindow(
							 v.getApplicationWindowToken(), 0);
				}
				mContent = contentEt.getText().toString();
				if (mListener != null) {
					mListener.onFinish(mView, mContent);
				}
				if (mContent != null && TextUtils.isEmpty(mContent.trim())) {
					return;
				}
				else {
					AddEvaluationEditDialog.this.dismiss();
				}
				break;
			}
		}
	};

	public interface OnDialogFinishListener{
		public void onFinish(View view, String content);
	}

}
