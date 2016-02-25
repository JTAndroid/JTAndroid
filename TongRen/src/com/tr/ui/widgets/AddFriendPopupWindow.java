package com.tr.ui.widgets;

import com.tr.R;
import com.tr.ui.widgets.OverflowAffPopupWindow.OnOverflowItemClickListener;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class AddFriendPopupWindow extends PopupWindow {

	private EditText messageEt;
	private Button addBtn;
	private Context mContext;
	private OnAddFriendListener mAddListener;
	
	public AddFriendPopupWindow(Context context){
		
		mContext = context;
		View container = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.widget_popup_overflow_aff, null);
		container.setOnClickListener(mClickListener);
		messageEt = (EditText) container.findViewById(R.id.messageEt);
		addBtn = (Button) container.findViewById(R.id.addBtn);
		addBtn.setOnClickListener(mClickListener);
		
		// 设置SelectPicPopupWindow的View
		setContentView(container);
		// 设置SelectPicPopupWindow弹出窗体的宽
		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x55000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		setBackgroundDrawable(dw);
	}
	
	private OnClickListener mClickListener =new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == addBtn){
				mAddListener.add(messageEt.getText().toString());
			}
			dismiss();
		}
	};
	
	/**
	 * 弹出
	 */
	public void show(View anchor,String url){
		
		super.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, 0,0);
	}
	
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setOnAddFriendListener(OnAddFriendListener listener){
		mAddListener = listener;
	}
	
	public interface OnAddFriendListener{
		public void add(String message);
	}
}
