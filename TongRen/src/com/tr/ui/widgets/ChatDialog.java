package com.tr.ui.widgets;

import com.tr.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;

public class ChatDialog extends Dialog {

	private ImageView voiceIv;
	private AnimationDrawable mAnim;
	
	public ChatDialog(Context context) {
		super(context,R.style.dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true); 
		setContentView(R.layout.widget_chat_dialog);
		initControls();
	}
	
	
	private void initControls(){
		voiceIv = (ImageView) findViewById(R.id.voiceIv);
		mAnim = (AnimationDrawable) voiceIv.getBackground();
	}

	@Override
	public void show(){
		super.show();
		mAnim.start();
	}
	
	@Override
	public void dismiss(){
		super.dismiss();
		mAnim.stop();
	}
}
