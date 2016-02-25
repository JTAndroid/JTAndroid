package com.tr.ui.widgets;

import com.tr.R;
import com.tr.ui.widgets.ProgressPopupWindow.ProgressAdapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @ClassName:     VisibleRangePopupWindow.java
 * @Description:   可见范围选择弹出框
 * @Author         leon
 * @Version        v 1.0  
 * @Create         2014-04-04 
 * @Update         2014-04-15
 */
public class VisibleRangePopupWindow extends PopupWindow {
	
	private TextView publicTv; // 公开
	private TextView privateTv; // 非公开
	private Context mContext; 
	private OnVisibleRangeItemClickListener mListener;
	
	public VisibleRangePopupWindow(Context context) {
		super(context);
		mContext = context;
		View container = LayoutInflater.from(context).inflate(
				R.layout.widget_popup_visible_range, null);
		container.setOnClickListener(mClickListener);
		publicTv = (TextView) container.findViewById(R.id.publicTv);
		publicTv.setOnClickListener(mClickListener);
		privateTv = (TextView) container.findViewById(R.id.privateTv);
		privateTv.setOnClickListener(mClickListener);
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
	
	// 下方弹出（公开-0， 非公开-1）
	public void showAsDropDown(View anchor,int selection){ // 公开
		if(selection == 0){
			// 黄底橘色
			publicTv.setTextColor(mContext.getResources().getColor(R.color.commen_text_color_4));
			publicTv.setBackgroundColor(mContext.getResources().getColor(R.color.commen_text_color_3));
			// 白底黑字
			privateTv.setTextColor(mContext.getResources().getColor(R.color.commen_text_color_1));
			privateTv.setBackgroundColor(mContext.getResources().getColor(R.color.commen_text_color_4));
		} 
		else{ // 非公开
			
			// 黄底橘色
			privateTv.setTextColor(mContext.getResources().getColor(
					R.color.commen_text_color_4));
			privateTv.setBackgroundColor(mContext.getResources().getColor(
					R.color.commen_text_color_3));
			// 白底黑字
			publicTv.setTextColor(mContext.getResources().getColor(
					R.color.commen_text_color_1));
			publicTv.setBackgroundColor(mContext.getResources().getColor(
					R.color.commen_text_color_4));
		}
		super.showAsDropDown(anchor);
    }
	
	private OnClickListener mClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == publicTv){ // 公开
				mListener.onPublic();
				dismiss();
			}
			else if(v == privateTv){ // 非公开
				mListener.onPrivate();
				dismiss();
			}
			else{
				dismiss();
			}
		}
		
	};
	
	/***
	 * 设置点击监听器
	 * @param listener
	 */
	public void setOnVisibleRangeItemClickListener(
			OnVisibleRangeItemClickListener listener) {
		mListener = listener;
	}
	
	public interface OnVisibleRangeItemClickListener{
		public void onPublic(); // 公开
		public void onPrivate(); // 非公开
	}

}
