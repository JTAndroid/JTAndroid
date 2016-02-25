package com.tr.ui.organization.widgets;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.cloudDisk.FileManagementActivity;
import com.utils.common.EUtil;

/**
 * 新建目录对话框
 */
public class OrganizationInfoAlertDialog extends Dialog implements View.OnClickListener {

	private TextView tipTv;
	private EditText nameEt;
	private TextView cancelTv;
	private TextView okTv;
	private LinearLayout centerll;

	private Context mContext;
	private OnDialogClickListener mListener;

	public OrganizationInfoAlertDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.create_catalog_alert_dialog);
		mContext = context;
		initControls();
	}
	private void initControls() {
		tipTv = (TextView) findViewById(R.id.tipTv);
		nameEt = (EditText) findViewById(R.id.nameEt);
		centerll = (LinearLayout) findViewById(R.id.centerll);
		centerll.setVisibility(View.GONE);
		nameEt.setVisibility(View.GONE);
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(this);
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(this);
	}
	
	//设置title
	public void setTipTv(String context)
	{
		tipTv.setText(context);
	}
	
	@Override
	public void onClick(View v) {
		if (v == okTv) {
			mListener.okTv();
		} 
		if (v == cancelTv) {
			mListener.cancelTv();
		}
		
	}

	public interface OnDialogClickListener {
		public void okTv();
		public void cancelTv();
	}

	/**
	 * 设置监听函数
	 * 
	 * @param listener
	 */
	public void setOnDialogClickListener(OnDialogClickListener listener) {
		mListener = listener;
	}

}
