package com.tr.ui.widgets;

import com.tr.R;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class RelationOrtherHomeMenuPopupWindow extends PopupWindow {

	private Context mContext;
	private OnOrtherHomeMenuItemClickListener mItemClickListener;
	private TextView reportTv;
	
	public RelationOrtherHomeMenuPopupWindow(Context context){
		mContext = context;

		View root = LayoutInflater.from(mContext)
				.inflate(R.layout.widget_popup_relation_other_menu, null);
		
		root.setOnClickListener(mClickListener);
		addFriendTv = (TextView) root.findViewById(R.id.addFriendTv);
		LinearLayout container = (LinearLayout) root.findViewById(R.id.containerLl);
		
		reportTv = (TextView) root.findViewById(R.id.reportTv);
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
			case R.id.addFriendTv:
				mItemClickListener.addFriend();
				break;
			case R.id.blackNumTv:
				mItemClickListener.blackNum();
				break;
			case R.id.reportTv:
				mItemClickListener.ToReport();
				break;
			}
			dismiss();
		}
	};
	private TextView addFriendTv;
	
	public void hintAddFriendTv(){
		addFriendTv.setVisibility(View.GONE);
	}
	
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
	public void setOnItemClickListener(OnOrtherHomeMenuItemClickListener listener){
		mItemClickListener = listener;
	}
	
	public interface OnOrtherHomeMenuItemClickListener{
		public void addFriend(); // 刷新
		public void ToReport(); //举报
		public void blackNum(); // 刷新
	}
}
