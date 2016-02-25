package com.tr.ui.widgets;

import com.tr.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class OverflowAffPopupWindow extends PopupWindow{

	private Context mContext;
	private OnOverflowItemClickListener mItemClickListener;
	
	public OverflowAffPopupWindow(Context context){
		mContext = context;

		View root = LayoutInflater.from(mContext)
				.inflate(R.layout.widget_popup_overflow_aff, null);
		
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
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x77000000);
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
			case R.id.editTv:
				mItemClickListener.edit();
				break;
			case R.id.closeBReqTv:
				mItemClickListener.closeBReq();
				break;
			case R.id.agreeFinishProjectTv:
				mItemClickListener.agreeFinishProject();
				break;
			case R.id.applyFinishProjectTv:
				mItemClickListener.applyFinishProject();
				break;
			case R.id.startLaunchProjectTv:
				mItemClickListener.startLaunchProject();
				break;
			case R.id.agreeLauchProjectTv:
				mItemClickListener.agreeLaunchProject();
				break;
			case R.id.applyLaunchProjectTv:
				mItemClickListener.applyLaunchProject();
				break;
			case R.id.closeProjectTv:
				mItemClickListener.closeProject();
				break;
			case R.id.rejectFinishProjectTv:
				mItemClickListener.rejectFinishProject();
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
	public void setOnItemClickListener(OnOverflowItemClickListener listener){
		mItemClickListener = listener;
	}
	
	public interface OnOverflowItemClickListener{
		public void edit(); //修改
		public void closeBReq(); // 终止业务需求
		public void agreeFinishProject(); // 批准结项
		public void applyFinishProject(); // 申请结项
		public void startLaunchProject(); // 启动立项
		public void agreeLaunchProject(); // 批准立项
		public void applyLaunchProject(); // 申请立项
		public void closeProject(); // 终止项目
		public void rejectFinishProject(); // 驳回结项
	}
}
