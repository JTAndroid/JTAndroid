package com.tr.ui.widgets;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tr.R;
import com.utils.common.EUtil;

/**
 * 添加职业标签对话框
 * 
 * @author zhongshan
 * 
 */
public class EditTextAlertDialog extends Dialog implements View.OnClickListener {

	private TextView tipTv;
	private EditText nameEt;
	private TextView contentTv;
	private TextView cancelTv;
	private TextView okTv;

	private Context mContext;
	private OnEditDialogClickListener mListener;
	private InputMethodManager imm;
	private boolean isWatcher = true;

	public EditTextAlertDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_inpute_alert_dialog);
		mContext = context;
		initControls();
		imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	public EditTextAlertDialog(Context context, boolean isWatcher){
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_inpute_alert_dialog);
		mContext = context;
		this.isWatcher = isWatcher;
		initControls();
		imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	private void initControls() {
		tipTv = (TextView) findViewById(R.id.tipTv);
		tipTv.setOnClickListener(this);
		nameEt = (EditText) findViewById(R.id.nameEt);
		if(isWatcher){
			nameEt.addTextChangedListener(new MaxLengthWatcher(mContext, 20, "字符数长度不能超过20", nameEt));
		}
		contentTv = (TextView) findViewById(R.id.contentTv);
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(this);
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (mListener == null) {
			return;
		}
		if (v == okTv) { // 确定
			if (nameEt.getText() != null && !TextUtils.isEmpty(nameEt.getText().toString().trim())) { // 目录名称不为空
				mListener.onClick(nameEt.getText().toString());
				dismiss();
			} else {
				EUtil.showToast(mContext, "输入不能为空");
			}
		} else if (v == cancelTv) { // 取消
			mListener.onClick(null);
			dismiss();
		}
	}
	
	public void setTitle(String title){
		tipTv.setText(title);
	}
	
	public void setInput(String input){
		nameEt.setText(input);
	}
	
	public void setHint(String hint){
		nameEt.setHint(hint);
	}
	
	public void setInputType(int type){
		nameEt.setInputType(type);
	}
	
	public void setOkTv(String ok){
		okTv.setText(ok);
	}

	public interface OnEditDialogClickListener {
		public void onClick(String evaluationValue);
	}

	/**
	 * 设置监听函数
	 * 
	 * @param listener
	 */
	public void setOnDialogClickListener(OnEditDialogClickListener listener) {
		mListener = listener;
	}

}
