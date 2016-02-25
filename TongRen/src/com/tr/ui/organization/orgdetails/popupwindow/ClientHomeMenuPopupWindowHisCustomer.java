package com.tr.ui.organization.orgdetails.popupwindow;

import com.tr.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class ClientHomeMenuPopupWindowHisCustomer extends PopupWindow{
	private Context mContext;
	private ClientHomeMenuItemClickListenerHisCustomer mItemClickListener;
	
	public ClientHomeMenuPopupWindowHisCustomer(Context context){
		mContext = context;

		View root = LayoutInflater.from(mContext)
				.inflate(R.layout.client_other_widget_creater_dialog, null);
		
		root.setOnClickListener(mClickListener);
		
		LinearLayout container = (LinearLayout) root.findViewById(R.id.containerLl);
		
		for (int i = 0; i < container.getChildCount(); i++) {
			container.getChildAt(i).setOnClickListener(mClickListener);
		}
		
		// 设置SelectPicPopupWindow的View
		setContentView(root);
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
	
	private OnClickListener mClickListener =new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mItemClickListener ==null){
				return;
			}
			switch(v.getId()){
			
			case R.id.client_save://保存
				
//				mItemClickListener.scaveCustomer();
				
				break;
			case R.id.client_report_big_happy://对接
				mItemClickListener.joint();
				break;
			case R.id.client_findbody_big_happy://查看发布人
//				mItemClickListener.deletFriend();
				break;
			
			}
			dismiss();
		}
	};
	
	/**
	 * 弹出
	 */
	public void showAsDropDown(View anchor){
		
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		super.showAsDropDown(anchor);
	}
	
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setOnItemClickListener(ClientHomeMenuItemClickListenerHisCustomer listener){
		mItemClickListener = listener;
	}
	
	public interface ClientHomeMenuItemClickListenerHisCustomer{
		public void scaveCustomer();
		public void deletFriend();//刷新
		public void joint(); //修改
	}
}
