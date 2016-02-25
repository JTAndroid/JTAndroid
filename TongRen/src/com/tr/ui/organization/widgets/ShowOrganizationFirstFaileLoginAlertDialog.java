package com.tr.ui.organization.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.cloudDisk.FileManagementActivity;
import com.utils.common.EUtil;

/**
 * 显示图片对话框
 */
public class ShowOrganizationFirstFaileLoginAlertDialog extends Dialog implements View.OnClickListener{

	private OnShowFaileDialogClickListener mListener;
	private Button okButton;
	private TextView faileTv;

	public ShowOrganizationFirstFaileLoginAlertDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_organization_first_faile_alert_dialog);
		okButton = (Button) findViewById(R.id.okButton);
		faileTv = (TextView) findViewById(R.id.faileTv);
		okButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if (v == okButton) {
			mListener.startLogin();
		}
	}
	
	
	/**
	 * 设置失败原因
	 * @param text
	 */
	public void setFaileText(String text)
	{
		faileTv.setText(text);
	}
	
	
	
	public interface OnShowFaileDialogClickListener {
		public void startLogin();
	}

	/**
	 * 设置监听函数
	 * 
	 * @param listener
	 */
	public void setOnShowFaileDialogClickListener(OnShowFaileDialogClickListener listener) {
		mListener = listener;
	}


}
