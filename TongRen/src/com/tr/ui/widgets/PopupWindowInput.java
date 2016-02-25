package com.tr.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import com.tr.R;
import com.tr.model.obj.EduExperience;
import com.utils.common.EUtil;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopupWindowInput extends PopupWindow {

	private final String TAG = getClass().getSimpleName();
	public final static int userType_showtitle=1;
	
	private View container;
	private Context mContext;
	public int type=0;
	public int useType=0;
	View okView=null;
	View cancelView=null;
	EditText edit=null;
	
	public PopupWindowInput(Context context) {
		super(context);
		mContext = context;
		container = LayoutInflater.from(context).inflate(
				R.layout.popupwindowinput, null);
		okView=container.findViewById(R.id.ok);
		cancelView=container.findViewById(R.id.cancel);
		edit=(EditText)container.findViewById(R.id.edit0);
		
		// 设置SelectPicPopupWindow的View
		setContentView(container);
		// 设置SelectPicPopupWindow弹出窗体的宽
		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		setBackgroundDrawable(dw);
    }
	
	
	public void setOkListener(OnClickListener on){
		okView.setOnClickListener(on);
		okView.setTag(edit);
	}
	public void setCancelListener(OnClickListener on){
		cancelView.setOnClickListener(on);
	}
	
	public void showAsPullUp(View anchor){
				
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		//anchorTv = anchor;
		        
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        container.measure(w, h);
        int height =container.getMeasuredHeight();
        
		super.showAtLocation(anchor, Gravity.TOP, location[0],
				location[1] - height);
    }

}
