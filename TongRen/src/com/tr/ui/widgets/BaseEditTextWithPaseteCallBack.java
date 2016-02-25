package com.tr.ui.widgets;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 加 粘贴方法时有回调方法的 edittext
 * @author gushi
 *
 */
public class BaseEditTextWithPaseteCallBack extends EditText {

	private static final int ID_PASTE = android.R.id.paste;
	private OnClipboardListener onClipboardListener;
	
	public BaseEditTextWithPaseteCallBack(Context context, AttributeSet attrs ) {
		super(context, attrs);
	}

	
	@SuppressLint("NewApi")
	@Override
	public boolean onTextContextMenuItem(int id) {
		if(id == ID_PASTE){
            ClipboardManager clip = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//            clip.setText("[color=#ff0000]APKBus==>[/color]"+clip.getText());
            String clipboardContent = clip.getText().toString();
            if(onClipboardListener != null & (clipboardContent.contains("http://") || clipboardContent.contains("https://"))){
            	onClipboardListener.onPaste(clipboardContent);
            	return false;
            }
            else {
            	return super.onTextContextMenuItem(id);
            }
        }
		
		return super.onTextContextMenuItem(id);
		
	}
	
	public void setOnClipboardListener(OnClipboardListener onClipboardListener) {
		this.onClipboardListener = onClipboardListener;
	}

	public interface OnClipboardListener{
		public void onPaste(String clipboardContent);
	}
	
}
