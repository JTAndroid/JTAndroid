package com.tr.ui.widgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.tr.R;

public class ProjectModePopupWindow extends PopupWindow{

	private TextView mode1Tv; 
	private TextView mode2Tv; 
	private TextView mode3Tv; 
	private View container;
	private Context mContext; 
	private OnProjectModeItemClickListener mListener;
	
	public ProjectModePopupWindow(Context context) {
		super(context);
		mContext = context;
		container = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.widget_popup_project_mode, null);
		container.setOnClickListener(mClickListener);
		mode1Tv = (TextView) container.findViewById(R.id.mode1Tv);
		mode1Tv.setOnClickListener(mClickListener);
		mode2Tv = (TextView) container.findViewById(R.id.mode2Tv);
		mode2Tv.setOnClickListener(mClickListener);
		mode3Tv = (TextView) container.findViewById(R.id.mode3Tv);
		mode3Tv.setOnClickListener(mClickListener);
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
	
	public void showAsPullUp(View anchor,int selection){ // 公开
		if(selection == 0){
			// 黄底橘色
			mode1Tv.setTextColor(mContext.getResources().getColor(
					R.color.commen_text_color_4));
			mode1Tv.setBackgroundColor(mContext.getResources().getColor(
					R.color.commen_text_color_3));
			// 白底黑字
			mode2Tv.setTextColor(mContext.getResources().getColor(
					R.color.commen_text_color_1));
			mode2Tv.setBackgroundColor(mContext.getResources().getColor(
					R.color.commen_text_color_4));
			// 白底黑字
			mode3Tv.setTextColor(mContext.getResources().getColor(
					R.color.commen_text_color_1));
			mode3Tv.setBackgroundColor(mContext.getResources().getColor(
					R.color.commen_text_color_4));
			
		} 
		else if(selection ==1){ // 非公开
			
			// 黄底橘色
			mode2Tv.setTextColor(mContext.getResources().getColor(
					R.color.commen_text_color_4));
			mode2Tv.setBackgroundColor(mContext.getResources().getColor(
					R.color.commen_text_color_3));
			// 白底黑字
			mode1Tv.setTextColor(mContext.getResources().getColor(
					R.color.commen_text_color_1));
			mode1Tv.setBackgroundColor(mContext.getResources().getColor(
					R.color.commen_text_color_4));
			// 白底黑字
			mode3Tv.setTextColor(mContext.getResources().getColor(
					R.color.commen_text_color_1));
			mode3Tv.setBackgroundColor(mContext.getResources().getColor(
					R.color.commen_text_color_4));
		}
		else if(selection ==2){
			// 黄底橘色
			mode3Tv.setTextColor(mContext.getResources().getColor(
					R.color.commen_text_color_4));
			mode3Tv.setBackgroundColor(mContext.getResources().getColor(
					R.color.commen_text_color_3));
			// 白底黑字
			mode1Tv.setTextColor(mContext.getResources().getColor(
					R.color.commen_text_color_1));
			mode1Tv.setBackgroundColor(mContext.getResources().getColor(
					R.color.commen_text_color_4));
			// 白底黑字
			mode2Tv.setTextColor(mContext.getResources().getColor(
					R.color.commen_text_color_1));
			mode2Tv.setBackgroundColor(mContext.getResources().getColor(
					R.color.commen_text_color_4));
		}
		
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        container.measure(w, h);
        int height = container.getMeasuredHeight();
		
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		super.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0],
				location[1] - height);
    }
	
	private OnClickListener mClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == mode1Tv){
				mListener.onMode1();
			}
			else if(v == mode2Tv){ 
				mListener.onMode2();
			}
			else if(v == mode3Tv){
				mListener.onMode3();
			}
			dismiss();
		}
	};
	
	public void setOnProjectModeItemClickListener(
			OnProjectModeItemClickListener listener) {
		mListener = listener;
	}
	
	public interface OnProjectModeItemClickListener{
		public void onMode1();
		public void onMode2(); 
		public void onMode3();
	}

}
