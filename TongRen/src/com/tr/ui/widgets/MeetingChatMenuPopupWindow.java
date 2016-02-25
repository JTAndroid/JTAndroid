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

public class MeetingChatMenuPopupWindow extends PopupWindow {

	private Context mContext;
	private OnMeetingChatMenuItemClickListener mItemClickListener;
//	private TextView notesTv;
//	private TextView navigationTv;
//	private TextView noticeTv;
	public MeetingChatMenuPopupWindow(Context context){
		mContext = context;

		View root = LayoutInflater.from(mContext)
				.inflate(R.layout.widget_popup_meeting_chat_menu, null);
		
		root.setOnClickListener(mClickListener);
//		noticeTv = (TextView) root.findViewById(R.id.noticeTv);
//		navigationTv = (TextView) root.findViewById(R.id.navigationTv);
//		notesTv = (TextView) root.findViewById(R.id.notesTv);
		LinearLayout container = (LinearLayout) root.findViewById(R.id.containerLl);
		
		for (int i = 0; i < container.getChildCount(); i++) {
			container.getChildAt(i).setOnClickListener(mClickListener);
		}
		
		// 设置SelectPicPopupWindow的View
		setContentView(root);
		// 设置SelectPicPopupWindow弹出窗体的宽
		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		setFocusable(false);
		setAnimationStyle(R.style.PupwindowAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x44000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		setBackgroundDrawable(dw);
		setOutsideTouchable(false);
	}
	
	private OnClickListener mClickListener =new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mItemClickListener ==null){
				return;
			}
			switch(v.getId()){
			case R.id.noticeTv:
				mItemClickListener.noticeClick();
				break;
			case R.id.navigationTv:
				mItemClickListener.navigationClick();
				break;
			case R.id.notesTv:
				mItemClickListener.notesClick();
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
	public void setOnItemClickListener(OnMeetingChatMenuItemClickListener listener){
		mItemClickListener = listener;
	}
	
	public interface OnMeetingChatMenuItemClickListener{
		public void noticeClick();
		public void navigationClick();
		public void notesClick();
	}
}
