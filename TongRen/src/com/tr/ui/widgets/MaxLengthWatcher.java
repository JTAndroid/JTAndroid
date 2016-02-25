package com.tr.ui.widgets;

import java.io.UnsupportedEncodingException;

import com.utils.string.StringUtils;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 监听输入内容是否超出最大长度，并设置光标位置 
 * @author gushi
 *
 */
public class MaxLengthWatcher implements TextWatcher {
	String TAG = "MaxLengthWatcher";
	
	private Context context;
	private int maxLen = 0;  
    private EditText editText = null; 
    private String showText;
    private String lastString;
    private int selEndIndex;
	
	
    public MaxLengthWatcher(Context context , int maxLen, String showText, EditText editText) {  
    	this.context = context;
        this.maxLen = maxLen;  
        this.editText = editText;  
        this.showText = showText;
    }

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		String MSG = "beforeTextChanged()";
		lastString = s.toString();
		Editable editable = editText.getText();  
		selEndIndex = Selection.getSelectionEnd(editable); 
		
		Log.i(TAG, MSG + " s.toString() = " + s.toString());
//		Log.i(TAG, MSG + " selEndIndex= " + selEndIndex);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String MSG = "onTextChanged()";
		
		Editable editable = editText.getText();  
//        int len = editable.length();  
		String  currentStr = editText.getText().toString();
		int gbkLen = StringUtils.String_length(currentStr);
          
        if(gbkLen > maxLen)  
        {  
//            int selEndIndex = Selection.getSelectionEnd(editable);  
//            String str = editable.toString();  
            //截取新字符串  
//            String newStr = str.substring(0,maxLen); 
//            String newStr = null;
//            editText.setText(newStr);  
        	editText.setText(lastString);
            editable = editText.getText();  
              
            //新字符串的长度  
            int newLen = editable.length();  
            //旧光标位置超过字符串长度  
            if(selEndIndex > newLen)  
            {  
                selEndIndex = editable.length();  
            }  
            
            //设置新光标所在的位置  
            Selection.setSelection(editable, selEndIndex); 
            
//            如果不是  上次字符为空, 这次字包含网址
//            if(!(StringUtils.isEmpty(lastString) & (currentStr.contains("http://") || currentStr.contains("https://")))){
            	
            	// 显示提示文字
            	if(showText !=  null ){
            		Toast.makeText(context , showText, 0).show();
            	}
//            }
        }
		
		
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

}
