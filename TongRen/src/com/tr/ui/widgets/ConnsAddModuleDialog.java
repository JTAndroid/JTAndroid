package com.tr.ui.widgets;

import com.tr.R;
import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class ConnsAddModuleDialog extends Dialog {

	public ConnsAddModuleDialog(Activity context,final OnFinishListener listener) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_conns_add_module_dialog);
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.width = context.getWindowManager().getDefaultDisplay().getWidth() - 50;
		final EditText titleEt = (EditText) findViewById(R.id.titleEt);
		findViewById(R.id.okTv).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				if(listener != null){
					if(TextUtils.isEmpty(titleEt.getText().toString())){
						listener.onFinish("自定义模块");
					}
					else{
						listener.onFinish(titleEt.getText().toString());
					}
					dismiss();
				}
			}
		});
		findViewById(R.id.cancelTv).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
	public interface OnFinishListener{
		public void onFinish(String title);
	}

}
