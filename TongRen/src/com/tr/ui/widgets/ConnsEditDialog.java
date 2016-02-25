package com.tr.ui.widgets;

import com.tr.R;
import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class ConnsEditDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private TextView cancelTv; // 取消
	private TextView okTv; // 确定
	private EditText contentEt; // 内容
	
	// 变量
	private Activity mContext;
	private OnFinishListener mListener;
	private String mContent;
//	private int mRequestCode;
	private View mView;
	
	public ConnsEditDialog(Activity context, View view, String content, OnFinishListener listener) {
		super(context,R.style.ConnsDialogTheme);
		setContentView(R.layout.widget_conns_edit_dialog);
		mContext = context;
		mView = view;
		mContent = content;
		mListener = listener;
		initDialogStyle();
		initControls();
	}
	
	// 初始化对话框样式
	@SuppressWarnings("deprecation")
	private void initDialogStyle(){
		getWindow().setWindowAnimations(R.style.ConnsDialogAnim);
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.width = mContext.getWindowManager().getDefaultDisplay().getWidth();
		wmlp.gravity = Gravity.BOTTOM ;
	}
	
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
				ConnsEditDialog.this.dismiss();
				break;
			case R.id.okTv:  // 完成
				mContent = contentEt.getText().toString();
				if (mListener != null) {
					mListener.onFinish(mView, mContent);
				}
				ConnsEditDialog.this.dismiss();
				break;
			}
		}
	};
	
	
	public interface OnFinishListener{
		public void onFinish(View view, String content);
	}

}
