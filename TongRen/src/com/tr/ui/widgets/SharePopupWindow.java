package com.tr.ui.widgets;

import com.tr.R;
import com.tr.ui.widgets.OverflowReqPopupWindow.OnOverflowReqItemClickListener;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class SharePopupWindow extends PopupWindow {

	private Context mContext;
	private OnShareItemClickListener mItemClickListener;
	
	public SharePopupWindow(Context context){
		mContext = context;
		View root =  LayoutInflater.from(mContext)
				.inflate(R.layout.widget_popup_share, null);
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
			case R.id.conTv:
				mItemClickListener.con();
				break;
			case R.id.jterTv:
				mItemClickListener.jter();
				break;
			case R.id.estTv:
				mItemClickListener.est();
				break;
			case R.id.finTv:
				mItemClickListener.fin();
				break;
			case R.id.peTv:
				mItemClickListener.pe();
				break;
			case R.id.vcTv:
				mItemClickListener.vc();
				break;
			case R.id.smTv:
				mItemClickListener.sm();
				break;
			case R.id.emailTv:
				mItemClickListener.email();
				break;
			default:
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
	public void setOnItemClickListener(OnShareItemClickListener listener){
		mItemClickListener = listener;
	}
	
	public interface OnShareItemClickListener{
		public void con(); // 关系
		public void jter(); // 桐人
		public void fin(); // 金融
		public void est(); // 房产
		public void pe(); // PE
		public void vc(); // VC
		public void sm(); // 短信
		public void email(); // 邮件
	}
}
