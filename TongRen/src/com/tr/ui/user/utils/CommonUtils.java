package com.tr.ui.user.utils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tr.ui.widgets.EProgressDialog;

public class CommonUtils implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3817393754305767406L;
	
	private static EProgressDialog mProgressDialog;
	
	
    /**
     * 显示自定义土司
     * @param context
     * @param resource
     * @param text
     * @return
     */
    public static Toast showCustomToast(Context context,int resource,String text)
    {
    	Toast toast = Toast.makeText(context,text, Toast.LENGTH_LONG);
    	toast.setGravity(Gravity.CENTER, 0, 0);
    	LinearLayout toastView = (LinearLayout) toast.getView();
    	ImageView imageCodeProject = new ImageView(context);
    	imageCodeProject.setImageResource(resource);
    	toastView.addView(imageCodeProject, 0);
		return toast;
    }
    
    
    /**
     * 是否包含中文汉字或标点
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
        	return true;
        }
        return false;
    }

}
