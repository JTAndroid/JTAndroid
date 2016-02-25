package com.tr.ui.widgets;

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
public class ShowOrganizationAndLoginPromptAlertDialog extends Dialog implements View.OnClickListener{

	private Button okButton;

	private OnShowPromptDialogClickListener mListener;

	private TextView ismailTv;

	public ShowOrganizationAndLoginPromptAlertDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_organization_login_prompt_alert_dialog);
		okButton = (Button) findViewById(R.id.okButton);
		ismailTv = (TextView) findViewById(R.id.ismailTv);
		okButton.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		if (v == okButton) {
			mListener.startLogin();
		}
	}
	
	
	/**
	 * 设置提示内容
	 * @param context
	 */
	public void setMailText(String context){
		ismailTv.setText(context);
	}
	
	
	public interface OnShowPromptDialogClickListener {
		public void startLogin();
	}

	/**
	 * 设置监听函数
	 * 
	 * @param listener
	 */
	public void setOnDialogClickListener(OnShowPromptDialogClickListener listener) {
		mListener = listener;
	}


}
