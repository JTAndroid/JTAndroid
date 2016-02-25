package com.tr.ui.demand.popu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.tr.R;

public class MyPopupWindow extends PopupWindow {

	private Context mContext;
	//private OnMyHomeMenuItemClickListener mItemClickListener;
	
	public MyPopupWindow(Context context,View view){
		mContext = context;

		/*View root = LayoutInflater.from(mContext)
				.inflate(R.layout.widget_popup_relation_my_menu, null);
		*/
		//root.setOnClickListener(mClickListener);
		
		//LinearLayout container = (LinearLayout) root.findViewById(R.id.containerLl);
		
		/*for (int i = 0; i < container.getChildCount(); i++) {
			container.getChildAt(i).setOnClickListener(mClickListener);
		}*/
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				
			}
		});
		// 设置SelectPicPopupWindow的View
		setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		setFocusable(true);
		setAnimationStyle(R.style.PupwindowAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x44000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		setBackgroundDrawable(dw);
	}
	/*
	private OnClickListener mClickListener =new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mItemClickListener ==null){
				return;
			}
			switch(v.getId()){
			case R.id.editTv:
				mItemClickListener.edit();
				break;
			case R.id.jointTv:
				mItemClickListener.joint();
				break;
			}
			dismiss();
		}
	};
	*/
	/**
	 * 弹出
	 */
	public void showAsDropDown(View anchor){
		
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		super.showAsDropDown(anchor);
	}
	/*
	*//**
	 * 设置监听器
	 * @param listener
	 *//*
	public void setOnItemClickListener(OnMyHomeMenuItemClickListener listener){
		mItemClickListener = listener;
	}
	
	public interface OnMyHomeMenuItemClickListener{
		public void edit(); //修改
		public void joint(); // 刷新
	}*/
}
