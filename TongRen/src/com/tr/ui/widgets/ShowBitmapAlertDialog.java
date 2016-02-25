package com.tr.ui.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.cloudDisk.FileManagementActivity;
import com.utils.common.EUtil;

/**
 * 显示图片对话框
 */
public class ShowBitmapAlertDialog extends Dialog {

	private ImageView showImageView;

	private Context mContext;
	private OnEditDialogClickListener mListener;

	public ShowBitmapAlertDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_bitmap_alert_dialog);
		mContext = context;
		showImageView = (ImageView) findViewById(R.id.showbitmap);
		
	}
	

	
	
	@Override
	public boolean isShowing() {
		return super.isShowing();
	}

	


	@Override
	public void show() {
		if (mListener!=null) {
			mListener.onshow(showImageView);
		}
		super.show();
	}




	public interface OnEditDialogClickListener {
		public void onshow(ImageView showImageView);
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
