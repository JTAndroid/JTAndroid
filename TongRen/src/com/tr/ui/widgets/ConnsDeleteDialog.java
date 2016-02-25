package com.tr.ui.widgets;

import com.tr.R;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 人脉对话框（删除）
 * @author leon
 *
 */
public class ConnsDeleteDialog extends Dialog {

	public ConnsDeleteDialog(Activity context,final OnClickListener listener) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_conns_delete_dialog);
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.width = context.getWindowManager().getDefaultDisplay().getWidth() - 50;
		findViewById(R.id.okTv).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.onClick(0);
					dismiss();
				}
			}
		});
		findViewById(R.id.cancelTv).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.onClick(1);
					dismiss();
				}
			}
		});
	}
	
	public interface OnClickListener{
		public void onClick(int which);
	}
}
